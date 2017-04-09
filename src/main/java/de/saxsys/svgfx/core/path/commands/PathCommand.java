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

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

/**
 * This represents a basic path command. All literal operation in this class are supposed to be case insensitive.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class PathCommand {

    // region Public

    /**
     * Determines if the given command name is equals to the {@link #getAbsoluteName()} or {@link #getRelativeName()},
     * which is the case when then given name is the same but regardless of casing.
     *
     * @param name the name to compare to.
     *
     * @return true if the given name is the same as the {@link #getAbsoluteName()} or {@link #getRelativeName()}, otherwise false.
     */
    public final boolean isCommand(final char name) {
        return getAbsoluteName() == name || getRelativeName() == name;
    }

    /**
     * Determines if the given name is a absolute command name or not.
     *
     * @param name the name to check.
     *
     * @return a new {@link Optional} with true if the name is the absolute name, false if the name is the relative name or {@link Optional#empty()} if the name is neither.
     */
    public Optional<Boolean> isAbsoluteCommand(final char name) {
        return isAbsoluteOrRelativeCommand(name, true);
    }

    /**
     * Determines if the given name is a relative command name or not.
     *
     * @param name the name to check.
     *
     * @return a new {@link Optional} with true if the name is the relative name, false if the name is the absolute name or {@link Optional#empty()} if the name is neither.
     */
    public Optional<Boolean> isRelativeCommand(final char name) {
        return isAbsoluteOrRelativeCommand(name, false);
    }

    // endregion

    // region Protected

    /**
     * Takes the given {@link String} and returns only the elements beyond the first occurrence of the {@link #getAbsoluteName()} or {@link #getRelativeName()}.
     *
     * @param data the command to be parsed.
     *
     * @return a new {@link String} containing the data beyond the first occurrence of the {@link #getAbsoluteName()} or {@link #getRelativeName()}
     * or the given data if the {@link #getAbsoluteName()} or {@link #getRelativeName()} was not found.
     */
    protected final String stripCommandName(final String data) {
        for (int i = 0; i < data.length(); i++) {
            final char dataChar = data.charAt(i);
            if (getAbsoluteName() == dataChar || getRelativeName() == dataChar) {
                return data.substring(i + 1, data.length());
            }
        }

        return data;
    }

    // endregion

    // region Abstract

    /**
     * Returns the absolute name of this command.
     *
     * @return the absolute name of this command.
     */
    public abstract char getAbsoluteName();

    /**
     * Returns the relative name of this command.
     *
     * @return the relative name of this command.
     */
    public abstract char getRelativeName();

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

    // region Private

    private Optional<Boolean> isAbsoluteOrRelativeCommand(final char name, final boolean checkForAbsolute) {
        if (getAbsoluteName() == name) {
            return Optional.of(checkForAbsolute);
        } else if (getRelativeName() == name) {
            return Optional.of(!checkForAbsolute);
        } else {
            return Optional.empty();
        }
    }

    // endregion
}