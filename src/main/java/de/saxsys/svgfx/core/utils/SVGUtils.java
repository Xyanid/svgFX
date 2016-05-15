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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeType;
import de.saxsys.svgfx.core.content.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeDashArray;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeLineJoin;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeType;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGLinearGradient;
import de.saxsys.svgfx.core.elements.SVGRadialGradient;
import de.saxsys.svgfx.css.definitions.Constants;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class provides functionality related to svg processing
 *
 * @author by Xyanid on 02.11.2015.
 */
public final class SVGUtils {

    // region Classes

    /**
     * This interface allows that {@link #split(String, List, SplitConsumer)} to consume data an indicate whether to further add new characters or consume
     * the currentData.
     */
    @FunctionalInterface
    public interface SplitConsumer {
        /**
         * Called when a delimiter or the last character is read and will indicate whether the current read data can be used or not.
         *
         * @param currentData {@link String} containing the currently read data so far.
         * @param index       the index of the character in the original data that was currently read
         *
         * @return true if the currentData shall be consumed otherwise false if not.
         *
         * @throws SVGException in case the splitting is not possible due to invalid data ans so on.
         */
        boolean consume(String currentData, int index) throws SVGException;
    }

    // endregion

    // region Fields

    private static final char CLOSING_BRACE = ')';

    // endregion

    // region Constructor

    private SVGUtils() {
    }

    // endregion

    // region Methods misc

    /**
     * Strips the given {@link String} from the IRI identifiers if any.
     *
     * @param data the {@link String} to be stripped, must not be null or empty.
     *
     * @return the data striped of its IRI identifiers or null the string does not contain any IRI identifiers.
     *
     * @throws IllegalArgumentException if data is null or empty.
     */
    public static String stripIRIIdentifiers(final String data) throws IllegalArgumentException {

        if (StringUtils.isNullOrEmpty(data)) {
            throw new IllegalArgumentException("given data must not be null or empty");
        }

        // initially we assume that the IRI_IDENTIFIER was used
        int dataLengthReduction = 1;
        int identifierLength = de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER.length();
        int index = -1;
        if (data.length() > de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER.length() &&
            data.startsWith(de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER)) {
            index = de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER.length();
        }

        if (index == -1) {
            dataLengthReduction = 0;
            identifierLength = de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER.length();
            if (data.length() > de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER.length() &&
                data.startsWith(de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER)) {
                index = de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER.length();
            }
        }

        if (index > -1) {
            return data.substring(identifierLength, data.length() - dataLengthReduction);
        }

        return null;
    }

    // endregion

    // region Methods related to Styling

    /**
     * Returns the element which might be referenced by the given data. The data will need to start with the
     * {@link de.saxsys.svgfx.core.definitions.Constants#IRI_IDENTIFIER} in order to be
     * resolved as a reference.
     *
     * @param data              the string which contains the reference to resolve.
     * @param dataProvider      the {@link SVGDataProvider} which contains the data which is referenced.
     * @param clazz             the class of the element that is expected.
     * @param <TSVGElementBase> the type of element which is expected.
     *
     * @return the {@link SVGElementBase} which is referenced by the data or null if the data does not reference an element.
     *
     * @throws SVGException             if the data references a resource which is not contained in the {@link SVGDataProvider}.
     * @throws IllegalArgumentException if either the data is null or empty, the dataProvider is null or the clazz is null.
     */
    public static <TSVGElementBase extends SVGElementBase<?>> TSVGElementBase resolveIRI(final String data,
                                                                                         final SVGDataProvider dataProvider,
                                                                                         final Class<TSVGElementBase> clazz)
            throws SVGException, IllegalArgumentException {

        if (dataProvider == null) {
            throw new IllegalArgumentException("given dataprovider must not be null");
        }

        if (clazz == null) {
            throw new IllegalArgumentException("given clazz must not be null");
        }

        String reference = stripIRIIdentifiers(data);
        if (StringUtils.isNullOrEmpty(reference)) {
            throw new SVGException(String.format("Given data %s appears to not be a IRI reference.", data));
        }

        TSVGElementBase result;

        try {
            result = dataProvider.getData(clazz, reference);
        } catch (Exception e) {
            throw new SVGException("An error occurred during the parsing of the reference", e);
        }

        if (result == null) {
            throw new SVGException(String.format("Given reference %s could not be resolved", data));
        }

        return result;
    }

    /**
     * Resolves the given data into a paint. The data must either be valid hex web color (e.g. #00FF00FF)
     * or a reference which can be resolved by the given data dataProvider.
     *
     * @param data         data to be resolved
     * @param dataProvider the {@link SVGDataProvider} to be used, must not be null
     *
     * @return {@link Paint} which represents the color
     *
     * @throws SVGException             if the data references another element which is not found in the given data dataProvider.
     * @throws IllegalArgumentException if the the data is empty, the dataProvider is null.
     */
    public static Paint parseColor(final String data, final SVGDataProvider dataProvider) throws SVGException, IllegalArgumentException {

        if (StringUtils.isNullOrEmpty(data)) {
            throw new IllegalArgumentException("given data must not be null or empty");
        }

        // its not possible to use the IRI_FRAGMENT_IDENTIFIER on colors so we will only resolve references if we are sure its not a color itself
        SVGElementBase reference = null;

        if (data.startsWith(de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER)) {
            reference = resolveIRI(data, dataProvider, SVGElementBase.class);
        }

        Paint result = null;

        if (reference != null) {

            if (reference instanceof SVGLinearGradient) {
                result = ((SVGLinearGradient) reference).getResult();
            } else if (reference instanceof SVGRadialGradient) {
                result = ((SVGRadialGradient) reference).getResult();
            }

            if (result == null) {
                throw new IllegalArgumentException("given data can not be resolved to a color");
            }
        } else if (data.equals(Constants.PROPERTY_VALUE_NONE)) {
            result = Color.TRANSPARENT;
        } else {
            result = Color.web(data);
        }

        return result;
    }

    /**
     * Applies the basic style every {@link Shape} supports to the given shape.
     *
     * @param shape        {@link Shape} to which the the styles should be applied, must not be null
     * @param style        {@link SVGCssStyle} to use, must not be null
     * @param dataProvider the {@link SVGDataProvider} to be used
     * @param <TShape>     type of the shape to be used
     *
     * @throws IllegalArgumentException if either shape or style is null
     */
    public static <TShape extends Shape> void applyStyle(final TShape shape, final SVGCssStyle style, final SVGDataProvider dataProvider)
            throws IllegalArgumentException {

        if (dataProvider == null) {
            throw new IllegalArgumentException("Given dataProvider must not be null");
        }

        if (shape == null) {
            throw new IllegalArgumentException("Given shape must not be null");
        }

        if (style == null) {
            throw new IllegalArgumentException("Given style must not be null");
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.FILL.getName())) {

            Paint paint = style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue();

            if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.OPACITY.getName())) {
                SVGUtils.applyOpacity(paint,
                                      style.getAttributeTypeHolder()
                                           .getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class)
                                           .getValue());
            }

            shape.setFill(paint);
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE.getName())) {

            Paint paint = style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue();

            if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.OPACITY.getName())) {
                SVGUtils.applyOpacity(paint,
                                      style.getAttributeTypeHolder()
                                           .getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class)
                                           .getValue());
            }

            shape.setStroke(paint);
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_TYPE.getName())) {
            shape.setStrokeType(style.getAttributeTypeHolder()
                                     .getAttribute(PresentationAttributeMapper.STROKE_TYPE.getName(), SVGAttributeTypeStrokeType.class)
                                     .getValue());
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName())) {
            shape.setStrokeWidth(style.getAttributeTypeHolder()
                                      .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                      .getValue());
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_DASHARRAY.getName())) {
            shape.getStrokeDashArray().clear();
            shape.getStrokeDashArray().addAll(style.getAttributeTypeHolder()
                                                   .getAttribute(PresentationAttributeMapper.STROKE_DASHARRAY.getName(), SVGAttributeTypeStrokeDashArray.class)
                                                   .getDashValues());
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_DASHOFFSET.getName())) {
            shape.setStrokeDashOffset(style.getAttributeTypeHolder()
                                           .getAttribute(PresentationAttributeMapper.STROKE_DASHOFFSET.getName(), SVGAttributeTypeLength.class)
                                           .getValue());
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_LINEJOIN.getName())) {
            shape.setStrokeLineJoin(style.getAttributeTypeHolder()
                                         .getAttribute(PresentationAttributeMapper.STROKE_LINEJOIN.getName(), SVGAttributeTypeStrokeLineJoin.class)
                                         .getValue());
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_LINECAP.getName())) {
            shape.setStrokeLineCap(style.getAttributeTypeHolder()
                                        .getAttribute(PresentationAttributeMapper.STROKE_LINECAP.getName(), SVGAttributeTypeStrokeLineCap.class)
                                        .getValue());
        }

        if (style.getAttributeTypeHolder().hasAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName())) {
            shape.setStrokeMiterLimit(style.getAttributeTypeHolder()
                                           .getAttribute(PresentationAttributeMapper.STROKE_MITERLIMIT.getName(), SVGAttributeTypeDouble.class)
                                           .getValue());
        }
    }

    /**
     * This method will use the contentMap of the given style, if any property uses inheritance, the given otherStyle will be used to resolve it.
     * If the otherStyle does not contain a value for the inherited property, then the default value will be used.
     *
     * @param style      {@link SVGCssStyle} which contains the contentMap which are to be set, must not be null.
     * @param otherStyle the {@link SVGCssStyle} to use as a parent in order to resolve the inheritance, must not be null.
     *
     * @throws IllegalArgumentException if either style or inheritanceResolver are null.
     */
    public static void combineStylesAndResolveInheritance(final SVGCssStyle style, final SVGCssStyle otherStyle) throws IllegalArgumentException {
        if (style == null) {
            throw new IllegalArgumentException("Given style must not be null");
        }

        if (otherStyle == null) {
            throw new IllegalArgumentException("Given otherStyle must not be null");
        }

        style.combineWithStyle(otherStyle);

        for (Map.Entry<String, SVGAttributeType> property : style.getProperties().entrySet()) {
            if (property.getValue().getIsInherited()) {
                SVGAttributeType otherProperty = otherStyle.getAttributeTypeHolder().getAttribute(property.getKey());
                if (otherProperty != null && !otherProperty.getIsInherited()) {
                    if (otherProperty.getIsNone()) {
                        property.getValue().consumeText(SVGAttributeType.NONE_INDICATOR);
                    } else {
                        property.getValue().setValue(otherProperty.getValue());
                        property.getValue().setUnit(otherProperty.getUnit());
                    }
                } else {
                    property.getValue().setValue(property.getValue().getDefaultValue());
                    property.getValue().setUnit(null);
                }
            }
        }
    }

    /**
     * Returns the combined transformations available in the given string. The string must be contain data
     * corresponding to the SVGRoot specification for the transform attributes.
     *
     * @param data the string which contains the transformation data.
     *
     * @return the transformation which was gathered from the data or null if no data was gathered.
     *
     * @throws SVGException if there is an error in the transformation data of the given string.
     */
    public static Transform getTransform(final String data) throws SVGException {
        if (StringUtils.isNullOrEmpty(data)) {
            return null;
        }

        Transform result = null;

        EnumSet<Enumerations.Matrix> allMatrices = EnumSet.allOf(Enumerations.Matrix.class);
        allMatrices.remove(Enumerations.Matrix.NONE);

        for (int i = 0; i < data.length(); i++) {

            for (Enumerations.Matrix matrix : allMatrices) {
                if (data.startsWith(matrix.getName(), i)) {

                    // getting the start and end of the new substring, which contains the data in the braces
                    int start = i + matrix.getName().length() + 1;
                    i = data.indexOf(CLOSING_BRACE, start);

                    Transform transform = getTransform(matrix, data.substring(start, i), false);

                    if (result == null) {
                        result = transform;
                    } else {
                        result = result.createConcatenation(transform);
                    }

                    break;
                }
            }
        }

        return result;
    }

    /**
     * Applies the given opacity to the given {@link Paint}, overwriting the old opacity in the process.
     *
     * @param paint   the {@link Paint} to use.
     * @param opacity the opacity to apply.
     *
     * @return a new
     */
    public static Paint applyOpacity(final Paint paint, final double opacity) {
        if (paint instanceof Color) {
            Color color = (Color) paint;
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        }
        return paint;
    }

    // endregion

    // region Methods related to Transformation

    /**
     * Gets the {@link Transform} that is represented by the given data. The data must meet the following requirements.
     * Data can start with the name of the provide {@link Enumerations.Matrix}, in which case checkIfStartWithMatrix must be true,
     * otherwise an exception will occur when the actual data is processed.
     * Data must contain the values separated with a coma (e.g. 1,2,3). Optionally the values can be embraces with ().
     *
     * @param matrix                 the matrix to use
     * @param data                   the data to be used, must not be null or empty or {@link Enumerations.Matrix#NONE}.
     * @param checkIfStartWithMatrix determines if the given data is to be checked if it starts with the {@link Enumerations.Matrix#name}
     *
     * @return a new {@link Transform} which contains the transformation represented by the data.
     *
     * @throws IllegalArgumentException if the given data is empty or matrix is {@link Enumerations.Matrix#NONE}.
     * @throws SVGException             if there is an error in the transformation data of the given string.
     */
    public static Transform getTransform(final Enumerations.Matrix matrix, final String data, final boolean checkIfStartWithMatrix) throws SVGException {
        if (StringUtils.isNullOrEmpty(data)) {
            throw new IllegalArgumentException("Given data must not be null or empty");
        }

        if (matrix == Enumerations.Matrix.NONE) {
            throw new IllegalArgumentException("Given matrix must not be NONE");
        }

        Transform result;

        String actualData = data;

        // check if we need to extract the matrix name
        if (checkIfStartWithMatrix) {

            if (!data.startsWith(matrix.getName())) {
                throw new IllegalArgumentException(String.format("Given data does not start with the expected Matrix: %s", matrix.getName()));
            }

            actualData = actualData.substring(matrix.getName().length()).trim();
        }
        // check if we need to remove the braces at the start and end as well.
        if (actualData.charAt(0) == '(') {
            actualData = actualData.substring(1);
        }
        if (actualData.charAt(actualData.length() - 1) == ')') {
            actualData = actualData.substring(0, actualData.length());
        }

        List<String> values = split(actualData, Arrays.asList(' ', ','), (currentData, index) -> true);

        switch (matrix) {
            // a matrix will create an affine matrix and has 6 values
            case MATRIX:
                if (values.size() != 6) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 6 values but got %d",
                                                         matrix.getName(),
                                                         values.size()));
                }

                try {
                    // this is in corresponds with the svg spec, so that is why the number are not in order
                    result = new Affine(Double.parseDouble(values.get(0).trim()),
                                        Double.parseDouble(values.get(2).trim()),
                                        Double.parseDouble(values.get(4).trim()),
                                        Double.parseDouble(values.get(1).trim()),
                                        Double.parseDouble(values.get(3).trim()),
                                        Double.parseDouble(values.get(5).trim()));
                } catch (NumberFormatException e) {
                    throw new SVGException(e);
                }
                break;

            // a translate/scale will create a translate/scale matrix and has either 1 or 2 values
            case TRANSLATE:
            case SCALE:
                if (values.size() != 1 && values.size() != 2) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 1 or 2 values but got %d",
                                                         matrix.getName(),
                                                         values.size()));
                }

                try {
                    // if only one value is present the the second one is assume to be like the first
                    double x = Double.parseDouble(values.get(0).trim());
                    double y = values.size() == 2 ? Double.parseDouble(values.get(1).trim()) : x;

                    if (matrix == Enumerations.Matrix.TRANSLATE) {
                        result = new Translate(x, y);
                    } else {
                        result = new Scale(x, y);
                    }
                } catch (NumberFormatException e) {
                    throw new SVGException(e);
                }
                break;

            // a rotate will create a rotate matrix and has either 1 or 3 values
            case ROTATE:
                if (values.size() != 1 && values.size() != 3) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 1 or 3 values but got %d",
                                                         matrix.getName(),
                                                         values.size()));
                }

                // if more then one value is present then the rotation also contains a translation
                try {
                    double x = values.size() == 3 ? Double.parseDouble(values.get(1).trim()) : 0.0d;
                    double y = values.size() == 3 ? Double.parseDouble(values.get(2).trim()) : 0.0d;

                    result = new Rotate(Double.parseDouble(values.get(0).trim()), x, y);
                } catch (NumberFormatException e) {
                    throw new SVGException(e);
                }
                break;

            // default would be a skewY/POSITION_X which will create a shear matrix and have only one value
            // we do not actually need the default but otherwise the compiler will complain that result is not initialized
            case SKEW_X:
            case SKEW_Y:
            default:
                if (values.size() != 1) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 1 value but got %d",
                                                         matrix.getName(),
                                                         values.size()));
                }

                double shearing;

                try {
                    shearing = Double.parseDouble(values.get(0).trim());
                } catch (NumberFormatException e) {
                    throw new SVGException(e);
                }

                if (matrix == Enumerations.Matrix.SKEW_X) {
                    result = new Shear(shearing, 0.0d);
                } else {
                    result = new Shear(0.0d, shearing);
                }
                break;
        }

        return result;
    }

    /**
     * This method will iterate through the given toSplit and call the dataConsumer {@link Function} when the current character in toSplit is contained in
     * the delimiters.
     * The {@link String} passed to the dataConsumer will contain all the characters read in the String that are NOT considered delimiters. When the
     * dataConsumer returns true, the passed
     * {@link String} will be added to the list of results. A new call to the dataConsumer will then contain a new {@link String}.
     *
     * @param toSplit      the {@link String} which is to be parsed, must not be null or empty.
     * @param delimiters   the characters which indicate a delimiter in the toSplit, must not be null or empty.
     * @param dataConsumer the {@link Function} to be called when a character that is a delimiter is read, must not be null.
     *
     * @return the list of {@link String}s which are between the delimiters of the toSplit if any or an empty list.
     *
     * @throws SVGException             is rethrown by the dataConsumer
     * @throws IllegalArgumentException if either toSplit, delimiter or dataConsumer are null or empty.
     */
    public static List<String> split(String toSplit, List<Character> delimiters, SplitConsumer dataConsumer) throws SVGException, IllegalArgumentException {
        if (StringUtils.isNullOrEmpty(toSplit)) {
            throw new IllegalArgumentException("Given toSplit must not be null or empty");
        }

        if (dataConsumer == null) {
            throw new IllegalArgumentException("Given dataConsumer must not be null");
        }

        if (delimiters == null || delimiters.isEmpty()) {
            throw new IllegalArgumentException("Given delimiters must not be null or empty");
        }

        List<String> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        boolean isLastCharacter;

        boolean isDelimiter;

        for (int i = 0; i < toSplit.length(); i++) {
            char character = toSplit.charAt(i);

            isDelimiter = delimiters.contains(character);

            isLastCharacter = i == toSplit.length() - 1;

            if (isLastCharacter || isDelimiter) {

                if (isLastCharacter && !isDelimiter) {
                    builder.append(character);
                }

                if (builder.length() > 0 && dataConsumer.consume(builder.toString(), i)) {
                    result.add(builder.toString());
                    builder.setLength(0);
                }
            } else {
                builder.append(character);
            }
        }

        return result;
    }

    // endregion

    // region Misc

    public static Point getRelativePosition(final Point position, final SVGElementBase parent) {
        SVGAttributeTypeLength parentX = SVGAttributeTypeLength.class.cast(parent.getAttributeHolder().getAttribute(CoreAttributeMapper.POSITION_X.getName()));
        SVGAttributeTypeLength parentY = SVGAttributeTypeLength.class.cast(parent.getAttributeHolder().getAttribute(CoreAttributeMapper.POSITION_Y.getName()));
        SVGAttributeTypeLength width = SVGAttributeTypeLength.class.cast(parent.getAttributeHolder().getAttribute(CoreAttributeMapper.WIDTH.getName()));
        SVGAttributeTypeLength height = SVGAttributeTypeLength.class.cast(parent.getAttributeHolder().getAttribute(CoreAttributeMapper.HEIGHT.getName()));

        return null;
    }

    // endregion
}