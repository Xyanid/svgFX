package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import javafx.scene.Group;
import org.xml.sax.Attributes;

/**
 * This class represents the svg element from svg
 * Created by Xyanid on 24.10.2015.
 */
@SVGElementMapping("svg") public class Svg extends SVGElementBase<Group> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Svg(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected Group createResultInternal() throws SVGException {
        return null;
    }

    @Override protected void initializeResult(Group group) throws SVGException {

    }

    //endregion
}
