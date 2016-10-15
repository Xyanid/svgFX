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

package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.xml.core.AttributeWrapper;

/**
 * @author Xyanid on 16.03.2016.
 */
public abstract class AttributeBase<TSVGContentType extends AttributeWrapper> {

    // region Fields

    /**
     * Contains the name of the attribute
     */
    private final String name;
    /**
     * This is the actual content of the attribute.
     */
    private final TSVGContentType content;

    // region

    // region Constructor

    public AttributeBase(final String name, final TSVGContentType content) {
        this.name = name;
        this.content = content;
    }

    // endregion


    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return the {@link #name}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the {@link #content}.
     *
     * @return the {@link #content}.
     */
    public TSVGContentType getContent() {
        return content;
    }

    // endregion
}
