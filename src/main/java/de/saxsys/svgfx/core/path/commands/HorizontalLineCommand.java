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
public final class HorizontalLineCommand extends PathCommand {

    // region Constants

    /**
     * The absolute name of a cubic bezier curve command.
     */
    public static final char ABSOLUTE_NAME = 'H';

    /**
     * The relative name of a cubic bezier curve command.
     */
    public static final char RELATIVE_NAME = Character.toLowerCase(ABSOLUTE_NAME);

    // endregion

    // region fields

    /**
     * Contains the horizontal distance which will be travelled by this command.
     */
    private final Double distance;

    // endregion

    // region Field

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if this command is absolute or not.
     * @param distance   the amount of horizontal space this command will travel.
     */
    HorizontalLineCommand(final boolean isAbsolute, final double distance) {
        super(isAbsolute);
        this.distance = distance;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return position.add(distance, 0.0d);
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        final Point2D nextPosition = getNextPosition(position);

        return new Rectangle(Math.min(position.getX(), nextPosition.getX()),
                             position.getY(),
                             Math.abs(distance),
                             0.0d);
    }

    // endregion
}