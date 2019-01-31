/*
 * Copyright 2015 - 2017 Xyanid
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

package de.saxsys.svgfx.core.definitions.enumerations;

import javafx.scene.shape.FillRule;

/**
 * Contains information which maps a given svg fill rule to the javafx {@link FillRule} rule.
 *
 * @author Xyanid on 01.02.2017.
 */
public enum FillRuleMapping {
    EVEN_ODD("evenodd", FillRule.EVEN_ODD),
    NON_ZERO("nonzero", FillRule.NON_ZERO);

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
