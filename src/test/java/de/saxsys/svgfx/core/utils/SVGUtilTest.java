/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGContentTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test to ensure the {@link SVGUtils} work as expected.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGUtilTest {

    /**
     * Ensures that {@link SVGUtils#split(String, List, de.saxsys.svgfx.core.utils.SVGUtils.SplitConsumer)} will throw the expected exceptions when arguments
     * are missing.
     */
    @Test
    public void ensureSplitThrowsTheExpectedExceptionsWhenArgumentsAreInvalid() {

        List<Character> delimiters = new ArrayList<>();
        delimiters.add(' ');

        try {
            SVGUtils.split(null, delimiters, (currentData, index) -> true);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            SVGUtils.split("", delimiters, (currentData, index) -> true);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            SVGUtils.split("Test", null, (currentData, index) -> true);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            SVGUtils.split("Test", new ArrayList<>(), (currentData, index) -> true);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            SVGUtils.split("Test", delimiters, null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            SVGUtils.split("Test", delimiters, (currentData, index) -> {
                throw new SVGException("test");
            });
            Assert.fail();
        } catch (SVGException ignored) {
        }

    }

    /**
     * Ensures that {@link SVGUtils#split(String, List, de.saxsys.svgfx.core.utils.SVGUtils.SplitConsumer)} is able to split a string which contains spaces
     * as expected.
     */
    @Test
    public void ensureSplitWillSplitAStringContainingSpacesCorrectly() {

        List<Character> delimiters = new ArrayList<>();
        delimiters.add(' ');

        List<String> result = SVGUtils.split("  This  is a  test  ", delimiters, (currentData, index) -> true);

        assertEquals(4, result.size());
        assertEquals("This", result.get(0));
        assertEquals("is", result.get(1));
        assertEquals("a", result.get(2));
        assertEquals("test", result.get(3));
    }

    /**
     * Ensures that {@link SVGUtils#split(String, List, de.saxsys.svgfx.core.utils.SVGUtils.SplitConsumer)} is able to split a string which contains multiple
     * delimiters.
     */
    @Test
    public void ensureSplitWillSplitAMoreThenOneDelimiter() {

        List<Character> delimiters = new ArrayList<>();
        delimiters.add(';');
        delimiters.add(',');
        delimiters.add(' ');

        List<String> result = SVGUtils.split(" This,is a;test", delimiters, (currentData, index) -> true);

        assertEquals(4, result.size());
        assertEquals("This", result.get(0));
        assertEquals("is", result.get(1));
        assertEquals("a", result.get(2));
        assertEquals("test", result.get(3));
    }

    /**
     * Ensures that {@link SVGUtils#split(String, List, de.saxsys.svgfx.core.utils.SVGUtils.SplitConsumer)} will only process {@link String}s without
     * delimiters.
     */
    @Test
    public void ensureSplitWillNeverContainDelimiters() {

        List<Character> delimiters = new ArrayList<>();
        delimiters.add(' ');

        List<String> result = SVGUtils.split(" This , is a test ", delimiters, (currentData, index) -> {

            Assert.assertFalse(currentData.contains(" "));

            return true;
        });

        assertEquals(5, result.size());
        assertEquals("This", result.get(0));
        assertEquals(",", result.get(1));
        assertEquals("is", result.get(2));
        assertEquals("a", result.get(3));
        assertEquals("test", result.get(4));
    }

    /**
     * Ensures that {@link SVGUtils#split(String, List, de.saxsys.svgfx.core.utils.SVGUtils.SplitConsumer)} will only process {@link String}s that are not
     * empty.
     */
    @Test
    public void ensureSplitWillNeverContainEmptyStrings() {

        List<Character> delimiters = new ArrayList<>();
        delimiters.add(' ');

        List<String> result = SVGUtils.split(" This , is a test ", delimiters, (currentData, index) -> {

            Assert.assertTrue(currentData.length() > 0);

            return true;
        });

        assertEquals(5, result.size());
        assertEquals("This", result.get(0));
        assertEquals(",", result.get(1));
        assertEquals("is", result.get(2));
        assertEquals("a", result.get(3));
        assertEquals("test", result.get(4));
    }

    /**
     * Ensures that {@link SVGUtils#split(String, List, de.saxsys.svgfx.core.utils.SVGUtils.SplitConsumer)} is able to combine strings if the dataConsumer
     * returns false.
     * In this test we will use the {@link String} "1 , 2 3 , 4" and the result should be "1,2" and "3,4".
     */
    @Test
    public void ensureSplitIsAbleToCombineResults() {

        List<Character> delimiters = new ArrayList<>();
        delimiters.add(' ');

        List<String> result = SVGUtils.split(" 1 , 2 3 , 4 ", delimiters, (currentData, index) -> {

            Assert.assertFalse(currentData.contains(" "));

            return currentData.charAt(currentData.length() - 1) != ',' && currentData.contains(",");
        });

        assertEquals(2, result.size());
        assertEquals("1,2", result.get(0));
        assertEquals("3,4", result.get(1));
    }

    /**
     * Ensures that {@link SVGUtils#stripIRIIdentifiers(String)} is able to resolve the url as expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void ensureStripIRIExpectedExceptions() {

        SVGUtils.stripIRIIdentifiers(null);
    }

    /**
     * Ensures that {@link SVGUtils#stripIRIIdentifiers(String)} is able to strip a string from its iri identifiers.
     */
    @Test
    public void ensureSplitIRIWillStripStringFromIRIIdentifiers() {

        assertEquals("test", SVGUtils.stripIRIIdentifiers(Constants.IRI_IDENTIFIER + "test)"));

        assertEquals("test", SVGUtils.stripIRIIdentifiers(Constants.IRI_FRAGMENT_IDENTIFIER + "test"));
    }

    /**
     * Ensures that {@link SVGUtils#stripIRIIdentifiers(String)} is able to strip a string from its iri identifiers.
     */
    @Test
    public void ensureSplitIRIWillReturnNullIfIRIIdentifiersCanNotBeStriped() {

        Assert.assertNull(SVGUtils.stripIRIIdentifiers("test)"));

        Assert.assertNull(SVGUtils.stripIRIIdentifiers("(#test"));

        Assert.assertNull(SVGUtils.stripIRIIdentifiers("url(#"));

        Assert.assertNull(SVGUtils.stripIRIIdentifiers("#"));
    }

    /**
     * Ensures that {@link SVGUtils#resolveIRI(String, SVGDataProvider, Class)} is able to resolve the url as expected.
     */
    @Test
    public void ensureResolveIRICauseTheExpectedExceptions() {

        try {
            SVGUtils.resolveIRI(null, new SVGDataProvider(), SVGElementBase.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("data"));
        }

        try {
            SVGUtils.resolveIRI("", new SVGDataProvider(), SVGElementBase.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("data"));
        }

        try {
            SVGUtils.resolveIRI("test", null, SVGElementBase.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("dataprovider"));
        }

        try {
            SVGUtils.resolveIRI("test", new SVGDataProvider(), null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("clazz"));
        }

        try {
            SVGUtils.resolveIRI(Constants.IRI_IDENTIFIER, new SVGDataProvider(), SVGCircle.class);
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains("reference"));
        }

        try {
            SVGUtils.resolveIRI(Constants.IRI_IDENTIFIER, new SVGDataProvider(), SVGCircle.class);
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains("reference"));
        }
    }

    /**
     * Ensures that {@link SVGUtils#resolveIRI(String, SVGDataProvider, Class)} is able to resolve the url as expected.
     */
    @Test
    public void ensureResolveIRICanResolveReference() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGDataProvider dataProvider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", new SVGCircle("circle", attributes, null, dataProvider));

        SVGCircle circle = SVGUtils.resolveIRI(Constants.IRI_IDENTIFIER + "test)", dataProvider, SVGCircle.class);

        Assert.assertNotNull(circle);

        circle = SVGUtils.resolveIRI(Constants.IRI_FRAGMENT_IDENTIFIER + "test", dataProvider, SVGCircle.class);

        Assert.assertNotNull(circle);
    }

    /**
     * Ensures that {@link SVGUtils#resolveIRI(String, SVGDataProvider, Class)} is able to resolve the url as expected.
     */
    @Test
    public void ensureResolveIRICanNotResolveReference() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        try {
            SVGUtils.resolveIRI(Constants.IRI_IDENTIFIER + "test1)", new SVGDataProvider(), SVGCircle.class);
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(Constants.IRI_IDENTIFIER + "test1)"));
        }
    }

    /**
     * Ensures that {@link SVGUtils#combineStylesAndResolveInheritance(SVGCssStyle, SVGCssStyle)} causes the expected exceptions
     */
    @Test
    public void ensureCombineAndResolveInheritanceCausesTheExpectedExceptions() {

        try {
            SVGUtils.combineStylesAndResolveInheritance(null, new SVGCssStyle(new SVGDataProvider()));
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            SVGUtils.combineStylesAndResolveInheritance(new SVGCssStyle(new SVGDataProvider()), null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that the value will be retrieved from the provided {@link SVGCssStyle}.
     */
    @Test
    public void ensureCombineAndResolveInheritanceUsesValuesFromOtherStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st1{fill:inherit;stroke:#222222}");

        SVGCssStyle style1 = new SVGCssStyle(new SVGDataProvider());

        style1.parseCssText(".st1{fill:#111111;}");

        SVGUtils.combineStylesAndResolveInheritance(style, style1);

        assertEquals(Color.web("#111111"), style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getValue());
        assertEquals(Color.web("#222222"), style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue());
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} of the inheritanceResolver will be added.
     */
    @Test
    public void ensureCombineAndResolveInheritanceAddsValuesFromOtherStyle() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st1{fill:inherit;}");

        SVGCssStyle style1 = new SVGCssStyle(new SVGDataProvider());

        style1.parseCssText(".st1{fill:#111111;stroke:#222222}");

        SVGUtils.combineStylesAndResolveInheritance(style, style1);

        assertEquals(Color.web("#111111"), style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getValue());
        assertEquals(Color.web("#222222"), style.getCssContentType(PresentationAttributeMapper.STROKE.getName(), SVGContentTypePaint.class).getValue());
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that the value already set will not be overwritten.
     */
    @Test
    public void ensureCombineAndResolveInheritanceDoesNotOverrideExistingValues() {

        SVGCssStyle style = new SVGCssStyle(new SVGDataProvider());

        style.parseCssText(".st1{fill:#333333;}");

        SVGCssStyle style1 = new SVGCssStyle(new SVGDataProvider());

        style1.parseCssText(".st1{fill:#111111;}");

        SVGUtils.combineStylesAndResolveInheritance(style, style1);

        assertEquals(Color.web("#333333"), style.getCssContentType(PresentationAttributeMapper.FILL.getName(), SVGContentTypePaint.class).getValue());
    }

    /**
     * This test will create ensure that all types of matrix are supported by
     * {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}}.
     */
    @Test
    public void ensureMatrixIsCreatedCorrectly() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("matrix(1,2,3,4,5,6)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Affine.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 3.0d, 0.01d);
        assertEquals(transform.getTx(), 5.0d, 0.01d);
        assertEquals(transform.getMyx(), 2.0d, 0.01d);
        assertEquals(transform.getMyy(), 4.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("matrix(1 2 3 4 5 6)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Affine.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 3.0d, 0.01d);
        assertEquals(transform.getTx(), 5.0d, 0.01d);
        assertEquals(transform.getMyx(), 2.0d, 0.01d);
        assertEquals(transform.getMyy(), 4.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);
    }

    /**
     * Parsing a matrix with an invalid name will not cause an exception.
     */
    @Test
    public void ensureInvalidMatrixNameWillNotCauseAnException() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("motrix(1,2,3,4,5,6)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(transform);
    }

    /**
     * Ensures that matrices which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidNumberOfAttributesForMatrixIsProvided() {

        try {
            SVGUtils.getTransform("matrix(1,2,3,4,5)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("matrix(1,2,3,4,5,6,7)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("matrix(1,2,3,4,5,A)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of translate are supported by
     * {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void parseTranslateMatrix() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("translate(1, 2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("translate(1 2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("translate(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 1.0d, 0.01d);
    }

    /**
     * Parsing a translate with an invalid name will not cause an exception.
     */
    @Test
    public void ensureInvalidTranslateNameWillNotCauseAnException() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("translata(1,2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(transform);
    }

    /**
     * Ensures that matrices which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidTranslateMatrixIsProvided() {

        try {
            SVGUtils.getTransform("translate(1,2,3)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("translate(1,A)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of scale are supported by
     * {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void parseScaleMatrix() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("scale(1, 2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 2.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("scale(1 2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 2.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("scale(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);
    }

    /**
     * That an invalid name of a scale matrix will be cause an exception
     */
    @Test
    public void ensureInvalidScaleNameWillNotCauseAnExceptionMatrix() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("scdle(1,2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidScaleMatrixIsProvided() {

        try {
            SVGUtils.getTransform("scale(1,2,3)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("scale(1,A)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of scale are supported by
     * {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void parseRotateMatrix() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("rotate(1, 2, 3)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Rotate.class));

        Rotate rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 2.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 3.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("rotate(1 2 3)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Rotate.class));

        rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 2.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 3.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("rotate(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Rotate.class));

        rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 0.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 0.0d, 0.01d);
    }

    /**
     * Ensures that an invalid name of rotate matrix will not cause an exception.
     */
    @Test
    public void ensureInvalidRotateNameWillNotCauseAnException() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("rosate(1,2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidRotateMatrixIsProvided() {

        try {
            SVGUtils.getTransform("rotate(1,2)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("rotate(1,2,3, 4)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("rotate(1,A)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of skewX are supported by
     * {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void parseSkewMatrix() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("skewX(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Shear.class));

        Shear shear = (Shear) transform;

        assertEquals(shear.getX(), 1.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("skewY(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Shear.class));

        shear = (Shear) transform;

        assertEquals(shear.getY(), 1.0d, 0.01d);
    }

    /**
     * Ensures that an invalid skew name will not cause an exception.
     */
    @Test
    public void ensureInvalidSkewNameWillNotCauseAnException() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("sketX(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(transform);

        try {
            transform = SVGUtils.getTransform("sketY(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidSkewMatrixIsProvided() {

        try {
            SVGUtils.getTransform("skewX(1,2)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("skewY(1,2)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("skewX(A)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtils.getTransform("skewY(A)");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that combined matrices (consisting of more then one matrix in the string) are supported are supported by
     * {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void parseCombinedMatrices() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("translate(1,2) translate(3, 4)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 4.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);

        try {
            transform = SVGUtils.getTransform("translate(1,2) scale(3, 4)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);

        assertEquals(transform.getMxx(), 3.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 4.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);
    }

    /**
     * Test that the opacity is altered as expected.
     */
    @Test
    public void applyOpacityToColorCorrectly() {

        Color red = Color.RED;

        SVGUtils.applyOpacity(red, 0.25d);

        assertEquals(0.25d, ((Color) SVGUtils.applyOpacity(red, 0.25d)).getOpacity(), 0.01d);
    }
}