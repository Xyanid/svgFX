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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeHolder;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeType;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeTransform;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.utils.SVGUtil;
import de.saxsys.svgfx.core.utils.StringUtil;
import de.saxsys.svgfx.css.definitions.Constants;
import de.saxsys.svgfx.xml.core.ElementBase;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.Optional;

/**
 * This class represents a basic scg element, which provides some basic functionality to get the style of the class.
 *
 * @param <TResult> The type of the result this element will provide @author Xyanid on 28.10.2015.
 */
public abstract class SVGElementBase<TResult> extends ElementBase<SVGAttributeType, SVGAttributeHolder, SVGDocumentDataProvider, TResult, SVGElementBase<?>, SVGElementBase<?>> {

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

    // region Override ElementBase

    /**
     * {@inheritDoc}
     */
    @Override
    public final TResult getResult() throws SAXException {
        if (result == null) {
            try {
                result = createAndInitializeResult(this::getStyle);
            } catch (final SVGException e) {
                throw new SAXException(e);
            }
        }

        return result;
    }

    // endregion

    // region Public

    /**
     * Creates a result represented by this element and uses the given supplier in order to fetch data needed to initialize the result
     *
     * @return the result produced by this element.
     *
     * @throws SVGException thrown when an exception during creation occurs.
     */
    public final TResult createAndInitializeResult(final StyleSupplier styleSupplier) throws SVGException {

        final TResult result = createResult(styleSupplier);
        initializeResult(result, styleSupplier);
        return result;
    }

    /**
     * Gets the elements own style and resolves its inheritance, also removed any self references.
     *
     * @return this elements own style.
     */
    public final SVGCssStyle getStyle() throws SVGException {
        return getStyleAndResolveInheritance(getParent() != null ? getParent().getStyle() : new SVGCssStyle(getDocumentDataProvider()));
    }

    /**
     * Gets the elements own {@link SVGCssStyle} and combines it with the given {@link SVGCssStyle}
     *
     * @param otherStyle the other {@link SVGCssStyle} the own style shall be combined with, can be null in which case {@link #combineStylesAndResolveInheritance(SVGCssStyle, SVGCssStyle)}
     *                   is not invoked, hence it will only return its own style.
     *
     * @return the {@link SVGCssStyle} if this element combined and resolved with the given {@link SVGCssStyle}.
     */
    public final SVGCssStyle getStyleAndResolveInheritance(final SVGCssStyle otherStyle) throws SVGException {
        final SVGCssStyle style = getCombinedStyle();

        combineStylesAndResolveInheritance(style, otherStyle);

        cleanStyleBeforeUsing(style);

        return style;
    }

    //endregion

    // region Protected

    /**
     * @return the transformation to be applied to this element if the {@link CoreAttributeMapper#TRANSFORM} is present.
     * otherwise null.
     *
     * @throws SVGException if there is a transformation which has invalid data for its matrix.
     */
    protected final Transform getTransformation() throws SVGException {

        final Optional<SVGAttributeTypeTransform> transform = getAttributeHolder().getAttribute(CoreAttributeMapper.TRANSFORM.getName(), SVGAttributeTypeTransform.class);

        if (transform.isPresent()) {
            return transform.get().getValue();
        }

        return null;
    }

    /**
     * Returns a node which represents the clip path to be applied to this element.
     *
     * @return the clip path to use or null if this element does not have a clip path.
     *
     * @throws SVGException             when there is a {@link SVGClipPath} referenced but the reference can not be found in the {@link #documentDataProvider}.
     * @throws IllegalArgumentException if the referenced {@link SVGClipPath} is an empty string.
     */
    protected final Node getClipPath(final StyleSupplier styleSupplier) throws SVGException {

        final Optional<SVGAttributeTypeString> referenceIRI = getStyle().getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_PATH.getName(), SVGAttributeTypeString.class);

        if (referenceIRI.isPresent() && StringUtil.isNotNullOrEmpty(referenceIRI.get().getValue())) {
            return SVGUtil.resolveIRI(referenceIRI.get().getValue(), getDocumentDataProvider(), SVGClipPath.class).createAndInitializeResult(styleSupplier);
        }

        return null;
    }

    /**
     * Checks if the element is inside a {@link SVGDefinitions} and store it inside the {@link #documentDataProvider} if so.
     *
     * @throws SVGException if an error occurs during the retrieval of the id.
     */
    protected final void storeElementInDocumentDataProvider() throws SVGException {
        if (getParent() instanceof SVGDefinitions) {
            final Optional<SVGAttributeTypeString> id = getAttributeHolder().getAttribute(CoreAttributeMapper.ID.getName(), SVGAttributeTypeString.class);
            if (id.isPresent()) {
                getDocumentDataProvider().storeData(id.get().getValue(), this);
            }
        }
    }

    // endregion

    // region Private

    /**
     * It will ensure that messy svg files, which contain elements that reference themself, will not cause StackOverflowExceptions.
     *
     * @param style the {@link SVGCssStyle} that should be cleaned.
     */
    private void cleanStyleBeforeUsing(final SVGCssStyle style) throws SVGException {

        final Optional<SVGAttributeTypeString> id = getAttributeHolder().getAttribute(CoreAttributeMapper.ID.getName(), SVGAttributeTypeString.class);
        if (!id.isPresent()) {
            return;
        }

        final Optional<SVGAttributeTypeString> clipPath = style.getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_PATH.getName(), SVGAttributeTypeString.class);
        if (!clipPath.isPresent()) {
            return;
        }

        final String clipPathReference = SVGUtil.stripIRIIdentifiers(clipPath.get().getValue());
        if (StringUtil.isNotNullOrEmpty(clipPathReference) && clipPathReference.equals(id.get().getValue())) {
            style.getProperties().remove(PresentationAttributeMapper.CLIP_PATH.getName());
        }
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
    private SVGCssStyle getCombinedStyle() throws SVGException {

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
     * Gets the elements own {@link SVGCssStyle}, which will only be available if the element has the {@link CoreAttributeMapper#STYLE}.
     *
     * @return the {@link SVGCssStyle} of this element or null if there is none.
     */
    private SVGCssStyle getOwnStyle() throws SVGException {

        final Optional<SVGAttributeTypeString> style = getAttributeHolder().getAttribute(CoreAttributeMapper.STYLE.getName(), SVGAttributeTypeString.class);
        if (style.isPresent()) {

            final String attribute = style.get().getValue();

            final SVGCssStyle ownStyle = new SVGCssStyle(getDocumentDataProvider());


            try {
                ownStyle.parseCssText(String.format("ownStyle%s%s%s%s",
                                                    Constants.DECLARATION_BLOCK_START,
                                                    attribute,
                                                    attribute.endsWith(Constants.PROPERTY_END_STRING) ? "" : Constants.PROPERTY_END,
                                                    Constants.DECLARATION_BLOCK_END));
            } catch (final IllegalArgumentException e) {
                throw new SVGException(SVGException.Reason.INVALID_CSS_STYLE, e);
            }

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

            return getDocumentDataProvider().getUnmodifiableStyles()
                                            .stream()
                                            .filter(data -> data.getName().endsWith(reference))
                                            .findFirst()
                                            .orElseThrow(() -> new SVGException(SVGException.Reason.MISSING_STYLE, String.format("Given style reference %s was not found", reference)));

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
    private SVGCssStyle getPresentationCssStyle() throws SVGException {

        SVGCssStyle result = null;

        final StringBuilder cssText = new StringBuilder();

        for (final PresentationAttributeMapper attributeMapper : PresentationAttributeMapper.VALUES) {
            getAttributeHolder().getAttribute(attributeMapper.getName()).ifPresent(attribute -> {
                final String data = attribute.getText();
                if (StringUtil.isNotNullOrEmpty(data)) {
                    if (cssText.length() == 0) {
                        cssText.append("presentationStyle" + Constants.DECLARATION_BLOCK_START);
                    }

                    cssText.append(String.format("%s%s%s%s", attributeMapper.getName(), Constants.PROPERTY_SEPARATOR, data, Constants.PROPERTY_END));
                }
            });
        }

        if (cssText.length() > 0) {
            cssText.append(Constants.DECLARATION_BLOCK_END);
            result = new SVGCssStyle(getDocumentDataProvider());

            try {
                result.parseCssText(cssText.toString());
            } catch (final Exception e) {
                throw new SVGException(SVGException.Reason.INVALID_CSS_STYLE, e);
            }
        }

        return result;
    }

    /**
     * This method will use the contentMap of the given style, if any property uses inheritance, the given otherStyle will be used to resolve it.
     * If the otherStyle does not contain a value for the inherited property, then the default value will be used.
     *
     * @param style      {@link SVGCssStyle} which contains the contentMap which are to be set, must not be null.
     * @param otherStyle the {@link SVGCssStyle} to use as a parent in order to resolve the inheritance, must not be null.
     *
     * @throws IllegalArgumentException if either style or inheritanceResolver are null.
     */
    private void combineStylesAndResolveInheritance(final SVGCssStyle style, final SVGCssStyle otherStyle) throws SVGException {
        if (style == null) {
            throw new SVGException(SVGException.Reason.NULL_ARGUMENT, "Given style must not be null");
        }

        if (otherStyle == null) {
            throw new SVGException(SVGException.Reason.NULL_ARGUMENT, "Given otherStyle must not be null");
        }

        style.combineWithStyle(otherStyle);

        for (final Map.Entry<String, SVGAttributeType> property : style.getProperties().entrySet()) {
            if (property.getValue().getIsInherited()) {
                final Optional<SVGAttributeType> otherProperty = otherStyle.getAttributeHolder().getAttribute(property.getKey());
                if (otherProperty.isPresent()) {
                    property.getValue().setText(otherProperty.get().getText());
                } else {
                    property.getValue().useDefaultValue();
                }
            }
        }
    }


    // endregion

    // region Abstract

    /**
     * Must be overwritten in the actual implementation to create a new result for this element based on the
     * information available.
     *
     * @return a new instance of the result or null of no result was created
     *
     * @throws SVGException will be thrown when an error during creation occurs
     */
    protected abstract TResult createResult(final StyleSupplier styleSupplier) throws SVGException;

    /**
     * This method will be called in the {@link #createAndInitializeResult(StyleSupplier)} and allows to modify the result such as applying a style or
     * transformations.
     * The given inheritanceResolver should be used to retrieve such data, this simply is needed because some elements can actually be referenced e.g.
     * {@link SVGUse} and their actual parent is not
     * the one from which the inherited style attributes can be retrieved.
     *
     * @param result the result which should be modified.
     *
     * @throws SVGException will be thrown when an error during modification
     */
    protected abstract void initializeResult(final TResult result, final StyleSupplier styleSupplier) throws SVGException;

    // endregion
}