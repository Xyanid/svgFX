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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.interfaces.SVGSupplier;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that {@link SVGGradientBase} works as intended.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("unchecked")
@RunWith (MockitoJUnitRunner.class)
public final class SVGGradientBaseTest {

    /**
     * Ensure that stop elements are retrieved from a referenced element.
     */
    @Test
    public void stopElementsAreRetrievedFromReferencedElement() throws SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");

        final SVGGradientBase<Color> gradient = new SVGGradientBase<Color>("gradientbase", attributes, dataProvider) {
            @Override
            protected Color createResult(SVGCssStyle ownStyle) throws SVGException {
                return null;
            }

            @Override
            public Color createResult(SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox, Transform elementTransform) throws SVGException {
                return null;
            }
        };

        final List<Stop> actualStops = gradient.getStops();

        assertEquals(2, actualStops.size());

        assertEquals(0.1d, actualStops.get(0).getOffset(), 0.01d);
        assertEquals(Color.RED, actualStops.get(0).getColor());

        assertEquals(0.2d, actualStops.get(1).getOffset(), 0.01d);
        assertEquals(Color.BLUE, actualStops.get(1).getColor());
    }

    /**
     * Ensure that stop elements are retrieved from the element itself even when referenced elements are available
     */
    @Test
    public void ownStopElementsArePreferredBeforeReferencedStops() throws SVGException {

        final Attributes attributes = mock(Attributes.class);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");

        final SVGGradientBase<Color> gradient = new SVGGradientBase<Color>("gradientbase", attributes, dataProvider) {
            @Override
            protected Color createResult(SVGCssStyle ownStyle) throws SVGException {
                return null;
            }

            @Override
            public Color createResult(SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox, Transform elementTransform) throws SVGException {
                return null;
            }
        };

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.3");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("green");

        ((List<SVGElementBase<?>>) Whitebox.getInternalState(gradient, "children")).add(new SVGStop("stop", attributes, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.4");
        when(attributes.getValue(1)).thenReturn("yellow");

        ((List<SVGElementBase<?>>) Whitebox.getInternalState(gradient, "children")).add(new SVGStop("stop", attributes, dataProvider));

        final List<Stop> actualStops = gradient.getStops();

        assertEquals(2, actualStops.size());

        assertEquals(0.3d, actualStops.get(0).getOffset(), 0.01d);
        assertEquals(Color.GREEN, actualStops.get(0).getColor());

        assertEquals(0.4d, actualStops.get(1).getOffset(), 0.01d);
        assertEquals(Color.YELLOW, actualStops.get(1).getColor());
    }

    /**
     * Ensure that stop elements are retrieved from the element itself
     */
    @Test
    public void stopElementsAreRetrievedFromTheElementItselfIfTheAreReferenced() throws SVGException {

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");

        final SVGGradientBase<Color> gradient = new SVGGradientBase<Color>("gradientbase", attributes, dataProvider) {
            @Override
            protected Color createResult(SVGCssStyle ownStyle) throws SVGException {
                return null;
            }

            @Override
            public Color createResult(SVGSupplier<SVGAttributeTypeRectangle.SVGTypeRectangle> boundingBox, Transform elementTransform) throws SVGException {
                return null;
            }
        };

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        ((List<SVGElementBase<?>>) Whitebox.getInternalState(gradient, "children")).add(new SVGStop("stop", attributes, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        ((List<SVGElementBase<?>>) Whitebox.getInternalState(gradient, "children")).add(new SVGStop("stop", attributes, dataProvider));

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");

        final List<Stop> actualStops = gradient.getStops();

        assertEquals(2, actualStops.size());

        assertEquals(0.1d, actualStops.get(0).getOffset(), 0.01d);
        assertEquals(Color.RED, actualStops.get(0).getColor());

        assertEquals(0.2d, actualStops.get(1).getOffset(), 0.01d);
        assertEquals(Color.BLUE, actualStops.get(1).getColor());
    }
}