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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * A register singleton that registers charts.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 * @version 1.0
 */
public class ChartRegistry implements java.io.Serializable {

    /** The algorithm. */
    private static MessageDigest algorithm;

    /** The id. */
    private static long id = Math.round(Math.random() * 1000000);

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(ChartRegistry.class.getName());

    /* HashMap, used to keep Registry of charts */
    /** The Constant registeredCharts. */
    private static final AutoRefreshMap registeredCharts =
            new AutoRefreshMap("registeredCharts");

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -955823968012239964L;

    /** The Constant SINGLETON. */
    public static final ChartRegistry SINGLETON = new ChartRegistry();

    /**
     * Gets the m d5.
     * 
     * @param key
     *            the key
     * 
     * @return the m d5
     */
    private static String getMD5(String key) {

        algorithm.reset();
        algorithm.update(key.getBytes());
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }

        return hexString.toString();

    }

    /**
     * Singleton constructor for ChartRegistry().
     */
    private ChartRegistry() {

        try {

            algorithm = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            logger.fatal(e);
        }
    }

    /*
     */
    /**
     * Adds the chart.
     * 
     * @param id
     *            the id
     * @param array
     *            the array
     */
    public void addChart(String id, byte[] array) {
        if (array == null)
            throw new NullPointerException("OutputStream is not defined (null)");

        // expires after one minute
        registeredCharts.put(id, array);

    }

    /*
     */
    /**
     * Adds the chart.
     * 
     * @param id
     *            the id
     * @param array
     *            the array
     * @param expiretime
     *            the expiretime
     */
    public void addChart(String id, byte[] array, long expiretime) {
        if (array == null)
            throw new NullPointerException("OutputStream is not defined (null)");

        registeredCharts.put(id, array, expiretime);

    }

    /*
     */
    /**
     * Gets the chart.
     * 
     * @param id
     *            the id
     * 
     * @return the chart
     */
    public byte[] getChart(String id) {

        return (byte[]) registeredCharts.get(id);

    }

    /**
     * Gets the hex.
     * 
     * @return the hex
     */
    public String getHex() {

        return getMD5("" + id++);
    }

    /**
     * Gets the hex.
     * 
     * @return the hex
     */
    public String getHex(String hexme) {

        return getMD5("" + hexme);
    }

    /**
     * The readResolve method serves to prevent the release of multiple
     * instances upon de-serialization. Logic unknown... research needed.
     * 
     * @return the object
     * 
     * @throws ObjectStreamException
     *             the object stream exception
     */
    private Object readResolve() throws java.io.ObjectStreamException {
        return null;
    }

    /*
     */
    /**
     * Save chart.
     * 
     * @param id
     *            the id
     * @param path
     *            the path
     * @param fileName
     *            the file name
     */
    public void saveChart(String id, String path, String fileName) {

        // Create the transcoder output.
        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(path + fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] image = getChart(id);

        if (ostream == null || image == null)
            throw new RuntimeException("id: " + id + ", image value is null.");

        try {
            ostream.write(image);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // // Flush and close the stream.
        try {
            ostream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            ostream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}// class
