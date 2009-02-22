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

import glowaxes.data.DataIsland;
import glowaxes.data.SqlSerie;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("unchecked")
/* extends AbstractTag */
/**
 * The Class DataTag.
 */
public class DataTag extends AbstractBodyTag {

    // Define a static logger variable
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(DataTag.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8269546235289854797L;

    private String arrayList;

    /** The data attributes. */
    private HashMap<String, Object> dataAttributes =
            new HashMap<String, Object>();

    private ArrayList<HashMap<String, String>> formatList;

    private int scope = PageContext.REQUEST_SCOPE;

    private ArrayList<SqlSerie> sqlSeries;

    // Properties, stored in dataAttributes
    // -------------------------------------------------------------------------

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new data tag.
     */
    public DataTag() {
        super();

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.tags.AttributeInterface#setAttribute(java.lang.String,
     *      java.util.HashMap)
     */
    public void addFormat(HashMap<String, String> formatMap) {
        if (formatList == null)
            formatList = new ArrayList<HashMap<String, String>>();
        formatList.add(formatMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.tags.AttributeInterface#setAttribute(java.lang.String,
     *      java.util.HashMap)
     */
    public void addSqlSeries(SqlSerie sqlSerie) {
        if (sqlSeries == null)
            sqlSeries = new ArrayList<SqlSerie>();
        sqlSeries.add(sqlSerie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    @Override
    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        if (body != null) {
            String text = body.getString();
            if (!text.trim().equals("")) {
                body.clearBody();
                setData(text);
            }
        }
        return EVAL_PAGE;
    }

    /**
     * Sets the attribute.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     */
    @SuppressWarnings("unchecked")
    public synchronized void setAttribute(String key, HashMap value) {

        dataAttributes.put(key, value.clone());

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
    @SuppressWarnings("unchecked")
    @Override
    public int doEndTag() throws JspTagException {

        String id = (String) dataAttributes.get("id");

     // if no id is set then default to the charts id
        if (id == null) {

            

            ChartTag chartTag =
                    (ChartTag) findAncestorWithClass(this, ChartTag.class);

            if (chartTag == null) {
                throw new RuntimeException("A data tag (that is not"
                        + " nested in a chart tag) must include an"
                        + " id attribute, so that it can be reference later");

            }
            id = chartTag.getId();
        }
        setId(id);

        AreaTag areaTag = (AreaTag) findAncestorWithClass(this, AreaTag.class);

        if (areaTag != null) {
            areaTag.setDataId(id);
        }

        if (formatList == null) {
            HashMap<String, String> formatAttributes =
                    new HashMap<String, String>();
            addFormat(formatAttributes);
        }
        dataAttributes.put("formatList", formatList);

        if (sqlSeries != null) {
            dataAttributes.put("sqlSeries", sqlSeries);
        }

        if (arrayList != null) {
            dataAttributes.put("arrayList", arrayList);
        }

        if (dataAttributes.get("data") != null
                || dataAttributes.get("sqlSeries") != null)
            DataIsland.SINGLETON
                    .setData(id, dataAttributes, pageContext, scope);

        if (dataAttributes.get("labels") != null) {
            logger.error(dataAttributes.get("labels"));
            DataIsland.SINGLETON.getData(id, pageContext).put("labels",
                    dataAttributes.get("labels"));
        }

        // todo flush can be pooled
        dataAttributes = new HashMap<String, Object>();
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
        dataAttributes = new HashMap<String, Object>();
        super.release();
    }

    /**
     * Sets the arrayList to page context with scope.
     * 
     * @param arrayList
     *            the arrayList
     */
    public void setArrayList(String arrayList) {
        this.arrayList = arrayList;
    }

    /**
     * Sets the data.
     * 
     * @param data
     *            the new data
     */
    public void setData(String data) {
        if (data != null && data.trim().equals(""))
            data = null;
        dataAttributes.put("data", data);
    }

    /**
     * Sets the effect.
     * 
     * @param effect
     *            the effect
     */
    public void setEffect(String effect) {
        dataAttributes.put("effect", Defs.addUsed(effect));
    }

    /**
     * Sets the expireTimeMilli.
     * 
     * @param expireTimeMilli
     *            the new expireTimeMilli
     */
    public void setExpireTimeMilli(String expireTimeMilli) {
        if (expireTimeMilli.trim().equals(""))
            expireTimeMilli = null;
        dataAttributes.put("expireTimeMilli", expireTimeMilli);
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    @Override
    public void setId(String id) {
        if (id != null && id.trim().equals(""))
            id = null;
        dataAttributes.put("id", id);
    }

    /**
     * Sets the palette.
     * 
     * @param palette
     *            The palette used to paint the
     *            [primary][rainbow][texture][blue-pallete][red-pallete][green-pallete][yellow-pallete][orange-pallete]
     *            see http://www.w3.org/TR/SVG/coords.html
     */
    public void setPalette(String palette) {
        dataAttributes.put("palette", palette);
    }

    /**
     * Sets the scope. Default is request scope. Session, page and application
     * can also be used.
     * 
     * @param scope
     *            The scope used in the page context
     */
    public void setScope(String scope) {
        if (logger.isDebugEnabled())
            logger.debug("setting scope to: " + scope);
        if (scope.equalsIgnoreCase("page"))
            this.scope = PageContext.PAGE_SCOPE;
        else if (scope.equalsIgnoreCase("request"))
            this.scope = PageContext.REQUEST_SCOPE;
        else if (scope.equalsIgnoreCase("session"))
            this.scope = PageContext.SESSION_SCOPE;
        else if (scope.equalsIgnoreCase("application"))
            this.scope = PageContext.APPLICATION_SCOPE;
        else
            throw new IllegalArgumentException("invalid scope");
    }

    /**
     * Sets the source.
     * 
     * @param source
     *            The data source of the area [file.xml][file.csv] see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setSource(String source) {
        dataAttributes.put("source", source);
    }

    /**
     * Sets the defaultStyle.
     * 
     * @param style
     *            the style
     */
    public void setStyle(String style) {
        dataAttributes.put("style", Defs.parseStyle(style));
    }
}
