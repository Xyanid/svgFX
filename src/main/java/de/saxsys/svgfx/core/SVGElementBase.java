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

package de.saxsys.svgfx.core;

import com.oracle.webservices.internal.api.message.ContentType;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.core.CssStyle;
import de.saxsys.svgfx.css.core.CssValue;
import de.saxsys.svgfx.css.definitions.Constants;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.EnumSet;
import java.util.Map;

/**
 * This class represents a basic scg element, which provides some basic functionality to get the style of the class.
 *
 * @param <TResult> The type of the result this element will provide Created by Xyanid on 28.10.2015.
 */
public abstract class SVGElementBase<TResult> extends ElementBase<SVGDataProvider, TResult, SVGElementBase<?>> {

    // region Enumerations

    /**
     * Contains the core attributes each svg element may have.
     */
    public enum CoreAttribute {

        /**
         * The id for an element, needed in case an element is referenced by another element.
         */
        ID("id"),
        /**
         * Represents the transformation to be applied to an element.
         */
        TRANSFORM("transform"),
        /**
         * Represents the style of an element, the style need to follow the css text restrictions to be used.
         */
        STYLE("style"),
        /**
         * Represents a class link to an existing style, in this case the element will use this link to style itself.
         */
        CLASS("class"),
        /**
         * Represents x component of a center position, this element is used for {@link de.saxsys.svgfx.core.elements.Circle}s and {@link de.saxsys.svgfx.core.elements.Ellipse}s.
         */
        CENTER_X("cx"),
        /**
         * Represents y component of a center position, this element is used for {@link de.saxsys.svgfx.core.elements.Circle}s and {@link de.saxsys.svgfx.core.elements.Ellipse}s.
         */
        CENTER_Y("cy"),
        /**
         * Represents a radius.
         */
        RADIUS("r"),
        /**
         * Represents a radius which is used in the x direction.
         */
        RADIUS_X("rx"),
        /**
         * Represents a radius which is used in the y direction.
         */
        RADIUS_Y("ry"),
        /**
         * Represents the focus in x direction, this attribute is used by a radial gradient.
         */
        FOCUS_X("fx"),
        /**
         * Represents the focus in y direction, this attribute is used by a radial gradient.
         */
        FOCUS_Y("fy"),
        /**
         * Represents a comma separated list of points.
         */
        POINTS("points"),
        /**
         * Represents the start x component of a line.
         */
        START_X("x1"),
        /**
         * Represents the start y component of a line.
         */
        START_Y("y1"),
        /**
         * Represents the end x component of a line.
         */
        END_X("x2"),
        /**
         * Represents the end y component of a line.
         */
        END_Y("y2"),
        /**
         * Represents a series of path descriptions.
         */
        PATH_DESCRIPTION("d"),
        /**
         * Represents the x component of a position, how this is used depends on the element it is used in.
         */
        POSITION_X("x"),
        /**
         * Represents the y component of a position, how this is used depends on the element it is used in.
         */
        POSITION_Y("y"),
        /**
         * Represents the width of an element.
         */
        WIDTH("width"),
        /**
         * Represents the height of an element.
         */
        HEIGHT("height"),
        /**
         * Represents the offset from a start position.
         */
        OFFSET("offset"),
        /**
         * Represents the type of the element.
         */
        TYPE("type");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        CoreAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * This contains all the presentation attributes an svg element might. The presentation attributes are used for {@link de.saxsys.svgfx.core.SVGShapeBase}.
     */
    public enum PresentationAttribute {
        /**
         * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
         */
        STROKE("stroke", "#000000"),
        /**
         * Determines the type of stroke used.
         */
        STROKE_TYPE("stroke-type", null),
        /**
         * Determines the controls the pattern of dashes and gaps used to stroke paths.
         */
        STROKE_DASHARRAY("stroke-dasharray", null),
        /**
         * Determines the dash offset of a stroke.
         */
        STROKE_DASHOFFSET("stroke-dashoffset", "1"),
        /**
         * Determines the shape to be used at the end of open subpaths when they are stroked.
         */
        STROKE_LINECAP("stroke-linecap", null),
        /**
         * Determines the shape to be used at the corners of paths or basic shapes when they are stroked.
         */
        STROKE_LINEJOIN("stroke-linejoin", null),
        /**
         * Determines a limit on the ratio of the miter length to the stroke-width. When the limit is exceeded, the join is converted from a miter to a bevel.
         */
        STROKE_MITERLIMIT("stroke-miterlimit", "0"),
        /**
         * Determines the opacity of the outline on the current object.
         */
        STROKE_OPACITY("stroke-opacity", "1"),
        /**
         * Determines the width of the outline on the current object.
         */
        STROKE_WIDTH("stroke-width", "0"),
        /**
         * Represents a clip path which will be applied to the given element.
         */
        CLIP_PATH("clip-path", null),
        /**
         * Represents the clip rule which determines how an element inside a {@link de.saxsys.svgfx.core.elements.ClipPath} will be used.
         * It works like the {@link PresentationAttribute#FILL_RULE}.
         */
        CLIP_RULE("clip-rule", null),
        /**
         * Represents the color of the interior of the given graphical element.
         */
        FILL("fill", "#000000"),
        /**
         * Represents the algorithm which is to be used to determine what side of a path is inside the shape.
         */
        FILL_RULE("fill-rule", Enumerations.FillRuleMapping.EVEN_ODD.getName()),
        /**
         * Represents the color to use for a stop.
         */
        STOP_COLOR("stop-color", null),
        /**
         * Represents the transparency of a gradient, that is, the degree to which the background behind the element is overlaid.
         */
        STOP_OPACITY("stop-opacity", "1"),
        /**
         * Represents a color which cna be used for other rule such as fill, stroke or stop-color.
         */
        COLOR("color", "xxx"),
        /**
         * Represents the transparency of an element, that is, the degree to which the background behind the element is overlaid.
         */
        OPACITY("opacity", "1");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        /**
         * Determines the default value for this
         */
        private final String defaultValue;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name         the name of the attribute within the svg element
         * @param defaultValue the defaultValue
         */
        PresentationAttribute(final String name, final String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        /**
         * @return the {@link #defaultValue}.
         */
        public final String getDefaultValue() {
            return defaultValue;
        }

        // endregion
    }

    /**
     * Contains the xlink attributes each svg element may have.
     */
    public enum XLinkAttribute {

        /**
         * Meaning this element has a reference to an existing element.
         */
        XLINK_HREF("xlink:href");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        XLinkAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * Determines which keyword in a transform attribute of a matrix map to their corresponding javafx classes.
     */
    public enum Matrix {

        NONE(""),
        MATRIX("matrix"),
        TRANSLATE("translate"),
        SCALE("scale"),
        ROTATE("rotate"),
        SKEW_X("skewX"),
        SKEW_Y("skewY");

        // region Fields

        /**
         * The of the transformation within svg.
         */
        private final String name;


        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element.
         */
        Matrix(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link Matrix#name}.
         *
         * @return the {@link Matrix#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    // endregion

    // region Fields

    /**
     * The result represented by this element.
     */
    private TResult result;

    // endregion

    // region Constructor

    /**
     * Creates a new instance of he element using the given attributes, parent and dataProvider.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     *
     * @throws IllegalArgumentException if either value or dataProvider are null
     */
    public SVGElementBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region Public

    /**
     * This method attempts to create a {@link CssStyle} by looking up all the supported {@link PresentationAttribute}. If any attribute is present a
     * valid
     * cssString is returned.
     *
     * @return a {@link CssStyle} containing the {@link PresentationAttribute}s of this element if any or null if no
     * {@link PresentationAttribute} exists.
     */
    public final CssStyle getPresentationCssStyle() {

        CssStyle result = null;

        StringBuilder cssText = new StringBuilder();

        for (PresentationAttribute attribute : EnumSet.allOf(PresentationAttribute.class)) {

            String data = getAttribute(attribute.getName());

            if (StringUtils.isNotNullOrEmpty(data)) {
                if (cssText.length() == 0) {
                    cssText.append("presentationStyle" + Constants.DECLARATION_BLOCK_START);
                }

                cssText.append(String.format("%s%s%s%s", attribute.getName(), Constants.PROPERTY_SEPARATOR, data, Constants.PROPERTY_END));
            }
        }

        if (cssText.length() > 0) {
            cssText.append(Constants.DECLARATION_BLOCK_END);
            result = new CssStyle();
            result.consumeCssText(cssText.toString());
        }

        return result;
    }

    /**
     * Gets the elements own {@link CssStyle}, which will only be available if the element has the {@link CoreAttribute#STYLE}.
     *
     * @return the {@link CssStyle} of this element or null if there is none.
     */
    public final CssStyle getOwnStyle() {

        if (StringUtils.isNullOrEmpty(getAttribute(CoreAttribute.STYLE.getName()))) {
            return null;
        }

        String attribute = getAttribute(CoreAttribute.STYLE.getName());

        CssStyle ownStyle = new CssStyle();

        ownStyle.setCssText(String.format("ownStyle%s%s%s%s",
                                          Constants.DECLARATION_BLOCK_START,
                                          attribute,
                                          attribute.endsWith(Constants.PROPERTY_END_STRING) ? "" : Constants.PROPERTY_END,
                                          Constants.DECLARATION_BLOCK_END));

        return ownStyle;
    }

    /**
     * Gets the elements referenced {@link CssStyle}, which will only be available if the element has the {@link CoreAttribute#CLASS}.
     *
     * @return the {@link CssStyle} referenced by this element or null if there is none.
     */
    public CssStyle getReferencedStyle() {
        if (StringUtils.isNullOrEmpty(getAttribute(CoreAttribute.CLASS.getName()))) {
            return null;
        }

        String styleClass = getAttribute(CoreAttribute.CLASS.getName());

        return getDataProvider().getStyles().stream().filter(data -> data.getSelectorText().endsWith(styleClass)).findFirst().get();
    }

    /**
     * Returns the {@link CssStyle} of this element. Since an element can contain a {@link PresentationAttribute}s, an own {@link CssStyle} or a
     * reference to an existing {@link CssStyle} there need to be a rule how the {@link CssStyle} is build. The rule is as follows: </br>
     * {@link PresentationAttribute}s are preferred if they are present and will overwrite existing attribute of an own
     * {@link CssStyle} or a referenced {@link CssStyle}. The following example shows an element which has two
     * {@link PresentationAttribute}s and an own {@link CssStyle}.
     * <pre>
     *     <circle fill="none" stroke="#808080" style="fill:#111111; stroke:#001122 fill-rule:odd" />
     * </pre>
     * this will result in fill = none, stroke = #808080 and fill-rule = odd. The same behavior is to be expected if the {@link CssStyle} would be a reference e.g.
     * <pre>
     *     .st1{fill:#111111; stroke:#001122 fill-rule:odd}
     *     <circle fill="none" stroke="#808080" class="st1" />
     * </pre>
     * An own {@link CssStyle} is always preferred before a referenced {@link CssStyle} and will overwrite existing attributes just as a
     * {@link PresentationAttribute} would. The following example shows an element which has an own
     * {@link CssStyle} and a reference to a {@link CssStyle}.
     * <pre>
     *     .st1{fill:none; stroke:#808080 fill-rule:odd}
     *     <circle style="fill:#111111; stroke:#001122" class="st1"/>
     * </pre>
     * this will result in fill = 111111, stroke = #001122 and fill-rule = odd.
     *
     * @return the {@link CssStyle} of this element or first style in the {@link ElementBase#parent} tree
     */
    public final CssStyle getCssStyle() {

        // first we get a referenced style class if any
        CssStyle result = getReferencedStyle();

        // if an own style is present it will be used overwriting other attributes in the process
        CssStyle ownStyle = getOwnStyle();
        if (ownStyle != null) {
            if (result == null) {
                result = ownStyle;
            } else {
                result.combineWithStyle(ownStyle);
            }
        }

        // if a presentation style is present it will be used overwriting other attributes in the process
        CssStyle presentationStyle = getPresentationCssStyle();
        if (presentationStyle != null) {

            if (result == null) {
                result = presentationStyle;
            } else {
                result.combineWithStyle(presentationStyle);
            }
        }

        // now we apply inheritance so we can actually get the data from another style
        if (result != null && getParent() != null) {

            boolean hasInheritedValue = false;

            StringBuilder newCssStyleDeclaration = new StringBuilder();

            CssStyle parentStyle = null;

            for (Map.Entry<String, CssValue> cssValue : result.getCssStyleDeclaration().getUnmodifiableCssValues().entrySet()) {

                String newValue = cssValue.getValue().getValue();

                if (CssValue.INHERIT_INDICATOR.equals(cssValue.getValue().getValue())) {

                    hasInheritedValue = true;

                    if (parentStyle == null) {
                        parentStyle = getParent().getCssStyle();
                    }

                    // if the parent does not provide a value we use the default one
                    newValue = parentStyle.getCssStyleDeclaration().getPropertyValue(cssValue.getKey());
                    if (StringUtils.isNullOrEmpty(newValue)) {
                        newValue = cssValue.getValue().getDefaultValue();
                    }
                }

                newCssStyleDeclaration.append(String.format("%s%s%s%s", cssValue.getKey(), Constants.PROPERTY_SEPARATOR, newValue, Constants.PROPERTY_END));
            }

            // if one value in the original css style was inherited then we need to adjust the declaration block now
            if (hasInheritedValue) {
                result.getCssStyleDeclaration().consumeCssText(newCssStyleDeclaration.toString());
            }
        }

        return result;
    }

    /**
     * @return the transformation to be applied to this element if the {@link CoreAttribute#TRANSFORM} is present.
     * otherwise null.
     *
     * @throws SVGException if there is a transformation which has invalid data for its matrix.
     */
    public final Transform getTransformation() throws SVGException {
        if (StringUtils.isNotNullOrEmpty(getAttribute(CoreAttribute.TRANSFORM.getName()))) {
            return SVGUtils.getTransform(getAttribute(CoreAttribute.TRANSFORM.getName()));
        }

        return null;
    }

    /**
     * This method will be called in the {@link #createResult()} and allows to modify the result such as applying a style or transformations.
     *
     * @param result the result which should be modified.
     *
     * @throws SVGException will be thrown when an error during modification
     */
    protected abstract void initializeResult(final TResult result) throws SVGException;

    // endregion

    //region Abstract

    /**
     * Must be overwritten in the actual implementation to create a new result for this element based on the
     * information available.
     *
     * @return a new instance of the result or null of no result was created
     *
     * @throws SVGException will be thrown when an error during creation occurs
     */
    protected abstract TResult createResultInternal() throws SVGException;

    /**
     * Creates a result represented by this element.
     *
     * @return the result produced by this element.
     *
     * @throws SVGException thrown when an exception during creation occurs.
     */
    public final TResult createResult() throws SVGException {
        TResult result = createResultInternal();

        initializeResult(result);

        return result;
    }

    //endregion

    // region Override ElementBase

    /**
     * @inheritDoc
     */
    @Override public final TResult getResult() throws SVGException {

        if (result == null) {
            result = createResult();
        }

        return result;
    }

    /**
     * @inheritDoc
     */
    @Override public void startProcessing() throws SVGException {
    }

    /**
     * @inheritDoc
     */
    @Override public void processCharacterData(final char[] ch, final int start, final int length) throws SVGException {
    }

    /**
     * @inheritDoc
     */
    @Override public void endProcessing() throws SVGException {
    }

    // endregion
}
