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
 * This represents a command that closes the current path back to he starting point. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public class CloseCommand extends PathCommand {

    // region Fields

    /**
     * Contains the position which will be done by this command.
     */
    private final Point2D startPoint;

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link Point2D}, which is the first position that has been moved to in a path command.
     *
     * @param isAbsolute determines if the command is absolute or not.
     * @param startPoint the start point.
     */
    CloseCommand(final boolean isAbsolute, final Point2D startPoint) {
        super(isAbsolute);
        this.startPoint = startPoint;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return startPoint;
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) throws PathException {
        return new Rectangle(getMinX(position, startPoint),
                             getMinY(position, startPoint),
                             getDistanceX(position, startPoint),
                             getDistanceY(position, startPoint));
    }

    // endregion
}