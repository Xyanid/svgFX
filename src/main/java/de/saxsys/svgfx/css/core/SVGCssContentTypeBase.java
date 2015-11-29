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

import de.saxsys.svgfx.core.SVGDataProvider;
import javafx.util.Pair;

import java.util.Optional;

/**
 * /**
 * This class contains the css value of a property
 *
 * @param <TValue> type of the value.
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class SVGCssContentTypeBase<TValue, TUnit> extends CssContentTypeBase<TValue, TUnit> {

    // region Enumerations

    /**
     * Contains a {@link String} which indicates that the {@link SVGCssContentTypeBase} of a property is inherited.
     */
    public static String INHERIT_INDICATOR = "inherit";

    // endregion

    //region Fields

    /**
     * Determines whether the value will be retrieved from its parent.
     */
    private boolean isInherited;

    /**
     * Determines the data provider to use when additional data is needed.
     */
    private final SVGDataProvider dataProvider;

    //endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param defaultValue the default value of this to use
     */
    public SVGCssContentTypeBase(final TValue defaultValue, final SVGDataProvider dataProvider) {
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
     * @return The {@link #dataProvider}.
     */
    public SVGDataProvider getDataProvider() {
        return dataProvider;
    }

    //endregion

    //region Public

    /**
     * This will prevent the parsing of cssText which is marked as being inherited.
     *
     * @param cssText cssText which is not equal to {@link #INHERIT_INDICATOR}.
     */
    protected abstract Optional<Pair<TValue, TUnit>> getValueAndUnit(String cssText);

    //endregion

    // region Abstract

    /**
     * Consumes the given css text setting the values in the process
     *
     * @param cssText text to consume.
     */
    @Override public final void consumeCssValue(final String cssText) {
        isInherited = INHERIT_INDICATOR.equals(cssText);

        if (!isInherited) {
            Optional<Pair<TValue, TUnit>> data = getValueAndUnit(cssText);

            if (data.isPresent()) {
                value = data.get().getKey();
                unit = data.get().getValue();
            }
        }
    }

    // endregion
}
