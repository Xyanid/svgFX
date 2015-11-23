package de.saxsys.svgfx.core.utils;

/**
 * This class provides functionality to handle Strings
 * Created by Xyanid on 28.10.2015.
 */
public final class StringUtils {

    // region Constructor

    /**
     *
     */
    private StringUtils() {

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
    public static String replaceLast(final String source, final char from, final char to) {

        return replaceLast(source, String.valueOf(from), String.valueOf(to));
    }

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


}
