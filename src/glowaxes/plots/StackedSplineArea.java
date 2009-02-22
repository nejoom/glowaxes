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
import glowaxes.maths.BezierControlPoints;
import glowaxes.util.TypeConverter;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class StackedSplineArea.
 */
public class StackedSplineArea extends ALinePlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(StackedSplineArea.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.ALinePlotter#drawSeries(glowaxes.data.Series)
     */
    @Override
    public Element drawSeries(Series values) {

        Data data = getData();

        // get ready to process element path
        Element path = new Element("path");
        path.setAttribute("desc", "stacked-spline-area");
        path.setAttribute("d", "");

        Series mySeries = new Series();
        for (int i = 0; i < values.getSize(); i++) {

            double y = (values.getValue(i)).getA1();

            Value myYOffset1 = new Value();
            myYOffset1.setY(y);

            // get svg x point
            y = data.getParentArea().getYAxis().getSVGOffset(myYOffset1);

            // get svg y point
            double x =
                    data.getParentArea().getXAxis().getSVGOffset(
                            (values.getValue(i)));

            Value myNewValue = new Value();

            myNewValue.setX(x);
            myNewValue.setY(y);

            mySeries.addValue(myNewValue);

        }

        Series mySeries2 = new Series();
        for (int i = values.getSize() - 1; i >= 0; i--) {

            double y = (values.getValue(i)).getA2();

            Value myYOffset1 = new Value();
            myYOffset1.setY(y);

            // get svg x point
            y = data.getParentArea().getYAxis().getSVGOffset(myYOffset1);

            // get svg y point
            double x =
                    data.getParentArea().getXAxis().getSVGOffset(
                            (values.getValue(i)));

            Value myNewValue = new Value();

            myNewValue.setX(x);
            myNewValue.setY(y);

            mySeries2.addValue(myNewValue);

        }
        // if value has a style: use it, otherwise use the default
        path.setAttribute("style", values.getDefaultShapeStyle());

        path.setAttribute("d", BezierControlPoints.getPathArea(mySeries,
                mySeries2));

        return path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#prepareData()
     */
    public void prepareData() {

        Data data = getData();

        Group plotSet = data.getGroup();

        // debug code
        // logger.error("max of x: " + data.getGroup().getXMaxSeriesList());
        // logger.error("min of x: " + data.getGroup().getXMinSeriesList());
        // logger.error("max of y: " + data.getGroup().getYMaxSeriesList());
        // logger.error("min of y: " + data.getGroup().getYMinSeriesList());

        HashMap<String, Double> myStackedMap = new HashMap<String, Double>();

        for (int i = 0; i < plotSet.getSize(); i++) {

            Series mySeries = plotSet.getSeries(i);

            // don't do anything with shapes
            // String defaultShape = mySeries.getDefaultShape();

            for (int j = 0; j < mySeries.getSize(); j++) {

                // dont do anything with dataMarkers
                // Element dataMarker;
                // if (myValue.getShape() != null)
                // dataMarker = new Element("g");
                // else if (defaultShape != null)
                // dataMarker = new Element("g");
                // else {
                // dataMarker = new Element("circle");
                // // prefixToken = "c";
                // }

                Value myValue = plotSet.getSeries(i).getValue(j);

                double yOffset1 =
                        TypeConverter.getDouble(myStackedMap.get(myValue
                                .getXObject().toString()), 0);

                // x point
                double yOffset2 = myValue.getY() + yOffset1;

                myStackedMap.put(myValue.getXObject().toString(), new Double(
                        yOffset2));

                myValue.setA1(yOffset1);
                myValue.setA2(yOffset2);

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

            // add the line
            area.addContent(drawSeries(mySeries));

        }// for plotSet

        return area;

    }

}
