/*
 * Copyright 2015 - 2016 Xyanid
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
import de.saxsys.svgfx.core.interfaces.SVGSupplier;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
    public RadialGradient createResult(final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox,
                                       final Transform elementTransform) throws SVGException {
        return determineResult(boundingBox, elementTransform);
    }

    // endregion

    // region Private

    private RadialGradient determineResult(final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox,
                                           final Transform elementTransform) throws SVGException {
        final List<Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException(SVGException.Reason.MISSING_STOPS, "Given radial gradient does not have colors");
        }

        final AtomicReference<Double> centerX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_X.getName(), Double.class, DEFAULT_CENTER));
        final AtomicReference<Double> centerY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_Y.getName(), Double.class, DEFAULT_CENTER));
        final AtomicReference<Double> focusX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_X.getName(), Double.class, centerX.get()));
        final AtomicReference<Double> focusY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_Y.getName(), Double.class, centerY.get()));
        final AtomicReference<Double> radius = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.RADIUS.getName(), Double.class, DEFAULT_RADIUS));

        convertToObjectBoundingBox(centerX, centerY, focusX, focusY, radius, boundingBox, elementTransform);

        double diffX = focusX.get() - centerX.get();
        double diffY = focusY.get() - centerY.get();

        // here we check if x is 0 then use y or if y is 0 then use x, otherwise calculate
        double focusDistance = getDistance(diffX, diffY);

        return new RadialGradient(Math.toDegrees(Math.atan2(diffY, diffX)),
                                  // we need to adjust the focus distance to the radius here, eg focus distance of 0.5 with a radius of 0.5 is actually 0.25
                                  focusDistance > radius.get() ? 1.0d : focusDistance / radius.get(),
                                  centerX.get(),
                                  centerY.get(),
                                  radius.get(),
                                  true,
                                  getAttributeHolder().getAttributeValue(CoreAttributeMapper.SPREAD_METHOD.getName(), CycleMethod.class, SVGAttributeTypeCycleMethod.DEFAULT_VALUE),
                                  stops);
    }

    private void convertToObjectBoundingBox(final AtomicReference<Double> centerX,
                                            final AtomicReference<Double> centerY,
                                            final AtomicReference<Double> focusX,
                                            final AtomicReference<Double> focusY,
                                            final AtomicReference<Double> radius,
                                            final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox,
                                            final Transform elementTransform) throws SVGException {

        final Optional<Transform> usedTransform = getTransform(elementTransform);

        final GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                 GradientUnit.class,
                                                                                 GradientUnit.OBJECT_BOUNDING_BOX);

        // when being in user space we first need to transform then make the values relative
        if (gradientUnit == GradientUnit.USER_SPACE_ON_USE) {
            if (boundingBox == null) {
                throw new SVGException(SVGException.Reason.MISSING_ELEMENT, "Can not create linear gradient when user space is defined but no bounding box is provided.");
            }

            usedTransform.ifPresent(transform -> transformPosition(centerX, centerY, focusX, focusY, radius, transform));
            convertToObjectBoundingBox(centerX, centerY, focusX, focusY, radius, boundingBox.get());
        } else if (usedTransform.isPresent()) {
            final SVGAttributeTypeRectangle.SVGTypeRectangle rectangle = boundingBox.get();
            convertFromObjectBoundingBox(centerX, centerY, focusX, focusY, radius, rectangle);
            transformPosition(centerX, centerY, focusX, focusY, radius, usedTransform.get());
            convertToObjectBoundingBox(centerX, centerY, focusX, focusY, radius, rectangle);
        }
    }

    private void transformPosition(final AtomicReference<Double> centerX,
                                   final AtomicReference<Double> centerY,
                                   final AtomicReference<Double> focusX,
                                   final AtomicReference<Double> focusY,
                                   final AtomicReference<Double> radius,
                                   final Transform transform) {

        final AtomicReference<Double> radiusX = new AtomicReference<>(centerX.get() + radius.get());
        final AtomicReference<Double> radiusY = new AtomicReference<>(centerY.get() + radius.get());

        transformPosition(centerX, centerY, transform);
        transformPosition(focusX, focusY, transform);
        transformPosition(radiusX, radiusY, transform);
        // for radius we take a point on the circle apply transform then measure the new distance which is the radius
        final double radiusXTmp = radiusX.get() - centerX.get();
        final double radiusYTmp = radiusY.get() - centerY.get();
        radius.set(radiusXTmp > radiusYTmp ? radiusXTmp : radiusYTmp);
    }

    private void convertToObjectBoundingBox(final AtomicReference<Double> centerX,
                                            final AtomicReference<Double> centerY,
                                            final AtomicReference<Double> focusX,
                                            final AtomicReference<Double> focusY,
                                            final AtomicReference<Double> radius,
                                            final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox) throws SVGException {

        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

        if (width == 0.0d || height == 0.0d) {
            return;
        }

        centerX.set(Math.abs(boundingBox.getMinX().getValue() - centerX.get()) / width);
        centerY.set(Math.abs(boundingBox.getMinY().getValue() - centerY.get()) / height);
        focusX.set(Math.abs(boundingBox.getMinX().getValue() - focusX.get()) / width);
        focusY.set(Math.abs(boundingBox.getMinY().getValue() - focusY.get()) / height);
        radius.set(radius.get() / (height > width ? height : width));
    }

    private void convertFromObjectBoundingBox(final AtomicReference<Double> centerX,
                                              final AtomicReference<Double> centerY,
                                              final AtomicReference<Double> focusX,
                                              final AtomicReference<Double> focusY,
                                              final AtomicReference<Double> radius,
                                              final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox) throws SVGException {

        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

        if (width == 0.0d || height == 0.0d) {
            return;
        }

        centerX.set(boundingBox.getMinX().getValue() + centerX.get() * width);
        centerY.set(boundingBox.getMinY().getValue() + centerY.get() * height);
        focusX.set(boundingBox.getMinX().getValue() + focusX.get() * width);
        focusY.set(boundingBox.getMinY().getValue() + focusY.get() * height);
        radius.set(radius.get() * (height > width ? height : width));
    }

    private double getDistance(final double diffX, final double diffY) {
        return diffX == 0.0d ? Math.abs(diffY) : diffY == 0.0d ? Math.abs(diffX) : Math.hypot(diffX, diffY);
    }

    // endregion
}