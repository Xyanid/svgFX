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
import de.saxsys.svgfx.core.elements.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * This test will ensure that svg ellipse elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGEllipseTest {

    /**
     * Ensures that the attributes required for a ellipse are parse correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("25");
        Mockito.when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("35");

        SVGEllipse ellipse = new SVGEllipse("ellipse", attributes, null, new SVGDocumentDataProvider());

        Assert.assertEquals(50.0d, ellipse.getResult().getCenterX(), 0.01d);
        Assert.assertEquals(100.0d, ellipse.getResult().getCenterY(), 0.01d);
        Assert.assertEquals(25.0d, ellipse.getResult().getRadiusX(), 0.01d);
        Assert.assertEquals(35.0d, ellipse.getResult().getRadiusY(), 0.01d);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());
        Mockito.when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("A");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("25");
        Mockito.when(attributes.getValue(3)).thenReturn("15");

        TestUtils.assertCreationFails(SVGEllipse::new, "ellipse", attributes, null, new SVGDocumentDataProvider(), SVGEllipse.class, NumberFormatException.class);

        Mockito.when(attributes.getValue(0)).thenReturn("50");
        Mockito.when(attributes.getValue(1)).thenReturn("A");
        Mockito.when(attributes.getValue(2)).thenReturn("25");
        Mockito.when(attributes.getValue(3)).thenReturn("15");

        TestUtils.assertCreationFails(SVGEllipse::new, "ellipse", attributes, null, new SVGDocumentDataProvider(), SVGEllipse.class, NumberFormatException.class);

        Mockito.when(attributes.getValue(0)).thenReturn("50");
        Mockito.when(attributes.getValue(1)).thenReturn("100");
        Mockito.when(attributes.getValue(2)).thenReturn("A");
        Mockito.when(attributes.getValue(3)).thenReturn("15");

        TestUtils.assertCreationFails(SVGEllipse::new, "ellipse", attributes, null, new SVGDocumentDataProvider(), SVGEllipse.class, NumberFormatException.class);

        Mockito.when(attributes.getValue(0)).thenReturn("50");
        Mockito.when(attributes.getValue(1)).thenReturn("100");
        Mockito.when(attributes.getValue(2)).thenReturn("25");
        Mockito.when(attributes.getValue(3)).thenReturn("A");

        TestUtils.assertCreationFails(SVGEllipse::new, "ellipse", attributes, null, new SVGDocumentDataProvider(), SVGEllipse.class, NumberFormatException.class);
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS_X.getName());

        SVGEllipse ellipse = new SVGEllipse("ellipse", attributes, null, new SVGDocumentDataProvider());

        TestUtils.assertResultFails(ellipse, NullPointerException.class);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS_Y.getName());

        ellipse = new SVGEllipse("ellipse", attributes, null, new SVGDocumentDataProvider());

        TestUtils.assertResultFails(ellipse, NullPointerException.class);
    }
}