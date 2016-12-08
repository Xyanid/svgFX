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
import de.saxsys.svgfx.xml.core.ElementBase;
import javafx.scene.Group;
import javafx.scene.Node;
import org.xml.sax.Attributes;

/**
 * This class represents a clipPath element from svg @author Xyanid on 25.10.2015.
 */
public class SVGClipPath extends SVGNodeBase<Group> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "clipPath";

    // endregion

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    SVGClipPath(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region SVGElementBase

    /**
     * {@inheritDoc}
     *
     * @return false always.
     */
    @Override
    public boolean canConsumeResult() {
        return false;
    }

    @Override
    protected final Group createResult(final StyleSupplier styleSupplier) throws SVGException {

        Group result = new Group();

        int counter = 0;

        for (final ElementBase child : getUnmodifiableChildren()) {
            try {
                // instead of letting the child use the clip path as its parent, we simply tell the child that the parent style to use is the style of the element using the clip path
                final SVGElementBase actualChild = (SVGElementBase) child;
                result.getChildren().add((Node) actualChild.createAndInitializeResult(() -> actualChild.getStyleAndResolveInheritance(styleSupplier.get())));
            } catch (final SVGException e) {
                throw new SVGException(SVGException.Reason.FAILED_TO_GET_RESULT, String.format("Could not get result from child %d", counter), e);
            }

            counter++;
        }

        return result;
    }

    // endregion
}
