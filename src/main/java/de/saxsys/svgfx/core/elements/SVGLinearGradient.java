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
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

import java.util.List;

/**
 * This class represents the linear gradient element from svg
 *
 * @author Xyanid on 24.10.2015.
 */
@SVGElementMapping("linearGradient")
public class SVGLinearGradient extends SVGGradientBase<LinearGradient> {


    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGLinearGradient(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final LinearGradient createResult(final SVGCssStyle style) throws SVGException {

        List<Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException("Given linear gradient does not have colors");
        }

        Double
                startX =
                getAttributeHolder().hasAttribute(CoreAttributeMapper.START_X.getName())
                ? getAttributeHolder().getAttribute(CoreAttributeMapper.START_X.getName(),
                                                    SVGAttributeTypeLength.class)
                                      .getValue()
                : SVGAttributeTypeLength.DEFAULT_VALUE;
        Double
                startY =
                getAttributeHolder().hasAttribute(CoreAttributeMapper.START_Y.getName())
                ? getAttributeHolder().getAttribute(CoreAttributeMapper.START_Y.getName(),
                                                    SVGAttributeTypeLength.class)
                                      .getValue()
                : SVGAttributeTypeLength.DEFAULT_VALUE;
        Double
                endX =
                getAttributeHolder().hasAttribute(CoreAttributeMapper.END_X.getName())
                ? getAttributeHolder().getAttribute(CoreAttributeMapper.END_X.getName(), SVGAttributeTypeLength.class).getValue()
                : SVGAttributeTypeLength.DEFAULT_VALUE;
        Double
                endY =
                getAttributeHolder().hasAttribute(CoreAttributeMapper.END_Y.getName())
                ? getAttributeHolder().getAttribute(CoreAttributeMapper.END_Y.getName(), SVGAttributeTypeLength.class).getValue()
                : SVGAttributeTypeLength.DEFAULT_VALUE;

        // TODO figure out how to apply proportional values here


        // TODO convert the coordinates into the correct space, first convert then apply transform

        return new LinearGradient(startX, startY, endX, endY, false, CycleMethod.NO_CYCLE, stops);
    }

    //endregion
}
