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
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGUtils;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.core.CssStyle;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

/**
 * This class represents a use element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("use") public class Use extends SVGElementBase<Node> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Use(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    /**
     * {@inheritDoc} Resolves the needed reference
     */
    @Override protected Node createResultInternal() throws SVGException {
        String reference = getAttributes().get(Enumerations.SvgAttribute.XLINK_HREF.getName());
        if (StringUtils.isNullOrEmpty(reference)) {
            throw new IllegalArgumentException("given use element does not contain a reference");
        }

        SVGElementBase element = getDataProvider().getData(SVGElementBase.class, reference.replaceFirst("#", ""));
        if (element == null) {
            throw new IllegalArgumentException(String.format("given reference %s does not exist", reference));
        }

        Node result = (Node) element.createResult();

        //apply style to the element if its a shape and we have a style for this use element
        if (result instanceof Shape) {
            CssStyle style = getCssStyle();

            if (style != null) {
                SVGUtils.applyStyle((Shape) result, style, getDataProvider());
            }
        }

        return result;
    }

    @Override protected void initializeResult(Node node) throws SVGException {

    }


    //endregion
}
