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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.utils.SVGUtil;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Xyanid on 16.10.2016.
 */
@RunWith (MockitoJUnitRunner.class)
public class SVGAttributeTypePaintTest {

    // region Fields

    @Mock
    private SVGDocumentDataProvider dataProvider;

    private SVGAttributeTypePaint cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypePaint(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * Test that the opacity is altered as expected.
     */
    @Test
    public void opacityWillBeAppliedToColorCorrectly() {

        final Color red = Color.RED;

        SVGUtil.applyOpacity(red, 0.25d);

        assertEquals(0.25d, ((Color) SVGUtil.applyOpacity(red, 0.25d)).getOpacity(), 0.01d);
    }

    //endregion
}