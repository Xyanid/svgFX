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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.elements.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

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
    public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("25");
        Mockito.when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("35");

        SVGLine line = new SVGLine("line", attributes, null, new SVGDataProvider());

        Assert.assertEquals(50.0d, line.getResult().getStartX(), 0.01d);
        Assert.assertEquals(100.0d, line.getResult().getStartY(), 0.01d);
        Assert.assertEquals(25.0d, line.getResult().getEndX(), 0.01d);
        Assert.assertEquals(35.0d, line.getResult().getEndY(), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        Mockito.when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("A");
        Mockito.when(attributes.getValue(1)).thenReturn("75");
        Mockito.when(attributes.getValue(2)).thenReturn("50");
        Mockito.when(attributes.getValue(3)).thenReturn("25");

        SVGLine circle = new SVGLine("line", attributes, null, new SVGDataProvider());

        TestUtils.assertExceptionContainsSVGElementName(circle, NumberFormatException.class);

        Mockito.when(attributes.getValue(0)).thenReturn("100");
        Mockito.when(attributes.getValue(1)).thenReturn("A");
        Mockito.when(attributes.getValue(2)).thenReturn("50");
        Mockito.when(attributes.getValue(3)).thenReturn("25");

        circle = new SVGLine("line", attributes, null, new SVGDataProvider());

        TestUtils.assertExceptionContainsSVGElementName(circle, NumberFormatException.class);

        Mockito.when(attributes.getValue(0)).thenReturn("100");
        Mockito.when(attributes.getValue(1)).thenReturn("75");
        Mockito.when(attributes.getValue(2)).thenReturn("A");
        Mockito.when(attributes.getValue(3)).thenReturn("25");

        circle = new SVGLine("line", attributes, null, new SVGDataProvider());

        TestUtils.assertExceptionContainsSVGElementName(circle, NumberFormatException.class);

        Mockito.when(attributes.getValue(0)).thenReturn("100");
        Mockito.when(attributes.getValue(1)).thenReturn("75");
        Mockito.when(attributes.getValue(2)).thenReturn("50");
        Mockito.when(attributes.getValue(3)).thenReturn("A");

        circle = new SVGLine("line", attributes, null, new SVGDataProvider());

        TestUtils.assertExceptionContainsSVGElementName(circle, NumberFormatException.class);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);
        Mockito.when(attributes.getValue(0)).thenReturn("75");
        Mockito.when(attributes.getValue(1)).thenReturn("50");
        Mockito.when(attributes.getValue(2)).thenReturn("25");

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());

        SVGLine line = new SVGLine("line", attributes, null, new SVGDataProvider());

        try {
            line.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGLine.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_Y.getName());

        line = new SVGLine("line", attributes, null, new SVGDataProvider());

        try {
            line.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGLine.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.END_X.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_Y.getName());

        line = new SVGLine("line", attributes, null, new SVGDataProvider());

        try {
            line.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGLine.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_Y.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.END_X.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_Y.getName());

        line = new SVGLine("line", attributes, null, new SVGDataProvider());

        try {
            line.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGLine.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }
    }
}
