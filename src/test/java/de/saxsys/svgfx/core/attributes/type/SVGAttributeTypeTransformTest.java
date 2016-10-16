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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.SVGUtil;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 16.10.2016.
 */
@RunWith (MockitoJUnitRunner.class)
public class SVGAttributeTypeTransformTest {

    // region Fields

    @Mock
    private SVGDocumentDataProvider dataProvider;

    private SVGAttributeTypeTransform cut;

    // endregion

    // region Setup

    @Before
    public void setup() {
        cut = new SVGAttributeTypeTransform(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * This test will create ensure that all types of matrix are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}}.
     */
    @Test
    public void aMatrixCanBeParsed() {

        Transform transform = null;

        try {
            transform = SVGUtil.getTransform("matrix(1,2,3,4,5,6)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, instanceOf(Affine.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 3.0d, 0.01d);
        assertEquals(transform.getTx(), 5.0d, 0.01d);
        assertEquals(transform.getMyx(), 2.0d, 0.01d);
        assertEquals(transform.getMyy(), 4.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("matrix(1 2 3 4 5 6)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, instanceOf(Affine.class));

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
            transform = SVGUtil.getTransform("motrix(1,2,3,4,5,6)");
        } catch (final Exception e) {
            fail();
        }

        assertNull(transform);
    }

    /**
     * Ensures that matrices which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidNumberOfAttributesForMatrixIsProvided() {

        try {
            SVGUtil.getTransform("matrix(1,2,3,4,5)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("matrix(1,2,3,4,5,6,7)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("matrix(1,2,3,4,5,A)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of translate are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aTranslateMatrixCanBeParsed() {

        Transform transform = null;

        try {
            transform = SVGUtil.getTransform("translate(1, 2)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("translate(1 2)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("translate(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Translate.class));

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
            transform = SVGUtil.getTransform("translata(1,2)");
        } catch (final Exception e) {
            fail();
        }

        assertNull(transform);
    }

    /**
     * Ensures that matrices which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidTranslateMatrixIsProvided() {

        try {
            SVGUtil.getTransform("translate(1,2,3)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("translate(1,A)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of scale are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aScaleMatrixCanBeParsed() {

        Transform transform = null;

        try {
            transform = SVGUtil.getTransform("scale(1, 2)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 2.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("scale(1 2)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 2.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("scale(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Scale.class));

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
            transform = SVGUtil.getTransform("scdle(1,2)");
        } catch (final Exception e) {
            fail();
        }

        assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidScaleMatrixIsProvided() {

        try {
            SVGUtil.getTransform("scale(1,2,3)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("scale(1,A)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of scale are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aRotateMatrixCanBeParsed() {

        Transform transform = null;

        try {
            transform = SVGUtil.getTransform("rotate(1, 2, 3)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Rotate.class));

        Rotate rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 2.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 3.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("rotate(1 2 3)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Rotate.class));

        rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 2.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 3.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("rotate(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Rotate.class));

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
            transform = SVGUtil.getTransform("rosate(1,2)");
        } catch (final Exception e) {
            fail();
        }

        assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidRotateMatrixIsProvided() {

        try {
            SVGUtil.getTransform("rotate(1,2)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("rotate(1,2,3, 4)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("rotate(1,A)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that all types of skewX are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aSkewMatrixCanBeParsed() {

        Transform transform = null;

        try {
            transform = SVGUtil.getTransform("skewX(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Shear.class));

        Shear shear = (Shear) transform;

        assertEquals(shear.getX(), 1.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("skewY(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Shear.class));

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
            transform = SVGUtil.getTransform("sketX(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNull(transform);

        try {
            transform = SVGUtil.getTransform("sketY(1)");
        } catch (final Exception e) {
            fail();
        }

        assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void throwExceptionIfAnInvalidSkewMatrixIsProvided() {

        try {
            SVGUtil.getTransform("skewX(1,2)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("skewY(1,2)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("skewX(A)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }

        try {
            SVGUtil.getTransform("skewY(A)");
            fail();
        } catch (final Exception e) {
            assertThat(e, new IsInstanceOf(SVGException.class));
        }
    }

    /**
     * This test will create ensure that combined matrices (consisting of more then one matrix in the string) are supported are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void multipleTransformMatricesWillBeCombined() {

        Transform transform = null;

        try {
            transform = SVGUtil.getTransform("translate(1,2) translate(3, 4)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 4.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);

        try {
            transform = SVGUtil.getTransform("translate(1,2) scale(3, 4)");
        } catch (final Exception e) {
            fail();
        }

        assertNotNull(transform);

        assertEquals(transform.getMxx(), 3.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 4.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);
    }

    //endregion
}