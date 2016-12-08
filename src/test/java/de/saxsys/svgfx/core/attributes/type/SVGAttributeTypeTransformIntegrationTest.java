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
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Xyanid on 16.10.2016.
 */
public class SVGAttributeTypeTransformIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypeTransform cut;

    // endregion

    // region Setup

    @Before
    public void setup() {
        cut = new SVGAttributeTypeTransform(dataProvider);
    }

    // endregion

    //region Tests

    // matrix

    /**
     * This test will create ensure that all types of matrix are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}}.
     */
    @Test
    public void aMatrixCanBeParsedWithOrWithoutComaAsSeparators() throws SVGException {

        cut.setText("matrix(1,2,3,4,5,6)");
        Transform transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, instanceOf(Affine.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 3.0d, 0.01d);
        assertEquals(transform.getTx(), 5.0d, 0.01d);
        assertEquals(transform.getMyx(), 2.0d, 0.01d);
        assertEquals(transform.getMyy(), 4.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);

        cut.setText("matrix(1 2 3 4 5 6)");
        transform = cut.getValue();

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
    public void anInvalidMatrixNameWillNotCauseAnSVGException() throws SVGException {
        cut.setText("motrix(1,2,3,4,5,6)");

        final Transform transform = cut.getValue();

        assertNull(transform);
    }

    /**
     * Ensures that matrices which are invalid cause the expected exception.
     */
    @Test
    public void ifAnInvalidFormatForMatrixIsProvidedAnSVGExceptionWillBeThrown() {

        cut.setText("matrix(1,2,3,4,5)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("matrix(1,2,3,4,5,6,7)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("matrix(1,2,3,4,5,A)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }
    }

    // transform

    /**
     * This test will create ensure that all types of translate are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aTranslateMatrixCanBeParsedWithOrWithoutComaAsSeparators() throws SVGException {

        cut.setText("translate(1, 2)");

        Transform transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);

        cut.setText("translate(1 2)");

        transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Translate.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 1.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 2.0d, 0.01d);

        cut.setText("translate(1)");

        transform = cut.getValue();

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
    public void anInvalidTranslateNameWillNotCauseAnSVGException() throws SVGException {
        cut.setText("translata(1,2)");

        final Transform transform = cut.getValue();

        assertNull(transform);
    }

    /**
     * Ensures that matrices which are invalid cause the expected exception.
     */
    @Test
    public void ifAnInvalidFormatForTranslateMatrixIsProvidedAnSVGExceptionWillBeThrown() {

        cut.setText("translate(1,2,3)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("translate(1,A)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }
    }

    // scale

    /**
     * This test will create ensure that all types of scale are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aScaleMatrixCanBeParsedWithOrWithoutComaAsSeparators() throws SVGException {

        cut.setText("scale(1, 2)");

        Transform transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 2.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);

        cut.setText("scale(1 2)");

        transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Scale.class));

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 0.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 2.0d, 0.01d);
        assertEquals(transform.getTy(), 0.0d, 0.01d);

        cut.setText("scale(1)");

        transform = cut.getValue();

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
    public void anInvalidScaleNameWillNotCauseAnSVGException() throws SVGException {

        cut.setText("scdle(1,2)");

        final Transform transform = cut.getValue();

        assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void ifAnInvalidFormatForScaleMatrixIsProvidedAnSVGExceptionWillBeThrown() {

        cut.setText("scale(1,2,3)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("scale(1,A)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }
    }

    // rotate

    /**
     * This test will create ensure that all types of scale are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aRotateMatrixCanBeParsedWithOrWithoutComaAsSeparators() throws SVGException {

        cut.setText("rotate(1, 2, 3)");

        Transform transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Rotate.class));

        Rotate rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 2.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 3.0d, 0.01d);

        cut.setText("rotate(1 2 3)");

        transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Rotate.class));

        rotate = (Rotate) transform;

        assertEquals(rotate.getAngle(), 1.0d, 0.01d);
        assertEquals(rotate.getPivotX(), 2.0d, 0.01d);
        assertEquals(rotate.getPivotY(), 3.0d, 0.01d);

        cut.setText("rotate(1)");

        transform = cut.getValue();

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
    public void anInvalidRotateNameWillNotCauseAnSVGException() throws SVGException {

        cut.setText("rosate(1,2)");

        final Transform transform = cut.getValue();

        assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void ifAnInvalidFormatForRotateMatrixIsProvidedAnSVGExceptionWillBeThrown() {

        cut.setText("rotate(1,2)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("rotate(1,2,3, 4)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("rotate(1,2,A)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }
    }

    // Skew

    /**
     * This test will create ensure that all types of skewX are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void aSkewMatrixCanBeParsedForXOrYDirection() throws SVGException {

        cut.setText("skewX(1)");

        Transform transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Shear.class));

        Shear shear = (Shear) transform;

        assertEquals(shear.getX(), 1.0d, 0.01d);

        cut.setText("skewY(1)");

        transform = cut.getValue();

        assertNotNull(transform);
        assertThat(transform, new IsInstanceOf(Shear.class));

        shear = (Shear) transform;

        assertEquals(shear.getY(), 1.0d, 0.01d);
    }

    /**
     * Ensures that an invalid skew name will not cause an exception.
     */
    @Test
    public void anInvalidSkewNameWillNotCauseAnSVGException() throws SVGException {

        cut.setText("sketX(1)");

        Transform transform = cut.getValue();

        assertNull(transform);

        cut.setText("sketY(1)");

        transform = cut.getValue();

        assertNull(transform);
    }

    /**
     * Ensures that scale which are invalid cause the expected exception.
     */
    @Test
    public void ifAnInvalidFormatForSkewMatrixIsProvidedAnSVGExceptionWillBeThrown() {

        cut.setText("skewX(1,2)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("skewY(1,2)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_MATRIX_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH, e.getReason());
        }

        cut.setText("skewX(A)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }

        cut.setText("skewY(A)");

        try {
            cut.getValue();
            fail("Should not be able to get result when matrix is invalid");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_NUMBER_FORMAT, e.getReason());
        }
    }

    /**
     * This test will create ensure that combined matrices (consisting of more then one matrix in the string) are supported are supported by
     * {@link SVGAttributeTypeTransform#getTransform(String)} and {@link SVGAttributeTypeTransform#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test
    public void multipleTransformMatricesWillBeCombined() throws SVGException {

        cut.setText("translate(1,2) translate(3, 4)");

        Transform transform = cut.getValue();

        assertNotNull(transform);

        assertEquals(transform.getMxx(), 1.0d, 0.01d);
        assertEquals(transform.getMxy(), 0.0d, 0.01d);
        assertEquals(transform.getTx(), 4.0d, 0.01d);
        assertEquals(transform.getMyx(), 0.0d, 0.01d);
        assertEquals(transform.getMyy(), 1.0d, 0.01d);
        assertEquals(transform.getTy(), 6.0d, 0.01d);

        cut.setText("translate(1,2) scale(3, 4)");

        transform = cut.getValue();

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