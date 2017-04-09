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

import java.util.function.Supplier;

/**
 * Same interfaces as the {@link Supplier}, allows to throw checked {@link Exception}s to be thrown.
 *
 * @author Xyanid on 04.02.2017.
 */
@FunctionalInterface
public interface ThrowableSupplier<T, E extends Exception> extends Supplier<T> {

    @Override
    default T get() {
        try {
            return getOrFail();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the data or fails in the process.
     *
     * @return the desired data.
     *
     * @throws E if any exception occurs.
     */
    T getOrFail() throws E;
}
