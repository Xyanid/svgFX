package de.saxsys.svgfx.css.definitions;

/**
 * Contains all constants related to css. Created by Xyanid on 29.10.2015.
 */
public final class Constants {

    // region CssRule

    /**
     * Determines the character with which a id selector can start.
     */
    public static final char ID_SELECTOR = '#';

    /**
     * Determines the character with which a id selector can start.
     */
    public static final String ID_SELECTOR_STRING = String.valueOf(ID_SELECTOR);

    /**
     * Determines the character with which a class selector can start.
     */
    public static final char CLASS_SELECTOR = '.';

    /**
     * Determines the character with which a class selector can start.
     */
    public static final String CLASS_SELECTOR_STRING = String.valueOf(CLASS_SELECTOR);

    // endregion

    // region Declaration Block

    /**
     * Determines the character which indicates the start of a declarations.
     */
    public static final char DECLARATION_BLOCK_START = '{';

    /**
     * Determines the character which indicates the start of a declarations.
     */
    public static final String DECLARATION_BLOCK_START_STRING = String.valueOf(DECLARATION_BLOCK_START);

    /**
     * Determines the character which indicates the end of the declarations.
     */
    public static final char DECLARATION_BLOCK_END = '}';

    /**
     * Determines the character which indicates the end of the declarations.
     */
    public static final String DECLARATION_BLOCK_END_STRING = String.valueOf(DECLARATION_BLOCK_END);

    // endregion

    // region Property

    /**
     * Determines the character which indicates the separator of a declaration.
     */
    public static final char PROPERTY_SEPARATOR = ':';

    /**
     * Determines the character which indicates the separator of a declaration.
     */
    public static final String PROPERTY_SEPARATOR_STRING = String.valueOf(PROPERTY_SEPARATOR);

    /**
     * Determines the character which indicates the end of a declaration.
     */
    public static final char PROPERTY_END = ';';

    /**
     * Determines the character which indicates the end of a declaration.
     */
    public static final String PROPERTY_END_STRING = String.valueOf(PROPERTY_END);

    /**
     * Indicator which determines that instead of an actual value value, another value is referenced.
     */
    public static final String PROPERTY_VALUE_REFERENCE_URL = "url(#";

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

    /**
     * Indicates the start or end of a string value.
     */
    public static final String STRING_INDICATOR_STRING = String.valueOf(STRING_INDICATOR);

    // endregion

    // region Constructor

    private Constants() {

    }

    // endregion
}
