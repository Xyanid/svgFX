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

package de.saxsys.svgfx.core.content;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.content.ContentTypeBase;
import javafx.util.Pair;

/**
 * This class is the base class for svg content used either in css oder by attributes of an {@link de.saxsys.svgfx.core.elements.SVGElementBase}.
 *
 * @param <TValue> type of the value.
 * @param <TUnit>  the type of the unit if any.
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class SVGContentTypeBase<TValue, TUnit> extends ContentTypeBase<TValue, TUnit> {

    // region Enumerations

    /**
     * Contains a {@link String} which indicates that the {@link SVGContentTypeBase} of a property is inherited.
     */
    public static String INHERIT_INDICATOR = "inherit";

    /**
     * Contains a {@link String} which indicates that the {@link SVGContentTypeBase} of a property is none, meaning its not used.
     */
    public static String NONE_INDICATOR = "none";

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
    private final SVGDataProvider dataProvider;

    //endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param defaultValue the default value of this to use.
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypeBase(final TValue defaultValue, final SVGDataProvider dataProvider) {
        super(defaultValue);

        this.dataProvider = dataProvider;
    }

    //endregion

    //region Getter

    /**
     * @return The {@link #isInherited}.
     */
    public boolean getIsInherited() {
        return isInherited;
    }

    /**
     * @return The {@link #isNone}.
     */
    public boolean getIsNone() {
        return isNone;
    }

    /**
     * @return The {@link #dataProvider}.
     */
    public SVGDataProvider getDataProvider() {
        return dataProvider;
    }

    //endregion

    //region Abstract

    /**
     * This will be called if actual data is present in the css text, this is the case if the cssText is not
     * {@link #INHERIT_INDICATOR} or {@link #NONE_INDICATOR}.
     *
     * @param cssText cssText which is not equal to {@link #INHERIT_INDICATOR} or {@link #NONE_INDICATOR}.
     *
     * @return a {@link Pair} which contains the value as the key and the value as the value.
     */
    protected abstract Pair<TValue, TUnit> getValueAndUnit(String cssText);

    //endregion

    // region Override ContentTypeBase

    @Override
    public final void consumeText(final String text) {
        isInherited = INHERIT_INDICATOR.equals(text);

        isNone = NONE_INDICATOR.equals(text);

        if (!isInherited && !isNone) {
            Pair<TValue, TUnit> data = getValueAndUnit(text);

            if (data != null) {
                value = data.getKey();
                unit = data.getValue();
            }
        }
    }

    // endregion

    //region Override Object

    /**
     * @return the XORed hash of
     */
    @Override
    public int hashCode() {
        return Boolean.hashCode(isNone) ^ Boolean.hashCode(isInherited) ^ super.hashCode();
    }

    /**
     * Checks whether the object is reference equal or if its also a {@link SVGContentTypeBase} and its {@link #isNone} and {@link #isInherited} are the same.
     *
     * @param obj object to check.
     *
     * @return true if the object is the same otherwise false.
     */
    @Override
    public boolean equals(Object obj) {

        boolean result = super.equals(obj);

        // in this case the object might be the same be we also need to check if inherit and none are the same
        if (result && obj instanceof SVGContentTypeBase) {
            SVGContentTypeBase base = (SVGContentTypeBase) obj;
            result = isNone == base.isNone && isInherited == base.isInherited;
        }

        return result;
    }

    //endregion
}
