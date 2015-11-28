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

package de.saxsys.svgfx.xml.core;

import de.saxsys.svgfx.xml.elements.ElementBase;
import org.xml.sax.Attributes;

/**
 * This interfaces is used to create an instance of an element.
 *
 * @param <TDataProvider> the type of the {@link IDataProvider} Created by Xyanid on 25.10.2015.
 */
public interface IElementCreator<TDataProvider extends IDataProvider> {

    /**
     * creates a new instance of the desired {@link ElementBase} using the given value as an indicator which instance
     * to create.
     *
     * @param name         local value of the element for which the {@link ElementBase} is to be created
     * @param attributes   attributes which are to be applied to the {@link ElementBase}
     * @param parent       the parent to be used for the {@link ElementBase}
     * @param dataProvider the dataProvider to be used for the {@link ElementBase}
     *
     * @return a new instance of an {@link ElementBase}
     */
    ElementBase<TDataProvider, ?, ?> createElement(String name, Attributes attributes, ElementBase<TDataProvider, ?, ?> parent, TDataProvider dataProvider);
}
