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
import javafx.scene.shape.Rectangle;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 08.04.2017.
 */
public class PositionCommandTest {

    // region Class

    private static class PositionCommandMock extends PositionCommand {

        PositionCommandMock(String data) throws PathException {
            super(data);
        }

        @Override
        public char getAbsoluteName() {
            return 'X';
        }

        @Override
        public char getRelativeName() {
            return 'x';
        }
    }

    // endregion

    // region Test

    @Test
    public void aPositionCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("X10 10",
                                                     "x10 10",
                                                     "X 10 10",
                                                     "x 10 10",
                                                     " X 10 10",
                                                     " x 10 10",
                                                     " X 10 10 ",
                                                     " x 10 10 ",
                                                     " 10 10 ",
                                                     "   X    10    10    ",
                                                     "   x    10    10    ",
                                                     "X10,10",
                                                     "x10,10",
                                                     "X 10,10",
                                                     "x 10,10",
                                                     " X 10,10",
                                                     " x 10,10",
                                                     " X 10,10 ",
                                                     " x 10,10 ",
                                                     " 10 10 ",
                                                     "   X    10  ,  10    ",
                                                     "   x    10  ,  10    ");


        for (final String data : validData) {
            try {
                new PositionCommandMock(data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test
    public void aPositionCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> validData = Arrays.asList("X1010",
                                                     "x1010",
                                                     "X 1010",
                                                     "x 1010",
                                                     " X 1010",
                                                     " x 1010",
                                                     " X 1010 ",
                                                     " x 1010 ",
                                                     " 1010 ",
                                                     "   X    1010    ",
                                                     "   x    1010    ",
                                                     "Z10 10",
                                                     "X10 10Z",
                                                     "X10 10 10",
                                                     "X10 10 Z");


        for (final String data : validData) {
            try {
                new PositionCommandMock(data);
                fail(String.format("Should have failed to use data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculated() throws Exception {

        final Point2D position1 = new PositionCommandMock("10 20").getNextPosition(new Point2D(10, 30));

        assertEquals(20.0d, position1.getX(), 0.01d);
        assertEquals(50.0d, position1.getY(), 0.01d);

        final Point2D position2 = new PositionCommandMock("-10 20").getNextPosition(new Point2D(20, 20));

        assertEquals(10.0d, position2.getX(), 0.01d);
        assertEquals(40.0d, position2.getY(), 0.01d);

        final Point2D position3 = new PositionCommandMock("10 20").getNextPosition(new Point2D(-20, 20));

        assertEquals(-10.0d, position3.getX(), 0.01d);
        assertEquals(40.0d, position3.getY(), 0.01d);

        final Point2D position4 = new PositionCommandMock("10 -20").getNextPosition(new Point2D(10, 10));

        assertEquals(20.0d, position4.getX(), 0.01d);
        assertEquals(-10.0d, position4.getY(), 0.01d);

        final Point2D position5 = new PositionCommandMock("10 30").getNextPosition(new Point2D(10, -20));

        assertEquals(20.0d, position5.getX(), 0.01d);
        assertEquals(10.0d, position5.getY(), 0.01d);

        final Point2D position6 = new PositionCommandMock("-10 30").getNextPosition(new Point2D(-10, 20));

        assertEquals(-20.0d, position6.getX(), 0.01d);
        assertEquals(50.0d, position6.getY(), 0.01d);

        final Point2D position7 = new PositionCommandMock("10 -30").getNextPosition(new Point2D(10, -20));

        assertEquals(20.0d, position7.getX(), 0.01d);
        assertEquals(-50.0d, position7.getY(), 0.01d);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculated() throws Exception {

        final Rectangle boundingBox1 = new PositionCommand("10 20") {
            @Override
            public char getAbsoluteName() {
                return 0;
            }

            @Override
            public char getRelativeName() {
                return 0;
            }
        }.getBoundingBox(new Point2D(0, 0));

        assertEquals(0.0d, boundingBox1.getX(), 0.01d);
        assertEquals(0.0d, boundingBox1.getY(), 0.01d);
        assertEquals(10.0d, boundingBox1.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox1.getHeight(), 0.01d);


        final Rectangle boundingBox2 = new PositionCommandMock("-10 -20").getBoundingBox(new Point2D(0, 0));

        assertEquals(-10.0d, boundingBox2.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox2.getY(), 0.01d);
        assertEquals(10.0d, boundingBox2.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox2.getHeight(), 0.01d);

        final Rectangle boundingBox3 = new PositionCommandMock("-10 -20").getBoundingBox(new Point2D(-10, -20));

        assertEquals(-20.0d, boundingBox3.getX(), 0.01d);
        assertEquals(-40.0d, boundingBox3.getY(), 0.01d);
        assertEquals(10.0d, boundingBox3.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox3.getHeight(), 0.01d);

        final Rectangle boundingBox4 = new PositionCommandMock("10 20").getBoundingBox(new Point2D(-10, -20));

        assertEquals(-10.0d, boundingBox4.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox4.getY(), 0.01d);
        assertEquals(10.0d, boundingBox4.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox4.getHeight(), 0.01d);
    }

    // endregion
}