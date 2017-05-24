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

import static de.saxsys.svgfx.core.TestUtil.NORMAL_DEVIATION;
import static org.junit.Assert.assertEquals;

/**
 * @author Xyanid on 24.05.2017.
 */
@SuppressWarnings ("ConstantConditions")
public class QuadraticBezierCurveCommandTest {

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheAbsoluteControlPointForARelativeCommandAndProvidingNoPoint() throws Exception {
        new QuadraticBezierCurveCommand(false, Point2D.ZERO, Point2D.ZERO).getAbsoluteControlPoint(null);
    }

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheAbsoluteStartControlPointForARelativeCommandAndProvidingNoPoint() throws Exception {
        new QuadraticBezierCurveCommand(false, Point2D.ZERO, Point2D.ZERO).getAbsoluteStartControlPoint(null);
    }

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheAbsoluteStartControlPointForAnAbsoluteCommandAndProvidingNoPoint() throws Exception {
        new QuadraticBezierCurveCommand(true, Point2D.ZERO, Point2D.ZERO).getAbsoluteStartControlPoint(null);
    }

    @Test
    public void theCorrectAbsoluteStartControlPointWillBeReturned() throws Exception {
        final QuadraticBezierCurveCommand cut = new QuadraticBezierCurveCommand(false, new Point2D(30.0d, 30.0d), Point2D.ZERO);

        assertEquals(new Point2D(50.0d, 60.0d), cut.getAbsoluteStartControlPoint(new Point2D(30.0d, 40.0d)));

        final QuadraticBezierCurveCommand cut1 = new QuadraticBezierCurveCommand(true, new Point2D(70.0d, 60.0d), Point2D.ZERO);

        assertEquals(new Point2D(60.0d, 50.0d), cut1.getAbsoluteStartControlPoint(new Point2D(40.0d, 30.0d)));
    }

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheAbsoluteEndControlPointForARelativeCommandAndProvidingNoPoint() throws Exception {
        new QuadraticBezierCurveCommand(false, Point2D.ZERO, Point2D.ZERO).getAbsoluteEndControlPoint(null);
    }

    @Test
    public void theCorrectAbsoluteEndControlPointWillBeReturned() throws Exception {
        final QuadraticBezierCurveCommand cut = new QuadraticBezierCurveCommand(false, new Point2D(10.0d, 30.0d), new Point2D(100.0d, 0.0d));

        assertEquals(new Point2D(70.0d, 20.0d), cut.getAbsoluteEndControlPoint(new Point2D(30.0d, 0.0d)));

        final QuadraticBezierCurveCommand cut1 = new QuadraticBezierCurveCommand(true, new Point2D(110.0d, 90.0d), new Point2D(200.0, 60.0d));

        assertEquals(new Point2D(140.0d, 80.0d), cut1.getAbsoluteEndControlPoint(null));
        assertEquals(new Point2D(140.0d, 80.0d), cut1.getAbsoluteEndControlPoint(new Point2D(30.0d, 0.0d)));
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Optional<Rectangle> boundingBox1 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(10.0d, 10.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(10, 10));

        assertEquals(10.0d, boundingBox1.get().getX(), NORMAL_DEVIATION);
        assertEquals(10.0d, boundingBox1.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox1.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(7.5d, boundingBox1.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox2 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(50.0d, 10.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(70, 10));

        assertEquals(68.9d, boundingBox2.get().getX(), NORMAL_DEVIATION);
        assertEquals(10.0d, boundingBox2.get().getY(), NORMAL_DEVIATION);
        assertEquals(42.3d, boundingBox2.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(7.5d, boundingBox2.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox3 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(30.0d, 30.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(10, 60));

        assertEquals(10.0d, boundingBox3.get().getX(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox3.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox3.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox3.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox4 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(50.0d, 30.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(70, 60));

        assertEquals(68.9d, boundingBox4.get().getX(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox4.get().getY(), NORMAL_DEVIATION);
        assertEquals(42.3d, boundingBox4.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox4.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox5 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(20.0d, 30.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(10, 110));

        assertEquals(10.0d, boundingBox5.get().getX(), NORMAL_DEVIATION);
        assertEquals(110.0d, boundingBox5.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox5.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox5.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox6 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(40.0d, -30.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(70, 110));

        assertEquals(70.0d, boundingBox6.get().getX(), NORMAL_DEVIATION);
        assertEquals(101.3d, boundingBox6.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox6.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(17.3d, boundingBox6.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox7 = new QuadraticBezierCurveCommand(false,
                                                                                 new Point2D(170.0d, 30.0d),
                                                                                 new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(130, 110));

        assertEquals(96.6d, boundingBox7.get().getX(), NORMAL_DEVIATION);
        assertEquals(110.0d, boundingBox7.get().getY(), NORMAL_DEVIATION);
        assertEquals(106.9d, boundingBox7.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox7.get().getHeight(), NORMAL_DEVIATION);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Optional<Rectangle> boundingBox1 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(40.0d, 20.0d),
                                                                                 new Point2D(50.0d, 10.0d)).getBoundingBox(new Point2D(10, 10));

        assertEquals(10.0d, boundingBox1.get().getX(), NORMAL_DEVIATION);
        assertEquals(10.0d, boundingBox1.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox1.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(7.5d, boundingBox1.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox2 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(120.0d, 20.0d),
                                                                                 new Point2D(110.0d, 10.0d)).getBoundingBox(new Point2D(70, 10));

        assertEquals(68.9d, boundingBox2.get().getX(), NORMAL_DEVIATION);
        assertEquals(10.0d, boundingBox2.get().getY(), NORMAL_DEVIATION);
        assertEquals(42.3d, boundingBox2.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(7.5d, boundingBox2.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox3 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(40.0d, 90.0d),
                                                                                 new Point2D(50.0d, 60.0d)).getBoundingBox(new Point2D(10, 60));

        assertEquals(10.0d, boundingBox3.get().getX(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox3.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox3.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox3.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox4 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(120.0d, 90.0d),
                                                                                 new Point2D(110.0d, 60.0d)).getBoundingBox(new Point2D(70, 60));

        assertEquals(68.9d, boundingBox4.get().getX(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox4.get().getY(), NORMAL_DEVIATION);
        assertEquals(42.3d, boundingBox4.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox4.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox5 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(30.0d, 140.0d),
                                                                                 new Point2D(50.0d, 110.0d)).getBoundingBox(new Point2D(10, 110));

        assertEquals(10.0d, boundingBox5.get().getX(), NORMAL_DEVIATION);
        assertEquals(110.0d, boundingBox5.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox5.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox5.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox6 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(110.0d, 80.0d),
                                                                                 new Point2D(110.0d, 110.0d)).getBoundingBox(new Point2D(70, 110));

        assertEquals(70.0d, boundingBox6.get().getX(), NORMAL_DEVIATION);
        assertEquals(101.3d, boundingBox6.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox6.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(17.3d, boundingBox6.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox7 = new QuadraticBezierCurveCommand(true,
                                                                                 new Point2D(300.0d, 140.0d),
                                                                                 new Point2D(170.0d, 110.0d)).getBoundingBox(new Point2D(130, 110));

        assertEquals(96.6d, boundingBox7.get().getX(), NORMAL_DEVIATION);
        assertEquals(110.0d, boundingBox7.get().getY(), NORMAL_DEVIATION);
        assertEquals(106.9d, boundingBox7.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(22.5d, boundingBox7.get().getHeight(), NORMAL_DEVIATION);
    }
}