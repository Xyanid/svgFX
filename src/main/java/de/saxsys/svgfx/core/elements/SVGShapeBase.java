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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeDashArray;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeLineJoin;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeStrokeType;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a base class which contains shape element from svg.
 *
 * @param <TShape> type of the shape represented by this element
 *
 * @author Xyanid on 25.10.2015.
 */
public abstract class SVGShapeBase<TShape extends Shape> extends SVGNodeBase<TShape> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    protected SVGShapeBase(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    // region Override SVGNodeBase

    /**
     * {@inheritDoc}
     * Applies the css style the the element if possible.
     */
    @Override
    protected void initializeResult(final TShape result, final SVGCssStyle ownStyle) throws SVGException {
        super.initializeResult(result, ownStyle);

        applyStyle(result, ownStyle, getTransformation().orElse(null));
    }

    // endregion

    // region Private

    /**
     * Applies the basic style every {@link Shape} supports to the given shape.
     *
     * @param shape {@link Shape} to which the the styles should be applied, must not be null
     *
     * @throws SVGException when an error occurs during the applying of the style
     */
    private void applyStyle(final TShape shape, final SVGCssStyle ownStyle, final Transform transform)
            throws SVGException {

        if (shape == null) {
            throw new SVGException(SVGException.Reason.NULL_ARGUMENT, "Given shape must not be null");
        }


        // apply fill
        final Optional<SVGAttributeTypePaint> fill = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class);
        if (fill.isPresent()) {
            Paint paint = fill.get().getValue(() -> createBoundingBox(shape), transform);

            final Optional<SVGAttributeTypeDouble> opacity = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class);
            if (opacity.isPresent()) {
                paint = applyOpacity(paint, opacity.get().getValue());
            }

            shape.setFill(paint);
        }

        // apply stroke
        final Optional<SVGAttributeTypePaint> stroke = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class);
        if (stroke.isPresent()) {
            Paint paint = stroke.get().getValue(() -> createBoundingBox(shape), transform);

            final Optional<SVGAttributeTypeDouble> opacity = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_OPACITY.getName(), SVGAttributeTypeDouble.class);
            if (opacity.isPresent()) {
                paint = applyOpacity(paint, opacity.get().getValue());
            }

            shape.setStroke(paint);
        }

        // apply stroke width
        final Optional<SVGAttributeTypeLength> strokeWidth = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class);
        if (strokeWidth.isPresent()) {
            shape.setStrokeWidth(strokeWidth.get().getValue());
        }

        // apply stroke type
        final Optional<SVGAttributeTypeStrokeType> strokeType = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_TYPE.getName(), SVGAttributeTypeStrokeType.class);
        if (strokeType.isPresent()) {
            shape.setStrokeType(strokeType.get().getValue());
        }


        // apply stroke dash array
        final Optional<SVGAttributeTypeStrokeDashArray> strokeDashArray = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_DASHARRAY.getName(),
                                                                                                                     SVGAttributeTypeStrokeDashArray.class);
        if (strokeDashArray.isPresent()) {
            shape.getStrokeDashArray().clear();
            shape.getStrokeDashArray().addAll(strokeDashArray.get().getDashValues());
        }

        // apply stroke dash offset
        final Optional<SVGAttributeTypeLength> strokeDashOffset = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_DASHOFFSET.getName(),
                                                                                                             SVGAttributeTypeLength.class);
        if (strokeDashOffset.isPresent()) {
            shape.setStrokeDashOffset(strokeDashOffset.get().getValue());
        }

        // apply stroke line join
        final Optional<SVGAttributeTypeStrokeLineJoin> strokeLineJoin = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_LINEJOIN.getName(),
                                                                                                                   SVGAttributeTypeStrokeLineJoin.class);
        if (strokeLineJoin.isPresent()) {
            shape.setStrokeLineJoin(strokeLineJoin.get().getValue());
        }

        // apply stroke line cap
        final Optional<SVGAttributeTypeStrokeLineCap> strokeLineCap = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_LINECAP.getName(),
                                                                                                                 SVGAttributeTypeStrokeLineCap.class);
        if (strokeLineCap.isPresent()) {
            shape.setStrokeLineCap(strokeLineCap.get().getValue());
        }

        // apply stroke line cap
        final Optional<SVGAttributeTypeDouble> strokeMiterLimit = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(),
                                                                                                             SVGAttributeTypeDouble.class);
        if (strokeMiterLimit.isPresent()) {
            shape.setStrokeMiterLimit(strokeMiterLimit.get().getValue());
        }
    }

    /**
     * Applies the given opacity to the given {@link Paint}, overwriting the old opacity in the process.
     *
     * @param paint   the {@link Paint} to use.
     * @param opacity the opacity to apply.
     *
     * @return a new
     */
    private Paint applyOpacity(final Paint paint, final double opacity) {
        if (paint instanceof Color) {
            final Color color = (Color) paint;
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        } else if (paint instanceof LinearGradient) {
            final LinearGradient gradient = (LinearGradient) paint;
            final List<Stop> newStops = new ArrayList<>();
            gradient.getStops().forEach(stop -> newStops.add(new Stop(stop.getOffset(), new Color(stop.getColor().getRed(),
                                                                                                  stop.getColor().getGreen(),
                                                                                                  stop.getColor().getBlue(),
                                                                                                  opacity))));

            return new LinearGradient(gradient.getStartX(),
                                      gradient.getStartY(),
                                      gradient.getEndX(),
                                      gradient.getEndY(),
                                      gradient.isProportional(),
                                      gradient.getCycleMethod(),
                                      newStops);
        } else if (paint instanceof RadialGradient) {
            final RadialGradient gradient = (RadialGradient) paint;
            final List<Stop> newStops = new ArrayList<>();
            gradient.getStops().forEach(stop -> newStops.add(new Stop(stop.getOffset(), new Color(stop.getColor().getRed(),
                                                                                                  stop.getColor().getGreen(),
                                                                                                  stop.getColor().getBlue(),
                                                                                                  opacity))));

            return new RadialGradient(gradient.getFocusAngle(),
                                      gradient.getFocusDistance(),
                                      gradient.getCenterX(),
                                      gradient.getCenterY(),
                                      gradient.getRadius(),
                                      gradient.isProportional(),
                                      gradient.getCycleMethod(),
                                      newStops);
        }
        return paint;
    }

    /**
     * Returns the bounding box of the given shape
     *
     * @param shape the {@link Shape} to use.
     *
     * @return a new {@link de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle.SVGTypeRectangle}.
     */
    protected abstract SVGAttributeTypeRectangle.SVGTypeRectangle createBoundingBox(final TShape shape) throws SVGException;

    // endregion
}