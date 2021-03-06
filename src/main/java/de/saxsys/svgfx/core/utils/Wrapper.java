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

package de.saxsys.svgfx.core.utils;

import java.util.Optional;

/**
 * Simple wrapper class to wraps an object. The wrapper is not immutable and the object can be changed.
 *
 * @param <T> the type of the object that is being wrapped.
 *
 * @author Xyanid on 08.04.2017.
 */
public class Wrapper<T> {

    // region Fields

    /**
     * The current object that is being wrapped if any.
     */
    private T object;

    // endregion

    // region Constructor

    public Wrapper() {}

    public Wrapper(final T object) {
        this.object = object;
    }

    // endregion

    // region Getter/Setter

    /**
     * Returns a {@link Optional} containing the {@link #object}.
     *
     * @return a new {@link Optional} containing the {@link #object}.
     */
    public Optional<T> getOptional() {
        return Optional.ofNullable(object);
    }

    /**
     * Returns the {@link #object} only if it is not null, otherwise an {@link IllegalStateException} will be thrown.
     *
     * @return the {@link #object}.
     *
     * @throws IllegalStateException if the {@link #object} is null.
     */
    public T getOrFail() {
        if (object == null) {
            throw new IllegalStateException("Can not request argument when it is not set");
        }
        return get();
    }

    /**
     * Returns the {@link #object}.
     *
     * @return the {@link #object}.
     */
    public T get() {
        return object;
    }

    /**
     * Set the {@link #object}.
     *
     * @param object the value to use.
     */
    public void set(T object) {
        this.object = object;
    }

    // endregion
}
