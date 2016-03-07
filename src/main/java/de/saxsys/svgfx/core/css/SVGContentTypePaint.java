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

package de.saxsys.svgfx.core.css;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.utils.SVGUtils;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

/**
 * Represents a {@link Paint} used to color fill and strokes, the default value is {@link Color#TRANSPARENT}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGContentTypePaint extends SVGContentTypeBase<Paint, Void> {

    // region Static

    /**
     * Indicator representing that the color is to be retrieved from the color attribute.
     */
    private static final String CURRENT_COLOR = "currentColor";

    /**
     * Determines the default color to use for a {@link SVGContentTypePaint}.
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
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypePaint(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    // region Getter

    /**
     * @return The {@link #isCurrentColor}.
     */
    public boolean getIsCurrentColor() {
        return isCurrentColor;
    }

    // endregion

    //region Override ContentTypeBase

    @Override
    protected Pair<Paint, Void> getValueAndUnit(final String cssText) {
        isCurrentColor = CURRENT_COLOR.equals(cssText);

        if (isCurrentColor) {
            return null;
        }

        return new Pair<>(SVGUtils.parseColor(cssText, getDataProvider()), null);
    }

    //endregion
}
