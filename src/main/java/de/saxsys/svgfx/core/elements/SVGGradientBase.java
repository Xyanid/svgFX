/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

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
    public SVGGradientBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Public

    /**
     * Gets the stops related to this gradient.
     *
     * @return the stops which this gradient needs
     */
    public final List<Stop> getStops() {
        List<Stop> stops = new ArrayList<>();

        for (ElementBase element : getChildren()) {
            if (element instanceof SVGStop) {
                stops.add(((SVGStop) element).getResult());
            }
        }

        // own stops are preferred, now we check for stops that are on referenced elements
        if (stops.isEmpty() && StringUtils.isNotNullOrEmpty(getAttribute(Enumerations.XLinkAttribute.XLINK_HREF.getName()))) {

            SVGElementBase referenceElement = SVGUtils.resolveIRI(getAttribute(Enumerations.XLinkAttribute.XLINK_HREF.getName()), getDataProvider(), SVGElementBase.class);

            for (Object child : referenceElement.getChildren()) {
                if (child instanceof SVGStop) {
                    stops.add(((SVGStop) child).getResult());
                }
            }
        }

        return stops;
    }

    //endregion

    // region Override SVGElementBase

    @Override
    protected final void initializeResult(final TPaint paint, final SVGCssStyle style) throws SVGException {

        // TODO figure out how to apply transformation to a paint if that is possible
    }

    // endregion
}
