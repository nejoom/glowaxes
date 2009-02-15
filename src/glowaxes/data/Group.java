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

package glowaxes.data;

import glowaxes.util.DateParser;
import glowaxes.util.TypeConverter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * <p>
 * This object holds multiple {@link Series}. A series contains multiple
 * {@link Value}s. Values represent a point in a co-ordinate system, with point
 * chart specific attributes (label, shape, style).
 * </p>
 * The Group contains static utility methods to {@link #loadXMLURI} or
 * {@link #loadXMLString}. This allows the xml to be loaded from a webserver or
 * a string.
 * </p>
 * <p>
 * Here's an example of what the xml structure representing the data looks like:
 * </p>
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 * &lt;data&gt;
 *     &lt;group id=&quot;group1&quot;&gt;
 *         &lt;series legend=&quot;England &amp; Wales&quot;&gt;
 *             &lt;value&gt;
 *                 &lt;x&gt;5&lt;/x&gt;
 *                 &lt;y&gt;two&lt;/y&gt;
 *             &lt;/value&gt;
 *             &lt;value&gt;
 *                 &lt;x&gt;3&lt;/x&gt;
 *                 &lt;y&gt;three&lt;/y&gt;
 *                 &lt;style&gt;fill:green;stroke:red;opacity:1&lt;/style&gt;
 *                 &lt;shape&gt;x&lt;/shape&gt;
 *                 &lt;label
 *                     style=&quot;font-color:green;font-size:15;&quot;&gt;
 *                     styled label
 *                 &lt;/label&gt;
 *             &lt;/value&gt;
 *         &lt;/label&gt;
 *     &lt;/group&gt;
 * &lt;/data&gt;
 * </pre>
 * 
 * @see Value
 * @see Series
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 * @version $Id : XXXXXX $
 * @since ${since_tag}
 */
public class Group {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    private static Logger logger = Logger.getLogger(Group.class.getName());

    /**
     * This will return the Group associated with xml data passed as a String.
     * 
     * @return <code>Group</code> - the first Group associated with xml data
     *         represented as a String.
     * 
     * @param xml
     *            <code>String</code> the xml data represented as a String.
     */
    public static Group loadXMLString(String xml) {
        return loadXMLString(xml, null);
    }

    @SuppressWarnings("unchecked")
    /**
     * This will return the Group associated with xml data passed as a String.
     * 
     * @return <code>Group</code> - the Group associated with xml data
     *         represented as a String.
     * 
     * @param xml
     *            <code>String</code> the xml data represented as a String.
     * @param id
     *            <code>String</code> id of the group in the xml data
     *            represented as a String.
     */
    public static Group loadXMLString(String xml, String id) {

        ByteArrayInputStream bas = new ByteArrayInputStream(xml.getBytes());

        Group group = new Group();

        SAXBuilder saxBuilder = new SAXBuilder();

        try {
            logger.info("Loading xml string.... ");

            Document doc = saxBuilder.build(bas);
            Element root = doc.getRootElement();
            List<Element> children = root.getChildren("group");

            Iterator<Element> iterator = children.iterator();

            Element dataGroup = iterator.next();

            if (id != null) {
                while (!dataGroup.getAttribute("id").getValue().equals(id)) {
                    dataGroup = iterator.next();
                }

                group.id = id;
            } else {
                group.id = dataGroup.getAttribute("id").getValue();
            }

            if (logger.isDebugEnabled())
                logger.debug("Loaded: " + group.id);
            group.addChildren(dataGroup);

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (org.jdom.JDOMException e) {
            throw new RuntimeException(e);
        }

        group.setRanges();

        return group;

    }

    @SuppressWarnings("unchecked")
    public static Group loadFromSql(ArrayList<SqlSerie> sqlSeries) {
        Group group = new Group();
        group.id = "sql";

        logger.info("Transforming sql data.... ");

        for (int i = 0; i < sqlSeries.size(); i++) {

            SqlSerie sqlS = sqlSeries.get(i);

            Series aSeries = new Series();
            if (sqlS.getDefaultLabel() != null) {
                Element label = new Element("label");
                label.setText(sqlS.getDefaultLabel());
                aSeries.setDefaultLabel(label);
            }
            aSeries.setDefaultLabelBackgroundStyle(sqlS
                    .getDefaultLabelBackgroundStyle());
            aSeries.setDefaultLabelStyle(sqlS.getDefaultLabelStyle());
            aSeries.setDefaultShape(sqlS.getDefaultShape());
            aSeries.setDefaultShapeStyle(sqlS.getDefaultShapeStyle());

            aSeries.setId(sqlS.getId());
            aSeries.setLegend(sqlS.getLegend());
            aSeries.setLineStyle(sqlS.getLineStyle());
            group.addSeries(aSeries);

            HashMap<String, Object>[] hmp = sqlS.getHashMapArray();
            aSeries.setHashMapArray(hmp);

            if (hmp != null)
                for (int j = 0; j < hmp.length; j++) {

                    Value val = new Value();

                    val.setX(sqlS.getVal("x", j));
                    val.setY(sqlS.getVal("y", j));
                    val.setA1(sqlS.getVal("a1", j));
                    val.setA2(sqlS.getVal("a2", j));
                    val.setB1(sqlS.getVal("b1", j));
                    val.setB2(sqlS.getVal("b2", j));

                    if (sqlS.getVal("label", j) != null) {
                        Element e = new Element("label");
                        e.setText((String) sqlS.getVal("label", j));
                        val.setLabel(e);
                    }

                    val.setLabelStyle((String) sqlS.getVal("labelStyle", j));
                    val.setLabelBackgroundStyle((String) sqlS.getVal(
                            "labelBackgroundStyle", j));
                    val.setLabelHref((String) sqlS.getVal("labelHref", j));
                    val.setLabelEvents((String) sqlS.getVal("labelEvent", j));
                    val.setStyle((String) sqlS.getVal("style", j));
                    val.setShape((String) sqlS.getVal("shape", j));

                    group.getSeries(group.getSize() - 1).addValue(val);
                }
        }

        group.setRanges();

        return group;

    }

    /**
     * This will return the Group associated with data passed as an URI.
     * 
     * @return <code>Group</code> - the Group associated with xml data passed
     *         as an URI.
     * 
     * @param uri
     *            <code>String</code> the xml data passed as an URI.
     */
    public static Group loadXMLURI(String uri) {
        return loadXMLURI(uri, null);
    }

    /**
     * This will return the Group associated with data passed as an URI.
     * 
     * @return <code>Group</code> - the Group associated with xml data passed
     *         as an URI.
     * 
     * @param uri
     *            <code>String</code> the xml data passed as an URI.
     * @param id
     *            <code>String</code> id of the group in the xml data.
     */
    @SuppressWarnings("unchecked")
    public static Group loadXMLURI(String uri, String id) {

        Group group = new Group();

        SAXBuilder saxBuilder = new SAXBuilder();

        try {
            logger.info("Loading xml file.... " + uri);

            Document doc = saxBuilder.build(uri);
            Element root = doc.getRootElement();
            List<Element> children = root.getChildren("group");

            Iterator<Element> iterator = children.iterator();

            Element dataGroup = iterator.next();

            if (id != null) {
                while (!dataGroup.getAttribute("id").getValue().equals(id)) {
                    dataGroup = iterator.next();
                }

                group.id = id;
            } else {
                group.id = dataGroup.getAttribute("id").getValue();
            }

            if (logger.isDebugEnabled())
                logger.debug("Loaded: " + group.id);
            group.addChildren(dataGroup);

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (org.jdom.JDOMException e) {
            throw new RuntimeException(e);
        }

        group.setRanges();

        return group;
    }

    private Double a1MaxSerieList;

    private Double a1MinSerieList;

    private Double a2MaxSerieList;

    private Double a2MinSerieList;

    private Double b1MaxSerieList;

    private Double b1MinSerieList;

    private Double b2MaxSerieList;

    private Double b2MinSerieList;

    private String id = "";

    private final ArrayList<Series> SeriesList = new ArrayList<Series>();

    private Double xMaxSerieList;

    private Double xMinSerieList;

    private Double yMaxSerieList;

    private Double yMinSerieList;

    private Double zMaxSerieList;

    private Double zMinSerieList;

    /**
     * Constructs a newly allocated <code>Group</code> object that represents
     * a <code>Group</code> of <code>Series</code>.
     * 
     * <p>
     * Series can be added using the addSerie(Series) method.
     * 
     * @see Series
     * @see Value
     */
    public Group() {
    }

    /*
     * recursive algorithm to populate Group with Series and Series with values
     */
    @SuppressWarnings("unchecked")
    private void addChildren(Element current) {

        if (current.getName().toLowerCase().equals("series")) {

            Series aSeries = new Series();
            if (current.getAttribute("legend") != null) {
                aSeries.setLegend(current.getAttribute("legend").getValue());
            }
            if (current.getAttribute("default-shape") != null) {
                aSeries.setDefaultShape(current.getAttribute("default-shape")
                        .getValue());
            }
            if (current.getAttribute("default-shape-style") != null) {
                aSeries.setDefaultShapeStyle(current.getAttribute(
                        "default-shape-style").getValue());
            }
            if (current.getAttribute("default-label-style") != null) {
                aSeries.setDefaultLabelStyle(current.getAttribute(
                        "default-label-style").getValue());
            }
            if (current.getAttribute("default-label-background-style") != null) {
                aSeries.setDefaultLabelBackgroundStyle(current.getAttribute(
                        "default-label-background-style").getValue());
            }
            if (current.getAttribute("line-style") != null) {
                ;
                aSeries.setLineStyle(current.getAttribute("line-style")
                        .getValue());
            }
            if (current.getAttribute("id") != null) {
                aSeries.setId(current.getAttribute("id").getValue());
            }
            addSeries(aSeries);

        } else if (current.getName().toLowerCase().equals("value")) {

            Value aValue = new Value();
            if (current.getAttribute("shape") != null) {
                aValue.setShape(current.getAttribute("shape").getValue());
            }
            if (current.getAttribute("style") != null) {
                aValue.setStyle(current.getAttribute("style").getValue());
            }
            if (current.getAttribute("label") != null) {
                Element label = new Element("label");
                label.setText(current.getAttribute("label").getValue());
                aValue.setLabel(label);
            }
            if (current.getAttribute("label-style") != null) {
                aValue.setLabelStyle(current.getAttribute("label-style")
                        .getValue());

            }
            if (current.getAttribute("label-background-style") != null) {
                aValue.setLabelBackgroundStyle(current.getAttribute(
                        "label-background-style").getValue());

            }
            getSeries(getSize() - 1).addValue(aValue);

        } else if (!current.getName().toLowerCase().equals("group")) {

            Value workingValue =
                    getSeries(getSize() - 1).getValue(
                            getSeries(getSize() - 1).getSize() - 1);

            String xDateFormat = null;

            String yDateFormat = null;

            if (current.getName().toLowerCase().equals("x")) {
                // if date is explicitly given
                if (xDateFormat != null
                        || current.getAttribute("date-format") != null) {
                    if (current.getAttribute("date-format") != null)
                        xDateFormat =
                                current.getAttribute("date-format").getValue();

                    Date myDate =
                            DateParser.getFormat(current.getText().trim(),
                                    xDateFormat);

                    workingValue.setX(new Long(myDate.getTime()));

                } else {
                    if (current.getText() != null) {
                        workingValue.setX(current.getText().trim());
                    }
                }

            } else if (current.getName().toLowerCase().equals("y")) {
                // if date is explicitly given
                if (yDateFormat != null
                        || current.getAttribute("date-format") != null) {
                    if (current.getAttribute("date-format") != null)
                        yDateFormat =
                                current.getAttribute("date-format").getValue();
                    Date myDate =
                            DateParser.getFormat(current.getText().trim(),
                                    yDateFormat);
                    workingValue.setY(new Long(myDate.getTime()));
                } else {
                    if (current.getText() != null) {
                        workingValue.setY(current.getText().trim());
                    }
                }

            } else if (current.getName().toLowerCase().equals("a1")) {
                if (current.getText() != null) {
                    workingValue.setA1(current.getText().trim());
                }

            } else if (current.getName().toLowerCase().equals("a2")) {
                if (current.getText() != null) {
                    workingValue.setA2(current.getText().trim());
                }

            } else if (current.getName().toLowerCase().equals("b1")) {
                if (current.getText() != null) {
                    workingValue.setB1(current.getText().trim());
                }
            } else if (current.getName().toLowerCase().equals("b2")) {
                if (current.getText() != null) {
                    workingValue.setB2(current.getText().trim());
                }
            } else if (current.getName().toLowerCase().equals("label")) {
                if (current.getText() != null) {
                    workingValue.setLabel(current);
                }
                if (current.getAttribute("style") != null) {
                    workingValue.setLabelStyle(current.getAttribute("style")
                            .getValue());

                }
                if (current.getAttribute("href") != null) {
                    workingValue.setLabelHref(current.getAttribute("href")
                            .getValue());

                }
                if (current.getAttribute("events") != null) {
                    workingValue.setLabelEvents(current.getAttribute("events")
                            .getValue());
                }

            } else if (current.getName().toLowerCase().equals("style")) {
                if (current.getText() != null) {
                    workingValue.setStyle(current.getText().trim());
                }
            } else if (current.getName().toLowerCase().equals("shape")) {
                if (current.getText() != null) {
                    workingValue.setShape(current.getText().trim());
                }
            }

        }

        List<Element> children = current.getChildren();
        Iterator<Element> iterator = children.iterator();
        while (iterator.hasNext()) {
            Element child = iterator.next();
            addChildren(child);
        }

    }

    /**
     * This will add the Series to the Group. The Group is backed by an
     * ArrayList. The max and min values are calculated.
     * 
     * @param series
     *            <code>{@link Series}</code> the series to add to the group.
     */
    public void addSeries(Series series) {

        series.setIndex(SeriesList.size());
        setRanges(series);
        SeriesList.add(series);

    }

    /**
     * This will return the maximum a1 value of the Group.
     * 
     * @return <code>Double</code> - the maximum a1 Double value of the Group.
     */
    public Double getA1MaxSeriesList() {
        return a1MaxSerieList;
    }

    /**
     * This will return the minimal a1 value of the Group.
     * 
     * @return <code>Double</code> - the minimal a1 Double value of the Group.
     */
    public Double getA1MinSeriesList() {
        return a1MinSerieList;
    }

    /**
     * This will return the maximum a2 value of the Group.
     * 
     * @return <code>Double</code> - the maximum a2 Double value of the Group.
     */
    public Double getA2MaxSeriesList() {
        return a2MaxSerieList;
    }

    /**
     * This will return the minimal a2 value of the Group.
     * 
     * @return <code>Double</code> - the minimal a2 Double value of the Group.
     */
    public Double getA2MinSeriesList() {
        return a2MinSerieList;
    }

    /**
     * This will return the identity string of this Group. Elements are often
     * rendered with this identity string for debugging.
     * 
     * @return <code>String</code> - the identity string of this Group.
     */
    public String getId() {
        return id;
    }

    /**
     * This will return the size of the largest Series, that is the number of
     * values listed in the largest Series.
     * 
     * @return <code>int</code> - the size of the Group.
     */
    public int getMaxSeriesSize() {
        int max = 0;
        if (SeriesList != null)
            for (int i = 0; i < SeriesList.size(); i++) {
                if (SeriesList.get(i) == null)
                    continue;
                int size = SeriesList.get(i).getSize();
                if (size > max)
                    max = size;
            }
        return max;
    }

    /**
     * This will return the largest x Value of the Group.
     * 
     * @return <code>double</code> - the largest x Value of the Group.
     */
    public double getMaxStackXAxis() {

        int numSerie = this.getSeriesList().size();

        double maxStack = 0;

        // used to hold the sets; LinkedHashSet is ordered and keys are
        // unique
        LinkedHashMap<String, Double> myMap =
                new LinkedHashMap<String, Double>();

        // make unique ordered list
        for (int i = 0; i < numSerie; i++) {
            ArrayList<Value> currentList = this.getSeries(i).getArrayList();

            for (int j = 0; j < currentList.size(); j++) {
                String category = currentList.get(j).getYObject().toString();
                double value =
                        TypeConverter
                                .getDouble(currentList.get(j).getXObject());
                double myDouble =
                        TypeConverter.getDouble(myMap.get(category), 0);
                myDouble = myDouble + value;
                myMap.put(currentList.get(j).getYObject().toString(),
                        new Double(myDouble));
                if (myDouble > maxStack)
                    maxStack = myDouble;
            }
        }

        return maxStack;

    }

    /**
     * This will return the largest y Value of the Group.
     * 
     * @return <code>double</code> - the largest y Value of the Group.
     */
    public double getMaxStackYAxis() {

        int numSerie = this.getSeriesList().size();

        double maxStack = 0;

        // used to hold the sets; LinkedHashSet is ordered and keys are
        // unique
        LinkedHashMap<String, Double> myMap =
                new LinkedHashMap<String, Double>();

        // make unique ordered list
        for (int i = 0; i < numSerie; i++) {
            ArrayList<Value> currentList = this.getSeries(i).getArrayList();
            // size += currentList.size();

            for (int j = 0; j < currentList.size(); j++) {
                String category = currentList.get(j).getXObject().toString();
                double value =
                        TypeConverter
                                .getDouble(currentList.get(j).getYObject());
                double myDouble =
                        TypeConverter.getDouble(myMap.get(category), 0);
                myDouble = myDouble + value;
                myMap.put(currentList.get(j).getXObject().toString(),
                        new Double(myDouble));
                if (myDouble > maxStack)
                    maxStack = myDouble;
            }
        }

        return maxStack;

    }

    /**
     */
    public HashMap<String, Object>[] getHashMapArray(int index) {
        return SeriesList.get(index).getHashMapArray();
    }

    /**
     * This will return the Series in the Group for the given index. The list of
     * the Group instance is backed by an ArrayList.
     * 
     * @param index
     *            <code>int</code> the index of the Series you want.
     * @return <code>{@link Series}</code> - the series at the given index.
     */
    public Series getSeries(int index) {
        return SeriesList.get(index);
    }

    /**
     * This will return a list of series of this Group as an ArrayList.
     * 
     * @return <code>ArrayList</code> - the series list as an ArrayList.
     */
    public ArrayList<Series> getSeriesList() {
        return SeriesList;
    }

    /**
     * This will return the size of the Group, that is the number of series
     * listed in this Group.
     * 
     * @return <code>int</code> - the size of the Group.
     */
    public int getSize() {
        return SeriesList.size();
    }

    /**
     * This will return the maximum x value of the Group.
     * 
     * @return <code>Double</code> - the maximum Double value of all series.
     */
    public Double getXMaxSeriesList() {
        return xMaxSerieList;
    }

    /**
     * This will return the minimal x value of the Group.
     * 
     * @return <code>Double</code> - the minimal x Double value of the Group.
     */
    public Double getXMinSeriesList() {
        return xMinSerieList;
    }

    /**
     * This will return the maximum y value of the Group.
     * 
     * @return <code>Double</code> - the maximum y Double value of the Group.
     */
    public Double getYMaxSeriesList() {
        return yMaxSerieList;
    }

    /**
     * This will return the minimal y value of the Group.
     * 
     * @return <code>Double</code> - the minimal y Double value of the Group.
     */
    public Double getYMinSeriesList() {
        return yMinSerieList;
    }

    /**
     * This will return the maximum z value of the Group.
     * 
     * @return <code>Double</code> - the maximum z Double value of the Group.
     */
    public Double getZMaxSeriesList() {
        return zMaxSerieList;
    }

    /**
     * This will return the minimal z value of the Group.
     * 
     * @return <code>Double</code> - the minimal z Double value of the Group.
     */
    public Double getZMinSeriesList() {
        return zMinSerieList;
    }

    private void setRanges() {

        if (logger.isDebugEnabled())
            logger.debug("setRanges");

        for (int i = 0; i < SeriesList.size(); i++) {
            getSeries(i).setRanges();
            setRanges(getSeries(i));
        }

    }

    private void setRanges(Series series) {

        if (xMinSerieList == null
                || series.getXMin().doubleValue() < xMinSerieList.doubleValue()) {
            xMinSerieList = series.getXMin();
        }
        if (xMaxSerieList == null
                || series.getXMax().doubleValue() > xMaxSerieList.doubleValue()) {
            xMaxSerieList = series.getXMax();
        }
        if (yMinSerieList == null
                || series.getYMin().doubleValue() < yMinSerieList.doubleValue()) {
            yMinSerieList = series.getYMin();
        }
        if (yMaxSerieList == null
                || series.getYMax().doubleValue() > yMaxSerieList.doubleValue()) {
            yMaxSerieList = series.getYMax();
        }
        if (zMinSerieList == null
                || series.getZMin().doubleValue() < zMinSerieList.doubleValue()) {
            zMinSerieList = series.getZMin();
        }
        if (zMaxSerieList == null
                || series.getZMax().doubleValue() > zMaxSerieList.doubleValue()) {
            zMaxSerieList = series.getZMax();
        }
        if (a1MinSerieList == null
                || series.getA1Min().doubleValue() < a1MinSerieList
                        .doubleValue()) {
            a1MinSerieList = series.getA1Min();
        }
        if (a1MaxSerieList == null
                || series.getA1Max().doubleValue() > a1MaxSerieList
                        .doubleValue()) {
            a1MaxSerieList = series.getA1Max();
        }
        if (a2MinSerieList == null
                || series.getA2Min().doubleValue() < a2MinSerieList
                        .doubleValue()) {
            a2MinSerieList = series.getA2Min();
        }
        if (a2MaxSerieList == null
                || series.getA2Max().doubleValue() > a2MaxSerieList
                        .doubleValue()) {
            a2MaxSerieList = series.getA2Max();
        }
        if (b1MinSerieList == null
                || series.getB1Min().doubleValue() < b1MinSerieList
                        .doubleValue()) {
            b1MinSerieList = series.getB1Min();
        }
        if (b1MaxSerieList == null
                || series.getB1Max().doubleValue() > b1MaxSerieList
                        .doubleValue()) {
            b1MaxSerieList = series.getB1Max();
        }
        if (b2MinSerieList == null
                || series.getB2Min().doubleValue() < b2MinSerieList
                        .doubleValue()) {
            b2MinSerieList = series.getB2Min();
        }
        if (b2MaxSerieList == null
                || series.getB2Max().doubleValue() > b2MaxSerieList
                        .doubleValue()) {
            b2MaxSerieList = series.getB2Max();
        }

    }

    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        final String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {");

        result.append(newLine);
        result.append(" Group: ");
        result.append(newLine);

        for (int i = 0; i < getSize(); i++) {
            result.append(this.getSeries(i).toString());
            result.append(newLine);
        }
        result.append(" getXMaxSerieList: ");
        result.append(getXMaxSeriesList());
        result.append(newLine);
        result.append(" getYMaxSerieList: ");
        result.append(getYMaxSeriesList());
        result.append(newLine);
        result.append(" getXMinSerieList: ");
        result.append(getXMinSeriesList());
        result.append(newLine);
        result.append(" getYMinSerieList: ");
        result.append(getYMinSeriesList());
        result.append(newLine);
        result.append("}");
        return result.toString();

    }

    // @SuppressWarnings("unchecked")
    // public static void main(String[] args) {
    // System.currentTimeMillis();
    // HashMap[] testCase = new HashMap[3];
    // testCase[0] = new HashMap();
    // testCase[0].put("x", new Double(4));
    // testCase[0].put("y", new Double(6));
    //
    // testCase[1] = new HashMap();
    // testCase[1].put("x", new Double(3));
    // testCase[1].put("y", new Double(3));
    //
    // testCase[2] = new HashMap();
    // testCase[2].put("x", new Double(-3));
    // testCase[2].put("y", new Double(-4));
    // // Series mySerie = new Series(testCase);
    // Group myGroup = new Group();
    //
    // // System.out.println(
    // // mySerie.getYMin()
    // // );
    // // mySet.getDefaultSerie().setLegend("A legend") ;
    // // System.out.println(
    // // mySet.getDefaultSerie().getValue(0).getY()
    // // );
    // // System.out.println(
    // // mySet.getYMinSerieList()
    // // );
    // // System.out.println(
    // myGroup.getXMLGroup("data_chart.xml");
    // //
    // mySet.setXMLSet("http://quotes.nasdaq.com/quote.dll?page=xml&mode=stock&symbol=GOOG&symbol=AAPL")
    // // ;
    //
    // System.currentTimeMillis();
    // myGroup.getXMLGroup("data_chart.xml");
    // // long t0 = System.currentTimeMillis();
    // }
}
