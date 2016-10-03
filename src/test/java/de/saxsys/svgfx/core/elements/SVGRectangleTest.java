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

import static de.saxsys.svgfx.core.elements.utils.TestUtils.assertCreationFails;
import static de.saxsys.svgfx.core.elements.utils.TestUtils.assertResultFails;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg rectangle elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGRectangleTest {

    /**
     * Ensures that the attributes supported for a rectangle are parse correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(6);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POSITION_X.getName());
        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.POSITION_Y.getName());
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.WIDTH.getName());
        when(attributes.getValue(2)).thenReturn("25");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.HEIGHT.getName());
        when(attributes.getValue(3)).thenReturn("35");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        when(attributes.getValue(4)).thenReturn("5");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());
        when(attributes.getValue(5)).thenReturn("10");

        final SVGRectangle rectangle = new SVGRectangle(SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertEquals(50.0d, rectangle.getResult().getX(), 0.01d);
        assertEquals(100.0d, rectangle.getResult().getY(), 0.01d);
        assertEquals(25.0d, rectangle.getResult().getWidth(), 0.01d);
        assertEquals(35.0d, rectangle.getResult().getHeight(), 0.01d);
        assertEquals(10.0d, rectangle.getResult().getArcWidth(), 0.01d);
        assertEquals(20.0d, rectangle.getResult().getArcHeight(), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(6);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POSITION_X.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.POSITION_Y.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.WIDTH.getName());
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.HEIGHT.getName());
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());


        when(attributes.getValue(0)).thenReturn("A");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertCreationFails(SVGRectangle::new, SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGRectangle.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getValue(1)).thenReturn("A");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertCreationFails(SVGRectangle::new, SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGRectangle.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("A");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertCreationFails(SVGRectangle::new, SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGRectangle.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("A");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertCreationFails(SVGRectangle::new, SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGRectangle.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("A");
        when(attributes.getValue(5)).thenReturn("5");

        assertCreationFails(SVGRectangle::new, SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGRectangle.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("A");

        assertCreationFails(SVGRectangle::new, SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGRectangle.class, NumberFormatException.class);
    }

    /**
     * Ensures that a {@link SVGException} is thrown if one of the required attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getValue(0)).thenReturn("50.0");

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.WIDTH.getName());

        final SVGRectangle rectangle = new SVGRectangle(SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertResultFails(rectangle, IllegalArgumentException.class);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.HEIGHT.getName());

        final SVGRectangle rectangle1 = new SVGRectangle(SVGRectangle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertResultFails(rectangle1, IllegalArgumentException.class);
    }
}
