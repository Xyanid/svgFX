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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeHolder;
import de.saxsys.svgfx.core.content.SVGAttributeType;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;
import de.saxsys.svgfx.core.content.SVGAttributeTypeTransform;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.definitions.Constants;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.Optional;

/**
 * This class represents a basic scg element, which provides some basic functionality to get the style of the class.
 *
 * @param <TResult> The type of the result this element will provide @author Xyanid on 28.10.2015.
 */
public abstract class SVGElementBase<TResult> extends ElementBase<SVGAttributeType, SVGAttributeHolder, SVGDocumentDataProvider, TResult, SVGElementBase<?>> {

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
    protected SVGElementBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider, new SVGAttributeHolder(dataProvider));
    }

    // endregion

    // region Private

    /**
     * This method should be used before a call to {@link #createAndInitializeResult(SVGCssStyle)}, {@link #createResult(SVGCssStyle)} or {@link #initializeResult(Object, SVGCssStyle)} is made.
     * It will ensure that messy svg files, which contain elements that reference themself, will not cause StackOverflowExceptions.
     *
     * @param style the {@link SVGCssStyle} that should be cleaned.
     */
    private void cleanStyleBeforeUsing(final SVGCssStyle style) {
        // since the style is used here we need to ensure its not possible for an element to reference itself
        getAttributeHolder().getAttribute(CoreAttributeMapper.ID.getName(), SVGAttributeTypeString.class)
                            .ifPresent(id -> style.getAttributeHolder()
                                                  .getAttribute(PresentationAttributeMapper.CLIP_PATH.getName(), SVGAttributeTypeString.class)
                                                  .ifPresent(clipPath -> {
                                                      final String clipPathReference = SVGUtils.stripIRIIdentifiers(clipPath.getValue());
                                                      if (StringUtils.isNotNullOrEmpty(clipPathReference) &&
                                                          clipPathReference.equals(id.getValue())) {
                                                          style.getProperties()
                                                               .remove(PresentationAttributeMapper.CLIP_PATH.getName());
                                                      }
                                                  }));
    }

    // endregion

    // region Abstract

    /**
     * Must be overwritten in the actual implementation to create a new result for this element based on the
     * information available.
     *
     * @param style the element to use when data is needed.
     *
     * @return a new instance of the result or null of no result was created
     *
     * @throws SVGException will be thrown when an error during creation occurs
     */
    protected abstract TResult createResult(final SVGCssStyle style) throws SVGException;

    /**
     * This method will be called in the {@link #createAndInitializeResult(SVGCssStyle)} and allows to modify the result such as applying a style or
     * transformations.
     * The given inheritanceResolver should be used to retrieve such data, this simply is needed because some elements can actually be referenced e.g.
     * {@link SVGUse} and their actual parent is not
     * the one from which the inherited style attributes can be retrieved.
     *
     * @param result the result which should be modified.
     * @param style  the {@link SVGCssStyle} to use when data is needed.
     *
     * @throws SVGException will be thrown when an error during modification
     */
    protected abstract void initializeResult(final TResult result, final SVGCssStyle style) throws SVGException;

    // endregion

    // region Public Related to Styling

    /**
     * Gets the elements own {@link SVGCssStyle} and combines it with the {@link SVGCssStyle} of the parent if there one.
     *
     * @return the {@link SVGCssStyle} if this element combined and resolved with the parents {@link SVGCssStyle} if a parents exits.
     */
    public final SVGCssStyle getCssStyleAndResolveInheritance() {
        return getCssStyleAndResolveInheritance(getParent() != null ? getParent().getCssStyleAndResolveInheritance() : new SVGCssStyle(getDocumentDataProvider()));
    }

    /**
     * Gets the elements own {@link SVGCssStyle} and combines it with the given {@link SVGCssStyle}
     *
     * @param otherStyle the other {@link SVGCssStyle} the own style shall be combined with, can be null in which case {@link SVGUtils#combineStylesAndResolveInheritance(SVGCssStyle, SVGCssStyle)}
     *                   is not invoked, hence it will only return its own style.
     *
     * @return the {@link SVGCssStyle} if this element combined and resolved with the given {@link SVGCssStyle}.
     */
    public final SVGCssStyle getCssStyleAndResolveInheritance(final SVGCssStyle otherStyle) {
        final SVGCssStyle style = getCssStyle();

        SVGUtils.combineStylesAndResolveInheritance(style, otherStyle);

        return style;
    }

    /**
     * Returns the {@link SVGCssStyle} of this element. Since an element can contain a {@link PresentationAttributeMapper}s, an own {@link SVGCssStyle} or a reference to an existing
     * {@link SVGCssStyle} there need to be a rule how the {@link SVGCssStyle} is build. The rule is as follows:
     * <p>
     * {@link PresentationAttributeMapper}s are preferred if they are present and will overwrite existing attribute of an own {@link SVGCssStyle} or a referenced {@link SVGCssStyle}. The following
     * example shows an element which has two {@link PresentationAttributeMapper}s and an own {@link SVGCssStyle}.
     * <pre>
     *     e.G.
     *     circle fill="none" stroke="#808080" style="fill:#111111; stroke:#001122 fill-rule:odd"
     * </pre>
     * this will result in fill = none, stroke = #808080 and fill-rule = odd. The same behavior is to be expected if the {@link SVGCssStyle} would be a
     * reference e.g.
     * <pre>
     *     e.G.
     *     .st1{fill:#111111; stroke:#001122 fill-rule:odd}
     *     circle fill="none" stroke="#808080" class="st1"
     * </pre>
     * <p>
     * An own {@link SVGCssStyle} is always preferred before a referenced {@link SVGCssStyle} and will overwrite existing attributes just as a {@link PresentationAttributeMapper} would. The
     * following example shows an element which has an own {@link SVGCssStyle} and a reference to a {@link SVGCssStyle}.
     * <pre>
     *     e.G.
     *     .st1{fill:none; stroke:#808080 fill-rule:odd}
     *     circle style="fill:#111111; stroke:#001122" class="st1"
     * </pre>
     * this will result in fill = 111111, stroke = #001122 and fill-rule = odd.
     *
     * @return the {@link SVGCssStyle} of this element or null if no style can be determined.
     */
    public final SVGCssStyle getCssStyle() {

        // first we get a referenced style class if any
        SVGCssStyle style = getPresentationCssStyle();

        // if an own style is present it will be used overwriting other attributes in the process
        SVGCssStyle ownStyle = getOwnStyle();
        if (ownStyle != null) {
            if (style == null) {
                style = ownStyle;
            } else {
                style.combineWithStyle(ownStyle);
            }
        }

        // if a referenced style is present it will be used overwriting other attributes in the process
        SVGCssStyle referencedStyle = getReferencedStyle();
        if (referencedStyle != null) {

            if (style == null) {
                style = referencedStyle;
            } else {
                style.combineWithStyle(referencedStyle);
            }
        }

        if (style == null) {
            style = new SVGCssStyle(getDocumentDataProvider());
        }

        return style;
    }

    /**
     * @return the transformation to be applied to this element if the {@link CoreAttributeMapper#TRANSFORM} is present.
     * otherwise null.
     *
     * @throws SVGException if there is a transformation which has invalid data for its matrix.
     */
    public final Transform getTransformation() throws SVGException {

        final Optional<SVGAttributeTypeTransform> transform = getAttributeHolder().getAttribute(CoreAttributeMapper.TRANSFORM.getName(), SVGAttributeTypeTransform.class);

        if (transform.isPresent()) {
            return transform.get().getDefaultValue();
        }

        return null;
    }

    /**
     * Encapsulate a call to {@link #getClipPath(SVGCssStyle)} using the {@link #getCssStyleAndResolveInheritance()} as the style.
     *
     * @return the clip path to use or null if this element does not have a clip path.
     *
     * @throws SVGException when there is a {@link SVGClipPath} referenced but the reference can not be found in the {@link #documentDataProvider}.
     */
    public final Node getClipPath() throws SVGException {
        return getClipPath(getCssStyleAndResolveInheritance());
    }

    /**
     * Returns a node which represents the clip path to be applied to this element.
     *
     * @param style the {@link SVGCssStyle} which is used when inherited style attributes are to be resolved.
     *
     * @return the clip path to use or null if this element does not have a clip path.
     *
     * @throws SVGException             when there is a {@link SVGClipPath} referenced but the reference can not be found in the {@link #documentDataProvider}.
     * @throws IllegalArgumentException if the referenced {@link SVGClipPath} is an empty string.
     */
    public final Node getClipPath(final SVGCssStyle style) throws SVGException {

        final Optional<SVGAttributeTypeString> referenceIRI = style.getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_PATH.getName(), SVGAttributeTypeString.class);

        if (referenceIRI.isPresent() && StringUtils.isNotNullOrEmpty(referenceIRI.get().getValue())) {
            return SVGUtils.resolveIRI(referenceIRI.get().getValue(), getDocumentDataProvider(), SVGClipPath.class).createAndInitializeResult();
        }

        return null;
    }

    // endregion

    // region Public related to Creating Results

    /**
     * Gets the result if its was not yet created using the given {@link SVGCssStyle} to create the styling.
     *
     * @param style the {@link SVGCssStyle} which will be used in order to get data for styling.
     *
     * @return the result of this element.
     *
     * @throws SVGException if something fails during the creation of the element
     */
    public final TResult getResult(final SVGCssStyle style) throws SVGException {

        if (result == null) {
            result = createAndInitializeResult(style);
        }

        return result;
    }

    /**
     * Creates a result represented by this element.
     *
     * @return the result produced by this element.
     *
     * @throws SVGException thrown when an exception during creation occurs.
     */
    public final TResult createAndInitializeResult() throws SVGException {
        return createAndInitializeResult(getCssStyleAndResolveInheritance());
    }

    /**
     * Creates a result represented by this element and uses the given supplier in order to fetch data needed to initialize the result
     *
     * @param style the {@link SVGCssStyle} to use when data is needed.
     *
     * @return the result produced by this element.
     *
     * @throws SVGException thrown when an exception during creation occurs.
     */
    public final TResult createAndInitializeResult(final SVGCssStyle style) throws SVGException {

        final TResult result;

        cleanStyleBeforeUsing(style);

        try {
            result = createResult(style);
            initializeResult(result, style);
        } catch (Exception e) {
            throw new SVGException(String.format("Creation of element %s failed.\nOriginal element is %s ", getClass().getName(), toString()), e);
        }

        return result;
    }

    //endregion

    // region Override ElementBase

    /**
     * {@inheritDoc}
     */
    @Override
    public final TResult getResult() throws SVGException {
        if (result == null) {
            result = createAndInitializeResult();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startProcessing() throws SVGException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCharacterData(final char[] ch, final int start, final int length) throws SVGException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endProcessing() throws SVGException {
    }

    // endregion

    // region Private/Package Private


    /**
     * Gets the elements own {@link SVGCssStyle}, which will only be available if the element has the {@link CoreAttributeMapper#STYLE}.
     *
     * @return the {@link SVGCssStyle} of this element or null if there is none.
     */
    private SVGCssStyle getOwnStyle() {

        final Optional<SVGAttributeTypeString> style = getAttributeHolder().getAttribute(CoreAttributeMapper.STYLE.getName(), SVGAttributeTypeString.class);
        if (style.isPresent()) {

            final String attribute = style.get().getValue();

            final SVGCssStyle ownStyle = new SVGCssStyle(getDocumentDataProvider());

            ownStyle.parseCssText(String.format("ownStyle%s%s%s%s",
                                                Constants.DECLARATION_BLOCK_START,
                                                attribute,
                                                attribute.endsWith(Constants.PROPERTY_END_STRING) ? "" : Constants.PROPERTY_END,
                                                Constants.DECLARATION_BLOCK_END));

            return ownStyle;
        }
        return null;
    }

    /**
     * Gets the elements referenced {@link SVGCssStyle}, which will only be available if the element has the {@link CoreAttributeMapper#CLASS}.
     *
     * @return the {@link SVGCssStyle} referenced by this element or null if there is none.
     *
     * @throws SVGException if the element uses a style reference but the style was not found in the {@link #documentDataProvider}.
     */
    private SVGCssStyle getReferencedStyle() throws SVGException {

        final Optional<SVGAttributeTypeString> className = getAttributeHolder().getAttribute(CoreAttributeMapper.CLASS.getName(), SVGAttributeTypeString.class);
        if (className.isPresent()) {
            final String reference = className.get().getValue();
            try {
                return getDocumentDataProvider().getStyles().stream().filter(data -> data.getName().endsWith(reference)).findFirst().get();
            } catch (Exception e) {
                throw new SVGException(String.format("Given style reference %s was not found", reference), e);
            }
        }
        return null;
    }

    /**
     * This method attempts to create a {@link SVGCssStyle} by looking up all the supported {@link PresentationAttributeMapper}. If any attribute is present a
     * valid cssString is returned.
     *
     * @return a {@link SVGCssStyle} containing the {@link PresentationAttributeMapper}s of this element if any or null if not attributes are submitted.
     * {@link PresentationAttributeMapper} exists.
     */
    private SVGCssStyle getPresentationCssStyle() {

        SVGCssStyle result = null;

        final StringBuilder cssText = new StringBuilder();

        for (final PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {
            getAttributeHolder().getAttribute(attribute.getName()).ifPresent(contentType -> {
                String data = contentType.getLastConsumedText();
                if (StringUtils.isNotNullOrEmpty(data)) {
                    if (cssText.length() == 0) {
                        cssText.append("presentationStyle" + Constants.DECLARATION_BLOCK_START);
                    }

                    cssText.append(String.format("%s%s%s%s", attribute.getName(), Constants.PROPERTY_SEPARATOR, data, Constants.PROPERTY_END));
                }
            });
        }

        if (cssText.length() > 0) {
            cssText.append(Constants.DECLARATION_BLOCK_END);
            result = new SVGCssStyle(getDocumentDataProvider());
            result.parseCssText(cssText.toString());
        }

        return result;
    }


    // endregion
}