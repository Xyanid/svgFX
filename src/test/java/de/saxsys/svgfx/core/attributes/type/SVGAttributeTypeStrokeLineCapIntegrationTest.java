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
import javafx.scene.shape.StrokeLineCap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 16.10.2016.
 */
public class SVGAttributeTypeStrokeLineCapIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypeStrokeLineCap cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypeStrokeLineCap(dataProvider);
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
            assertEquals(SVGException.Reason.INVALID_STROKE_LINE_CAP, e.getReason());
        }
    }

    /**
     * All {@link StrokeLineCap}s are supported and can be parsed correctly.
     */
    @Test
    public void allStrokeLineCapsAreCanBeParsed() throws SVGException {

        for (final StrokeLineCap cap : StrokeLineCap.values()) {
            cut.setText(cap.name().toLowerCase());
            assertEquals(cap, cut.getValue());
            assertNull(cut.getUnit());
        }
    }

    //endregion
}