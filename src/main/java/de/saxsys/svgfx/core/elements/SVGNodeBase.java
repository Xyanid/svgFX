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
import de.saxsys.svgfx.core.css.StyleSupplier;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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
     * @param parent               parent of the element
     * @param documentDataProvider dataprovider to be used
     */
    protected SVGNodeBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider documentDataProvider) {
        super(name, attributes, parent, documentDataProvider);
    }

    //endregion

    // region Override SVGElementBase

    @Override
    public boolean rememberElement() {
        return true;
    }

    @Override
    public void startProcessing() throws SAXException {}

    @Override
    public void processCharacterData(char[] ch, int start, int length) throws SAXException {}

    /**
     * {@inheritDoc}
     *
     * @return true if the element not not inside a {@link SVGClipPath} or {@link SVGGroup}, otherwise false.
     */
    @Override
    public boolean canConsumeResult() {
        return !((getParent() instanceof SVGClipPath) || (getParent() instanceof SVGGroup));
    }

    /**
     * {@inheritDoc}Will apply the transformation to the element.
     */
    @Override
    protected void initializeResult(final TNode node, final StyleSupplier supplier) throws SVGException {

        final Transform transform = getTransformation();
        if (transform != null) {
            node.getTransforms().add(transform);
        }

        final Node clip = getClipPath(supplier);
        if (clip != null) {
            node.setClip(clip);
        }
    }

    // endregion
}
