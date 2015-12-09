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

/**
 * This Class does not directly represent a SVG element but rather a Css element
 * Created by Xyanid on 27.10.2015.
 */
public class CssStyle extends CssBase implements CSSStyleRule {

    //region Fields

    private String selectorText;

    private CssStyleDeclaration cssStyleDeclaration;

    //endregion

    //region Constructor

    /**
     * Creates a new instance.
     */
    public CssStyle() {
        super();
    }

    /**
     * Creates a new instance.
     *
     * @param isModifiable determins if this element is modifiable
     */
    public CssStyle(final boolean isModifiable) {
        super(isModifiable);
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

    //endregion

    //region Implement CSSRule

    /**
     * {@inheritDoc}
     */
    @Override public short getType() {
        return CSSRule.STYLE_RULE;
    }

    /**
     * {@inheritDoc}
     */
    @Override public CSSStyleSheet getParentStyleSheet() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override public CSSRule getParentRule() {
        return null;
    }

    //endregion

    //region Implement CSSStyleRule

    /**
     * {@inheritDoc}
     */
    @Override public String getSelectorText() {
        return selectorText;
    }

    /**
     * {@inheritDoc}
     */
    @Override public void setSelectorText(final String selectorText) throws DOMException {
        if (selectorText == null) {
            throw new IllegalArgumentException("given selector must not ne null");
        }

        this.selectorText = selectorText;
    }

    /**
     * {@inheritDoc}
     */
    @Override public CSSStyleDeclaration getStyle() {
        return cssStyleDeclaration;
    }

    //endregion

    //region Override CssBase

    /**
     * {@inheritDoc}
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

        if (this.cssStyleDeclaration == null) {
            this.cssStyleDeclaration = new CssStyleDeclaration(this);
        }

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
