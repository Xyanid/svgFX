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
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.utils.SVGUtil;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     *
     * @throws IllegalArgumentException if either value or dataProvider are null
     */
    protected SVGGradientBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider)
            throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Public

    /**
     * Gets the stops related to this gradient.
     *
     * @return the stops which this gradient needs
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

    //endregion

    // region Override SVGElementBase

    @Override
    public boolean rememberElement() {
        return true;
    }

    @Override
    public void startProcessing() throws SAXException {}

    @Override
    public void processCharacterData(char[] ch, int start, int length) throws SAXException {}

    @Override
    public void endProcessing() throws SAXException {
        try {
            storeElementInDocumentDataProvider();
        } catch (final SVGException e) {
            throw new SAXException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return false always.
     */
    @Override
    public boolean canConsumeResult() {
        return false;
    }

    @Override
    protected final void initializeResult(final TPaint paint, final StyleSupplier styleSupplier) throws SVGException {}

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
     * @param element the {@link SVGElementBase} requesting this gradient.
     *
     * @return a new {@link TPaint}.
     */
    public abstract TPaint createResult(final StyleSupplier styleSupplier, final SVGElementBase<?> element) throws SVGException;

    // endregion
}