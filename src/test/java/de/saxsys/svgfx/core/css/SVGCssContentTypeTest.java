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
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGAttributeType}.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGCssContentTypeTest {

    /**
     * Checks if the {@link SVGAttributeTypeFillRule} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeFillRuleIsFullySupported() {

        final SVGAttributeTypeFillRule contentType = new SVGAttributeTypeFillRule(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        for (final Enumerations.FillRuleMapping value : Enumerations.FillRuleMapping.values()) {

            contentType.consumeText(value.getName());
            assertEquals(value.getRule(), contentType.getValue());
            assertNull(contentType.getUnit());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeLength} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeLengthIsFullySupported() {

        final SVGAttributeTypeLength contentType = new SVGAttributeTypeLength(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        final Random random = new Random();

        for (final SVGAttributeTypeLength.Unit unit : SVGAttributeTypeLength.Unit.values()) {
            final double value = random.nextDouble();

            contentType.consumeText(String.format("%f%s", value, unit.getName()));
            assertEquals(value, contentType.getValue(), 0.01d);
            assertEquals(unit, contentType.getUnit());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeDouble} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeDoubleIsFullySupported() {

        final SVGAttributeTypeDouble contentType = new SVGAttributeTypeDouble(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        contentType.consumeText("1.0");
        assertEquals(1.0d, contentType.getValue(), 0.01d);
        assertNull(contentType.getUnit());
    }

    /**
     * Checks if the {@link SVGAttributeTypePaint} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypePaintIsFullySupported() {

        final SVGAttributeTypePaint contentType = new SVGAttributeTypePaint(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        contentType.consumeText("currentColor");
        assertTrue(contentType.getIsCurrentColor());

        contentType.consumeText("rgb(255,32,96)");
        assertEquals(Color.web("rgb(255,32,96)"), contentType.getValue());

        contentType.consumeText("rgb(20%,30%,10%)");
        assertEquals(Color.web("rgb(20%,30%,10%)"), contentType.getValue());

        contentType.consumeText("red");
        assertEquals(Color.web("red"), contentType.getValue());
    }

    /**
     * Checks if the {@link SVGAttributeTypeString} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStringIsFullySupported() {

        final SVGAttributeTypeString contentType = new SVGAttributeTypeString(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        contentType.consumeText("random");
        assertEquals("random", contentType.getValue());
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeDashArray} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeDashArrayIsFullySupported() {

        final SVGAttributeTypeStrokeDashArray contentType = new SVGAttributeTypeStrokeDashArray(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        final Random random = new Random();

        final StringBuilder data = new StringBuilder();

        for (final SVGAttributeTypeLength.Unit unit : SVGAttributeTypeLength.Unit.values()) {
            final double value = random.nextDouble();

            int counter = random.nextInt(10) + 1;

            data.setLength(0);

            while (counter-- > 0) {
                data.append(String.format("%s%s%s", data.length() == 0 ? "" : ",", String.format("%f", value).replace(",", "."), unit.getName()));
            }

            contentType.consumeText(data.toString());

            for (final SVGAttributeTypeLength length : contentType.getValue()) {
                assertEquals(value, length.getValue(), 0.01d);
                assertEquals(unit, length.getUnit());
            }

            for (final Double valueDouble : contentType.getDashValues()) {
                assertEquals(value, valueDouble, 0.01d);
            }
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeLineCap} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineCapIsFullySupported() {

        final SVGAttributeTypeStrokeLineCap contentType = new SVGAttributeTypeStrokeLineCap(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        for (final StrokeLineCap cap : StrokeLineCap.values()) {
            contentType.consumeText(cap.name().toLowerCase());
            assertEquals(cap, contentType.getValue());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeLineJoin} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineJoinIsFullySupported() {

        final SVGAttributeTypeStrokeLineJoin contentType = new SVGAttributeTypeStrokeLineJoin(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        for (final StrokeLineJoin join : StrokeLineJoin.values()) {
            contentType.consumeText(join.name().toLowerCase());
            assertEquals(join, contentType.getValue());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeType} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeTypeIsFullySupported() {

        final SVGAttributeTypeStrokeType contentType = new SVGAttributeTypeStrokeType(new SVGDocumentDataProvider());

        contentType.consumeText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        assertTrue(contentType.getIsNone());

        for (final StrokeType type : StrokeType.values()) {
            contentType.consumeText(type.name().toLowerCase());
            assertEquals(type, contentType.getValue());
        }
    }
}
