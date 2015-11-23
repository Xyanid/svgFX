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
 * This Class represents a radial gradient from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("radialGradient") public class RadialGradient extends SVGGradientBase<javafx.scene.paint.RadialGradient> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public RadialGradient(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override RadialGradient

    @Override protected final javafx.scene.paint.RadialGradient createResultInternal() {

        List<javafx.scene.paint.Stop> stops = getStops();

        if (stops.isEmpty()) {
            throw new SVGException("given radial gradient does not have colors");
        }

        double cx = Double.parseDouble(getAttribute(Enumerations.SvgAttribute.CENTER_X.getName()));
        double cy = Double.parseDouble(getAttribute(Enumerations.SvgAttribute.CENTER_Y.getName()));

        double fx = getAttributes().containsKey(Enumerations.SvgAttribute.FOCUS_X.getName()) ? Double.parseDouble(getAttribute(Enumerations.SvgAttribute.FOCUS_X.getName())) : cx;
        double fy = getAttributes().containsKey(Enumerations.SvgAttribute.FOCUS_Y.getName()) ? Double.parseDouble(getAttribute(Enumerations.SvgAttribute.FOCUS_Y.getName())) : cy;

        double diffX = fx - cx;
        double diffY = fy - cy;

        double distance = diffX != 0 && diffY != 0 ? Math.hypot(diffX, diffY) : 0;
        double angle = diffX != 0 && diffY != 0 ? Math.atan2(diffY, diffX) : 0;

        return new javafx.scene.paint.RadialGradient(angle, distance, cx, cy, Double.parseDouble(getAttribute(Enumerations.SvgAttribute.RADIUS.getName())), false, CycleMethod.NO_CYCLE, stops);
    }

    //endregion
}
