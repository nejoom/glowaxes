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

import glowaxes.data.Group;
import glowaxes.servlet.ImageServlet;
import glowaxes.tags.Constants;
import glowaxes.tags.Defs;
import glowaxes.util.AreaMap;
import glowaxes.util.ChartOutputter;
import glowaxes.util.Configuration;
import glowaxes.util.CreateImage;
import glowaxes.util.Encrypt;
import glowaxes.util.JSPHelper;
import glowaxes.util.TypeConverter;
import glowaxes.util.XOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;

/**
 * The Class Chart.
 */
public class Chart extends SimpleGlyph {

    /** The logger. */
    private static Logger logger = Logger.getLogger(Chart.class.getName());

    /**
     * Change namespace.
     * 
     * @param element
     *            the element
     * @param to
     *            the namespace to change to
     * 
     * @return the element
     */
    static Element changeNamespace(Element element, Namespace to) {
        return changeNamespace(element, element.getNamespace(), to);
    }

    /**
     * Change namespace.
     * 
     * @param element
     *            the element
     * @param from
     *            the namespace to change from
     * @param to
     *            the namespace to change to
     * 
     * @return the element
     */
    @SuppressWarnings("unchecked")
    static Element changeNamespace(Element element, Namespace from, Namespace to) {
        Namespace ns = element.getNamespace();

        if (!ns.equals(from))
            return element;

        /*
         * Uncomment to change attribute namespaces.... for (Iterator ait =
         * emt.getAttributes().iterator(); ait.hasNext();) { Attribute att =
         * (Attribute)ait.next(); if (att.getNamespace().equals(from) &&
         * !att.getNamespace().equals(to)) { att.setNamespace(to);
         * System.out.println(att.getName()); } }
         */
        if (!ns.equals(to)) {
            element.setNamespace(to);
        }
        for (Iterator<Element> it = element.getChildren().iterator(); it.hasNext();) {
            changeNamespace(it.next(), from, to);
        }
        return element;
    }

    /** The areas. */
    private final ArrayList<Area> areas = new ArrayList<Area>();

    /** The chart attributes. */
    private final HashMap<String, Object> chartAttributes =
            new HashMap<String, Object>();

    /** The chart title. */
    private final ChartTitle chartTitle;

    /** The CPAL license. */
    private final int CPALLicense = 1;

    @SuppressWarnings("unused")
    private final int EnterpriseLicense = 3;

    /** The html area map. */
    private AreaMap htmlAreaMap;

    /** The legend. */
    private Legend legend = null;

    @SuppressWarnings("unused")
    private final int TLDLicense = 2;

    /**
     * Instantiates a new chart.
     * 
     * @param map
     *            the map
     */
    @SuppressWarnings("unchecked")
    public Chart(LinkedHashMap map) {

        logger.info("construction Chart");
        chartAttributes.put("output", map.get("output"));
        chartTitle =
                new ChartTitle((String) map.get("title"), (String) map
                        .get("titleStyle"), (String) map.get("titleEffect"),
                        (String) map.get("titleAlign"), (String) map
                                .get("titleValign"));
        chartTitle.setId("title");

        setGlyphHeight(TypeConverter.getDouble(map.get("height"), 300));
        setGlyphStyle((String) map.get("style"));
        setGlyphWidth(TypeConverter.getDouble(map.get("width"), 600));
        setId((String) map.get("id"));

        setGlyphRx(TypeConverter.getDouble(map.get("rx"), 0));
        setGlyphRy(TypeConverter.getDouble(map.get("ry"), 0));

        logger.info("Setting output type to: " + getOutput());

        // initialize pageContext for data in area
        chartAttributes.put("pageContext", map.get("pageContext"));
        for (Iterator tagIterator = map.keySet().iterator(); tagIterator
                .hasNext();) {

            String name = (String) tagIterator.next();

            if (name.indexOf("area") != -1) {

                Area myArea =
                        new Area((HashMap<Object, Object>) map.get(name), this);
                areas.add(myArea);

            } else if (name.indexOf("legend") != -1) {
                legend =
                        new Legend((HashMap<String, Object>) map.get(name),
                                areas);
                if (logger.isDebugEnabled())
                    logger.debug("adding legend: "
                            + legend.getLegendTitle().toString());
            } else {
                chartAttributes.put(name, map.get(name));
            }
        }

        if (getOutput().indexOf("html") == -1) {
            setTicMarks();
        }
    }

    /**
     * Gets the area.
     * 
     * @param index
     *            the index
     * 
     * @return the area
     */
    public Area getArea(int index) {
        return areas.get(index);
    }

    /**
     * Gets the area grouping.
     * 
     * @return the area grouping
     */
    public String getAreaGrouping() {
        // force tile-vertical as default
        String areaGrouping =
                TypeConverter.getString(chartAttributes.get("areaGrouping"),
                        "tile-vertical");
        // force tile-vertical as default
        if (!Constants.areaGroupingVector.contains(areaGrouping))
            areaGrouping = "tile-vertical";
        return areaGrouping;
    }

    /**
     * Gets the area map.
     * 
     * @return the area map
     */
    public String getAreaMap() {
        if (htmlAreaMap == null)
            return "";
        return htmlAreaMap.getAreaMap();
    }

    /**
     * Gets the areas size.
     * 
     * @return the areas size
     */
    public int getAreasSize() {
        return areas.size();
    }

    /**
     * Gets the chart title.
     * 
     * @return the chart title
     */
    public ChartTitle getChartTitle() {

        return chartTitle;
    }

    /**
     * Gets the expire time.
     * 
     * @return the expire time
     */
    public long getExpireTime() {
        long expireTime =
                System.currentTimeMillis()
                        + TypeConverter.getLong(chartAttributes
                                .get("expiretime"), TypeConverter.getLong(
                                Configuration.getInstance().getValue(
                                        "glowaxes", "Renderer.expiretime"),
                                5 * 60 * 1000));
        return expireTime;
    }

    /**
     * Gets the image.
     * 
     * @return the image
     */
    public String getImage() {
        return TypeConverter.getString(chartAttributes.get("image"), Constants
                .getDefault("chart.image"));
    }

    /**
     * Gets the effect.
     * 
     * @return the effect
     */
    public String getImageEffect() {
        return TypeConverter
                .getString(chartAttributes.get("imageEffect"), null);
    }

    /**
     * Gets the legend.
     * 
     * @return the legend
     */
    public Legend getLegend() {
        return legend;
    }

    /**
     * Gets the image style.
     * 
     * @return the image style
     */
    // public String getImageStyle() {
    // return (String) chartAttributes.get("imageStyle");
    // }
    /**
     * Gets the license.
     * 
     * @return the License
     */
    public int getLicense() {
        int license = CPALLicense;
        return license;
    }

    /**
     * Gets output method of a chart component.
     * 
     * @return output The output of the chart
     */
    public String getOutput() {

        String output = (String) chartAttributes.get("output");

        return output;
    }

    /**
     * Gets the page context.
     * 
     * @return the page context
     */
    public PageContext getPageContext() {
        return (PageContext) chartAttributes.get("pageContext");
    }

    /**
     * Gets the popup map.
     * 
     * @return the popup map
     */
    public String getPopupMap() {
        if (htmlAreaMap == null)
            return "";
        return htmlAreaMap.getPopupMap();
    }

    /**
     * Gets the result.
     * 
     * @return the result
     */
    public String getResult() {

        HttpServletRequest request =
                (HttpServletRequest) this.getPageContext().getRequest();
        HttpServletResponse response =
                (HttpServletResponse) this.getPageContext().getResponse();

        Format formatter = Format.getRawFormat();
        formatter.setEncoding("UTF-8");
        formatter.setIndent("  ");
        formatter.setLineSeparator(System.getProperty("line.separator"));

        ChartOutputter serializer = new ChartOutputter(formatter);

        Document doc = getSVGChart();
        String xml = "";

        try {
            StringWriter sw = new StringWriter();
            serializer.output(doc, sw);
            String _xml = sw.toString();

            logger.info(_xml);

        } catch (Exception e) {
            logger.fatal(e);
        }

        if (getOutput().equals("svg")) {

            try {
                StringWriter sw = new StringWriter();
                serializer.output(doc, sw);
                xml = sw.toString();
            } catch (Exception e) {
                logger.fatal(e);
            }
            response.setContentType("image/svg+xml;charset=UTF-8");
            JSPHelper.setNoCache(request, response);

        } else if (getOutput().indexOf("html") != -1) {

            try {
                StringWriter sw = new StringWriter();
                serializer.output(doc, sw);
                xml = sw.toString();
            } catch (Exception e) {
                logger.fatal(e);
            }
            response.setContentType("text/html;charset=UTF-8");
            JSPHelper.setNoCache(request, response);

        } else if (getOutput().equals("png") || getOutput().equals("jpg")
                || getOutput().equals("tiff") || getOutput().equals("pdf")) {

            XOutputter outputter = new XOutputter();

            outputter.setForceNamespaceAware(true);

            org.w3c.dom.Document document = null;

            // the root of the webapp
            String contextPath = request.getContextPath();

            // the directory that triggers the ImageServlet to work
            String directoryOut = "error";
            try {
                document = outputter.output(doc);
                directoryOut =
                        ImageServlet.getUrlPattern(getPageContext()
                                .getServletContext());
            } catch (JDOMException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            logger.info("directoryOut: " + directoryOut);

            String fileOut = getId() + "." + getOutput();

            // String webPathOut =
            // chart.getPageContext().getServletContext().getRealPath(
            // "temp");
            //
            // String webFileOut = "";

            long expiretime = getExpireTime();

            // start thread that produces image
            new CreateImage(document, fileOut, expiretime, null, null);

            /*
             * xml = "<script type=\"text/javascript\" src=\"" + contextPath +
             * "/javascript/tooltip.js\"></script>\n" + "<div id='identity1'
             * style=\"display: none;\">" + "Testing</div><map
             * name='green'>\n" + "<area shape='polygon'
             * coords='1,1,400,1,400,200,1,200' " +
             * "onmouseover=\"_show(event,'identity1');\" " +
             * "onmouseout=\"_hide();\" href='test.jsp' />\n</map>\n";
             */

            xml += getAreaMap() + System.getProperty("line.separator");
            // logger.error(xml);
            xml +=
                    AreaMap.getImageTag(contextPath + "/" + directoryOut + "/"
                            + fileOut, getId(), getChartTitle().getText(),
                            getGlyphWidth(), getGlyphHeight())
                            + System.getProperty("line.separator");

            xml += getPopupMap();

        }

        return xml;
    }

    /**
     * Gets the sVG chart.
     * 
     * @return the sVG chart
     */
    public Document getSVGChart() {

        Document doc = null;

        if (getOutput().indexOf("html") != -1) {
            doc = new Document(renderGlyph());
        } else {
            Element svg = new Element("svg");

            // improve rendering of fonts
            svg.setAttribute("style", "shape-rendering:geometricPrecision;"
                    + "text-rendering:geometricPrecision;"
                    + "image-rendering:optimizeQuality"
                    + "color-rendering:optimizeQuality");

            svg.setAttribute("width", "" + getGlyphWidth());
            svg.setAttribute("height", "" + getGlyphHeight());

            svg.addContent(renderGlyph());

            // svg.addContent(0,
            // Configuration.getInstance().getXml("defs.xml"));
            svg.addContent(0, Defs.getUsedDefs());

            DocType type =
                    new DocType("svg", "-//W3C//DTD SVG 1.1//EN",
                            "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");

            // load xml data for plotting shapes
            type.setInternalSubset(Configuration.getInstance().getValue(
                    "glowaxes", "xml_shapes"));

            changeNamespace(svg, Namespace
                    .getNamespace("http://www.w3.org/2000/svg"));
            doc = new Document(svg, type);
        }

        return doc;
    }

    /**
     * Process area.
     * 
     * @param myArea
     *            the my area
     * 
     * @return the area
     */
    private Area processArea(Area myArea) {

        // logger.error(legend);
        if (getChartTitle().getGlyphVAlign().equals("bottom")) {
            myArea.setGlyphOffsetBottom(0);
        } else {
            myArea.setGlyphOffsetTop(0);
        }

        myArea.setGlyphOffsetLeft(Math.max(myArea.getYAxis().getGlyphWidth(),
                -myArea.getXAxis().getAxisOffshot()) + 5);

        myArea.setGlyphOffsetRight(myArea.getXAxis().getAxisOffshot() > 0
                ? (myArea.getXAxis().getAxisOffshot() + 5) : 5);

        myArea.setGlyphOffsetBottom(myArea.getXAxis().getGlyphHeight());

        // there is no legend attached to the chart
        if (legend == null) {

            // calculate the area's height
            myArea
                    .setGlyphHeight(getGlyphHeight()
                            - (getChartTitle().getGlyphOffsetTop()
                                    + getChartTitle().getGlyphHeight() + getChartTitle()
                                    .getGlyphOffsetBottom())
                            - (myArea.getGlyphOffsetTop() + myArea
                                    .getGlyphOffsetBottom()));

            // calculate the width of the area
            myArea.setGlyphWidth(getGlyphWidth()
                    - (myArea.getGlyphOffsetLeft() + myArea
                            .getGlyphOffsetRight()));

        } else {// there is a legend attached to the chart

            if (legend.getGlyphAlign().equals("center")) {

                // calculate the area's height
                myArea
                        .setGlyphHeight(getGlyphHeight()
                                - (getChartTitle().getGlyphOffsetTop()
                                        + getChartTitle().getGlyphHeight() + getChartTitle()
                                        .getGlyphOffsetBottom())
                                - (myArea.getGlyphOffsetTop() + myArea
                                        .getGlyphOffsetBottom())
                                - (legend.getGlyphHeight()
                                        + legend.getGlyphOffsetTop() + legend
                                        .getGlyphOffsetBottom()));

                // calculate the width of the area
                myArea.setGlyphWidth(getGlyphWidth()
                        - (myArea.getGlyphOffsetLeft() + myArea
                                .getGlyphOffsetRight()));

            } else {// legend is not centered

                // calculate the area's height
                myArea
                        .setGlyphHeight(getGlyphHeight()
                                - (getChartTitle().getGlyphOffsetTop()
                                        + getChartTitle().getGlyphHeight() + getChartTitle()
                                        .getGlyphOffsetBottom())
                                - (myArea.getGlyphOffsetTop() + myArea
                                        .getGlyphOffsetBottom()));

                // calculate the width of the area
                myArea.setGlyphWidth(getGlyphWidth()
                        - (myArea.getGlyphOffsetLeft() + myArea
                                .getGlyphOffsetRight())
                        - (legend.getGlyphOffsetLeft()
                                + legend.getGlyphOffsetRight() + legend
                                .getGlyphWidth()));

            }

        }

        myArea
                .setXScale(myArea.getGlyphWidth()
                        / myArea.getXAxis().getLength());

        myArea.setYScale(myArea.getGlyphHeight()
                / myArea.getYAxis().getLength());

        return myArea;
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
    @SuppressWarnings("unchecked")
    public Element renderGlyph() {

        if (getOutput().indexOf("html") != -1) {

            Element div = new Element("div");
            for (int i = 0; i < getAreasSize(); i++) {

                // todo add logic to add graphs
                Area areaGlyph = getArea(i);
                Element ids = areaGlyph.renderHtml();

                for (Iterator j = ids.getChildren().iterator(); j.hasNext();) {
                    Element e = (Element) j.next();
                    j.remove();
                    div.addContent(e);
                }

            }
            return div;
        }

        // following also renders child;
        Element glyph = super.renderGlyph();

        if (legend != null) {
            legend.initLegend();
        }

        if ((getImage()) != null) {

            Element image = new Element("image");
            image.setAttribute("preserveAspectRatio", "xMidYMid slice");
            image.setAttribute("width", "" + this.getGlyphWidth());
            image.setAttribute("height", "" + this.getGlyphHeight());

            Namespace xlink =
                    Namespace.getNamespace("xlink",
                            "http://www.w3.org/1999/xlink");
            image.addNamespaceDeclaration(xlink);
            image.setAttribute("href", getImage(), xlink);

            Element g = new Element("g");
            if (getImageEffect() != null) {
                g.setAttribute("filter", "url(#" + getImageEffect() + ")");
                g.addContent(image);
            } else {
                g = image;
            }

            glyph.setContent(0, g);

        }

        /*
         * use a column glyph to build the chart
         */
        ColumnGlyph rasterGlyph = new ColumnGlyph();

        /*
         * use a rows glyph to build elements in the chart
         */
        RowGlyph rowGlyph = null;

        /*
         * use a rows glyph to build elements in the chart
         */
        OverlayGlyph overlay = new OverlayGlyph();

        // add the title to the top?
        if (!getChartTitle().getGlyphVAlign().equals("bottom")) {
            rasterGlyph.addGlyph(getChartTitle());
        } else {
            // add some space at the top
            // SpacerGlyph sg = new SpacerGlyph();
            // sg.setGlyphHeight(TypeConverter.getDouble(Constants
            // .getDefault("area.min.offset"), 0));
            // sg.setGlyphWidth(1);
            // rasterGlyph.addGlyph(sg);
        }

        // if (legend != null) {
        // logger.error("legend.getGlyphAlign(): " + legend.getGlyphAlign());
        // logger.error("legend.getGlyphVAlign(): " + legend.getGlyphVAlign());
        // }

        // create a row glyph to manage legend in row
        if (legend != null && !legend.getGlyphAlign().equals("center")) {

            rowGlyph = new RowGlyph();

        } else {

        }

        if (legend != null) {
            if (rowGlyph == null && legend.getGlyphVAlign().equals("top")) {
                legend.setGlyphOffset(0);
                legend.setGlyphOffsetTop(2);
                legend.setGlyphOffsetBottom(5);
                rasterGlyph.addGlyph(legend);

            } else if (legend.getGlyphAlign().equals("left")) {

                legend.setGlyphOffsetTop(0);// getArea(0).getGlyphOffsetTop()
                legend.setGlyphOffsetBottom(0);// getArea(0).getGlyphOffsetBottom()
                legend.setGlyphOffsetLeft(5);
                legend.setGlyphOffsetRight(10);

                rowGlyph.addGlyph(legend);

            }
        }
        // always add the glyph to overlay if overlay is empty
        // add the next glyph
        for (int i = 0; i < getAreasSize(); i++) {

            // todo add logic to add graphs
            Area areaGlyph = processArea(getArea(i));

            overlay.addGlyph(areaGlyph);
            // logger.error(areaGlyph.getGlyphOffsetTop());

        }

        ((Area) overlay.getGlyph(overlay.getSize() - 1)).setRenderLabels(true);

        // plot the areas
        if (rowGlyph != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(rowGlyph.getGlyphWidth());
                logger.debug(overlay.getGlyphWidth());
            }
            rowGlyph.addGlyph(overlay);
        } else {
            rasterGlyph.addGlyph(overlay);
        }

        if (legend != null) {
            if (rowGlyph == null && legend.getGlyphVAlign().equals("bottom")) {

                legend.setGlyphOffsetTop(5);
                legend.setGlyphOffsetBottom(5);
                rasterGlyph.addGlyph(legend);

            } else if (rowGlyph != null
                    && !legend.getGlyphAlign().equals("left")) {

                legend.setGlyphOffsetTop(0);// getArea(0).getGlyphOffsetTop()
                legend.setGlyphOffsetBottom(0);// getArea(0).getGlyphOffsetBottom());
                legend.setGlyphOffsetLeft(7);
                legend.setGlyphOffsetRight(5);

                rowGlyph.addGlyph(legend);
            }
        }
        // create a row glyph to manage legend in row
        if (legend != null && !legend.getGlyphAlign().equals("center")) {

            rasterGlyph.addGlyph(rowGlyph);

        }

        if (getChartTitle().getGlyphVAlign().equals("bottom")) {

            rasterGlyph.addGlyph(getChartTitle());
        }
        rasterGlyph.calculateDimensions();

        getArea(0).setXOffsetChart(overlay.getGlyphX());
        getArea(0).setYOffsetChart(overlay.getGlyphY());

        glyph.addContent(rasterGlyph.renderGlyph());
        //
        try {
            Defs.addUsed("text.glow");
            if (Encrypt.getLicenseType().equals("CPAL")) {
                ChartTitle author =
                        new ChartTitle(
                                "Graph by glowaxes.org",
                                "fill:darkred;font-weight:bold;font-family:Arial;font-size:12",
                                "text.glow", 0, 0);
                author
                        .setGlyphY(getGlyphHeight() - author.getGlyphHeight()
                                - 4);
                author.setGlyphX(getGlyphWidth() - author.getGlyphWidth() - 3);
                // glyph.addContent(author.renderGlyph());
            } else if (Encrypt.getLicenseType().equals("TLD")) {

                ChartTitle author =
                        new ChartTitle(
                                "Graph by glowaxes.org",
                                "fill:darkred;font-weight:bold;font-family:Arial;font-size:12",
                                "text.glow", 0, 0);
                author
                        .setGlyphY(getGlyphHeight() - author.getGlyphHeight()
                                - 4);
                author.setGlyphX(getGlyphWidth() - author.getGlyphWidth() - 3);
                // glyph.addContent(author.renderGlyph());
            } else if (Encrypt.getLicenseType().equals("Enterprise")) {

                ChartTitle author =
                        new ChartTitle(
                                "Graph by glowaxes.org",
                                "fill:darkred;font-weight:bold;font-family:Arial;font-size:12",
                                "text.glow", 0, 0);
                author
                        .setGlyphY(getGlyphHeight() - author.getGlyphHeight()
                                - 4);
                author.setGlyphX(getGlyphWidth() - author.getGlyphWidth() - 3);
                // glyph.addContent(author.renderGlyph());
            } else {

                ChartTitle author =
                        new ChartTitle(
                                "Graph by glowaxes.org",
                                "fill:darkred;font-weight:bold;font-family:Arial;font-size:12",
                                "text.glow", 0, 0);
                author
                        .setGlyphY(getGlyphHeight() - author.getGlyphHeight()
                                - 4);
                author.setGlyphX(getGlyphWidth() - author.getGlyphWidth() - 3);
                // glyph.addContent(author.renderGlyph());
            }
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // success
        return glyph;
    }

    //
    // /**
    // * Sets the area map.
    // *
    // * @param _shape
    // * the _shape
    // * @param _coords
    // * the _coords
    // * @param _events
    // * the _events
    // * @param _content
    // * the _content
    // * @param _href
    // * the _href
    // */
    // public void setAreaMap(String _shape, String _coords, String _events,
    // Element _content, String _href) {
    // if (htmlAreaMap == null) {
    // htmlAreaMap = new AreaMap(this.getId());
    // }
    // htmlAreaMap.addArea(_shape, _coords, _events, _content, _href);
    //
    // }

    /**
     * Sets the area map.
     * 
     * @param _shape
     *            the _shape
     * @param _coords
     *            the _coords
     * @param _events
     *            the _events
     * @param _content
     *            the _content
     */
    public void setAreaMap(String _shape, String _coords, String _events,
            String xml, String _href) {
        if (htmlAreaMap == null) {
            htmlAreaMap = new AreaMap(this.getId());
        }
        if (xml.indexOf("<?") == -1) {
            xml =
                    "<?xml version='1.0' encoding='utf-8'?><div>" + xml
                            + "</div>";
        }
        xml = xml.trim();
        SAXBuilder builder = new SAXBuilder();
        Element element = null;
        // Create the document
        try {
            element = builder.build(new StringReader(xml)).getRootElement();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logger.error(xml);

        htmlAreaMap.addArea(_shape, _coords, _events, element, _href);

    }

    /**
     * Sets the tic marks.
     */
    public void setTicMarks() {

        /*
         * 
         */
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;

        boolean init = true;
        for (int i = 0; i < getAreasSize(); i++) {

            if (getArea(i).getType().equals("pie"))
                break;

            Group myGroup = getArea(i).getData().getGroup();

            double _minX = 0;
            double _maxX = 0;
            double _minY = 0;
            double _maxY = 0;

            if (getArea(i).getYAxis() != null) {
                if (getArea(i).getYAxis().getType().toLowerCase().equals(
                        "number")) {
                    if (getArea(i).getType().equals("columnrange")) {
                        _minY =
                                Math.min(myGroup.getA1MinSeriesList()
                                        .doubleValue(), myGroup
                                        .getA2MinSeriesList().doubleValue());
                        _maxY =
                                Math.max(myGroup.getA1MaxSeriesList()
                                        .doubleValue(), myGroup
                                        .getA2MaxSeriesList().doubleValue());
                    } else if (getArea(i).getType().equals("stackedcolumn")) {
                        _minY = 0;
                        _maxY = myGroup.getMaxStackYAxis();

                    } else if (getArea(i).getType().equals("stackedarea")) {
                        _minY = 0;
                        _maxY = myGroup.getMaxStackYAxis();
                    } else if (getArea(i).getType().equals("stackedsplinearea")) {
                        _minY = 0;
                        _maxY = myGroup.getMaxStackYAxis();
                    } else if (getArea(i).getType().equals("stackedcolumn100")) {
                        _minY = 0;
                        _maxY = 1;

                    } else if (getArea(i).getType().equals("stackedarea100")) {

                        _minY = 0;
                        _maxY = 1;

                    } else if (getArea(i).getType().equals(
                            "stackedsplinearea100")) {

                        _minY = 0;
                        _maxY = 1;

                    } else {

                        _minY = myGroup.getYMinSeriesList().doubleValue();
                        _maxY = myGroup.getYMaxSeriesList().doubleValue();
                    }
                }

                if (getArea(i).getYAxis().getType().toLowerCase()
                        .equals("date")) {
                    _minY = myGroup.getYMinSeriesList().longValue();
                    _maxY = myGroup.getYMaxSeriesList().longValue();
                }
                if (getArea(i).getYAxis().getType().toLowerCase().equals(
                        "category")) {
                    // todo throw exception?
                }
            }

            if (getArea(i).getXAxis() != null) {
                if (getArea(i).getXAxis().getType().toLowerCase().equals(
                        "number")) {
                    if (getArea(i).getType().equals("stackedbar")) {
                        _minX = 0;
                        _maxX = myGroup.getMaxStackXAxis();
                    } else if (getArea(i).getType().equals("stackedbar100")) {
                        _minX = 0;
                        _maxX = 1;
                    } else {
                        _minX = myGroup.getXMinSeriesList().doubleValue();
                        _maxX = myGroup.getXMaxSeriesList().doubleValue();
                    }
                }

                if (getArea(i).getXAxis().getType().toLowerCase()
                        .equals("date")) {
                    _minX = myGroup.getXMinSeriesList().longValue();
                    _maxX = myGroup.getXMaxSeriesList().longValue();
                }

            }

            if (_minX < minX || init)
                minX = _minX;
            if (_maxX > maxX || init)
                maxX = _maxX;
            if (_minY < minY || init)
                minY = _minY;
            if (_maxY > maxY || init)
                maxY = _maxY;
            init = false;

        }

        for (int i = 0; i < getAreasSize(); i++) {

            // logger.error(i);

            if (getArea(i).getType().equals("pie"))
                break;

            Group myGroup = getArea(i).getData().getGroup();
            if (getArea(i).getYAxis() != null) {
                if (getArea(i).getYAxis().getType().toLowerCase().equals(
                        "number"))
                    getArea(i).getYAxis().setTicMarks(minY, maxY);

                if (getArea(i).getYAxis().getType().toLowerCase()
                        .equals("date"))
                    getArea(i).getYAxis().setDateTicMarks((long) minY,
                            (long) maxY);

                if (getArea(i).getYAxis().getType().toLowerCase().equals(
                        "category"))
                    getArea(i).getYAxis().setTicMarks(myGroup);

            }

            if (getArea(i).getXAxis() != null) {
                if (getArea(i).getXAxis().getType().toLowerCase().equals(
                        "number"))
                    getArea(i).getXAxis().setTicMarks(minX, maxX);

                if (getArea(i).getXAxis().getType().toLowerCase()
                        .equals("date"))
                    getArea(i).getXAxis().setDateTicMarks((long) minX,
                            (long) maxX);

                if (getArea(i).getXAxis().getType().toLowerCase().equals(
                        "category"))
                    getArea(i).getXAxis().setTicMarks(myGroup);
            }

            if (getArea(i).getYAxis() != null) {
                if (getArea(i).getYAxis().getType().toLowerCase().equals(
                        "number"))
                    getArea(i).getYAxis().setTicMarks(minY, maxY);

                if (getArea(i).getYAxis().getType().toLowerCase()
                        .equals("date"))
                    getArea(i).getYAxis().setDateTicMarks((long) minY,
                            (long) maxY);

                if (getArea(i).getYAxis().getType().toLowerCase().equals(
                        "category"))
                    getArea(i).getYAxis().setTicMarks(myGroup);

            }
        }
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

        result.append(this.getClass().getName() + " Object {");
        result.append(" areaGrouping: ");
        result.append(getAreaGrouping());
        result.append(newLine);

        result.append(" image: ");
        result.append(getImage());
        result.append(newLine);

        result.append(" output: ");
        result.append(getOutput());
        result.append(newLine);

        if (areas.size() > 0) {
            for (Iterator<Area> i = areas.iterator(); i.hasNext();) { // No
                // ForUpdate
                Area area = i.next();
                result.append(newLine);
                result.append(area.toString());
            }
        }

        result.append("}");

        return result.toString();

    }

}
