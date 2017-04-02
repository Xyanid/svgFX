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
import de.saxsys.svgfx.core.utils.StringUtil;

import java.util.List;

/**
 * This interface is used to split an {@link String} and consume the data after each split.
 *
 * @author Xyanid on 02.04.2017.
 * @see StringUtil#splitByDelimiters(String, List, SplitConsumer)
 */
@FunctionalInterface
public interface SplitConsumer {

    /**
     * Consumes split data.
     *
     * @param delimiter the delimiter in front of the data.
     * @param data      the data behind the delimiter.
     */
    void consume(final Character delimiter, final String data) throws SVGException;
}
