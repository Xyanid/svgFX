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

import de.saxsys.svgfx.core.SVGException;

/**
 * This interface simply allows for an svg exception to bet thrown.
 */
@FunctionalInterface
public interface SplitPredicate {
    /**
     * Called when a delimiter or the last character is read and will indicate whether the current read data can be used or not.
     *
     * @param data the {@link String} containing the currently read data so far.
     *
     * @return true if the currentData shall be consumed otherwise false if not.
     *
     * @throws SVGException in case the splitting is not possible due to invalid data ans so on.
     */
    boolean test(final String data) throws SVGException;
}
