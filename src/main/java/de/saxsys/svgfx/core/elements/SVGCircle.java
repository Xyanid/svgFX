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
import de.saxsys.svgfx.core.content.SVGContentTypeLength;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.shape.Circle;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("circle")
public class SVGCircle extends SVGShapeBase<Circle> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGCircle(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final Circle createResult(final SVGCssStyle style) {

        Double centerX = hasContentType(CoreAttributeMapper.CENTER_X.getName())
                         ? getContentType(CoreAttributeMapper.CENTER_X.getName(),
                                          SVGContentTypeLength.class).getValue()
                         : SVGContentTypeLength.DEFAULT_VALUE;
        Double centerY = hasContentType(CoreAttributeMapper.CENTER_Y.getName())
                         ? getContentType(CoreAttributeMapper.CENTER_Y.getName(),
                                          SVGContentTypeLength.class).getValue()
                         : SVGContentTypeLength.DEFAULT_VALUE;

        return new Circle(centerX, centerY, getContentType(CoreAttributeMapper.RADIUS.getName(), SVGContentTypeLength.class).getValue());
    }

    //endregion
}
