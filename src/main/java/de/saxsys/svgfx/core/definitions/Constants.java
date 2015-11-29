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

package de.saxsys.svgfx.core.definitions;

import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.elements.Circle;
import de.saxsys.svgfx.core.elements.ClipPath;
import de.saxsys.svgfx.core.elements.Defs;
import de.saxsys.svgfx.core.elements.Ellipse;
import de.saxsys.svgfx.core.elements.Group;
import de.saxsys.svgfx.core.elements.Line;
import de.saxsys.svgfx.core.elements.LinearGradient;
import de.saxsys.svgfx.core.elements.Path;
import de.saxsys.svgfx.core.elements.Polygon;
import de.saxsys.svgfx.core.elements.Polyline;
import de.saxsys.svgfx.core.elements.RadialGradient;
import de.saxsys.svgfx.core.elements.Rectangle;
import de.saxsys.svgfx.core.elements.Stop;
import de.saxsys.svgfx.core.elements.Style;
import de.saxsys.svgfx.core.elements.Svg;
import de.saxsys.svgfx.core.elements.Use;

import java.util.Arrays;
import java.util.List;

/**
 * Contains constant values of
 * Created by Xyanid on 01.11.2015.
 */
public final class Constants {

    //region Fields

    /**
     * contains a list with all the known svg classes for elements.
     */
    public static final List<Class<? extends SVGElementBase<?>>> SVG_ELEMENT_CLASSES = Arrays.asList(Circle.class,
                                                                                                     ClipPath.class,
                                                                                                     Defs.class,
                                                                                                     Ellipse.class,
                                                                                                     Group.class,
                                                                                                     Line.class,
                                                                                                     LinearGradient.class,
                                                                                                     Path.class,
                                                                                                     Polyline.class,
                                                                                                     Polygon.class,
                                                                                                     RadialGradient.class,
                                                                                                     Rectangle.class,
                                                                                                     Stop.class,
                                                                                                     Style.class,
                                                                                                     Svg.class,
                                                                                                     Use.class);


    //endregion

    // region Constructor

    /**
     *
     */
    private Constants() {

    }

    // endregion
}
