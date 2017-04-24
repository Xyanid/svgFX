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

import de.saxsys.svgfx.core.path.commands.CloseCommand;
import de.saxsys.svgfx.core.path.commands.CommandFactory;
import de.saxsys.svgfx.core.path.commands.CubicBezierCurveCommand;
import de.saxsys.svgfx.core.path.commands.HorizontalLineCommand;
import de.saxsys.svgfx.core.path.commands.LineCommand;
import de.saxsys.svgfx.core.path.commands.MoveCommand;
import de.saxsys.svgfx.core.path.commands.PathCommand;
import de.saxsys.svgfx.core.path.commands.QuadraticBezierCurveCommand;
import de.saxsys.svgfx.core.path.commands.ShortCubicBezierCurveCommand;
import de.saxsys.svgfx.core.path.commands.ShortQuadraticBezierCurveCommand;
import de.saxsys.svgfx.core.path.commands.VerticalLineCommand;
import de.saxsys.svgfx.core.utils.StringUtil;
import de.saxsys.svgfx.core.utils.Wrapper;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.Collection;


/**
 * @author Xyanid on 01.04.2017.
 */
public class PathCommandParser {

    // region

    // region Constants

    /**
     * Contains the command names know by this parser.
     */
    private static final Collection<Character> COMMAND_NAMES = Arrays.asList(
            MoveCommand.ABSOLUTE_NAME,
            MoveCommand.RELATIVE_NAME,
            LineCommand.ABSOLUTE_NAME,
            LineCommand.RELATIVE_NAME,
            VerticalLineCommand.ABSOLUTE_NAME,
            VerticalLineCommand.RELATIVE_NAME,
            HorizontalLineCommand.ABSOLUTE_NAME,
            HorizontalLineCommand.RELATIVE_NAME,
            CloseCommand.ABSOLUTE_NAME,
            CloseCommand.RELATIVE_NAME,
            CubicBezierCurveCommand.ABSOLUTE_NAME,
            CubicBezierCurveCommand.RELATIVE_NAME,
            ShortCubicBezierCurveCommand.ABSOLUTE_NAME,
            ShortCubicBezierCurveCommand.RELATIVE_NAME,
            QuadraticBezierCurveCommand.ABSOLUTE_NAME,
            QuadraticBezierCurveCommand.RELATIVE_NAME,
            ShortQuadraticBezierCurveCommand.ABSOLUTE_NAME,
            ShortQuadraticBezierCurveCommand.RELATIVE_NAME);

    // endregion

    // region Public

    public Rectangle getBoundingBox(final String path) throws PathException {

        final Wrapper<PathCommand> previousCommand = new Wrapper<>();
        final Wrapper<Point2D> startPosition = new Wrapper<>();
        final Wrapper<Point2D> previousPosition = new Wrapper<>();
        final Wrapper<Rectangle> previousBoundingBox = new Wrapper<>();

        StringUtil.splitByDelimiters(path, COMMAND_NAMES, (delimiter, data) -> {
            final PathCommand nextCommand = getCommandOrFail(delimiter,
                                                             path,
                                                             previousCommand.get().orElse(null),
                                                             startPosition.get().orElse(new Point2D(0.0d, 0.0d)));

            previousBoundingBox.set(getNextBoundingBox(nextCommand, previousBoundingBox, previousPosition));
            previousPosition.set(getNextPosition(nextCommand, previousPosition));

            if (!startPosition.get().isPresent() && previousPosition.get().isPresent()) {
                startPosition.set(previousPosition.get().get());
            }
        });

        return previousBoundingBox.get().orElseThrow(() -> new PathException(String.format("Could not get bounding box from data [%s]", path)));
    }

    // endregion

    // region Private

    private PathCommand getCommandOrFail(final Character delimiter,
                                         final String data,
                                         final PathCommand previousCommand,
                                         final Point2D startingPoint) throws PathException {
        if (MoveCommand.ABSOLUTE_NAME == delimiter || MoveCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createMoveCommand(delimiter, data);
        } else if (LineCommand.ABSOLUTE_NAME == delimiter || LineCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createLineCommand(delimiter, data);
        } else if (HorizontalLineCommand.ABSOLUTE_NAME == delimiter || HorizontalLineCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createHorizontalLineCommand(delimiter, data);
        } else if (VerticalLineCommand.ABSOLUTE_NAME == delimiter || VerticalLineCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createVerticalLineCommand(delimiter, data);
        } else if (CloseCommand.ABSOLUTE_NAME == delimiter || CloseCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createCloseCommand(delimiter, startingPoint);
        } else if (CubicBezierCurveCommand.ABSOLUTE_NAME == delimiter || CubicBezierCurveCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createCubicBezierCurveCommand(delimiter, data);
        } else if (ShortCubicBezierCurveCommand.ABSOLUTE_NAME == delimiter || ShortCubicBezierCurveCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(delimiter, data, previousCommand);
        } else if (QuadraticBezierCurveCommand.ABSOLUTE_NAME == delimiter || QuadraticBezierCurveCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(delimiter, data);
        } else if (ShortQuadraticBezierCurveCommand.ABSOLUTE_NAME == delimiter || ShortQuadraticBezierCurveCommand.RELATIVE_NAME == delimiter) {
            return CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(delimiter, data, previousCommand);
        } else {
            throw new PathException(String.format("Could not use delimiter: [%s] must be one of the know delimiters", delimiter));
        }
    }

    private Rectangle getNextBoundingBox(final PathCommand command, final Wrapper<Rectangle> previousBoundingBox, final Wrapper<Point2D> previousPosition) {
        return null;
    }

    private Point2D getNextPosition(final PathCommand command, final Wrapper<Point2D> previousPosition) {
        return null;
    }


    // endregion
}