/**
 * Copyright Dietmar Ebner, 2004, ebner@apm.tuwien.ac.at
 * 
 * This file is part of PFLP.
 * 
 * PFLP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * PFLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PFLP; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Modified by terms above, this version is maintained by glowaxes.org:
 *
 * Copyright 2008-2009 Elements. All Rights Reserved.
 *
 * License version: CPAL 1.0
 *
 * The Original Code is located at:
 *   http://www.ads.tuwien.ac.at/research/labeling/
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
 * Elements is the Initial Developer of the Original Code.
 * The Original Developer is Dietmar Ebner, ebner@apm.tuwien.ac.at
 *
 * The contents of this file may be used under the terms of the Elements 
 * End-User License Agreement (the Elements License), in which case the 
 * provisions of the Elements License are applicable instead of those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */

package glowaxes.labeling;

import glowaxes.glyphs.Data;
import glowaxes.glyphs.IGlyph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

/**
 * This class represents a instance for the map labeling problem. It holds an
 * collection of all points and the given map height and width.
 * 
 * @author Ebner Dietmar, ebner@apm.tuwien.ac.at
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Instance {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Instance.class.getName());

    /** The data. */
    private Data data;

    /** The glyph list. */
    private final LinkedHashMap<IGlyph, Double> glyphList =
            new LinkedHashMap<IGlyph, Double>();

    /** The map height. */
    private double mapHeight = 0;

    /** The map width. */
    private double mapWidth = 0;

    /** The name. */
    private String name = "<not yet set>";

    /** The nodes. */
    private Node[] nodes = null;

    /**
     * Instantiates a new instance.
     */
    public Instance() {
        logger.info("Constructing... ");
    }

    /**
     * Adds a glyph.
     * 
     * @param glyph
     *            a glyph
     */
    public void addGlyph(IGlyph glyph) {
        addGlyph(glyph, 2);
    }

    /**
     * Adds a glyph.
     * 
     * @param glyph
     *            a glyph
     * @param priority
     *            a priority from 0 to 10
     */
    public void addGlyph(IGlyph glyph, double priority) {

        glyphList.put(glyph, priority);
    }

    /**
     * Gets the data.
     * 
     * @return the data
     */
    public Data getData() {
        return data;
    }

    /**
     * returns the height of the instance (map).
     * 
     * @return the map height
     */
    public double getMapHeight() {
        return mapHeight;
    }

    /**
     * returns the width of the instance (map).
     * 
     * @return the map width
     */
    public double getMapWidth() {
        return mapWidth;
    }

    /**
     * returns the name of the file or the string "random instance".
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * returns a reference to the array of nodes.
     * 
     * @return the nodes
     */
    public Node[] getNodes() {
        return (nodes);
    }

    /**
     * Process list.
     * 
     * @param mapWidth
     *            the map width
     * @param mapHeight
     *            the map height
     */
    public void processList(double mapWidth, double mapHeight) {

        if (logger.isDebugEnabled())
            logger.debug("processList of glyphs... ");
        this.mapWidth = mapWidth;

        this.mapHeight = mapHeight;

        ArrayList<Node> v = new ArrayList<Node>();

        Iterator<IGlyph> iterator = glyphList.keySet().iterator();
        while (iterator.hasNext()) {
            IGlyph child = iterator.next();

            Node node =
                    new Node(child.getGlyphX(), child.getGlyphY(), 10,
                            child, this);
            node.setPriority(glyphList.get(child));
            v.add(node);
        }

        nodes = new Node[v.size()];
        for (int i = 0; i < v.size(); i++)
            nodes[i] = v.get(i);

    }

    /**
     * Sets the name.
     * 
     * @param string
     *            the new name of the instance
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * Returns the size of the node.
     * 
     * @return the size of the node.
     */
    public int size() {
        return nodes.length;
    }
}
