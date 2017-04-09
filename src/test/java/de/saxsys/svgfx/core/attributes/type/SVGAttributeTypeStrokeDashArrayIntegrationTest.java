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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 16.10.2016.
 */
public class SVGAttributeTypeStrokeDashArrayIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypeStrokeDashArray cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypeStrokeDashArray(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * Invalid input data will cause the desired exception
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
     * A Stroke dash array can be correctly parsed.
     */
    @Test
    public void aStrokeDashArrayCanBeCorrectlyParsed() throws SVGException {

        final Random random = new Random();

        final StringBuilder data = new StringBuilder();

        for (final SVGAttributeTypeLength.Unit unit : SVGAttributeTypeLength.Unit.values()) {
            final double value = random.nextDouble();

            int counter = random.nextInt(10) + 1;

            data.setLength(0);

            while (counter-- > 0) {
                data.append(String.format("%s%s%s", data.length() == 0 ? "" : ",", String.format("%f", value).replace(",", "."), unit.getName()));
            }

            cut.setText(data.toString());

            for (final SVGAttributeTypeLength length : cut.getValue()) {
                assertEquals(value, length.getValue(), 0.01d);
                assertEquals(unit, length.getUnit());
            }

            for (final Double valueDouble : cut.getDashValues()) {
                assertEquals(value, valueDouble, 0.01d);
            }
        }
    }

    //endregion
}