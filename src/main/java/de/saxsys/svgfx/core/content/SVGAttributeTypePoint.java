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

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.definitions.Constants;
import javafx.util.Pair;

/**
 * This class represents a svg length content type
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypePoint extends SVGAttributeType<SVGAttributeTypePoint.SVGPoint, Void> {

    // region Classes

    /**
     * This class represents a point in svg, it differs from a normal point in the sense that each x and y coordinates are {@link SVGAttributeTypeLength}
     * instead of normal double or ints.
     */
    public class SVGPoint {

        //region Fields

        /**
         * The {@link SVGDocumentDataProvider} to be used.
         */
        private final SVGDocumentDataProvider dataProvider;
        /**
         * Determines the x coordinate of the point.
         */
        private final SVGAttributeTypeLength x;
        /**
         * Determines the y coordinate of the point.
         */
        private final SVGAttributeTypeLength y;

        //endregion

        //region Constructor

        public SVGPoint(final SVGDocumentDataProvider dataProvider) {
            this.dataProvider = dataProvider;
            x = new SVGAttributeTypeLength(this.dataProvider);
            y = new SVGAttributeTypeLength(this.dataProvider);
        }

        //endregion

        // region Getter

        /**
         * Returns {@link #x}.
         *
         * @return {@link #x}.
         */
        public final SVGAttributeTypeLength getX() {
            return x;
        }

        /**
         * Returns {@link #y}.
         *
         * @return {@link #y}.
         */
        public final SVGAttributeTypeLength getY() {
            return y;
        }

        // endregion
    }

    // endregion

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final SVGPoint DEFAULT_VALUE = null;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypePoint(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override AttributeType

    /**
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGAttributeTypePoint}
     */
    @Override
    protected Pair<SVGPoint, Void> getValueAndUnit(final String text) {

        String[] pointSplit = text.split(Constants.POSITION_DELIMITER_STRING);

        if (pointSplit.length != 2) {
            throw new IllegalArgumentException("point does not provide x and y position");
        }

        SVGPoint point = new SVGPoint(getDataProvider());
        point.getX().consumeText(pointSplit[0].trim());
        point.getY().consumeText(pointSplit[1].trim());

        return new Pair<>(point, null);
    }

    //endregion
}