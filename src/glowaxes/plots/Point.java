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

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class Point.
 */
public class Point extends APlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(Point.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#drawValue(glowaxes.data.Value)
     */
    public Element drawValue(Value value) {

        // process element g
        Element dataMarker = null;

        Data data = getData();

        double x;
        double y;

        if (value.getShape() != null)
            dataMarker = new Element("g");
        else
            dataMarker = new Element("circle");

        x = data.getParentArea().getXAxis().getSVGOffset(value);

        y = data.getParentArea().getYAxis().getSVGOffset(value);
        // add description to read point
        dataMarker.setAttribute("desc", "point");

        // plot shape
        if (value.getShape() != null) {
            Element path = new Element("path");
            Attribute pathAttribute = new Attribute("d", value.getShape());
            pathAttribute.setAttributeType(Attribute.ENTITY_TYPE);
            path.setAttribute(pathAttribute);

            if (value.getShapeStyle() != null)
                path.setAttribute("style", value.getShapeStyle());

            dataMarker.addContent(path);
        }

        if (value.getShapeStyle() != null)
            dataMarker.setAttribute("style", value.getShapeStyle());

        double scale = 1;

        // scale so that min will scale to 0.5
        // max will scale to 3
        scale =
                0.5 + (2.5 * (value.getA1() - data.getGroup()
                        .getA1MinSeriesList()) / (data.getGroup()
                        .getA1MaxSeriesList() - data.getGroup()
                        .getA1MinSeriesList()));

        String scaleString = "";
        if (!Double.isNaN(scale))
            scaleString = " scale(" + scale + ", " + scale + ")";

        if (dataMarker.getName().toLowerCase().equals("g")) {
            dataMarker.setAttribute("transform", "translate(" + x + ", " + y
                    + ")" + scaleString);
        } else // circle
        {
            dataMarker.setAttribute("cx", "" + x);
            dataMarker.setAttribute("cy", "" + y);
            dataMarker.setAttribute("r", "2");
        }

        return dataMarker;

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

        // Element e = s.getElements();
        // dataMarker.getChild("area").addContent(e);
        /*
         * for (int i = 0; i < plotSet.getSize(); i++) { Series mySeries =
         * plotSet.getSeries(i); for (int j = 0; j < mySeries.getSize(); j++) { //
         * process element g // Element dataMarker = new Element("g"); //
         * dataMarker.setAttribute("desc", "labels"); // /////////////// // plot
         * labels // // if (myValue.getLabel() != null) { //
         * makeLabel(defaultLabelStyle, myValue, dataMarker, x, y); } // else if
         * (defaultLabel != null) { // special label parser } // //
         * element.addContent(dataMarker); }// for mySeries }// for plotSeries
         */
        // area.addContent(element);
        // ///////////////
        // element.addContent(dataMarker);
        for (int i = 0; i < plotSet.getSize(); i++) {
            Series mySeries = plotSet.getSeries(i);

            for (int j = 0; j < mySeries.getSize(); j++) {
                Value myValue = plotSet.getSeries(i).getValue(j);
                area.addContent(drawValue(myValue));

            }// for mySeries
        }// for plotSet

        return area;

    }

}
