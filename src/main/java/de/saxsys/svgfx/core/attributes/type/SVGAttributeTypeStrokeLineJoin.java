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
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Pair;

/**
 * Represents a {@link StrokeLineJoin}, the default value is {@link StrokeLineJoin#MITER}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeStrokeLineJoin extends SVGAttributeType<StrokeLineJoin, Void> {

    // region Static

    /**
     * Determines the default value to use for this {@link SVGAttributeType}.
     */
    public static final StrokeLineJoin DEFAULT_VALUE = StrokeLineJoin.MITER;

    // endregion

    //region Constructor

    /**
     * Creates new instance with a default value of {@link #DEFAULT_VALUE}.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeStrokeLineJoin(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override AttributeWrapper

    @Override
    protected Pair<StrokeLineJoin, Void> getValueAndUnit(final String cssText) throws SVGException {
        try {
            return new Pair<>(StrokeLineJoin.valueOf(cssText.toUpperCase()), null);
        } catch (IllegalArgumentException e) {
            throw new SVGException(String.format("Css text [%s] does not represent a valid stroke line join", cssText), e);
        }
    }

    //endregion
}