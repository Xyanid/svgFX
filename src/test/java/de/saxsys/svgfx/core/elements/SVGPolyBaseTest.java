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
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Transform;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * This test will ensure the {@link SVGPolyBase} behaves as expected.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGPolyBaseTest {

    /**
     * Ensures Points are parsed correctly
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100,40 100,80");

        final SVGPolyBase<Polygon> polyBase = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {
            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        assertEquals(6, polyBase.getPoints().size());
        assertEquals(60.0d, polyBase.getPoints().get(0), MINIMUM_DEVIATION);
        assertEquals(20.0d, polyBase.getPoints().get(1), MINIMUM_DEVIATION);
        assertEquals(100.0d, polyBase.getPoints().get(2), MINIMUM_DEVIATION);
        assertEquals(40.0d, polyBase.getPoints().get(3), MINIMUM_DEVIATION);
        assertEquals(100.0d, polyBase.getPoints().get(4), MINIMUM_DEVIATION);
        assertEquals(80.0d, polyBase.getPoints().get(5), MINIMUM_DEVIATION);
    }

    /**
     * Ensures multiple spaces in a row will cause no problem.
     */
    @Test
    public void multipleSpacesWithInThePointsSeparatorWillCauseNoProblems() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20    100,40    100,80");

        final SVGPolyBase<Polygon> polyBase = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {

            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        assertEquals(6, polyBase.getPoints().size());
        assertEquals(60.0d, polyBase.getPoints().get(0), MINIMUM_DEVIATION);
        assertEquals(20.0d, polyBase.getPoints().get(1), MINIMUM_DEVIATION);
        assertEquals(100.0d, polyBase.getPoints().get(2), MINIMUM_DEVIATION);
        assertEquals(40.0d, polyBase.getPoints().get(3), MINIMUM_DEVIATION);
        assertEquals(100.0d, polyBase.getPoints().get(4), MINIMUM_DEVIATION);
        assertEquals(80.0d, polyBase.getPoints().get(5), MINIMUM_DEVIATION);
    }

    /**
     * Ensures there are no points if the {@link CoreAttributeMapper#POINTS} is missing.
     */
    @Test
    public void pointsAreEmptyIfAttributeIsMissing() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGPolyBase<Polygon> polyBase = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {

            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        assertEquals(0, polyBase.getPoints().size());
    }

    /**
     * Ensures that points with a missing x or y position will cause an exception.
     */
    @Test
    public void whenAPointDoesNotProvideXAndYAnSVGExceptionIsThrownDuringTheRetrievalOfThePoints() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100 100,80");

        final SVGPolyBase<Polygon> polyBase1 = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {

            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        try {
            polyBase1.getPoints();
            fail("Should not be able to get points");
        } catch (final SVGException ignored) {
        }

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100,10 100");

        final SVGPolyBase<Polygon> polyBase2 = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {

            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        try {
            polyBase2.getPoints();
            fail("Should not be able to get points");
        } catch (final SVGException ignored) {
        }
    }

    /**
     * Ensures that points with a missing x or y position will cause an exception.
     */
    @Test (expected = SVGException.class)
    public void whenAPointContainsInvalidDataAnSVGExceptionWillBeThrownDuringTheRetrievalOfThePoints() throws SVGException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("60,20 100,A 100,80");

        final SVGPolyBase<Polygon> polyBase = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {

            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        polyBase.getPoints();
    }

    /**
     * The bounding rectangle described by the shape can be correctly determined.
     */
    @Test
    public void theBoundingBoxCanBeDeterminedCorrectly() throws SVGException {
        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        when(attributes.getValue(0)).thenReturn("20,20 100,100 10,120");

        final SVGPolyBase<Polygon> polyBase = new SVGPolyBase<Polygon>("polygon", attributes, new SVGDocumentDataProvider()) {

            @Override
            protected Polygon createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return null;
            }
        };

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = polyBase.createBoundingBox(null);

        assertEquals(10.0d, boundingBox.getMinX().getValue(), MINIMUM_DEVIATION);
        assertEquals(100.0d, boundingBox.getMaxX().getValue(), MINIMUM_DEVIATION);
        assertEquals(20.0d, boundingBox.getMinY().getValue(), MINIMUM_DEVIATION);
        assertEquals(120.0d, boundingBox.getMaxY().getValue(), MINIMUM_DEVIATION);
    }
}