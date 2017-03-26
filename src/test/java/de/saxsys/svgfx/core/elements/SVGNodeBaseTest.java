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
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author Xyanid on 12.03.2017.
 */
public class SVGNodeBaseTest {

    // region Classes

    private static class SVGNodeBaseMock extends SVGNodeBase<Node> {

        protected SVGNodeBaseMock(final Attributes attributes, final SVGDocumentDataProvider documentDataProvider) {
            super("Test", attributes, documentDataProvider);
        }

        @Override
        protected Node createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {
            return null;
        }
    }

    // endregion

    // region Test

    /**
     * Ensure that no {@link SVGClipPath} will be created if the referenced {@link SVGClipPath} does not exist in the {@link SVGDocumentDataProvider}.
     */
    @Test
    public void anSVGExceptionIsThrownIfTheReferencedClipPathIsMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#path)");

        final SVGNodeBase element = new SVGNodeBaseMock(attributes, new SVGDocumentDataProvider());

        try {
            getClipPath(element);
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

        final SVGNodeBase circle = new SVGNodeBaseMock(attributes, new SVGDocumentDataProvider());

        assertFalse(getClipPath(circle).isPresent());
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

        final SVGNodeBase circle = new SVGNodeBaseMock(attributes, new SVGDocumentDataProvider());

        getClipPath(circle);
    }

    /**
     * Ensure that a {@link SVGClipPath} will be created if the element meets all the requirements.
     */
    @Test
    public void aClipPathIsReturnedIfThereIsAClipPathReference() throws SVGException {

        final Attributes attributesClipPath = Mockito.mock(Attributes.class);

        when(attributesClipPath.getLength()).thenReturn(0);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributesClipPath, dataProvider));

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGNodeBase element = new SVGNodeBaseMock(attributes, dataProvider);

        assertNotNull(getClipPath(element));
    }

    /**
     * Ensure that no {@link StackOverflowError} will be thrown if a clippath has a clippath that reference itself.
     */
    @Test
    public void ifAClipPathReferencesItSelfAsAClipPathNoOverflowWillOccur() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGNodeBase element = new SVGNodeBaseMock(attributes, dataProvider);

        assertNotNull(getClipPath(element));
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

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGNodeBase element = new SVGNodeBaseMock(attributes, dataProvider);

        final Optional<Node> clipPath = getClipPath(element);

        assertTrue(clipPath.isPresent());

        final Optional<Node> clipPath1 = getClipPath(element);

        assertTrue(clipPath1.isPresent());

        assertNotSame(clipPath.get(), clipPath1.get());
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

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGClipPath(SVGClipPath.ELEMENT_NAME, attributes, dataProvider));

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        when(attributes.getValue(0)).thenReturn("url(#test)");

        final SVGNodeBase element = new SVGNodeBaseMock(attributes, dataProvider);

        assertTrue(getClipPath(element).isPresent());
    }

    // endregion

    // region Private

    private Optional<Node> getClipPath(final SVGNodeBase<?> element) throws SVGException {
        return getClipPath(element, SVGElementBaseTest.getStyle(element), element.getTransformation().orElse(null));
    }

    @SuppressWarnings ("unchecked")
    private Optional<Node> getClipPath(final SVGNodeBase element, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
        try {
            final Method method = SVGNodeBase.class.getDeclaredMethod("getClipPath", SVGCssStyle.class, Transform.class);

            method.setAccessible(true);

            return Optional.class.cast(method.invoke(element, style, ownTransform));
        } catch (final IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not get method getClipPath", e.getCause());
        } catch (final InvocationTargetException e) {
            if (e.getTargetException() instanceof SVGException) {
                throw (SVGException) e.getTargetException();
            } else {
                throw new IllegalArgumentException("Could not invoke method getClipPath", e.getCause());
            }
        }
    }

    // endregion

}