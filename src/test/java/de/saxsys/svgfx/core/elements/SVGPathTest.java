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
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.shape.FillRule;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

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
    public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.PATH_DESCRIPTION.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("M 100 100 L 300 100 L 200 300 z");

        SVGPath line = new SVGPath("path", attributes, null, new SVGDataProvider());

        Assert.assertEquals("M 100 100 L 300 100 L 200 300 z", line.getResult().getContent());
    }

    /**
     * Ensures that the fill rule is parsed correctly.
     */
    @Test
    public void ensureFillRuleParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.PATH_DESCRIPTION.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("M 100 100 L 300 100 L 200 300 z");
        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.PresentationAttribute.FILL_RULE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("evenodd");

        SVGPath line = new SVGPath("path", attributes, null, new SVGDataProvider());

        Assert.assertEquals(FillRule.EVEN_ODD, line.getResult().getFillRule());
    }

    /**
     * Ensures that no {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureNoSVGExceptionIfTheContentContainsInvalidData() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.PATH_DESCRIPTION.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("M =& 100 L 300 ?) 300 z");

        SVGPath line = new SVGPath("path", attributes, null, new SVGDataProvider());

        try {
            line.getResult();
        } catch (SVGException e) {
            Assert.fail();
        }
    }

    /**
     * Ensures that no {@link SVGException} is thrown of one of the attributes is missing.
     */
    @Test
    public void ensureNoSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGPath path = new SVGPath("path", attributes, null, new SVGDataProvider());

        try {
            path.getResult();
        } catch (SVGException e) {
            Assert.fail();
        }
    }
}
