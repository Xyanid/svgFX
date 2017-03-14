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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.enumerations.FillRuleMapping;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Transform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Test to ensure the base functionality of {@link SVGElementBase}. Note that this test does not use mocks for the {@link SVGElementBase} since it would be
 * too much effort to train them.
 *
 * @author Xyanid on 28.11.2015.
 */
@SuppressWarnings ({"OptionalGetWithoutIsPresent", "unchecked"})
@RunWith (MockitoJUnitRunner.class)
public class SVGElementBaseTest {

    // region Classes

    private static class SVGElementBaseMock extends SVGElementBase<Object> {

        /**
         * Creates a new instance of he element using the given attributes, parent and dataProvider.
         *
         * @param attributes   attributes of the element
         * @param dataProvider dataprovider to be used
         *
         * @throws IllegalArgumentException if either value or dataProvider are null
         */
        protected SVGElementBaseMock(final Attributes attributes, final SVGDocumentDataProvider dataProvider) throws IllegalArgumentException {
            super("Test", attributes, dataProvider);
        }

        @Override
        protected Object createResult(final SVGCssStyle styleSupplier) throws SVGException {
            return null;
        }

        @Override
        protected void initializeResult(final Object o, final SVGCssStyle styleSupplier) throws SVGException {

        }

        @Override
        public boolean keepElement() {
            return false;
        }

        @Override
        public void startProcessing() throws SAXException {

        }

        @Override
        public void processCharacterData(final char[] ch, final int start, final int length) throws SAXException {

        }

        @Override
        public void endProcessing() throws SAXException {

        }
    }

    // endregion

    //region Tests

    //region Test for Style resolving

    /**
     * Ensure that presentation attributes create a valid css style.
     */
    @Test
    public void presentationAttributesWillCreateAValidStyle() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(PresentationAttributeMapper.VALUES.size());

        int counter = 0;

        for (final PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            String value = "1.0";

            if (attribute == PresentationAttributeMapper.FILL_RULE) {
                value = FillRuleMapping.EVEN_ODD.getName();
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                value = "#808080";
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                value = "10,5";
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                value = StrokeLineCap.BUTT.name().toLowerCase();
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                value = StrokeLineJoin.BEVEL.name().toLowerCase();
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                value = StrokeType.OUTSIDE.name().toLowerCase();
            }

            when(attributes.getQName(counter)).thenReturn(attribute.getName());
            when(attributes.getValue(counter++)).thenReturn(value);
        }

        final SVGCssStyle style = getStyle(new SVGElementBaseMock(attributes, new SVGDocumentDataProvider()));

        assertNotNull(style);

        for (final PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            Object value = 1.0d;

            if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                value = "1.0";
            } else if (attribute == PresentationAttributeMapper.FILL_RULE) {
                value = FillRuleMapping.EVEN_ODD.getRule();
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                value = Color.web("#808080");
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                final SVGAttributeTypeLength[] result = (SVGAttributeTypeLength[]) style.getAttributeHolder().getAttribute(attribute.getName()).get().getValue();
                assertEquals(2, result.length);
                assertEquals(10.0d, result[0].getValue(), 0.01d);
                assertEquals(5.0d, result[1].getValue(), 0.01d);
                continue;
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                value = StrokeLineCap.BUTT;
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                value = StrokeLineJoin.BEVEL;
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                value = StrokeType.OUTSIDE;
            }

            assertEquals(value, style.getAttributeHolder().getAttribute(attribute.getName()).get().getValue());
        }
    }

    /**
     * Ensures that {@link PresentationAttributeMapper}s will be preferred and own {@link SVGCssStyle} attributes are kept.
     */
    @Test
    public void aPresentationStyleIsAlwaysPreferredBeforeAnOwnStyleButOwnStyleAttributesAreAddedIfTheyDoNotExistInThePresentationStyle() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("none");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#808080");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.FILL_RULE.getName());
        when(attributes.getValue(2)).thenReturn("evenodd");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(3)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");

        final SVGCssStyle style = getStyle(new SVGElementBaseMock(attributes, new SVGDocumentDataProvider()));

        assertNotNull(style);

        assertTrue(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getIsNone());
        assertEquals(Color.web("#808080"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(FillRule.EVEN_ODD, style.getAttributeHolder()
                                             .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                             .get()
                                             .getValue());
        assertEquals(50.0d, style.getAttributeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .get()
                                 .getValue(), 0.01d);
    }

    /**
     * Ensures that {@link PresentationAttributeMapper}s will be preferred and own {@link SVGCssStyle} attributes are kept.
     */
    @Test
    public void anOwnStyleIsPreferredBeforeAReferencedStyleButReferencedStyleAttributesAreAddedIfTheyDoNotExistInTheOwnStyle() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CLASS.getName());
        when(attributes.getValue(1)).thenReturn("st1");

        final SVGCssStyle referencedStyle = new SVGCssStyle(new SVGDocumentDataProvider());

        referencedStyle.parseCssText("st1{fill:none;stroke:#001122;fill-rule:evenodd;}");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        dataProvider.addStyle(referencedStyle);

        final SVGCssStyle style = getStyle(new SVGElementBaseMock(attributes, dataProvider));

        assertNotNull(style);

        assertEquals(Color.web("#101010"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#080808"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(FillRule.EVEN_ODD, style.getAttributeHolder()
                                             .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                             .get()
                                             .getValue());
        assertEquals(50.0d, style.getAttributeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .get()
                                 .getValue(), 0.01d);
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that values which are not present on the {@link SVGCssStyle} of the parent will
     * not return data but will not cause any
     * exceptions.
     */
    @Test
    public void styleAttributesAreInheritedFromParentEvenIfTheyAreMarkedAsBeingInherited() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#000000;stroke:#111111;fill-rule:evenodd;");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGCssStyle referencedStyle = new SVGCssStyle(dataProvider);

        referencedStyle.parseCssText("st1{fill-rule:inherit;}");

        dataProvider.addStyle(referencedStyle);

        final SVGElementBase parent = new SVGElementBaseMock(attributes, dataProvider);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("inherit");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(1)).thenReturn("stroke:inherit;");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.CLASS.getName());
        when(attributes.getValue(2)).thenReturn("st1");

        final SVGCssStyle style = getStyle(new SVGElementBaseMock(attributes, dataProvider), getStyle(parent));

        assertNotNull(style);

        assertEquals(Color.web("#000000"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue(),
                     Color.web("#111111"));
        assertEquals(FillRule.EVEN_ODD, style.getAttributeHolder()
                                             .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                             .get()
                                             .getValue());
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited from the parent tree and that missing attributes have the default value.
     */
    @Test
    public void styleAttributesAreInheritedFromParentTreeAndIfNoParentHasAValueThatCanBeInheritedThenTheDefaultValuesAreUsed() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#222222");

        final SVGElementBase parent = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("stroke:#111111");

        final SVGElementBase parent1 = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("inherit");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("inherit");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STROKE_LINECAP.getName());
        when(attributes.getValue(2)).thenReturn("inherit");

        final SVGCssStyle style = getStyle(new SVGElementBaseMock(attributes, new SVGDocumentDataProvider()), getStyle(parent1, getStyle(parent)));

        assertNotNull(style);

        assertEquals(Color.web("#222222"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#111111"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(SVGAttributeTypeStrokeLineCap.DEFAULT_VALUE, style.getAttributeHolder()
                                                                       .getAttribute(PresentationAttributeMapper.STROKE_LINECAP.getName(), SVGAttributeTypeStrokeLineCap.class)
                                                                       .get()
                                                                       .getValue());
    }

    /**
     * Ensure the {@link SVGElementBase#getStyleAndResolveInheritance(SVGCssStyle)} will always return a new {@link SVGCssStyle} and never null.
     */
    @Test
    public void aNewStyleWillAlwaysBeReturned() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        final SVGCssStyle cssStyle = getStyle(element);

        assertNotNull(cssStyle);

        final SVGCssStyle cssStyle1 = getStyle(element);

        assertNotNull(cssStyle1);

        assertTrue(cssStyle != cssStyle1);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("#000000");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#111111");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        when(attributes.getValue(2)).thenReturn("0.5");

        element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        final SVGCssStyle cssStyle2 = getStyle(element);

        assertNotNull(cssStyle2);
    }

    /**
     * Ensure the {@link SVGElementBase#getStyleAndResolveInheritance(SVGCssStyle)} will return the same result as {@link SVGElementBase#getStyleAndResolveInheritance(SVGCssStyle)}, if the
     * element does not have a parent.
     */
    @Test
    public void aStyleWillBeReturnedEvenIfTheElementDoesNotHaveAParent() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("#000000");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#111111");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        when(attributes.getValue(2)).thenReturn("0.5");

        final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        final SVGCssStyle style = getStyle(element);

        assertNotNull(style);

        assertEquals(Color.web("#000000"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#111111"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(0.5d, style.getAttributeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).get().getValue(), 0.01d);

        final SVGCssStyle style1 = getStyle(element);

        assertNotNull(style1);

        assertNotSame(style, style1);

        assertEquals(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue(),
                     style1.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue(),
                     style1.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(style.getAttributeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).get().getValue(),
                     style1.getAttributeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).get().getValue(),
                     0.01d);


    }

    /**
     * Ensures that {@link SVGElementBase#combineStylesAndResolveInheritance(SVGCssStyle, SVGCssStyle)} will fail if the presentation style has an invalid format.
     */
    @Test
    public void gettingTheStyleWillFailWhenAnyPresentationAttributeHasAnInvalidValueThatWouldLeadToAnInvalidPresentationStyle() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        for (final PresentationAttributeMapper attributeMapper : PresentationAttributeMapper.VALUES) {
            when(attributes.getQName(0)).thenReturn(attributeMapper.getName());
            when(attributes.getValue(0)).thenReturn("}}");

            final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

            try {
                getStyle(element);
                fail();
            } catch (final SVGException e) {
                assertEquals(e.getReason(), SVGException.Reason.INVALID_CSS_STYLE);
            }
        }
    }

    /**
     * Ensures that {@link SVGElementBase#combineStylesAndResolveInheritance(SVGCssStyle, SVGCssStyle)} will fail if the own style has an invalid format.
     */
    @Test
    public void gettingTheStyleWillFailWhenTheOwnStyleHasAnInvalidFormat() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("{strokedata");

        final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        try {
            getStyle(element);
            fail();
        } catch (final SVGException e) {
            assertEquals(e.getReason(), SVGException.Reason.INVALID_CSS_STYLE);
        }
    }

    /**
     * Ensures that {@link SVGElementBase#combineStylesAndResolveInheritance(SVGCssStyle, SVGCssStyle)} will fail if the the referenced style does not exist.
     */
    @Test
    public void gettingTheStyleWillFailIfTheReferencedStyleDoesNotExist() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CLASS.getName());
        when(attributes.getValue(0)).thenReturn("ref");

        final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());

        try {
            getStyle(element);
            fail();
        } catch (final SVGException e) {
            assertEquals(e.getReason(), SVGException.Reason.MISSING_STYLE);
        }
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that the value will be retrieved from the provided {@link SVGCssStyle}.
     */
    @Test
    public void getStyleAndResolveInheritanceUsesValuesFromTheOtherStyle() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:inherit;stroke:#222222");

        final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());
        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st1{fill:#111111;}");

        final SVGCssStyle elementStyle = getStyle(element, style);

        assertEquals(Color.web("#111111"), elementStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#222222"), elementStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} of the inheritanceResolver will be added.
     */
    @Test
    public void getStyleAndResolveInheritanceAddsValuesFromOtherStyle() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:inherit");

        final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());
        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st1{fill:#111111;stroke:#222222}");

        final SVGCssStyle elementStyle = getStyle(element, style);

        assertEquals(Color.web("#111111"), elementStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#222222"), elementStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that the value already set will not be overwritten.
     */
    @Test
    public void getStyleAndResolveInheritanceDoesNotOverrideExistingValues() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#333333");

        final SVGElementBase element = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider());
        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st1{fill:#111111}");

        final SVGCssStyle elementStyle = getStyle(element, style);

        assertEquals(Color.web("#333333"), elementStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
    }

    //endregion

    //region Test for Transformation attribute

    /**
     * Ensures that {@link CoreAttributeMapper#TRANSFORM} attribute is correctly read.
     */
    @Test
    public void theTransformAttributeIsReadAndReturnsACorrectTransformation() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TRANSFORM.getName());
        when(attributes.getValue(0)).thenReturn("translate(30)");

        final Optional<Transform> transform = new SVGElementBaseMock(attributes, new SVGDocumentDataProvider()).getTransformation();

        assertTrue(transform.isPresent());

        assertEquals(30.0d, transform.get().getTx(), 0.01d);
    }

    //endregion


    //endregion

    // region Private

    public static SVGCssStyle getStyle(final SVGElementBase elementBase) throws SVGException {
        return getStyle(elementBase, null);
    }

    public static SVGCssStyle getStyle(final SVGElementBase element, final SVGCssStyle otherStyle) throws SVGException {
        try {
            final Method method = SVGElementBase.class.getDeclaredMethod("getStyleAndResolveInheritance", SVGCssStyle.class);

            method.setAccessible(true);

            return SVGCssStyle.class.cast(method.invoke(element, otherStyle));
        } catch (final IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not get method getStyleAndResolveInheritance", e.getCause());
        } catch (final InvocationTargetException e) {
            if (e.getTargetException() instanceof SVGException) {
                throw (SVGException) e.getTargetException();
            } else {
                throw new IllegalArgumentException("Could not invoke method getStyleAndResolveInheritance", e.getCause());
            }
        }
    }

    // endregion
}