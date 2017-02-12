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
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static de.saxsys.svgfx.core.utils.TestUtils.assertResultFails;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg circle elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@RunWith (MockitoJUnitRunner.class)
public final class SVGCircleTest {

    /**
     * Ensures that the attributes required for a circle are parse correctly.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(2)).thenReturn("25");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertEquals(50.0d, circle.getResult().getCenterX(), 0.01d);
        assertEquals(100.0d, circle.getResult().getCenterY(), 0.01d);
        assertEquals(25.0d, circle.getResult().getRadius(), 0.01d);
    }

    /**
     * Ensures that a {@link de.saxsys.svgfx.core.SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void whenAnyAttributeIsInvalidAnSVGExceptionIsThrownDuringTheCreatingOfTheResult() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS.getName());

        when(attributes.getValue(0)).thenReturn("A");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("25");

        assertResultFails(SVGCircle::new, SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getValue(0)).thenReturn("10.0");
        when(attributes.getValue(1)).thenReturn("B");
        when(attributes.getValue(2)).thenReturn("25");

        assertResultFails(SVGCircle::new, SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getValue(0)).thenReturn("10.0");
        when(attributes.getValue(1)).thenReturn("10.0");
        when(attributes.getValue(2)).thenReturn("A");

        assertResultFails(SVGCircle::new, SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });
    }

    /**
     * Ensures that a {@link de.saxsys.svgfx.core.SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void whenAnyAttributeIsMissingAnSVGExceptionIsThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("25");

        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS.getName());

        assertResultFails(SVGCircle::new, SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.MISSING_ATTRIBUTE, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS.getName());

        assertResultFails(SVGCircle::new, SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.MISSING_ATTRIBUTE, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());

        assertResultFails(SVGCircle::new, SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.MISSING_ATTRIBUTE, ((SVGException) exception.getCause()).getReason());
        });
    }

    //    /**
    //     * The bounding rectangle described by the shape can be correctly determined.
    //     */
    //    @Test
    //    public void theBoundingBoxCanBeDeterminedCorrectly() throws SVGException {
    //        final Attributes attributes = Mockito.mock(Attributes.class);
    //
    //        when(attributes.getLength()).thenReturn(3);
    //
    //        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
    //        when(attributes.getValue(0)).thenReturn("50.0");
    //        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
    //        when(attributes.getValue(1)).thenReturn("100.0");
    //        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS.getName());
    //        when(attributes.getValue(2)).thenReturn("25");
    //
    //        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());
    //
    //        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = circle.createBoundingBox();
    //
    //        assertEquals(25.0d, boundingBox.getMinX().getValue(), 0.01d);
    //        assertEquals(75.0d, boundingBox.getMaxX().getValue(), 0.01d);
    //        assertEquals(75.0d, boundingBox.getMinY().getValue(), 0.01d);
    //        assertEquals(125.0d, boundingBox.getMaxY().getValue(), 0.01d);
    //    }
}