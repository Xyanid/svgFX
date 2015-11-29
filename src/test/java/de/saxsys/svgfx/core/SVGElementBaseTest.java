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

import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.elements.Circle;
import de.saxsys.svgfx.core.elements.Defs;
import de.saxsys.svgfx.css.core.CssStyle;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import java.util.EnumSet;

import static org.mockito.Mockito.when;

/**
 * Created by Xyanid on 28.11.2015.
 */
public class SVGElementBaseTest {

    private static SVGElementBase element3 = Mockito.mock(SVGElementBase.class);

    private static SVGElementBase element2 = Mockito.mock(SVGElementBase.class);

    private static SVGElementBase element1 = Mockito.mock(SVGElementBase.class);

    /**
     * sets up the element so the test has all the required data.
     */
    @BeforeClass public static void trainMocks() {

        when(element1.getParent()).thenReturn(element2);
        when(element2.getParent()).thenReturn(element3);
        when(element3.getParent()).thenReturn(null);
    }

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

        when(attributes.getLength()).thenReturn(EnumSet.allOf(Enumerations.PresentationAttribute.class).size());

        int counter = 0;

        for (Enumerations.PresentationAttribute attribute : EnumSet.allOf(Enumerations.PresentationAttribute.class)) {
            when(attributes.getQName(counter)).thenReturn(attribute.getName());
            when(attributes.getValue(counter)).thenReturn(String.format("%d", counter++));
        }

        Circle circle = new Circle("circle", attributes, null, new SVGDataProvider());

        CssStyle style = circle.getPresentationCssStyle();

        Assert.assertNotNull(style);

        counter = 0;

        for (Enumerations.PresentationAttribute attribute : EnumSet.allOf(Enumerations.PresentationAttribute.class)) {
            Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(attribute.getName()), String.format("%d", counter++));
        }
    }

    /**
     * Ensures that {@link de.saxsys.svgfx.core.definitions.Enumerations.PresentationAttribute}s will be preferred and own {@link CssStyle} attributes are kept.
     */
    @Test public void ensureThatPresentationStyleIsPreferredAndOwnCssStyleAttributesAreKept() {

        Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(4);

        when(attributes.getQName(0)).thenReturn(Enumerations.PresentationAttribute.FILL.getName());
        when(attributes.getValue(0)).thenReturn("none");
        when(attributes.getQName(1)).thenReturn(Enumerations.PresentationAttribute.STROKE.getName());
        when(attributes.getValue(1)).thenReturn("#808080");
        when(attributes.getQName(2)).thenReturn(Enumerations.PresentationAttribute.FILL_RULE.getName());
        when(attributes.getValue(2)).thenReturn("odd");
        when(attributes.getQName(3)).thenReturn(Enumerations.CoreAttribute.STYLE.getName());
        when(attributes.getValue(3)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");

        Circle circle = new Circle("circle", attributes, null, new SVGDataProvider());

        CssStyle style = circle.getCssStyle();

        Assert.assertNotNull(style);

        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.FILL.getName()), "none");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.STROKE.getName()), "#808080");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.FILL_RULE.getName()), "odd");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.STROKE_WIDTH.getName()), "50");
    }

    /**
     * Ensures that {@link de.saxsys.svgfx.core.definitions.Enumerations.PresentationAttribute}s will be preferred and own {@link CssStyle} attributes are kept.
     */
    @Test public void ensureThatOwnStyleIsPreferredAndReferencedCssStyleAttributesAreKept() {

        Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");
        when(attributes.getQName(1)).thenReturn(Enumerations.CoreAttribute.CLASS.getName());
        when(attributes.getValue(1)).thenReturn("st1");

        CssStyle referencedStyle = new CssStyle();

        referencedStyle.consumeCssText("st1{fill:none;stroke:#001122;fill-rule:odd;}");

        SVGDataProvider dataProvider = new SVGDataProvider();

        dataProvider.getStyles().add(referencedStyle);

        Circle circle = new Circle("circle", attributes, null, dataProvider);

        CssStyle style = circle.getCssStyle();

        Assert.assertNotNull(style);

        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.FILL.getName()), "#101010");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.STROKE.getName()), "#080808");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.FILL_RULE.getName()), "odd");
        Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(Enumerations.PresentationAttribute.STROKE_WIDTH.getName()), "50");
    }
}
