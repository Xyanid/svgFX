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

import java.util.function.BiFunction;

/**
 * Same interface as {@link BiFunction} but allows for checked {@link Exception}s to be thrown.
 *
 * @author Xyanid on 02.04.2017.
 */
@FunctionalInterface
public interface ThrowableBiFunction<T, U, R, E extends Exception> extends BiFunction<T, U, R> {

    @Override
    default R apply(final T first, final U second) {
        try {
            return applyOrFail(first, second);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Consumes the given two elements.
     *
     * @param first  the first element to be consumed.
     * @param second the second element to be consumed.
     *
     * @return the result of the call.
     *
     * @throws E if an error occurs.
     */
    R applyOrFail(final T first, final U second) throws E;
}
