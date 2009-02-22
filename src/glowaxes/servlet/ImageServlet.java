/**
 *
 * Copyright 2008-2009 Elements. All Rights Reserved.
 *
 * License version: CPAL 1.0
 *
 * The Original Code is glowaxes.org code. Please visit glowaxes.org to see how
 * you can contribute and improve this software.
 *
 * The contents of this file are licensed under the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *    http://glowaxes.org/license.
 *
 * The License is based on the Mozilla Public License Version 1.1.
 *
 * Sections 14 and 15 have been added to cover use of software over a computer
 * network and provide for attribution determined by Elements.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 *
 * Elements is the Initial Developer and the Original Developer of the Original
 * Code.
 *
 * The contents of this file may be used under the terms of the Elements 
 * End-User License Agreement (the Elements License), in which case the 
 * provisions of the Elements License are applicable instead of those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */

package glowaxes.servlet;

import glowaxes.util.AutoRefreshMap;
import glowaxes.util.ChartRegistry;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * The Class ImageServlet.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class ImageServlet extends HttpServlet {

    /** The logger. */
    private static Logger logger =
            Logger.getLogger(ImageServlet.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Gets the url pattern.
     * 
     * @param context the context
     * 
     * @return the url pattern
     * 
     * @throws JDOMException the JDOM exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("unchecked")
    public static String getUrlPattern(ServletContext context)
            throws JDOMException, IOException {

        String webConfig = "/WEB-INF/web.xml";
        InputStream input = context.getResourceAsStream(webConfig);
        if (input == null)
            throw new RuntimeException("web.xml file cannot be found");

        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(input);
        Element root = doc.getRootElement();

        List children = root.getChildren("servlet-mapping");
        Iterator i = children.iterator();
        while (i.hasNext()) {
            Element e = ((Element) i.next());
            logger.error(e.getName());
            String name = e.getChildText("servlet-name");
            if (name.equals("ImageServlet"))
                return e.getChildText("url-pattern").replace("*", "").replace(
                        "/", "");
        }

        return "image-out";
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        // Get file name from request.
        String file = request.getPathInfo().substring(1);

        if (file == null)
            throw new RuntimeException("id parameter is not defined (null)");

        // Get content type by filename.
        String contentType = URLConnection.guessContentTypeFromName(file);

        // Check if file is actually an image (avoid download of other files by
        // hackers!).
        // For all content types, see:
        // http://www.w3schools.com/media/media_mimeref.asp
        if (contentType == null || !contentType.startsWith("image")) {
            return;
        }

        // Prepare streams.
        InputStream input = null;
        OutputStream output = null;
        try {

            long ms = System.currentTimeMillis();

            while (ChartRegistry.SINGLETON.getChart(file) == null) {
                Thread.sleep(10);
            }
            logger.info("Image " + file + " registered in ["
                    + (System.currentTimeMillis() - ms) + "]");
            long t0 = System.currentTimeMillis();
            while (ChartRegistry.SINGLETON.getChart(file).length == 0
                    && System.currentTimeMillis() - t0 < 10 * 1000) {
                Thread.sleep(100);

            }
            if (ChartRegistry.SINGLETON.getChart(file).length == 0) {
                throw new RuntimeException("Image did not get generated");
            }
            logger.info("Image " + file + " produced in ["
                    + (System.currentTimeMillis() - t0) + "]");

            // Open image file.
            input =
                    new ByteArrayInputStream(ChartRegistry.SINGLETON
                            .getChart(file));
            int contentLength = input.available();

            // Init servlet response.
            response.reset();
            response.setContentLength(contentLength);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", "inline; filename=\""
                    + file + "\"");
            output = new BufferedOutputStream(response.getOutputStream());

            // Write file contents to response.
            while (contentLength-- > 0) {
                output.write(input.read());
            }

            AutoRefreshMap.getAutoRefreshMap("chartRegistry").printLog();

            // Finalize task.
            output.flush();
        } catch (IOException e) {
            // Something went wrong?
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // Gently close streams.
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // This is a serious error. Do more than just printing a
                    // trace.
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // This is a serious error. Do more than just printing a
                    // trace.
                }
            }
        }
    }
}
