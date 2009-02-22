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
package glowaxes.data;

import glowaxes.sql.ResultImpl;
import glowaxes.util.TypeConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.log4j.Logger;

/**
 * <p>
 * This object manages the getting and setting of cached data.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class DataIsland {

    /** The Constant SINGLETON. */
    public static final DataIsland SINGLETON = new DataIsland();

    // Define a static logger variable
    /** The Constant logger. */
    private static final Logger logger =
            Logger.getLogger(DataIsland.class.getName());

    /** The cache list. */
    private static HashMap<String, Integer> cacheList =
            new HashMap<String, Integer>();

    /**
     * Instantiates a new data island (private singleton pattern).
     */
    private DataIsland() {
    }

    /**
     * Internal class to represent a cache object with an internal time stamp.
     * 
     * @param cache
     *            The internal cache
     */
    static class Cache {

        /** The expiretime. */
        private final long expiretime;

        /** The ms to expire. */
        private final long msToExpire;

        /** The cache. */
        private final HashMap<String, Object> cache;

        /**
         * Instantiates a new cache.
         * 
         * @param cache
         *            the cache
         * @param msToExpire
         *            the ms to expire
         */
        Cache(HashMap<String, Object> cache, long msToExpire) {
            this.cache = cache;
            this.msToExpire = msToExpire;
            if (msToExpire >= 0)
                expiretime = System.currentTimeMillis() + msToExpire;
            else
                expiretime = -1;
        }

        /**
         * Gets the internal cache.
         * 
         * @return the cache
         */
        HashMap<String, Object> getCache() {
            return cache;
        }

        /**
         * Checks if the cache is expired.
         * 
         * @return true, if its expired
         */
        boolean isExpired() {
            if (expiretime > System.currentTimeMillis() || expiretime == -1)
                return false;
            else
                return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            if (getCache() == null)
                return "null";
            return expiretime + ", " + getCache().toString();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object valueObject) {

            long t0 = System.currentTimeMillis();
            if (!(valueObject instanceof Cache))
                return false;
            Cache value = (Cache) valueObject;
            if (value.msToExpire != msToExpire) {
                return false;
            }

            HashMap<String, Object> valueMap = value.getCache();
            HashMap<String, Object> currentMap = getCache();

            if (valueMap == null || currentMap == null) {
                return false;
            }

            Iterator<String> keys = valueMap.keySet().iterator();

            while (keys.hasNext()) {
                String key = keys.next();

                if (!currentMap.containsKey(key)) {
                    return false;
                }

                if (key.equals("sqlSeries")) {

                    ArrayList<SqlSerie> valueSqlSeries =
                            (ArrayList<SqlSerie>) valueMap.get("sqlSeries");
                    ArrayList<SqlSerie> currentSqlSeries =
                            (ArrayList<SqlSerie>) currentMap.get("sqlSeries");

                    if (valueSqlSeries.size() != currentSqlSeries.size()) {
                        return false;
                    }
                    for (int i = 0; i < valueSqlSeries.size(); i++) {
                        if (!valueSqlSeries.equals(currentSqlSeries)) {
                            return false;
                        }
                    }

                }

                if (!valueMap.get(key).equals(currentMap.get(key))) {
                    return false;
                }
            }

            if (logger.isDebugEnabled()) {
                long ms = System.currentTimeMillis() - t0;
                logger.debug("Equals method took: " + ms + "[ms]");
            }

            return true;

        }
    }

    /**
     * Gets the data.
     * 
     * @param id
     *            the id identifying the data.
     * @param pageContext
     *            the page context (jsp).
     * 
     * @return the data
     */
    public HashMap<String, Object> getData(String id, PageContext pageContext) {
        int scope = pageContext.getAttributesScope(id);
        if (logger.isDebugEnabled()) {
            logger.debug("get id, scope: (" + id + ", " + scope + ")");
            logger.debug("Session id: " + pageContext.getSession().getId());
        }
        if (scope == 0) {
            throw new RuntimeException(
                    "Cannot find data island in any scope with id: " + id);
        }
        Cache cache = (Cache) pageContext.getAttribute(id, scope);
        if (cache != null) {

            return cache.getCache();

        }
        if (logger.isDebugEnabled())
            logger.debug("cannot find data for id: " + id);
        return null;
    }

    /**
     * Sets the data.
     * 
     * @param id
     *            the id
     * @param map
     *            the map
     * @param pageContext
     *            the page context
     * @param scope
     *            the scope
     * 
     * @return true, if successful
     */
    @SuppressWarnings("unchecked")
    public boolean setData(String id, HashMap<String, Object> map,
            PageContext pageContext, int scope) {

        flushCache(pageContext);

        HashMap<String, Object> dataAttributes =
                (HashMap<String, Object>) map.clone();

        if (logger.isDebugEnabled()) {
            logger.debug("Adding data to scope: " + scope + " for id: " + id);
            logger.debug("Current session id: "
                    + pageContext.getSession().getId());
        }
        long expireTime =
                TypeConverter.getLong(dataAttributes.get("expireTimeMilli"), 0);
        if (id == null)
            throw new IllegalArgumentException("id cannot be null");
        if (dataAttributes == null)
            throw new IllegalArgumentException("dataAttributes cannot be null");
        if (pageContext == null)
            throw new IllegalArgumentException("pageContext cannot be null");

        // don't refresh data that has not changed or is not expired
        Cache cache = (Cache) pageContext.getAttribute(id, scope);
        Cache temp = new Cache(dataAttributes, expireTime);

        if (cache != null && !cache.isExpired() && cache.equals(temp)) {
            logger.info("Not refreshing cache with identical structure");
            return false;
        }

        int foundScope = pageContext.getAttributesScope(id);
        while (foundScope != 0) {
            pageContext.removeAttribute(id);
            foundScope = pageContext.getAttributesScope(id);
        }

        performHeavyLoad(pageContext, scope, dataAttributes);

        if (scope == PageContext.PAGE_SCOPE) {
            if (logger.isDebugEnabled())
                logger.debug("Adding data to page context");
            cacheList.put(id, scope);
            pageContext.setAttribute(id, temp, scope);
        } else if (scope == PageContext.REQUEST_SCOPE) {
            if (logger.isDebugEnabled())
                logger.debug("Adding data to request context");
            cacheList.put(id, scope);
            pageContext.setAttribute(id, temp, scope);
        } else if (scope == PageContext.SESSION_SCOPE) {
            if (logger.isDebugEnabled())
                logger.debug("Adding data to session context");
            cacheList.put(id, scope);
            pageContext.setAttribute(id, temp, scope);
        } else if (scope == PageContext.APPLICATION_SCOPE) {
            if (logger.isDebugEnabled())
                logger.debug("Adding data to application context");
            cacheList.put(id, scope);
            pageContext.setAttribute(id, temp, scope);
        } else
            throw new IllegalArgumentException("invalid scope");

        logger.info("Expire Time: " + expireTime + "[ms]");

        return true;
    }

    /**
     * Flush cache.
     * 
     * @param pageContext
     *            the page context
     */
    private void flushCache(PageContext pageContext) {

        if (cacheList == null)
            return;
        Iterator<String> keys = cacheList.keySet().iterator();

        while (keys.hasNext()) {

            String key = keys.next();
            int scope = cacheList.get(key);

            if (!(pageContext.getAttribute(key, scope) instanceof Cache)) {
                logger.info("removing stale key from cacheList");
                keys.remove();
                continue;
            }

            Cache cache = (Cache) pageContext.getAttribute(key, scope);

            if (cache.isExpired()) {
                logger.info("removing expired cache");
                keys.remove();
                pageContext.removeAttribute(key, scope);
            }
        }
    }

    /**
     * Perform heavy load of database connection/ loading data.
     * 
     * @param pageContext
     *            the page context
     * @param scope
     *            the scope
     * @param dataAttributes
     *            the data attributes
     */
    @SuppressWarnings("unchecked")
    private void performHeavyLoad(PageContext pageContext, int scope,
            HashMap<String, Object> dataAttributes) {

        logger.info("Refreshing cache with new data");

        // fill data
        ArrayList<SqlSerie> sqlSeries =
                (ArrayList<SqlSerie>) dataAttributes.get("sqlSeries");

        Group myDataGroup;

        if (sqlSeries != null) {

            ArrayList<Result> resultArray = new ArrayList<Result>();
            for (int i = 0; i < sqlSeries.size(); i++) {

                SqlSerie sqlS = sqlSeries.get(i);

                // perform the sql
                sqlS.processSql();

                Result result =
                        new ResultImpl(sqlSeries.get(i).getHashMapArray(),
                                sqlSeries.get(i).getId(), sqlSeries.get(i)
                                        .getLegend());

                resultArray.add(result);

                if (sqlSeries.get(i).getVar() != null)
                    pageContext.setAttribute(sqlSeries.get(i).getVar(), result,
                            scope);
            }

            pageContext.setAttribute((String) dataAttributes.get("arrayList"),
                    resultArray, scope);

            myDataGroup = Group.loadFromSql(sqlSeries);

        } else {

            if (dataAttributes.get("source") == null) {
                // logger.error(dataAttributes.get("data"));
                myDataGroup =
                        Group
                                .loadXMLString((String) dataAttributes
                                        .get("data"));
            } else {
                // logger.error("data body is null");
                myDataGroup =
                        Group.loadXMLURI((String) dataAttributes.get("source"));
            }
        }
        dataAttributes.put("dataGroup", myDataGroup);
    }
}
