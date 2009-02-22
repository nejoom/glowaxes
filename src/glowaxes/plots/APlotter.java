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

package glowaxes.plots;

import glowaxes.glyphs.Data;
import glowaxes.glyphs.IGlyph;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class APlotter.
 */
public abstract class APlotter extends AGeneralPlotter {

    /** The data. */
    private Data data;

    /** The glyphs. */
    private ArrayList<IGlyph> glyphs = new ArrayList<IGlyph>();

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#addGlyph(glowaxes.glyphs.IGlyph)
     */
    public void addGlyph(IGlyph glyph) {
        glyphs.add(glyph);
    }

    public double getAxisDateRange(long min, long max) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#getData()
     */
    public Data getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#getGlyphs()
     */
    public ArrayList<IGlyph> getGlyphs() {
        return glyphs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#setData(glowaxes.glyphs.Data)
     */
    public void setData(Data data) {
        this.data = data;
    }

}
