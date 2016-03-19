package de.saxsys.svgfx.core.elements.utils;

import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import org.junit.Assert;

/**
 * @author Xyanid on 19.03.2016.
 */
public class TestUtils {

    /**
     * Asserts that the creation of an svg elements fails and that it will throw the desired exception.
     */
    public static void assertExceptionContainsSVGElementName(final SVGElementBase element, final Class<? extends Exception> exception) {
        try {
            element.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(element.getClass().getName()));
            Assert.assertEquals(exception, e.getCause().getClass());
        }
    }
}
