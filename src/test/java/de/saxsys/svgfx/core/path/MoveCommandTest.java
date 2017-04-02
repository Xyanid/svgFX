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
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 01.04.2017.
 */
public class MoveCommandTest {

    @Test
    public void aMoveCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("M10 10",
                                                     "m10 10",
                                                     "M 10 10",
                                                     "m 10 10",
                                                     " M 10 10",
                                                     " m 10 10",
                                                     " M 10 10 ",
                                                     " m 10 10 ",
                                                     " 10 10 ",
                                                     "   M    10    10    ",
                                                     "   m    10    10    ");


        for (final String data : validData) {
            try {
                new MoveCommand(data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test
    public void aMoveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> validData = Arrays.asList("M1010",
                                                     "m1010",
                                                     "M 1010",
                                                     "m 1010",
                                                     " M 1010",
                                                     " m 1010",
                                                     " M 1010 ",
                                                     " m 1010 ",
                                                     " 1010 ",
                                                     "   M    1010    ",
                                                     "   m    1010    ",
                                                     "Z10 10",
                                                     "M10 10Z",
                                                     "M10 10 10",
                                                     "M10 10 Z");


        for (final String data : validData) {
            try {
                new MoveCommand(data);
                fail(String.format("Should have failed to use data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculated() throws Exception {

        final Point2D position1 = new MoveCommand("10 20").getNextPosition(new Point2D(10, 30));

        assertEquals(20.0d, position1.getX(), 0.01d);
        assertEquals(50.0d, position1.getY(), 0.01d);

        final Point2D position2 = new MoveCommand("-10 20").getNextPosition(new Point2D(20, 20));

        assertEquals(10.0d, position2.getX(), 0.01d);
        assertEquals(40.0d, position2.getY(), 0.01d);

        final Point2D position3 = new MoveCommand("10 20").getNextPosition(new Point2D(-20, 20));

        assertEquals(-10.0d, position3.getX(), 0.01d);
        assertEquals(40.0d, position3.getY(), 0.01d);

        final Point2D position4 = new MoveCommand("10 -20").getNextPosition(new Point2D(10, 10));

        assertEquals(20.0d, position4.getX(), 0.01d);
        assertEquals(-10.0d, position4.getY(), 0.01d);

        final Point2D position5 = new MoveCommand("10 30").getNextPosition(new Point2D(10, -20));

        assertEquals(20.0d, position5.getX(), 0.01d);
        assertEquals(10.0d, position5.getY(), 0.01d);

        final Point2D position6 = new MoveCommand("-10 30").getNextPosition(new Point2D(-10, 20));

        assertEquals(-20.0d, position6.getX(), 0.01d);
        assertEquals(50.0d, position6.getY(), 0.01d);

        final Point2D position7 = new MoveCommand("10 -30").getNextPosition(new Point2D(10, -20));

        assertEquals(20.0d, position7.getX(), 0.01d);
        assertEquals(-50.0d, position7.getY(), 0.01d);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculated() throws Exception {

        final Rectangle boundingBox1 = new MoveCommand("10 20").getBoundingBox(new Point2D(0, 0));

        assertEquals(0.0d, boundingBox1.getX(), 0.01d);
        assertEquals(0.0d, boundingBox1.getY(), 0.01d);
        assertEquals(10.0d, boundingBox1.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox1.getHeight(), 0.01d);


        final Rectangle boundingBox2 = new MoveCommand("-10 -20").getBoundingBox(new Point2D(0, 0));

        assertEquals(-10.0d, boundingBox2.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox2.getY(), 0.01d);
        assertEquals(10.0d, boundingBox2.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox2.getHeight(), 0.01d);

        final Rectangle boundingBox3 = new MoveCommand("-10 -20").getBoundingBox(new Point2D(-10, -20));

        assertEquals(-20.0d, boundingBox3.getX(), 0.01d);
        assertEquals(-40.0d, boundingBox3.getY(), 0.01d);
        assertEquals(10.0d, boundingBox3.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox3.getHeight(), 0.01d);

        final Rectangle boundingBox4 = new MoveCommand("10 20").getBoundingBox(new Point2D(-10, -20));

        assertEquals(-10.0d, boundingBox4.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox4.getY(), 0.01d);
        assertEquals(10.0d, boundingBox4.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox4.getHeight(), 0.01d);
    }
}