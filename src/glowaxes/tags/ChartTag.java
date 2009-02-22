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

import glowaxes.glyphs.Chart;
import glowaxes.util.ChartRegistry;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
// @SuppressWarnings("unchecked")
/**
 * The Class ChartTag.
 */
public class ChartTag extends AbstractBodyTag {

    /** The area counter. */
    private static int areaCounter = 0;

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger = Logger.getLogger(ChartTag.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8645363958238084709L;

    /** The chart attributes. */
    private LinkedHashMap<String, Object> chartAttributes =
            new LinkedHashMap<String, Object>();

    // Properties, stored in chartAttributes
    // -------------------------------------------------------------------------

    // Tag interface
    // -------------------------------------------------------------------------
    /*
     * Override: Call the default TagSupport constructor.
     */
    /**
     * Instantiates a new chart tag.
     */
    public ChartTag() {
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
            String text = body.getString();
            body.clearBody();
            setTitle(text);
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
            if (chartAttributes != null) {

                chartAttributes.put("pageContext", pageContext);

                Chart myChart = new Chart(chartAttributes);
                pageContext.getOut().write(myChart.getResult());

            } else
                pageContext.getOut()
                        .write("null atrributes for chart" + "<br>");

            chartAttributes = new LinkedHashMap<String, Object>();

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

    //
    // public void setWidth(int width) {
    // chartAttributes.put("width", new Integer(width));
    // }
    //
    // public int getWidth() {
    // return TypeConverter.getInt(chartAttributes.get("width"), 0);
    // }

    /*
     * Override: Container calls the doStartTag when tag starts
     */
    // public int doStartTag() throws JspException {
    // try
    // {
    // if (chartAttributes != null)
    // {
    // Integer chartId = new
    // Integer(Constants.getCounterAndIncrease());
    // chartAttributes.put("chartId", chartId);
    // String JSESSIONID = pageContext.getSession().getId();
    // chartAttributes.put("JSESSIONID", JSESSIONID);
    // pageContext.getOut().write(chartAttributes.toString() + "<br>");
    // }
    // }
    // catch(java.io.IOException e)
    // {
    // throw new JspTagException("IO Error: " + e.getMessage());
    // }
    // return EVAL_BODY_INCLUDE;
    // }
    /*
     * Override: The method below will use the PageContext to get a JspWriter
     * class. It will then use this Writer to write a message to the encapsuling
     * JSP page. As the SupportTag has implemented a doStartTag() method that
     * returns SKIP_BODY, we do need to return EVAL_BODY_BUFFERED we want to
     * override.
     */
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspTagException {
        Defs.init();
        chartAttributes.put("id", ChartRegistry.SINGLETON.getHex());

        setId((String) chartAttributes.get("id"));

        return EVAL_BODY_BUFFERED;
    }

    // public int getHeight() {
    // return chartAttributes.get("height");
    // }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    @Override
    public void release() {
        super.release();
    }

    //
    // public void setX(int x) {
    // chartAttributes.put("x", new Integer(x));
    // }

    protected String getOutput() {
        return (String) chartAttributes.get("output");
    }

    /**
     * Sets AreaGrouping, the way the area plots of graphs are arranged in a
     * chart.
     * 
     * @param areaGrouping
     *            The areaGrouping [tile-vertical][tile-horizontal][combined]
     */
    public void setAreaGrouping(String areaGrouping) {
        areaGrouping = areaGrouping.toLowerCase().trim();
        chartAttributes.put("areaGrouping", areaGrouping);
    }

    // public int getY() {
    // return TypeConverter.getInt(chartAttributes.get("y"), 0);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see glowaxes.tags.AttributeInterface#setAttribute(java.lang.String,
     *      java.util.HashMap)
     */
    @SuppressWarnings("unchecked")
    public synchronized void setAttribute(String key, HashMap value) {
        if (chartAttributes.containsKey(key) && key.equals("area"))
            key += key + areaCounter++;
        chartAttributes.put(key, value.clone());
    }

    // public int getRx() {
    // return TypeConverter.getInt(chartAttributes.get("rx"), 0);
    // }

    /**
     */
    public void setEffect(String effect) {
    }

    // public int getRy() {
    // return TypeConverter.getInt(chartAttributes.get("ry"), 0);
    // }

    /**
     * Sets the expire time, the time in ms after which the chart image is
     * removed from cached.
     * 
     * @param expiretime
     *            the expiretime
     */
    public void setExpireTimeMilli(String expiretime) {
        expiretime = expiretime.toLowerCase().trim();
        chartAttributes.put("expiretime", expiretime);
    }

    /**
     * Sets the height.
     * 
     * @param height
     *            The height of the chart see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setHeight(String height) {
        chartAttributes.put("height", height);
    }

    /**
     * Sets the backgroundImage, the chart backgroundImage.
     * 
     * @param imageStyle
     *            the image style
     */
    // public void setImageStyle(String imageStyle) {
    // logger.error(imageStyle);
    // chartAttributes.put("imageStyle", Defs.parseStyle(imageStyle));
    // }
    /**
     * Sets the backgroundImage, the chart backgroundImage.
     * 
     * @param image
     *            the image
     */
    public void setImage(String image) {
        if (!image.trim().equals(""))
            chartAttributes.put("image", image);
    }

    // public void setLocale(Locale locale) {
    // chartAttributes.put("locale", locale);
    // }

    // public Locale getLocale() {
    // return TypeConverter.getLocale((String)chartAttributes.get("locale"));
    // }

    /**
     * Sets the image effect, the chart image effect.
     * 
     * @param effect
     *            The svg effect on the chart image
     *            [none][Shadow][ShadowGloss][ShadowFast][ShadowTopExpensive][ShadowCartesian][ShadowAdobe][ShadowBrushTM]
     */
    public void setImageEffect(String imageEffect) {
        chartAttributes.put("imageEffect", Defs.addUsed(imageEffect));
    }

    /**
     * Sets output of this tag.
     * 
     * @param output
     *            The output [svg] [png] [jpg] [tiff] [html-table_tm*] [xml*] *
     *            not implemented
     */
    public void setOutput(String output) {
        if (output != null)
            output = output.toLowerCase().trim();
        chartAttributes.put("output", output);
    }

    /**
     * Sets rx of the chart rectangle (SVG).
     * 
     * @param rx
     *            The rx radius (SVG) see
     *            http://www.w3.org/TR/SVG/shapes.html#RectElement
     */
    public void setRx(String rx) {
        chartAttributes.put("rx", rx);
    }

    /**
     * Sets ry of the chart rectangle (SVG).
     * 
     * @param ry
     *            The ry radius (SVG) see
     *            http://www.w3.org/TR/SVG/shapes.html#RectElement
     */
    public void setRy(String ry) {
        chartAttributes.put("ry", ry);
    }

    /**
     * Sets the style, the way the chart rectangle is styled.
     * 
     * @param style
     *            The style
     *            [fill:url(#vertical);stroke:blue;stroke-width:3;opacity:1.0]
     *            see http://www.w3.org/TR/SVG/painting.html
     */
    public void setStyle(String style) {
        chartAttributes.put("style", Defs.parseStyle(style));
    }

    /**
     * Sets the title, the chart title.
     * 
     * @param title
     *            The chart's title, nothing defaults to empty string ""
     */
    public void setTitle(String title) {
        title = title.trim();
        if (chartAttributes.get("title") == null || !title.equals(""))
            chartAttributes.put("title", title);
    }

    /**
     * Sets the titleAlign, the chart titleAlign.
     * 
     * @param titleAlign
     *            The chart's alignment of the title, [left][right][center][x,y]
     */
    public void setTitleAlign(String titleAlign) {
        titleAlign = titleAlign.toLowerCase().trim();
        chartAttributes.put("titleAlign", titleAlign);
    }

    /**
     * Sets the titleEffect, the chart titleEffect.
     * 
     * @param titleEffect
     *            the title effect
     */
    public void setTitleEffect(String titleEffect) {
        chartAttributes.put("titleEffect", Defs.addUsed(titleEffect));
    }

    /**
     * Sets the titleStyle, the chart titleStyle.
     * 
     * @param titleStyle
     *            the title style
     * 
     * @see <a
     *      href='http://www.w3.org/TR/SVG/painting.html'>http://www.w3.org/TR/SVG/painting.html</a>
     */
    public void setTitleStyle(String titleStyle) {
        chartAttributes.put("titleStyle", Defs.parseStyle(titleStyle));
    }

    /**
     * Sets the titleValign, the chart titleValign.
     * 
     * @param titleValign
     *            The chart's verticle alignment of the title, [top][bottom]
     */
    public void setTitleValign(String titleValign) {
        titleValign = titleValign.toLowerCase().trim();
        chartAttributes.put("titleValign", titleValign);
    }

    /**
     * Sets the width.
     * 
     * @param width
     *            The width of the chart see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setWidth(String width) {
        chartAttributes.put("width", width);
    }

    /**
     * Sets the x offset from left (SVG).
     * 
     * @param x
     *            The x offset from left (SVG) see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setX(String x) {
        chartAttributes.put("x", x);
    }

    /**
     * Sets the y offset from left (SVG).
     * 
     * @param y
     *            The y offset from left (SVG) see
     *            http://www.w3.org/TR/SVG/coords.html
     */
    public void setY(String y) {
        chartAttributes.put("y", y);
    }

}
