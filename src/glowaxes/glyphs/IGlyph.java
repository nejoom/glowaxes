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

import org.jdom.Element;

/**
 * <code>IGlyph</code> provides a contract for the glyphs in a two dimensional
 * coordinate system.
 * <p>
 * The coordinate nomenclature is from standard geometry: (x, y).
 * </p>
 * <p>
 * Most glyph implement the IGlyph interface by extending {@link SimpleGlyph} or
 * {@link TextGlyph}, which represent a simple filled rectangle on the chart,
 * or multi-lined text.
 * 
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public interface IGlyph {

    /**
     * Gets the glyph align.
     * 
     * @return the glyph align
     */
    public String getGlyphAlign();

    /**
     * Gets the glyph effect.
     * 
     * @return the glyph effect
     */
    public String getGlyphEffect();

    /**
     * Gets the glyph height.
     * 
     * @return the glyph height
     */
    public double getGlyphHeight();

    /**
     * Gets the glyph offset bottom.
     * 
     * @return the glyph offset bottom
     */
    public double getGlyphOffsetBottom();

    /**
     * Gets the glyph offset left.
     * 
     * @return the glyph offset left
     */
    public double getGlyphOffsetLeft();

    /**
     * Gets the glyph offset right.
     * 
     * @return the glyph offset right
     */
    public double getGlyphOffsetRight();

    /**
     * Gets the glyph offset top.
     * 
     * @return the glyph offset top
     */
    public double getGlyphOffsetTop();

    /**
     * Gets the glyph rx.
     * 
     * @return the glyph rx
     */
    public double getGlyphRx();

    /**
     * Gets the glyph ry.
     * 
     * @return the glyph ry
     */
    public double getGlyphRy();

    /**
     * Gets the glyph style.
     * 
     * @return the glyph style
     */
    public String getGlyphStyle();

    /**
     * Gets the glyph v align.
     * 
     * @return the glyph v align
     */
    public String getGlyphVAlign();

    /**
     * Gets the glyph width.
     * 
     * @return the glyph width
     */
    public double getGlyphWidth();

    /**
     * Gets the glyph x.
     * 
     * @return the glyph x
     */
    public double getGlyphX();

    /**
     * Gets the glyph y.
     * 
     * @return the glyph y
     */
    public double getGlyphY();

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId();

    /**
     * Render glyph.
     * 
     * @return the element
     */
    public Element renderGlyph();

    /**
     * Sets the glyph align.
     * 
     * @param align
     *            the new glyph align
     */
    public void setGlyphAlign(String align);

    /**
     * Sets the glyph effect.
     * 
     * @param glyphEffect
     *            the new glyph effect
     */
    public void setGlyphEffect(String glyphEffect);

    /**
     * Sets the glyph height.
     * 
     * @param height
     *            the new glyph height
     */
    public void setGlyphHeight(double height);

    /**
     * Sets the glyph offset.
     * 
     * @param glyphOffset
     *            the new glyph offset
     */
    public void setGlyphOffset(double glyphOffset);

    /**
     * Sets the glyph offset bottom.
     * 
     * @param glyphOffsetBottom
     *            the new glyph offset bottom
     */
    public void setGlyphOffsetBottom(double glyphOffsetBottom);

    /**
     * Sets the glyph offset left.
     * 
     * @param glyphOffsetLeft
     *            the new glyph offset left
     */
    public void setGlyphOffsetLeft(double glyphOffsetLeft);

    /**
     * Sets the glyph offset right.
     * 
     * @param glyphOffsetRight
     *            the new glyph offset right
     */
    public void setGlyphOffsetRight(double glyphOffsetRight);

    /**
     * Sets the glyph offset top.
     * 
     * @param glyphOffsetTop
     *            the new glyph offset top
     */
    public void setGlyphOffsetTop(double glyphOffsetTop);

    /**
     * Sets the glyph rx.
     * 
     * @param rx
     *            the new glyph rx
     */
    public void setGlyphRx(double rx);

    /**
     * Sets the glyph ry.
     * 
     * @param ry
     *            the new glyph ry
     */
    public void setGlyphRy(double ry);

    /**
     * Sets the glyph style.
     * 
     * @param glyphStyle
     *            the new glyph style
     */
    public void setGlyphStyle(String glyphStyle);

    /**
     * Sets the glyph v align.
     * 
     * @param vAlign
     *            the new glyph v align
     */
    public void setGlyphVAlign(String vAlign);

    /**
     * Sets the glyph width.
     * 
     * @param width
     *            the new glyph width
     */
    public void setGlyphWidth(double width);

    /**
     * Sets the glyph x.
     * 
     * @param glyphX
     *            the new glyph x
     */
    public void setGlyphX(double glyphX);

    /**
     * Sets the glyph y.
     * 
     * @param glyphY
     *            the new glyph y
     */
    public void setGlyphY(double glyphY);

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(String id);
}