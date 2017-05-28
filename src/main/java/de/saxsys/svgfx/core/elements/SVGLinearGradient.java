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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.definitions.enumerations.GradientUnit;
import de.saxsys.svgfx.core.utils.Wrapper;
import javafx.geometry.Point2D;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.Arrays;
import java.util.List;

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

    // region Implement SVGGradientBase

    @Override
    public final LinearGradient createResult(final SVGAttributeTypeRectangle.SVGTypeRectangle elementBoundingBox, final Transform elementTransform) throws SVGException {
        return determineResult(elementBoundingBox, elementTransform);
    }

    // endregion

    // region Private

    private LinearGradient determineResult(final SVGAttributeTypeRectangle.SVGTypeRectangle elementBoundingBox, final Transform elementTransform)
            throws SVGException {

        final List<Stop> stops = getStops();
        if (stops.isEmpty()) {
            throw new SVGException("Given linear gradient does not have stop colors");
        }

        final double startX = getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);
        final double startY = getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);
        final Wrapper<Point2D> start = new Wrapper<>(new Point2D(startX, startY));
        final double endX = getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_X.getName(), Double.class, DEFAULT_END_X);
        final double endY = getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);
        final Wrapper<Point2D> end = new Wrapper<>(new Point2D(endX, endY));

        convertToRelativeCoordinates(start, end, elementBoundingBox, elementTransform);

        return new LinearGradient(start.getOrFail().getX(),
                                  start.getOrFail().getY(),
                                  end.getOrFail().getX(),
                                  end.getOrFail().getY(),
                                  true,
                                  getAttributeHolder().getAttributeValue(CoreAttributeMapper.SPREAD_METHOD.getName(), CycleMethod.class, SVGAttributeTypeCycleMethod.DEFAULT_VALUE),
                                  stops);
    }

    private void convertToRelativeCoordinates(final Wrapper<Point2D> start,
                                              final Wrapper<Point2D> end,
                                              final SVGAttributeTypeRectangle.SVGTypeRectangle elementBoundingBox,
                                              final Transform elementTransform) throws SVGException {

        final Rectangle boundingBox = transformBoundingBox(elementBoundingBox, elementTransform);

        final GradientUnit gradientUnit = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                 GradientUnit.class,
                                                                                 GradientUnit.OBJECT_BOUNDING_BOX);

        final List<Wrapper<Point2D>> points = Arrays.asList(start, end);

        // when being in user space we first need to transform then make the values relative
        if (gradientUnit == GradientUnit.USER_SPACE_ON_USE) {
            getCombinedTransform(elementTransform).ifPresent(transform -> transformPosition(transform, start, end));
            convertToRelativeCoordinates(boundingBox, points);
        } else {
            convertToAbsoluteCoordinates(boundingBox, points);
            getGradientTransform().ifPresent(transform -> transformPosition(transform, start, end));
            convertToRelativeCoordinates(boundingBox, points);
        }
    }

    private void transformPosition(final Transform transform,
                                   final Wrapper<Point2D> start,
                                   final Wrapper<Point2D> end) {
        start.set(transform.transform(start.getOrFail()));
        end.set(transform.transform(end.getOrFail()));
    }

    // endregion
}