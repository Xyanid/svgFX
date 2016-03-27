package de.saxsys.svgfx.core.content;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.xml.attribute.AttributeHolder;

/**
 * @author Xyanid on 27.03.2016.
 */
public class SVGAttributeHolder extends AttributeHolder<SVGAttributeType> {

    //region Fields

    private SVGDataProvider dataProvider;

    //endregion

    // region Constructor

    public SVGAttributeHolder(final SVGDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // endregion

    //region Override AttributeHolder

    @Override
    public SVGAttributeType createAttributeType(final String name) {
        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(dataProvider);
            }
        }

        for (CoreAttributeMapper attribute : CoreAttributeMapper.VALUES) {
            if (attribute.getName().equals(name)) {
                return attribute.getContentTypeCreator().apply(dataProvider);
            }
        }

        return new SVGAttributeTypeString(dataProvider);
    }

    //endregion
}
