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
 * terms of the Elements End-User License Agreeement (the Elements License), in
 * which case the provisions of the Elements License are applicable instead of
 * those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */
package glowaxes.glyphs;

import glowaxes.labeling.Instance;
import glowaxes.labeling.SimulatedAnnealing;
import glowaxes.labeling.Solution;
import glowaxes.plots.IPlotter;
import glowaxes.plots.PlotterFactory;
import glowaxes.tags.Constants;
import glowaxes.util.TypeConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class Area.
 */
public class Area extends SimpleGlyph {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Area.class.getName());

    /** The annotations. */
    private final ArrayList<Annotation> annotations =
            new ArrayList<Annotation>();

    /** The area attributes. */
    private final HashMap<String, Object> areaAttributes =
            new HashMap<String, Object>();

    /** The data. */
    private Data data = null;

    /** The index. */
    @SuppressWarnings("unused")
    private final int index = 0;

    // labeling algoritms
    /** The instance. */
    private Instance instance = null;

    /** The legend. */
    private Legend legend = null;

    /** The render. */
    private boolean render = false;

    /** The row glyphs. */
    private final RowGlyph rowGlyphs = null;

    private double x;

    /** The xaxis. */
    private XAxis xaxis = null; // new XAxis(null);

    /** The x markers. */
    private final ArrayList<XMarker> xMarkers = new ArrayList<XMarker>();

    /** The x scale. */
    private double xScale = 1;

    private double y;

    /** The yaxis. */
    private YAxis yaxis = null; // new YAxis(null);

    /** The y markers. */
    private final ArrayList<YMarker> yMarkers = new ArrayList<YMarker>();

    /** The y scale. */
    private double yScale = 1;

    /**
     * Constructor, instantiates a new area.
     * 
     * @param attributes
     *            the attributes
     * @param parent
     *            the parent
     */
    @SuppressWarnings("unchecked")
    public Area(HashMap attributes, Chart parent) {

        logger.info("construction Area");

        setParentChart(parent);
        setGlyphEffect((String) attributes.get("effect"));
        setGlyphRx(TypeConverter.getDouble(attributes.get("rx"), 0));
        setGlyphRy(TypeConverter.getDouble(attributes.get("ry"), 0));
        setGlyphStyle((String) attributes.get("style"));
        setId("area");
        setGlyphOffset(TypeConverter.getDouble(Constants
                .getDefault("area.min.offset"), 0));

        // dependent on combined areas stacked or overlay, legend and axis
        if (true) {
            this.setGlyphHeight(parent.getGlyphHeight()
                    - (parent.getChartTitle().getGlyphOffsetTop()
                            + parent.getChartTitle().getGlyphHeight() + parent
                            .getChartTitle().getGlyphOffsetBottom())
                    - (getGlyphOffsetTop() + getGlyphOffsetBottom()));

        }

        // dependent on legend floating or fixed and axis
        if (true) {
            setGlyphWidth(parent.getGlyphWidth()
                    - (getGlyphOffsetLeft() + getGlyphOffsetRight()));

        }

        // retrieve an Iterator from the list...
        Iterator tagIterator = attributes.keySet().iterator();

        // use the Iterator to visit each element in the list...
        while (tagIterator.hasNext()) {

            String name = (String) tagIterator.next();

            if (name.indexOf("xmarker") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding xmarker");
                XMarker xMarker = new XMarker((HashMap) attributes.get(name));
                xMarkers.add(xMarker);
            } else if (name.indexOf("ymarker") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding ymarker");
                YMarker yMarker = new YMarker((HashMap) attributes.get(name));
                yMarkers.add(yMarker);
            } else if (name.indexOf("annotation") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding annotation");
                Annotation annotation =
                        new Annotation((HashMap) attributes.get(name));
                annotations.add(annotation);
            } else if (name.indexOf("xaxis") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding xaxis");
                xaxis =
                        new XAxis((HashMap<String, Object>) attributes
                                .get(name), this);
            } else if (name.indexOf("yaxis") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding yaxis");
                yaxis =
                        new YAxis((HashMap<String, Object>) attributes
                                .get(name), this);
            } else if (name.indexOf("data") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding data");
                String dataId = (String) attributes.get(name);
                data = new Data(dataId, this);
            } else if (name.indexOf("legend") != -1) {
                if (logger.isDebugEnabled())
                    logger.debug("adding legend");
                ArrayList<Area> al = new ArrayList<Area>();
                al.add(this);
                legend =
                        new Legend((HashMap<String, Object>) attributes
                                .get(name), al);
            } else {
                if (logger.isDebugEnabled())
                    logger.debug("adding " + name);
                areaAttributes.put(name, attributes.get(name));
            }

        }

        // if there is a legend and its this legend add this area
        try {
            if (legend == null && getLegend() != null) {
                getLegend().addArea(this);
            }
        } catch (Exception e) {
        }

        IPlotter plot = PlotterFactory.getInstance(getType(), getData());
        setPlotter(plot);

    }

    /**
     * Gets the data.
     * 
     * @return the data
     */
    public Data getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphHeight()
     */
    @Override
    public double getGlyphHeight() {
        if (yaxis == null && getParentChart().getAreasSize() != 0) {
            if (getParentChart().getArea(0) == this) {
                throw new RuntimeException("No axis defined");
            }
            return getParentChart().getArea(0).getGlyphHeight();
        }
        return super.getGlyphHeight();
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphStyle()
     */
    @Override
    public String getGlyphStyle() {
        if (xaxis == null && yaxis == null)
            return "opacity:0";
        else
            return super.getGlyphStyle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.glyphs.SimpleGlyph#getGlyphWidth()
     */
    @Override
    public double getGlyphWidth() {
        if (yaxis == null && getParentChart().getAreasSize() != 0) {
            if (getParentChart().getArea(0) == this) {
                throw new RuntimeException("No axis defined");
            }
            return getParentChart().getArea(0).getGlyphWidth();
        }
        return super.getGlyphWidth();
    }

    /**
     * Gets the single instance of the Area.
     * 
     * @return single instance of the Area
     */
    public Instance getInstance() {
        if (yaxis == null && xaxis == null) {
            if (getParentChart().getArea(0).getInstance() == null) {
                throw new RuntimeException("No Instance defined");
            }
            return getParentChart().getArea(0).getInstance();
        } else {
            if (instance == null) {
                instance = new Instance();
            }
            return instance;
        }
    }

    /**
     * Gets the legend.
     * 
     * @return the legend
     */
    public Legend getLegend() {
        if (legend == null && getParentChart().getArea(0) != this)
            return getParentChart().getArea(0).getLegend();
        else
            return legend;
    }

    /**
     * Gets the parent chart.
     * 
     * @return the parent chart
     */
    public Chart getParentChart() {
        return (Chart) areaAttributes.get("chart");
    }

    /**
     * Gets the plotter.
     * 
     * @return the plotter
     */
    public IPlotter getPlotter() {
        return (IPlotter) areaAttributes.get("plotter");
    }

    /**
     * Gets the renderingWidth.
     * 
     * @return the rendering width
     */
    public String getRenderingWidth() {
        String renderingWidth =
                TypeConverter.getString(areaAttributes.get("renderingWidth"),
                        Constants.getDefault("area.renderingWidth"));
        return renderingWidth;
    }

    /**
     * Gets the type of plot in the area.
     * 
     * @return the type
     */
    public String getType() {
        return TypeConverter.getString(areaAttributes.get("type"), Constants
                .getDefault("area.type"));
    }

    /**
     * Gets the x axis.
     * 
     * @return the x axis
     */
    public XAxis getXAxis() {
        if (xaxis == null && getParentChart().getAreasSize() != 0) {
            if (getParentChart().getArea(0) == this) {
                return null;
            }
            return getParentChart().getArea(0).getXAxis();
        }
        return xaxis;
    }

    /**
     * Gets the x offset to chart (0,0).
     * 
     * @return the x offset to chart (0,0).
     */
    public double getXOffsetChart() {
        return x;
    }

    /**
     * Gets the x scale.
     * 
     * @return the x scale
     */
    public double getXScale() {
        return xScale;
    }

    /**
     * Gets the y axis.
     * 
     * @return the y axis
     */
    public YAxis getYAxis() {
        if (yaxis == null && getParentChart().getAreasSize() != 0) {
            if (getParentChart().getArea(0) == this) {
                return null;
            }
            return getParentChart().getArea(0).getYAxis();
        }
        return yaxis;
    }

    /**
     * Gets the y offset to chart (0,0).
     * 
     * @return the y offset to chart (0,0).
     */
    public double getYOffsetChart() {
        return y;
    }

    /**
     * Gets the y scale.
     * 
     * @return the y scale
     */
    public double getYScale() {
        return yScale;
    }

    /**
     * Checks for x axis.
     * 
     * @return true, if successful
     */
    public boolean hasXAxis() {
        if (xaxis == null)
            return false;
        return true;
    }

    /**
     * Checks for y axis.
     * 
     * @return true, if successful
     */
    public boolean hasYAxis() {
        if (yaxis == null)
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     */
    public Element renderHtml() {

        IPlotter plotter = getPlotter();
        return plotter.render();

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

        if (getLegend() != null) {
            getLegend().initLegend();
        }
        logger.info("renderGlyph(): " + getType());

        Element glyph = super.renderGlyph();
        Element masterGlyph = new Element("g");
        masterGlyph.setAttribute("desc", "holder");
        masterGlyph.addContent(glyph);

        IPlotter plotter = getPlotter();

        if (getLegend() != null) {
            plotter.addGlyph(getLegend());
        }

        // only render axis if its not a pie chart, and it has an axis
        if (!getType().equals("pie") && xaxis != null) {
            masterGlyph.addContent(xaxis.renderGlyph());
        }
        if (!getType().equals("pie") && yaxis != null) {
            masterGlyph.addContent(yaxis.renderGlyph());
        }

        masterGlyph.addContent(plotter.render());

        if (!getType().equals("pie")) {
            getXAxis().addAxisTo(glyph);
            getYAxis().addAxisTo(glyph);
        }

        plotter.processLabels();

        // add labels
        if (render) {
            getInstance().processList(getGlyphWidth(), getGlyphHeight());

            SimulatedAnnealing sa = new SimulatedAnnealing(getInstance());

            Solution solution = sa.getSolution();

            Element labels = solution.getSolution();

            masterGlyph.addContent(labels);
        }

        return masterGlyph;
    }

    /**
     * Sets the parent chart.
     * 
     * @param chart
     *            the new parent chart
     */
    private void setParentChart(Chart chart) {
        areaAttributes.put("chart", chart);
    }

    /**
     * Sets the plotter.
     * 
     * @param plotter
     *            the new plotter
     */
    private void setPlotter(IPlotter plotter) {
        areaAttributes.put("plotter", plotter);
    }

    /**
     * Sets if to render labels.
     * 
     * @param render
     *            if to render labels
     */
    public void setRenderLabels(boolean render) {
        this.render = render;
    }

    /**
     * Sets the x offset to chart (0,0).
     * 
     * @param the
     *            x offset to chart (0,0).
     */
    public void setXOffsetChart(double x) {
        this.x = x;
    }

    /**
     * Sets the x scale.
     * 
     * @param xScale
     *            the new x scale
     */
    public void setXScale(double xScale) {
        this.xScale = xScale;
    }

    /**
     * Gets the y offset to chart (0,0).
     * 
     * @param the
     *            y offset to chart (0,0).
     */
    public void setYOffsetChart(double y) {
        this.y = y;
    }

    /**
     * Sets the y scale.
     * 
     * @param yScale
     *            the new y scale
     */
    public void setYScale(double yScale) {
        this.yScale = yScale;
    }

    /**
     * To string.
     * 
     * @return a formatted string representation of the get methods
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        final String newLine = System.getProperty("line.separator");

        result.append(getClass().getName() + " Object {");

        result.append(xaxis);
        result.append(newLine);
        result.append(yaxis);
        result.append(newLine);
        result.append(data);
        result.append(newLine);
        result.append(rowGlyphs);
        result.append(newLine);
        result.append(legend);

        if (xMarkers.size() > 0) {
            for (Iterator<XMarker> i = xMarkers.iterator(); i.hasNext();) { // No
                // ForUpdate
                XMarker xMarker = i.next();
                result.append(newLine);
                result.append(xMarker);
            }
        }
        if (yMarkers.size() > 0) {
            for (Iterator<YMarker> i = yMarkers.iterator(); i.hasNext();) { // No
                // ForUpdate
                YMarker yMarker = i.next();
                result.append(newLine);
                result.append(yMarker);
            }
        }
        if (annotations.size() > 0) {
            for (Iterator<Annotation> i = annotations.iterator(); i.hasNext();) { // No
                // ForUpdate
                Annotation annotation = i.next();
                result.append(newLine);
                result.append(annotation);
            }
        }

        result.append("}");

        return result.toString();

    }
}
