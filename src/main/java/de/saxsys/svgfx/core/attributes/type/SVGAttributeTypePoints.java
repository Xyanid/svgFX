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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypePoints extends SVGAttributeType<List<SVGAttributeTypePoint>, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final List<SVGAttributeTypePoint> DEFAULT_VALUE = new ArrayList<>();

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypePoints(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws SVGException when any value inside the array is not a valid {@link SVGAttributeTypePoint}.
     */
    @Override
    protected Pair<List<SVGAttributeTypePoint>, Void> getValueAndUnit(final String data) throws SVGException {
        final List<SVGAttributeTypePoint> actualPoints = new ArrayList<>();

        if (StringUtil.isNotNullOrEmpty(data)) {
            final List<String> values = StringUtil.splitByDelimiters(data, Arrays.asList(Constants.WHITESPACE, Constants.COMMA), StringUtil::isNotNullOrEmptyAfterTrim);

            if (values.size() % 2 != 0) {
                throw new SVGException(SVGException.Reason.INVALID_POINT_FORMAT, String.format("Must have an even number of points [%s]", data));
            }

            for (int i = 0; i < values.size(); i += 2) {
                final SVGAttributeTypePoint actualPoint = new SVGAttributeTypePoint(getDocumentDataProvider());
                actualPoint.setText(String.format("%s%s%s", values.get(i), Constants.COMMA, values.get(i + 1)));
                actualPoints.add(actualPoint);
            }
        }

        return new Pair<>(actualPoints, null);
    }

    //endregion
}