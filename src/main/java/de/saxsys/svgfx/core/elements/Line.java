package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGShapeBase;
import de.saxsys.svgfx.core.definitions.Enumerations;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("line") public class Line extends SVGShapeBase<javafx.scene.shape.Line> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Line(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final javafx.scene.shape.Line createResultInternal() throws SVGException {
        return new javafx.scene.shape.Line(Double.parseDouble(getAttribute(Enumerations.SvgAttribute.X1.getName())),
                                           Double.parseDouble(getAttribute(Enumerations.SvgAttribute.Y1.getName())),
                                           Double.parseDouble(getAttribute(Enumerations.SvgAttribute.X2.getName())),
                                           Double.parseDouble(getAttribute(Enumerations.SvgAttribute.Y2.getName())));
    }

    //endregion
}
