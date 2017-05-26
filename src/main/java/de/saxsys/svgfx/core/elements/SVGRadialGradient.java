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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeCycleMethod;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.enumerations.GradientUnit;
import de.saxsys.svgfx.core.interfaces.ThrowableSupplier;
import de.saxsys.svgfx.core.utils.Wrapper;
import javafx.geometry.Point2D;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This Class represents a radial gradient from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGRadialGradient extends SVGGradientBase<RadialGradient> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "radialGradient";
    /**
     * Contains the default value for the radius if it was not provided
     */
    private static final Double DEFAULT_RADIUS = 0.5d;
    /**
     * Contains the default value for the radius if it was not provided
     */
    private static final Double DEFAULT_CENTER = 0.5d;
    // endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGRadialGradient(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }


    //endregion

    //region Override SVGGradientBase

    @Override
    protected final RadialGradient createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {
        return determineResult(null, null);
    }

    //endregion

    // region SVGGradientBase

    @Override
    public RadialGradient createResult(final ThrowableSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle, SVGException> boundingBox,
                                       final Transform elementTransform) throws SVGException {
        return determineResult(boundingBox, elementTransform);
    }

    // endregion

    // region Private

    private RadialGradient determineResult(final ThrowableSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle, SVGException> boundingBox,
                                           final Transform elementTransform) throws SVGException {
        final List<Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException("Given radial gradient does not have stop colors");
        }

        final double centerX = getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_X.getName(), Double.class, DEFAULT_CENTER);
        final double centerY = getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_Y.getName(), Double.class, DEFAULT_CENTER);
        final Wrapper<Point2D> center = new Wrapper<>(new Point2D(centerX, centerY));
        final double focusX = getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_X.getName(), Double.class, centerX);
        final double focusY = getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_Y.getName(), Double.class, centerY);
        final Wrapper<Point2D> focus = new Wrapper<>(new Point2D(focusX, focusY));
        final Wrapper<Double> radius = new Wrapper<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.RADIUS.getName(), Double.class, DEFAULT_RADIUS));

        convertToRelativeCoordinates(center, focus, radius, boundingBox, elementTransform);

        double diffX = focus.getOrFail().getX() - center.getOrFail().getX();
        double diffY = focus.getOrFail().getY() - center.getOrFail().getY();

        // here we check if x is 0 then use y or if y is 0 then use x, otherwise calculate
        double focusDistance = getDistance(diffX, diffY);

        return new RadialGradient(Math.toDegrees(Math.atan2(diffY, diffX)),
                                  // we need to adjust the focus distance to the radius here, eg focus distance of 0.5 with a radius of 0.5 is actually 0.25
                                  focusDistance > radius.get() ? 1.0d : focusDistance / radius.get(),
                                  center.getOrFail().getX(),
                                  center.getOrFail().getY(),
                                  radius.get(),
                                  true,
                                  getAttributeHolder().getAttributeValue(CoreAttributeMapper.SPREAD_METHOD.getName(), CycleMethod.class, SVGAttributeTypeCycleMethod.DEFAULT_VALUE),
                                  stops);
    }

    private void convertToRelativeCoordinates(final Wrapper<Point2D> center,
                                              final Wrapper<Point2D> focus,
                                              final Wrapper<Double> radius,
                                              final ThrowableSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle, SVGException> elementBoundingBoxSupplier,
                                              final Transform elementTransform) throws SVGException {

        final Optional<Transform> usedTransform = getTransform(elementTransform);

        final GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                 GradientUnit.class,
                                                                                 GradientUnit.OBJECT_BOUNDING_BOX);

        final List<Wrapper<Point2D>> points = Arrays.asList(center, focus);

        // when being in user space we first need to transform then make the values relative
        if (gradientUnit == GradientUnit.USER_SPACE_ON_USE) {
            final Rectangle boundingBox = transformBoundingBox(elementBoundingBoxSupplier.getOrFail(), elementTransform);
            usedTransform.ifPresent(transform -> transformPosition(center, focus, radius, transform));
            convertToRelativeCoordinates(boundingBox, radius, points);
        } else if (usedTransform.isPresent()) {
            final Rectangle boundingBox = transformBoundingBox(elementBoundingBoxSupplier.getOrFail(), elementTransform);
            convertToAbsoluteCoordinates(boundingBox, radius, points);
            transformPosition(center, focus, radius, usedTransform.get());
            convertToRelativeCoordinates(boundingBox, radius, points);
        }
    }

    private void transformPosition(final Wrapper<Point2D> center,
                                   final Wrapper<Point2D> focus,
                                   final Wrapper<Double> radius,
                                   final Transform transform) {

        final Wrapper<Point2D> radiusP = new Wrapper<>(new Point2D(center.getOrFail().getX() + radius.get(),
                                                                   center.getOrFail().getY() + radius.get()));

        center.set(transform.transform(center.getOrFail()));
        focus.set(transform.transform(focus.getOrFail()));
        radiusP.set(transform.transform(radiusP.getOrFail()));

        // for radius we take a point on the circle apply transform then measure the new distance which is the radius
        final double radiusXTmp = radiusP.getOrFail().getX() - center.getOrFail().getX();
        final double radiusYTmp = radiusP.getOrFail().getY() - center.getOrFail().getY();
        radius.set(radiusXTmp > radiusYTmp ? radiusXTmp : radiusYTmp);
    }

    private void convertToRelativeCoordinates(final Rectangle boundingBox,
                                              final Wrapper<Double> radius,
                                              final List<Wrapper<Point2D>> points) throws SVGException {

        if (boundingBox.getWidth() == 0.0d || boundingBox.getHeight() == 0.0d) {
            return;
        }

        convertToRelativeCoordinates(boundingBox, points);
        radius.set(radius.get() / (boundingBox.getHeight() > boundingBox.getWidth() ? boundingBox.getHeight() : boundingBox.getWidth()));
    }

    private void convertToAbsoluteCoordinates(final Rectangle boundingBox,
                                              final Wrapper<Double> radius,
                                              final List<Wrapper<Point2D>> points) throws SVGException {

        if (boundingBox.getWidth() == 0.0d || boundingBox.getHeight() == 0.0d) {
            return;
        }

        convertToAbsoluteCoordinates(boundingBox, points);
        radius.set(radius.get() * (boundingBox.getHeight() > boundingBox.getWidth() ? boundingBox.getHeight() : boundingBox.getWidth()));
    }

    private double getDistance(final double diffX, final double diffY) {
        return diffX == 0.0d ? Math.abs(diffY) : diffY == 0.0d ? Math.abs(diffX) : Math.hypot(diffX, diffY);
    }

    // endregion
}