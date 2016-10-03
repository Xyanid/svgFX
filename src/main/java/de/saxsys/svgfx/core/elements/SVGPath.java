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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import org.xml.sax.Attributes;

import java.util.function.Supplier;

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

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    SVGPath(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final javafx.scene.shape.SVGPath createResult(final Supplier<SVGCssStyle> styleSupplier) {
        javafx.scene.shape.SVGPath result = new javafx.scene.shape.SVGPath();

        getAttributeHolder().getAttribute(CoreAttributeMapper.PATH_DESCRIPTION.getName(), SVGAttributeTypeString.class)
                            .ifPresent(path -> result.setContent(path.getValue()));

        return result;
    }

    /**
     * {@inheritDoc}
     * Applies the file rule to the path.
     */
    @Override
    protected final void initializeResult(final javafx.scene.shape.SVGPath path, final Supplier<SVGCssStyle> styleSupplier) {
        super.initializeResult(path, styleSupplier);

        styleSupplier.get().getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                     .ifPresent(fillRule -> path.setFillRule(fillRule.getValue()));

    }

    //endregion
}
