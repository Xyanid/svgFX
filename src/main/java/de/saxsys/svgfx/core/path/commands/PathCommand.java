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
import java.util.function.BiFunction;
import java.util.function.Function;

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

    // region Public Abstract

    /**
     * Gets the next possible absoluteCurrentPoint based on the internal state of the command and the provided absoluteCurrentPoint.
     *
     * @param absoluteCurrentPoint the absoluteCurrentPoint from which to start.
     *
     * @return a new  {@link Point2D} describing the absoluteCurrentPoint that is reached after this command has been applied.
     *
     * @throws PathException if the next absoluteCurrentPoint can not be determined.
     */
    public abstract Point2D getAbsoluteEndPoint(final Point2D absoluteCurrentPoint) throws PathException;

    /**
     * Gets the bounding box which would result of using the given absoluteCurrentPoint as the starting point and then applying the internal state of the command.
     *
     * @param absoluteCurrentPoint the absoluteCurrentPoint to be use as the starting point.
     *
     * @return a {@link Optional} describing the resulting bounding box.
     *
     * @throws PathException if the bounding box can not be determined.
     */
    public abstract Optional<Rectangle> getBoundingBox(final Point2D absoluteCurrentPoint) throws PathException;

    // endregion

    // region Protected

    /**
     * Combines the two given points based on hte {@link #isAbsolute}. If the command is absolute, the second point will be returned.
     * Otherwise second point will be added to the first.
     *
     * @param first  the first point to use, may be null of the command is absolute.
     * @param second the second point.
     *
     * @return either the second point if the command is absolute or the result of adding the second point to the first one.
     *
     * @throws PathException if the command is not absolute the first point is null.
     */
    protected Point2D addPoints(final Point2D first, final Point2D second) throws PathException {
        if (isAbsolute()) {
            return second;
        } else {
            if (first == null) {
                throw new PathException("Position of a relative command can not be determined if the position is not provided");
            }
            return first.add(second);
        }
    }

    /**
     * Returns the x distance between the two points. if one of the points is null, then then 0.0 will be returned.
     *
     * @param first  the first point.
     * @param second the second point.
     *
     * @return the distance x between the two points, or 0.0 if either of the points is null.
     *
     * @throws PathException if both points are null.
     */
    protected double getDistanceX(final Point2D first, final Point2D second) throws PathException {
        return getValueOrFail(first, second, value -> 0.0d, (firstValue, secondValue) -> Math.abs(firstValue.getX() - secondValue.getX()));
    }

    /**
     * Returns the minimum x of the two points. If either of the points is null, then then x of the other point will be returned.
     *
     * @param first  the first point.
     * @param second the second point.
     *
     * @return the minimum x between the two points.
     *
     * @throws PathException if both points are null.
     */
    protected double getMinX(final Point2D first, final Point2D second) throws PathException {
        return getValueOrFail(first, second, Point2D::getX, (firstP, secondP) -> Math.min(firstP.getX(), secondP.getX()));
    }

    /**
     * Returns the y distance between the two points. if one of the points is null, then then 0.0 will be returned.
     *
     * @param first  the first point.
     * @param second the second point.
     *
     * @return the distance y between the two points, or 0.0 if either of the points is null.
     *
     * @throws PathException if both points are null.
     */
    protected double getDistanceY(final Point2D first, final Point2D second) throws PathException {
        return getValueOrFail(first, second, value -> 0.0d, (firstValue, secondValue) -> Math.abs(firstValue.getY() - secondValue.getY()));
    }

    /**
     * Returns the minimum y of the two points. If either of the points is null, then then y of the other point will be returned.
     *
     * @param first  the first point.
     * @param second the second point.
     *
     * @return the minimum y between the two points.
     *
     * @throws PathException if both points are null.
     */
    protected double getMinY(final Point2D first, final Point2D second) throws PathException {
        return getValueOrFail(first, second, Point2D::getY, (firstP, secondP) -> Math.min(firstP.getY(), secondP.getY()));
    }

    /**
     * Ensures to always return a value of the given values, even if either of the provided values is null. So if both values are given the resolver is called.
     * If one of the values is null, the valueProvider will provide the result based on the value that was not null.
     *
     * @param first         the first value to use.
     * @param second        the second value to use.
     * @param valueProvider the {@link Function} to be called when one of the given values was null.
     * @param resolver      the {@link BiFunction} to be called when both values are not null.
     * @param <T>           the type of the values.
     * @param <R>           the type of the resulting value.
     *
     * @return <ul>
     * <li>if first and second are not null = the value of the resolver is returned.</li>
     * <li>if first or second is null = the value of the valueProvider is returned, for the value that is not null</li>
     * </ul>
     *
     * @throws PathException if both values are null.
     */
    protected <T, R> R getValueOrFail(final T first,
                                      final T second,
                                      final Function<T, R> valueProvider,
                                      final BiFunction<T, T, R> resolver) throws PathException {
        if (first == null) {
            if (second == null) {
                throw new PathException("May not determine desired value if both arguments are null");
            } else {
                return valueProvider.apply(second);
            }
        } else if (second == null) {
            return valueProvider.apply(first);
        } else {
            return resolver.apply(first, second);
        }
    }

    // endregion
}