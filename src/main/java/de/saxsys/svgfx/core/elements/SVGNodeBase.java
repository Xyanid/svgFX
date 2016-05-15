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

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

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
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGNodeBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    // region Override SVGElementBase

    /**
     * {@inheritDoc}
     * Will apply the transformation to the element.
     */
    @Override
    protected void initializeResult(final TNode node, final SVGCssStyle style) throws SVGException {

        Transform transform = getTransformation();
        if (transform != null) {
            node.getTransforms().add(transform);
        }

        Node clip = getClipPath(style);
        if (clip != null) {
            node.setClip(clip);
        }
    }

    // endregion
}
