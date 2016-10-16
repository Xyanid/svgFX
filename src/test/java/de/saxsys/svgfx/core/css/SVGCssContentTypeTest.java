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
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeType;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeDashArray;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineJoin;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeType;
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
    public void checkSVGCssContentTypeFillRuleIsFullySupported() throws SVGException {

        final SVGAttributeTypeFillRule contentType = new SVGAttributeTypeFillRule(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        for (final Enumerations.FillRuleMapping value : Enumerations.FillRuleMapping.values()) {

            contentType.setText(value.getName());
            assertEquals(value.getRule(), contentType.getValue());
            assertNull(contentType.getUnit());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeLength} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeLengthIsFullySupported() throws SVGException {

        final SVGAttributeTypeLength contentType = new SVGAttributeTypeLength(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        final Random random = new Random();

        for (final SVGAttributeTypeLength.Unit unit : SVGAttributeTypeLength.Unit.values()) {
            final double value = random.nextDouble();

            contentType.setText(String.format("%f%s", value, unit.getName()));
            assertEquals(value, contentType.getValue(), 0.01d);
            assertEquals(unit, contentType.getUnit());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeDouble} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeDoubleIsFullySupported() throws SVGException {

        final SVGAttributeTypeDouble contentType = new SVGAttributeTypeDouble(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        contentType.setText("1.0");
        assertEquals(1.0d, contentType.getValue(), 0.01d);
        assertNull(contentType.getUnit());
    }

    /**
     * Checks if the {@link SVGAttributeTypePaint} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypePaintIsFullySupported() throws SVGException {

        final SVGAttributeTypePaint contentType = new SVGAttributeTypePaint(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        contentType.setText("currentColor");
        assertTrue(contentType.getIsCurrentColor());

        contentType.setText("rgb(255,32,96)");
        assertEquals(Color.web("rgb(255,32,96)"), contentType.getValue());

        contentType.setText("rgb(20%,30%,10%)");
        assertEquals(Color.web("rgb(20%,30%,10%)"), contentType.getValue());

        contentType.setText("red");
        assertEquals(Color.web("red"), contentType.getValue());
    }

    /**
     * Checks if the {@link SVGAttributeTypeString} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStringIsFullySupported() throws SVGException {

        final SVGAttributeTypeString contentType = new SVGAttributeTypeString(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        contentType.setText("random");
        assertEquals("random", contentType.getValue());
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeDashArray} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeDashArrayIsFullySupported() throws SVGException {

        final SVGAttributeTypeStrokeDashArray contentType = new SVGAttributeTypeStrokeDashArray(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
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

            contentType.setText(data.toString());

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
    public void checkSVGCssContentTypeStrokeLineCapIsFullySupported() throws SVGException {

        final SVGAttributeTypeStrokeLineCap contentType = new SVGAttributeTypeStrokeLineCap(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        for (final StrokeLineCap cap : StrokeLineCap.values()) {
            contentType.setText(cap.name().toLowerCase());
            assertEquals(cap, contentType.getValue());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeLineJoin} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineJoinIsFullySupported() throws SVGException {

        final SVGAttributeTypeStrokeLineJoin contentType = new SVGAttributeTypeStrokeLineJoin(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        for (final StrokeLineJoin join : StrokeLineJoin.values()) {
            contentType.setText(join.name().toLowerCase());
            assertEquals(join, contentType.getValue());
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeType} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeTypeIsFullySupported() throws SVGException {

        final SVGAttributeTypeStrokeType contentType = new SVGAttributeTypeStrokeType(new SVGDocumentDataProvider());

        contentType.setText("inherit");
        assertTrue(contentType.getIsInherited());

        contentType.setText("none");
        assertTrue(contentType.getIsNone());

        for (final StrokeType type : StrokeType.values()) {
            contentType.setText(type.name().toLowerCase());
            assertEquals(type, contentType.getValue());
        }
    }
}
