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

package de.saxsys.svgfx.core.css;


import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeHolder;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeType;
import de.saxsys.svgfx.css.core.CssStyle;

/**
 * This Class does not directly represent a SVG element but rather a Css element
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssStyle extends CssStyle<SVGAttributeType, SVGAttributeHolder> {

    // region Fields

    private final SVGDocumentDataProvider provider;

    // endregion

    //region Constructor

    /**
     * Creates a new instance.
     *
     * @param provider the data provider to use
     */
    public SVGCssStyle(final SVGDocumentDataProvider provider) {
        super(new SVGAttributeHolder(provider));

        this.provider = provider;
    }

    /**
     * Creates a new instance.
     *
     * @param name     the name to of this style.
     * @param provider the data provider to use.
     */
    public SVGCssStyle(final String name, final SVGDocumentDataProvider provider) {
        super(name, new SVGAttributeHolder(provider));

        this.provider = provider;
    }

    //endregion
}
