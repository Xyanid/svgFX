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
import javafx.scene.shape.FillRule;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg path elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGPathTest {

    /**
     * Ensures that the path required for a line are parse correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.PATH_DESCRIPTION.getName());
        when(attributes.getValue(0)).thenReturn("M 100 100 L 300 100 L 200 300 z");

        final SVGPath line = new SVGPath(SVGPath.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        Assert.assertEquals("M 100 100 L 300 100 L 200 300 z", line.getResult().getContent());
    }

    /**
     * Ensures that the fill rule is parsed correctly.
     */
    @Test
    public void ensureFillRuleParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.PATH_DESCRIPTION.getName());
        when(attributes.getValue(0)).thenReturn("M 100 100 L 300 100 L 200 300 z");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.FILL_RULE.getName());
        when(attributes.getValue(1)).thenReturn("evenodd");

        final SVGPath line = new SVGPath(SVGPath.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        Assert.assertEquals(FillRule.EVEN_ODD, line.getResult().getFillRule());
    }

    /**
     * Ensures that no {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureNoSVGExceptionIfTheContentContainsInvalidData() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.PATH_DESCRIPTION.getName());
        when(attributes.getValue(0)).thenReturn("M =& 100 L 300 ?) 300 z");

        final SVGPath line = new SVGPath(SVGPath.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        try {
            line.getResult();
        } catch (final SAXException e) {
            fail();
        }
    }

    /**
     * Ensures that no {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void ensureNoSVGExceptionIsThrownWhenAttributesAreMissing() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGPath path = new SVGPath(SVGPath.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        path.getResult();
    }
}
