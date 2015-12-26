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

import de.saxsys.svgfx.core.elements.SVGClipPath;
import de.saxsys.svgfx.core.elements.SVGDefs;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGElementCreator;
import de.saxsys.svgfx.core.elements.SVGStyle;
import de.saxsys.svgfx.xml.core.SAXParser;
import javafx.scene.Node;
import org.xml.sax.SAXException;

/**
 * This parser is used to create SVG path data for javafx
 *
 * @author Xyanid on 24.10.2015.
 */
public class SVGParser extends SAXParser<javafx.scene.Group, SVGDataProvider, SVGElementCreator, SVGElementBase<?>> {

    //region Constructor

    /**
     * Creates a new instance of the parser and uses the given elementCreator.
     */
    public SVGParser() {

        super(new SVGElementCreator(), new SVGDataProvider());
    }

    //endregion

    //region Override SAXParser

    @Override
    protected javafx.scene.Group enteringDocument() {
        return new javafx.scene.Group();
    }

    @Override
    protected void leavingDocument(final javafx.scene.Group result) {

    }

    @Override
    protected void consumeElementStart(final javafx.scene.Group result, final SVGDataProvider dataProvider, final SVGElementBase<?> element) {

        //definitions will not be kept as children
        if (element instanceof SVGDefs && element.getParent() != null) {
            element.getParent().getChildren().remove(element);
        }
    }

    @Override
    protected void consumeElementEnd(final javafx.scene.Group result, final SVGDataProvider dataProvider, final SVGElementBase<?> element) throws SAXException {

        if (element.getParent() instanceof SVGDefs) {
            dataProvider.setData(element.getAttribute(SVGElementBase.CoreAttribute.ID.getName()), element);
        } else if (element instanceof SVGStyle) {
            dataProvider.getStyles().addAll(((SVGStyle) element).getResult());
            //elements which are inside a group or clip SVGPath as well as clipPath elements will not be added
        } else if (!((element instanceof SVGClipPath) || (element.getParent() instanceof SVGClipPath) || (element.getParent() instanceof de.saxsys.svgfx.core.elements.SVGGroup)) &&
                   element.getResult() instanceof Node) {
            result.getChildren().add((Node) element.getResult());
        }
    }

    //endregion
}
