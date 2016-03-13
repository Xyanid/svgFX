/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.core.css;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGContentTypeBase;
import de.saxsys.svgfx.core.content.SVGContentTypeDouble;
import de.saxsys.svgfx.core.content.SVGContentTypeFillRule;
import de.saxsys.svgfx.core.content.SVGContentTypeLength;
import de.saxsys.svgfx.core.content.SVGContentTypePaint;
import de.saxsys.svgfx.core.content.SVGContentTypeString;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeDashArray;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeLineCap;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeLineJoin;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeType;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGContentTypeBase}.
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

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            10.0d,
                            0.01d);
    }

    /**
     * Ensures that styles read with property that are not inside the {@link PresentationAttributeMapper}s are still contained as {@link SVGContentTypeString}.
     */
    @Test
    public void ensureUnknownPropertyArePresentAsStrings() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;sumthing:else}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(5, style.getProperties().size());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            10.0d,
                            0.01d);

        Assert.assertNotNull(style.getCssContentType("sumthing"));
        Assert.assertEquals(SVGContentTypeString.class, style.getCssContentType("sumthing").getClass());
        Assert.assertEquals("else", style.getCssContentType("sumthing", SVGContentTypeString.class).getValue());
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

            Assert.assertNotNull(style.getCssContentType(attribute.getName()));

            if (attribute == PresentationAttributeMapper.FILL_RULE) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypeFillRule.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeFillRule.class).getValue(), FillRule.EVEN_ODD);
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypePaint.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypePaint.class).getValue(), Color.web("#808080"));
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypeStrokeDashArray.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeStrokeDashArray.class).getValue().length, 4);
                for (SVGContentTypeLength length : style.getCssContentType(attribute.getName(), SVGContentTypeStrokeDashArray.class).getValue()) {
                    Assert.assertEquals(length.getValue(), 10.0d, 0.01d);
                }
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypeStrokeLineCap.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeStrokeLineCap.class).getValue(), StrokeLineCap.ROUND);
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypeStrokeLineJoin.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeStrokeLineJoin.class).getValue(), StrokeLineJoin.BEVEL);
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypeStrokeType.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeStrokeType.class).getValue(), StrokeType.OUTSIDE);
            } else if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), SVGContentTypeString.class);
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeString.class).getValue(), "random");
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHOFFSET || attribute == PresentationAttributeMapper.STROKE_WIDTH) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeLength.class).getValue(), 10.0d, 0.01d);
            } else {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGContentTypeDouble.class).getValue(), 10.0d, 0.01d);
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

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            10.0d,
                            0.01d);

        style.parseCssText(".st0{fill:none;stroke:#080808;/*{\"this is ;:a string\";:}*/stroke-width:4;stroke-miterlimit:11;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue(), Color.web("#080808"));

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            11.0d,
                            0.01d);
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

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            10.0d,
                            0.01d);

        style.parseCssText(".st0{fill:none;clip-rule:\";{ar;asd:j}:sda;asd:\";stroke-width:4;stroke-miterlimit:12;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getProperties().size());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.CLIP_RULE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.CLIP_RULE.getName()).getClass(), SVGContentTypeString.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.CLIP_RULE.getName(), SVGContentTypeString.class).getValue(),
                            ";{ar;asd:j}:sda;asd:");

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            12.0d,
                            0.01d);
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

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName()).getClass(), SVGContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName()).getClass(), SVGContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).getClass(), SVGContentTypeDouble.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGContentTypeDouble.class).getValue(),
                            10.0d,
                            0.01d);

        Assert.assertNotNull(style.getCssContentType(PresentationAttributeMapper.FILL_RULE.getName()));
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL_RULE.getName()).getClass(), SVGContentTypeFillRule.class);
        Assert.assertEquals(style.getCssContentType(PresentationAttributeMapper.FILL_RULE.getName(), SVGContentTypeFillRule.class).getValue(),
                            FillRule.EVEN_ODD);
    }

    //endregion
}
