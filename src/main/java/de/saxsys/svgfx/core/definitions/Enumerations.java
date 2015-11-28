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

import javafx.scene.shape.FillRule;

/**
 * This class contains all enumerations used for processing svg data.
 * Created by Xyanid on 29.10.2015.
 */
public final class Enumerations {

    /**
     * Contains information which maps a given svg fill rule to the javafx fill rule.
     */
    public enum FillRuleMapping {
        EVEN_ODD("evenodd", FillRule.EVEN_ODD), NON_ZERO("nonzero ", FillRule.NON_ZERO);

        // region Fields

        /**
         * the name of the rule within css.
         */
        private final String name;

        /**
         * the {@link FillRule} that this rule corresponds to.
         */
        private final FillRule rule;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name name to be used
         * @param rule {@link FillRule} this rule corresponds to
         */
        FillRuleMapping(final String name, final FillRule rule) {
            this.name = name;
            this.rule = rule;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link FillRuleMapping#name}.
         *
         * @return {@link FillRuleMapping#name}
         */
        public final String getName() {
            return name;
        }

        /**
         * Returns the {@link FillRuleMapping#rule}.
         *
         * @return {@link FillRuleMapping#rule}
         */
        public final FillRule getRule() {
            return rule;
        }

        // endregion
    }

    /**
     * Contains the core attributes each svg element may have.
     */
    public enum CoreAttribute {

        /**
         * The id for an element, needed in case an element is referenced by another element.
         */
        ID("id"),
        /**
         * Represents the transformation to be applied to an element.
         */
        TRANSFORM("transform"),
        /**
         * Represents the style of an element, the style need to follow the css text restrictions to be used.
         */
        STYLE("style"),
        /**
         * Represents a class link to an existing style, in this case the element will use this link to style itself.
         */
        CLASS("class"),
        /**
         * Represents x component of a center position, this element is used for {@link de.saxsys.svgfx.core.elements.Circle}s and {@link de.saxsys.svgfx.core.elements.Ellipse}s.
         */
        CENTER_X("cx"),
        /**
         * Represents y component of a center position, this element is used for {@link de.saxsys.svgfx.core.elements.Circle}s and {@link de.saxsys.svgfx.core.elements.Ellipse}s.
         */
        CENTER_Y("cy"),
        /**
         * Represents a radius.
         */
        RADIUS("r"),
        /**
         * Represents a radius which is used in the x direction.
         */
        RADIUS_X("rx"),
        /**
         * Represents a radius which is used in the y direction.
         */
        RADIUS_Y("ry"),
        /**
         * Represents the focus in x direction, this attribute is used by a radial gradient.
         */
        FOCUS_X("fx"),
        /**
         * Represents the focus in y direction, this attribute is used by a radial gradient.
         */
        FOCUS_Y("fy"),
        /**
         * Represents a comma separated list of points.
         */
        POINTS("points"),
        /**
         * Represents the start x component of a line.
         */
        START_X("x1"),
        /**
         * Represents the start y component of a line.
         */
        START_Y("y1"),
        /**
         * Represents the end x component of a line.
         */
        END_X("x2"),
        /**
         * Represents the end y component of a line.
         */
        END_Y("y2"),
        /**
         * Represents a series of path descriptions.
         */
        PATH_DESCRIPTION("d"),
        /**
         * Represents the x component of a position, how this is used depends on the element it is used in.
         */
        POSITION_X("x"),
        /**
         * Represents the y component of a position, how this is used depends on the element it is used in.
         */
        POSITION_Y("y"),
        /**
         * Represents the width of an element.
         */
        WIDTH("width"),
        /**
         * Represents the height of an element.
         */
        HEIGHT("height"),
        /**
         * Represents the offset from a start position.
         */
        OFFSET("offset"),
        /**
         * Represents the type of the element.
         */
        TYPE("type");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        CoreAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * This contains all the presentation attributes an svg element might. The presentation attributes are used for {@link de.saxsys.svgfx.core.SVGShapeBase}.
     */
    public enum PresentationAttribute {
        /**
         * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
         */
        STROKE("stroke"),
        /**
         * Determines the type of stroke used.
         */
        STROKE_TYPE("stroke-type"),
        /**
         * Determines the controls the pattern of dashes and gaps used to stroke paths.
         */
        STROKE_DASHARRAY("stroke-dasharray"),
        /**
         * Determines the dash offset of a stroke.
         */
        STROKE_DASHOFFSET("stroke-dashoffset"),
        /**
         * Determines the shape to be used at the end of open subpaths when they are stroked.
         */
        STROKE_LINECAP("stroke-linecap"),
        /**
         * Determines the shape to be used at the corners of paths or basic shapes when they are stroked.
         */
        STROKE_LINEJOIN("stroke-linejoin"),
        /**
         * Determines a limit on the ratio of the miter length to the stroke-width. When the limit is exceeded, the join is converted from a miter to a bevel.
         */
        STROKE_MITERLIMIT("stroke-miterlimit"),
        /**
         * Determines the opacity of the outline on the current object.
         */
        STROKE_OPACITY("stroke-opacity"),
        /**
         * Determines the width of the outline on the current object.
         */
        STROKE_WIDTH("stroke-width"),
        /**
         * Represents a clip path which will be applied to the given element.
         */
        CLIP_PATH("clip-path"),
        /**
         * Represents the clip rule which determines how an element inside a {@link de.saxsys.svgfx.core.elements.ClipPath} will be used.
         * It works like the {@link Enumerations.PresentationAttribute#FILL_RULE}.
         */
        CLIP_RULE("clip-rule"),
        /**
         * Represents the color of the interior of the given graphical element.
         */
        FILL("fill"),
        /**
         * Represents the algorithm which is to be used to determine what side of a path is inside the shape.
         */
        FILL_RULE("fill-rule"),
        /**
         * Represents the color to use for a stop.
         */
        STOP_COLOR("stop-color"),
        /**
         * Represents the transparency of a gradient, that is, the degree to which the background behind the element is overlaid.
         */
        STOP_OPACITY("stop-opacity"),
        /**
         * Represents a color which cna be used for other rule such as fill, stroke or stop-color.
         */
        COLOR("color"),
        /**
         * Represents the transparency of an element, that is, the degree to which the background behind the element is overlaid.
         */
        OPACITY("opacity");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        PresentationAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * Contains the xlink attributes each svg element may have.
     */
    public enum XLinkAttribute {

        /**
         * Meaning this element has a reference to an existing element.
         */
        XLINK_HREF("xlink:href");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        XLinkAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * Determines which keyword in a transform attribute of a matrix map to their corresponding javafx classes.
     */
    public enum Matrix {

        NONE(""),
        MATRIX("matrix"),
        TRANSLATE("translate"),
        SCALE("scale"),
        ROTATE("rotate"),
        SKEW_X("skewX"),
        SKEW_Y("skewY");

        // region Fields

        /**
         * The of the transformation within svg.
         */
        private final String name;


        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element.
         */
        Matrix(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link Matrix#name}.
         *
         * @return the {@link Matrix#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }
}
