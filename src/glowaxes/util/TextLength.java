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

// batik-ext.jar
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGTextElement;

// TODO: Auto-generated Javadoc
/**
 * The Class TextLength.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class TextLength {

    /** The arm. */
    static AutoRefreshMap arm = new AutoRefreshMap("textlength", 10, 10, 100);

    /** The builder. */
    public static org.apache.batik.bridge.GVTBuilder builder =
            new org.apache.batik.bridge.GVTBuilder();

    /** The impl. */
    public static DOMImplementation impl =
            SVGDOMImplementation.getDOMImplementation();

    // private static SVGDocument document = impl.createDocument(svgNS, "svg",
    // null);
    // private static DOMImplementation impl =
    // SVGDOMImplementation.getDOMImplementation();
    // private static String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    // private static Document doc = impl.createDocument(svgNS, "svg", null);
    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(TextLength.class.getName());

    /** The outputter. */
    static org.jdom.output.DOMOutputter outputter =
            new org.jdom.output.DOMOutputter();

    /** The renderer. */
    public static org.apache.batik.gvt.renderer.StaticRenderer renderer =
            new org.apache.batik.gvt.renderer.StaticRenderer();

    /** The image. */
    public static java.awt.image.BufferedImage image = renderer.getOffScreen();

    /** The svg ns. */
    public static String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    /** The user agent. */
    public static org.apache.batik.bridge.UserAgentAdapter userAgent =
            new org.apache.batik.bridge.UserAgentAdapter();

    /** The loader. */
    public static org.apache.batik.bridge.DocumentLoader loader =
            new org.apache.batik.bridge.DocumentLoader(userAgent);

    /** The ctx. */
    public static org.apache.batik.bridge.BridgeContext ctx =
            new org.apache.batik.bridge.BridgeContext(userAgent, loader);
    static {
        ctx.setDynamicState(org.apache.batik.bridge.BridgeContext.DYNAMIC);
    }

    /**
     * Builds the.
     * 
     * @param omDoc
     *            the om doc
     * 
     * @return the org.apache.batik.gvt. graphics node
     */
    public static org.apache.batik.gvt.GraphicsNode Build(SVGOMDocument omDoc) {
        return builder.build(ctx, omDoc);
    }

    // http://www-128.ibm.com/developerworks/xml/library/x-tipcjdm.html
    /**
     * Convert to dom.
     * 
     * @param jdomDocument
     *            the jdom document
     * 
     * @return the org.w3c.dom. document
     * 
     * @throws JDOMException
     *             the JDOM exception
     */
    public static org.w3c.dom.Document convertToDOM(
            org.jdom.Document jdomDocument) throws org.jdom.JDOMException {

        return outputter.output(jdomDocument);
    }

    // public static void traverseSVG( String fileSpec ) {
    // try {
    // java.io.File file = new java.io.File( fileSpec );
    // org.apache.batik.util.XMLResourceDescriptor.setXMLParserClassName("org.apache.crimson.parser.XMLReaderImpl");
    // System.out.println("Reading: " + file );
    // org.apache.batik.bridge.UserAgentAdapter userAgent = new
    // org.apache.batik.bridge.UserAgentAdapter();
    // org.apache.batik.bridge.DocumentLoader loader = new
    // org.apache.batik.bridge.DocumentLoader(userAgent);
    // org.w3c.dom.Document svgDoc =
    // loader.loadDocument(file.toURL().toString());
    // System.out.println( "Building...");
    // org.apache.batik.bridge.BridgeContext ctx = new
    // org.apache.batik.bridge.BridgeContext(userAgent, loader);
    // ctx.setDynamicState(BridgeContext.DYNAMIC);
    //    
    // System.out.println( " ...context...");
    // org.apache.batik.bridge.GVTBuilder builder = new
    // org.apache.batik.bridge.GVTBuilder();
    // System.out.println( " ...builder...");
    // org.apache.batik.gvt.GraphicsNode gvt = builder.build(ctx, svgDoc);
    // System.out.println( " ...graphics node...");
    // System.out.print("Rendering...");
    // System.out.println( " ...build static renderer...");
    // StaticRenderer renderer = new StaticRenderer();
    // System.out.println( " ...set tree...");
    // renderer.setTree(gvt);
    // System.out.println( " ...render image...");
    // BufferedImage image = renderer.getOffScreen();
    // System.out.println("...Ok");
    //            
    // SVGDocument svg = (SVGDocument) svgDoc;
    // System.out.println( svg );
    // Element lines = svg.getElementById("lines");
    // SVGTextElement textElement = (SVGTextElement) lines;
    // System.out.println( lines );
    // SVGRect bbox = textElement.getBBox();
    // //I've got a non-null bounding box here so I'm hoping I'm ok...
    // System.out.println( bbox +" height="+ bbox.getHeight() + "
    // width="+bbox.getWidth() );
    //            
    // Element circle = svg.getElementById("circle");
    // System.out.println( circle );
    // //I am trying to figure out how to get the shape node representing the
    // circle so I can apply
    // //Thomas's suggestion...
    //                                    
    // System.out.println( "Completed successfully...");
    // } catch( Throwable t ) {
    // System.out.println( "Throwable:"+t);
    // }
    //        
    // }

    // static int counter = 0;

    /**
     * Gets the bB.
     * 
     * @param textElement
     *            the text element
     * 
     * @return the bB
     */
    public static SVGRect getBB(SVGTextElement textElement) {
        return textElement.getBBox();
    }

    /**
     * Gets the text height.
     * 
     * @param text
     *            the text
     * @param style
     *            the style
     * 
     * @return the text height
     */
    public static double getTextHeight(String text, String style) {
        double titleHeight = 0;
        if (text != null && !text.equals("")) {

            double fontSize =
                    TypeConverter
                            .getDouble(TextProcessor
                                    .getSemiColonSeparatedValue(style,
                                            "font-size"), 12);

            titleHeight = fontSize;
        }
        return titleHeight;
    }

    /**
     * Gets the text height2.
     * 
     * @param text
     *            the text
     * @param style
     *            the style
     * 
     * @return the text height2
     */
    public static double getTextHeight2(String text, String style) {

        // todo: think through correct behavior.
        if (text == null || style == null)
            return 0d;

        // long t0 = System.currentTimeMillis();
        // logger.fatal("performance1 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();

        // boolean stop = true;
        // if (stop) return 10d;
        // // create
        org.jdom.DocType type =
                new org.jdom.DocType("svg", "-//W3C//DTD SVG 1.1//EN",
                        "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");

        org.jdom.Element svg =
                new org.jdom.Element("svg", "http://www.w3.org/2000/svg");// ,
        // xhtml);

        // logger.fatal("performance2 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();

        //
        // org.apache.batik.bridge.UserAgentAdapter userAgent = new
        // org.apache.batik.bridge.UserAgentAdapter();
        // org.apache.batik.bridge.DocumentLoader loader = new
        // org.apache.batik.bridge.DocumentLoader(userAgent);
        //
        // org.apache.batik.bridge.BridgeContext ctx = new
        // org.apache.batik.bridge.BridgeContext(userAgent, loader);
        // ctx.setDynamicState(org.apache.batik.bridge.BridgeContext.DYNAMIC);
        //    
        // System.out.println( " ...context...");
        // org.apache.batik.bridge.GVTBuilder builder = new
        // org.apache.batik.bridge.GVTBuilder();
        // System.out.println( " ...builder...");
        //
        org.jdom.Element jdomElement2 =
                new org.jdom.Element("text", "http://www.w3.org/2000/svg");
        jdomElement2.addContent(text);
        // System.out.println("getText "+jdomElement.getText());
        jdomElement2.setAttribute("id", "1");
        jdomElement2.setAttribute("style", style);
        // logger.fatal("performance3 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();

        svg.addContent(jdomElement2);
        org.jdom.Document jdomDoc = new org.jdom.Document(svg, type);
        Document doc = null;

        try {
            // org.jdom.output.XMLOutputter outputter = new
            // org.jdom.output.XMLOutputter();
            // outputter.output(jdomDoc, System.out);
            doc = convertToDOM(jdomDoc);
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        // logger.fatal("performance4 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        SVGOMDocument omDoc =
                (SVGOMDocument) org.apache.batik.dom.util.DOMUtilities
                        .deepCloneDocument(doc, new SVGDOMImplementation());
        // logger.fatal("performance5 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        // get the root element (the svg element)
        // Element svgRoot = omDoc.getDocumentElement();
        //
        org.apache.batik.gvt.GraphicsNode gvt = Build(omDoc);
        // System.out.println( " ...graphics node...");
        // System.out.print("Rendering...");
        // System.out.println( " ...build static renderer...");
        // org.apache.batik.gvt.renderer.StaticRenderer renderer = new
        // org.apache.batik.gvt.renderer.StaticRenderer();
        // System.out.println( " ...set tree...");
        // logger.fatal("performance6 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        renderer.setTree(gvt);
        // logger.fatal("performance7 " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        // System.out.println( " ...render image...");
        // java.awt.image.BufferedImage image = renderer.getOffScreen();
        // System.out.println("...Ok");
        //
        Element myElement = omDoc.getElementById("1");
        // logger.fatal("performance8a " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        // System.out.println(myElement);
        SVGTextElement textElement = (SVGTextElement) myElement;
        // System.out.println( textElement );
        // logger.fatal("performance8b " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        SVGRect bbox = getBB(textElement); // textElement.getBBox();
        // logger.fatal("performance8c " + (System.currentTimeMillis() - t0));
        // t0 = System.currentTimeMillis();
        // System.out.println( "textElement.getCTM() : " +
        // textElement.getCTM().getA()
        // );
        // System.out.println( "textElement.getBBox() : " + gvt.isVisible() );

        // I've got a non-null bounding box here so I'm hoping I'm ok...
        // System.out.println( "bbox: " + bbox );
        // System.out.println( bbox +" height="+ bbox.getHeight() + "
        // width="+bbox.getWidth() );
        //
        // System.out.println("jdomDoc: " + jdomDoc);
        // System.out.println("doc: " + doc);
        // System.out.println("svgRoot: " + svgRoot);

        // rectangle.setAttributeNS(null, "style", "fill:red");
        //
        // // attach the rectangle to the svg root element
        // svgRoot.appendChild(rectangle);
        // System.out.println(bbox.getHeight());
        return bbox.getHeight();
    }

    /**
     * Gets the text size2.
     * 
     * @param jdomElement
     *            the jdom element
     * 
     * @return the text size2
     */
    public static double getTextSize2(org.jdom.Element jdomElement) {

        Document doc = impl.createDocument(svgNS, "svg", null);
        // removeAll(doc, Node.ELEMENT_NODE, "text");

        // get the root element (the svg element)
        Element svgRoot = doc.getDocumentElement();

        // create the rectangle
        Element text = doc.createElementNS(svgNS, "text");
        text.setAttributeNS(null, "style", jdomElement.getAttribute("style")
                .getValue());
        text.setAttributeNS(null, "id", "1");
        text.appendChild(doc.createTextNode(jdomElement.getText()));
        svgRoot.appendChild(text);

        // System.out.println( " ...context...");
        // System.out.println( " ...builder...");

        SVGOMDocument omDoc = (SVGOMDocument) doc;

        org.apache.batik.gvt.GraphicsNode gvt = builder.build(ctx, omDoc);
        // System.out.println( " ...graphics node...");
        // System.out.print("Rendering...");
        // System.out.println( " ...build static renderer...");
        // System.out.println( " ...set tree...");
        renderer.setTree(gvt);
        // System.out.println( " ...render image...");
        // System.out.println("...Ok");

        Element myElement = omDoc.getElementById("1");
        // System.out.println(myElement);
        SVGTextElement textElement = (SVGTextElement) myElement;
        // System.out.println( textElement );
        SVGRect bbox = textElement.getBBox();

        return bbox.getWidth();
    }

    /**
     * Gets the text width.
     * 
     * @param text
     *            the text
     * @param style
     *            the style
     * 
     * @return the text width
     */
    public static double getTextWidth(String text, String style) {

        // todo: think through correct behavior.
        if (text == null || style == null || text.length() == 0)
            return 0d;

        if (arm.containsKey(text.concat(style))) {
            return ((Double) arm.get(text.concat(style))).doubleValue();
        }
        // logger.error(counter++);
        // create
        org.jdom.DocType type =
                new org.jdom.DocType("svg", "-//W3C//DTD SVG 1.1//EN",
                        "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");

        org.jdom.Element svg =
                new org.jdom.Element("svg", "http://www.w3.org/2000/svg");

        org.jdom.Element jdomElement2 =
                new org.jdom.Element("text", "http://www.w3.org/2000/svg");
        jdomElement2.addContent(text);

        jdomElement2.setAttribute("id", "1");
        jdomElement2.setAttribute("style", style);

        svg.addContent(jdomElement2);
        org.jdom.Document jdomDoc = new org.jdom.Document(svg, type);
        Document doc = null;

        try {
            doc = convertToDOM(jdomDoc);
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        SVGOMDocument omDoc =
                (SVGOMDocument) org.apache.batik.dom.util.DOMUtilities
                        .deepCloneDocument(doc, new SVGDOMImplementation());

        org.apache.batik.gvt.GraphicsNode gvt = builder.build(ctx, omDoc);

        renderer.setTree(gvt);

        Element myElement = omDoc.getElementById("1");

        SVGTextElement textElement = (SVGTextElement) myElement;

        SVGRect bbox = textElement.getBBox();

        arm.put(text.concat(style), new Double(bbox.getWidth()));
        return bbox.getWidth();
    }

    // This method walks the document and removes all nodes
    // of the specified type and specified name.
    // If name is null, then the node is removed if the type matches.
    /**
     * Removes the all.
     * 
     * @param node
     *            the node
     * @param nodeType
     *            the node type
     * @param name
     *            the name
     */
    public static void removeAll(Node node, short nodeType, String name) {
        if (node.getNodeType() == nodeType
                && (name == null || node.getNodeName().equals(name))) {
            node.getParentNode().removeChild(node);
        } else {
            // Visit the children
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                removeAll(list.item(i), nodeType, name);
            }
        }
    }

    /**
     * Instantiates a new text length.
     */
    private TextLength() {
        logger.info("starting");
    }

}// class
