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

import de.saxsys.svgfx.core.SVGDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

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
    public void ensureNoResultIsCreated() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGRoot root = new SVGRoot("svg", attributes, null, new SVGDataProvider());

        Assert.assertNull(root.getResult());
    }
}
