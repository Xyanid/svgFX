package de.saxsys.svgfx.core.elements.utils;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import org.xml.sax.Attributes;

/**
 * @author Xyanid on 19.03.2016.
 */
@FunctionalInterface
public interface SVGElementCreator<TResult extends SVGElementBase> {

    TResult apply(final String name, final Attributes attributes, final SVGElementBase parent, final SVGDataProvider dataProvider);
}
