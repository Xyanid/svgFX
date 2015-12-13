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

package de.saxsys.svgfx.xml.elements;


import de.saxsys.svgfx.xml.core.IDataProvider;
import de.saxsys.svgfx.xml.core.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an actual element of the parsed document from an {@link SAXParser}.
 *
 * @param <TDataProvider> the type of the {@link IDataProvider} to be used
 * @param <TResult>       the type of result provided by the element
 * @param <TParent>       the type of parent of this element
 *                        @author Xyanid on 24.10.2015.
 */
public abstract class ElementBase<TDataProvider extends IDataProvider, TResult, TParent extends ElementBase<TDataProvider, ?, ?>> {

    //region Fields

    /**
     * the value of the element which is also its identifier.
     */
    private final String name;

    /**
     * Determines the attributes of the element.
     */
    private final Map<String, String> attributes = new HashMap<>();

    /**
     * The parent elemnent of this element if any.
     */
    private final TParent parent;

    /**
     * Contains the children of this node if any.
     */
    private final List<ElementBase> children;

    /**
     * Method to be called when data is needed by this element.
     */
    private final TDataProvider dataProvider;

    //endregion

    //region Getter

    /**
     * Creates a new instance of he element using the given attributes, parent and dataProvider.
     *
     * @param name         value of the element, must not be null
     * @param attributes   attributes to be used
     * @param parent       parent of the element
     * @param dataProvider dataProvider to be used, must not be null
     *
     * @throws IllegalArgumentException if either value or dataProvider are null
     */
    public ElementBase(final String name, final Attributes attributes, final TParent parent, final TDataProvider dataProvider) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("given value must not be null");
        }

        if (dataProvider == null) {
            throw new IllegalArgumentException("given data provider must not be null");
        }

        this.name = name;

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                this.attributes.put(attributes.getQName(i), attributes.getValue(i));
            }
        }

        this.parent = parent;
        this.children = new ArrayList<>();
        this.dataProvider = dataProvider;
    }

    /**
     * Gets the value of the property.
     *
     * @return the {@link ElementBase#name} of the element
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the attribute map as an unmodifiable map.
     *
     * @return the map of the attributes
     */
    public final Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Gets the {@link ElementBase#parent}.
     *
     * @return the {@link ElementBase#parent}
     */
    public TParent getParent() {
        return parent;
    }

    /**
     * Gets the {@link ElementBase#children}.
     *
     * @return the {@link ElementBase#children}
     */
    public List<ElementBase> getChildren() {
        return children;
    }

    /**
     * Gets the {@link ElementBase#dataProvider}.
     *
     * @return the {@link ElementBase#dataProvider}
     */
    public TDataProvider getDataProvider() {
        return dataProvider;
    }

    //endregion

    //region Constructor

    /**
     * Returns the result for the current element.
     *
     * @return result for the element
     *
     * @throws SAXException will be thrown when an error occurs retrieving the result
     */
    public abstract TResult getResult() throws SAXException;

    //endregion

    //region Public

    /**
     * returns the attributes with the given key.
     *
     * @param key key of the attribute
     *
     * @return the value of the attribute with the given key or null if it does not exist
     */
    public String getAttribute(final String key) {
        return getAttributes().get(key);
    }

    //endregion

    //region Abstract

    /**
     * Will be called when an element is started that represents this element.
     *
     * @throws SAXException will be thrown when an error occurs during processing
     */
    public abstract void startProcessing() throws SAXException;

    /**
     * Will be called when character data (CDATA) is read for an element.
     *
     * @param ch     The characters.
     * @param start  The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     *
     * @throws SAXException will be thrown when an error occurs during processing the characters
     */
    public abstract void processCharacterData(char[] ch, int start, int length) throws SAXException;

    /**
     * Will be called when the end of the element was been reached and thus the processing is finished.
     *
     * @throws SAXException will be thrown when an error occurs during processing
     */
    public abstract void endProcessing() throws SAXException;

    //endregion
}
