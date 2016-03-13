package de.saxsys.svgfx.content;

import java.util.HashMap;
import java.util.Map;

/**
 * This class simply hold a {@link java.util.Map} of {@link String} and {@link ContentTypeBase}. Its main purpose is to handle the splitting and storage of
 * data which is represented by a key and has a certain content, where as the content is described as the {@link TContentType}.
 *
 * @param <TContentType> the type of the content that is stored.
 *
 * @author Xyanid on 13.03.2016.
 */
public abstract class ContentTypeHolder<TContentType extends ContentTypeBase> {

    //region Fields

    /**
     * Contains all the contentMap provided by this style.
     */
    protected final Map<String, TContentType> contentMap = new HashMap<>();

    //endregion

    // region Abstract

    /**
     * This method is used each time a new {@link ContentTypeBase} is needed.
     *
     * @param name the name of the content type to be created.
     *
     * @return a new {@link TContentType} for the given name.
     */
    public abstract TContentType createContentType(final String name);

    // endregion

    //region Public

    /**
     * Returns the {@link TContentType} in {@link #contentMap} using the provided key or null if no such content type exist.
     *
     * @param name name of the content.
     *
     * @return the {@link TContentType} in the given map or null.
     */
    public final TContentType getContentType(final String name) {
        return contentMap.get(name);
    }

    /**
     * Determines if the given {@link TContentType} is contained in the {@link #contentMap}.
     *
     * @param name name of the {@link TContentType} to look for.
     *
     * @return true if a {@link TContentType} with the name exists otherwise false.
     */
    public final boolean hasContentType(String name) {
        return contentMap.containsKey(name);
    }

    /**
     * Returns the {@link TContentType} in the {@link #contentMap} as the desired type using the provided key or null if no such content type exist.
     *
     * @param <TContent> type of the content desired.
     * @param name       name of the property
     * @param clazz      class of the type of the property used for casting.
     *
     * @return the {@link TContentType} or null.
     */
    public final <TContent extends TContentType> TContent getContentType(final String name, final Class<TContent> clazz) {
        return clazz.cast(contentMap.get(name));
    }

    //endregion
}