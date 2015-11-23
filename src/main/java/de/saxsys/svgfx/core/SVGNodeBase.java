package de.saxsys.svgfx.core;

import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

/**
 * This class represents a base class which contains shape element from svg.
 *
 * @param <TNode> type of the shape represented by this element
 *                Created by Xyanid on 25.10.2015.
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
    public SVGNodeBase(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    // region Override SVGElementBase

    /**
     * {@inheritDoc}
     * Will apply the transformation to the element.
     */
    @Override protected void initializeResult(TNode node) throws SVGException {

        Transform transform = getTransformation();

        if (transform != null) {
            node.getTransforms().add(transform);
        }
    }

    // endregion
}
