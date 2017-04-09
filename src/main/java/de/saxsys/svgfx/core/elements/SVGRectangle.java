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
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.Optional;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGRectangle extends SVGShapeBase<Rectangle> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "rect";

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGRectangle(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final Rectangle createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        return new Rectangle(getAttributeHolder().getAttributeValue(CoreAttributeMapper.POSITION_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE),
                             getAttributeHolder().getAttributeValue(CoreAttributeMapper.POSITION_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE),
                             getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.WIDTH.getName(), SVGAttributeTypeLength.class).getValue(),
                             getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.HEIGHT.getName(), SVGAttributeTypeLength.class).getValue());
    }

    /**
     * {@inheritDoc}
     * Applies the corner radius if any.
     */
    @Override
    protected void initializeResult(final Rectangle result, final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {
        super.initializeResult(result, ownStyle, ownTransform);

        // note that we need to multiply the radius since the arc is a diameter for whatever reason
        final Optional<SVGAttributeTypeLength> radiusX = getAttributeHolder().getAttribute(CoreAttributeMapper.RADIUS_X.getName(), SVGAttributeTypeLength.class);
        if (radiusX.isPresent()) {
            result.setArcWidth(radiusX.get().getValue() * 2.0d);
        }

        final Optional<SVGAttributeTypeLength> radiusY = getAttributeHolder().getAttribute(CoreAttributeMapper.RADIUS_Y.getName(), SVGAttributeTypeLength.class);
        if (radiusY.isPresent()) {
            result.setArcHeight(radiusY.get().getValue() * 2.0d);
        }
    }

    @Override
    public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {

        final SVGAttributeTypeLength posX = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.POSITION_X.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength posY = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.POSITION_Y.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength width = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.WIDTH.getName(), SVGAttributeTypeLength.class);
        final SVGAttributeTypeLength height = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.HEIGHT.getName(), SVGAttributeTypeLength.class);

        final SVGAttributeTypeRectangle.SVGTypeRectangle result = new SVGAttributeTypeRectangle.SVGTypeRectangle(getDocumentDataProvider());
        result.getMinX().setText(String.format("%f%s", posX.getValue(), posX.getUnit().getName()));
        result.getMinY().setText(String.format("%f%s", posY.getValue(), posY.getUnit().getName()));
        result.getMaxX().setText(String.format("%f%s", posX.getValue() + width.getValue(), posX.getUnit().getName()));
        result.getMaxY().setText(String.format("%f%s", posY.getValue() + height.getValue(), posY.getUnit().getName()));

        return result;
    }

    //endregion
}