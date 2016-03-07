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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for polygons and polyline.
 *
 * @param <TShape> the type of shape this element creates
 *
 * @author Xyanid on 07.11.2015.
 */
public abstract class SVGPolyBase<TShape extends Shape> extends SVGShapeBase<TShape> {

    // region Static

    /**
     * Determines the delimiter that separated a pair of points.
     */
    private static char POINTS_DELIMITER = ' ';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    private static char POSITION_DELIMITER = ',';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    private static String POSITION_DELIMITER_STRING = String.valueOf(POSITION_DELIMITER);

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGPolyBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Public

    /**
     * Returns the list of points contained by the attributes.
     *
     * @return the list of points contained by the attributes
     *
     * @throws SVGException             if any of the points in the corresponding attribute does not provide x and y position.
     * @throws IllegalArgumentException if any of the points in the corresponding attribute does not provide x and y position.
     */
    public final List<Double> getPoints() throws SVGException, IllegalArgumentException {
        List<Double> actualPoints = new ArrayList<>();

        String points = getAttribute(Enumerations.CoreAttribute.POINTS.getName());

        if (StringUtils.isNullOrEmpty(points)) {
            return actualPoints;
        }

        List<String> values = SVGUtils.split(points, Collections.singletonList(POINTS_DELIMITER), (currentData, index) -> {

            // check if the required delimiter is present and that the last character is not a delimiter so the string can be split
            boolean containsDelimiter = currentData.contains(POSITION_DELIMITER_STRING);
            if (containsDelimiter && currentData.charAt(currentData.length() - 1) != POSITION_DELIMITER) {
                return true;
            }
            // in this special case we have two non delimiters characters separated by a split delimiter which is invalid e.G. "1,2 3 4,5"
            else if (index == points.length() - 1 || points.charAt(index + 1) != POINTS_DELIMITER) {
                throw new SVGException("Invalid points format");
            }

            return false;
        });

        for (String pointsSplit : values) {

            String[] pointSplit = pointsSplit.split(POSITION_DELIMITER_STRING);

            if (pointSplit.length != 2) {
                throw new IllegalArgumentException("At least one point does not provide x and y position");
            }

            actualPoints.add(Double.parseDouble(pointSplit[0].trim()));
            actualPoints.add(Double.parseDouble(pointSplit[1].trim()));
        }

        return actualPoints;
    }

    //endregion
}
