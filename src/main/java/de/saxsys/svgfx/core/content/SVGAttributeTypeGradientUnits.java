/*
 * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.core.content;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.util.Pair;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeGradientUnits extends SVGAttributeType<Enumerations.GradientUnits, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final Enumerations.GradientUnits DEFAULT_VALUE = Enumerations.GradientUnits.OBJECT_BOUNDING_BOX;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeGradientUnits(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws de.saxsys.svgfx.core.SVGException when any value inside the array is not a valid {@link SVGAttributeTypeGradientUnits}
     */
    @Override
    protected Pair<Enumerations.GradientUnits, Void> getValueAndUnit(final String cssText) {

        Enumerations.GradientUnits gradientUnits = Enumerations.GradientUnits.NONE;

        for (Enumerations.GradientUnits units : Enumerations.GradientUnits.values()) {
            if (units.getName().equals(cssText)) {
                gradientUnits = units;
                break;
            }
        }

        return new Pair<>(gradientUnits, null);
    }

    //endregion
}