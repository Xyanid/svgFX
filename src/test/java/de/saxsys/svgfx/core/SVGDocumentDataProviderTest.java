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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGElementFactory;
import de.saxsys.svgfx.core.elements.SVGRectangle;
import de.saxsys.svgfx.core.elements.SVGStyle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Ensure that {@link SVGDocumentDataProvider} works as intended.
 *
 * @author Xyanid on 14.12.2015.
 */
public class SVGDocumentDataProviderTest {

    //region Fields

    private static final SVGDocumentDataProvider DATA_PROVIDER = new SVGDocumentDataProvider();

    private static final Attributes ATTRIBUTES = Mockito.mock(Attributes.class);

    private static final SVGElementFactory FACTORY = new SVGElementFactory();

    //endregion

    //region Tests

    @Before
    public void setUp() {
        Mockito.when(ATTRIBUTES.getLength()).thenReturn(0);
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getUnmodifiableData()} is set correctly when adding and removing data.
     */
    @Test
    public void testGetUnmodifiableData() throws Exception {

        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.data.put("test", FACTORY.createElement("rect", ATTRIBUTES, null, DATA_PROVIDER));

        assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.data.remove("test");

        assertEquals(0, DATA_PROVIDER.getUnmodifiableData().size());
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getStyles()} is set correctly when adding and removing data.
     */
    @Test
    public void testGetStyles() {

        DATA_PROVIDER.styles.add(new SVGCssStyle("test", DATA_PROVIDER));

        assertEquals(1, DATA_PROVIDER.getStyles().size());

        DATA_PROVIDER.styles.remove(new SVGCssStyle("test", DATA_PROVIDER));

        assertEquals(0, DATA_PROVIDER.getStyles().size());
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#hasData(String)} works as intended.
     */
    @Test
    public void testHasData() {
        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        assertTrue(DATA_PROVIDER.hasData("test"));

        DATA_PROVIDER.data.remove("test");

        assertFalse(DATA_PROVIDER.hasData("test"));
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#setData(String, de.saxsys.svgfx.core.elements.SVGElementBase)} works as intended and adds and overwrites elements in
     * {@link SVGDocumentDataProvider#data}.
     */
    @Test
    public void testSetData() {
        DATA_PROVIDER.setData("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        assertNotNull(DATA_PROVIDER.getData("test", SVGCircle.class));

        DATA_PROVIDER.setData("test", FACTORY.createElement("style", ATTRIBUTES, null, DATA_PROVIDER));

        assertNotNull(DATA_PROVIDER.getData("test", SVGStyle.class));
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getData(String, Class)} works as intended.
     */
    @Test
    public void testGetData() {
        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        assertNotNull(DATA_PROVIDER.getData("test", SVGCircle.class));

        assertNull(DATA_PROVIDER.getData("test", SVGRectangle.class));

        DATA_PROVIDER.data.remove("test");

        assertNull(DATA_PROVIDER.getData("test", SVGCircle.class));
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#clear()} works as intended.
     */
    @Test
    public void testClear() {

        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.clear();

        assertEquals(0, DATA_PROVIDER.getUnmodifiableData().size());
    }

    //endregion
}