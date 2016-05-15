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

package de.saxsys.svgfx.core.content;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.xml.attribute.AttributeHolder;

/**
 * @author Xyanid on 27.03.2016.
 */
public class SVGAttributeHolder extends AttributeHolder<SVGAttributeType> {

    //region Fields

    private SVGDataProvider dataProvider;

    //endregion

    // region Constructor

    public SVGAttributeHolder(final SVGDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // endregion

    //region Override AttributeHolder

    @Override
    public SVGAttributeType createAttributeType(final String name) {
        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(dataProvider);
            }
        }

        for (CoreAttributeMapper attribute : CoreAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(dataProvider);
            }
        }

        return new SVGAttributeTypeString(dataProvider);
    }

    //endregion
}
