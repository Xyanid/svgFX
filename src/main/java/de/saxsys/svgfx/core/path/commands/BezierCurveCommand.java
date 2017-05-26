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

import de.saxsys.svgfx.core.path.PathException;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This represents a bezier curve command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class BezierCurveCommand extends PathCommand {

    // region Constants

    /**
     * Determine a value that indicated a near null.
     */
    private static final double NEAR_NULL = 1e-12;

    // endregion

    // region Fields

    /**
     * The end control point of the curve.
     */
    private final Point2D endPoint;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is absolute or not.
     * @param endPoint   the end point of this command.
     */
    BezierCurveCommand(final boolean isAbsolute,
                       final Point2D endPoint) {
        super(isAbsolute);
        this.endPoint = endPoint;
    }

    // endregion

    // region Getter

    /**
     * Returns the {@link #endPoint}.
     *
     * @return the {@link #endPoint}.
     */
    public Point2D getEndPoint() {
        return endPoint;
    }

    // endregion

    // region Public Abstract

    public abstract Point2D getAbsoluteStartControlPoint(final Point2D absoluteCurrentPoint) throws PathException;

    public abstract Point2D getAbsoluteEndControlPoint(final Point2D absoluteCurrentPoint) throws PathException;

    // endregion

    // region Implement PathCommand

    @Override
    public Point2D getAbsoluteEndPoint(final Point2D absoluteCurrentPoint) throws PathException {
        return addPoints(absoluteCurrentPoint, this.endPoint);
    }

    @Override
    public Optional<Rectangle> getBoundingBox(final Point2D absoluteCurrentPoint) throws PathException {
        if (absoluteCurrentPoint == null) {
            throw new PathException("Can not create bounding box with missing start position");
        }

        final Point2D absoluteStartControlPoint = getAbsoluteStartControlPoint(absoluteCurrentPoint);
        final Point2D absoluteEndControlPoint = getAbsoluteEndControlPoint(absoluteCurrentPoint);
        final Point2D absoluteEndPoint = getAbsoluteEndPoint(absoluteCurrentPoint);
        final List<Double> localExtrema = getExtremaValues(absoluteCurrentPoint, absoluteStartControlPoint, absoluteEndControlPoint, absoluteEndPoint);

        double minX = Math.min(absoluteCurrentPoint.getX(), absoluteEndPoint.getX());
        double maxX = Math.max(absoluteCurrentPoint.getX(), absoluteEndPoint.getX());
        double minY = Math.min(absoluteCurrentPoint.getY(), absoluteEndPoint.getY());
        double maxY = Math.max(absoluteCurrentPoint.getY(), absoluteEndPoint.getY());

        for (final Double t : localExtrema) {
            final Point2D curvePosition = getCurvePoint(t, absoluteCurrentPoint, absoluteStartControlPoint, absoluteEndControlPoint, absoluteEndPoint);
            minX = Math.min(minX, curvePosition.getX());
            maxX = Math.max(maxX, curvePosition.getX());
            minY = Math.min(minY, curvePosition.getY());
            maxY = Math.max(maxY, curvePosition.getY());
        }

        return Optional.of(new Rectangle(minX, minY, maxX - minX, maxY - minY));
    }

    // endregion

    // region Private

    private Point2D getCurvePoint(final Double t,
                                  final Point2D start,
                                  final Point2D startControl,
                                  final Point2D endControl,
                                  final Point2D end) {
        final Double mt = 1.0d - t;

        final Double tEnd = Math.pow(t, 3);
        final Double tEndControl = 3 * Math.pow(t, 2) * mt;

        final Double mtStart = Math.pow(mt, 3);
        final Double mtStartControl = 3 * Math.pow(mt, 2) * t;

        final Double x = mtStart * start.getX() + mtStartControl * startControl.getX() + tEndControl * endControl.getX() + tEnd * end.getX();
        final Double y = mtStart * start.getY() + mtStartControl * startControl.getY() + tEndControl * endControl.getY() + tEnd * end.getY();

        return new Point2D(x, y);
    }

    private List<Double> getExtremaValues(final Point2D start,
                                          final Point2D startControl,
                                          final Point2D endControl,
                                          final Point2D end) {

        final List<Double> result = new ArrayList<>(0);

        result.addAll(getExtremaValues(start.getX(), startControl.getX(), endControl.getX(), end.getX()));
        result.addAll(getExtremaValues(start.getY(), startControl.getY(), endControl.getY(), end.getY()));

        return result;
    }

    private List<Double> getExtremaValues(final Double start, final Double startControl, final Double endControl, final Double end) {

        final List<Double> result = new ArrayList<>(0);

        final Double a = -3 * start + 9 * startControl - 9 * endControl + 3 * end;
        final Double b = 6 * start - 12 * startControl + 6 * endControl;
        final Double c = 3 * startControl - 3 * start;

        final Double b2ac = b * b - 4 * c * a;
        final Double sqrtb2ac = Math.sqrt(b2ac);

        if (isSmallerOrNearNull(Math.abs(a)) && !isSmallerOrNearNull(Math.abs(b))) {
            final double t = -c / b;
            if (0 < t && t < 1) {
                result.add(t);
            }
        } else if (b2ac >= 0) {
            final Double t1 = (-b + sqrtb2ac) / (2 * a);
            if (0 < t1 && t1 < 1) {
                result.add(t1);
            }
            final Double t2 = (-b - sqrtb2ac) / (2 * a);
            if (0 < t2 && t2 < 1) {
                result.add(t2);
            }
        }

        return result;
    }

    private boolean isSmallerOrNearNull(final double value) {
        return value < NEAR_NULL;
    }

    // endregion
}