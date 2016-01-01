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
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Pair;

/**
 * Represents a {@link StrokeLineCap}, the default value is {@link StrokeLineCap#SQUARE}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssContentTypeStrokeLineCap extends SVGCssContentTypeBase<StrokeLineCap, Void> {

    // region Static

    /**
     * Determines the default value to use for this {@link SVGCssContentTypeBase}.
     */
    public static final StrokeLineCap DEFAULT_VALUE = StrokeLineCap.SQUARE;

    // endregion

    //region Constructor

    /**
     * Creates new instance with a default value of {@link #DEFAULT_VALUE}.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGCssContentTypeStrokeLineCap(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override CssContentTypeBase

    @Override
    protected Pair<StrokeLineCap, Void> getValueAndUnit(final String cssText) {

        return new Pair<>(StrokeLineCap.valueOf(cssText.toUpperCase()), null);
    }

    //endregion
}