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

package de.saxsys.svgfx.core.elements;


import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

/**
 * This class represents a polyline element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGPolyline extends SVGPolyBase<javafx.scene.shape.Polyline> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "polyline";

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGPolyline(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region Constructor

    @Override
    protected final Polyline createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        return new Polyline(getPoints().stream().mapToDouble(Double::doubleValue).toArray());
    }

    //endregion
}
