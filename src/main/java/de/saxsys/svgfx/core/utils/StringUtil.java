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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.interfaces.ThrowableBiConsumer;
import de.saxsys.svgfx.core.interfaces.ThrowablePredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class provides functionality to handle Strings
 *
 * @author by Xyanid on 28.10.2015.
 */
public final class StringUtil {

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
     * Determines if the given {@link String} is null or empty even after bring trimmed.
     *
     * @param data the {@link String} to check.
     *
     * @return true if the {@link String} is null or empty after trimmed otherwise false.
     */
    public static boolean isNullOrEmptyAfterTrim(final String data) {
        return isNullOrEmpty(data) || isNullOrEmpty(data.trim());
    }

    /**
     * Determines if the given {@link String} is not null or empty even after bring trimmed.
     *
     * @param data the {@link String} to check.
     *
     * @return true if the {@link String} is not null or empty after trimmed otherwise false.
     */
    public static boolean isNotNullOrEmptyAfterTrim(final String data) {
        return !isNullOrEmptyAfterTrim(data);
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
     * The delimiters are not part of the items in the list.
     *
     * @param data             the data that needs to be split.
     * @param delimiters       the delimiters that indicate when the string needs to be split.
     * @param consumePredicate the {@link ThrowablePredicate} to use, which will determine if the currently split value will be part of the result.
     *
     * @return a new {@link List} of {@link String}s that contain the data, which was split by any of the given delimiters, delimiters are not present in the list.
     *
     * @throws E when an error occurs during the splitting.
     */
    public static <E extends Exception> List<String> splitByDelimiters(final String data, final Collection<Character> delimiters, final ThrowablePredicate<String, E> consumePredicate) throws E {

        final List<String> result = new ArrayList<>();

        splitByDelimiters(data, delimiters, (delimiter, split) -> {
            if (consumePredicate.testOrFail(split)) {
                result.add(split);
            }
        });

        return result;
    }

    /**
     * Parse the given data and split the string at every position that is a one of the provided delimiters.
     * The data that has been read so far and the last know delimiter will be consumed by the {@link ThrowableBiConsumer}. Unless this function returns true, the data will be accumulated.
     * <p>
     * Imaging the following string with T and R being the sole delimiters. The following would happen:
     * <pre>
     * <table>
     * <tr>
     * <th>Provided data</th>
     * <th>Consumption</th>
     * <th>Delimiter</th>
     * <th>Data</th>
     * </tr>
     * <tr>
     * <td>"T"</td>
     * <td>1.</td>
     * <td>"T"</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>"123"</td>
     * <td>1.</td>
     * <td>null</td>
     * <td>"123"</td>
     * </tr>
     * <tr>
     * <td>"T123"</td>
     * <td>1.</td>
     * <td>"T"</td>
     * <td>"123"</td>
     * </tr>
     * <tr>
     * <td>"T123R"</td>
     * <td>1.</td>
     * <td>"T"</td>
     * <td>"123"</td>
     * </tr>
     * <tr>
     * <td></td>
     * <td>2.</td>
     * <td>"R"</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>"T123R567"</td>
     * <td>1.</td>
     * <td>"T"</td>
     * <td>"123"</td>
     * </tr>
     * <tr>
     * <td></td>
     * <td>2.</td>
     * <td>"R"</td>
     * <td>"567"</td>
     * </tr>
     * <tr>
     * <td>"T123R567T"</td>
     * <td>1.</td>
     * <td>"T"</td>
     * <td>"123"</td>
     * </tr>
     * <tr>
     * <td></td>
     * <td>2.</td>
     * <td>"R"</td>
     * <td>"567"</td>
     * </tr>
     * <tr>
     * <td></td>
     * <td>3.</td>
     * <td>"T"</td>
     * <td>null</td>
     * </tr>
     * </table>
     * </pre>
     *
     * @param data          the data that needs to be split.
     * @param delimiters    the delimiters that indicate when the string needs to be split.
     * @param splitConsumer the {@link ThrowableBiConsumer} which will consumeOrFail each split action.
     *
     * @throws E when an error occurs during the splitting.
     */
    public static <E extends Exception> void splitByDelimiters(final String data, final Collection<Character> delimiters, final ThrowableBiConsumer<Character, String, E> splitConsumer) throws E {

        final StringBuilder builder = new StringBuilder();

        boolean isLastCharacter;
        boolean isDelimiter;
        Character lastFoundDelimiter = null;

        for (int i = 0; i < data.length(); i++) {
            char character = data.charAt(i);

            isDelimiter = delimiters.contains(character);
            isLastCharacter = i == data.length() - 1;

            if (isLastCharacter || isDelimiter) {

                // needed in case we reached the end we need to apply the last character to the builder
                if (isLastCharacter && !isDelimiter) {
                    builder.append(character);
                }

                // we only consumeOrFail if there is data or we have a previous delimiter found
                if ((lastFoundDelimiter != null || builder.length() > 0)) {
                    splitConsumer.acceptOrFail(lastFoundDelimiter, builder.toString());
                }

                // reset data
                lastFoundDelimiter = null;
                builder.setLength(0);

                // if this character was a delimiter we will use it as the last know delimiter
                if (isDelimiter) {
                    lastFoundDelimiter = character;
                }
            } else {
                builder.append(character);
            }
        }

        // special case in which the string is only one of the delimiters, so we consumeOrFail the delimiter with null data
        if (lastFoundDelimiter != null) {
            splitConsumer.acceptOrFail(lastFoundDelimiter, null);
        }
    }

    //endregion
}
