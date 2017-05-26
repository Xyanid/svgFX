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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGGradientBase;
import de.saxsys.svgfx.core.interfaces.ThrowableSupplier;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import org.junit.Before;
import org.junit.Test;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xyanid on 16.10.2016.
 */
@SuppressWarnings ("unchecked")
public class SVGAttributeTypePaintIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    private SVGAttributeTypePaint cut;

    // endregion

    // region Setup

    @Before
    public void setUp() {
        cut = new SVGAttributeTypePaint(dataProvider);
    }

    // endregion

    //region Tests

    /**
     * Test that the opacity is altered as expected.
     */
    @Test
    public void whenCurrentColorIsDefinedNoColorWillBeReturnedButTheIndicatorForCurrentColorIsSet() throws SVGException {

        cut.setText("currentColor");

        final Paint paint = cut.getValue();

        assertNull(paint);
        assertTrue(cut.getIsCurrentColor());
    }

    /**
     * Test that the opacity is altered as expected.
     */
    @Test
    public void aLiteralDescriptionOfAColorCanBeParsedCorrectly() throws SVGException {

        cut.setText("RED");

        final Paint paint = cut.getValue();

        assertThat(paint, instanceOf(Color.class));
        assertEquals(Color.RED, paint);
    }

    /**
     * Test that the opacity is altered as expected.
     */
    @Test
    public void aHEXColorCodeCanBeParsedCorrectly() throws SVGException {

        cut.setText("#00FF00");

        final Paint paint = cut.getValue();

        assertThat(paint, instanceOf(Color.class));
        assertEquals(0.0d, ((Color) paint).getRed(), MINIMUM_DEVIATION);
        assertEquals(1.0d, ((Color) paint).getGreen(), MINIMUM_DEVIATION);
        assertEquals(0.0d, ((Color) paint).getBlue(), MINIMUM_DEVIATION);
        assertEquals(1.0d, ((Color) paint).getOpacity(), MINIMUM_DEVIATION);
    }

    /**
     * Test that the opacity is altered as expected.
     */
    @Test
    public void aRGBColorCodeCanBeParsedCorrectly() throws SVGException {

        cut.setText("rgb(255,128,64)");

        final Paint paint1 = cut.getValue();

        assertThat(paint1, instanceOf(Color.class));
        assertEquals(1.0d, ((Color) paint1).getRed(), MINIMUM_DEVIATION);
        assertEquals(0.5d, ((Color) paint1).getGreen(), MINIMUM_DEVIATION);
        assertEquals(0.25d, ((Color) paint1).getBlue(), MINIMUM_DEVIATION);
        assertEquals(1.0d, ((Color) paint1).getOpacity(), MINIMUM_DEVIATION);

        cut.setText("rgb(30%,20%,10%)");

        final Paint paint2 = cut.getValue();

        assertThat(paint2, instanceOf(Color.class));
        assertEquals(0.3d, ((Color) paint2).getRed(), MINIMUM_DEVIATION);
        assertEquals(0.2d, ((Color) paint2).getGreen(), MINIMUM_DEVIATION);
        assertEquals(0.1d, ((Color) paint2).getBlue(), MINIMUM_DEVIATION);
        assertEquals(1.0d, ((Color) paint2).getOpacity(), MINIMUM_DEVIATION);
    }


    /**
     * When the color described in the text is a reference to a {@link SVGGradientBase} but no {@link SVGElementBase} has been provided, an exception will be thrown.
     */
    @Test (expected = SVGException.class)
    public void whenTheTextIsAReferenceToAnNonExistingElementAndNotShapeHasBeenProvidedAnSVGExceptionWillBeThrown() throws SVGException {

        cut.setText(null);

        cut.getValue();
    }

    /**
     * When the color described in the text is a reference that can not be resolved into a {@link de.saxsys.svgfx.core.elements.SVGGradientBase}, an exception will be thrown.
     */
    @Test (expected = SVGException.class)
    public void whenTheTextIsAReferenceToAnNonExistingElementAnSVGExceptionWillBeThrown() throws SVGException {

        cut.setText("url(#test)");

        cut.getValue(mock(ThrowableSupplier.class), mock(Transform.class));
    }

    /**
     * When the color described in the text is a reference of a {@link SVGElementBase} that is not a {@link SVGGradientBase}, an exception will be thrown.
     */
    @Test (expected = SVGException.class)
    public void whenTheTextIsAReferenceToAnElementThatIsNotAGradientAnSVGExceptionWillBeThrown() throws SVGException {

        final SVGElementBase element = mock(SVGElementBase.class);
        dataProvider.storeData("test", element);

        cut.setText("url(#test)");

        cut.getValue(mock(ThrowableSupplier.class), mock(Transform.class));
    }

    /**
     * When the color described in the text is a reference to an existing {@link SVGGradientBase}, then the color of the gradient will be used.
     */
    @Test
    public void whenTheTextIsAReferenceToAnExistingGradientThePaintDescribedByTheGradientWillBeUsed() throws SVGException {

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = mock(SVGAttributeTypeRectangle.SVGTypeRectangle.class);

        final Paint expectedPaint = mock(Paint.class);

        final SVGGradientBase gradientBase = mock(SVGGradientBase.class);
        when(gradientBase.createResult(any(ThrowableSupplier.class), any(Transform.class))).thenReturn(expectedPaint);

        dataProvider.storeData("test", gradientBase);

        cut.setText("url(#test)");

        final Paint result = cut.getValue(() -> boundingBox, null);

        assertSame(expectedPaint, result);
        verify(gradientBase, times(1)).createResult(any(ThrowableSupplier.class), any(Transform.class));
    }

    //endregion
}