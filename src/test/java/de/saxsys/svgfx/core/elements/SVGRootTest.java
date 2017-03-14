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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg svg elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGRootTest {

    /**
     * Ensures that no result is created when calling SVGRoot
     */
    @Test
    public void noResultIsCreatedWhenTheResultIsRequested() throws SAXException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGRoot root = new SVGRoot(SVGRoot.ELEMENT_NAME, attributes, new SVGDocumentDataProvider());

        assertNull(root.getResult());
    }

    /**
     * When the element has been read the {@link SVGDocumentDataProvider} will contain the element.
     */
    @Test
    public void whenTheElementIsFinishedProcessingTheDocumentDataLoaderWillContainTheElement() throws SAXException, SVGException {

        final Attributes attributes = mock(Attributes.class);
        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        when(attributes.getLength()).thenReturn(0);

        final SVGRoot root = new SVGRoot(SVGRoot.ELEMENT_NAME, attributes, dataProvider);

        root.startProcessing();

        final Optional<SVGRoot> rootData = dataProvider.getData(SVGRoot.ELEMENT_NAME, SVGRoot.class);

        assertTrue(rootData.isPresent());
        assertSame(root, rootData.get());
    }
}
