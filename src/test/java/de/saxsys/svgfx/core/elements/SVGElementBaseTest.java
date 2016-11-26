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
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
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
         * @param parent       parent of the element
         * @param dataProvider dataprovider to be used
         *
         * @throws IllegalArgumentException if either value or dataProvider are null
         */
        protected SVGElementBaseMock(Attributes attributes, SVGElementBase<?> parent, SVGDocumentDataProvider dataProvider) throws IllegalArgumentException {
            super("Test", attributes, parent, dataProvider);
        }

        @Override
        protected Object createResult(StyleSupplier styleSupplier) throws SVGException {
            return null;
        }

        @Override
        protected void initializeResult(Object o, StyleSupplier styleSupplier) throws SVGException {

        }

        @Override
        public boolean rememberElement() {
            return false;
        }

        @Override
        public void startProcessing() throws SAXException {

        }

        @Override
        public void processCharacterData(char[] ch, int start, int length) throws SAXException {

        }

        @Override
        public void endProcessing() throws SAXException {

        }

        @Override
        public boolean canConsumeResult() {
            return false;
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
                value = Enumerations.FillRuleMapping.EVEN_ODD.getName();
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

        final SVGCssStyle style = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider()).getStyle();

        assertNotNull(style);

        for (final PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            Object value = 1.0d;

            if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                value = "1.0";
            } else if (attribute == PresentationAttributeMapper.FILL_RULE) {
                value = Enumerations.FillRuleMapping.EVEN_ODD.getRule();
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

        final SVGCssStyle style = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider()).getStyle();

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

        final SVGCssStyle style = new SVGElementBaseMock(attributes, null, dataProvider).getStyle();

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

        final SVGElementBase parent = new SVGElementBaseMock(attributes, null, dataProvider);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("inherit");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(1)).thenReturn("stroke:inherit;");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.CLASS.getName());
        when(attributes.getValue(2)).thenReturn("st1");

        final SVGCssStyle style = new SVGElementBaseMock(attributes, parent, dataProvider).getStyle();

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

        final SVGElementBase parent = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("stroke:#111111");

        final SVGElementBase parent1 = new SVGElementBaseMock(attributes, parent, new SVGDocumentDataProvider());

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("inherit");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("inherit");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STROKE_LINECAP.getName());
        when(attributes.getValue(2)).thenReturn("inherit");

        final SVGCssStyle style = new SVGElementBaseMock(attributes, parent1, new SVGDocumentDataProvider()).getStyle();

        assertNotNull(style);

        assertEquals(Color.web("#222222"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#111111"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(SVGAttributeTypeStrokeLineCap.DEFAULT_VALUE, style.getAttributeHolder()
                                                                       .getAttribute(PresentationAttributeMapper.STROKE_LINECAP.getName(), SVGAttributeTypeStrokeLineCap.class)
                                                                       .get()
                                                                       .getValue());
    }

    /**
     * Ensure the {@link SVGElementBase#getStyle()} will always return a new {@link SVGCssStyle} and never null.
     */
    @Test
    public void aNewStyleWillAlwaysBeReturned() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle cssStyle = element.getStyle();

        assertNotNull(cssStyle);

        final SVGCssStyle cssStyle1 = element.getStyle();

        assertNotNull(cssStyle1);

        assertTrue(cssStyle != cssStyle1);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("#000000");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#111111");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        when(attributes.getValue(2)).thenReturn("0.5");

        element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle cssStyle2 = element.getStyle();

        assertNotNull(cssStyle2);
    }

    /**
     * Ensure the {@link SVGElementBase#getStyle()} will return the same result as {@link SVGElementBase#getStyle()}, if the
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

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle style = element.getStyle();

        assertNotNull(style);

        assertEquals(Color.web("#000000"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#111111"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(0.5d, style.getAttributeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).get().getValue(), 0.01d);

        final SVGCssStyle style1 = element.getStyle();

        assertNotNull(style1);

        assertTrue(style != style1);

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

            final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

            try {
                element.getStyle();
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

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        try {
            element.getStyle();
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

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        try {
            element.getStyle();
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

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());
        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st1{fill:#111111;}");

        final SVGCssStyle elementStyle = element.getStyleAndResolveInheritance(style);

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

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());
        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st1{fill:#111111;stroke:#222222}");

        final SVGCssStyle elementStyle = element.getStyleAndResolveInheritance(style);

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

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());
        final SVGCssStyle style = new SVGCssStyle(new SVGDocumentDataProvider());

        style.parseCssText(".st1{fill:#111111}");

        final SVGCssStyle elementStyle = element.getStyleAndResolveInheritance(style);

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

        final Transform transform = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider()).getTransformation();

        assertNotNull(transform);
        assertEquals(Translate.class, transform.getClass());

        assertEquals(30.0d, transform.getTx(), 0.01d);
    }

    //endregion

    // region Test for ClipPath attribute

    /**
     * Ensure that no {@link SVGClipPath} will be created if the referenced {@link SVGClipPath} does not exist in the {@link SVGDocumentDataProvider}.
     */
    @Test
    public void anSVGExceptionIsThrownIfTheReferencedClipPathIsMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#path)");

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        try {
            element.getClipPath(element::getStyle);
            fail();
        } catch (final SVGException e) {
            assertTrue(e.getMessage().contains("path"));
        }
    }

    /**
     * Ensure that no {@link SVGClipPath} will be created if the element does not have {@link PresentationAttributeMapper#CLIP_PATH} attribute.
     */
    @Test
    public void noClipPathIsReturnedIfTheElementDoesNotHaveAClipPathAttribute() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGElementBase circle = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        assertNull(circle.getClipPath(circle::getStyle));
    }

    /**
     * Ensure that an {@link SVGException} is thrown if the referenced {@link SVGClipPath} is not present in the {@link SVGDocumentDataProvider}.
     */
    @Test (expected = SVGException.class)
    public void anSVGExceptionIsThrownIfTheClipPathReferenceIsMissingInTheDataProvider() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#path)");

        final SVGElementBase circle = new SVGElementBaseMock(attributes, null, new SVGDocumentDataProvider());

        circle.getClipPath(circle::getStyle);
    }

    /**
     * Ensure that a {@link SVGClipPath} will be created if the element meets all the requirements.
     */
    @Test
    public void aClipPathIsReturnedIfThereIsAClipPathReference() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, dataProvider);

        assertNotNull(element.getClipPath(element::getStyle));
    }

    /**
     * Ensure that a {@link SVGClipPath} will always return a new instance.
     */
    @Test
    public void aDifferentClipPathInstanceWillBeReturnedAlwaysReturn() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGElementBase element = new SVGElementBaseMock(attributes, null, dataProvider);

        final Node clipPath = element.getClipPath(element::getStyle);

        assertNotNull(clipPath);

        final Node clipPath1 = element.getClipPath(element::getStyle);

        assertNotNull(clipPath1);

        assertNotSame(clipPath, clipPath1);
    }

    /**
     * Ensure that a {@link SVGClipPath} will not be able to cause a stack overflow in case the {@link SVGClipPath} reference it self.
     */
    @Test
    public void aClipPathAttributeWillNotCauseStackOverflowWhenReferencingTheElementItIsContainedIn() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(1)).thenReturn("test");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, dataProvider);

        final Node clipPath = circle.getClipPath(circle::getStyle);

        assertNotNull(clipPath);
    }

    // endregion

    //endregion
}