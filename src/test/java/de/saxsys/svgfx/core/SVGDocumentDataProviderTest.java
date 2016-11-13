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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Ensure that {@link SVGDocumentDataProvider} works as intended.
 *
 * @author Xyanid on 14.12.2015.
 */
@RunWith (MockitoJUnitRunner.class)
public class SVGDocumentDataProviderTest {

    // region Fields

    @Mock
    private Attributes attributes;

    private final SVGElementFactory factory = new SVGElementFactory();

    private SVGDocumentDataProvider cut;

    // endregion

    // region SetUp

    @Before
    public void setUp() {
        Mockito.when(attributes.getLength()).thenReturn(0);

        cut = new SVGDocumentDataProvider();
    }

    // endregion

    // region Test

    /**
     * Ensure that {@link SVGDocumentDataProvider#getUnmodifiableData()} is set correctly when adding and removing data.
     */
    @Test
    public void savingTheSameDataTwiceWillOverwriteTheOldData() throws Exception {

        cut.storeData("test", factory.createElement("circle", attributes, null, cut));

        assertEquals(1, cut.getUnmodifiableData().size());

        cut.storeData("test", factory.createElement("rect", attributes, null, cut));

        assertEquals(1, cut.getUnmodifiableData().size());
    }

    /**
     * When data is stored it is possible to retrieved it, data that is not saved can not be retrieved and once the {@link SVGDocumentDataProvider} is cleared, no data can be retrieved
     */
    @Test
    public void storedDataCanBeRetrievedButUnsavedDataCanNot() throws SVGException {

        cut.storeData("test", factory.createElement("circle", attributes, null, cut));

        assertTrue(cut.getData("test", SVGCircle.class).isPresent());

        assertFalse(cut.getData("test", SVGRectangle.class).isPresent());

        cut.clear();

        assertFalse(cut.getData("test", SVGCircle.class).isPresent());
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getUnmodifiableStyles()} is set correctly when adding and removing data.
     */
    @Test
    public void stylesCanBeAddedAndWillBeClearedWhenTheDocumentDataProviderIsCleared() {

        cut.addStyle(new SVGCssStyle("test", cut));

        assertEquals(1, cut.getUnmodifiableStyles().size());

        cut.clear();

        assertEquals(0, cut.getUnmodifiableStyles().size());
    }

    /**
     * Ensure that {@link SVGDocumentDataProvider#getUnmodifiableData()} is set correctly when adding and removing data.
     */
    @Test
    public void whenTheDocumentDataProviderIsClearedStoredDataAndStylesAreLost() throws Exception {

        cut.addStyle(new SVGCssStyle("test", cut));
        cut.storeData("test", factory.createElement("circle", attributes, null, cut));

        assertEquals(1, cut.getUnmodifiableData().size());
        assertEquals(1, cut.getUnmodifiableStyles().size());

        cut.clear();

        assertEquals(0, cut.getUnmodifiableData().size());
        assertEquals(0, cut.getUnmodifiableStyles().size());
    }

    // endregion
}