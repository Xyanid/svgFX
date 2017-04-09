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
 * This represents a move command in a svg path. This class is immutable, so each instance represents a separate movement.
 *
 * @author Xyanid on 01.04.2017.
 */
public final class MoveCommand extends PositionCommand {

    // region Constants

    /**
     * The absolute name of a move command.
     */
    public static final char ABSOLUTE_NAME = 'M';

    /**
     * The relative name of a move command.
     */
    public static final char RELATIVE_NAME = Character.toLowerCase(ABSOLUTE_NAME);

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link String} that contains two numeric values separated by whitespaces or one comma which determine which position is moved to.
     * The given data may start and end with whitespaces and contain the either the {@link #ABSOLUTE_NAME} or the {@link #RELATIVE_NAME} followed by two numeric values
     * separated with as many whitespaces as desired or one comma.
     *
     * @param data the data to be used.
     *
     * @throws PathException if the string does not contain two numeric values separated by a whitespaces.
     */
    MoveCommand(final String data) throws PathException {
        super(data);
    }

    // endregion

    // region Implement PathCommand

    @Override
    public char getAbsoluteName() {
        return ABSOLUTE_NAME;
    }

    @Override
    public char getRelativeName() {
        return RELATIVE_NAME;
    }

    // endregion
}
