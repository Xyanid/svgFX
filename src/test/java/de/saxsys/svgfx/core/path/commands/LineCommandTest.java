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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Xyanid on 08.04.2017.
 */
public class LineCommandTest {

    @Test
    public void aLineCommandWillHaveTheExpectedNames() throws PathException {

        final LineCommand cut = new LineCommand("10 10");

        assertEquals('L', cut.getAbsoluteName());
        assertEquals('l', cut.getRelativeName());
        assertNotEquals('X', cut.getRelativeName());
        assertNotEquals('x', cut.getRelativeName());
    }
}