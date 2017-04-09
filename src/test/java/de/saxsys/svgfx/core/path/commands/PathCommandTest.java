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
import org.junit.Before;
import org.junit.Test;

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
    public void setUp() throws Exception {

        cut = new PathCommand() {
            @Override
            public char getAbsoluteName() {
                return 'M';
            }

            @Override
            public char getRelativeName() {
                return 'm';
            }

            @Override
            public Point2D getNextPosition(Point2D position) {
                return null;
            }

            @Override
            public Rectangle getBoundingBox(Point2D position) {
                return null;
            }
        };
    }

    // endregion

    // region Tests

    @Test
    public void itIsPossibleToDetermineIfAGivenCharacterResemblesTheAbsoluteCommandName() {
        assertTrue(cut.isAbsoluteCommand('M').isPresent());
        assertTrue(cut.isAbsoluteCommand('M').get());
        assertTrue(cut.isAbsoluteCommand('m').isPresent());
        assertFalse(cut.isAbsoluteCommand('m').get());
        assertFalse(cut.isAbsoluteCommand('T').isPresent());
    }

    @Test
    public void itIsPossibleToDetermineIfAGivenCharacterResemblesTheRelativeCommandName() {
        assertTrue(cut.isRelativeCommand('M').isPresent());
        assertFalse(cut.isRelativeCommand('M').get());
        assertTrue(cut.isRelativeCommand('m').isPresent());
        assertTrue(cut.isRelativeCommand('m').get());
        assertFalse(cut.isRelativeCommand('T').isPresent());
    }

    @Test
    public void itIsPossibleToDetermineIfAGivenCharacterResemblesTheCommandName() {
        assertTrue(cut.isCommand('m'));
        assertTrue(cut.isCommand('M'));
        assertFalse(cut.isCommand('T'));
    }

    @Test
    public void aStringCanBeStrippedAtFirstOccurrenceOfTheCommandNameRegardlessOfItsCase() {
        assertEquals("20 ", cut.stripCommandName("10 10m20 "));
        assertEquals(" 30 ", cut.stripCommandName("10 20M 30 "));
        assertEquals("10 20T 30 ", cut.stripCommandName("10 20T 30 "));
    }

    // endregion
}