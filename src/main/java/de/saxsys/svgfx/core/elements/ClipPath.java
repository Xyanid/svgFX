package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGNodeBase;
import javafx.scene.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents a clipPath element from svg Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("clipPath") public class ClipPath extends SVGNodeBase<Node> {

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public ClipPath(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region SVGElementBase

    @Override protected final Node createResultInternal() throws SVGException {

        Node result = null;

        if (getChildren().size() > 0) {
            try {
                result = (Node) getChildren().get(0).getResult();
            } catch (SAXException e) {
                throw new SVGException(e);
            }
        }

        return result;
    }

    // endregion
}
