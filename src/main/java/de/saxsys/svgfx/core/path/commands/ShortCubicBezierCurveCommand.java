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

package de.saxsys.svgfx.core.path.commands;

import de.saxsys.svgfx.core.path.PathException;

/**
 * This represents a short cubic bezier curve command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public final class ShortCubicBezierCurveCommand extends BezierCurveCommand {

    // region Constants

    /**
     * The absolute name of a short cubic bezier curve command.
     */
    public static final char ABSOLUTE_NAME = 'S';

    /**
     * The relative name of a short cubic bezier curve command.
     */
    public static final char RELATIVE_NAME = Character.toLowerCase(ABSOLUTE_NAME);

    // endregion

    // region Field

    ShortCubicBezierCurveCommand(final boolean isAbsolute) throws PathException {
        super(isAbsolute);
    }

    // endregion
}