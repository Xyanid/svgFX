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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGContentTypeFillRule;
import de.saxsys.svgfx.core.content.SVGContentTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("path")
public class SVGPath extends SVGShapeBase<javafx.scene.shape.SVGPath> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGPath(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final javafx.scene.shape.SVGPath createResult(final SVGCssStyle style) {
        javafx.scene.shape.SVGPath result = new javafx.scene.shape.SVGPath();

        if (hasContentType(CoreAttributeMapper.PATH_DESCRIPTION.getName())) {
            result.setContent(getContentType(CoreAttributeMapper.PATH_DESCRIPTION.getName(), SVGContentTypeString.class).getValue());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * Applies the file rule to the path.
     */
    @Override
    protected final void initializeResult(final javafx.scene.shape.SVGPath path, final SVGCssStyle style) {
        super.initializeResult(path, style);

        if (style != null) {

            if (style.hasContentType(PresentationAttributeMapper.FILL_RULE.getName())) {
                path.setFillRule(style.getContentType(PresentationAttributeMapper.FILL_RULE.getName(), SVGContentTypeFillRule.class).getValue());
            }
        }
    }

    //endregion
}
