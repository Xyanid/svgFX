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

package de.saxsys.svgfx.core.path;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

/**
 * This represents a basic path command. All literal operation in this class are supposed to be case insensitive.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class PathCommand {

    // region Fields

    /**
     * Determines the name of this command.
     */
    private final char name;

    // endregion

    // region Constructor

    protected PathCommand(final char name) {this.name = Character.toLowerCase(name);}

    // endregion

    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return the {@link #name}.
     */
    public char getName() {
        return name;
    }

    // endregion

    // region Public

    /**
     * Determines if the given command name is equals to the {@link #name}, which is the case when then given name is the same but regardless of casing.
     *
     * @param name the name to compare to.
     *
     * @return true if the given name is the same as the {@link #name} regardless of case, otherwise false.
     */
    public final boolean isCommand(final char name) {
        return this.name == name;
    }

    // endregion

    // region Protected

    /**
     * Takes the given {@link String} and returns only the elements beyond the first occurrence of the {@link #name}.
     *
     * @param data the command to be parsed.
     *
     * @return a new {@link String} containing the data beyond the first occurrence of the {@link #name} or the given data if the {@link #name} was not found.
     */
    protected final String stripCommandName(final String data) {
        for (int i = 0; i < data.length(); i++) {
            if (name == Character.toLowerCase(data.charAt(i))) {
                return data.substring(i + 1, data.length());
            }
        }

        return data;
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
    public abstract Point2D getNextPosition(final Point2D position);

    /**
     * Gets the bounding box which would result of using the given position as the starting point and then applying the internal state of the command.
     *
     * @param position the position to be use as the starting point.
     *
     * @return a new  {@link Rectangle} describing the resulting bounding box.
     */
    public abstract Rectangle getBoundingBox(final Point2D position);

    // endregion
}
