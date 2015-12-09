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

import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.elements.LinearGradient;
import de.saxsys.svgfx.core.elements.RadialGradient;
import de.saxsys.svgfx.core.utils.ConvertUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.core.CssStyle;
import de.saxsys.svgfx.css.definitions.Constants;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class provides functionality related to svg processing Created by Xyanid on 02.11.2015.
 */
public final class SVGUtils {

    // region Fields

    private static final char CLOSING_BRACE = ')';

    // endregion

    // region Constructor

    /**
     * Resolves the given data into a paint. The data must either be valid hex web color (e.g. #00FF00FF)
     * or a reference which can be resolved by the given data provider.
     *
     * @param data     data to be resolved
     * @param provider the {@link SVGDataProvider} to be used
     *
     * @return {@link Paint} which represents the color
     *
     * @throws IllegalArgumentException if the the data is empty, the provider is null.
     */
    public static Paint parseColor(final String data, final SVGDataProvider provider) {

        if (provider == null) {
            throw new IllegalArgumentException("given dataprovider must not be null or empty");
        }

        if (StringUtils.isNullOrEmpty(data)) {
            throw new IllegalArgumentException("given data must not be null or empty");
        }

        int index = data.indexOf(Constants.PROPERTY_VALUE_REFERENCE_URL);

        Paint result = null;

        if (index > -1) {

            String reference = data.substring(Constants.PROPERTY_VALUE_REFERENCE_URL.length(), data.length() - 1);

            // check if its a linear gradient color or linear gradient
            LinearGradient linearGradientColor = provider.getData(LinearGradient.class, reference);
            if (linearGradientColor != null) {
                result = linearGradientColor.getResult();
                // check if its a radial gradient
            } else {
                RadialGradient radialGradient = provider.getData(RadialGradient.class, reference);
                if (radialGradient != null) {
                    result = radialGradient.getResult();
                }
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

    // endregion

    // region Methods related to Styling

    /**
     * Tries to get the {@link Enumerations.CssStyleProperty} from the given {@link CssStyle}. The declaration is
     * first converted and then consumed.
     *
     * @param style       style which will provide tha data
     * @param declaration declaration to be used
     * @param consumer    consumer which will use that converted data
     * @param converter   converter which will parse the provided data into the desired type
     * @param <TData>     type of the data
     *
     * @return true if the declaration is present in the style and was used
     */
    public static <TData> boolean applyStyleDeclaration(final CssStyle style,
                                                        final Enumerations.CssStyleProperty declaration,
                                                        final Consumer<TData> consumer,
                                                        final Function<String, TData> converter) {
        return applyStyleDeclaration(style, declaration, consumer, converter, null);
    }

    /**
     * Tries to get the {@link Enumerations.CssStyleProperty} from the given {@link CssStyle}. The declaration is
     * first converted and then if need be validated and then consumed.
     *
     * @param style       style which will provide tha data
     * @param declaration declaration to be used
     * @param consumer    consumer which will use that converted data
     * @param converter   converter which will parse the provided data into the desired type
     * @param validator   validator to use, may be null if not needed in which case no validation is performed
     * @param <TData>     type of the data
     *
     * @return true if the declaration is present in the style
     */
    public static <TData> boolean applyStyleDeclaration(final CssStyle style,
                                                        final Enumerations.CssStyleProperty declaration,
                                                        final Consumer<TData> consumer, final Function<String, TData> converter, final Function<String, Boolean> validator) {

        String value = style.getStyle().getPropertyValue(declaration.getName());

        if (StringUtils.isNullOrEmpty(value)) {
            return false;
        }

        if (validator != null && !validator.apply(value)) {
            return false;
        }

        ConvertUtils.applyData(value, consumer, converter);

        return true;
    }

    /**
     * Applies the basic style every {@link Shape} supports to the given shape.
     *
     * @param shape        {@link Shape} to which the the styles should be applied, must not be null
     * @param style        {@link CssStyle} to use, must not be null
     * @param dataProvider the {@link SVGDataProvider} to be used
     * @param <TShape>     type of the shape to be used
     *
     * @throws IllegalArgumentException if either shape or style is null
     */
    public static <TShape extends Shape> void applyStyle(final TShape shape, final CssStyle style, final SVGDataProvider dataProvider) throws IllegalArgumentException {

        if (dataProvider == null) {
            throw new IllegalArgumentException("given dataProvider must not be null");
        }

        if (shape == null) {
            throw new IllegalArgumentException("given shape must not be null");
        }

        if (style == null) {
            throw new IllegalArgumentException("given style must not be null");
        }

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.FILL, shape::setFill, (data) -> parseColor(data, dataProvider));

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.STROKE, shape::setStroke, (data) -> parseColor(data, dataProvider));

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.STROKE_TYPE, shape::setStrokeType, (data) -> StrokeType.valueOf(data.toUpperCase()));

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.STROKE_WIDTH, shape::setStrokeWidth, Double::parseDouble);

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.STROKE_DASHOFFSET, shape::setStrokeDashOffset, Double::parseDouble);

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.STROKE_LINECAP, shape::setStrokeLineCap, (data) -> StrokeLineCap.valueOf(data.toUpperCase()));

        applyStyleDeclaration(style, Enumerations.CssStyleProperty.STROKE_MITERLIMIT, shape::setStrokeMiterLimit, Double::parseDouble);
    }

    /**
     * Returns the combined transformations available in the given string. The string must be contain data
     * corresponding to the Svg specification for the transform attribute.
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

        String[] values = actualData.split(",");

        switch (matrix) {
            // a matrix will create an affine matrix and has 6 values
            case MATRIX:
                if (values.length != 6) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 6 values but got %d", matrix.getName(), values.length));
                }

                try {
                    // this is in corresponds with the svg spec, so that is why the number are not in order
                    result = new Affine(Double.parseDouble(values[0].trim()),
                                        Double.parseDouble(values[2].trim()),
                                        Double.parseDouble(values[4].trim()),
                                        Double.parseDouble(values[1].trim()),
                                        Double.parseDouble(values[3].trim()),
                                        Double.parseDouble(values[5].trim()));
                } catch (NumberFormatException e) {
                    throw new SVGException(e);
                }
                break;

            // a translate/scale will create a translate/scale matrix and has either 1 or 2 values
            case TRANSLATE:
            case SCALE:
                if (values.length != 1 && values.length != 2) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 1 or 2 values but got %d", matrix.getName(), values.length));
                }

                try {
                    // if only one value is present the the second one is assume to be like the first
                    double x = Double.parseDouble(values[0].trim());
                    double y = values.length == 2 ? Double.parseDouble(values[1].trim()) : x;

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
                if (values.length != 1 && values.length != 3) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 1 or 3 values but got %d", matrix.getName(), values.length));
                }

                // if more then one value is present then the rotation also contains a translation
                try {
                    double x = values.length == 3 ? Double.parseDouble(values[1].trim()) : 0.0d;
                    double y = values.length == 3 ? Double.parseDouble(values[2].trim()) : 0.0d;

                    result = new Rotate(Double.parseDouble(values[0].trim()), x, y);
                } catch (NumberFormatException e) {
                    throw new SVGException(e);
                }
                break;

            // default would be a skewY/X which will create a shear matrix and have only one value
            // we do not actually need the default but otherwise the compiler will complain that result is not initialized
            case SKEW_X:
            case SKEW_Y:
            default:
                if (values.length != 1) {
                    throw new SVGException(String.format("Given number of values does not match for matrix %s. Expected 1 value but got %d", matrix.getName(), values.length));
                }

                double shearing;

                try {
                    shearing = Double.parseDouble(values[0].trim());
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

    private SVGUtils() {

    }

    // endregion
}
