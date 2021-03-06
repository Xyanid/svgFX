/*
 * Copyright 2015 - 2017 Xyanid
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

package de.saxsys.svgfx.xml.core;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents an actual element of the parsed document from an {@link SAXParser}.
 *
 * @param <TAttributeType>        the type of the {@link AttributeWrapper} to be used
 * @param <TAttributeHolder>      the type of the {@link AttributeHolder} to be used
 * @param <TDocumentDataProvider> the type of the {@link IDocumentDataProvider} to be used
 * @param <TResult>               the type of result provided by the element
 *
 * @author Xyanid on 24.10.2015.
 */
public abstract class ElementBase<TAttributeType extends AttributeWrapper,
        TAttributeHolder extends AttributeHolder<TAttributeType>,
        TDocumentDataProvider extends IDocumentDataProvider, TResult,
        TChild extends ElementBase<?, ?, TDocumentDataProvider, ?, ?>> {

    //region Fields


    /**
     * the value of the element which is also its identifier.
     */
    private final String name;

    /**
     * Contains the children of this node if any.
     */
    private final List<TChild> children;

    /**
     * Method to be called when data contained in the document is needed by this element.
     */
    private final TDocumentDataProvider documentDataProvider;

    /**
     * This contains the actual attributes of the element.
     */
    private final TAttributeHolder attributeHolder;

    //endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes, parent and documentDataProvider.
     *
     * @param name                 value of the element, must not be null
     * @param attributes           attributes to be used
     * @param documentDataProvider documentDataProvider to be used, must not be null
     * @param attributeHolder      the element that will contain the attributes of this element.
     *
     * @throws IllegalArgumentException if either value or documentDataProvider are null
     */
    public ElementBase(final String name,
                       final Attributes attributes,
                       final TDocumentDataProvider documentDataProvider,
                       final TAttributeHolder attributeHolder) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException(String.format("Creation of element %s failed. Given name must not be null", getClass().getName()));
        }

        if (documentDataProvider == null) {
            throw new IllegalArgumentException(String.format("Creation of element %s failed. given data provider must not be null", getClass().getName()));
        }

        if (attributeHolder == null) {
            throw new IllegalArgumentException(String.format("Creation of element %s failed. given attributeHolder must not be null", getClass().getName()));
        }

        this.name = name;
        this.attributeHolder = attributeHolder;

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); ++i) {

                final String attributeName = attributes.getQName(i);
                final TAttributeType contentType = this.attributeHolder.createAttributeType(attributeName);
                final String attributeValue = attributes.getValue(i);

                if (contentType != null) {
                    try {
                        contentType.setText(attributeValue);
                    } catch (final Exception e) {
                        throw new IllegalArgumentException(String.format("Creation of element [%s] failed. The attribute [%s] is not valid, value is [%s]",
                                                                         getClass().getName(),
                                                                         attributeName,
                                                                         attributeValue), e);
                    }
                    this.attributeHolder.getAttributes().put(attributeName, contentType);
                }
            }
        }

        this.children = new ArrayList<>();
        this.documentDataProvider = documentDataProvider;
    }

    //endregion

    //region Getter

    /**
     * Gets the value of the property.
     *
     * @return the {@link #name} of the element
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the attribute map as an unmodifiable map.
     *
     * @return the map of the attributes
     */
    public final Map<String, TAttributeType> getAttributes() {
        return Collections.unmodifiableMap(this.attributeHolder.getAttributes());
    }

    /**
     * Gets the {@link #documentDataProvider}.
     *
     * @return the {@link #documentDataProvider}
     */
    public TDocumentDataProvider getDocumentDataProvider() {
        return documentDataProvider;
    }

    /**
     * Gets the {@link #attributeHolder}.
     *
     * @return the {@link #attributeHolder}
     */
    public TAttributeHolder getAttributeHolder() {
        return attributeHolder;
    }

    //endregion

    // region Children

    /**
     * Gets an unmodifiable instance of the {@link #children}.
     *
     * @return an unmodifiable instance of the {@link #children}
     */
    public List<TChild> getUnmodifiableChildren() {
        return Collections.unmodifiableList(children);
    }

    boolean addChild(final TChild child) {
        return children.add(child);
    }

    // endregion

    //region Abstract

    /**
     * Determines if the element will be kept, meaning it will be kept in memory and is available as a parent for all its children.
     *
     * @return true if the element will be kept, otherwise false.
     */
    public abstract boolean keepElement();

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
    public abstract void processCharacterData(final char[] ch, final int start, final int length) throws SAXException;

    /**
     * Will be called when the end of the element was been reached and thus the processing is finished.
     *
     * @throws SAXException will be thrown when an error occurs during processing
     */
    public abstract void endProcessing() throws SAXException;

    /**
     * Returns the result for the current element.
     *
     * @return result for the element
     *
     * @throws SAXException will be thrown when an error occurs retrieving the result
     */
    public abstract TResult getResult() throws SAXException;

    //endregion

    // region Override Object

    @Override
    public String toString() {

        final StringBuilder data = new StringBuilder();

        data.append("<").append(name);

        this.attributeHolder.getAttributes().forEach((key, value) -> data.append(String.format(" %s:%s", key, value)));

        data.append(">");

        return data.toString();
    }

    // endregion
}