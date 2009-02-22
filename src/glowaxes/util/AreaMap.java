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
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.Format;

// TODO: Auto-generated Javadoc
/**
 * The Class AreaMap.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class AreaMap {

    /**
     * The Class Area.
     */
    private class Area {

        /** The accesskey. */
        private String accesskey;

        /** The alt. */
        private String alt;

        /** The area id. */
        private final int areaId;

        /** The content. */
        private final Element content;

        /** The coords. */
        private final String coords;

        /** The dir. */
        private String dir;

        /** The events. */
        private final String events;

        /** The href. */
        private String href;

        /** The id. */
        private String id;

        /** The lang. */
        private String lang;

        /** The name. */
        private String name;

        /** The shape. */
        private final String shape;

        /** The style. */
        private String style;

        /** The tabindex. */
        private String tabindex;

        /** The target. */
        private String target;

        /** The title. */
        private String title;

        /**
         * Instantiates a new area.
         * 
         * @param _shape
         *            the _shape
         * @param _coords
         *            the _coords
         * @param _events
         *            the _events
         * @param _content
         *            the _content
         */
        public Area(String _shape, String _coords, String _events,
                Element _content) {

            href = "noscript.html";
            shape = _shape;
            coords = _coords;
            events = _events;
            areaId = internalId++;
            content = _content;

        }

        /**
         * Instantiates a new area.
         * 
         * @param _shape
         *            the _shape
         * @param _coords
         *            the _coords
         * @param _events
         *            the _events
         * @param _content
         *            the _content
         */
        public Area(String _shape, String _coords, String _events,
                Element _content, String _href) {

            href = "noscript.html";
            shape = _shape;
            coords = _coords;
            events = _events;
            areaId = internalId++;
            content = _content;
            href = _href;

        }

        /**
         * Instantiates a new area.
         * 
         * @param _id
         *            the _id
         * @param _href
         *            the _href
         * @param _target
         *            the _target
         * @param _shape
         *            the _shape
         * @param _coords
         *            the _coords
         * @param _events
         *            the _events
         * @param _content
         *            the _content
         */
        public Area(String _id, String _href, String _target, String _shape,
                String _coords, String _events, Element _content) {

            id = _id;
            title = _id;
            name = _id;
            alt = _id;
            href = _href;
            target = _target;
            shape = _shape;
            coords = _coords;
            events = _events;
            areaId = internalId++;
            content = _content;

        }

        /**
         * Instantiates a new area.
         * 
         * @param _id
         *            the _id
         * @param _lang
         *            the _lang
         * @param _dir
         *            the _dir
         * @param _title
         *            the _title
         * @param _style
         *            the _style
         * @param _name
         *            the _name
         * @param _alt
         *            the _alt
         * @param _href
         *            the _href
         * @param _target
         *            the _target
         * @param _tabindex
         *            the _tabindex
         * @param _accesskey
         *            the _accesskey
         * @param _shape
         *            the _shape
         * @param _coords
         *            the _coords
         * @param _events
         *            the _events
         * @param _content
         *            the _content
         */
        public Area(String _id, String _lang, String _dir, String _title,
                String _style, String _name, String _alt, String _href,
                String _target, String _tabindex, String _accesskey,
                String _shape, String _coords, String _events, Element _content) {

            id = _id;
            lang = _lang;
            dir = _dir;
            title = _title;
            style = _style;
            name = _name;
            alt = _alt;
            href = _href;
            target = _target;
            tabindex = _tabindex;
            accesskey = _accesskey;
            shape = _shape;
            coords = _coords;
            events = _events;
            areaId = internalId++;
            content = _content;

        }

        /**
         * Gets the area element.
         * 
         * @return the area element
         */
        public Element getAreaElement() {

            String strategy = "popup";

            Element area = new Element("area");
            if (id != null)
                area.setAttribute("id", id);
            if (lang != null)
                area.setAttribute("lang", lang);
            if (dir != null)
                area.setAttribute("dir", dir);
            if (title != null)
                area.setAttribute("title", title);
            if (style != null)
                area.setAttribute("style", style);
            if (name != null)
                area.setAttribute("name", name);
            if (alt != null)
                area.setAttribute("alt", alt);
            if (href != null)
                area.setAttribute("href", href);
            else {
                area.setAttribute("onclick", "return false;");
                // area.setAttribute("href","");
            }
            if (href != null && target != null)
                area.setAttribute("target", target);
            if (tabindex != null)
                area.setAttribute("tabindex", tabindex);
            if (accesskey != null)
                area.setAttribute("accesskey", accesskey);
            if (shape != null)
                area.setAttribute("shape", shape);
            if (coords != null)
                area.setAttribute("coords", coords);
            if (strategy.equals("popup")) {
                area.setAttribute("onmouseout", "_hide();");
                area.setAttribute("onmouseover", "_show(event,'id" + areaId
                        + "');");

            }
            if (logger.isDebugEnabled() && events != null)
                logger.debug("Events: " + events);

            return area;
        }
    }

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(AreaMap.class.getName());

    /**
     * Gets the image tag.
     * 
     * @param src
     *            the src
     * @param usemap
     *            the usemap
     * @param title
     *            the title
     * 
     * @return the image tag
     */
    public static String getImageTag(String src, String usemap, String title,
            double width, double height) {

        Element wrap = new Element("div");
        wrap.setAttribute("style", "position: relative;");
        Element image = new Element("img");
        image.setAttribute("src", src);
        image.setAttribute("border", "0");
        image.setAttribute("width", "" + width);
        image.setAttribute("height", "" + height);
        if (usemap != null)
            image.setAttribute("usemap", "#" + usemap);
        if (title != null) {
            image.setAttribute("title", title);
            image.setAttribute("alt", title);
        }

        Element a = new Element("a");
        a.setAttribute("href", "");
        a.setAttribute("onclick", "");

        Element div = new Element("div");
        div.setAttribute("class", "frame");
        div.setAttribute("id", usemap);
        div.addContent("");
        a.addContent(div);
        wrap.addContent(image);
        wrap.addContent(a);
        return print(wrap);

    }

    // private final HashMap<String, String> popups = new HashMap<String,
    // String>();

    /**
     * Prints the element.
     * 
     * @param element
     *            the element
     * 
     * @return the string
     */
    public static String print(Element element) {

        // SAXBuilder builder = new SAXBuilder();
        Format formatter = Format.getRawFormat();
        formatter.setEncoding("US-ASCII");
        formatter.setIndent("  ");
        formatter.setLineSeparator(System.getProperty("line.separator"));

        ChartOutputter serializer = new ChartOutputter(formatter);

        String xml = null;
        try {
            StringWriter sw = new StringWriter();
            serializer.output(element, sw);
            xml = sw.toString();
        } catch (Exception e) {
            logger.error(e);
        }
        // logger.error(xml);
        return xml;
    }

    /** The areas. */
    private final ArrayList<Area> areas = new ArrayList<Area>();

    /** The internal id. */
    private int internalId = 0;

    /** The map name. */
    private String mapName;

    // private void setMapName(String _mapName) {
    // mapName = _mapName;
    // }
    //
    // private String getMapName() {
    // return mapName;
    // }

    /**
     * Instantiates a new area map.
     */
    @SuppressWarnings("unused")
    private AreaMap() {
    }

    /**
     * Instantiates a new area map.
     * 
     * @param _mapName
     *            the _map name
     */
    public AreaMap(String _mapName) {
        mapName = _mapName;
    }

    /**
     * Adds the area.
     * 
     * @param _shape
     *            the _shape
     * @param _coords
     *            the _coords
     * @param _events
     *            the _events
     * @param _content
     *            the _content
     */
    public void addArea(String _shape, String _coords, String _events,
            Element _content) {
        Area myArea = new Area(_shape, _coords, _events, _content);
        areas.add(myArea);
    }

    /**
     * Adds the area.
     * 
     * @param _shape
     *            the _shape
     * @param _coords
     *            the _coords
     * @param _events
     *            the _events
     * @param _content
     *            the _content
     * @param _href
     *            the _href
     */
    public void addArea(String _shape, String _coords, String _events,
            Element _content, String _href) {
        Area myArea = new Area(_shape, _coords, _events, _content, _href);
        areas.add(myArea);
    }

    /**
     * Adds the area.
     * 
     * @param _id
     *            the _id
     * @param _href
     *            the _href
     * @param _target
     *            the _target
     * @param _shape
     *            the _shape
     * @param _coords
     *            the _coords
     * @param _events
     *            the _events
     * @param _content
     *            the _content
     */
    public void addArea(String _id, String _href, String _target,
            String _shape, String _coords, String _events, Element _content) {
        Area myArea =
                new Area(_id, _href, _target, _shape, _coords, _events,
                        _content);
        areas.add(myArea);
    }

    /**
     * Adds the area.
     * 
     * @param _id
     *            the _id
     * @param _lang
     *            the _lang
     * @param _dir
     *            the _dir
     * @param _title
     *            the _title
     * @param _style
     *            the _style
     * @param _name
     *            the _name
     * @param _alt
     *            the _alt
     * @param _href
     *            the _href
     * @param _target
     *            the _target
     * @param _tabindex
     *            the _tabindex
     * @param _accesskey
     *            the _accesskey
     * @param _shape
     *            the _shape
     * @param _coords
     *            the _coords
     * @param _events
     *            the _events
     * @param _content
     *            the _content
     */
    public void addArea(String _id, String _lang, String _dir, String _title,
            String _style, String _name, String _alt, String _href,
            String _target, String _tabindex, String _accesskey, String _shape,
            String _coords, String _events, Element _content) {
        Area myArea =
                new Area(_id, _lang, _dir, _title, _style, _name, _alt, _href,
                        _target, _tabindex, _accesskey, _shape, _coords,
                        _events, _content);
        areas.add(myArea);
    }

    /**
     * Gets the area map.
     * 
     * @return the area map
     */
    public String getAreaMap() {
        Element map = new Element("map");
        if (mapName != null)
            map.setAttribute("name", mapName);
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).content != null
                    && !areas.get(i).content.getTextTrim().equals(""))
                map.addContent(areas.get(i).getAreaElement());
        }
        return print(map);
    }

    /**
     * Gets the map size.
     * 
     * @return the map size
     */
    public int getMapSize() {
        return areas.size();
    }

    /**
     * Gets the popup map.
     * 
     * @return the popup map
     */
    public String getPopupMap() {
        Element wrapper = new Element("div");
        for (int i = 0; i < areas.size(); i++) {

            if (areas.get(i).content != null
                    && !areas.get(i).content.getTextTrim().equals("")) {

                Element topwrapper = new Element("div");
                topwrapper.setAttribute("id", "id" + areas.get(i).areaId);
                topwrapper.setAttribute("style", "display: none");
                Element content = new Element("div");

                content.setAttribute("class", "dialog");

                Element contentHolder = new Element("div");

                contentHolder.setAttribute("class", "content");
                Element top = new Element("div");
                top.setAttribute("class", "t");
                top.addContent("");
                Element bottom = new Element("div");
                bottom.setAttribute("class", "b");

                Element bottom2 = new Element("div");
                bottom2.addContent("");
                bottom.addContent(bottom2);

                Element p = new Element("span");
                p.setAttribute("id", "label-content");
                // div.setAttribute("style", "display: none");
                p.setAttribute("class", "label-content");

                // logger.error(areas.get(i).content.getName());
                if (areas.get(i).content.getName().equals("string")) {
                    p
                            .addContent(areas.get(i).content.getText());
                } else {
                    try {
                        p.addContent(areas.get(i).content.detach());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                contentHolder.addContent(top);
                contentHolder.addContent(p);

                content.addContent(contentHolder);
                content.addContent(bottom);

                topwrapper.addContent(content);

                wrapper.addContent(topwrapper);
            }
        }
        return print(wrapper);
    }
}
