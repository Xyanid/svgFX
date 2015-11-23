package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGShapeBase;
import de.saxsys.svgfx.core.definitions.Enumerations;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("ellipse") public class Ellipse extends SVGShapeBase<javafx.scene.shape.Ellipse> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Ellipse(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final javafx.scene.shape.Ellipse createResultInternal() {

        return new javafx.scene.shape.Ellipse(Double.parseDouble(getAttribute(Enumerations.SvgAttribute.CENTER_X.getName())),
                                              Double.parseDouble(getAttribute(Enumerations.SvgAttribute.CENTER_Y.getName())),
                                              Double.parseDouble(getAttribute(Enumerations.SvgAttribute.RADIUS_X.getName())),
                                              Double.parseDouble(getAttribute(Enumerations.SvgAttribute.RADIUS_Y.getName())));
    }

    //endregion
}
