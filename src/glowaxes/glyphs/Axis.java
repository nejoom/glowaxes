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

import glowaxes.data.Group;
import glowaxes.data.Value;
import glowaxes.tags.Constants;
import glowaxes.util.DataFormatter;
import glowaxes.util.TypeConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * The Class Axis.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public abstract class Axis extends SimpleGlyph {

    /** The Constant CATEGORY. */
    private static final int CATEGORY = 1;

    /** The Constant DATE. */
    private static final int DATE = 2;

    /** The Constant HOUR. */
    public static final long HOUR = 60l * 60l * 1000l;

    /** The Constant DAY. */
    public static final long DAY = HOUR * 24l;

    /** The Constant HALF_MONTH. */
    public static final long HALF_MONTH = DAY * 15l;

    /** The Constant MONTH. */
    public static final long MONTH = DAY * 30l;

    /** The Constant BIMONTH. */
    public static final long BIMONTH = MONTH * 2;

    /** The Constant FEW_MONTHS. */
    public static final long FEW_MONTHS = MONTH * 3;

    /** The Constant WEEK. */
    public static final long WEEK = DAY * 7l;

    /** The Constant YEAR. */
    public static final long YEAR = DAY * 365l;

    /** The Constant DECADE. */
    public static final long DECADE = YEAR * 10l;

    /** The Constant HALF_CENTURY. */
    public static final long HALF_CENTURY = YEAR * 100l / 2;

    /** The Constant CENTURY. */
    public static final long CENTURY = YEAR * 100l;

    /** The logger. */
    private static Logger logger = Logger.getLogger(Axis.class.getName());

    /** The Constant NUMBER. */
    private static final int NUMBER = 3;

    /** The Constant SOME_MONTHS. */
    public static final long SOME_MONTHS = MONTH * 4;

    /** The axis attributes. */
    private HashMap<String, Object> axisAttributes =
            new HashMap<String, Object>();

    /** The axis max. */
    private double axisMax;

    /** The axis min. */
    private double axisMin;

    /** The axis title. */
    private TextGlyph axisTitle;

    /** The axis type. */
    private int axisType = CATEGORY;

    /** The day width. */
    private double dayWidth = 0;

    /** The first label width. */
    private double firstLabelWidth;

    /** The first offshot. */
    private double firstOffshot;

    /** The first tic offset. */
    private double firstTicOffset = -1;

    /** The last label width. */
    private double lastLabelWidth;

    /** The last offshot. */
    private double lastOffshot;

    /** The last tic offset. */
    private double lastTicOffset = -1;

    /** The max label height. */
    private double maxLabelHeight = -1;

    /** The max label width. */
    private double maxLabelWidth = -1;

    /** The minor tic marks. */
    private LinkedHashMap<Double, Date> minorTicMarks =
            new LinkedHashMap<Double, Date>();

    /** The minor tic marks special. */
    private final LinkedHashMap<Double, Boolean> minorTicMarksSpecial =
            new LinkedHashMap<Double, Boolean>();

    /** The prefered minor grids. */
    private int preferedMinorGrids = 0;

    /** The shift category. */
    private final boolean shiftCategory = false;

    /** The shift to zero. */
    private final boolean shiftToZero =
            TypeConverter.getBoolean(Constants.getDefault("area.shiftToZero"),
                    false);

    /** The tic marks. */
    private LinkedHashMap<Double, ChartTitle> ticMarks =
            new LinkedHashMap<Double, ChartTitle>();

    /** The tic width. */
    private double ticWidth = 0;

    /** The zero axis. */
    private double zeroAxis;

    /**
     * Gets the axis attributes.
     * 
     * @return the axisAttributes
     */
    public HashMap<String, Object> getAxisAttributes() {
        return axisAttributes;
    }

    /**
     * Gets the axis offshot.
     * 
     * @return the axis offshot
     */
    public double getAxisOffshot() {
        if (logger.isDebugEnabled()) {
            logger.debug("getAxisOffshot()");
        }
        double rotation = getLabelRotation();
        // logger.error(rotation);
        if (rotation == 0) {
            if (getLastTicOffset() - getLength() + getMaxLabelWidth() > 0) {
                double value =
                        getLastTicOffset() - getLength() + lastLabelWidth / 2;
                if (logger.isDebugEnabled())
                    logger.debug("getAxisOffshot: " + (value));
                return value;
            }
        } else if ((rotation > 0 && rotation <= 90)
                || (rotation < -90 && rotation >= -180)) {
            double value =
                    (getLastTicOffset() - getLength() + lastLabelWidth + lastOffshot) > 0
                            ? (getLastTicOffset() - getLength()
                                    + lastLabelWidth + lastOffshot) : 0;
            if (logger.isDebugEnabled())
                logger.debug("getAxisOffshot: " + (value));
            return value;
        } else if ((rotation > 90 && rotation <= 180)
                || (rotation < 0 && rotation >= -90)) {
            double value =
                    (getFirstTicOffset() - 10 - (firstLabelWidth - (firstLabelWidth + firstOffshot))) < 0
                            ? (getFirstTicOffset() - 10 - (firstLabelWidth - (firstLabelWidth + firstOffshot)))
                            : 0;
            if (logger.isDebugEnabled())
                logger.debug("getAxisOffshot: " + (value));
            return value;
        }

        return 0;
    }

    /**
     * Gets the axis title.
     * 
     * @return the axisTitle
     */
    public TextGlyph getAxisTitle() {
        return axisTitle;
    }

    /**
     * Gets the axis token.
     * 
     * @return the axisToken
     */
    public abstract String getAxisToken();

    /**
     * Gets the day width.
     * 
     * @return the dayWidth
     */
    public double getDayWidth() {
        return dayWidth;
    }

    /*
     * @returns the FirstTicOffset
     */
    /**
     * Gets the first tic offset.
     * 
     * @return the first tic offset
     */
    public double getFirstTicOffset() {
        return firstTicOffset;
    }

    /**
     * Gets the format.
     * 
     * @param toFormat
     *            the to format
     * 
     * @return the format
     */
    public String getFormat(Object toFormat) {
        return getFormat(toFormat, getLabelFormat());

    }

    /**
     * Gets the format.
     * 
     * @param toFormat
     *            the to format
     * @param labelFormat
     *            the label format
     * 
     * @return the format
     */
    public String getFormat(Object toFormat, String labelFormat) {

        if (labelFormat == null)
            return TypeConverter.getString(toFormat, "undefined");

        logger.fatal(getType() + " " + labelFormat);
        // assume date for category if no text formatting is done
        if (getType().equals("date")
                || (getType().equals("category") && labelFormat.length() > 1
                        && labelFormat.indexOf("#") == -1 && labelFormat
                        .indexOf("0") == -1)) {

            if (logger.isDebugEnabled())
                logger.debug("Formatting for date: " + toFormat + ", "
                        + labelFormat + ", type: "
                        + toFormat.getClass().getName());

            toFormat = TypeConverter.getDate(toFormat);
        } else if ((getType().equals("number") || toFormat instanceof Number)
                && (labelFormat.indexOf("#k") != -1
                        || labelFormat.indexOf("0k") != -1 || labelFormat
                        .indexOf(")k") != -1)) {
            if (logger.isDebugEnabled())
                logger.debug("Formatting for #k: " + toFormat + ", "
                        + labelFormat);

            toFormat = TypeConverter.getFloat(toFormat);

        } else if (getType().equals("number")) {

            if (logger.isDebugEnabled())
                logger.debug("Formatting for number: " + toFormat + ", "
                        + labelFormat);

            toFormat = TypeConverter.getFloat(toFormat);

        } else // treat as category
        {
            if (logger.isDebugEnabled())
                logger.debug("Formatting for category: " + toFormat + ", "
                        + labelFormat);

            toFormat = TypeConverter.getString(toFormat, "null");

        }

        return DataFormatter.getFormat(toFormat, getParentArea().getData()
                .getDataFormatter(), labelFormat);
    }

    /**
     * Gets the grid minor style.
     * 
     * @return the grid minor style
     */
    public String getGridMinorStyle() {
        String style =
                TypeConverter.getString(getAxisAttributes().get(
                        "gridMinorStyle"), Constants.getDefault(getAxisToken()
                        .concat("axis.gridMinorStyle")));
        if (style == null)
            return "";
        else
            return style;
    }

    /**
     * Gets the grid style.
     * 
     * @return the grid style
     */
    public String getGridStyle() {
        String style =
                TypeConverter.getString(getAxisAttributes().get("gridStyle"),
                        Constants.getDefault(getAxisToken().concat(
                                "axis.gridStyle")));
        if (style == null)
            return "";
        else
            return style;
    }

    /**
     * Gets the label effect.
     * 
     * @return the label effect
     */
    public String getLabelEffect() {
        String labelEffect =
                TypeConverter.getString(getAxisAttributes().get("labelEffect"),
                        null);

        return labelEffect;
    }

    /**
     * Gets the label format.
     * 
     * @return the label format
     */
    public String getLabelFormat() {
        return (String) getAxisAttributes().get("labelFormat");
    }

    /**
     * Gets the label rotation.
     * 
     * @return the label rotation
     */
    public double getLabelRotation() {
        double rotation =
                TypeConverter.getDouble(getAxisAttributes()
                        .get("labelRotation"), 0d);

        // only use rotations between 180 and -180
        if (rotation > 180 || rotation < -180) {
            rotation =
                    (rotation % 180) - 180
                            * Math.pow(-1, Math.ceil(rotation / 180));
        }

        return rotation;
    }

    /**
     * Gets the label step.
     * 
     * @return the label step
     */
    public Double getLabelStep() {
        String labelStep =
                TypeConverter.getString(getAxisAttributes().get("labelStep"),
                        null);

        double nLabelStep = 0;

        if (labelStep == null)
            return null;

        if (labelStep.equals("h")) {
            nLabelStep = HOUR;
        } else if (labelStep.equals("d")) {
            nLabelStep = DAY;
        } else if (labelStep.equals("w")) {
            nLabelStep = WEEK;
        } else if (labelStep.equals("hm")) {
            nLabelStep = HALF_MONTH;
        } else if (labelStep.equals("m")) {
            nLabelStep = MONTH;
        } else if (labelStep.equals("3m")) {
            nLabelStep = FEW_MONTHS;
        } else if (labelStep.equals("4m")) {
            nLabelStep = SOME_MONTHS;
        } else if (labelStep.equals("2m")) {
            nLabelStep = BIMONTH;
        } else if (labelStep.equals("y")) {
            nLabelStep = YEAR;
        } else if (labelStep.equals("d")) {
            nLabelStep = DECADE;
        } else if (labelStep.equals("hc")) {
            nLabelStep = HALF_CENTURY;
        } else if (labelStep.equals("c")) {
            nLabelStep = CENTURY;
        } else {
            nLabelStep = TypeConverter.getDouble(labelStep, 0);
        }

        return nLabelStep;
    }

    /**
     * Gets the label style.
     * 
     * @return the label style
     */
    public String getLabelStyle() {
        String labelStyle =
                TypeConverter.getString(getAxisAttributes().get("labelStyle"),
                        Constants.getDefault(getAxisToken().concat(
                                "axis.labelStyle")));
        if (labelStyle == null)
            return "";
        // logger.error(labelStyle);
        return labelStyle;
    }

    /*
     * @returns the LastTicOffset
     */
    /**
     * Gets the last tic offset.
     * 
     * @return the last tic offset
     */
    public double getLastTicOffset() {
        return lastTicOffset;
    }

    /*
     * @returns the axis length (area length)
     */
    /**
     * Gets the length.
     * 
     * @return the length
     */
    public abstract double getLength();

    /**
     * Gets the max.
     * 
     * @return the max
     */
    public double getMax() {
        if (getAxisAttributes().get("max") == null) {
            return axisMax;
        } else {
            double max =
                    TypeConverter.getDouble(getAxisAttributes().get("max"),
                            Constants.getDefaultDouble(
                                    getAxisToken().concat("axis.max"))
                                    .doubleValue());
            return max;
        }
    }

    /**
     * Gets the maximum height of the axis label.
     * 
     * @return the max label height
     */
    public double getMaxLabelHeight() {
        return maxLabelHeight;
    }

    /**
     * Gets the maximum width of the axis label.
     * 
     * @return the max label width
     */
    public double getMaxLabelWidth() {
        return maxLabelWidth;
    }

    /**
     * Gets the min.
     * 
     * @return the min
     */
    public double getMin() {

        if (getAxisAttributes().get("min") == null) {
            return axisMin;
        } else {
            double min =
                    TypeConverter.getDouble(getAxisAttributes().get("min"),
                            Constants.getDefaultDouble(
                                    getAxisToken().concat("axis.min"))
                                    .doubleValue());
            return min;
        }

    }

    /**
     * Gets the minor tic marks.
     * 
     * @return the minorTicMarks
     */
    public LinkedHashMap<Double, Date> getMinorTicMarks() {
        return minorTicMarks;
    }

    /**
     * Gets the minor tic marks special.
     * 
     * @return the minorTicMarksSpecial
     */
    public LinkedHashMap<Double, Boolean> getMinorTicMarksSpecial() {
        return minorTicMarksSpecial;
    }

    /**
     * Gets the parent area.
     * 
     * @return the parent area
     */
    public Area getParentArea() {
        return (Area) getAxisAttributes().get("area");
    }

    /**
     * Gets the prefered minor grids.
     * 
     * @return the preferedMinorGrids
     */
    public int getPreferedMinorGrids() {
        return preferedMinorGrids;
    }

    /**
     * Gets the show zero.
     * 
     * @return the show zero
     */
    public boolean getShowZero() {
        return TypeConverter
                .getBool(getAxisAttributes().get("showZero"), false);
    }

    /**
     * Gets the tic marks step name for dates.
     * 
     * @return the tic marks step name for dates
     */
    public String getStepName(double step) {
        String stepName = "unknown";
        if (step == HOUR) {
            stepName = "HOUR";
        } else if (step == DAY) {
            stepName = "DAY";
        } else if (step == WEEK) {
            stepName = "WEEK";
        } else if (step == HALF_MONTH) {
            stepName = "HALF_MONTH";
        } else if (step == MONTH) {
            stepName = "MONTH";
        } else if (step == FEW_MONTHS) {
            stepName = "FEW_MONTHS";
        } else if (step == SOME_MONTHS) {
            stepName = "SOME_MONTHS";
        } else if (step == BIMONTH) {
            stepName = "BIMONTH";
        } else if (step == YEAR) {
            stepName = "YEAR";
        } else if (step == DECADE) {
            stepName = "DECADE";
        } else if (step == HALF_CENTURY) {
            stepName = "HALF_CENTURY";
        } else if (step == CENTURY) {
            stepName = "CENTURY";
        }
        return stepName;
    }

    /**
     * Gets the SVG offset.
     * 
     * @param value
     *            the value
     * 
     * @return the SVG offset
     */
    public double getSVGOffset(Value value) {

        // plot svg coordinates on parents canvas todo
        // if (this.getParentArea().getIndex() != 0)
        // if (this.axisToken.equals("x"))
        // return this.getParentArea().getParentChart().getArea(0)
        // .getXAxis().getSVGOffset(_value);
        // else
        // return this.getParentArea().getParentChart().getArea(0)
        // .getYAxis().getSVGOffset(_value);

        double length = getLength();
        double svgOffset = 0d;

        double valueOffset = 0d;

        if (getType().equals("category")) {

        } else {
            if (getAxisToken().equals("x"))
                valueOffset = TypeConverter.getDouble(value.getXObject(), 0d);

            if (getAxisToken().equals("y"))
                valueOffset = TypeConverter.getDouble(value.getYObject(), 0d);
        }

        double max = getMax();
        double min = getMin();

        if (axisType == CATEGORY) {
            if (getAxisToken().equals("x"))
                svgOffset =
                        TypeConverter.getDouble(value.getSpot(), 0d)
                                * getParentArea().getXScale();
            else
                svgOffset =
                        TypeConverter.getDouble(value.getSpot(), 0d)
                                * getParentArea().getYScale();
            double yPos = 0;

            // for categorys shift offset so that label is between tic marks
            if (ticMarks.size() > 1 && isShiftCategory()) {
                double offset = getLength() / (ticMarks.size() - 1) / 2;

                yPos += offset;
            }
            svgOffset = svgOffset - yPos;

        } else if (axisType == NUMBER) {

            if (getAxisToken().equals("x")) {

                svgOffset =
                        valueOffset * length / (max - min) - min * length
                                / (max - min);
            } else // axisToken.equals("y")
            {

                svgOffset =
                        valueOffset * length / (max - min) - min * length
                                / (max - min);
                // reverse direction
                svgOffset = length - svgOffset;
            }

        } else if (axisType == DATE) {

            if (getAxisToken().equals("x")) {

                svgOffset =
                        valueOffset * length / (max - min) - min * length
                                / (max - min);
            } else // axisToken.equals("y")
            {

                svgOffset =
                        valueOffset * length / (max - min) - min * length
                                / (max - min);
                // reverse direction
                svgOffset = length - svgOffset;
            }

        }

        return svgOffset;
    }

    /**
     * Gets the tic length.
     * 
     * @return the tic length
     */
    public double getTicLength() {

        double ticLength =
                TypeConverter.getDouble(getAxisAttributes().get("ticLength"),
                        TypeConverter.getDouble(Constants
                                .getDefault(getAxisToken().concat(
                                        "AbstractAxis.getTicLength")), 3));

        return ticLength;

    }

    /**
     * Gets the tic marks.
     * 
     * @return the tic marks
     */
    public LinkedHashMap<Double, ChartTitle> getTicMarks() {
        return ticMarks;
    }

    /**
     * Gets the tic rotation.
     * 
     * @return the tic rotation
     */
    public double getTicRotation() {
        double rotation =
                TypeConverter.getDouble(getAxisAttributes().get("ticRotation"),
                        Constants.getDefault(getAxisToken().concat(
                                "axis.ticRotation")));
        return rotation;
    }

    /**
     * Gets the tic style.
     * 
     * @return the tic style
     */
    public String getTicStyle() {
        String style =
                TypeConverter.getString(getAxisAttributes().get("ticStyle"),
                        Constants.getDefault(getAxisToken().concat(
                                "axis.ticStyle")));
        if (style == null)
            return "";
        else
            return style;
    }

    /**
     * Gets the tic width.
     * 
     * @return the tic width
     */
    public double getTicWidth() {
        return ticWidth;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {

        String type =
                TypeConverter
                        .getString(
                                getAxisAttributes().get("type"),
                                Constants.getDefault(getAxisToken().concat(
                                        "axis.type"))).toLowerCase();

        if (type == null)
            type = "no type";

        return type;
    }

    /**
     * Gets the zero axis.
     * 
     * @return the zero axis
     */
    public double getZeroAxis() {
        return zeroAxis;
    }

    /**
     * Checks if is shift category.
     * 
     * @return if we need to shiftCategory
     */
    public boolean isShiftCategory() {
        return shiftCategory;
    }

    /**
     * Checks if is shuffle.
     * 
     * @return true, if is shuffle
     */
    public abstract boolean isShuffle();

    /**
     * Sets the axis attributes.
     * 
     * @param axisAttributes
     *            the axisAttributes to set
     */
    public void setAxisAttributes(HashMap<String, Object> axisAttributes) {
        this.axisAttributes = axisAttributes;
    }

    /**
     * Sets the axis title.
     * 
     * @param axisTitle
     *            the axisTitle to set
     */
    public void setAxisTitle(TextGlyph axisTitle) {
        this.axisTitle = axisTitle;
    }

    /*
     * sets the tic marks for dates
     */
    /**
     * Sets the date tic marks.
     * 
     * @param min
     *            the min
     * @param max
     *            the max
     */
    public void setDateTicMarks(long min, long max) {

        double step = HOUR;
        // long t0 = System.currentTimeMillis();
        // if (logger.isDebugEnabled()) {
        logger.info("Entering..." + new Date(min) + ", " + new Date(max));
        // }

        // String type = getParentArea().getType();

        // expand/ contract range depending on implementation of plotter
        double width = getParentArea().getPlotter().getAxisDateRange(min, max);

        max = max + (long) width;
        min = min - (long) width;

        // long t0 = System.currentTimeMillis();
        // if (logger.isDebugEnabled()) {
        logger.info("Recalculated..." + new Date(min) + ", " + new Date(max));
        // }

        if (min > max) {
            long tmp = min;
            min = max;
            max = tmp;
        }
        axisMax = max;
        axisMin = min;
        // logger.info("LENGTH");

        if (getLabelStep() == null) {
            long milliDistance = max - min;
            // double length = TypeConverter.getDouble(getLength(), 100d);
            // logger.info("min date: " + new Date(min));
            // logger.info("max date: " + new Date(max) );

            if (milliDistance <= DAY) {
                step = HOUR;
                logger.info("step: HOUR");

            } else if (milliDistance <= WEEK) {
                step = DAY;
                logger.info("step: DAY");
            } else if (milliDistance <= HALF_MONTH) {
                step = DAY;
                logger.info("step: DAY");
            } else if (milliDistance <= MONTH) {
                step = WEEK;
                logger.info("step: WEEK");
            } else if (milliDistance <= FEW_MONTHS) {
                step = WEEK;
                logger.info("step: WEEK");
            } else if (milliDistance <= SOME_MONTHS) {
                step = HALF_MONTH;
                logger.info("step: HALF_MONTH");
            } else if (milliDistance <= YEAR * 0.9) {
                step = MONTH;
                logger.info("step: MONTH");
            } else if (milliDistance <= YEAR * 2) {
                step = BIMONTH;
                logger.info("step: BIMONTH");
            } else if (milliDistance <= YEAR * 5) {
                step = FEW_MONTHS;
                logger.info("step: BIMONTH");
            } else if (milliDistance <= DECADE * 2.5) {
                step = YEAR;
                logger.info("step: YEAR");
            } else if (milliDistance <= CENTURY * 1.5) {
                step = DECADE;
                logger.info("step: DECADE");
            } else if (milliDistance <= CENTURY * 2.5) {
                step = HALF_CENTURY;
                logger.info("step: CENTURY");
            } else {
                step = CENTURY;
                logger.info("step: CENTURY");
            }
        } else {
            step = getLabelStep();
        }
        setDateTicMarks(min, max, step);

        logger.info("Stepping with: " + getStepName(step));
        // if (logger.isDebugEnabled()) {
        // logger.debug("Leaving... " + (System.currentTimeMillis() - t0)
        // + "[ms]");
        // }
    }

    /*
     * sets the tic marks for dates with specified stepping
     */
    /**
     * Sets the date tic marks.
     * 
     * @param min
     *            the min
     * @param max
     *            the max
     * @param step
     *            the aXI s_ step
     */
    public void setDateTicMarks(long min, long max, double step) {

        // long t0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("Entering..." + new Date(min) + ", " + new Date(max)
                    + ", " + getStepName(step));
        }

        ticMarks = new LinkedHashMap<Double, ChartTitle>();
        minorTicMarks = new LinkedHashMap<Double, Date>();

        axisType = DATE;
        // logger.info("min: " + new Date(min));
        // logger.info("max: " + new Date(max));
        if (min > max) {
            long tmp = min;
            min = max;
            max = tmp;
        }
        axisMax = max;
        axisMin = min;
        // long t0 = (System.currentTimeMillis());
        // logger.info("AXIS_STEP");

        // create a gregorian calendar to handle dates
        GregorianCalendar cal =
                (GregorianCalendar) GregorianCalendar.getInstance(
                        getParentArea().getData().getDataFormatter()
                                .getTimeZone(), getParentArea().getData()
                                .getDataFormatter().getLocale());

        ArrayList<Date> markSet = new ArrayList<Date>();
        // logger.info("getParentArea().getParentChart().getTimeZone(): "
        // + getParentArea().getParentChart().getTimeZone());
        if (step == HOUR) {
            // round down to nearest hour
            long minRound = Math.round(min / HOUR) * HOUR;
            long maxRound = Math.round(max / HOUR) * HOUR;

            cal.setTime(new Date(minRound));

            while (cal.getTime().getTime() < maxRound + HOUR * 2) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.HOUR_OF_DAY, 1);
            }
            preferedMinorGrids = 2;

        } else if (step == DAY) {
            // round down to nearest day
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + DAY)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
            }
            preferedMinorGrids = 7;

        } else if (step == WEEK) {
            // round down to nearest week
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            // reset if first day of week is after min day
            while (cal.getTime().getTime() > min) {
                cal.set(GregorianCalendar.DAY_OF_WEEK, -7);
            }

            while (cal.getTime().getTime() < (max + WEEK)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.DAY_OF_WEEK, 7);
            }
            preferedMinorGrids = 7;

        } else if (step == HALF_MONTH) {
            // round down to nearest month
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + HALF_MONTH + DAY)) {

                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.DAY_OF_MONTH, 15);

                if (cal.getTime().getTime() < (max + HALF_MONTH + DAY)) {
                    markSet.add(new Date(cal.getTime().getTime()));
                    cal.add(GregorianCalendar.MONTH, 1);
                    cal.set(GregorianCalendar.MILLISECOND, 0);
                    cal.set(GregorianCalendar.SECOND, 0);
                    cal.set(GregorianCalendar.MINUTE, 0);
                    cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
                    cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
                }

            }
            preferedMinorGrids = 7;
        } else if (step == MONTH) {
            // round down to nearest month
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + MONTH + DAY)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.MONTH, 1);
            }
            preferedMinorGrids = 7;
        } else if (step == BIMONTH) {
            // round down to nearest month
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + MONTH + DAY)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.MONTH, 2);
            }
            preferedMinorGrids = 2;
        } else if (step == FEW_MONTHS) {
            // round down to nearest month
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + MONTH + DAY)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.MONTH, 3);
            }

            logger.error(markSet);
            preferedMinorGrids = 3;
        } else if (step == YEAR) {
            // round down to nearest year
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + YEAR + MONTH)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.YEAR, 1);
            }
            preferedMinorGrids = 4;

        } else if (step == DECADE) {
            // round down to nearest decade
            cal.setTime(new Date(min));

            int year = cal.get(GregorianCalendar.YEAR);
            year = Math.round(year / 10) * 10;
            cal.set(GregorianCalendar.YEAR, year);

            cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + DECADE + MONTH)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.YEAR, 10);
            }
            preferedMinorGrids = 5;

        } else if (step == HALF_CENTURY) {
            // round down to nearest decade
            cal.setTime(new Date(min));

            int year = cal.get(GregorianCalendar.YEAR);
            year = Math.round(year / 50) * 50;
            cal.set(GregorianCalendar.YEAR, year);

            cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + DECADE + MONTH)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.YEAR, 50);
            }
            preferedMinorGrids = 5;

        } else // (AXIS_STEP == CENTURY)
        {
            // round down to nearest decade
            cal.setTime(new Date(min));

            int year = cal.get(GregorianCalendar.YEAR);
            year = Math.round(year / 100) * 100;
            cal.set(GregorianCalendar.YEAR, year);

            cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            while (cal.getTime().getTime() < (max + CENTURY + MONTH)) {
                markSet.add(new Date(cal.getTime().getTime()));
                cal.add(GregorianCalendar.YEAR, 100);
            }
            preferedMinorGrids = 4;
        }

        ticMarks = new LinkedHashMap<Double, ChartTitle>();

        double scale = getLength() / (max - min);

        // long first = ((Date)markSet.getFirst()).getTime();

        double length = getLength();

        int counter = 0;
        for (Iterator<Date> iter = markSet.iterator(); iter.hasNext();) {

            Date mappedTic = iter.next();

            ChartTitle text =
                    new ChartTitle(getFormat(mappedTic), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            double textWidth = text.getGlyphWidth();
            double textHeight = text.getGlyphHeight();

            if (textWidth > maxLabelWidth) {

                maxLabelWidth = textWidth;

            }

            if (textHeight > maxLabelHeight) {

                maxLabelHeight = textHeight;

            }

            if (!iter.hasNext()) {
                lastOffshot = text.getPlotX();
                lastLabelWidth = textWidth;
            } else if (counter == 0) {
                firstOffshot = text.getPlotX();
                firstLabelWidth = text.getGlyphWidth();
                counter++;
            }

            Double distanceSVG =
                    new Double((mappedTic.getTime() - min) * scale);
            // firstTicOffset not set if negative
            // register the first tic mark if its above zero
            if (firstTicOffset < 0 && distanceSVG > 0) {
                firstTicOffset = distanceSVG;
            }

            if (distanceSVG > 0) {
                ticMarks.put(distanceSVG, text);

                if (distanceSVG < length) {

                    if (distanceSVG > lastTicOffset
                            && distanceSVG < getLength()) {
                        lastTicOffset = distanceSVG;
                    }
                    // logger.info("***"+getFormat(mappedTic).toString());
                    // logger.info("***"+maxLabelLength);
                }
            }

        }

        // minor tic marks for weekends
        if (getPreferedMinorGrids() == 7) {

            // calculate the width between marks.
            java.util.Set<Double> _calcWidth = ticMarks.keySet();
            if (_calcWidth.size() > 1) {
                Iterator<Double> iter = _calcWidth.iterator();
                double firstTic = iter.next();
                double nextTic = iter.next();
                ticWidth = nextTic - firstTic;
            }

            // double minDayDistance =
            // TypeConverter.getDouble(Constants
            // .getDefault("min.day-distance"), 10);
            //
            // logger.error("************************* minDayDistance" +
            // minDayDistance);

            // round down to nearest day
            cal.setTime(new Date(min));
            cal.set(GregorianCalendar.MILLISECOND, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);

            // logger.error("************************* dayWidth" + dayWidth);
            // debug choose saterday or choose first day of week
            // if (dayWidth < minDayDistance)
            // while (cal.get(GregorianCalendar.DAY_OF_WEEK) !=
            // Calendar.SATURDAY) {
            // cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
            // }

            // todo: mark holidays also?
            ArrayList<Date> minorMarkSet = new ArrayList<Date>();
            ArrayList<Date> minorWeekendsAndHolidaysMarkSet =
                    new ArrayList<Date>();
            while (cal.getTime().getTime() < (max + DAY)) {
                // if (!markSet.contains(new Date(cal.getTime().getTime())))
                minorMarkSet.add(new Date(cal.getTime().getTime()));
                if (cal.get(GregorianCalendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || cal.get(GregorianCalendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    minorWeekendsAndHolidaysMarkSet.add(new Date(cal.getTime()
                            .getTime()));
                    minorMarkSet.add(new Date(cal.getTime().getTime()));
                }
                cal.add(GregorianCalendar.DAY_OF_YEAR, 1);

            }

            for (Iterator<Date> iter = minorMarkSet.iterator(); iter.hasNext();) {

                Date mappedTic = iter.next();

                Double distanceSVG =
                        new Double((mappedTic.getTime() - min) * scale);

                // agreement between renderer that if weekday or holiday then
                // make negative
                // to render with no fill
                if (minorWeekendsAndHolidaysMarkSet.contains(mappedTic)) {
                    getMinorTicMarks().put(distanceSVG, mappedTic);
                    getMinorTicMarksSpecial().put(distanceSVG,
                            new Boolean(true));
                } else {
                    getMinorTicMarks().put(distanceSVG, mappedTic);
                    getMinorTicMarksSpecial().put(distanceSVG,
                            new Boolean(false));
                }

            }

            // logger.error("******* minorTicMarks" + minorTicMarks);

        }

        dayWidth = new Double((24 * 60 * 60 * 1000) * scale);

        // if (logger.isDebugEnabled()) {
        // logger.debug("Leaving... " + (System.currentTimeMillis() - t0)
        // + "[ms]");
        // }
    }

    /**
     * Sets the parent area.
     * 
     * @param area
     *            the new parent area
     */
    public void setParentArea(Area area) {
        getAxisAttributes().put("area", area);
    }

    /**
     * Sets the tic marks.
     * 
     * @param min
     *            the min
     * @param max
     *            the max
     */
    public void setTicMarks(double min, double max) {

        long t0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("Entering... (" + min + ", " + max + ")");
        }

        axisType = NUMBER;

        double length = getLength() * 1.0001d;
        boolean showZero = getShowZero();
        // ticMarks = new LinkedHashMap();

        if (getMin() > getMax()) {
            double tmp = min;
            min = max;
            max = tmp;
        }

        if (getShowZero() == true)
            if (min > 0 && max > 0) {
                min = 0;
            } else if (min < 0 && max < 0) {
                max = 0;
            }

        // calculate room on both sides
        double range =
                getParentArea().getPlotter().getAxisNumericalRange(min, max);

        if (min != 0)
            min = min - range;

        if (max != 0)
            max = max + range;

        axisMax = max;
        axisMin = min;

        // long t1 = (System.currentTimeMillis());

        // create data tic marks in memory
        // getParentArea().getXAxis().setTicMarks(30000d,
        // 100000d,
        // TypeConverter.getDouble(getLength(), 0d),
        // getShowZero() );

        double inputMin = min;
        double inputMax = max;

        double axisLength = length;
        double minNumTics = 2d;
        double minLengthPerTic = 17d;
        double maxLengthPerTic =
                TypeConverter.getDouble(Constants
                        .getDefault("max.length.2tics"), 70d);

        // just do two tics if we cant do anything
        if (axisLength < minLengthPerTic * minNumTics) {
            if (logger.isDebugEnabled())
                logger.debug("two tics, very small");
            ticMarks = new LinkedHashMap<Double, ChartTitle>();
            ChartTitle minText =
                    new ChartTitle(getFormat(min), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            ticMarks.put(new Double(0), minText);

            ChartTitle maxText =
                    new ChartTitle(getFormat(min), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            ticMarks.put(new Double(axisLength), maxText);
            lastTicOffset = axisLength;

            return;
        }

        double normal = 0d;
        // to normalize:
        if (!showZero) {
            normal = inputMax - inputMin;
        } else if (inputMin > 0d) {
            normal = inputMax - 0d;
            inputMin = 0;

        } else if (inputMax < 0d) {
            normal = 0d - inputMin;
            inputMax = 0;

        } else {
            normal = inputMax - inputMin;
        }

        // criteria: input = normal * 10^y | 10 < normal <= 100
        long powerPrefered =
                (long) Math.floor(Math.log(normal) / Math.log(10d));
        powerPrefered = powerPrefered - 1;

        double normalMax = inputMax / Math.pow(10d, powerPrefered);

        double normalMin = inputMin / Math.pow(10d, powerPrefered);

        normal = normal / Math.pow(10d, powerPrefered);

        // logger.info("normal: " + normal);
        // logger.info("normalMax: " + normalMax);
        // logger.info("powerMax: " + powerMax);
        // logger.info("normalMin: " + normalMin);
        // logger.info("powerMin: " + powerMin);
        // logger.info("powerPrefered: " + powerPrefered);

        // vector of normalized tic units
        java.util.Vector<Double> ticsVector = new java.util.Vector<Double>();
        ticsVector.add(new Double(1d));
        ticsVector.add(new Double(2d));
        ticsVector.add(new Double(2.5d));
        ticsVector.add(new Double(5d));
        ticsVector.add(new Double(10d));
        ticsVector.add(new Double(20d));
        ticsVector.add(new Double(25d));

        double ticsUnit = 1d;

        // criteria distance per tic > 15
        // minimum tics > 3
        if (normal <= 11d) {
            ticsUnit = 2d;// 6 tics
            preferedMinorGrids = 2;
        } else if (normal <= 12d) {
            ticsUnit = 2d;// 6 tics
            preferedMinorGrids = 2;
        } else if (normal <= 13d) {
            ticsUnit = 2d;// 7 tics
            preferedMinorGrids = 2;
        } else if (normal <= 14d) {
            ticsUnit = 2d;// 7 tics
            preferedMinorGrids = 2;
        } else if (normal <= 15d) {
            ticsUnit = 2d;// 8 tics
            preferedMinorGrids = 2;
        } else if (normal <= 16d) {
            ticsUnit = 2d;// 8 tics
            preferedMinorGrids = 2;
        } else if (normal <= 17d) {
            ticsUnit = 2d;// 9 tics
            preferedMinorGrids = 2;
        } else if (normal <= 18d) {
            ticsUnit = 2d;// 9 tics
            preferedMinorGrids = 2;
        } else if (normal <= 19d) {
            ticsUnit = 2.5d;// 8 tics
            preferedMinorGrids = 2;
        } else if (normal <= 20d) {
            ticsUnit = 2.5d;// 8 tics
            preferedMinorGrids = 2;
        } else if (normal <= 21d) {
            ticsUnit = 2.5d;// 9 tics
            preferedMinorGrids = 2;
        } else if (normal <= 22d) {
            ticsUnit = 2.5d;// 9 tics
            preferedMinorGrids = 2;
        } else if (normal <= 23d) {
            ticsUnit = 2.5d;// 10 tics
            preferedMinorGrids = 2;
        } else if (normal <= 24d) {
            ticsUnit = 2.5d;// 10 tics
            preferedMinorGrids = 2;
        } else if (normal <= 25d) {
            ticsUnit = 2.5d;// 10 tics
            preferedMinorGrids = 2;
        } else if (normal <= 30d) {
            ticsUnit = 5d;// 6 tics
            preferedMinorGrids = 5;
        } else if (normal <= 35d) {
            ticsUnit = 5d;// 7 tics
            preferedMinorGrids = 5;
        } else if (normal <= 40d) {
            ticsUnit = 5d;// 8 tics
            preferedMinorGrids = 5;
        } else if (normal <= 45d) {
            ticsUnit = 5d;// 9 tics
            preferedMinorGrids = 5;
        } else if (normal <= 50d) {
            ticsUnit = 5d;// 10 tics
            preferedMinorGrids = 5;
        } else if (normal <= 55d) {
            ticsUnit = 10d;// 6 tics
            preferedMinorGrids = 5;
        } else if (normal <= 60d) {
            ticsUnit = 10d;// 6 tics
            preferedMinorGrids = 5;
        } else if (normal <= 65d) {
            ticsUnit = 10d;// 7 tics
            preferedMinorGrids = 5;
        } else if (normal <= 70d) {
            ticsUnit = 10d;// 7 tics
            preferedMinorGrids = 5;
        } else if (normal <= 75d) {
            ticsUnit = 10d;// 8 tics
            preferedMinorGrids = 5;
        } else if (normal <= 80d) {
            ticsUnit = 10d;// 8 tics
            preferedMinorGrids = 5;
        } else if (normal <= 85d) {
            ticsUnit = 10d;// 9 tics
            preferedMinorGrids = 5;
        } else if (normal <= 90d) {
            ticsUnit = 10d;// 9 tics
            preferedMinorGrids = 5;
        } else if (normal <= 95d) {
            ticsUnit = 10d;// 10 tics
            preferedMinorGrids = 5;
        } else if (normal <= 100d) {
            ticsUnit = 10d;// 10 tics
            preferedMinorGrids = 5;
        } else {
            // logger.info("no tics 1");
            ticMarks = new LinkedHashMap<Double, ChartTitle>();

            ChartTitle minText =
                    new ChartTitle(getFormat(min), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            ticMarks.put(new Double(0), minText);

            ChartTitle maxText =
                    new ChartTitle(getFormat(min), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            ticMarks.put(new Double(axisLength), maxText);

            lastTicOffset = axisLength;
            return;
        }

        double preferredTicsUnit = ticsUnit;

        double numberOfTics = Math.ceil(normal / ticsUnit);

        // get the first tic unit minimum
        double firstTicUnit = 0;
        double lastTicUnit = 0;

        if (normalMin <= 0 && normalMax >= 0) {
            firstTicUnit =
                    Math.ceil(normalMin / preferredTicsUnit)
                            * preferredTicsUnit;
            lastTicUnit =
                    Math.floor(normalMax / preferredTicsUnit)
                            * preferredTicsUnit;
        } else if (normalMin >= 0 && showZero) // data is above zero mark, but
        // zero should be shown
        {
            firstTicUnit = 0;
            lastTicUnit =
                    Math.floor(normalMax / preferredTicsUnit)
                            * preferredTicsUnit;
        } else if (normalMax <= 0 && showZero) // data is below zero mark, but
        // zero should be shown
        {
            firstTicUnit =
                    Math.ceil(normalMin / preferredTicsUnit)
                            * preferredTicsUnit;
            lastTicUnit = 0;
        } else {
            firstTicUnit =
                    Math.ceil(normalMin / preferredTicsUnit)
                            * preferredTicsUnit;
            lastTicUnit =
                    Math.floor(normalMax / preferredTicsUnit)
                            * preferredTicsUnit;
        }

        // scale so that outliners are placed at beginning and end of axis
        double distancePerTic =
                axisLength * (lastTicUnit - firstTicUnit)
                        / (numberOfTics * (normalMax - normalMin));

        int indexOfVector;

        // logger.info("numberOfTics: " + numberOfTics);
        //
        // logger.info("distancePerTic:" + distancePerTic);
        // logger.info("preferredTicsUnit:" + preferredTicsUnit);
        // logger.info("axisLength:" + axisLength);

        // if distance per tic is too small, find next ticsUnit that works
        if (distancePerTic < minLengthPerTic) {
            // if (axisLength<minLengthPerTic*numberOfTics) {
            // logger.info("too small");

            indexOfVector = ticsVector.indexOf(new Double(ticsUnit));
            while (distancePerTic < minLengthPerTic
                    && indexOfVector < ticsVector.size() - 1) {
                indexOfVector++;
                ticsUnit = ticsVector.get(indexOfVector).doubleValue();
                numberOfTics = (long) Math.ceil(normal / ticsUnit);
                distancePerTic =
                        axisLength
                                * (lastTicUnit - firstTicUnit)
                                / ((numberOfTics - 1) * (normalMax - normalMin));

                preferredTicsUnit = ticsUnit;
                preferedMinorGrids = 2;
                // logger.info("indexOfVector: " + indexOfVector);
                // logger.info("lastTicUnit: " + lastTicUnit);
                // logger.info("firstTicUnit:" + firstTicUnit);
                // logger.info("numberOfTics:" + numberOfTics);
                // logger.info("distancePerTic:" + distancePerTic);
                // logger.info("normalMax:" + normalMax);
                // logger.info("normalMin:" + normalMin);
                // logger.info("preferredTicsUnit:" + preferredTicsUnit);
                // logger.info("axisLength:" + axisLength);

            }
        }

        // /*
        // logger.info("numberOfTics: " + numberOfTics);
        //
        // logger.info("distancePerTic:" + distancePerTic);
        // logger.info("preferredTicsUnit:" + preferredTicsUnit);
        // logger.info("axisLength:" + axisLength);
        // */

        if (distancePerTic < minLengthPerTic) {
            // /*
            numberOfTics = 2;
            // logger.info("no tics 2");
            // */
            // return;
        }

        // if distance per tic is too large
        if (distancePerTic > maxLengthPerTic) {
            // logger.info("too large");
            indexOfVector = ticsVector.indexOf(new Double(ticsUnit));
            while (distancePerTic > minLengthPerTic
                    && distancePerTic > maxLengthPerTic && indexOfVector > 0) {
                indexOfVector--;
                ticsUnit = ticsVector.get(indexOfVector).doubleValue();
                numberOfTics = (long) Math.ceil(normal / ticsUnit);
                distancePerTic =
                        axisLength
                                * (lastTicUnit - firstTicUnit)
                                / ((numberOfTics - 1) * (normalMax - normalMin));

                preferredTicsUnit = ticsUnit;

            }
        }

        ticsUnit = preferredTicsUnit;
        numberOfTics = Math.ceil(normal / ticsUnit);

        distancePerTic =
                axisLength * (lastTicUnit - firstTicUnit)
                        / (numberOfTics * (normalMax - normalMin));

        // if(distancePerTic<minLengthPerTic) {
        // /*
        // numberOfTics = 2;
        // logger.info("no tics 3");
        // */
        // return;
        // }

        // where here because something is preventing tics
        // just do first tic, and last tic.
        // logger.info("numberOfTics: " + numberOfTics);
        if (numberOfTics == 2) {

            ticMarks = new LinkedHashMap<Double, ChartTitle>();

            ChartTitle minText =
                    new ChartTitle(getFormat(min), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            ticMarks.put(new Double(0), minText);

            ChartTitle maxText =
                    new ChartTitle(getFormat(min), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            ticMarks.put(new Double(axisLength), maxText);

            lastTicOffset = axisLength;
            // logger.info("numberOfTics: " + numberOfTics);
            // logger.info("ticMarks: " + ticMarks);
            return;
        }

        // logger.info("");
        // logger.info("numberOfTics: " + numberOfTics);
        //
        // logger.info("distancePerTic:" + distancePerTic);
        // logger.info("preferredTicsUnit:" + preferredTicsUnit);
        // logger.info("axisLength:" + axisLength);
        // logger.info("inputMax:" + inputMax);
        // logger.info("inputMin:" + inputMin);
        // logger.info("normalMax:" + normalMax);
        // logger.info("normalMin:" + normalMin);
        // logger.info("minTic:" + minTic);
        // logger.info("maxTic:" + maxTic);
        // logger.info("normal*10^powerPrefered:" +
        // normal +" [10^" + powerPrefered +"]");
        // t0 = (System.currentTimeMillis() - t0);
        // logger.info(t0 + "[ms]");

        TreeSet<Double> ts = new TreeSet<Double>();

        // unnormalize, data crosses zero mark
        // double delta = 0.1d;
        if (normalMin <= 0 && normalMax >= 0) {
            for (double counter = 0; counter > normalMin - preferredTicsUnit; counter =
                    counter - preferredTicsUnit) {
                ts.add(new Double(Math.round(counter
                        * Math.pow(10d, powerPrefered) * 1000d) / 1000d));
                // logger.info(counter);

            }
            for (double counter = 0; counter < normalMax + preferredTicsUnit; counter =
                    counter + preferredTicsUnit) {
                // logger.info(normalMax);
                // logger.info(preferredTicsUnit);
                // logger.info(counter);
                // logger.info(counter <= normalMax);
                ts.add(new Double(Math.round(counter
                        * Math.pow(10d, powerPrefered) * 1000d) / 1000d));
                // logger.info(counter);
                // logger.info(ts);
            }
            // logger.info("crossing zero");

        } else if (normalMin >= 0 && showZero) // data is above zero mark, but
        // zero should be shown
        {
            for (double counter = 0; counter < normalMax + preferredTicsUnit; counter =
                    counter + preferredTicsUnit) {
                // logger.info(counter);
                ts.add(new Double(Math.round(counter
                        * Math.pow(10d, powerPrefered) * 1000d) / 1000d));
            }
            // logger.info("above zero, but zero shown");

        } else if (normalMax <= 0 && showZero) // data is below zero mark, but
        // zero should be shown
        {
            for (double counter = 0; counter > normalMin - preferredTicsUnit; counter =
                    counter - preferredTicsUnit) {
                // logger.info(counter);
                ts.add(new Double(Math.round(counter
                        * Math.pow(10d, powerPrefered) * 1000d) / 1000d));
            }
            // logger.info("below zero, but zero shown:" + showZero);

        } else // data is above/ below zero mark, zero should not be considered
        {
            double minTic = normalMin / preferredTicsUnit;
            double startCounter = Math.floor(minTic) * preferredTicsUnit;

            for (double counter = startCounter; counter < normalMax
                    + preferredTicsUnit; counter = counter + preferredTicsUnit) {
                // logger.info(counter);
                ts.add(new Double(Math.round(counter
                        * Math.pow(10d, powerPrefered) * 1000d) / 1000d));
            }
            // logger.info("zero not shown");

        }
        // logger.info("ts: " + ts);
        // logger.info("normalMax: " + normalMax);

        // get pixelize marks
        double minMark = ts.first().doubleValue();
        double maxMark = ts.last().doubleValue();

        // double distancePerMark = length/(ts.size()-1);
        // get the first tic unit minimum

        double distancePerMark =
                axisLength * (maxMark - minMark)
                        / ((ts.size() - 1) * (max - min));
        double firstDistanceMark =
                minMark * axisLength / (max - min) - axisLength * min
                        / (max - min);

        // logger.info(distancePerMark + "distancePerMark");
        // logger.info(firstDistanceMark + "firstDistanceMark");
        // logger.info(axisLength + "axisLength");
        // logger.info(lastTicUnit + "lastTicUnit");
        // logger.info(firstTicUnit + "firstTicUnit");
        // logger.info((ts.size()-1) + "ts.size()-1");
        // logger.info(normalMax + "normalMax");
        // logger.info(normalMin + "normalMin");

        TreeSet<Double> markSet = new TreeSet<Double>();
        // logger.info("1");

        // register the first tic mark if its above zero
        if (firstDistanceMark >= 0) {
            firstTicOffset = firstDistanceMark;
        } else {
            firstTicOffset = firstDistanceMark + distancePerMark;
        }

        for (double marks = firstDistanceMark; marks <= length
                + distancePerMark; marks += distancePerMark) {
            // logger.info("2");

            markSet.add(new Double(marks));
        }

        // logger.info("markSet: " + markSet);
        ticMarks = new LinkedHashMap<Double, ChartTitle>();
        int counter = 0;
        for (Iterator<Double> iter = markSet.iterator(); iter.hasNext();) {
            // logger.info("3");
            Double mark = iter.next();
            Double mappedTic = ts.first();

            ChartTitle text =
                    new ChartTitle(getFormat(mappedTic), getLabelStyle(),
                            getLabelEffect(), getAxisToken().equals("x")
                                    ? "left" : "right", getLabelRotation());

            double textWidth = text.getGlyphWidth();
            double textHeight = text.getGlyphHeight();

            if (textWidth > maxLabelWidth) {

                maxLabelWidth = textWidth;

            }

            if (textHeight > maxLabelHeight) {

                maxLabelHeight = textHeight;

            }

            if (!iter.hasNext()) {
                lastOffshot = text.getPlotX();
                lastLabelWidth = textWidth;
            } else if (counter == 0) {
                firstOffshot = text.getPlotX();
                firstLabelWidth = text.getGlyphWidth();
                counter++;
            }

            // shift zero mark on axis
            if (mappedTic.doubleValue() == 0d && shiftToZero) {
                setZeroAxis(mark.doubleValue());
                ticMarks.put(mark, text);
            } else {
                ticMarks.put(mark, text);
            }

            if (mark > lastTicOffset && mark <= getLength()) {
                lastTicOffset = mark;
            }

            // logger.info("mappedTic: " + mappedTic);
            ts.remove(mappedTic);
            if (ts.size() == 0)
                break;
            // logger.info("ts: " + ts);
        }
        // logger.info(maxLabelLength + "
        // maxLabelLength********************************************************
        // ***********************");
        // logger.info("ticMarks: " + ticMarks);
        // logger.info(ticMarks.size());

        // boolean stop = true;
        // if (stop) return;

        ticWidth = distancePerMark;

        if (logger.isDebugEnabled()) {
            logger.debug("Leaving... " + (System.currentTimeMillis() - t0)
                    + "[ms]");
        }
    }

    /**
     * Sets the tic marks.
     * 
     * @param group
     *            the new tic marks
     */
    public void setTicMarks(Group group) {

        long t0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.error("Entering...");
        }

        getParentArea().getPlotter().setAxisCategories();
        axisType = CATEGORY;

        // hold the ticmarks
        ticMarks = new LinkedHashMap<Double, ChartTitle>();

        int size = getParentArea().getPlotter().getAxisCategoricalUnits();

        double scale = getLength() / ((double) size + 1);

        // logger.error("max size: " + set.getMaxSizeSeries());
        // logger.error("scale: " + scale);

        double i = 0;

        // register for this axis width between tics
        ticWidth = scale;

        // register the first tic mark if its above zero
        firstTicOffset = scale;

        // now we just use all series defined
        for (int j = 0; j < group.getSeriesList().size(); j++) {
            ArrayList<Value> arrayList = group.getSeries(j).getArrayList();

            double counter = 0;
            for (Iterator<Value> iter = arrayList.iterator(); iter.hasNext();) {

                Value value = iter.next();

                Object object = null;
                if (getAxisToken().equals("x")) {
                    object = value.getXObject();
                    i =
                            getParentArea().getPlotter().getXAxisCategoryIndex(
                                    object, j);
                } else if (getAxisToken().equals("y")) {
                    object = value.getYObject();
                    i =
                            getParentArea().getPlotter().getYAxisCategoryIndex(
                                    object, j);
                }
                ChartTitle text =
                        new ChartTitle(getFormat(object), getLabelStyle(),
                                getLabelEffect(), getAxisToken().equals("x")
                                        ? "left" : "right", getLabelRotation());

                double textWidth = text.getGlyphWidth();
                double textHeight = text.getGlyphHeight();

                if (textWidth > maxLabelWidth) {

                    maxLabelWidth = textWidth;

                }

                if (textHeight > maxLabelHeight) {

                    maxLabelHeight = textHeight;

                }

                if (!iter.hasNext()) {
                    lastOffshot = text.getPlotX();
                    lastLabelWidth = textWidth;
                } else if (counter == 0) {
                    firstOffshot = text.getPlotX();
                    firstLabelWidth = text.getGlyphWidth();
                    counter++;
                }

                Double distanceSVGTic;
                Double distanceSVG;

                if (getAxisToken().equals("y")) {
                    // reverse category order by subtracting
                    distanceSVGTic =
                            new Double(TypeConverter.getDouble(getLength())
                                    - scale * (i + 1));
                    distanceSVG =
                            new Double(TypeConverter.getDouble(getLength())
                                    - scale * (i + 1));
                } else {
                    distanceSVGTic =
                            new Double(scale
                                    * (i + getParentArea().getPlotter()
                                            .getInitialAxisCategoricalOffset()));
                    distanceSVG =
                            new Double(
                                    scale
                                            * (i + getParentArea().getPlotter()
                                                    .getAxisCategoricalOffset(
                                                            object, j)));
                }

                // for a category mark the spot that this specific category
                // has to be rendered
                value.setSpot(distanceSVG);

                ticMarks.put(distanceSVGTic, text);

                if (distanceSVGTic > lastTicOffset
                        && distanceSVGTic <= getLength()) {
                    lastTicOffset = distanceSVGTic;
                }

            }
        }
        preferedMinorGrids = 2;

        if (logger.isDebugEnabled())
            logger.debug("ticMarks: " + ticMarks);

        if (logger.isDebugEnabled()) {
            logger.debug("Leaving... " + (System.currentTimeMillis() - t0)
                    + "[ms]");
        }
    }

    /**
     * Sets the zero axis.
     * 
     * @param zeroAxis
     *            the new zero axis
     */
    public void setZeroAxis(double zeroAxis) {
        this.zeroAxis = zeroAxis;
    }
}
