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

/**
 * This class provides convenient functions to handle converting data from one type to another using functional interfaces
 *
 * @author by Xyanid on 29.10.2015.
 */
public final class CompareUtils {

    // region Constructor

    /**
     * Compares object a and b against each other.
     *
     * @param a first object to use
     * @param b second object to use
     *
     * @return true if a and b are equal or both are null otherwise false.
     */
    public static boolean areEqualOrNull(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    // endregion

    // region Methods

    /**
     *
     */
    private CompareUtils() {

    }

    // endregion
}
