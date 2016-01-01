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
 *
 * @author by Xyanid on 28.10.2015.
 */
public final class StringUtils {

    // region Constructor

    /**
     * Replaces the last occurrence of a character in the given source.
     *
     * @param source    source which contains the data to be used
     * @param toReplace the character that is to be replaced
     * @param toUse     the character that is used instead
     *
     * @return a new {@link String} where last occurrence of the given character is replaced by the provided character or the original string if its null or does not contain the character
     */
    public static String replaceLast(final String source, final char toReplace, final char toUse) {

        return replaceLast(source, String.valueOf(toReplace), String.valueOf(toUse));
    }

    // endregion

    /**
     * Replaces the last occurrence of a character in the given source.
     *
     * @param source    source which contains the data to be used
     * @param toReplace the character that is to be replaced
     * @param toUse     the character that is used instead
     *
     * @return a new {@link String} where last occurrence of the given character is replaced by the provided character or the original string if its null or does not contain the character
     */
    public static String replaceLast(final String source, final String toReplace, final String toUse) {

        if (source == null) {
            return null;
        }

        int lastIndex = source.lastIndexOf(toReplace);

        if (lastIndex < 0) {
            return source;
        }

        return String.format("%s%s", source.substring(0, lastIndex), source.substring(lastIndex).replaceFirst(toReplace, toUse));
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
     * * Strips the " characters at the start and end of the given string if present.
     *
     * @param data data to be used, must not be null or empty
     *
     * @return the data without " character at the start and end.
     *
     * @throws IllegalArgumentException if the given data is null or empty
     */
    public static String stripStringIndicators(String data) {
        if (isNullOrEmpty(data)) {
            throw new IllegalArgumentException("given data must not be null or empty");
        }

        return data.substring(data.charAt(0) == '"' ? 1 : 0, data.charAt(data.length() - 1) == '"' ? data.length() - 1 : data.length());
    }

    /**
     *
     */
    private StringUtils() {

    }
}
