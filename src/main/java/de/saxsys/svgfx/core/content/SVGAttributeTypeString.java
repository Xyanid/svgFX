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

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import javafx.util.Pair;

/**
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeString extends SVGAttributeType<String, Void> {

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeString(final SVGDocumentDataProvider dataProvider) {
        super(null, dataProvider);
    }

    //endregion

    //region Override AttributeWrapper

    @Override
    protected Pair<String, Void> getValueAndUnit(final String cssText) {

        return new Pair<>(cssText, null);
    }

    //endregion
}