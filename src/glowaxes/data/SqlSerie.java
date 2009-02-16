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
package glowaxes.data;

import glowaxes.sql.DatabaseStatementsManager;
import glowaxes.tags.Defs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * The Class SqlSerie.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class SqlSerie {

    /** The logger. */
    private static Logger logger = Logger.getLogger(SqlSerie.class.getName());

    /** The content. */
    private String content;

    /** The data source. */
    private String dataSource;

    /** The default label. */
    private String defaultLabel;

    /** The default label background style. */
    private String defaultLabelBackgroundStyle;

    /** The default label style. */
    private String defaultLabelStyle;

    /** The default shape. */
    private String defaultShape;

    /** The default style. */
    private String defaultStyle;

    /** The id. */
    private String id = "";

    /** The legend label. */
    private String legendLabel;

    /** The line style. */
    private String lineStyle = null;

    /** The parameters. */
    private ArrayList<Object> parameters;

    /** The sql attributes. */
    private HashMap<String, String> sqlAttributes;

    /** The hash map array. */
    private HashMap<String, Object>[] hashMapArray;

    /** The var. */
    private String var;

    /**
     * Gets the content.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the data source.
     * 
     * @return the data source
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Gets the parameters.
     * 
     * @return the parameters
     */
    public ArrayList<Object> getParameters() {
        return parameters;
    }

    /**
     * Returns the default label for the values in this Series (if a value's
     * getLabel is null).
     * 
     * @return <code>String</code> default label in this Series.
     */
    public String getDefaultLabel() {
        return defaultLabel;
    }

    /**
     * This will return the default label background svg style for this Series.
     * 
     * @return The <code>String</code> defaultLabelBackgroundStyle for this
     *         Series.
     */
    public String getDefaultLabelBackgroundStyle() {
        return defaultLabelBackgroundStyle;
    }

    /**
     * This will return the default label svg style for this Series.
     * 
     * @return The <code>String</code> defaultLabelStyle for this Series.
     */
    public String getDefaultLabelStyle() {
        return defaultLabelStyle;
    }

    /**
     * This will return the default svg shape element for this Series.
     * 
     * @return <code>String</code> default svg shape element for this Series.
     */
    public String getDefaultShape() {
        return defaultShape;
    }

    /**
     * This will return the default label svg style for this Series.
     * 
     * @return <code>String</code> the default label svg style.
     */
    public String getDefaultShapeStyle() {
        return defaultStyle;
    }

    /**
     * Gets the hash map array.
     * 
     * @return the hash map array
     */
    public HashMap<String, Object>[] getHashMapArray() {
        return hashMapArray;
    }

    /**
     * Gets the var.
     * 
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * This will return the id for this Series.
     * 
     * @return The <code>String</code> id for this Series.
     */
    public String getId() {
        return id;
    }

    /**
     * This will return the legend for this Series.
     * 
     * @return The <code>String</code> legend for this Series.
     */
    public String getLegend() {
        return legendLabel;
    }

    /**
     * This will return the default line style for the values in this Series (if
     * values make a line).
     * 
     * @return <code>String</code> default line style in this Series.
     */
    public String getLineStyle() {

        if (lineStyle == null) {
            lineStyle = defaultStyle;
        }
        return lineStyle;
    }

    /**
     * Gets the sql attributes.
     * 
     * @return the sql attributes
     */
    public HashMap<String, String> getSqlAttributes() {
        return sqlAttributes;
    }

    /**
     * Gets the sql attribute.
     * 
     * @param key
     *            the key
     * 
     * @return the sql attribute
     */
    public String getSqlAttribute(String key) {
        if (sqlAttributes.get(key) == null) {
            return key;
        } else {
            return sqlAttributes.get(key);
        }
    }

    /**
     * Gets the val.
     * 
     * @param key
     *            the key
     * @param i
     *            the i
     * 
     * @return the val
     */
    public Object getVal(String key, int i) {
        return hashMapArray[i].get(sqlAttributes.get(key));
    }

    /**
     * Process sql.
     */
    @SuppressWarnings("unchecked")
    public void processSql() {

        String sql = getContent();
        ArrayList arrayList = getParameters();
        String dataSource = getDataSource();

        logger.info("procesing sql for: " + sql + ", " + dataSource + ", "
                + arrayList);

        hashMapArray =
                DatabaseStatementsManager.getMultipleExecute(sql, arrayList,
                        dataSource);

        logger.info("result hashMapArray: " + hashMapArray);

        if (hashMapArray != null && hashMapArray[0] != null) {
            Iterator<String> iterator = hashMapArray[0].keySet().iterator();

            if (iterator.hasNext()) {
                if (sqlAttributes.get("x") == null)
                    sqlAttributes.put("x", iterator.next());
                else
                    iterator.next();
            }
            if (iterator.hasNext()) {
                if (sqlAttributes.get("y") == null)
                    sqlAttributes.put("y", iterator.next());
            }
        }

    }

    /**
     * Sets the content.
     * 
     * @param _content
     *            the new content
     */
    public void setContent(String _content) {
        content = _content;
    }

    /**
     * Sets the parameters.
     * 
     * @param _parameters
     *            the new parameters
     */
    public void setParameters(ArrayList<Object> _parameters) {
        parameters = _parameters;
    }

    /**
     * Sets the data source.
     * 
     * @param _dataSource
     *            the new data source
     */
    public void setDataSource(String _dataSource) {
        dataSource = _dataSource;
    }

    /**
     * This will set the default label for the values in this Series.
     * 
     * @param _defaultLabel
     *            default label for the values in this Series.
     */
    public void setDefaultLabel(String _defaultLabel) {
        defaultLabel = _defaultLabel;
    }

    /**
     * This will set the default label background style for the values label in
     * this Series.
     * 
     * @param defaultLabelBackgroundStyle
     *            default label background style the values in this Series.
     */
    public void setDefaultLabelBackgroundStyle(
            String defaultLabelBackgroundStyle) {
        this.defaultLabelBackgroundStyle =
                Defs.parseStyle(defaultLabelBackgroundStyle);
    }

    /**
     * This will set the default label svg style for this Series.
     * 
     * @param _defaultLabelStyle
     *            the default label svg style for this Series.
     */
    public void setDefaultLabelStyle(String _defaultLabelStyle) {

        defaultLabelStyle = Defs.parseStyle(_defaultLabelStyle);
        ;
    }

    /**
     * This will set the default svg shape element for this Series.
     * 
     * @param _defaultShape
     *            the default the default svg shape element for this Series used
     *            to plot values.
     */
    public void setDefaultShape(String _defaultShape) {
        defaultShape = _defaultShape;
    }

    /**
     * This will set the default shape style for this Series.
     * 
     * @param _defaultStyle
     *            the default shape svg style for this Series used to plot
     *            values.
     */
    public void setDefaultShapeStyle(String _defaultStyle) {
        defaultStyle = Defs.parseStyle(_defaultStyle);
    }

    /**
     * This will set the default label for the values in this Series.
     * 
     * @param _Id
     *            set the id, identifying this class for this Series. id does
     *            not have to be unique.
     */
    public void setId(String _Id) {
        id = _Id;
    }

    /**
     * This will set the legend for this Series.
     * 
     * @param _legendLabel
     *            the legend for this Series
     */
    public void setLegend(String _legendLabel) {
        legendLabel = _legendLabel;
    }

    /**
     * This will set the line style for this Series.
     * 
     * @param _lineStyle
     *            the style for lines making this Series
     */
    public void setLineStyle(String _lineStyle) {
        lineStyle = Defs.parseStyle(_lineStyle);
    }

    /**
     * Sets the sql attributes.
     * 
     * @param _sqlAttributes
     *            the _sql attributes
     */
    @SuppressWarnings("unchecked")
    public void setSqlAttributes(HashMap<String, Object> _sqlAttributes) {
        this.sqlAttributes = (HashMap<String, String>) _sqlAttributes.clone();
    }

    /**
     * Sets the var.
     * 
     * @param _var
     *            the new var
     */
    public void setVar(String _var) {
        var = _var;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object valueObject) {

        if (!(valueObject instanceof SqlSerie))
            return false;
        SqlSerie value = (SqlSerie) valueObject;
        if (value == null)
            return false;

        if (value.getContent() != null
                && !value.getContent().equals(getContent())) {
            return false;
        } else if (value.getDataSource() != null
                && !value.getDataSource().equals(getDataSource())) {
            return false;
        } else if (value.getDefaultLabel() != null
                && !value.getDefaultLabel().equals(getDefaultLabel())) {
            return false;
        } else if (value.getDefaultLabelBackgroundStyle() != null
                && !value.getDefaultLabelBackgroundStyle().equals(
                        getDefaultLabelBackgroundStyle())) {
            return false;
        } else if (value.getDefaultLabelStyle() != null
                && !value.getDefaultLabelStyle().equals(getDefaultLabelStyle())) {
            return false;
        } else if (value.getDefaultShape() != null
                && !value.getDefaultShape().equals(getDefaultShape())) {
            return false;
        } else if (value.getDefaultShapeStyle() != null
                && !value.getDefaultShapeStyle().equals(getDefaultShapeStyle())) {
            return false;
        } else if (value.getId() != null && !value.getId().equals(getId())) {
            return false;
        } else if (value.getLegend() != null
                && !value.getLegend().equals(getLegend())) {
            return false;
        } else if (value.getLineStyle() != null
                && !value.getLineStyle().equals(getLineStyle())) {
            return false;
        }

        ArrayList<Object> valueList = value.getParameters();
        ArrayList<Object> currentList = getParameters();

        if (valueList == null && currentList == null)
            return true;

        if (valueList == null && currentList != null)
            return false;

        if (currentList == null && valueList != null)
            return false;

        if (valueList.size() != currentList.size()) {
            return false;
        }

        for (int i = 0; i < valueList.size(); i++) {
            if (!valueList.equals(currentList)) {
                return false;
            }
        }

        if (logger.isDebugEnabled())
            logger.debug("returning equals");

        return true;
    }
}
