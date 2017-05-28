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
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.definitions.enumerations.CycleMethodMapping;
import de.saxsys.svgfx.core.definitions.enumerations.GradientUnit;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.transform.Translate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg radial gradient elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("unchecked")
public final class SVGRadialGradientTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException, SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(8);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("25");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("50");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("50");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("75");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("75");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.SPREAD_METHOD.getName());
        when(attributes.getValue(5)).thenReturn(CycleMethodMapping.REPEAT.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");
        when(attributes.getQName(7)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(7)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());

        final SVGRadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider);

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(dataProvider);
        boundingBox.getMinX().setText("0");
        boundingBox.getMinY().setText("0");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMaxY().setText("100");

        final RadialGradient result = gradient.createResult(boundingBox, null);

        Assert.assertEquals(0.25d, result.getCenterX(), MINIMUM_DEVIATION);
        Assert.assertEquals(0.50d, result.getCenterY(), MINIMUM_DEVIATION);
        Assert.assertEquals(0.47d, result.getFocusDistance(), MINIMUM_DEVIATION);
        Assert.assertEquals(45, result.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(CycleMethod.REPEAT, result.getCycleMethod());
    }

    /**
     * Ensures that the an {@link SAXException} is thrown if there are no stops elements.
     */
    @Test (expected = SVGException.class)
    public void whenStopsAreMissingAnSVGExceptionIsThrown() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);
        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        when(attributes.getLength()).thenReturn(0);

        final SVGRadialGradient cut = new SVGRadialGradient(SVGLinearGradient.ELEMENT_NAME,
                                                            attributes,
                                                            dataProvider);

        cut.createResult(new SVGAttributeTypeRectangle.SVGTypeRectangle(dataProvider), null);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is no gradient transform and the values are in {@link GradientUnit#USER_SPACE_ON_USE}.
     */
    @Test
    public void whenGradientUnitsAreAbsoluteAndNoGradientTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("100");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("175");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("125");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("125");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("50");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.5d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(1.0, gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d - 0.75d, 0.75d - 0.5d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is a gradient transform and the values are in {@link GradientUnit#USER_SPACE_ON_USE}.
     */
    @Test
    public void whenGradientUnitsAreAbsoluteAndAGradientTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(8);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("75");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("125");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("100");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("150");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("50");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(6)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(6)).thenReturn("translate(-150 -125) matrix(2 0 0 1 0 0) translate(25 100)");
        when(attributes.getQName(7)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(7)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.0d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.0d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(Math.hypot(0.5, 0.25), gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d, 0.5d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensure that the values of the gradient will not be affected if there is no gradient transform and the values are in {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreRelativeAndNoGradientTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.5");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.75");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.75");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.25");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(dataProvider);
        boundingBox.getMinX().setText("0");
        boundingBox.getMinY().setText("0");
        boundingBox.getMaxX().setText("200");
        boundingBox.getMaxY().setText("100");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.5d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(1.0, gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d - 0.75d, 0.75d - 0.5d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensure that the values of the gradient will not be affected if there is a gradient transform and the values are in {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreRelativeAndAGradientTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(8);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.25");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.25");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.5");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.5");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(6)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(6)).thenReturn("translate(-150 -125) matrix(2 0 0 1 0 0) translate(25 100)");
        when(attributes.getQName(7)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(7)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.0d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.0d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(Math.hypot(0.5, 0.25), gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d, 0.5d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is no gradient transform but an element transform and the values are in
     * {@link GradientUnit#USER_SPACE_ON_USE}.
     */
    @Test
    public void whenGradientUnitsAreAbsoluteAndNoGradientTransformButAnElementTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("100");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("175");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("125");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("125");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("100");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("75");
        boundingBox.getMaxX().setText("175");
        boundingBox.getMinY().setText("125");
        boundingBox.getMaxY().setText("225");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(-25.0d, -25.0d));

        assertEquals(0.25d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(Math.hypot(0.25, 0.5), gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.0d - 0.5d, 0.5d - 0.25d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is a gradient transform as well as an element transform and the values are in
     * {@link GradientUnit#USER_SPACE_ON_USE}.
     */
    @Test
    public void whenGradientUnitsAreAbsoluteAndAGradientTransformAsWellAsAnElementTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(8);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("75");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("125");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("100");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("150");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("100");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(6)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(6)).thenReturn("translate(25 0)");
        when(attributes.getQName(7)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(7)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("25");
        boundingBox.getMaxX().setText("125");
        boundingBox.getMinY().setText("75");
        boundingBox.getMaxY().setText("175");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(25d, 25d));

        assertEquals(0.75d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(Math.hypot(0.25, 0.25), gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d, 0.25d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensure that the values of the gradient will not be affected if there is no gradient transform but an element transform and the values are in {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreRelativeAndNoGradientTransformButAnElementTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.5");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.75");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.75");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.25");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("1.0");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(0.0d, 25.0d));

        assertEquals(0.5d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(Math.hypot(0.25, 0.5), gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d - 0.75d, 0.75d - 0.5d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getRadius(), MINIMUM_DEVIATION);
    }

    /**
     * Ensure that the values of the gradient will not be affected if there is a gradient transform as well as an element transform and the values are in {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreRelativeAndAGradientTransformAsWellAsAnElementTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjustedAccordingly()
            throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(8);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.25");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.25");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.5");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.5");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("1.0");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(6)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(6)).thenReturn("translate(12.5 0)");
        when(attributes.getQName(7)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(7)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(12.5d, 25.0d));

        assertEquals(0.375d, gradient.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getCenterY(), MINIMUM_DEVIATION);
        assertEquals(Math.hypot(0.25, 0.25), gradient.getFocusDistance(), MINIMUM_DEVIATION);
        assertEquals(Math.toDegrees(Math.atan2(0.25d, 0.25d)), gradient.getFocusAngle(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getRadius(), MINIMUM_DEVIATION);
    }


    /**
     * Providing a focus distance that is outside the radius will trim the focus distance to the radius.
     */
    @Test
    public void whenTheFocusDistanceIsOutOfTheRadiusItWillNotExceedTheRadius() throws SAXException, SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.5");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.5");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("1.0");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.0");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.SPREAD_METHOD.getName());
        when(attributes.getValue(5)).thenReturn(CycleMethodMapping.REPEAT.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGRadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, dataProvider);

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(dataProvider);
        boundingBox.getMinX().setText("0");
        boundingBox.getMinY().setText("0");
        boundingBox.getMaxX().setText("200");
        boundingBox.getMaxY().setText("100");

        assertEquals(1.0d, gradient.createResult(boundingBox, null).getFocusDistance(), MINIMUM_DEVIATION);
    }
}
