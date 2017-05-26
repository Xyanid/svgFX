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

import de.saxsys.svgfx.core.path.commands.CommandFactory;
import javafx.scene.shape.Rectangle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Xyanid on 08.05.2017.
 */
public class CommandParserIntegrationTest {

    // region Fields

    private CommandParser cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        final CommandFactory commandFactory = new CommandFactory();

        cut = new CommandParser(commandFactory);
    }

    // endregion

    // region Tests

    @Test (expected = PathException.class)
    public void ifThePathCommandIsEmptyAnPathExceptionWillBeThrown() throws PathException {
        cut.getBoundingBox(null);
    }

    @Test (expected = PathException.class)
    public void ifThePathCommandContainsInvalidDataAPathExceptionWillBeThrown() throws PathException {
        cut.getBoundingBox("AB");
    }

    @Test
    public void aBoundingBoxCanBeRetrievedFromAPath() throws PathException {
        final Rectangle result = cut.getBoundingBox("M 10 5 L 30 20");

        assertEquals(10.0d, result.getX(), 0.01d);
        assertEquals(5.0d, result.getY(), 0.01d);
        assertEquals(20.0d, result.getWidth(), 0.01d);
        assertEquals(15.0d, result.getHeight(), 0.01d);
    }

    @Test
    public void relativeAndAbsoluteCommandCanBeUsedInAPath() throws PathException {
        final Rectangle result = cut.getBoundingBox("M 10 5 l 30 20 V 50 h -20 L 10 5");

        assertEquals(10.0d, result.getX(), 0.01d);
        assertEquals(5.0d, result.getY(), 0.01d);
        assertEquals(30.0d, result.getWidth(), 0.01d);
        assertEquals(45.0d, result.getHeight(), 0.01d);
    }

    @Test
    public void onlyNonMoveCommandsWillAffectTheBoundingBox() throws PathException {
        final Rectangle result = cut.getBoundingBox("M 10 5 M 50 5 L 75 10");

        assertEquals(50.0d, result.getX(), 0.01d);
        assertEquals(5.0d, result.getY(), 0.01d);
        assertEquals(25.0d, result.getWidth(), 0.01d);
        assertEquals(5.0d, result.getHeight(), 0.01d);
    }

    @Test
    public void aCloseCommandWillNotUseThePositionsDefinedByMoveCommands() throws PathException {
        final Rectangle result = cut.getBoundingBox("M 60 5 L 75 10 V 20 Z");

        assertEquals(60.0d, result.getX(), 0.01d);
        assertEquals(5.0d, result.getY(), 0.01d);
        assertEquals(15.0d, result.getWidth(), 0.01d);
        assertEquals(15.0d, result.getHeight(), 0.01d);
    }

    @Test
    public void aCloseCommandAtTheStartWillNotAffectTheBoundingBox() throws PathException {
        final Rectangle result = cut.getBoundingBox("Z M 5 10 L 15 30");

        assertEquals(5.0d, result.getX(), 0.01d);
        assertEquals(10.0d, result.getY(), 0.01d);
        assertEquals(10.0d, result.getWidth(), 0.01d);
        assertEquals(20.0d, result.getHeight(), 0.01d);
    }

    // endregion
}