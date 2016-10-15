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
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");

        final SVGLinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getValue(1)).thenReturn("red");

        gradient.getUnmodifiableChildren().add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, gradient, new SVGDocumentDataProvider()));
        gradient.getUnmodifiableChildren().add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, gradient, new SVGDocumentDataProvider()));

        assertEquals(0.1d, gradient.getResult().getStartX(), 0.01d);
        assertEquals(0.15d, gradient.getResult().getStartY(), 0.01d);
        assertEquals(0.9d, gradient.getResult().getEndX(), 0.01d);
        assertEquals(0.95d, gradient.getResult().getEndY(), 0.01d);
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test (expected = SVGException.class)
    public void ensureSVGExceptionIsThrownWhenStopsAreMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGLinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        gradient.getResult();
    }
}
