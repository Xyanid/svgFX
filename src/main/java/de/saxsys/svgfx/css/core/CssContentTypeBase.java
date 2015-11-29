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

/**
 * This class contains the css value of a property
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class CssContentTypeBase<TValue, TUnit> {

    //region Fields

    /**
     * The string representation of the value of this CSSValue.
     */
    protected TValue value;

    /**
     * Contains the default value of this
     */
    protected TValue defaultValue;

    /**
     * The unit which is placed at the en
     */
    protected TUnit unit;


    //endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param defaultValue the default value of this to use
     */
    public CssContentTypeBase(final TValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    //endregion

    //region Getter

    /**
     * Returns the {@link CssContentTypeBase#value}.
     *
     * @return {@link CssContentTypeBase#value}.
     */
    public TValue getValue() {
        return value;
    }

    /**
     * @return The {@link #defaultValue}.
     */
    public TValue getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return The {@link #unit}.
     */
    public TUnit getUnit() {
        return unit;
    }

    //endregion

    //region Public

    /**
     * Consumes the given css text setting the values in the process
     *
     * @param cssText text to consume.
     */
    public abstract void consumeCssValue(final String cssText);

    //endregion
}
