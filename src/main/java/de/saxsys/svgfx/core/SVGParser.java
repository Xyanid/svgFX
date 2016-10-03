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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGElementFactory;
import de.saxsys.svgfx.xml.core.SAXParser;
import javafx.scene.Group;
import javafx.scene.Node;
import org.xml.sax.SAXException;

/**
 * This parser is used to create SVG path data for javafx
 *
 * @author Xyanid on 24.10.2015.
 */
public class SVGParser extends SAXParser<Group, SVGDocumentDataProvider, SVGElementFactory, SVGElementBase<?>> {

    //region Constructor

    /**
     * Creates a new instance of the parser and uses the given elementCreator.
     */
    public SVGParser() {

        super(new SVGElementFactory(), new SVGDocumentDataProvider());
    }

    //endregion

    //region Override SAXParser

    @Override
    protected Group enteringDocument() {
        return new Group();
    }

    @Override
    protected void leavingDocument(final Group result) {
    }

    @Override
    protected void consumeElementStart(final Group result, final SVGDocumentDataProvider dataProvider, final SVGElementBase<?> element) {
    }

    @Override
    protected void consumeElementEnd(final Group result, final SVGDocumentDataProvider documentDataProvider, final SVGElementBase<?> element) throws SAXException {
        result.getChildren().add((Node) element.getResult());
    }

    //endregion
}
