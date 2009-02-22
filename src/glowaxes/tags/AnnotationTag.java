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

// TODO: Auto-generated Javadoc
// @SuppressWarnings("unchecked")
/**
 * The Class AnnotationTag.
 */
public class AnnotationTag extends AbstractBodyTag {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6296175380501569208L;

    /** The annotation attributes. */
    private HashMap<String, Object> annotationAttributes =
            new HashMap<String, Object>();

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new annotation tag.
     */
    public AnnotationTag() {
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
        // flush can be pooled
        annotationAttributes = new HashMap<String, Object>();
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
            throw new JspTagException(
                    "<ga:annotation> tag must be within a <ga:area> tag");
        } else {
            areaTag.setAttribute("annotation", annotationAttributes);
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
        annotationAttributes = new HashMap<String, Object>();
        super.release();
    }


    /**
     * Sets the background, the way the annotation background is styled.
     * 
     * @param background
     *            The background style
     *            [fill:url(#vertical);stroke:blue;stroke-width:3;opacity:1.0]
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     */
    public void setBackground(String background) {
        annotationAttributes.put("background", Defs.parseStyle(background));
    }

    /**
     * Sets the height.
     * 
     * @param height
     *            The height of the annotation see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setHeight(String height) {
        annotationAttributes.put("height", height);
    }

    /**
     * Sets the popup.
     * 
     * @param popup
     *            the new popup
     */
    public void setPopup(String popup) {
        annotationAttributes.put("popup", popup);
    }

    /**
     * Sets rx of the annotation rectangle (SVG).
     * 
     * @param rx
     *            The rx radius (SVG) see
     *            http://www.w3.org/TR/SVG/shapes.html#RectElement
     */
    public void setRx(String rx) {
        annotationAttributes.put("rx", rx);
    }

    /**
     * Sets ry of the annotation rectangle (SVG).
     * 
     * @param ry
     *            The ry radius (SVG)
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/shapes.html#RectElement'>http://www.w3.org/TR/SVG/shapes.html#RectElement</a>
     */
    public void setRy(String ry) {
        annotationAttributes.put("ry", ry);
    }

    /**
     * Sets the annotation style.
     * 
     * @param style
     *            The annotations's style, no style defaults to nice
     *            [text-anchor:middle;font-family:Arial;font-size:
     *            14pt;font-weight:bold;]
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     */
    public void setStyle(String style) {
        annotationAttributes.put("style", Defs.parseStyle(style));
    }

    /**
     * Sets the title, the annotation title.
     * 
     * @param text
     *            the text
     */
    public void setText(String text) {
        if (text != null)
            text = text.trim();
        annotationAttributes.put("text", text);
    }

    /**
     * Sets the width.
     * 
     * @param width
     *            The width of the annotation see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setWidth(String width) {
        annotationAttributes.put("width", width);
    }

    /**
     * Sets the x offset from left (SVG).
     * 
     * @param x
     *            The x offset from left (SVG) see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setX(String x) {
        annotationAttributes.put("x", x);
    }

    /**
     * Sets the y offset from left (SVG).
     * 
     * @param y
     *            The y offset from left (SVG) see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setY(String y) {
        annotationAttributes.put("y", y);
    }

}
