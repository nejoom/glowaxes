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

import glowaxes.data.Value;
import glowaxes.glyphs.Data;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DataFormatter.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class DataFormatter {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger =
            Logger.getLogger(DataFormatter.class.getName());

    /** The url. */
    private static final String url =
            "http://java.sun.com/j2se/1.4.2/docs/api/java/text/DecimalFormat.html";

    /**
     * Gets the currency format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * 
     * @return the currency format
     */
    private static String getCurrencyFormat(Object toFormat,
            DataFormatter dataFormatter) {

        String result = null;
        if (dataFormatter == null)
            return TypeConverter.getString(toFormat, "NULL");

        @SuppressWarnings("unused")
        Locale locale = dataFormatter.getLocale();

        String labelFormat = dataFormatter.getCurrencyFormat();
        if (labelFormat == null)
            return TypeConverter.getString(toFormat, "NULL");

        if (logger.isDebugEnabled())
            logger.debug("Formatting for currency: " + toFormat + ", "
                    + labelFormat);

        toFormat = TypeConverter.getString(toFormat, "null");

        result = toFormat.toString();

        if (labelFormat.lastIndexOf("#") != -1) {

            int length = labelFormat.lastIndexOf("#") + 1;

            if (result.length() > length) {
                result =
                        result.substring(0, length).trim().concat(
                                labelFormat.substring(length, labelFormat
                                        .length()));
            }
        }

        return result;
    }

    /**
     * Gets the currency format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * @param customFormat the custom format
     * 
     * @return the currency format
     */
    @SuppressWarnings("unused")
    private static String getCurrencyFormat(Object toFormat,
            DataFormatter dataFormatter, String customFormat) {

        dataFormatter.formatAttributes.put("currencyFormat", customFormat);
        return getCurrencyFormat(toFormat, new DataFormatter(
                dataFormatter.formatAttributes));

    }

    /**
     * Gets the date format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * 
     * @return the date format
     */
    private static String getDateFormat(final Object toFormat,
            final DataFormatter dataFormatter) {

        String result = null;
        if (dataFormatter == null)
            return TypeConverter.getDate(toFormat).toString();

        Locale locale = dataFormatter.getLocale();

        String labelFormat = dataFormatter.getDateFormat();
        if (labelFormat == null)
            return TypeConverter.getDate(toFormat).toString();

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(dataFormatter.getTimeZone());

        if (labelFormat.toUpperCase().equals("SHORT")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.SHORT, locale);
        } else if (labelFormat.toUpperCase().equals("MEDIUM")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.MEDIUM, locale);
        } else if (labelFormat.toUpperCase().equals("LONG")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.LONG, locale);
        } else if (labelFormat.toUpperCase().equals("FULL")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.FULL, locale);
        } else if (labelFormat.toUpperCase().equals("SHORT_DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.SHORT, locale);
        } else if (labelFormat.toUpperCase().equals("MEDIUM_DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.MEDIUM, locale);
        } else if (labelFormat.toUpperCase().equals("LONG_DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.LONG, locale);
        } else if (labelFormat.toUpperCase().equals("FULL_DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.FULL, locale);
        } else if (labelFormat.toUpperCase().equals("SHORT.DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.SHORT, locale);
        } else if (labelFormat.toUpperCase().equals("MEDIUM.DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.MEDIUM, locale);
        } else if (labelFormat.toUpperCase().equals("LONG.DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.LONG, locale);
        } else if (labelFormat.toUpperCase().equals("FULL.DATE")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getDateInstance(
                            SimpleDateFormat.FULL, locale);
        } else if (labelFormat.toUpperCase().equals("SHORT_TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.SHORT, locale);
        } else if (labelFormat.toUpperCase().equals("MEDIUM_TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.MEDIUM, locale);
        } else if (labelFormat.toUpperCase().equals("LONG_TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.LONG, locale);
        } else if (labelFormat.toUpperCase().equals("FULL_TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.FULL, locale);
        } else if (labelFormat.toUpperCase().equals("SHORT.TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.SHORT, locale);
        } else if (labelFormat.toUpperCase().equals("MEDIUM.TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.MEDIUM, locale);
        } else if (labelFormat.toUpperCase().equals("LONG.TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.LONG, locale);
        } else if (labelFormat.toUpperCase().equals("FULL.TIME")) {
            sdf =
                    (SimpleDateFormat) DateFormat.getTimeInstance(
                            SimpleDateFormat.FULL, locale);
        } else {
            String[] format = labelFormat.split(" ");
            result = "";
            for (int i = 0; i < format.length; i++) {
                if (format[i].indexOf("E") >= 0
                        && format[i].indexOf("EE") == -1) {
                    sdf = new SimpleDateFormat("EEEE", locale);
                    sdf.setTimeZone(dataFormatter.getTimeZone());
                    String pretoken = "";
                    String posttoken = "";
                    if (format[i].length() > 1) {
                        pretoken =
                                format[i].substring(0, format[i].indexOf("E"));
                        posttoken =
                                format[i].substring(format[i].indexOf("E") + 1,
                                        format[i].length());
                        SimpleDateFormat sdfToken =
                                new SimpleDateFormat(posttoken, locale);
                        posttoken = sdfToken.format(toFormat);
                        sdfToken = new SimpleDateFormat(pretoken, locale);
                        pretoken = sdfToken.format(toFormat);
                    }
                    result +=
                            pretoken + sdf.format(toFormat).substring(0, 1)
                                    + posttoken + " ";
                } else if (format[i].indexOf("EE") >= 0
                        && format[i].indexOf("EEE") == -1) {
                    sdf = new SimpleDateFormat("EEEE", locale);
                    sdf.setTimeZone(dataFormatter.getTimeZone());

                    String pretoken = "";
                    String posttoken = "";
                    if (format[i].length() > 2) {
                        pretoken =
                                format[i].substring(0, format[i].indexOf("EE"));
                        posttoken =
                                format[i].substring(
                                        format[i].indexOf("EE") + 2, format[i]
                                                .length());
                        SimpleDateFormat sdfToken =
                                new SimpleDateFormat(posttoken, locale);
                        posttoken = sdfToken.format(toFormat);
                        sdfToken = new SimpleDateFormat(pretoken, locale);
                        pretoken = sdfToken.format(toFormat);
                    }
                    result +=
                            pretoken + sdf.format(toFormat).substring(0, 2)
                                    + posttoken + " ";
                } else if (format[i].indexOf("M1") >= 0) {
                    sdf = new SimpleDateFormat("MMMM", locale);
                    sdf.setTimeZone(dataFormatter.getTimeZone());

                    String pretoken = "";
                    String posttoken = "";
                    if (format[i].length() > 2) {
                        pretoken =
                                format[i].substring(0, format[i].indexOf("M1"));
                        posttoken =
                                format[i].substring(
                                        format[i].indexOf("M1") + 2, format[i]
                                                .length());
                        SimpleDateFormat sdfToken =
                                new SimpleDateFormat(posttoken, locale);
                        posttoken = sdfToken.format(toFormat);
                        sdfToken = new SimpleDateFormat(pretoken, locale);
                        pretoken = sdfToken.format(toFormat);
                    }
                    result +=
                            pretoken + sdf.format(toFormat).substring(0, 1)
                                    + posttoken + " ";
                } else if (format[i].indexOf("M2") >= 0) {
                    sdf = new SimpleDateFormat("MMMM", locale);
                    sdf.setTimeZone(dataFormatter.getTimeZone());

                    String pretoken = "";
                    String posttoken = "";
                    if (format[i].length() > 2) {
                        pretoken =
                                format[i].substring(0, format[i].indexOf("M2"));
                        posttoken =
                                format[i].substring(
                                        format[i].indexOf("M2") + 2, format[i]
                                                .length());
                        SimpleDateFormat sdfToken =
                                new SimpleDateFormat(posttoken, locale);
                        posttoken = sdfToken.format(toFormat);
                        sdfToken = new SimpleDateFormat(pretoken, locale);
                        pretoken = sdfToken.format(toFormat);
                    }
                    result +=
                            pretoken + sdf.format(toFormat).substring(0, 2)
                                    + posttoken + " ";
                } else {
                    sdf = new SimpleDateFormat(format[i], locale);
                    sdf.setTimeZone(dataFormatter.getTimeZone());
                    result += sdf.format(toFormat) + " ";
                }
            }
            result = result.trim();
        }

        if (result == null) {
            result = sdf.format(toFormat);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("TimeZone: " + sdf.getTimeZone());
            logger.debug("Format result: " + result);
        }

        return result;

    }

    /**
     * Gets the date format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * @param customFormat the custom format
     * 
     * @return the date format
     */
    private static String getDateFormat(Object toFormat,
            DataFormatter dataFormatter, String customFormat) {

        dataFormatter.formatAttributes.put("dateFormat", customFormat);

        return getDateFormat(toFormat, new DataFormatter(
                dataFormatter.formatAttributes));

    }

    /**
     * Gets the float format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * 
     * @return the float format
     */
    private static String getFloatFormat(Object toFormat,
            final DataFormatter dataFormatter) {

        String result = null;
        if (dataFormatter == null)
            return TypeConverter.getFloat(toFormat).toString();

        Locale locale = dataFormatter.getLocale();

        String labelFormat = dataFormatter.getFloatFormat();
        if (labelFormat == null)
            return TypeConverter.getFloat(toFormat).toString();

        if (labelFormat.indexOf("#k") != -1 || labelFormat.indexOf("0k") != -1
                || labelFormat.indexOf(")k") != -1) {
            if (logger.isDebugEnabled())
                logger.debug("Formatting for #k: " + toFormat + ", "
                        + labelFormat);

            Double doubleFormat = TypeConverter.getDouble(toFormat);

            if (doubleFormat.doubleValue() == 0)
                return "0";

            double formatMe = doubleFormat.doubleValue();

            double formatMeAbs = Math.abs(formatMe);

            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(locale);

            String prefix = "";
            try {
                prefix =
                // sign < 0 ? "-" : ""
                        labelFormat.substring(labelFormat.indexOf("#"),
                                labelFormat.indexOf("k"));
            } catch (Exception e) {

                throw new RuntimeException(
                        "Format syntax incorrect, try altering label format: "
                                + "#,##0.00;(#,##0.00), see " + url);
            }

            if (formatMeAbs < 1d / 1000d) {
                Double formatMeWithK = new Double(formatMe * 1000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + " \u00B5";

            } else if (formatMeAbs < 1d / 10d) {
                Double formatMeWithK = new Double(formatMe * 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + " m";

            } else if (formatMeAbs < 1d) {

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 10d) {

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 100d) {

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 1000d) {
                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 10000d) {
                Double formatMeWithK = new Double(formatMe / 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "k";

            } else if (formatMeAbs < 100000d) {
                Double formatMeWithK = new Double(formatMe / 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "k";

            } else if (formatMeAbs < 1000000d) {
                Double formatMeWithK = new Double(formatMe / 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "k";

            } else if (formatMeAbs < 1000000000d) {
                Double formatMeWithK = new Double(formatMe / 1000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "M";

            } else if (formatMeAbs < 1000000000000d) {
                Double formatMeWithK = new Double(formatMe / 1000000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "G";

            } else {
                Double formatMeWithK = new Double(formatMe / 1000000000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "T";

            }

            int length = labelFormat.lastIndexOf("k") + 1;

            result =
                    result.concat(labelFormat.substring(length, labelFormat
                            .length()));

        } else {

            if (logger.isDebugEnabled())
                logger.debug("Formatting for number: " + toFormat + ", "
                        + labelFormat);

            toFormat = TypeConverter.getDouble(toFormat);

            DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(locale);

            String format = labelFormat;
            nf.applyPattern(format);

            result = nf.format(toFormat);

        }

        return result;

    }

    /**
     * Gets the float format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * @param customFormat the custom format
     * 
     * @return the float format
     */
    private static String getFloatFormat(Object toFormat,
            DataFormatter dataFormatter, String customFormat) {

        dataFormatter.formatAttributes.put("floatFormat", customFormat);

        return getFloatFormat(toFormat, new DataFormatter(
                dataFormatter.formatAttributes));

    }

    /**
     * Gets the format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * 
     * @return the format
     */
    public static String getFormat(Object toFormat, DataFormatter dataFormatter) {
        if (toFormat == null)
            return null;
        if (toFormat.toString().indexOf('$') == 0) {
            return getCurrencyFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Date) {
            return getDateFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Integer) {
            return getIntFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Long) {
            return getIntFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Short) {
            return getIntFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Byte) {
            return getIntFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Double) {
            return getFloatFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Float) {
            return getFloatFormat(toFormat, dataFormatter);
        } else if (toFormat instanceof Object) {
            return getTextFormat(toFormat, dataFormatter);
        }
        return null;
    }

    /**
     * Gets the format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * @param customFormat the custom format
     * 
     * @return the format
     */
    public static String getFormat(Object toFormat,
            DataFormatter dataFormatter, String customFormat) {
        if (toFormat.toString().indexOf('$') == 0) {
            return getCurrencyFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Date) {
            return getDateFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Integer) {
            return getIntFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Long) {
            return getIntFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Short) {
            return getIntFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Byte) {
            return getIntFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Double) {
            return getFloatFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Float) {
            return getFloatFormat(toFormat, dataFormatter, customFormat);
        } else if (toFormat instanceof Object) {
            return getTextFormat(toFormat, dataFormatter, customFormat);
        }
        return null;
    }

    /**
     * Gets the int format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * 
     * @return the int format
     */
    private static String getIntFormat(Object toFormat,
            final DataFormatter dataFormatter) {

        String result = null;
        if (dataFormatter == null)
            return TypeConverter.getInteger(toFormat).toString();

        Locale locale = dataFormatter.getLocale();

        String labelFormat = dataFormatter.getIntFormat();
        if (labelFormat == null)
            return TypeConverter.getInteger(toFormat).toString();

        if (labelFormat.indexOf("#k") != -1 || labelFormat.indexOf("0k") != -1
                || labelFormat.indexOf(")k") != -1) {
            if (logger.isDebugEnabled())
                logger.debug("Formatting for #k: " + toFormat + ", "
                        + labelFormat);

            Double doubleFormat = TypeConverter.getDouble(toFormat);

            if (doubleFormat.doubleValue() == 0)
                return "0";

            double formatMe = doubleFormat.doubleValue();

            double formatMeAbs = Math.abs(formatMe);

            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(locale);

            String prefix = "";
            try {
                prefix =
                // sign < 0 ? "-" : ""
                        labelFormat.substring(labelFormat.indexOf("#"),
                                labelFormat.indexOf("k"));
            } catch (Exception e) {

                throw new RuntimeException(
                        "Format syntax incorrect, try altering label format: "
                                + "#,##0.00;(#,##0.00), see " + url);
            }

            if (formatMeAbs < 1d / 1000d) {
                Double formatMeWithK = new Double(formatMe * 1000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + " \u00B5";

            } else if (formatMeAbs < 1d / 10d) {
                Double formatMeWithK = new Double(formatMe * 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + " m";

            } else if (formatMeAbs < 1d) {

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 10d) {

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 100d) {

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 1000d) {
                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMe);

            } else if (formatMeAbs < 10000d) {
                Double formatMeWithK = new Double(formatMe / 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "k";

            } else if (formatMeAbs < 100000d) {
                Double formatMeWithK = new Double(formatMe / 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "k";

            } else if (formatMeAbs < 1000000d) {
                Double formatMeWithK = new Double(formatMe / 1000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "k";

            } else if (formatMeAbs < 1000000000d) {
                Double formatMeWithK = new Double(formatMe / 1000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "M";

            } else if (formatMeAbs < 1000000000000d) {
                Double formatMeWithK = new Double(formatMe / 1000000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "G";

            } else {
                Double formatMeWithK = new Double(formatMe / 1000000000000d);

                String format = prefix;

                df.applyPattern(format);

                result = df.format(formatMeWithK) + "T";

            }

            int length = labelFormat.lastIndexOf("k") + 1;

            result =
                    result.concat(labelFormat.substring(length, labelFormat
                            .length()));

        } else {

            if (logger.isDebugEnabled())
                logger.debug("Formatting for number: " + toFormat + ", "
                        + labelFormat);

            toFormat = TypeConverter.getDouble(toFormat);

            DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(locale);

            String format = labelFormat;
            nf.applyPattern(format);

            result = nf.format(toFormat);

        }

        return result;
    }

    /**
     * Gets the int format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * @param customFormat the custom format
     * 
     * @return the int format
     */
    private static String getIntFormat(Object toFormat,
            DataFormatter dataFormatter, String customFormat) {

        dataFormatter.formatAttributes.put("intFormat", customFormat);

        return getIntFormat(toFormat, new DataFormatter(
                dataFormatter.formatAttributes));

    }

    /**
     * Gets the text format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * 
     * @return the text format
     */
    private static String getTextFormat(Object toFormat,
            DataFormatter dataFormatter) {

        String result = null;
        if (dataFormatter == null)
            return TypeConverter.getString(toFormat, "NULL");

        String labelFormat = dataFormatter.getTextFormat();
        if (labelFormat == null)
            return TypeConverter.getString(toFormat, "NULL");

        if (logger.isDebugEnabled())
            logger.debug("Formatting for text: " + toFormat + ", "
                    + labelFormat);

        toFormat = TypeConverter.getString(toFormat, "null");

        result = toFormat.toString();

        if (labelFormat.lastIndexOf("#") != -1) {

            int length = labelFormat.lastIndexOf("#") + 1;

            if (result.length() > length) {
                result =
                        result.substring(0, length).trim().concat(
                                labelFormat.substring(length, labelFormat
                                        .length()));
            }
        }

        return result;
    }

    /**
     * Gets the text format.
     * 
     * @param toFormat the to format
     * @param dataFormatter the data formatter
     * @param customFormat the custom format
     * 
     * @return the text format
     */
    private static String getTextFormat(Object toFormat,
            DataFormatter dataFormatter, String customFormat) {

        dataFormatter.formatAttributes.put("textFormat", customFormat);

        return getTextFormat(toFormat, new DataFormatter(
                dataFormatter.formatAttributes));

    }

    /**
     * Replace label.
     * 
     * @param label the label
     * @param myValue the my value
     * @param data the data
     * 
     * @return the string
     */
    public String replaceLabel(String label, Value myValue, Data data) {

        // replace {x}, {y} with values
        if (label.indexOf('{') != -1) {
            if (label.indexOf("{a1}") != -1) {
                label = label.replaceAll("\\{a1\\}", "" + myValue.getA1());
            }
            if (label.indexOf("{a2}") != -1) {
                label = label.replaceAll("\\{a2\\}", "" + myValue.getA2());
            }

            if (label.indexOf("{b1}") != -1) {
                if (data.getParentArea().getType().equals("stackedbar100"))
                    label =
                            label.replaceAll("\\{b1\\}", data.getParentArea()
                                    .getXAxis().getFormat(myValue.getB1()));
                if (data.getParentArea().getType().equals("stackedcolumn100")
                        || data.getParentArea().getType().equals(
                                "stackedarea100")
                        || data.getParentArea().getType().equals(
                                "stackedsplinearea100"))
                    label =
                            label.replaceAll("\\{b1\\}", data.getParentArea()
                                    .getYAxis().getFormat(myValue.getB1()));
                else
                    label = label.replaceAll("\\{b1\\}", "" + myValue.getB1());
            }
            if (label.indexOf("{b2}") != -1) {
                label = label.replaceAll("\\{b2\\}", "" + myValue.getB2());
            }
            if (label.indexOf("{x}") != -1) {
                if (data.getParentArea().getType().equals("stackedbar100"))
                    label =
                            label.replaceAll("\\{x\\}", ""
                                    + myValue.getXObject());

                else if (data.getParentArea().getXAxis() != null) {
                    label =
                            label
                                    .replaceAll("\\{x\\}", data.getParentArea()
                                            .getXAxis().getFormat(
                                                    myValue.getXObject()));
                } else {
                    label =
                            label.replaceAll("\\{x\\}", DataFormatter
                                    .getFormat(myValue.getXObject(), data
                                            .getDataFormatter()));
                }
            }
            if (label.indexOf("{y}") != -1) {
                if (data.getParentArea().getType().equals("stackedcolumn100")
                        || data.getParentArea().getType().equals(
                                "stackedarea100")
                        || data.getParentArea().getType().equals(
                                "stackedsplinearea100"))
                    label =
                            label.replaceAll("\\{y\\}", ""
                                    + myValue.getYObject());
                else if (data.getParentArea().getYAxis() != null) {
                    label =
                            label
                                    .replaceAll("\\{y\\}", data.getParentArea()
                                            .getYAxis().getFormat(
                                                    myValue.getYObject()));
                } else {
                    label =
                            label.replaceAll("\\{y\\}", DataFormatter
                                    .getFormat(myValue.getYObject(), data
                                            .getDataFormatter()));
                }
            }
            if (label.indexOf("{series}") != -1) {

                label =
                        label.replaceAll("\\{series\\}", myValue
                                .getParentSeries().getId());
            }
            if (label.indexOf("{group}") != -1) {

                label =
                        label
                                .replaceAll("\\{group\\}", data.getGroup()
                                        .getId());
            }
            if (label.indexOf("{legend}") != -1) {

                label =
                        label.replaceAll("\\{legend\\}", myValue
                                .getParentSeries().getLegend());
            }
        }
        return label;
    }

    /** The data attributes. */
    private HashMap<String, String> formatAttributes =
            new HashMap<String, String>();

    /**
     * Instantiates a new data formatter.
     * 
     * @param formatAttributes the format attributes
     */
    public DataFormatter(HashMap<String, String> formatAttributes) {
        if (formatAttributes != null)
            this.formatAttributes = formatAttributes;
    }

    /**
     * Gets the currency format.
     * 
     * @return the currency format
     */
    public String getCurrencyFormat() {
        return formatAttributes.get("currencyFormat");
    }

    /**
     * Gets the date format.
     * 
     * @return the date format
     */
    public String getDateFormat() {
        return formatAttributes.get("dateFormat");
    }

    /**
     * Gets the float format.
     * 
     * @return the float format
     */
    public String getFloatFormat() {
        return formatAttributes.get("floatFormat");
    }

    /**
     * Gets the int format.
     * 
     * @return the int format
     */
    public String getIntFormat() {
        return formatAttributes.get("intFormat");
    }

    /**
     * Gets the locale.
     * 
     * @return the locale
     */
    public Locale getLocale() {
        Locale locale = TypeConverter.getLocale(formatAttributes.get("locale"));
        return locale;
    }

    /**
     * Gets the text format.
     * 
     * @return the text format
     */
    public String getTextFormat() {
        return formatAttributes.get("textFormat");
    }

    /**
     * Gets the time zone.
     * 
     * @return the time zone
     */
    public TimeZone getTimeZone() {
        return TypeConverter.getTimeZone(formatAttributes.get("timeZone"));
    }
}
