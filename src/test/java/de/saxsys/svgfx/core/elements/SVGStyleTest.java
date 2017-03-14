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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg style elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("OptionalGetWithoutIsPresent")
public final class SVGStyleTest {

    /**
     * Ensures that one css style provided by {@link SVGStyle} is correct based on the input.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException, SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        final SVGStyle style = new SVGStyle(SVGStyle.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        ((StringBuilder) Whitebox.getInternalState(style, "characters")).append("circle {fill:orange;stroke:black;stroke-width:10px;}");

        assertNotNull(style.getResult());
        assertEquals(1, style.getResult().size());

        final SVGCssStyle result = style.getResult().iterator().next();

        assertEquals("circle", result.getName());
        assertEquals(3, result.getProperties().size());
        assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertEquals(Color.ORANGE, result.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertEquals(Color.BLACK, result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertEquals(10.0d,
                     result.getAttributeHolder()
                           .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                           .get()
                           .getValue(),
                     0.01d);
    }

    /**
     * SVGStyle elements will not remain in the DOM tree since they are not needed, instead they will save all their data into the dataprovider.
     */
    @Test
    public void SVGStylesWillNotBeRemembered() throws SAXException {
        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        final SVGStyle style = new SVGStyle(SVGStyle.ELEMENT_NAME, attributes, dataProvider);
        ((StringBuilder) Whitebox.getInternalState(style, "characters")).append("circle {fill:orange;stroke:black;stroke-width:10px;}");

        assertFalse(style.keepElement());

        style.endProcessing();

        assertEquals(1, dataProvider.getUnmodifiableStyles().size());
        assertTrue(dataProvider.getUnmodifiableStyles().containsAll(style.getResult()));
    }

    /**
     * Ensures that the {@link de.saxsys.svgfx.css.definitions.Constants#PROPERTY_END} can be missing at the end of a style
     */
    @Test
    public void missingPropertyEndStringsAreHandledCorrectly() throws SAXException, SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        final SVGStyle style = new SVGStyle(SVGStyle.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        ((StringBuilder) Whitebox.getInternalState(style, "characters")).append("circle {fill:orange;stroke:black;stroke-width:10px}");

        assertNotNull(style.getResult());
        assertEquals(1, style.getResult().size());

        final SVGCssStyle result = style.getResult().iterator().next();

        assertEquals("circle", result.getName());
        assertEquals(3, result.getProperties().size());
        assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertEquals(Color.ORANGE,
                     result.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertEquals(Color.BLACK,
                     result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertEquals(10.0d,
                     result.getAttributeHolder()
                           .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                           .get().getValue(),
                     0.01d);
    }

    /**
     * Ensures that multiple css style provided by {@link SVGStyle} is correct based on the input.
     */
    @SuppressWarnings ("MismatchedQueryAndUpdateOfStringBuilder")
    @Test
    public void multipleResultAreCreatedCorrectly() throws SAXException, SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        final SVGStyle style = new SVGStyle(SVGStyle.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        final StringBuilder builder = (StringBuilder) Whitebox.getInternalState(style, "characters");

        builder.append("st1{fill:orange;}");
        builder.append("st2{stroke:black;}");
        builder.append("st3{stroke-width:10px;}");

        assertNotNull(style.getResult());
        assertEquals(3, style.getResult().size());


        for (final SVGCssStyle result : style.getResult()) {
            assertEquals(1, result.getProperties().size());

            switch (result.getName()) {
                case "st1":
                    assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
                    assertEquals(Color.ORANGE,
                                 result.getAttributeHolder()
                                       .getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class)
                                       .get()
                                       .getValue());
                    break;
                case "st2":
                    assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
                    assertEquals(Color.BLACK,
                                 result.getAttributeHolder()
                                       .getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class)
                                       .get()
                                       .getValue());
                    break;
                case "st3":

                    assertNotNull(result.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
                    assertEquals(10.0d,
                                 result.getAttributeHolder()
                                       .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                       .get()
                                       .getValue(),
                                 0.01d);
                    break;
                default:
                    Assert.fail();
                    break;
            }
        }
    }
}
