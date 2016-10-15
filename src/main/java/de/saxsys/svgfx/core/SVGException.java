/*
 * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.elements.SVGElementBase;

/**
 * This exception will be thrown if an error occurs while processing an svg file.
 *
 * @author Xyanid on 02.11.2015.
 */
public class SVGException extends Exception {

    // region Enumeration

    /**
     * Contains the reason for the exception.
     */
    public enum Reason {
        /**
         * Meaning the reason for the exception is unknown, this may be the result of another exception being thrown.
         */
        UNKNOWN,
        /**
         * Meaning that an argument was transmitted that was null but should have not been.
         */
        NULL_ARGUMENT,
        /**
         * Meaning that a {@link de.saxsys.svgfx.core.elements.SVGElementBase} could not be created for any reason.
         */
        ELEMENT_CREATION_FAILED,
        /**
         * Meaning the {@link SVGElementBase#getResult()} failed for any reason.
         */
        FAILED_TO_GET_RESULT,
        /**
         * Meaning that a {@link de.saxsys.svgfx.core.elements.SVGStyle} was referenced but was not found.
         */
        STYLE_NOT_FOUND,
        /**
         * Meaning that the {@link de.saxsys.svgfx.core.elements.SVGRoot} was requested from the {@link SVGDocumentDataProvider} but was not found.
         */
        MISSING_SVG_ROOT,
        /**
         * Meaning that a {@link javafx.scene.paint.Color} could not be created/found even though it was required.
         */
        MISSING_COLOR,
        /**
         * Meaning that an {@link de.saxsys.svgfx.core.elements.SVGGradientBase} does not contain {@link de.saxsys.svgfx.core.elements.SVGStop}s.
         */
        MISSING_STOPS,
        /**
         * Meaning that a {@link String} was parsed that was not actually an {@link de.saxsys.svgfx.core.definitions.Constants#IRI_IDENTIFIER}.
         */
        NOT_AN_IRI_IDENTIFIER,
        /**
         * Meaning that a {@link String} that could be an {@link de.saxsys.svgfx.core.definitions.Constants#IRI_IDENTIFIER} to another element could not be parsed.
         */
        FAILED_TO_PARSE_IRI,
        /**
         * Meaning that a {@link String} is an {@link de.saxsys.svgfx.core.definitions.Constants#IRI_IDENTIFIER} that could not be found.
         */
        MISSING_IRI,
        /**
         * Meaning that the {@link javafx.scene.transform.Transform} could not be created due to a problem with the matrix.
         */
        FAILED_TO_CREATE_MATRIX,
        /**
         * Meaning that a {@link de.saxsys.svgfx.core.content.SVGAttributeType} could not be created.
         */
        FAILED_TO_CREATE_ATTRIBUTE_TYPE,
        /**
         * Meaning that during the creation of a {@link de.saxsys.svgfx.core.content.SVGAttributeTypePoint}, the point format was not as expected.
         */
        INVALID_POINT_FORMAT,
    }

    // endregion

    // region Fields


    private final Reason reason;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param reason the {@link Reason} for the exception.
     */
    public SVGException(final Reason reason) {
        this(reason, null, null);
    }

    /**
     * Creates a new instance.
     *
     * @param message the message of the exception
     */
    public SVGException(final Reason reason, final String message) {
        this(reason, message, null);
    }

    /**
     * Creates a new instance.
     *
     * @param cause the cause for this exception
     */
    public SVGException(final Reason reason, final Throwable cause) {
        this(reason, null, cause);
    }

    /**
     * Creates a new instance.
     *
     * @param reason    the {@link Reason} for the exception.
     * @param message   the message of the exception.
     * @param throwable the throwable that might have caused this exception.
     */
    public SVGException(final Reason reason, final String message, final Throwable throwable) {
        super(message, throwable);
        this.reason = reason;
    }

    // endregion

    // region Getter


    public Reason getReason() {
        return reason;
    }

    // endregion
}