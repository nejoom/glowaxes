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

import glowaxes.data.Group;
import glowaxes.data.Series;
import glowaxes.data.Value;
import glowaxes.glyphs.Data;
import glowaxes.util.TypeConverter;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class GroupedColumn.
 */
public class GroupedColumn extends ABarPlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(GroupedColumn.class.getName());

    double unit = 0.35;

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#drawValue(glowaxes.data.Value)
     */
    public Element drawValue(Value value) {

        double x;
        double y;
        double xStackOffset = 0;
        Data data = getData();

        boolean shift = false;
        if (data.getParentArea().getXAxis().getType().equals("date")) {
            shift = true;
        }

        // get svg x point
        x = data.getParentArea().getXAxis().getSVGOffset(value);

        // get svg y point
        y = data.getParentArea().getYAxis().getSVGOffset(value);

        // get svn 0 point
        Value v = new Value();
        v.setY(0);
        double y0 = data.getParentArea().getYAxis().getSVGOffset(v);

        // get ready to process element polygon
        Element polygon = new Element("polygon");
        polygon.setAttribute("desc", "grouped-column");

        double yd = y;
        // negative numbers are taken into account
        double yOffset =
                data.getParentArea().getGlyphHeight()
                        - (data.getParentArea().getGlyphHeight() - y0);

        // determine shrink factor of standard width
        double shrinkFactor = 1d;
        if (shift) {

            // figured out on paper
            xStackOffset =
                    (value.getParentSeries().getIndex() - (data.getGroup()
                            .getSize() - 1d) / 2d);
            xStackOffset *= getWidth();
            x = x + xStackOffset;
        }

        // logger.error("************************" + plotSet.getSize());

        double x0 = x + getWidth() * shrinkFactor / 2;
        double x1 = x - getWidth() * shrinkFactor / 2;

        if (x1 < 0)
            x1 = 0;

        double length = data.getParentArea().getXAxis().getLength();
        if (x0 > length)
            x0 = length;

        // if value has a style: use it, otherwise use the default
        if (value.getShapeStyle() != null)
            polygon.setAttribute("style", value.getShapeStyle());

        polygon.setAttribute("points", x0 + "," + yOffset + " " + x0 + "," + yd
                + " " + x1 + "," + yd + " " + x1 + "," + yOffset);

        double xMap = data.getParentArea().getXOffsetChart();
        double yMap = data.getParentArea().getYOffsetChart();
        x0 += xMap;
        yOffset += yMap;
        yd += yMap;
        x1 += xMap;

        if (data.getLabels().getPopup().equalsIgnoreCase("true")) {
            data.getParentArea().getParentChart().setAreaMap(
                    "polygon",
                    "" + x0 + "," + yOffset + ", " + x0 + "," + yd + ", " + x1
                            + "," + yd + ", " + x1 + "," + yOffset,
                    value.getLabelEvents(),
                    replaceLabel((value.getLabel() != null) ? value.getLabel()
                            .getText().toString() : "", value),
                    value.getLabelHref());
        } else if (data.getLabels().getPopup().equalsIgnoreCase("html")
                && data.getLabels().getText() != null) {

            String xml = replaceLabel(data.getLabels().getText(), value);

            data.getParentArea().getParentChart().setAreaMap(
                    "polygon",
                    "" + x0 + "," + yOffset + ", " + x0 + "," + yd + ", " + x1
                            + "," + yd + ", " + x1 + "," + yOffset, null, xml,
                    value.getLabelHref());
        }
        return polygon;
    }

    @Override
    public double getAxisCategoricalOffset(Object object, int series) {
        int numSeries = getData().getGroup().getSeriesList().size();

        if (numSeries == 1)
            return myXArrayList.indexOf(object.toString());

        double normalize = unit / (numSeries - 1);

        // normalize
        double nSeries = series * normalize;

        // shift from center
        nSeries = nSeries - unit / 2;

        return getInitialAxisCategoricalOffset() + nSeries;
    }

    /**
     * Gets the width.
     * 
     * @return width of single data column or bar
     */
    @Override
    public double getWidth() {

        double width = 5;
        String _width = getData().getParentArea().getRenderingWidth();

        // logger.fatal(_width);
        if (_width != null) {

            if (_width.indexOf("%") != -1) {

                _width = _width.substring(0, _width.indexOf("%"));
                width = TypeConverter.getDouble(_width, 0d);

                double plottingWidth;
                if (getData().getParentArea().getType().indexOf("bar") != -1) {
                    plottingWidth = getData().getParentArea().getGlyphHeight();
                } else {
                    plottingWidth = getData().getParentArea().getGlyphWidth();
                }

                width =
                        (plottingWidth / getAxisCategoricalUnits()) * width
                                / 100;
                // logger.fatal(plottingWidth);
            } else {
                width = TypeConverter.getDouble(_width, 0d);

                if (width < 0)
                    width = 5;
            }
        }
        // logger.fatal(getAxisCategoricalUnits());
        // logger.fatal(width);

        return width * 2 * unit / getData().getGroup().getSeriesList().size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#prepareData()
     */
    public void prepareData() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#render()
     */
    public Element render() {

        Data data = getData();

        logger.info("Generating chart from "
                + this.getClass().getCanonicalName());

        if (data.getParentArea().getYAxis().getType() != "date"
                && data.getParentArea().getYAxis().getType() != "category"
                && data.getParentArea().getYAxis().getType() != "number") {
            throw new RuntimeException(
                    "Chart must have a date, category or number axis!");
        }

        Element area = new Element("g");
        area.setAttribute("desc", "data");
        if (data.getEffect() != null) {
            area.setAttribute("filter", "url(#" + data.getEffect() + ")");
        }
        Group plotSet = data.getGroup();

        for (int i = 0; i < plotSet.getSize(); i++) {

            Series mySeries = plotSet.getSeries(i);

            for (int j = 0; j < mySeries.getSize(); j++) {

                Value myValue = plotSet.getSeries(i).getValue(j);

                // add the polygon (the bar)
                area.addContent(drawValue(myValue));

            }// for mySeries
        }// for plotSet

        return area;
    }
}
