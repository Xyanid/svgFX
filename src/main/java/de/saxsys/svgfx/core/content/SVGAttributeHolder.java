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

package de.saxsys.svgfx.core.content;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.xml.elements.AttributeHolder;

import java.util.Optional;

/**
 * @author Xyanid on 27.03.2016.
 */
public class SVGAttributeHolder extends AttributeHolder<SVGAttributeType> {

    //region Fields

    private SVGDocumentDataProvider dataProvider;

    //endregion

    // region Constructor

    public SVGAttributeHolder(final SVGDocumentDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // endregion

    /**
     * Returns the value of the desired attribute as the desired type using the provided key or the given default value should the attribute not exist.
     *
     * @param <TValue> type of the value of the attribute desired.
     * @param name     name of the property
     * @param clazz    class of the type of the property used for casting.
     *
     * @return the value of the desired attribute as the {@link TValue} or the default value should the attribute not exist.
     */
    public final <TValue> TValue getAttributeValue(final String name, final Class<TValue> clazz, final TValue defaultValue) {
        final Optional<SVGAttributeType> attribute = getAttribute(name);

        if (!attribute.isPresent()) { return defaultValue; }

        return clazz.cast(attribute.get().getValue());
    }

    //region Override AttributeHolder

    @Override
    public SVGAttributeType createAttributeType(final String name) {
        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(dataProvider);
            }
        }

        for (CoreAttributeMapper attribute : CoreAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(dataProvider);
            }
        }

        return new SVGAttributeTypeString(dataProvider);
    }

    //endregion
}
