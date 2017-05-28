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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static de.saxsys.svgfx.core.utils.TestUtils.assertResultFails;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
    public void allAttributesAreParsedCorrectly() throws SAXException {

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

        final SVGRectangle rectangle = new SVGRectangle(SVGRectangle.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        assertEquals(50.0d, rectangle.getResult().getX(), MINIMUM_DEVIATION);
        assertEquals(100.0d, rectangle.getResult().getY(), MINIMUM_DEVIATION);
        assertEquals(25.0d, rectangle.getResult().getWidth(), MINIMUM_DEVIATION);
        assertEquals(35.0d, rectangle.getResult().getHeight(), MINIMUM_DEVIATION);
        assertEquals(10.0d, rectangle.getResult().getArcWidth(), MINIMUM_DEVIATION);
        assertEquals(20.0d, rectangle.getResult().getArcHeight(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void whenAnyAttributeIsInvalidAnSVGExceptionIsThrownDuringTheCreatingOfTheResult() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POSITION_X.getName());
        when(attributes.getValue(0)).thenReturn("A");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.POSITION_Y.getName());
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.WIDTH.getName());
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.HEIGHT.getName());
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());
        when(attributes.getValue(5)).thenReturn("5");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getValue(1)).thenReturn("A");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("A");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("A");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("5");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("A");
        when(attributes.getValue(5)).thenReturn("5");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getValue(0)).thenReturn("50.0");
        when(attributes.getValue(1)).thenReturn("100.0");
        when(attributes.getValue(2)).thenReturn("75");
        when(attributes.getValue(3)).thenReturn("25");
        when(attributes.getValue(4)).thenReturn("15");
        when(attributes.getValue(5)).thenReturn("A");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));
    }

    /**
     * Ensures that a {@link SVGException} is thrown if one of the required attributes is missing.
     */
    @Test
    public void whenAnyAttributeIsMissingAnSVGExceptionIsThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.WIDTH.getName());
        when(attributes.getValue(0)).thenReturn("50.0");

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.HEIGHT.getName());

        assertResultFails(SVGRectangle::new,
                          SVGRectangle.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));
    }

    /**
     * The bounding rectangle described by the shape can be correctly determined.
     */
    @Test
    public void theBoundingBoxCanBeDeterminedCorrectly() throws SVGException {
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

        final SVGRectangle rectangle = new SVGRectangle(SVGEllipse.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = rectangle.createBoundingBox(null);

        assertEquals(50.0d, boundingBox.getMinX().getValue(), MINIMUM_DEVIATION);
        assertEquals(75.0d, boundingBox.getMaxX().getValue(), MINIMUM_DEVIATION);
        assertEquals(100.0d, boundingBox.getMinY().getValue(), MINIMUM_DEVIATION);
        assertEquals(135.0d, boundingBox.getMaxY().getValue(), MINIMUM_DEVIATION);
    }
}
