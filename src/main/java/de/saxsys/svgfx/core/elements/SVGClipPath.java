/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Group;
import javafx.scene.Node;
import org.xml.sax.Attributes;

/**
 * This class represents a clipPath element from svg @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("clipPath")
public class SVGClipPath extends SVGNodeBase<Group> {

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGClipPath(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region SVGElementBase

    @Override
    protected final Group createResult(final SVGCssStyle style) throws SVGException {

        Group result = new Group();

        int counter = 0;

        for (ElementBase child : getChildren()) {
            try {

                SVGElementBase actualChild = (SVGElementBase) child;

                SVGCssStyle childStyle = actualChild.getCssStyle();

                SVGUtils.combineStylesAndResolveInheritance(childStyle, style);

                result.getChildren().add((Node) actualChild.getResult(childStyle));
            } catch (SVGException e) {
                throw new SVGException(String.format("Could not get result from child %d", counter), e);
            }

            counter++;
        }

        return result;
    }

    // endregion
}
