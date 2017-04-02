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

package de.saxsys.svgfx.core.path;

import de.saxsys.svgfx.core.SVGException;

/**
 * @author Xyanid on 01.04.2017.
 */
public class PathException extends Exception {

    // region Enumeration

    /**
     * Contains the reason for the exception.
     */
    public enum Reason {
        /**
         * Meaning that an argument was transmitted that was null but should have not been.
         */
        NULL_ARGUMENT,
        /**
         * Meaning a string did not represent a valid number.
         */
        INVALID_NUMBER_FORMAT,
        /**
         * Meaning that a path command could not be created because the number of elements in the path was not correct.
         */
        INVALID_PATH_COMMAND_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH,
        /**
         * Meaning that the data describing the command could not be splitted.
         */
        FAILED_TO_SPLIT_DATA,
    }

    // endregion

    // region Fields

    private final Reason reason;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param reason the {@link SVGException.Reason} for the exception.
     */
    public PathException(final Reason reason) {
        this(reason, null, null);
    }

    /**
     * Creates a new instance.
     *
     * @param message the message of the exception
     * @param reason  the {@link SVGException.Reason} for this exception
     */
    public PathException(final Reason reason, final String message) {
        this(reason, message, null);
    }

    /**
     * Creates a new instance.
     *
     * @param cause  the cause for this exception
     * @param reason the {@link SVGException.Reason} for this exception
     */
    public PathException(final Reason reason, final Throwable cause) {
        this(reason, null, cause);
    }

    /**
     * Creates a new instance.
     *
     * @param reason    the {@link SVGException.Reason} for the exception.
     * @param message   the message of the exception.
     * @param throwable the throwable that might have caused this exception.
     */
    public PathException(final Reason reason, final String message, final Throwable throwable) {
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
