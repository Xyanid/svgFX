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
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.utils.SVGUtils;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

/**
 * This class represents a base class which contains shape element from svg.
 *
 * @param <TShape> type of the shape represented by this element
 *
 * @author Xyanid on 25.10.2015.
 */
public abstract class SVGShapeBase<TShape extends Shape> extends SVGNodeBase<TShape> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    protected SVGShapeBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    // region Override SVGNodeBase

    /**
     * {@inheritDoc}
     * Applies the css style the the element if possible.
     */
    @Override
    protected void initializeResult(final TShape shape, final StyleSupplier styleSupplier) throws SVGException {
        super.initializeResult(shape, styleSupplier);

        SVGUtils.applyStyle(shape, getStyle(), getDocumentDataProvider());
    }

    // endregion
}
