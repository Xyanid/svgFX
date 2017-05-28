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
public class CubicBezierCurveCommandTest {

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheAbsoluteStartControlPointForARelativeCommandAndProvidingNoPoint() throws Exception {
        new CubicBezierCurveCommand(false, Point2D.ZERO, Point2D.ZERO, Point2D.ZERO).getAbsoluteStartControlPoint(null);
    }

    @Test
    public void theCorrectAbsoluteStartControlPointWillBeReturned() throws Exception {
        final CubicBezierCurveCommand cut = new CubicBezierCurveCommand(false, new Point2D(10.0d, 20.0d), Point2D.ZERO, Point2D.ZERO);

        assertEquals(new Point2D(40.0d, 60.0d), cut.getAbsoluteStartControlPoint(new Point2D(30.0d, 40.0d)));

        final CubicBezierCurveCommand cut1 = new CubicBezierCurveCommand(true, new Point2D(10.0d, 20.0d), Point2D.ZERO, Point2D.ZERO);

        assertEquals(new Point2D(10.0d, 20.0d), cut1.getAbsoluteStartControlPoint(null));
        assertEquals(new Point2D(10.0d, 20.0d), cut1.getAbsoluteStartControlPoint(new Point2D(30.0d, 40.0d)));
    }

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheAbsoluteEndControlPointForARelativeCommandAndProvidingNoPoint() throws Exception {
        new CubicBezierCurveCommand(false, Point2D.ZERO, Point2D.ZERO, Point2D.ZERO).getAbsoluteEndControlPoint(null);
    }

    @Test
    public void theCorrectAbsoluteEndControlPointWillBeReturned() throws Exception {
        final CubicBezierCurveCommand cut = new CubicBezierCurveCommand(false, Point2D.ZERO, new Point2D(10.0d, 20.0d), Point2D.ZERO);

        assertEquals(new Point2D(40.0d, 60.0d), cut.getAbsoluteEndControlPoint(new Point2D(30.0d, 40.0d)));

        final CubicBezierCurveCommand cut1 = new CubicBezierCurveCommand(true, Point2D.ZERO, new Point2D(10.0d, 20.0d), Point2D.ZERO);

        assertEquals(new Point2D(10.0d, 20.0d), cut1.getAbsoluteEndControlPoint(null));
        assertEquals(new Point2D(10.0d, 20.0d), cut1.getAbsoluteEndControlPoint(new Point2D(30.0d, 40.0d)));
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Optional<Rectangle> boundingBox1 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(0.0d, 20.0d),
                                                                             new Point2D(40.0d, 20.0d),
                                                                             new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(0.0d, 0.0d));

        assertEquals(0.0d, boundingBox1.get().getX(), NORMAL_DEVIATION);
        assertEquals(0.0d, boundingBox1.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox1.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(15.0d, boundingBox1.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox2 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(20.0, 20.0d),
                                                                             new Point2D(40.0d, 20.0d),
                                                                             new Point2D(60.0d, 0.0d)).getBoundingBox(new Point2D(60.0d, 0.0d));

        assertEquals(60.0d, boundingBox2.get().getX(), NORMAL_DEVIATION);
        assertEquals(00.0d, boundingBox2.get().getY(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox2.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(15.0d, boundingBox2.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox3 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(-20.0, 20.0d),
                                                                             new Point2D(40.0d, 20.0d),
                                                                             new Point2D(20.0d, 0.0d)).getBoundingBox(new Point2D(160.0d, 0.0d));

        assertEquals(155.9d, boundingBox3.get().getX(), NORMAL_DEVIATION);
        assertEquals(0.0d, boundingBox3.get().getY(), NORMAL_DEVIATION);
        assertEquals(28.3d, boundingBox3.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(15.0d, boundingBox3.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox4 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(0.0d, 20.0d),
                                                                             new Point2D(40.0d, -20.0d),
                                                                             new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(0.0d, 60.0d));

        assertEquals(0.0d, boundingBox4.get().getX(), NORMAL_DEVIATION);
        assertEquals(54.2d, boundingBox4.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox4.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox4.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox5 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(20.0d, 20.0d),
                                                                             new Point2D(40.0d, -20.0d),
                                                                             new Point2D(60.0d, 0.0d)).getBoundingBox(new Point2D(60.0d, 60.0d));

        assertEquals(60.0d, boundingBox5.get().getX(), NORMAL_DEVIATION);
        assertEquals(54.2d, boundingBox5.get().getY(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox5.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox5.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox6 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(-20.0d, 20.0d),
                                                                             new Point2D(40.0d, -20.0d),
                                                                             new Point2D(20.0d, 0.0d)).getBoundingBox(new Point2D(160.0d, 60.0d));

        assertEquals(155.9d, boundingBox6.get().getX(), NORMAL_DEVIATION);
        assertEquals(54.2d, boundingBox6.get().getY(), NORMAL_DEVIATION);
        assertEquals(28.3d, boundingBox6.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox6.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox7 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(0.0d, -20.0d),
                                                                             new Point2D(40.0d, 0.0d),
                                                                             new Point2D(40.0d, 20.0d)).getBoundingBox(new Point2D(0.0d, 120.0d));

        assertEquals(0.0d, boundingBox7.get().getX(), NORMAL_DEVIATION);
        assertEquals(111.7d, boundingBox7.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox7.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(28.3d, boundingBox7.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox8 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(-20.0d, -20.0d),
                                                                             new Point2D(20.0d, -20.0d),
                                                                             new Point2D(0.0d, -40.0d)).getBoundingBox(new Point2D(80.0d, 140.0d));

        assertEquals(74.2d, boundingBox8.get().getX(), NORMAL_DEVIATION);
        assertEquals(100.0d, boundingBox8.get().getY(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox8.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox8.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox9 = new CubicBezierCurveCommand(false,
                                                                             new Point2D(0.0d, -20.0d),
                                                                             new Point2D(80.0d, -40.0d),
                                                                             new Point2D(60.0d, -40.0d)).getBoundingBox(new Point2D(120.0d, 140.0d));

        assertEquals(120.0d, boundingBox9.get().getX(), NORMAL_DEVIATION);
        assertEquals(100.0d, boundingBox9.get().getY(), NORMAL_DEVIATION);
        assertEquals(63.2d, boundingBox9.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox9.get().getHeight(), NORMAL_DEVIATION);
    }

    @Test
    public void dependingOnTheDataTheBoundingBoxWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Optional<Rectangle> boundingBox1 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(0.0d, 20.0d),
                                                                             new Point2D(40.0d, 20.0d),
                                                                             new Point2D(40.0d, 0.0d)).getBoundingBox(new Point2D(0.0d, 0.0d));

        assertEquals(0.0d, boundingBox1.get().getX(), NORMAL_DEVIATION);
        assertEquals(0.0d, boundingBox1.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox1.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(15.0d, boundingBox1.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox2 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(80.0d, 20.0d),
                                                                             new Point2D(100.0d, 20.0d),
                                                                             new Point2D(120.0d, 0.0d)).getBoundingBox(new Point2D(60.0d, 0.0d));

        assertEquals(60.0d, boundingBox2.get().getX(), NORMAL_DEVIATION);
        assertEquals(00.0d, boundingBox2.get().getY(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox2.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(15.0d, boundingBox2.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox3 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(140.0d, 20.0d),
                                                                             new Point2D(200.0d, 20.0d),
                                                                             new Point2D(180.0d, 0.0d)).getBoundingBox(new Point2D(160.0d, 0.0d));

        assertEquals(155.9d, boundingBox3.get().getX(), NORMAL_DEVIATION);
        assertEquals(0.0d, boundingBox3.get().getY(), NORMAL_DEVIATION);
        assertEquals(28.3d, boundingBox3.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(15.0d, boundingBox3.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox4 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(0.0d, 80.0d),
                                                                             new Point2D(40.0d, 40.0d),
                                                                             new Point2D(40.0d, 60.0d)).getBoundingBox(new Point2D(0.0d, 60.0d));

        assertEquals(0.0d, boundingBox4.get().getX(), NORMAL_DEVIATION);
        assertEquals(54.2d, boundingBox4.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox4.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox4.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox5 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(80.0d, 80.0d),
                                                                             new Point2D(100.0d, 40.0d),
                                                                             new Point2D(120.0d, 60.0d)).getBoundingBox(new Point2D(60.0d, 60.0d));

        assertEquals(60.0d, boundingBox5.get().getX(), NORMAL_DEVIATION);
        assertEquals(54.2d, boundingBox5.get().getY(), NORMAL_DEVIATION);
        assertEquals(60.0d, boundingBox5.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox5.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox6 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(140.0d, 80.0d),
                                                                             new Point2D(200.0d, 40.0d),
                                                                             new Point2D(180.0d, 60.0d)).getBoundingBox(new Point2D(160.0d, 60.0d));

        assertEquals(155.9d, boundingBox6.get().getX(), NORMAL_DEVIATION);
        assertEquals(54.2d, boundingBox6.get().getY(), NORMAL_DEVIATION);
        assertEquals(28.3d, boundingBox6.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox6.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox7 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(0.0d, 100.0d),
                                                                             new Point2D(40.0d, 120.0d),
                                                                             new Point2D(40.0d, 140.0d)).getBoundingBox(new Point2D(0.0d, 120.0d));

        assertEquals(0.0d, boundingBox7.get().getX(), NORMAL_DEVIATION);
        assertEquals(111.7d, boundingBox7.get().getY(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox7.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(28.3d, boundingBox7.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox8 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(60.0d, 120.0d),
                                                                             new Point2D(100.0d, 120.0d),
                                                                             new Point2D(80.0d, 100.0d)).getBoundingBox(new Point2D(80.0d, 140.0d));

        assertEquals(74.2d, boundingBox8.get().getX(), NORMAL_DEVIATION);
        assertEquals(100.0d, boundingBox8.get().getY(), NORMAL_DEVIATION);
        assertEquals(11.5d, boundingBox8.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox8.get().getHeight(), NORMAL_DEVIATION);

        final Optional<Rectangle> boundingBox9 = new CubicBezierCurveCommand(true,
                                                                             new Point2D(120.0d, 120.0d),
                                                                             new Point2D(200.0d, 100.0d),
                                                                             new Point2D(180.0d, 100.0d)).getBoundingBox(new Point2D(120.0d, 140.0d));

        assertEquals(120.0d, boundingBox9.get().getX(), NORMAL_DEVIATION);
        assertEquals(100.0d, boundingBox9.get().getY(), NORMAL_DEVIATION);
        assertEquals(63.2d, boundingBox9.get().getWidth(), NORMAL_DEVIATION);
        assertEquals(40.0d, boundingBox9.get().getHeight(), NORMAL_DEVIATION);
    }
}