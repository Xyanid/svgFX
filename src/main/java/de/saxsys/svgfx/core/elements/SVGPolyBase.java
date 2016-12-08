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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePoint;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePoints;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for polygons and polyline.
 *
 * @param <TShape> the type of shape this element creates
 *
 * @author Xyanid on 07.11.2015.
 */
public abstract class SVGPolyBase<TShape extends Shape> extends SVGShapeBase<TShape> {

    // region Static

    /**
     * Determines the delimiter that separated a pair of points.
     */
    private static char POINTS_DELIMITER = ' ';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    private static char POSITION_DELIMITER = ',';

    /**
     * Determines the delimiter that separated a the positions of a point.
     */
    private static String POSITION_DELIMITER_STRING = String.valueOf(POSITION_DELIMITER);

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
    protected SVGPolyBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Public

    /**
     * Returns the list of points contained by the attributes.
     *
     * @return the list of points contained by the attributes
     *
     * @throws SVGException if any of the points in the corresponding attribute does not provide x and y position.
     */
    public final List<Double> getPoints() throws SVGException {
        final List<Double> actualPoints = new ArrayList<>();

        final Optional<SVGAttributeTypePoints> points = getAttributeHolder().getAttribute(CoreAttributeMapper.POINTS.getName(), SVGAttributeTypePoints.class);
        if (points.isPresent()) {
            for (final SVGAttributeTypePoint point : points.get().getValue()) {
                actualPoints.add(point.getValue().getX().getValue());
                actualPoints.add(point.getValue().getY().getValue());
            }
        }

        return actualPoints;
    }

    //endregion

    // region Implement SVGShapeBase

    @Override
    public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox() throws SVGException {

        final SVGAttributeTypeRectangle.SVGTypeRectangle result = new SVGAttributeTypeRectangle.SVGTypeRectangle(getDocumentDataProvider());

        final AtomicBoolean isFirstRun = new AtomicBoolean(true);

        final Optional<SVGAttributeTypePoints> points = getAttributeHolder().getAttribute(CoreAttributeMapper.POINTS.getName(), SVGAttributeTypePoints.class);
        if (points.isPresent()) {
            for (final SVGAttributeTypePoint point : points.get().getValue()) {
                if (isFirstRun.get()
                    || result.getMinX().getValue() > point.getValue().getX().getValue()) {
                    result.getMinX().setText(String.format("%f%s", point.getValue().getX().getValue(), point.getValue().getX().getUnit().getName()));
                }

                if (isFirstRun.get()
                    || result.getMinY().getValue() > point.getValue().getY().getValue()) {
                    result.getMinY().setText(String.format("%f%s", point.getValue().getY().getValue(), point.getValue().getY().getUnit().getName()));
                }

                if (isFirstRun.get()
                    || result.getMaxX().getValue() < point.getValue().getX().getValue()) {
                    result.getMaxX().setText(String.format("%f%s", point.getValue().getX().getValue(), point.getValue().getX().getUnit().getName()));
                }

                if (isFirstRun.get()
                    || result.getMaxY().getValue() < point.getValue().getY().getValue()) {
                    result.getMaxY().setText(String.format("%f%s", point.getValue().getY().getValue(), point.getValue().getY().getUnit().getName()));
                }

                if (isFirstRun.get()) {
                    isFirstRun.set(false);
                }
            }
        }

        return result;
    }

    // endregion
}
