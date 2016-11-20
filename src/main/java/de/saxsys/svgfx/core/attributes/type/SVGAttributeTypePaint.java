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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.elements.SVGGradientBase;
import de.saxsys.svgfx.core.elements.SVGShapeBase;
import de.saxsys.svgfx.core.utils.SVGUtil;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

/**
 * Represents a {@link Paint} used to color fill and strokes, the default value is {@link Color#TRANSPARENT}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypePaint extends SVGAttributeType<Paint, Void> {

    // region Static

    /**
     * Indicator representing that the color is to be retrieved from the color attribute.
     */
    private static final String CURRENT_COLOR = "currentColor";

    /**
     * Determines the default color to use for a {@link SVGAttributeTypePaint}.
     */
    public static final Paint DEFAULT_VALUE = Color.BLACK;

    // endregion

    // region Fields

    /**
     * Determines that the color is to be retrieved from the color attribute.
     */
    private boolean isCurrentColor;

    // endregion

    //region Constructor

    /**
     * Creates new instance with a default value of {@link Color#BLACK}.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypePaint(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    // region Getter

    /**
     * @return The {@link #isCurrentColor}.
     */
    public boolean getIsCurrentColor() throws SVGException {
        getValueAndUnit();
        return isCurrentColor;
    }

    // endregion

    //region Override AttributeTypeAdjustable

    @Override
    public Pair<Paint, Void> getValueAndUnit(final String text) throws SVGException {
        isCurrentColor = CURRENT_COLOR.equals(text);

        if (isCurrentColor) {
            return new Pair<>(null, null);
        }

        try {
            return new Pair<>(Color.web(text), null);
        } catch (final Exception e) {
            throw new SVGException(SVGException.Reason.INVALID_COLOR_FORMAT, String.format("Color %s is invalid", text), e);
        }
    }

    //endregion

    // region Public

    /**
     * Resolves the given data into a paint. The data must either be valid hex web color (e.g. #00FF00FF) or a reference which can be resolved into a {@link SVGGradientBase}.
     *
     * @param shape the element for which this paint is
     *
     * @return {@link Paint} which represents the color
     *
     * @throws SVGException <ul>
     *                      <li>if the paint is actually a reference to a {@link SVGGradientBase} that was not found.</li>
     *                      </ul>
     */
    @SuppressWarnings ("unchecked")
    public Paint getValue(final SVGShapeBase<?> shape) throws SVGException {

        // its not possible to use the IRI_FRAGMENT_IDENTIFIER on colors so we will only resolve references if we are sure its not a color itself
        if (getText().startsWith(de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER)) {
            return SVGUtil.resolveIRI(getText(), getDocumentDataProvider(), SVGGradientBase.class).createResult(shape);
        }

        return getValue();
    }

    // endregion

}