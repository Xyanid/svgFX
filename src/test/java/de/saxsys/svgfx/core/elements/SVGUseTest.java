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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg use elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ({"unchecked", "OptionalGetWithoutIsPresent", "ConstantConditions"})
public final class SVGUseTest {

    /**
     * Ensures that a attributes used by the use node are applied to the referenced result
     */
    @Test
    public void attributesAreAppliedToTheReferencedElement() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(1)).thenReturn("25");

        final SVGDocumentDataProvider provider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, provider));

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.POSITION_X.getName());
        when(attributes.getValue(1)).thenReturn("1");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.POSITION_Y.getName());
        when(attributes.getValue(2)).thenReturn("2");

        final SVGUse use = new SVGUse(SVGUse.ELEMENT_NAME, attributes, provider);

        assertEquals("#test", use.getAttributeHolder().getAttribute(XLinkAttributeMapper.XLINK_HREF.getName(), SVGAttributeTypeString.class).get().getValue());
        assertEquals(1.0d, use.getAttributeHolder().getAttribute(CoreAttributeMapper.POSITION_X.getName(), SVGAttributeTypeLength.class).get().getValue(), MINIMUM_DEVIATION);
        assertEquals(2.0d, use.getAttributeHolder().getAttribute(CoreAttributeMapper.POSITION_Y.getName(), SVGAttributeTypeLength.class).get().getValue(), MINIMUM_DEVIATION);

        assertNotNull(use.getResult());
        assertEquals(Group.class, use.getResult().getClass());
        assertEquals(1.0d, use.getResult().getLayoutX(), MINIMUM_DEVIATION);
        assertEquals(2.0d, use.getResult().getLayoutY(), MINIMUM_DEVIATION);

        final Group group = use.getResult();

        final Circle circle = (Circle) group.getChildren().get(0);

        assertEquals(25.0d, circle.getRadius(), MINIMUM_DEVIATION);
        assertEquals(0.0d, circle.getCenterX(), MINIMUM_DEVIATION);
        assertEquals(0.0d, circle.getCenterY(), MINIMUM_DEVIATION);
    }

    /**
     * Ensure that a referenced element which has style attributes that uses inheritance will get its actually value from the use element.
     */
    @Test
    public void styleAreInheritedForReferencedElements() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(1)).thenReturn("25");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(2)).thenReturn("inherit");

        final SVGDocumentDataProvider provider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, provider));

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.FILL.getName());
        when(attributes.getValue(1)).thenReturn("black");

        final SVGUse use = new SVGUse(SVGUse.ELEMENT_NAME, attributes, provider);

        assertEquals(Color.BLACK, ((Circle) use.getResult().getChildren().get(0)).getFill());
    }

    /**
     * Ensures that a use duplicates an element.
     */
    @Test
    public void elementsWillBeDuplicatedWhenTheResultIsRequestedMultipleTimes() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(1)).thenReturn("25");

        final SVGDocumentDataProvider provider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, provider));

        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#test");

        final SVGUse use1 = new SVGUse(SVGUse.ELEMENT_NAME, attributes, provider);

        final SVGUse use2 = new SVGUse(SVGUse.ELEMENT_NAME, attributes, provider);

        assertNotNull(use1.getResult());
        assertEquals(Circle.class, use1.getResult().getChildren().get(0).getClass());

        assertNotNull(use2.getResult());
        assertEquals(Circle.class, use2.getResult().getChildren().get(0).getClass());

        assertNotEquals(use1.getResult(), use2.getResult());
    }


    /**
     * Ensures that an {@link SAXException} is thrown if the referenced element can not be found
     */
    @Test
    public void whenReferencedElementCanNotBeResolvedAnSVGExceptionWillBeThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        when(attributes.getValue(0)).thenReturn("test");
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(0)).thenReturn("25");

        final SVGDocumentDataProvider provider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(provider, "data")).put("test", new SVGCircle(SVGCircle.ELEMENT_NAME, attributes, provider));

        when(attributes.getQName(0)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(0)).thenReturn("#something");

        final SVGUse use = new SVGUse(SVGUse.ELEMENT_NAME, attributes, provider);

        try {
            use.getResult();
            fail("Should not be able to get result when referenced element is missing");
        } catch (final SAXException e) {
            assertThat(e.getCause(), instanceOf(SVGException.class));
        }
    }
}
