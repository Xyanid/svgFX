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

import java.util.Optional;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;

/**
 * @author Xyanid on 09.05.2017.
 */
@SuppressWarnings ("ConstantConditions")
public class VerticalLineCommandTest {

    // region Tests

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheNextPositionAndProvidingNoPoint() throws Exception {
        new VerticalLineCommand(false, 0.0d).getAbsoluteEndPoint(null);
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Point2D position1 = new VerticalLineCommand(false, 10.0d).getAbsoluteEndPoint(new Point2D(10, 10));

        assertEquals(10.0d, position1.getX(), MINIMUM_DEVIATION);
        assertEquals(20.0d, position1.getY(), MINIMUM_DEVIATION);

        final Point2D position2 = new VerticalLineCommand(false, -10.0d).getAbsoluteEndPoint(new Point2D(20, 20));

        assertEquals(20.0d, position2.getX(), MINIMUM_DEVIATION);
        assertEquals(10.0d, position2.getY(), MINIMUM_DEVIATION);

        final Point2D position3 = new VerticalLineCommand(false, 10.0d).getAbsoluteEndPoint(new Point2D(30, -20));

        assertEquals(30.0d, position3.getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, position3.getY(), MINIMUM_DEVIATION);

        final Point2D position4 = new VerticalLineCommand(false, -10.0d).getAbsoluteEndPoint(new Point2D(40, -10));

        assertEquals(40.0d, position4.getX(), MINIMUM_DEVIATION);
        assertEquals(-20.0d, position4.getY(), MINIMUM_DEVIATION);
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Point2D position1 = new VerticalLineCommand(true, 10.0d).getAbsoluteEndPoint(new Point2D(10, 10));

        assertEquals(10.0d, position1.getX(), MINIMUM_DEVIATION);
        assertEquals(10.0d, position1.getY(), MINIMUM_DEVIATION);

        final Point2D position2 = new VerticalLineCommand(true, -10.0d).getAbsoluteEndPoint(new Point2D(20, 20));

        assertEquals(20.0d, position2.getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, position2.getY(), MINIMUM_DEVIATION);

        final Point2D position3 = new VerticalLineCommand(true, 10.0d).getAbsoluteEndPoint(new Point2D(30, -20));

        assertEquals(30.0d, position3.getX(), MINIMUM_DEVIATION);
        assertEquals(10.0d, position3.getY(), MINIMUM_DEVIATION);

        final Point2D position4 = new VerticalLineCommand(true, -10.0d).getAbsoluteEndPoint(new Point2D(40, -10));

        assertEquals(40.0d, position4.getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, position4.getY(), MINIMUM_DEVIATION);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Optional<Rectangle> boundingBox1 = new VerticalLineCommand(false, 10.0d).getBoundingBox(new Point2D(10, 10));

        assertEquals(10.0d, boundingBox1.get().getX(), MINIMUM_DEVIATION);
        assertEquals(10.0d, boundingBox1.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox1.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(10.0d, boundingBox1.get().getHeight(), MINIMUM_DEVIATION);


        final Optional<Rectangle> boundingBox2 = new VerticalLineCommand(false, -10.0d).getBoundingBox(new Point2D(20, 10));

        assertEquals(20.0d, boundingBox2.get().getX(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox2.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox2.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(10.0d, boundingBox2.get().getHeight(), MINIMUM_DEVIATION);

        final Optional<Rectangle> boundingBox3 = new VerticalLineCommand(false, 10.0d).getBoundingBox(new Point2D(30, -10));

        assertEquals(30.0d, boundingBox3.get().getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, boundingBox3.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox3.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(10.0d, boundingBox3.get().getHeight(), MINIMUM_DEVIATION);

        final Optional<Rectangle> boundingBox4 = new VerticalLineCommand(false, -10.0d).getBoundingBox(new Point2D(40, -10));

        assertEquals(40.0d, boundingBox4.get().getX(), MINIMUM_DEVIATION);
        assertEquals(-20.0d, boundingBox4.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox4.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(10.0d, boundingBox4.get().getHeight(), MINIMUM_DEVIATION);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Optional<Rectangle> boundingBox1 = new VerticalLineCommand(true, 10.0d).getBoundingBox(new Point2D(10, 10));

        assertEquals(10.0d, boundingBox1.get().getX(), MINIMUM_DEVIATION);
        assertEquals(10.0d, boundingBox1.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox1.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox1.get().getHeight(), MINIMUM_DEVIATION);


        final Optional<Rectangle> boundingBox2 = new VerticalLineCommand(true, -10.0d).getBoundingBox(new Point2D(20, 10));

        assertEquals(20.0d, boundingBox2.get().getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, boundingBox2.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox2.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(20.0d, boundingBox2.get().getHeight(), MINIMUM_DEVIATION);

        final Optional<Rectangle> boundingBox3 = new VerticalLineCommand(true, 10.0d).getBoundingBox(new Point2D(30, -10));

        assertEquals(30.0d, boundingBox3.get().getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, boundingBox3.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox3.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(20.0d, boundingBox3.get().getHeight(), MINIMUM_DEVIATION);

        final Optional<Rectangle> boundingBox4 = new VerticalLineCommand(true, -10.0d).getBoundingBox(new Point2D(40, -10));

        assertEquals(40.0d, boundingBox4.get().getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, boundingBox4.get().getY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox4.get().getWidth(), MINIMUM_DEVIATION);
        assertEquals(0.0d, boundingBox4.get().getHeight(), MINIMUM_DEVIATION);
    }

    // endregion
}