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
import de.saxsys.svgfx.core.definitions.enumerations.Matrix;
import de.saxsys.svgfx.core.utils.StringUtil;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static de.saxsys.svgfx.core.definitions.Constants.COMMA;
import static de.saxsys.svgfx.core.definitions.Constants.WHITESPACE;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGAttributeTypeTransform extends SVGAttributeType<Transform, Void> {

    // region Static

    /**
     * Determines the character that represents a closing brace for a matrix.
     */
    private static final char CLOSING_BRACE = ')';
    /**
     * Determines the default value for this {@link SVGAttributeType}.
     */
    public static final Transform DEFAULT_VALUE = null;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDocumentDataProvider} to use when data is needed.
     */
    public SVGAttributeTypeTransform(final SVGDocumentDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGAttributeType

    /**
     * @throws SVGException when any value inside the array is not a valid {@link SVGAttributeTypeTransform}
     */
    @Override
    protected Pair<Transform, Void> getValueAndUnit(final String text) throws SVGException {
        return new Pair<>(getTransform(text), null);
    }

    //endregion

    // region Private

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
    private Transform getTransform(final String data) throws SVGException {
        if (StringUtil.isNullOrEmpty(data)) {
            return null;
        }

        Transform result = null;

        EnumSet<Matrix> allMatrices = EnumSet.allOf(Matrix.class);
        allMatrices.remove(Matrix.NONE);

        for (int i = 0; i < data.length(); i++) {

            for (Matrix matrix : allMatrices) {
                if (data.startsWith(matrix.getName(), i)) {

                    // getting the start and end of the new substring, which contains the data in the braces
                    int start = i + matrix.getName().length() + 1;
                    i = data.indexOf(CLOSING_BRACE, start);

                    Transform transform = getTransform(matrix, data.substring(start, i));

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
     * Gets the {@link Transform} that is represented by the given data. The data must meet the following requirements.
     * Data can start with the name of the provide {@link Matrix}, in which case checkIfStartWithMatrix must be true,
     * otherwise an exception will occur when the actual data is processed.
     * Data must contain the values separated with a coma (e.g. 1,2,3). Optionally the values can be embraces with ().
     *
     * @param matrix the matrix to use
     * @param data   the data to be used, must not be null or empty or {@link Matrix#NONE}.
     *
     * @return a new {@link Transform} which contains the transformation represented by the data.
     *
     * @throws IllegalArgumentException if the given data is empty or matrix is {@link Matrix#NONE}.
     * @throws SVGException             if there is an error in the transformation data of the given string.
     */
    private Transform getTransform(final Matrix matrix, final String data) throws SVGException {
        if (StringUtil.isNullOrEmpty(data)) {
            throw new IllegalArgumentException("Given data must not be null or empty");
        }

        if (matrix == Matrix.NONE) {
            throw new IllegalArgumentException("Given matrix must not be NONE");
        }

        Transform result;

        String actualData = data;

        // check if we need to remove the braces at the start and end as well.
        if (actualData.charAt(0) == '(') {
            actualData = actualData.substring(1);
        }
        if (actualData.charAt(actualData.length() - 1) == ')') {
            actualData = actualData.substring(0, actualData.length());
        }

        final List<String> values = StringUtil.splitByDelimiters(actualData,
                                                                 Arrays.asList(WHITESPACE, COMMA),
                                                                 StringUtil::isNotNullOrEmptyAfterTrim);

        switch (matrix) {
            // a matrix will create an affine matrix and has 6 values
            case MATRIX:
                if (values.size() != 6) {
                    throw new SVGException(String.format("Given number of values does not match for matrix [%s] data is [%s]. Expected 6 values but got %d",
                                                         matrix.getName(),
                                                         actualData,
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
                } catch (final NumberFormatException e) {
                    throw new SVGException(String.format("Any value in the matrix [%s] does not represent a valid number, data is [%s]", matrix.getName(), actualData), e);
                }
                break;

            // a translate/scale will create a translate/scale matrix and has either 1 or 2 values
            case TRANSLATE:
            case SCALE:
                if (values.size() != 1 && values.size() != 2) {
                    throw new SVGException(String.format("Given number of values does not match for matrix [%s] data is [%s]. Expected 1 or 2 values but got %d",
                                                         matrix.getName(),
                                                         actualData,
                                                         values.size()));
                }

                try {
                    // if only one value is present the the second one is assume to be like the first
                    double x = Double.parseDouble(values.get(0).trim());
                    double y = values.size() == 2 ? Double.parseDouble(values.get(1).trim()) : x;

                    if (matrix == Matrix.TRANSLATE) {
                        result = new Translate(x, y);
                    } else {
                        result = new Scale(x, y);
                    }
                } catch (final NumberFormatException e) {
                    throw new SVGException(String.format("Any value in the matrix [%s] does not represent a valid number, data is [%s]", matrix.getName(), actualData), e);
                }
                break;

            // a rotate will create a rotate matrix and has either 1 or 3 values
            case ROTATE:
                if (values.size() != 1 && values.size() != 3) {
                    throw new SVGException(String.format("Given number of values does not match for matrix [%s] data is [%s]. Expected 1 or 3 values but got %d",
                                                         matrix.getName(),
                                                         actualData,
                                                         values.size()));
                }

                // if more then one value is present then the rotation also contains a translation
                try {
                    double x = values.size() == 3 ? Double.parseDouble(values.get(1).trim()) : 0.0d;
                    double y = values.size() == 3 ? Double.parseDouble(values.get(2).trim()) : 0.0d;

                    result = new Rotate(Double.parseDouble(values.get(0).trim()), x, y);
                } catch (final NumberFormatException e) {
                    throw new SVGException(String.format("Any value in the matrix [%s] does not represent a valid number, data is [%s]", matrix.getName(), actualData), e);
                }
                break;

            // default would be a skewY/POSITION_X which will create a shear matrix and have only one value
            // we do not actually need the default but otherwise the compiler will complain that result is not initialized
            case SKEW_X:
            case SKEW_Y:
            default:
                if (values.size() != 1) {
                    throw new SVGException(String.format("Given number of values does not match for matrix [%s] data is [%s]. Expected 1 value but got %d",
                                                         matrix.getName(),
                                                         actualData,
                                                         values.size()));
                }

                double shearing;

                try {
                    shearing = Double.parseDouble(values.get(0).trim());
                } catch (final NumberFormatException e) {
                    throw new SVGException(String.format("Any value in the matrix [%s] does not represent a valid number, data is [%s]", matrix.getName(), actualData), e);
                }

                if (matrix == Matrix.SKEW_X) {
                    result = new Shear(shearing, 0.0d);
                } else {
                    result = new Shear(0.0d, shearing);
                }
                break;
        }

        return result;
    }

    // endregion
}