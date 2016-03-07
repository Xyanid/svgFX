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

package de.saxsys.svgfx.core.css;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Tests the behavior of {@link SVGCssStyle} and hence also {@link SVGContentTypeBase}.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGCssContentTypeTest {

    /**
     * Checks if the {@link SVGContentTypeFillRule} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeFillRuleIsFullySupported() {

        SVGContentTypeFillRule contentType = new SVGContentTypeFillRule(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (Enumerations.FillRuleMapping value : Enumerations.FillRuleMapping.values()) {

            contentType.parseCssText(value.getName());
            Assert.assertEquals(contentType.getValue(), value.getRule());
            Assert.assertEquals(contentType.getUnit(), null);
        }
    }

    /**
     * Checks if the {@link SVGContentTypeLength} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeLengthIsFullySupported() {

        SVGContentTypeLength contentType = new SVGContentTypeLength(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        Random random = new Random();

        for (SVGContentTypeLength.Unit unit : SVGContentTypeLength.Unit.values()) {
            double value = random.nextDouble();

            contentType.parseCssText(String.format("%f%s", value, unit.getName()));
            Assert.assertEquals(contentType.getValue(), value, 0.01d);
            Assert.assertEquals(contentType.getUnit(), unit);
        }
    }

    /**
     * Checks if the {@link SVGContentTypeDouble} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeDoubleIsFullySupported() {

        SVGContentTypeDouble contentType = new SVGContentTypeDouble(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.parseCssText("1.0");
        Assert.assertEquals(1.0d, contentType.getValue(), 0.01d);
        Assert.assertNull(contentType.getUnit());
    }

    /**
     * Checks if the {@link SVGContentTypePaint} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypePaintIsFullySupported() {

        SVGContentTypePaint contentType = new SVGContentTypePaint(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.parseCssText("currentColor");
        Assert.assertTrue(contentType.getIsCurrentColor());

        contentType.parseCssText("rgb(255,32,96)");
        Assert.assertEquals(contentType.getValue(), Color.web("rgb(255,32,96)"));

        contentType.parseCssText("rgb(20%,30%,10%)");
        Assert.assertEquals(contentType.getValue(), Color.web("rgb(20%,30%,10%)"));

        contentType.parseCssText("red");
        Assert.assertEquals(contentType.getValue(), Color.web("red"));
    }

    /**
     * Checks if the {@link SVGContentTypeString} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStringIsFullySupported() {

        SVGContentTypeString contentType = new SVGContentTypeString(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        contentType.parseCssText("random");
        Assert.assertEquals(contentType.getValue(), "random");
    }

    /**
     * Checks if the {@link SVGContentTypeStrokeDashArray} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeDashArrayIsFullySupported() {

        SVGContentTypeStrokeDashArray contentType = new SVGContentTypeStrokeDashArray(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        Random random = new Random();

        StringBuilder data = new StringBuilder();

        for (SVGContentTypeLength.Unit unit : SVGContentTypeLength.Unit.values()) {
            double value = random.nextDouble();

            int counter = random.nextInt(10) + 1;

            data.setLength(0);

            while (counter-- > 0) {
                data.append(String.format("%s%s%s", data.length() == 0 ? "" : ",", String.format("%f", value).replace(",", "."), unit.getName()));
            }

            contentType.parseCssText(data.toString());

            for (SVGContentTypeLength length : contentType.getValue()) {
                Assert.assertEquals(length.getValue(), value, 0.01d);
                Assert.assertEquals(length.getUnit(), unit);
            }

            for (Double valueDouble : contentType.getDashValues()) {
                Assert.assertEquals(valueDouble, value, 0.01d);
            }
        }
    }

    /**
     * Checks if the {@link SVGContentTypeStrokeLineCap} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineCapIsFullySupported() {

        SVGContentTypeStrokeLineCap contentType = new SVGContentTypeStrokeLineCap(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeLineCap cap : StrokeLineCap.values()) {
            contentType.parseCssText(cap.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), cap);
        }
    }

    /**
     * Checks if the {@link SVGContentTypeStrokeLineJoin} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeLineJoinIsFullySupported() {

        SVGContentTypeStrokeLineJoin contentType = new SVGContentTypeStrokeLineJoin(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeLineJoin join : StrokeLineJoin.values()) {
            contentType.parseCssText(join.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), join);
        }
    }

    /**
     * Checks if the {@link SVGContentTypeStrokeType} is fully supported, meaning all parsing of text produces the required results.
     */
    @Test
    public void checkSVGCssContentTypeStrokeTypeIsFullySupported() {

        SVGContentTypeStrokeType contentType = new SVGContentTypeStrokeType(new SVGDataProvider());

        contentType.parseCssText("inherit");
        Assert.assertTrue(contentType.getIsInherited());

        contentType.parseCssText("none");
        Assert.assertTrue(contentType.getIsNone());

        for (StrokeType type : StrokeType.values()) {
            contentType.parseCssText(type.name().toLowerCase());
            Assert.assertEquals(contentType.getValue(), type);
        }
    }
}
