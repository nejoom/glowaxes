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
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("unchecked")
/**
 * The Class LegendTag.
 */
public class LegendTag extends AbstractBodyTag {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(LegendTag.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3197057081021574799L;

    /** The legend attributes. */
    private HashMap<String, Object> legendAttributes =
            new HashMap<String, Object>();

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new legend tag.
     */
    public LegendTag() {
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
            setTitle(text);
        }
        return EVAL_PAGE;
    }

    /*
     * Override: The method below will flush underlying hashMap
     */
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspTagException {
        // flush can be pooled
        legendAttributes = new HashMap<String, Object>();
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
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspTagException {
        AreaTag areaTag = (AreaTag) findAncestorWithClass(this, AreaTag.class);
        if (areaTag == null) {

            ChartTag chartTag =
                    (ChartTag) findAncestorWithClass(this, ChartTag.class);

            if (chartTag == null) {
                throw new JspTagException(
                        "<ga:legend> tag must be within a <ga:area> tag "
                                + "or a <ga:chart> tag");
            }

            legendAttributes.put("parent", "chart");

            // center will always render rows
            if (legendAttributes.get("align").equals("center")) {
                legendAttributes.put("strategy", "rows");
            } else {
                legendAttributes.put("strategy", "columns");
            }
            chartTag.setAttribute("legend", legendAttributes);
        } else {

            legendAttributes.put("parent", "area");
            areaTag.setAttribute("legend", legendAttributes);
        }

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
        legendAttributes = new HashMap<String, Object>();
        super.release();
    }

    /**
     * Sets the align, the legend align.
     * 
     * @param align
     *            The legend's alignment relative to the area containing it,
     *            [left][right][center][x,y]
     */
    public void setAlign(String align) {
        if (logger.isDebugEnabled())
            logger.debug(align);
        align = align.toLowerCase().trim();
        legendAttributes.put("align", align);
    }

    /**
     * Sets the background, the way the legend background is styled.
     * 
     * @param background
     *            The background style
     *            [fill:url(#vertical);stroke:blue;stroke-width:3;opacity:1.0]
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     * 
     */
    public void setBackground(String background) {
        legendAttributes.put("background", Defs.parseStyle(background));
    }

    /**
     * Sets the background, the way the legend background is styled.
     * 
     * @param effect
     *            the effect
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     */
    public void setEffect(String effect) {
        legendAttributes.put("effect", Defs.addUsed(effect));
    }

    /**
     * Sets the align, the legend align.
     * 
     * @param format
     *            the format
     */
    public void setFormat(String format) {
        format = format.toLowerCase().trim();
        legendAttributes.put("format", format);
    }

    /**
     * Sets the labelEffect, the legends label effect.
     * 
     * @param labelEffect
     *            the label effect
     */
    public void setLabelEffect(String labelEffect) {
        legendAttributes.put("labelEffect", Defs.addUsed(labelEffect));
    }

    /**
     * Sets the labelStyle, the legends label style.
     * 
     * @param labelStyle
     *            The legends's style of the labels, no style defaults to nice
     *            [text-anchor:middle;font-family:Arial;font-size:
     *            14pt;font-weight:bold;]
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     * 
     */
    public void setLabelStyle(String labelStyle) {
        legendAttributes.put("labelStyle", Defs.parseStyle(labelStyle));
    }

    /**
     * Sets the position of labels relative to area (float) or chart (fixed).
     * 
     * @param position
     *            The legends' position relative to area (float) or chart
     *            (fixed)
     */
    public void setPosition(String position) {
        legendAttributes.put("position", position);
    }

    /**
     * Sets the rx, the way the legend background is curved.
     * 
     * @param rx
     *            The rx curve
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     * 
     */
    public void setRx(String rx) {
        legendAttributes.put("rx", rx);
    }

    /**
     * Sets the ry, the way the legend background is curved.
     * 
     * @param ry
     *            The ry curve
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     * 
     */
    public void setRy(String ry) {
        legendAttributes.put("ry", ry);
    }

    /**
     * Sets the strategy for redering labels (row|[column]).
     * 
     * @param strategy
     *            The legends's strategy for redering labels (row|[column])
     */
    public void setStrategy(String strategy) {
        legendAttributes.put("strategy", strategy);
    }

    /**
     * Sets the title, the legend title.
     * 
     * @param title
     *            The legend's title, nothing defaults to NULLIFIED/MINIMISED
     */
    public void setTitle(String title) {
        if (title != null)
            title = title.trim();
        legendAttributes.put("title", title);
    }

    /**
     * Sets the title alignment.
     * 
     * @param titleAlign
     *            The legend's title alignment nothing defaults to left
     *            [left]|right|center
     */
    public void setTitleAlign(String titleAlign) {
        if (titleAlign != null)
            titleAlign = titleAlign.trim();
        legendAttributes.put("titleAlign", titleAlign);
    }

    /**
     * Sets the titleEffect, the legend titleEffect.
     * 
     * @param titleEffect
     *            the title effect
     */
    public void setTitleEffect(String titleEffect) {
        legendAttributes.put("titleEffect", Defs.addUsed(titleEffect));
    }

    /**
     * Sets the titleStyle, the legend titleStyle.
     * 
     * @param titleStyle
     *            the title style
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     * 
     */
    public void setTitleStyle(String titleStyle) {
        legendAttributes.put("titleStyle", Defs.parseStyle(titleStyle));
    }

    /**
     * Sets the valign, the legend valign.
     * 
     * @param valign
     *            The legend's vertical alignment relative to the area
     *            containing it, [top][bottom]
     */
    public void setValign(String valign) {
        if (logger.isDebugEnabled())
            logger.debug(valign);
        valign = valign.toLowerCase().trim();
        legendAttributes.put("valign", valign);
    }

}
