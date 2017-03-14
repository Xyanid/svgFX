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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.fail;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

/**
 * @author Xyanid on 19.03.2016.
 */
public class TestUtils {

    /**
     * Ensures that element creation fails with the desired exception.
     */
    public static <TElement extends SVGElementBase<?>> void assertResultFails(final SVGElementCreator<TElement> creator,
                                                                              final String name,
                                                                              final Attributes attributes,
                                                                              final SVGDocumentDataProvider dataProvider,
                                                                              final Consumer<SAXException> exceptionPredicate) {
        assertResultFails(creator.apply(name, attributes, dataProvider), exceptionPredicate);
    }

    /**
     * Asserts that the creation of the result fails.
     */
    public static void assertResultFails(final SVGElementBase element, final Consumer<SAXException> exceptionPredicate) {
        try {
            element.getResult();
            fail("Wanted to fail the retrieval of the result of element" + element.getName());
        } catch (final SAXException e) {
            exceptionPredicate.accept(e);
        }
    }

    @SuppressWarnings ("unchecked")
    public static <T extends SVGElementBase> List<T> getChildren(final SVGElementBase element) {
        return (List<T>) getInternalState(element, "children");
    }
}