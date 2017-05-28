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

import de.saxsys.svgfx.core.utils.StringUtil;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * Same interface as {@link BiConsumer} but allows for checked {@link Exception}s to be thrown.
 *
 * @author Xyanid on 02.04.2017.
 * @see StringUtil#splitByDelimiters(String, Collection, ThrowableBiConsumer)
 */
@FunctionalInterface
public interface ThrowableBiConsumer<T, U, E extends Exception> extends BiConsumer<T, U> {

    @Override
    default void accept(final T first, final U second) {
        try {
            acceptOrFail(first, second);
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
     * @throws E if an error occurs.
     */
    void acceptOrFail(final T first, final U second) throws E;
}
