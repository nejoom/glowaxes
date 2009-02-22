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

import glowaxes.glyphs.Rasterizer;
import glowaxes.util.TypeConverter;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Logger;

/**
 * The Class RasterizerTag.
 */
public class RasterizerTag extends AbstractBodyTag {

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(RasterizerTag.class.getName());

    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = -6754217302435362967L;

    private double height = 10;

    private String src;

    private String text;

    private double width = 10;

    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new chart tag.
     */
    public RasterizerTag() {
        super();

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    @Override
    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        if (body != null) {
            text = body.getString();
            body.clearBody();
        }

        return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspTagException {

        long t0 = System.currentTimeMillis();
        try {
            if (src != null) {

                Rasterizer myRasterizer = new Rasterizer();
                myRasterizer.setSource(src);
                myRasterizer.setGlyphHeight(height);
                myRasterizer.setGlyphWidth(width);
                myRasterizer.setPageContext(pageContext);
                pageContext.getOut().write(myRasterizer.getResult());

            } else if (text != null) {

                Rasterizer myRasterizer = new Rasterizer(text);
                myRasterizer.setPageContext(pageContext);
                myRasterizer.setGlyphHeight(height);
                myRasterizer.setGlyphWidth(width);
                pageContext.getOut().write(myRasterizer.getResult());

            } else
                pageContext.getOut().write("null atrributes for src" + "<br>");

        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            pageContext.getOut().flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String memory =
                "["
                        + ((Runtime.getRuntime().totalMemory() - Runtime
                                .getRuntime().freeMemory()) / (1024 * 1024))
                        + " Mb] used of ["
                        + (Runtime.getRuntime().totalMemory() / (1024 * 1024))
                        + " Mb], ";

        logger.info("doEndTag finished in " + (System.currentTimeMillis() - t0)
                + " ms " + memory);

        return EVAL_PAGE;
    }

    /*
     * Override: The method below will use the PageContext to get a JspWriter
     * class. It will then use this Writer to write a message to the encapsuling
     * JSP page. As the SupportTag has implemented a doStartTag() method that
     * returns SKIP_BODY, we do need to return EVAL_BODY_BUFFERED we want to
     * override.
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspTagException {
        Defs.init();
        return EVAL_BODY_BUFFERED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    @Override
    public void release() {
        super.release();
    }

    /**
     * Sets the height.
     * 
     * @param height
     *            The height of the chart see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setHeight(String height) {
        this.height = TypeConverter.getDouble(height, 100);
    }

    /**
     * Sets the src of the chart.
     * 
     * @param src
     *            The svg src url
     * 
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * Sets the width.
     * 
     * @param width
     *            The width of the chart see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setWidth(String width) {
        this.width = TypeConverter.getDouble(width, 100);
    }

}
