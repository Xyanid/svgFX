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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 02.04.2017.
 */
public class SVGAttributeTypePointsTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypePoints cut;

    // endregion

    // region setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypePoints(dataProvider);
    }

    // endregion

    // region Test

    /**
     * When a text is provided that does not contain 4 values separated either by a space or comma, an exception will be thrown.
     */
    @Test
    public void whenTheProvidedTextIsInvalidAnSVGExceptionWillBeThrown() {

        cut.setText("1");

        try {
            cut.getValue();
            fail("Should have thrown an SVGException");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_POINT_FORMAT, e.getReason());
        }

        cut.setText("1T2");

        try {
            cut.getValue();
            fail("Should have thrown an SVGException");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_POINT_FORMAT, e.getReason());
        }

        cut.setText("1 2 3");

        try {
            cut.getValue();
            fail("Should have thrown an SVGException");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_POINT_FORMAT, e.getReason());
        }

        cut.setText("1 2 3|4");

        try {
            cut.getValue();
            fail("Should have thrown an SVGException");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_POINT_FORMAT, e.getReason());
        }
    }


    /**
     * A text can contains spaces AND commas and will still be recognised as a correct rectangle format
     */
    @Test
    public void spacesAndCommasAreBothValidDelimiters() throws SVGException {

        cut.setText(" 1 ,2 3, 4    5 6   7,8");

        assertEquals(4, cut.getValue().size());
    }

    // endregion
}