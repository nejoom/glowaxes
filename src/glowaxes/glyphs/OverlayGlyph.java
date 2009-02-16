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
 * Based on commercial needs the contents of this file may be used under the
 * terms of the Elements End-User License Agreement (the Elements License), in
 * which case the provisions of the Elements License are applicable instead of
 * those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */
package glowaxes.glyphs;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class OverlayGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class OverlayGlyph extends SimpleGlyph implements IGlyphLayoutManager {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(OverlayGlyph.class.getName());

    /** The glyphs. */
    private ArrayList<IGlyph> glyphs = new ArrayList<IGlyph>();

    /**
     * Instantiates a new overlay glyph.
     */
    public OverlayGlyph() {
        setId("overlayGlyph");
        this.setGlyphVAlign("top");
    }

    /**
     * Instantiates a new overlay glyph. Copy constructor
     * http://www.javapractices.com/topic/TopicAction.do?Id=12
     * 
     * 
     * @param aHolderGlyph
     *            the a holder glyph
     */
    public OverlayGlyph(OverlayGlyph aHolderGlyph) {
        setId("overlayGlyph");
        for (int i = 0; i < aHolderGlyph.glyphs.size(); i++) {
            IGlyph myGlyph = aHolderGlyph.glyphs.get(i);
            addGlyph(myGlyph);
        }
        this.setGlyphVAlign(aHolderGlyph.getGlyphVAlign());
        this.setGlyphAlign(aHolderGlyph.getGlyphAlign());
        this.calculateDimensions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyphLayoutManager#addGlyph(glowaxes.glyphs.IGlyph)
     */
    public void addGlyph(IGlyph glyph) {

        glyphs.add(glyph);

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyphLayoutManager#calculateDimensions()
     */
    public void calculateDimensions() {
        // logger.error("calculateDimensions.");

        this.setGlyphHeight(-1);
        this.setGlyphWidth(-1);

        for (int i = 0; i < glyphs.size(); i++) {
            IGlyph glyph = getGlyph(i);

            if (glyph instanceof IGlyphLayoutManager) {
                ((IGlyphLayoutManager) glyph).calculateDimensions();
            }

            if (glyph.getGlyphWidth() > getGlyphWidth()) {

                setGlyphWidth(glyph.getGlyphWidth());

            }
            if (glyph.getGlyphOffsetLeft() > getGlyphOffsetLeft()) {

                setGlyphOffsetLeft(glyph.getGlyphOffsetLeft());

            }
            if (glyph.getGlyphOffsetRight() > getGlyphOffsetRight()) {

                setGlyphOffsetRight(glyph.getGlyphOffsetRight());

            }

            if (glyph.getGlyphHeight() > getGlyphHeight()) {

                setGlyphHeight(glyph.getGlyphHeight());

            }
            if (glyph.getGlyphOffsetBottom() > getGlyphOffsetBottom()) {

                setGlyphOffsetBottom(glyph.getGlyphOffsetBottom());

            }
            if (glyph.getGlyphOffsetTop() > getGlyphOffsetTop()) {

                setGlyphOffsetTop(glyph.getGlyphOffsetTop());

            }

        }
        // logger.error("getGlyphWidth(): " + getGlyphWidth());
        // logger.error("getGlyphHeight(): " + getGlyphHeight());
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyphLayoutManager#getGlyph(int)
     */
    public IGlyph getGlyph(int i) {
        return glyphs.get(i);
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyphLayoutManager#getSize()
     */
    public int getSize() {
        return glyphs.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#renderGlyph()
     */
    public Element renderGlyph() {

        Element glyph = super.renderGlyph();

        for (int i = 0; i < glyphs.size(); i++) {

            IGlyph myGlyph = glyphs.get(i);
            // myGlyph.setGlyphX(getGlyphX());
            // myGlyph.setGlyphY(getGlyphY());
            glyph.addContent(myGlyph.renderGlyph());

        }
        return glyph;
    }
}
