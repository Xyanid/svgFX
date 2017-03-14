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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.utils.StringUtil;
import de.saxsys.svgfx.xml.core.IDocumentDataProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Holds an provides data for parsed svg elements
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGDocumentDataProvider implements IDocumentDataProvider {

    //region Fields

    /**
     * Contains all the data provided by this data provider.
     */
    private final Map<String, SVGElementBase> data = new HashMap<>();

    /**
     * Contains all the available styles.
     */
    private final Set<SVGCssStyle> styles = new HashSet<>();

    //endregion

    //region Public

    /**
     * Returns the {@link SVGDocumentDataProvider#data} as an unmodifiable map.
     *
     * @return {@link SVGDocumentDataProvider#data} as an unmodifiable map.
     */
    public final Map<String, SVGElementBase> getUnmodifiableData() {
        return Collections.unmodifiableMap(data);
    }

    /**
     * Gets the styles of the converted svg file.
     *
     * @return the styles contained in the svg file
     */
    public final Set<SVGCssStyle> getUnmodifiableStyles() {
        return Collections.unmodifiableSet(styles);
    }

    /**
     * Adds the given {@link SVGCssStyle} to the {@link #styles}.
     *
     * @param style the {@link SVGCssStyle} to add.
     *
     * @return true if the {@link SVGCssStyle} was added otherwise false.
     */
    public boolean addStyle(final SVGCssStyle style) {
        return styles.add(style);
    }

    /**
     * Adds the given {@link SVGCssStyle}s to the {@link #styles}.
     *
     * @param styles the {@link SVGCssStyle}s to add.
     *
     * @return true if the {@link SVGCssStyle}s were added otherwise false.
     */
    public boolean addStyles(final Collection<SVGCssStyle> styles) {
        return this.styles.addAll(styles);
    }

    /**
     * Sets the given data into the map.
     *
     * @param key  the key of the identifier of the data, must not be null or epmty.
     * @param data the the data that should be set, must not be null.
     *
     * @throws IllegalArgumentException if either key is null or empty or data is null.
     */
    public final void storeData(final String key, final SVGElementBase data) {

        if (StringUtil.isNullOrEmpty(key)) {
            throw new IllegalArgumentException("given key must not be null or empty");
        }

        if (data == null) {
            throw new IllegalArgumentException("given data must not be null");
        }

        this.data.put(key, data);
    }

    /**
     * Returns the data of the given key as the desired type if it exists.
     *
     * @param <TData> desired type of the data
     * @param key     key to be used must not be null or empty
     * @param clazz   class to be used, must not be null
     *
     * @return an {@link Optional} containing the desired data or {@link Optional#empty()} if no such element exist.
     *
     * @throws SVGException if any provided parameter is null.
     */
    public final <TData extends SVGElementBase> Optional<TData> getData(final String key, final Class<TData> clazz) throws SVGException {

        if (clazz == null) {
            throw new SVGException(SVGException.Reason.NULL_ARGUMENT, "given class must not be null or empty");
        }

        if (StringUtil.isNullOrEmpty(key)) {
            throw new SVGException(SVGException.Reason.NULL_ARGUMENT, "given key must not be null or empty");
        }

        final SVGElementBase data = this.data.get(key);

        if (data != null && clazz.isAssignableFrom(data.getClass())) {
            return Optional.of(clazz.cast(data));
        }

        return Optional.empty();
    }

    //endregion

    //region Implement IDocumentDataProvider

    /**
     * Resets the data provider clearing all the stored data and styles.
     */
    @Override
    public final void clear() {
        data.clear();
        styles.clear();
    }

    //endregion
}