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

package de.saxsys.svgfx.core.definitions.enumerations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Determine which element string represents a certain command in an svg path.
 *
 * @author Xyanid on 01.02.2017.
 */
public enum PathCommand {

    /**
     * Indicates that the element in a path is a move command
     */
    MOVE('m'),
    /**
     * Indicates that the element in a path is a line command.
     */
    LINE('l'),
    /**
     * Indicates that the element in a path is a line horizontal command.
     */
    LINE_HORIZONTAL('h'),
    /**
     * Indicates that the element in a path is a line vertical command.
     */
    LINE_VERTICAL('v'),
    /**
     * Indicates that the element in a path is a close command.
     */
    CLOSE('z'),
    /**
     * Indicates that the element in a path is a cubic bezier command.
     */
    BEZIER_CUBIC('c'),
    /**
     * Indicates that the element in a path is a connection for a cubic bezier command.
     */
    BEZIER_CUBIC_CONTINUE('s'),
    /**
     * Indicates that the element in a path is a quadratic bezier command.
     */
    BEZIER_QUADRATIC('q'),
    /**
     * Indicates that the element in a path is a connection for quadratic bezier command.
     */
    BEZIER_QUADRATIC_CONTINUE('t'),
    /**
     * Indicates that the element in a path is a arc command.
     */
    ARC('a');

    public static final List<PathCommand> PATH_COMMANDS = Collections.unmodifiableList(Arrays.asList(PathCommand.values()));

    // region Fields

    /**
     * The of the command within the path.
     */
    private final char name;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param name the name of the attribute within the svg element.
     */
    PathCommand(final char name) {
        this.name = name;
    }

    // endregion

    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return the {@link #name}
     */
    public final char getName() {
        return name;
    }

    // endregion
}