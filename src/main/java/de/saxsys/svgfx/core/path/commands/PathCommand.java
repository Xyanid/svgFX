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

/**
 * This represents a basic path command. All literal operation in this class are supposed to be case insensitive.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class PathCommand {

    // region Field

    /**
     * Determine if this command is an absolute command or not. An absolute command has it coordinates as world coordinates.
     */
    private final boolean isAbsolute;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is absolute or not.
     */
    protected PathCommand(final boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
    }

    // endregion

    // region Getter

    /**
     * Returns {@link #isAbsolute}.
     *
     * @return {@link #isAbsolute}.
     */
    public boolean isAbsolute() {
        return isAbsolute;
    }

    // endregion

    // region Abstract

    /**
     * Gets the next possible position based on the internal state of the command and the provided position.
     *
     * @param position the position from which to start.
     *
     * @return a new  {@link Point2D} describing the position that is reached after this command has been applied.
     */
    public abstract Point2D getNextPosition(final Point2D position) throws PathException;

    /**
     * Gets the bounding box which would result of using the given position as the starting point and then applying the internal state of the command.
     *
     * @param position the position to be use as the starting point.
     *
     * @return a new  {@link Rectangle} describing the resulting bounding box.
     */
    public abstract Rectangle getBoundingBox(final Point2D position) throws PathException;

    // endregion
}