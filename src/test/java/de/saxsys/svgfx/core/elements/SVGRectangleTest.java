/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

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

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(6);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.POSITION_X.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.POSITION_Y.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getQName(2)).thenReturn(SVGElementBase.CoreAttribute.WIDTH.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("25");
        Mockito.when(attributes.getQName(3)).thenReturn(SVGElementBase.CoreAttribute.HEIGHT.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("35");
        Mockito.when(attributes.getQName(4)).thenReturn(SVGElementBase.CoreAttribute.RADIUS_X.getName());
        Mockito.when(attributes.getValue(4)).thenReturn("5");
        Mockito.when(attributes.getQName(5)).thenReturn(SVGElementBase.CoreAttribute.RADIUS_Y.getName());
        Mockito.when(attributes.getValue(5)).thenReturn("10");

        SVGRectangle rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        Assert.assertEquals(50.0d, rectangle.getResult().getX(), 0.01d);
        Assert.assertEquals(100.0d, rectangle.getResult().getY(), 0.01d);
        Assert.assertEquals(25.0d, rectangle.getResult().getWidth(), 0.01d);
        Assert.assertEquals(35.0d, rectangle.getResult().getHeight(), 0.01d);
        Assert.assertEquals(10.0d, rectangle.getResult().getArcWidth(), 0.01d);
        Assert.assertEquals(20.0d, rectangle.getResult().getArcHeight(), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(6);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.POSITION_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.POSITION_Y.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(SVGElementBase.CoreAttribute.WIDTH.getName());
        Mockito.when(attributes.getQName(3)).thenReturn(SVGElementBase.CoreAttribute.HEIGHT.getName());
        Mockito.when(attributes.getQName(4)).thenReturn(SVGElementBase.CoreAttribute.RADIUS_X.getName());
        Mockito.when(attributes.getQName(5)).thenReturn(SVGElementBase.CoreAttribute.RADIUS_Y.getName());


        Mockito.when(attributes.getValue(0)).thenReturn("A");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("75");
        Mockito.when(attributes.getValue(3)).thenReturn("25");
        Mockito.when(attributes.getValue(4)).thenReturn("15");
        Mockito.when(attributes.getValue(5)).thenReturn("5");


        SVGRectangle rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("50");
        Mockito.when(attributes.getValue(1)).thenReturn("A");
        Mockito.when(attributes.getValue(2)).thenReturn("75");
        Mockito.when(attributes.getValue(3)).thenReturn("25");
        Mockito.when(attributes.getValue(4)).thenReturn("15");
        Mockito.when(attributes.getValue(5)).thenReturn("5");

        rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("A");
        Mockito.when(attributes.getValue(3)).thenReturn("25");
        Mockito.when(attributes.getValue(4)).thenReturn("15");
        Mockito.when(attributes.getValue(5)).thenReturn("5");

        rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("75");
        Mockito.when(attributes.getValue(3)).thenReturn("A");
        Mockito.when(attributes.getValue(4)).thenReturn("15");
        Mockito.when(attributes.getValue(5)).thenReturn("5");

        rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("75");
        Mockito.when(attributes.getValue(3)).thenReturn("25");
        Mockito.when(attributes.getValue(4)).thenReturn("A");
        Mockito.when(attributes.getValue(5)).thenReturn("5");

        rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("75");
        Mockito.when(attributes.getValue(3)).thenReturn("25");
        Mockito.when(attributes.getValue(4)).thenReturn("15");
        Mockito.when(attributes.getValue(5)).thenReturn("A");

        rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }
    }

    /**
     * Ensures that a {@link SVGException} is thrown if one of the required attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.WIDTH.getName());

        SVGRectangle rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.HEIGHT.getName());

        rectangle = new SVGRectangle("rect", attributes, null, new SVGDataProvider());

        try {
            rectangle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGRectangle.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }
    }
}
