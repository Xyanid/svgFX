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

import java.util.Arrays;
import java.util.Collection;

/**
 * Contains the known names of path command
 *
 * @author Xyanid on 30.04.2017.
 */
public enum CommandName {
    MOVE('M'),
    LINE('L'),
    CLOSE('Z'),
    HORIZONTAL_LINE('H'),
    VERTICAL_LINE('L'),
    CUBIC_BEZIER_CURVE('C'),
    SHORT_CUBIC_BEZIER_CURVE('S'),
    QUADRATIC_BEZIER_CURVE('Q'),
    SHORT_QUADRATIC_BEZIER_CURVE('T');

    // region Fields

    private final char absoluteName;

    private final char relativeName;

    // endregion


    // region Constructor

    CommandName(final char name) {
        this.absoluteName = Character.toUpperCase(name);
        this.relativeName = Character.toLowerCase(name);
    }

    // endregion

    // region Getter

    public boolean isCommandName(final char name) {
        return absoluteName == name || relativeName == name;
    }

    /**
     * Returns the {@link #absoluteName}.
     *
     * @return the {@link #absoluteName}.
     */
    public char getAbsoluteName() {
        return absoluteName;
    }

    /**
     * Returns the {@link #relativeName}.
     *
     * @return the {@link #relativeName}.
     */
    public char getRelativeName() {
        return relativeName;
    }

    /**
     * Returns a list containing the {@link #absoluteName} and the {@link #relativeName}.
     *
     * @return a new list containing the {@link #absoluteName} and the {@link #relativeName},
     */
    public Collection<Character> getNames() {
        return Arrays.asList(absoluteName, relativeName);
    }

    // endregion
}
