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

package de.saxsys.svgfx.core.css;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeType;
import de.saxsys.svgfx.core.content.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.content.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeDashArray;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeLineJoin;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeType;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGAttributeType}.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGCssStyleTest {

    //region Tests

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test
    public void parseCssTextToCreateCssStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#808080"));

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 10.0d, 0.01d);
    }

    /**
     * Ensures that styles read with property that are not inside the {@link PresentationAttributeMapper}s are still contained as {@link SVGAttributeTypeString}.
     */
    @Test
    public void ensureUnknownPropertyArePresentAsStrings() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;sumthing:else}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(5, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#808080"));

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 10.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute("sumthing"));
        Assert.assertEquals(SVGAttributeTypeString.class, style.getAttributeTypeHolder().getAttribute("sumthing").getClass());
        Assert.assertEquals("else", style.getAttributeTypeHolder().getAttribute("sumthing", SVGAttributeTypeString.class).getValue());
    }

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test
    public void parseCssTextToEnsureAllAttributesAreSupported() {

        StringBuilder cssText = new StringBuilder();
        cssText.append(".st0{");

        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            if (attribute == PresentationAttributeMapper.FILL_RULE) {
                cssText.append(String.format("%s:evenodd;", attribute.getName()));
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                cssText.append(String.format("%s:#808080;", attribute.getName()));
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                cssText.append(String.format("%s:10, 10, 10, 10;", attribute.getName()));
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                cssText.append(String.format("%s:round;", attribute.getName()));
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                cssText.append(String.format("%s:bevel;", attribute.getName()));
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                cssText.append(String.format("%s:outside;", attribute.getName()));
            } else if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                cssText.append(String.format("%s:random;", attribute.getName()));
            } else {
                cssText.append(String.format("%s:10;", attribute.getName()));
            }
        }

        cssText.append("}");

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(cssText.toString());

        Assert.assertEquals(style.getProperties().size(), PresentationAttributeMapper.VALUES.size());

        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(attribute.getName()));

            if (attribute == PresentationAttributeMapper.FILL_RULE) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypeFillRule.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeFillRule.class).getValue(),
                                    FillRule.EVEN_ODD);
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypePaint.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypePaint.class).getValue(),
                                    Color.web("#808080"));
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypeStrokeDashArray.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeDashArray.class).getValue().length, 4);
                for (SVGAttributeTypeLength length : style.getAttributeTypeHolder()
                                                          .getAttribute(attribute.getName(), SVGAttributeTypeStrokeDashArray.class)
                                                          .getValue()) {
                    Assert.assertEquals(length.getValue(), 10.0d, 0.01d);
                }
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypeStrokeLineCap.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeLineCap.class).getValue(),
                                    StrokeLineCap.ROUND);
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypeStrokeLineJoin.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeLineJoin.class).getValue(),
                                    StrokeLineJoin.BEVEL);
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypeStrokeType.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeType.class).getValue(),
                                    StrokeType.OUTSIDE);
            } else if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName()).getClass(), SVGAttributeTypeString.class);
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeString.class).getValue(), "random");
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHOFFSET || attribute == PresentationAttributeMapper.STROKE_WIDTH) {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeLength.class).getValue(), 10.0d, 0.01d);
            } else {
                Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(attribute.getName(), SVGAttributeTypeDouble.class).getValue(), 10.0d, 0.01d);
            }
        }
    }

    /**
     * Parses a css text which contains comments and also characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test
    public void parseCssTextWithCommentAndCssCharactersToCreateCssStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;/*this is a comment*/stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#808080"));

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 10.0d, 0.01d);

        style.parseCssText(".st0{fill:none;stroke:#080808;/*{\"this is ;:a string\";:}*/stroke-width:4;stroke-miterlimit:11;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#080808"));

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 11.0d, 0.01d);
    }

    /**
     * Parses a css text which contains string indicators inside a property and also contains characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test
    public void parseCssTextWithStringIndicatorsAndCssCharactersToCreateCssStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:\"#808080\";stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#808080"));

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 10.0d, 0.01d);

        style.parseCssText(".st0{fill:none;clip-rule:\";{ar;asd:j}:sda;asd:\";stroke-width:4;stroke-miterlimit:12;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.CLIP_RULE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.CLIP_RULE.getName()).getClass(),
                            SVGAttributeTypeString.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.CLIP_RULE.getName(), SVGAttributeTypeString.class).getValue(),
                            ";{ar;asd:j}:sda;asd:");

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 12.0d, 0.01d);
    }

    /**
     * Combines two css styles and checks if the contentMap have been overwritten as expected.
     */
    @Test
    public void combineCssStyles() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        SVGCssStyle otherStyle = new SVGCssStyle(new SVGDataProvider());

        otherStyle.parseCssText(".st0{fill-rule:evenodd;stroke-width:4;stroke-miterlimit:15;}");

        style.combineWithStyle(otherStyle);

        Assert.assertEquals(5, style.getProperties().size());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGAttributeTypePaint.class);
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#808080"));

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(),
                            SVGAttributeTypeLength.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(),
                            SVGAttributeTypeDouble.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                 .getValue(), 10.0d, 0.01d);

        Assert.assertNotNull(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL_RULE.getName()));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL_RULE.getName()).getClass(),
                            SVGAttributeTypeFillRule.class);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                 .getValue(), FillRule.EVEN_ODD);
    }

    //endregion
}
