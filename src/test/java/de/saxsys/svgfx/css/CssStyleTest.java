package de.saxsys.svgfx.css;

import de.saxsys.svgfx.css.core.CssStyle;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rico.hentschel on 05.10.2015.
 */
public final class CssStyleTest {

    //region Fields

    private final String cssText = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:10;}";

    private final String cssDeclarationBlock = "fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:15;";

    private final String cssTextChanged = ".st0{fill:none;stroke:#808080;stroke-width:3;stroke-miterlimit:15;}";

    private final String cssTextWithComment = ".st0{fill:none;stroke:#808080;/*this is a comment*/stroke-width:3;stroke-miterlimit:10;}";

    private final String cssTextWithCommentAndCssCharacters = ".st0{fill:none;stroke:#808080;/*{\"this is ;:a string\";:}*/stroke-width:3;stroke-miterlimit:10;}";

    private final String cssTextWithString = ".st0{fill:none;stroke:\"#808080\";stroke-width:3;stroke-miterlimit:10;}";

    private final String cssTextWithStringAndCssCharacters = ".st0{fill:none;stroke:\";{ar;asd:j}:sda;asd:\";stroke-width:3;stroke-miterlimit:10;}";

    //endregion

    //region Tests

    /**
     * Parses the {@link CssStyleTest#cssText}.
     */
    @Test public void parse() {

        CssStyle style = new CssStyle();

        style.setCssText(cssText);

        Assert.assertEquals(cssText, style.getCssText());
    }

    /**
     * Parses the {@link CssStyleTest#cssTextWithComment}.
     */
    @Test public void parseWithComment() {

        CssStyle style = new CssStyle();

        style.setCssText(cssTextWithComment);

        Assert.assertEquals(cssText, style.getCssText());

        style.setCssText(cssTextWithCommentAndCssCharacters);

        Assert.assertEquals(cssText, style.getCssText());
    }

    /**
     * Parses the {@link CssStyleTest#cssTextWithString} and {@link CssStyleTest#cssTextWithStringAndCssCharacters}.
     */
    @Test public void parseWithString() {

        CssStyle style = new CssStyle();

        style.setCssText(cssTextWithString);

        Assert.assertEquals(cssTextWithString, style.getCssText());

        style.setCssText(cssTextWithStringAndCssCharacters);

        Assert.assertEquals(cssTextWithStringAndCssCharacters, style.getCssText());
    }

    /**
     * Parses the {@link CssStyleTest#cssText} and changed the CssStyleDeclations to {@link CssStyleTest#cssDeclarationBlock}.
     */
    @Test public void change() {

        CssStyle style = new CssStyle();

        style.setCssText(cssText);

        Assert.assertEquals(cssText, style.getCssText());

        style.getCssStyleDeclaration().setCssText(cssDeclarationBlock);

        Assert.assertEquals(cssTextChanged, style.getCssText());

        style.getCssStyleDeclaration().getPropertyCSSValue("stroke-miterlimit").setCssText("10");

        Assert.assertEquals(cssText, style.getCssText());
    }

    //endregion
}
