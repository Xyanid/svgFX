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
import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.utils.StringUtil;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents a svg length content type
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeRectangle extends SVGAttributeType<SVGAttributeTypeRectangle.SVGTypeRectangle, Void> {

    // region Class

    /**
     * @author Xyanid on 16.10.2016.
     */
    public static class SVGTypeRectangle {

        //region Fields

        /**
         * Determines the minX coordinate of the point.
         */
        private final SVGAttributeTypeLength minX;
        /**
         * Determines the minY coordinate of the point.
         */
        private final SVGAttributeTypeLength minY;
        /**
         * Determines the maxX coordinate of the point.
         */
        private final SVGAttributeTypeLength maxX;
        /**
         * Determines the maxY coordinate of the point.
         */
        private final SVGAttributeTypeLength maxY;

        //endregion

        //region Constructor

        public SVGTypeRectangle(final SVGDocumentDataProvider dataProvider) {
            minX = new SVGAttributeTypeLength(dataProvider);
            minY = new SVGAttributeTypeLength(dataProvider);
            maxX = new SVGAttributeTypeLength(dataProvider);
            maxY = new SVGAttributeTypeLength(dataProvider);
        }

        //endregion

        // region Getter

        /**
         * Returns {@link #minX}.
         *
         * @return {@link #minX}.
         */
        public final SVGAttributeTypeLength getMinX() {
            return minX;
        }

        /**
         * Returns {@link #minY}.
         *
         * @return {@link #minY}.
         */
        public final SVGAttributeTypeLength getMinY() {
            return minY;
        }

        /**
         * Returns {@link #maxX}.
         *
         * @return {@link #maxX}.
         */
        public final SVGAttributeTypeLength getMaxX() {
            return maxX;
        }

        /**
         * Returns {@link #maxY}.
         *
         * @return {@link #maxY}.
         */
        public final SVGAttributeTypeLength getMaxY() {
            return maxY;
        }

        // endregion
    }

    // endregion

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final SVGTypeRectangle DEFAULT_VALUE = null;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeRectangle(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override AttributeWrapper

    /**
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGAttributeTypeRectangle}
     */
    @Override
    protected Pair<SVGTypeRectangle, Void> getValueAndUnit(final String text) throws SVGException {

        final List<String> pointSplit = StringUtil.splitByDelimiters(text,
                                                                     Arrays.asList(Constants.WHITESPACE, Constants.COMMA),
                                                                     StringUtil::isNotNullOrEmptyAfterTrim);

        if (pointSplit.size() != 4) {
            throw new SVGException(SVGException.Reason.INVALID_RECTANGLE_FORMAT, String.format("%s does not provide minX, minY, maxX and maxY", text));
        }

        final SVGTypeRectangle point = new SVGTypeRectangle(getDocumentDataProvider());
        point.getMinX().setText(pointSplit.get(0).trim());
        point.getMinY().setText(pointSplit.get(1).trim());
        point.getMaxX().setText(pointSplit.get(2).trim());
        point.getMaxY().setText(pointSplit.get(3).trim());

        return new Pair<>(point, null);
    }

    //endregion
}