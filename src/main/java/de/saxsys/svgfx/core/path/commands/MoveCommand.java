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
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

/**
 * This represents a position command in a svg path which will either. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public class MoveCommand extends LineCommand {


    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is an absolute command or not.
     * @param position   the position to be used.
     */
    MoveCommand(final boolean isAbsolute, final Point2D position) {
        super(isAbsolute, position);
    }

    // endregion

    // region Implement LineCommand

    /**
     * Returns {@link Optional#empty()}.
     *
     * @param position value is irrelevant
     *
     * @return {@link Optional#empty()}.
     *
     * @throws PathException never.
     */
    @Override
    public Optional<Rectangle> getBoundingBox(final Point2D position) throws PathException {
        return Optional.empty();
    }

    // endregion
}