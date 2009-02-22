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

import glowaxes.data.SqlSerie;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("unchecked")
/* extends AbstractTag */
/**
 * The Class DataTag.
 */
public class SqlTag extends AbstractBodyTag {

    // Define a static logger variable
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(SqlTag.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8269546235289854797L;

    /** The sql attributes. */
    private HashMap<String, Object> sqlAttributes =
            new HashMap<String, Object>();

    @SuppressWarnings("unused")
    private ArrayList<Object> parameters;

    // Properties, stored in sqlAttributes
    // -------------------------------------------------------------------------

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new data tag.
     */
    public SqlTag() {
        super();

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
            body.clearBody();
            sqlAttributes.put("content", text);
        }
        return EVAL_PAGE;
    }

    /*
     * Override: The method below will use the PageContext to get a JspWriter
     * class. It will then use this Writer to write a message to the encapsuling
     * JSP page. As the SupportTag has implemented a doStartTag() method that
     * returns SKIP_BODY, we do not need to implement that or any other method
     * that we don't want to override.
     */
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @SuppressWarnings("unchecked")
    @Override
    public int doEndTag() throws JspTagException {
        DataTag dataTag = (DataTag) findAncestorWithClass(this, DataTag.class);
        if (dataTag == null) {
            throw new JspTagException(
                    "<ga:sql> tag must be within a <ga:data> tag");
        } else {
            SqlSerie sqlSerie = new SqlSerie();
            sqlSerie.setContent((String) sqlAttributes.get("content"));
            sqlSerie.setParameters(parameters);
            sqlSerie.setVar((String) sqlAttributes.get("var"));
            sqlSerie.setDataSource((String) sqlAttributes.get("dataSource"));
            sqlSerie.setDefaultLabel((String) sqlAttributes
                    .get("default-label"));
            sqlSerie.setDefaultLabelBackgroundStyle((String) sqlAttributes
                    .get("default-label-background-style"));
            sqlSerie.setDefaultLabelStyle((String) sqlAttributes
                    .get("default-label-style"));
            sqlSerie.setDefaultShape((String) sqlAttributes
                    .get("default-shape"));
            sqlSerie.setDefaultShapeStyle((String) sqlAttributes
                    .get("default-shape-style"));
            sqlSerie.setId((String) sqlAttributes.get("id"));
            sqlSerie.setLegend((String) sqlAttributes.get("legend"));
            sqlSerie.setLineStyle((String) sqlAttributes.get("line-style"));

            sqlAttributes.remove("content");
            sqlAttributes.remove("dataSource");
            sqlAttributes.remove("default-label");
            sqlAttributes.remove("default-label-background-style");
            sqlAttributes.remove("default-label-style");
            sqlAttributes.remove("default-shape");
            sqlAttributes.remove("id");
            sqlAttributes.remove("legend");
            sqlAttributes.remove("line-style");

            sqlSerie.setSqlAttributes(sqlAttributes);
            dataTag.addSqlSeries(sqlSerie);
        }

        // todo flush can be pooled
        sqlAttributes = new HashMap<String, Object>();
        parameters = null;
        return EVAL_PAGE;
    }

    /*
     * Override: Container calls the doStartTag when tag starts
     */
    /*
     * (non-Javadoc)
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
    @SuppressWarnings("unchecked")
    @Override
    public void release() {
        // flush to garbage (can be pooled)
        sqlAttributes = new HashMap<String, Object>();
        parameters = null;
        super.release();
    }

    /**
     * Sets the a1.
     * 
     * @param a1
     *            the new a1
     */
    public void setA1(String a1) {
        sqlAttributes.put("a1", a1);
    }

    /**
     * Sets the a2.
     * 
     * @param a2
     *            the new a2
     */
    public void setA2(String a2) {
        sqlAttributes.put("a2", a2);
    }

    /**
     * Sets the b1.
     * 
     * @param b1
     *            the new b1
     */
    public void setB1(String b1) {
        sqlAttributes.put("b1", b1);
    }

    /**
     * Sets the b2.
     * 
     * @param b2
     *            the new b2
     */
    public void setB2(String b2) {
        sqlAttributes.put("b2", b2);
    }

    /**
     * Sets the effect.
     * 
     * @param effect
     *            the effect
     */
    public void setEffect(String effect) {
        sqlAttributes.put("effect", Defs.addUsed(effect));
    }

    /**
     */
    public void setDefaultLabel(String label) {
        sqlAttributes.put("default-label", label);
    }

    /**
     */
    public void setDefaultLabelBackgroundStyle(
            String defaultLabelBackgroundStyle) {
        logger.error(defaultLabelBackgroundStyle);
        sqlAttributes.put("default-label-background-style",
                defaultLabelBackgroundStyle);
    }

    /**
     */
    public void setDefaultLabelStyle(String defaultLabelStyle) {
        sqlAttributes.put("default-label-style", defaultLabelStyle);
    }

    /**
     */
    public void setDefaultShape(String defaultShape) {
        sqlAttributes.put("default-shape", defaultShape);
    }

    /**
     */
    public void setDefaultShapeStyle(String defaultShapeStyle) {
        sqlAttributes.put("default-shape-style", defaultShapeStyle);
    }

    /**
     */
    @Override
    public void setId(String id) {
        sqlAttributes.put("id", id);
    }

    /**
     */
    public void setLegend(String legend) {
        sqlAttributes.put("legend", legend);
    }

    /**
     */
    public void setLineStyle(String lineStyle) {
        sqlAttributes.put("line-style", lineStyle);
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            the new label
     */
    public void setLabel(String label) {
        sqlAttributes.put("label", label);
    }

    /**
     * Sets the label background style.
     * 
     * @param labelBackgroundStyle
     *            the new label background style
     */
    public void setLabelBackgroundStyle(String labelBackgroundStyle) {
        sqlAttributes.put("labelBackgroundStyle", labelBackgroundStyle);
    }

    /**
     * Sets the label events.
     * 
     * @param labelEvents
     *            the new label events
     */
    public void setLabelEvents(String labelEvents) {
        sqlAttributes.put("labelEvents", labelEvents);
    }

    /**
     * Sets the label href.
     * 
     * @param labelHref
     *            the new label href
     */
    public void setLabelHref(String labelHref) {
        sqlAttributes.put("labelHref", labelHref);
    }

    /**
     * Sets the label style.
     * 
     * @param labelStyle
     *            the new label style
     */
    public void setLabelStyle(String labelStyle) {
        sqlAttributes.put("labelStyle", labelStyle);
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
        sqlAttributes.put("palette", palette);
    }

    /**
     * Sets the parameters.
     * 
     * @param parameters
     *            the new parameters
     */
    @SuppressWarnings("unchecked")
    public void setParameters(ArrayList<Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Sets the data source.
     * 
     * @param dataSource
     *            the new data source
     */
    public void setDataSource(String dataSource) {
        sqlAttributes.put("dataSource", dataSource);
    }

    /**
     * Sets the series.
     * 
     * @param series
     *            the new series
     */
    public void setSeries(String series) {
        sqlAttributes.put("series", series);
    }

    /**
     * Sets the shape. shape-name [ size ][ angle ]
     * 
     * @param shape
     *            The shape used to paint the [alternating arrow circle clover
     *            cross dollar dot euro ibeam octagon pound rectangle star tbeam
     *            triangle x ybeam yield] see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setShape(String shape) {
        sqlAttributes.put("shape", shape);
    }

    /**
     * Sets the shapeStyle. [fill:yellow;stroke:black]
     * 
     * @param shapeStyle
     *            The shapeStyle used to paint the [fill:yellow;stroke:black]
     *            see http://www.w3.org/TR/SVG/coords.html
     */
    public void setShapeStyle(String shapeStyle) {
        sqlAttributes.put("shapeStyle", Defs.parseStyle(shapeStyle));
    }

    /**
     * Sets the source.
     * 
     * @param source
     *            The data source of the area [file.xml][file.csv] see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setSource(String source) {
        sqlAttributes.put("source", source);
    }

    /**
     * Sets the defaultStyle.
     * 
     * @param style
     *            the style
     */
    public void setStyle(String style) {
        sqlAttributes.put("style", Defs.parseStyle(style));
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the new type
     */
    public void setType(String type) {
        sqlAttributes.put("type", type);
    }

    /**
     * Sets the variable to hold the result object.
     * 
     * @param var
     *            the variable holding the result object
     */
    public void setVar(String var) {
        sqlAttributes.put("var", var);
    }

    /**
     * Sets the x.
     * 
     * @param x
     *            the new x
     */
    public void setX(String x) {
        sqlAttributes.put("x", x);
    }

    /**
     * Sets the y.
     * 
     * @param y
     *            the new y
     */
    public void setY(String y) {
        sqlAttributes.put("y", y);
    }

    /**
     * Sets the z.
     * 
     * @param z
     *            the new z
     */
    public void setZ(String z) {
        sqlAttributes.put("z", z);
    }

}
