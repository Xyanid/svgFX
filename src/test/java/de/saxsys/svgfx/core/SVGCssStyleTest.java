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

package de.saxsys.svgfx.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGCssContentTypeBase}.
 * Created by Xyanid on 05.10.2015.
 */
public final class SVGCssStyleTest {


    //region Tests

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test public void parseCssTextToCreateCssStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getUnmodifiableProperties().size());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName(), SVGCssContentTypeLength.class).getValue(), 10.0d, 0.01d);
    }

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test public void parseCssTextToEnsureAllAttributesAreSupported() {

        StringBuilder cssText = new StringBuilder();
        cssText.append(".st0{");

        for (SVGCssStyle.PresentationAttribute attribute : SVGCssStyle.PresentationAttribute.values()) {

            if (attribute.getContentTypeClass().equals(SVGCssContentTypePaint.class)) {
                cssText.append(String.format("%s:#808080;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeLength.class)) {
                cssText.append(String.format("%s:10;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeFillRule.class)) {
                cssText.append(String.format("%s:evenodd;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeDashArray.class)) {
                cssText.append(String.format("%s:10, 10, 10, 10;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeLineCap.class)) {
                cssText.append(String.format("%s:round;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeLineJoin.class)) {
                cssText.append(String.format("%s:bevel;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeType.class)) {
                cssText.append(String.format("%s:outside;", attribute.getName()));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeString.class)) {
                cssText.append(String.format("%s:random;", attribute.getName()));
            }
        }

        cssText.append("}");

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(cssText.toString());

        Assert.assertEquals(style.getUnmodifiableProperties().size(), SVGCssStyle.PresentationAttribute.values().length);

        for (SVGCssStyle.PresentationAttribute attribute : SVGCssStyle.PresentationAttribute.values()) {

            Assert.assertNotNull(style.getCssContentType(attribute.getName()));
            Assert.assertEquals(style.getCssContentType(attribute.getName()).getClass(), attribute.getContentTypeClass());

            if (attribute.getContentTypeClass().equals(SVGCssContentTypePaint.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#808080"));
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeLength.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeLength.class).getValue(), 10.0d, 0.01d);
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeFillRule.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeFillRule.class).getValue(), FillRule.EVEN_ODD);
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeDashArray.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeStrokeDashArray.class).getValue().length, 4);
                for (SVGCssContentTypeLength length : style.getCssContentType(attribute.getName(), SVGCssContentTypeStrokeDashArray.class).getValue()) {
                    Assert.assertEquals(length.getValue(), 10.0d, 0.01d);
                }
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeLineCap.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeStrokeLineCap.class).getValue(), StrokeLineCap.ROUND);
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeLineJoin.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeStrokeLineJoin.class).getValue(), StrokeLineJoin.BEVEL);
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeStrokeType.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeStrokeType.class).getValue(), StrokeType.OUTSIDE);
            } else if (attribute.getContentTypeClass().equals(SVGCssContentTypeString.class)) {
                Assert.assertEquals(style.getCssContentType(attribute.getName(), SVGCssContentTypeString.class).getValue(), "random");
            }
        }
    }

    /**
     * Parses a css text which contains comments and also characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test public void parseCssTextWithCommentAndCssCharactersToCreateCssStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;/*this is a comment*/stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getUnmodifiableProperties().size());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName(), SVGCssContentTypeLength.class).getValue(), 10.0d, 0.01d);

        style.parseCssText(".st0{fill:none;stroke:#080808;/*{\"this is ;:a string\";:}*/stroke-width:4;stroke-miterlimit:11;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getUnmodifiableProperties().size());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#080808"));

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName(), SVGCssContentTypeLength.class).getValue(), 11.0d, 0.01d);
    }

    /**
     * Parses a css text which contains string indicators inside a property and also contains characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test public void parseCssTextWithStringIndicatorsAndCssCharactersToCreateCssStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:\"#808080\";stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getUnmodifiableProperties().size());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 3.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName(), SVGCssContentTypeLength.class).getValue(), 10.0d, 0.01d);

        style.parseCssText(".st0{fill:none;clip-rule:\";{ar;asd:j}:sda;asd:\";stroke-width:4;stroke-miterlimit:12;}");

        Assert.assertEquals("st0", style.getName());
        Assert.assertEquals(4, style.getUnmodifiableProperties().size());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.CLIP_RULE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.CLIP_RULE.getName()).getClass(), SVGCssContentTypeString.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.CLIP_RULE.getName(), SVGCssContentTypeString.class).getValue(), ";{ar;asd:j}:sda;asd:");

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName(), SVGCssContentTypeLength.class).getValue(), 12.0d, 0.01d);
    }

    /**
     * Combines two css styles and checks if the properties have been overwritten as expected.
     */
    @Test public void combineCssStyles() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        SVGCssStyle otherStyle = new SVGCssStyle(new SVGDataProvider());

        otherStyle.parseCssText(".st0{fill-rule:evenodd;stroke-width:4;stroke-miterlimit:15;}");

        style.combineWithStyle(otherStyle);

        Assert.assertEquals(5, style.getUnmodifiableProperties().size());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName()).getClass(), SVGCssContentTypePaint.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#808080"));

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 4.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName()).getClass(), SVGCssContentTypeLength.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_MITERLIMIT.getName(), SVGCssContentTypeLength.class).getValue(), 15.0d, 0.01d);

        Assert.assertNotNull(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName()));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName()).getClass(), SVGCssContentTypeFillRule.class);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName(), SVGCssContentTypeFillRule.class).getValue(), FillRule.EVEN_ODD);
    }

    //endregion
}
