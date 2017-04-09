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

/**
 * @author Xyanid on 01.04.2017.
 */
public class PathException extends Exception {

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param message the message of the exception
     */
    public PathException(final String message) {
        this(message, null);
    }

    /**
     * Creates a new instance.
     *
     * @param cause the cause for this exception
     */
    public PathException(final Throwable cause) {
        this(null, cause);
    }

    /**
     * Creates a new instance.
     *
     * @param message   the message of the exception.
     * @param throwable the throwable that might have caused this exception.
     */
    public PathException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    // endregion
}
