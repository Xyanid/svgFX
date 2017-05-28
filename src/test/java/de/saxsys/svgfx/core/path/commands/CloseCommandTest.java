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

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Xyanid on 14.05.2017.
 */
public class CloseCommandTest {

    @Test
    public void theNextPositionWillAlwaysBeTheSameAsTheStartingPosition() throws Exception {

        final double multiplier = 10.0d;

        for (int i = 0; i < 20; ++i) {
            final Point2D expected = new Point2D(Math.random() * multiplier, Math.random() * multiplier);

            assertEquals(expected, new CloseCommand(false, expected).getAbsoluteEndPoint(new Point2D(Math.random() * multiplier, Math.random() * multiplier)));
        }
    }

    @Test
    public void noBoundingBoxWillBeCreated() throws Exception {

        final double multiplier = 10.0d;

        for (int i = 0; i < 10; ++i) {
            final Point2D position = new Point2D(Math.random() * multiplier,
                                                 Math.random() * multiplier);

            final Optional<Rectangle> boundingBox = new CloseCommand(i % 2 == 0, new Point2D(Math.random() * multiplier, Math.random() * multiplier)).getBoundingBox(position);

            assertFalse(boundingBox.isPresent());
        }
    }
}