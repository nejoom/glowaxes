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

import glowaxes.glyphs.TextLabel;
import glowaxes.util.TextProcessor;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Represents a solution of the map labeling problems (a mapping of the label to
 * (x, y) - coordinates). <br>
 * Note that different threads may access the same solution object (e.g. the
 * search thread & the paint() method). This can be synchronized by using the
 * methods acquireAccess() and releaseAccess().
 * 
 * @author Ebner Dietmar, ebner@apm.tuwien.ac.at
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */

public class Solution {

    /** The logger. */
    private static Logger logger = Logger.getLogger(Solution.class.getName());

    /** The Constant randomGenerator. */
    public static final Random randomGenerator = new Random();

    /**
     * true <-> point selection is enabled.
     * 
     * @return the option point selection
     */
    public static boolean getOptionPointSelection() {
        return false;
    }

    /**
     * R100.
     * 
     * @param d
     *            the d
     * 
     * @return the double
     */
    private static double r100(double d) {
        return (Math.round(d * 100) / 100);
    }

    // the instance for this solution
    /** The instance. */
    private Instance instance = null;

    // where to place the labels
    /** The labels. */
    private Label[] labels = null;

    /**
     * Instantiates a new solution.
     * 
     * @param inst
     *            the inst
     */
    public Solution(Instance inst) {
        this(inst, true);
    }

    /**
     * constructs a new initial Solution to the given instance. For every node a
     * list of neighbours will be generated. The initial position is one of the
     * four possible corner positions
     * 
     * @param _instance
     *            the _instance
     * @param init_solution
     *            the init_solution
     */
    public Solution(Instance _instance, boolean init_solution) {
        instance = _instance;

        if (instance != null) {
            labels = new Label[instance.getNodes().length];

            for (int i = 0; i < instance.getNodes().length; i++)
                labels[i] = new Label(instance.getNodes()[i], i);
        }

        setNeighbours();

        if (init_solution)
            findInitialPlacement();
    }

    /**
     * clones the given solution nodes store only static information, that's why
     * they will not be cloned.
     * 
     * @param s
     *            the s
     */
    public Solution(Solution s) {
        instance = s.instance;
        labels = new Label[s.getLabels().length];

        for (int i = 0; i < s.getLabels().length; i++)
            labels[i] = new Label(s.getLabels()[i]);

        setNeighbours();
    }

    /**
     * returns a clone object of the current solution.
     * 
     * @return the solution
     */
    public Solution copy() {
        return new Solution(this);
    }

    /**
     * counts the number of valid labeled cities (no intersections).
     * 
     * @return the int
     */
    public int countLabeledCities() {
        int set = 0;
        for (int i = 0; i < labels.length; i++) {
            if (!labels[i].isUnplacable() && !labels[i].isOverlapping())
                set++;
        }

        return set;
    }

    /**
     * counts the number of intersecting or unused labels.
     * 
     * @return the int
     */
    public int countUnlabeledCities() {
        return (labels.length - countLabeledCities());
    }

    /**
     * Dumps the current solution in a predefined format to the given file.
     * 
     * @param filename
     *            the filename
     */
    public void dumpSolution(String filename) {
        try {
            // FileWriter f = new FileWriter(filename);
            String out = new String("");

            // System.out.println("Labels.length: " + labels.length);

            for (int i = 0; i < labels.length; i++) {

                Label l = labels[i];
                Node n = l.getNode();
                out += i + ": " /*
                                 * + (" + r100(n.getX()) + "," + r100(n.getY()) + " ) "
                                 */
                        + "[";
                out += r100(l.getWidth()) + " " + r100(l.getHeight()) + "] ";
                out += "\"" + n.getGlyph() + "\" ";
                out +=
                        "(" + r100(l.getTopleft().x) + ","
                                + r100(l.getTopleft().y) + ") ";
                out += (l.isUnplacable() || l.isOverlapping() ? "0" : "1");
                out += "\n";

                // System.out.println("i: " + i + ", " + ;

                // f.write(out);
            }
            System.out.println("out: " + out);
            // f.close();
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("something wrong dumping");
        }
    }

    /**
     * Exists overlapping.
     * 
     * @return true <-> at least one label is obstructed by another one
     */
    public boolean existsOverlapping() {
        for (int k = 0; k < labels.length; k++) {
            if (labels[k].isOverlapping())
                return true;
        }

        return false;
    }

    // public Element getElements() {
    //
    // Element holder = new Element("g");
    //
    // // edgesToSvg(holder);
    //
    // // System.out.println("Labels.length: " + labels.length);
    //
    // for (int i = 0; i < labels.length; i++) {
    //
    // Label l = labels[i];
    // holder.addContent(this.toElement(l));
    //
    // }
    //
    // return holder;
    // }

    /**
     * Find initial placement.
     */
    private void findInitialPlacement() {
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].hasNeighbours())
                labels[i].findInitialPlacement();
            else
                labels[i].moveTo(0., 0.);
        }
    }

    /**
     * finds the label which covers the given point. The values for the x and y
     * component of the point are expected to be in world coordinates.
     * 
     * @param p
     *            the point where
     * 
     * @return a reference to the covering label or null, if no label could be
     *         found
     */
    public Label findLabelByCoordinates(Point p) {
        for (int i = labels.length - 1; i >= 0; i--) {
            Rectangle2D.Double r = labels[i].getRectangle();

            if (r.contains(p) && !labels[i].isUnplacable())
                return labels[i];

            // check if the point surrounds the given Node...
            Node node = labels[i].getNode();
            int size = 2;
            Rectangle2D.Double r_node =
                    new Rectangle2D.Double(node.getX() - size, node.getY()
                            - size, 2 * size, 2 * size);
            if (r_node.contains(p))
                return labels[i];
        }

        return null;
    }

    /**
     * Gets the instance.
     * 
     * @return reference to the associated instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * returns the array of all {@link Label label} corresponding to the given
     * {@link Instance instance}.
     * 
     * @return the labels
     */
    public Label[] getLabels() {
        return labels;
    }

    /**
     * searches the label with the most intersecting area with all of it's
     * neighbours. If more than two labels cover a single point, the
     * corresponding area summed up multiple times.
     * 
     * @return a reference to the found label or null, if no label intersects
     *         another one
     */
    public int getMostIntersectingLabelIndex() {
        double max_area = -1;
        int max_lbl = -1;
        Rectangle2D r = new Rectangle2D.Double();

        for (int i = 0; i < labels.length; i++) {
            Label l = labels[i];

            if (l.isUnplacable())
                continue;

            Vector<Label> n = l.getNeighbours();
            double x = 0.0;
            Iterator<Label> it = n.iterator();

            while (it.hasNext()) {
                Label l2 = it.next();

                if (l2.isUnplacable() || !l.doesIntersect(l2))
                    continue;

                Rectangle2D.intersect(l.getRectangle(), l2.getRectangle(), r);
                x += r.getWidth() * r.getHeight();
            }

            if (x > 0.0 && x > max_area) {
                max_area = x;
                max_lbl = i;
            }
        }

        return max_lbl;
    }

    /**
     * Dumps the current solution in a predefined format to the given file.
     * 
     * @return the solution
     */
    public Element getSolution() {

        logger.info("getSolution of glyphs... ");

        Element out = new Element("g");
        out.setAttribute("desc", "labels");

        try {

            for (int i = labels.length - 1; i >= 0; i--) {

                Label l = labels[i];

                // logger.error(l.toString());

                // logger.error("Working with: " +
                // l.getNode().getGlyph().getId());
                // logger.error(l.isUnplacable());
                // logger.error(l.isOverlapping());

                if ((l.isUnplacable() || l.isOverlapping())
                        && l.getNode().getPriority() < 10) {
                    continue;
                }

                Node n = l.getNode();
                int yOffset = 0;
                if (n.getGlyph() instanceof TextLabel) {
                    yOffset = 4;
                }

                // if (n.getGlyph().equals(Node.pointFilling))
                // continue;

                Element g = new Element("g");
                g.setAttribute("desc", "labels");

                double x = l.getTopleft().x;
                double y = l.getTopleft().y;

                if (n.getPriority() == 10) {
                    // logger.error("BOOL: " + l.isTopLeft());
                } else {

                    // logger.error(l.getNode().getGlyph().toString());
                    // logger.error("tl" + l.isTopLeft());
                    // logger.error("tr" + l.isTopRight());
                    // logger.error("bl" + l.isBottomLeft());
                    // logger.error("br" + l.isBottomRight());
                    // logger.error("lh" + l.getHeight());
                    // logger.error("nh" + n.getHeight());
                    // logger.error("ov" + l.getOffsetVertical());
                    // logger.error("ly" + l.getTopleft().y);
                    // logger.error("ny" + n.getY());

                    if (l.isTopLeft()) {

                        x = x - 1;
                        y = y - yOffset;
                        if (n.getGlyph() instanceof TextLabel) {
                            ((TextLabel) n.getGlyph()).setMarker("tl");

                        }

                    } else if (l.isTopRight()) {

                        x = x + 1;
                        y = y - yOffset;
                        if (n.getGlyph() instanceof TextLabel) {
                            ((TextLabel) n.getGlyph()).setMarker("tr");

                        }

                    } else if (l.isBottomLeft()) {

                        x = x - 1;
                        y = y + yOffset;
                        if (n.getGlyph() instanceof TextLabel) {
                            ((TextLabel) n.getGlyph()).setMarker("bl");

                        }

                    } else if (l.isBottomRight()) {

                        x = x + 1;
                        y = y + yOffset;
                        if (n.getGlyph() instanceof TextLabel) {
                            ((TextLabel) n.getGlyph()).setMarker("br");

                        }

                    }
                }

                out.addContent(makeLabel(n, x, y, l));

                // System.out.println("i: " + i + ", " + ;

                // f.write(out);
            }
            return out;
            // f.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return out;
        }
    }

    /**
     * Make label.
     * 
     * @param n
     *            the n
     * @param x
     *            the x
     * @param y
     *            the y
     * @param l
     *            the l
     * 
     * @return the element
     */
    private Element makeLabel(Node n, double x, double y, Label l) {

        // logger.error("makeLabel of glyphs... ");

        int offset = 10;

        n.getGlyph().setGlyphX(x);
        n.getGlyph().setGlyphY(y);

        if (n.getGlyph() instanceof TextLabel) {
            TextLabel label = (TextLabel) n.getGlyph();
            if (x < -offset) {
                label.setHorizontalOffset(-x - offset);
                label.setGlyphStyle(TextProcessor.setSemiColonSeparatedKey(
                        label.getGlyphStyle(), "opacity", "0.4"));
                if (((TextLabel) n.getGlyph()).getMarker().indexOf("l") != -1) {
                    return new Element("g");
                }
            } else if (x + n.getWidth() > n.getMaxWidth() + offset) {
                label.setHorizontalOffset(n.getMaxWidth() - x - n.getWidth()
                        + offset);
                label.setGlyphStyle(TextProcessor.setSemiColonSeparatedKey(
                        label.getGlyphStyle(), "opacity", "0.4"));
                if (((TextLabel) n.getGlyph()).getMarker().indexOf("r") != -1) {
                    return new Element("g");
                }
            }
        }
        Element label = n.getGlyph().renderGlyph();

        return label;

    }

    /**
     * Sets the neighbours.
     */
    private void setNeighbours() {
        // logger.error("in setNeighbours");
        for (int i = 0; i < labels.length; i++) {
            Label current = labels[i];

            // create a list of neighbours
            for (int j = 0; j < labels.length; j++) {
                if (i != j) {

                    if (current.getNode().getGlyph().getId().equals("legend")) {

                        // logger.error(current.getNode().getX());
                        // logger.error(labels[j].getNode().getX());
                        //
                        // logger.error(current.getNode().getWidth());
                        // logger.error(labels[j].getNode().getWidth());
                        //
                        // logger.error("y1: " + current.getNode().getY());
                        // logger.error("y2: "
                        // + current.getNode().getGlyph().getGlyphY());
                        //
                        // logger.error("xn1: " + labels[j].getNode().getX());
                        // logger.error("xn2: "
                        // + labels[j].getNode().getGlyph().getGlyphX());
                        // logger.error("yn1: " + labels[j].getNode().getY());
                        // logger.error("yn2: "
                        // + labels[j].getNode().getGlyph().getGlyphY());
                        // logger.error("yn3: " + labels[j].getTopleft().y);
                        // logger.error("yn3: " + labels[j].getTopleft().x);
                        // logger.error("yn3 tl: " + labels[j].isTopLeft());
                        // logger.error("yn3 tr: " + labels[j].isTopRight());
                        // logger.error("yn3 bl: " + labels[j].isBottomLeft());
                        // logger.error("yn3 br: " + labels[j].isBottomRight());
                        //
                        // logger.error(current.getNode().getHeight());
                        // logger.error(labels[j].getNode().getHeight());

                    }
                    if (current.getNode().canIntersect(labels[j].getNode()))
                        current.addNeighbour(labels[j]);
                }
            }
            // logger.error(current.getNode().getGlyph().getId());
            // logger.error(current.getNeighbours().size());
        }
    }

    /**
     * returns the size of the solution.
     * 
     * @return the int
     */
    public int size() {
        if (instance == null)
            return 0;
        else
            return instance.size();
    }

}
