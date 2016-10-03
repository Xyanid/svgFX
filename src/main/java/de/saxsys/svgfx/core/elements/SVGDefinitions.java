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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.Node;
import org.xml.sax.Attributes;

import java.util.function.Supplier;

/**
 * This class represents a stop element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGDefinitions extends SVGElementBase<Node> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "defs";

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
    SVGDefinitions(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override
    public void startProcessing() {
        //definitions will not be kept as children
        if (getParent() != null) {
            getParent().getChildren().remove(this);
        }
    }

    @Override
    public void endProcessing() {
        getAttributeHolder().getAttribute(CoreAttributeMapper.ID.getName(), SVGAttributeTypeString.class)
                            .ifPresent(id -> getDocumentDataProvider().setData(id.getValue(), this));
    }

    /**
     * {@inheritDoc}
     *
     * @return false always
     */
    @Override
    public boolean canConsumeResult() {
        return false;
    }

    @Override
    protected final Node createResult(final Supplier<SVGCssStyle> styleSupplier) throws SVGException {
        return null;
    }

    @Override
    protected final void initializeResult(final Node node, final Supplier<SVGCssStyle> styleSupplier) throws SVGException {

    }

    //endregion
}
