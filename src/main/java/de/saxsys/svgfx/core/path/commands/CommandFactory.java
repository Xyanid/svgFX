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
import de.saxsys.svgfx.core.utils.StringUtil;
import javafx.geometry.Point2D;

import java.util.Arrays;
import java.util.List;

import static de.saxsys.svgfx.core.definitions.Constants.COMMA;
import static de.saxsys.svgfx.core.definitions.Constants.WHITESPACE;


/**
 * This class is responsible for creating instances of an {@link PathCommand}.
 *
 * @author Xyanid on 09.04.2017.
 */
public final class CommandFactory {

    // region Constants

    public static final CommandFactory INSTANCE = new CommandFactory();
    public static final String INVALID_COMMAND_NAME = "Given command name [%s] can not be used to create a [%s] required either [%s] or [%s]";
    public static final String INVALID_NUMBER_FORMAT = "Could not parse given data [%s] into a number";
    public static final String INVALID_PREVIOUS_COMMAND = "Expected [%s] to be the previous command but was [%s]";

    // endregion

    // region Constructor

    private CommandFactory() {}

    // endregion

    // region Public

    /**
     * Creates a new {@link MoveCommand} using the given data, which needs to be two numeric values separated by whitespaces or one comma
     * which determines which position is moved to.
     *
     * @param commandName the name of the command.
     * @param data        the data describing the command.
     *
     * @return a new {@link MoveCommand}.
     *
     * @throws PathException if the commandName is not {@link MoveCommand#ABSOLUTE_NAME} or {@link MoveCommand#RELATIVE_NAME}
     *                       or the data does not contain two numeric values separated be whitespaces or one comma.
     */
    public MoveCommand createMoveCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, MoveCommand.ABSOLUTE_NAME, MoveCommand.RELATIVE_NAME, MoveCommand.class);

        final Point2D position = createPoint(data.trim());

        return new MoveCommand(commandName == MoveCommand.ABSOLUTE_NAME, position);
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
     * @throws PathException if the commandName is not {@link LineCommand#ABSOLUTE_NAME} or {@link LineCommand#RELATIVE_NAME}
     *                       or the data does not contain two numeric values separated be whitespaces or one comma.
     */
    public LineCommand createLineCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, LineCommand.ABSOLUTE_NAME, LineCommand.RELATIVE_NAME, LineCommand.class);

        final Point2D position = createPoint(data.trim());

        return new LineCommand(commandName == LineCommand.ABSOLUTE_NAME, position);
    }

    /**
     * Creates a new {@link HorizontalLineCommand} using the given data, which need to contain the command name followed by one numeric value
     * which determine which position is moved to.
     *
     * @param data the data describing the command.
     *
     * @return a new {@link HorizontalLineCommand}.
     *
     * @throws PathException if the commandName is not {@link HorizontalLineCommand#ABSOLUTE_NAME} or {@link HorizontalLineCommand#RELATIVE_NAME}
     *                       or if the given data is not a number.
     */
    public HorizontalLineCommand createHorizontalLineCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, HorizontalLineCommand.ABSOLUTE_NAME, HorizontalLineCommand.RELATIVE_NAME, HorizontalLineCommand.class);

        final double distance;
        try {
            distance = Double.parseDouble(data.trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format(INVALID_NUMBER_FORMAT, data), e);
        }

        return new HorizontalLineCommand(commandName == HorizontalLineCommand.ABSOLUTE_NAME, distance);
    }

    /**
     * Creates a new {@link VerticalLineCommand} using the given data, which need to contain the command name followed by one numeric value
     * which determine which position is moved to.
     *
     * @param data the data describing the command.
     *
     * @return a new {@link VerticalLineCommand}.
     *
     * @throws PathException if the commandName is not {@link VerticalLineCommand#ABSOLUTE_NAME} or {@link VerticalLineCommand#RELATIVE_NAME}
     *                       or the given data is not a number.
     */
    public VerticalLineCommand createVerticalLineCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName, VerticalLineCommand.ABSOLUTE_NAME, VerticalLineCommand.RELATIVE_NAME, VerticalLineCommand.class);

        final double distance;
        try {
            distance = Double.parseDouble(data.trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format("Could not parse given data [%s] into a number", data), e);
        }

        return new VerticalLineCommand(commandName == VerticalLineCommand.ABSOLUTE_NAME, distance);
    }

    /**
     * Creates a new {@link CloseCommand} using the given data, which needs to contain the command name.
     *
     * @param commandName the name of the command.
     * @param startPoint  the start position which this command will travel to.
     *
     * @return a new {@link CloseCommand}.
     *
     * @throws PathException if the commandName is not {@link CloseCommand#ABSOLUTE_NAME} or {@link CloseCommand#RELATIVE_NAME}.
     */
    public CloseCommand createCloseCommand(final char commandName, final Point2D startPoint) throws PathException {
        checkCommandNameOrFail(commandName, CloseCommand.ABSOLUTE_NAME, CloseCommand.RELATIVE_NAME, CloseCommand.class);

        return new CloseCommand(commandName == CloseCommand.ABSOLUTE_NAME, startPoint);
    }

    public CubicBezierCurveCommand createCubicBezierCurveCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName,
                               CubicBezierCurveCommand.ABSOLUTE_NAME,
                               CubicBezierCurveCommand.RELATIVE_NAME,
                               CubicBezierCurveCommand.class);

        return new CubicBezierCurveCommand(commandName == CubicBezierCurveCommand.ABSOLUTE_NAME);
    }

    public ShortCubicBezierCurveCommand createShortCubicBezierCurveCommand(final char commandName, final String data, final PathCommand previousCommand) throws PathException {
        checkCommandNameOrFail(commandName,
                               ShortCubicBezierCurveCommand.ABSOLUTE_NAME,
                               ShortCubicBezierCurveCommand.RELATIVE_NAME,
                               ShortCubicBezierCurveCommand.class);

        checkPreviousCommandOrFail(previousCommand, CubicBezierCurveCommand.class);

        return new ShortCubicBezierCurveCommand(commandName == ShortCubicBezierCurveCommand.ABSOLUTE_NAME);
    }


    public QuadraticBezierCurveCommand createQuadraticBezierCurveCommand(final char commandName, final String data) throws PathException {
        checkCommandNameOrFail(commandName,
                               QuadraticBezierCurveCommand.ABSOLUTE_NAME,
                               QuadraticBezierCurveCommand.RELATIVE_NAME,
                               QuadraticBezierCurveCommand.class);

        return new QuadraticBezierCurveCommand(commandName == QuadraticBezierCurveCommand.ABSOLUTE_NAME);
    }

    public ShortQuadraticBezierCurveCommand createShortQuadraticBezierCurveCommand(final char commandName,
                                                                                   final String data,
                                                                                   final PathCommand previousCommand) throws PathException {
        checkCommandNameOrFail(commandName,
                               ShortQuadraticBezierCurveCommand.ABSOLUTE_NAME,
                               ShortQuadraticBezierCurveCommand.RELATIVE_NAME,
                               ShortQuadraticBezierCurveCommand.class);

        checkPreviousCommandOrFail(previousCommand, QuadraticBezierCurveCommand.class);

        return new ShortQuadraticBezierCurveCommand(commandName == ShortQuadraticBezierCurveCommand.ABSOLUTE_NAME);
    }

    // endregion

    // region Private

    private void checkCommandNameOrFail(char commandName, final char absoluteName, final char relativeName, final Class commandClass) throws PathException {
        if (commandName != absoluteName && commandName != relativeName) {
            throw new PathException(String.format(INVALID_COMMAND_NAME,
                                                  commandName,
                                                  commandClass.getSimpleName(),
                                                  absoluteName,
                                                  relativeName));
        }
    }

    private <T extends PathCommand> void checkPreviousCommandOrFail(final PathCommand previousCommand, final Class<T> expectedCommand) throws PathException {
        if (!expectedCommand.isAssignableFrom(previousCommand.getClass())) {
            throw new PathException(String.format(INVALID_PREVIOUS_COMMAND, expectedCommand.getSimpleName(), previousCommand.getClass().getSimpleName()));
        }
    }

    //    /**
    //     * Takes the given {@link String} and returns only the elements beyond the first occurrence of the {@code absoluteName} or {@code #relativeName}.
    //     *
    //     * @param data         the command to be parsed.
    //     * @param absoluteName the absoluteName of the command.
    //     * @param relativeName the relative name of the command.
    //     *
    //     * @return a new {@link Pair} which has a {@link String} containing the data beyond the first occurrence of the {@code absoluteName} or {@code relativeName},
    //     * as well as {@link Boolean} determining if the command was absolute or relative.
    //     *
    //     * @throws PathException if there is no absolute or relative character found.
    //     */
    //    private Pair<Boolean, String> analyzeCommand(final String data, final char absoluteName, final char relativeName) throws PathException {
    //        for (int i = 0; i < data.length(); i++) {
    //            final char character = data.charAt(i);
    //            if (absoluteName == character || relativeName == character) {
    //                return new Pair<>(absoluteName == character, data.substring(i + 1, data.length()).trim());
    //            }
    //        }
    //
    //        throw new PathException(String.format("Given data [%s] does not contain either %s or %s", data, absoluteName, relativeName));
    //    }

    /**
     * Parses the given data as a tuple of two numeric values separated by whitespaces and returns their values in a {@link Point2D}.
     *
     * @param data the data to consumeOrFail.
     *
     * @return a new {@link Point2D} containing the values.
     *
     * @throws PathException if the given data is null, the amount of values is not exactly two or the values are not numerical.
     */
    private Point2D createPoint(final String data) throws PathException {
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

        final Double x;
        final Double y;

        try {
            x = Double.parseDouble(split.get(0).trim());
            y = Double.parseDouble(split.get(1).trim());
        } catch (final NumberFormatException e) {
            throw new PathException(String.format("Given data: [%s] can not be used to create a move command", data), e);
        }

        return new Point2D(x, y);
    }

    // endregion
}