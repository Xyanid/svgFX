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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static de.saxsys.svgfx.core.utils.TestUtils.assertResultFails;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg polyline elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGPolylineTest {

    /**
     * Ensures that the polyline required for a line are parse correctly.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100,40 100,80");

        final SVGPolyline polyline = new SVGPolyline(SVGPolyline.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        assertEquals(6, polyline.getResult().getPoints().size());
        assertEquals(60.0d, polyline.getResult().getPoints().get(0), 0.01d);
        assertEquals(20.0d, polyline.getResult().getPoints().get(1), 0.01d);
        assertEquals(100.0d, polyline.getResult().getPoints().get(2), 0.01d);
        assertEquals(40.0d, polyline.getResult().getPoints().get(3), 0.01d);
        assertEquals(100.0d, polyline.getResult().getPoints().get(4), 0.01d);
        assertEquals(80.0d, polyline.getResult().getPoints().get(5), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void whenAnyAttributeIsInvalidAnSVGExceptionIsThrownDuringTheCreatingOfTheResult() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100,A 100,80");

        assertResultFails(SVGPolyline::new,
                          SVGPolyline.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100 100,80");

        assertResultFails(SVGPolyline::new,
                          SVGPolyline.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));
    }

    /**
     * Ensures that a {@link SAXException} is thrown of one of the attributes is missing.
     */
    @Test
    public void whenAnyAttributeIsMissingNoSVGExceptionIsThrownDuringTheCreatingOfTheResult() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGPolyline polyline = new SVGPolyline(SVGPolyline.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        try {
            assertEquals(0, polyline.getResult().getPoints().size());
        } catch (final SAXException e) {
            fail();
        }
    }
}