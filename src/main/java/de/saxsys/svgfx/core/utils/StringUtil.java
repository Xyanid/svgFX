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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides functionality to handle Strings
 *
 * @author by Xyanid on 28.10.2015.
 */
public final class StringUtil {

    // region Classes

    /**
     * This interface allows that {@link #splitByDelimiters(String, List)} to consume data an indicate whether to further add new characters or consume
     * the currentData.
     */
    @FunctionalInterface
    public interface SplitPredicate {
        /**
         * Called when a delimiter or the last character is read and will indicate whether the current read data can be used or not.
         *
         * @param data  the {@link String} containing the currently read data so far.
         * @param index the index of the character in the original data that was currently read
         *
         * @return true if the currentData shall be consumed otherwise false if not.
         *
         * @throws SVGException in case the splitting is not possible due to invalid data ans so on.
         */
        boolean test(final String data, int index) throws SVGException;
    }

    // endregion

    // region Constructor

    /**
     *
     */
    private StringUtil() {

    }

    // endregion

    //region Methods

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
    public static boolean isNullOrEmpty(final String data) {
        return data == null || data.isEmpty();
    }

    /**
     * Determines if the given {@link String} is not null or empty.
     *
     * @param data the {@link String} to check.
     *
     * @return true if the {@link String} is not null or empty otherwise false.
     */
    public static boolean isNotNullOrEmpty(final String data) {
        return !isNullOrEmpty(data);
    }

    /**
     * Strips the " characters at the start and end of the given string if present.
     *
     * @param data data to be used, must not be null or empty
     *
     * @return the data without " character at the start and end.
     *
     * @throws IllegalArgumentException if the given data is null or empty
     */
    public static String stripStringIndicators(final String data) {
        if (isNullOrEmpty(data)) {
            throw new IllegalArgumentException("given data must not be null or empty");
        }

        return data.substring(data.charAt(0) == '"' ? 1 : 0, data.charAt(data.length() - 1) == '"' ? data.length() - 1 : data.length());
    }

    /**
     * Creates a new {@link List} of {@link String}s that contain the data, which was split by any of the given delimiters.
     *
     * @param data       the data that needs to be split.
     * @param delimiters the delimiters that indicate when the string needs to be split.
     *
     * @return a new {@link List} of {@link String}s that contain the data, which was split by any of the given delimiters.
     *
     * @throws SVGException when an error occurs during the splitting.
     */
    public static List<String> splitByDelimiters(final String data, final List<Character> delimiters) throws SVGException {
        return splitByDelimiters(data, delimiters, (split, index) -> true);
    }

    /**
     * Creates a new {@link List} of {@link String}s that contain the data, which was split by any of the given delimiters.
     *
     * @param data             the data that needs to be split.
     * @param delimiters       the delimiters that indicate when the string needs to be split.
     * @param consumePredicate the {@link SplitPredicate} to use, which will determine if the currently split value will be part of the result.
     *
     * @return a new {@link List} of {@link String}s that contain the data, which was split by any of the given delimiters.
     *
     * @throws SVGException when an error occurs during the splitting.
     */
    public static List<String> splitByDelimiters(final String data, final List<Character> delimiters, final SplitPredicate consumePredicate) throws SVGException {

        final List<String> result = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();

        boolean isLastCharacter;
        boolean isDelimiter;

        for (int i = 0; i < data.length(); i++) {
            char character = data.charAt(i);

            isDelimiter = delimiters.contains(character);
            isLastCharacter = i == data.length() - 1;

            if (isLastCharacter || isDelimiter) {

                if (isLastCharacter && !isDelimiter) {
                    builder.append(character);
                }

                if (builder.length() > 0 && consumePredicate.test(builder.toString(), i)) {
                    result.add(builder.toString());
                    builder.setLength(0);
                }
            } else {
                builder.append(character);
            }
        }

        return result;
    }

    //endregion
}
