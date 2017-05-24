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
public class CubicBezierCurveCommand extends BezierCurveCommand {

    // region Fields

    /**
     * The start control point of the curve, which may either be relative or absolute.
     */
    private final Point2D startControlPoint;

    /**
     * The end control point of the curve, which may either be relative or absolute.
     */
    private final Point2D endControlPoint;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is absolute or not.
     */
    CubicBezierCurveCommand(final boolean isAbsolute,
                            final Point2D startControlPoint,
                            final Point2D endControlPoint,
                            final Point2D endPoint) {
        super(isAbsolute, endPoint);
        this.startControlPoint = startControlPoint;
        this.endControlPoint = endControlPoint;
    }

    // endregion


    // region Implement BezierCurveCommand

    /**
     * Returns the {@link #startControlPoint} which is in absolute coordinates.
     *
     * @param absoluteCurrentPoint the absolute current position of a path.
     *
     * @return the {@link #startControlPoint} which is in absolute coordinates.
     */
    public Point2D getAbsoluteStartControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
        return addPoints(absoluteCurrentPoint, startControlPoint);
    }

    /**
     * Returns the {@link #endControlPoint} which is in absolute coordinates.
     *
     * @param absoluteCurrentPoint the absolute current position of a path.
     *
     * @return the {@link #endControlPoint} which is in absolute coordinates.
     */
    public Point2D getAbsoluteEndControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
        return addPoints(absoluteCurrentPoint, endControlPoint);
    }

    // endregion
}