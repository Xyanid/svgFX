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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.enumerations.GradientUnit;
import de.saxsys.svgfx.core.interfaces.SVGSupplier;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class represents the linear gradient element from svg
 *
 * @author Xyanid on 24.10.2015.
 */
public class SVGLinearGradient extends SVGGradientBase<LinearGradient> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "linearGradient";
    /**
     * Determines the default value to use for the end x.
     */
    private static final Double DEFAULT_END_X = 1.0d;

    // endregion

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGLinearGradient(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    // endregion

    // region Override SVGElementBase

    @Override
    protected final LinearGradient createResult(final SVGCssStyle ownStyle) throws SVGException {
        return determineResult(null, null);
    }

    // endregion

    // region Implement SVGGradientBase

    @Override
    public final LinearGradient createResult(final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox, final Transform elementTransform) throws SVGException {
        return determineResult(boundingBox, elementTransform);
    }

    // endregion

    // region Private

    private LinearGradient determineResult(final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> elementBoundingBox, final Transform elementTransform)
            throws SVGException {

        final List<Stop> stops = getStops();
        if (stops.isEmpty()) {
            throw new SVGException(SVGException.Reason.MISSING_STOPS);
        }

        final AtomicReference<Double> startX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> startY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> endX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_X.getName(), Double.class, DEFAULT_END_X));
        final AtomicReference<Double> endY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));

        convertToObjectBoundingBox(startX, startY, endX, endY, elementBoundingBox, elementTransform);

        return new LinearGradient(startX.get(),
                                  startY.get(),
                                  endX.get(),
                                  endY.get(),
                                  true,
                                  getAttributeHolder().getAttributeValue(CoreAttributeMapper.SPREAD_METHOD.getName(), CycleMethod.class, SVGAttributeTypeCycleMethod.DEFAULT_VALUE),
                                  stops);
    }

    private void convertToObjectBoundingBox(final AtomicReference<Double> startX,
                                            final AtomicReference<Double> startY,
                                            final AtomicReference<Double> endX,
                                            final AtomicReference<Double> endY,
                                            final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> elementBoundingBox,
                                            final Transform elementTransform) throws SVGException {

        final Optional<Transform> usedTransform = getTransform(elementTransform);

        final GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                 GradientUnit.class,
                                                                                 GradientUnit.OBJECT_BOUNDING_BOX);

        // when being in user space we first need to transform then make the values relative
        if (gradientUnit == GradientUnit.USER_SPACE_ON_USE) {
            if (elementBoundingBox == null) {
                throw new SVGException(SVGException.Reason.MISSING_ELEMENT, "Can not create linear gradient when user space is defined but no bounding box is provided.");
            }

            usedTransform.ifPresent(transform -> transformPosition(startX, startY, endX, endY, transform));
            convertToObjectBoundingBox(startX, startY, endX, endY, elementBoundingBox.get());
        } else if (usedTransform.isPresent()) {
            final SVGAttributeTypeRectangle.SVGTypeRectangle rectangle = elementBoundingBox.get();
            convertFromObjectBoundingBox(startX, startY, endX, endY, rectangle);
            transformPosition(startX, startY, endX, endY, usedTransform.get());
            convertToObjectBoundingBox(startX, startY, endX, endY, rectangle);
        }
    }

    private void transformPosition(final AtomicReference<Double> startX,
                                   final AtomicReference<Double> startY,
                                   final AtomicReference<Double> endX,
                                   final AtomicReference<Double> endY,
                                   final Transform transform) {
        transformPosition(startX, startY, transform);
        transformPosition(endX, endY, transform);
    }

    private void convertToObjectBoundingBox(final AtomicReference<Double> startX,
                                            final AtomicReference<Double> startY,
                                            final AtomicReference<Double> endX,
                                            final AtomicReference<Double> endY,
                                            final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox) throws SVGException {

        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

        if (width == 0.0d || height == 0.0d) {
            return;
        }

        startX.set(Math.abs(boundingBox.getMinX().getValue() - startX.get()) / width);
        startY.set(Math.abs(boundingBox.getMinY().getValue() - startY.get()) / height);
        endX.set(Math.abs(boundingBox.getMinX().getValue() - endX.get()) / width);
        endY.set(Math.abs(boundingBox.getMinY().getValue() - endY.get()) / height);
    }

    private void convertFromObjectBoundingBox(final AtomicReference<Double> startX,
                                              final AtomicReference<Double> startY,
                                              final AtomicReference<Double> endX,
                                              final AtomicReference<Double> endY,
                                              final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox) throws SVGException {

        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

        if (width == 0.0d || height == 0.0d) {
            return;
        }

        startX.set(boundingBox.getMinX().getValue() + startX.get() * width);
        startY.set(boundingBox.getMinY().getValue() + startY.get() * height);
        endX.set(boundingBox.getMinX().getValue() + endX.get() * width);
        endY.set(boundingBox.getMinY().getValue() + endY.get() * height);
    }

    // endregion
}
