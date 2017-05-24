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

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;

/**
 * @author Xyanid on 21.05.2017.
 */
@SuppressWarnings ("ConstantConditions")
public class BezierCurveCommandTest {

    // region Class

    private static class BezierCurveCommandMock extends BezierCurveCommand {

        BezierCurveCommandMock(boolean isAbsolute, Point2D endPoint) {
            super(isAbsolute, endPoint);
        }

        @Override
        public Point2D getAbsoluteStartControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
            return Point2D.ZERO;
        }

        @Override
        public Point2D getAbsoluteEndControlPoint(final Point2D absoluteCurrentPoint) throws PathException {
            return Point2D.ZERO;
        }
    }

    // endregion

    // region Test

    @Test (expected = PathException.class)
    public void anExceptionWillBeThrownWhenRequestingTheNextPositionForARelativeCommandAndProvidingNoPoint() throws Exception {
        new BezierCurveCommandMock(false, Point2D.ZERO).getAbsoluteEndPoint(null);
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculatedIfTheCommandIsRelative() throws Exception {

        final Point2D position1 = new BezierCurveCommandMock(false, new Point2D(10.0d, 20.0d)).getAbsoluteEndPoint(new Point2D(10, 30));

        assertEquals(20.0d, position1.getX(), MINIMUM_DEVIATION);
        assertEquals(50.0d, position1.getY(), MINIMUM_DEVIATION);

        final Point2D position2 = new BezierCurveCommandMock(false, new Point2D(-10.0d, 20.0d)).getAbsoluteEndPoint(new Point2D(20, 20));

        assertEquals(10.0d, position2.getX(), MINIMUM_DEVIATION);
        assertEquals(40.0d, position2.getY(), MINIMUM_DEVIATION);

        final Point2D position3 = new BezierCurveCommandMock(false, new Point2D(10.0d, 20.0d)).getAbsoluteEndPoint(new Point2D(-20, 20));

        assertEquals(-10.0d, position3.getX(), MINIMUM_DEVIATION);
        assertEquals(40.0d, position3.getY(), MINIMUM_DEVIATION);

        final Point2D position4 = new BezierCurveCommandMock(false, new Point2D(10.0d, -20.0d)).getAbsoluteEndPoint(new Point2D(10, 10));

        assertEquals(20.0d, position4.getX(), MINIMUM_DEVIATION);
        assertEquals(-10.0d, position4.getY(), MINIMUM_DEVIATION);

        final Point2D position5 = new BezierCurveCommandMock(false, new Point2D(10.0d, 30.0d)).getAbsoluteEndPoint(new Point2D(10, -20));

        assertEquals(20.0d, position5.getX(), MINIMUM_DEVIATION);
        assertEquals(10.0d, position5.getY(), MINIMUM_DEVIATION);

        final Point2D position6 = new BezierCurveCommandMock(false, new Point2D(-10.0d, 30.0d)).getAbsoluteEndPoint(new Point2D(-10, 20));

        assertEquals(-20.0d, position6.getX(), MINIMUM_DEVIATION);
        assertEquals(50.0d, position6.getY(), MINIMUM_DEVIATION);

        final Point2D position7 = new BezierCurveCommandMock(false, new Point2D(10.0d, -30.0d)).getAbsoluteEndPoint(new Point2D(10, -20));

        assertEquals(20.0d, position7.getX(), MINIMUM_DEVIATION);
        assertEquals(-50.0d, position7.getY(), MINIMUM_DEVIATION);
    }

    @Test
    public void dependingOnTheDataTheNextPositionWillBeCorrectlyCalculatedIfTheCommandIsAbsolute() throws Exception {

        final Point2D position1 = new BezierCurveCommandMock(true, new Point2D(10.0d, 20.0d)).getAbsoluteEndPoint(new Point2D(10, 30));

        assertEquals(10.0d, position1.getX(), MINIMUM_DEVIATION);
        assertEquals(20.0d, position1.getY(), MINIMUM_DEVIATION);

        final Point2D position2 = new BezierCurveCommandMock(true, new Point2D(-10.0d, 20.0d)).getAbsoluteEndPoint(new Point2D(20, 20));

        assertEquals(-10.0d, position2.getX(), MINIMUM_DEVIATION);
        assertEquals(20.0d, position2.getY(), MINIMUM_DEVIATION);

        final Point2D position3 = new BezierCurveCommandMock(true, new Point2D(10.0d, 20.0d)).getAbsoluteEndPoint(new Point2D(-20, 20));

        assertEquals(10.0d, position3.getX(), MINIMUM_DEVIATION);
        assertEquals(20.0d, position3.getY(), MINIMUM_DEVIATION);

        final Point2D position4 = new BezierCurveCommandMock(true, new Point2D(10.0d, -20.0d)).getAbsoluteEndPoint(new Point2D(10, 10));

        assertEquals(10.0d, position4.getX(), MINIMUM_DEVIATION);
        assertEquals(-20.0d, position4.getY(), MINIMUM_DEVIATION);

        final Point2D position5 = new BezierCurveCommandMock(true, new Point2D(10.0d, 30.0d)).getAbsoluteEndPoint(new Point2D(10, -20));

        assertEquals(10.0d, position5.getX(), MINIMUM_DEVIATION);
        assertEquals(30.0d, position5.getY(), MINIMUM_DEVIATION);

        final Point2D position6 = new BezierCurveCommandMock(true, new Point2D(-10.0d, 30.0d)).getAbsoluteEndPoint(new Point2D(-10, 20));

        assertEquals(-10.0d, position6.getX(), MINIMUM_DEVIATION);
        assertEquals(30.0d, position6.getY(), MINIMUM_DEVIATION);

        final Point2D position7 = new BezierCurveCommandMock(true, new Point2D(10.0d, -30.0d)).getAbsoluteEndPoint(new Point2D(10, -20));

        assertEquals(10.0d, position7.getX(), MINIMUM_DEVIATION);
        assertEquals(-30.0d, position7.getY(), MINIMUM_DEVIATION);
    }

    // endregion
}