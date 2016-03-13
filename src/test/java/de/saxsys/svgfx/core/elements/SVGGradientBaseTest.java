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
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.elements.mocks.SVGGradientBaseMock;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This test will ensure that {@link SVGGradientBase} works as intended.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGGradientBaseTest {

    /**
     * Ensure that stop elements are retrieved from a referenced element.
     */
    @Test
    public void ensureStopElementsAreRetrievedFromReferencedElement() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getValue(1)).thenReturn("red");

        SVGDataProvider dataProvider = new SVGDataProvider();

        SVGElementBase elementBase = Mockito.mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop("stop", attributes, elementBase, dataProvider));

        Mockito.when(attributes.getValue(0)).thenReturn("0.2");
        Mockito.when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop("stop", attributes, elementBase, dataProvider));

        Mockito.when(elementBase.getChildren()).thenReturn(stops);

        Mockito.when(attributes.getLength()).thenReturn(1);
        Mockito.when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");

        SVGGradientBaseMock gradient = new SVGGradientBaseMock("gradientbase", attributes, null, dataProvider);

        List<Stop> actualStops = gradient.getStops();

        Assert.assertEquals(2, actualStops.size());

        Assert.assertEquals(0.1d, actualStops.get(0).getOffset(), 0.01d);
        Assert.assertEquals(Color.RED, actualStops.get(0).getColor());

        Assert.assertEquals(0.2d, actualStops.get(1).getOffset(), 0.01d);
        Assert.assertEquals(Color.BLUE, actualStops.get(1).getColor());
    }

    /**
     * Ensure that stop elements are retrieved from the element itself even when referenced elements are available
     */
    @Test
    public void ensureOwnStopElementsArePreferred() {

        Attributes attributes = Mockito.mock(Attributes.class);

        SVGDataProvider dataProvider = new SVGDataProvider();

        SVGElementBase elementBase = Mockito.mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        List<SVGElementBase> stops = new ArrayList<>();

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getValue(1)).thenReturn("red");

        stops.add(new SVGStop("stop", attributes, elementBase, dataProvider));

        Mockito.when(attributes.getValue(0)).thenReturn("0.2");
        Mockito.when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop("stop", attributes, elementBase, dataProvider));

        Mockito.when(elementBase.getChildren()).thenReturn(stops);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");

        SVGGradientBaseMock gradient = new SVGGradientBaseMock("gradientbase", attributes, null, dataProvider);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("0.3");
        Mockito.when(attributes.getValue(1)).thenReturn("green");

        gradient.getChildren().add(new SVGStop("stop", attributes, gradient, dataProvider));

        Mockito.when(attributes.getValue(0)).thenReturn("0.4");
        Mockito.when(attributes.getValue(1)).thenReturn("yellow");

        gradient.getChildren().add(new SVGStop("stop", attributes, gradient, dataProvider));

        List<Stop> actualStops = gradient.getStops();

        Assert.assertEquals(2, actualStops.size());

        Assert.assertEquals(0.3d, actualStops.get(0).getOffset(), 0.01d);
        Assert.assertEquals(Color.GREEN, actualStops.get(0).getColor());

        Assert.assertEquals(0.4d, actualStops.get(1).getOffset(), 0.01d);
        Assert.assertEquals(Color.YELLOW, actualStops.get(1).getColor());
    }

    /**
     * Ensure that stop elements are retrieved from the element itself
     */
    @Test
    public void ensureStopElementsAreRetrievedFromSelf() {

        SVGDataProvider dataProvider = new SVGDataProvider();

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        Mockito.when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");

        SVGGradientBaseMock gradient = new SVGGradientBaseMock("gradientbase", attributes, null, dataProvider);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getValue(1)).thenReturn("red");

        gradient.getChildren().add(new SVGStop("stop", attributes, gradient, dataProvider));

        Mockito.when(attributes.getValue(0)).thenReturn("0.2");
        Mockito.when(attributes.getValue(1)).thenReturn("blue");

        gradient.getChildren().add(new SVGStop("stop", attributes, gradient, dataProvider));

        Mockito.when(attributes.getLength()).thenReturn(1);
        Mockito.when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");

        List<Stop> actualStops = gradient.getStops();

        Assert.assertEquals(2, actualStops.size());

        Assert.assertEquals(0.1d, actualStops.get(0).getOffset(), 0.01d);
        Assert.assertEquals(Color.RED, actualStops.get(0).getColor());

        Assert.assertEquals(0.2d, actualStops.get(1).getOffset(), 0.01d);
        Assert.assertEquals(Color.BLUE, actualStops.get(1).getColor());
    }
}
