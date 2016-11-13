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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
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
                                                                                                             SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> centerY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.CENTER_Y.getName(),
                                                                                                             Double.class,
                                                                                                             SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> focusX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_X.getName(),
                                                                                                            Double.class,
                                                                                                            SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> focusY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.FOCUS_Y.getName(),
                                                                                                            Double.class,
                                                                                                            SVGAttributeTypeLength.DEFAULT_VALUE));

        final Enumerations.GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                              Enumerations.GradientUnit.class,
                                                                                              Enumerations.GradientUnit.OBJECT_BOUNDING_BOX);

        if (gradientUnit == Enumerations.GradientUnit.USER_SPACE_ON_USE) {
            if (shape == null) {
                throw new SVGException(SVGException.Reason.MISSING_ELEMENT, "Can not create linear gradient when user space is defined but the requesting shape is missing.");
            }
            adjustPosition(centerX, centerY, focusX, focusY, shape);
        }

        // TODO convert the coordinates into the correct space, first convert then apply transform

        double diffX = focusX.get() - centerX.get();
        double diffY = focusY.get() - centerY.get();

        double distance = diffX != 0 && diffY != 0 ? Math.hypot(diffX, diffY) : 0;
        double angle = diffX != 0 && diffY != 0 ? Math.atan2(diffY, diffX) : 0;

        // TODO figure out if the focus angle is correct or not

        return new RadialGradient(angle,
                                  distance,
                                  centerX.get(),
                                  centerY.get(),
                                  getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.RADIUS.getName(), SVGAttributeTypeLength.class).getValue(),
                                  false,
                                  CycleMethod.NO_CYCLE,
                                  stops);
    }

    private void adjustPosition(final AtomicReference<Double> centerX,
                                final AtomicReference<Double> centerY,
                                final AtomicReference<Double> focusX,
                                final AtomicReference<Double> focusY,
                                final SVGShapeBase<?> shape) throws SVGException {

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = shape.createBoundingBox();
        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

    }

    // endregion
}