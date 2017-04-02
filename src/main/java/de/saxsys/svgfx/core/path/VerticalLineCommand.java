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

package de.saxsys.svgfx.core.path;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

/**
 * This represents a horizontal line command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public final class VerticalLineCommand extends PathCommand {

    // region Constants

    private static final char NAME = 'V';

    // endregion

    // region fields

    /**
     * Contains the x position which will be done by this command.
     */
    private final Double x;

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link String} that contains one numeric value which determine which position is moved to.
     * The given data may start and end with whitespaces an contain the {@link #NAME}.
     *
     * @param data the data to be used.
     *
     * @throws PathException if the string does not contain one numeric value.
     */
    VerticalLineCommand(final String data) throws PathException {
        super(NAME);
        try {
            x = Double.parseDouble(stripCommandName(data).trim());
        } catch (final NumberFormatException e) {
            throw new PathException(PathException.Reason.INVALID_NUMBER_FORMAT, String.format("Can not parse data: [%s] into a number", data), e);
        }
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return position.add(x, 0.0d);
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        final Point2D nextPosition = getNextPosition(position);

        return new Rectangle(Math.min(position.getX(), nextPosition.getX()),
                             position.getY(),
                             Math.abs(x),
                             0.0d);
    }

    // endregion
}
