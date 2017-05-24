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

import java.util.Optional;

/**
 * This represents a position command in a svg path which will either. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public class HorizontalLineCommand extends PathCommand {

    // region Fields

    /**
     * Determines either the distance to travel if the command is relative or the x coordinate if the command is absolte.
     */
    private final double distance;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is an absolute command or not.
     * @param distance   the distance to be used.
     */
    HorizontalLineCommand(final boolean isAbsolute, final double distance) {
        super(isAbsolute);
        this.distance = distance;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public Point2D getAbsoluteEndPoint(final Point2D absoluteCurrentPoint) throws PathException {
        if (absoluteCurrentPoint == null) {
            throw new PathException("May not create a new position when there is no position to relate to");
        }

        if (isAbsolute()) {
            return new Point2D(distance, absoluteCurrentPoint.getY());
        } else {
            return new Point2D(absoluteCurrentPoint.getX() + distance, absoluteCurrentPoint.getY());
        }
    }

    @Override
    public Optional<Rectangle> getBoundingBox(final Point2D absoluteCurrentPoint) throws PathException {
        final Point2D nextPosition = getAbsoluteEndPoint(absoluteCurrentPoint);

        return Optional.of(new Rectangle(getMinX(absoluteCurrentPoint, nextPosition),
                                         absoluteCurrentPoint.getY(),
                                         isAbsolute() ? getDistanceX(nextPosition, absoluteCurrentPoint) : Math.abs(distance),
                                         0.0d));
    }

    // endregion
}