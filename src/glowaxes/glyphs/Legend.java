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

import glowaxes.data.Group;
import glowaxes.data.Series;
import glowaxes.data.Value;
import glowaxes.util.TextProcessor;
import glowaxes.util.TypeConverter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class Legend.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Legend extends SimpleGlyph {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Legend.class.getName());

    /** The areas. */
    private final ArrayList<Area> areas;

    /** The columns. */
    ColumnGlyph columns;

    /** The legend attributes. */
    private HashMap<String, Object> legendAttributes =
            new HashMap<String, Object>();

    /** The legend offset. */
    private final double legendOffset = 5;

    /** The legend title. */
    private TextGlyph legendTitle;

    /**
     * Instantiates a new legend.
     * 
     * @param _legendAttributes
     *            the _legend attributes
     * @param areas
     *            the areas
     */
    public Legend(HashMap<String, Object> _legendAttributes,
            ArrayList<Area> areas) {

        logger.info("construction Legend");

        legendAttributes = _legendAttributes;

        this.areas = areas;

        setId("legend");

        if (_legendAttributes.get("titleEffect") != null
                && ((String) _legendAttributes.get("titleEffect")).trim()
                        .equals(""))
            _legendAttributes.put("titleEffect", null);

        setLegendTitle(new ChartTitle((String) _legendAttributes.get("title"),
                (String) _legendAttributes.get("titleStyle"),
                (String) _legendAttributes.get("titleEffect"),
                (String) _legendAttributes.get("titleAlign"),
                (String) _legendAttributes.get("titleValign")));

        setGlyphRx(TypeConverter.getDouble(_legendAttributes.get("rx"), 10));
        setGlyphRy(TypeConverter.getDouble(_legendAttributes.get("ry"), 10));

        setGlyphEffect((String) _legendAttributes.get("effect"));
        setGlyphStyle((String) _legendAttributes.get("background"));

        setGlyphOffset(legendOffset);

    }

    /**
     * Adds the area.
     * 
     * @param area
     *            the area
     */
    public void addArea(Area area) {
        areas.add(area);
    }

    /**
     * Gets the format, the legend format The legend's format, nothing defaults
     * to {sym} {series}.
     * 
     * @return the format
     */
    public String getFormat() {

        return TypeConverter.getString(legendAttributes.get("format"),
                "{symbol} {series}");
    }

    /**
     * Gets the glyph.
     * 
     * @param aToken
     *            the a token
     * @param series
     *            the series
     * @param area
     *            the area
     * 
     * @return the glyph
     */
    private IGlyph getGlyph(String aToken, Series series, Area area) {

        IGlyph myGlyph;

        String token = "{" + aToken + "}";

        if (token.equals("{symbol}")) {

            double size =
                    TypeConverter.getDouble(TextProcessor
                            .getSemiColonSeparatedValue(getLabelStyle(),
                                    "font-size"), 12) + 1;
            myGlyph = new SymbolGlyph(size, size, series, area.getType());
            myGlyph.setGlyphOffset(0);
            myGlyph.setGlyphOffsetRight(4);
            myGlyph.setGlyphOffsetLeft(4);
            myGlyph.setGlyphOffsetTop(2);
            myGlyph.setGlyphOffsetBottom(0);
            myGlyph.setGlyphVAlign("top");

        } else if (token.equals("{xmin}")) {

            myGlyph =
                    new ChartTitle(area.getXAxis().getFormat(series.getXMin()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{xmax}")) {

            myGlyph =
                    new ChartTitle(area.getXAxis().getFormat(series.getXMax()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{xtotal}")) {

            myGlyph =
                    new ChartTitle(area.getXAxis()
                            .getFormat(series.getXTotal()).toString(),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{ymin}")) {

            myGlyph =
                    new ChartTitle(area.getYAxis().getFormat(series.getYMin()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{ymax}")) {

            myGlyph =
                    new ChartTitle(area.getYAxis().getFormat(series.getYMax()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{ytotal}")) {

            myGlyph =
                    new ChartTitle(area.getYAxis()
                            .getFormat(series.getYTotal()), getLabelStyle(),
                            "", "left", "top", 2);

        } else if (token.equals("{size}")) {

            myGlyph =
                    new ChartTitle("" + series.getSize(), getLabelStyle(), "",
                            "left", "top", 2);

        } else if (token.equals("{series}") || token.equals("{value}")) {

            myGlyph =
                    new ChartTitle(series.getLegend(), getLabelStyle(), "",
                            "left", "top", 2);

        } else {
            myGlyph =
                    new ChartTitle(aToken, getLabelStyle(), "", "left", "top",
                            3);
            if (aToken.length() <= 1) {
                myGlyph.setGlyphOffset(0);
                myGlyph.setGlyphOffsetRight(1);
            }
        }
        return myGlyph;

    }

    /**
     * Gets the glyph.
     * 
     * @param aToken
     *            the a token
     * @param value
     *            the value
     * @param area
     *            the area
     * 
     * @return the glyph
     */
    private IGlyph getGlyph(String aToken, Value value, Area area) {

        IGlyph myGlyph;

        String token = "{" + aToken + "}";

        if (token.equals("{symbol}")) {

            double size =
                    TypeConverter.getDouble(TextProcessor
                            .getSemiColonSeparatedValue(getLabelStyle(),
                                    "font-size"), 12) + 1;
            myGlyph = new SymbolGlyph(size, size, value, area.getType());
            myGlyph.setGlyphOffset(0);
            myGlyph.setGlyphOffsetRight(4);
            myGlyph.setGlyphOffsetLeft(4);
            myGlyph.setGlyphOffsetTop(2);
            myGlyph.setGlyphOffsetBottom(0);
            myGlyph.setGlyphVAlign("top");

        } else if (token.equals("{xmin}")) {

            myGlyph =
                    new ChartTitle(area.getXAxis().getFormat(
                            value.getParentSeries().getXMin()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{xmax}")) {

            myGlyph =
                    new ChartTitle(area.getXAxis().getFormat(
                            value.getParentSeries().getXMax()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{xtotal}")) {

            myGlyph =
                    new ChartTitle(area.getXAxis().getFormat(
                            value.getParentSeries().getXTotal()).toString(),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{x%}")) {

            double total = value.getParentSeries().getXTotal();
            double percent = value.getX() * 100 / total;
            myGlyph =
                    new ChartTitle(TypeConverter.r10(percent) + "%",
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{ymin}")) {

            myGlyph =
                    new ChartTitle(area.getYAxis().getFormat(
                            value.getParentSeries().getYMin()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{ymax}")) {

            myGlyph =
                    new ChartTitle(area.getYAxis().getFormat(
                            value.getParentSeries().getYMax()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{ytotal}")) {

            myGlyph =
                    new ChartTitle(area.getYAxis().getFormat(
                            value.getParentSeries().getYTotal()),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{y%}")) {

            double total = value.getParentSeries().getYTotal();
            double percent = value.getY() * 100 / total;
            myGlyph =
                    new ChartTitle(TypeConverter.r10(percent) + "%",
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{size}")) {

            myGlyph =
                    new ChartTitle("" + value.getParentSeries().getSize(),
                            getLabelStyle(), "", "left", "top", 2);

        } else if (token.equals("{series}") || token.equals("{value}")
                || token.equals("{label}")) {

            myGlyph =
                    new ChartTitle((value.getLabel() != null) ? value
                            .getLabel().getText().toString() : "",
                            getLabelStyle(), "", "left", "top", 2);

        } else {
            myGlyph =
                    new ChartTitle(aToken, getLabelStyle(), "", "left", "top",
                            3);
            if (aToken.length() <= 1) {
                myGlyph.setGlyphOffset(0);
                myGlyph.setGlyphOffsetRight(1);
            }
        }

        return myGlyph;
    }

    /**
     * Gets the labelEffect, the legend's label effect.
     * 
     * @return the label effect
     */
    public String getLabelEffect() {

        String effect =
                TypeConverter.getString(legendAttributes.get("labelEffect"),
                        null);

        // todo remove me
        if (effect != null && effect.trim().equals(""))
            effect = null;

        return effect;
    }

    /**
     * Gets the labelStyle, the legend's label style.
     * 
     * @return the label style
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     */
    public String getLabelStyle() {

        return TypeConverter.getString(legendAttributes.get("labelStyle"),
                "font-size:12;");
    }

    /**
     * Gets the legend title.
     * 
     * @return the legend title
     */
    public TextGlyph getLegendTitle() {
        return legendTitle;
    }

    /**
     * Gets the line.
     * 
     * @param area
     *            the area
     * @param series
     *            the series
     * 
     * @return the line
     */
    private RowGlyph getLine(Area area, Series series) {

        // logger.error(getFormat());

        RowGlyph row = new RowGlyph();

        String tokens[] = getFormat().split("\\s|\\{|\\}");

        for (int i = 0; i < tokens.length; i++) {
            // logger.error("tokens[i]: '" + tokens[i] + "'");
            if (tokens[i].length() == 0)
                continue;
            IGlyph glyph = getGlyph(tokens[i], series, area);
            row.addGlyph(glyph);
        }
        return row;
    }

    /**
     * Gets the line.
     * 
     * @param area
     *            the area
     * @param value
     *            the value
     * 
     * @return the line
     */
    private RowGlyph getLine(Area area, Value value) {

        // logger.error(getFormat());

        RowGlyph row = new RowGlyph();

        String tokens[] = getFormat().split("\\s|\\{|\\}");

        for (int i = 0; i < tokens.length; i++) {
            // logger.error("tokens[i]: '" + tokens[i] + "'");
            if (tokens[i].length() == 0)
                continue;
            IGlyph glyph = getGlyph(tokens[i], value, area);
            row.addGlyph(glyph);
        }
        return row;
    }

    /**
     * Gets the strategy, the way legends are rendered ([column]|row).
     * 
     * @return the strategy
     */
    public String getStrategy() {
        String strategy =
                TypeConverter.getString(legendAttributes.get("strategy"),
                        "columns");
        if (strategy.equals("columns")) {
            return strategy;
        } else
            return "rows";

    }

    /**
     * Inits the column legend.
     */
    public void initColumnLegend() {

        if (logger.isDebugEnabled())
            logger.debug("initColumnLegend");
        columns = new ColumnGlyph();

        double titleHeight = 0;
        if (!getLegendTitle().getText().equals("")) {
            getLegendTitle().setGlyphOffset(0);
            getLegendTitle().setGlyphOffsetTop(2);
            getLegendTitle().setGlyphOffsetLeft(4);
            getLegendTitle().setGlyphOffsetRight(4);
            columns.addGlyph(getLegendTitle());
            titleHeight = columns.getGlyphHeight();
        }
        double wrapLength =
                areas.get(0).getGlyphHeight() - titleHeight - 2 * legendOffset;

        if (areas.get(0).getParentChart().getLegend() != null) {
            wrapLength =
                    areas.get(0).getParentChart().getGlyphHeight()
                            - titleHeight - 2 * legendOffset;
        }

        ColumnGlyph aColumn = new ColumnGlyph();
        RowGlyph rowWrap = new RowGlyph();

        for (int i = 0; i < areas.size(); i++) {

            Area area = areas.get(i);
            Group group = area.getData().getGroup();

            if (area.getType().equals("pie")) {
                for (int j = 0; j < group.getSize(); j++) {
                    for (int k = 0; k < group.getSeries(j).getSize(); k++) {

                        RowGlyph line =
                                getLine(area, group.getSeries(j).getValue(k));
                        line.setGlyphOffsetLeft(5);

                        if (aColumn.getGlyphHeight() + line.getGlyphHeight() > wrapLength) {
                            rowWrap.addGlyph(new ColumnGlyph(aColumn));
                            aColumn = new ColumnGlyph();
                            aColumn.addGlyph(line);
                        } else {
                            aColumn.addGlyph(line);
                        }
                    }
                }
            } else {
                for (int j = group.getSize() - 1; j >= 0; j--) {
                    RowGlyph line = getLine(area, group.getSeries(j));
                    line.setGlyphOffsetLeft(5);
                    if (aColumn.getGlyphHeight() + line.getGlyphHeight() > wrapLength) {
                        rowWrap.addGlyph(new ColumnGlyph(aColumn));
                        aColumn = new ColumnGlyph();
                        aColumn.addGlyph(line);
                    } else {
                        aColumn.addGlyph(line);
                    }
                }
            }
        }

        rowWrap.addGlyph(aColumn);
        columns.addGlyph(rowWrap);

        columns.calculateDimensions();
        setGlyphHeight(columns.getGlyphHeight());
        setGlyphWidth(columns.getGlyphWidth());
    }

    /**
     * Inits the legend.
     */
    public void initLegend() {

        // logger.error("strategy: " + getStrategy());
        if (getStrategy().equals("rows")) {
            initRowLegend();
        } else {
            initColumnLegend();
        }

        // not needed if nested in chart
        if (legendAttributes.get("align") != null)
            setGlyphAlign((String) legendAttributes.get("align"));

        // not needed if nested in chart
        if (legendAttributes.get("valign") != null)
            setGlyphVAlign((String) legendAttributes.get("valign"));
    }

    /**
     * Inits the row legend.
     */
    public void initRowLegend() {

        if (logger.isDebugEnabled())
            logger.debug("initRowLegend");

        columns = new ColumnGlyph();

        if (!getLegendTitle().getText().equals("")) {
            getLegendTitle().setGlyphOffset(0);
            getLegendTitle().setGlyphOffsetTop(2);
            getLegendTitle().setGlyphOffsetLeft(4);
            getLegendTitle().setGlyphOffsetRight(4);
            columns.addGlyph(getLegendTitle());
        }

        double wrapLength = areas.get(0).getGlyphWidth() - 4 * legendOffset;
        // logger.error(wrapLength);
        if (areas.get(0).getParentChart().getLegend() != null) {
            wrapLength =
                    areas.get(0).getParentChart().getGlyphWidth() - 4
                            * legendOffset;
        }
        // logger.error(wrapLength);

        ArrayList<ColumnGlyph> columnList = new ArrayList<ColumnGlyph>();
        RowGlyph wrapable = new RowGlyph();
        boolean wrapped = false;
        int columnCount = 0;

        for (int i = 0; i < areas.size(); i++) {

            Area area = areas.get(i);
            Group group = area.getData().getGroup();

            double width = 0;

            if (area.getType().equals("pie")) {
                for (int j = 0; j < group.getSize(); j++) {
                    for (int k = 0; k < group.getSeries(j).getSize(); k++) {

                        RowGlyph line =
                                getLine(area, group.getSeries(j).getValue(k));
                        line.setGlyphOffsetLeft(legendOffset);
                        line.calculateDimensions();

                        width = width + line.getGlyphWidth();

                        if (!wrapped && width > wrapLength) {
                            wrapped = true;
                            columnList.get(columnCount).addGlyph(line);
                            columnCount++;
                            if (columnCount >= columnList.size())
                                columnCount = 0;
                        } else if (!wrapped) {
                            ColumnGlyph cg = new ColumnGlyph();
                            columnList.add(cg);
                            columnList.get(columnList.size() - 1)
                                    .addGlyph(line);
                            wrapable.addGlyph(columnList
                                    .get(columnList.size() - 1));
                        } else {
                            columnList.get(columnCount).addGlyph(line);
                            columnCount++;
                            if (columnCount >= columnList.size())
                                columnCount = 0;
                        }
                    }
                }
            } else {
                for (int j = 0; j < group.getSize(); j++) {
                    RowGlyph line = getLine(area, group.getSeries(j));
                    line.setGlyphOffsetLeft(legendOffset);
                    line.calculateDimensions();
                    width = width + line.getGlyphWidth();
                    if (!wrapped && width > wrapLength) {
                        wrapped = true;
                        columnList.get(columnCount).addGlyph(line);
                        columnCount++;
                        if (columnCount >= columnList.size())
                            columnCount = 0;
                    } else if (!wrapped) {
                        ColumnGlyph cg = new ColumnGlyph();
                        columnList.add(cg);
                        columnList.get(columnList.size() - 1).addGlyph(line);
                        wrapable
                                .addGlyph(columnList.get(columnList.size() - 1));
                    } else {
                        columnList.get(columnCount).addGlyph(line);
                        columnCount++;
                        if (columnCount >= columnList.size())
                            columnCount = 0;
                    }
                }
            }
        }
        wrapable.setGlyphOffsetBottom(5);
        columns.addGlyph(new RowGlyph(wrapable));

        columns.calculateDimensions();
        setGlyphHeight(columns.getGlyphHeight() + legendOffset);
        setGlyphWidth(columns.getGlyphWidth());

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

        Element glyph = super.renderGlyph();
        glyph.addContent(columns.renderGlyph());
        return glyph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#setGlyphAlign(java.lang.String)
     */
    @Override
    public void setGlyphAlign(String align) {

        super.setGlyphAlign(align);

        if (areas.get(0).getParentChart().getLegend() != null) {
            if (align.equals("left")) {
                setGlyphX(getGlyphOffsetLeft());
            } else if (align.equals("right")) {
                setGlyphX(areas.get(0).getParentChart().getGlyphWidth()
                        - (getGlyphWidth() + getGlyphOffsetRight()));
            } else {
                setGlyphX((areas.get(0).getParentChart().getGlyphWidth() - getGlyphWidth()) / 2);
            }
        }

        if (align.equals("left")) {
            setGlyphX(getGlyphOffsetLeft());
        } else if (align.equals("right")) {
            setGlyphX(areas.get(0).getGlyphWidth()
                    - (getGlyphWidth() + getGlyphOffsetRight()));
        } else {
            setGlyphX((areas.get(0).getGlyphWidth() - getGlyphWidth()) / 2);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#setGlyphVAlign(java.lang.String)
     */
    @Override
    public void setGlyphVAlign(String vAlign) {

        super.setGlyphVAlign(vAlign);

        if (areas.get(0).getParentChart().getLegend() != null) {
            if (vAlign.equals("top")) {
                setGlyphY(getGlyphOffsetTop());
            } else if (vAlign.equals("bottom")) {
                setGlyphY(areas.get(0).getParentChart().getGlyphHeight()
                        - (getGlyphHeight() + getGlyphOffsetBottom()));
            } else {
                setGlyphY((areas.get(0).getParentChart().getGlyphHeight() - getGlyphHeight()) / 2);
            }
        }

        if (vAlign.equals("top")) {
            setGlyphY(getGlyphOffsetTop());
        } else if (vAlign.equals("bottom")) {
            setGlyphY(areas.get(0).getGlyphHeight()
                    - (getGlyphHeight() + getGlyphOffsetBottom()));
        } else {
            setGlyphY((areas.get(0).getGlyphHeight() - getGlyphHeight()) / 2);
        }
    }

    /**
     * Sets the legend title.
     * 
     * @param legendTitle
     *            the new legend title
     */
    public void setLegendTitle(TextGlyph legendTitle) {
        this.legendTitle = legendTitle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getLegendTitle().getText();

    }
}
