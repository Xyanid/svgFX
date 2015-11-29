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

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

/**
 * This class contains the css value of a property
 * Created by Xyanid on 29.10.2015.
 */
public class CssValue extends CssBase<CssStyleDeclaration> implements CSSValue {

    // region Enumerations

    /**
     * Contains a {@link String} which indicates that the {@link de.saxsys.svgfx.css.core.CssValue} of a property is inherited.
     */
    public static String INHERIT_INDICATOR = "inherit";

    // endregion

    //region Fields

    /**
     * The string representation of the value of this CSSValue.
     */
    private String value;

    /**
     * Contains the default value of this
     */
    private String defaultValue;

    //endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param parent       the parent of the element
     * @param defaultValue the default value of this to use
     */
    public CssValue(final CssStyleDeclaration parent, final String defaultValue) {
        super(parent);

        this.defaultValue = defaultValue;
    }

    //endregion

    //region Getter

    /**
     * Returns the {@link CssValue#value}.
     *
     * @return {@link CssValue#value}.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return The {@link #defaultValue}.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    //endregion

    //region Implement CSSValue

    /**
     * @inheritDoc
     */
    @Override public short getCssValueType() {
        return CSSValue.CSS_CUSTOM;
    }

    //endregion

    //region Override CssBase

    /**
     * {@inheritDoc}
     */
    @Override public void consumeCssText(final String cssText) throws DOMException {
        value = cssText;
    }

    /**
     * {@inheritDoc}
     */
    @Override public String createCssText() {
        return value;
    }

    //endregion
}
