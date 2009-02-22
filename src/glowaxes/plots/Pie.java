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
import glowaxes.glyphs.Legend;
import glowaxes.util.Palette;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class Pie.
 */
public class Pie extends APlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(Pie.class.getName());

    /**
     * Sets the styles.
     * 
     * @param data
     *            the new styles
     */
    @SuppressWarnings("unchecked")
    public static void setStyles(Data data) {
        Group myGroup = data.getGroup();

        for (int i = 0; i < myGroup.getSize(); i++) {
            Series mySeries = myGroup.getSeries(i);

            ArrayList<String> al =
                    Palette.getPalette("paired", mySeries.getSize());

            for (int j = 0; j < mySeries.getSize(); j++) {

                if (mySeries.getValue(j).getShapeStyle() == null) {
                    mySeries.getValue(j).setStyle("fill:" + al.get(j));

                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#drawValue(glowaxes.data.Value)
     */
    public Element drawValue(Value value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the slice.
     * 
     * @param midX
     *            the mid x
     * @param midY
     *            the mid y
     * @param startRads
     *            the start rads
     * @param endRads
     *            the end rads
     * @param startRadius
     *            the start radius
     * @param endRadius
     *            the end radius
     * @param zRotation
     *            the z rotation
     * @param value
     *            the value
     * 
     * @return the slice
     */
    public Element getSlice(double midX, double midY, double startRads,
            double endRads, double startRadius, double endRadius,
            double zRotation, Value value) {

        double labelFactor = 0.8; // 80% towards outside of slice
        int direction = 0;
        if (endRads - startRads > Math.PI)
            direction = 1;

        double elipseYfactor = 1;
        double elipseXfactor = 1;

        /*
         * http://www.codestore.net/store.nsf/cmnts/C4D408E3731AEAE080256C2F007590EE?OpenDocument
         */

        double x0 = startRadius * Math.cos(startRads) * elipseXfactor;
        double y0 = startRadius * Math.sin(startRads) * elipseYfactor;
        double x1 = endRadius * Math.cos(startRads) * elipseXfactor;
        double y1 = endRadius * Math.sin(startRads) * elipseYfactor;

        double x2 = endRadius * Math.cos(endRads) * elipseXfactor;
        double y2 = endRadius * Math.sin(endRads) * elipseYfactor;
        double x3 = startRadius * Math.cos(endRads) * elipseXfactor;
        double y3 = startRadius * Math.sin(endRads) * elipseYfactor;

        double lx =
                labelFactor * (endRadius) * Math.cos((startRads + endRads) / 2)
                        * elipseXfactor;

        double ly =
                labelFactor * (endRadius) * Math.sin((startRads + endRads) / 2)
                        * elipseYfactor;

        double arcRadiusEndX = endRadius * elipseXfactor;
        double arcRadiusEndY = endRadius * elipseYfactor;

        double arcRadiusStartX = startRadius * elipseXfactor;
        double arcRadiusStartY = startRadius * elipseYfactor;

        // get ready to process element polygon
        Element slice = new Element("path");
        slice.setAttribute("desc", "pie-slice");

        String path =
                "M" + x0 + ", " + y0 + " L" + x1 + ", " + y1 + " A"
                        + arcRadiusEndX + ", " + arcRadiusEndY + " 0 "
                        + direction + ",1 " + x2 + ", " + y2 + " L" + x3 + ", "
                        + y3 + " A" + arcRadiusStartX + ", " + arcRadiusStartY
                        + " 0 " + direction + ",0 " + x0 + ", " + y0 + " Z";

        slice.setAttribute("d", path);

        slice.setAttribute("style", value.getShapeStyle());

        // set where the labels will be for a pie chart
        // less than 2%
        if (2 * Math.PI * 2 / 100 < Math.abs(endRads - startRads)) {
            value.setB1(midX + lx);
            value.setB2(midY + ly);
        } else {
            value.setB1(-1);
            value.setB2(-1);
        }
        Element g = new Element("g");

        g.setAttribute("transform", "translate(" + midX + ", " + midY + ")");

        g.addContent(slice);

        return g;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#prepareData()
     */
    public void prepareData() {

        setStyles(getData());
        logger.error("prepareData");

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

        double height = data.getParentArea().getGlyphHeight();
        double width = data.getParentArea().getGlyphWidth();

        double legendOffset = 0;
        double legendVOffset = 0;

        // arrange pie according to legend position
        if (data.getParentArea().getLegend() != null) {
            Legend legend = data.getParentArea().getLegend();
            double legendWidth = legend.getGlyphWidth();
            double legendHeight = legend.getGlyphHeight();
            String legendAlign = legend.getGlyphAlign();
            String legendVAlign = legend.getGlyphVAlign();

            if (legend.getStrategy().equals("rows")) {
                if (legendVAlign.equals("top")) {
                    legendVOffset =
                            legendHeight + legend.getGlyphOffsetBottom();
                    height -= legendHeight;
                } else {
                    legendVOffset -= legend.getGlyphOffsetTop();
                    height -= legendHeight;
                }
            } else {
                if (legendAlign.equals("left")) {
                    legendOffset = legendWidth;
                    width -= legendWidth;
                } else {
                    width -= legendWidth;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug(legend.getStrategy());
                logger.debug(legendOffset);
                logger.debug(legendVOffset);
                logger.debug(width);
                logger.debug(height);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(legendOffset);
        }

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

        double currentRad = -Math.PI / 2;// start angle
        boolean reverseOrder = true;
        double shrinkFactor = 0.90; // 90%

        double radius =
                shrinkFactor * Math.min(height, width) / 2 * plotSet.getSize();

        double radiusOffset = radius * 0.30;

        double midX =
                Math.max(radius + radius * (1 - shrinkFactor) / shrinkFactor,
                        width / 2)
                        + legendOffset;
        double midY =
                Math.max(radius + radius * (1 - shrinkFactor) / shrinkFactor,
                        height / 2)
                        + legendVOffset;

        for (int i = 0; i < plotSet.getSize(); i++) {

            double total = plotSet.getSeries(i).getXTotal();
            Series mySeries = plotSet.getSeries(i);

            int index;

            for (int j = 0; j < mySeries.getSize(); j++) {

                if (reverseOrder)
                    index = mySeries.getSize() - j - 1;
                else
                    index = j;

                double rad =
                        2 * Math.PI * mySeries.getValue(index).getX() / total;

                area.addContent(getSlice(midX, midY, currentRad, currentRad
                        + rad, i * radius + radiusOffset, (i + 1) * radius, 0,
                        mySeries.getValue(index)));

                currentRad += rad;

            }// for mySeries

        }// for plotSet

        return area;
    }

}
