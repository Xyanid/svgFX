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

import de.saxsys.svgfx.core.content.SVGContentTypeBase;
import de.saxsys.svgfx.core.content.SVGContentTypeDouble;
import de.saxsys.svgfx.core.content.SVGContentTypeFillRule;
import de.saxsys.svgfx.core.content.SVGContentTypeLength;
import de.saxsys.svgfx.core.content.SVGContentTypePaint;
import de.saxsys.svgfx.core.content.SVGContentTypeString;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeDashArray;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeLineCap;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeLineJoin;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeType;
import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGClipPath;
import de.saxsys.svgfx.core.elements.SVGEllipse;
import de.saxsys.svgfx.core.elements.SVGShapeBase;
import javafx.scene.shape.FillRule;

/**
 * This class contains all enumerations used for processing svg data.
 *
 * @author Xyanid on 29.10.2015.
 */
public final class Enumerations {

    /**
     * Contains information which maps a given svg fill rule to the javafx fill rule.
     */
    public enum FillRuleMapping {
        EVEN_ODD("evenodd", FillRule.EVEN_ODD),
        NON_ZERO("nonzero ", FillRule.NON_ZERO);

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

    /**
     * Contains the possible values for {@link de.saxsys.svgfx.core.attributes.GeneralAttributeMapper}
     */
    public enum GradientUnits{
        NONE(""),
        USER_SPACE_ON_USE("userSpaceOnUse"),
        OBJECT_BOUNDING_BOX("objectBoundingBox");

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
        GradientUnits(final String name) {
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
         * Represents x component of a center position, this element is used for {@link SVGCircle}s and {@link SVGEllipse}s.
         */
        CENTER_X("cx"),
        /**
         * Represents y component of a center position, this element is used for {@link SVGCircle}s and {@link SVGEllipse}s.
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
}
