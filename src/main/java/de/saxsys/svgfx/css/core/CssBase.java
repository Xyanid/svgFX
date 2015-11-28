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

package de.saxsys.svgfx.css.core;

import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.definitions.Constants;
import javafx.util.Pair;
import org.w3c.dom.DOMException;

import java.util.function.Function;

/**
 * This class represents the base class for all CssElements, it provides field to hold the cssText as well as parent.
 * It also contains an indication field which will be set internally when the cssElement is modified, thus the next
 * time the cssText is requested it will be updated using the {@link CssBase#createCssText()} method.
 *
 * @param <TParent> type of the parent Created by Xyanid on 29.10.2015.
 */
public abstract class CssBase<TParent extends CssBase> {

    // region Enumeration

    /**
     * Determines in which state a character read from a css text is.
     *
     * @author Xyanid
     */
    public enum CharacterState {

        /**
         * Meaning that the character state is not known, thus this value represents an invalid state.
         */
        NONE,
        /**
         * Meaning that the character of the css string is not inside a comment or string.
         */
        DATA,
        /**
         * Meaning that the character of the css string is a slash.
         */
        SLASH,
        /**
         * Meaning that the character of the css string is inside a comment.
         */
        COMMENT,
        /**
         * Meaning that the character of the css string is inside a string.
         */
        STRING,
    }

    // endregion

    // region Field

    private final TParent parent;

    /**
     * Determines if this element is modifiable or not.
     */
    private final boolean isModifiable;

    /**
     * Determines the cssText of this element.
     */
    private String cssText;

    /**
     * Determines whether this element was modified and the cssText needs to be updated.
     */
    private boolean needsUpdateCssText;

    // endregion

    // region CssText

    /**
     * Creates a new instance.
     *
     * @param isModifiable determines if the element is modifiable.
     * @param parent       set the parent of this element.
     */
    private CssBase(final boolean isModifiable, final TParent parent) {
        this.parent = parent;
        this.isModifiable = parent != null ? parent.getIsModifiable() : isModifiable;
    }

    /**
     * Creates a new instance.
     *
     * @param parent set the parent of this element.
     */
    public CssBase(final TParent parent) {
        this(true, parent);
    }

    /**
     * Creates a new instance of the class.
     *
     * @param isModifiable determines if the element is modifiable or not
     */
    public CssBase(final boolean isModifiable) {
        this(isModifiable, null);
    }

    /**
     * Creates a new instance.
     */
    public CssBase() {
        this(true, null);
    }

    // endregion

    // region Getter

    /**
     * Returns the {@link CssBase#parent} or this element.
     *
     * @return the {@link CssBase#parent} of this element.
     */
    public final TParent getParent() {
        return parent;
    }

    /**
     * Returns the {@link CssBase#isModifiable} of this element.
     *
     * @return the {@link CssBase#isModifiable} of this element
     */
    public final boolean getIsModifiable() {
        return isModifiable;
    }

    // endregion

    // region Setter

    /**
     * Gets the {@link CssBase#cssText}.
     *
     * @return the {@link CssBase#cssText}
     */
    public String getCssText() {
        if (needsUpdateCssText) {
            cssText = createCssText();
            needsUpdateCssText = false;
        }
        return cssText;
    }

    /**
     * Sets the {@link CssBase#cssText}.
     *
     * @param cssText the cssText which will be set
     *
     * @throws DOMException will be thrown when en error occurs
     */
    public final void setCssText(final String cssText) throws DOMException {
        if (!getIsModifiable()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "CssStyle can not be modified");
        }

        if (StringUtils.isNullOrEmpty(cssText)) {
            throw new IllegalArgumentException("given cssText must not be null or empty");
        }

        consumeCssText(cssText);

        setNeedsUpdateCssText(true);
    }

    /**
     * Sets the {@link CssBase#needsUpdateCssText} of this element.
     *
     * @param needsUpdateCssText the value for {@link CssBase#needsUpdateCssText} of this element
     */
    public final void setNeedsUpdateCssText(final boolean needsUpdateCssText) {
        this.needsUpdateCssText = needsUpdateCssText;

        if (this.needsUpdateCssText && parent != null) {
            parent.setNeedsUpdateCssText(true);
        }
    }

    // endregion

    // region Protected

    // CHECKSTYLE:OFF

    /**
     * Filters out comments and allows to react differently to characters which are read within a string and outside
     * a string.
     *
     * @param data            data to be read
     * @param commentConsumer function to be called when a comment character is read, if this function returns true
     *                        the iteration is stopped, can be null
     * @param insideConsumer  function to be called when a character inside a string is to be consumed, if this
     *                        function returns true the iteration is stopped, must not be null
     * @param outsideConsumer function to be called when a character outside a string is to be consumed, if this
     *                        function returns true the iteration is stopped, must not be null
     *
     * @return a {@link Pair} contains at its first position if we exited while being in a comment and at its second
     * position if we exited while being in a string
     *
     * @throws DOMException when a comment or string is not properly closed, this is only true if the string was read
     *                      until the end and not exited by one of the consumers
     */
    protected Pair<Boolean, Boolean> filterCommentAndString(final String data,
                                                            final Function<Character, Boolean> commentConsumer,
                                                            final Function<Character, Boolean> insideConsumer,
                                                            final Function<Character, Boolean> outsideConsumer) throws DOMException {

        if (data == null) {
            throw new IllegalArgumentException("given data must not be null");
        }

        if (insideConsumer == null) {
            throw new IllegalArgumentException("given insideConsumer must not be null");
        }

        if (outsideConsumer == null) {
            throw new IllegalArgumentException("given outsideConsumer must not be null");
        }

        boolean isInComment = false;

        boolean isInString = false;

        for (int i = 0; i < data.length(); i++) {

            char character = data.charAt(i);

            // if we are in a comment do not need to do anything
            if (isInComment) {
                // in this case we are at the end of the comment section
                if (character == Constants.COMMENT_TAG && i > 0 && data.charAt(i - 1) == Constants.COMMENT_INDICATOR) {
                    isInComment = false;

                    // if the comment consumer say its time to leave we will stop
                    if (commentConsumer != null && commentConsumer.apply(character)) {
                        return new Pair<>(true, false);
                    }
                }

                continue;
            }

            // we are reading a string indicator so we either enter the string or we are leaving it
            if (character == Constants.STRING_INDICATOR) {
                isInString = !isInString;
            }

            // if we are not within a string every character is consumed since they are part of the value
            if (!isInString) {
                // in this case the comment section started
                if (character == Constants.COMMENT_TAG && i < data.length() - 1 && data.charAt(i + 1) == Constants.COMMENT_INDICATOR) {
                    isInComment = true;

                    // if the comment consumer say its time to leave we will stop
                    if (commentConsumer != null && commentConsumer.apply(character)) {
                        return new Pair<>(true, false);
                    }

                    continue;
                }

                // if the outsideConsumer say its time to leave we will stop
                if (outsideConsumer.apply(character)) {
                    return new Pair<>(false, false);
                }

                continue;
            }

            // if the insideConsumer say its time to leave we will stop
            if (insideConsumer.apply(character)) {
                return new Pair<>(false, true);
            }
        }

        // comment was not properly closed
        if (isInComment) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Given declaration block contains an unclosed comment.");
        }

        // string was not properly closed
        if (isInString) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Given declaration block contains an unclosed string.");
        }

        return new Pair<>(false, false);
    }

    // CHECKSTYLE:ON

    // endregion

    // region Abstract

    /**
     * Will be called when the given cssText should be consumed by this element.
     *
     * @param cssText the cssText to be consumed
     *
     * @throws DOMException if an error during consumption occurs
     */
    public abstract void consumeCssText(String cssText) throws DOMException;

    /**
     * Creates the cssText for this element, this should be based upon the current status of the element.
     *
     * @return the cssText for this element.
     */
    public abstract String createCssText();

    // endregion
}
