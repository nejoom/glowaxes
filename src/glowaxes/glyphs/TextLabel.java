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


import glowaxes.util.TextProcessor;
import glowaxes.util.TypeConverter;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class TextLabel.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class TextLabel extends TextGlyph {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(TextLabel.class.getName());

    /** The marker. */
    private String marker;

    /** The x offset. */
    private double xOffset = 0;

    /**
     * Instantiates a new text label.
     * 
     * @param text the text
     * @param labelStyle the label style
     * @param backgroundStyle the background style
     * @param effect the effect
     * @param x the x
     * @param y the y
     */
    public TextLabel(String text, String labelStyle, String backgroundStyle,
            String effect, double x, double y) {
        setText(text);
        setTextStyle(labelStyle);
        setTextEffect(effect);
        setGlyphStyle(backgroundStyle);
        setGlyphX(x);
        setGlyphY(y);

        setGlyphRx(5);
        setGlyphRy(5);

        setTextOffset(2);
        setId("label");

    }

    /**
     * Gets the marker.
     * 
     * @return the marker
     */
    public String getMarker() {
        return marker;
    }

    /* (non-Javadoc)
     * @see glowaxes.glyphs.TextGlyph#renderGlyph()
     */
    public Element renderGlyph() {

        Element text = new Element("text");

        // double x = getTextX();
        // double y = getTextY();

        text.setAttribute("style", getTextStyle());

        if (getText().split("\n").length > 1) {

            if (getTextY() != 0) {
                text.setAttribute("y", "" + (getTextY()));
            }

            String[] lines = getText().split("\n");
            for (int i = 0; i < lines.length; i++) {

                Element tspan = new Element("tspan");

                if (getTextRotation() == 0) {
                    tspan.setAttribute("x", "" + (getTextX()));
                } else {
                    tspan.setAttribute("x", "0");
                }
                tspan.setAttribute("dy", "1em");

                tspan.addContent(lines[i].trim());
                text.addContent(tspan);
            }
        } else {
            text.addContent(getText());

            if (getTextX() != 0 || getTextY() != 0) {
                text.setAttribute("x", "" + (getTextX()));
                text.setAttribute("y", "" + (getTextY()));
            }
        }

        if (getTextEffect() != null) {

            Element g = new Element("g");
            g.setAttribute("filter", "url(#" + getTextEffect() + ")");
            g.addContent(text);
            text = g;

        }

        if (getTextRotation() != 0) {

            text.setAttribute("x", "0");
            text.setAttribute("y", "0");

            if (!text.getName().equals("g")) {
                Element g = new Element("g");
                g.addContent(text);
                text = g;
            }

            double xt = 0;
            double yt = 0;
            if (getTextRotation() >= 0 && getTextRotation() < 90) {

                if (getGlyphAlign().equals("left")) {
                    xt =
                            getGlyphOffsetLeft()
                                    + Math.sin(getTextRotation() * Math.PI
                                            / 180) * getTextHeight();
                    yt = getGlyphOffsetTop();
                } else if (getGlyphAlign().equals("right")) {
                    xt =
                            getGlyphOffsetLeft()
                                    + getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180)
                                    + getTextHeight()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180);
                    yt =
                            getGlyphOffsetTop()
                                    + getTextWidth()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180);
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            getGlyphWidth()
                                    - getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180) / 2 - getGlyphOffsetRight();
                    yt =
                            getGlyphOffsetTop()
                                    + getTextWidth()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180) / 2;
                }

            } else if (getTextRotation() >= 90 && getTextRotation() <= 180) {

                if (getGlyphAlign().equals("left")) {
                    xt = getGlyphWidth() - getGlyphOffsetRight();

                    yt =
                            getGlyphOffsetTop()
                                    - Math.cos(getTextRotation() * Math.PI
                                            / 180) * getTextHeight();
                } else if (getGlyphAlign().equals("right")) {// done
                    xt =
                            getGlyphWidth()
                                    + getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180) - getGlyphOffsetRight();

                    yt = getGlyphHeight() - getGlyphOffsetBottom();
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            getGlyphOffsetLeft()
                                    + getTextHeight()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180)
                                    - getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180) / 2;
                    yt =
                            getGlyphOffsetTop()
                                    + getTextWidth()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180)
                                    / 2
                                    - getTextHeight()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180);
                }

            } else if (getTextRotation() < 0 && getTextRotation() >= -90) {

                if (getGlyphAlign().equals("left")) {

                    xt = getGlyphOffsetLeft();
                    yt =
                            getGlyphOffsetTop()
                                    - Math.sin(getTextRotation() * Math.PI
                                            / 180) * getTextWidth();
                } else if (getGlyphAlign().equals("right")) {

                    xt =
                            getGlyphOffsetLeft()
                                    + getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180);
                    yt = getGlyphOffsetTop();
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            getGlyphOffsetLeft()
                                    + getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180) / 2;
                    yt =
                            getGlyphOffsetTop()
                                    - getTextWidth()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180) / 2;
                }

            } else if (getTextRotation() < -90 && getTextRotation() >= -180) {

                if (getGlyphAlign().equals("left")) {
                    xt =
                            getGlyphWidth()
                                    - getGlyphOffsetRight()
                                    + Math.sin(getTextRotation() * Math.PI
                                            / 180) * getTextHeight();
                    yt = getGlyphHeight() - getGlyphOffsetBottom();
                } else if (getGlyphAlign().equals("right")) {
                    xt = getGlyphOffsetLeft();
                    yt =
                            getGlyphOffsetTop()
                                    - Math.cos(getTextRotation() * Math.PI
                                            / 180) * getTextHeight();
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            getGlyphOffsetLeft()
                                    - getTextWidth()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180) / 2;
                    yt =
                            getGlyphOffsetTop()
                                    - getTextWidth()
                                    * Math.sin(getTextRotation() * Math.PI
                                            / 180)
                                    / 2
                                    - getTextHeight()
                                    * Math.cos(getTextRotation() * Math.PI
                                            / 180);

                }

            }

            if (getGlyphStyle().equals("")) {
                xt += getGlyphX();
                yt += getGlyphY();
            }
            text.setAttribute("transform", "translate(" + xt + ", " + yt
                    + ") rotate(" + getTextRotation() + ")");

        }

        if (!getGlyphStyle().equals("")) {

            Element g = new Element("g");

            Element rect = new Element("rect");

            if (getGlyphRx() > 0) {
                rect.setAttribute("rx", "" + getGlyphRx());
            }

            if (getGlyphRy() > 0) {
                rect.setAttribute("ry", "" + getGlyphRy());
            }

            if (getGlyphStyle() != null) {
                rect.setAttribute("style", getGlyphStyle());
            }

            rect.setAttribute("width", "" + getGlyphWidth());

            rect.setAttribute("height", "" + getGlyphHeight());

            if (getGlyphEffect() != null && !getGlyphEffect().equals("")) {
                g.setAttribute("filter", "url(#" + getGlyphEffect() + ")");
            }

            if (xOffset != 0) {
                Element off = new Element("g");
                off.setAttribute("transform", "translate(" + xOffset + ", 0)");
                off.addContent(rect);
                off.addContent(text);
                g.addContent(off);
            } else {
                g.addContent(rect);
                g.addContent(text);
            }
            Element pointer = new Element("polygon");

            // pointer.setAttribute("fill", "midnightblue");
            // pointer.setAttribute("stroke", "black");
            // pointer.setAttribute("stroke-width", "1");

            double startPointX = 0;
            double startPointY = 0;
            double endPointX = 0;
            double endPointY = 0;

            // todo: guessing nice values... doesnt really make sense/ make
            // nicer
            if (marker != null) {
                if (marker.equals("br")) {
                    startPointX = 0;
                    startPointY = -5;
                    endPointX = 12;
                    endPointY = 0;
                } else if (marker.equals("bl")) {
                    startPointX = getGlyphWidth();
                    startPointY = -5;
                    endPointX = getGlyphWidth() - 12;
                    endPointY = 0;
                } else if (marker.equals("tl")) {
                    startPointX = getGlyphWidth();
                    startPointY = getGlyphHeight() + 5;
                    endPointX = getGlyphWidth() - 12;
                    endPointY = getGlyphHeight();
                } else if (marker.equals("tr")) {
                    startPointX = 0;
                    startPointY = getGlyphHeight() + 5;
                    endPointX = 12;
                    endPointY = getGlyphHeight();
                }

                // create balloon effect
                String backstyle =
                        TextProcessor.setSemiColonSeparatedKey(getGlyphStyle(),
                                "stroke", TextProcessor
                                        .getSemiColonSeparatedValue(
                                                getGlyphStyle(), "fill"));
                backstyle =
                        TextProcessor
                                .setSemiColonSeparatedKey(
                                        backstyle,
                                        "stroke-width",
                                        ""
                                                + TypeConverter
                                                        .getDouble(
                                                                TextProcessor
                                                                        .getSemiColonSeparatedValue(
                                                                                backstyle,
                                                                                "stroke-width"),
                                                                1) * 2);
                pointer.setAttribute("style", backstyle);

                String attribute = "" + (endPointX - 5) + "," + endPointY;
                attribute += " " + startPointX + "," + startPointY;
                attribute += " " + (endPointX + 5) + "," + endPointY;

                pointer.setAttribute("points", attribute);

                Element polyline = new Element("polyline");
                polyline.setAttribute("style", getGlyphStyle());
                polyline.setAttribute("points", attribute);

                Element line = new Element("polyline");
                line.setAttribute("style", backstyle);
                attribute = "" + (endPointX - 4) + "," + endPointY;
                attribute += " " + (endPointX + 4) + "," + endPointY;
                line.setAttribute("points", attribute);

                g.addContent(line);
                g.addContent(pointer);
                g.addContent(polyline);
            }

            text = g;

            if (getGlyphX() != 0 || getGlyphY() != 0)
                text.setAttribute("transform", "translate(" + getGlyphX()
                        + ", " + getGlyphY() + ")");

        }

        text.setAttribute("desc", getId());

        if (text.getName().equals("text")) {

            if (getGlyphY() != 0 || getGlyphX() != 0 || getTextX() != 0
                    || getTextY() != 0) {
                text.setAttribute("x", "" + (getGlyphX() + getTextX()));
                text.setAttribute("y", "" + (getGlyphY() + getTextY()));
            }

        }

        return text;
    }

    /**
     * Sets the horizontal offset.
     * 
     * @param xOffset the new horizontal offset
     */
    public void setHorizontalOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Sets the marker.
     * 
     * @param marker the new marker
     */
    public void setMarker(String marker) {
        this.marker = marker;
    }

}
