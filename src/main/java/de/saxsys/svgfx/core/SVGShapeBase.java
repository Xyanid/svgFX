package de.saxsys.svgfx.core;

import de.saxsys.svgfx.css.core.CssStyle;
import javafx.scene.shape.Shape;
import org.xml.sax.Attributes;

/**
 * This class represents a base class which contains shape element from svg.
 *
 * @param <TShape> type of the shape represented by this element
 *                 Created by Xyanid on 25.10.2015.
 */
public abstract class SVGShapeBase<TShape extends Shape> extends SVGNodeBase<TShape> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGShapeBase(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    // region Override SVGNodeBase

    /**
     * {@inheritDoc}
     * Applies the css style the the element if possible.
     */
    @Override protected void initializeResult(TShape shape) throws SVGException {
        super.initializeResult(shape);

        CssStyle style = getCssStyle();

        if (style != null) {

            SVGUtils.applyStyle(shape, style, getDataProvider());
        }
    }

    // endregion
}
