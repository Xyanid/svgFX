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

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
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
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGAttributeType}.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("OptionalGetWithoutIsPresent")
public final class SVGCssStyleTest {

    //region Tests

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test
    public void parseCssTextToCreateCssStyle() {

        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        assertEquals("st0", style.getName());
        assertEquals(4, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(3.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(10.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);
    }

    /**
     * Ensures that styles read with property that are not inside the {@link PresentationAttributeMapper}s are still contained as {@link SVGAttributeTypeString}.
     */
    @Test
    public void ensureUnknownPropertyArePresentAsStrings() {

        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;sumthing:else}");

        assertEquals("st0", style.getName());
        assertEquals(5, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(3.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(10.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute("sumthing"));
        assertThat(style.getAttributeHolder().getAttribute("sumthing").get(), instanceOf(SVGAttributeTypeString.class));
        assertEquals("else", style.getAttributeHolder().getAttribute("sumthing", SVGAttributeTypeString.class).get().getValue());
    }

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test
    public void parseCssTextToEnsureAllAttributesAreSupported() {

        final StringBuilder cssText = new StringBuilder();
        cssText.append(".st0{");

        for (final PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

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

        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(cssText.toString());

        assertEquals(PresentationAttributeMapper.VALUES.size(), style.getProperties().size());

        for (final PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            assertNotNull(style.getAttributeHolder().getAttribute(attribute.getName()));

            if (attribute == PresentationAttributeMapper.FILL_RULE) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypeFillRule.class));
                assertEquals(FillRule.EVEN_ODD, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeFillRule.class).get().getValue());
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
                assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypePaint.class).get().getValue());
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypeStrokeDashArray.class));
                assertEquals(4, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeDashArray.class).get().getValue().length);
                for (final SVGAttributeTypeLength length : style.getAttributeHolder()
                                                                .getAttribute(attribute.getName(), SVGAttributeTypeStrokeDashArray.class)
                                                                .get()
                                                                .getValue()) {
                    assertEquals(10.0d, length.getValue(), 0.01d);
                }
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypeStrokeLineCap.class));
                assertEquals(StrokeLineCap.ROUND, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeLineCap.class).get().getValue());
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypeStrokeLineJoin.class));
                assertEquals(StrokeLineJoin.BEVEL, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeLineJoin.class).get().getValue());
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypeStrokeType.class));
                assertEquals(StrokeType.OUTSIDE, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeStrokeType.class).get().getValue());
            } else if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                assertThat(style.getAttributeHolder().getAttribute(attribute.getName()).get(), instanceOf(SVGAttributeTypeString.class));
                assertEquals("random", style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeString.class).get().getValue());
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHOFFSET || attribute == PresentationAttributeMapper.STROKE_WIDTH) {
                assertEquals(10.0d, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeLength.class).get().getValue(), 0.01d);
            } else {
                assertEquals(10.0d, style.getAttributeHolder().getAttribute(attribute.getName(), SVGAttributeTypeDouble.class).get().getValue(), 0.01d);
            }
        }
    }

    /**
     * Parses a css text which contains comments and also characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test
    public void parseCssTextWithCommentAndCssCharactersToCreateCssStyle() {

        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;/*this is a comment*/stroke-width:3;stroke-miterlimit:10;}");

        assertEquals("st0", style.getName());
        assertEquals(4, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(3.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(10.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);

        style.parseCssText(".st0{fill:none;stroke:#080808;/*{\"this is ;:a string\";:}*/stroke-width:4;stroke-miterlimit:11;}");

        assertEquals("st0", style.getName());
        assertEquals(4, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertEquals(Color.web("#080808"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(4.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(11.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);
    }

    /**
     * Parses a css text which contains string indicators inside a property and also contains characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test
    public void parseCssTextWithStringIndicatorsAndCssCharactersToCreateCssStyle() {

        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st0{fill:none;stroke:\"#808080\";stroke-width:3;stroke-miterlimit:10;}");

        assertEquals("st0", style.getName());
        assertEquals(4, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(3.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(10.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);

        style.parseCssText(".st0{fill:none;clip-rule:\";{ar;asd:j}:sda;asd:\";stroke-width:4;stroke-miterlimit:12;}");

        assertEquals("st0", style.getName());
        assertEquals(4, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_RULE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_RULE.getName()).get(), instanceOf(SVGAttributeTypeString.class));
        assertEquals(";{ar;asd:j}:sda;asd:", style.getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_RULE.getName(), SVGAttributeTypeString.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(4.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(12.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);
    }

    /**
     * Combines two css styles and checks if the contentMap have been overwritten as expected.
     */
    @Test
    public void combineCssStyles() {

        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        final SVGCssStyle otherStyle = new SVGCssStyle(new SVGDocumentDataProvider());

        otherStyle.parseCssText(".st0{fill-rule:evenodd;stroke-width:4;stroke-miterlimit:15;}");

        style.combineWithStyle(otherStyle);

        assertEquals(5, style.getProperties().size());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName()).get(), instanceOf(SVGAttributeTypePaint.class));
        assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName()).get(), instanceOf(SVGAttributeTypeLength.class));
        assertEquals(3.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName()).get(), instanceOf(SVGAttributeTypeDouble.class));
        assertEquals(10.0d,
                     style.getAttributeHolder()
                          .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                          .get()
                          .getValue(),
                     0.01d);

        assertNotNull(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL_RULE.getName()));
        assertThat(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL_RULE.getName()).get(), instanceOf(SVGAttributeTypeFillRule.class));
        assertEquals(FillRule.EVEN_ODD, style.getAttributeHolder()
                                             .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                             .get()
                                             .getValue());
    }

    //endregion
}
