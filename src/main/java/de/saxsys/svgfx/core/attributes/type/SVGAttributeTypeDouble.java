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
import javafx.util.Pair;

/**
 * This class represents a svg length content type
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeDouble extends SVGAttributeType<Double, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final double DEFAULT_VALUE = 0.0d;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeDouble(final SVGDocumentDataProvider dataProvider) {
        super(0.0d, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGAttributeTypeDouble}
     */
    @Override
    protected Pair<Double, Void> getValueAndUnit(final String cssText) throws SVGException {
        try {
            return new Pair<>(Double.parseDouble(cssText.replaceAll(",", ".")), null);
        } catch (final NumberFormatException e) {
            throw new SVGException(SVGException.Reason.INVALID_NUMBER_FORMAT, e);
        }
    }

    //endregion
}