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

import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.interfaces.ThrowableBiFunction;
import de.saxsys.svgfx.core.path.CommandName;
import de.saxsys.svgfx.core.path.PathException;
import de.saxsys.svgfx.core.utils.StringUtil;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.saxsys.svgfx.core.definitions.Constants.COMMA;
import static de.saxsys.svgfx.core.definitions.Constants.WHITESPACE;
import static de.saxsys.svgfx.core.path.CommandName.CLOSE;
import static de.saxsys.svgfx.core.path.CommandName.CUBIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.HORIZONTAL_LINE;
import static de.saxsys.svgfx.core.path.CommandName.LINE;
import static de.saxsys.svgfx.core.path.CommandName.MOVE;
import static de.saxsys.svgfx.core.path.CommandName.QUADRATIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.SHORT_CUBIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.SHORT_QUADRATIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.VERTICAL_LINE;


/**
 * This class is responsible for creating instances of an {@link PathCommand}.
 *
 * @author Xyanid on 09.04.2017.
 */
public final class CommandFactory {

    // region Constants

    private static final String INVALID_NUMBER_FORMAT = "Could not parse given data [%s] into a number";
    private static final String INVALID_COMMAND_NAME = "Given command name [%s] can not be used to create a [%s] required either [%s] or [%s]";

    // endregion

    // region Public

    /**
     * Creates a new {@link PathCommand} based on the given information.
     *
     * @param delimiter                 the {@link Character} which determines the command.
     * @param data                      the data present for the command.
     * @param absolutePathStartingPoint the absolute start point of a path command.
     * @param absoluteCurrentPoint      the absolute end point of the previous command, which is also the starting position of the created comand.
     * @param previousCommand           the previous command.
     *
     * @return a new {@link PathCommand}.
     *
     * @throws PathException if any error occurs during the creation of a {@link PathCommand}.
     */
    public PathCommand createCommandOrFail(final Character delimiter,
                                           final String data,
                                           final Point2D absolutePathStartingPoint,
                                           final Point2D absoluteCurrentPoint,
                                           final PathCommand previousCommand) throws PathException {
        if (delimiter == null) {
            throw new PathException(String.format("Can not create command for data %s when no command delimiter was found", data));
        } else if (MOVE.isCommandName(delimiter)) {
            return createMoveCommand(delimiter, data);
        } else if (LINE.isCommandName(delimiter)) {
            return createLineCommand(delimiter, data);
        } else if (HORIZONTAL_LINE.isCommandName(delimiter)) {
            return createHorizontalLineCommand(delimiter, data);
        } else if (VERTICAL_LINE.isCommandName(delimiter)) {
            return createVerticalLineCommand(delimiter, data);
        } else if (CLOSE.isCommandName(delimiter)) {
            return createCloseCommand(delimiter, absolutePathStartingPoint);
        } else if (CUBIC_BEZIER_CURVE.isCommandName(delimiter)) {
            return createCubicBezierCurveCommand(delimiter, data);
        } else if (SHORT_CUBIC_BEZIER_CURVE.isCommandName(delimiter)) {
            return createShortCubicBezierCurveCommand(delimiter, data, absoluteCurrentPoint, previousCommand);
        } else if (QUADRATIC_BEZIER_CURVE.isCommandName(delimiter)) {
            return createQuadraticBezierCurveCommand(delimiter, data);
        } else if (SHORT_QUADRATIC_BEZIER_CURVE.isCommandName(delimiter)) {
            return createShortQuadraticBezierCurveCommand(delimiter, data, absoluteCurrentPoint, previousCommand);
        } else {
            throw new PathException(String.format("Could not use delimiter: [%s] must be one of the know delimiters", delimiter));
        }
    }

    /**
     * Creates a new {@link LineCommand} using the given data, which needs to be two numeric values separated by whitespaces or one comma
     * which determines which position is moved to.
     *
     * @param commandName the name of the command.
     * @param data        the data describing the command.
     *
     * @return a new {@link LineCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#MOVE}
     *                       or the data does not contain two numeric values separated be whitespaces or one comma.
     */
    public MoveCommand createMoveCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, MOVE, MoveCommand.class);

        final Point2D position = createPointOrFail(data.trim());

        return new MoveCommand(commandName == MOVE.getAbsoluteName(), position);
    }

    /**
     * Creates a new {@link LineCommand} using the given data, which needs to be two numeric values separated by whitespaces or one comma
     * which determines which position is moved to.
     *
     * @param commandName the name of the command.
     * @param data        the data describing the command.
     *
     * @return a new {@link LineCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#LINE}
     *                       or the data does not contain two numeric values separated be whitespaces or one comma.
     */
    public LineCommand createLineCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, LINE, LineCommand.class);

        final Point2D position = createPointOrFail(data.trim());

        return new LineCommand(commandName == LINE.getAbsoluteName(), position);
    }

    /**
     * Creates a new {@link LineCommand} using the given data, which need to contain one numeric value which determines which position is moved to.
     *
     * @param commandName the name of the command.
     * @param data        the data describing the command.
     *
     * @return a new {@link LineCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#HORIZONTAL_LINE}
     *                       or if the given data is not a number.
     */
    public HorizontalLineCommand createHorizontalLineCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, HORIZONTAL_LINE, HorizontalLineCommand.class);

        final double distance;
        try {
            distance = Double.parseDouble(data.trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format(INVALID_NUMBER_FORMAT, data), e);
        }

        return new HorizontalLineCommand(commandName == HORIZONTAL_LINE.getAbsoluteName(), distance);
    }

    /**
     * Creates a new {@link LineCommand} using the given data, which need to contain one numeric value which determines which position is moved to.
     *
     * @param commandName the name of the command.
     * @param data        the data describing the command.
     *
     * @return a new {@link LineCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#VERTICAL_LINE}
     *                       or the given data is not a number.
     */
    public VerticalLineCommand createVerticalLineCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, VERTICAL_LINE, VerticalLineCommand.class);

        final double distance;
        try {
            distance = Double.parseDouble(data.trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format("Could not parse given data [%s] into a number", data), e);
        }

        return new VerticalLineCommand(commandName == VERTICAL_LINE.getAbsoluteName(), distance);
    }

    /**
     * Creates a new {@link CloseCommand} using the given data, which needs to contain the command name.
     *
     * @param commandName the name of the command.
     * @param startPoint  the start position which this command will travel to.
     *
     * @return a new {@link CloseCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#CLOSE}.
     */
    public CloseCommand createCloseCommand(final char commandName, final Point2D startPoint) throws PathException {
        checkCommandNameOrFail(commandName, CLOSE, CloseCommand.class);

        return new CloseCommand(commandName == CLOSE.getAbsoluteName(), startPoint);
    }

    /**
     * Creates a new {@link BezierCurveCommand} using the given data, which needs to contains three points.
     *
     * @param commandName the name of the command.
     * @param data        the three points describing the curve.
     *
     * @return a new {@link BezierCurveCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#CUBIC_BEZIER_CURVE}
     *                       or the data does not represent three points.
     */
    public CubicBezierCurveCommand createCubicBezierCurveCommand(final char commandName,
                                                                 final String data) throws PathException {
        checkCommandNameOrFail(commandName, CUBIC_BEZIER_CURVE, CubicBezierCurveCommand.class);

        final List<Point2D> points = createPointsOrFail(data, 3);

        return new CubicBezierCurveCommand(commandName == CUBIC_BEZIER_CURVE.getAbsoluteName(),
                                           points.get(0),
                                           points.get(1),
                                           points.get(2));
    }

    /**
     * Creates a new {@link BezierCurveCommand} using the given data, which needs to contains three points.
     *
     * @param commandName          the name of the command.
     * @param data                 the three points describing the curve.
     * @param absoluteCurrentPoint the position of the start of the previous command, which is needed to determine the start position of the created command.
     * @param previousCommand      the previous {@link PathCommand} which needs to be a {@link BezierCurveCommand}.
     *
     * @return a new {@link BezierCurveCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#SHORT_CUBIC_BEZIER_CURVE}
     *                       or the data does not represent three points.
     */
    public CubicBezierCurveCommand createShortCubicBezierCurveCommand(final char commandName,
                                                                      final String data,
                                                                      final Point2D absoluteCurrentPoint,
                                                                      final PathCommand previousCommand) throws PathException {
        checkCommandNameOrFail(commandName, SHORT_CUBIC_BEZIER_CURVE, CubicBezierCurveCommand.class);

        final List<Point2D> points = createPointsOrFail(data, 2);
        final boolean isAbsolute = commandName == SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName();

        return new CubicBezierCurveCommand(isAbsolute,
                                           getAdjustedStartControlPoint(isAbsolute, absoluteCurrentPoint, previousCommand),
                                           points.get(0),
                                           points.get(1));
    }

    /**
     * Creates a new {@link BezierCurveCommand} using the given data, which needs to contains three points.
     *
     * @param commandName the name of the command.
     * @param data        the three points describing the curve.
     *
     * @return a new {@link BezierCurveCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#CUBIC_BEZIER_CURVE}
     *                       or the data does not represent 2 points.
     */
    public QuadraticBezierCurveCommand createQuadraticBezierCurveCommand(final char commandName,
                                                                         final String data) throws PathException {
        checkCommandNameOrFail(commandName, QUADRATIC_BEZIER_CURVE, QuadraticBezierCurveCommand.class);

        final List<Point2D> points = createPointsOrFail(data, 2);

        return new QuadraticBezierCurveCommand(commandName == QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                               points.get(0),
                                               points.get(1));
    }

    /**
     * Creates a new {@link BezierCurveCommand} using the given data, which needs to contains three points.
     *
     * @param commandName        the name of the command.
     * @param data               the three points describing the curve.
     * @param absoluteStartPoint the position of the start of the previous command, which is needed to determine the start position of the created command.
     * @param previousCommand    the previous {@link PathCommand} which needs to be a {@link BezierCurveCommand}.
     *
     * @return a new {@link BezierCurveCommand}.
     *
     * @throws PathException if the commandName is not absolute or relative {@link CommandName#SHORT_CUBIC_BEZIER_CURVE}
     *                       or the given command is not a {@link BezierCurveCommand}
     *                       or the data does not represent a point.
     */
    public QuadraticBezierCurveCommand createShortQuadraticBezierCurveCommand(final char commandName,
                                                                              final String data,
                                                                              final Point2D absoluteStartPoint,
                                                                              final PathCommand previousCommand) throws PathException {
        checkCommandNameOrFail(commandName, SHORT_QUADRATIC_BEZIER_CURVE, QuadraticBezierCurveCommand.class);

        final Point2D endPoint = createPointOrFail(data);
        final boolean isAbsolute = commandName == SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName();
        final Point2D adjustedControlPoint = getAdjustedControlPoint(isAbsolute, absoluteStartPoint, previousCommand);

        return new QuadraticBezierCurveCommand(isAbsolute,
                                               adjustedControlPoint,
                                               endPoint);
    }

    // endregion

    // region Private

    private void checkCommandNameOrFail(char name, final CommandName commandName, final Class commandClass) throws PathException {
        if (!commandName.isCommandName(name)) {
            throw new PathException(String.format(INVALID_COMMAND_NAME,
                                                  name,
                                                  commandClass.getSimpleName(),
                                                  commandName.getAbsoluteName(),
                                                  commandName.getRelativeName()));
        }
    }

    private List<Point2D> createPointsOrFail(final String data, final int numberOfPoints) throws PathException {
        final List<String> values = StringUtil.splitByDelimiters(data, Arrays.asList(Constants.WHITESPACE, Constants.COMMA), StringUtil::isNotNullOrEmptyAfterTrim);

        if (values.size() % 2 != 0) {
            throw new PathException(String.format("Data [%s] must have an even number of points but has [%d]", data, values.size()));
        }

        final List<Point2D> points = new ArrayList<>(0);

        for (int i = 0; i < values.size(); i += 2) {
            final Point2D point = createPointOrFail(values.get(i), values.get(i + 1));
            points.add(point);
        }

        if (points.size() != numberOfPoints) {
            throw new PathException(String.format("Could not create cubic bezier curve command from data [%s] cause the number of points is [%d] but needs to be [%d]",
                                                  data,
                                                  points.size(),
                                                  numberOfPoints));
        }

        return points;
    }

    private Point2D createPointOrFail(final String data) throws PathException {
        if (StringUtil.isNullOrEmpty(data)) {
            throw new PathException(String.format("Given data: [%s] can not be used to create a move command", data));
        }

        final List<String> split;
        try {
            split = StringUtil.splitByDelimiters(data, Arrays.asList(WHITESPACE, COMMA), StringUtil::isNotNullOrEmptyAfterTrim);
        } catch (final Exception e) {
            throw new PathException(String.format("Given data: [%s] can not be used to create a move command", data), e);
        }

        if (split.size() != 2) {
            throw new PathException(String.format("Given data: [%s] can not be used to create a move command", data));
        }

        return createPointOrFail(split.get(0), split.get(1));
    }

    private Point2D createPointOrFail(final String xValue, final String yValue) throws PathException {
        final Double x;
        final Double y;

        try {
            x = Double.parseDouble(xValue.trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format("Given data: [%s] can not be used to create a move command", xValue), e);
        }

        try {
            y = Double.parseDouble(yValue.trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format("Given data: [%s] can not be used to create a move command", yValue), e);
        }

        return new Point2D(x, y);
    }

    private Point2D getAdjustedStartControlPoint(final boolean isAbsolute, final Point2D absoluteCurrentPoint, final PathCommand command) throws PathException {
        return getAdjustedPoint(isAbsolute,
                                absoluteCurrentPoint,
                                command,
                                CubicBezierCurveCommand.class,
                                CubicBezierCurveCommand::getAbsoluteEndControlPoint);

    }

    private Point2D getAdjustedControlPoint(final boolean isAbsolute, final Point2D absoluteCurrentPoint, final PathCommand command) throws PathException {
        return getAdjustedPoint(isAbsolute,
                                absoluteCurrentPoint,
                                command,
                                QuadraticBezierCurveCommand.class,
                                QuadraticBezierCurveCommand::getAbsoluteControlPoint);
    }

    private <T extends BezierCurveCommand> Point2D getAdjustedPoint(final boolean isAbsolute,
                                                                    final Point2D absoluteCurrentPoint,
                                                                    final PathCommand command,
                                                                    final Class<T> clazz,
                                                                    final ThrowableBiFunction<T, Point2D, Point2D, PathException> controlPointSupplier) throws PathException {

        final Point2D negativeDistance;
        final Point2D startPoint;

        if (command != null && clazz.isAssignableFrom(command.getClass())) {

            final T bezierCurveCommand = clazz.cast(command);
            final Point2D absoluteEndPoint = bezierCurveCommand.getAbsoluteEndPoint(absoluteCurrentPoint);
            final Point2D absoluteControlPoint = controlPointSupplier.apply(bezierCurveCommand, absoluteCurrentPoint);

            negativeDistance = absoluteControlPoint.subtract(absoluteEndPoint).multiply(-1);
            startPoint = absoluteEndPoint;
        } else {
            negativeDistance = Point2D.ZERO;
            startPoint = absoluteCurrentPoint;
        }

        if (isAbsolute) {
            return startPoint.add(negativeDistance);
        } else {
            return negativeDistance;
        }
    }

    // endregion
}