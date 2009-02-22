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
package glowaxes.tags;

import glowaxes.util.Configuration;
import glowaxes.util.TypeConverter;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/* static methods for implementing static class constants */
// @SuppressWarnings("unchecked")
/**
 * The Class Constants.
 */
public class Constants {

    /** The align vector. */
    public static Vector<String> alignVector = new Vector<String>();

    /** The area grouping vector. */
    public static Vector<String> areaGroupingVector = new Vector<String>();

    /** The counter. */
    private static int counter = 0;

    /** The effect vector. */
    public static Vector<String> effectVector = new Vector<String>();

    // holds default values
    /** The output vector. */
    // public static Vector<String> outputVector = new Vector<String>();
    /** The PROPERTIE s_ file. */
    private static String PROPERTIES_FILE = "glowaxes";

    /** The valign vector. */
    public static Vector<String> valignVector = new Vector<String>();

    static /* initializer */
    {
        // outputVector.add("xml");
        // outputVector.add("svg");
        // outputVector.add("png");
        // outputVector.add("jpg");
        // outputVector.add("pdf");
        // outputVector.add("tiff");
        // outputVector.add("html");
        // outputVector.add("gif");
        // outputVector.add("html-table_tm");

        areaGroupingVector.add("tile-vertical");
        areaGroupingVector.add("tile-horizontal");
        areaGroupingVector.add("combined");

        effectVector.add("none");
        effectVector.add("shadow");
        effectVector.add("shadowgloss");
        effectVector.add("shadowfast");
        effectVector.add("shadowtopexpensive");
        effectVector.add("shadowcartesian");
        effectVector.add("shadowadobe");
        effectVector.add("shadowbrushtm");

        alignVector.add("left");
        alignVector.add("right");
        alignVector.add("center");

        valignVector.add("top");
        valignVector.add("bottom");

    }

    /**
     * Gets the counter.
     * 
     * @return the counter
     */
    public static synchronized int getCounter() {
        return counter;
    }

    /**
     * Gets the counter and increase.
     * 
     * @return the counter and increase
     */
    public static synchronized int getCounterAndIncrease() {
        return counter++;
    }

    /**
     * Gets the default.
     * 
     * @param key
     *            the key
     * 
     * @return the default
     */
    public static String getDefault(String key) {
        return Configuration.getInstance().getValue(PROPERTIES_FILE, key);
    }

    /**
     * Gets the default double.
     * 
     * @param key
     *            the key
     * 
     * @return the default double
     */
    public static Double getDefaultDouble(String key) {
        return TypeConverter.getDouble(Configuration.getInstance().getValue(
                PROPERTIES_FILE, key));
    }
}