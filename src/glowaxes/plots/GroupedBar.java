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
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class GroupedBar.
 */
public class GroupedBar extends ABarPlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(GroupedBar.class.getName());

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

        // get svg x point
        x = data.getParentArea().getXAxis().getSVGOffset(value) + xStackOffset;

        // get svg y point
        y = data.getParentArea().getYAxis().getSVGOffset(value);

        // logger.error(myStackedMap);

        // get ready to process element polygon
        Element polygon = new Element("polygon");
        polygon.setAttribute("desc", "stacked-bar");

        double yd = y;
        double xd = x;

        // if value has a style: use it, otherwise use the default
        if (value.getShapeStyle() != null)
            polygon.setAttribute("style", value.getShapeStyle());

        // negative numbers are taken into account
        double xOffset = 0;
        /*
         * data.getParentArea().getXAxis().getAxisOffset();
         */

        polygon.setAttribute("points", xOffset + "," + (yd + getWidth() / 2)
                + " " + xd + "," + (yd + getWidth() / 2) + " " + xd + ","
                + (yd - getWidth() / 2) + " " + xOffset + ","
                + (yd - getWidth() / 2));

        double xMap = data.getParentArea().getXOffsetChart();
        double yMap = data.getParentArea().getYOffsetChart();
        xOffset += xMap;
        yd += yMap;
        xd += yMap;

        if (data.getLabels().getPopup().equalsIgnoreCase("true"))
            data.getParentArea().getParentChart().setAreaMap(
                    "polygon",
                    "" + xOffset + "," + (yd + getWidth() / 2) + ", " + xd
                            + "," + (yd + getWidth() / 2) + ", " + xd + ","
                            + (yd - getWidth() / 2) + ", " + xOffset + ","
                            + (yd - getWidth() / 2),
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
