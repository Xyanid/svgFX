package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGShapeBase;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.css.core.CssStyle;
import javafx.scene.shape.SVGPath;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 * Created by Xyanid on 25.10.2015.
 */
@SVGElementMapping("path") public class Path extends SVGShapeBase<SVGPath> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Path(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override protected final SVGPath createResultInternal() {
        SVGPath result = new javafx.scene.shape.SVGPath();

        result.contentProperty().set(getAttribute(Enumerations.SvgAttribute.D.getName()));

        return result;
    }

    /**
     * {@inheritDoc}
     * Applies the file rule to the path.
     */
    @Override protected final void initializeResult(SVGPath path) {
        super.initializeResult(path);

        CssStyle style = getCssStyle();

        if (style != null) {
            //apply the fill rule if need be
            String ruleValue = style.getCssStyleDeclaration().getPropertyValue(Enumerations.CssStyleProperty.FILL_RULE.getName());
            if (!StringUtils.isEmpty(ruleValue)) {
                for (Enumerations.FillRuleMapping fillRule : Enumerations.FillRuleMapping.values()) {
                    if (fillRule.getName().equals(ruleValue)) {
                        path.setFillRule(fillRule.getRule());
                    }
                }
            }
        }
    }

    //endregion
}
