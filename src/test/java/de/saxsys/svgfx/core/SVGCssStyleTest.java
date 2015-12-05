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

import de.saxsys.svgfx.css.core.CssStyle;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the behavior of {@link CssStyle} and hence also the behavior of the {@link de.saxsys.svgfx.css.core.CssStyleDeclaration} and {@link de.saxsys.svgfx.css.core.CssValue}.
 * Created by Xyanid on 05.10.2015.
 */
public final class SVGCssStyleTest {


    //region Tests

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test public void parseCssTextToCreateCssStyle() {

        String cssText = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}";

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(cssText);

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

        String cssText = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}";

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(cssText);


    }

    /**
     * Parses a css text to create a css style and ensures that all elements are present.
     */
    @Test public void parseCssTextToEnsureAllInvalidValuesCauseExceptions() {

        String cssText = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}";

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(cssText);

    }

    /**
     * Parses a css text which contains comments and also characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test public void parseCssTextWithCommentAndCssCharactersToCreateCssStyle() {

        String cssText = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}";

        CssStyle style = new CssStyle();

        style.setCssText(".st0{fill:none;stroke:#808080;/*this is a comment*/stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals(cssText, style.getCssText());

        style.setCssText(".st0{fill:none;stroke:#808080;/*{\"this is ;:a string\";:}*/stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals(cssText, style.getCssText());
    }

    /**
     * Parses a css text which contains string indicators inside a property and also contains characters related to css parsing.
     * This will ensure that no character inside a string will interrupt the parsing.
     */
    @Test public void parseCssTextWithStringIndicatorsAndCssCharactersToCreateCssStyle() {

        CssStyle style = new CssStyle();

        String cssTextWithString = ".st0{fill:none;stroke:\"#808080\";stroke-width:3;stroke-miterlimit:10;}";

        String cssTextWithStringAndCssCharacters = ".st0{fill:none;stroke:\";{ar;asd:j}:sda;asd:\";stroke-width:3;stroke-miterlimit:10;}";

        style.setCssText(cssTextWithString);

        Assert.assertEquals(cssTextWithString, style.getCssText());

        style.setCssText(cssTextWithStringAndCssCharacters);

        Assert.assertEquals(cssTextWithStringAndCssCharacters, style.getCssText());
    }

    /**
     * Parses a css text and set a property of the css style afterwards, this ensures that the css text generated by the css style will be changed.
     */
    @Test public void parseCssTextToCreateCssStyleAndChangeCssPropertyAfterwards() {

        String cssText = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}";

        CssStyle style = new CssStyle();

        style.setCssText(cssText);

        Assert.assertEquals(cssText, style.getCssText());

        style.getCssStyleDeclaration().setCssText("fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:15;");

        Assert.assertEquals(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:15;}", style.getCssText());

        style.getCssStyleDeclaration().getPropertyCSSValue("stroke-miterlimit").setCssText("10");

        Assert.assertEquals(cssText, style.getCssText());
    }

    /**
     * Combines two css styles and checks if the properties have been overwritten as expected.
     */
    @Test public void combineCssStyles() {

        CssStyle style = new CssStyle();

        style.setCssText(".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}");

        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("fill"), "none");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("stroke"), "#808080");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("stroke-miterlimit"), "10");

        CssStyle otherStyle = new CssStyle();

        otherStyle.setCssText(".st0{stroke-width:3;stroke-miterlimit:15;}");

        Assert.assertEquals(otherStyle.getCssStyleDeclaration().getPropertyValue("stroke-width"), "3");
        Assert.assertEquals(otherStyle.getCssStyleDeclaration().getPropertyValue("stroke-miterlimit"), "15");

        style.combineWithStyle(otherStyle);

        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("fill"), "none");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("stroke"), "#808080");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("stroke-miterlimit"), "15");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue("stroke-width"), "3");
    }

    //endregion
}
