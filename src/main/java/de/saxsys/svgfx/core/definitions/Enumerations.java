package de.saxsys.svgfx.core.definitions;

import javafx.scene.shape.FillRule;
import org.apache.commons.lang3.StringUtils;

/**
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
     * Contains the basic attributes each svg element may have.
     */
    public enum SvgAttribute {

        /**
         * The id for an element, needed in case this element is referenced by another element.
         */
        ID("id"),
        /**
         * Meaning this element has a reference to an existing element.
         */
        XLINK_HREF("xlink:href"),
        /**
         * Meaning the element has a transformation applied.
         */
        TRANSFORM("transform"),
        /**
         * Meaning the element has its own style included in a declaration block.
         */
        STYLE("style"),
        /**
         * Meaning the element has a reference to an existing style.
         */
        CLASS("class"),
        /**
         * Meaning this attributes represents the center x.
         */
        CENTER_X("cx"),
        /**
         * Meaning this attribute represents the center y.
         */
        CENTER_Y("cy"),
        /**
         * Meaning this attribute represents radius.
         */
        RADIUS("r"),
        /**
         * Meaning this attribute represents a radius x.
         */
        RADIUS_X("rx"),
        /**
         * Meaning this attribute represents a radius y.
         */
        RADIUS_Y("ry"),
        /**
         * Meaning this attribute represents focus x.
         */
        FOCUS_X("fx"),
        /**
         * Meaning this attribute represents focus y.
         */
        FOCUS_Y("fy"),
        /**
         * Meaning this attributes represents a comma separated list of points.
         */
        POINTS("points"),
        X1("x1"),
        Y1("y1"),
        X2("x2"),
        Y2("y2"),
        D("d"),
        X("x"),
        Y("y"),
        WIDTH("width"),
        HEIGHT("height"),
        OFFSET("offset"),
        TYPE("type"),
        MEDIA("media"),
        TITLE("title");

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
        SvgAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link SvgAttribute#name}.
         *
         * @return the {@link SvgAttribute#name}
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

        FILL("fill"),
        FILL_RULE("fill-rule"),
        COLOR("stop-color"),
        OPACITY("stop-opacity");

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
         * Returns the {@link SvgAttribute#name}.
         *
         * @return the {@link SvgAttribute#name}
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

        NONE(StringUtils.EMPTY),
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
