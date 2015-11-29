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
import javafx.scene.shape.StrokeType;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Represents a {@link StrokeType}, the default value is {@link StrokeType#INSIDE}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssContentTypeStrokeType extends SVGCssContentTypeBase<StrokeType, Void> {

    //region Constructor

    /**
     * Creates new instance with a default value of {@link StrokeType#CENTERED}.
     */
    public SVGCssContentTypeStrokeType(SVGDataProvider dataProvider) {
        super(StrokeType.CENTERED, dataProvider);
    }

    //endregion

    //region Override CssContentTypeBase

    @Override public Optional<Pair<StrokeType, Void>> getValueAndUnit(final String cssText) {
        return Optional.of(new Pair<>(StrokeType.valueOf(cssText.toUpperCase()), null));
    }

    //endregion
}
