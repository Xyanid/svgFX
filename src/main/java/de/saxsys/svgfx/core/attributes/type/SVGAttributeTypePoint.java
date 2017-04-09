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
public class SVGAttributeTypePoint extends SVGAttributeType<SVGAttributeTypePoint.SVGTypePoint, Void> {

    // region Class

    /**
     * @author Xyanid on 16.10.2016.
     */
    public static class SVGTypePoint {

        //region Fields

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

        public SVGTypePoint(final SVGDocumentDataProvider dataProvider) {
            x = new SVGAttributeTypeLength(dataProvider);
            y = new SVGAttributeTypeLength(dataProvider);
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
    public static final SVGTypePoint DEFAULT_VALUE = null;

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

    //region Override AttributeWrapper

    /**
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGAttributeTypePoint}
     */
    @Override
    protected Pair<SVGTypePoint, Void> getValueAndUnit(final String text) throws SVGException {

        final List<String> pointSplit = StringUtil.splitByDelimiters(text, Arrays.asList(Constants.COMMA, Constants.WHITESPACE), StringUtil::isNotNullOrEmptyAfterTrim);

        if (pointSplit.size() != 2) {
            throw new SVGException(SVGException.Reason.INVALID_POINT_FORMAT, "point does not provide x and y position");
        }

        final SVGTypePoint point = new SVGTypePoint(getDocumentDataProvider());
        point.getX().setText(pointSplit.get(0).trim());
        point.getY().setText(pointSplit.get(1).trim());

        return new Pair<>(point, null);
    }

    //endregion
}