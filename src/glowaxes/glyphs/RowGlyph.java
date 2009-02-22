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
 * The Class RowGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class RowGlyph extends SimpleGlyph implements IGlyphLayoutManager {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(RowGlyph.class.getName());

    /** The glyphs. */
    @SuppressWarnings("unused")
    private ArrayList<IGlyph> glyphs = new ArrayList<IGlyph>();

    /**
     * Instantiates a new row glyph.
     */
    public RowGlyph() {
        setId("rowGlyph");
        setGlyphOffsetTop(0);
        setGlyphOffsetBottom(2);
        setGlyphOffsetLeft(0);
        setGlyphOffsetRight(2);
    }

    /*
     * Copy constructor http://www.javapractices.com/topic/TopicAction.do?Id=12
     */
    /**
     * Instantiates a new row glyph.
     * 
     * @param aRowGlyph
     *            the a row glyph
     */
    public RowGlyph(RowGlyph aRowGlyph) {
        setId("rowGlyph");
        for (int i = 0; i < aRowGlyph.glyphs.size(); i++) {
            IGlyph myGlyph = aRowGlyph.glyphs.get(i);
            addGlyph(myGlyph);
        }
        this.setGlyphVAlign(aRowGlyph.getGlyphVAlign());
        this.setGlyphAlign(aRowGlyph.getGlyphAlign());
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
        double xt = 0;
        double yt = 0;

        double previousGlyphOffsetRight = 0;
        for (int i = 0; i < glyphs.size(); i++) {

            IGlyph glyph = getGlyph(i);

            // calculate offsets
            if (glyph instanceof IGlyphLayoutManager) {
                ((IGlyphLayoutManager) glyph).calculateDimensions();
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
            if (getGlyphWidth() == -1) {

                setGlyphWidth(glyph.getGlyphOffsetLeft()
                        + glyph.getGlyphWidth() + glyph.getGlyphOffsetRight());

            } else {

                if (glyph.getGlyphOffsetLeft() >= previousGlyphOffsetRight) {
                    setGlyphWidth(getGlyphWidth() - previousGlyphOffsetRight
                            + glyph.getGlyphOffsetLeft()
                            + glyph.getGlyphWidth()
                            + glyph.getGlyphOffsetRight());
                } else {
                    setGlyphWidth(getGlyphWidth() + glyph.getGlyphWidth()
                            + glyph.getGlyphOffsetRight());
                }

            }

            // calculate x, y
            if (i != 0) {
                previousGlyphOffsetRight =
                        glyphs.get(i - 1).getGlyphOffsetRight();
            }

        }
        previousGlyphOffsetRight = 0;
        for (int i = 0; i < glyphs.size(); i++) {

            IGlyph glyph = getGlyph(i);

            // calculate x, y
            if (i != 0) {
                previousGlyphOffsetRight =
                        glyphs.get(i - 1).getGlyphOffsetRight();
            }
            double offset =
                    glyph.getGlyphOffsetLeft() > previousGlyphOffsetRight
                            ? glyph.getGlyphOffsetLeft()
                            : previousGlyphOffsetRight;
            xt = xt + offset;

            if (glyph.getGlyphVAlign().equals("center")
                    || glyph.getGlyphVAlign().equals("middle")) {

                yt = (getGlyphHeight() - glyph.getGlyphHeight()) / 2;
            } else if (glyph.getGlyphVAlign().equals("bottom")) {
                yt =
                        getGlyphHeight() - glyph.getGlyphHeight()
                                - glyph.getGlyphOffsetBottom();
            } else {
                yt = glyph.getGlyphOffsetTop();
            }

            glyph.setGlyphX(xt);
            glyph.setGlyphY(yt);

            xt = xt + glyph.getGlyphWidth();

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
