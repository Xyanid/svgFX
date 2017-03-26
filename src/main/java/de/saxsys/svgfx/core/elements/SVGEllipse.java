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

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

/**
 * This class represents a svg ellipse element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGEllipse extends SVGShapeBase<Ellipse> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "ellipse";

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGEllipse(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final Ellipse createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        return new Ellipse(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE),
                           getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE),
                           getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS_X.getName(), SVGAttributeTypeLength.class).getValue(),
                           getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS_Y.getName(), SVGAttributeTypeLength.class).getValue());
    }

    @Override
    protected SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Ellipse shape) throws SVGException {

        final SVGAttributeTypeLength centerX = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.CENTER_X.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength centerY = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.CENTER_Y.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength radiusX = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS_X.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength radiusY = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS_Y.getName(), SVGAttributeTypeLength.class);

        final SVGAttributeTypeRectangle.SVGTypeRectangle result = new SVGAttributeTypeRectangle.SVGTypeRectangle(getDocumentDataProvider());
        result.getMinX().setText(String.format("%f%s", centerX.getValue() - radiusX.getValue(), radiusX.getUnit().getName()));
        result.getMinY().setText(String.format("%f%s", centerY.getValue() - radiusY.getValue(), radiusY.getUnit().getName()));
        result.getMaxX().setText(String.format("%f%s", centerX.getValue() + radiusX.getValue(), radiusX.getUnit().getName()));
        result.getMaxY().setText(String.format("%f%s", centerY.getValue() + radiusY.getValue(), radiusY.getUnit().getName()));

        return result;
    }

    //endregion
}
