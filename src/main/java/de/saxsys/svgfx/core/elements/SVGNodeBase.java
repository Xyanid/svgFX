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
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.SVGUtil;
import de.saxsys.svgfx.core.utils.StringUtil;
import javafx.scene.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Optional;

/**
 * This class represents a base class which contains shape element from svg.
 *
 * @param <TNode> type of the shape represented by this element
 *
 * @author Xyanid on 25.10.2015.
 */
public abstract class SVGNodeBase<TNode extends Node> extends SVGElementBase<TNode> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name                 the name of the element
     * @param attributes           attributes of the element
     * @param documentDataProvider dataprovider to be used
     */
    protected SVGNodeBase(final String name, final Attributes attributes, final SVGDocumentDataProvider documentDataProvider) {
        super(name, attributes, documentDataProvider);
    }

    //endregion

    // region Override SVGElementBase

    @Override
    public boolean keepElement() {
        return true;
    }

    @Override
    public void processCharacterData(char[] ch, int start, int length) throws SAXException {}

    /**
     * {@inheritDoc}Will apply the transformation to the element and add clipPath if available.
     */
    @Override
    protected void initializeResult(final TNode result, final SVGCssStyle ownStyle) throws SVGException {

        getTransformation().ifPresent(transform -> result.getTransforms().add(transform));

        getClipPath(ownStyle).ifPresent(result::setClip);
    }

    // endregion

    // endregion

    /**
     * Returns a node which represents the clip path to be applied to this element.
     *
     * @param ownStyle the {@link SVGCssStyle} to be used when there is a {@link SVGClipPath} defined for the element and it needs a style.
     *
     * @return the clip path to use or null if this element does not have a clip path.
     *
     * @throws SVGException             when there is a {@link SVGClipPath} referenced but the reference can not be found in the {@link #documentDataProvider}.
     * @throws IllegalArgumentException if the referenced {@link SVGClipPath} is an empty string.
     */
    private Optional<Node> getClipPath(final SVGCssStyle ownStyle) throws SVGException {

        final Optional<SVGAttributeTypeString> referenceIRI = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.CLIP_PATH.getName(), SVGAttributeTypeString.class);

        if (referenceIRI.isPresent() && StringUtil.isNotNullOrEmpty(referenceIRI.get().getValue())) {
            final SVGClipPath clipPath = SVGUtil.resolveIRI(referenceIRI.get().getValue(), getDocumentDataProvider(), SVGClipPath.class);

            if (this != clipPath) {
                return Optional.of(clipPath.createAndInitializeResult(ownStyle));
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    // region
}
