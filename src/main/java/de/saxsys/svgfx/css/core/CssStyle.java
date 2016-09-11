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

package de.saxsys.svgfx.css.core;


import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.definitions.Constants;
import de.saxsys.svgfx.xml.attribute.AttributeHolder;
import de.saxsys.svgfx.xml.attribute.AttributeType;
import javafx.util.Pair;
import org.w3c.dom.DOMException;

import java.util.Map;

/**
 * This Class does not directly represent a SVG element but rather a Css element
 *
 * @param <TAttributeHolder> type of the {@link AttributeHolder} of this style.
 * @param <TAttributeType>       type of the {@link AttributeType} of this style.
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class CssStyle<TAttributeType extends AttributeType, TAttributeHolder extends AttributeHolder<TAttributeType>> {

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

    /**
     * Determines what kind of selector is used in the style if any.
     */
    private enum Selector {
        NONE("."),
        CLASS("."),
        ID("#");

        // region Fields

        private final String name;

        // endregion

        // region Constructor

        Selector(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        public String getName() {
            return name;
        }

        // endregion
    }

    // endregion

    //region Fields

    /**
     * Determines the selector of the style
     */
    private Selector selector;
    /**
     * The name of the style excluding the class or id selector
     */
    private String name;
    /**
     * Contains all the attributes of this {@link CssStyle}.
     */
    private final TAttributeHolder attributeHolder;

    //endregion

    //region Constructor

    /**
     * Creates a new instance.
     */
    public CssStyle(final TAttributeHolder attributeHolder) {
        this.attributeHolder = attributeHolder;
    }

    /**
     * Creates a new instance.
     *
     * @param name the name to of this style.
     */
    public CssStyle(final String name, final TAttributeHolder attributeHolder) {
        this.name = name;
        this.attributeHolder = attributeHolder;
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
     * Return the {@link #attributeHolder}.
     *
     * @return the {@link #attributeHolder}.
     */
    public final TAttributeHolder getAttributeHolder() {
        return attributeHolder;
    }

    /**
     * Returns the {@link #attributeHolder}s {@link AttributeHolder#getAttributes()}.
     *
     * @return the {@link #attributeHolder}s {@link AttributeHolder#getAttributes()}.
     */
    public final Map<String, TAttributeType> getProperties() {
        return attributeHolder.getAttributes();
    }

    /**
     * Sets the {@link #name} also parsing any selector the name might have.
     *
     * @param name the name to use which might contain the selector.
     */
    private void setNameAndSelector(final String name) {
        if (StringUtils.isNotNullOrEmpty(name)) {
            if (name.startsWith(Selector.ID.getName())) {
                selector = Selector.ID;
                this.name = name.replace(Selector.ID.getName(), "");
            } else if (name.startsWith(Selector.CLASS.getName())) {
                selector = Selector.CLASS;
                this.name = name.replace(Selector.CLASS.getName(), "");
            } else {
                selector = Selector.NONE;
                this.name = name;
            }
        } else {
            this.name = name;
        }
    }

    // endregion

    // region Private

    /**
     * Determines the {@link TAttributeType} from the given data.
     *
     * @param data data to be used.
     *
     * @return a new {@link Pair} containing the name of the {@link TAttributeType} as the key and the {@link TAttributeType} as the value;
     */
    private Pair<String, TAttributeType> determineAttributeType(final String data) throws SVGException {
        if (StringUtils.isNullOrEmpty(data)) {
            throw new SVGException("Given data must not be null in order to create a attribute type from it");
        }

        String trimmedData = data.trim();

        int index = trimmedData.indexOf(Constants.PROPERTY_SEPARATOR);

        if (index == -1 || index >= trimmedData.length() - 1) {
            throw new SVGException("Given data either does not provide a attribute type separator separator or is to short");
        }

        String name = trimmedData.substring(0, index).trim();

        TAttributeType attribute = attributeHolder.createAttributeType(StringUtils.stripStringIndicators(name));

        if (attribute != null) {
            String cssText = StringUtils.stripStringIndicators(trimmedData.substring(index + 1).trim());

            try {
                attribute.consumeText(cssText);
            } catch (Exception e) {
                throw new SVGException(String.format("Could not parse %s for attribute type %s", cssText, attribute.getClass().getName()), e);
            }
        }

        return new Pair<>(name, attribute);
    }

    // endregion

    // region Public

    /**
     * Combines this {@link CssStyle} with the given {@link CssStyle}, new {@link AttributeType}s not present in this style will be added.
     *
     * @param style the {@link CssStyle} which is be used, must not be null.
     *
     * @throws IllegalArgumentException if the given {@link CssStyle} is null.
     */
    public final void combineWithStyle(final CssStyle<TAttributeType, TAttributeHolder> style) {

        if (style == null) {
            throw new IllegalArgumentException("given style must not be null");
        }

        if (this == style) {
            return;
        }

        for (Map.Entry<String, TAttributeType> entry : style.attributeHolder.getAttributes().entrySet()) {
            if (!attributeHolder.getAttributes().containsKey(entry.getKey())) {
                attributeHolder.getAttributes().put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Consumes the given css text and set the style. the css text must follow the default rules of a css style.
     *
     * @param cssText the text that is to be consumed, must not be null or empty.
     */
    public final void parseCssText(final String cssText) throws DOMException {

        name = null;
        selector = Selector.NONE;
        attributeHolder.getAttributes().clear();

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
                    setNameAndSelector(dataBuilder.toString().trim());
                    isInsideDeclarationBlock = true;
                    dataBuilder.setLength(0);
                    continue;

                    // we have found the end of a property so we consume if if possible and add it
                } else if (character == Constants.PROPERTY_END) {

                    Pair<String, TAttributeType> property = determineAttributeType(dataBuilder.toString());

                    if (property.getValue() != null) {
                        attributeHolder.getAttributes().put(property.getKey(), property.getValue());
                    }

                    dataBuilder.setLength(0);
                    continue;

                    // at this point we have reached the end of the style and stop doing what ever we did
                } else if (character == Constants.DECLARATION_BLOCK_END) {

                    if (dataBuilder.toString().trim().length() > 0) {
                        Pair<String, TAttributeType> property = determineAttributeType(dataBuilder.toString());

                        if (property.getValue() != null) {
                            attributeHolder.getAttributes().put(property.getKey(), property.getValue());
                        }
                    }

                    isInsideDeclarationBlock = false;
                    break;
                }

                dataBuilder.append(character);
            }
            // we are inside a string in which case we only add data if we are inside the declaration block already
            else if (isInsideDeclarationBlock) {
                dataBuilder.append(character);
            }
        }

        if (isInsideDeclarationBlock) {
            throw new SVGException("Css text not properly closed");
        }
    }

    // endregion

    //region Override Object

    /**
     * Gets the HashCode this object, which is based on the {@link #name}.
     *
     * @return the HashCode of the {@link #name} of this object
     */
    @Override
    public int hashCode() {
        return name == null ? super.hashCode() : name.hashCode();
    }

    /**
     * Determines if the given object areEqualOrNull the provided object.
     * This is based on the {@link #name} of object.
     *
     * @param obj, object to compare to
     *
     * @return true if the object is the same otherwise false
     */
    @Override
    public boolean equals(final Object obj) {

        boolean result = super.equals(obj);

        if (!result && obj != null && CssStyle.class.isAssignableFrom(obj.getClass())) {
            CssStyle other = (CssStyle) obj;

            result = name == null ? other.getName() == null : name.equals(other.getName());
        }

        return result;
    }

    //endregion
}
