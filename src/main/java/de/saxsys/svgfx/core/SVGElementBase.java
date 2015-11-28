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
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.core.CssStyle;
import de.saxsys.svgfx.css.definitions.Constants;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.EnumSet;

/**
 * This class represents a basic scg element, which provides some basic functionality to get the style of the class.
 *
 * @param <TResult> The type of the result this element will provide Created by Xyanid on 28.10.2015.
 */
public abstract class SVGElementBase<TResult> extends ElementBase<SVGDataProvider, TResult, SVGElementBase<SVGDataProvider>> {

    // region Fields

    /**
     * The result represented by this element.
     */
    private TResult result;

    // endregion

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes, parent and dataProvider.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     *
     * @throws IllegalArgumentException if either value or dataProvider are null
     */
    public SVGElementBase(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region Public

    /**
     * Returns the {@link CssStyle} of this element, which is either a reference to an existing style or the element
     * itself has a style. If the element itself does not provide a style then the style of the
     * {@link ElementBase#parent} will be used if it exist. This is a cascading call meaning that at worst case the
     * last {@link ElementBase#parent} that has a style will be called
     *
     * @return the {@link CssStyle} of this element or first style in the {@link ElementBase#parent} tree
     */
    public final CssStyle getCssStyle() {

        CssStyle result;

        CssStyle presentationStyle = getPresentationCssStyle();

        // we may have our own style or need to get a style
        if (StringUtils.isNotNullOrEmpty(getAttribute(Enumerations.CoreAttribute.STYLE.getName()))) {

            String attribute = getAttribute(Enumerations.CoreAttribute.STYLE.getName());

            result = new CssStyle();

            result.setCssText(String.format("ownStyle%s%s%s%s",
                                            Constants.DECLARATION_BLOCK_START,
                                            attribute,
                                            attribute.endsWith(Constants.PROPERTY_END_STRING) ? "" : Constants.PROPERTY_END,
                                            Constants.DECLARATION_BLOCK_END));
            // other wise we are referencing a class and want the style here
        } else if (StringUtils.isNotNullOrEmpty(getAttribute(Enumerations.CoreAttribute.CLASS.getName()))) {

            String styleClass = getAttribute(Enumerations.CoreAttribute.CLASS.getName());

            result = getDataProvider().getStyles().stream().filter(data -> data.getSelectorText().endsWith(styleClass)).findFirst().get();
            // otherwise we might have a parent that provides us with a style so use that one instead
        } else {
            result = presentationStyle;
        }

        result.combineWithStyle(presentationStyle);

        return result;
    }

    /**
     * @return the transformation to be applied to this element if the {@link Enumerations.CoreAttribute#TRANSFORM} is present.
     * otherwise null.
     *
     * @throws SVGException if there is a transformation which has invalid data for its matrix.
     */
    public final Transform getTransformation() throws SVGException {
        if (StringUtils.isNotNullOrEmpty(getAttribute(Enumerations.CoreAttribute.TRANSFORM.getName()))) {
            return SVGUtils.getTransform(getAttribute(Enumerations.CoreAttribute.TRANSFORM.getName()));
        }

        return null;
    }

    /**
     * Must be overwritten in the actual implementation to create a new result for this element based on the
     * information available.
     *
     * @return a new instance of the result or null of no result was created
     *
     * @throws SVGException will be thrown when an error during creation occurs
     */
    protected abstract TResult createResultInternal() throws SVGException;

    // endregion

    // region Private

    /**
     * This method attempts to create a {@link CssStyle} by looking up all the supported {@link de.saxsys.svgfx.core.definitions.Enumerations.PresentationAttribute}. If any attribute is present a
     * valid
     * cssString is returned.
     *
     * @return a {@link CssStyle} containing the {@link de.saxsys.svgfx.core.definitions.Enumerations.PresentationAttribute}s of this element if any or null if no
     * {@link de.saxsys.svgfx.core.definitions.Enumerations.PresentationAttribute} exists.
     */
    public CssStyle getPresentationCssStyle() {

        CssStyle result = null;

        StringBuilder cssText = new StringBuilder();

        for (Enumerations.PresentationAttribute attribute : EnumSet.allOf(Enumerations.PresentationAttribute.class)) {

            String data = getAttribute(attribute.getName());

            if (StringUtils.isNotNullOrEmpty(data)) {
                if (cssText.length() == 0) {
                    cssText.append("presentationStyle" + Constants.DECLARATION_BLOCK_START);
                }

                cssText.append(String.format("%s%s%s%s", attribute.getName(), Constants.PROPERTY_SEPARATOR, data, Constants.PROPERTY_END));
            }
        }

        if (cssText.length() > 0) {
            cssText.append(Constants.DECLARATION_BLOCK_END);
            result = new CssStyle();
            result.consumeCssText(cssText.toString());
        }

        return result;
    }

    // endregion

    //region Abstract

    /**
     * This method will be called in the {@link #createResult()} and allows to modify the result such as applying a style or transformations.
     *
     * @param result the result which should be modified.
     *
     * @throws SVGException will be thrown when an error during modification
     */
    protected abstract void initializeResult(TResult result) throws SVGException;

    public final TResult createResult() throws SVGException {
        TResult result = createResultInternal();

        initializeResult(result);

        return result;
    }

    //endregion

    // region Override ElementBase

    /**
     * @inheritDoc
     */
    @Override public final TResult getResult() throws SVGException {

        if (result == null) {
            result = createResult();
        }

        return result;
    }

    /**
     * @inheritDoc
     */
    @Override public void startProcessing() throws SVGException {
    }

    /**
     * @inheritDoc
     */
    @Override public void processCharacterData(final char[] ch, final int start, final int length) throws SVGException {
    }

    /**
     * @inheritDoc
     */
    @Override public void endProcessing() throws SVGException {
    }

    // endregion
}
