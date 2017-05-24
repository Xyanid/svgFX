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
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static de.saxsys.svgfx.core.path.CommandName.CUBIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.HORIZONTAL_LINE;
import static de.saxsys.svgfx.core.path.CommandName.LINE;
import static de.saxsys.svgfx.core.path.CommandName.MOVE;
import static de.saxsys.svgfx.core.path.CommandName.QUADRATIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.SHORT_CUBIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.SHORT_QUADRATIC_BEZIER_CURVE;
import static de.saxsys.svgfx.core.path.CommandName.VERTICAL_LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 23.04.2017.
 */
public class CommandFactoryTest {

    // region Tests

    @Test (expected = PathException.class)
    public void aMoveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createMoveCommand('X', "10 10");
    }

    @Test
    public void aMoveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createMoveCommand('M', "10 10");
        CommandFactory.INSTANCE.createMoveCommand('m', "10 10");
    }

    @Test
    public void aMoveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10 Z",
                                                       "10 , Z",
                                                       "Z, 10");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createMoveCommand(MOVE.getAbsoluteName(), data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aMoveCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10 10",
                                                     " 10 10",
                                                     " 10 10 ",
                                                     "   10   10   ",
                                                     "10,10",
                                                     " 10,10",
                                                     " 10,10",
                                                     " 10,10 ",
                                                     "   10  ,  10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createMoveCommand(MOVE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createLineCommand('X', "10 10");
    }

    @Test
    public void aLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createLineCommand('L', "10 10");
        CommandFactory.INSTANCE.createLineCommand('l', "10 10");
    }

    @Test
    public void aLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10 Z",
                                                       "10 , Z",
                                                       "Z, 10");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createLineCommand(LINE.getRelativeName(), data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aLineCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10 10",
                                                     " 10 10",
                                                     " 10 10 ",
                                                     "   10   10   ",
                                                     "10,10",
                                                     " 10,10",
                                                     " 10,10",
                                                     " 10,10 ",
                                                     "   10  ,  10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createLineCommand(LINE.getRelativeName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aVerticalLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createVerticalLineCommand('X', "10");
    }

    @Test
    public void aVerticalLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createVerticalLineCommand('V', "10");
        CommandFactory.INSTANCE.createVerticalLineCommand('v', "10");
    }

    @Test
    public void aVerticalLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10 10",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createVerticalLineCommand(VERTICAL_LINE.getAbsoluteName(), data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aVerticalLineCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10",
                                                     " 10",
                                                     " 10 ",
                                                     "   10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createVerticalLineCommand(VERTICAL_LINE.getAbsoluteName(), data);
            } catch (final PathException ignore) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aHorizontalLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createHorizontalLineCommand('X', "10");
    }

    @Test
    public void aHorizontalLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createHorizontalLineCommand('H', "10");
        CommandFactory.INSTANCE.createHorizontalLineCommand('h', "10");
    }

    @Test
    public void aHorizontalLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10 10",
                                                       "10Z",
                                                       "10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createHorizontalLineCommand(HORIZONTAL_LINE.getAbsoluteName(), data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aHorizontalLineCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10",
                                                     " 10",
                                                     " 10 ",
                                                     "   10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createHorizontalLineCommand(HORIZONTAL_LINE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createCubicBezierCurveCommand('X', "10");
    }

    @Test
    public void aCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createCubicBezierCurveCommand('C', "10, 10 10, 10 10, 10");
        CommandFactory.INSTANCE.createCubicBezierCurveCommand('c', "10, 10 10, 10 10, 10");
    }

    @Test
    public void aCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10",
                                                       "10 10 10",
                                                       "10 10 10 10",
                                                       "10 10 10 10 10",
                                                       "10 10 10 10 10 10Z",
                                                       "10 10 10 10 10 10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aCubicBezierCurveCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10 10 10 10 10 10",
                                                     " 10 10 10 10 10 10",
                                                     " 10 10 10 10 10 10 ",
                                                     "   10 10 10 10 10 10   ",
                                                     "10,10 10,10 10,10",
                                                     " 10,10 10,10 10,10",
                                                     " 10,10 10,10 10,10 ",
                                                     "   10,10 10,10 10,10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aShortCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createShortCubicBezierCurveCommand('X', "10", Point2D.ZERO, null);
    }

    @Test
    public void aShortCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createShortCubicBezierCurveCommand('S', "10 10 10 10", Point2D.ZERO, null);
        CommandFactory.INSTANCE.createShortCubicBezierCurveCommand('s', "10 10 10 10", Point2D.ZERO, null);
    }

    @Test
    public void aShortCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() throws PathException {
        final PathCommand command = CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "10, 10 10, 10 10, 10");

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10",
                                                       "10 10 10",
                                                       "10 10 10Z",
                                                       "10 10 10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(), data, Point2D.ZERO, command);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aShortCubicBezierCurveCommandCanBeCreatedFromAllPossibleValidDataStrings() throws PathException {
        final PathCommand command = CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "10, 10 10, 10 10, 10");

        final List<String> validData = Arrays.asList("10 10 10 10",
                                                     " 10 10 10 10",
                                                     " 10 10 10 10 ",
                                                     "   10 10 10 10   ",
                                                     "10,10 10,10",
                                                     " 10,10 10,10",
                                                     " 10,10 10,10 ",
                                                     "   10,10 10,10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(), data, Point2D.ZERO, command);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test
    public void aShortCubicBezierCurveCommandWillHaveItStartControlPointSetToTheCurrentPointIfThePreviousCommandWasNoCubicBezierCurveCommand()
            throws PathException, NoSuchFieldException, IllegalAccessException {

        final CubicBezierCurveCommand cut = CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                       "10 10 10 10",
                                                                                                       new Point2D(10.0d, 10.0d),
                                                                                                       null);

        assertEquals(new Point2D(10.0d, 10.0d), getStartControlPoint(cut));

        final CubicBezierCurveCommand cut1 = CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getRelativeName(),
                                                                                                        "10 10 10 10",
                                                                                                        new Point2D(10.0d, 10.0d),
                                                                                                        null);

        assertEquals(new Point2D(0.0d, 0.0d), getStartControlPoint(cut1));
    }

    @Test
    public void aShortCubicBezierCurveCommandWillHaveItsStartControlPointBeBasedOneThePreviousEndControlPointOfThePreviousCommand() throws PathException, NoSuchFieldException, IllegalAccessException {
        final Point2D absoluteCurrentPoint = new Point2D(10.0d, 10.0d);

        final CubicBezierCurveCommand command = CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "20,20 30,20 40,10");
        final CubicBezierCurveCommand cut = CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(), "60,20 70,10", absoluteCurrentPoint, command);

        assertEquals(new Point2D(50.0d, 0.0d), getStartControlPoint(cut));

        final CubicBezierCurveCommand command1 = CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getRelativeName(), "10,-10 20,-10 30,0");
        final CubicBezierCurveCommand cut1 = CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(), "60,20 70,10", absoluteCurrentPoint, command1);

        assertEquals(new Point2D(50.0d, 20.0d), getStartControlPoint(cut1));

        final CubicBezierCurveCommand command2 = CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "20,0 30,0 40,10");
        final CubicBezierCurveCommand cut2 = CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getRelativeName(), "50,10 60,00", absoluteCurrentPoint, command2);

        assertEquals(new Point2D(10.0d, 10.0d), getStartControlPoint(cut2));

        final CubicBezierCurveCommand command3 = CommandFactory.INSTANCE.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getRelativeName(), "10,-10 20,-10 30,0");
        final CubicBezierCurveCommand cut3 = CommandFactory.INSTANCE.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getRelativeName(), "50,10 60,00", absoluteCurrentPoint, command3);

        assertEquals(new Point2D(10.0d, 10.0d), getStartControlPoint(cut3));
    }


    @Test (expected = PathException.class)
    public void aQuadraticBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createQuadraticBezierCurveCommand('X', "10");
    }

    @Test
    public void aQuadraticCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createQuadraticBezierCurveCommand('Q', "10 10 10 10");
        CommandFactory.INSTANCE.createQuadraticBezierCurveCommand('q', "10 10 10 10");
    }

    @Test
    public void aQuadraticCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() throws PathException {
        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10",
                                                       "10 10 10",
                                                       "10 10 10Z",
                                                       "10 10 10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(), data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aQuadraticCubicBezierCurveCommandCanBeCreatedFromAllPossibleValidDataStrings() throws PathException {
        final List<String> validData = Arrays.asList("10 10 10 10",
                                                     " 10 10 10 10",
                                                     " 10 10 10 10 ",
                                                     "   10 10 10 10   ",
                                                     "10,10 10,10",
                                                     " 10,10 10,10",
                                                     " 10,10 10,10 ",
                                                     "   10,10 10,10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aShortQuadraticBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand('X', "10", Point2D.ZERO, null);
    }

    @Test
    public void aShortQuadraticCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand('T', "10 10", Point2D.ZERO, null);
        CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand('t', "10 10", Point2D.ZERO, null);
    }

    @Test
    public void aShortQuadraticBezierCurveCommandWillHaveItControlPointSetToTheCurrentPointIfThePreviousCommandWasNoCubicBezierCurveCommand()
            throws PathException, NoSuchFieldException, IllegalAccessException {

        final QuadraticBezierCurveCommand cut = CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                               "20 20",
                                                                                                               new Point2D(10.0d, 10.0d),
                                                                                                               null);

        assertEquals(new Point2D(10.0d, 10.0d), getControlPoint(cut));

        final QuadraticBezierCurveCommand cut1 = CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                                "20 20",
                                                                                                                new Point2D(10.0d, 10.0d),
                                                                                                                null);

        assertEquals(new Point2D(0.0d, 0.0d), getControlPoint(cut1));
    }

    @Test
    public void aShortQuadraticBezierCurveCommandWillHaveItsStartControlPointBeBasedOneThePreviousEndControlPointOfThePreviousCommand()
            throws PathException, NoSuchFieldException, IllegalAccessException {
        final Point2D absoluteCurrentPoint = new Point2D(10.0d, 20.0d);

        final QuadraticBezierCurveCommand command = CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                              "100,40 130,20");
        final QuadraticBezierCurveCommand cut = CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                               "200,30",
                                                                                                               absoluteCurrentPoint,
                                                                                                               command);

        assertEquals(new Point2D(160.0d, 0.0d), getControlPoint(cut));

        final QuadraticBezierCurveCommand command1 = CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                               "90,20 120,0");
        final QuadraticBezierCurveCommand cut1 = CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                                "200,30",
                                                                                                                absoluteCurrentPoint,
                                                                                                                command1);

        assertEquals(new Point2D(160.0d, 0.0d), getControlPoint(cut1));

        final QuadraticBezierCurveCommand command2 = CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                               "100,40 130,20");
        final QuadraticBezierCurveCommand cut2 = CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                                "200,30",
                                                                                                                absoluteCurrentPoint,
                                                                                                                command2);

        assertEquals(new Point2D(30.0d, -20.0d), getControlPoint(cut2));

        final QuadraticBezierCurveCommand command3 = CommandFactory.INSTANCE.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                               "90,20 120,0");
        final QuadraticBezierCurveCommand cut3 = CommandFactory.INSTANCE.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                                "200,30",
                                                                                                                absoluteCurrentPoint,
                                                                                                                command3);

        assertEquals(new Point2D(30.0d, -20.0d), getControlPoint(cut3));
    }

    // endregion

    // region Private

    private Point2D getStartControlPoint(final CubicBezierCurveCommand command) throws NoSuchFieldException, IllegalAccessException {
        final Field field = CubicBezierCurveCommand.class.getDeclaredField("startControlPoint");
        field.setAccessible(true);
        return (Point2D) field.get(command);
    }

    private Point2D getControlPoint(final QuadraticBezierCurveCommand command) throws NoSuchFieldException, IllegalAccessException {
        final Field field = QuadraticBezierCurveCommand.class.getDeclaredField("controlPoint");
        field.setAccessible(true);
        return (Point2D) field.get(command);
    }


    // endregion
}