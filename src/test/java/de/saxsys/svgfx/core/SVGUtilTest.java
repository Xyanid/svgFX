package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test to ensure the {@link SVGUtils} work as expected.
 * Created by rico.hentschel on 05.10.2015.
 */
public final class SVGUtilTest {

    //region Fields

    public static final double STANDARD_DEVIATION = 0.01d;

    //endregion

    //region Tests

    /**
     * This test will create ensure that all types of matrix are supported by {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test public void createMatrix() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("matrix(1,2,3,4,5,6)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Affine.class));

        Assert.assertEquals(transform.getMxx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 3.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 5.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 2.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 4.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 6.0d, STANDARD_DEVIATION);

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
    @Test public void invalidMatrix() {

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
     * This test will create ensure that all types of translate are supported by {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test public void createTranslate() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("translate(1, 2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Translate.class));

        Assert.assertEquals(transform.getMxx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 2.0d, STANDARD_DEVIATION);

        try {
            transform = SVGUtils.getTransform("translate(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Translate.class));

        Assert.assertEquals(transform.getMxx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 1.0d, STANDARD_DEVIATION);

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
    @Test public void invalidTranslate() {

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
     * This test will create ensure that all types of scale are supported by {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test public void createScale() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("scale(1, 2)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Scale.class));

        Assert.assertEquals(transform.getMxx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 2.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 0.0d, STANDARD_DEVIATION);

        try {
            transform = SVGUtils.getTransform("scale(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Scale.class));

        Assert.assertEquals(transform.getMxx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 0.0d, STANDARD_DEVIATION);

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
    @Test public void invalidScale() {

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
     * This test will create ensure that all types of scale are supported by {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test public void createRotate() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("rotate(1, 2, 3)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Rotate.class));

        Rotate rotate = (Rotate) transform;

        Assert.assertEquals(rotate.getAngle(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(rotate.getPivotX(), 2.0d, STANDARD_DEVIATION);
        Assert.assertEquals(rotate.getPivotY(), 3.0d, STANDARD_DEVIATION);

        try {
            transform = SVGUtils.getTransform("rotate(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Rotate.class));

        rotate = (Rotate) transform;

        Assert.assertEquals(rotate.getAngle(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(rotate.getPivotX(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(rotate.getPivotY(), 0.0d, STANDARD_DEVIATION);

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
    @Test public void invalidRotate() {

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
     * This test will create ensure that all types of skewX are supported by {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test public void createSkew() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("skewX(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Shear.class));

        Shear shear = (Shear) transform;

        Assert.assertEquals(shear.getX(), 1.0d, STANDARD_DEVIATION);

        try {
            transform = SVGUtils.getTransform("skewY(1)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);
        Assert.assertThat(transform, new IsInstanceOf(Shear.class));

        shear = (Shear) transform;

        Assert.assertEquals(shear.getY(), 1.0d, STANDARD_DEVIATION);

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
    @Test public void invalidSkew() {

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
     * This test will create ensure that all types of matrix are supported by {@link SVGUtils#getTransform(String)} and {@link SVGUtils#getTransform(Enumerations.Matrix, String, boolean)}.
     */
    @Test public void createCombined() {

        Transform transform = null;

        try {
            transform = SVGUtils.getTransform("translate(1,2) translate(3, 4)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);

        Assert.assertEquals(transform.getMxx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 4.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 6.0d, STANDARD_DEVIATION);

        try {
            transform = SVGUtils.getTransform("translate(1,2) scale(3, 4)");
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(transform);

        Assert.assertEquals(transform.getMxx(), 3.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMxy(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTx(), 1.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyx(), 0.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getMyy(), 4.0d, STANDARD_DEVIATION);
        Assert.assertEquals(transform.getTy(), 2.0d, STANDARD_DEVIATION);
    }

    //endregion
}
