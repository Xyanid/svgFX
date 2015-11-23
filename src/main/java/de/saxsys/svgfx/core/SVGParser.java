package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.elements.ClipPath;
import de.saxsys.svgfx.core.elements.Defs;
import de.saxsys.svgfx.core.elements.Group;
import de.saxsys.svgfx.core.elements.Style;
import de.saxsys.svgfx.xml.core.SAXParser;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Node;
import org.xml.sax.SAXException;

/**
 * This parser is used to create SVG path data for javafx
 * Created by Xyanid on 24.10.2015.
 */
public class SVGParser extends SAXParser<javafx.scene.Group, SVGDataProvider, SVGElementCreator> {

    //region Constructor

    /**
     * Creates a new instance of the parser and uses the given elementCreator.
     *
     * @throws NoSuchMethodException thrown by the {@link SVGElementCreator} if an element does not have the required constructor
     */
    public SVGParser() {

        super(new SVGElementCreator(), new SVGDataProvider());
    }

    //endregion

    //region Override SAXParser

    @Override protected javafx.scene.Group enteringDocument() {
        return new javafx.scene.Group();
    }

    @Override protected void leavingDocument(final javafx.scene.Group result) {

    }

    @Override protected void consumeElementStart(final javafx.scene.Group result, final SVGDataProvider dataProvider, final ElementBase<SVGDataProvider, ?, ?> element) {

        //definitions will not be kept as children
        if (element instanceof Defs && element.getParent() != null) {
            element.getParent().getChildren().remove(element);
        }
    }

    @Override protected void consumeElementEnd(final javafx.scene.Group result, final SVGDataProvider dataProvider, final ElementBase<SVGDataProvider, ?, ?> element) throws SAXException {

        if (element.getParent() instanceof Defs) {
            dataProvider.getData().put(element.getAttributes().get(Enumerations.SvgAttribute.ID.getName()), (SVGElementBase) element);
        } else if (element instanceof Style) {
            dataProvider.getStyles().addAll(((Style) element).getResult());
        } else if (!((element.getParent() instanceof ClipPath) || (element.getParent() instanceof Group)) && element.getResult() instanceof Node) {
            result.getChildren().add((Node) element.getResult());
        }
    }

    //endregion
}
