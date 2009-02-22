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

import org.apache.log4j.Logger;

/**
 * The Class ChartTitle represents a chart's title.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class ChartTitle extends TextGlyph {

    /** The logger. */
    private static Logger logger = Logger.getLogger(ChartTitle.class.getName());

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        double textRotation = -350;

        System.out.println(textRotation % 180);

        System.out.println(Math.ceil(textRotation / 180));
        System.out.println(Math.pow(-1, Math.ceil(textRotation / 180)));

        if (textRotation > 180 || textRotation < -180) {
            textRotation =
                    (textRotation % 180) - 180
                            * Math.pow(-1, Math.ceil(textRotation / 180));
        }

        System.out.println(textRotation);

        ChartTitle chartTitle =
                new ChartTitle("Testing description",
                        "font-size:20;stroke:navy;stroke-width:1;", null,
                        "Align", "Valign");
        // chartTitle.setGlyphStyle("fill:purple;stroke:black;opacity:0.8");
        // chartTitle.setGlyphRx(10);
        logger.error(chartTitle.getTextStyle());
        chartTitle.setId("Testing description");
        chartTitle.setTextOffset(0);
        chartTitle.setGlyphStyle("fill:red;stroke:black;opacity:0.8");
        chartTitle.setGlyphAlign("center");
        chartTitle.setGlyphVAlign("bottom");
        chartTitle.setGlyphOffset(30);

        logger.error(chartTitle.getTextStyle());
        chartTitle.setTextRotation(-45);
        chartTitle.setText("Testing");
        // chartTitle.setTextRotation(-91);

        // SimpleGlyph sg = new SimpleGlyph();
        // sg.setGlyphStyle("fill:blue;stroke:black;opacity:0.8");
        // sg.setId("a simple container");
        // sg.setGlyphWidth(500);
        // sg.setGlyphHeight(500);

        // Element element = sg.renderGlyph();
        // Renderer.print(element);
        // chartTitle.setGlyphX(500);
        // chartTitle.setGlyphY(200);
        // chartTitle.setText("-179000");
        // chartTitle.setTextOffset(0);
        // chartTitle.setTextRotation(-109);
        // element = chartTitle.renderGlyph();
        // Renderer.print(element);
        //
        // chartTitle.setGlyphX(300);
        // chartTitle.setGlyphY(400);
        // chartTitle.setGlyphStyle("fill:blue;stroke:black;opacity:0.8");
        // chartTitle.setGlyphRx(10);
        // chartTitle.setText("-179000");
        // chartTitle.setTextOffset(5);
        // chartTitle.setTextRotation(-180);
        // element = chartTitle.renderGlyph();
        // Renderer.print(element);
        //
        // chartTitle.setGlyphX(100);
        // chartTitle.setGlyphY(200);
        // chartTitle.setGlyphStyle("fill:green;stroke:black;opacity:0.8");
        // chartTitle.setGlyphRx(10);
        // chartTitle.setText("-179000");
        // chartTitle.setTextOffset(5);
        // chartTitle.setTextRotation(0);
        // element = chartTitle.renderGlyph();
        // Renderer.print(element);
        //
        // chartTitle.setGlyphX(0);
        // chartTitle.setGlyphY(0);
        // chartTitle.setGlyphStyle("fill:orange;stroke:black;opacity:0.8");
        // chartTitle.setGlyphRx(10);
        // chartTitle.setText("-179000");
        // chartTitle.setTextOffset(5);
        // chartTitle.setTextRotation(0);
        // element = chartTitle.renderGlyph();
        // Renderer.print(element);
        //
        // Renderer.print(element);
        // chartTitle.setGlyphX(500);
        // chartTitle.setGlyphY(500);
        // chartTitle.setGlyphStyle("fill:purple;stroke:black;opacity:0.8");
        // chartTitle.setGlyphRx(10);
        // chartTitle.setText("-179000");
        // chartTitle.setTextOffset(2);
        // chartTitle.setTextRotation(60);
        // element = chartTitle.renderGlyph();
        // Renderer.print(element);
        /*
         * checked -0 to -90 chartTitle.setText("-10000");
         * chartTitle.setTextRotation(-10); element = chartTitle.renderGlyph();
         * Renderer.print(element);
         * 
         * chartTitle.setText("-80000"); chartTitle.setTextRotation(-80);
         * element = chartTitle.renderGlyph(); Renderer.print(element);
         */

        /*
         * checked 0 to 180 chartTitle.setText("10000");
         * chartTitle.setTextRotation(10); element = chartTitle.renderGlyph();
         * Renderer.print(element); chartTitle.setText("000000");
         * chartTitle.setTextRotation(0); element = chartTitle.renderGlyph();
         * Renderer.print(element); chartTitle.setText("90000");
         * chartTitle.setTextRotation(90); element = chartTitle.renderGlyph();
         * Renderer.print(element); chartTitle.setText("170009");
         * chartTitle.setTextRotation(179); element = chartTitle.renderGlyph();
         * Renderer.print(element);
         */

    }

    /**
     * Instantiates a new chart title.
     * 
     * @param text the text
     * @param style the style
     * @param effect the effect
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public ChartTitle(String text, String style, String effect, double x,
            double y) {
        setText(text);
        setTextStyle(style);
        setTextEffect(effect);
        setGlyphX(x);
        setGlyphY(y);
        setGlyphOffset(2);
        // this.setGlyphStyle("fill:orange");
    }

    /**
     * Instantiates a new chart title.
     * 
     * @param text the text
     * @param style the style
     * @param effect the effect
     * @param align the align
     * @param rotation the rotation
     */
    public ChartTitle(String text, String style, String effect, String align,
            double rotation) {
        setText(text);
        setTextStyle(style);
        setTextEffect(effect);
        setGlyphAlign(align);
//        setTextAlign(align);
        setTextRotation(rotation);
        setGlyphOffset(0);
        setTextOffset(0);
    }

    /**
     * Instantiates a new chart title.
     * 
     * @param text the text
     * @param style the style
     * @param effect the effect
     * @param align the align
     * @param vAlign the v align
     */
    public ChartTitle(String text, String style, String effect, String align,
            String vAlign) {
        setText(text);
        setTextStyle(style);
        setTextEffect(effect);
        setGlyphAlign(align);
//        setTextAlign(align);
        setGlyphVAlign(vAlign);
        setGlyphOffset(2);
    }

    //used for making legends
    /**
     * Instantiates a new chart title.
     * 
     * @param text the text
     * @param style the style
     * @param effect the effect
     * @param align the align
     * @param vAlign the v align
     * @param offsetRight the offset right
     */
    public ChartTitle(String text, String style, String effect, String align,
            String vAlign, double offsetRight) {
        setText(text);
        setTextStyle(style);
        setTextEffect(effect);
        setGlyphAlign(align);
//        setTextAlign(align);
        setGlyphVAlign(vAlign);
        setGlyphOffset(0);
        setGlyphOffsetRight(offsetRight);
        setGlyphOffsetTop(2);
        setGlyphOffsetBottom(1);
    }

    /**
     * Gets the plot x.
     * 
     * @return the plot x
     */
    public double getPlotX() {

        double offset =
                getTextHeight()
                        * Math.abs(Math.sin(this.getTextRotation() * Math.PI
                                / 180)) / 2;

        if (this.getGlyphAlign().equals("left")) {
            if (getTextRotation() > 0 && getTextRotation() <= 90)
                return getTextX() - offset;
            else if (getTextRotation() > 90 && getTextRotation() <= 180)
                return getTextX() - getGlyphWidth() + offset;
            else if (getTextRotation() < 0 && getTextRotation() >= -90)
                return getTextX() - getGlyphWidth() + offset;
            else if (getTextRotation() < -90 && getTextRotation() >= -180)
                return getTextX() - offset;
        } else if (this.getGlyphAlign().equals("center")) {
            return getTextX() - getGlyphWidth();
        } else if (this.getGlyphAlign().equals("right")) {
            return getTextX();
        }

        return getTextX();
    }

    /**
     * Gets the plot y.
     * 
     * @return the plot y
     */
    public double getPlotY() {
        double offset =
                getTextHeight()
                        * Math.abs(Math.cos(this.getTextRotation() * Math.PI
                                / 180)) / 2;

        if (this.getGlyphAlign().equals("right")) {
            if (getTextRotation() >= 0 && getTextRotation() <= 90)
                return getTextY() + getGlyphHeight() - offset;
            else if (getTextRotation() > 90 && getTextRotation() <= 180)
                return getTextY() + offset;// - getGlyphHeight() ;
            else if (getTextRotation() < 0 && getTextRotation() >= -90)
                return getTextY() + offset;
            else if (getTextRotation() < -90 && getTextRotation() >= -180)
                return getTextY() + getGlyphHeight() - offset;
        }

        return getTextY();
    }

    /* (non-Javadoc)
     * @see glowaxes.glyphs.TextGlyph#toString()
     */
    public String toString() {
        return this.getText() + "{" + this.getGlyphX() + ", "
                + this.getGlyphY() + "}";
    }

}
