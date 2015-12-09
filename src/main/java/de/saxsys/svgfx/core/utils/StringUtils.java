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

package de.saxsys.svgfx.core.utils;

/**
 * This class provides functionality to handle Strings
 * Created by Xyanid on 28.10.2015.
 */
public final class StringUtils {

    // region Constructor

    /**
     * Replaces the last occurrence of a character in the given source.
     *
     * @param source source which contains the data to be used
     * @param from   the character that is to be replaced
     * @param to     the character that is used instead
     *
     * @return a new {@link String} where last occurrence of the given character is replaced by the provided character or the original string if its null or does not contain the character
     */
    public static String replaceLast(final String source, final char from, final char to) {

        return replaceLast(source, String.valueOf(from), String.valueOf(to));
    }

    // endregion

    /**
     * Replaces the last occurrence of a character in the given source.
     *
     * @param source source which contains the data to be used
     * @param from   the character that is to be replaced
     * @param to     the character that is used instead
     *
     * @return a new {@link String} where last occurrence of the given character is replaced by the provided character or the original string if its null or does not contain the character
     */
    public static String replaceLast(final String source, final String from, final String to) {

        if (source == null) {
            return null;
        }

        int lastIndex = source.lastIndexOf(from);

        if (lastIndex < 0) {
            return source;
        }

        return String.format("%s%s", source.substring(0, lastIndex), source.substring(lastIndex).replaceFirst(from, to));
    }

    /**
     * Determines if the given {@link String} is null or empty.
     *
     * @param data the {@link String} to check.
     *
     * @return true if the {@link String} is null or empty otherwise false.
     */
    public static boolean isNullOrEmpty(String data) {
        return data == null || data.isEmpty();
    }

    /**
     * Determines if the given {@link String} is not null or empty.
     *
     * @param data the {@link String} to check.
     *
     * @return true if the {@link String} is not null or empty otherwise false.
     */
    public static boolean isNotNullOrEmpty(String data) {
        return !isNullOrEmpty(data);
    }

    /**
     *
     */
    private StringUtils() {

    }
}
