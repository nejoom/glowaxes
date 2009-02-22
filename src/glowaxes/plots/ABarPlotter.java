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
import glowaxes.util.TypeConverter;

import java.util.ArrayList;

/**
 * The Class ABarPlotter.
 */
public abstract class ABarPlotter extends AGeneralPlotter {

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

    public int getAxisCategoricalUnits() {

        int size = 0;
        int numSeries = getData().getGroup().getSeriesList().size();

        for (int i = 0; i < numSeries; i++) {
            int tmpSize = getData().getGroup().getSeries(i).getSize();
            if (tmpSize > size) size = tmpSize;
        }
        return size-1;
    }

    public double getAxisDateRange(long min, long max) {
        int size = 0;
        int numSeries = getData().getGroup().getSize();

        for (int i = 0; i < numSeries; i++) {
            size += getData().getGroup().getSeries(i).getSize();
        }
        double width = (max - min) / (size);
        return width;
    }

    public double getAxisNumericalRange(double min, double max) {
        return (max - min) * 0.1;
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
    
    public double getInitialAxisCategoricalOffset() {

        return 0.5;
    }

    /**
     * Gets the width.
     * 
     * @return width of single data column or bar
     */
    public double getWidth() {

        double width = 5;
        String _width = data.getParentArea().getRenderingWidth();

        if (_width != null) {

            if (_width.indexOf("%") != -1) {

                _width = _width.substring(0, _width.indexOf("%"));
                width = TypeConverter.getDouble(_width, 0d);

                double plottingWidth;
                if (data.getParentArea().getType().indexOf("bar") != -1) {
                    plottingWidth = data.getParentArea().getGlyphHeight();
                } else {
                    plottingWidth = data.getParentArea().getGlyphWidth();
                }
                // 90% actually used?
                plottingWidth = plottingWidth * 90 / 100;

                width = (plottingWidth / getAxisCategoricalUnits()) * width / 100;
            } else {
                width = TypeConverter.getDouble(_width, 0d);

                if (width < 0)
                    width = 5;
            }
        }
        return width;
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
