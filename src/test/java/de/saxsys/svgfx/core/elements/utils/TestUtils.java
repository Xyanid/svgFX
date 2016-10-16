/*
 * Copyright 2015 - 2016 Xyanid
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package de.saxsys.svgfx.core.elements.utils;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
                                                                             final SVGDocumentDataProvider dataProvider,
                                                                             final Class<TElement> elementClass,
                                                                             final Class<? extends Exception> exception) {
        try {
            creator.apply(name, attributes, parent, dataProvider);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(elementClass.getName()));
            assertEquals(exception, e.getCause().getClass());
        }
    }

    /**
     * Asserts that the creation of the result fails.
     */
    public static void assertResultFails(final SVGElementBase element, final Class<? extends Exception> exception) {
        try {
            element.getResult();
            fail();
        } catch (final SAXException e) {
            assertTrue(e.getMessage().contains(element.getClass().getName()));
            assertEquals(exception, e.getCause().getClass());
        }
    }
}
