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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypePoint;
import de.saxsys.svgfx.core.content.SVGAttributeTypePoints;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

import java.util.ArrayList;
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

        if (!getAttributeHolder().hasAttribute(CoreAttributeMapper.POINTS.getName())) {
            return actualPoints;
        }

        List<SVGAttributeTypePoint> points = getAttributeHolder().getAttribute(CoreAttributeMapper.POINTS.getName(), SVGAttributeTypePoints.class).getValue();

        for (SVGAttributeTypePoint point : points) {
            actualPoints.add(point.getValue().getX().getValue());
            actualPoints.add(point.getValue().getY().getValue());
        }

        return actualPoints;
    }

    //endregion
}
