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
 * Based on commercial needs the contents of this file may be used under the
 * terms of the Elements End-User License Agreement (the Elements License), in
 * which case the provisions of the Elements License are applicable instead of
 * those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */
package glowaxes.glyphs;

import glowaxes.util.TextLength;
import glowaxes.util.TextProcessor;
import glowaxes.util.TypeConverter;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class TextGlyph.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public abstract class TextGlyph implements ITextGlyph {

    /** The align values. */
    private static ArrayList<String> alignValues = new ArrayList<String>();

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(TextGlyph.class.getName());

    static {
        alignValues.add("center");
        alignValues.add("middle");
        alignValues.add("right");
        alignValues.add("left");
        alignValues.add("top");
        alignValues.add("bottom");
    }

    /** The align. */
    private String align = "left";

    /** The glyph effect. */
    private String glyphEffect = null;

    /** The glyph height. */
    private double glyphHeight = -1;

    /** The glyph offset bottom. */
    private double glyphOffsetBottom = 0;

    /** The glyph offset left. */
    private double glyphOffsetLeft = 0;

    /** The glyph offset right. */
    private double glyphOffsetRight = 0;

    /** The glyph offset top. */
    private double glyphOffsetTop = 0;

    /** The glyph style. */
    private String glyphStyle = "";

    /** The glyph width. */
    private double glyphWidth = -1;

    /** The glyph x. */
    private double glyphX = 0;

    /** The glyph y. */
    private double glyphY = 0;

    /** The id. */
    private String id = "anonymous text glyph";

    /** The offset bottom. */
    private double offsetBottom = 0;

    /** The offset left. */
    private double offsetLeft = 0;

    /** The offset right. */
    private double offsetRight = 0;

    /** The offset top. */
    private double offsetTop = 0;

    /** The rx. */
    private double rx = 0;

    /** The ry. */
    private double ry = 0;

    /** The text. */
    private String text = "";

    // private IGlyph childGlyph;

    /** The text effect. */
    private String textEffect = null;

    /** The text height. */
    private double textHeight = -1;

    /** The text rotation. */
    private double textRotation = 0;

    /** The text style. */
    private String textStyle = "";

    /** The text width. */
    private double textWidth = -1;

    /** The text x. */
    private double textX = -1;

    /** The text y. */
    private double textY = -1;

    // private final static ArrayList<String> vAlignValues = new
    // ArrayList<String>();

    /** The v align. */
    private String vAlign = "center";

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphAlign()
     */
    public String getGlyphAlign() {
        return align;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getEffect()
     */
    public String getGlyphEffect() {
        return glyphEffect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getGlyphHeight()
     */
    public double getGlyphHeight() {

        if (glyphHeight != -1)
            return glyphHeight;

        if (getText() == null || getText().equals("")) {
            return glyphHeight = 0;
        }

        double height =
                offsetTop
                        + Math.abs(Math.cos(textRotation * Math.PI / 180)
                                * getTextHeight()) + offsetBottom;
        height +=
                Math.abs(Math.sin(textRotation * Math.PI / 180))
                        * getTextWidth();
        return glyphHeight = height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetBottom()
     */
    public double getGlyphOffsetBottom() {
        return glyphOffsetBottom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetLeft()
     */
    public double getGlyphOffsetLeft() {
        return glyphOffsetLeft;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetRight()
     */
    public double getGlyphOffsetRight() {
        return glyphOffsetRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphOffsetTop()
     */
    public double getGlyphOffsetTop() {
        return glyphOffsetTop;
    }

    // public String getTextAlign() {
    // return textAlign;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphRx()
     */
    public double getGlyphRx() {
        return rx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphRy()
     */
    public double getGlyphRy() {
        return ry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphStyle()
     */
    public String getGlyphStyle() {
        return glyphStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphVAlign()
     */
    public String getGlyphVAlign() {
        return vAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getGlyphWidth()
     */
    public double getGlyphWidth() {

        if (glyphWidth != -1)
            return glyphWidth;

        if (getText() == null || getText().equals("")) {
            return glyphWidth = 0;
        }

        double width =
                Math.abs(Math.sin(textRotation * Math.PI / 180))
                        * getTextHeight();
        width +=
                offsetLeft + Math.abs(Math.cos(textRotation * Math.PI / 180))
                        * getTextWidth() + offsetRight;
        return glyphWidth = width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getEffect()
     */
    public double getGlyphX() {
        return glyphX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getGlyphY()
     */
    public double getGlyphY() {
        return glyphY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#getId()
     */
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getText()
     */
    public String getText() {

        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.ITextGlyph#getTextEffect()
     */
    public String getTextEffect() {

        return textEffect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getTextHeight()
     */
    public double getTextHeight() {

        if (textHeight != -1)
            return textHeight;

        String tmpTitleHeight =
                TextProcessor
                        .getSemiColonSeparatedValue(textStyle, "font-size");

        // squiggle calibrated for 1em
        double lineCount = text.trim().split("\n").length * 0.94;

        // factor calculated per squiggle calibration large fonts
        return textHeight =
                TypeConverter.getDouble(tmpTitleHeight, 12d) * 1.12 * lineCount;

    }

    /*
     * (non-Javadoc)
     * 
     * 
     */
    /**
     * Gets the text rotation.
     * 
     * @return the text rotation
     */
    public double getTextRotation() {
        return textRotation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getStyle()
     */
    public String getTextStyle() {

        if (textStyle == null)
            textStyle = "";

        // factor calculated per squiggle calibration large fonts
        if (textStyle.indexOf("baseline-shift") == -1)
            textStyle =
                    TextProcessor.setSemiColonSeparatedKey(textStyle,
                            "baseline-shift", "-" + 0.81 * getTextHeight());

        return textStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#getTextWidth()
     */
    public double getTextWidth() {

        if (textWidth != -1)
            return textWidth;

        String maxText = text;
        int maxLength = 0;

        String line[] = text.trim().split("\n");

        for (int i = 0; i < line.length; i++) {
            if (line[i].trim().length() > maxLength) {
                maxText = line[i].trim();
                maxLength = maxText.length();
            }
        }

        // factor calculated per squiggle calibration large fonts
        return textWidth = TextLength.getTextWidth(maxText, textStyle) * 1.0285;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.ITextGlyph#getTextX()
     */
    public double getTextX() {

        if (textX != -1)
            return textX;

        if (getGlyphAlign().equals("left")) {

            textX = offsetLeft;

            textStyle =
                    TextProcessor.setSemiColonSeparatedKey(getTextStyle(),
                            "text-anchor", "start");

        } else if (getGlyphAlign().equals("right")) {

            textX = getGlyphWidth() - offsetRight;

            textStyle =
                    TextProcessor.setSemiColonSeparatedKey(getTextStyle(),
                            "text-anchor", "end");

        } else if (getGlyphAlign().equals("center")
                || getGlyphAlign().equals("middle")) {

            textX = (offsetLeft + getGlyphWidth() - offsetRight) / 2;

            textStyle =
                    TextProcessor.setSemiColonSeparatedKey(getTextStyle(),
                            "text-anchor", "middle");

        }
        return textX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.ITextGlyph#getTextY()
     */
    public double getTextY() {

        if (textY != -1)
            return textY;

        return textY = offsetTop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#renderGlyph()
     */
    public Element renderGlyph() {

        Element text = new Element("text");

        // double x = getTextX();
        // double y = getTextY();

        text.setAttribute("style", getTextStyle());

        if (getText().split("\n").length > 1) {

            // if (getTextY() != 0) {
            // text.setAttribute("y", "" + (getTextY()));
            // }

            String[] lines = getText().split("\n");
            for (int i = 0; i < lines.length; i++) {

                Element tspan = new Element("tspan");

                // if (textRotation == 0) {
                // tspan.setAttribute("x", "" + (getTextX()));
                // } else {
                // tspan.setAttribute("x", "0");
                // }
                tspan.setAttribute("dy", "1em");

                tspan.addContent(lines[i].trim());
                text.addContent(tspan);
            }
        } else {
            text.addContent(getText());

            // if (getTextX() != 0 || getTextY() != 0) {
            // text.setAttribute("x", "" + (getTextX()));
            // text.setAttribute("y", "" + (getTextY()));
            // }
        }

        {
            if (getGlyphAlign().equals("center")
                    || getGlyphAlign().equals("middle")) {
                TextProcessor.setSemiColonSeparatedKey(text
                        .getAttribute("style"), "text-anchor", "middle");
            } else if (getGlyphAlign().equals("right")
                    || getGlyphAlign().equals("top")
                    || getGlyphAlign().equals("bottom")) {
                TextProcessor.setSemiColonSeparatedKey(text
                        .getAttribute("style"), "text-anchor", "end");
            }
            // text.setAttribute("x", "0");
            // text.setAttribute("y", "0");

            Element g = new Element("g");
            g.addContent(text);
            text = g;

            double xt = 0;
            double yt = 0;
            if (textRotation >= 0 && textRotation < 90) {
                if (getGlyphAlign().equals("left")) {
                    xt =
                            offsetLeft + Math.sin(textRotation * Math.PI / 180)
                                    * getTextHeight();
                    yt = offsetTop;
                } else if (getGlyphAlign().equals("right")) {
                    xt =
                            offsetLeft + getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180)
                                    + getTextHeight()
                                    * Math.sin(textRotation * Math.PI / 180);
                    yt =
                            offsetTop + getTextWidth()
                                    * Math.sin(textRotation * Math.PI / 180);
                    TextProcessor.setSemiColonSeparatedKey(text
                            .getAttribute("style"), "text-anchor", "end");

                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            getGlyphWidth() - getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180)
                                    / 2 - offsetRight;
                    yt =
                            offsetTop + getTextWidth()
                                    * Math.sin(textRotation * Math.PI / 180)
                                    / 2;
                }

            } else if (textRotation >= 90 && textRotation <= 180) {

                if (getGlyphAlign().equals("left")) {
                    xt = getGlyphWidth() - offsetRight;

                    yt =
                            offsetTop - Math.cos(textRotation * Math.PI / 180)
                                    * getTextHeight();
                } else if (getGlyphAlign().equals("right")) {// done
                    xt =
                            getGlyphWidth() + getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180)
                                    - offsetRight;

                    yt = getGlyphHeight() - offsetBottom;
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            offsetLeft + getTextHeight()
                                    * Math.sin(textRotation * Math.PI / 180)
                                    - getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180)
                                    / 2;
                    yt =
                            offsetTop + getTextWidth()
                                    * Math.sin(textRotation * Math.PI / 180)
                                    / 2 - getTextHeight()
                                    * Math.cos(textRotation * Math.PI / 180);
                }

            } else if (textRotation < 0 && textRotation >= -90) {

                if (getGlyphAlign().equals("left")) {

                    xt = offsetLeft;
                    yt =
                            offsetTop - Math.sin(textRotation * Math.PI / 180)
                                    * getTextWidth();
                } else if (getGlyphAlign().equals("right")) {

                    xt =
                            offsetLeft + getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180);
                    yt = offsetTop;
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            offsetLeft + getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180)
                                    / 2;
                    yt =
                            offsetTop - getTextWidth()
                                    * Math.sin(textRotation * Math.PI / 180)
                                    / 2;
                }

            } else if (textRotation < -90 && textRotation >= -180) {

                if (getGlyphAlign().equals("left")) {
                    xt =
                            getGlyphWidth() - offsetRight
                                    + Math.sin(textRotation * Math.PI / 180)
                                    * getTextHeight();
                    yt = getGlyphHeight() - offsetBottom;
                } else if (getGlyphAlign().equals("right")) {
                    xt = offsetLeft;
                    yt =
                            offsetTop - Math.cos(textRotation * Math.PI / 180)
                                    * getTextHeight();
                } else if (getGlyphAlign().equals("middle")
                        || getGlyphAlign().equals("center")) {
                    xt =
                            offsetLeft - getTextWidth()
                                    * Math.cos(textRotation * Math.PI / 180)
                                    / 2;
                    yt =
                            offsetTop - getTextWidth()
                                    * Math.sin(textRotation * Math.PI / 180)
                                    / 2 - getTextHeight()
                                    * Math.cos(textRotation * Math.PI / 180);

                }

            }

            text.setAttribute("transform", "translate(" + xt + ", " + yt
                    + ") rotate(" + textRotation + ")");
            // text.setAttribute("transform", "rotate(" + textRotation + ")");
        }
        if (textEffect != null && !textEffect.equals("")) {
            text.setAttribute("filter", "url(#" + textEffect + ")");
        }
        // glyphStyle = "fill:none;stroke:yellow";
        if (!glyphStyle.equals("")) {

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

            g.addContent(rect);
            g.addContent(text);

            text = g;

        } else {
            Element g = new Element("g");
            g.addContent(text);
            text = g;
        }

        if (getGlyphX() != 0 || getGlyphY() != 0)
            text.setAttribute("transform", "translate(" + getGlyphX() + ", "
                    + getGlyphY() + ")");

        text.setAttribute("desc", getId());

        // if (text.getName().equals("text")) {
        //
        // if (getGlyphY() != 0 || getGlyphX() != 0 || getTextX() != 0
        // || getTextY() != 0) {
        // text.setAttribute("x", "" + (getGlyphX() + getTextX()));
        // text.setAttribute("y", "" + (getGlyphY() + getTextY()));
        // }
        //
        // }

        return text;
    }

    // public void setTextAlign(String textAlign) {
    //
    // if (!alignValues.contains(textAlign)) {
    // textAlign = alignValues.get(0);
    // }
    //
    // this.textAlign = textAlign;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphAlign(java.lang.String)
     */
    public void setGlyphAlign(String align) {
        if (!alignValues.contains(align)) {
            align = alignValues.get(0);
        }
        this.align = align;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphEffect(java.lang.String)
     */
    public void setGlyphEffect(String glyphEffect) {
        this.glyphEffect = glyphEffect;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphHeight(double)
     */
    public void setGlyphHeight(double height) {
        this.glyphHeight = height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffset(double)
     */
    public void setGlyphOffset(double glyphOffset) {
        this.glyphOffsetLeft = glyphOffset;
        this.glyphOffsetRight = glyphOffset;
        this.glyphOffsetTop = glyphOffset;
        this.glyphOffsetBottom = glyphOffset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetBottom(double)
     */
    public void setGlyphOffsetBottom(double glyphOffsetBottom) {
        this.glyphOffsetBottom = glyphOffsetBottom;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetLeft(double)
     */
    public void setGlyphOffsetLeft(double glyphOffsetLeft) {
        this.glyphOffsetLeft = glyphOffsetLeft;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetRight(double)
     */
    public void setGlyphOffsetRight(double glyphOffsetRight) {
        this.glyphOffsetRight = glyphOffsetRight;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphOffsetTop(double)
     */
    public void setGlyphOffsetTop(double glyphOffsetTop) {
        this.glyphOffsetTop = glyphOffsetTop;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphRx(double)
     */
    public void setGlyphRx(double rx) {
        this.rx = rx;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphRy(double)
     */
    public void setGlyphRy(double ry) {
        this.ry = ry;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphStyle(java.lang.String)
     */
    public void setGlyphStyle(String glyphStyle) {
        if (glyphStyle == null)
            glyphStyle = "";
        this.glyphStyle = glyphStyle;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphVAlign(java.lang.String)
     */
    public void setGlyphVAlign(String vAlign) {
        if (!alignValues.contains(vAlign)) {
            vAlign = alignValues.get(0);
        }
        this.vAlign = vAlign;

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setGlyphWidth(double)
     */
    public void setGlyphWidth(double width) {
        this.glyphWidth = width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setGlyphX(double)
     */
    public void setGlyphX(double glyphX) {
        this.glyphX = glyphX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setGlyphY(double)
     */
    public void setGlyphY(double glyphY) {
        this.glyphY = glyphY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.IGlyph#setId(java.lang.String)
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setText(java.lang.String,
     *      java.lang.String)
     */
    public void setText(String text) {

        if (text != null) {

            textHeight = -1;

            textWidth = -1;

            this.text = text.trim();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.ITextGlyph#setTextEffect(java.lang.String)
     */
    public void setTextEffect(String textEffect) {
        this.textEffect = textEffect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setOffsetLeft(double)
     */
    /**
     * Sets the text offset.
     * 
     * @param offset
     *            the new text offset
     */
    public void setTextOffset(double offset) {
        textX = -1;
        textY = -1;
        this.offsetLeft = offset;
        this.offsetRight = offset;
        this.offsetTop = offset;
        this.offsetBottom = offset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setOffsetBottom(double)
     */
    public void setTextOffsetBottom(double offsetBottom) {
        textX = -1;
        textY = -1;
        this.offsetBottom = offsetBottom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setOffsetLeft(double)
     */
    public void setTextOffsetLeft(double offsetLeft) {
        textX = -1;
        textY = -1;
        this.offsetLeft = offsetLeft;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setOffsetRight(double)
     */
    public void setTextOffsetRight(double offsetRight) {
        textX = -1;
        textY = -1;
        this.offsetRight = offsetRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setOffsetTop(double)
     */
    public void setTextOffsetTop(double offsetTop) {
        textX = -1;
        textY = -1;
        this.offsetTop = offsetTop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.GlyphInterface#setTextRotation(double)
     */
    public void setTextRotation(double textRotation) {

        // reset dimensions
        glyphWidth = -1;

        glyphHeight = -1;

        // only use rotations between 180 and -180
        if (textRotation > 180 || textRotation < -180) {
            textRotation =
                    (textRotation % 180) - 180
                            * Math.pow(-1, Math.ceil(textRotation / 180));
        }

        this.textRotation = textRotation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.ITextGlyph#setTextStyle(java.lang.String)
     */
    public void setTextStyle(String textStyle) {
        if (textStyle == null)
            textStyle = "";
        this.textStyle = textStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getText();
    }
}
