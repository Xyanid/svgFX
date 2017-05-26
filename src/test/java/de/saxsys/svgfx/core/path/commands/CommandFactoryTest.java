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
import org.junit.Before;
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

    // region Fields

    private CommandFactory cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new CommandFactory();
    }

    // endregion

    // region Tests

    @Test (expected = PathException.class)
    public void aMoveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createMoveCommand('X', "10 10");
    }

    @Test
    public void aMoveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createMoveCommand('M', "10 10");
        cut.createMoveCommand('m', "10 10");
    }

    @Test
    public void aMoveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10 Z",
                                                       "10 , Z",
                                                       "Z, 10");

        for (final String data : invalidData) {
            try {
                cut.createMoveCommand(MOVE.getAbsoluteName(), data);
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
                cut.createMoveCommand(MOVE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createLineCommand('X', "10 10");
    }

    @Test
    public void aLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createLineCommand('L', "10 10");
        cut.createLineCommand('l', "10 10");
    }

    @Test
    public void aLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10 Z",
                                                       "10 , Z",
                                                       "Z, 10");

        for (final String data : invalidData) {
            try {
                cut.createLineCommand(LINE.getRelativeName(), data);
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
                cut.createLineCommand(LINE.getRelativeName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aVerticalLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createVerticalLineCommand('X', "10");
    }

    @Test
    public void aVerticalLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createVerticalLineCommand('V', "10");
        cut.createVerticalLineCommand('v', "10");
    }

    @Test
    public void aVerticalLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10 10",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                cut.createVerticalLineCommand(VERTICAL_LINE.getAbsoluteName(), data);
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
                cut.createVerticalLineCommand(VERTICAL_LINE.getAbsoluteName(), data);
            } catch (final PathException ignore) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aHorizontalLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createHorizontalLineCommand('X', "10");
    }

    @Test
    public void aHorizontalLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createHorizontalLineCommand('H', "10");
        cut.createHorizontalLineCommand('h', "10");
    }

    @Test
    public void aHorizontalLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10 10",
                                                       "10Z",
                                                       "10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                cut.createHorizontalLineCommand(HORIZONTAL_LINE.getAbsoluteName(), data);
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
                cut.createHorizontalLineCommand(HORIZONTAL_LINE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createCubicBezierCurveCommand('X', "10");
    }

    @Test
    public void aCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createCubicBezierCurveCommand('C', "10, 10 10, 10 10, 10");
        cut.createCubicBezierCurveCommand('c', "10, 10 10, 10 10, 10");
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
                cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), data);
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
                cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aShortCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createShortCubicBezierCurveCommand('X', "10", Point2D.ZERO, null);
    }

    @Test
    public void aShortCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createShortCubicBezierCurveCommand('S', "10 10 10 10", Point2D.ZERO, null);
        cut.createShortCubicBezierCurveCommand('s', "10 10 10 10", Point2D.ZERO, null);
    }

    @Test
    public void aShortCubicBezierCurveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() throws PathException {
        final PathCommand command = cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "10, 10 10, 10 10, 10");

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10",
                                                       "10 10 10",
                                                       "10 10 10Z",
                                                       "10 10 10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(), data, Point2D.ZERO, command);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aShortCubicBezierCurveCommandCanBeCreatedFromAllPossibleValidDataStrings() throws PathException {
        final PathCommand command = cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "10, 10 10, 10 10, 10");

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
                cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(), data, Point2D.ZERO, command);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test
    public void aShortCubicBezierCurveCommandWillHaveItStartControlPointSetToTheCurrentPointIfThePreviousCommandWasNoCubicBezierCurveCommand()
            throws PathException, NoSuchFieldException, IllegalAccessException {

        final CubicBezierCurveCommand shortCubicBezierCurveCommand = cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                            "10 10 10 10",
                                                                                                            new Point2D(10.0d, 10.0d),
                                                                                                            null);

        assertEquals(new Point2D(10.0d, 10.0d), getStartControlPoint(shortCubicBezierCurveCommand));

        final CubicBezierCurveCommand shortCubicBezierCurveCommand1 = cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getRelativeName(),
                                                                                                             "10 10 10 10",
                                                                                                             new Point2D(10.0d, 10.0d),
                                                                                                             null);

        assertEquals(new Point2D(0.0d, 0.0d), getStartControlPoint(shortCubicBezierCurveCommand1));
    }

    @Test
    public void aShortCubicBezierCurveCommandWillHaveItsStartControlPointBeBasedOneThePreviousEndControlPointOfThePreviousCommand() throws PathException, NoSuchFieldException, IllegalAccessException {
        final Point2D absoluteCurrentPoint = new Point2D(10.0d, 10.0d);

        final CubicBezierCurveCommand command = cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "20,20 30,20 40,10");
        final CubicBezierCurveCommand shortCubicBezierCurveCommand = cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                            "60,20 70,10",
                                                                                                            absoluteCurrentPoint,
                                                                                                            command);

        assertEquals(new Point2D(50.0d, 0.0d), getStartControlPoint(shortCubicBezierCurveCommand));

        final CubicBezierCurveCommand command1 = cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getRelativeName(), "10,-10 20,-10 30,0");
        final CubicBezierCurveCommand shortCubicBezierCurveCommand1 = cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                             "60,20 70,10",
                                                                                                             absoluteCurrentPoint,
                                                                                                             command1);

        assertEquals(new Point2D(50.0d, 20.0d), getStartControlPoint(shortCubicBezierCurveCommand1));

        final CubicBezierCurveCommand command2 = cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getAbsoluteName(), "20,0 30,0 40,10");
        final CubicBezierCurveCommand shortCubicBezierCurveCommand2 = cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getRelativeName(),
                                                                                                             "50,10 60,00",
                                                                                                             absoluteCurrentPoint,
                                                                                                             command2);

        assertEquals(new Point2D(10.0d, 10.0d), getStartControlPoint(shortCubicBezierCurveCommand2));

        final CubicBezierCurveCommand command3 = cut.createCubicBezierCurveCommand(CUBIC_BEZIER_CURVE.getRelativeName(), "10,-10 20,-10 30,0");
        final CubicBezierCurveCommand shortCubicBezierCurveCommand3 = cut.createShortCubicBezierCurveCommand(SHORT_CUBIC_BEZIER_CURVE.getRelativeName(),
                                                                                                             "50,10 60,00",
                                                                                                             absoluteCurrentPoint,
                                                                                                             command3);

        assertEquals(new Point2D(10.0d, 10.0d), getStartControlPoint(shortCubicBezierCurveCommand3));
    }


    @Test (expected = PathException.class)
    public void aQuadraticBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createQuadraticBezierCurveCommand('X', "10");
    }

    @Test
    public void aQuadraticCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createQuadraticBezierCurveCommand('Q', "10 10 10 10");
        cut.createQuadraticBezierCurveCommand('q', "10 10 10 10");
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
                cut.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(), data);
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
                cut.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(), data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }


    @Test (expected = PathException.class)
    public void aShortQuadraticBezierCurveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        cut.createShortQuadraticBezierCurveCommand('X', "10", Point2D.ZERO, null);
    }

    @Test
    public void aShortQuadraticCubicBezierCurveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        cut.createShortQuadraticBezierCurveCommand('T', "10 10", Point2D.ZERO, null);
        cut.createShortQuadraticBezierCurveCommand('t', "10 10", Point2D.ZERO, null);
    }

    @Test
    public void aShortQuadraticBezierCurveCommandWillHaveItControlPointSetToTheCurrentPointIfThePreviousCommandWasNoCubicBezierCurveCommand()
            throws PathException, NoSuchFieldException, IllegalAccessException {

        final QuadraticBezierCurveCommand shortQuadraticBezierCurveCommand = cut.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                                        "20 20",
                                                                                                                        new Point2D(10.0d, 10.0d),
                                                                                                                        null);

        assertEquals(new Point2D(10.0d, 10.0d), getControlPoint(shortQuadraticBezierCurveCommand));

        final QuadraticBezierCurveCommand shortQuadraticBezierCurveCommand1 = cut.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                                         "20 20",
                                                                                                                         new Point2D(10.0d, 10.0d),
                                                                                                                         null);

        assertEquals(new Point2D(0.0d, 0.0d), getControlPoint(shortQuadraticBezierCurveCommand1));
    }

    @Test
    public void aShortQuadraticBezierCurveCommandWillHaveItsStartControlPointBeBasedOneThePreviousEndControlPointOfThePreviousCommand()
            throws PathException, NoSuchFieldException, IllegalAccessException {
        final Point2D absoluteCurrentPoint = new Point2D(10.0d, 20.0d);

        final QuadraticBezierCurveCommand command = cut.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                          "100,40 130,20");
        final QuadraticBezierCurveCommand shortQuadraticBezierCurveCommand = cut.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                                        "200,30",
                                                                                                                        absoluteCurrentPoint,
                                                                                                                        command);

        assertEquals(new Point2D(160.0d, 0.0d), getControlPoint(shortQuadraticBezierCurveCommand));

        final QuadraticBezierCurveCommand command1 = cut.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                           "90,20 120,0");
        final QuadraticBezierCurveCommand shortQuadraticBezierCurveCommand1 = cut.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                                                         "200,30",
                                                                                                                         absoluteCurrentPoint,
                                                                                                                         command1);

        assertEquals(new Point2D(160.0d, 0.0d), getControlPoint(shortQuadraticBezierCurveCommand1));

        final QuadraticBezierCurveCommand command2 = cut.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getAbsoluteName(),
                                                                                           "100,40 130,20");
        final QuadraticBezierCurveCommand shortQuadraticBezierCurveCommand2 = cut.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                                         "200,30",
                                                                                                                         absoluteCurrentPoint,
                                                                                                                         command2);

        assertEquals(new Point2D(30.0d, -20.0d), getControlPoint(shortQuadraticBezierCurveCommand2));

        final QuadraticBezierCurveCommand command3 = cut.createQuadraticBezierCurveCommand(QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                           "90,20 120,0");
        final QuadraticBezierCurveCommand shortQuadraticBezierCurveCommand3 = cut.createShortQuadraticBezierCurveCommand(SHORT_QUADRATIC_BEZIER_CURVE.getRelativeName(),
                                                                                                                         "200,30",
                                                                                                                         absoluteCurrentPoint,
                                                                                                                         command3);

        assertEquals(new Point2D(30.0d, -20.0d), getControlPoint(shortQuadraticBezierCurveCommand3));
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