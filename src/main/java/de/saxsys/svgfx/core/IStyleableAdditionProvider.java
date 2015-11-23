package de.saxsys.svgfx.core;

/**
 * This interface is to be attached to all the {@link javafx.scene.control.Skin}s that use a {@link StyleableAdditionBase}
 * Created by Xyanid on 08.11.2015.
 */
interface IStyleableAdditionProvider {

    /**
     * Provides the {@link StyleableAdditionBase} for the desired class that is attached to this skin.
     *
     * @param clazz                the class for which the skin addition should be returned.
     * @param <TStyleableAddition> type of the styleable
     *
     * @return the {@link StyleableAdditionBase} that is attached to this skin
     */
    <TStyleableAddition extends StyleableAdditionBase> TStyleableAddition getSkinAddition(final Class<TStyleableAddition> clazz);
}

