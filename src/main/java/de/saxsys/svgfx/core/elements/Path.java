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

import de.saxsys.svgfx.core.SVGCssContentTypeFillRule;
import de.saxsys.svgfx.core.SVGCssStyle;
import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGShapeBase;
import javafx.scene.shape.SVGPath;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("path") public class Path extends SVGShapeBase<SVGPath> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Path(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final SVGPath createResultInternal() {
        SVGPath result = new javafx.scene.shape.SVGPath();

        result.contentProperty().set(getAttribute(CoreAttribute.PATH_DESCRIPTION.getName()));

        return result;
    }

    /**
     * {@inheritDoc}
     * Applies the file rule to the path.
     */
    @Override protected final void initializeResult(SVGPath path) {
        super.initializeResult(path);

        SVGCssStyle style = getCssStyle();

        if (style != null) {

            if (style.hasCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName())) {
                path.setFillRule(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName(), SVGCssContentTypeFillRule.class).getValue());
            }
        }
    }

    //endregion
}
