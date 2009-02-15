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
 * terms of the Elements End-User License Agreeement (the Elements License), in
 * which case the provisions of the Elements License are applicable instead of
 * those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */

package glowaxes.data;

import glowaxes.tags.Constants;
import glowaxes.tags.Defs;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * <p>
 * The Series class represents a Series of {@link Value}s in a svg chart (see
 * also {@link Value}, or {@link Group} for an overview). Using these three
 * concepts of a Group containing one or more Series and a Series containing one
 * or more values; things get visually represented in a chart (eg Legend of a
 * Series, svg Style/ svg predefined Shapes).
 * </p>
 * <p>
 * Multiple <code>Series</code> form a {@link Group}. This class contains
 * methods to manage a <code>Series</code>.
 * <p>
 * 
 * @see Group Group for more details
 * @version ${version}, 5-jun-2006
 * @author Eddie Moojen
 * @since ${since_tag}
 */
public class Series {

    /** The logger. */
    private static Logger logger = Logger.getLogger(Series.class.getName());

    /** The a1 max. */
    private Double a1Max;

    /** The a1 min. */
    private Double a1Min;

    /** The a1 total. */
    private double a1Total;

    /** The a2 max. */
    private Double a2Max;

    /** The a2 min. */
    private Double a2Min;

    /** The a2 total. */
    private double a2Total;

    /** The b1 max. */
    private Double b1Max;

    /** The b1 min. */
    private Double b1Min;

    /** The b1 total. */
    private double b1Total;

    /** The b2 max. */
    private Double b2Max;

    /** The b2 min. */
    private Double b2Min;

    /** The b2 total. */
    private double b2Total;

    /** The default label. */
    private Element defaultLabel;

    /** The default label background style. */
    private String defaultLabelBackgroundStyle =
            Defs.parseStyle(Constants.getDefault("labels.background.style"));

    /** The default label style. */
    private String defaultLabelStyle;

    /** The default shape. */
    private String defaultShape = "dot";

    /** The default style. */
    private String defaultStyle;

    /** The id. */
    private String id = "";

    /** The index. */
    private int index;

    /** The legend label. */
    private String legendLabel;

    /** The line style. */
    private String lineStyle = null;

    /** The value series. */
    private final ArrayList<Value> valueSeries = new ArrayList<Value>();

    /** The x max. */
    private Double xMax;

    /** The x min. */
    private Double xMin;

    /** The x total. */
    private double xTotal;

    /** The y max. */
    private Double yMax;

    /** The y min. */
    private Double yMin;

    /** The y total. */
    private double yTotal;

    /** The z max. */
    private Double zMax;

    /** The z min. */
    private Double zMin;

    /** The z total. */
    private double zTotal;

    /** The map. */
    private HashMap<String, Object>[] map;

    /**
     * Constructs a newly allocated <code>Series</code> object that represents
     * a Series of <code>Value</code>s in a svg chart.
     */
    public Series() {
    }

    /**
     * This will add the Value to the Series. The Series is backed by an
     * ArrayList. The max and min values are calculated.
     * 
     * @param value
     *            <code>{@link Value}</code> the value to add to the Series.
     */
    public void addValue(Value value) {

        valueSeries.add(value);
        value.setParentSeries(this);
    }

    /**
     * This will return the maximum a1 in the Series.
     * 
     * @return The <code>Double</code> maximum a1 value in this Series.
     */
    public Double getA1Max() {
        return a1Max;
    }

    /**
     * This will return the minimum a1 in the Series.
     * 
     * @return The <code>Double</code> minimum a1 value in this Series.
     */
    public Double getA1Min() {
        return a1Min;
    }

    /**
     * This will return the total for a1 in the Series.
     * 
     * @return The <code>Double</code> total of a1 values in this Series.
     */
    public Double getA1Total() {
        return a1Total;
    }

    /**
     * This will return the maximum a2 in the Series.
     * 
     * @return The <code>Double</code> maximum a2 value in this Series.
     */
    public Double getA2Max() {
        return a2Max;
    }

    /**
     * This will return the minimum a2 in the Series.
     * 
     * @return The <code>Double</code> minimum a2 value in this Series.
     */
    public Double getA2Min() {
        return a2Min;
    }

    /**
     * This will return the total for a2 in the Series.
     * 
     * @return The <code>Double</code> total of a2 values in this Series.
     */
    public Double getA2Total() {
        return a2Total;
    }

    /**
     * This will return a list of value of this Series as an ArrayList.
     * 
     * @return <code>ArrayList</code> - the value list as an ArrayList.
     */
    public ArrayList<Value> getArrayList() {
        return this.valueSeries;
    }

    /**
     * This will return the maximum b1 in the Series.
     * 
     * @return The <code>Double</code> maximum b1 value in this Series.
     */
    public Double getB1Max() {
        return b1Max;
    }

    /**
     * This will return the minimum b1 in the Series.
     * 
     * @return The <code>Double</code> minimum b1 value in this Series.
     */
    public Double getB1Min() {
        return b1Min;
    }

    /**
     * This will return the total for b1 in the Series.
     * 
     * @return The <code>Double</code> total of b1 values in this Series.
     */
    public Double getB1Total() {
        return b1Total;
    }

    /**
     * This will return the maximum b2 in the Series.
     * 
     * @return The <code>Double</code> maximum b2 value in this Series.
     */
    public Double getB2Max() {
        return b2Max;
    }

    /**
     * This will return the minimum b2 in the Series.
     * 
     * @return The <code>Double</code> minimum b2 value in this Series.
     */
    public Double getB2Min() {
        return b2Min;
    }

    /**
     * This will return the total for b2 in the Series.
     * 
     * @return The <code>Double</code> total of b2 values in this Series.
     */
    public Double getB2Total() {
        return b2Total;
    }

    /**
     * Returns the default label for the values in this Series (if a value's
     * getLabel is null).
     * 
     * @return <code>Element</code> default label in this Series.
     */
    public Element getDefaultLabel() {
        return defaultLabel;
    }

    /**
     * This will return the default label background svg style for this Series.
     * 
     * @return The <code>String</code> defaultLabelBackgroundStyle for this
     *         Series.
     */
    public String getDefaultLabelBackgroundStyle() {
        return defaultLabelBackgroundStyle;
    }

    /**
     * This will return the default label svg style for this Series.
     * 
     * @return The <code>String</code> defaultLabelStyle for this Series.
     */
    public String getDefaultLabelStyle() {
        return defaultLabelStyle;
    }

    /**
     * This will return the default svg shape element for this Series.
     * 
     * @return <code>String</code> default svg shape element for this Series.
     */
    public String getDefaultShape() {
        return defaultShape;
    }

    /**
     * This will return the default label svg style for this Series.
     * 
     * @return <code>String</code> the default label svg style.
     */
    public String getDefaultShapeStyle() {
        return defaultStyle;
    }

    /**
     * This will return the id for this Series.
     * 
     * @return The <code>String</code> id for this Series.
     */
    public String getId() {
        return id;
    }

    /**
     * This will return the index of this Series in its Group.
     * 
     * @return <code>int</code> the index of this Series in its Group.
     */
    public int getIndex() {
        return index;
    }

    /**
     * This will return the legend for this Series.
     * 
     * @return The <code>String</code> legend for this Series.
     */
    public String getLegend() {
        return legendLabel;
    }

    /**
     * This will return the default line style for the values in this Series (if
     * values make a line).
     * 
     * @return <code>String</code> default line style in this Series.
     */
    public String getLineStyle() {

        if (lineStyle == null) {
            lineStyle = defaultStyle;
        }
        return lineStyle;
    }

    /**
     * Gets the hash map array.
     * 
     * @return the hash map array
     */
    public HashMap<String, Object>[] getHashMapArray() {
        return map;
    }

    /**
     * This will return the number of values in this Series.
     * 
     * @return <code>int</code> the number of values in this Series.
     */
    public int getSize() {
        return valueSeries.size();
    }

    /**
     * Get a {@link Value} from the <code>Series</code> given an index.
     * 
     * @param index
     *            the indexed spot of the Series (internalized by a
     *            {@link java.util.ArrayList}).
     * 
     * @return the {@link Value} at indexed spot <code>index</code>.
     */
    public Value getValue(int index) {
        return valueSeries.get(index);
    }

    /**
     * This will return the maximum x in the Series.
     * 
     * @return The <code>Double</code> maximum x value in this Series.
     */
    public Double getXMax() {
        return xMax;
    }

    /**
     * This will return the minimum x in the Series.
     * 
     * @return The <code>Double</code> minimum x value in this Series.
     */
    public Double getXMin() {
        return xMin;
    }

    /**
     * This will return the total for x in the Series.
     * 
     * @return The <code>Double</code> total of x values in this Series.
     */
    public Double getXTotal() {
        return xTotal;
    }

    /**
     * This will return the maximum y in the Series.
     * 
     * @return The <code>Double</code> maximum y value in this Series.
     */
    public Double getYMax() {
        return yMax;
    }

    /**
     * This will return the minimum y in the Series.
     * 
     * @return The <code>Double</code> minimum y value in this Series.
     */
    public Double getYMin() {
        return yMin;
    }

    /**
     * This will return the total for y in the Series.
     * 
     * @return The <code>Double</code> total of y values in this Series.
     */
    public Double getYTotal() {
        return yTotal;
    }

    /**
     * This will return the maximum z in the Series.
     * 
     * @return The <code>Double</code> maximum z value in this Series.
     */
    public Double getZMax() {
        return zMax;
    }

    /**
     * This will return the minimum z in the Series.
     * 
     * @return The <code>Double</code> minimum z value in this Series.
     */
    public Double getZMin() {
        return zMin;
    }

    /**
     * This will set the default label for the values in this Series.
     * 
     * @param _defaultLabel
     *            default label for the values in this Series.
     */
    public void setDefaultLabel(Element _defaultLabel) {
        defaultLabel = _defaultLabel;
    }

    /**
     * This will set the default label background style for the values label in
     * this Series.
     * 
     * @param defaultLabelBackgroundStyle
     *            default label background style the values in this Series.
     */
    public void setDefaultLabelBackgroundStyle(
            String defaultLabelBackgroundStyle) {

        if (defaultLabelBackgroundStyle != null)
            this.defaultLabelBackgroundStyle =
                    Defs.parseStyle(defaultLabelBackgroundStyle);
    }

    /**
     * This will set the default label svg style for this Series.
     * 
     * @param _defaultLabelStyle
     *            the default label svg style for this Series.
     */
    public void setDefaultLabelStyle(String _defaultLabelStyle) {

        if (_defaultLabelStyle != null)
            defaultLabelStyle = Defs.parseStyle(_defaultLabelStyle);
    }

    /**
     * This will set the default svg shape element for this Series.
     * 
     * @param _defaultShape
     *            the default the default svg shape element for this Series used
     *            to plot values.
     */
    public void setDefaultShape(String _defaultShape) {
        defaultShape = _defaultShape;
    }

    /**
     * This will set the default shape style for this Series.
     * 
     * @param _defaultStyle
     *            the default shape svg style for this Series used to plot
     *            values.
     */
    public void setDefaultShapeStyle(String _defaultStyle) {
        defaultStyle = Defs.parseStyle(_defaultStyle);
    }

    /**
     * This will set the default label for the values in this Series.
     * 
     * @param _Id
     *            set the id, identifying this class for this Series. id does
     *            not have to be unique.
     */
    public void setId(String _Id) {
        id = _Id;
    }

    /**
     * This will set the index of this Series in its Group.
     * 
     * @param index
     *            the index
     */
    protected void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the hash map array.
     * 
     * @param map
     *            the map
     */
    protected void setHashMapArray(HashMap<String, Object>[] map) {
        this.map = map;
    }

    /**
     * This will set the legend for this Series.
     * 
     * @param _legendLabel
     *            the legend for this Series
     */
    public void setLegend(String _legendLabel) {
        legendLabel = _legendLabel;
    }

    /**
     * This will set the line style for this Series.
     * 
     * @param _lineStyle
     *            the style for lines making this Series
     */
    public void setLineStyle(String _lineStyle) {
        lineStyle = Defs.parseStyle(_lineStyle);
    }

    /**
     * Sets the ranges.
     */
    protected void setRanges() {

        if (logger.isDebugEnabled())
            logger.debug("setRanges");

        for (int i = 0; i < valueSeries.size(); i++) {
            setRanges(getValue(i));
        }

    }

    /**
     * Sets the ranges.
     * 
     * @param value
     *            the new ranges
     */
    private void setRanges(Value value) {

        if (logger.isDebugEnabled())
            logger.debug("value: " + value);

        if (value == null)
            return;

        // toggle mins and max's for this value
        if (value.getXObject() != null
                && (xMin == null || value.getX() < xMin.doubleValue()))
            xMin = new Double(value.getX());
        if (value.getXObject() != null
                && (xMax == null || value.getX() > xMax.doubleValue()))
            xMax = new Double(value.getX());
        if (value.getYObject() != null
                && (yMin == null || value.getY() < yMin.doubleValue()))
            yMin = new Double(value.getY());
        if (value.getYObject() != null
                && (yMax == null || value.getY() > yMax.doubleValue()))
            yMax = new Double(value.getY());
        if (value.getZObject() != null
                && (zMin == null || value.getZ() < zMin.doubleValue()))
            zMin = new Double(value.getZ());
        if (value.getZObject() != null
                && (zMax == null || value.getZ() > zMax.doubleValue()))
            zMax = new Double(value.getZ());

        if (a1Min == null || value.getA1() < a1Min.doubleValue())
            a1Min = new Double(value.getA1());
        if (a1Max == null || value.getA1() > a1Max.doubleValue())
            a1Max = new Double(value.getA1());
        if (a2Min == null || value.getA2() < a2Min.doubleValue())
            a2Min = new Double(value.getA2());
        if (a2Max == null || value.getA2() > a2Max.doubleValue())
            a2Max = new Double(value.getA2());
        if (b1Min == null || value.getB1() < b1Min.doubleValue())
            b1Min = new Double(value.getB1());
        if (b1Max == null || value.getB1() > b1Max.doubleValue())
            b1Max = new Double(value.getB1());
        if (b2Min == null || value.getB2() < b2Min.doubleValue())
            b2Min = new Double(value.getB2());
        if (b2Max == null || value.getB2() > b2Max.doubleValue())
            b2Max = new Double(value.getB2());
        xTotal += value.getX();
        yTotal += value.getY();
        zTotal += value.getZ();

        a1Total += value.getA1();
        a2Total += value.getA2();
        b1Total += value.getB1();
        b2Total += value.getB2();

    }

    /**
     * This will return the string representation of this object (for debug).
     * 
     * @return <code>String</code> debug Series.
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        final String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {");

        result.append(newLine);
        result.append(" Series: ");
        result.append(newLine);

        result.append(" xmin: ");
        result.append(getXMin());
        result.append(newLine);

        result.append(" xmax: ");
        result.append(getXMax());
        result.append(newLine);

        result.append(" ymin: ");
        result.append(getYMin());
        result.append(newLine);

        result.append(" ymax: ");
        result.append(getYMax());
        result.append(newLine);

        result.append(" ytotal: ");
        result.append(getYTotal());
        result.append(newLine);

        result.append(" defaultLabelStyle: ");
        result.append(getDefaultLabelStyle());
        result.append(newLine);

        result.append(" defaultShapeStyle: ");
        result.append(getDefaultShapeStyle());
        result.append(newLine);

        result.append(" lineStyle: ");
        result.append(getLineStyle());
        result.append(newLine);

        result.append(" defaultLabel: ");
        result.append(getDefaultLabel());
        result.append(newLine);

        for (int i = 0; i < getSize(); i++) {
            result.append(this.getValue(i).toString());
            result.append(newLine);
        }

        result.append("}");
        return result.toString();

    }

}
