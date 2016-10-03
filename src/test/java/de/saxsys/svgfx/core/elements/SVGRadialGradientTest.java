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
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg radial gradient elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGRadialGradientTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(5);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");

        final SVGRadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getValue(1)).thenReturn("red");

        gradient.getChildren().add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, gradient, new SVGDocumentDataProvider()));
        gradient.getChildren().add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, gradient, new SVGDocumentDataProvider()));

        Assert.assertEquals(0.1d, gradient.getResult().getCenterX(), 0.01d);
        Assert.assertEquals(0.15d, gradient.getResult().getCenterY(), 0.01d);
        Assert.assertEquals(Math.hypot(0.9d - 0.1d, 0.95d - 0.15d), gradient.getResult().getFocusDistance(), 0.01d);
        Assert.assertEquals(Math.atan2(0.95d - 0.15d, 0.9d - 0.1d), gradient.getResult().getFocusAngle(), 0.01d);
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test (expected = SVGException.class)
    public void ensureSVGExceptionIsThrownWhenStopsAreMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGRadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        gradient.getResult();
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there needed attributes are missing.
     */
    @Test (expected = SVGException.class)
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGRadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getValue(1)).thenReturn("red");

        gradient.getChildren().add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, gradient, new SVGDocumentDataProvider()));
        gradient.getChildren().add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, gradient, new SVGDocumentDataProvider()));

        gradient.getResult();
    }
}
