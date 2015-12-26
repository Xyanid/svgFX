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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

import java.util.Map;

/**
 * This test will ensure that svg use elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGUseTest {

    /**
     * Ensures that a attributes used by the use node are applied to the referenced result
     */
    @Test
    @Ignore(value = "Since the use element only has x and y it might be better to implement it as a group instead of it creating the type of the referenced element")
    public void ensureAttributesAreAppliedToTheReferencedElement() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.ID.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("test");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("25");

        SVGDataProvider provider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle("circle", attributes, null, provider));

        Mockito.when(attributes.getLength()).thenReturn(5);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.XLinkAttribute.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.POSITION_X.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("1");
        Mockito.when(attributes.getQName(2)).thenReturn(SVGElementBase.CoreAttribute.POSITION_Y.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("2");
        Mockito.when(attributes.getQName(3)).thenReturn(SVGElementBase.CoreAttribute.WIDTH.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("3");
        Mockito.when(attributes.getQName(4)).thenReturn(SVGElementBase.CoreAttribute.HEIGHT.getName());
        Mockito.when(attributes.getValue(4)).thenReturn("4");

        SVGUse use = new SVGUse("use", attributes, null, provider);

        Assert.assertEquals("#test", use.getAttribute(SVGElementBase.XLinkAttribute.XLINK_HREF.getName()));
        Assert.assertEquals("1", use.getAttribute(SVGElementBase.CoreAttribute.POSITION_X.getName()));
        Assert.assertEquals("2", use.getAttribute(SVGElementBase.CoreAttribute.POSITION_Y.getName()));
        Assert.assertEquals("3", use.getAttribute(SVGElementBase.CoreAttribute.WIDTH.getName()));
        Assert.assertEquals("4", use.getAttribute(SVGElementBase.CoreAttribute.HEIGHT.getName()));

        Assert.assertNotNull(use.getResult());
        Assert.assertEquals(Circle.class, use.getResult().getClass());

        Circle circle = (Circle) use.getResult();

        Assert.assertEquals(25.0d, circle.getRadius(), 0.01d);
        Assert.assertEquals(1.0d, circle.getCenterX(), 0.01d);
        Assert.assertEquals(2.0d, circle.getCenterY(), 0.01d);
    }

    /**
     * Ensure that a referenced element which has style attribute that uses inheritance will get its actually value from the use element.
     */
    @Test
    public void ensureStyleAreInheritedForReferencedElements() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.ID.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("test");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("25");
        Mockito.when(attributes.getQName(2)).thenReturn(SVGCssStyle.PresentationAttribute.FILL.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("inherit");

        SVGDataProvider provider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle("circle", attributes, null, provider));

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.XLinkAttribute.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGCssStyle.PresentationAttribute.FILL.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("black");

        SVGUse use = new SVGUse("use", attributes, null, provider);

        Assert.assertEquals(Color.BLACK, ((Circle) use.getResult()).getFill());
    }

    /**
     * Ensures that a use duplicates an element.
     */
    @Test
    public void ensureElementsAreDuplicated() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.ID.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("test");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("25");

        SVGDataProvider provider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle("circle", attributes, null, provider));

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.XLinkAttribute.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#test");

        SVGUse use1 = new SVGUse("use", attributes, null, provider);

        SVGUse use2 = new SVGUse("use", attributes, null, provider);

        Assert.assertNotNull(use1.getResult());
        Assert.assertEquals(Circle.class, use1.getResult().getClass());

        Assert.assertNotNull(use2.getResult());
        Assert.assertEquals(Circle.class, use2.getResult().getClass());

        Assert.assertNotEquals(use1.getResult(), use2.getResult());
    }


    /**
     * Ensures that an {@link de.saxsys.svgfx.core.SVGException} is thrown if the referenced element can not be found
     */
    @Test(expected = SVGException.class)
    public void ensureSVGExceptionIsThrownWhenReferencedElementCanNotBeResolved() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.ID.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("test");
        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("25");

        SVGDataProvider provider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle("circle", attributes, null, provider));

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.XLinkAttribute.XLINK_HREF.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#something");

        SVGUse use1 = new SVGUse("use", attributes, null, provider);

        use1.getResult();
    }
}
