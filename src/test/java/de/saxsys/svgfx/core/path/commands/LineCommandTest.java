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

import static org.junit.Assert.assertEquals;

/**
 * @author Xyanid on 08.04.2017.
 */
public class LineCommandTest {

    // region Test

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheNextPositionForARelativeCommandAndProvidingNoPoint() throws Exception {
        new LineCommand(false, new Point2D(10.0d, 20.0d)).getNextPosition(null);
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Point2D position1 = new LineCommand(false, new Point2D(10.0d, 20.0d)).getNextPosition(new Point2D(10, 30));

        assertEquals(20.0d, position1.getX(), 0.01d);
        assertEquals(50.0d, position1.getY(), 0.01d);

        final Point2D position2 = new LineCommand(false, new Point2D(-10.0d, 20.0d)).getNextPosition(new Point2D(20, 20));

        assertEquals(10.0d, position2.getX(), 0.01d);
        assertEquals(40.0d, position2.getY(), 0.01d);

        final Point2D position3 = new LineCommand(false, new Point2D(10.0d, 20.0d)).getNextPosition(new Point2D(-20, 20));

        assertEquals(-10.0d, position3.getX(), 0.01d);
        assertEquals(40.0d, position3.getY(), 0.01d);

        final Point2D position4 = new LineCommand(false, new Point2D(10.0d, -20.0d)).getNextPosition(new Point2D(10, 10));

        assertEquals(20.0d, position4.getX(), 0.01d);
        assertEquals(-10.0d, position4.getY(), 0.01d);

        final Point2D position5 = new LineCommand(false, new Point2D(10.0d, 30.0d)).getNextPosition(new Point2D(10, -20));

        assertEquals(20.0d, position5.getX(), 0.01d);
        assertEquals(10.0d, position5.getY(), 0.01d);

        final Point2D position6 = new LineCommand(false, new Point2D(-10.0d, 30.0d)).getNextPosition(new Point2D(-10, 20));

        assertEquals(-20.0d, position6.getX(), 0.01d);
        assertEquals(50.0d, position6.getY(), 0.01d);

        final Point2D position7 = new LineCommand(false, new Point2D(10.0d, -30.0d)).getNextPosition(new Point2D(10, -20));

        assertEquals(20.0d, position7.getX(), 0.01d);
        assertEquals(-50.0d, position7.getY(), 0.01d);
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Point2D position1 = new LineCommand(true, new Point2D(10.0d, 20.0d)).getNextPosition(new Point2D(10, 30));

        assertEquals(10.0d, position1.getX(), 0.01d);
        assertEquals(20.0d, position1.getY(), 0.01d);

        final Point2D position2 = new LineCommand(true, new Point2D(-10.0d, 20.0d)).getNextPosition(new Point2D(20, 20));

        assertEquals(-10.0d, position2.getX(), 0.01d);
        assertEquals(20.0d, position2.getY(), 0.01d);

        final Point2D position3 = new LineCommand(true, new Point2D(10.0d, 20.0d)).getNextPosition(new Point2D(-20, 20));

        assertEquals(10.0d, position3.getX(), 0.01d);
        assertEquals(20.0d, position3.getY(), 0.01d);

        final Point2D position4 = new LineCommand(true, new Point2D(10.0d, -20.0d)).getNextPosition(new Point2D(10, 10));

        assertEquals(10.0d, position4.getX(), 0.01d);
        assertEquals(-20.0d, position4.getY(), 0.01d);

        final Point2D position5 = new LineCommand(true, new Point2D(10.0d, 30.0d)).getNextPosition(new Point2D(10, -20));

        assertEquals(10.0d, position5.getX(), 0.01d);
        assertEquals(30.0d, position5.getY(), 0.01d);

        final Point2D position6 = new LineCommand(true, new Point2D(-10.0d, 30.0d)).getNextPosition(new Point2D(-10, 20));

        assertEquals(-10.0d, position6.getX(), 0.01d);
        assertEquals(30.0d, position6.getY(), 0.01d);

        final Point2D position7 = new LineCommand(true, new Point2D(10.0d, -30.0d)).getNextPosition(new Point2D(10, -20));

        assertEquals(10.0d, position7.getX(), 0.01d);
        assertEquals(-30.0d, position7.getY(), 0.01d);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Rectangle boundingBox1 = new LineCommand(false, new Point2D(10.0d, 20.0d)) {}.getBoundingBox(new Point2D(0, 0));

        assertEquals(0.0d, boundingBox1.getX(), 0.01d);
        assertEquals(0.0d, boundingBox1.getY(), 0.01d);
        assertEquals(10.0d, boundingBox1.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox1.getHeight(), 0.01d);


        final Rectangle boundingBox2 = new LineCommand(false, new Point2D(-10.0d, -20.0d)).getBoundingBox(new Point2D(0, 0));

        assertEquals(-10.0d, boundingBox2.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox2.getY(), 0.01d);
        assertEquals(10.0d, boundingBox2.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox2.getHeight(), 0.01d);

        final Rectangle boundingBox3 = new LineCommand(false, new Point2D(-10.0d, -20.0d)).getBoundingBox(new Point2D(-10, -20));

        assertEquals(-20.0d, boundingBox3.getX(), 0.01d);
        assertEquals(-40.0d, boundingBox3.getY(), 0.01d);
        assertEquals(10.0d, boundingBox3.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox3.getHeight(), 0.01d);

        final Rectangle boundingBox4 = new LineCommand(false, new Point2D(10.0d, 20.0d)).getBoundingBox(new Point2D(-10, -20));

        assertEquals(-10.0d, boundingBox4.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox4.getY(), 0.01d);
        assertEquals(10.0d, boundingBox4.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox4.getHeight(), 0.01d);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Rectangle boundingBox1 = new LineCommand(true, new Point2D(10.0d, 20.0d)) {}.getBoundingBox(new Point2D(0, 0));

        assertEquals(0.0d, boundingBox1.getX(), 0.01d);
        assertEquals(0.0d, boundingBox1.getY(), 0.01d);
        assertEquals(10.0d, boundingBox1.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox1.getHeight(), 0.01d);


        final Rectangle boundingBox2 = new LineCommand(true, new Point2D(-10.0d, -20.0d)).getBoundingBox(new Point2D(0, 0));

        assertEquals(-10.0d, boundingBox2.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox2.getY(), 0.01d);
        assertEquals(10.0d, boundingBox2.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox2.getHeight(), 0.01d);

        final Rectangle boundingBox3 = new LineCommand(true, new Point2D(-10.0d, -20.0d)).getBoundingBox(new Point2D(-10, -40));

        assertEquals(-10.0d, boundingBox3.getX(), 0.01d);
        assertEquals(-40.0d, boundingBox3.getY(), 0.01d);
        assertEquals(0.0d, boundingBox3.getWidth(), 0.01d);
        assertEquals(20.0d, boundingBox3.getHeight(), 0.01d);

        final Rectangle boundingBox4 = new LineCommand(true, new Point2D(10.0d, 20.0d)).getBoundingBox(new Point2D(-10, -20));

        assertEquals(-10.0d, boundingBox4.getX(), 0.01d);
        assertEquals(-20.0d, boundingBox4.getY(), 0.01d);
        assertEquals(20.0d, boundingBox4.getWidth(), 0.01d);
        assertEquals(40.0d, boundingBox4.getHeight(), 0.01d);
    }


    // endregion
}