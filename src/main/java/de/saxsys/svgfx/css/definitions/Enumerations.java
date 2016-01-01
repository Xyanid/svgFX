/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.css.definitions;

/**
 * This file contains all the enumerations that are used for parsing svg data.
 *
 * @author Xyanid on 01.11.2015.
 */
public final class Enumerations {

    /**
     * Determines which type of length unit a css value is.
     */
    public enum CssValueLengthType {
        /**
         * Meaning the length type is not specified.
         */
        NONE(""),
        /**
         * Meaning the value is defined in pixel.
         */
        PIXEL("px"),
        /**
         * Meaning the value is defined in centimeters.
         */
        CENTIMETER("cm"),
        /**
         * Meaning the value is defined in millimeters.
         */
        MILLIMETER("mm"),
        /**
         * Meaning the value is defined in inches.
         */
        INCH("in"),
        /**
         * Meaning the value is defined in points.
         */
        POINT("pt"),
        /**
         * Meaning the value is defined in picas.
         */
        PICAS("pc"),
        /**
         * Meaning the value is defined in percentage.
         */
        PERCENT("%"),
        /**
         * Meaning the value is defined in relative size to its current size.
         */
        RELATIVE_SELF("em"),
        /**
         * Meaning the value is defined in relative size to its current height.
         */
        RELATIVE_HEIGHT("ex"),
        /**
         * Meaning the value is defined in relative size to its current width.
         */
        RELATIVE_WIDTH("ch"),
        /**
         * Meaning the value is defined in relative size to its current width.
         */
        RELATIVE_ROOT("rem"),
        /**
         * Meaning the value is defined in relative size to its current viewport width.
         */
        RELATIVE_VIEW_WIDTH("vw"),
        /**
         * Meaning the value is defined in relative size to its current viewport height.
         */
        RELATIVE_VIEW_HEIGHT("vh"),
        /**
         * Meaning the value is defined in relative size to its current viewport smallest dimension.
         */
        RELATIVE_VIEW_MIN("vmin"),
        /**
         * Meaning the value is defined in relative size to its current viewport largest dimension.
         */
        RELATIVE_VIEW_MAX("vmax");

        // region Fields

        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance of the {@link CssValueLengthType#name} with the given name.
         *
         * @param name the name to be used.
         */
        CssValueLengthType(final String name) {
            this.name = name;
        }

        // endregion


        // region Getter

        /**
         * Returns the {@link CssValueLengthType#name}.
         *
         * @return the {@link CssValueLengthType#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    // region Constructor

    private Enumerations() {

    }

    // endregion
}
