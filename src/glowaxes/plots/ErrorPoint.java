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
// @SuppressWarnings("static-access")
/**
 * The Class ErrorPoint.
 */
public class ErrorPoint extends APlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(ErrorPoint.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#drawValue(glowaxes.data.Value)
     */
    public Element drawValue(Value value) {

        Element dataMarker = null;
        if (value.getShape() != null)
            dataMarker = new Element("g");
        else
            dataMarker = new Element("circle");

        Data data = getData();

        Value errorValue = new Value();

        double x;
        double y;

        errorValue.setX(value.getA1());

        double x1 = data.getParentArea().getXAxis().getSVGOffset(errorValue);

        errorValue.setX(value.getA2());

        double x2 = data.getParentArea().getXAxis().getSVGOffset(errorValue);

        errorValue.setY(value.getB1());

        double y1 = data.getParentArea().getYAxis().getSVGOffset(errorValue);

        errorValue.setY(value.getB2());

        double y2 = data.getParentArea().getYAxis().getSVGOffset(errorValue);

        errorValue.setY(0);
        errorValue.setX(0);

        double offsetY =
                data.getParentArea().getYAxis().getSVGOffset(errorValue);
        double offsetX =
                data.getParentArea().getXAxis().getSVGOffset(errorValue);

        y1 = y1 - offsetY;
        y2 = y2 - offsetY;

        x1 = x1 - offsetX;
        x2 = x2 - offsetX;

        if (Math.abs(x1) < 3)
            x1 = -3;
        if (Math.abs(x2) < 3)
            x2 = 3;

        String defaultShapeStyle = value.getShapeStyle();

        Element vline = new Element("line");
        vline.setAttribute("desc", "v-error-line");
        vline.setAttribute("style", defaultShapeStyle);
        vline.setAttribute("x1", "" + 0);
        vline.setAttribute("x2", "" + 0);
        vline.setAttribute("y1", "" + y1);
        vline.setAttribute("y2", "" + y2);

        Element hline1 = new Element("line");
        hline1.setAttribute("desc", "h-error-line");
        hline1.setAttribute("style", defaultShapeStyle);
        hline1.setAttribute("x1", "" + x1);
        hline1.setAttribute("x2", "" + x2);
        hline1.setAttribute("y1", "" + y1);
        hline1.setAttribute("y2", "" + y1);

        Element hline2 = new Element("line");
        hline2.setAttribute("desc", "h-error-line");
        hline2.setAttribute("style", defaultShapeStyle);
        hline2.setAttribute("x1", "" + x1);
        hline2.setAttribute("x2", "" + x2);
        hline2.setAttribute("y1", "" + y2);
        hline2.setAttribute("y2", "" + y2);

        dataMarker.addContent(vline);
        dataMarker.addContent(hline1);
        dataMarker.addContent(hline2);

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

        if (dataMarker.getName().toLowerCase().equals("g")) {
            dataMarker.setAttribute("transform", "translate(" + x + ", " + y
                    + ")");
        } else // circle
        {
            dataMarker.setAttribute("cx", "" + x);
            dataMarker.setAttribute("cy", "" + y);
            dataMarker.setAttribute("r", "2");
        }

        double xMap = data.getParentArea().getXOffsetChart() + x;
        double yMap = data.getParentArea().getYOffsetChart() + y;

        // logger.error(data.getParentArea().getGlyphX());
        // logger.error(data.getParentArea().getGlyphY());

        if (data.getLabels().getPopup().equalsIgnoreCase("true"))
            data.getParentArea().getParentChart().setAreaMap(
                    "circle",
                    "" + xMap + ", " + yMap + "," + 10,
                    value.getLabelEvents(),
                    replaceLabel((value.getLabel() != null) ? value.getLabel()
                            .getText().toString() : "", value),
                    value.getLabelHref());

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
