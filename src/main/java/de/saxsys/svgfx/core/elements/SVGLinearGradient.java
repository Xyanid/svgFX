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
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

import java.util.List;
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

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    SVGLinearGradient(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final LinearGradient createResult(final StyleSupplier styleSupplier) throws SVGException {
        return determineResult(null);
    }

    //endregion

    // region Implement SVGGradientBase

    @Override
    public LinearGradient createResult(final SVGShapeBase<?> shape) throws SVGException {
        return determineResult(shape);
    }

    // endregion

    // region Private

    private LinearGradient determineResult(final SVGShapeBase<?> shape) throws SVGException {

        final List<Stop> stops = getStops();
        if (stops.isEmpty()) {
            throw new SVGException(SVGException.Reason.MISSING_STOPS);
        }

        final AtomicReference<Double> startX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> startY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> endX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_X.getName(), Double.class, DEFAULT_END_X));
        final AtomicReference<Double> endY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));

        final Enumerations.GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                              Enumerations.GradientUnit.class,
                                                                                              Enumerations.GradientUnit.OBJECT_BOUNDING_BOX);

        if (gradientUnit == Enumerations.GradientUnit.USER_SPACE_ON_USE) {
            if (shape == null) {
                throw new SVGException(SVGException.Reason.MISSING_ELEMENT, "Can not create linear gradient when user space is defined but the requesting shape is missing.");
            }
            adjustPosition(startX, startY, endX, endY, shape);
        }


        return new LinearGradient(startX.get(),
                                  startY.get(),
                                  endX.get(),
                                  endY.get(),
                                  true,
                                  getAttributeHolder().getAttributeValue(CoreAttributeMapper.SPREAD_METHOD.getName(), CycleMethod.class, SVGAttributeTypeCycleMethod.DEFAULT_VALUE),
                                  stops);
    }

    private void adjustPosition(final AtomicReference<Double> startX,
                                final AtomicReference<Double> startY,
                                final AtomicReference<Double> endX,
                                final AtomicReference<Double> endY,
                                final SVGShapeBase<?> shape) throws SVGException {

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = shape.createBoundingBox();
        final Double width = boundingBox.getMaxX().getValue() - boundingBox.getMinX().getValue();
        final Double height = boundingBox.getMaxY().getValue() - boundingBox.getMinY().getValue();

        startX.set(Math.abs(boundingBox.getMinX().getValue() - startX.get()) / width);
        startY.set(Math.abs(boundingBox.getMinY().getValue() - startY.get()) / height);
        endX.set(Math.abs(boundingBox.getMinX().getValue() - endX.get()) / width);
        endY.set(Math.abs(boundingBox.getMinY().getValue() - endY.get()) / height);
    }

    // endregion
}
