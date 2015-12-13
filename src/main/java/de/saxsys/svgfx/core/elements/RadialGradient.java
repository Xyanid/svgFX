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
import javafx.scene.paint.CycleMethod;
import org.xml.sax.Attributes;

import java.util.List;

/**
 * This Class represents a radial gradient from svg
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("radialGradient") public class RadialGradient extends SVGGradientBase<javafx.scene.paint.RadialGradient> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public RadialGradient(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override RadialGradient

    @Override protected final javafx.scene.paint.RadialGradient createResultInternal() {

        List<javafx.scene.paint.Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException("given radial gradient does not have colors");
        }

        double cx = Double.parseDouble(getAttribute(CoreAttribute.CENTER_X.getName()));
        double cy = Double.parseDouble(getAttribute(CoreAttribute.CENTER_Y.getName()));

        double fx = getAttributes().containsKey(CoreAttribute.FOCUS_X.getName()) ? Double.parseDouble(getAttribute(CoreAttribute.FOCUS_X.getName())) : cx;
        double fy = getAttributes().containsKey(CoreAttribute.FOCUS_Y.getName()) ? Double.parseDouble(getAttribute(CoreAttribute.FOCUS_Y.getName())) : cy;

        double diffX = fx - cx;
        double diffY = fy - cy;

        double distance = diffX != 0 && diffY != 0 ? Math.hypot(diffX, diffY) : 0;
        double angle = diffX != 0 && diffY != 0 ? Math.atan2(diffY, diffX) : 0;

        return new javafx.scene.paint.RadialGradient(angle, distance, cx, cy, Double.parseDouble(getAttribute(CoreAttribute.RADIUS.getName())), false, CycleMethod.NO_CYCLE, stops);
    }

    //endregion
}
