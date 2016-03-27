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

import de.saxsys.svgfx.core.content.SVGAttributeTypeGradientUnits;
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
     * Contains the possible values for {@link SVGAttributeTypeGradientUnits}.
     */
    public enum GradientUnits {
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
}
