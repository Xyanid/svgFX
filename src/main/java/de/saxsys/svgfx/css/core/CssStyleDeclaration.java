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

import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.css.definitions.Constants;
import javafx.util.Pair;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class represents a CSSStyleDeclaration found in a CSSStyleRule.
 * Created by Xyanid on 29.10.2015.
 */
public class CssStyleDeclaration extends CssBase<CssStyle> implements CSSStyleDeclaration {

    //region Fields

    /**
     * Map containing all the properties of the CSSStyleDeclaration.
     */
    Map<String, CssValue> cssProperties = new LinkedHashMap<>();

    //endregion

    //region Constructor

    /**
     * Creates a new instance of the CssStyleProperty using he given parent.
     */
    public CssStyleDeclaration() {
        super();
    }

    /**
     * Creates a new instance of the CssStyleProperty using the given modifiable.
     *
     * @param isModifiable determines if the CssStyleProperty is modifiable
     */
    public CssStyleDeclaration(final boolean isModifiable) {
        super(isModifiable);
    }

    /**
     * Creates a new instance of the CssStyleProperty using he given parent.
     *
     * @param parent parent to use may be null if not needed
     */
    public CssStyleDeclaration(final CssStyle parent) {
        super(parent);
    }

    //endregion

    // region Private

    /**
     * Get the default value which will be used as the default value for a {@link CssValue}.
     *
     * @param name name of the property for which a default value should be returned
     *
     * @return the default value corresponding to the given name or null if not match was found.
     */
    private String getDefaultValue(final String name) {

        for (SVGElementBase.PresentationAttribute attribute : SVGElementBase.PresentationAttribute.values()) {
            if (attribute.getName().equals(name)) {
                return attribute.getDefaultValue();
            }
        }

        return null;
    }

    // endregion

    //region Public

    /**
     * @return The {@link #cssProperties} as a readonly {@link Map}.
     */
    public Map<String, CssValue> getUnmodifiableCssValues() {
        return Collections.unmodifiableMap(cssProperties);
    }

    /**
     * Returns the desired declaration as the desired type if it exits.
     *
     * @param key       key to be used
     * @param converter converter interfaces to be used
     * @param <TData>   type of the data
     *
     * @return the data as the given type or null if the declaration is not present
     */
    public <TData> TData getPropertyAs(final String key, final Function<String, TData> converter) {

        String value = getPropertyValue(key);

        if (value != null) {
            return converter.apply(value);
        }

        return null;
    }

    //endregion

    //region Implement CSSStyleDeclaration

    /**
     * @inheritDoc The value is stripped of its lengthType
     */
    @Override public String getPropertyValue(final String propertyName) {

        CssValue value = cssProperties.get(propertyName);

        if (value != null) {
            return value.getValue();
        } else {
            return "";
        }
    }

    /**
     * @inheritDoc
     */
    @Override public CSSValue getPropertyCSSValue(final String propertyName) {
        return cssProperties.get(propertyName);
    }

    /**
     * @inheritDoc
     */
    @Override public String removeProperty(final String propertyName) throws DOMException {

        if (!getIsModifiable()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "CssStyleProperty can not be modified");
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    @Override public String getPropertyPriority(final String propertyName) {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override public void setProperty(final String propertyName, final String value, final String priority) throws DOMException {

    }

    /**
     * @inheritDoc
     */
    @Override public int getLength() {
        return cssProperties.size();
    }

    /**
     * @inheritDoc
     */
    @Override public String item(final int index) {

        int counter = 0;
        for (Map.Entry<String, CssValue> entry : cssProperties.entrySet()) {
            if (counter == index) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    @Override public CSSRule getParentRule() {
        return getParent();
    }

    //endregion

    //region Override CssBase

    /**
     * @inheritDoc This method will throw an exception if the an unclosed comment or string is present in the declarationBlock
     */
    @Override public void consumeCssText(final String cssText) throws DOMException {

        cssProperties.clear();

        StringBuilder builder = new StringBuilder();

        Pair<Boolean, Boolean> result = filterCommentAndString(cssText, null, character -> {
            builder.append(character);

            return false;
        }, character -> {
            if (character == Constants.DECLARATION_BLOCK_START) {
                return false;
            } else if (character == Constants.DECLARATION_BLOCK_END) {
                return true;
            } else if (character == Constants.PROPERTY_END) {

                String property = builder.toString();

                int index = builder.toString().indexOf(Constants.PROPERTY_SEPARATOR);

                if (index > -1 && index < property.length() - 1) {

                    String name = property.substring(0, index).trim();

                    CssValue cssValue = new CssValue(this, getDefaultValue(name));

                    cssValue.setCssText(property.substring(index + 1).trim());

                    cssProperties.put(name, cssValue);
                }

                builder.setLength(0);

                return false;
            }

            builder.append(character);

            return false;
        });

        //comment was not properly closed
        if (result.getKey()) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Given declaration block contains an unclosed comment.");
        }

        //string was not properly closed
        if (result.getValue()) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Given declaration block contains an unclosed string.");
        }
    }

    @Override public String createCssText() {

        StringBuilder builder = new StringBuilder();

        builder.append(Constants.DECLARATION_BLOCK_START);

        for (Map.Entry<String, CssValue> value : cssProperties.entrySet()) {
            builder.append(String.format("%s%s%s%s", value.getKey(), Constants.PROPERTY_SEPARATOR, value.getValue().getCssText(), Constants.PROPERTY_END));
        }

        builder.append(Constants.DECLARATION_BLOCK_END);

        return builder.toString();
    }

    //endregion


}
