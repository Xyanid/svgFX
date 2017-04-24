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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Xyanid on 08.04.2017.
 */
public class PathCommandTest {

    // region Tests

    @Test
    public void itIsPossibleToDetermineIfTheCommandIsAbsolute() throws PathException {

        final PathCommand cut = new PathCommand(true) {
            @Override
            public Point2D getNextPosition(Point2D position) {
                return null;
            }

            @Override
            public Rectangle getBoundingBox(Point2D position) {
                return null;
            }
        };

        assertTrue(cut.isAbsolute());
    }

    @Test
    public void itIsPossibleToDetermineIfTheCommandIsRelative() throws PathException {

        final PathCommand cut = new PathCommand(false) {
            @Override
            public Point2D getNextPosition(Point2D position) {
                return null;
            }

            @Override
            public Rectangle getBoundingBox(Point2D position) {
                return null;
            }
        };

        assertFalse(cut.isAbsolute());
    }

    // endregion
}