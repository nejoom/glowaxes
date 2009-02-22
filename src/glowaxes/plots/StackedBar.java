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
 * The Class StackedBar.
 */
public class StackedBar extends ABarPlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(StackedBar.class.getName());

    /**
     * Prepare data.
     * 
     * @param data
     *            the data
     */
    public static void prepareData(Data data) {

        Group plotSet = data.getGroup();

        // debug code
        // logger.error("max of x: " + data.getGroup().getXMaxSeriesList());
        // logger.error("min of x: " + data.getGroup().getXMinSeriesList());
        // logger.error("max of y: " + data.getGroup().getYMaxSeriesList());
        // logger.error("min of y: " + data.getGroup().getYMinSeriesList());

        HashMap<String, Double> myStackedMap = new HashMap<String, Double>();

        for (int i = 0; i < plotSet.getSize(); i++) {

            Series mySeries = plotSet.getSeries(i);

            for (int j = 0; j < mySeries.getSize(); j++) {

                Value myValue = plotSet.getSeries(i).getValue(j);

                double xOffset1 =
                        TypeConverter.getDouble(myStackedMap.get(myValue
                                .getYObject().toString()), 0);

                double xOffset2 = myValue.getX() + xOffset1;

                myStackedMap.put(myValue.getYObject().toString(), new Double(
                        xOffset2));

                myValue.setA1(xOffset1);
                myValue.setA2(xOffset2);

            }// for mySeries
        }// for plotSet
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#drawValue(glowaxes.data.Value)
     */
    public Element drawValue(Value value) {

        // get ready to process element polygon
        Element polygon = new Element("polygon");
        polygon.setAttribute("desc", "stacked-bar");

        Data data = getData();

        // if value has a style: use it, otherwise use the default
        if (value.getShapeStyle() != null)
            polygon.setAttribute("style", value.getShapeStyle());

        double xOffset1 = value.getA1();
        double xOffset2 = value.getA2();

        Value myXOffset1 = new Value();
        myXOffset1.setX(xOffset1);

        Value myXOffset2 = new Value();
        myXOffset2.setX(xOffset2);

        // get svg x point
        xOffset1 = data.getParentArea().getXAxis().getSVGOffset(myXOffset1);

        // get svg x point
        xOffset2 = data.getParentArea().getXAxis().getSVGOffset(myXOffset2);

        // get svg y point
        double y = data.getParentArea().getYAxis().getSVGOffset(value);

        polygon.setAttribute("points", xOffset1 + "," + (y + getWidth() / 2)
                + " " + xOffset2 + "," + (y + getWidth() / 2) + " " + xOffset2
                + "," + (y - getWidth() / 2) + " " + xOffset1 + ","
                + (y - getWidth() / 2));

        double xMap = data.getParentArea().getXOffsetChart();
        double yMap = data.getParentArea().getYOffsetChart();
        xOffset1 += xMap;
        xOffset2 += xMap;
        y += yMap;

        if (data.getLabels().getPopup().equalsIgnoreCase("true"))
            data.getParentArea().getParentChart().setAreaMap(
                    "polygon",
                    "" + xOffset1 + "," + (y + getWidth() / 2) + ", "
                            + xOffset2 + "," + (y + getWidth() / 2) + ", "
                            + xOffset2 + "," + (y - getWidth() / 2) + ", "
                            + xOffset1 + "," + (y - getWidth() / 2),
                    value.getLabelEvents(),
                    replaceLabel((value.getLabel() != null) ? value.getLabel()
                            .getText().toString() : "", value),
                    value.getLabelHref());

        return polygon;
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

                double xOffset1 =
                        TypeConverter.getDouble(myStackedMap.get(myValue
                                .getYObject().toString()), 0);

                // x point
                double xOffset2 = myValue.getX() + xOffset1;

                myStackedMap.put(myValue.getYObject().toString(), new Double(
                        xOffset2));

                myValue.setA1(xOffset1);
                myValue.setA2(xOffset2);

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
