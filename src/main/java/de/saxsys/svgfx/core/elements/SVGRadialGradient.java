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

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

import java.util.List;

/**
 * This Class represents a radial gradient from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("radialGradient")
public class SVGRadialGradient extends SVGGradientBase<RadialGradient> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGRadialGradient(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGGradientBase

    @Override
    protected final RadialGradient createResult(final SVGCssStyle style) {

        List<Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException("Given radial gradient does not have colors");
        }

        Double centerX = getAttributeHolder().hasAttribute(CoreAttributeMapper.CENTER_X.getName()) ? getAttributeHolder().getAttribute(
                CoreAttributeMapper.CENTER_X.getName(),
                SVGAttributeTypeLength.class).getValue() : SVGAttributeTypeLength.DEFAULT_VALUE;
        Double centerY = getAttributeHolder().hasAttribute(CoreAttributeMapper.CENTER_Y.getName()) ? getAttributeHolder().getAttribute(
                CoreAttributeMapper.CENTER_Y.getName(),
                SVGAttributeTypeLength.class).getValue() : SVGAttributeTypeLength.DEFAULT_VALUE;
        Double
                focusX =
                getAttributeHolder().hasAttribute(CoreAttributeMapper.FOCUS_X.getName())
                ? getAttributeHolder().getAttribute(CoreAttributeMapper.FOCUS_X.getName(),
                                                    SVGAttributeTypeLength.class)
                                      .getValue()
                : SVGAttributeTypeLength.DEFAULT_VALUE;
        Double
                focusY =
                getAttributeHolder().hasAttribute(CoreAttributeMapper.FOCUS_Y.getName())
                ? getAttributeHolder().getAttribute(CoreAttributeMapper.FOCUS_Y.getName(),
                                                    SVGAttributeTypeLength.class)
                                      .getValue()
                : SVGAttributeTypeLength.DEFAULT_VALUE;

        // TODO figure out how to apply proportional values here
        // TODO convert the coordinates into the correct space, first convert then apply transform

        double diffX = focusX - centerX;
        double diffY = focusY - centerY;

        double distance = diffX != 0 && diffY != 0 ? Math.hypot(diffX, diffY) : 0;
        double angle = diffX != 0 && diffY != 0 ? Math.atan2(diffY, diffX) : 0;

        // TODO figure out if the focus angle is correct or not

        return new RadialGradient(angle,
                                  distance,
                                  centerX,
                                  centerY,
                                  getAttributeHolder().getAttribute(CoreAttributeMapper.RADIUS.getName(), SVGAttributeTypeLength.class).getValue(),
                                  false,
                                  CycleMethod.NO_CYCLE,
                                  stops);
    }

    //endregion
}
