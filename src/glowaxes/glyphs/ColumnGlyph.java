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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class ColumnGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class ColumnGlyph extends SimpleGlyph implements IGlyphLayoutManager {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(ColumnGlyph.class.getName());

    /** The glyphs. */
    @SuppressWarnings("unused")
    private ArrayList<IGlyph> glyphs = new ArrayList<IGlyph>();

    /**
     * Instantiates a new column glyph.
     */
    public ColumnGlyph() {
        setId("columnGlyph");
        this.setGlyphVAlign("top");
    }

    /**
     * Instantiates a new column glyph.
     * <p>
     * Copy constructor http://www.javapractices.com/topic/TopicAction.do?Id=12
     * 
     * @param aColumnGlyph
     *            the a column glyph
     */
    public ColumnGlyph(ColumnGlyph aColumnGlyph) {
        setId("columnGlyph");
        for (int i = 0; i < aColumnGlyph.glyphs.size(); i++) {
            IGlyph myGlyph = aColumnGlyph.glyphs.get(i);
            addGlyph(myGlyph);
        }
        this.setGlyphVAlign(aColumnGlyph.getGlyphVAlign());
        this.setGlyphAlign(aColumnGlyph.getGlyphAlign());
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
        double previousGlyphOffsetBottom = 0;

        this.setGlyphHeight(-1);
        this.setGlyphWidth(-1);
        double xt = 0;
        double yt = 0;

        for (int i = 0; i < glyphs.size(); i++) {

            IGlyph glyph = getGlyph(i);

            // calculate offsets
            if (glyph instanceof IGlyphLayoutManager) {
                ((IGlyphLayoutManager) glyph).calculateDimensions();
            }

            if (glyph.getGlyphWidth() + glyph.getGlyphOffsetLeft()
                    + glyph.getGlyphOffsetRight() > getGlyphWidth()) {
                setGlyphWidth(glyph.getGlyphWidth()
                        + glyph.getGlyphOffsetLeft()
                        + glyph.getGlyphOffsetRight());
            }

            if (getGlyphHeight() == -1) {
                setGlyphHeight(glyph.getGlyphHeight()
                        + glyph.getGlyphOffsetTop()
                        + glyph.getGlyphOffsetBottom());
            } else {
                setGlyphHeight(getGlyphHeight() + glyph.getGlyphHeight());

                if (glyph.getGlyphOffsetTop() >= previousGlyphOffsetBottom) {
                    setGlyphHeight(glyph.getGlyphOffsetTop()
                            - previousGlyphOffsetBottom + getGlyphHeight()
                            + glyph.getGlyphOffsetBottom());
                } else {
                    setGlyphHeight(getGlyphHeight()
                            + glyph.getGlyphOffsetBottom());
                }
            }

            // calculate x, y
            if (i != 0)
                previousGlyphOffsetBottom =
                        glyphs.get(i - 1).getGlyphOffsetBottom();
        }
        previousGlyphOffsetBottom = 0;
        for (int i = 0; i < glyphs.size(); i++) {

            IGlyph glyph = getGlyph(i);
            // calculate x, y
            if (i != 0)
                previousGlyphOffsetBottom =
                        glyphs.get(i - 1).getGlyphOffsetBottom();
            double offset =
                    glyph.getGlyphOffsetTop() > previousGlyphOffsetBottom
                            ? glyph.getGlyphOffsetTop()
                            : previousGlyphOffsetBottom;
            yt = yt + offset;

            // logger.error(glyph.getId());
            // logger.error(glyph.getGlyphAlign());
            // logger.error(glyph.getGlyphWidth());
            // logger.error(getGlyphWidth());
            if (glyph.getGlyphAlign().equals("center")) {
                xt = (getGlyphWidth() - glyph.getGlyphWidth()) / 2;
            } else if (glyph.getGlyphAlign().equals("right")) {
                xt =
                        getGlyphWidth() - glyph.getGlyphWidth()
                                - glyph.getGlyphOffsetRight();
            } else {
                xt = glyph.getGlyphOffsetLeft();
            }

            glyph.setGlyphX(xt);
            glyph.setGlyphY(yt);

            yt = yt + glyph.getGlyphHeight();
        }
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

        Element e = super.renderGlyph();
        for (int i = 0; i < glyphs.size(); i++) {

            IGlyph glyph = glyphs.get(i);
            e.addContent(glyph.renderGlyph());

        }
        return e;
    }
}
