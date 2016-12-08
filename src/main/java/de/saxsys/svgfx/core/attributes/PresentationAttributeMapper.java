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

package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeType;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeDashArray;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineJoin;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeType;
import de.saxsys.svgfx.core.elements.SVGClipPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * This class determines which svg presentation attributes are mapped to the desired {@link SVGAttributeType}.
 *
 * @author Xyanid on 09.03.2016.
 */
public class PresentationAttributeMapper extends BaseAttributeMapper<SVGDocumentDataProvider> {

    // region Constants

    /**
     * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
     */
    public static final PresentationAttributeMapper STROKE = new PresentationAttributeMapper("stroke", SVGAttributeTypePaint::new);
    /**
     * Determines the type of stroke used.
     */
    public static final PresentationAttributeMapper STROKE_TYPE = new PresentationAttributeMapper("stroke-type", SVGAttributeTypeStrokeType::new);
    /**
     * Determines the controls the pattern of dashes and gaps used to stroke paths.
     */
    public static final PresentationAttributeMapper STROKE_DASHARRAY = new PresentationAttributeMapper("stroke-dasharray", SVGAttributeTypeStrokeDashArray::new);
    /**
     * Determines the dash offset of a stroke.
     */
    public static final PresentationAttributeMapper STROKE_DASHOFFSET = new PresentationAttributeMapper("stroke-dashoffset", SVGAttributeTypeLength::new);
    /**
     * Determines the shape to be used at the end of open subpaths when they are stroked.
     */
    public static final PresentationAttributeMapper STROKE_LINECAP = new PresentationAttributeMapper("stroke-linecap", SVGAttributeTypeStrokeLineCap::new);
    /**
     * Determines the shape to be used at the corners of paths or basic shapes when they are stroked.
     */
    public static final PresentationAttributeMapper STROKE_LINEJOIN = new PresentationAttributeMapper("stroke-linejoin", SVGAttributeTypeStrokeLineJoin::new);
    /**
     * Determines a limit on the ratio of the miter length to the stroke-width. When the limit is exceeded, the join is converted from a miter to a bevel.
     */
    public static final PresentationAttributeMapper STROKE_MITERLIMIT = new PresentationAttributeMapper("stroke-miterlimit", SVGAttributeTypeDouble::new);
    /**
     * Determines the opacity of the outline on the current object.
     */
    public static final PresentationAttributeMapper STROKE_OPACITY = new PresentationAttributeMapper("stroke-opacity", SVGAttributeTypeDouble::new);
    /**
     * Determines the width of the outline on the current object.
     */
    public static final PresentationAttributeMapper STROKE_WIDTH = new PresentationAttributeMapper("stroke-width", SVGAttributeTypeLength::new);
    /**
     * Represents a clip path which will be applied to the given element.
     */
    public static final PresentationAttributeMapper CLIP_PATH = new PresentationAttributeMapper("clip-path", SVGAttributeTypeString::new);
    /**
     * Represents the clip rule which determines how an element inside a {@link SVGClipPath} will be used.
     * It works like the {@link PresentationAttributeMapper#FILL_RULE}.
     */
    public static final PresentationAttributeMapper CLIP_RULE = new PresentationAttributeMapper("clip-rule", SVGAttributeTypeString::new);
    /**
     * Represents the color of the interior of the given graphical element.
     */
    public static final PresentationAttributeMapper FILL = new PresentationAttributeMapper("fill", SVGAttributeTypePaint::new);
    /**
     * Represents the color of the interior of the given graphical element.
     */
    public static final PresentationAttributeMapper FILL_OPACITY = new PresentationAttributeMapper("fill-opacity", SVGAttributeTypeDouble::new);
    /**
     * Represents the algorithm which is to be used to determine what side of a path is inside the shape.
     */
    public static final PresentationAttributeMapper FILL_RULE = new PresentationAttributeMapper("fill-rule", SVGAttributeTypeFillRule::new);
    /**
     * Represents the color to use for a stop.
     */
    public static final PresentationAttributeMapper STOP_COLOR = new PresentationAttributeMapper("stop-color", SVGAttributeTypePaint::new);
    /**
     * Represents the transparency of a gradient, that is, the degree to which the background behind the element is overlaid.
     */
    public static final PresentationAttributeMapper STOP_OPACITY = new PresentationAttributeMapper("stop-opacity", SVGAttributeTypeDouble::new);
    /**
     * Represents a color which cna be used for other rule such as fill, stroke or stop-color.
     */
    public static final PresentationAttributeMapper COLOR = new PresentationAttributeMapper("color", SVGAttributeTypePaint::new);
    /**
     * Represents the transparency of an element, that is, the degree to which the background behind the element is overlaid.
     */
    public static final PresentationAttributeMapper OPACITY = new PresentationAttributeMapper("opacity", SVGAttributeTypeDouble::new);

    /**
     * Contains all the values that are available for this attribute class.
     */
    public static final List<PresentationAttributeMapper> VALUES = new ArrayList<>(Arrays.asList(STROKE,
                                                                                                 STROKE_TYPE,
                                                                                                 STROKE_DASHARRAY,
                                                                                                 STROKE_DASHOFFSET,
                                                                                                 STROKE_LINECAP,
                                                                                                 STROKE_LINEJOIN,
                                                                                                 STROKE_MITERLIMIT,
                                                                                                 STROKE_OPACITY,
                                                                                                 STROKE_WIDTH,
                                                                                                 CLIP_PATH,
                                                                                                 CLIP_RULE,
                                                                                                 FILL,
                                                                                                 FILL_OPACITY,
                                                                                                 FILL_RULE,
                                                                                                 STOP_COLOR,
                                                                                                 STOP_OPACITY,
                                                                                                 COLOR,
                                                                                                 OPACITY));

    // endregion

    //region Constructor

    /**
     * {@inheritDoc}
     */
    private PresentationAttributeMapper(final String name, final Function<SVGDocumentDataProvider, ? extends SVGAttributeType> contentTypeCreator) {
        super(name, contentTypeCreator);
    }

    //endregion
}