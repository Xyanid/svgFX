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

import de.saxsys.svgfx.core.utils.CompareUtils;

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
    protected final TValue defaultValue;

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

    //region Getter/Setter

    /**
     * Returns the {@link CssContentTypeBase#value}.
     *
     * @return {@link CssContentTypeBase#value}.
     */
    public TValue getValue() {
        return value;
    }

    /**
     * Sets the {@link #value}.
     *
     * @param value the value to use.
     */
    public void setValue(TValue value) {
        this.value = value;
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

    /**
     * Sets the {@link #unit}.
     *
     * @param unit the unit to use.
     */
    public void setUnit(TUnit unit) {
        this.unit = unit;
    }

    //endregion

    //region Abstract

    /**
     * Consumes the given css text setting the values in the process
     *
     * @param cssText text to consume.
     */
    public abstract void parseCssText(final String cssText) throws Exception;

    //endregion

    //region Override Object

    /**
     * @return the XORed hash of
     */
    @Override
    public int hashCode() {
        return (value == null ? 991 : value.hashCode()) ^ (unit == null ? 997 : unit.hashCode());
    }

    /**
     * Checks whether the object is reference equal or if its also a {@link CssContentTypeBase} and its {@link #value} and {@link #unit} are the same.
     *
     * @param obj object to check.
     *
     * @return true if the object is the same otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;

        if (!result && obj instanceof CssContentTypeBase) {
            CssContentTypeBase base = (CssContentTypeBase) obj;
            result = CompareUtils.areEqualOrNull(value, base.value) && CompareUtils.areEqualOrNull(unit, base.unit);
        }

        return result;
    }

    //endregion
}
