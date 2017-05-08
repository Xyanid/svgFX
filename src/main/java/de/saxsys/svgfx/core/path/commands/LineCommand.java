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
 * This represents a position command in a svg path which will either. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public class LineCommand extends PathCommand {

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
    LineCommand(final boolean isAbsolute, final Point2D position) {
        super(isAbsolute);
        this.position = position;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public Point2D getNextPosition(final Point2D position) throws PathException {
        final Point2D result;

        if (isAbsolute()) {
            result = this.position;
        } else {
            if (position == null) {
                throw new PathException("Position of a relative command can not be determined if the position is not provided");
            }
            result = position.add(this.position);
        }

        return result;
    }

    @Override
    public Rectangle getBoundingBox(final Point2D position) throws PathException {
        final Point2D nextPosition = getNextPosition(position);

        return new Rectangle(getMinX(position, nextPosition),
                             getMinY(position, nextPosition),
                             Math.abs(isAbsolute() ? getDistanceX(nextPosition, position) : this.position.getX()),
                             Math.abs(isAbsolute() ? getDistanceY(nextPosition, position) : this.position.getY()));
    }

    // endregion
}