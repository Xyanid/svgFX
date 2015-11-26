package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.css.core.CssStyle;
import javafx.scene.paint.Color;
import org.xml.sax.Attributes;

/**
 * This class represents a stop element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("stop") public class Stop extends SVGElementBase<javafx.scene.paint.Stop> {

    //region Stop

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Stop(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override protected final javafx.scene.paint.Stop createResultInternal() throws SVGException {
        double offset = Double.parseDouble(getAttribute(Enumerations.CoreAttribute.OFFSET.getName()));

        CssStyle style = getCssStyle();

        double opacity = style.getCssStyleDeclaration().getPropertyAs(Enumerations.PresentationAttribute.STOP_OPACITY, Double::parseDouble);

        String color = style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.STOP_COLOR.getName());

        if (color == null) {
            throw new IllegalArgumentException("given color must not be null");
        }

        return new javafx.scene.paint.Stop(offset, Color.web(color, opacity));
    }

    @Override protected final void initializeResult(javafx.scene.paint.Stop stop) throws SVGException {

    }

    //endregion
}
