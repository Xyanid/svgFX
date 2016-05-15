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
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

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

        SVGAttributeTypeFillRule contentType = new SVGAttributeTypeFillRule(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (Enumerations.FillRuleMapping value : Enumerations.FillRuleMapping.values()) {

            contentType.consumeText(value.getName());
            Assert.assertEquals(contentType.getValue(), value.getRule());
            Assert.assertEquals(contentType.getUnit(), null);
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeLength} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeLengthIsFullySupported() {

        SVGAttributeTypeLength contentType = new SVGAttributeTypeLength(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        Random random = new Random();

        for (SVGAttributeTypeLength.Unit unit : SVGAttributeTypeLength.Unit.values()) {
            double value = random.nextDouble();

            contentType.consumeText(String.format("%f%s", value, unit.getName()));
            Assert.assertEquals(contentType.getValue(), value, 0.01d);
            Assert.assertEquals(contentType.getUnit(), unit);
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeDouble} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeDoubleIsFullySupported() {

        SVGAttributeTypeDouble contentType = new SVGAttributeTypeDouble(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.consumeText("1.0");
        Assert.assertEquals(1.0d, contentType.getValue(), 0.01d);
        Assert.assertNull(contentType.getUnit());
    }

    /**
     * Checks if the {@link SVGAttributeTypePaint} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypePaintIsFullySupported() {

        SVGAttributeTypePaint contentType = new SVGAttributeTypePaint(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.consumeText("currentColor");
        Assert.assertTrue(contentType.getIsCurrentColor());

        contentType.consumeText("rgb(255,32,96)");
        Assert.assertEquals(contentType.getValue(), Color.web("rgb(255,32,96)"));

        contentType.consumeText("rgb(20%,30%,10%)");
        Assert.assertEquals(contentType.getValue(), Color.web("rgb(20%,30%,10%)"));

        contentType.consumeText("red");
        Assert.assertEquals(contentType.getValue(), Color.web("red"));
    }

    /**
     * Checks if the {@link SVGAttributeTypeString} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStringIsFullySupported() {

        SVGAttributeTypeString contentType = new SVGAttributeTypeString(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.consumeText("random");
        Assert.assertEquals(contentType.getValue(), "random");
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeDashArray} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeDashArrayIsFullySupported() {

        SVGAttributeTypeStrokeDashArray contentType = new SVGAttributeTypeStrokeDashArray(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        Random random = new Random();

        StringBuilder data = new StringBuilder();

        for (SVGAttributeTypeLength.Unit unit : SVGAttributeTypeLength.Unit.values()) {
            double value = random.nextDouble();

            int counter = random.nextInt(10) + 1;

            data.setLength(0);

            while (counter-- > 0) {
                data.append(String.format("%s%s%s", data.length() == 0 ? "" : ",", String.format("%f", value).replace(",", "."), unit.getName()));
            }

            contentType.consumeText(data.toString());

            for (SVGAttributeTypeLength length : contentType.getValue()) {
                Assert.assertEquals(length.getValue(), value, 0.01d);
                Assert.assertEquals(length.getUnit(), unit);
            }

            for (Double valueDouble : contentType.getDashValues()) {
                Assert.assertEquals(valueDouble, value, 0.01d);
            }
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeLineCap} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineCapIsFullySupported() {

        SVGAttributeTypeStrokeLineCap contentType = new SVGAttributeTypeStrokeLineCap(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeLineCap cap : StrokeLineCap.values()) {
            contentType.consumeText(cap.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), cap);
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeLineJoin} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineJoinIsFullySupported() {

        SVGAttributeTypeStrokeLineJoin contentType = new SVGAttributeTypeStrokeLineJoin(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeLineJoin join : StrokeLineJoin.values()) {
            contentType.consumeText(join.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), join);
        }
    }

    /**
     * Checks if the {@link SVGAttributeTypeStrokeType} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeTypeIsFullySupported() {

        SVGAttributeTypeStrokeType contentType = new SVGAttributeTypeStrokeType(new SVGDataProvider());

        contentType.consumeText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.consumeText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeType type : StrokeType.values()) {
            contentType.consumeText(type.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), type);
        }
    }
}
