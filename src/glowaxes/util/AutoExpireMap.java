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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * AutoExpireMap is a wrapped HashMap. It is made thread safe by atomic locking
 * objects.<br>
 * It provides the common methods implemented in HashMap and contains logic to
 * trim, or throw away key value pairs that have been expired.<br>
 * The method remove() is called if the value is expired.<br>
 * Each key value pair has a expire timestamp internally represented in a
 * "light" value object.
 * 
 * The put() and get() methods contain logic to decides which key value pairs to
 * throw away based on the condition that pairs that are expired become null.<br>
 * 
 * The put() method contains logic to check if values are expired, and if so
 * remove them from the underlying hashmap after 10000 puts. The HashMap is
 * trimmed down to only non expired values.<br>
 * <br>
 * Tested: <br>
 * 10000000 gets in ~4544 ms AEM with smart configuration/monitoring<br>
 * 10000000 gets in ~4244 ms AEM no logging compared to<br>
 * 10000000 gets in ~2756 ms regular hashmap.<br>
 * <br>
 * 100000 puts in ~675 ms AEM with smart configuration/monitoring<br>
 * 100000 puts in ~457 ms AEM no logging compared to<br>
 * 100000 puts in ~438 ms regular hashmap<br>
 * <br>
 * 7500 removed (timmed) in 30ms<br>
 * <br>
 * Gets are a bit expensive, but it was cheap to begin. Performance can be
 * boosted w/o monitoring logging.
 * 
 * Puts get a tad more expensive, and locked if trimming.<br>
 * 
 * Test in application environment show no noticable effects compared to
 * hashmap, when not removing<br>
 * 
 * 

 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class AutoExpireMap {

    // The underlying hashmap
    private final HashMap<Object, Value> cachedMap;

    // an identifying entity
    private final String name;

    private final Object lock = new Object();

    private final long defaultMsToExpire = 1000;

    private int absoluteAccessRequestCounter = 0;

    private int putCounter = 0;

    private final int putTrigger = 10000;

    private int accessRequestCounter = 0;

    private int accessMissCounter = 0;

    private long lastStatusTimestamp = System.currentTimeMillis();

    private static int requestsMonitor =
            TypeConverter.getInt(Configuration.getInstance().getValue(
                    "tweaking", "AutoRefreshMap.get.requests"), 5000);

    private final static boolean isLogEnabled =
            TypeConverter.getBool(Configuration.getInstance().getValue(
                    "tweaking", "AutoExpireMap.isLogEnabled"), true);

    private final static HashSet<AutoExpireMap> arMaps =
            new HashSet<AutoExpireMap>();

    // Define a static logger variable so that it references the Logger instance
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(AutoExpireMap.class.getName());

    /**
     * Internal class to represent a value object with an internal timestamp.
     * Implements comparable interface to sort values by access timestamp.
     * 
     * @param value
     *            The internal value
     */
    static class Value {
        private final long expiretime;

        private final Object value;

        Value(Object value, long msToExpire) {
            this.value = value;
            if (msToExpire > 0)
                expiretime = System.currentTimeMillis() + msToExpire;
            else
                expiretime = -1;
        }

        Object getValue() {
            return value;
        }

        boolean isExpired() {
            if (expiretime > System.currentTimeMillis() || expiretime == -1)
                return false;
            else
                return true;
        }

        @Override
        public String toString() {
            if (getValue() == null)
                return "null";
            return expiretime + ", " + getValue().toString();
        }

    }

    /**
     * Private basic knowledge required to instantiate object.
     */
    @SuppressWarnings("unused")
    private AutoExpireMap() {
        this.name = "Testing";
        cachedMap = new HashMap<Object, Value>(200);
        arMaps.add(this);
    }

    /**
     * Construct a new, empty map with the 20000 initial capacity, default
     * hashmap load factor, 10000 minSize and 17500 maxSize
     * 
     * @param _name
     *            The name that is displayed when monitoring information is
     *            displayed
     */
    public AutoExpireMap(String _name) {
        this.name = _name;
        cachedMap = new HashMap<Object, Value>(250);
        arMaps.add(this);

    }

    public static AutoExpireMap getAutoExpireMap(String _name) {
        Iterator<AutoExpireMap> myIterator = arMaps.iterator();

        AutoExpireMap arm = null;

        while (myIterator.hasNext()) {
            arm = myIterator.next();
            if (arm.getName().equals(_name)) {
                break;
            }
        }
        return arm;
    }

    /**
     * Construct a new, empty map with specified initial capacity and default
     * load factor, _minSize and _maxSize values
     * 
     * @param _name
     *            The name that is displayed when monitoring information is
     *            displayed
     * @param _minSize
     *            The maximum number of key value pairs to before the
     *            AutoRefreshMap is trimmed
     * @param _maxSize
     *            The number of requests to the AutoRefreshMap.get() method
     *            before monitoring results are displayed to the logger
     */
    public AutoExpireMap(String name, int initialCapacity) {
        this.name = name;
        cachedMap = new HashMap<Object, Value>(initialCapacity);
        arMaps.add(this);

    }

    /**
     * Get the value associated with the specified key in this map.
     * 
     * @param key
     *            Key with which the value is associated
     * 
     * @return The value associated with the specified key, or <code>null</code>
     *         if there was no mapping for key
     * 
     */
    public Object get(Object key) {
        int size = cachedMap.size();
        synchronized (lock/* Get */) {
            if (isLogEnabled) {
                absoluteAccessRequestCounter++;
                accessRequestCounter++;

                if (accessRequestCounter >= requestsMonitor) {

                    float quota =
                            (float) (accessRequestCounter - accessMissCounter)
                                    * 100 / accessRequestCounter;

                    logger.info(name + " accessed " + requestsMonitor
                            + " times since last message. Size: "
                            + cachedMap.size());

                    requestsMonitor =
                            TypeConverter.getInt(Configuration.getInstance()
                                    .getValue("tweaking",
                                            "AutoRefreshMap.get.requests"),
                                    50000);

                    long secs =
                            (System.currentTimeMillis() - lastStatusTimestamp) / 1000l;

                    logger.warn(name + ": Status "
                            + ((float) Math.round(quota * 10) / 10)
                            + "% cached hits" + ", size=" + size + ", mis/req="
                            + accessMissCounter + "/" + accessRequestCounter
                            + ", [" + secs + "s] last stat.");// + ", " +

                    accessRequestCounter = 0;
                    accessMissCounter = 0;
                    lastStatusTimestamp = System.currentTimeMillis();

                }
            }// log

            Value val = cachedMap.get(key);

            if (val != null)
                if (!val.isExpired()) {
                    return val.getValue();
                } else {
                    cachedMap.remove(key);
                }

            if (isLogEnabled) {
                accessMissCounter++;
            }
            return null;
        }

    }

    /**
     * Associate the specified value with the specified key in this map. If the
     * map previously contained a mapping for this key, the old value is
     * replaced.
     * 
     * @param key
     *            Key with which the specified value is to be associated
     * @param value
     *            Value to be associated with the specified key
     * 
     * @return The previous value associated with the specified key, or
     *         <code>null</code> if there was no mapping for key
     * 
     */
    public Object put(Object key, Object value) {

        synchronized (lock) {
            putCounter++;
            Object oldValue =
                    cachedMap.put(key, new Value(value, defaultMsToExpire));
            if (putCounter > putTrigger) {
                putCounter = 0;
                Iterator<Object> iterator = cachedMap.keySet().iterator();
                while (iterator.hasNext()) {
                    Object keyValue = iterator.next();
                    Value val = cachedMap.get(keyValue);
                    if (val != null)
                        if (!val.isExpired()) {
                            return val.getValue();
                        } else {
                            cachedMap.remove(key);
                        }
                }
            }
            return oldValue;
        }// synchronized

    }

    /**
     * Associate the specified value with the specified key in this map. If the
     * map previously contained a mapping for this key, the old value is
     * replaced.
     * 
     * @param key
     *            Key with which the specified value is to be associated
     * @param value
     *            Value to be associated with the specified key
     * 
     * @return The previous value associated with the specified key, or
     *         <code>null</code> if there was no mapping for key
     * 
     */
    public Object put(Object key, Object value, long msToExpire) {

        synchronized (lock) {
            putCounter++;
            Object oldValue = cachedMap.put(key, new Value(value, msToExpire));
            if (putCounter > putTrigger) {
                putCounter = 0;
                Iterator<Object> iterator = cachedMap.keySet().iterator();
                while (iterator.hasNext()) {
                    Object keyValue = iterator.next();
                    Value val = cachedMap.get(keyValue);
                    if (val != null)
                        if (!val.isExpired()) {
                            return val.getValue();
                        } else {
                            cachedMap.remove(key);
                        }
                }
            }
            return oldValue;
        }// synchronized

    }

    /**
     * Remove the specified key and associate value from this map. If the map
     * previously contained a mapping for this key, the old value is returned.
     * 
     * @param key
     *            Key with which the specified value is to be associated
     * 
     * @return The previous value associated with the specified key, or
     *         <code>null</code> if there was no mapping for key or value is
     *         null.
     * 
     */
    public Object remove(Object key) {
        synchronized (lock) {

            return cachedMap.remove(key);

        }
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.
     * 
     * @param key
     *            The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key.
     */
    public boolean containsKey(Object key) {
        synchronized (lock) {
            if (!cachedMap.containsKey(key)) {
                if (isLogEnabled)
                    accessMissCounter++;
                return false;
            }
            return true;
        }

    }

    public Set<Object> keySet() {
        return cachedMap.keySet();
    }

    /**
     * Returns the number of key-value mappings in this map.
     * 
     * @return the number of key-value mappings in this map.
     */
    public int size() {
        synchronized (lock) {
            return cachedMap.size();
        }

    }

    /**
     * Returns a string representation of this map. The string representation
     * consists of a list of key-value mappings in random order. Each key-value
     * mapping is rendered as the key followed by an equals sign (<tt>"="</tt>)
     * followed by the associated value. Keys and values are converted to
     * strings as by <tt>String.valueOf(Object)</tt>.
     * <p>
     * 
     * @return a String representation of this map.
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{");

        Iterator<Object> i = cachedMap.keySet().iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {

            Object key = i.next();
            Value value = cachedMap.get(key);
            buf.append(key + "=" + value.toString());

            hasNext = i.hasNext();
            if (hasNext)
                buf.append(", ");
        }

        buf.append("}");
        return buf.toString();
    }

    /**
     * Returns a formatted string representation of this map. The string
     * representation consists of a list of key-value mappings in random order.
     * Each key-value mapping is rendered as the key followed by an equals sign (<tt>"="</tt>)
     * followed by the associated value, followed by an in square brackets (<tt>"["</tt>)
     * enclosed System timestamp. Keys and values are converted to strings as by
     * <tt>String.valueOf(Object)</tt>.
     * <p>
     * 
     * @return a String representation of this map.
     */
    public String toStringF() {
        StringBuffer buf = new StringBuffer();
        buf.append("{");

        Iterator<Object> i = cachedMap.keySet().iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {

            Object key = i.next();
            Value value = cachedMap.get(key);
            buf.append(key + "=" + value.toString() + "[" + value.isExpired()
                    + "]");

            hasNext = i.hasNext();
            if (hasNext)
                buf.append(", ");
        }

        buf.append("}");
        return buf.toString();
    }

    public String getName() {
        return name;
    }

    // testing
    public static void main(String[] args) {

        long iterations = 100000;
        HashMap<Object, Object> arm1 = new HashMap<Object, Object>();
        long t0 = System.currentTimeMillis();
        System.out.println("start [" + (System.currentTimeMillis() - t0)
                + "]ms");

        for (int i = 0; i < iterations; i++) {
            arm1.put("k" + i, "v" + i);
        }
        System.out.println("end put [" + (System.currentTimeMillis() - t0)
                + "]ms");

        AutoExpireMap armTest = new AutoExpireMap("key", 700);
        t0 = System.currentTimeMillis();
        System.out.println("start [" + (System.currentTimeMillis() - t0)
                + "]ms");

        for (int i = 0; i < iterations; i++) {
            armTest.put("k" + i, "v" + i);
        }
        System.out.println("k9: " + armTest.get("k" + 9));
        System.out.println("end put [" + (System.currentTimeMillis() - t0)
                + "]ms");
        System.out.println("size: " + armTest.size());

        t0 = System.currentTimeMillis();
        System.out.println("start get [" + (System.currentTimeMillis() - t0)
                + "]ms");

        for (int i1 = 1; i1 <= iterations * 100; i1++) {
            armTest.get("k" + i1);
        }

        System.out.println("size: " + armTest.size());
        System.out.println("end get [" + (System.currentTimeMillis() - t0)
                + "]ms");
        System.out.println(arm1.get("k" + 1));
        System.out.println("k9: " + armTest.get("k" + 9));

        t0 = System.currentTimeMillis();
        System.out.println("start get [" + (System.currentTimeMillis() - t0)
                + "]ms");

        for (int i1 = 1; i1 <= iterations * 100; i1++) {
            arm1.get("k" + i1);
        }

        System.out.println("size: " + armTest.size());
        System.out.println("end get [" + (System.currentTimeMillis() - t0)
                + "]ms");

    }

}
