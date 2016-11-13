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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import org.junit.Before;
import org.junit.Test;

import static de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeFillRule.DEFAULT_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Xyanid on 16.10.2016.
 */
public class SVGAttributeTypeFillRuleIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypeFillRule cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypeFillRule(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * When the test provided is not valid, the default value will be returned.
     */
    @Test
    public void whenTheProvidedTextIsNotValidTheDefaultValueWillBeReturned() throws SVGException {
        cut.setText("invalid");

        assertSame(DEFAULT_VALUE, cut.getValue());
    }

    /**
     * All fill rules can be converted when being provided as text.
     */
    @Test
    public void allFillRulesAreCorrectlyParsed() throws SVGException {
        for (final Enumerations.FillRuleMapping value : Enumerations.FillRuleMapping.values()) {
            cut.setText(value.getName());
            assertEquals(value.getRule(), cut.getValue());
            assertNull(cut.getUnit());
        }
    }

    //endregion
}