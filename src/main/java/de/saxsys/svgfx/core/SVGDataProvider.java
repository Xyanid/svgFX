package de.saxsys.svgfx.core;

import de.saxsys.svgfx.css.core.CssStyle;
import de.saxsys.svgfx.xml.core.IDataProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds an provides data for parsed svg elements
 * Created by Xyanid on 25.10.2015.
 */
public class SVGDataProvider implements IDataProvider {

    //region Fields

    /**
     * Contains all the data provided by this data provider.
     */
    private final Map<String, SVGElementBase> data = new HashMap<>();

    /**
     * Contains all the available styles.
     */
    private final Set<CssStyle> styles = new HashSet<>();

    //endregion

    //region Public

    /**
     * Returns the {@link SVGDataProvider#data}.
     *
     * @return {@link SVGDataProvider#data}
     */
    public final Map<String, SVGElementBase> getData() {
        return data;
    }

    /**
     * Gets the styles of the converted svg file.
     *
     * @return the styles contained in the svg file
     */
    public final Set<CssStyle> getStyles() {
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

        if (StringUtils.isEmpty(key)) {
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
