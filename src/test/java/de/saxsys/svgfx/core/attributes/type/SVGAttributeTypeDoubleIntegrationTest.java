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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Xyanid on 16.10.2016.
 */
public class SVGAttributeTypeDoubleIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypeDouble cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypeDouble(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * When the test provided is not valid, an {@link SVGException} will be thrown.
     */
    @Test (expected = SVGException.class)
    public void whenTheProvidedTextIsValidAnSVGExceptionWillBeThrown() throws SVGException {
        cut.setText("invalid");

        cut.getValue();
    }

    /**
     * All units can be converted when being provided as text.
     */
    @Test
    public void differentValuesAreCorrectlyParsedAsLongAsTheyAreValidDoubleValues() throws SVGException {

        final Random random = new Random();

        int counter = 20;

        while (counter-- > 0) {
            final double value = random.nextDouble();

            cut.setText(String.format("%f", value));
            assertEquals(value, cut.getValue(), MINIMUM_DEVIATION);
            assertNull(cut.getUnit());
        }
    }

    //endregion
}