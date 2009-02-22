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
package glowaxes.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class JSPHelper.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class JSPHelper {

    /**
     * Flush buffer.
     * 
     * @param out the out
     */
    public static void flushBuffer(JspWriter out) {
        String bufferFlusher = " ";
        for (int i = 0; i < 8; i++) {

            bufferFlusher = bufferFlusher.concat(bufferFlusher);

        }
        try {
            out.println(bufferFlusher);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * Gets the cookie.
     * 
     * @param request the request
     * @param cookieKey the cookie key
     * 
     * @return the cookie
     * 
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String getCookie(HttpServletRequest request, String cookieKey)
            throws UnsupportedEncodingException {

        Cookie[] cookies = request.getCookies();

        // try to get the cookieKey
        if (cookies != null) {

            for (int i = 0; i < cookies.length; i++) {

                Cookie c = cookies[i];

                if (c.getName().equals(cookieKey)) {

                    return URLDecoder.decode(c.getValue(), "UTF-8");
                }
            }

            return null;

        } else {

            return null;

        }

    }

    /**
     * Gets the created cookie.
     * 
     * @param request the request
     * @param response the response
     * @param cookieKey the cookie key
     * @param defaultValue the default value
     * @param defaultSeconds the default seconds
     * 
     * @return the created cookie
     * 
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String getCreatedCookie(HttpServletRequest request,
            HttpServletResponse response, String cookieKey,
            String defaultValue, int defaultSeconds)
            throws UnsupportedEncodingException {

        Cookie[] cookies = request.getCookies();

        // String cookieValue;

        // try to get the cookieKey
        if (cookies != null) {

            for (int i = 0; i < cookies.length; i++) {

                Cookie c = cookies[i];

                if (c.getName().equals(cookieKey)) {
                    return URLDecoder.decode(c.getValue(), "UTF-8");
                }

            }

        }

        // Create a client cookie
        Cookie cookie = new Cookie(cookieKey, defaultValue);

        // default expirey time
        cookie.setMaxAge(defaultSeconds);

        // added path (internet sites say this must be done)
        // 25-05-2003
        cookie.setPath("/");

        response.addCookie(cookie);

        return defaultValue;

    }

    /**
     * Gets the current url.
     * 
     * @param path the path
     * @param query the query
     * 
     * @return the current url
     */
    private static String getCurrentUrl(String path, String query) {
        String fullUrl = "";
        if (query.length() == 0)
            fullUrl = path;
        else
            fullUrl = path.concat("?").concat(query);

        return fullUrl;
    }

    /**
     * Gets the modified url.
     * 
     * @param request the request
     * @param key the key
     * @param value the value
     * 
     * @return the modified url
     */
    public static String getModifiedUrl(HttpServletRequest request, String key,
            String value) {

        if (key == null || value == null)
            return null;

        String path = request.getServletPath().trim();
        String query = request.getQueryString();

        if (query == null)
            query = "";
        else
            query = query.trim();

        if (key.length() == 0)
            return path;

        if (query.indexOf(key.concat("=")) == -1) {

            if (query.length() == 0) {

                query = query.concat(key).concat("=").concat(value);

            } else {

                query = query.concat("&").concat(key).concat("=").concat(value);

            }

        } else {

            String startQuery =
                    query.substring(0, query.indexOf(key.concat("=")));

            String endQuery =
                    query.substring(query.indexOf(key.concat("=")), query
                            .length());

            boolean hasAmpersand = (endQuery.indexOf("&") != -1);

            if (hasAmpersand) {

                endQuery =
                        endQuery.substring(endQuery.indexOf("&") + 1, endQuery
                                .length());

                query =
                        startQuery.concat(endQuery).concat("&").concat(key)
                                .concat("=").concat(value);

            } else {

                endQuery = "";

                query =
                        startQuery.concat(endQuery).concat(key).concat("=")
                                .concat(value);

            }

        }

        if (query.indexOf("&") == 0)
            query = query.substring(1, query.length());

        return getCurrentUrl(path, query);
    }

    /**
     * Set 'No cache' for the Servlet/JSP response.
     * <p>
     * A correct working of Servlets and JSPs is only guaranteed if no caching
     * takes place, eighther at the client side or at some place between the
     * client and the server. According to the specifications browsers and proxy
     * servers should never cache JSPs and Servlets, practise however shows this
     * is not always the case. For this reason you <b>should</b> always use
     * this method for disabling caching.
     * <p>
     * <b>Note:</b> no mention has been made in the Servlet specification about
     * the moment header information for a response has to be set. However it is
     * always safe to set the Header information first before writing any output
     * in the response.
     * 
     * @param aRequest the HTTP request from the client
     * @param aResponse the HTTP response for the client
     */
    public static void setNoCache(HttpServletRequest aRequest,
            HttpServletResponse aResponse) {

        // set the response header to disable caching at the client side or any
        // proxy server between the client and the server. The actual header
        // depends on the HTTP protocol.
        //
        // the protocol is trimmed for whitespace for some buggy servlet engines
        // may return a CRLF at the end (Tomcat 3.2.0 e.g.)
        String protocol = aRequest.getProtocol().trim();

        if (protocol.equals("HTTP/1.0")) {
            aResponse.setHeader("Pragma", "no-cache");
        } else if (protocol.equals("HTTP/1.1")) {
            aResponse.setHeader("Cache-Control", "no-cache");
        }

        // retrieve the user agent of the HTTP client.
        String userAgent = aRequest.getHeader("User-Agent");

        // the expires header will be set if the client is Microsoft IE for this
        // browser will cahche pages differently
        if ((userAgent != null) && (userAgent.indexOf("MSIE") != -1)) {

            // add the HTTP EXPIRE header in the servlet response, the date and
            // timestamp will be set to 1 Jan 1970 so no caching is performed.
            aResponse.setDateHeader("Expires", 0);
        }
    }


}