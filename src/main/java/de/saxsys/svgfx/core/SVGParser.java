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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.elements.ClipPath;
import de.saxsys.svgfx.core.elements.Defs;
import de.saxsys.svgfx.core.elements.Group;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGElementCreator;
import de.saxsys.svgfx.core.elements.Style;
import de.saxsys.svgfx.xml.core.SAXParser;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Node;
import org.xml.sax.SAXException;

/**
 * This parser is used to create SVG path data for javafx
 * @author Xyanid on 24.10.2015.
 */
public class SVGParser extends SAXParser<javafx.scene.Group, SVGDataProvider, SVGElementCreator> {

    //region Constructor

    /**
     * Creates a new instance of the parser and uses the given elementCreator.
     */
    public SVGParser() {

        super(new SVGElementCreator(), new SVGDataProvider());
    }

    //endregion

    //region Override SAXParser

    @Override protected javafx.scene.Group enteringDocument() {
        return new javafx.scene.Group();
    }

    @Override protected void leavingDocument(final javafx.scene.Group result) {

    }

    @Override protected void consumeElementStart(final javafx.scene.Group result, final SVGDataProvider dataProvider, final ElementBase<SVGDataProvider, ?, ?> element) {

        //definitions will not be kept as children
        if (element instanceof Defs && element.getParent() != null) {
            element.getParent().getChildren().remove(element);
        }
    }

    @Override protected void consumeElementEnd(final javafx.scene.Group result, final SVGDataProvider dataProvider, final ElementBase<SVGDataProvider, ?, ?> element) throws SAXException {

        if (element.getParent() instanceof Defs) {
            dataProvider.getUnmodifiableData().put(element.getAttributes().get(SVGElementBase.CoreAttribute.ID.getName()), (SVGElementBase) element);
        } else if (element instanceof Style) {
            dataProvider.getStyles().addAll(((Style) element).getResult());
        } else if (!((element.getParent() instanceof ClipPath) || (element.getParent() instanceof Group)) && element.getResult() instanceof Node) {
            result.getChildren().add((Node) element.getResult());
        }
    }

    //endregion
}
