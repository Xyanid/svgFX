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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGCircle extends SVGShapeBase<Circle> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "circle";

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGCircle(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final Circle createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        return new Circle(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE),
                          getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE),
                          getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS.getName(), SVGAttributeTypeLength.class).getValue());
    }

    @Override
    protected SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Circle shape) throws SVGException {

        final SVGAttributeTypeLength centerX = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.CENTER_X.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength centerY = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.CENTER_Y.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength radius = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS.getName(), SVGAttributeTypeLength.class);

        final SVGAttributeTypeRectangle.SVGTypeRectangle result = new SVGAttributeTypeRectangle.SVGTypeRectangle(getDocumentDataProvider());
        result.getMinX().setText(String.format("%f%s", centerX.getValue() - radius.getValue(), radius.getUnit().getName()));
        result.getMinY().setText(String.format("%f%s", centerY.getValue() - radius.getValue(), radius.getUnit().getName()));
        result.getMaxX().setText(String.format("%f%s", centerX.getValue() + radius.getValue(), radius.getUnit().getName()));
        result.getMaxY().setText(String.format("%f%s", centerY.getValue() + radius.getValue(), radius.getUnit().getName()));

        return result;
    }

    //endregion
}
