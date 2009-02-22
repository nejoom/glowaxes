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

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * The Class Gradient represents a gradient color. Produces elements of form: *
 * 
 * <pre>
 * &lt;linearGradient id=&quot;linearGradient|lightyellow|beige|beige&quot; x1=&quot;0%&quot; y1=&quot;0%&quot; x2=&quot;100%&quot; y2=&quot;0%&quot;&gt;
 * &lt;stop offset=&quot;0%&quot; style=&quot;stop-color:lightyellow;stop-opacity:0.8&quot; /&gt;
 * &lt;stop offset=&quot;50%&quot; style=&quot;stop-color:beige;stop-opacity:0.8&quot; /&gt; 
 * &lt;stop offset=&quot;100%&quot; style=&quot;stop-color:beige;stop-opacity:0.8&quot; /&gt;
 * &lt;/linearGradient&gt;
 * </pre>
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Gradient {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(Gradient.class.getName());

    /**
     * Gets the gradient element.
     * 
     * @return the gradient element
     */
    public static Element getGradientElement(String gradientName) {
        Gradient g = new Gradient(gradientName);
        return g.getGradientElement();
    }

    /**
     * Gets the gradient element.
     * 
     * @return the gradient element
     */
    public static String getGradientName(String style) {

        if (!Gradient.isGradientElement(style)) {
            throw new RuntimeException("Style does not have gradient: " + style);
        }
        String[] styles = style.split(";");
        String gradientName = null;

        for (int i = 0; i < styles.length; i++) {
            if (styles[i].toLowerCase().indexOf(":url(#lineargradient") != -1) {

                gradientName =
                        style.substring(styles[i].toLowerCase().indexOf(
                                ":url(#")+6, styles[i].length()-1);

                break;

            } else if (styles[i].toLowerCase().indexOf(":url(#radialgradient") != -1) {

                gradientName =
                        style.substring(styles[i].toLowerCase().indexOf(
                                ":url(#")+6, styles[i].length()-1);

                break;

            }
        }
        return gradientName;
    }

    /**
     * Gets the gradient element.
     * 
     * @return the gradient element
     */
    public static boolean isGradientElement(String style) {

        if (style.toLowerCase().indexOf(":url(#lineargradient") == -1
                && style.toLowerCase().indexOf(":url(#radialgradient") == -1) {
            return false;
        }
        if (style.indexOf("|") == -1) {
            return false;
        }
        return true;
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        // test cases
        Gradient
                .getGradientElement("linearGradient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red|[0%,0%,0%,100%]");
        Gradient
                .getGradientElement("linearGradient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red|horizontal");
        Gradient
                .getGradientElement("linearGradient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red|vertical");
        Gradient
                .getGradientElement("linearGradient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red|diagonal");
        Gradient
                .getGradientElement("linearGradient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red");
        Gradient
                .getGradientElement("radialgRadient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red");
        Gradient
                .getGradientElement("radialgRadient|darkslategrey;stop-opacity:0.5|steelblue|darkslategrey|red");
        AreaMap.print(Gradient
        .getGradientElement(Gradient
                .getGradientName("fill:url(#linearGradient|darkslategrey|steelblue|darkslategrey);stroke:steelblue;stroke-width:1;opacity:0.7")));
    }

    private String gradientName;

    /**
     * Instantiates a new area map.
     */
    @SuppressWarnings("unused")
    public Gradient(String gradientName) {
        this.gradientName = gradientName;
    }

    /**
     * Gets the gradient element.
     * 
     * @return the gradient element
     */
    public Element getGradientElement() {

        String[] token = gradientName.split("\\|");
        int tokenCount = token.length;

        // default to horizontal
        String x1 = "0%";
        String x2 = "100%";
        String y1 = "0%";
        String y2 = "0%";

        // default nice radial gradient
        String cx = "0%";
        String cy = "0%";
        String r = "50%";
        String fx = "50%";
        String fy = "50%";

        String gradientType = token[0];
        if (gradientType.toLowerCase().equals("lineargradient")) {
            gradientType = "linearGradient";
        } else if (gradientType.toLowerCase().equals("radialgradient")) {
            gradientType = "radialGradient";
        } else {

            throw new RuntimeException("Unknown gradient definition: "
                    + gradientName);
        }
        Element gradient = new Element(gradientType);
        gradient.setAttribute("id", gradientName);

        if (gradientType.toLowerCase().equals("lineargradient")) {
            if (token[tokenCount - 1].indexOf("[") != -1) {
                String[] coordinate =
                        token[tokenCount - 1].substring(1,
                                token[tokenCount - 1].length() - 1).split(",");
                int coordinateCount = coordinate.length;
                if (coordinateCount >= 4) {
                    x1 = coordinate[0];
                    y1 = coordinate[1];
                    x2 = coordinate[2];
                    y2 = coordinate[3];
                } else {
                    logger.error("Unknown gradient [x1, y1, x2, y2] in: "
                            + gradientName);
                    logger
                            .error("See: http://www.w3.org/TR/SVG/pservers.html#LinearGradientElement");
                }
                tokenCount--;
            } else if (token[tokenCount - 1].toLowerCase().equals("horizontal")) {
                x1 = "0%";
                x2 = "100%";
                y1 = "0%";
                y2 = "0%";
                tokenCount--;
            } else if (token[tokenCount - 1].toLowerCase().equals("vertical")) {
                x1 = "0%";
                x2 = "0%";
                y1 = "0%";
                y2 = "100%";
                tokenCount--;
            } else if (token[tokenCount - 1].toLowerCase().equals("diagonal")) {
                x1 = "0%";
                x2 = "100%";
                y1 = "0%";
                y2 = "100%";
                tokenCount--;
            }

            gradient.setAttribute("x1", x1);
            gradient.setAttribute("x2", x2);
            gradient.setAttribute("y1", y1);
            gradient.setAttribute("y2", y2);

        } else {
            if (token[tokenCount - 1].indexOf("[") != -1) {
                String[] coordinate =
                        token[tokenCount - 1].substring(1,
                                token[tokenCount - 1].length() - 1).split(",");
                int coordinateCount = coordinate.length;
                if (coordinateCount >= 5) {

                    cx = coordinate[0];
                    cy = coordinate[1];
                    r = coordinate[2];
                    fx = coordinate[3];
                    fy = coordinate[4];

                } else {
                    logger.error("Unknown gradient [cx, cy, r, fx, fy] in: "
                            + gradientName);
                    logger
                            .error("See: http://www.w3.org/TR/SVG/pservers.html#RadialGradientElement");
                }
                tokenCount--;
            }

            gradient.setAttribute("cx", cx);
            gradient.setAttribute("cy", cy);
            gradient.setAttribute("r", r);
            gradient.setAttribute("fx", fx);
            gradient.setAttribute("fy", fy);
        }

        for (int i = 1; i < tokenCount; i++) {
            Element stop = new Element("stop");
            stop.setAttribute("offset", ((i-1) * 100 / (tokenCount - 2)) + "%");
            stop.setAttribute("style", "stop-color:" + token[i]);
            gradient.addContent(stop);
        }

        // AreaMap.print(gradient);
        return gradient;
    }

}
