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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static de.saxsys.svgfx.core.elements.utils.TestUtils.assertCreationFails;
import static de.saxsys.svgfx.core.elements.utils.TestUtils.assertResultFails;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg line elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGLineTest {

    /**
     * Ensures that the attributes required for a line are parse correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("25");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("35");

        final SVGLine line = new SVGLine(SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertEquals(50.0d, line.getResult().getStartX(), 0.01d);
        assertEquals(100.0d, line.getResult().getStartY(), 0.01d);
        assertEquals(25.0d, line.getResult().getEndX(), 0.01d);
        assertEquals(35.0d, line.getResult().getEndY(), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());

        when(attributes.getValue(0)).thenReturn("A");
        when(attributes.getValue(1)).thenReturn("75");
        when(attributes.getValue(2)).thenReturn("50");
        when(attributes.getValue(3)).thenReturn("25");

        assertCreationFails(SVGLine::new, SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGLine.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("100");
        when(attributes.getValue(1)).thenReturn("A");
        when(attributes.getValue(2)).thenReturn("50");
        when(attributes.getValue(3)).thenReturn("25");

        assertCreationFails(SVGLine::new, SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGLine.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("100");
        when(attributes.getValue(1)).thenReturn("75");
        when(attributes.getValue(2)).thenReturn("A");
        when(attributes.getValue(3)).thenReturn("25");

        assertCreationFails(SVGLine::new, SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGLine.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("100");
        when(attributes.getValue(1)).thenReturn("75");
        when(attributes.getValue(2)).thenReturn("50");
        when(attributes.getValue(3)).thenReturn("A");

        assertCreationFails(SVGLine::new, SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGLine.class, NumberFormatException.class);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);
        when(attributes.getValue(0)).thenReturn("75");
        when(attributes.getValue(1)).thenReturn("50");
        when(attributes.getValue(2)).thenReturn("25");

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());

        final SVGLine line = new SVGLine(SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertResultFails(line, IllegalArgumentException.class);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_Y.getName());

        final SVGLine line1 = new SVGLine(SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertResultFails(line1, IllegalArgumentException.class);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_Y.getName());

        final SVGLine line2 = new SVGLine(SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertResultFails(line2, IllegalArgumentException.class);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_Y.getName());

        final SVGLine line3 = new SVGLine(SVGLine.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertResultFails(line3, IllegalArgumentException.class);
    }
}
