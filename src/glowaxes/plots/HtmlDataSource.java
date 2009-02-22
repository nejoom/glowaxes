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
import glowaxes.glyphs.IGlyph;
import glowaxes.util.DataFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class Bar.
 */
public class HtmlDataSource extends AGeneralPlotter {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(HtmlDataSource.class.getName());

    /** The data. */
    private Data data;

    public void addGlyph(IGlyph glyph) {
        // TODO Auto-generated method stub

    }

    public Element drawValue(Value value) {

        return null;
    }

    public double getAxisDateRange(long min, long max) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Data getData() {
        return data;
    }

    public ArrayList<IGlyph> getGlyphs() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#prepareData()
     */
    public void prepareData() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.plots.IPlotter#render()
     */
    @SuppressWarnings("unchecked")
    public Element render() {

        Group plotSet = data.getGroup();

        Element root = new Element("root");

        // build the tab view nav set
        Element navset = new Element("div");
        navset.setAttribute("id", "tabview");
        navset.setAttribute("class", "yui-navset");
        root.addContent(navset);
        Element ul = new Element("ul");
        ul.setAttribute("class", "yui-nav");
        navset.addContent(ul);
        for (int i = 0; i < plotSet.getSize(); i++) {

            Element li = new Element("li");
            if (i == 0)
                li.setAttribute("class", "selected");
            Element a = new Element("a");
            a.setAttribute("href", "#tab" + (i + 1));
            Element em = new Element("em");
            if (plotSet.getSeries(i).getLegend() != null)
                em.addContent(plotSet.getSeries(i).getLegend());
            else
                em.addContent("series " + (i + 1));
            li.addContent(a);
            a.addContent(em);
            ul.addContent(li);
        }

        int rowCounter = 0;

        // build the content view for the nav set
        Element yuicontent = new Element("div");
        // class for tab view
        yuicontent.setAttribute("class", "yui-content");
        // id for getting content tables
        yuicontent.setAttribute("id", "yui-content");
        for (int i = plotSet.getSize() - 1; i >= 0; i--) {

            logger.info("creating table");

            Series mySeries = plotSet.getSeries(i);

            Element id = new Element("div");
            Element table = new Element("table");

            if (mySeries.getId() != null) {
                id.setAttribute("id", mySeries.getId());
                table.setAttribute("id", "table-" + mySeries.getId());
            }
            HashMap[] hashMapArray = plotSet.getHashMapArray(i);

            if (hashMapArray == null)
                return new Element("empty-resultset");

            Iterator headIterator = hashMapArray[0].keySet().iterator();

            Element thead = new Element("thead");
            Element tr = new Element("tr");

            while (headIterator.hasNext()) {
                logger.info("creating header cell");
                Element td = new Element("td");
                String value = headIterator.next().toString();
                td.addContent(value);
                tr.addContent(td);
            }
            thead.addContent(tr);
            table.addContent(thead);
            Element tbody = new Element("tbody");
            for (int j = 0; j < hashMapArray.length; j++) {

                logger.info("creating data row");
                Iterator myIterator = hashMapArray[j].values().iterator();
                headIterator = hashMapArray[0].keySet().iterator();

                tr = new Element("tr");

                while (myIterator.hasNext()) {
                    Element td = new Element("td");

                    Element span = new Element("span");
                    Object value = myIterator.next();
                    Object head = headIterator.next();
                    String formattedValue =
                            DataFormatter.getFormat(value, data
                                    .getDataFormatter());
                    String sortedValue;
                    if (formattedValue == null) {
                        formattedValue = "null";
                        sortedValue = "null";
                    } else {
                        formattedValue =
                                data.getDataFormatter().replaceLabel(
                                        formattedValue, mySeries.getValue(j),
                                        data);
                        sortedValue = value.toString();
                    }
                    span.addContent(formattedValue);
                    span.setAttribute("sort", sortedValue);
                    span.setAttribute("row", "" + rowCounter);
                    span.setAttribute("column", head.toString());
                    td.addContent(span);
                    tr.addContent(td);
                }

                tbody.addContent(tr);
                rowCounter++;
            }// for mySeries
            table.addContent(tbody);
            id.addContent(table);
            yuicontent.addContent(id);
        }// for plotSet
        navset.addContent(yuicontent);
        return root;

    }

    public void setData(Data data) {
        this.data = data;
    }

}
