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

package de.saxsys.svgfx.core;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dash array used for strokes, the default value is an empty array.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssContentTypeStrokeDashArray extends SVGCssContentTypeBase<SVGCssContentTypeLength[], Void> {

    // region Fields

    /**
     * Contains the dashValues as double.
     */
    private final List<Double> dashValues = new ArrayList<>();

    // endregion

    //region Constructor

    /**
     * Creates new instance with a default value of an empty array.
     */
    public SVGCssContentTypeStrokeDashArray(SVGDataProvider dataProvider) {
        super(new SVGCssContentTypeLength[]{}, dataProvider);
    }

    //endregion

    // region Public

    /**
     * @return the {@link #dashValues}.
     */
    public final List<Double> getDashValues() {
        return dashValues;
    }

    // endregion

    //region Override CssContentTypeBase

    /**
     * {@inheritDoc} This implementation will parse the given data as a coma separated list of dashValues.
     *
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGCssContentTypeLength}
     */
    @Override public Pair<SVGCssContentTypeLength[], Void> getValueAndUnit(final String cssText) {

        String[] values = cssText.split(",");

        SVGCssContentTypeLength[] array = new SVGCssContentTypeLength[values.length];
        dashValues.clear();

        for (int i = 0; i < values.length; i++) {
            array[i] = new SVGCssContentTypeLength(getDataProvider());
            array[i].parseCssValue(values[i].trim());
            dashValues.add(array[i].getValue());
        }

        return new Pair<>(array, null);
    }

    //endregion
}
