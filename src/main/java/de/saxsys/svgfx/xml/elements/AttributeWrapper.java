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

package de.saxsys.svgfx.xml.elements;

/**
 * This class is used for parsing data which comes from a text. Usually when parsing text into data, there might be more then just normal atomic values as a result of the parsing. This class allows
 * for a default value to be specified. The most common scenario where you want to use this, is if you are parsing length data e.g. 10cm or 10mm.
 *
 * @author Xyanid on 29.10.2015.
 */
public abstract class AttributeWrapper {

    //region Fields

    /**
     * Contains the consumed text.
     */
    private String text;

    //endregion

    //region Constructor

    protected AttributeWrapper() {}

    //endregion

    //region Getter/Setter

    public void setText(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    //endregion

    //region Override Object

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final AttributeWrapper that = (AttributeWrapper) o;

        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    //endregion
}