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
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGLine extends SVGShapeBase<Line> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "line";

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGLine(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final Line createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        return new Line(getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.START_X.getName(), SVGAttributeTypeLength.class).getValue(),
                        getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.START_Y.getName(), SVGAttributeTypeLength.class).getValue(),
                        getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.END_X.getName(), SVGAttributeTypeLength.class).getValue(),
                        getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.END_Y.getName(), SVGAttributeTypeLength.class).getValue());
    }

    @Override
    protected SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Line shape) throws SVGException {

        final SVGAttributeTypeLength startX = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.START_X.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength startY = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.START_Y.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength endX = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.END_X.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength endY = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.END_Y.getName(), SVGAttributeTypeLength.class);

        final SVGAttributeTypeRectangle.SVGTypeRectangle result = new SVGAttributeTypeRectangle.SVGTypeRectangle(getDocumentDataProvider());
        result.getMinX().setText(String.format("%f%s", Math.min(startX.getValue(), endX.getValue()), startX.getUnit().getName()));
        result.getMinY().setText(String.format("%f%s", Math.min(startY.getValue(), endY.getValue()), startY.getUnit().getName()));
        result.getMaxX().setText(String.format("%f%s", Math.max(startX.getValue(), endX.getValue()), startX.getUnit().getName()));
        result.getMaxY().setText(String.format("%f%s", Math.max(startY.getValue(), endY.getValue()), startY.getUnit().getName()));

        return result;
    }

    //endregion
}
