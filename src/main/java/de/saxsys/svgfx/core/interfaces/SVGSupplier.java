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
 * Same interfaces as the {@link java.util.function.Supplier}, allows to throw {@link SVGException}s
 *
 * @author Xyanid on 04.02.2017.
 */
@FunctionalInterface
public interface SVGSupplier<T> {

    T get() throws SVGException;
}
