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
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents the style element from svg
 *
 * @author Xyanid on 27.10.2015.
 */
@SVGElementMapping("g")
public class Group extends SVGNodeBase<javafx.scene.Group> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Group(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override
    protected final javafx.scene.Group createResultInternal() throws SVGException {
        javafx.scene.Group result = new javafx.scene.Group();

        result.setOpacity(1.0d);

        for (ElementBase child : getChildren()) {
            try {

                Object childResult = child.getResult();

                if (childResult instanceof Node) {
                    result.getChildren().add((Node) childResult);
                }
            } catch (SAXException e) {
                throw new SVGException(e);
            }
        }

        return result;
    }

    //endregion
}
