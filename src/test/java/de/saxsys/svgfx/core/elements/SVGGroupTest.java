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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import javafx.scene.shape.Circle;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * This test will ensure that svg group elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGGroupTest {

    /**
     * Ensures elements within a group produce the correct output.
     */
    @Test
    public void ensureGroupResultContainsTheCorrectData() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.ID.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("test");

        SVGDataProvider provider = new SVGDataProvider();

        SVGClipPath clipPath = new SVGClipPath("clipPath", attributes, null, provider);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("50");

        clipPath.getChildren().add(new SVGCircle("circle", attributes, clipPath, provider));

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("25");

        clipPath.getChildren().add(new SVGCircle("circle", attributes, clipPath, provider));

        Assert.assertEquals("test", clipPath.getAttribute(SVGElementBase.CoreAttribute.ID.getName()));
        Assert.assertNotNull(clipPath.getResult());

        Assert.assertEquals(2, clipPath.getResult().getChildren().size());

        Assert.assertEquals(Circle.class, clipPath.getResult().getChildren().get(0).getClass());
        Assert.assertEquals(50.0d, ((Circle) clipPath.getResult().getChildren().get(0)).getRadius(), 0.01d);

        Assert.assertEquals(Circle.class, clipPath.getResult().getChildren().get(1).getClass());
        Assert.assertEquals(25.0d, ((Circle) clipPath.getResult().getChildren().get(1)).getRadius(), 0.01d);
    }
}
