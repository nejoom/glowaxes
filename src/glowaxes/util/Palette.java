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
package glowaxes.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
//import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
//import java.util.LinkedHashSet;
//import java.util.Map;
//import java.util.Set;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
//import org.apache.log4j.Logger;

/**
 * The Class Palette.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Palette {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
//    private static Logger logger = Logger.getLogger(Palette.class.getName());

    /** The PROPERTIE s_ file. */
    private static String PROPERTIES_FILE = "palette";

    /**
     * Gets the palette.
     * 
     * @param paletteName the palette name
     * 
     * @return the palette
     */
    public static ArrayList<String> getPalette(String paletteName) {
        return getPalette(paletteName, false);
    }
    
    /**
     * Gets the palette.
     * 
     * @param paletteName the palette name
     * @param reverse the reverse
     * 
     * @return the palette
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<String> getPalette(String paletteName, boolean reverse) {

        Hashtable myHashTable =
                Configuration.getInstance().getHashtable(PROPERTIES_FILE);

        String palette = (String) myHashTable.get(paletteName);

        if (palette == null) {
            throw new RuntimeException("Unkown Palette name: " + paletteName);
        }
        String[] values = palette.split(",");

        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(values));

        //logger.error(reverse);
        if (reverse)
            Collections.reverse(arrayList);

        return arrayList;
    }

    /**
     * Gets the palette.
     * 
     * @param paletteName the palette name
     * @param colors the colors
     * 
     * @return the palette
     */
    public static ArrayList<String> getPalette(String paletteName, int colors) {

        return getPalette(paletteName, colors, false);
    }

    /**
     * Gets the palette.
     * 
     * @param paletteName the palette name
     * @param colors the colors
     * @param reverse the reverse
     * 
     * @return the palette
     */
    public static ArrayList<String> getPalette(String paletteName, int colors, boolean reverse) {
        paletteName = paletteName.toLowerCase();
        String padding = "";
        if (colors <= 9)
            padding = "0";
        return getPalette(paletteName + "." + padding + colors, reverse);
    }

    /**
     * Gets the palette names.
     * 
     * @return the palette names
     */
    @SuppressWarnings("unchecked")
    public static TreeSet getPaletteNames() {
        Hashtable myHashTable =
                Configuration.getInstance().getHashtable(PROPERTIES_FILE);
        return new TreeSet(myHashTable.keySet());
    }

    /**
     * Gets the root palette names.
     * 
     * @return the root palette names
     */
    @SuppressWarnings("unchecked")
    public static TreeSet<String> getRootPaletteNames() {
        Hashtable myHashTable =
                Configuration.getInstance().getHashtable(PROPERTIES_FILE);
        
        TreeSet<String> paletteNames = new TreeSet<String>();
        
        for(Iterator it = myHashTable.keySet().iterator(); it.hasNext(); ) {
            String key1 = (String) it.next();

            String [] key = key1.split("\\.");

            paletteNames.add(key[0]);
            
        }
        return paletteNames;

    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {

//        Palette myPalette = new Palette();
//
//        System.out.println(myPalette.getRootPaletteNames());
//
//        Set mySet = myPalette.getPaletteNames();
//
//        // Iterating over the elements in the set
//        Iterator it = mySet.iterator();
//        while (it.hasNext()) {
//            // Get element
//            String element = (String) it.next();
//            System.out.println("<h1>" + element + "</h1>");
//            ArrayList<String> myList = myPalette.getPalette(element);
//            System.out.println("<table><tr>");
//            for (Iterator<String> it1 = myList.iterator(); it1.hasNext();) {
//                System.out.println("<td>" + it1.next() + "</td>");
//            }
//            System.out.println("</tr></table>");
//        }

    }
}
