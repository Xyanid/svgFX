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

/**
 * This represents a line command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public final class LineCommand extends PositionCommand {

    // region Constants

    private static final char NAME = 'L';

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link String} that contains two numeric values separated by a whitespaces which determine which position is moved to.
     * The given data may start and end with whitespaces and may have as many whitespaces as desired between the two numeric values.
     *
     * @param data the data to be used.
     *
     * @throws PathException if the string does not contain two numeric values separated by a whitespaces.
     */
    LineCommand(final String data) throws PathException {
        super(NAME, data);
    }

    // endregion
}
