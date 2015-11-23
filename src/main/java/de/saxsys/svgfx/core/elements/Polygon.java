package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGPolyBase;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("polygon") public class Polygon extends SVGPolyBase<javafx.scene.shape.Polygon> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Polygon(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final javafx.scene.shape.Polygon createResultInternal() {

        return new javafx.scene.shape.Polygon(getPoints().stream().mapToDouble(Double::doubleValue).toArray());
    }

    //endregion
}
