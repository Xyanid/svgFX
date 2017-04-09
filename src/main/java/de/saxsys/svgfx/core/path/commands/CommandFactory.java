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


/**
 * This class is responsible for creating instances of an {@link PathCommand}.
 *
 * @author Xyanid on 09.04.2017.
 */
public final class CommandFactory {

    // region Constants

    public static final CommandFactory INSTANCE = new CommandFactory();

    // endregion

    // region Constructor

    private CommandFactory() {}

    // endregion

    // region Public

    public MoveCommand createMoveCommand(final String data) throws PathException {
        return new MoveCommand(data);
    }

    public LineCommand createLineCommand(final String data) throws PathException {
        return new LineCommand(data);
    }

    public HorizontalLineCommand createHorizontalLineCommand(final String data) throws PathException {
        return new HorizontalLineCommand(data);
    }

    public VerticalLineCommand createVerticalLineCommand(final String data) throws PathException {
        return new VerticalLineCommand(data);
    }

    public CloseCommand createCloseCommand(final Point2D startPoint) throws PathException {
        return new CloseCommand(startPoint);
    }

    public CubicBezierCurveCommand createCubicBezierCurveCommand(final String data) throws PathException {
        return new CubicBezierCurveCommand(data);
    }

    public ShortCubicBezierCurveCommand createShortCubicBezierCurveCommand(final String data) throws PathException {
        return new ShortCubicBezierCurveCommand(data);
    }

    public QuadraticBezierCurveCommand createQuadraticBezierCurveCommand(final String data) throws PathException {
        return new QuadraticBezierCurveCommand(data);
    }

    public ShortQuadraticBezierCurveCommand createShortQuadraticBezierCurveCommand(final String data) throws PathException {
        return new ShortQuadraticBezierCurveCommand(data);
    }

    // endregion
}