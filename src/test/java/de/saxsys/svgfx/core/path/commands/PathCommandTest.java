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
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Xyanid on 08.04.2017.
 */
public class PathCommandTest {

    // region Fields

    private PathCommand cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new PathCommand(false) {
            @Override
            public Point2D getNextPosition(Point2D position) {
                return null;
            }

            @Override
            public Optional<Rectangle> getBoundingBox(Point2D position) {
                return Optional.empty();
            }
        };
    }

    // endregion

    // region Tests

    @Test
    public void itIsPossibleToDetermineIfTheCommandIsAbsolute() throws PathException {

        final PathCommand cut = new PathCommand(true) {
            @Override
            public Point2D getNextPosition(Point2D position) {
                return null;
            }

            @Override
            public Optional<Rectangle> getBoundingBox(Point2D position) {
                return Optional.empty();
            }
        };

        assertTrue(cut.isAbsolute());
    }

    @Test
    public void itIsPossibleToDetermineIfTheCommandIsRelative() throws PathException {
        assertFalse(cut.isAbsolute());
    }

    @Test (expected = PathException.class)
    public void aPathExceptionWillBeThrownIfNullValuesAreProvidedWhenDeterminingTheDistanceX() throws PathException {
        cut.getDistanceX(null, null);
    }

    @Test
    public void theDistanceXCanBeDeterminedForAnyCombinationScenarioOfTwoPoints() throws PathException {
        assertEquals(1.0d, cut.getDistanceX(new Point2D(1.0d, 0.0d), new Point2D(2.0d, 0.0d)), 0.01d);
        assertEquals(0.0d, cut.getDistanceX(new Point2D(2.0d, 0.0d), null), 0.01d);
        assertEquals(0.0d, cut.getDistanceX(null, new Point2D(3.0d, 0.0d)), 0.01d);
    }

    @Test (expected = PathException.class)
    public void aPathExceptionWillBeThrownIfNullValuesAreProvidedWhenDeterminingTheMinimumX() throws PathException {
        cut.getMinX(null, null);
    }

    @Test
    public void theMinimumXCanBeDeterminedForAnyCombinationScenarioOfTwoPoints() throws PathException {
        assertEquals(1.0d, cut.getMinX(new Point2D(1.0d, 0.0d), new Point2D(2.0d, 0.0d)), 0.01d);
        assertEquals(2.0d, cut.getMinX(new Point2D(2.0d, 0.0d), null), 0.01d);
        assertEquals(3.0d, cut.getMinX(null, new Point2D(3.0d, 0.0d)), 0.01d);
    }

    @Test (expected = PathException.class)
    public void aPathExceptionWillBeThrownIfNullValuesAreProvidedWhenDeterminingTheDistanceY() throws PathException {
        cut.getDistanceY(null, null);
    }

    @Test
    public void theDistanceYCanBeDeterminedForAnyCombinationScenarioOfTwoPoints() throws PathException {
        assertEquals(1.0d, cut.getDistanceY(new Point2D(0.0d, 1.0d), new Point2D(0.0d, 2.0d)), 0.01d);
        assertEquals(0.0d, cut.getDistanceY(new Point2D(0.0d, 2.0d), null), 0.01d);
        assertEquals(0.0d, cut.getDistanceY(null, new Point2D(0.0d, 3.0d)), 0.01d);
    }

    @Test (expected = PathException.class)
    public void aPathExceptionWillBeThrownIfNullValuesAreProvidedWhenDeterminingTheMinimumY() throws PathException {
        cut.getMinY(null, null);
    }

    @Test
    public void theMinimumYCanBeDeterminedForAnyCombinationScenarioOfTwoPoints() throws PathException {
        assertEquals(1.0d, cut.getMinY(new Point2D(0.0d, 1.0d), new Point2D(0.0d, 2.0d)), 0.01d);
        assertEquals(2.0d, cut.getMinY(new Point2D(0.0d, 2.0d), null), 0.01d);
        assertEquals(3.0d, cut.getMinY(null, new Point2D(0.0d, 3.0d)), 0.01d);
    }

    // endregion
}