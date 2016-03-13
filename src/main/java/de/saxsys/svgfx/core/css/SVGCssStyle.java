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


import de.saxsys.svgfx.content.ContentTypeBase;
import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGContentTypeBase;
import de.saxsys.svgfx.core.content.SVGContentTypeString;
import de.saxsys.svgfx.css.core.CssStyle;

/**
 * This Class does not directly represent a SVG element but rather a Css element
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssStyle extends CssStyle<SVGContentTypeBase> {

    // region Fields

    private final SVGDataProvider provider;

    // endregion

    //region Constructor

    /**
     * Creates a new instance.
     *
     * @param provider the data provider to use
     */
    public SVGCssStyle(final SVGDataProvider provider) {
        super();

        this.provider = provider;
    }

    /**
     * Creates a new instance.
     *
     * @param name     the name to of this style.
     * @param provider the data provider to use.
     */
    public SVGCssStyle(final String name, final SVGDataProvider provider) {
        super(name);

        this.provider = provider;
    }

    //endregion

    // region Override CssStyle

    /**
     * This implementation will use the name and validate it against{@link PresentationAttributeMapper}s and then create an instance of a
     * {@link ContentTypeBase}. If the given name does not correspond with any {@link PresentationAttributeMapper}, no {@link ContentTypeBase} will be
     * created and null will be returned.
     *
     * @param name then name of the property
     *
     * @return a {@link ContentTypeBase} or null if the name is not supported.
     */
    @Override
    public SVGContentTypeBase createContentType(final String name) {
        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(provider);
            }
        }

        return new SVGContentTypeString(provider);
    }

    //endregion
}
