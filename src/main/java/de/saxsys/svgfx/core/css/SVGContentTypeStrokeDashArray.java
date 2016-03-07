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

package de.saxsys.svgfx.core.css;

import de.saxsys.svgfx.core.SVGDataProvider;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dash array used for strokes, the default value is an empty array.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGContentTypeStrokeDashArray extends SVGContentTypeBase<SVGContentTypeLength[], Void> {

    // region Static

    /**
     * Determines the default value to use for this {@link SVGContentTypeBase}.
     */
    public static final SVGContentTypeLength[] DEFAULT_VALUE = new SVGContentTypeLength[]{};

    // endregion

    // region Fields

    /**
     * Contains the dashValues as double.
     */
    private final List<Double> dashValues = new ArrayList<>();

    // endregion

    //region Constructor

    /**
     * Creates new instance with a default value of an empty array.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypeStrokeDashArray(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
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

    //region Override ContentTypeBase

    /**
     * {@inheritDoc} This implementation will parse the given data as a coma separated list of dashValues.
     *
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGContentTypeLength}
     */
    @Override
    protected Pair<SVGContentTypeLength[], Void> getValueAndUnit(final String cssText) {

        String[] values = cssText.split(",");

        SVGContentTypeLength[] array = new SVGContentTypeLength[values.length];
        dashValues.clear();

        for (int i = 0; i < values.length; i++) {
            array[i] = new SVGContentTypeLength(getDataProvider());
            array[i].parseCssText(values[i].trim());
            dashValues.add(array[i].getValue());
        }

        return new Pair<>(array, null);
    }

    //endregion
}
