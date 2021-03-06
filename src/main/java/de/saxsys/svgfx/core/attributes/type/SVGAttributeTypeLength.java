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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import javafx.util.Pair;

import java.util.EnumSet;

/**
 * This class represents a svg length content type
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeLength extends SVGAttributeType<Double, SVGAttributeTypeLength.Unit> {

    // region Static

    /**
     * Represents the unit which might be attached to a length value
     */
    public enum Unit {
        /**
         * Meaning the length type is not specified.
         */
        NONE(""),
        /**
         * Meaning the value is defined in relative size to its current size.
         */
        RELATIVE_SELF("em"),
        /**
         * Meaning the value is defined in relative size to its current height.
         */
        RELATIVE_HEIGHT("ex"),
        /**
         * Meaning the value is defined in pixel.
         */
        PIXEL("px"),
        /**
         * Meaning the value is defined in inches.
         */
        INCH("in"),
        /**
         * Meaning the value is defined in centimeters.
         */
        CENTIMETER("cm"),
        /**
         * Meaning the value is defined in millimeters.
         */
        MILLIMETER("mm"),
        /**
         * Meaning the value is defined in points.
         */
        POINT("pt"),
        /**
         * Meaning the value is defined in picas.
         */
        PICAS("pc"),
        /**
         * Meaning the value is defined in percentage.
         */
        PERCENT("%");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        Unit(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link #name}.
         *
         * @return the {@link #name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    // endregion

    // region Constants

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final double DEFAULT_VALUE = 0.0d;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeLength(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override AttributeWrapper

    /**
     * @throws NumberFormatException when any value inside the array is not a valid {@link SVGAttributeTypeLength}
     */
    @Override
    protected Pair<Double, Unit> getValueAndUnit(final String cssText) throws SVGException {

        Unit usedUnit = Unit.NONE;

        for (Unit unit : EnumSet.complementOf(EnumSet.of(Unit.NONE))) {
            if (cssText.endsWith(unit.getName())) {
                usedUnit = unit;
                break;
            }
        }

        try {
            return new Pair<>(Double.parseDouble(cssText.substring(0, cssText.length() - usedUnit.getName().length()).replaceAll(",", ".")), usedUnit);
        } catch (final NumberFormatException e) {
            throw new SVGException(String.format("Css text [%s] does not represent valid number", cssText), e);
        }
    }

    //endregion
}