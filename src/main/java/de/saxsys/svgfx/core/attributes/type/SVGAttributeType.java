/*
 * Copyright 2015 - 2017 Xyanid
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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.xml.core.AttributeWrapper;
import javafx.util.Pair;

import java.util.Objects;

/**
 * This class is the base class for svg content used either in css oder by attributes of an {@link de.saxsys.svgfx.core.elements.SVGElementBase}.
 *
 * @param <TValue> type of the value.
 * @param <TUnit>  the type of the unit if any.
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class SVGAttributeType<TValue, TUnit> extends AttributeWrapper {

    // region Enumerations

    /**
     * Contains a {@link String} which indicates that the {@link SVGAttributeType} of a property is inherited.
     */
    private static String INHERIT_INDICATOR = "inherit";

    /**
     * Contains a {@link String} which indicates that the {@link SVGAttributeType} of a property is none, meaning its not used.
     */
    private static String NONE_INDICATOR = "none";

    // endregion

    //region Fields

    /**
     * Determines whether the value will be retrieved from its parent.
     */
    private boolean isInherited;
    /**
     * Determines that the value is none, so no data will be processed.
     */
    private boolean isNone;
    /**
     * Determines the data provider to use when additional data is needed.
     */
    private final SVGDocumentDataProvider dataProvider;
    /**
     * Contains the default value of this.
     */
    private final TValue defaultValue;
    /**
     * The actual value and unit contained within the parsed text
     */
    private Pair<TValue, TUnit> valueAndUnit;

    //endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param defaultValue the default value of this to use.
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeType(final TValue defaultValue, final SVGDocumentDataProvider dataProvider) {
        this.defaultValue = defaultValue;
        this.dataProvider = dataProvider;
    }

    //endregion

    //region Getter

    /**
     * @return The {@link #dataProvider}.
     */
    public final SVGDocumentDataProvider getDocumentDataProvider() {
        return dataProvider;
    }

    /**
     * @return The {@link #isInherited}.
     */
    public final boolean getIsInherited() {
        return isInherited;
    }

    /**
     * @return The {@link #isNone}.
     */
    public final boolean getIsNone() {
        return isNone;
    }

    /**
     * Returns the {@link #valueAndUnit}s key.
     *
     * @return {@link #valueAndUnit}s key.
     *
     * @throws SVGException when an error occurs during the initialization of the value and unit.
     */
    public final TValue getValue() throws SVGException {
        initializeValueAndUnit();
        return valueAndUnit.getKey();
    }

    /**
     * Returns the {@link #valueAndUnit}s value.
     *
     * @return The {@link #valueAndUnit}s value.
     *
     * @throws SVGException when an error occurs during the initialization of the value and unit.
     */
    public final TUnit getUnit() throws SVGException {
        initializeValueAndUnit();
        return valueAndUnit.getValue();
    }

    /**
     * Determines if this length value has the provided unit.
     *
     * @param unit the {@link SVGAttributeTypeLength.Unit} that is expected.
     *
     * @return true if the unit of this attribute is equals to th expected one, otherwise false.
     *
     * @throws SVGException if a problem occurs during the retrieval of the unit and value of this attribute.
     */
    public boolean hasUnit(final TUnit unit) throws SVGException {
        return Objects.equals(getUnit(), unit);
    }

    /**
     * Returns the {@link #valueAndUnit}.
     *
     * @return the {@link #valueAndUnit}.
     *
     * @throws SVGException when an error occurs during the initialization of the value
     */
    public final Pair<TValue, TUnit> getValueAndUnit() throws SVGException {
        initializeValueAndUnit();
        return valueAndUnit;
    }

    /**
     * Set the default value
     */
    public final void useDefaultValue() {
        this.valueAndUnit = new Pair<>(defaultValue, null);
    }

    @Override
    public final void setText(final String text) {
        super.setText(text);
        isInherited = INHERIT_INDICATOR.equals(text);
        isNone = NONE_INDICATOR.equals(text);
        valueAndUnit = null;
    }

    // endregion

    // region Private

    /**
     * Initializes the result based on the
     */
    private void initializeValueAndUnit() throws SVGException {
        if (valueAndUnit == null) {
            if (!isInherited && !isNone) {
                valueAndUnit = getValueAndUnit(getText());
            } else {
                valueAndUnit = new Pair<>(null, null);
            }
        }
    }

    // endregion

    //region Abstract

    /**
     * This will be called if actual data is present in the css text, this is the case if the cssText is not
     * {@link #INHERIT_INDICATOR} or {@link #NONE_INDICATOR}.
     *
     * @param text cssText which is not equal to {@link #INHERIT_INDICATOR} or {@link #NONE_INDICATOR}.
     *
     * @return a {@link Pair} which contains the value as the key and the value as the value.
     *
     * @throws SVGException when any error occurs during the consumption of the given text.
     */
    protected abstract Pair<TValue, TUnit> getValueAndUnit(final String text) throws SVGException;

    //endregion
}
