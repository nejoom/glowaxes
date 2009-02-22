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

import glowaxes.data.Series;
import glowaxes.data.Value;
import glowaxes.glyphs.Data;
import glowaxes.glyphs.IGlyph;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class ALinePlotter.
 */
public abstract class ALinePlotter extends AGeneralPlotter {

    // Define a static logger variable
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(ALinePlotter.class.getName());

    /** The data. */
    private Data data;

    /** The glyphs. */
    private final ArrayList<IGlyph> glyphs = new ArrayList<IGlyph>();

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#addGlyph(glowaxes.glyphs.IGlyph)
     */
    public void addGlyph(IGlyph glyph) {
        glyphs.add(glyph);
    }

    /**
     * Draw series.
     * 
     * @param values
     *            the values
     * 
     * @return the element
     */
    public abstract Element drawSeries(Series values);

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

    public double getAxisDateRange(long min, long max) {
        int maxSize = 1;
        int numSeries = getData().getGroup().getSeriesList().size();

        for (int i = 0; i < numSeries; i++) {
            int size = getData().getGroup().getSeries(i).getSize();
            if (size > maxSize)
                maxSize = size;
        }
        double width = (max - min) / (maxSize);
        return width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#getData()
     */
    public Data getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#getGlyphs()
     */
    public ArrayList<IGlyph> getGlyphs() {
        return glyphs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#setData(glowaxes.glyphs.Data)
     */
    public void setData(Data data) {
        this.data = data;
    }

}
