/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core.css;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.shape.FillRule;
import javafx.util.Pair;

/**
 * Represents a {@link FillRule}, the default value is {@link FillRule#EVEN_ODD}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssContentTypeFillRule extends SVGCssContentTypeBase<FillRule, Void> {

    //region Constructor

    /**
     * Creates new instance with a default value of {@link FillRule#EVEN_ODD}.
     */
    public SVGCssContentTypeFillRule(SVGDataProvider dataProvider) {
        super(FillRule.EVEN_ODD, dataProvider);
    }

    //endregion

    //region Override CssContentTypeBase

    @Override
    public Pair<FillRule, Void> getValueAndUnit(final String cssText) {

        FillRule rule = FillRule.EVEN_ODD;

        for (Enumerations.FillRuleMapping mapping : Enumerations.FillRuleMapping.values()) {
            if (mapping.getName().equals(cssText)) {
                rule = mapping.getRule();
                break;
            }
        }

        return new Pair<>(rule, null);
    }

    //endregion
}
