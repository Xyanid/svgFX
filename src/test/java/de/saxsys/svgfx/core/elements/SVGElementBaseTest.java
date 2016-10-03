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
import de.saxsys.svgfx.core.content.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.content.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.css.SVGCssStyle;
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
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

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
public class SVGElementBaseTest {

    /**
     * Ensure that presentation attributes create a valid css style.
     */
    @Test
    public void ensureThatPresentationAttributesCreateValidCssStyle() {

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

        final SVGCssStyle style = new SVGCircle("circle", attributes, null, new SVGDocumentDataProvider()).getStyle();

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
    public void ensureThatPresentationStyleIsPreferredAndOwnCssStyleAttributesAreKept() {

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

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle style = circle.getStyle();

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
    public void ensureThatOwnStyleIsPreferredAndReferencedCssStyleAttributesAreKept() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CLASS.getName());
        when(attributes.getValue(1)).thenReturn("st1");

        final SVGCssStyle referencedStyle = new SVGCssStyle(new SVGDocumentDataProvider());

        referencedStyle.parseCssText("st1{fill:none;stroke:#001122;fill-rule:odd;}");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        dataProvider.getStyles().add(referencedStyle);

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, dataProvider);

        final SVGCssStyle style = circle.getStyle();

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
    public void ensureThatCssStyleAttributesAreInheritedFromParent() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#000000;stroke:#111111;fill-rule:evenodd;");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGCssStyle referencedStyle = new SVGCssStyle(dataProvider);

        referencedStyle.parseCssText("st1{fill-rule:inherit;}");

        dataProvider.getStyles().add(referencedStyle);

        final SVGGroup group = new SVGGroup(SVGGroup.ELEMENT_NAME, attributes, null, dataProvider);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("inherit");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(1)).thenReturn("stroke:inherit;");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.CLASS.getName());
        when(attributes.getValue(2)).thenReturn("st1");

        SVGCircle circle = new SVGCircle("circle", attributes, group, dataProvider);

        SVGCssStyle style = circle.getStyle();

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
    public void ensureThatCssStyleAttributesAreInheritedFromParentTreeAndDefaultValuesAreApplied() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#222222");

        final SVGGroup group = new SVGGroup(SVGGroup.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("stroke:#111111");

        final SVGGroup group1 = new SVGGroup(SVGGroup.ELEMENT_NAME, attributes, group, new SVGDocumentDataProvider());

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("inherit");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("inherit");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STROKE_LINECAP.getName());
        when(attributes.getValue(2)).thenReturn("inherit");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, group1, new SVGDocumentDataProvider());

        final SVGCssStyle style = circle.getStyle();

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
    public void ensureGetCssStyleWillAlwaysReturnANewStyleAndNeverNull() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle cssStyle = circle.getStyle();

        assertNotNull(cssStyle);

        final SVGCssStyle cssStyle1 = circle.getStyle();

        assertNotNull(cssStyle1);

        assertTrue(cssStyle != cssStyle1);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("#000000");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#111111");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        when(attributes.getValue(2)).thenReturn("0.5");

        circle = new SVGCircle("circle", attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle cssStyle2 = circle.getStyle();

        assertNotNull(cssStyle2);
    }

    /**
     * Ensure the {@link SVGElementBase#getStyle()} will return the same result as {@link SVGElementBase#getStyle()}, if the
     * element does not have a parent.
     */
    @Test
    public void ensureGetCssStyleAndResolveInheritanceWillWorkIfThereIsNoParent() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(0)).thenReturn("#000000");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#111111");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        when(attributes.getValue(2)).thenReturn("0.5");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        final SVGCssStyle style = circle.getStyle();

        assertNotNull(style);

        assertEquals(Color.web("#000000"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(Color.web("#111111"), style.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).get().getValue());
        assertEquals(0.5d, style.getAttributeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).get().getValue(), 0.01d);

        final SVGCssStyle style1 = circle.getStyle();

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
     * Ensures that {@link CoreAttributeMapper#TRANSFORM} attribute is correctly read.
     */
    @Test
    public void ensureThatTransformAttributeIsRead() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TRANSFORM.getName());
        when(attributes.getValue(0)).thenReturn("translate(30)");

        final Transform transform = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider()).getTransformation();

        assertNotNull(transform);
        assertEquals(Translate.class, transform.getClass());

        assertEquals(30.0d, transform.getTx(), 0.01d);
    }

    /**
     * Ensure that no {@link SVGClipPath} will be created if the referenced {@link SVGClipPath} does not exist in the {@link SVGDocumentDataProvider}.
     */
    @Test
    public void ensureSVGExceptionIsThrownIfTheReferencedClipPathIsMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#path)");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        try {
            circle.getClipPath(circle::getStyle);
            fail();
        } catch (final SVGException e) {
            assertTrue(e.getMessage().contains("path"));
        }
    }

    /**
     * Ensure that no {@link SVGClipPath} will be created if the element does not have {@link PresentationAttributeMapper#CLIP_PATH} attribute.
     */
    @Test
    public void ensureNoClipPathIsReturnedIfTheElementDoesNotHaveAClipPathAttribute() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertNull(circle.getClipPath(circle::getStyle));
    }

    /**
     * Ensure that an {@link SVGException} is thrown if the referenced {@link SVGClipPath} is not present in the {@link SVGDocumentDataProvider}.
     */
    @Test (expected = SVGException.class)
    public void ensureSVGExceptionIsThrownIfTheClipPathReferenceIsMissingInTheDataProvider() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#path)");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        circle.getClipPath(circle::getStyle);
    }

    /**
     * Ensure that a {@link SVGClipPath} will be created if the element meets all the requirements.
     */
    @Test
    public void ensureClipPathIsReturnedIfThereIsClipPathReference() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, dataProvider);

        assertNotNull(circle.getClipPath(circle::getStyle));
    }

    /**
     * Ensure that a {@link SVGClipPath} will always return a new instance.
     */
    @Test
    public void ensureClipPathIsAlwaysADifferentInstance() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGCircle circle = new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, null, dataProvider);

        final Node clipPath = circle.getClipPath(circle::getStyle);

        assertNotNull(clipPath);

        final Node clipPath1 = circle.getClipPath(circle::getStyle);

        assertNotNull(clipPath1);

        assertNotSame(clipPath, clipPath1);
    }

    /**
     * Ensure that a {@link SVGClipPath} will not be able to cause a stack overflow in case the {@link SVGClipPath} reference it self.
     */
    @Test
    public void ensureClipPathWillNotCauseStackOverflow() {

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
}
