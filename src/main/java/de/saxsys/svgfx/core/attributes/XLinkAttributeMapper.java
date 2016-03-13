package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.content.SVGContentTypeBase;
import de.saxsys.svgfx.core.content.SVGContentTypeString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

/**
 * This class determines which svg xlink attributes are mapped to the desired {@link SVGContentTypeBase}.
 *
 * @author Xyanid on 09.03.2016.
 */
public class XLinkAttributeMapper extends BaseAttributeMapper<SVGDataProvider> {

    // region Constants

    /**
     * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
     */
    public static final XLinkAttributeMapper XLINK_HREF = new XLinkAttributeMapper("xlink:href", SVGContentTypeString::new);

    /**
     * Contains all the values that are available for this attribute class.
     */
    public static final ArrayList<XLinkAttributeMapper> VALUES = new ArrayList<>(Collections.singletonList(XLINK_HREF));

    // endregion

    //region Constructor

    /**
     * {@inheritDoc}
     */
    public XLinkAttributeMapper(final String name, final Function<SVGDataProvider, ? extends SVGContentTypeBase> contentTypeCreator) {
        super(name, contentTypeCreator);
    }

    //endregion
}