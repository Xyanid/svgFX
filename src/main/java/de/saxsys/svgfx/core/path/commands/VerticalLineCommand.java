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

/**
 * This represents a horizontal line command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public final class VerticalLineCommand extends PathCommand {

    // region Constants

    /**
     * The absolute name of a vertical line command.
     */
    public static final char ABSOLUTE_NAME = 'V';

    /**
     * The relative name of a vertical line command.
     */
    public static final char RELATIVE_NAME = Character.toLowerCase(ABSOLUTE_NAME);

    // endregion

    // region fields

    /**
     * Contains the distance which will be travelled by this command.
     */
    private final Double distance;

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link String} that contains one numeric value which determine which position is moved to.
     * The given data may start and end with whitespaces.
     *
     * @param isAbsolute determines if this command is absolute or not.
     * @param distance   the amount of vertical space this command will travel.
     */
    VerticalLineCommand(final boolean isAbsolute, final double distance) {
        super(isAbsolute);
        this.distance = distance;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return position.add(0.0d, distance);
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        final Point2D nextPosition = getNextPosition(position);

        return new Rectangle(position.getX(),
                             Math.min(position.getY(), nextPosition.getY()),
                             0.0d,
                             Math.abs(distance));
    }

    // endregion
}