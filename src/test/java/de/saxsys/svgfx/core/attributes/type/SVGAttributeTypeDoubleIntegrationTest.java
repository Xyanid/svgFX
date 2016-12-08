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
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

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
    @Test
    public void whenTheProvidedTextIsValidAnSVGExceptionWillBeThrown() {
        cut.setText("invalid");

        try {
            cut.getValue();
            fail("Should not be able to get result when input value is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }
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
            assertEquals(value, cut.getValue(), 0.01d);
            assertNull(cut.getUnit());
        }
    }

    //endregion
}