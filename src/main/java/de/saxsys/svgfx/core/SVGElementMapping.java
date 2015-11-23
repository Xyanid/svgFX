package de.saxsys.svgfx.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface to be applied to all elements which are actual svg elements.
 * Created by Xyanid on 01.11.2015.
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public @interface SVGElementMapping {

    /**
     * Returns the name of the svg element this element corresponds to.
     *
     * @return the name of the svg element this element corresponds to
     */
    String value();
}
