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
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * This test will ensure that svg linear gradient elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGLinearGradientTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("0.15");
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("0.9");
        Mockito.when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("0.95");

        SVGLinearGradient gradient = new SVGLinearGradient("stop", attributes, null, new SVGDataProvider());

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getValue(1)).thenReturn("red");

        gradient.getChildren().add(new SVGStop("stop", attributes, gradient, new SVGDataProvider()));
        gradient.getChildren().add(new SVGStop("stop", attributes, gradient, new SVGDataProvider()));

        Assert.assertEquals(0.1d, gradient.getResult().getStartX(), 0.01d);
        Assert.assertEquals(0.15d, gradient.getResult().getStartY(), 0.01d);
        Assert.assertEquals(0.9d, gradient.getResult().getEndX(), 0.01d);
        Assert.assertEquals(0.95d, gradient.getResult().getEndY(), 0.01d);
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test(expected = SVGException.class)
    public void ensureSVGExceptionIsThrownWhenStopsAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGLinearGradient gradient = new SVGLinearGradient("stop", attributes, null, new SVGDataProvider());

        gradient.getResult();
    }
}
