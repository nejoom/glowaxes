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
package glowaxes.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

/**
 * The Class AbstractBodyTag.
 */
public abstract class AbstractBodyTag extends BodyTagSupport {

    /** The logger. */
    private static Logger logger =
            Logger.getLogger(AbstractBodyTag.class.getName());

    /**
     * Instantiates a new abstract body tag.
     */
    public AbstractBodyTag() {
    }

    /**
     * Handle exception.
     * 
     * @param exception
     *            the exception
     * 
     * @throws JspException
     *             the jsp exception
     */
    protected void handleException(Exception exception) throws JspException {
        if (exception instanceof JspException) {
            throw (JspException) exception;
        } else {
            logger.error(exception.getMessage());
            throw new JspException(exception.getMessage());
        }
    }
}