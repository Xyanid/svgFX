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
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.path.CommandParser;
import de.saxsys.svgfx.core.path.PathException;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.Optional;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGPath extends SVGShapeBase<javafx.scene.shape.SVGPath> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "path";

    // endregion

    // region Field

    private final CommandParser commandParser;

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name          value of the element
     * @param attributes    attributes of the element
     * @param dataProvider  the {@link SVGDocumentDataProvider} to be used
     * @param commandParser the {@link CommandParser} to use.
     */
    SVGPath(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider, final CommandParser commandParser) {
        super(name, attributes, dataProvider);
        this.commandParser = commandParser;
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final javafx.scene.shape.SVGPath createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {
        javafx.scene.shape.SVGPath result = new javafx.scene.shape.SVGPath();

        final Optional<SVGAttributeTypeString> path = getAttributeHolder().getAttribute(CoreAttributeMapper.PATH_DESCRIPTION.getName(), SVGAttributeTypeString.class);
        if (path.isPresent()) {
            result.setContent(path.get().getValue());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * Applies the file rule to the path.
     */
    @Override
    protected final void initializeResult(final javafx.scene.shape.SVGPath result,
                                          final SVGCssStyle ownStyle,
                                          final Transform ownTransform) throws SVGException {
        super.initializeResult(result, ownStyle, ownTransform);

        final Optional<SVGAttributeTypeFillRule> fillRule = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class);
        if (fillRule.isPresent()) {
            result.setFillRule(fillRule.get().getValue());
        }
    }

    @Override
    protected SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final javafx.scene.shape.SVGPath shape) throws SVGException {

        final String path = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.PATH_DESCRIPTION.getName(), SVGAttributeTypeString.class).getValue();

        final Rectangle boundingBox;
        try {
            boundingBox = commandParser.getBoundingBox(path);
        } catch (final PathException e) {
            throw new SVGException(String.format("Could not parse path [%s]", path), e);
        }

        final SVGAttributeTypeRectangle.SVGTypeRectangle result = new SVGAttributeTypeRectangle.SVGTypeRectangle(getDocumentDataProvider());
        result.getMinX().setText(String.format("%f", boundingBox.getX()));
        result.getMinY().setText(String.format("%f", boundingBox.getY()));
        result.getMaxX().setText(String.format("%f", boundingBox.getX() + boundingBox.getWidth()));
        result.getMaxY().setText(String.format("%f", boundingBox.getY() + boundingBox.getHeight()));

        return result;
    }

    //endregion
}