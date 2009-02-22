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
package glowaxes.glyphs;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class SimpleGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public abstract class SimpleGlyph implements IGlyph {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(SimpleGlyph.class.getName());

    /** The align. */
    private String align = "left";

    /** The glyph effect. */
    private String glyphEffect = null;

    /** The glyph height. */
    private double glyphHeight = -1;

    /** The glyph offset bottom. */
    private double glyphOffsetBottom = 0;

    /** The glyph offset left. */
    private double glyphOffsetLeft = 0;

    /** The glyph offset right. */
    private double glyphOffsetRight = 0;

    /** The glyph offset top. */
    private double glyphOffsetTop = 0;

    /** The glyph style. */
    private String glyphStyle = "";

    /** The glyph width. */
    private double glyphWidth = -1;

    /** The glyph x. */
    private double glyphX = -1;

    /** The glyph y. */
    private double glyphY = -1;

    /** The id. */
    private String id = "anonymous simple glyph";

    /** The rx. */
    private double rx = 0;

    /** The ry. */
    private double ry = 0;

    /** The v align. */
    private String vAlign = "center";

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphAlign()
     */
    public String getGlyphAlign() {
        return align;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphEffect()
     */
    public String getGlyphEffect() {
        if (glyphEffect != null && glyphEffect.trim().equals(""))
            glyphEffect = null;
        return glyphEffect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphHeight()
     */
    public double getGlyphHeight() {
        return glyphHeight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetBottom()
     */
    public double getGlyphOffsetBottom() {
        return glyphOffsetBottom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetLeft()
     */
    public double getGlyphOffsetLeft() {
        return glyphOffsetLeft;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetRight()
     */
    public double getGlyphOffsetRight() {
        return glyphOffsetRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetTop()
     */
    public double getGlyphOffsetTop() {
        return glyphOffsetTop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphRx()
     */
    public double getGlyphRx() {
        return rx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphRy()
     */
    public double getGlyphRy() {
        return ry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphStyle()
     */
    public String getGlyphStyle() {
        return glyphStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphVAlign()
     */
    public String getGlyphVAlign() {
        return vAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphWidth()
     */
    public double getGlyphWidth() {
        return glyphWidth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphX()
     */
    public double getGlyphX() {
        return glyphX == -1 ? 0 : glyphX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphY()
     */
    public double getGlyphY() {
        return glyphY == -1 ? 0 : glyphY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getId()
     */
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#renderGlyph()
     */
    public Element renderGlyph() {

        Element g = new Element("g");

        if (getGlyphX() > 0 || getGlyphY() > 0) {
            g.setAttribute("transform", "translate(" + getGlyphX() + ", "
                    + getGlyphY() + ")");
        }

        if (glyphStyle != null && !glyphStyle.equals("")) {

            Element rect = new Element("rect");

            if (getGlyphRx() > 0) {
                rect.setAttribute("rx", "" + getGlyphRx());
            }

            if (getGlyphRy() > 0) {
                rect.setAttribute("ry", "" + getGlyphRy());
            }

            // setGlyphStyle("fill:none;stroke:red");
            if (getGlyphStyle() != null) {
                rect.setAttribute("style", getGlyphStyle());
            }

            rect.setAttribute("width", "" + getGlyphWidth());

            rect.setAttribute("height", "" + getGlyphHeight());

            if (getGlyphEffect() != null && !getGlyphEffect().equals("")) {
                g.setAttribute("filter", "url(#" + getGlyphEffect() + ")");
            }

            g.addContent(rect);

            if (getGlyphX() != 0 || getGlyphY() != 0)
                g.setAttribute("transform", "translate(" + getGlyphX() + ", "
                        + getGlyphY() + ")");

        }

        // if (childGlyph != null) {
        //
        // Element childPlacer = new Element("g");
        // Element renderChild = childGlyph.renderGlyph();
        // // if (this.g)
        // childPlacer.addContent(renderChild);
        //
        // double xt = 0;
        // double yt = 0;
        //
        // if (childGlyph.getGlyphAlign().equals("left")) {
        //
        // xt = childGlyph.getGlyphOffsetLeft();
        //
        // } else if (childGlyph.getGlyphAlign().equals("right")) {
        //
        // xt =
        // this.getGlyphWidth() - childGlyph.getGlyphWidth()
        // - childGlyph.getGlyphOffsetRight();
        //
        // } else if (childGlyph.getGlyphAlign().equals("center")
        // || childGlyph.getGlyphAlign().equals("middle")) {
        //
        // xt = (this.getGlyphWidth() - childGlyph.getGlyphWidth()) / 2;
        //
        // }
        //
        // if (childGlyph.getGlyphVAlign().equals("top")) {
        //
        // yt = childGlyph.getGlyphOffsetTop();
        //
        // } else if (childGlyph.getGlyphVAlign().equals("bottom")) {
        //
        // yt =
        // this.getGlyphHeight() - childGlyph.getGlyphHeight()
        // - childGlyph.getGlyphOffsetBottom();
        //
        // } else if (childGlyph.getGlyphVAlign().equals("center")
        // || childGlyph.getGlyphVAlign().equals("middle")) {
        //
        // yt = (this.getGlyphHeight() - childGlyph.getGlyphHeight()) / 2;
        //
        // }
        //
        // childPlacer.setAttribute("transform", "translate(" + xt + ", " + yt
        // + ")");
        //
        // g.addContent(childPlacer);
        //
        // }

        g.setAttribute("desc", getId());

        return g;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphAlign(java.lang.String)
     */
    public void setGlyphAlign(String align) {
        this.align = align;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphEffect(java.lang.String)
     */
    public void setGlyphEffect(String glyphEffect) {
        this.glyphEffect = glyphEffect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphHeight(double)
     */
    public void setGlyphHeight(double height) {
        this.glyphHeight = height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffset(double)
     */
    public void setGlyphOffset(double glyphOffset) {
        this.glyphOffsetLeft = glyphOffset;
        this.glyphOffsetRight = glyphOffset;
        this.glyphOffsetTop = glyphOffset;
        this.glyphOffsetBottom = glyphOffset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetBottom(double)
     */
    public void setGlyphOffsetBottom(double glyphOffsetBottom) {
        this.glyphOffsetBottom = glyphOffsetBottom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetLeft(double)
     */
    public void setGlyphOffsetLeft(double glyphOffsetLeft) {
        this.glyphOffsetLeft = glyphOffsetLeft;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetRight(double)
     */
    public void setGlyphOffsetRight(double glyphOffsetRight) {
        this.glyphOffsetRight = glyphOffsetRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetTop(double)
     */
    public void setGlyphOffsetTop(double glyphOffsetTop) {
        this.glyphOffsetTop = glyphOffsetTop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphRx(double)
     */
    public void setGlyphRx(double rx) {
        this.rx = rx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphRy(double)
     */
    public void setGlyphRy(double ry) {
        this.ry = ry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphStyle(java.lang.String)
     */
    public void setGlyphStyle(String glyphStyle) {
        this.glyphStyle = glyphStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphVAlign(java.lang.String)
     */
    public void setGlyphVAlign(String vAlign) {
        this.vAlign = vAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphWidth(double)
     */
    public void setGlyphWidth(double width) {
        this.glyphWidth = width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphX(double)
     */
    public void setGlyphX(double glyphX) {
        this.glyphX = glyphX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphY(double)
     */
    public void setGlyphY(double glyphY) {
        this.glyphY = glyphY;
    }

    // public void setParentGlyph(IGlyph parentGlyph) {
    // this.parentGlyph = parentGlyph;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setId(java.lang.String)
     */
    public void setId(String id) {
        this.id = id;
    }

}
