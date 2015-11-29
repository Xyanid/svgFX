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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for polygons and polyline.
 *
 * @param <TShape> the type of shape this element creates
 *                 Created by Xyanid on 07.11.2015.
 */
public abstract class SVGPolyBase<TShape extends Shape> extends SVGShapeBase<TShape> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGPolyBase(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Public

    /**
     * Returns the list of points contained by the attributes.
     *
     * @return the list of points contained by the attributes
     */
    public List<Double> getPoints() {
        List<Double> actualPoints = new ArrayList<>();

        for (String pointsSplit : getAttribute(CoreAttribute.POINTS.getName()).trim().split(" ")) {
            String[] pointSplit = pointsSplit.split(",");
            for (String point : pointSplit) {
                actualPoints.add(Double.parseDouble(point));
            }
        }

        return actualPoints;
    }

    //endregion
}
