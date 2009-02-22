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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Represents a mapping of a label (specified by the corresponding node) to
 * (x,y) corrdinates.
 * 
 * @author Ebner Dietmar, ebner@apm.tuwien.ac.at
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Label {

    /** The Constant BOTTOMLEFT. */
    public static final int BOTTOMLEFT = 3;

    /** The Constant BOTTOMRIGHT. */
    public static final int BOTTOMRIGHT = 4;

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Label.class.getName());

    /** hidden. */
    public static boolean hidden = false;

    /** The Constant TOPLEFT. */
    public static final int TOPLEFT = 1;

    /** The Constant TOPRIGHT. */
    public static final int TOPRIGHT = 2;

    /** Returns the height of the current label. */
    private double heightTemp = -1;

    // index of the label in the solution object...
    /** The index. */
    int index = -1;

    // the position of the label
    /** The lbl_h_offset. */
    private double lbl_h_offset = 0.0; // 0 <= lbl_h_offset <= node.lbl_width

    /** The lbl_v_offset. */
    private double lbl_v_offset = 0.0; // 0 <= lbl_v_offset <= node.lbl_height

    // a reference to all neighbours
    /** The neighbours. */
    private final Vector<Label> neighbours = new Vector<Label>();

    // a reference to the node itself
    /** The node. */
    private Node node = null;

    // todo: optimize; 12%
    // true: 2.3 secs
    // false: 2.6 secs
    /**  optimize?  */
    private final boolean optimize = true;

    /** Returns the top left point of the current label. */
    private Point2D.Double pdTemp = null;

    // true <-> label is not included in the current solution
    /** unplacable? */
    private boolean unplacable = false;

    /** update flag. */
    private boolean updateFlag = false;

    /** Returns the width of the current label. */
    private double widthTemp = -1;

    /**
     * cretes an exact copy of the given label don't forget to add all
     * neighbours to the new object.
     * 
     * @param label
     *            the label
     */
    public Label(Label label) {
        node = label.getNode();
        lbl_h_offset = label.getOffsetHorizontal();
        lbl_v_offset = label.getOffsetVertical();
        unplacable = label.isUnplacable();
        index = label.getIndex();
    }

    /**
     * constructs a new label centerd at the specified city.
     * 
     * @param n
     *            the corresponding
     * @param idx
     *            the idx
     */
    public Label(Node n, int idx) {
        index = idx;
        node = n;
        // moveTo(getWidth() / 2, getHeight() / 2);
        moveTo(BOTTOMRIGHT);
    }

    /**
     * Adds the given label to the Vector of neighbours.
     * 
     * @param l
     *            a reference to a label, that can intersect with the current
     *            label in at least one possible position
     */
    public void addNeighbour(Label l) {
        neighbours.add(l);
    }

    /**
     * Returns true, if the current label intersects with the given label l2.
     * This means, there is at least on point covered by both labels.
     * 
     * @param l2
     *            a reference to the label to compare
     * 
     * @return true, if does intersect
     */
    public boolean doesIntersect(Label l2) {
        Point2D.Double tl1 = getTopleft();
        Point2D.Double tl2 = l2.getTopleft();

        // logger.error(getNode().getGlyph().getId());
        // logger.error(getNode().getGlyph().getGlyphX());
        // logger.error(getNode().getGlyph().getGlyphY());
        // logger.error(getNode().getGlyph().getGlyphWidth());
        // logger.error(getNode().getGlyph().getGlyphHeight());
        //
        // logger.error(l2.getNode().getGlyph().getId());
        // logger.error(l2.getNode().getGlyph().getGlyphX());
        // logger.error(l2.getNode().getGlyph().getGlyphY());
        // logger.error(l2.getNode().getGlyph().getGlyphWidth());
        // logger.error(l2.getNode().getGlyph().getGlyphHeight());

        return ((tl2.x + l2.getWidth() > tl1.x && tl2.x < tl1.x + getWidth()) && (tl2.y
                + l2.getHeight() > tl1.y && tl2.y < tl1.y + getHeight()));
    }

    /**
     * Returns true, if the current label intersects the given label in
     * horizontal direction.
     * 
     * @param l2
     *            a reference to the label to compare
     * 
     * @return true, if does intersect horizontal
     */
    public boolean doesIntersectHorizontal(Label l2) {
        Point2D.Double tl1 = getTopleft();
        Point2D.Double tl2 = l2.getTopleft();

        return ((tl2.x + l2.getWidth() > tl1.x && tl2.x < tl1.x + getWidth()));
    }

    /**
     * Returns true, if the current label intersects the given label in vertical
     * direction.
     * 
     * @param l2
     *            a reference to the label to compare
     * 
     * @return true, if does intersect vertical
     */
    public boolean doesIntersectVertical(Label l2) {
        Point2D.Double tl1 = getTopleft();
        Point2D.Double tl2 = l2.getTopleft();

        return ((tl2.y + l2.getHeight() > tl1.y && tl2.y < tl1.y + getHeight()));
    }

    /**
     * Searches a starting position for the given label.
     */
    public void findInitialPlacement() {
        // use randomly one of the four corners....
        // moveTo(getWidth() * PFLPApp.random_generator.nextInt(2), getHeight()
        // * PFLPApp.random_generator.nextInt(2));
    }

    /**
     * Returns the center point of the current label.
     * 
     * @return the center
     */
    public Point2D.Double getCenter() {
        return new Point2D.Double(
                node.getX() - lbl_h_offset + (getWidth() / 2), node.getY()
                        - lbl_v_offset + (getHeight() / 2));
    }

    /**
     * returns the smallest distance between two rectangles, -1 if they
     * overlap...
     * 
     * @param l2
     *            the l2
     * 
     * @return the distance
     */
    public double getDistance(Label l2) {
        Point2D.Double tl1 = getTopleft();
        Point2D.Double tl2 = l2.getTopleft();

        double d = 0.0;

        boolean h_intersect = doesIntersectHorizontal(l2);
        boolean v_intersect = doesIntersectVertical(l2);

        if (v_intersect && h_intersect) {
            d = -1.0;
        } else if (h_intersect) {
            d = Math.abs(tl1.y - tl2.y);
            if (tl1.y < tl2.y)
                d -= getHeight();
            else
                d -= l2.getHeight();
        } else if (v_intersect) {
            d = Math.abs(tl1.x - tl2.x);
            if (tl1.x < tl2.x)
                d -= getWidth();
            else
                d -= l2.getWidth();
        } else {
            double x1 = tl1.x;
            double y1 = tl1.y;
            double x2 = tl2.x;
            double y2 = tl2.y;

            if (tl1.x > tl2.x) {
                if (tl1.y > tl2.y) {
                    y1 += getHeight();
                    x2 += l2.getWidth();
                } else {
                    x2 += l2.getWidth();
                    y2 += l2.getHeight();
                }
            } else {
                if (tl1.y > tl2.y) {
                    x1 += getWidth();
                    y1 += getHeight();
                } else {
                    y2 += l2.getHeight();
                    x1 += getWidth();
                }
            }

            double dx = x1 - x2;
            double dy = y1 - y2;

            d = Math.sqrt(dx * dx + dy * dy);
        }

        return d;
    }

    /**
     * Gets the height.
     * 
     * @return the height
     */
    public double getHeight() {
        if (!optimize)
            return (node.getHeight());
        if (heightTemp == -1) {
            heightTemp = node.getHeight();
            return (node.getHeight());
        } else {
            return heightTemp;
        }
    }

    /**
     * Gets the index.
     * 
     * @return index of the label in the current instance
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns a Vector of the labels neighbours.
     * 
     * @return a reference to a Vector of neighbours
     */
    public Vector<Label> getNeighbours() {
        return neighbours;
    }

    /**
     * Returns a reference to the corresponding node.
     * 
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Returns the horizontal offset. Zero means the label is positioned with
     * it's left border at the city.
     * 
     * @return the offset horizontal
     */
    public double getOffsetHorizontal() {
        return (lbl_h_offset);
    }

    /**
     * Returns the vertical offset. Zero means the label is positioned with it's
     * top border at the city.
     * 
     * @return the offset vertical
     */
    public double getOffsetVertical() {
        return (lbl_v_offset);
    }

    /**
     * Returns a rectangle representing the current placement.
     * 
     * @return the rectangle
     */
    public Rectangle2D.Double getRectangle() {
        Point2D.Double tl = getTopleft();
        return new Rectangle2D.Double(tl.getX(), tl.getY(), getWidth(),
                getHeight());
    }

    /**
     * Gets the topleft.
     * 
     * @return the topleft
     */
    public Point2D.Double getTopleft() {
        if (!optimize)
            return new Point2D.Double(node.getX() - lbl_h_offset, node.getY()
                    + lbl_v_offset);
        if (pdTemp == null) {
            pdTemp =
                    new Point2D.Double(node.getX() - lbl_h_offset, node.getY()
                            + lbl_v_offset);
            // pdTemp.setLocation(node.getX() - lbl_h_offset, node.getY()
            // - lbl_v_offset);
        } else if (updateFlag) {
            pdTemp.setLocation(node.getX() - lbl_h_offset, node.getY()
                    + lbl_v_offset);
            updateFlag = false;
        }
        return pdTemp;
    }

    /**
     * Gets the width.
     * 
     * @return the width
     */
    public double getWidth() {
        if (!optimize)
            return (node.getWidth());
        if (widthTemp == -1) {
            widthTemp = node.getWidth();
            return (node.getWidth());
        } else {
            return widthTemp;
        }
    }

    /**
     * Returns true, if there is at least one label that could intersect in any
     * position with the current label. If has_neighbours() returns false, any
     * given position is a valid labeling.
     * 
     * @return true, if checks for neighbours
     */
    public boolean hasNeighbours() {
        return (!neighbours.isEmpty());
    }

    /**
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Checks if is bottom left.
     * 
     * @return true <-> label is placed at the upper left corner
     */
    public boolean isBottomLeft() {
        return getOffsetHorizontal() == getWidth() && getOffsetVertical() == 0;
    }

    /**
     * Checks if is bottom right.
     * 
     * @return true <-> label is placed at the upper right corner
     */
    public boolean isBottomRight() {
        return getOffsetHorizontal() == 0 && getOffsetVertical() == 0;
    }

    /**
     * Returns true, if the label intersects with one of it's neighbours.
     * 
     * @return true, if checks if is overlapping
     */
    public boolean isOverlapping() {

        // logger.error("isOverlapping");

        if (isUnplacable()) {
            return false;
        }

        Iterator<Label> it = neighbours.iterator();

        while (it.hasNext()) {
            Label next = it.next();

            if (next.isUnplacable()) {
                continue;
            }

            if (doesIntersect(next)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if is top left.
     * 
     * @return true <-> label is placed at the lower left corner
     */
    public boolean isTopLeft() {
        return getOffsetHorizontal() == getWidth()
                && getOffsetVertical() == -getHeight();
    }

    /**
     * Checks if is top right.
     * 
     * @return true <-> label is placed at the lower right corner
     */
    public boolean isTopRight() {
        return getOffsetHorizontal() == 0
                && getOffsetVertical() == -getHeight();
    }

    /**
     * Returns true, if the label is not included in the current solution (not
     * labeled).
     * 
     * @return true, if checks if is unplacable
     */
    public boolean isUnplacable() {

        // dont go above top, bottom, left and right edges
        if (getTopleft().y - node.getGlyph().getGlyphOffsetTop() < 0
                || (getRectangle().x < 0)
                || ((getRectangle().x + getRectangle().width) > getNode()
                        .getMaxWidth())
                || ((getRectangle().y + getRectangle().height) > getNode()
                        .getMaxHeight())) {
            return true;
        }
        return unplacable;
    }

    /**
     * Moves the label to the position. Note, that the given values are not
     * checked and should be calculated carefully. A offset of (0, 0) means the
     * lower right corner.
     * 
     * @param h_offset
     *            the horizontal offset
     * @param v_offset
     *            the vertical offset
     */
    public void moveTo(double h_offset, double v_offset) {
        lbl_h_offset = h_offset;
        lbl_v_offset = v_offset;
        // TODO: performance
        updateFlag = true;
    }

    /**
     * Move to.
     * 
     * @param pos
     *            the pos
     */
    public void moveTo(int pos) {
        if (pos != TOPLEFT && pos != TOPRIGHT && pos != BOTTOMLEFT
                && pos != BOTTOMRIGHT)
            return;

        double f1 = (pos == TOPLEFT || pos == BOTTOMLEFT) ? 1 : 0;
        double f2 = (pos == TOPLEFT || pos == TOPRIGHT) ? 1 : 0;

        moveTo(f1 * getWidth(), -f2 * getHeight());
    }

    /**
     * Allows to mark the current label as "unset".
     * 
     * @param b
     *            true, if the label shouldn't be included in the current
     *            solution
     */
    public void setUnplacable(boolean b) {
        unplacable = b;
    }

    /**
     */
    public void setHidden(boolean b) {
        hidden = b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        final String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {\n");

        result.append(" isUnplacable(): ");
        result.append(isUnplacable());
        result.append(newLine);

        result.append(" isOverlapping(): ");
        result.append(isOverlapping());
        result.append(newLine);

        result.append(" hasNeighbours(): ");
        result.append(hasNeighbours());
        result.append(newLine);

        result.append(" getWidth(): ");
        result.append(getWidth());
        result.append(newLine);

        result.append(" getHeight(): ");
        result.append(getHeight());
        result.append(newLine);

        result.append(" getOffsetHorizontal(): ");
        result.append(getOffsetHorizontal());
        result.append(newLine);

        result.append(" getOffsetVertical(): ");
        result.append(getOffsetVertical());
        result.append(newLine);

        result.append(" isTopLeft(): ");
        result.append(isTopLeft());
        result.append(newLine);

        result.append(" isTopRight(): ");
        result.append(isTopRight());
        result.append(newLine);

        result.append(" isBottomLeft(): ");
        result.append(isBottomLeft());
        result.append(newLine);

        result.append(" isBottomRight(): ");
        result.append(isBottomRight());
        result.append(newLine);

        result.append(" getIndex(): ");
        result.append(getIndex());
        result.append(newLine);

        result.append(" getNode(): ");
        result.append(getNode());
        result.append(newLine);
        result.append("}");
        return result.toString();
    }

}
