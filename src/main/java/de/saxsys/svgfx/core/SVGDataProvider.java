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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.xml.core.IDataProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds an provides data for parsed svg elements
 * @author Xyanid on 25.10.2015.
 */
public class SVGDataProvider implements IDataProvider {

    //region Fields

    /**
     * Contains all the data provided by this data provider.
     */
    final Map<String, SVGElementBase> data = new HashMap<>();

    /**
     * Contains all the available styles.
     */
    final Set<SVGCssStyle> styles = new HashSet<>();

    //endregion

    //region Public

    /**
     * Returns the {@link SVGDataProvider#data} as an unmodifiable map.
     *
     * @return {@link SVGDataProvider#data} as an unmodifiable map.
     */
    public final Map<String, SVGElementBase> getUnmodifiableData() {
        return Collections.unmodifiableMap(data);
    }

    /**
     * Gets the styles of the converted svg file.
     *
     * @return the styles contained in the svg file
     */
    public final Set<SVGCssStyle> getStyles() {
        return styles;
    }

    /**
     * Determines whether the given key exists in the {@link SVGDataProvider#data}.
     *
     * @param key key to check
     *
     * @return true if the given key exists
     */
    public final boolean hasData(final String key) {
        return data.get(key) != null;
    }

    /**
     * Returns the data of the given key as the desired type if it exists.
     *
     * @param clazz   class to be used, must not be null
     * @param key     key to be used must not be null or empty
     * @param <TData> desired type of the data
     *
     * @return the data as the desired type or null if the data is null or can not be cast into the desired type
     */
    public final <TData extends SVGElementBase> TData getData(final Class<TData> clazz, final String key) {

        if (clazz == null) {
            throw new IllegalArgumentException("given class must not be null or empty");
        }

        if (StringUtils.isNullOrEmpty(key)) {
            throw new IllegalArgumentException("given key must not be null or empty");
        }

        SVGElementBase data = this.data.get(key);

        if (data != null && clazz.isAssignableFrom(data.getClass())) {
            return clazz.cast(data);
        }

        return null;
    }

    //endregion

    //region Implement IDataProvider

    /**
     * Resets the data provider clearing all the stored data and styles.
     */
    @Override public final void clear() {
        data.clear();
        styles.clear();
    }

    //endregion
}
