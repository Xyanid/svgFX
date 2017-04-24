/*
 * Copyright 2015 - 2017 Xyanid
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package de.saxsys.svgfx.core.path.commands;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a bezier curve command in a svg path which will either be a {@link CubicBezierCurveCommand}, {@link ShortCubicBezierCurveCommand}, {@link QuadraticBezierCurveCommand}
 * or {@link ShortQuadraticBezierCurveCommand}. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class BezierCurveCommand extends PathCommand {

    // region Constants


    // endregion

    // region Field

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is absolute or not.
     */
    BezierCurveCommand(final boolean isAbsolute) {
        super(isAbsolute);
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return null;
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        final Point2D nextPosition = getNextPosition(position);

        return null;
    }

    // endregion

    // region Private

    /**
     * Returns a list of values that represent the local extrema for a cubic bezier curve.
     *
     * @param startX        the x coordinate of the start point
     * @param startY        the y coordinate of the start point
     * @param startControlX the x coordinate of the start control point
     * @param startControlY the y coordinate of the start control point
     * @param endControlX   the x coordinate of the end control point
     * @param endControlY   the y coordinate of the end control point
     * @param endX          the x coordinate of the end point
     * @param endY          the y coordinate of the end point
     *
     * @return a new {@link List} containing the calculated local extrema.
     */
    protected List<Double> getLocalExtremaForCubicCurve(final Double startX,
                                                        final Double startY,
                                                        final Double startControlX,
                                                        final Double startControlY,
                                                        final Double endControlX,
                                                        final Double endControlY,
                                                        final Double endX,
                                                        final Double endY) {
        final List<Double> tValues = new ArrayList<>(0);

        Double a;
        Double b;
        Double c;
        Double t;
        Double t1;
        Double t2;
        Double b2ac;
        Double sqrtb2ac;

        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                b = 6 * startX - 12 * startControlX + 6 * endControlX;
                a = -3 * startX + 9 * startControlX - 9 * endControlX + 3 * endX;
                c = 3 * startControlX - 3 * startX;
            } else {
                b = 6 * startY - 12 * startControlY + 6 * endControlY;
                a = -3 * startY + 9 * startControlY - 9 * endControlY + 3 * endY;
                c = 3 * startControlY - 3 * startY;
            }

            if (Math.abs(a) < 1e-12) {
                if (Math.abs(b) < 1e-12) {
                    continue;
                }
                t = -c / b;
                if (0 < t && t < 1) {
                    tValues.add(t);
                }
                continue;
            }

            b2ac = b * b - 4 * c * a;
            sqrtb2ac = Math.sqrt(b2ac);
            if (b2ac < 0) {
                continue;
            }
            t1 = (-b + sqrtb2ac) / (2 * a);
            if (0 < t1 && t1 < 1) {
                tValues.add(t1);
            }
            t2 = (-b - sqrtb2ac) / (2 * a);
            if (0 < t2 && t2 < 1) {
                tValues.add(t2);
            }
        }

        return tValues;
    }

    /**
     * Calculates the {@link Point2D} of a bezier curve for the given t.
     *
     * @param t             the t value to use.
     * @param startX        the x coordinate of the start point
     * @param startY        the y coordinate of the start point
     * @param startControlX the x coordinate of the start control point
     * @param startControlY the y coordinate of the start control point
     * @param endControlX   the x coordinate of the end control point
     * @param endControlY   the y coordinate of the end control point
     * @param endX          the x coordinate of the end point
     * @param endY          the y coordinate of the end point
     *
     * @return a new {@link Point2D} containing the calculated bezier curve position.
     */
    protected Point2D getCubicBezierCurvePoint(final Double t,
                                               final Double startX,
                                               final Double startY,
                                               final Double startControlX,
                                               final Double startControlY,
                                               final Double endControlX,
                                               final Double endControlY,
                                               final Double endX,
                                               final Double endY) {
        final Double mt = 1.0d - t;

        final Double tEnd = Math.pow(t, 3);
        final Double tEndControl = 3 * Math.pow(t, 2) * mt;

        final Double mtStart = Math.pow(mt, 3);
        final Double mtStartControl = 3 * Math.pow(mt, 2) * t;

        final Double x = mtStart * startX + mtStartControl * startControlX + tEndControl * endControlX + tEnd * endX;
        final Double y = mtStart * startY + mtStartControl * startControlY + tEndControl * endControlY + tEnd * endY;

        return new Point2D(x, y);
    }
    //endregion

}
