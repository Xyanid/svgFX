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
import de.saxsys.svgfx.core.definitions.enumerations.GradientUnit;
import javafx.util.Pair;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeGradientUnits extends SVGAttributeType<GradientUnit, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final GradientUnit DEFAULT_VALUE = GradientUnit.OBJECT_BOUNDING_BOX;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeGradientUnits(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws SVGException when any value inside the array is not a valid {@link SVGAttributeTypeGradientUnits}
     */
    @Override
    protected Pair<GradientUnit, Void> getValueAndUnit(final String cssText) throws SVGException {

        GradientUnit gradientUnit = null;

        for (final GradientUnit units : GradientUnit.values()) {
            if (units.getName().equals(cssText)) {
                gradientUnit = units;
                break;
            }
        }

        if (gradientUnit == null) {
            throw new SVGException(String.format("Css text [%s] does not represent a valid gradient unit", cssText));
        }

        return new Pair<>(gradientUnit, null);
    }

    //endregion
}