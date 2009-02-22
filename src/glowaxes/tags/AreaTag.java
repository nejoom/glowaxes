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

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("unchecked")
/**
 * The Class AreaTag.
 */
public class AreaTag extends AbstractTag {

    // Define a static logger variable
    /** The logger. */
    private static Logger logger = Logger.getLogger(AreaTag.class.getName());

    /** The annotation counter. */
    private static int annotationCounter = 0;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3973706208497336193L;

    /** The area attributes. */
    private HashMap<String, Object> areaAttributes =
            new HashMap<String, Object>();

    // Properties, stored in areaAttributes
    // -------------------------------------------------------------------------

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new area tag.
     */
    public AreaTag() {
        super();

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
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspTagException {
        ChartTag chartTag =
                (ChartTag) findAncestorWithClass(this, ChartTag.class);
        if (chartTag == null) {
            throw new JspTagException(
                    "<ga:area> tag must be within a <ga:chart> tag");
        } else {
            if (chartTag.getOutput().indexOf("html") != -1) {
                logger.info("Overriding area type for output: "
                        + chartTag.getOutput());
                areaAttributes.put("type", chartTag.getOutput());
            }
            chartTag.setAttribute("area", areaAttributes);
        }

        // flush can be pooled
        areaAttributes = new HashMap<String, Object>();
        return EVAL_PAGE;
    }

    /*
     * Override: Container calls the doStartTag when tag starts
     */
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        return EVAL_BODY_INCLUDE;
    }

    //
    // public void setWidth(int width) {
    // areaAttributes.put("width", new Integer(width));
    // }
    //
    // public int getWidth() {
    // return TypeConverter.getInt(areaAttributes.get("width"), 0);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#release()
     */
    @Override
    public void release() {
        // flush to garbage (can be pooled)
        areaAttributes = new HashMap<String, Object>();
        super.release();
    }

    // public int getHeight() {
    // return areaAttributes.get("height");
    // }

    //
    // public void setX(int x) {
    // areaAttributes.put("x", new Integer(x));
    // }

    // public int getX() {
    // return TypeConverter.getInt(areaAttributes.get("x"), 0);
    // }

    // public int getY() {
    // return TypeConverter.getInt(areaAttributes.get("y"), 0);
    // }

    /**
     * Sets the align, the area align.
     * 
     * @param align
     *            The area's alignment relative to the chart containing it,
     *            [left][right][center][x,y]
     */
    public void setAlign(String align) {
        align = align.toLowerCase().trim();
        areaAttributes.put("align", align);
    }

    // public int getRx() {
    // return TypeConverter.getInt(areaAttributes.get("rx"), 0);
    // }

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
        if (areaAttributes.containsKey(key) && key.equals("annotation"))
            key = key + annotationCounter++;
        if (areaAttributes.containsKey(key) && key.equals("xmarkers"))
            key = key + annotationCounter++;
        if (areaAttributes.containsKey(key) && key.equals("ymarkers"))
            key = key + annotationCounter++;

        areaAttributes.put(key, value.clone());

    }

    protected void setDataId(String dataId) {
        areaAttributes.put("data", dataId);
    }

    // public int getRy() {
    // return TypeConverter.getInt(areaAttributes.get("ry"), 0);
    // }

    // public Locale getLocale() {
    // return TypeConverter.getLocale((String)areaAttributes.get("locale"));
    // }

    /**
     * Sets the backgroundImage, the area backgroundImage.
     * 
     * @param backgroundImage
     *            The backgroundImage of the area eg "/home/sample_area.jpg"
     */
    public void setBackgroundImage(String backgroundImage) {
        areaAttributes.put("backgroundImage", Defs.parseStyle(backgroundImage));
    }

    /**
     * Sets the effect, the area effect.
     * 
     * @param effect
     *            The svg effect on the area
     *            [none][Shadow][ShadowGloss][ShadowFast][ShadowTopExpensive][ShadowCartesian][ShadowAdobe][ShadowBrushTM]
     */
    public void setEffect(String effect) {
        areaAttributes.put("effect", Defs.addUsed(effect));
    }

    /**
     * Sets the height.
     * 
     * @param height
     *            The height of the area see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setHeight(String height) {
        height = height.trim();
        if (height.lastIndexOf('%') != height.length() - 1) {
            throw new RuntimeException("Height attribute for area tag can "
                    + "only be a percentage. Example syntax height=\"50%\"");
        }
        areaAttributes.put("height", height);
    }

    /**
     * Sets the rendering width of the data (line width/ bar width).
     * 
     * @param renderingWidth
     *            the rendering width of the data
     */
    public void setRenderingWidth(String renderingWidth) {
        areaAttributes.put("renderingWidth", renderingWidth);
    }

    /**
     * Sets rx of the area rectangle (SVG).
     * 
     * @param rx
     *            The rx radius (SVG) see
     *            http://www.w3.org/TR/SVG/shapes.html#RectElement
     */
    public void setRx(String rx) {
        areaAttributes.put("rx", rx);
    }

    /**
     * Sets ry of the area rectangle (SVG).
     * 
     * @param ry
     *            The ry radius (SVG) see
     *            http://www.w3.org/TR/SVG/shapes.html#RectElement
     */
    public void setRy(String ry) {
        areaAttributes.put("ry", ry);
    }

    /**
     * Sets the style, the way the area rectangle is styled.
     * 
     * @param style
     *            The style
     *            [fill:url(#vertical);stroke:blue;stroke-width:3;opacity:1.0]
     *            see http://www.w3.org/TR/SVG/painting.html
     */
    public void setStyle(String style) {
        areaAttributes.put("style", Defs.parseStyle(style));
    }

    /**
     * Sets the type of plot in the area.
     * 
     * @param type
     *            The area's type of plot. Possible value are: Stacked Column
     *            100%, Stacked Bar 100%, Stacked Area 100%, Area, Bar Bubble,
     *            Column, Doughnut, Error Bar Charts, Line, Point, Range Column
     *            Chart Range, Spline, SplineArea, Spline Range, Stacked Bar,
     *            Stacked Column Stacked Area, Stock, Funnel Charts, Gantt,
     *            Histogram Charts, Pyramid Charts Pie, Pie Chart Small
     *            Segments, Polar Charts, Radar Chart
     */
    public void setType(String type) {
        type = type.toLowerCase().trim();
        areaAttributes.put("type", type);
    }

    /**
     * Sets the valign, the area valign.
     * 
     * @param valign
     *            The area's vertical alignment relative to the chart containing
     *            it, [top][bottom]
     */
    public void setValign(String valign) {
        valign = valign.toLowerCase().trim();
        areaAttributes.put("valign", valign);
    }

    /**
     * Sets the width.
     * 
     * @param width
     *            The width of the area see http://www.w3.org/TR/SVG/coords.html
     */
    public void setWidth(String width) {
        width = width.trim();
        if (width.lastIndexOf('%') != width.length() - 1) {
            throw new RuntimeException("Width attribute for area tag can "
                    + "only be a percentage. Example syntax width=\"50%\"");
        }
        areaAttributes.put("width", width);
    }

}
