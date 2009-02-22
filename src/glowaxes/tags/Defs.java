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

import glowaxes.util.AreaMap;
import glowaxes.util.Configuration;
import glowaxes.util.Gradient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.jdom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class Defs represents the Defs section of the svg xml. Produces form:
 * 
 * <pre>
 * 
 * &lt;defs&gt;
 * &lt;filter id=&quot;shadow&quot; filterUnits=&quot;objectBoundingBox&quot; x=&quot;-10%&quot; y=&quot;-10%&quot; width=&quot;120%&quot; height=&quot;120%&quot;&gt;
 * &lt;feGaussianBlur in=&quot;SourceAlpha&quot; stdDeviation=&quot;2&quot; result=&quot;BlurAlpha&quot; /&gt;
 * &lt;feOffset in=&quot;BlurAlpha&quot; dx=&quot;4&quot; dy=&quot;-4&quot; result=&quot;OffsetBlurAlpha&quot; /&gt;
 * &lt;feMerge&gt;
 * &lt;feMergeNode in=&quot;OffsetBlurAlpha&quot; /&gt;
 * &lt;feMergeNode in=&quot;SourceGraphic&quot; /&gt;
 * &lt;/feMerge&gt;
 * &lt;/filter&gt;
 * </pre>
 */
@SuppressWarnings("unchecked")
public class Defs {

    /** The defs. */
    private static HashMap<String, Element> defs =
            new HashMap<String, Element>();

    /** initialized. */
    private static boolean initialized = false;

    // Logger instance named after class.
    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Defs.class.getName());

    /** The used defs. */
    private static TreeSet<String> usedDefs = new TreeSet<String>();

    /**
     * Adds the element.
     * 
     * @param id
     *            the id
     * @param element
     *            the element
     */
    @SuppressWarnings("unused")
    private static void addElement(String id, Element element) {
        defs.put(id, element);
    }

    /**
     * Adds the id to the list of used defs.
     * 
     * @param id
     *            the id
     * @return the id string if it is defined in the defs list, otherwise a null
     *         string.
     */
    public static String addUsed(String id) {
        if (defs.containsKey(id)) {
            usedDefs.add(id);
            return id;
        } else {
            return null;
        }
    }

    /**
     * Gets the defs.
     * 
     * @return the defs
     */
    public static TreeSet getDefs() {
        if (!initialized)
            init();
        return new TreeSet(defs.keySet());
    }

    /**
     * Gets the used defs.
     * 
     * @return the used defs
     */
    public static Element getUsedDefs() {

        Element defElement = new Element("defs");

        Iterator<String> itr = usedDefs.iterator();

        while (itr.hasNext())
            defElement.addContent(defs.get(itr.next()).detach());

        return defElement;
    }

    /**
     * Inits the.
     */
    @SuppressWarnings("unused")
    public static void init() {

        final Element xml = Configuration.getInstance().getXml("defs.xml");
        final List<Element> myList = xml.getChildren();

        for (int intIndex = 0; intIndex < myList.size(); intIndex++) {
            if (myList.get(intIndex).getAttribute("id") != null)
                defs.put(myList.get(intIndex).getAttribute("id").getValue(),
                        myList.get(intIndex));
        }

        initialized = true;

    }

    /**
     * The main method.
     * 
     * @param args
     *            the args
     */
    public static void main(String[] args) {

        Defs
                .parseStyle("fill:url(#linearGradient|red|steelblue|[0%,0%,0%,100%]);stroke:url(#shadow);stroke-width:1;opacity:0.7");
        AreaMap.print(Defs.getUsedDefs());

    }

    /**
     * Parses the style for gradient information in form.
     * 
     * @param style
     *            the style to parse and extract gradient information
     * 
     * @return the string
     */
    public static String parseStyle(String style) {

        if (style == null)
            return null;

        if (style.indexOf("url(#") != -1) {

            String token = ";";
            String styles[] = style.split(token);
            for (int i = 0; i < styles.length; i++) {

                if (styles[i].indexOf("url(#") != -1) {

                    String id =
                            styles[i].substring(styles[i].toLowerCase()
                                    .indexOf(":url(#") + 6,
                                    styles[i].length() - 1);

                    if (defs.containsKey(id)) {
                        addUsed(id);
                    } else {

                        if (Gradient.isGradientElement(styles[i])) {
                            String gradientName =
                                    Gradient.getGradientName(styles[i]);

                            Element e =
                                    Gradient.getGradientElement(gradientName);

                            Defs.addElement(gradientName, e);
                            addUsed(id);
                        } else {
                            logger.error("Syntax error in gradient parsing: "
                                    + styles[i]
                                    + ", replacing with empty string.");

                            // ignore and join
                            styles[i] = "";
                            StringBuffer sb = new StringBuffer();

                            for (int x = 0; x < (styles.length - 1); x++) {
                                sb.append(styles[x]);
                                sb.append(token);
                            }
                            sb.append(styles[styles.length - 1]);

                            style = sb.toString();
                        }
                    }// if else defs
                }// if url
            }// for split ;
        }// if url#
        return style;
    }

}
