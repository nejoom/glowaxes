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

import glowaxes.data.Value;
import glowaxes.glyphs.Data;
import glowaxes.glyphs.IGlyph;

import java.util.ArrayList;

import org.jdom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Interface IPlotter.
 */
public interface IPlotter {

    /**
     * Adds the glyph.
     * 
     * @param glyph
     *            the glyph
     */
    public abstract void addGlyph(IGlyph glyph);

    /**
     * Draw value.
     * 
     * @param value
     *            the value
     * 
     * @return the element
     */
    public abstract Element drawValue(Value value);

    public abstract double getAxisCategoricalOffset(Object object, int series);

    /**
     * Gets the range of the axis in categorical units.
     * 
     * @return the factor to scale units by
     */
    public abstract int getAxisCategoricalUnits();

    /**
     * Gets the range of the axis in date units.
     * 
     * @return the range
     */
    public abstract double getAxisDateRange(long min, long max);

    /**
     * Gets the range of the axis in numerical units.
     * 
     * @return the factor to multiply units by
     */
    public abstract double getAxisNumericalRange(double min, double max);
    /**
     * Gets the data.
     * 
     * @return the data
     */
    public abstract Data getData();

    /**
     * Gets the glyphs.
     * 
     * @return the glyphs
     */
    public abstract ArrayList<IGlyph> getGlyphs();

    public abstract double getInitialAxisCategoricalOffset();

    public abstract double getXAxisCategoryIndex(Object object, int series);

    public abstract double getYAxisCategoryIndex(Object object, int series);

    /**
     * Prepare data.
     */
    public abstract void prepareData();

    /**
     * Process labels.
     */
    public abstract void processLabels();

    /**
     * Render.
     * 
     * @return the element
     */
    public abstract Element render();

    /**
     * Sets up the variables for x and y categories.
     * 
     */
    public abstract void setAxisCategories();

    /**
     * Sets the data.
     * 
     * @param data
     *            the new data
     */
    public abstract void setData(Data data);

}
