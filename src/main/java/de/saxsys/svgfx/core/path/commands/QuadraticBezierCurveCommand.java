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

/**
 * This represents a bezier curve command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public class QuadraticBezierCurveCommand extends BezierCurveCommand {

    // region Constants

    private static final double TWO_THIRD = 2.0d / 3.0d;

    // endregion

    // region Fields

    /**
     * The control point of the curve, which may either be relative or absolute.
     */
    private final Point2D controlPoint;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is absolute or not.
     */
    QuadraticBezierCurveCommand(final boolean isAbsolute,
                                final Point2D controlPoint,
                                final Point2D endPoint) {
        super(isAbsolute, endPoint);
        this.controlPoint = controlPoint;
    }

    // endregion

    // region Getter

    /**
     * Returns the control point, which is in absolute coordinates.
     *
     * @param absoluteCurrentPoint the absolute current position of a path.
     *
     * @return a new {@link Point2D} describing the control point.
     *
     * @throws PathException if the command is relative but the provided point is null.
     */
    public Point2D getAbsoluteControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
        return addPoints(absoluteCurrentPoint, controlPoint);
    }

    // endregion

    // region Implement BezierCurveCommand

    /**
     * Returns the start control point, which is in absolute coordinates.
     *
     * @param absoluteCurrentPoint the absolute current position of a path.
     *
     * @return a new {@link Point2D} describing the start control point.
     */
    public Point2D getAbsoluteStartControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
        if (absoluteCurrentPoint == null) {
            throw new PathException("Can not get absolute start control point when current point is missing");
        }

        final Point2D distance;

        if (isAbsolute()) {
            distance = new Point2D(TWO_THIRD * (controlPoint.getX() - absoluteCurrentPoint.getX()),
                                   TWO_THIRD * (controlPoint.getY() - absoluteCurrentPoint.getY()));
        } else {
            distance = controlPoint.multiply(TWO_THIRD);
        }

        return absoluteCurrentPoint.add(distance);
    }

    /**
     * Returns the end control point, which is in absolute coordinates.
     *
     * @param absoluteCurrentPoint the absolute current position of a path.
     *
     * @return a new {@link Point2D} describing the end control point.
     */
    public Point2D getAbsoluteEndControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
        final Point2D absoluteEndPoint = getAbsoluteEndPoint(absoluteCurrentPoint);
        final Point2D absoluteControlPoint;

        if (isAbsolute()) {
            absoluteControlPoint = controlPoint;
        } else {
            absoluteControlPoint = absoluteCurrentPoint.add(controlPoint);
        }

        final Point2D distance = new Point2D(TWO_THIRD * (absoluteControlPoint.getX() - absoluteEndPoint.getX()),
                                             TWO_THIRD * (absoluteControlPoint.getY() - absoluteEndPoint.getY()));

        return absoluteEndPoint.add(distance);
    }

    // endregion
}