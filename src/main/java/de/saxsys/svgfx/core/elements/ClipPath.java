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
import javafx.scene.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents a clipPath element from svg @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("clipPath") public class ClipPath extends SVGNodeBase<Node> {

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public ClipPath(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region SVGElementBase

    @Override protected final Node createResultInternal() throws SVGException {

        Node result = null;

        if (getChildren().size() > 0) {
            try {
                result = (Node) getChildren().get(0).getResult();
            } catch (SAXException e) {
                throw new SVGException(e);
            }
        }

        return result;
    }

    // endregion
}
