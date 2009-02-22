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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * simulated annealing like proposed by Christensen/Marks/Shieber.
 * 
 * @author Ebner Dietmar, ebner@apm.tuwien.ac.at
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */

public class SimulatedAnnealing extends LabelAlgorithm {
    
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(SimulatedAnnealing.class.getName());

    /** The MOVE s_ pe r_ iteration. */
    private static int MOVES_PER_ITERATION = 500;

    /** The REMOV e_ penalty. */
    private static long REMOVE_PENALTY = 1;

    /** The labels. */
    private Label[] labels = null;

    /** The n rejected. */
    private int nRejected = 0;

    /** The n stages. */
    private int nStages = 0;

    /** The n taken. */
    private int nTaken = 0;

    /** The objective. */
    private long objective = 0;

    /** The obstructed labels. */
    private HashSet<Label> obstructedLabels = null;

    // private Solution solution = null;

    /** The size. */
    private int size = 0;

    /** The temperature. */
    private double temperature = 0;

    /**
     * Instantiates a new simulated annealing.
     */
    public SimulatedAnnealing() {
        super();
        name = new String("simulated annealing (4pos)");
    }

    /**
     * Instantiates a new simulated annealing.
     * 
     * @param instance
     *            the instance
     */
    public SimulatedAnnealing(Instance instance) {
        this.instance = instance;
        solution = new Solution(instance, true);
        this.run();
    }

    /**
     * Calc objective function.
     * 
     * @return the long
     */
    private long calcObjectiveFunction() {
        long overplots = 0;
        long removed = 0;
        // count the number of pairwise overplots + #delted labels...
        for (int i = 0; i < size; i++) {
            Label l1 = labels[i];
            if (l1.isUnplacable()) {
                removed++;
            } else {
                Iterator<Label> it = l1.getNeighbours().iterator();
                while (it.hasNext()) {
                    Label o = it.next();
                    if (!o.isUnplacable() && o.doesIntersect(l1))
                        overplots++;
                }
            }
        }

        return (overplots / 2) + (removed * REMOVE_PENALTY);
    }

    /**
     * Cleanup solution.
     */
    private void cleanupSolution() {
        if (Solution.getOptionPointSelection())
            super.cleanupSolution(solution);
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.labeling.LabelAlgorithm#iterate()
     */
    @Override
    protected boolean iterate() {
        // logger.error("iterate");
        // logger.error("obstructedLabels.size() " + obstructedLabels.size());
        // logger.error("MOVES_PER_ITERATION");
        List<Label> newOverlappingLabels = new LinkedList<Label>();
        for (int k = 0; k < MOVES_PER_ITERATION; k++) {
            // choose random overlapping label
            Label l = null;
            boolean found = false;
            do {
                if (obstructedLabels.size() == 0) // optimal solution found
                {
                    logger.info("stopping (optimum found)...");
                    return true;
                }

                int i =
                        Solution.randomGenerator.nextInt(obstructedLabels
                                .size());

                // is there a better way to get the n_th Element out of a set?
                Iterator<Label> it = obstructedLabels.iterator();
                for (int j = 0; j <= i && it.hasNext(); j++)
                    l = it.next();

                // remove non-obstructed labels from the set
                if (l.isUnplacable() || l.isOverlapping())
                    found = true;
                else
                    obstructedLabels.remove(l);

            } while (!found);

            // 0 -> UNPLACED
            // 1 -> TOPLEFT
            // 2 -> TOPRIGHT
            // 3 -> BOTTOMLEFT
            // 4 -> BOTTOMRIGHT
            int old_pos = 0;
            int next_pos = -1;
            if (!l.isUnplacable()) {
                if (l.isTopLeft())
                    old_pos = 1;
                else if (l.isTopRight())
                    old_pos = 2;
                else if (l.isBottomLeft())
                    old_pos = 3;
                else if (l.isBottomRight())
                    old_pos = 4;
                else
                    logger.error("should be never reached [0]! investigate.");
            }

            boolean moved = false;
            if (Solution.getOptionPointSelection()) {
                // overlapping labels are removed with p = 1/4
                if (old_pos != 0 && l.isOverlapping()
                        && Solution.randomGenerator.nextDouble() <= 1. / 4.) {
                    next_pos = 0;
                    moved = true;
                }
            }

            if (!moved) {
                // reinsert or move the label to another randomly chosen
                // position
                if (old_pos == 0)
                    next_pos = Solution.randomGenerator.nextInt(4) + 1;
                else
                    next_pos =
                            (old_pos + Solution.randomGenerator.nextInt(3)) % 4 + 1;

            }

            // calculate the change of the objective function (< 0 means
            // better)...
            long dE = 0;
            Label clone = null;
            newOverlappingLabels.clear();

            if (next_pos == 0) // we remove the label
            {
                dE += REMOVE_PENALTY; // deleted label

                Iterator<Label> orig_neighbours = l.getNeighbours().iterator();
                while (orig_neighbours.hasNext()) {
                    Label n = orig_neighbours.next();
                    if (!n.isUnplacable() && n.doesIntersect(l))
                        dE--;
                }
            } else {
                clone = new Label(l);
                clone.setUnplacable(false);
                switch (next_pos) {
                case 1:
                    clone.moveTo(Label.TOPLEFT);
                    break;
                case 2:
                    clone.moveTo(Label.TOPRIGHT);
                    break;
                case 3:
                    clone.moveTo(Label.BOTTOMLEFT);
                    break;
                case 4:
                    clone.moveTo(Label.BOTTOMRIGHT);
                    break;
                }

                if (old_pos == 0) // original label was unplaced
                    dE -= REMOVE_PENALTY;

                boolean add_once = false;

                Iterator<Label> orig_neighbours = l.getNeighbours().iterator();
                while (orig_neighbours.hasNext()) {
                    Label neigh = orig_neighbours.next();

                    if (neigh.isUnplacable())
                        continue;

                    boolean old_overplots = false;
                    if (!l.isUnplacable())
                        old_overplots = neigh.doesIntersect(l);

                    boolean new_overplots = neigh.doesIntersect(clone);

                    if (new_overplots) {
                        if (!add_once) {
                            add_once = true;
                            newOverlappingLabels.add(l);
                        }

                        newOverlappingLabels.add(neigh);
                    }

                    if (old_overplots && !new_overplots)
                        dE--;
                    else if (!old_overplots && new_overplots)
                        dE++;
                }
            }

            double p = Solution.randomGenerator.nextDouble();

            if (dE == 0 || dE > 0
                    && p >= Math.exp(-((double) dE) / temperature)) {
                nRejected++;
            } else {
                nTaken++;

                // apply the move
                if (next_pos == 0) {
                    l.setUnplacable(true);
                    obstructedLabels.add(l);
                } else if (l.getNode().getPriority() < 9d) {
                    l.moveTo(clone.getOffsetHorizontal(), clone
                            .getOffsetVertical());
                    l.setUnplacable(false);

                    // add new produced intersections to our set of obstructed
                    // labels...
                    Iterator<Label> itInt = newOverlappingLabels.iterator();
                    while (itInt.hasNext()) {
                        Label o = itInt.next();
                        obstructedLabels.add(o);
                    }
                }

                // save new objective function value
                objective += dE;

                // if(objective != calcObjectiveFunction())
                // {
                // System.err.println("da is was faul im staate dänemark!" +
                // objective + " vs. " + calcObjectiveFunction());
                // }
            }

            // cool?
            if (nTaken + nRejected >= 20 * size || nTaken > 5 * size) {
                if (nTaken == 0) // stop
                {
                    logger.info("stopping (nTaken == 0)...");
                    cleanupSolution();
                    return true;
                }

                // decrease temperature by 10%
                temperature = temperature * 0.9;

                if (logger.isDebugEnabled())
                    logger.debug("decreasing temp. to " + temperature
                            + ", nTaken = " + nTaken + ", nRejected = "
                            + nRejected + ", size = " + size);
                nStages++;
                nRejected = 0;
                nTaken = 0;
            }

            if (nStages > 50) // stop
            {
                logger.info("stopping (max stages reached)...");
                cleanupSolution();
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.labeling.LabelAlgorithm#precompute()
     */
    @Override
    protected void precompute() {
        // create new HashSet containing all obstructed labels
        obstructedLabels = new HashSet<Label>();

        // create initial solution
        if (solution == null) {
            solution = new Solution(instance);
            size = solution.size();
            labels = solution.getLabels();
        } else {
            size = solution.size();
            labels = solution.getLabels();

            // check each label if it is a valid 4pos placement
            for (int i = 0; i < size; i++) {
                Label l = labels[i];
                if (!l.isBottomLeft() && !l.isBottomRight() && !l.isTopLeft()
                        && !l.isTopRight())
                    l.moveTo(Label.TOPLEFT);
            }
        }

        // p should be 2/3 when dE = 1
        temperature = -1.0 / Math.log(1. / 3.);
        logger.info("simulated annealing starting with temperature "
                + temperature);
        nTaken = nStages = nRejected = 0;

        // initialize the HashSet with all obstructed labels
        for (int i = 0; i < size; i++) {
            if (labels[i].isOverlapping() || labels[i].isUnplacable())
                obstructedLabels.add(labels[i]);
        }

        objective = calcObjectiveFunction();
    }
}
