/*
 * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.core.definitions;

/**
 * Contains constant values of
 *
 * @author Xyanid on 01.11.2015.
 */
public final class Constants {

    //region Fields

    /**
     * Indicator which determines that instead of an actual value value, another value is referenced.
     */
    public static final String IRI_IDENTIFIER = "url(#";

    /**
     * Indicator which determines that instead of an actual value value, another value is referenced.
     */
    public static final String IRI_FRAGMENT_IDENTIFIER = "#";

    /**
     * Determines the delimiter that separated a pair of points.
     */
    public static char WHITESPACE = ' ';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    public static char COMMA = ',';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    public static String COMMA_STRING = String.valueOf(COMMA);

    //endregion

    // region Constructor

    /**
     *
     */
    private Constants() {

    }

    // endregion
}