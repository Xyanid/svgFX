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
import de.saxsys.svgfx.core.interfaces.ThrowableSupplier;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Transform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Xyanid on 06.11.2016.
 */
@SuppressWarnings ("unchecked")
@RunWith (MockitoJUnitRunner.class)
public class SVGShapeBaseIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    // endregion

    // region Tests

    /**
     * If a fill and a opacity exist in the style, then the fill color will be adjusted by the opacity.
     */
    @Test
    public void theFillColorWillBeInfluencedByAnOpacity() throws SAXException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:#FF0000;opacity:0.5;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final Rectangle result = cut.getResult();

        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5d), result.getFill());
    }

    /**
     * If a fill color that is a gradient and an opacity exist in the style, then the color will be adjusted by the opacity.
     */
    @Test
    public void ifTheFillIsALinearGradientAndAnOpacityIsProvidedItsStopColorsWillBeInfluencedByAnOpacity() throws SAXException, SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:url(#test);opacity:0.5;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final List<Stop> stops = new ArrayList<>(2);
        stops.add(new Stop(0, Color.web("#FF0000")));
        stops.add(new Stop(1, Color.web("#00FF00")));

        final LinearGradient linearGradient = new LinearGradient(0.0d, 0.0d, 1.0d, 1.0d, true, CycleMethod.NO_CYCLE, stops);

        final SVGGradientBase gradientBase = mock(SVGGradientBase.class);

        dataProvider.storeData("test", gradientBase);
        when(gradientBase.createResult(any(ThrowableSupplier.class), any(Transform.class))).thenReturn(linearGradient);

        final Rectangle result = cut.getResult();

        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5), LinearGradient.class.cast(result.getFill()).getStops().get(0).getColor());
        assertEquals(new Color(0.0d, 1.0d, 0.0d, 0.5), LinearGradient.class.cast(result.getFill()).getStops().get(1).getColor());
    }

    /**
     * If a fill color that is a gradient and an opacity exist in the style, then the color will be adjusted by the opacity.
     */
    @Test
    public void ifTheFillIsARadialGradientAndAnOpacityIsProvidedItsStopColorsWillBeInfluencedByAnOpacity() throws SAXException, SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("fill:url(#test);opacity:0.5;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final List<Stop> stops = new ArrayList<>(2);
        stops.add(new Stop(0, Color.web("#FF0000")));
        stops.add(new Stop(1, Color.web("#00FF00")));

        final RadialGradient radialGradient = new RadialGradient(0.0d, 0.0d, 1.0d, 1.0d, 1.0d, true, CycleMethod.NO_CYCLE, stops);

        final SVGGradientBase gradientBase = mock(SVGGradientBase.class);

        dataProvider.storeData("test", gradientBase);
        when(gradientBase.createResult(any(ThrowableSupplier.class), any(Transform.class))).thenReturn(radialGradient);

        final Rectangle result = cut.getResult();

        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5), RadialGradient.class.cast(result.getFill()).getStops().get(0).getColor());
        assertEquals(new Color(0.0d, 1.0d, 0.0d, 0.5), RadialGradient.class.cast(result.getFill()).getStops().get(1).getColor());
    }

    /**
     * If a stroke and a opacity exist in the style, then the fill color will be adjusted by the opacity.
     */
    @Test
    public void theStrokeColorWillBeInfluencedByAnStrokeOpacity() throws SAXException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("stroke:#FF0000;stroke-opacity:0.5;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final Rectangle result = cut.getResult();

        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5d), result.getStroke());
    }

    /**
     * If a fill color that is a gradient and an opacity exist in the style, then the color will be adjusted by the opacity.
     */
    @Test
    public void ifTheStrokeIsALinearGradientAndAnOpacityIsProvidedItsStopColorsWillBeInfluencedByAnStrokeOpacity() throws SAXException, SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("stroke:url(#test);stroke-opacity:0.5;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(10.0d, 10.0d, 100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final List<Stop> stops = new ArrayList<>(2);
        stops.add(new Stop(0, Color.web("#FF0000")));
        stops.add(new Stop(1, Color.web("#00FF00")));

        final LinearGradient linearGradient = new LinearGradient(0.0d, 0.0d, 1.0d, 1.0d, true, CycleMethod.NO_CYCLE, stops);

        final SVGGradientBase gradientBase = mock(SVGGradientBase.class);

        dataProvider.storeData("test", gradientBase);
        when(gradientBase.createResult(any(ThrowableSupplier.class), any(Transform.class))).thenReturn(linearGradient);

        final Rectangle result = cut.getResult();

        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5), LinearGradient.class.cast(result.getStroke()).getStops().get(0).getColor());
        assertEquals(new Color(0.0d, 1.0d, 0.0d, 0.5), LinearGradient.class.cast(result.getStroke()).getStops().get(1).getColor());
    }

    /**
     * If a fill color that is a gradient and an opacity exist in the style, then the color will be adjusted by the opacity.
     */
    @Test
    public void ifTheStrokeIsARadialGradientAndAnOpacityIsProvidedItsStopColorsWillBeInfluencedByAnStrokeOpacity() throws SAXException, SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn("stroke:url(#test);stroke-opacity:0.5;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final List<Stop> stops = new ArrayList<>(2);
        stops.add(new Stop(0, Color.web("#FF0000")));
        stops.add(new Stop(1, Color.web("#00FF00")));

        final RadialGradient radialGradient = new RadialGradient(0.0d, 0.0d, 1.0d, 1.0d, 1.0d, true, CycleMethod.NO_CYCLE, stops);

        final SVGGradientBase gradientBase = mock(SVGGradientBase.class);

        dataProvider.storeData("test", gradientBase);
        when(gradientBase.createResult(any(ThrowableSupplier.class), any(Transform.class))).thenReturn(radialGradient);

        final Rectangle result = cut.getResult();

        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5), RadialGradient.class.cast(result.getStroke()).getStops().get(0).getColor());
        assertEquals(new Color(0.0d, 1.0d, 0.0d, 0.5), RadialGradient.class.cast(result.getStroke()).getStops().get(1).getColor());
    }

    /**
     * The values from a style will be correctly applied to the result of a {@link SVGShapeBase}.
     */
    @Test
    public void whenTheResultIsCreatedTheStyleProvidedWillBeApplied() throws SAXException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        when(attributes.getValue(0)).thenReturn(
                "fill:#FF0000;stroke:#00FF00;stroke-type:inside;stroke-width:1;stroke-dasharray:2, 3, 4, 5;stroke-dashoffset:6;stroke-linejoin:bevel;stroke-linecap:butt;stroke-miterlimit:7;");

        final SVGShapeBase<Rectangle> cut = new SVGShapeBase<Rectangle>("Test", attributes, dataProvider) {
            @Override
            public SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final Rectangle rectangle) throws SVGException {
                return null;
            }

            @Override
            protected Rectangle createResult(final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                return new Rectangle(100.0d, 100.0d);
            }

            @Override
            protected void initializeResult(final Rectangle result, final SVGCssStyle style, final Transform ownTransform) throws SVGException {
                super.initializeResult(result, style, ownTransform);
            }
        };

        final Rectangle result = cut.getResult();

        assertEquals(Color.web("#FF0000"), result.getFill());
        assertEquals(Color.web("#00FF00"), result.getStroke());
        assertEquals(StrokeType.INSIDE, result.getStrokeType());
        assertEquals(1.0d, result.getStrokeWidth(), 0.01d);
        assertEquals(4, result.getStrokeDashArray().size());
        assertEquals(2.0d, result.getStrokeDashArray().get(0), 0.01d);
        assertEquals(3.0d, result.getStrokeDashArray().get(1), 0.01d);
        assertEquals(4.0d, result.getStrokeDashArray().get(2), 0.01d);
        assertEquals(5.0d, result.getStrokeDashArray().get(3), 0.01d);
        assertEquals(6.0d, result.getStrokeDashOffset(), 0.01d);
        assertEquals(StrokeLineJoin.BEVEL, result.getStrokeLineJoin());
        assertEquals(StrokeLineCap.BUTT, result.getStrokeLineCap());
        assertEquals(7.0d, result.getStrokeMiterLimit(), 0.01d);
    }

    // endregion
}