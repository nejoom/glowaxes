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

/**
 * The Interface ITextGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public interface ITextGlyph extends IGlyph {

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public String getText();

    /**
     * Gets the text effect.
     * 
     * @return the text effect
     */
    public String getTextEffect();

    /**
     * Gets the text height.
     * 
     * @return the text height
     */
    public double getTextHeight();

    /**
     * Gets the text style.
     * 
     * @return the text style
     */
    public String getTextStyle();

    /**
     * Gets the text width.
     * 
     * @return the text width
     */
    public double getTextWidth();

    /**
     * Gets the text x.
     * 
     * @return the text x
     */
    public double getTextX();

    /**
     * Gets the text y.
     * 
     * @return the text y
     */
    public double getTextY();

    /**
     * Sets the text.
     * 
     * @param text
     *            the new text
     */
    public void setText(String text);

    /**
     * Sets the text effect.
     * 
     * @param textEffect
     *            the new text effect
     */
    public void setTextEffect(String textEffect);

    /**
     * Sets the text offset bottom.
     * 
     * @param offsetBottom
     *            the new text offset bottom
     */
    public void setTextOffsetBottom(double offsetBottom);

    /**
     * Sets the text offset left.
     * 
     * @param offsetLeft
     *            the new text offset left
     */
    public void setTextOffsetLeft(double offsetLeft);

    /**
     * Sets the text offset right.
     * 
     * @param offsetRight
     *            the new text offset right
     */
    public void setTextOffsetRight(double offsetRight);

    /**
     * Sets the text offset top.
     * 
     * @param offsetTop
     *            the new text offset top
     */
    public void setTextOffsetTop(double offsetTop);

    /**
     * Sets the text rotation.
     * 
     * @param textRotation
     *            the new text rotation
     */
    public void setTextRotation(double textRotation);

    /**
     * Sets the text style.
     * 
     * @param textStyle
     *            the new text style
     */
    public void setTextStyle(String textStyle);

}
