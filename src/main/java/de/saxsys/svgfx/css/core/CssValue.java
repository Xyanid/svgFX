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

import de.saxsys.svgfx.css.definitions.Enumerations;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

/**
 * This class contains the css value of a property
 * Created by Xyanid on 29.10.2015.
 */
public class CssValue extends CssBase<CssStyleDeclaration> implements CSSValue {

    //region Fields

    /**
     * The string representation of the value of this CSSValue.
     */
    private String value;

    /**
     * Determines what kind of length value this unit is Css Value is. This is only true for css value that represent an actual length
     */
    private Enumerations.CssValueLengthType lengthType = Enumerations.CssValueLengthType.NONE;

    //endregion

    //region Constructor

    /**
     * Creates new instance.
     */
    public CssValue() {
        super();
    }

    /**
     * Creates new instance.
     *
     * @param parent the parent of the element
     */
    public CssValue(final CssStyleDeclaration parent) {
        super(parent);
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
     * Returns the {@link CssValue#lengthType}.
     *
     * @return {@link CssValue#lengthType}
     */
    public Enumerations.CssValueLengthType getLengthType() {
        return lengthType;
    }

    //endregion

    //region Implement CSSValue

    /**
     * {@inheritDoc}
     */
    @Override public void consumeCssText(final String cssText) throws DOMException {

        for (Enumerations.CssValueLengthType lengthType : Enumerations.CssValueLengthType.values()) {

            if (lengthType == Enumerations.CssValueLengthType.NONE) {
                continue;
            }

            if (cssText.endsWith(lengthType.getName())) {
                value = cssText.substring(0, cssText.lastIndexOf(lengthType.getName()));
                this.lengthType = lengthType;
                return;
            }
        }

        value = cssText;
    }

    @Override public String createCssText() {
        return String.format("%s%s", value, lengthType.getName());
    }

    //endregion

    //region Override CssBase

    /**
     * {@inheritDoc}
     */
    @Override public short getCssValueType() {
        return CSSValue.CSS_CUSTOM;
    }

    //endregion
}
