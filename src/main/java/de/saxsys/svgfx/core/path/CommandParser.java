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

import de.saxsys.svgfx.core.path.commands.CommandFactory;
import de.saxsys.svgfx.core.path.commands.MoveCommand;
import de.saxsys.svgfx.core.path.commands.PathCommand;
import de.saxsys.svgfx.core.utils.StringUtil;
import de.saxsys.svgfx.core.utils.Wrapper;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static de.saxsys.svgfx.core.utils.StringUtil.isNullOrEmpty;


/**
 * @author Xyanid on 01.04.2017.
 */
public class CommandParser {

    // region Constants

    /**
     * Contains the command names know by this parser.
     */
    private static final Collection<Character> COMMAND_NAMES;

    static {
        final Collection<Character> values = new ArrayList<>(CommandName.values().length * 2);
        for (final CommandName commandName : CommandName.values()) {
            values.addAll(commandName.getNames());
        }
        COMMAND_NAMES = values;
    }

    // endregion

    // region Public

    /**
     * Returns the bounding box that would result from the given path command.
     *
     * @param path a {@link String} which represents an svg path command chain.
     *
     * @return a new {@link Rectangle} determining the bounding box.
     *
     * @throws PathException if any error occurrs during the creation of the bounding box.
     */
    public Rectangle getBoundingBox(final String path) throws PathException {

        if (isNullOrEmpty(path)) {
            throw new PathException("Can not get bounding box from empty path command");
        }

        final Wrapper<Point2D> startPosition = new Wrapper<>();
        final Wrapper<Point2D> previousPosition = new Wrapper<>();
        final Wrapper<PathCommand> previousCommand = new Wrapper<>();
        final Wrapper<Rectangle> previousBoundingBox = new Wrapper<>();

        StringUtil.splitByDelimiters(path, COMMAND_NAMES, (delimiter, data) -> {
            final PathCommand nextCommand = CommandFactory.INSTANCE.createCommandOrFail(delimiter,
                                                                                        data,
                                                                                        previousCommand.get(),
                                                                                        startPosition.getOptional().orElse(Point2D.ZERO));

            final Optional<Rectangle> nextBoundingBox = getNextBoundingBox(nextCommand,
                                                                           previousPosition.getOptional().orElse(Point2D.ZERO),
                                                                           previousCommand,
                                                                           previousBoundingBox);
            nextBoundingBox.ifPresent(previousBoundingBox::set);

            if (!startPosition.getOptional().isPresent() && previousPosition.getOptional().isPresent()
                && !MoveCommand.class.isAssignableFrom(nextCommand.getClass())) {
                startPosition.set(previousPosition.get());
            }

            previousPosition.set(nextCommand.getNextPosition(previousPosition.get()));
        });

        return previousBoundingBox.getOptional().orElseThrow(() -> new PathException(String.format("Could not get bounding box from data [%s]", path)));
    }

    // endregion

    // region Private

    private Optional<Rectangle> getNextBoundingBox(final PathCommand currentCommand,
                                                   final Point2D previousPoint,
                                                   final Wrapper<PathCommand> previousCommand,
                                                   final Wrapper<Rectangle> previousBoundingBox) throws PathException {
        final Optional<Rectangle> result;

        final Optional<Rectangle> commandBoundingBox = currentCommand.getBoundingBox(previousPoint);
        if (commandBoundingBox.isPresent() && previousBoundingBox.getOptional().isPresent()) {
            result = Optional.of(combineBoundingBoxes(commandBoundingBox.get(), previousBoundingBox.get()));
        } else if (commandBoundingBox.isPresent()) {
            result = commandBoundingBox;
        } else if (previousBoundingBox.getOptional().isPresent()) {
            result = previousBoundingBox.getOptional();
        } else {
            result = Optional.empty();
        }

        return result;
    }


    private Rectangle combineBoundingBoxes(final Rectangle first, final Rectangle second) {
        final double minX = Math.min(first.getX(), second.getX());
        final double minY = Math.min(first.getY(), second.getY());
        final double maxX = Math.max(first.getX() + first.getWidth(), second.getX() + second.getWidth());
        final double maxY = Math.max(first.getY() + first.getHeight(), second.getY() + second.getHeight());

        return new Rectangle(minX, minY, Math.abs(minX - maxX), Math.abs(minY - maxY));
    }

    // endregion
}