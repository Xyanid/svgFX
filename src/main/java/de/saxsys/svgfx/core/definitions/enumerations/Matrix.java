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

/**
 * Determines which keyword in a transform attribute of a matrix map to their corresponding javafx classes.
 *
 * @author Xyanid on 01.02.2017.
 */
public enum Matrix {

    NONE(""),
    MATRIX("matrix"),
    TRANSLATE("translate"),
    SCALE("scale"),
    ROTATE("rotate"),
    SKEW_X("skewX"),
    SKEW_Y("skewY");

    // region Fields

    /**
     * The of the transformation within svg.
     */
    private final String name;


    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param name the name of the attribute within the svg element.
     */
    Matrix(final String name) {
        this.name = name;
    }

    // endregion

    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return the {@link #name}
     */
    public final String getName() {
        return name;
    }

    // endregion
}
