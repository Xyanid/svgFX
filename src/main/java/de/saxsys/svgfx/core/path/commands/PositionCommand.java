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

/**
 * This represents a position command in a svg path which will either be a {@link MoveCommand} or a {@link LineCommand}. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public abstract class PositionCommand extends PathCommand {

    // region Fields

    /**
     * Contains the position which will be done by this command.
     */
    private final Point2D position;

    // endregion

    // region Field

    /**
     * Creates a new instance.
     *
     * @param isAbsolute determines if the command is an absolute command or not.
     * @param position   the position to be used.
     */
    PositionCommand(final boolean isAbsolute, final Point2D position) {
        super(isAbsolute);
        this.position = position;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return position.add(this.position);
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        final Point2D nextPosition = getNextPosition(position);

        return new Rectangle(Math.min(position.getX(), nextPosition.getX()),
                             Math.min(position.getY(), nextPosition.getY()),
                             Math.abs(this.position.getX()),
                             Math.abs(this.position.getY()));
    }

    // endregion
}