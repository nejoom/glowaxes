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
 * Based on commercial needs the contents of this file may be used under the
 * terms of the Elements End-User License Agreement (the Elements License), in
 * which case the provisions of the Elements License are applicable instead of
 * those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */
package glowaxes.glyphs;

import glowaxes.data.DataIsland;
import glowaxes.data.Group;
import glowaxes.tags.Constants;
import glowaxes.util.DataFormatter;
import glowaxes.util.TypeConverter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * The Class Data.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Data {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Data.class.getName());

    /** The id. */
    private final String id;

    /** The labels. */
    private Labels labels = new Labels(new HashMap<String, String>());

    /** The parent area. */
    private final Area parentArea;

    /** The data attributes. */
    private HashMap<String, Object> dataAttributes;

    /**
     * Instantiates new data.
     * 
     * @param id the id
     * @param area the area
     */
    @SuppressWarnings("unchecked")
    public Data(String id, Area area) {
        if (id == null)
            throw new IllegalArgumentException("id cannot be null");

        this.id = id;
        this.parentArea = area;

        setDataAttributes(id, area);

        if (dataAttributes == null)
            throw new RuntimeException(
                    "data could not be found in cache with id: " + id);

        dataAttributes.put("area", area);

        if (dataAttributes.get("labels") != null) {
            labels =
                    new Labels((HashMap<String, String>) dataAttributes
                            .get("labels"));

            logger.error("found labels" + dataAttributes.get("labels"));
        }
    }

    /**
     * Gets the data formatter.
     * 
     * @param index the index
     * 
     * @return the data formatter
     */
    @SuppressWarnings("unchecked")
    public DataFormatter getDataFormatter(int index) {
        ArrayList<HashMap<String, String>> al =
                (ArrayList<HashMap<String, String>>) dataAttributes
                        .get("formatList");

        if (al == null) {
            throw new RuntimeException("Format list is null.");
        } else if (index > al.size() - 1) {
            throw new RuntimeException("index is too big.");
        }
        return new DataFormatter(al.get(index));

    }

    /**
     * Gets the data formatter.
     * 
     * @return the data formatter
     */
    @SuppressWarnings("unchecked")
    public DataFormatter getDataFormatter() {
        ArrayList<HashMap<String, String>> al =
                (ArrayList<HashMap<String, String>>) dataAttributes
                        .get("formatList");

        if (al == null) {
            throw new RuntimeException("Format list is null.");
        }
        return new DataFormatter(al.get(0));

    }

    /**
     * Sets the data attributes.
     * 
     * @param id the id
     * @param area the area
     */
    private void setDataAttributes(String id, Area area) {
        dataAttributes =
                DataIsland.SINGLETON.getData(id, area.getParentChart()
                        .getPageContext());

    }

    // Properties, stored in dataAttributes
    // -------------------------------------------------------------------------

    /**
     * Gets the effect.
     * 
     * The effect used to paint the data.
     * 
     * @return the effect
     */
    public String getEffect() {
        return TypeConverter.getString(dataAttributes.get("effect"), null);
    }

    /**
     * Gets the expireTimeMilli.
     * 
     * @return the expire time milli
     */
    public long getExpireTimeMilli() {
        long expireTimeMilli =
                TypeConverter.getLong(dataAttributes.get("expireTimeMilli"), 0);
        return expireTimeMilli;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the source.
     * 
     * @return the group
     */
    public Group getGroup() {
        return (Group) dataAttributes.get("dataGroup");

    }

    /**
     * Sets the source.
     * 
     * @return the group
     */
    public Labels getLabels() {
        return labels;

    }

    /**
     * Gets the palette.
     * 
     * The palette used to paint colors from nice defaults.
     * [primary][rainbow][texture][blue-pallete][red-pallete][green-pallete][yellow-pallete][orange-pallete]
     * 
     * @return the palette
     */
    public String getPalette() {
        return TypeConverter.getString(dataAttributes.get("palette"), Constants
                .getDefault("data.palette"));
    }

    /**
     * Gets the parent area.
     * 
     * @return the parent area
     */
    public Area getParentArea() {
        return parentArea;
    }

    /**
     * Sets the source.
     * 
     * @return the source
     */
    public String getSource() {

        String source =
                TypeConverter.getString(dataAttributes.get("source"), Constants
                        .getDefault("data.source"));

        if (source.indexOf(":/") == -1) {
            if (source.indexOf('/') == 0)
                source = getUriBase().concat(source);
            else
                source = getUriPath().concat("/").concat(source);
        }
        return source;
    }

    /**
     * Gets the uri base.
     * 
     * @return the uri base
     */
    private String getUriBase() {
        HttpServletRequest request =
                (HttpServletRequest) getParentArea().getParentChart()
                        .getPageContext().getRequest();

        URL reconstructedURL;
        try {
            reconstructedURL =
                    new URL(request.getScheme(), request.getServerName(),
                            request.getServerPort(), "");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return reconstructedURL.toString();

    }

    /**
     * Gets the uri path.
     * 
     * @return the uri path
     */
    private String getUriPath() {
        HttpServletRequest request =
                (HttpServletRequest) getParentArea().getParentChart()
                        .getPageContext().getRequest();

        String file = request.getRequestURI();

        file = file.substring(0, file.lastIndexOf('/'));

        URL reconstructedURL;
        try {
            reconstructedURL =
                    new URL(request.getScheme(), request.getServerName(),
                            request.getServerPort(), file);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return reconstructedURL.toString();

    }

    /**
     * To string.
     * 
     * @return a formatted string representation of the get methods
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        final String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {");
        result.append(" source: ");
        result.append(getSource() == null ? "null" : getSource());
        result.append(newLine);
        result.append(" palette: ");
        result.append(getPalette() == null ? "null" : getPalette());
        result.append(newLine);
        // result.append(" shape: ");
        // result.append(getShape());
        result.append(newLine);
        // result.append(" shapeStyle: ");
        // result.append(getShapeStyle());
        result.append(newLine);
        // result.append(" style: ");
        // result.append(getStyle());
        result.append(newLine);

        result.append("}");

        return result.toString();

    }

}
