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
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Group;
import javafx.scene.Node;
import org.xml.sax.Attributes;

import java.util.function.Supplier;

/**
 * This class represents the style element from svg
 *
 * @author Xyanid on 27.10.2015.
 */
public class SVGGroup extends SVGNodeBase<Group> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "g";

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
    SVGGroup(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override
    protected final Group createResult(final Supplier<SVGCssStyle> styleSupplier) throws SVGException {
        final Group result = new Group();

        result.setOpacity(1.0d);

        for (final ElementBase child : getChildren()) {

            final SVGElementBase actualChild = (SVGElementBase) child;

            Object childResult = actualChild.createAndInitializeResult(() -> actualChild.getStyleAndResolveInheritance(styleSupplier.get()));

            if (childResult instanceof Node) {
                result.getChildren().add((Node) childResult);
            }
        }

        return result;
    }

    //endregion
}