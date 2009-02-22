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
// Referenced classes of package glowaxes.tags:
// AbstractBodyTag
// @SuppressWarnings("unchecked")
/**
 * The Class AbstractMarkerTag.
 */
public abstract class AbstractMarkerTag extends AbstractBodyTag {

    /** The marker attributes. */
    HashMap<String, String> markerAttributes;

    /**
     * Instantiates a new abstract marker tag.
     */
    public AbstractMarkerTag() {
        markerAttributes = new HashMap<String, String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    public int doAfterBody() throws JspException {
        BodyContent bodycontent = getBodyContent();
        if (bodycontent != null) {
            String s = bodycontent.getString();
            bodycontent.clearBody();
            setText(s);
        }
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    public int doEndTag() throws JspTagException {
        markerAttributes = new HashMap<String, String>();
        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    public void release() {
        markerAttributes = new HashMap<String, String>();
        super.release();
    }

    /**
     * Sets the range.
     * 
     * @param s
     *            the new range
     */
    public void setRange(String s) {
        markerAttributes.put("range", s);
    }

    /**
     * Sets the style.
     * 
     * @param s
     *            the new style
     */
    public void setStyle(String s) {
        markerAttributes.put("style", Defs.parseStyle(s));
    }

    /**
     * Sets the text.
     * 
     * @param s
     *            the new text
     */
    public void setText(String s) {
        markerAttributes.put("text", s);
    }

}
