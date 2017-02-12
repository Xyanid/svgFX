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
import de.saxsys.svgfx.core.definitions.enumerations.CycleMethodMapping;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 22.11.2016.
 */
public class SVGAttributeTypeCycleMethodIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypeCycleMethod cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypeCycleMethod(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * When the test provided is not valid, the default value will be returned.
     */
    @Test
    public void whenTheProvidedTextIsNotValidTheDefaultValueWillBeReturned() {
        cut.setText("invalid");

        try {
            cut.getValue();
            fail("Should not be able to parse invalid cycle method");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_CYCLE_METHOD, e.getReason());
        }
    }

    /**
     * All {@link javafx.scene.paint.CycleMethod}s can be converted when being provided as text.
     */
    @Test
    public void allCycleMethodsAreCorrectlyParsed() throws SVGException {
        for (final CycleMethodMapping value : CycleMethodMapping.values()) {
            cut.setText(value.getName());
            assertEquals(value.getMethod(), cut.getValue());
            assertNull(cut.getUnit());
        }
    }

    //endregion
}