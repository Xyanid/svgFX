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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

/**
 * This test will ensure that svg style elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGStyleTest {

    /**
     * Ensures that one css style provided by {@link SVGStyle} is correct based on the input.
     */
    @Test
    public void ensureOneResultIsCreatedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        SVGStyle style = new SVGStyle("style", attributes, null, new SVGDataProvider());

        ((StringBuilder) Whitebox.getInternalState(style, "characters")).append("circle {fill:orange;stroke:black;stroke-width:10px;}");

        Assert.assertNotNull(style.getResult());
        Assert.assertEquals(1, style.getResult().size());

        SVGCssStyle result = style.getResult().iterator().next();

        Assert.assertEquals("circle", result.getName());
        Assert.assertEquals(3, result.getProperties().size());
        Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(Color.ORANGE,
                            result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(Color.BLACK,
                            result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(10.0d,
                            result.getAttributeTypeHolder()
                                  .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                  .getValue(),
                            0.01d);
    }

    /**
     * Ensures that the {@link de.saxsys.svgfx.css.definitions.Constants#PROPERTY_END} can be missing and end of a style
     */
    @Test
    public void ensureMissingPropertyEndStringsAreHandledCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        SVGStyle style = new SVGStyle("style", attributes, null, new SVGDataProvider());

        ((StringBuilder) Whitebox.getInternalState(style, "characters")).append("circle {fill:orange;stroke:black;stroke-width:10px}");

        Assert.assertNotNull(style.getResult());
        Assert.assertEquals(1, style.getResult().size());

        SVGCssStyle result = style.getResult().iterator().next();

        Assert.assertEquals("circle", result.getName());
        Assert.assertEquals(3, result.getProperties().size());
        Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(Color.ORANGE,
                            result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(Color.BLACK,
                            result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(10.0d,
                            result.getAttributeTypeHolder()
                                  .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                  .getValue(),
                            0.01d);
    }

    /**
     * Ensures that multiple css style provided by {@link SVGStyle} is correct based on the input.
     */
    @Test
    public void ensureMultipleResultAreCreatedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TYPE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn(SVGStyle.CSS_TYPE);

        SVGStyle style = new SVGStyle("style", attributes, null, new SVGDataProvider());

        StringBuilder builder = (StringBuilder) Whitebox.getInternalState(style, "characters");

        builder.append("st1{fill:orange;}");
        builder.append("st2{stroke:black;}");
        builder.append("st3{stroke-width:10px;}");

        Assert.assertNotNull(style.getResult());
        Assert.assertEquals(3, style.getResult().size());


        for (SVGCssStyle result : style.getResult()) {
            Assert.assertEquals(1, result.getProperties().size());

            switch (result.getName()) {
                case "st1":
                    Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
                    Assert.assertEquals(Color.ORANGE,
                                        result.getAttributeTypeHolder()
                                              .getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class)
                                              .getValue());
                    break;
                case "st2":
                    Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
                    Assert.assertEquals(Color.BLACK,
                                        result.getAttributeTypeHolder()
                                              .getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class)
                                              .getValue());
                    break;
                case "st3":

                    Assert.assertNotNull(result.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
                    Assert.assertEquals(10.0d,
                                        result.getAttributeTypeHolder()
                                              .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
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
