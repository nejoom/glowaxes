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

import glowaxes.tags.Constants;
import glowaxes.util.AreaMap;
import glowaxes.util.TextProcessor;
import glowaxes.util.TypeConverter;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class XAxis.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class XAxis extends Axis {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(XAxis.class.getName());

    // private boolean shuffle = false; // initial value to toggle offset

    /**
     * Instantiates a new x axis.
     * 
     * @param _axisAttributes
     *            the _axis attributes
     * @param parent
     *            the parent
     */
    public XAxis(HashMap<String, Object> _axisAttributes, Area parent) {

        logger.info("construction Axis");
        setAxisAttributes(_axisAttributes);
        setParentArea(parent);
        setGlyphWidth(parent.getGlyphWidth());
        setGlyphStyle(TypeConverter.getString(getAxisAttributes().get("style"),
                Constants.getDefault(getAxisToken().concat("axis.style"))));
        setId("x-axis");

        setAxisTitle(new ChartTitle((String) _axisAttributes.get("title"),
                (String) _axisAttributes.get("titleStyle"),
                (String) _axisAttributes.get("titleEffect"),
                (String) _axisAttributes.get("titleAlign"),
                (String) _axisAttributes.get("titleValign")));

    }

    /**
     * Adds the axis.
     * 
     * @param axis
     *            the axis
     */
    protected void addAxisTo(Element axis) {

        // logger.error(getFirstTicOffset());
        // logger.error(getLastTicOffset());

        Element element = new Element("line");

        // calculate vertical
        double y = this.getParentArea().getYAxis().getLength();

        if ((getGlyphStyle()) != null) {
            element.setAttribute("style", getGlyphStyle());
        }

        element.setAttribute("x1", "0");
        element.setAttribute("x2", "" + getLength());

        element.setAttribute("y1", "" + y);
        element.setAttribute("y2", "" + y);
        axis.addContent(element);

    }

    /**
     * Adds the grid.
     * 
     * @param axis
     *            the axis
     */
    private void addGrid(Element axis) {

        boolean addExtra = false;
        double previousMarkSvg = -1;

        // calculate vertical
        double axisHeight =
                getParentArea().getGlyphHeight() - getGlyphOffsetTop();

        // grids
        for (Iterator<Double> iter = getTicMarks().keySet().iterator(); iter
                .hasNext();) {

            double svgMark = iter.next() * getParentArea().getXScale();

            Element grid = new Element("line");
            grid.setAttribute("desc", "grid");

            grid.setAttribute("x1", "" + svgMark);
            grid.setAttribute("y1", "-" + axisHeight);

            grid.setAttribute("x2", "" + svgMark);
            grid.setAttribute("y2", "0");

            grid.setAttribute("style", getGridStyle());

            if (svgMark >= 0d && svgMark <= getLength()) {
                axis.addContent(grid);
            }

            if (getGridMinorStyle() != null) {
                if (previousMarkSvg != -1) // add minor grid marks
                {
                    double xOffset = svgMark;

                    int number = getPreferedMinorGrids();
                    if (number == 0) {
                        number =
                                TypeConverter.getInt(Constants
                                        .getDefault("minorGrids"), 5);
                    }

                    double distance = (xOffset - previousMarkSvg) / number;

                    if (!(getType().equals("date") && this
                            .getPreferedMinorGrids() == 7)) {
                        for (int i = 1; i < number; i++) {
                            Element gridMinor = new Element("line");
                            gridMinor.setAttribute("desc", "grid-minor");

                            gridMinor.setAttribute("x1", ""
                                    + (xOffset - distance * i));
                            gridMinor.setAttribute("y1", "-" + axisHeight);

                            gridMinor.setAttribute("x2", ""
                                    + (xOffset - distance * i));
                            gridMinor.setAttribute("y2", "0");

                            gridMinor
                                    .setAttribute("style", getGridMinorStyle());

                            if ((xOffset - distance * i) < getLength()
                                    && (xOffset - distance * i) > 0) {
                                axis.addContent(gridMinor);
                            }

                        }
                        if (addExtra) {
                            // add to bottom
                            for (int i = 1 + number; i < number * 2; i++) {
                                Element gridMinor = new Element("line");

                                gridMinor.setAttribute("desc", "grid-minor");

                                gridMinor.setAttribute("x1", ""
                                        + (xOffset - distance * i));
                                gridMinor.setAttribute("y1", "-" + axisHeight);

                                gridMinor.setAttribute("x2", ""
                                        + (xOffset - distance * i));
                                gridMinor.setAttribute("y2", "0");

                                gridMinor.setAttribute("style",
                                        getGridMinorStyle());

                                if ((xOffset - distance * i) < getLength()
                                        && (xOffset - distance * i) > 0) {
                                    axis.addContent(gridMinor);
                                }

                            }
                            addExtra = false;
                        }
                        if (!iter.hasNext()) {
                            // add to top
                            for (int i = 1; i < number; i++) {
                                Element gridMinor = new Element("line");

                                gridMinor.setAttribute("desc", "grid-minor");

                                gridMinor.setAttribute("x1", ""
                                        + (xOffset + distance * i));
                                gridMinor.setAttribute("y1", "-" + axisHeight);

                                gridMinor.setAttribute("x2", ""
                                        + (xOffset + distance * i));
                                gridMinor.setAttribute("y2", "0");

                                gridMinor.setAttribute("style",
                                        getGridMinorStyle());

                                if ((xOffset + distance * i) < getLength()
                                        && (xOffset + distance * i) > 0) {
                                    axis.addContent(gridMinor);
                                }

                            }
                        }

                    }
                    previousMarkSvg = svgMark;

                } else {
                    previousMarkSvg = svgMark;
                    addExtra = true;

                }
            }
        }
    }

    /**
     * Adds the labels.
     * 
     * @param axis
     *            the axis
     */
    private void addLabels(Element axis) {

        // labels
        int counter = 0;
        for (Iterator<Double> iter = getTicMarks().keySet().iterator(); iter
                .hasNext();) {

            double svgMark = iter.next();
            ChartTitle label = getTicMarks().get(svgMark);
            svgMark *= getParentArea().getXScale();
            label.setId("xlabel");
            // label.setGlyphStyle("stroke:blue;stroke-width:1;fill:none");
            // logger.error(label.getText());
            // logger.error(label.getTextStyle());

            // tic height plus offset of label to tics
            double ticOffsetY = getTicLength();

            double ticOffsetX = getTicLength();

            if (getLabelRotation() == 0) {
                label =
                        new ChartTitle(label.getText(), this.getLabelStyle(),
                                this.getLabelEffect(), "center", this
                                        .getLabelRotation());

            }

            // logger.error(label.getText());

            // grow tic
            if (isShuffle()) {

                ticOffsetY += this.getMaxLabelHeight() * (counter % 2);

                ticOffsetX += this.getMaxLabelHeight() * (counter % 2);

                counter++;
            }

            ticOffsetY =
                    ticOffsetY
                            * (Math.cos(getTicRotation() * Math.PI / 180) < 0
                                    ? 0 : Math.cos(getTicRotation() * Math.PI
                                            / 180));

            ticOffsetX *= -Math.sin(getTicRotation() * Math.PI / 180);

            double yPos = ticOffsetY;

            double xPos = svgMark + ticOffsetX;

            xPos += label.getPlotX();

            // offset to middle of two tic marks if we have a category
            if (getType() != null && getType().equals("category")
                    && getTicMarks().size() > 1 && isShiftCategory()) {
                double offset = getLength() / (getTicMarks().size() - 1) / 2;

                xPos += offset;
            }

            Element rotTransLabels = new Element("g");
            rotTransLabels.setAttribute("desc", "transform-label");
            rotTransLabels.setAttribute("transform", "translate(" + xPos + ", "
                    + yPos + ")");

            rotTransLabels.addContent(label.renderGlyph());

            if (svgMark >= 0d && svgMark <= getLength()) {
                axis.addContent(rotTransLabels);
            }

        }
    }

    /**
     * Adds the tic.
     * 
     * @param axis
     *            the axis
     */
    private void addTic(Element axis) {

        // calculate vertical
        // double axisHeight =
        // getParentArea().getGlyphHeight() - getGlyphOffsetTop();

        // tics
        int counter = 0;
        if (logger.isDebugEnabled())
            logger.debug(getTicMarks());
        for (Iterator<Double> iter = getTicMarks().keySet().iterator(); iter
                .hasNext();) {

            double svgMark = iter.next() * getParentArea().getXScale();

            Element tic = new Element("line");
            tic.setAttribute("desc", "tic");

            double ticLength = getTicLength();

            // grow tic
            if (isShuffle()) {

                ticLength += getMaxLabelHeight() * (counter % 2);
                counter++;

            }

            tic.setAttribute("x1", "0");
            tic.setAttribute("y1", "" + ticLength);

            tic.setAttribute("x2", "0");
            tic.setAttribute("y2", "0");

            tic.setAttribute("style", getTicStyle());

            Element rotTransTics = new Element("g");
            rotTransTics.setAttribute("desc", "tic");
            rotTransTics.setAttribute("transform", "translate(" + svgMark
                    + ",0) " + "rotate(" + getTicRotation() + ")");

            rotTransTics.addContent(tic);

            if (svgMark >= 0d && svgMark <= getLength()) {
                axis.addContent(rotTransTics);
            }

        }
    }

    /**
     * Adds the weekend grid.
     * 
     * @param axis
     *            the axis
     */
    @SuppressWarnings("unused")
    private void addWeekendGrid(Element axis) {

        logger.info("in add weekend grid");
        // use odd to determine direction so that grid lines match.
        boolean isOdd = true;

        double axisLength = getLength();

        for (Iterator<Double> iter =
                getMinorTicMarksSpecial().keySet().iterator(); iter.hasNext();) {

            Double key = iter.next();
            double value = key.doubleValue();
            boolean special =
                    getMinorTicMarksSpecial().get(new Double(value))
                            .booleanValue();

            Element gridMinor = new Element("polyline");
            gridMinor.setAttribute("desc", "grid-minor");

            String gridMinorStyle = getGridMinorStyle();
            if (gridMinorStyle == null)
                gridMinorStyle = "";

            gridMinor.setAttribute("style", gridMinorStyle);

            // transparent = true;
            TextProcessor.setSemiColonSeparatedKey(gridMinor
                    .getAttribute("style"), "fill", "none");

            double barWidth = getDayWidth() * getParentArea().getXScale();

            double markSvg = value * getParentArea().getXScale() - barWidth / 2;
            double intDay =
                    markSvg + getDayWidth() * getParentArea().getXScale();

            // calculate vertical
            double axisHeight =
                    -getParentArea().getGlyphHeight() - getGlyphOffsetTop();

            gridMinor.setAttribute("points", ("" + markSvg).concat(",").concat(
                    "0").concat(" ").concat(("" + markSvg)).concat(",").concat(
                    "" + axisHeight));

            logger.info("markSvg < axisLength: " + (markSvg < axisLength));
            logger.info("gridMinor: " + AreaMap.print(gridMinor));
            if (!getTicMarks().containsKey(key) && markSvg > 0
                    && markSvg < axisLength)
                axis.addContent(gridMinor);

            if (special) {

                // dont render off area
                if (markSvg > axisLength)
                    markSvg = axisLength;
                else if (markSvg < 0)
                    markSvg = 0;

                if (intDay > axisLength)
                    intDay = axisLength;

                Element gridSpecialMinor = new Element("polyline");

                gridSpecialMinor.setAttribute("desc", "grid-special");
                gridSpecialMinor.setAttribute("style", gridMinorStyle);
                // transparent = true;
                TextProcessor.setSemiColonSeparatedKey(gridSpecialMinor
                        .getAttribute("style"), "stroke-width", "0");
                TextProcessor.setSemiColonSeparatedKey(gridSpecialMinor
                        .getAttribute("style"), "stroke-dasharray", "");
                TextProcessor.setSemiColonSeparatedKey(gridSpecialMinor
                        .getAttribute("style"), "opacity", "0.5");

                if (isOdd) {
                    gridSpecialMinor.setAttribute("points", ("" + markSvg)
                            .concat(",").concat("0").concat(" ").concat(
                                    ("" + markSvg)).concat(",").concat(
                                    "" + axisHeight).concat(" ").concat(
                                    "" + intDay).concat(",").concat(
                                    "" + axisHeight).concat(" ").concat(
                                    "" + intDay).concat(",").concat("0")
                            .concat(" ").concat(("" + markSvg)).concat(",")
                            .concat("0"));
                } else {
                    gridSpecialMinor.setAttribute("points", ("" + markSvg)
                            .concat(",").concat("" + axisHeight).concat(" ")
                            .concat("" + intDay).concat(",").concat(
                                    "" + axisHeight).concat(" ").concat(
                                    "" + intDay).concat(",").concat("0")
                            .concat(" ").concat(("" + markSvg)).concat(",")
                            .concat("0"));
                }
                isOdd = !isOdd;
                axis.addContent(gridSpecialMinor);
            }

        }// iterate minor tic marks
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#getAxisToken()
     */
    @Override
    public String getAxisToken() {
        return "x";
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphHeight()
     */
    @Override
    public double getGlyphHeight() {

        return getTicLengthY() + getMaxLabelHeight()
        /* + (isShuffle() ? getMaxLabelHeight() : 0) */
        + getAxisTitle().getGlyphHeight();

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphY()
     */
    @Override
    public double getGlyphY() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#getLength()
     */
    @Override
    public double getLength() {

        return getGlyphWidth() * getParentArea().getXScale();

    }

    /**
     * Gets the tic length y.
     * 
     * @return the tic length y
     */
    private double getTicLengthY() {

        // tic height plus offset of label to tics
        double ticOffsetY = getTicLength();

        // grow tic
        if (isShuffle()) {

            ticOffsetY += this.getMaxLabelHeight();

        }

        ticOffsetY =
                ticOffsetY
                        * (Math.cos(getTicRotation() * Math.PI / 180) < 0 ? 0
                                : Math.cos(getTicRotation() * Math.PI / 180));
        return ticOffsetY;

    }

    /**
     * Gets the title x.
     * 
     * @return the title x
     */
    private double getTitleX() {
        double width = getAxisTitle().getGlyphWidth();
        if (getAxisTitle().getGlyphAlign().equals("right")) {
            return getLength() - width;
        } else if (getAxisTitle().getGlyphAlign().equals("center")
                || getAxisTitle().getGlyphAlign().equals("middle")) {
            return (getLength() - width) / 2;
        }
        return 0;
    }

    // public double getGlyphHeight() {
    // double textHeight = TextLength.getTextHeight("T", getLabelStyle());
    // double svgLength =
    // Math.abs(getMaxLabelSvgLength()
    // * Math.sin(getLabelRotation() * Math.PI / 180))
    // + Math
    // .abs(Math.cos(getLabelRotation() * Math.PI
    // / 180));
    // return svgLength;
    //
    // }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#isShuffle()
     */
    @Override
    public boolean isShuffle() {
        if (getLabelRotation() == 0 && getMaxLabelWidth() > getTicWidth())
            return true;

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#renderGlyph()
     */
    @Override
    public Element renderGlyph() {

        logger.info("renderGlyph()");
        Element axis = new Element("g");
        axis.setAttribute("desc", "x-axis");
        axis.setAttribute("transform",
                "translate(0, "
                        + (getParentArea().getGlyphHeight()
                                - getGlyphOffsetTop() - getGlyphY()) + ") ");

        // todo uncomment for weekend grid
        // if ((getType().equals("date") && getPreferedMinorGrids() == 7)) {
        // addWeekendGrid(axis);
        // }// if date
        addTic(axis);
        addGrid(axis);
        addLabels(axis);

        getAxisTitle().setGlyphY(
                getGlyphY() + getTicLengthY() + getMaxLabelHeight());
        getAxisTitle().setGlyphX(getTitleX());

        axis.addContent(getAxisTitle().renderGlyph());
        return axis;
    }
}
