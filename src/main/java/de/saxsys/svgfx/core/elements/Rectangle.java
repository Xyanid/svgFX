package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGShapeBase;
import de.saxsys.svgfx.core.definitions.Enumerations;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("rect") public class Rectangle extends SVGShapeBase<javafx.scene.shape.Rectangle> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Rectangle(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final javafx.scene.shape.Rectangle createResultInternal() {

        return new javafx.scene.shape.Rectangle(Double.parseDouble(getAttribute(Enumerations.CoreAttribute.POSITION_X.getName())),
                                                Double.parseDouble(getAttribute(Enumerations.CoreAttribute.POSITION_Y.getName())),
                                                Double.parseDouble(getAttribute(Enumerations.CoreAttribute.WIDTH.getName())),
                                                Double.parseDouble(getAttribute(Enumerations.CoreAttribute.HEIGHT.getName())));
    }

    /**
     * {@inheritDoc}
     * Applies the corner radius if any.
     */
    @Override protected void initializeResult(javafx.scene.shape.Rectangle rect) throws SVGException {
        super.initializeResult(rect);

        // note that we need to multiply the radius since the arc is a diameter for whatever reason

        if (StringUtils.isNotEmpty(getAttribute(Enumerations.CoreAttribute.RADIUS_X.getName()))) {
            rect.setArcWidth(Double.parseDouble(getAttribute(Enumerations.CoreAttribute.RADIUS_X.getName())) * 2.0d);
        }

        if (StringUtils.isNotEmpty(getAttribute(Enumerations.CoreAttribute.RADIUS_Y.getName()))) {
            rect.setArcHeight(Double.parseDouble(getAttribute(Enumerations.CoreAttribute.RADIUS_Y.getName())) * 2.0d);
        }
    }

    //endregion
}
