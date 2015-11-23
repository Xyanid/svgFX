package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGGradientBase;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.CycleMethod;
import org.xml.sax.Attributes;

import java.util.List;

/**
 * This class represents the linear gradient element from svg
 * Created by Xyanid on 24.10.2015.
 */
@SVGElementMapping("linearGradient") public class LinearGradient extends SVGGradientBase<javafx.scene.paint.LinearGradient> {


    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public LinearGradient(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final javafx.scene.paint.LinearGradient createResultInternal() throws SVGException {

        List<javafx.scene.paint.Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException("given linear gradient does not have colors");
        }

        return new javafx.scene.paint.LinearGradient(Double.parseDouble(getAttribute(Enumerations.SvgAttribute.X1.getName())),
                                                     Double.parseDouble(getAttribute(Enumerations.SvgAttribute.Y1.getName())),
                                                     Double.parseDouble(getAttribute(Enumerations.SvgAttribute.X2.getName())),
                                                     Double.parseDouble(getAttribute(Enumerations.SvgAttribute.Y2.getName())),
                                                     false,
                                                     CycleMethod.NO_CYCLE,
                                                     stops);
    }

    //endregion
}
