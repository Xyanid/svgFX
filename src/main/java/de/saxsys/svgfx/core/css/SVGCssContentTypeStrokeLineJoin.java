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
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Pair;

/**
 * Represents a {@link StrokeLineJoin}, the default value is {@link StrokeLineJoin#MITER}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssContentTypeStrokeLineJoin extends SVGCssContentTypeBase<StrokeLineJoin, Void> {

    //region Constructor

    /**
     * Creates new instance with a default value of {@link StrokeLineJoin#MITER}.
     */
    public SVGCssContentTypeStrokeLineJoin(SVGDataProvider dataProvider) {
        super(StrokeLineJoin.MITER, dataProvider);
    }

    //endregion

    //region Override CssContentTypeBase

    @Override
    public Pair<StrokeLineJoin, Void> getValueAndUnit(final String cssText) {
        return new Pair<>(StrokeLineJoin.valueOf(cssText.toUpperCase()), null);
    }

    //endregion
}
