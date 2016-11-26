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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.CycleMethod;
import javafx.util.Pair;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeCycleMethod extends SVGAttributeType<CycleMethod, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final CycleMethod DEFAULT_VALUE = CycleMethod.NO_CYCLE;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeCycleMethod(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws SVGException when any value inside the array is not a valid {@link SVGAttributeTypeCycleMethod}
     */
    @Override
    protected Pair<CycleMethod, Void> getValueAndUnit(final String cssText) throws SVGException {

        CycleMethod result = null;

        for (final Enumerations.CycleMethodMapping cycleMethod : Enumerations.CycleMethodMapping.values()) {
            if (cycleMethod.getName().equals(cssText)) {
                result = cycleMethod.getMethod();
                break;
            }
        }

        if (result == null) {
            throw new SVGException(SVGException.Reason.INVALID_CYCLE_METHOD, String.format("Given %s can not be parsed into a cycle method", cssText));
        }

        return new Pair<>(result, null);
    }

    //endregion
}