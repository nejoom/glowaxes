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
 * Based on commercial needs the contents of this file may be used under the
 * terms of the Elements End-User License Agreement (the Elements License), in
 * which case the provisions of the Elements License are applicable instead of
 * those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */
package glowaxes.glyphs;

import glowaxes.servlet.ImageServlet;
import glowaxes.util.AreaMap;
import glowaxes.util.ChartOutputter;
import glowaxes.util.ChartRegistry;
import glowaxes.util.Configuration;
import glowaxes.util.CreateImage;
import glowaxes.util.JSPHelper;
import glowaxes.util.TypeConverter;
import glowaxes.util.XOutputter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;

/**
 * The Class Rasterizer.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Rasterizer {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Rasterizer.class.getName());

    /** The file out. */
    private String fileOut;

    /** The height. */
    private double height = 100;

    /** The id. */
    private String id;

    /** The output. */
    private String output = "png";

    /** The page context. */
    private PageContext pageContext;

    /** The src. */
    private String src;

    /** The text. */
    private String text;

    /** The width. */
    private double width = 100;

    /**
     * Instantiates a new chart.
     */
    @SuppressWarnings("unchecked")
    public Rasterizer() {
        setId(ChartRegistry.SINGLETON.getHex());

    }

    /**
     * Instantiates a new chart.
     * 
     * @param text
     *            the text
     */
    @SuppressWarnings("unchecked")
    public Rasterizer(String text) {
        setId(ChartRegistry.SINGLETON.getHex());
        this.text = text;

    }

    /**
     * Gets the expire time.
     * 
     * @return the expire time
     */
    private long getExpireTime() {
        long expireTime =
                System.currentTimeMillis()
                        + TypeConverter.getLong(Configuration.getInstance()
                                .getValue("glowaxes", "Renderer.expiretime"),
                                5 * 60 * 1000);
        return expireTime;
    }

    /**
     * Gets the file out.
     * 
     * @return the file out
     */
    public String getFileOut() {
        return fileOut;
    }

    /**
     * Gets the glyph height.
     * 
     * @return the glyph height
     */
    private double getGlyphHeight() {
        return height;
    }

    /**
     * Gets the glyph width.
     * 
     * @return the glyph width
     */
    private double getGlyphWidth() {
        return width;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the output.
     * 
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * Gets the page context.
     * 
     * @return the page context
     */
    public PageContext getPageContext() {
        return pageContext;
    }

    /**
     * Gets the result.
     * 
     * @return the result
     */
    public String getResult() {

        HttpServletRequest request =
                (HttpServletRequest) this.getPageContext().getRequest();
        HttpServletResponse response =
                (HttpServletResponse) this.getPageContext().getResponse();

        Format formatter = Format.getRawFormat();
        formatter.setEncoding("US-ASCII");
        formatter.setIndent("  ");
        formatter.setLineSeparator(System.getProperty("line.separator"));

        ChartOutputter serializer = new ChartOutputter(formatter);

        Document doc = getSVG();
        String xml = "";

        try {
            StringWriter sw = new StringWriter();
            serializer.output(doc, sw);
            String _xml = sw.toString();
            logger.error(_xml);
        } catch (Exception e) {
            logger.fatal(e);
        }

        if (getOutput().equals("svg")) {

            try {
                StringWriter sw = new StringWriter();
                serializer.output(doc, sw);
                xml = sw.toString();
            } catch (Exception e) {
                logger.fatal(e);
            }
            response.setContentType("image/svg+xml;charset=UTF-8");
            JSPHelper.setNoCache(request, response);

        } else if (getOutput().equals("png") || getOutput().equals("jpg")
                || getOutput().equals("tiff") || getOutput().equals("pdf")) {

            XOutputter outputter = new XOutputter();

            outputter.setForceNamespaceAware(true);

            org.w3c.dom.Document document = null;

            // the root of the webapp
            String contextPath = request.getContextPath();

            // the directory that triggers the ImageServlet to work
            String directoryOut = "error";
            try {
                document = outputter.output(doc);
                directoryOut =
                        ImageServlet.getUrlPattern(getPageContext()
                                .getServletContext());
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            logger.error("directoryOut: " + directoryOut);

            fileOut = getId() + "." + getOutput();

            // String webPathOut =
            // chart.getPageContext().getServletContext().getRealPath(
            // "temp");
            //
            // String webFileOut = "";

            long expiretime = getExpireTime();

            // start thread that produces image
            new CreateImage(document, fileOut, expiretime, null, null);

            // logger.error(xml);
            xml +=
                    AreaMap.getImageTag(contextPath + "/" + directoryOut + "/"
                            + fileOut, getId(), getTitle(), getGlyphWidth(),
                            getGlyphHeight())
                            + System.getProperty("line.separator");

        }

        return xml;
    }

    /**
     * Gets the SVG.
     * 
     * @return the SVG
     */
    public Document getSVG() {
        Document doc;
        if (text != null) {

            ByteArrayInputStream bas =
                    new ByteArrayInputStream(text.getBytes());

            SAXBuilder saxBuilder = new SAXBuilder();

            try {
                logger.info("Loading xml string.... ");

                doc = saxBuilder.build(bas);

            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            } catch (org.jdom.JDOMException e) {
                throw new RuntimeException(e);
            }
        } else {
            doc = loadUri(src);
        }

        logger.info("Returning doc");
        return doc;
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    private String getTitle() {
        return "";
    }

    /**
     * Load uri.
     * 
     * @param src
     *            the src
     * 
     * @return the document
     */
    private Document loadUri(String src) {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc;
        try {
            logger.info("Loading xml file.... " + src);

            doc = saxBuilder.build(src);

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (org.jdom.JDOMException e) {
            throw new RuntimeException(e);
        }

        logger.info("Loaded xml file.... " + src);
        return doc;
    }

    /**
     * Sets the expire time.
     * 
     * @param expireTime
     *            the new expire time
     */
    public void setExpireTime(long expireTime) {
    }

    /**
     * Sets the glyph height.
     * 
     * @param height
     *            the new glyph height
     */
    public void setGlyphHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the glyph width.
     * 
     * @param width
     *            the new glyph width
     */
    public void setGlyphWidth(double width) {
        this.width = width;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the output.
     * 
     * @param output
     *            the new output
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Sets the page context.
     * 
     * @param pageContext
     *            the page context
     */
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    /**
     * Gets the result.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * 
     * @return the result
     */
    public void setResult(HttpServletRequest request,
            HttpServletResponse response) {

        fileOut = getId() + "." + getOutput();

        // assume cached
        if (ChartRegistry.SINGLETON.getChart(fileOut) != null)
            return;

        Format formatter = Format.getRawFormat();
        formatter.setEncoding("US-ASCII");
        formatter.setIndent("  ");
        formatter.setLineSeparator(System.getProperty("line.separator"));

        ChartOutputter serializer = new ChartOutputter(formatter);

        Document doc = getSVG();
        try {
            StringWriter sw = new StringWriter();
            serializer.output(doc, sw);
            String _xml = sw.toString();
            logger.error(_xml);
        } catch (Exception e) {
            logger.fatal(e);
        }

        if (getOutput().equals("svg")) {

            try {
                StringWriter sw = new StringWriter();
                serializer.output(doc, sw);
            } catch (Exception e) {
                logger.fatal(e);
            }
            response.setContentType("image/svg+xml;charset=UTF-8");
            JSPHelper.setNoCache(request, response);

        } else if (getOutput().equals("png") || getOutput().equals("jpg")
                || getOutput().equals("tiff") || getOutput().equals("pdf")) {

            XOutputter outputter = new XOutputter();

            outputter.setForceNamespaceAware(true);

            org.w3c.dom.Document document = null;

            try {
                document = outputter.output(doc);
            } catch (JDOMException e) {
                e.printStackTrace();
            }

            long expiretime = getExpireTime();

            // start thread that produces image
            new CreateImage(document, fileOut, expiretime, null, null);

        }

    }

    /**
     * Sets the source.
     * 
     * @param src
     *            the new source
     */
    public void setSource(String src) {
        setId(ChartRegistry.SINGLETON.getHex(src));
        this.src = src;
    }
}
