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
 * The Class LabelsBehaviorTag.
 */
public class LabelsBehaviorTag extends AbstractBodyTag {

    // Define a static logger variable
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(LabelsBehaviorTag.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1731548815061564185L;

    /** The labels attributes. */
    private HashMap<String, String> labelsAttributes =
            new HashMap<String, String>();

    // Properties, stored in labelsAttributes
    // -------------------------------------------------------------------------

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new labels tag.
     */
    public LabelsBehaviorTag() {
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
            setText(text);
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

        DataTag dataTag = (DataTag) findAncestorWithClass(this, DataTag.class);
        if (dataTag == null) {
            throw new JspTagException(
                    "&lt;ga:label> tag must be within a &lt;ga:data> tag");
        } else {
            dataTag.setAttribute("labels", labelsAttributes);
        }
        // flush can be pooled
        labelsAttributes = new HashMap<String, String>();
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
        labelsAttributes = new HashMap<String, String>();
        super.release();
    }

    /**
     * Sets the popup.
     * 
     * @param popup
     *            The popup of the labels [true|false|smartOverlayTM]
     */
    public void setPopup(String popup) {
        labelsAttributes.put("popup", popup);
    }

    // /**
    // * Sets rx of the labels rectangle (SVG).
    // *
    // * @param rx
    // * The rx radius (SVG) see
    // * http://www.w3.org/TR/SVG/shapes.html#RectElement
    // */
    // public void setRx(String rx) {
    // labelsAttributes.put("rx", rx);
    // }
    //
    // /**
    // * Sets ry of the labels rectangle (SVG).
    // *
    // * @param ry
    // * The ry radius (SVG) see
    // * http://www.w3.org/TR/SVG/shapes.html#RectElement
    // */
    // public void setRy(String ry) {
    // labelsAttributes.put("ry", ry);
    // }

    /**
     * Sets the title, the labels text.
     * 
     * @param text
     *            the text
     */
    public void setText(String text) {
        if (text != null)
            text = text.trim();
        logger.error(text);
        labelsAttributes.put("text", text);
    }

    public void setType(String type) {
        labelsAttributes.put("type", type);
    }

}
