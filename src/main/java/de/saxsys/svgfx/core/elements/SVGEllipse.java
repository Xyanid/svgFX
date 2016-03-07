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
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.StringUtils;
import javafx.scene.shape.Ellipse;
import org.xml.sax.Attributes;

/**
 * This class represents a svg ellipse element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("ellipse")
public class SVGEllipse extends SVGShapeBase<Ellipse> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGEllipse(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final Ellipse createResult(final SVGCssStyle style) {

        String centerX = getAttribute(Enumerations.CoreAttribute.CENTER_X.getName());
        String centerY = getAttribute(Enumerations.CoreAttribute.CENTER_Y.getName());

        return new Ellipse(StringUtils.isNullOrEmpty(centerX) ? 0.0d : Double.parseDouble(centerX),
                           StringUtils.isNullOrEmpty(centerY) ? 0.0d : Double.parseDouble(centerY),
                           Double.parseDouble(getAttribute(Enumerations.CoreAttribute.RADIUS_X.getName())),
                           Double.parseDouble(getAttribute(Enumerations.CoreAttribute.RADIUS_Y.getName())));
    }

    //endregion
}
