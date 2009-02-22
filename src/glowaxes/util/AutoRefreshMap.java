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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * AutoRefreshMap is a wrapped HashMap. It is made thread safe by atomic locking
 * objects.<br>
 * It provides the common methods implemented in HashMap and contains logic to
 * trim, or throw away key value pairs that are have not been recently accessed.<br>
 * The method trim() is called if the Map grows beyond a certain size, set with
 * the method maxSize() method.<br>
 * Each key value pair has a last access timestamp internally represented in a
 * "light" value object.
 * 
 * trim() decides which key value pairs to throw away based on the logic that
 * pairs not accessed are less relevant than pairs that are recently accessed.
 * (cf. LRUCache)<br>
 * 
 * The HashMap is trimmed down to the minSize() parameter.<br>
 * <br>
 * Tested: <br>
 * 10000000 gets in ~1150 ms ARM with smart configuration/monitoring<br>
 * 10000000 gets in ~400 ms ARM no logging compared to<br>
 * 10000000 gets in ~750 ms regular hashmap.<br>
 * <br>
 * 100000 puts in ~1700 ms ARM with smart configuration/monitoring<br>
 * 100000 puts in ~1450 ms ARM no logging compared to<br>
 * 100000 puts in ~1200 ms regular hashmap<br>
 * <br>
 * 7500 removed (timmed) in 30ms<br>
 * <br>
 * Gets are a bit expensive, but it was cheap to begin. Performance can be
 * boosted w/o monitoring logging. dont know why it out performs the underlying
 * hashmap..?..
 * 
 * Puts get a tad more expensive, and locked if trimming.<br>
 * 
 * Test in application environment show no noticable effects compared to
 * hashmap, when not removing<br>
 * 

 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class AutoRefreshMap implements Runnable {

    /**
     * Internal class to represent a value object with an internal timestamp.
     * Implements comparable interface to sort values by access timestamp.
     * 
     * @param value The internal value
     */
    static class Value implements Comparable<Object> {
        
        /** The accesstime. */
        private long accesstime;

        /** The creationtime. */
        private long creationtime;

        /** The expiretime. */
        private long expiretime = -1;

        /** The value. */
        private Object value;

        /**
         * Instantiates a new value.
         * 
         * @param value the value
         */
        Value(Object value) {
            this.value = value;
            creationtime = accesstime = System.currentTimeMillis();
        }

        /**
         * Instantiates a new value.
         * 
         * @param value the value
         * @param expiretime the expiretime
         */
        Value(Object value, long expiretime) {
            this.value = value;
            creationtime = accesstime = System.currentTimeMillis();
            this.expiretime = expiretime;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Object o) throws ClassCastException {
            if (!(o instanceof Value)) {
                throw new ClassCastException();
            }
            long ot = ((Value) o).getLastAccessTime();
            return (int) (this.getLastAccessTime() - ot);
        }

        /**
         * Gets the creation time.
         * 
         * @return the creation time
         */
        long getCreationTime() {
            return this.creationtime;
        }

        /**
         * Gets the expired time.
         * 
         * @return the expired time
         */
        long getExpiredTime() {
            return this.expiretime;
        }

        /**
         * Gets the last access time.
         * 
         * @return the last access time
         */
        long getLastAccessTime() {
            return this.accesstime;
        }

        /**
         * Gets the value.
         * 
         * @return the value
         */
        Object getValue() {
            /* refresh the time it has been accessed */
            this.accesstime = System.currentTimeMillis();
            return value;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            if (value == null)
                return "null";
            return value.toString();
        }
    }

    /** The Constant arMaps. */
    private final static HashSet<AutoRefreshMap> arMaps =
            new HashSet<AutoRefreshMap>();

    /** The Constant hasExpiredThread. */
    private final static boolean hasExpiredThread = // true;
            TypeConverter.getBool(Configuration.getInstance().getValue(
                    "glowaxes", "AutoRefreshMap.hasExpiredThread"), false);

    /** The Constant isLogEnabled. */
    private final static boolean isLogEnabled = // true;
            TypeConverter.getBool(Configuration.getInstance().getValue(
                    "glowaxes", "AutoRefreshMap.isLogEnabled"), true);

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(AutoRefreshMap.class.getName());

    // flushes keys in all autorefresh maps created
    /**
     * Flush.
     * 
     * @param keyToFlush the key to flush
     */
    public static void flush(String keyToFlush) {

        Iterator<AutoRefreshMap> myIterator = arMaps.iterator();

        AutoRefreshMap arm = null;

        while (myIterator.hasNext()) {
            arm = myIterator.next();

            int removed = 0;
            if (arm != null) {
                Iterator<Object> myArmIterator = arm.keySet().iterator();

                while (myArmIterator.hasNext()) {
                    Object key = myArmIterator.next();

                    if (key.toString().indexOf(keyToFlush) != -1) {
                        // debug throws ConcurrentModificationException
                        // logger.debug("Removing: " + key +
                        // "=" + arm.get(key));

                        myArmIterator.remove();
                        removed++;
                    }
                }
            }
            logger.info("Size of " + arm.getName() + ": " + arm.size()
                    + ", removed " + removed);
        }
    }

    // private final Object lockGet = new Object();

    /**
     * Gets the auto refresh map.
     * 
     * @param _name the _name
     * 
     * @return the auto refresh map
     */
    public static AutoRefreshMap getAutoRefreshMap(String _name) {
        Iterator<AutoRefreshMap> myIterator = arMaps.iterator();

        AutoRefreshMap arm = null;

        while (myIterator.hasNext()) {
            arm = myIterator.next();
            if (arm.getName().equals(_name)) {
                break;
            }
        }
        return arm;
    }

    // testing
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        /*
         * org.tinyelements.util.TxtBean tb = new
         * org.tinyelements.util.TxtBean(); AutoRefreshMap arm = new
         * AutoRefreshMap("key"); AutoRefreshMap arm2 = new
         * AutoRefreshMap("key2");
         * 
         * HashMap arm = new HashMap(); long t0 = System.currentTimeMillis();
         * logger.fatal("start [" + (System.currentTimeMillis() - t0) + "]ms");
         * 
         * arm.put("k"+0,"v"+0);
         * 
         * for (int i=0; i<500; i++) { arm.put("k"+i,"v"+i); // 000
         * arm.get("k"+i); // arm.get("k"+0); } arm.flush("k"+4);
         * 
         * AutoRefreshMap armTest = AutoRefreshMap.getAutoRefreshMap("key4");
         * logger.fatal("**"+armTest);
         * 
         * logger.fatal("end put [" + (System.currentTimeMillis() - t0) +
         * "]ms"); //t0 = System.currentTimeMillis(); // arm.get("k"+10000); //
         * arm.get("k"+10000); // arm.get("k"+10000); // arm.get("k"+10000); //
         * arm.get("k"+10000); // arm.get("k"+10000); // arm.put("k"+0,"v");
         * logger.fatal("start get [" + (System.currentTimeMillis() - t0) +
         * "]ms"); t0 = System.currentTimeMillis(); for (int i=1; i<=5000000;
         * i++) { // arm.put("k"+i,"v"+i);
         * 
         * arm.get("k"+i); // arm.get("k"+0); } logger.fatal("end get [" +
         * (System.currentTimeMillis() - t0) + "]ms"); // //
         * System.out.println(arm.get("k"+0));
         */
    }

    /** The absolute access request counter. */
    private int absoluteAccessRequestCounter = 0;

    // private int accessTrivialRequestCounter = 0;

    /** The access miss counter. */
    private int accessMissCounter = 0;

    /** The access request counter. */
    private int accessRequestCounter = 0;

    // The underlying hashmap
    /** The cached map. */
    private HashMap<Object, Value> cachedMap;

    /** The last remove timestamp. */
    private long lastRemoveTimestamp = System.currentTimeMillis();

    /** The last status timestamp. */
    private long lastStatusTimestamp = System.currentTimeMillis();

    /** The lock. */
    private final Object lock = new Object();

    /** The max size. */
    private int maxSize;

    // the maximal capacity of the hashmap at which
    // dynamically trimmed minimal and
    /** The min size. */
    private int minSize;

    // an identifying entity
    /** The name. */
    private String name;

    /** The thread. */
    private Thread thread;

    /**
     * Construct a new, empty map with the 200 initial capacity, 250 hashmap
     * load factor, 100 minSize and 250 maxSize.
     * 
     * @param _name The name that is displayed when monitoring information is
     * displayed
     */
    public AutoRefreshMap(String _name) {
        this.name = _name;
        minSize = 200;
        maxSize = 250;
        cachedMap = new HashMap<Object, Value>(250);

        arMaps.add(this);

        // logger.error(hasExpiredThread);

        if (hasExpiredThread) {
            thread = new Thread(this);
            thread.start();
        }

    }

    /**
     * Construct a new, empty map with specified initial capacity and default
     * load factor, _minSize and _maxSize values.
     * 
     * @param _minSize The maximum number of key value pairs to before the
     * AutoRefreshMap is trimmed
     * @param _maxSize The number of requests to the AutoRefreshMap.get() method
     * before monitoring results are displayed to the logger
     * @param name the name
     * @param initialCapacity the initial capacity
     */
    public AutoRefreshMap(String name, int initialCapacity, int _minSize,
            int _maxSize) {
        this.name = name;
        if (_minSize > _maxSize) {
            int temp = _maxSize;
            _maxSize = _minSize;
            _minSize = temp;
            logger.error("Illegal construction. Swapping parameters for "
                    + name);
        }
        minSize = _minSize;
        maxSize = _maxSize;
        cachedMap = new HashMap<Object, Value>(initialCapacity);

        arMaps.add(this);

        if (hasExpiredThread) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.
     * 
     * @param key The key whose presence in this map is to be tested
     * 
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key.
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

    /**
     * Get the value associated with the specified key in this map.
     * 
     * @param key Key with which the value is associated
     * 
     * @return The value associated with the specified key, or <code>null</code>
     * if there was no mapping for key
     */
    public Object get(Object key) {

        synchronized (lock/* Get */) {

            if (isLogEnabled) {
                absoluteAccessRequestCounter++;
                accessRequestCounter++;
            }

            Object obj = cachedMap.get(key);

            if (obj != null) {
                return ((Value) obj).getValue();
            }

            if (isLogEnabled) {
                accessMissCounter++;
            }
            return null;
        }

    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Key set.
     * 
     * @return the set< object>
     */
    public Set<Object> keySet() {
        return cachedMap.keySet();
    }

    /**
     * Prints the log.
     */
    public void printLog() {

        if (!isLogEnabled)
            return;

        float quota =
                (float) (accessRequestCounter - accessMissCounter) * 100
                        / accessRequestCounter;

        /*
         * float quotaTrivial = (float)(accessRequestCounter-
         * accessTrivialRequestCounter-accessMissCounter)*100 /
         * (float)(accessRequestCounter-accessTrivialRequestCounter);
         */

        logger.info(name + " accessed " + accessRequestCounter
                + " time(s). Size: " + cachedMap.size());

        long secs = (System.currentTimeMillis() - lastStatusTimestamp) / 1000l;

        logger.info(name + ": Status " + ((float) Math.round(quota * 10) / 10)
                + "% cached hits" + ", size=" + size() + ", mis/req="
                + accessMissCounter + "/" + accessRequestCounter + ", [" + secs
                + "s] last stat.");

        lastStatusTimestamp = System.currentTimeMillis();

    }// log

    /**
     * Associate the specified value with the specified key in this map. If the
     * map previously contained a mapping for this key, the old value is
     * replaced.
     * 
     * @param key Key with which the specified value is to be associated
     * @param value Value to be associated with the specified key
     * 
     * @return The previous value associated with the specified key, or
     * <code>null</code> if there was no mapping for key
     */
    public Object put(Object key, Object value) {

        int size = cachedMap.size();
        synchronized (lock) {
            /* trim if map is too big */
            if (size > maxSize)
                trim();
            return cachedMap.put(key, new Value(value));

        }

    }

    /**
     * Associate the specified value with the specified key in this map. If the
     * map previously contained a mapping for this key, the old value is
     * replaced.
     * 
     * @param key Key with which the specified value is to be associated
     * @param value Value to be associated with the specified key
     * @param expiretime the unix timestamp after which the specified key will expire
     * 
     * @return The previous value associated with the specified key, or
     * <code>null</code> if there was no mapping for key
     */
    public Object put(Object key, Object value, long expiretime) {

        int size = cachedMap.size();
        synchronized (lock) {
            /* trim if map is too big */
            if (size > maxSize)
                trim();
            return cachedMap.put(key, new Value(value, expiretime));

        }

    }

    /**
     * Remove the specified key and associate value from this map. If the map
     * previously contained a mapping for this key, the old value is returned.
     * 
     * @param key Key with which the specified value is to be associated
     * 
     * @return The previous value associated with the specified key, or
     * <code>null</code> if there was no mapping for key or value is
     * null.
     */
    public Object remove(Object key) {
        synchronized (lock) {

            return cachedMap.remove(key);

        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {

            long t0 = System.currentTimeMillis();
            if (isLogEnabled)
                logger.debug("Trimming expired key value pairs.");
            trim(t0);

            Thread.sleep(10 * 1000);
            run();

        } catch (NullPointerException noe) {
            // ignore
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Sets the maximum size of this map. When the map exceeds its maxSize the
     * map is trimmed down to the minSize, freeing memory.
     * 
     * @param _maxSize _maxSize the maximum size of this map.
     */
    public void setMaxSize(int _maxSize) {
        synchronized (lock) {
            this.maxSize = _maxSize;

            // dont allow minSize to be in invalid state
            if (maxSize < minSize)
                minSize = maxSize - 1;
        }
    }

    /**
     * Sets the minumal size of this map. When the map exceeds its maxSize the
     * map is trimmed down to this size, freeing memory.
     * 
     * @param _minSize _minSize the minumal size of this map.
     */
    public void setMinSize(int _minSize) {
        synchronized (lock) {
            this.minSize = _minSize;

            // dont allow minSize to be in invalid state
            if (minSize > maxSize)
                maxSize = minSize + 1;

        }
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
    public String toString() {
        synchronized (lock) {
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
        synchronized (lock) {
            StringBuffer buf = new StringBuffer();
            buf.append("{");

            Iterator<Object> i = cachedMap.keySet().iterator();
            boolean hasNext = i.hasNext();
            while (hasNext) {

                Object key = i.next();
                Value value = cachedMap.get(key);
                buf.append(key + "=" + value.toString() + "["
                        + value.getLastAccessTime() + "]");

                hasNext = i.hasNext();
                if (hasNext)
                    buf.append(", ");
            }

            buf.append("}");
            return buf.toString();
        }
    }

    /*
     * Private trim() the cachedMap down to min size removing keys that havent
     * been referrenced get()'ed for the longest time
     */
    /**
     * Trim.
     */
    private void trim() {
        if (!(cachedMap.size() > maxSize))
            return;

        synchronized (lock) {

            try {
                long now = System.currentTimeMillis();

                /* sort values from oldest to newest */
                Object[] values = cachedMap.values().toArray();

                Arrays.sort(values);

                int _minChopped = values.length - minSize;

                long targetToChop =
                        ((Value) values[_minChopped]).getLastAccessTime();

                Iterator<Object> index = cachedMap.keySet().iterator();

                int removedKeys = 0;

                // loop through hashMap and remove oldest values
                while (index.hasNext() && cachedMap.size() > minSize) {

                    Object currentKey = index.next();

                    Value testValue = cachedMap.get(currentKey);

                    if (testValue.getLastAccessTime() <= targetToChop) {

                        index.remove();
                        removedKeys++;

                    }

                }

                if (isLogEnabled)
                    if (removedKeys != 0) {

                        long sec =
                                (System.currentTimeMillis() - lastRemoveTimestamp) / 1000l;
                        long chop =
                                (System.currentTimeMillis() - targetToChop) / 60000l;

                        logger.info("[" + (System.currentTimeMillis() - now)
                                + "ms] " + name + " removed " + removedKeys
                                + " keys after " + absoluteAccessRequestCounter
                                + " requests in [" + sec
                                + "s]. Chopped key is: [" + chop
                                + "mins] old, new size: " + cachedMap.size());

                        absoluteAccessRequestCounter = 0;
                        lastRemoveTimestamp = System.currentTimeMillis();
                    }

            } catch (Exception e) {

                logger.fatal("Design flaw: " + e + ", " + name);

            }
        }

    }

    /*
     * Private trim(expiretime) the cachedMap down removing keys that have a
     * expiretime before the given expiretime
     */
    /**
     * Trim.
     * 
     * @param expiretime the expiretime
     */
    private void trim(long expiretime) {

        synchronized (lock) {

            try {

                long now = System.currentTimeMillis();

                Iterator<Object> index = cachedMap.keySet().iterator();

                int removedKeys = 0;

                // loop through hashMap and remove oldest values
                while (index.hasNext()) {

                    Object currentKey = index.next();

                    Value testValue = cachedMap.get(currentKey);

                    if (testValue.getExpiredTime() != -1
                            && testValue.getExpiredTime() <= expiretime) {

                        index.remove();
                        removedKeys++;

                    }

                }

                if (isLogEnabled)
                    if (removedKeys != 0) {

                        logger.info("[" + (System.currentTimeMillis() - now)
                                + "ms] " + name + " removed " + removedKeys
                                + " expired keys. New size: "
                                + cachedMap.size());

                    }

            } catch (Exception e) {

                logger.fatal("Design flaw: " + e + ", " + name);

            }
        }

    }

}
