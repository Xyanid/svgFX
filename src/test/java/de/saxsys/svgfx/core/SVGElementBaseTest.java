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

    private static Attributes presentationAttributes = Mockito.mock(Attributes.class);

    private static SVGElementBase element3 = Mockito.mock(SVGElementBase.class);

    private static SVGElementBase element2 = Mockito.mock(SVGElementBase.class);

    private static SVGElementBase element1 = Mockito.mock(SVGElementBase.class);

    /**
     * sets up the element so the test has all the required data.
     */
    @BeforeClass public static void trainMocks() {

        when(presentationAttributes.getLength()).thenReturn(EnumSet.allOf(Enumerations.PresentationAttribute.class).size());

        int counter = 0;

        for (Enumerations.PresentationAttribute attribute : EnumSet.allOf(Enumerations.PresentationAttribute.class)) {
            when(presentationAttributes.getQName(counter)).thenReturn(attribute.getName());
            when(presentationAttributes.getValue(counter)).thenReturn(String.format("%d", counter++));
        }

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

        SVGDataProvider dataProvider = new SVGDataProvider();

        Circle circle = new Circle("circle", presentationAttributes, null, dataProvider);

        CssStyle style = circle.getPresentationCssStyle();

        Assert.assertNotNull(style);

        int counter = 0;

        for (Enumerations.PresentationAttribute attribute : EnumSet.allOf(Enumerations.PresentationAttribute.class)) {
            Assert.assertEquals(style.getCssStyleDeclaration().getPropertyValue(attribute.getName()), String.format("%d", counter++));
        }
    }
}
