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

import java.io.StringWriter;
import org.apache.log4j.Logger;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.Format;

// TODO: Auto-generated Javadoc
/**
 * The Class TextProcessor.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class TextProcessor {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(TextProcessor.class.getName());

    // private static final String hexArray = "0123456789ABCDEF";

    /**
     * Element to string.
     * 
     * @param element the element
     * 
     * @return the string
     */
    public static String elementToString(Element element) {

        Format formatter = Format.getPrettyFormat();
        formatter.setEncoding("US-ASCII");
        formatter.setIndent("  ");

        ChartOutputter serializer = // new XMLOutputter(formatter, true,
                // "UTF-8");
                new ChartOutputter(formatter);

        String xml = null;
        try {
            StringWriter sw = new StringWriter();
            serializer.output(element, sw);

            xml = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Gets the semi colon separated value.
     * 
     * @param att the att
     * @param key the key
     * 
     * @return the semi colon separated value
     */
    public static String getSemiColonSeparatedValue(Attribute att, String key) {
        if (att == null)
            return null;

        return getSemiColonSeparatedValue(att.getValue(), key);
    }

    /**
     * Gets the semi colon separated value.
     * 
     * @param att the att
     * @param key the key
     * 
     * @return the semi colon separated value
     */
    public static String getSemiColonSeparatedValue(String att, String key) {
        if (att == null) {
            return null;
        }
        String string = att;
        if (key == null) {
            return null;
        }

        if (string == null)
            string = "";

        String[] result = string.split(";");

        String value = null;

        for (int x = 0; x < result.length; x++) {
            String tmp = result[x].trim();
            if (tmp.indexOf(key) == 0) {
                value = tmp.split(":")[1];
            }

        }

        return value;

    }

    /**
     * Replace.
     * 
     * @param psWord the ps word
     * @param psReplace the ps replace
     * @param psNewSeg the ps new seg
     * 
     * @return the string
     */
    public static String replace(String psWord, String psReplace,
            String psNewSeg) {
        StringBuffer lsNewStr = new StringBuffer();

        // Tested by DR 03/23/98 and modified
        int liFound = 0;
        int liLastPointer = 0;

        do {

            liFound = psWord.indexOf(psReplace, liLastPointer);

            if (liFound < 0)
                lsNewStr.append(psWord
                        .substring(liLastPointer, psWord.length()));

            else {

                if (liFound > liLastPointer)
                    lsNewStr.append(psWord.substring(liLastPointer, liFound));

                lsNewStr.append(psNewSeg);
                liLastPointer = liFound + psReplace.length();
            }

        } while (liFound > -1);

        return lsNewStr.toString();
    }

    /**
     * Replace.
     * 
     * @param text the text
     * @param searchString the search string
     * @param replaceString the replace string
     * @param caseSensitive the case sensitive
     * 
     * @return the string
     */
    public static String replace(String text, String searchString,
            String replaceString, boolean caseSensitive) {
        if (text == null)
            return null;

        // add one space for when searchstring is at very end
        // text = text.concat(" ");

        // check if the text contains "seachString"
        int length = text.length();
        int lengthSearch = searchString.length();

        StringBuffer validatingText = new StringBuffer(text);

        for (int i = length; i >= lengthSearch; i--) {
            if (caseSensitive) {

                if (validatingText.substring(i - lengthSearch, i).equals(
                        searchString)) {
                    validatingText.replace(i - lengthSearch, i, replaceString);
                }

                // if we replaced the end with a shorter word then
                // correct i
                if (i > validatingText.length()) {
                    i = validatingText.length();
                }

            } else {

                if (validatingText.substring(i - lengthSearch, i)
                        .equalsIgnoreCase(searchString)) {

                    validatingText.replace(i - lengthSearch, i, replaceString);
                }

                // if we replaced the end with a shorter word then
                // correct i
                if (i > validatingText.length()) {
                    i = validatingText.length();
                }

            }// if else
        }// for

        // get rid of first space added
        // int mylength = validatingText.length()-1;
        // validatingText.setLength(mylength);
        return validatingText.toString();

    }

    /**
     * Sets the semi colon separated key.
     * 
     * @param att the att
     * @param key the key
     * @param value the value
     */
    public static void setSemiColonSeparatedKey(Attribute att, String key,
            String value) {
        if (att == null) {
            return;
        }

        if (key == null)
            return;

        String string = att.getValue();
        if (string == null)
            string = "";

        String[] result = string.split(";");

        String returnString = "";
        boolean found = false;

        for (int x = 0; x < result.length; x++) {
            String tmp = result[x].trim();
            if (tmp.indexOf(key.concat(":")) == 0) {
                if (value != null && !found) {
                    returnString += key.concat(":").concat(value);
                    returnString = returnString.concat(";");
                }
                found = true;
            } else {
                returnString = returnString.concat(result[x].trim());
                returnString = returnString.concat(";");
            }

        }
        if (!found && value != null) {
            returnString =
                    returnString.concat(key).concat(":").concat(value).concat(
                            ";");
            // System.out.println("!found returnString: " + returnString);
        }

        if (value == null) {
            System.out.println("value: " + value);
            System.out.println("returnString: " + returnString);
            System.out.println("key: " + key);

        }

        att.setValue(returnString);

    }

    /**
     * Sets the semi colon separated key.
     * 
     * @param att the att
     * @param key the key
     * @param value the value
     * 
     * @return the string
     */
    public static String setSemiColonSeparatedKey(String att, String key,
            String value) {
        if (att == null) {
            return null;
        }

        if (key == null)
            return att;

        String string = att;
        if (string == null)
            string = "";

        String[] result = string.split(";");

        String returnString = "";
        boolean found = false;

        for (int x = 0; x < result.length; x++) {
            String tmp = result[x].trim();
            if (tmp.indexOf(key.concat(":")) == 0) {
                if (value != null && !found) {
                    returnString += key.concat(":").concat(value);
                    returnString = returnString.concat(";");
                }
                found = true;
            } else {
                returnString = returnString.concat(result[x].trim());
                returnString = returnString.concat(";");
            }

        }
        if (!found && value != null) {
            returnString =
                    returnString.concat(key).concat(":").concat(value).concat(
                            ";");
            // System.out.println("!found returnString: " + returnString);
        }

        if (value == null) {
            System.out.println("value: " + value);
            System.out.println("returnString: " + returnString);
            System.out.println("key: " + key);

        }

        return returnString;

    }

    /**
     * Instantiates a new text processor.
     */
    private TextProcessor() {
    }

}// class
