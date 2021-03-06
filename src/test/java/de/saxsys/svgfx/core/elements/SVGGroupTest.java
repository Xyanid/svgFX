/*
 * Copyright 2015 - 2017 Xyanid
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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import javafx.scene.shape.Circle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static de.saxsys.svgfx.core.utils.TestUtils.getChildren;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg group elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ({"OptionalGetWithoutIsPresent", "ConstantConditions"})
@RunWith (MockitoJUnitRunner.class)
public final class SVGGroupTest {

    /**
     * Ensures elements within a group produce the correct output.
     */
    @Test
    public void groupResultContainsTheCorrectData() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");

        final SVGDocumentDataProvider provider = new SVGDocumentDataProvider();

        final SVGClipPath clipPath = new SVGClipPath("clipPath", attributes, provider);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(0)).thenReturn("50");

        getChildren(clipPath).add(new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, provider));

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(0)).thenReturn("25");

        getChildren(clipPath).add(new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, provider));

        assertEquals("test", clipPath.getAttributeHolder().getAttribute(CoreAttributeMapper.ID.getName(), SVGAttributeTypeString.class).get().getValue());
        Assert.assertNotNull(clipPath.getResult());

        assertEquals(2, clipPath.getResult().getChildren().size());

        assertThat(clipPath.getResult().getChildren().get(0), instanceOf(Circle.class));
        assertEquals(50.0d, ((Circle) clipPath.getResult().getChildren().get(0)).getRadius(), MINIMUM_DEVIATION);

        assertThat(clipPath.getResult().getChildren().get(1), instanceOf(Circle.class));
        assertEquals(25.0d, ((Circle) clipPath.getResult().getChildren().get(1)).getRadius(), MINIMUM_DEVIATION);
    }
}