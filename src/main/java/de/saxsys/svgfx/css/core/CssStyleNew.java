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

package de.saxsys.svgfx.css.core;


import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.css.definitions.Constants;
import org.w3c.dom.DOMException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class does not directly represent a SVG element but rather a Css element
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class CssStyleNew {

    // region Enumeration

    /**
     * Determines how a character read while parsing css text is processed.
     */
    private enum ParsingState {
        /**
         * Meaning the character will be parsed as correct data.
         */
        DATA,
        /**
         * Meaning the character will be parsed as a comment, meaning it will not be used.
         */
        COMMENT,
        /**
         * Meaning the character will be parsed as a string.
         */
        STRING
    }

    // endregion

    //region Fields

    private String name;

    private final Map<String, CssContentTypeBase> properties;

    //endregion

    //region Constructor

    /**
     * Creates a new instance.
     */
    public CssStyleNew() {
        super();

        properties = new HashMap<>();
    }

    /**
     * Creates a new instance.
     *
     * @param name the name to of this style.
     */
    public CssStyleNew(final String name) {
        this();

        this.name = name;
    }

    //endregion

    //region Getter

    /**
     * @return the {@link #name}.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the {@link CssStyleNew#properties} as an unmodifiable list.
     *
     * @return the {@link CssStyleNew#properties} as an unmodifiable list.
     */
    public final Map<String, CssContentTypeBase> getUnmodifiableProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Creates at new {@link CssContentTypeBase} based on the given name.
     *
     * @param name name of the property
     *
     * @return a new {@link CssContentTypeBase}.
     */
    protected abstract CssContentTypeBase createContentType(final String name);

    /**
     * Returns the {@link CssContentTypeBase} in the given map of properties using the provided key or null if no such content type exist.
     *
     * @param name name of the property
     *
     * @return the {@link CssContentTypeBase} in the given map or null.
     */
    public final CssContentTypeBase getCssContentType(final String name) {
        return properties.get(name);
    }

    // endregion

    // region Public

    /**
     * Returns the {@link CssContentTypeBase} in the given map of properties as the desired type using the provided key or null if no such content type exist.
     *
     * @param <TContent> type of the content desired.
     * @param name       name of the property
     * @param clazz      class of the type of the property used for casting.
     *
     * @return the {@link CssContentTypeBase} in the given map or null.
     */
    public final <TContent extends CssContentTypeBase> TContent getCssContentType(final String name, final Class<TContent> clazz) {
        return clazz.cast(properties.get(name));
    }

    /**
     * Combines this {@link CssStyleNew} with the given {@link CssStyleNew}. Note that if the provided style has a {@link CssValue} which is also present in this style, then this value will be used
     * instead overwritting the existing one in this style.
     *
     * @param style the {@link CssStyleNew} which is be used, must not be null.
     *
     * @throws IllegalArgumentException if the given {@link CssStyleNew} is null.
     */
    public final void combineWithStyle(final CssStyleNew style) {

        if (style == null) {
            throw new IllegalArgumentException("given style must not be null");
        }

        if (this == style) {
            return;
        }

        for (Map.Entry<String, CssContentTypeBase> entry : style.getUnmodifiableProperties().entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
    }

    //endregion

    // region Abstract

    /**
     * Consumes the given css text and set the style. the css text must follow the default rules of a css style.
     */
    public final void parseCssText(final String cssText) throws DOMException {

        StringBuilder dataBuilder = new StringBuilder();

        boolean isInsideDeclarationBlock = false;

        ParsingState state = ParsingState.DATA;

        for (int i = 0; i < cssText.length(); i++) {

            char character = cssText.charAt(i);

            // if we are in a comment do not need to do anything
            if (state == ParsingState.COMMENT) {
                // in this case we are at the end of the comment section
                if (character == Constants.COMMENT_TAG && i > 0 && cssText.charAt(i - 1) == Constants.COMMENT_INDICATOR) {
                    state = ParsingState.DATA;
                }
                continue;
            }

            // we are reading a string indicator so we either enter the string or we are leaving it
            if (character == Constants.STRING_INDICATOR) {

                if (state != ParsingState.STRING) {
                    state = ParsingState.STRING;
                } else {
                    state = ParsingState.DATA;
                }
            }

            // if we are not within a string every character is consumed since they are part of the value
            if (state != ParsingState.STRING) {

                // in this case the comment section started
                if (character == Constants.COMMENT_TAG && i < cssText.length() - 1 && cssText.charAt(i + 1) == Constants.COMMENT_INDICATOR) {
                    state = ParsingState.COMMENT;
                    continue;
                }

                // if the declaration block starts we set the current data as the name of the css style
                if (character == Constants.DECLARATION_BLOCK_START) {
                    name = dataBuilder.toString().trim();
                    isInsideDeclarationBlock = true;
                    dataBuilder.setLength(0);

                    // we have found the end of a property so we consume if if possible and add it
                } else if (character == Constants.PROPERTY_END) {

                    String property = dataBuilder.toString();

                    int index = property.indexOf(Constants.PROPERTY_SEPARATOR);

                    if (index > -1 && index < property.length() - 1) {

                        String name = property.substring(0, index).trim();

                        CssContentTypeBase content = createContentType(name);

                        content.consumeCssValue(property.substring(index + 1).trim());

                        properties.put(name, content);
                    }

                    dataBuilder.setLength(0);

                    // at this point we have reached the end of the style and stop doing what ever we did
                } else if (character == Constants.DECLARATION_BLOCK_END) {

                    isInsideDeclarationBlock = false;
                    break;
                }
            }
            // we are inside a string in which case we only add data if we are inside the declaration block already
            else if (isInsideDeclarationBlock) {
                dataBuilder.append(character);
            }
        }

        if (isInsideDeclarationBlock) {
            throw new SVGException("css text not properly closed");
        }
    }

    // endregion

    //region Override Object

    /**
     * Gets the HashCode this object, which is based on the {@link #name}.
     *
     * @return the HashCode of the {@link #name} of this object
     */
    @Override public int hashCode() {
        return name == null ? super.hashCode() : name.hashCode();
    }

    /**
     * Determines if the given object equals the provided object.
     * This is based on the {@link #name} of object.
     *
     * @param obj, object to compare to
     *
     * @return true if the object is the same otherwise false
     */
    @Override public boolean equals(final Object obj) {

        boolean result = super.equals(obj);

        if (!result && obj != null && CssStyleNew.class.isAssignableFrom(obj.getClass())) {
            CssStyleNew other = (CssStyleNew) obj;

            result = name == null ? other.getName() == null : name.equals(other.getName());
        }

        return result;
    }

    //endregion
}
