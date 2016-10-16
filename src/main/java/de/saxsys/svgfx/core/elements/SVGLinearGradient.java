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
        return determineResult(styleSupplier, null);
    }

    //endregion

    // region Implement SVGGradientBase

    @Override
    public LinearGradient createResult(final StyleSupplier styleSupplier, final SVGElementBase<?> elementBase) throws SVGException {
        return determineResult(styleSupplier, elementBase);
    }

    // endregion

    // region Private

    private LinearGradient determineResult(final StyleSupplier styleSupplier, final SVGElementBase<?> element) throws SVGException {

        final List<Stop> stops = getStops();
        if (stops.isEmpty()) {
            throw new SVGException(SVGException.Reason.MISSING_STOPS);
        }

        final AtomicReference<Double> startX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> startY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> endX = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));
        final AtomicReference<Double> endY = new AtomicReference<>(getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE));

        final Enumerations.GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                              Enumerations.GradientUnit.class,
                                                                                              Enumerations.GradientUnit.OBJECT_BOUNDING_BOX);

        if (gradientUnit == Enumerations.GradientUnit.USER_SPACE_ON_USE) {
            if (element == null) {
                throw new SVGException(SVGException.Reason.MISSING_ELEMENT, "Can not create gradient when user space is defined but the referenced element is missing");
            }
            getRelativePosition(startX, startY, endX, endY, element);
        }


        return new LinearGradient(startX.get(), startY.get(), endX.get(), endY.get(), false, CycleMethod.NO_CYCLE, stops);
    }

    private SVGAttributeTypeRectangle.SVGTypeRectangle getRelativePosition(final AtomicReference<Double> startX,
                                                                           final AtomicReference<Double> startY,
                                                                           final AtomicReference<Double> endX,
                                                                           final AtomicReference<Double> endY,
                                                                           final SVGElementBase<?> element) throws SVGException {

        return null;
    }

    // endregion
}
