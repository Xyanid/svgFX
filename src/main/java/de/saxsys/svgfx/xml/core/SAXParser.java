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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic XML parser which uses a given elementCreator to process the data provided while parsing.
 *
 * @param <TResult>         the type of the result provided by this parser
 * @param <TDataProvider>   the type of the {@link IDataProvider}
 * @param <TElementCreator> the type of the {@link IElementCreator} @author Xyanid on 24.10.2015.
 */
public abstract class SAXParser<TResult, TDataProvider extends IDataProvider, TElementCreator extends IElementCreator<TDataProvider, TElement>, TElement extends ElementBase<TDataProvider, ?,
        TElement>>
        extends DefaultHandler {

    // region Enumeration

    /**
     * Determines in which state the converter is in.
     */
    public enum State {
        /**
         * Meaning the parser has not yet been set up or done any parsing.
         */
        IDLE,
        /**
         * Meaning the parser is being prepared to read data.
         */
        PREPARING,
        /**
         * Meaning the parser currently reading the start of the document.
         */
        STARTING,
        /**
         * Meaning the parser is entering a new element.
         */
        PARSING_ENTERING_ELEMENT,
        /**
         * Meaning the parser has finished entering a new element.
         */
        PARSING_ENTERING_ELEMENT_FINISHED,
        /**
         * Meaning the parser is starting to process characters for an element.
         */
        PARSING_ENTERING_ELEMENT_CHARACTERS,
        /**
         * Meaning the parser has finished processing characters for an element.
         */
        PARSING_ENTERING_ELEMENT_CHARACTERS_FINISHED,
        /**
         * Meaning the parser is leaving an element.
         */
        PARSING_LEAVING_ELEMENT,
        /**
         * Meaning the parser has finished leaving an element.
         */
        PARSING_LEAVING_ELEMENT_FINISHED,
        /**
         * Meaning the parser has finished parsing the entire document.
         */
        FINISHED,
    }

    // endregion

    // region Fields

    /**
     * Determines the data provider to be used to supply elements with data.
     */
    private final TElementCreator elementCreator;

    /**
     * Determines the dataprovider to be used for this parser.
     */
    private final TDataProvider dataProvider;

    /**
     * Determines the State.
     */
    private final ReadOnlyObjectWrapper<State> state = new ReadOnlyObjectWrapper<>(State.IDLE);

    /**
     * Contains the result of this handler, it may only be valid after this handler was used to parse actual data.
     */
    private TResult result;

    /**
     * Determines the amount of parsings done with this parser.
     */
    private long attemptedParses;

    /**
     * Determines the amount of successful parses.
     */
    private long successfulParses;

    /**
     * The currently processed element.
     */
    private TElement currentElement;

    // endregion

    // region Constructor

    /**
     * Creates a new instance of the parser using the provided interfaces.
     *
     * @param elementCreator element creator to be used
     * @param dataProvider   data provider to be used
     *
     * @throws IllegalArgumentException if either elementCreator or dataProvider are null
     */
    public SAXParser(final TElementCreator elementCreator, final TDataProvider dataProvider) throws IllegalArgumentException {

        if (elementCreator == null) {
            throw new IllegalArgumentException("given elementcreator must not be null");
        }

        if (dataProvider == null) {
            throw new IllegalArgumentException("given dataProvider must not be null");
        }

        this.elementCreator = elementCreator;
        this.dataProvider = dataProvider;
        this.result = null;
    }

    // endregion

    // region Getter    

    /**
     * Gets the {@link SAXParser#result}, which is only set after {@link SAXParser#parse(InputSource)} has been called.
     *
     * @return {@link SAXParser#result}
     */
    public final TResult getResult() {
        return result;
    }

    /**
     * gets the {@link SAXParser#attemptedParses}.
     *
     * @return the {@link SAXParser#attemptedParses}
     */
    public final long getAttemptedParses() {
        return attemptedParses;
    }

    /**
     * gets the {@link SAXParser#successfulParses}.
     *
     * @return the {@link SAXParser#successfulParses}
     */
    public final long getSuccessfulParses() {
        return successfulParses;
    }

    /**
     * sets the {@link SAXParser#successfulParses}.
     *
     * @param value value to be used for {@link SAXParser#successfulParses}
     */
    public final void setSuccessfulParses(final long value) {
        successfulParses = value;
    }

    /**
     * Gets the value of the State.
     *
     * @return the value of the State
     */
    public final State getState() {
        return state.getValue();
    }

    /**
     * Sets the value of {@link #state}.
     *
     * @param state the state to use.
     */
    private void setState(State state) {
        synchronized (this.state) {
            this.state.set(state);
        }
    }

    /**
     * Determines if the parser is busy doing its work, this is the case if the state is not IDLE or FINISHED.
     *
     * @return true if the parser is busy, otherwise false.
     */
    public final boolean isBusy() {
        synchronized (this.state) {
            return state.get() != State.IDLE && state.get() != State.FINISHED;
        }
    }

    /**
     * This method will be called as soon as the parsing of the document has started and set the current {@link #result}.
     * Ideally a new result will be initialized here so it can be filled when the document is processed.
     *
     * @return the new value for the {@link #result}.
     *
     * @throws SAXException when an error occurs
     */
    protected abstract TResult enteringDocument() throws SAXException;

    // endregion

    // region Abstract

    /**
     * This method will be called as soon as the parsing of the document has been finished, the current {@link #result}
     * will be be provided so final operation can be performed.
     *
     * @param result the current {@link #result}, which was initialized during {@link #enteringDocument()}.
     *
     * @throws SAXException when an error occurs
     */
    protected abstract void leavingDocument(final TResult result) throws SAXException;

    /**
     * This method will be called when a new element is starting in the XML tree.
     * If possible the data of the element can be processed here already for the {@link #result}.
     *
     * @param result       the current {@link #result}, which was initialized during {@link #enteringDocument()}.
     * @param dataProvider the {@link #dataProvider} that as provided during initialization
     * @param element      element to be consumed
     *
     * @throws SAXException when an error occurs
     */
    protected abstract void consumeElementStart(final TResult result, final TDataProvider dataProvider, final TElement element) throws SAXException;

    /**
     * This method will be called when an element has ended in the XML tree.
     * Generally the data of the element should be consumed here, however it might not be necessary to consume the data of the element here,
     * since it could be contained in another element. E.g. if an element A contains an element B, then the data of B can still be consumed when element A
     * ends, since element B will be available as child of A.
     *
     * @param result       the current {@link #result}, which was initialized during {@link #enteringDocument()}.
     * @param dataProvider the {@link #dataProvider} that as provided during initialization.
     * @param element      element to be consumed.
     *
     * @throws SAXException when an error occurs
     */
    protected abstract void consumeElementEnd(final TResult result, final TDataProvider dataProvider, final TElement element) throws SAXException;

    /**
     * Gets the property State.
     *
     * @return the State property
     */
    public final ReadOnlyProperty<State> stateProperty() {
        return state.getReadOnlyProperty();
    }

    // endregion

    // region Public

    /**
     * This method will clear the current {@link #result} as well as calling {@link IDataProvider#clear()}.
     */
    public final void clear() {
        result = null;
        dataProvider.clear();
    }

    /**
     * Parses the data of the file provided by the given path.
     *
     * @param path path of the file to be used
     *
     * @throws SAXParseException        if an exception occurs while parsing the data
     * @throws IllegalArgumentException if the given file is null
     * @throws IllegalStateException    if this method is being called while the parser is still busy
     * @throws IOException              if the file can not be found or opened
     */
    public final void parse(final String path) throws SAXParseException, IllegalArgumentException, IllegalStateException, IOException {
        parse(new File(path));
    }

    /**
     * Parses the data of the given file.
     *
     * @param file file to be used
     *
     * @throws SAXParseException        if an exception occurs while parsing the data
     * @throws IllegalArgumentException if the given file is null
     * @throws IllegalStateException    if this method is being called while the parser is still busy
     * @throws IOException              if the file can not be found or opened
     */
    public final void parse(final File file) throws SAXParseException, IllegalArgumentException, IllegalStateException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("given file must not be null");
        }

        InputStream stream = new FileInputStream(file);

        parse(new InputSource(stream));

        stream.close();
    }

    /**
     * Parse the data of the given input source.
     *
     * @param data data to be used, must not be null
     *
     * @throws SAXParseException        if an exception occurs while parsing the data
     * @throws IllegalArgumentException if the given data is null
     * @throws IllegalStateException    if this method is being called while the parser is still busy
     */
    public final void parse(final InputSource data) throws SAXParseException, IllegalArgumentException, IllegalStateException {

        if (data == null) {
            throw new IllegalArgumentException("given data must not be null");
        }

        if (isBusy()) {
            throw new IllegalStateException("Can not attempt to parse while the parser is still working");
        }

        try {
            setState(State.PREPARING);

            dataProvider.clear();

            attemptedParses++;

            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();

            reader.setContentHandler(this);

            reader.setFeature("http://xml.org/sax/features/validation", false);

            reader.parse(data);

            successfulParses++;

        } catch (Exception e) {

            throw new SAXParseException(null, null, e);
        }
    }

    // endregion

    // region Override DefaultHandler

    @Override
    public final void startDocument() throws SAXException {
        result = enteringDocument();

        currentElement = null;

        setState(State.STARTING);
    }

    @Override
    public final void endDocument() throws SAXException {
        currentElement = null;

        setState(State.FINISHED);

        leavingDocument(result);
    }

    @Override
    public final void startElement(final String namespaceURI, final String localName, final String qName, final Attributes attributes) throws SAXException {

        setState(State.PARSING_ENTERING_ELEMENT);

        TElement nextElement = elementCreator.createElement(qName, attributes, currentElement, dataProvider);

        if (nextElement != null) {

            if (currentElement != null) {
                currentElement.getChildren().add(nextElement);
            }

            currentElement = nextElement;

            currentElement.startProcessing();

            consumeElementStart(result, dataProvider, currentElement);
        }

        setState(State.PARSING_ENTERING_ELEMENT_FINISHED);
    }

    @Override
    public final void endElement(final String namespaceURI, final String localName, final String qName) throws SAXException {

        setState(State.PARSING_LEAVING_ELEMENT);

        if (currentElement != null && currentElement.getName().equals(qName)) {

            currentElement.endProcessing();

            consumeElementEnd(result, dataProvider, currentElement);

            // clear the previous element that was processed before the current one, so we can also end its processing if need be
            currentElement = currentElement.getParent();
        }

        setState(State.PARSING_LEAVING_ELEMENT_FINISHED);
    }

    @Override
    public final void characters(final char[] ch, final int start, final int length) throws SAXException {

        setState(State.PARSING_ENTERING_ELEMENT_CHARACTERS);

        if (currentElement != null) {
            currentElement.processCharacterData(ch, start, length);
        }

        setState(State.PARSING_ENTERING_ELEMENT_CHARACTERS_FINISHED);
    }

    // endregion
}
