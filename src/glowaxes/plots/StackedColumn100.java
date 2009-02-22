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

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class StackedColumn100.
 */
public class StackedColumn100 extends ABarPlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(StackedColumn100.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#drawValue(glowaxes.data.Value)
     */
    public Element drawValue(Value value) {

        // get ready to process element polygon
        Element polygon = new Element("polygon");
        polygon.setAttribute("desc", "stacked-column-100%");

        Data data = getData();

        // if value has a style: use it, otherwise use the default
        if (value.getShapeStyle() != null)
            polygon.setAttribute("style", value.getShapeStyle());

        double yOffset1 = value.getA1();
        double yOffset2 = value.getA2();

        Value myYOffset1 = new Value();
        myYOffset1.setY(yOffset1);

        Value myYOffset2 = new Value();
        myYOffset2.setY(yOffset2);

        // get svg x point
        yOffset1 = data.getParentArea().getYAxis().getSVGOffset(myYOffset1);

        // get svg x point
        yOffset2 = data.getParentArea().getYAxis().getSVGOffset(myYOffset2);

        // get svg y point
        double x = data.getParentArea().getXAxis().getSVGOffset(value);

        double x0 = x + getWidth() / 2;
        double x1 = x - getWidth() / 2;
        if (x1 < 0)
            x1 = 0;

        double length = data.getParentArea().getXAxis().getLength();
        if (x0 > length)
            x0 = length;

        polygon.setAttribute("points", x0 + "," + yOffset1 + " " + x0 + ","
                + yOffset2 + " " + x1 + "," + yOffset2 + " " + x1 + ","
                + yOffset1);

        double xMap = data.getParentArea().getXOffsetChart();
        double yMap = data.getParentArea().getYOffsetChart();
        x0 += xMap;
        yOffset1 += yMap;
        yOffset2 += yMap;
        x1 += xMap;

        if (data.getLabels().getPopup().equalsIgnoreCase("true"))
            data.getParentArea().getParentChart().setAreaMap(
                    "polygon",
                    "" + x0 + "," + yOffset1 + ", " + x0 + "," + yOffset2
                            + ", " + x1 + "," + yOffset2 + " " + x1 + ","
                            + yOffset1,
                    value.getLabelEvents(),
                    replaceLabel((value.getLabel() != null) ? value.getLabel()
                            .getText().toString() : "", value),
                    value.getLabelHref());

        return polygon;
    }

    @Override
    public double getAxisNumericalRange(double min, double max) {
        return (max - min) * 0.001;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#prepareData()
     */
    public void prepareData() {

        Data data = getData();

        Group plotSet = data.getGroup();

        HashMap<String, Double> myStackedMap = new HashMap<String, Double>();

        for (int i = 0; i < plotSet.getSize(); i++) {

            Series mySeries = plotSet.getSeries(i);

            for (int j = 0; j < mySeries.getSize(); j++) {

                Value myValue = plotSet.getSeries(i).getValue(j);

                double yOffset1 =
                        TypeConverter.getDouble(myStackedMap.get(myValue
                                .getXObject().toString()), 0);

                double yOffset2 = myValue.getY() + yOffset1;

                myStackedMap.put(myValue.getXObject().toString(), new Double(
                        yOffset2));

                // a1 is 1st offset y axis, a2 is end point
                myValue.setA1(yOffset1);
                myValue.setA2(yOffset2);

            }// for mySeries

        }// for plotSet

        for (int i = 0; i < plotSet.getSize(); i++) {

            Series mySeries = plotSet.getSeries(i);
            for (int j = 0; j < mySeries.getSize(); j++) {

                Value myValue = plotSet.getSeries(i).getValue(j);

                double length =
                        TypeConverter.getDouble(myStackedMap.get(myValue
                                .getXObject().toString()), 0);

                myValue.setA1(myValue.getA1() / length);
                myValue.setA2(myValue.getA2() / length);
                myValue.setB1(myValue.getY() / length);

            }// for mySeries
        }// for plotSet
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

        // makeLabel(element, data, area);
        return area;
    }
}
