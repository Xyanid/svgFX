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

package de.saxsys.svgfx.xml.attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * This class simply hold a {@link java.util.Map} of {@link String} and {@link AttributeType}. Its main purpose is to handle the splitting and storage of
 * data which is represented by a key and has a certain content, where as the content is described as the {@link TContentType}.
 *
 * @param <TContentType> the type of the content that is stored.
 *
 * @author Xyanid on 13.03.2016.
 */
public abstract class AttributeHolder<TContentType extends AttributeType> {

    //region Fields

    /**
     * Contains all the attributes provided by this style.
     */
    private final Map<String, TContentType> attributes = new HashMap<>();

    //endregion

    // region Abstract

    /**
     * This method is used each time a new {@link AttributeType} is needed.
     *
     * @param name the name of the content type to be created.
     *
     * @return a new {@link TContentType} for the given name.
     */
    public abstract TContentType createAttributeType(final String name);

    // endregion

    //region Public

    /**
     * Returns the {@link #attributes}.
     *
     * @return the {@link #attributes}
     */
    public final Map<String, TContentType> getAttributes() {
        return attributes;
    }

    /**
     * Returns the {@link TContentType} in {@link #attributes} using the provided key or null if no such content type exist.
     *
     * @param name name of the content.
     *
     * @return the {@link TContentType} in the given map or null.
     */
    public final TContentType getAttribute(final String name) {
        return attributes.get(name);
    }

    /**
     * Determines if the given {@link TContentType} is contained in the {@link #attributes}.
     *
     * @param name name of the {@link TContentType} to look for.
     *
     * @return true if a {@link TContentType} with the name exists otherwise false.
     */
    public final boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Returns the {@link TContentType} in the {@link #attributes} as the desired type using the provided key or null if no such content type exist.
     *
     * @param <TContent> type of the content desired.
     * @param name       name of the property
     * @param clazz      class of the type of the property used for casting.
     *
     * @return the {@link TContentType} or null.
     */
    public final <TContent extends TContentType> TContent getAttribute(final String name, final Class<TContent> clazz) {
        return clazz.cast(attributes.get(name));
    }

    //endregion
}