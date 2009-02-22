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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("static-access")
/**
 * The Class Bar.
 */
public class Html extends AGeneralPlotter {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Html.class.getName());

    /** The data. */
    private Data data;

    public void addGlyph(IGlyph glyph) {
        // TODO Auto-generated method stub

    }

    public Element drawValue(Value value) {

        // get ready to process element polygon
        Element tr = new Element("tr");
        Element td = new Element("td");
        td.addContent("" + value.getX());
        tr.addContent(td);
        td = new Element("td");
        td.addContent("" + value.getY());
        tr.addContent(td);
        if (value.getLabel() != null) {
            td = new Element("td");
            td.addContent(replaceLabel(value.getLabel().getText().toString(),
                    value).trim());
            tr.addContent(td);
        } else {
            td = new Element("td");
            td.addContent("");
            tr.addContent(td);
        }

        return tr;
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
    public Element render() {

        Group plotSet = data.getGroup();
        Element root = new Element("root");

        for (int i = 0; i < plotSet.getSize(); i++) {

            Series mySeries = plotSet.getSeries(i);

            Element id = new Element("div");
            Element table = new Element("table");

            if (mySeries.getId() != null) {
                id.setAttribute("id", mySeries.getId());
                table.setAttribute("id", "table-" + mySeries.getId());
            }

            Element thead = new Element("thead");

            Element tr = new Element("tr");
            Element td = new Element("td");
            td.addContent("x");
            tr.addContent(td);
            td = new Element("td");
            td.addContent("y");
            tr.addContent(td);
            td = new Element("td");
            td.addContent(mySeries.getId());
            tr.addContent(td);
            thead.addContent(tr);
            table.addContent(thead);

            Element tbody = new Element("tbody");
            for (int j = 0; j < mySeries.getSize(); j++) {

                Value myValue = plotSet.getSeries(i).getValue(j);

                // add the polygon (the bar)
                tbody.addContent(drawValue(myValue));

            }// for mySeries
            table.addContent(tbody);
            id.addContent(table);
            root.addContent(id);
        }// for plotSet
        return root;

    }

    public void setData(Data data) {
        this.data = data;
    }

}
