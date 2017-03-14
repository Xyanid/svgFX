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
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeTransform;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.interfaces.SVGSupplier;
import de.saxsys.svgfx.core.utils.SVGUtil;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Contains basic functionality to handle gradients of svg.
 *
 * @param <TPaint> the type of the paint need
 *
 * @author Xyanid on 06.11.2015.
 */
public abstract class SVGGradientBase<TPaint extends Paint> extends SVGElementBase<TPaint> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes, parent and dataProvider.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     *
     * @throws IllegalArgumentException if either value or dataProvider are null
     */
    protected SVGGradientBase(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider)
            throws IllegalArgumentException {
        super(name, attributes, dataProvider);
    }

    //endregion

    // region Override SVGElementBase

    @Override
    public final boolean keepElement() {
        return false;
    }

    @Override
    public final void processCharacterData(char[] ch, int start, int length) throws SAXException {}

    @Override
    protected final void initializeResult(final TPaint paint, final SVGCssStyle styleSupplier) throws SVGException {}

    // endregion

    // region Public

    /**
     * Gets the stops related to this gradient.
     *
     * @return the stops which this gradient needs
     *
     * @throws SVGException if an error occurs during the retrieval of the stops.
     */
    @SuppressWarnings ("unchecked")
    public final List<Stop> getStops() throws SVGException {
        final List<Stop> stops = new ArrayList<>(0);

        fillStopsOrFail(stops, getUnmodifiableChildren());

        // own stops are preferred, now we check for stops that are on referenced elements
        if (stops.isEmpty()) {

            final Optional<SVGAttributeTypeString> link = getAttributeHolder().getAttribute(XLinkAttributeMapper.XLINK_HREF.getName(), SVGAttributeTypeString.class);

            if (link.isPresent()) {
                fillStopsOrFail(stops, SVGUtil.resolveIRI(link.get().getValue(), getDocumentDataProvider(), SVGElementBase.class).getUnmodifiableChildren());
            }
        }

        return stops;
    }

    // endregion

    // region Protected

    /**
     * Uses the given x and y and applies the given {@link Transform}. After the call the x and y values will have been adjusted.
     *
     * @param x         the x to use.
     * @param y         the y to use.
     * @param transform the {@link Transform} to use.
     */
    protected void transformPosition(final AtomicReference<Double> x, final AtomicReference<Double> y, final Transform transform) {
        final Point2D start = transform.transform(x.get(), y.get());
        x.set(start.getX());
        y.set(start.getY());
    }

    /**
     * Will be used to get the correct transformation for the gradient if any.
     *
     * @param elementTransform the {@link Transform} supplied by the element that uses this gradient.
     *
     * @return a new {@link Optional} with the correct {@link Transform} or {@link Optional#empty()} if there is no transform.
     *
     * @throws SVGException if there is a problem when the transformation is requested.
     */
    protected Optional<Transform> getTransform(final Transform elementTransform) throws SVGException {
        final Optional<SVGAttributeTypeTransform> ownTransform = getAttributeHolder().getAttribute(CoreAttributeMapper.GRADIENT_TRANSFORM.getName(), SVGAttributeTypeTransform.class);
        final Optional<Transform> usedTransform;

        if (elementTransform != null) {
            if (ownTransform.isPresent()) {
                usedTransform = Optional.of(elementTransform.createConcatenation(ownTransform.get().getValue()));
            } else {
                usedTransform = Optional.of(elementTransform);
            }
        } else if (ownTransform.isPresent()) {
            usedTransform = Optional.of(ownTransform.get().getValue());
        } else {
            usedTransform = Optional.empty();
        }

        return usedTransform;
    }

    // endregion

    // region Private

    private void fillStopsOrFail(final List<Stop> stops, final List<SVGElementBase<?>> children) throws SVGException {
        for (final SVGElementBase child : children) {
            if (child instanceof SVGStop) {
                try {
                    stops.add(((SVGStop) child).getResult());
                } catch (final SAXException e) {
                    throw new SVGException(SVGException.Reason.FAILED_TO_GET_RESULT, String.format("Could not create result for stop: %s", child));
                }
            }
        }
    }

    // endregion

    // region Abstract

    /**
     * This method can be used to create a result, that depends on the provided {@link SVGElementBase}.
     *
     * @param boundingBox      the supplier used to get the bounding box of the shape.
     * @param elementTransform the {@link Transform} to apply.
     *
     * @return a new {@link TPaint}.
     *
     * @throws SVGException if an error occurs during the creation of the result.
     */
    public abstract TPaint createResult(final SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox, final Transform elementTransform) throws SVGException;

    // endregion
}