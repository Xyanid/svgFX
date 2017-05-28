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
import javafx.scene.paint.LinearGradient;
import javafx.scene.transform.Translate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static de.saxsys.svgfx.core.definitions.enumerations.GradientUnit.USER_SPACE_ON_USE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg linear gradient elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("unchecked")
@RunWith (MockitoJUnitRunner.class)
public final class SVGLinearGradientTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException, SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        dataProvider.storeData("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("75");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("150");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("50");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.SPREAD_METHOD.getName());
        when(attributes.getValue(4)).thenReturn(CycleMethodMapping.REPEAT.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");
        when(attributes.getQName(6)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(6)).thenReturn(USER_SPACE_ON_USE.getName());

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(dataProvider);
        boundingBox.getMinX().setText("0");
        boundingBox.getMinY().setText("0");
        boundingBox.getMaxX().setText("200");
        boundingBox.getMaxY().setText("100");

        final SVGLinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider);

        final LinearGradient result = gradient.createResult(boundingBox, null);

        assertEquals(0.25d, result.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, result.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.75d, result.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, result.getEndY(), MINIMUM_DEVIATION);
        assertEquals(CycleMethod.REPEAT, result.getCycleMethod());
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test (expected = SVGException.class)
    public void whenStopsAreMissingAnSVGExceptionIsThrown() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);
        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        when(attributes.getLength()).thenReturn(0);

        final SVGLinearGradient cut = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME,
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

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();
        dataProvider.storeData("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("75");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("125");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("50");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("150");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("150");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndY(), MINIMUM_DEVIATION);
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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("75");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("150");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("125");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("200");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(5)).thenReturn("rotate(-90 100 175) translate(25 25)");
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is no gradient transform and the values are in
     * {@link GradientUnit#OBJECT_BOUNDING_BOX}.
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

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("0.75");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.25");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.85");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.5");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("150");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.75d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.85d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is a gradient transform and the values are in
     * {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreRelativeAndAGradientTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

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
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("0.25");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.5");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.75");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("1");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(5)).thenReturn("rotate(-90 100 175) translate(25 25)");
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, null);

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getEndY(), MINIMUM_DEVIATION);
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

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("75");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("100");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("50");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("125");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("75");
        boundingBox.getMaxY().setText("125");


        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(0.0d, 25.0d));

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndY(), MINIMUM_DEVIATION);
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

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("50");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("125");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("100");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("175");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(5)).thenReturn("translate(12.5 12.5)");
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("37.5");
        boundingBox.getMaxX().setText("137.5");
        boundingBox.getMinY().setText("87.5");
        boundingBox.getMaxY().setText("187.5");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(12.5d, 12.5d));

        assertEquals(0.25d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is no gradient transform but an element transform and the values are in
     * {@link GradientUnit#OBJECT_BOUNDING_BOX}.
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

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("0.5");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.25");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.75");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.5");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("150");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(12.5d, 0.0d));

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is a gradient transform as well as an element transform and the values are in
     * {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreRelativeAndAGradientTransformAsWellAsAnElementTransformIsSpecifiedTheValuesOfTheGradientAreAdjusted() throws SVGException, SAXException {

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
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("0.25");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.25");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.75");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.5");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(GradientUnit.OBJECT_BOUNDING_BOX.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(5)).thenReturn("translate(12.5 0)");
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(boundingBox, new Translate(12.5d, 25.0d));

        assertEquals(0.375d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.875d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getEndY(), MINIMUM_DEVIATION);
    }
}