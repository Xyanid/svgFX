/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
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

package de.saxsys.svgfx.core.css;


import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.elements.SVGShapeBase;
import de.saxsys.svgfx.css.core.CssContentTypeBase;
import de.saxsys.svgfx.css.core.CssStyle;

import java.lang.reflect.InvocationTargetException;

/**
 * This Class does not directly represent a SVG element but rather a Css element
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGCssStyle extends CssStyle<SVGCssContentTypeBase> {

    // region Enumeration

    /**
     * This contains all the presentation attributes an svg element might. The presentation attributes are used for {@link SVGShapeBase}.
     */
    public enum PresentationAttribute {
        /**
         * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
         */
        STROKE("stroke", SVGCssContentTypePaint.class),
        /**
         * Determines the type of stroke used.
         */
        STROKE_TYPE("stroke-type", SVGCssContentTypeStrokeType.class),
        /**
         * Determines the controls the pattern of dashes and gaps used to stroke paths.
         */
        STROKE_DASHARRAY("stroke-dasharray", SVGCssContentTypeStrokeDashArray.class),
        /**
         * Determines the dash offset of a stroke.
         */
        STROKE_DASHOFFSET("stroke-dashoffset", SVGCssContentTypeLength.class),
        /**
         * Determines the shape to be used at the end of open subpaths when they are stroked.
         */
        STROKE_LINECAP("stroke-linecap", SVGCssContentTypeStrokeLineCap.class),
        /**
         * Determines the shape to be used at the corners of paths or basic shapes when they are stroked.
         */
        STROKE_LINEJOIN("stroke-linejoin", SVGCssContentTypeStrokeLineJoin.class),
        /**
         * Determines a limit on the ratio of the miter length to the stroke-width. When the limit is exceeded, the join is converted from a miter to a bevel.
         */
        STROKE_MITERLIMIT("stroke-miterlimit", SVGCssContentTypeLength.class),
        /**
         * Determines the opacity of the outline on the current object.
         */
        STROKE_OPACITY("stroke-opacity", SVGCssContentTypeLength.class),
        /**
         * Determines the width of the outline on the current object.
         */
        STROKE_WIDTH("stroke-width", SVGCssContentTypeLength.class),
        /**
         * Represents a clip path which will be applied to the given element.
         */
        CLIP_PATH("clip-path", SVGCssContentTypeString.class),
        /**
         * Represents the clip rule which determines how an element inside a {@link de.saxsys.svgfx.core.elements.ClipPath} will be used.
         * It works like the {@link PresentationAttribute#FILL_RULE}.
         */
        CLIP_RULE("clip-rule", SVGCssContentTypeString.class),
        /**
         * Represents the color of the interior of the given graphical element.
         */
        FILL("fill", SVGCssContentTypePaint.class),
        /**
         * Represents the algorithm which is to be used to determine what side of a path is inside the shape.
         */
        FILL_RULE("fill-rule", SVGCssContentTypeFillRule.class),
        /**
         * Represents the color to use for a stop.
         */
        STOP_COLOR("stop-color", SVGCssContentTypePaint.class),
        /**
         * Represents the transparency of a gradient, that is, the degree to which the background behind the element is overlaid.
         */
        STOP_OPACITY("stop-opacity", SVGCssContentTypeLength.class),
        /**
         * Represents a color which cna be used for other rule such as fill, stroke or stop-color.
         */
        COLOR("color", SVGCssContentTypePaint.class),
        /**
         * Represents the transparency of an element, that is, the degree to which the background behind the element is overlaid.
         */
        OPACITY("opacity", SVGCssContentTypeLength.class);

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        /**
         * Determines the default value for this
         */
        private final Class<? extends SVGCssContentTypeBase> contentTypeClass;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name             the name of the attribute within the svg element
         * @param contentTypeClass the content type class to use
         */
        PresentationAttribute(final String name, final Class<? extends SVGCssContentTypeBase> contentTypeClass) {
            this.name = name;
            this.contentTypeClass = contentTypeClass;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link #name}.
         *
         * @return the {@link #name}
         */
        public final String getName() {
            return name;
        }

        /**
         * @return the {@link #contentTypeClass}.
         */
        public final Class<? extends SVGCssContentTypeBase> getContentTypeClass() {
            return contentTypeClass;
        }

        // endregion
    }

    // endregion

    // region Fields

    private final SVGDataProvider provider;

    // endregion

    //region Constructor

    /**
     * Creates a new instance.
     *
     * @param provider the data provider to use
     */
    public SVGCssStyle(final SVGDataProvider provider) {
        super();

        this.provider = provider;
    }

    /**
     * Creates a new instance.
     *
     * @param name     the name to of this style.
     * @param provider the data provider to use
     */
    public SVGCssStyle(final String name, final SVGDataProvider provider) {
        super(name);

        this.provider = provider;
    }

    //endregion

    // region Public

    /**
     * This implementation will use the name and validate it against {@link PresentationAttribute}s and then create an instance of a {@link CssContentTypeBase}.
     * If the given name does not correspond with any {@link PresentationAttribute}, no {@link CssContentTypeBase} will be created and null will be returned.
     *
     * @param name then name of the property
     *
     * @return a {@link CssContentTypeBase} or null if the name is not supported.
     */
    @Override
    protected SVGCssContentTypeBase createContentType(String name) {

        for (PresentationAttribute attribute : PresentationAttribute.values()) {
            if (attribute.getName().equals(name)) {
                try {
                    return attribute.getContentTypeClass().getConstructor(SVGDataProvider.class).newInstance(provider);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new SVGException("could not create an element for Content type class", e);
                }
            }
        }

        return null;
    }

    //endregion
}
