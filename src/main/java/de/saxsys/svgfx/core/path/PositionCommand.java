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

import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.utils.StringUtil;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.Collections;
import java.util.List;

import static de.saxsys.svgfx.core.definitions.Constants.WHITESPACE;

/**
 * This represents a position command in a svg path which will either be a {@link MoveCommand} or a {@link LineCommand}. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class PositionCommand extends PathCommand {

    // region fields

    /**
     * Contains the position which will be done by this command.
     */
    private final Point2D position;

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link String} that contains two numeric values separated by a whitespaces which determine which position is moved to.
     * The given data may start with whitespaces and may have as many whitespaces as desired between the two numeric values.
     *
     * @param name the name of the command.
     * @param data the data to be used.
     *
     * @throws PathException if the string does not contain two numeric values separated by a whitespaces.
     */
    PositionCommand(final char name, final String data) throws PathException {
        super(name);
        position = consumeData(stripCommandName(data));
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return position.add(this.position);
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        final Point2D nextPosition = getNextPosition(position);

        return new Rectangle(Math.min(position.getX(), nextPosition.getX()),
                             Math.min(position.getY(), nextPosition.getY()),
                             Math.abs(this.position.getX()),
                             Math.abs(this.position.getY()));
    }

    // endregion

    // region Private

    /**
     * Parses the given data as a tuple of two numeric values separated by whitespaces and returns their values in a {@link Point2D}.
     *
     * @param data the data to consume.
     *
     * @return a new {@link Point2D} containing the values.
     *
     * @throws PathException if the given data is null, the amount of values is not exactly two or the values are not numerical.
     */
    private Point2D consumeData(final String data) throws PathException {
        if (StringUtil.isNullOrEmpty(data)) {
            throw new PathException(PathException.Reason.NULL_ARGUMENT,
                                    String.format("Given data: [%s] can not be used to create a move command", data));
        }

        final List<String> split;
        try {
            split = StringUtil.splitByDelimiters(data, Collections.singletonList(WHITESPACE), StringUtil::isNotNullOrEmptyAfterTrim);
        } catch (final SVGException e) {
            throw new PathException(PathException.Reason.FAILED_TO_SPLIT_DATA,
                                    String.format("Given data: [%s] can not be used to create a move command", data),
                                    e);
        }

        if (split.size() != 2) {
            throw new PathException(PathException.Reason.INVALID_PATH_COMMAND_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH,
                                    String.format("Given data: [%s] can not be used to create a move command", data));
        }

        final Double x;
        final Double y;

        try {
            x = Double.parseDouble(split.get(0).trim());
            y = Double.parseDouble(split.get(1).trim());
        } catch (final NumberFormatException e) {
            throw new PathException(PathException.Reason.INVALID_NUMBER_FORMAT,
                                    String.format("Given data: [%s] can not be used to create a move command", data),
                                    e);
        }

        return new Point2D(x, y);
    }

    // endregion

}
