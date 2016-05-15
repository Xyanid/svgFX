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

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.elements.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * This test will ensure that svg polygon elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGPolygonTest {

    /**
     * Ensures that the polygon required for a line are parse correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100,40 100,80");

        SVGPolygon polygon = new SVGPolygon("polygon", attributes, null, new SVGDataProvider());

        Assert.assertEquals(6, polygon.getResult().getPoints().size());
        Assert.assertEquals(60.0d, polygon.getResult().getPoints().get(0), 0.01d);
        Assert.assertEquals(20.0d, polygon.getResult().getPoints().get(1), 0.01d);
        Assert.assertEquals(100.0d, polygon.getResult().getPoints().get(2), 0.01d);
        Assert.assertEquals(40.0d, polygon.getResult().getPoints().get(3), 0.01d);
        Assert.assertEquals(100.0d, polygon.getResult().getPoints().get(4), 0.01d);
        Assert.assertEquals(80.0d, polygon.getResult().getPoints().get(5), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIfTheContentContainsInvalidData() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100,A 100,80");

        TestUtils.assertCreationFails(SVGPolygon::new, "polygon", attributes, null, new SVGDataProvider(), SVGPolygon.class, NumberFormatException.class);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100 100,80");

        TestUtils.assertCreationFails(SVGPolygon::new, "polygon", attributes, null, new SVGDataProvider(), SVGPolygon.class, SVGException.class);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void ensureNoSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGPolygon polygon = new SVGPolygon("polygon", attributes, null, new SVGDataProvider());

        try {
            Assert.assertEquals(0, polygon.getResult().getPoints().size());
        } catch (SVGException e) {
            Assert.fail();
        }
    }
}
