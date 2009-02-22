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
// Referenced classes of package glowaxes.tags:
// AbstractBodyTag, AttributeInterface

// @SuppressWarnings("unchecked")
/**
 * The Class AbstractAxisTag.
 */
public abstract class AbstractAxisTag extends AbstractBodyTag {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(AbstractAxisTag.class.getName());

    /** The axis attributes. */
    HashMap<String, Object> axisAttributes;

    /**
     * Instantiates a new abstract axis tag.
     */
    public AbstractAxisTag() {
        axisAttributes = new HashMap<String, Object>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    @Override
    public int doAfterBody() throws JspException {
        BodyContent bodycontent = getBodyContent();
        if (bodycontent != null) {
            String s = bodycontent.getString();
            bodycontent.clearBody();
            setTitle(s);
        }
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspTagException {
        axisAttributes = new HashMap<String, Object>();
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspTagException {
        return EVAL_BODY_BUFFERED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    @Override
    public void release() {
        axisAttributes = new HashMap<String, Object>();
        super.release();
    }

    /**
     * Sets the arrow.
     * 
     * @param s
     *            the new arrow
     */
    public void setArrow(String s) {
        axisAttributes.put("arrow", s);
    }

    /**
     * Sets the grid minor style.
     * 
     * @param s
     *            the new grid minor style
     */
    public void setGridMinorStyle(String s) {
        axisAttributes.put("gridMinorStyle", Defs.parseStyle(s));
    }

    /**
     * Sets the grid style.
     * 
     * @param s
     *            the new grid style
     */
    public void setGridStyle(String s) {
        axisAttributes.put("gridStyle", Defs.parseStyle(s));
    }

    /**
     * Sets the label effect.
     * 
     * @param s
     *            the label effect
     */
    public void setLabelEffect(String s) {
        axisAttributes.put("labelEffect", Defs.addUsed(s));
    }

    /**
     * Sets the label format.
     * 
     * @param s
     *            the new label format
     */
    public void setLabelFormat(String s) {
        axisAttributes.put("labelFormat", s);
    }

    /**
     * Sets the label rotation.
     * 
     * @param s
     *            the new label rotation
     */
    public void setLabelRotation(String s) {
        axisAttributes.put("labelRotation", s);
    }

    /**
     * Sets the label step.
     * 
     * @param s
     *            the new label step
     */
    public void setLabelStep(String s) {
        axisAttributes.put("labelStep", s);
    }

    /**
     * Sets the label style.
     * 
     * @param s
     *            the new label style
     */
    public void setLabelStyle(String s) {
        axisAttributes.put("labelStyle", Defs.parseStyle(s));
    }

    /**
     * Sets the max.
     * 
     * @param s
     *            the new max
     */
    public void setMax(String s) {
        axisAttributes.put("max", s);
    }

    /**
     * Sets the min.
     * 
     * @param s
     *            the new min
     */
    public void setMin(String s) {
        axisAttributes.put("min", s);
    }

    /**
     * Sets the show zero.
     * 
     * @param s
     *            the new show zero
     */
    public void setShowZero(String s) {
        axisAttributes.put("showZero", s);
    }

    /**
     * Sets the style.
     * 
     * @param s
     *            the new style
     */
    public void setStyle(String s) {

        axisAttributes.put("style", Defs.parseStyle(s));
    }

    /**
     * Sets the tic align.
     * 
     * @param s
     *            the new tic align
     */
    public void setTicAlign(String s) {
        axisAttributes.put("ticAlign", s);
    }

    /**
     * Sets the tic length.
     * 
     * @param s
     *            the new tic length
     */
    public void setTicLength(String s) {
        axisAttributes.put("ticLength", s);
    }

    /**
     * Sets the tic rotation.
     * 
     * @param s
     *            the new tic rotation
     */
    public void setTicRotation(String s) {
        axisAttributes.put("ticRotation", s);
    }

    /**
     * Sets the tic style.
     * 
     * @param s
     *            the new tic style
     */
    public void setTicStyle(String s) {
        axisAttributes.put("ticStyle", Defs.parseStyle(s));
    }

    /**
     * Sets the title.
     * 
     * @param s
     *            the new title
     */
    public void setTitle(String s) {
        if (s != null)
            s = s.trim();
        axisAttributes.put("title", s);
    }

    /**
     * Sets the title align.
     * 
     * @param s
     *            the new title align
     */
    public void setTitleAlign(String s) {
        axisAttributes.put("titleAlign", s);
    }

    // public void setGap(String s) {
    // logger.error("setGap");
    // axisAttributes.put("gap", s);
    // }

    /**
     * Sets the title effect.
     * 
     * @param s
     *            the new title effect
     */
    public void setTitleEffect(String s) {
        axisAttributes.put("titleEffect", Defs.addUsed(s));
    }

    /**
     * Sets the title style.
     * 
     * @param s
     *            the new title style
     */
    public void setTitleStyle(String s) {
        axisAttributes.put("titleStyle", Defs.parseStyle(s));
    }

    /**
     * Sets the title valign.
     * 
     * @param s
     *            the new title valign
     */
    public void setTitleValign(String s) {
        axisAttributes.put("titleAlign", s);
    }

    /**
     * Sets the type.
     * 
     * @param s
     *            the new type
     */
    public void setType(String s) {
        axisAttributes.put("type", s);
    }

}