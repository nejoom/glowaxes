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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The <code>DateParser</code> class is used to parse dates with a given
 * format with static (thread safe) methods {@link java.util.Date}
 * 
 * @version ${version}, 4-jun-2006
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 * @since ${since_tag}
 */
public class DateParser {

    /**
     * Return a {@link java.util.Date} represented by a string, the pattern of
     * the date string is given by the format string (for details concerning
     * symbols see {@link java.text.SimpleDateFormat}).
     * 
     * @param dateString
     *            the date as a string (null = new Date())
     * @param formatString
     *            the string that represents the format of the dateString
     *            parameter (eg for "05 12, 1932", use "MM dd, yyyy")
     * 
     * @return a date represented by formatString or the current date if there
     *         is a parse error, see
     *         {@link java.text.SimpleDateFormat#parse(String)}.
     * 
     * <pre>
     * ENTER DATE STRING (MM dd, yyyy): 05 12, 1932
     * Original string: 05 12, 1932
     * Parsed date : Thu May 12 00:00:00 CET 1932
     * Parsed date : -1187830800000
     * </pre>
     */
    public static Date getFormat(String dateString, String formatString) {

        SimpleDateFormat format = new SimpleDateFormat(formatString);

        // System.out.println(dateString);
        // System.out.println(_formatString);
        //
        Date date = null;

        try {
            date = format.parse(dateString);
        } catch (ParseException pe) {
            System.err.println("ERROR: could not parse date in string \""
                    + dateString + "\", with format: " + formatString);
            date = new Date();
            // throw new RuntimeException(pe);
        }

        return date;

    }
}
