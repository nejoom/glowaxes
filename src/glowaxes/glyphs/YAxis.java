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
import glowaxes.util.TextProcessor;
import glowaxes.util.TypeConverter;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class YAxis.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class YAxis extends Axis {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(YAxis.class.getName());

    // private boolean shuffle = false; // initial value to toggle offset

    /**
     * Instantiates a new y axis.
     * 
     * @param _axisAttributes
     *            the _axis attributes
     * @param parent
     *            the parent
     */
    public YAxis(HashMap<String, Object> _axisAttributes, Area parent) {

        logger.info("construction Axis");
        setAxisAttributes(_axisAttributes);
        setParentArea(parent);
        setGlyphHeight(parent.getGlyphHeight());
        setGlyphStyle(TypeConverter.getString(getAxisAttributes().get("style"),
                Constants.getDefault(getAxisToken().concat("axis.style"))));
        setId("y-axis");

        setAxisTitle(new ChartTitle((String) _axisAttributes.get("title"),
                (String) _axisAttributes.get("titleStyle"),
                (String) _axisAttributes.get("titleEffect"),
                (String) _axisAttributes.get("titleAlign"),
                (String) _axisAttributes.get("titleAlign")));
        getAxisTitle().setTextRotation(-90);
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
        // double y = getParentArea().getGlyphHeight() - getGlyphOffsetTop();

        if ((getGlyphStyle()) != null) {
            element.setAttribute("style", getGlyphStyle());
        }

        double xCoordinate = getGlyphX();

        // if (shiftToZero)
        // xCoordinate = getParentArea().getXAxis().getAxisOffset();

        element.setAttribute("x1", "" + xCoordinate);
        element.setAttribute("x2", "" + xCoordinate);

        element.setAttribute("y1", "0");
        element.setAttribute("y2", "" + getLength());
        axis.addContent(element);
        // element = new Element("line");
        // element.setAttribute("style", getGlyphStyle());
        // element.setAttribute("x1", "" + getFirstTicOffset());
        // element.setAttribute("x2", "" + getFirstTicOffset());
        //
        // element.setAttribute("y1", "0");
        // element.setAttribute("y2", "" + getLength());
        // axis.addContent(element);
        // element = new Element("line");
        // element.setAttribute("style", getGlyphStyle());
        // element.setAttribute("x1", "" + 0);
        // element.setAttribute("x2", "" + 100);
        //
        // element.setAttribute("y1", "" +(
        // this.getLength()-getFirstTicOffset()));
        // element.setAttribute("y2", "" +
        // (this.getLength()-getFirstTicOffset()));
        // axis.addContent(element);
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

        // grids
        for (Iterator<Double> iter = getTicMarks().keySet().iterator(); iter
                .hasNext();) {

            double svgMark = iter.next() * getParentArea().getYScale();

            // todo control logic beter
            double yOffset;
            if (!this.getType().equals("category")) {
                yOffset = getLength() - svgMark;
            } else {
                yOffset = svgMark;
            }

            Element grid = new Element("line");
            grid.setAttribute("desc", "grid");

            grid.setAttribute("x1", "0");
            grid.setAttribute("y1", "" + yOffset);

            grid.setAttribute("x2", "" + getAreaWidth());
            grid.setAttribute("y2", "" + yOffset);

            grid.setAttribute("style", getGridStyle());

            if (yOffset >= 0d && yOffset <= getLength()) {
                axis.addContent(grid);
            }

            if (getGridMinorStyle() != null) {
                if (previousMarkSvg != -1) {

                    int number = getPreferedMinorGrids();
                    if (number == 0) {
                        number =
                                TypeConverter.getInt(Constants
                                        .getDefault("minorGrids"), 5);
                    }

                    double distance = (yOffset - previousMarkSvg) / number;

                    if (!(getType().equals("date") && this
                            .getPreferedMinorGrids() == 7)) {
                        for (int i = 1; i < number; i++) {
                            Element gridMinor = new Element("line");
                            gridMinor.setAttribute("desc", "grid-minor");

                            gridMinor.setAttribute("x1", "0");
                            gridMinor.setAttribute("y1", ""
                                    + (yOffset + distance * i));

                            gridMinor.setAttribute("x2", "" + getAreaWidth());
                            gridMinor.setAttribute("y2", ""
                                    + (yOffset + distance * i));

                            gridMinor
                                    .setAttribute("style", getGridMinorStyle());

                            if ((yOffset + distance * i) < getLength()
                                    && (yOffset + distance * i) > 0) {
                                axis.addContent(gridMinor);
                            }

                        }
                        if (addExtra) {
                            // add to bottom
                            for (int i = 1 + number; i < number * 2; i++) {
                                Element gridMinor = new Element("line");

                                gridMinor.setAttribute("desc", "grid-minor");

                                gridMinor.setAttribute("x1", "0");
                                gridMinor.setAttribute("y1", ""
                                        + (yOffset + distance * i));

                                gridMinor.setAttribute("x2", ""
                                        + getAreaWidth());
                                gridMinor.setAttribute("y2", ""
                                        + (yOffset + distance * i));

                                gridMinor.setAttribute("style",
                                        getGridMinorStyle());

                                if ((yOffset + distance * i) < getLength()
                                        && (yOffset + distance * i) > 0) {
                                    {
                                        axis.addContent(gridMinor);
                                    }

                                }
                                addExtra = false;
                            }
                            if (!iter.hasNext()) {
                                // add to top
                                for (int i = 1; i < number; i++) {
                                    Element gridMinor = new Element("line");

                                    gridMinor
                                            .setAttribute("desc", "grid-minor");

                                    gridMinor.setAttribute("x1", "0");
                                    gridMinor.setAttribute("y1", ""
                                            + (yOffset - distance * i));

                                    gridMinor.setAttribute("x2", ""
                                            + getAreaWidth());

                                    gridMinor.setAttribute("y2", ""
                                            + (yOffset - distance * i));

                                    gridMinor.setAttribute("style",
                                            getGridMinorStyle());

                                    if ((yOffset + distance * i) < getLength()
                                            && (yOffset + distance * i) > 0) {
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
            svgMark *= getParentArea().getYScale();

            // label.setTextAlign("right");
            label.setId("ylabel");
            // logger.error(label.getTextStyle());
            // label.setTextOffsetRight(1);
            // label.setGlyphStyle("stroke:blue;stroke-width:1;fill:none");

            // tic height plus offset of label to tics
            double ticOffsetY = getTicLength();

            double ticOffsetX = getTicLength();

            // grow tic
            if (isShuffle()) {

                label.setGlyphAlign("center");
                ticOffsetY += this.getMaxLabelWidth() * (counter % 2);
                ticOffsetX += this.getMaxLabelWidth() * (counter % 2);

                counter++;
            }

            ticOffsetX =
                    ticOffsetX
                            * (Math.cos(getTicRotation() * Math.PI / 180) < 0
                                    ? 0 : Math.cos(getTicRotation() * Math.PI
                                            / 180));

            ticOffsetY *= -Math.sin(getTicRotation() * Math.PI / 180);

            double yPos = getLength() - svgMark - label.getPlotY();

            double xPos = -label.getGlyphWidth() - ticOffsetX + getGlyphX();

            // xPos += label.getPlotX();

            // offset to middle of two tic marks if we have a category
            if (getType() != null && getType().equals("category")
                    && getTicMarks().size() > 1 && isShiftCategory()) {
                double offset = getLength() / (getTicMarks().size() - 1) / 2;

                yPos += offset;
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

        // tics
        int counter = 0;
        for (Iterator<Double> iter = getTicMarks().keySet().iterator(); iter
                .hasNext();) {

            double svgMark = iter.next() * getParentArea().getYScale();

            Element tic = new Element("line");
            tic.setAttribute("desc", "tic");

            double ticLength = getTicLength();

            // grow tic
            if (isShuffle()) {

                ticLength += getMaxLabelWidth() * (counter % 2);
                counter++;

            }

            tic.setAttribute("x1", "" + (-ticLength + getGlyphX()));
            tic.setAttribute("y1", "0");

            tic.setAttribute("x2", "" + (getGlyphX()));
            tic.setAttribute("y2", "0");

            tic.setAttribute("style", getTicStyle());

            Element rotTransTics = new Element("g");
            rotTransTics.setAttribute("desc", "tic");

            // todo control logic beter
            double yOffset;
            if (this.getType().equals("category"))
                yOffset = svgMark;
            else {
                yOffset = getLength() - svgMark;
            }

            rotTransTics.setAttribute("transform", "translate(0, " + yOffset
                    + ") " + "rotate(" + getTicRotation() + ")");

            rotTransTics.addContent(tic);
            // logger.error("yaxis add tic: " + yOffset);
            if (yOffset >= 0d && yOffset <= getLength()) {

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
    private void addWeekendGrid(Element axis) {

        // use odd to determine direction so that grid lines match.
        boolean isOdd = true;

        double axisLength = getAreaWidth();

        for (Iterator<Double> iter = getMinorTicMarks().keySet().iterator(); iter
                .hasNext();) {

            double markSvg = iter.next().doubleValue();
            boolean special =
                    getMinorTicMarksSpecial().get(new Double(markSvg))
                            .booleanValue();

            Element gridMinor = new Element("polyline");
            gridMinor.setAttribute("desc", "special-grid-minor");

            // negative mark is a token for making weekdays transparent
            String gridMinorStyle = getGridMinorStyle();
            if (gridMinorStyle == null)
                gridMinorStyle = "";

            gridMinor.setAttribute("style", gridMinorStyle);
            if (special) {
                // transparent = true;
                TextProcessor.setSemiColonSeparatedKey(gridMinor
                        .getAttribute("style"), "fill", "none");

            }

            // if marking is before area start place start at zero
            if (markSvg < 0) {
                markSvg = -markSvg; // transparent = true;
            }

            double yOffset = getLength() - markSvg;
            double intDay = getLength() - markSvg - getDayWidth();

            // dont allow weekend marker to cross over edge of area
            if (intDay > axisLength)
                intDay = axisLength;

            // use odd to determine direction of drawing so that grid
            // lines match.
            if (isOdd) {
                gridMinor.setAttribute("points", ("0").concat(",").concat(
                        "" + yOffset).concat(" ").concat(("" + getAreaWidth()))
                        .concat(",").concat("" + yOffset).concat(" ").concat(
                                "" + getAreaWidth()).concat(",").concat(
                                "" + intDay).concat(" ").concat(("0")).concat(
                                ",").concat("" + intDay).concat(" ").concat(
                                ("0")).concat(",").concat("" + yOffset));
                isOdd = !isOdd;
            } else {
                gridMinor.setAttribute("points", ("0").concat(",").concat(
                        "" + yOffset).concat(" ").concat(("0")).concat(",")
                        .concat("" + intDay).concat(" ").concat(
                                "" + getAreaWidth()).concat(",").concat(
                                "" + intDay).concat(" ").concat(
                                ("" + getAreaWidth())).concat(",").concat(
                                "" + yOffset).concat(" "));
                isOdd = !isOdd;
            }

            if (markSvg + getDayWidth() < axisLength && yOffset > 0)
                axis.addContent(gridMinor);

        }// iterate minor tic marks
    }

    /**
     * Gets the area width.
     * 
     * @return the area width
     */
    public double getAreaWidth() {

        return getParentArea().getGlyphWidth();

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#getAxisOffshot()
     */
    @Override
    public double getAxisOffshot() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#getAxisToken()
     */
    @Override
    public String getAxisToken() {
        return "y";
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphWidth()
     */
    @Override
    public double getGlyphWidth() {

        return getTicLengthX() + getMaxLabelWidth()
                + getAxisTitle().getGlyphWidth();

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphX()
     */
    @Override
    public double getGlyphX() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#getLength()
     */
    @Override
    public double getLength() {

        return getGlyphHeight() * getParentArea().getYScale();

    }

    /**
     * Gets the tic length x.
     * 
     * @return the tic length x
     */
    private double getTicLengthX() {

        double ticOffsetX = getTicLength();

        // grow tic
        if (isShuffle()) {

            ticOffsetX += this.getMaxLabelWidth();

        }

        ticOffsetX =
                ticOffsetX
                        * (Math.cos(getTicRotation() * Math.PI / 180) < 0 ? 0
                                : Math.cos(getTicRotation() * Math.PI / 180));
        return ticOffsetX;

    }

    //
    // public double getGlyphHeight() {
    //
    // // tic height plus offset of label to tics
    // double ticOffsetY = getTicLength();
    //
    // // grow tic
    // if (shuffle) {
    //
    // ticOffsetY += this.getMaxLabelHeight();
    //
    // }
    //
    // ticOffsetY =
    // ticOffsetY
    // * (Math.cos(getTicRotation() * Math.PI / 180) < 0 ? 0
    // : Math.cos(getTicRotation() * Math.PI / 180));
    //
    // double labelOffsetY = getMaxLabelHeight();
    //
    // return ticOffsetY + labelOffsetY;
    //
    // }

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

    /**
     * Gets the title y.
     * 
     * @return the title y
     */
    private double getTitleY() {
        double height = getAxisTitle().getGlyphHeight();
        if (getAxisTitle().getGlyphVAlign().equals("bottom")) {
            return getLength() - height;
        } else if (getAxisTitle().getGlyphAlign().equals("center")
                || getAxisTitle().getGlyphAlign().equals("middle")) {
            return (getLength() - height) / 2;
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.Axis#isShuffle()
     */
    @Override
    public boolean isShuffle() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#renderGlyph()
     */
    @Override
    public Element renderGlyph() {

        Element axis = new Element("g");
        axis.setAttribute("desc", "y-axis");

        if ((getType().equals("date") && getPreferedMinorGrids() == 7)) {
            addWeekendGrid(axis);
        }// if date

        addTic(axis);
        addGrid(axis);
        addLabels(axis);

        getAxisTitle().setGlyphX(
                getGlyphX() - getTicLengthX() - getMaxLabelWidth()
                        - getAxisTitle().getGlyphWidth());
        //
        // if (this.getAxisTitle().getGlyphAlign().equals("bottom")) {
        // logger.error("altering");
        // logger.error(this.getAxisTitle().getGlyphAlign());
        // getAxisTitle().setGlyphY(
        // getTitleY() - getAxisTitle().getGlyphWidth());
        // } else {
        // logger.error("not altering");
        // logger.error(this.getAxisTitle().getGlyphAlign());
        getAxisTitle().setGlyphY(getTitleY());
        // }

        axis.addContent(getAxisTitle().renderGlyph());

        return axis;
    }

}
