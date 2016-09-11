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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

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

        Assert.assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.data.put("test", FACTORY.createElement("rect", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.data.remove("test");

        Assert.assertEquals(0, DATA_PROVIDER.getUnmodifiableData().size());
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getStyles()} is set correctly when adding and removing data.
     */
    @Test
    public void testGetStyles() {

        DATA_PROVIDER.styles.add(new SVGCssStyle("test", DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getStyles().size());

        DATA_PROVIDER.styles.remove(new SVGCssStyle("test", DATA_PROVIDER));

        Assert.assertEquals(0, DATA_PROVIDER.getStyles().size());
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#hasData(String)} works as intended.
     */
    @Test
    public void testHasData() {
        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertTrue(DATA_PROVIDER.hasData("test"));

        DATA_PROVIDER.data.remove("test");

        Assert.assertFalse(DATA_PROVIDER.hasData("test"));
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#setData(String, de.saxsys.svgfx.core.elements.SVGElementBase)} works as intended and adds and overwrites elements in
     * {@link SVGDocumentDataProvider#data}.
     */
    @Test
    public void testSetData() {
        DATA_PROVIDER.setData("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertNotNull(DATA_PROVIDER.getData(SVGCircle.class, "test"));

        DATA_PROVIDER.setData("test", FACTORY.createElement("style", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertNotNull(DATA_PROVIDER.getData(SVGStyle.class, "test"));
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getData(Class, String)} works as intended.
     */
    @Test
    public void testGetData() {
        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertNotNull(DATA_PROVIDER.getData(SVGCircle.class, "test"));

        Assert.assertNull(DATA_PROVIDER.getData(SVGRectangle.class, "test"));

        DATA_PROVIDER.data.remove("test");

        Assert.assertNull(DATA_PROVIDER.getData(SVGCircle.class, "test"));
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#clear()} works as intended.
     */
    @Test
    public void testClear() {

        DATA_PROVIDER.data.put("test", FACTORY.createElement("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.clear();

        Assert.assertEquals(0, DATA_PROVIDER.getUnmodifiableData().size());
    }

    //endregion
}