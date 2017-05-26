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
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.util.Pair;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeStyle extends SVGAttributeType<SVGCssStyle, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final SVGCssStyle DEFAULT_VALUE = null;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeStyle(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws SVGException when the given text can not be resolved to a valid {@link SVGAttributeTypeStyle}
     */
    @Override
    protected Pair<SVGCssStyle, Void> getValueAndUnit(final String text) throws SVGException {
        final SVGCssStyle ownStyle = new SVGCssStyle(getDocumentDataProvider());
        try {
            ownStyle.parseCssText(text);
        } catch (final IllegalStateException e) {
            throw new SVGException(String.format("Css text [%s] could not be parsed", text), e);
        }
        return new Pair<>(ownStyle, null);
    }

    //endregion
}