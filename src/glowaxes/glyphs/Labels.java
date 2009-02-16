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

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * The Class Labels.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Labels {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Labels.class.getName());

    /** The labels attributes. */
    private HashMap<String, String> labelsAttributes =
            new HashMap<String, String>();

    /**
     * Instantiates a new labels object.
     * 
     * @param _labelsAttributes the _labels attributes
     */
    public Labels(HashMap<String, String> _labelsAttributes) {

        logger.info("construction Labels");

        labelsAttributes = _labelsAttributes;

    }

    /**
     * Get the popup.
     * 
     * @return the popup
     */
    public String getPopup() {
        if (labelsAttributes.get("popup") == null)
            return "false";
        return labelsAttributes.get("popup").trim();
    }

    // /**
    // */
    // public String getRx() {
    // return labelsAttributes.get("rx");
    // }
    //
    // /**
    // */
    // public String getRy() {
    // return labelsAttributes.get("ry");
    // }

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public String getText() {
        return labelsAttributes.get("text");
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        if (labelsAttributes.get("type") == null)
            return "none";
        else
            return labelsAttributes.get("type").trim();
    }
}
