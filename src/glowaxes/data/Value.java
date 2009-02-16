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

package glowaxes.data;

import glowaxes.tags.Defs;
import glowaxes.util.TypeConverter;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * <p>
 * The Value class represents a Value in a svg chart (see also {@link Series},
 * or {@link Group} for an overview). Using these three concepts Values can be
 * visually represented in a chart (eg Label of a Value).
 * </p>
 * <p>
 * Multiple <code>Value</code>s form a {@link Series}. This class contains
 * methods to manage a <code>Value</code>s.
 * <p>
 * 
 * @see Group Group for more details
 * @version ${version}, 5-jun-2006
 * @author Eddie Moojen
 * @since ${since_tag}
 */
public class Value {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Value.class.getName());

    /** The a1. */
    private Object a1;

    /** The a2. */
    private Object a2;

    /** The b1. */
    private Object b1;

    /** The b2. */
    private Object b2;

    /** The label. */
    private Element label;

    /** The label background style. */
    private String labelBackgroundStyle;

    /** The label events. */
    private String labelEvents;

    /** The label href. */
    private String labelHref;

    /** The label style. */
    private String labelStyle;

    /** The series. */
    private Series series;

    /** The shape. */
    private String shape = "dot";

    /** The spot coordinate. */
    private Object spot;

    /** The style. */
    private String style;

    /** The x coordinate. */
    private Object x;

    /** The y coordinate. */
    private Object y;

    /** The z coordinate. */
    private Object z;

    /**
     * Constructs a newly allocated <code>Value</code> object which represents
     * a <code>Value</code>s in a svg chart.
     */
    public Value() {
    }

    /**
     * This will return the a1 double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> a1 coordinate of the Value.
     */
    public double getA1() {
        return TypeConverter.getDouble(a1, 0d);
    }

    /**
     * This will return the a2 double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> a2 coordinate of the Value.
     */
    public double getA2() {
        return TypeConverter.getDouble(a2, 0d);
    }

    /**
     * This will return the b1 double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> b1 coordinate of the Value.
     */
    public double getB1() {
        return TypeConverter.getDouble(b1, 0d);
    }

    /**
     * This will return the b2 double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> b2 coordinate of the Value.
     */
    public double getB2() {
        return TypeConverter.getDouble(b2, 0d);
    }

    /**
     * This will return the label for the Value.
     * 
     * @return The <code>Element</code> label for the Value.
     */
    public Element getLabel() {

        if (label == null) {
            return this.getParentSeries().getDefaultLabel();
        } else {
            return label;
        }
    }

    /**
     * This will return the label's background style of the Value.
     * 
     * @return The <code>String</code> label's background style of the Value.
     */
    public String getLabelBackgroundStyle() {
        if (labelBackgroundStyle == null) {
            return this.getParentSeries().getDefaultLabelBackgroundStyle();
        } else {
            return labelBackgroundStyle;
        }
    }

    /**
     * This will return the label's Events event of the Value's tool tip.
     * 
     * @return the label's Events event of the Value's tool tip.
     */
    public String getLabelEvents() {
        return labelEvents;
    }

    /**
     * This will return the label's href of the Value's tool tip.
     * 
     * @return the label's href of the Value's tool tip.
     */
    public String getLabelHref() {
        return labelHref;
    }

    /**
     * This will return the label's style of the Value.
     * 
     * @return The <code>String</code> label's style of the Value.
     */
    public String getLabelStyle() {
        if (labelStyle == null) {
            return this.getParentSeries().getDefaultLabelStyle();
        } else {
            return labelStyle;
        }
    }

    /**
     * This will return the Series in which this Value is listed.
     * 
     * @return The <code>Series</code> in which this Value is listed.
     */
    public Series getParentSeries() {
        return series;
    }

    /**
     * This will return the shape of the Value.
     * 
     * @return The <code>String</code> the shape of the Value.
     */
    public String getShape() {
        if (shape == null) {
            return this.getParentSeries().getDefaultShape();
        } else {
            return shape;
        }
    }

    /**
     * This will return the shape's style of the Value.
     * 
     * @return The <code>String</code> the shape's style of the Value.
     */
    public String getShapeStyle() {
        if (style == null) {
            return this.getParentSeries().getDefaultShapeStyle();
        } else {
            return style;
        }

    }

    /**
     * This will return the spot of the Value.
     * 
     * @return The <code>Object</code> the spot of the Value.
     */
    public Object getSpot() {
        return spot;
    }

    /**
     * This will return the x double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> x coordinate of the Value.
     */
    public double getX() {
        return TypeConverter.getDouble(x, 0d);
    }

    /**
     * This will return the x coordinate of the Value as an Object.
     * 
     * @return The <code>Object</code> x coordinate of the Value.
     */
    public Object getXObject() {
        return x;
    }

    /**
     * This will return the y double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> y coordinate of the Value.
     */
    public double getY() {
        return TypeConverter.getDouble(y, 0d);
    }

    /**
     * This will return the y coordinate of the Value as an Object.
     * 
     * @return The <code>Object</code> y coordinate of the Value.
     */
    public Object getYObject() {
        return y;
    }

    /**
     * This will return the z double coordinate of the Value. A null value will
     * return zero.
     * 
     * @return The <code>double</code> z coordinate of the Value.
     */
    public double getZ() {
        return TypeConverter.getDouble(z, 0d);
    }

    /**
     * This will return the z coordinate of the Value as an Object.
     * 
     * @return The <code>Object</code> z coordinate of the Value.
     */
    public Object getZObject() {
        return z;
    }

    /**
     * This will set the a1 coordinate for this Value.
     * 
     * @param _a1
     *            <code>Object</code> the _a1 coordinate for this Value.
     */
    public void setA1(Object _a1) {
        a1 = _a1;
    }

    /**
     * This will set the a2 coordinate for this Value.
     * 
     * @param _a2
     *            <code>Object</code> the _a2 coordinate for this Value.
     */
    public void setA2(Object _a2) {
        a2 = _a2;
    }

    /**
     * This will set the b1 coordinate for this Value.
     * 
     * @param _b1
     *            <code>Object</code> the _b1 coordinate for this Value.
     */
    public void setB1(Object _b1) {
        b1 = _b1;
    }

    /**
     * This will set the b2 coordinate for this Value.
     * 
     * @param _b2
     *            <code>Object</code> the _b2 coordinate for this Value.
     */
    public void setB2(Object _b2) {
        b2 = _b2;
    }

    /**
     * This will set the label for this Value.
     * 
     * @param _label
     *            <code>Element</code> the _label for this Value.
     */
    public void setLabel(Element _label) {
        label = _label;
    }

    /**
     * This will set the label's background style for this Value.
     * 
     * @param _labelBackgroundStyle
     *            <code>Object</code> the _labelBackgroundStyle coordinate for
     *            this Value.
     */
    public void setLabelBackgroundStyle(String _labelBackgroundStyle) {
        labelBackgroundStyle = Defs.parseStyle(_labelBackgroundStyle);
    }

    /**
     * This will set the label's events.
     * 
     * @param _labelEvents
     *            <code>String</code> the _labelEvents link to go to for a
     *            tip.
     */
    public void setLabelEvents(String _labelEvents) {
        labelEvents = _labelEvents;
    }

    /**
     * This will set the label's href.
     * 
     * @param _labelHref
     *            <code>String</code> the _labelHref link to go to for a tip.
     */
    public void setLabelHref(String _labelHref) {
        labelHref = _labelHref;
    }

    /**
     * This will set the label style this Value.
     * 
     * @param _labelStyle
     *            <code>String</code> the _labelStyle for this Value.
     */
    public void setLabelStyle(String _labelStyle) {

        labelStyle = Defs.parseStyle(_labelStyle);
    }

    /**
     * Sets the parent series.
     * 
     * @param _series
     *            the new parent series
     */
    protected void setParentSeries(Series _series) {
        series = _series;
    }

    /**
     * This will set the shape for this Value.
     * 
     * @param _shape
     *            <code>String</code> the _shape coordinate for this Value.
     */
    public void setShape(String _shape) {
        shape = _shape;
    }

    /**
     * This will set the spot coordinate for this Value, used for setting the
     * svg coordinate of a category.
     * 
     * @param _spot
     *            <code>Object</code> the _spot coordinate for this Value.
     */
    public void setSpot(Object _spot) {
        spot = _spot;
    }

    /**
     * This will set the style for this Value.
     * 
     * @param _style
     *            <code>Object</code> the _style coordinate for this Value.
     */
    public void setStyle(String _style) {
        style = Defs.parseStyle(_style);
    }

    /**
     * This will set the x coordinate for this Value.
     * 
     * @param _x
     *            <code>Object</code> the _x coordinate for this Value.
     */
    public void setX(Object _x) {
        x = _x;
    }

    /**
     * This will set the y coordinate for this Value.
     * 
     * @param _y
     *            <code>Object</code> the _y coordinate for this Value.
     */
    public void setY(Object _y) {
        y = _y;
    }

    /**
     * This will set the z coordinate for this Value.
     * 
     * @param _z
     *            <code>Object</code> the _z coordinate for this Value.
     */
    public void setZ(Object _z) {
        z = _z;
    }

    /**
     * This will return the string representation of this object (for debug).
     * 
     * @return <code>String</code> debug Value.
     */
    @Override
    public String toString() {

        // logger.info("Calling...");

        StringBuffer result = new StringBuffer();
        final String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {");

        result.append(newLine);
        result.append(" x: ");
        result.append(getXObject());
        result.append(newLine);

        result.append(" y: ");
        result.append(getYObject());
        result.append(newLine);

        result.append(" a1: ");
        result.append(getA1());
        result.append(newLine);

        result.append(" b1: ");
        result.append(getB1());
        result.append(newLine);

        result.append(" a2: ");
        result.append(getA2());
        result.append(newLine);

        result.append(" b2: ");
        result.append(getB2());
        result.append(newLine);

        result.append(" label: ");
        result.append(getLabel());
        result.append(newLine);

        result.append(" labelStyle: ");
        result.append(getLabelStyle());
        result.append(newLine);

        result.append(" shape: ");
        result.append(getShape());
        result.append(newLine);

        result.append(" style: ");
        result.append(getShapeStyle());
        result.append(newLine);

        result.append(" spot: ");
        result.append(getSpot());
        result.append(newLine);

        result.append("}");
        return result.toString();

    }

}
