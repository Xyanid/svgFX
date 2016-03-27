/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.xml.attribute;

import de.saxsys.svgfx.core.utils.CompareUtils;

/**
 * This class contains is used for parsing data which comes from a text. Usually when parsing text into data, there might be more then just normal atomic
 * values as a result of the parsing. This class allows for a default value to be specified and also allows for the value to contain a unit. The most common
 * scenario where you want to use this, is if you are parsing length data e.g. 10cm or 10mm.
 *
 * @param <TValue> the type of the value contained in the content.
 * @param <TUnit>  the type of unit contained in the content.
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class AttributeType<TValue, TUnit> {

    //region Fields

    /**
     * The actual value contained within the parsed text.
     */
    protected TValue value;

    /**
     * Contains the default value of this.
     */
    protected final TValue defaultValue;

    /**
     * The unit which is contained, if any.
     */
    protected TUnit unit;

    //endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param defaultValue the default value of this to use
     */
    public AttributeType(final TValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    //endregion

    //region Getter/Setter

    /**
     * Returns the {@link AttributeType#value}.
     *
     * @return {@link AttributeType#value}.
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
     * Returns the {@link #defaultValue}.
     *
     * @return The {@link #defaultValue}.
     */
    public TValue getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the {@link #unit}.
     *
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
     * Consumes the given text, this should in turn set the {@link #value} and {@link #unit}.
     *
     * @param text text to consume.
     */
    public abstract void consumeText(final String text);

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
     * Checks whether the object is reference equal or if its also a {@link AttributeType} and its {@link #value} and {@link #unit} are the same.
     *
     * @param obj object to check.
     *
     * @return true if the object is the same otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;

        if (!result && obj instanceof AttributeType) {
            AttributeType base = (AttributeType) obj;
            result = CompareUtils.areEqualOrNull(value, base.value) && CompareUtils.areEqualOrNull(unit, base.unit);
        }

        return result;
    }

    //endregion
}