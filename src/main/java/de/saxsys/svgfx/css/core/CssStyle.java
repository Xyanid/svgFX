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

package de.saxsys.svgfx.css.core;


import de.saxsys.svgfx.css.definitions.Constants;
import javafx.util.Pair;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.util.Map;

/**
 * This Class does not directly represent a SVG element but rather a Css element
 * Created by Xyanid on 27.10.2015.
 */
public class CssStyle extends CssBase implements CSSStyleRule {

    //region Fields

    private String selectorText;

    private final CssStyleDeclaration cssStyleDeclaration;

    //endregion

    //region Constructor

    /**
     * Creates a new instance.
     */
    public CssStyle() {
        super();

        cssStyleDeclaration = new CssStyleDeclaration(this);
    }

    /**
     * Creates a new instance.
     *
     * @param isModifiable determins if this element is modifiable
     */
    public CssStyle(final boolean isModifiable) {
        super(isModifiable);

        cssStyleDeclaration = new CssStyleDeclaration(this);
    }

    //endregion

    //region Public

    /**
     * Returns the {@link CssStyle#cssStyleDeclaration}.
     *
     * @return the {@link CssStyle#cssStyleDeclaration}
     */
    public CssStyleDeclaration getCssStyleDeclaration() {
        return cssStyleDeclaration;
    }

    /**
     * Combines this {@link CssStyle} with the given {@link CssStyle}. Note that if the provided style has a {@link CssValue} which is also present in this style, then this value will be used
     * instead overwritting the existing one in this style.
     *
     * @param style the {@link CssStyle} which is be used, must not be null.
     *
     * @throws IllegalArgumentException if the given {@link CssStyle} is null.
     */
    public void combineWithStyle(final CssStyle style) {

        if (style == null) {
            throw new IllegalArgumentException("given style must not be null");
        }

        if (this == style || style.getCssStyleDeclaration() == null) {
            return;
        }

        for (Map.Entry<String, CssValue> entry : style.getCssStyleDeclaration().cssProperties.entrySet()) {
            cssStyleDeclaration.cssProperties.put(entry.getKey(), entry.getValue());
        }

        cssStyleDeclaration.setNeedsUpdateCssText(style.getCssStyleDeclaration().getLength() > 0);
    }

    //endregion

    //region Implement CSSRule

    /**
     * @inheritDoc
     */
    @Override public short getType() {
        return CSSRule.STYLE_RULE;
    }

    /**
     * @inheritDoc
     */
    @Override public CSSStyleSheet getParentStyleSheet() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override public CSSRule getParentRule() {
        return null;
    }

    //endregion

    //region Implement CSSStyleRule

    /**
     * @inheritDoc
     */
    @Override public String getSelectorText() {
        return selectorText;
    }

    /**
     * @inheritDoc
     */
    @Override public void setSelectorText(final String selectorText) throws DOMException {
        if (selectorText == null) {
            throw new IllegalArgumentException("given selector must not ne null");
        }

        this.selectorText = selectorText;
    }

    /**
     * @inheritDoc
     */
    @Override public CSSStyleDeclaration getStyle() {
        return cssStyleDeclaration;
    }

    //endregion

    //region Override CssBase

    /**
     * @inheritDoc
     */
    @Override public void consumeCssText(final String cssText) throws DOMException {

        StringBuilder builder = new StringBuilder();

        StringBuilder selector = new StringBuilder();

        StringBuilder cssStyleDeclaration = new StringBuilder();

        Pair<Boolean, Boolean> result = filterCommentAndString(cssText, null, character -> {
            builder.append(character);

            return false;
        }, character -> {
            if (character.equals(Constants.DECLARATION_BLOCK_START)) {

                selector.append(builder.toString().trim());

                builder.setLength(0);

            } else if (character.equals(Constants.DECLARATION_BLOCK_END)) {

                builder.append(character);

                cssStyleDeclaration.append(builder.toString());

                return true;
            }

            builder.append(character);

            return false;
        });

        //comment was not properly closed
        if (result.getKey()) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Given declaration block contains an unclosed comment.");
        }

        //string was not properly closed
        if (result.getValue()) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Given declaration block contains an unclosed string.");
        }

        setSelectorText(selector.toString());

        this.cssStyleDeclaration.setCssText(cssStyleDeclaration.toString());
    }

    @Override public String createCssText() {
        return String.format("%s%s", selectorText, getStyle() == null ? "" : getStyle().getCssText());
    }

    //endregion

    //region Override Object

    /**
     * Gets the HashCode this object, which is based on the selector.
     *
     * @return the HashCode of the selector of this object
     */
    @Override public int hashCode() {
        return selectorText == null ? super.hashCode() : selectorText.hashCode();
    }

    /**
     * Determines if the given object equals the provided object.
     * This is based on the selector of object.
     *
     * @param obj, object to compare to
     *
     * @return true if the object is the same otherwise false
     */
    @Override public boolean equals(final Object obj) {

        boolean result = super.equals(obj);

        if (!result && obj != null && CssStyle.class.isAssignableFrom(obj.getClass())) {
            CssStyle other = (CssStyle) obj;

            result = selectorText == null ? other.getSelectorText() == null : selectorText.equals(other.getSelectorText());
        }

        return result;
    }

    //endregion
}
