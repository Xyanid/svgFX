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

package de.saxsys.svgfx.core.definitions;

import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGClipPath;
import de.saxsys.svgfx.core.elements.SVGDefs;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGEllipse;
import de.saxsys.svgfx.core.elements.SVGGroup;
import de.saxsys.svgfx.core.elements.SVGLine;
import de.saxsys.svgfx.core.elements.SVGLinearGradient;
import de.saxsys.svgfx.core.elements.SVGPath;
import de.saxsys.svgfx.core.elements.SVGPolygon;
import de.saxsys.svgfx.core.elements.SVGPolyline;
import de.saxsys.svgfx.core.elements.SVGRadialGradient;
import de.saxsys.svgfx.core.elements.SVGRectangle;
import de.saxsys.svgfx.core.elements.SVGRoot;
import de.saxsys.svgfx.core.elements.SVGStop;
import de.saxsys.svgfx.core.elements.SVGStyle;
import de.saxsys.svgfx.core.elements.SVGUse;

import java.util.Arrays;
import java.util.List;

/**
 * Contains constant values of
 *
 * @author Xyanid on 01.11.2015.
 */
public final class Constants {

    //region Fields

    /**
     * contains a list with all the known svg classes for elements.
     */
    public static final List<Class<? extends SVGElementBase<?>>> SVG_ELEMENT_CLASSES = Arrays.asList(SVGCircle.class,
                                                                                                     SVGClipPath.class,
                                                                                                     SVGDefs.class,
                                                                                                     SVGEllipse.class,
                                                                                                     SVGGroup.class,
                                                                                                     SVGLine.class,
                                                                                                     SVGLinearGradient.class,
                                                                                                     SVGPath.class,
                                                                                                     SVGPolyline.class,
                                                                                                     SVGPolygon.class,
                                                                                                     SVGRadialGradient.class,
                                                                                                     SVGRectangle.class,
                                                                                                     SVGStop.class,
                                                                                                     SVGStyle.class,
                                                                                                     SVGRoot.class,
                                                                                                     SVGUse.class);

    /**
     * Indicator which determines that instead of an actual value value, another value is referenced.
     */
    public static final String IRI_IDENTIFIER = "url(#";

    /**
     * Indicator which determines that instead of an actual value value, another value is referenced.
     */
    public static final String IRI_FRAGMENT_IDENTIFIER = "#";

    /**
     * Determines the delimiter that separated a pair of points.
     */
    public static char POINTS_DELIMITER = ' ';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    public static char POSITION_DELIMITER = ',';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    public static String POSITION_DELIMITER_STRING = String.valueOf(POSITION_DELIMITER);

    //endregion

    // region Constructor

    /**
     *
     */
    private Constants() {

    }

    // endregion
}