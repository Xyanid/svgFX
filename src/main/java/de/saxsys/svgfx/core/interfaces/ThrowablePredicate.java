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

package de.saxsys.svgfx.core.interfaces;

import java.util.function.Predicate;

/**
 * Same as {@link Predicate} but allows for checked {@link Exception}s to be thrown.
 */
@FunctionalInterface
public interface ThrowablePredicate<T, E extends Exception> extends Predicate<T> {

    @Override
    default boolean test(final T data) {
        try {
            return testOrFail(data);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the given data or failes
     *
     * @param data the data to be tested.
     *
     * @return true if the data is what was expected otherwise false.
     *
     * @throws E if any error occurs.
     */
    boolean testOrFail(final T data) throws E;
}
