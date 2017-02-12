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
 * This test will ensure that svg ellipse elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@RunWith (MockitoJUnitRunner.class)
public final class SVGEllipseTest {

    /**
     * Ensures that the attributes required for a ellipse are parse correctly.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        when(attributes.getValue(2)).thenReturn("25");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("35");

        final SVGEllipse ellipse = new SVGEllipse(SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertEquals(50.0d, ellipse.getResult().getCenterX(), 0.01d);
        assertEquals(100.0d, ellipse.getResult().getCenterY(), 0.01d);
        assertEquals(25.0d, ellipse.getResult().getRadiusX(), 0.01d);
        assertEquals(35.0d, ellipse.getResult().getRadiusY(), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void whenAnyAttributeIsInvalidAnSVGExceptionIsThrownDuringTheCreatingOfTheResult() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("A");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        when(attributes.getValue(2)).thenReturn("25");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("15");

        assertResultFails(SVGEllipse::new, SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getValue(1)).thenReturn("A");
        when(attributes.getValue(2)).thenReturn("25");
        when(attributes.getValue(3)).thenReturn("15");

        assertResultFails(SVGEllipse::new, SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getValue(1)).thenReturn("100");
        when(attributes.getValue(2)).thenReturn("A");
        when(attributes.getValue(3)).thenReturn("15");

        assertResultFails(SVGEllipse::new, SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getValue(1)).thenReturn("100");
        when(attributes.getValue(2)).thenReturn("25");
        when(attributes.getValue(3)).thenReturn("A");

        assertResultFails(SVGEllipse::new, SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, ((SVGException) exception.getCause()).getReason());
        });
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void whenAnyAttributeIsMissingAnSVGExceptionIsThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getValue(0)).thenReturn("50.0");

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());

        assertResultFails(SVGEllipse::new, SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.MISSING_ATTRIBUTE, ((SVGException) exception.getCause()).getReason());
        });

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());

        assertResultFails(SVGEllipse::new, SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
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
    //        when(attributes.getLength()).thenReturn(4);
    //        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
    //        when(attributes.getValue(0)).thenReturn("50.0");
    //        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
    //        when(attributes.getValue(1)).thenReturn("100.0");
    //        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
    //        when(attributes.getValue(2)).thenReturn("25");
    //        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());
    //        when(attributes.getValue(3)).thenReturn("15");
    //
    //        final SVGEllipse circle = new SVGEllipse(SVGEllipse.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());
    //
    //        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = circle.createBoundingBox();
    //
    //        assertEquals(25.0d, boundingBox.getMinX().getValue(), 0.01d);
    //        assertEquals(75.0d, boundingBox.getMaxX().getValue(), 0.01d);
    //        assertEquals(85.0d, boundingBox.getMinY().getValue(), 0.01d);
    //        assertEquals(115.0d, boundingBox.getMaxY().getValue(), 0.01d);
    //    }
}