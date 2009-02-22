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

//import java.io.*;
//import org.jdom.*;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

// TODO: Auto-generated Javadoc
//@SuppressWarnings("unused")
/**
 * The Class ChartOutputter.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class ChartOutputter extends XMLOutputter {
    
    /**
     * Instantiates a new chart outputter.
     */
    public ChartOutputter() {
        super();
    }

    /**
     * Instantiates a new chart outputter.
     * 
     * @param format the format
     */
    public ChartOutputter(Format format) {
        super(format);
    }

    /**
     * Instantiates a new chart outputter.
     * 
     * @param that the that
     */
    public ChartOutputter(XMLOutputter that) {
        super(that);
    }

    /**
     * This will handle printing of a <code>{@link Attribute}</code> list.
     * 
     * @param attributes <code>List</code> of Attribute objcts
     * @param out <code>Writer</code> to use
     * @param parent the parent
     * @param namespaces the namespaces
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("unchecked")
    protected void printAttributes(Writer out, List attributes, Element parent,
            NamespaceStack namespaces) throws IOException {

        // I do not yet handle the case where the same prefix maps to
        // two different URIs. For attributes on the same element
        // this is illegal; but as yet we don't throw an exception
        // if someone tries to do this
        // Set prefixes = new HashSet();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            Namespace ns = attribute.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE)
                    && (ns != Namespace.XML_NAMESPACE)) {
                printNamespace(out, ns, namespaces);
            }

            out.write(" ");
            printQualifiedName(out, attribute);
            out.write("=");

            out.write("\"");
            if (attribute.getAttributeType() == Attribute.ENTITY_TYPE)
                out.write("&" + attribute.getValue() + ";");
            else
                out.write(escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
    }

    /**
     * This will handle printing of any needed <code>{@link Namespace}</code>
     * declarations.
     * 
     * @param ns <code>Namespace</code> to print definition of
     * @param out <code>Writer</code> to use.
     * @param namespaces the namespaces
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void printNamespace(Writer out, Namespace ns,
            NamespaceStack namespaces) throws IOException {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();

        // Already printed namespace decl?
        if (uri.equals(namespaces.getURI(prefix))) {
            return;
        }

        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(uri);
        out.write("\"");
        namespaces.push(ns);
    }

    // Support method to print a name without using att.getQualifiedName()
    // and thus avoiding a StringBuffer creation and memory churn
    /**
     * Prints the qualified name.
     * 
     * @param out the out
     * @param a the a
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void printQualifiedName(Writer out, Attribute a) throws IOException {
        String prefix = a.getNamespace().getPrefix();
        if ((prefix != null) && (!prefix.equals(""))) {
            out.write(prefix);
            out.write(':');
            out.write(a.getName());
        } else {
            out.write(a.getName());
        }
    }

    // Support method to print a name without using elt.getQualifiedName()
    // and thus avoiding a StringBuffer creation and memory churn
    /**
     * Prints the qualified name.
     * 
     * @param out the out
     * @param e the e
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("unused")
    private void printQualifiedName(Writer out, Element e) throws IOException {
        if (e.getNamespace().getPrefix().length() == 0) {
            out.write(e.getName());
        } else {
            out.write(e.getNamespace().getPrefix());
            out.write(':');
            out.write(e.getName());
        }
    }

}
