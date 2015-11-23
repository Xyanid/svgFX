package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import javafx.scene.Node;
import org.xml.sax.Attributes;

/**
 * This class represents a stop element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("defs") public class Defs extends SVGElementBase<Node> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Defs(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override protected final Node createResultInternal() throws SVGException {
        return null;
    }

    @Override protected final void initializeResult(Node node) throws SVGException {

    }

    //endregion
}
