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
package glowaxes.tags;

import glowaxes.util.TypeConverter;

import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

// @SuppressWarnings("unchecked")
/* extends AbstractTag */
/**
 * The Class DataTag.
 */
public class FormatTag extends AbstractBodyTag {

    // Define a static logger variable
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(FormatTag.class.getName());

    /**
     * 
     */
    private static final long serialVersionUID = -8258639903453250653L;

    /** The data attributes. */
    private HashMap<String, String> formatAttributes =
            new HashMap<String, String>();

    // Properties, stored in formatAttributes
    // -------------------------------------------------------------------------

    // Tag interface
    // -------------------------------------------------------------------------
    /**
     * Instantiates a new format tag.
     */
    public FormatTag() {
        super();
    }

    /*
     * Override: The method below will use the PageContext to get a JspWriter
     * class. It will then use this Writer to write a message to the encapsuling
     * JSP page. As the SupportTag has implemented a doStartTag() method that
     * returns SKIP_BODY, we do not need to implement that or any other method
     * that we don't want to override.
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspTagException {

        DataTag dataTag = (DataTag) findAncestorWithClass(this, DataTag.class);
        if (dataTag == null) {
            throw new JspTagException(
                    "<ga:format> tag must be within a <ga:data> tag");
        } else {

            if (formatAttributes.get("locale") == null) {
                setLocale();
            }

            if (formatAttributes.get("timeZone") == null) {
                setTimeZone("timeZone");
            }

            dataTag.addFormat(formatAttributes);
        }
        // todo flush can be pooled
        formatAttributes = new HashMap<String, String>();
        return EVAL_PAGE;
    }

    /*
     * Override: Container calls the doStartTag when tag starts
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    @Override
    public void release() {
        // flush to garbage (can be pooled)
        formatAttributes = new HashMap<String, String>();
        super.release();
    }

    /**
     * Sets the formatting for currency values.
     * 
     * @param currencyFormat
     *            the currency format
     */
    public void setCurrency(String currencyFormat) {
        if (currencyFormat != null && currencyFormat.trim().equals(""))
            currencyFormat = null;
        formatAttributes.put("currencyFormat", currencyFormat);
    }

    /**
     * Sets the formatting for date values.
     * 
     * @param dateFormat
     *            the date format
     */
    public void setDate(String dateFormat) {
        if (dateFormat != null && dateFormat.trim().equals(""))
            dateFormat = null;
        formatAttributes.put("dateFormat", dateFormat);
    }

    /**
     * Sets the formatting for float and double values.
     * 
     * @param floatFormat
     *            the floats format
     */
    public void setFloat(String floatFormat) {
        if (floatFormat != null && floatFormat.trim().equals(""))
            floatFormat = null;
        formatAttributes.put("floatFormat", floatFormat);
    }

    /**
     * Sets the formatting for header values.
     * 
     * @param headerFormat
     *            the heading format
     */
    public void setHeader(String headerFormat) {
        if (headerFormat != null && headerFormat.trim().equals(""))
            headerFormat = null;
        formatAttributes.put("headerFormat", headerFormat);
    }

    /**
     * Sets the formatting for byte, short, integers and long values.
     * 
     * @param intFormat
     *            the integer format
     */
    public void setInt(String intFormat) {
        if (intFormat != null && intFormat.trim().equals(""))
            intFormat = null;
        formatAttributes.put("intFormat", intFormat);
    }

    /**
     * Sets the locale, the way numbers/ dates are presented.
     * 
     * @param locale
     *            The locale see java.util.Locale
     */
    public void setLocale(String locale) {

        Locale tmpLocale = TypeConverter.getLocale(locale);

        formatAttributes.put("locale", tmpLocale.toString());

    }

    /**
     * Sets the locale, the way numbers/ dates are presented.
     * 
     * @param locale
     *            The locale see java.util.Locale
     */
    private void setLocale() {

        // set response locale
        ServletResponse response = pageContext.getResponse();
        Locale locale = response.getLocale();

        formatAttributes.put("locale", locale.toString());
    }

    /**
     * Sets the regex match for which this formatting is done.
     * 
     * @param match
     *            the match format
     */
    public void setMatch(String match) {
        if (match != null && match.trim().equals(""))
            match = null;
        formatAttributes.put("match", match);
    }

    /**
     * Sets the formatting for text values.
     * 
     * @param textFormat
     *            the text format
     */
    public void setText(String textFormat) {
        if (textFormat != null && textFormat.trim().equals(""))
            textFormat = null;
        formatAttributes.put("textFormat", textFormat);
    }

    /**
     * Sets the timeZone, the way dates are presented.
     * 
     * @param timeZone
     *            The timeZone see java.util.TimeZone
     */
    public void setTimeZone(String var) {

        Object timeZone = null;

        if (timeZone == null)
            timeZone = pageContext.getAttribute(var, PageContext.PAGE_SCOPE);
        if (timeZone == null)
            timeZone = pageContext.getAttribute(var, PageContext.REQUEST_SCOPE);
        if (timeZone == null)
            timeZone = pageContext.getAttribute(var, PageContext.SESSION_SCOPE);
        if (timeZone == null)
            timeZone =
                    pageContext
                            .getAttribute(var, PageContext.APPLICATION_SCOPE);

        if (timeZone == null || !(timeZone instanceof TimeZone))
            timeZone = TypeConverter.getTimeZone(var, null);

        TimeZone tmpZone = (TimeZone) timeZone;

        formatAttributes.put("timeZone", tmpZone.getID());
    }
}
