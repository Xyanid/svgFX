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
import java.util.Optional;

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
     * @return a new {@link Optional} containing the attribute or {@link Optional#EMPTY} if the desired attribute does not exist.
     */
    public final Optional<TContentType> getAttribute(final String name) {
        return Optional.ofNullable(attributes.get(name));
    }

    /**
     * Returns the {@link TContentType} in the {@link #attributes} as the desired type using the provided key or null if no such content type exist.
     *
     * @param <TContent> type of the content desired.
     * @param name       name of the property
     * @param clazz      class of the type of the property used for casting.
     *
     * @return a new {@link Optional} containing the attribute or {@link Optional#EMPTY} if the desired attribute does not exist.
     */
    public final <TContent extends TContentType> Optional<TContent> getAttribute(final String name, final Class<TContent> clazz) {
        return Optional.ofNullable(clazz.cast(attributes.get(name)));
    }

    /**
     * Returns the {@link TContentType} in the {@link #attributes} as the desired type using the provided key or throws an {@link IllegalArgumentException} of the attribute does not exit.
     *
     * @param <TContent> type of the content desired.
     * @param name       name of the property
     * @param clazz      class of the type of the property used for casting.
     *
     * @return the desired attribute as the {@link TContentType}.
     *
     * @throws IllegalArgumentException if the desired attribute does not exist.
     */
    public final <TContent extends TContentType> TContent getAttributeOrFail(final String name, final Class<TContent> clazz) {
        final TContent attribute = clazz.cast(attributes.get(name));

        if (attribute == null) { throw new IllegalArgumentException(String.format("Could not find attribute %s", name)); }

        return attribute;
    }

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
        final TContentType attribute = attributes.get(name);

        if (attribute == null) { return defaultValue; }

        return clazz.cast(attribute.getValue());
    }

    //endregion
}