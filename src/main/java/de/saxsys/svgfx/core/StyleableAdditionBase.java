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

import javafx.css.Styleable;
import javafx.scene.control.Control;

/**
 * Base class from which all SkinAddition should derive
 *
 * @author Xyanid on 08.11.2015.
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
