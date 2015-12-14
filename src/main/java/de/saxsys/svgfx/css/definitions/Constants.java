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
 * Contains all constants related to css. @author Xyanid on 29.10.2015.
 */
public final class Constants {

    // region Declaration Block

    /**
     * Determines the character which indicates the start of a declarations.
     */
    public static final char DECLARATION_BLOCK_START = '{';

    /**
     * Determines the character which indicates the end of the declarations.
     */
    public static final char DECLARATION_BLOCK_END = '}';

    // endregion

    // region Property

    /**
     * Determines the character which indicates the separator of a declaration.
     */
    public static final char PROPERTY_SEPARATOR = ':';

    /**
     * Determines the character which indicates the end of a declaration.
     */
    public static final char PROPERTY_END = ';';

    /**
     * Determines the character which indicates the end of a declaration.
     */
    public static final String PROPERTY_END_STRING = String.valueOf(PROPERTY_END);

    /**
     * Indicator which determines that the value of the property is set to none.
     */
    public static final String PROPERTY_VALUE_NONE = "none";

    // endregion

    // region Content

    /**
     * Indicates the start of a comment section.
     */
    public static final char COMMENT_TAG = '/';

    /**
     * Indicates the start of a comment section.
     */
    public static final char COMMENT_INDICATOR = '*';

    /**
     * Indicates the start or end of a string value.
     */
    public static final char STRING_INDICATOR = '\"';

    // endregion

    // region Constructor

    private Constants() {

    }

    // endregion
}
