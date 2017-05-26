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
import static de.saxsys.svgfx.core.utils.TestUtils.assertResultFails;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
    public void allAttributesAreParsedCorrectly() throws SAXException {

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

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.START_X.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.SPREAD_METHOD.getName());
        when(attributes.getValue(4)).thenReturn(CycleMethodMapping.REPEAT.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGLinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider);

        assertEquals(0.1d, gradient.getResult().getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.15d, gradient.getResult().getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.9d, gradient.getResult().getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.95d, gradient.getResult().getEndY(), MINIMUM_DEVIATION);
        assertEquals(CycleMethod.REPEAT, gradient.getResult().getCycleMethod());
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test
    public void whenStopsAreMissingAnSVGExceptionIsThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        assertResultFails(SVGLinearGradient::new,
                          SVGLinearGradient.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));
    }

    /**
     * Ensures that the an {@link SVGException} if the {@link GradientUnit#USER_SPACE_ON_USE} attribute is used but no {@link SVGShapeBase} has been
     * provided.
     */
    @Test
    public void whenTheGradientIsInUserSpaceButNoShapeHasBeenProvidedAnSVGExceptionIsThrown() {

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
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.START_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.END_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.END_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(4)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        assertResultFails(SVGLinearGradient::new,
                          SVGLinearGradient.ELEMENT_NAME,
                          attributes,
                          new SVGDocumentDataProvider(),
                          exception -> assertThat(exception.getCause(), instanceOf(SVGException.class)));
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is no gradient transform and the values are in {@link GradientUnit#USER_SPACE_ON_USE}.
     */
    @Test
    public void whenGradientUnitsAreProvidedAsAbsoluteAndNoGradientTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

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
        when(attributes.getValue(4)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("150");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, null);

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(0.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is a gradient transform and the values are in {@link GradientUnit#USER_SPACE_ON_USE}.
     */
    @Test
    public void whenGradientUnitsAreProvidedAsAbsoluteAndAGradientTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

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
        when(attributes.getValue(4)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(5)).thenReturn("rotate(-90 100 175) translate(25 25)");
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("150");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("200");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, null);

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
    public void whenGradientUnitsAreProvidedAsRelativeAndNoGradientTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

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

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, null);

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
    public void whenGradientUnitsAreProvidedAsRelativeAndAGradientTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

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

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, null);

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
    public void whenGradientUnitsAreProvidedAsAbsoluteAndNoGradientTransformButAnElementTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

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
        when(attributes.getValue(4)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("75");
        boundingBox.getMaxY().setText("125");


        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, new Translate(0.0d, 25.0d));

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
    public void whenGradientUnitsAreProvidedAsAbsoluteAndAGradientTransformAsWellAsAnElementTransformIsSpecifiedTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

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
        when(attributes.getValue(4)).thenReturn(GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_TRANSFORM.getName());
        when(attributes.getValue(5)).thenReturn("translate(12.5 12.5)");
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("37.5");
        boundingBox.getMaxX().setText("137.5");
        boundingBox.getMinY().setText("87.5");
        boundingBox.getMaxY().setText("187.5");

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, new Translate(12.5d, 12.5d));

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
    public void whenGradientUnitsAreProvidedAsRelativeAndNoGradientTransformButAnElementTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

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

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, new Translate(12.5d, 0.0d));

        assertEquals(0.75d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.25d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensures that the values of the gradient will be affected and converted in to relative coordinates if there is a gradient transform as well as an element transform and the values are in
     * {@link GradientUnit#OBJECT_BOUNDING_BOX}.
     */
    @Test
    public void whenGradientUnitsAreProvidedAsRelativeAndAGradientTransformAsWellAsAnElementTransformIsSpecifiedTheValuesOfTheGradientAreNotAdjusted() throws SVGException, SAXException {

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

        final LinearGradient gradient = new SVGLinearGradient(SVGLinearGradient.ELEMENT_NAME, attributes, dataProvider).createResult(() -> boundingBox, new Translate(12.5d, 25.0d));

        assertEquals(0.5d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.75d, gradient.getEndY(), MINIMUM_DEVIATION);


    }

}