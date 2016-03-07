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

/**
 * This class represents a svg length content type
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGContentTypeDouble extends SVGContentTypeBase<Double, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGContentTypeBase}.
     */
    public static final double DEFAULT_VALUE = 0.0d;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypeDouble(final SVGDataProvider dataProvider) {
        super(0.0d, dataProvider);
    }

    //endregion

    //region Override SVGContentTypeBase

    /**
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGContentTypeDouble}
     */
    @Override
    protected Pair<Double, Void> getValueAndUnit(final String cssText) {
        return new Pair<>(Double.parseDouble(cssText.replaceAll(",", ".")), null);
    }

    //endregion
}
