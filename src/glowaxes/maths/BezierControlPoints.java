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

package glowaxes.maths;

import glowaxes.data.Series;
import glowaxes.data.Value;

/**
 * The Class BezierControlPoints. see code and:
 * <a href="http://www.antigrain.com/agg_research/bezier_interpolation.html">
 * bezier interpolation</a> for documentation. 
 * Represents a solution for plotting Bezier line curves.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class BezierControlPoints {

    /**
     * Calc.
     * 
     * @param previous
     *            the previous
     * @param start
     *            the start
     * @param end
     *            the end
     * @param next
     *            the next
     * 
     * @return the series
     */
    public static Series calc(Value previous, Value start, Value end, Value next) {

        double smooth_value = 0.9;
        // Assume we need to calculate the control
        // points between (x1,y1) and (x2,y2).
        // Then x0,y0 - the previous vertex,
        // x3,y3 - the next one.

        double x0 = previous.getX();
        double y0 = previous.getY();
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();
        double x3 = next.getX();
        double y3 = next.getY();

        double xc1 = (x0 + x1) / 2.0;
        double yc1 = (y0 + y1) / 2.0;
        double xc2 = (x1 + x2) / 2.0;
        double yc2 = (y1 + y2) / 2.0;
        double xc3 = (x2 + x3) / 2.0;
        double yc3 = (y2 + y3) / 2.0;

        double len1 = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
        double len2 = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double len3 = Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));

        double k1 = len1 / (len1 + len2);
        double k2 = len2 / (len2 + len3);

        double xm1 = xc1 + (xc2 - xc1) * k1;
        double ym1 = yc1 + (yc2 - yc1) * k1;

        double xm2 = xc2 + (xc3 - xc2) * k2;
        double ym2 = yc2 + (yc3 - yc2) * k2;

        // Resulting control points. Here smooth_value is mentioned
        // above coefficient K whose value should be in range [0...1].
        double ctrl1_x = xm1 + (xc2 - xm1) * smooth_value + x1 - xm1;
        double ctrl1_y = ym1 + (yc2 - ym1) * smooth_value + y1 - ym1;

        double ctrl2_x = xm2 + (xc2 - xm2) * smooth_value + x2 - xm2;
        double ctrl2_y = ym2 + (yc2 - ym2) * smooth_value + y2 - ym2;

        /*
         * see:
         * http://www.developer.com/java/other/article.php/3689251#Listing_3
         */
        // M100,100 C100,50 175,50 175,100 S250,150 250,100
        // As mentioned earlier, the red Bezier curve in Figure 1
        // consists of two Bezier segments. The start point for the
        // first segment is established by the moveto command (M).
        // This is followed by a curve command (C) followed by three
        // pairs of coordinate values.
        //
        // The first two pairs of coordinate
        // values specify the control points for the first segment.
        // The third pair specifies the end point for the first segment.
        //
        // This is followed by the special curve command (S).
        // Recall that the start point for the second and subsequent
        // Bezier segments is always assumed to be the end point for
        // the previous segment. This eliminates the need for one pair
        // of coordinate values following the S command.
        // Also recall that this special curve command (S)
        Value control1 = new Value();
        control1.setX(ctrl1_x);
        control1.setY(ctrl1_y);

        Value control2 = new Value();
        control2.setX(ctrl2_x);
        control2.setY(ctrl2_y);

        Series mySeries = new Series();
        mySeries.addValue(start);
        mySeries.addValue(control1);
        mySeries.addValue(control2);
        mySeries.addValue(end);

        // System.out.println(mySeries.getValue(0).getX() + ", "
        // + mySeries.getValue(0).getY());
        // System.out.println(mySeries.getValue(1).getX() + ", "
        // + mySeries.getValue(1).getY());
        // System.out.println(mySeries.getValue(2).getX() + ", "
        // + mySeries.getValue(2).getY());
        // System.out.println(mySeries.getValue(3).getX() + ", "
        // + mySeries.getValue(3).getY());
        // System.out.println();
        return mySeries;

    }

    /**
     * Calc.
     * 
     * @param values
     *            the values
     * 
     * @return the series
     */
    public static Series calc(Series values) {

        if (values.getSize() < 3)
            return null;

        Series myPath = new Series();
        Series myPoints = new Series();
        for (int i = 0; i < values.getSize() - 1; i++) {
            if (i == 0) {
                myPoints =
                        BezierControlPoints.calc(values.getValue(0), values
                                .getValue(0), values.getValue(1), values
                                .getValue(2));
            } else if (i == values.getSize() - 2) {
                myPoints =
                        BezierControlPoints.calc(values.getValue(i - 1), values
                                .getValue(i), values.getValue(i + 1), values
                                .getValue(i + 1));
            } else {
                myPoints =
                        BezierControlPoints.calc(values.getValue(i - 1), values
                                .getValue(i), values.getValue(i + 1), values
                                .getValue(i + 2));
            }
            if (i == 0) {

                myPath.addValue(myPoints.getValue(0));
                myPath.addValue(myPoints.getValue(1));
                myPath.addValue(myPoints.getValue(2));
                myPath.addValue(myPoints.getValue(3));
            } else {
                myPath.addValue(myPoints.getValue(2));
                myPath.addValue(myPoints.getValue(3));
            }
        }

        return myPath;
    }

    /**
     * Gets the path.
     * 
     * @param values
     *            the values
     * 
     * @return the path
     */
    public static String getPath(Series values) {

        Series myPath = calc(values);

        String path = "";

        String token = "M";

        for (int i = 0; i < myPath.getSize(); i++) {
            if (i == 1)
                token = "C";
            else if (i == 2 || i == 3 || (i % 2 != 0) /* odd */) {
                token = "";
            } else if (i != 0) {
                token = "S";
            }
            path =
                    path + token + myPath.getValue(i).getX() + ","
                            + myPath.getValue(i).getY() + " ";

        }

        path = path.trim();

        return path;
    }

    /**
     * Gets the path area.
     * 
     * @param values
     *            the values
     * @param zeroValue
     *            the zero value
     * 
     * @return the path area
     */
    public static String getPathArea(Series values, double zeroValue) {

        Series myPath = calc(values);

        String path = "";

        String token = "M";

        for (int i = 0; i < myPath.getSize(); i++) {
            if (i == 1)
                token = "C";
            else if (i == 2 || i == 3 || (i % 2 != 0) /* odd */) {
                token = "";
            } else if (i != 0) {
                token = "S";
            }
            path =
                    path + token + myPath.getValue(i).getX() + ","
                            + myPath.getValue(i).getY() + " ";

        }

        path =
                path + "L" + (myPath.getValue(myPath.getSize() - 1).getX())
                        + "," + zeroValue + " L" + (myPath.getValue(0).getX())
                        + "," + zeroValue + " L" + (myPath.getValue(0).getX())
                        + "," + (myPath.getValue(0).getY());

        return path;
    }

    /**
     * Gets the path area.
     * 
     * @param values
     *            the values
     * @param values2
     *            the values2
     * 
     * @return the path area
     */
    public static String getPathArea(Series values, Series values2) {

        Series myPath = calc(values);

        String path = "";

        String token = "M";

        for (int i = 0; i < myPath.getSize(); i++) {
            if (i == 1)
                token = "C";
            else if (i == 2 || i == 3 || (i % 2 != 0) /* odd */) {
                token = "";
            } else if (i != 0) {
                token = "S";
            }
            path =
                    path + token + myPath.getValue(i).getX() + ","
                            + myPath.getValue(i).getY() + " ";

        }

        Series myPath2 = calc(values2);

        String token2 = "L";

        for (int i = 0; i < myPath2.getSize(); i++) {
            if (i == 1)
                token2 = "C";
            else if (i == 2 || i == 3 || (i % 2 != 0) /* odd */) {
                token2 = "";
            } else if (i != 0) {
                token2 = "S";
            }
            path =
                    path + token2 + myPath2.getValue(i).getX() + ","
                            + myPath2.getValue(i).getY() + " ";

        }

        path =
                path + "L" + (myPath.getValue(0).getX()) + ","
                        + (myPath.getValue(0).getY());

        //
        // path =
        // path + "L" + (myPath.getValue(myPath.getSize() - 1).getX()) + ","
        // + zeroValue + " L" + (myPath.getValue(0).getX()) + "," + zeroValue
        // + " L" + (myPath.getValue(0).getX()) + "," + zeroValue + " "
        // + (myPath.getValue(0).getX()) + ","
        // + (myPath.getValue(0).getY());

        return path;
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        Value one = new Value();
        one.setX(0);
        one.setY(0);
        Value two = new Value();
        two.setX(100);
        two.setY(100);
        Value three = new Value();
        three.setX(200);
        three.setY(0);
        Value four = new Value();
        four.setX(300);
        four.setY(-100);
        Value five = new Value();
        five.setX(400);
        five.setY(0);
        Value six = new Value();
        six.setX(500);
        six.setY(100);
        Value seven = new Value();
        seven.setX(600);
        seven.setY(0);
        Series Series = new Series();

        Series.addValue(one);
        Series.addValue(two);
        Series.addValue(three);
        Series.addValue(four);
        Series.addValue(five);
        Series.addValue(six);
        Series.addValue(seven);

        // Seriess mySeries = BezierControlPoints.calc(Seriess);
        // String path = "";
        // for (int i = 0; i < mySeries.getSize(); i++) {
        // path =
        // path + mySeries.getValue(i).getX() + ","
        // + mySeries.getValue(i).getY() + " ";
        //
        // }
        System.out.println(BezierControlPoints.getPath(Series));
    }
}
