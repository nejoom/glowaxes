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

import glowaxes.glyphs.IGlyph;

/**
 * This class represents a node with it's location and the required space for
 * it's corresponding label. The corresponding coordinates are normalized to be
 * greater than 0 in each component
 * 
 * @author Ebner Dietmar, ebner@apm.tuwien.ac.at
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Node {

    /** The Constant DEFAULT_FONT. */
    static public final String DEFAULT_FONT = "Arial";

    /** The Constant DEFAULT_FONT_SIZE. */
    static public final int DEFAULT_FONT_SIZE = 16;

    /** The Constant padding. */
    final static int padding = 8;

    /** The Constant pointFilling. */
    final static String pointFilling = ".";

    /**
     * constructs a new node. width and height will be determined by the needs
     * of the text (default value)
     */
    final static int spacing = 16;

    /** the name of this city. */
    private final IGlyph glyph;

    /** the height of the corresponding label. */
    private double height = 0;

    /** The parent. */
    private final Instance parent;

    /** the priority of this city (not yet used). */
    private double priority = 0;

    /** the width of the corresponding label. */
    private double width = 0;

    /** the x coordinate of the city. */
    private double x = 0;

    /** the x coordinate of the city. */
    private double y = 0;

    /**
     * Instantiates a new point feature.
     * 
     * @param _x
     *            the _x
     * @param _y
     *            the _y
     * @param p
     *            the p
     * @param glyph
     *            the glyph
     * @param _parent
     *            the _parent
     */
    public Node(double _x, double _y, double p, IGlyph glyph, Instance _parent) {
        x = _x;
        y = _y;

        parent = _parent;

        this.glyph = glyph;
        width = glyph.getGlyphWidth();
        height = glyph.getGlyphHeight();

        if (glyph.equals(pointFilling))
            p = 0.5;
        else
            priority = p;

    }

    // public Node(double _x, double _y, double p, String _text,
    // String _style) {
    // new Node(_x, _y, p, _text, _style, null, null);
    // }

    /**
     * returns true if and only if the label can intersect in any location with
     * the label of the given node "node".
     * 
     * @param node
     *            the node to compare
     * 
     * @return true, if can intersect
     */
    public boolean canIntersect(Node node) {
        return ((getWidth() + node.getWidth() >= Math.abs(getX() - node.getX())) && (getHeight()
                + node.getHeight() >= Math.abs(getY() - node.getY())));
    }

    /**
     * Gets the glyph.
     * 
     * @return glyph representing the label
     */
    public IGlyph getGlyph() {
        return glyph;
    }

    /**
     * Gets the height.
     * 
     * @return Height of the label
     */
    public double getHeight() {
        return height;
    }

    /**
     * constructs a new node with the given size and width.
     * 
     * @return the max width
     */
    /*
     * public Node(double _x, double _y, double lbl_size_x, double lbl_size_y,
     * double p, String _text, String _font, int _fontsize) { x = _x; y = _y;
     * width = lbl_size_x; height = lbl_size_y; priority = p; text = _text; font =
     * _font; fontsize = _fontsize;
     * 
     * style = "font-family:"+font+";font-size: "+fontsize; }
     */

    public double getMaxWidth() {
        if (parent == null)
            return -1;
        else
            return parent.getMapWidth();
    }

    public double getMaxHeight() {
        if (parent == null)
            return -1;
        else
            return parent.getMapHeight();
    }

    /**
     * Gets the priority.
     * 
     * @return Priority of the label
     */
    public double getPriority() {
        return priority;
    }

    /**
     * Gets the width.
     * 
     * @return Width of the label
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the x.
     * 
     * @return x-coodinate of the label
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y.
     * 
     * @return y-coodinate of the label
     */
    public double getY() {
        return y;
    }

    // /**
    // * @param d
    // * new x-coordinate
    // */
    // public void setX(double d) {
    // x = d;
    // }
    //
    // /**
    // * @param d
    // * new y-coordinate
    // */
    // public void setY(double d) {
    // y = d;
    // }

    /**
     * Priority of the label.
     * 
     * @param priority
     *            the priority
     */
    public void setPriority(double priority) {
        this.priority = priority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        String newLine = System.getProperty("line.separator");
        sb.append("x: " + x + newLine);
        sb.append("y: " + y + newLine);
        sb.append("width: " + width + newLine);
        sb.append("height: " + height + newLine);
        sb.append("priority: " + priority + newLine);
        sb.append("text: " + glyph + newLine);
        return sb.toString();

    }
}
