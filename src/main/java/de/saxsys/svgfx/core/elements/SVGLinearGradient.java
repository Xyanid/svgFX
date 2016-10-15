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
import de.saxsys.svgfx.core.content.SVGAttributeTypeGradientUnits;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

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

        final List<Stop> stops = getStops();
        if (stops.isEmpty()) {
            throw new SVGException(SVGException.Reason.MISSING_STOPS);
        }

        Double startX = getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);
        Double startY = getAttributeHolder().getAttributeValue(CoreAttributeMapper.START_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);
        Double endX = getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_X.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);
        Double endY = getAttributeHolder().getAttributeValue(CoreAttributeMapper.END_Y.getName(), Double.class, SVGAttributeTypeLength.DEFAULT_VALUE);

        Enumerations.GradientUnits gradientUnits = getAttributeHolder().getAttributeValue(CoreAttributeMapper.GRADIENT_UNITS.getName(),
                                                                                          Enumerations.GradientUnits.class,
                                                                                          Enumerations.GradientUnits.OBJECT_BOUNDING_BOX);

        if (gradientUnits == Enumerations.GradientUnits.USER_SPACE_ON_USE) {
            final SVGRoot root = getDocumentDataProvider().getData(SVGRoot.ELEMENT_NAME, SVGRoot.class);
            if (root == null) { throw new SVGException(SVGException.Reason.MISSING_SVG_ROOT); }
        }

        // TODO apply transform and figure out if svg supports the cycle method

        getAttributeHolder().getAttribute(CoreAttributeMapper.GRADIENT_UNITS.getName(), SVGAttributeTypeGradientUnits.class).ifPresent(gradientUnit -> {
            if (gradientUnit.getValue() == Enumerations.GradientUnits.USER_SPACE_ON_USE) {

            }
        });


        return new LinearGradient(startX, startY, endX, endY, false, CycleMethod.NO_CYCLE, stops);
    }

    //endregion
}
