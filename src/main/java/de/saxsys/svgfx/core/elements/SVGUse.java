/*
 * Copyright 2015 - 2017 Xyanid
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
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.SVGUtil;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

/**
 * This class represents a use element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGUse extends SVGNodeBase<Group> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "use";

    // endregion

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGUse(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    // endregion

    // region SVGElementBase

    /**
     * {@inheritDoc} Resolves the needed reference.
     *
     * @throws SVGException if the {@link XLinkAttributeMapper#XLINK_HREF} is empty or null.
     */
    @Override
    protected Group createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        final SVGElementBase referencedElement = SVGUtil.resolveIRI(getAttributeHolder().getAttributeOrFail(XLinkAttributeMapper.XLINK_HREF.getName(),
                                                                                                            SVGAttributeTypeString.class).getValue(),
                                                                    getDocumentDataProvider(),
                                                                    SVGElementBase.class);

        final Group result = new Group();
        result.setLayoutX(getAttributeHolder().getAttributeValue(CoreAttributeMapper.POSITION_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        result.setLayoutY(getAttributeHolder().getAttributeValue(CoreAttributeMapper.POSITION_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        result.getChildren().add((Node) referencedElement.createAndInitializeResult(ownStyle, ownTransform));

        return result;
    }

    @Override
    protected void initializeResult(final Group result, final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {
    }

    // endregion
}
