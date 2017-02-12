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

import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.FillRule;

/**
 * Contains information which maps a given svg spread method rule to the javafx {@link CycleMethod} rule.
 *
 * @author Xyanid on 01.02.2017.
 */
public enum CycleMethodMapping {
    PAD("pad", CycleMethod.NO_CYCLE),
    REFLECT("reflect ", CycleMethod.REFLECT),
    REPEAT("repeat ", CycleMethod.REPEAT);

    // region Fields

    /**
     * the name of the rule within css.
     */
    private final String name;

    /**
     * the {@link FillRule} that this rule corresponds to.
     */
    private final CycleMethod method;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param name   name to be used
     * @param method {@link CycleMethod} this spread method corresponds to
     */
    CycleMethodMapping(final String name, final CycleMethod method) {
        this.name = name;
        this.method = method;
    }

    // endregion

    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return {@link #name}
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the {@link #method}.
     *
     * @return {@link #method}
     */
    public final CycleMethod getMethod() {
        return method;
    }

    // endregion
}
