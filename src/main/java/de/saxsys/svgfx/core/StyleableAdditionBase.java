package de.saxsys.svgfx.core;

import javafx.css.Styleable;
import javafx.scene.control.Control;

/**
 * Base class from which all SkinAddition should derive
 * Created by Xyanid on 08.11.2015.
 */
abstract class StyleableAdditionBase {

    /**
     * Returns the IStyleableAdditionProvider from the given styleable, which is either the styleable itself or its skin.
     *
     * @param styleable stylable to use.
     *
     * @return the styleable addition from the styleable
     */
    public static IStyleableAdditionProvider getStyleableAddition(final Styleable styleable) {
        if (styleable == null) {
            throw new IllegalArgumentException("given styleable must not be null");
        }

        if (styleable instanceof IStyleableAdditionProvider) {
            return (IStyleableAdditionProvider) styleable;
        } else {
            return (IStyleableAdditionProvider) ((Control) styleable).getSkin();
        }
    }

    /**
     * Returns the styleable addition from the given styleable using the given clazz.
     *
     * @param styleable            stylable to use.
     * @param clazz                class to use.
     * @param <TStyleableAddition> type of the styleable addition
     *
     * @return the styleable addition from the styleable
     */
    public static <TStyleableAddition extends StyleableAdditionBase> TStyleableAddition getStyleableAddition(final Styleable styleable, final Class<TStyleableAddition> clazz) {

        if (clazz == null) {
            throw new IllegalArgumentException("given class must not be null");
        }

        return getStyleableAddition(styleable).getSkinAddition(clazz);
    }
}
