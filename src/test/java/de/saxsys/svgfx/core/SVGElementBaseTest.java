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

package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.elements.Circle;
import de.saxsys.svgfx.core.elements.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import java.util.EnumSet;

/**
 * Test to ensure the base functionality of {@link SVGElementBase}. Note that this test does not use mocks for the {@link SVGElementBase} since it would be too much effort to train them.
 * Created by Xyanid on 28.11.2015.
 */
public class SVGElementBaseTest {

    /**
     * Ensured that styles are inherited by the element from their parent
     */
    @Test public void ensureThatStylesAreInherited() {


    }

    /**
     * Ensure that presentation attributes create a valid css style.
     */
    @Test public void ensureThatPresentationAttributesCreateValidCssStyle() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(EnumSet.allOf(SVGCssStyle.PresentationAttribute.class).size());

        int counter = 0;

        for (SVGCssStyle.PresentationAttribute attribute : EnumSet.allOf(SVGCssStyle.PresentationAttribute.class)) {
            Mockito.when(attributes.getQName(counter)).thenReturn(attribute.getName());
            Mockito.when(attributes.getValue(counter)).thenReturn(String.format("%d", counter++));
        }

        Circle circle = new Circle("circle", attributes, null, new SVGDataProvider());

        SVGCssStyle style = circle.getPresentationCssStyle();

        Assert.assertNotNull(style);

        counter = 0;

        for (SVGCssStyle.PresentationAttribute attribute : EnumSet.allOf(SVGCssStyle.PresentationAttribute.class)) {
            Assert.assertEquals(style.getCssContentType(attribute.getName()).getDefaultValue(), String.format("%d", counter++));
        }
    }

    /**
     * Ensures that {@link SVGCssStyle.PresentationAttribute}s will be preferred and own {@link SVGCssStyle} attributes are kept.
     */
    @Test public void ensureThatPresentationStyleIsPreferredAndOwnCssStyleAttributesAreKept() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGCssStyle.PresentationAttribute.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("none");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGCssStyle.PresentationAttribute.STROKE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("#808080");
        Mockito.when(attributes.getQName(2)).thenReturn(SVGCssStyle.PresentationAttribute.FILL_RULE.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("evenodd");
        Mockito.when(attributes.getQName(3)).thenReturn(SVGElementBase.CoreAttribute.STYLE.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");

        Circle circle = new Circle("circle", attributes, null, new SVGDataProvider());

        SVGCssStyle style = circle.getCssStyle();

        Assert.assertNotNull(style);

        Assert.assertTrue(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getIsNone());
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#808080"));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName(), SVGCssContentTypeFillRule.class).getValue(), FillRule.EVEN_ODD);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 50.0d, 0.01d);
    }

    /**
     * Ensures that {@link SVGCssStyle.PresentationAttribute}s will be preferred and own {@link SVGCssStyle} attributes are kept.
     */
    @Test public void ensureThatOwnStyleIsPreferredAndReferencedCssStyleAttributesAreKept() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.STYLE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.CLASS.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("st1");

        SVGCssStyle referencedStyle = new SVGCssStyle(new SVGDataProvider());

        referencedStyle.parseCssText("st1{fill:none;stroke:#001122;fill-rule:odd;}");

        SVGDataProvider dataProvider = new SVGDataProvider();

        dataProvider.getStyles().add(referencedStyle);

        Circle circle = new Circle("circle", attributes, null, dataProvider);

        SVGCssStyle style = circle.getCssStyle();

        Assert.assertNotNull(style);

        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#101010"));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#080808"));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName(), SVGCssContentTypeFillRule.class).getValue(), FillRule.EVEN_ODD);
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE_WIDTH.getName(), SVGCssContentTypeLength.class).getValue(), 50.0d, 0.01d);
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that values which are not present on the {@link SVGCssStyle} of the parent will not return data but will not cause any
     * exceptions.
     */
    @Test public void ensureThatCssStyleAttributesAreInherited() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.STYLE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("fill:#000000;stroke:#111111;fill-rule:evenodd;");

        SVGDataProvider dataProvider = new SVGDataProvider();

        SVGCssStyle referencedStyle = new SVGCssStyle(dataProvider);

        referencedStyle.parseCssText("st1{fill-rule:inherit;}");

        dataProvider.getStyles().add(referencedStyle);

        Group group = new Group("group", attributes, null, dataProvider);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGCssStyle.PresentationAttribute.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("inherit");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.STYLE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("stroke:inherit;");
        Mockito.when(attributes.getQName(2)).thenReturn(SVGElementBase.CoreAttribute.CLASS.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("st1");

        Circle circle = new Circle("circle", attributes, group, dataProvider);

        SVGCssStyle style = circle.getCssStyle();

        Assert.assertNotNull(style);

        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#000000"));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.STROKE.getName(), SVGCssContentTypePaint.class).getValue(), Color.web("#111111"));
        Assert.assertEquals(style.getCssContentType(SVGCssStyle.PresentationAttribute.FILL_RULE.getName(), SVGCssContentTypeFillRule.class).getValue(), FillRule.EVEN_ODD);
    }
}
