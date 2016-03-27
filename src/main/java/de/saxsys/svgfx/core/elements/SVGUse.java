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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.SVGUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import org.xml.sax.Attributes;

/**
 * This class represents a use element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("use")
public class SVGUse extends SVGElementBase<Group> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGUse(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    /**
     * {@inheritDoc} Resolves the needed reference.
     *
     * @throws SVGException if the {@link XLinkAttributeMapper#XLINK_HREF} is empty or null.
     */
    @Override
    protected Group createResult(final SVGCssStyle style) throws SVGException {

        if (!getAttributeHolder().hasAttribute(XLinkAttributeMapper.XLINK_HREF.getName())) {
            throw new SVGException("XLink attribute is invalid.");
        }

        SVGElementBase referencedElement = SVGUtils.resolveIRI(getAttributeHolder().getAttribute(XLinkAttributeMapper.XLINK_HREF.getName(),
                                                                                                 SVGAttributeTypeString.class).getValue(),
                                                               getDataProvider(),
                                                               SVGElementBase.class);

        Double positionX = getAttributeHolder().hasAttribute(CoreAttributeMapper.POSITION_X.getName()) ? getAttributeHolder().getAttribute(
                CoreAttributeMapper.POSITION_X.getName(),
                SVGAttributeTypeLength.class).getValue() : SVGAttributeTypeLength.DEFAULT_VALUE;

        Double positionY = getAttributeHolder().hasAttribute(CoreAttributeMapper.POSITION_Y.getName()) ? getAttributeHolder().getAttribute(
                CoreAttributeMapper.POSITION_Y.getName(),
                SVGAttributeTypeLength.class).getValue() : SVGAttributeTypeLength.DEFAULT_VALUE;

        Group result = new Group();
        result.setLayoutX(positionX);
        result.setLayoutY(positionY);

        SVGCssStyle childStyle = referencedElement.getCssStyleAndResolveInheritance();

        SVGUtils.combineStylesAndResolveInheritance(childStyle, style);

        result.getChildren().add((Node) referencedElement.createAndInitializeResult(childStyle));

        return result;
    }

    @Override
    protected void initializeResult(final Group node, final SVGCssStyle inheritanceResolver) throws SVGException {
    }

    //endregion
}
