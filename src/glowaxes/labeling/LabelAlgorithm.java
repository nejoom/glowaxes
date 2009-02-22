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

import java.util.Iterator;

/**
 * The thread implementing the algorithm.
 * 
 * @author Ebner Dietmar, ebner@apm.tuwien.ac.at
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */

public abstract class LabelAlgorithm {

    /** halt? */
    private boolean halt = false;

    /** The instance. */
    protected Instance instance = null;

    /** The iterations. */
    protected long iterations = 0;

    /** The name. */
    protected String name = null;

    /** running? */
    private boolean running = false;

    /** The solution. */
    protected Solution solution = null;

    /**
     * Instantiates a new label algorithm.
     */
    public LabelAlgorithm() {
        super();
    }

    /**
     * checks, if the algorithm is applicable to the current instance.
     * 
     * @return true, if check requirements
     */
    public boolean checkRequirements() {
        return true;
    }

    /**
     * subsequently removes the label with the maximum number of intersections,
     * until no more intersections are detected.
     * 
     * @param solution
     *            the solution
     */
    public void cleanupSolution(Solution solution) {
        Label[] labels = solution.getLabels();
        int n = labels.length;
        int numoverlaps[] = new int[n];

        int next_idx = -1;

        for (int i = 0; i < n; i++) {
            Label l = labels[i];
            numoverlaps[i] = 0;

            if (!l.isUnplacable()) {
                Iterator<Label> it = l.getNeighbours().iterator();
                while (it.hasNext()) {
                    Label l2 = it.next();
                    if (!l2.isUnplacable() && labels[i].doesIntersect(l2))
                        numoverlaps[i]++;
                }
            }

            if (numoverlaps[i] > 0
                    && (next_idx == -1 || numoverlaps[i] > numoverlaps[next_idx]))
                next_idx = i;
        }

        while (next_idx > -1) {
            // remove label next_idx
            Iterator<Label> it = labels[next_idx].getNeighbours().iterator();
            while (it.hasNext()) {
                Label l2 = it.next();
                if (!l2.isUnplacable() && labels[next_idx].doesIntersect(l2))
                    numoverlaps[l2.getIndex()]--;
            }

            labels[next_idx].setUnplacable(true);

            // find next victim
            next_idx = -1;
            for (int i = 0; i < n; i++) {
                if (!labels[i].isUnplacable()
                        && numoverlaps[i] > 0
                        && (next_idx == -1 || numoverlaps[i] > numoverlaps[next_idx]))
                    next_idx = i;
            }
        }
    }

    /**
     * Cleanup solution simple.
     * 
     * @param solution
     *            the solution
     */
    public void cleanupSolutionSimple(Solution solution) {
        // simple algorithm...
        Label[] labels = solution.getLabels();
        int size = labels.length;
        Label found = null;
        int max_ovl = -1;

        do {
            found = null;
            max_ovl = -1;
            for (int i = 0; i < size; i++) {
                if (labels[i].isUnplacable())
                    continue;

                int ovl_count = 0;
                Iterator<Label> it = labels[i].getNeighbours().iterator();
                while (it.hasNext()) {
                    Label l2 = it.next();
                    if (!l2.isUnplacable() && labels[i].doesIntersect(l2))
                        ovl_count++;
                }

                if (ovl_count > 0 && (found == null || ovl_count > max_ovl)) {
                    max_ovl = ovl_count;
                    found = labels[i];
                }
            }

            if (found != null)
                found.setUnplacable(true);

        } while (found != null);
    }

    /**
     * Gets the algorithm name.
     * 
     * @return name of the algorithm
     */
    public final String getAlgorithmName() {
        if (name == null)
            return new String("<not set>");
        else
            return name;
    }

    /**
     * whenever the visualization class creates a copy of the current solution
     * it will call this function to retrieve some debug information about the
     * current algorithm that can be displayed instead of label names.
     * 
     * @param i
     *            the i
     * 
     * @return the label info
     */
    public String getLabelInfo(int i) {
        return null;
    }

    /**
     * Gets the solution.
     * 
     * @return the solution
     */
    public final Solution getSolution() {
        return solution;
    }

    /**
     * whenever the visualization class creates a copy of the current solution
     * it will call this function to retrieve a status string that will be shown
     * in the status bar.
     * 
     * @return the status string
     */
    public String getStatusString() {
        return null;
    }

    /**
     * Returns true, if the current search thread is active.
     * 
     * @return true, if checks if is active
     */
    public boolean isActive() {
        return (!halt);
    }

    /**
     * Returns true, if the current search thread is working. Note that
     * isRunning() implies isActive.
     * 
     * @return true, if checks if is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * does the real work.
     * 
     * @return true <-> algorithm is ready
     */
    protected abstract boolean iterate();

    /**
     * computations that should happen only once.
     */
    protected abstract void precompute();

    /**
     * Run.
     */
    public void run() {
        halt = false;

        if (!checkRequirements()) {
            return;
        }

        iterations = 0;
        precompute();

        // start doing the "real" work...
        while (true) {
            if (!halt) {
                iterations++;

                running = true;

                halt = iterate();

                running = false;

            } else {
                return;
            }
        }
    }

    /**
     * Deactivates the search thread.
     */
    public void softHalt() {
        halt = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new String("pflp search algorithm: " + getAlgorithmName());
    }
}
