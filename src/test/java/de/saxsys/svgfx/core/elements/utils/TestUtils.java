package de.saxsys.svgfx.core.elements.utils;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import org.junit.Assert;
import org.xml.sax.Attributes;

/**
 * @author Xyanid on 19.03.2016.
 */
public class TestUtils {

    /**
     * Ensures that element creation fails with the desired exception.
     */
    public static <TElement extends SVGElementBase> void assertCreationFails(final SVGElementCreator<TElement> creator,
                                                                             final String name,
                                                                             final Attributes attributes,
                                                                             final SVGElementBase parent,
                                                                             final SVGDataProvider dataProvider,
                                                                             final Class<TElement> elementClass,
                                                                             final Class<? extends Exception> exception) {
        try {
            creator.apply(name, attributes, parent, dataProvider);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains(elementClass.getName()));
            Assert.assertEquals(exception, e.getCause().getClass());
        }
    }

    /**
     * Asserts that the creation of teh result fails.
     */
    public static void assertResultFails(final SVGElementBase element, final Class<? extends Exception> exception) {
        try {
            element.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(element.getClass().getName()));
            Assert.assertEquals(exception, e.getCause().getClass());
        }
    }
}
