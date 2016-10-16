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

package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.core.attributes.type.SVGAttributeType;
import de.saxsys.svgfx.xml.core.AttributeWrapper;
import de.saxsys.svgfx.xml.core.IDocumentDataProvider;

import java.util.function.Function;

/**
 * This class represents a base attributes, it is intended to be used
 *
 * @author Xyanid on 09.03.2016.
 */
public abstract class BaseAttributeMapper<TDataProvider extends IDocumentDataProvider> {

    // region Fields

    /**
     * The name of the attribute within the svg element.
     */
    private final String name;

    /**
     * This function is used to create a new instance of the underlying {@link AttributeWrapper} of the attribute using the given {@link TDataProvider}.
     */
    private final Function<TDataProvider, ? extends SVGAttributeType> contentTypeCreator;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param name               the name of the attribute within the svg element
     * @param contentTypeCreator the {@link Function} to use when a {@link AttributeWrapper} is needed.
     */
    BaseAttributeMapper(final String name, final Function<TDataProvider, ? extends SVGAttributeType> contentTypeCreator) {
        this.name = name;
        this.contentTypeCreator = contentTypeCreator;
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

    /**
     * Returns the {@link #contentTypeCreator}.
     *
     * @return the {@link #contentTypeCreator}.
     */
    public final Function<TDataProvider, ? extends SVGAttributeType> getContentTypeCreator() {
        return contentTypeCreator;
    }

    // endregion
}