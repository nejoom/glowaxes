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

import glowaxes.data.Series;
import glowaxes.data.Value;
import glowaxes.util.TextProcessor;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * The Class SymbolGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class SymbolGlyph extends SimpleGlyph {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(SymbolGlyph.class.getName());

    /** The height. */
    private final double height;

    /** The shape. */
    private final String shape;

    /** The shape style. */
    private final String shapeStyle;

    /** The style. */
    private final String style;

    /** The type. */
    private final String type;

    /** The width. */
    private final double width;

    /**
     * Instantiates a new symbol glyph.
     * 
     * @param width
     *            the width
     * @param height
     *            the height
     * @param series
     *            the series
     * @param type
     *            the type
     */
    @SuppressWarnings("unchecked")
    public SymbolGlyph(double width, double height, Series series, String type) {

        this.setGlyphWidth(width);
        this.setGlyphHeight(height);
        this.setGlyphStyle(series.getDefaultShapeStyle());
        this.setGlyphRx(4);
        this.setGlyphRy(4);
        this.type = type;
        this.style = series.getLineStyle();
        this.shapeStyle = series.getDefaultShapeStyle();
        this.shape = series.getDefaultShape();
        this.width = width;
        this.height = height;
        this.setId("symbolGlyph");

    }

    /**
     * Instantiates a new symbol glyph.
     * 
     * @param width
     *            the width
     * @param height
     *            the height
     * @param value
     *            the value
     * @param type
     *            the type
     */
    public SymbolGlyph(double width, double height, Value value, String type) {
        this.setGlyphWidth(width);
        this.setGlyphHeight(height);
        this.setGlyphStyle(value.getShapeStyle());
        this.setGlyphRx(4);
        this.setGlyphRy(4);
        this.type = type;
        this.width = width;
        this.height = height;
        this.style = value.getShapeStyle();
        this.shapeStyle = value.getShapeStyle();
        this.shape = value.getShape();
        this.setId("symbolGlyph");

    }

    //
    // Interface
    //
    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#renderGlyph()
     */
    @Override
    public Element renderGlyph() {
        Element glyph;
        if (type.equals("line") || type.equals("spline")
                || type.equals("point")) {
            glyph = new Element("g");
            Element line = new Element("line");

            line.setAttribute("style", style);

            TextProcessor.setSemiColonSeparatedKey(line.getAttribute("style"),
                    "fill", "none");

            line.setAttribute("x1", "-" + width * 0.2);
            line.setAttribute("y1", "" + height / 2);
            line.setAttribute("x2", "" + width * 1.2);
            line.setAttribute("y2", "" + height / 2);

            Element marker = new Element("g");

            marker.setAttribute("transform", "translate(" + width / 2 + ", "
                    + height / 2 + ")");

            Element path = new Element("path");
            path.setAttribute("style", shapeStyle);

            logger.error(shape);
            Attribute pathAttribute = new Attribute("d", shape);
            pathAttribute.setAttributeType(Attribute.ENTITY_TYPE);
            path.setAttribute(pathAttribute);

            if (!type.equals("point")) {
                glyph.addContent(line);
            }
            marker.addContent(path);
            glyph.addContent(marker);

        } else {
            glyph = super.renderGlyph();
        }
        return glyph;

    }
}
