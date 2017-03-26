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
import javafx.scene.Group;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents the svg element from svg
 *
 * @author Xyanid on 24.10.2015.
 */
public class SVGRoot extends SVGElementBase<Group> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "svg";

    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGRoot(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keepElement() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startProcessing() throws SAXException {
        getDocumentDataProvider().storeData(ELEMENT_NAME, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCharacterData(final char[] ch, final int start, final int length) throws SAXException {
    }

    @Override
    protected Group createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {
        return null;
    }

    @Override
    protected void initializeResult(final Group group, final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

    }

    //endregion
}
