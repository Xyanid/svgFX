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

package de.saxsys.svgfx.core.utils;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class provides convenient functions to handle converting data from one type to another using functional interfaces
 *
 * @author by Xyanid on 29.10.2015.
 */
public final class ConvertUtils {

    // region Constructor

    /**
     * Tries to apply the given string as data to the consumer interfaces using the given converter to parse the data
     * into the desired type. If the provided data is null then it will not be used.
     *
     * @param data      data to be used
     * @param consumer  consumer which will use that converted data
     * @param converter converter which will parse the provided data into the desired type
     * @param <TInput>  type of the incoming data
     * @param <TOutput> type of the output data
     */
    public static <TInput, TOutput> void applyData(final TInput data, final Consumer<TOutput> consumer, final Function<TInput, TOutput> converter) {
        applyData(data, consumer, converter, null);
    }

    // endregion

    // region Methods

    /**
     * Tries to apply the given string as data to the consumer interfaces using the given converter to parse the data
     * into the desired type. It also uses the given validator to confirm that the provided data is valid, if no
     * validator is provided, no validation will take place If the provided data is null then it will not be used.
     *
     * @param data      data to be used
     * @param consumer  consumer which will use that converted data
     * @param converter converter which will parse the provided data into the desired type
     * @param validator validator to ensure th provided data is valid, may be null in which case no validation is
     *                  done
     * @param <TInput>  type of the incoming data
     * @param <TOutput> type of the data
     *
     * @throws IllegalArgumentException if the data is not valid according to the provided validator
     */
    public static <TInput, TOutput> void applyData(final TInput data, final Consumer<TOutput> consumer, final Function<TInput, TOutput> converter, final Function<TInput, Boolean> validator)
            throws IllegalArgumentException {

        if (consumer == null) {
            throw new IllegalArgumentException("given consumer must not be null");
        }

        consumer.accept(convert(data, converter, validator));
    }

    /**
     * Tries to convert the given data into the output type. If a validator is provided the input will also be
     * validated.
     *
     * @param data      data to be used
     * @param converter converter which will parse the provided data into the desired type
     * @param validator validator to ensure th provided data is valid, may be null in which case no validation is
     *                  done
     * @param <TInput>  type of the incoming data
     * @param <TOutput> type of the data
     *
     * @return the converted input
     *
     * @throws IllegalArgumentException if the data is not valid according to the provided validator
     */
    public static <TInput, TOutput> TOutput convert(final TInput data, final Function<TInput, TOutput> converter, final Function<TInput, Boolean> validator) throws IllegalArgumentException {

        if (validator != null && !validator.apply(data)) {
            throw new IllegalArgumentException("given data is not valid according to the provided validator");
        }

        return converter.apply(data);
    }

    /**
     *
     */
    private ConvertUtils() {

    }

    // endregion
}
