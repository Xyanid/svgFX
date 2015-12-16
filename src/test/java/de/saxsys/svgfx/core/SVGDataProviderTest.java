/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.elements.Rectangle;
import de.saxsys.svgfx.core.elements.SVGCircle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * Ensure that {@link SVGDataProvider} works as intended.
 *
 * @author Xyanid on 14.12.2015.
 */
public class SVGDataProviderTest {

    //region Fields

    private static final SVGDataProvider DATA_PROVIDER = new SVGDataProvider();

    private static final Attributes ATTRIBUTES = Mockito.mock(Attributes.class);

    //endregion

    //region Tests

    @Before
    public void setUp() {
        Mockito.when(ATTRIBUTES.getLength()).thenReturn(0);
    }

    /**
     * Ensure that {@link SVGDataProvider#getUnmodifiableData()} is set correctly when adding and removing data.
     */
    @Test
    public void testGetUnmodifiableData() throws Exception {

        DATA_PROVIDER.data.put("test", new SVGCircle("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.data.put("test", new Rectangle("rect", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.data.remove("test");

        Assert.assertEquals(0, DATA_PROVIDER.getUnmodifiableData().size());
    }

    /**
     * Ensure that {@link SVGDataProvider#getStyles()} is set correctly when adding and removing data.
     */
    @Test
    public void testGetStyles() throws Exception {

        DATA_PROVIDER.styles.add(new SVGCssStyle("test", DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getStyles().size());

        DATA_PROVIDER.styles.remove(new SVGCssStyle("test", DATA_PROVIDER));

        Assert.assertEquals(0, DATA_PROVIDER.getStyles().size());
    }

    /**
     * Ensure that {@link SVGDataProvider#hasData(String)} works as intended.
     */
    @Test
    public void testHasData() throws Exception {
        DATA_PROVIDER.data.put("test", new SVGCircle("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertTrue(DATA_PROVIDER.hasData("test"));

        DATA_PROVIDER.data.remove("test");

        Assert.assertFalse(DATA_PROVIDER.hasData("test"));
    }

    /**
     * Ensure that {@link SVGDataProvider#getData(Class, String)} works as intended.
     */
    @Test
    public void testGetData() throws Exception {
        DATA_PROVIDER.data.put("test", new SVGCircle("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertNotNull(DATA_PROVIDER.getData(SVGCircle.class, "test"));

        Assert.assertNull(DATA_PROVIDER.getData(Rectangle.class, "test"));

        DATA_PROVIDER.data.remove("test");

        Assert.assertNull(DATA_PROVIDER.getData(SVGCircle.class, "test"));
    }

    /**
     * Ensure that {@link SVGDataProvider#clear()} works as intended.
     */
    @Test
    public void testClear() throws Exception {

        DATA_PROVIDER.data.put("test", new SVGCircle("circle", ATTRIBUTES, null, DATA_PROVIDER));

        Assert.assertEquals(1, DATA_PROVIDER.getUnmodifiableData().size());

        DATA_PROVIDER.clear();

        Assert.assertEquals(0, DATA_PROVIDER.getUnmodifiableData().size());
    }

    //endregion
}