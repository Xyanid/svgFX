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
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

import java.util.List;
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
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    SVGRadialGradient(final String name, final Attributes attributes, final SVGElementBase<SVGDocumentDataProvider> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }


    //endregion

    //region Override SVGGradientBase

    @Override
    protected final RadialGradient createResult(final StyleSupplier styleSupplier) throws SVGException {
        return determineResult(null);
    }


    //endregion

    // region SVGGradientBase

    @Override
    public RadialGradient createResult(final SVGShapeBase<?> shape) throws SVGException {
        return determineResult(shape);
    }

    // endregion


    // region Private

    private RadialGradient determineResult(final SVGShapeBase<?> shape) throws SVGException {
        final List<Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException(SVGException.Reason.MISSING_STOPS, "Given radial gradient does not have colors");
        }

        final AtomicReference<Double> centerX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_X.getName(),
                                                                                                             Double.class,
                                                                                                             DEFAULT_CENTER));
        final AtomicReference<Double> centerY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_Y.getName(),
                                                                                                             Double.class,
                                                                                                             DEFAULT_CENTER));
        final AtomicReference<Double> focusX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_X.getName(),
                                                                                                            Double.class,
                                                                                                            centerX.get()));
        final AtomicReference<Double> focusY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_Y.getName(),
                                                                                                            Double.class,
                                                                                                            centerY.get()));
        final AtomicReference<Double> radius = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.RADIUS.getName(),
                                                                                                            Double.class,
                                                                                                            DEFAULT_RADIUS));

        // TODO apply transform

        final Enumerations.GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                              Enumerations.GradientUnit.class,
                                                                                              Enumerations.GradientUnit.OBJECT_BOUNDING_BOX);

        if (gradientUnit == Enumerations.GradientUnit.USER_SPACE_ON_USE) {
            if (shape == null) {
                throw new SVGException(SVGException.Reason.MISSING_ELEMENT, "Can not create linear gradient when user space is defined but the requesting shape is missing.");
            }
            adjustPosition(centerX, centerY, focusX, focusY, radius, shape);
        }

        double diffX = focusX.get() - centerX.get();
        double diffY = focusY.get() - centerY.get();

        double distance = diffX != 0 && diffY != 0 ? Math.hypot(diffX, diffY) : 0;

        double angle = diffX != 0 && diffY != 0 ? Math.atan2(diffY, diffX) : 0;

        return new RadialGradient(angle,
                                  distance,
                                  centerX.get(),
                                  centerY.get(),
                                  radius.get(),
                                  true,
                                  getAttributeHolder().getAttributeValue(CoreAttributeMapper.SPREAD_METHOD.getName(), CycleMethod.class, SVGAttributeTypeCycleMethod.DEFAULT_VALUE),
                                  stops);
    }

    private void adjustPosition(final AtomicReference<Double> centerX,
                                final AtomicReference<Double> centerY,
                                final AtomicReference<Double> focusX,
                                final AtomicReference<Double> focusY,
                                final AtomicReference<Double> radius,
                                final SVGShapeBase<?> shape) throws SVGException {

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = shape.createBoundingBox();
        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

        centerX.set(Math.abs(boundingBox.getMinX().getValue() - centerX.get()) / width);
        centerY.set(Math.abs(boundingBox.getMinY().getValue() - centerY.get()) / height);
        focusX.set(Math.abs(boundingBox.getMinX().getValue() - focusX.get()) / width);
        focusY.set(Math.abs(boundingBox.getMinY().getValue() - focusY.get()) / height);
        radius.set(radius.get() / height);
    }

    // endregion
}