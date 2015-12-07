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

import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGCssContentTypeBase}.
 * Created by Xyanid on 05.10.2015.
 */
public final class SVGCssContentTypeTest {

    //region Tests

    /**
     * Checks if the {@link SVGCssContentTypeFillRule} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeFillRuleIsFullySupported() {

        SVGCssContentTypeFillRule contentType = new SVGCssContentTypeFillRule(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        for (Enumerations.FillRuleMapping value : Enumerations.FillRuleMapping.values()) {

            contentType.parseCssValue(value.getName());
            Assert.assertEquals(contentType.getValue(), value.getRule());
            Assert.assertEquals(contentType.getUnit(), null);
        }
    }

    /**
     * Checks if the {@link SVGCssContentTypeLength} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeLengthIsFullySupported() {

        SVGCssContentTypeLength contentType = new SVGCssContentTypeLength(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        Random random = new Random();

        for (SVGCssContentTypeLength.Unit unit : SVGCssContentTypeLength.Unit.values()) {
            double value = random.nextDouble();

            contentType.parseCssValue(String.format("%f%s", value, unit.getName()));
            Assert.assertEquals(contentType.getValue(), value, 0.01d);
            Assert.assertEquals(contentType.getUnit(), unit);
        }
    }

    /**
     * Checks if the {@link SVGCssContentTypePaint} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypePaintIsFullySupported() {

        SVGCssContentTypePaint contentType = new SVGCssContentTypePaint(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.parseCssValue("currentColor");
        Assert.assertTrue(contentType.getIsCurrentColor());

        contentType.parseCssValue("rgb(255,32,96)");
        Assert.assertEquals(contentType.getValue(), Color.web("rgb(255,32,96)"));

        contentType.parseCssValue("rgb(20%,30%,10%)");
        Assert.assertEquals(contentType.getValue(), Color.web("rgb(20%,30%,10%)"));

        contentType.parseCssValue("red");
        Assert.assertEquals(contentType.getValue(), Color.web("red"));
    }

    /**
     * Checks if the {@link SVGCssContentTypeString} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeStringIsFullySupported() {

        SVGCssContentTypeString contentType = new SVGCssContentTypeString(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.parseCssValue("random");
        Assert.assertEquals(contentType.getValue(), "random");
    }

    /**
     * Checks if the {@link SVGCssContentTypeStrokeDashArray} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeStrokeDashArrayIsFullySupported() {

        SVGCssContentTypeStrokeDashArray contentType = new SVGCssContentTypeStrokeDashArray(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        Random random = new Random();

        StringBuilder data = new StringBuilder();

        for (SVGCssContentTypeLength.Unit unit : SVGCssContentTypeLength.Unit.values()) {
            double value = random.nextDouble();

            int counter = random.nextInt(10) + 1;

            data.setLength(0);

            while (counter-- > 0) {
                data.append(String.format("%s%s%s", data.length() == 0 ? "" : ",", String.format("%f", value).replace(",", "."), unit.getName()));
            }

            contentType.parseCssValue(data.toString());

            for (SVGCssContentTypeLength length : contentType.getValue()) {
                Assert.assertEquals(length.getValue(), value, 0.01d);
                Assert.assertEquals(length.getUnit(), unit);
            }
        }
    }

    /**
     * Checks if the {@link SVGCssContentTypeStrokeLineCap} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeStrokeLineCapIsFullySupported() {

        SVGCssContentTypeStrokeLineCap contentType = new SVGCssContentTypeStrokeLineCap(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeLineCap cap : StrokeLineCap.values()) {
            contentType.parseCssValue(cap.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), cap);
        }
    }

    /**
     * Checks if the {@link SVGCssContentTypeStrokeLineJoin} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeStrokeLineJoinIsFullySupported() {

        SVGCssContentTypeStrokeLineJoin contentType = new SVGCssContentTypeStrokeLineJoin(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeLineJoin join : StrokeLineJoin.values()) {
            contentType.parseCssValue(join.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), join);
        }
    }

    /**
     * Checks if the {@link SVGCssContentTypeStrokeType} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test public void checkSVGCssContentTypeStrokeTypeIsFullySupported() {

        SVGCssContentTypeStrokeType contentType = new SVGCssContentTypeStrokeType(new SVGDataProvider());

        contentType.parseCssValue("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssValue("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeType type : StrokeType.values()) {
            contentType.parseCssValue(type.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), type);
        }
    }

    //endregion
}
