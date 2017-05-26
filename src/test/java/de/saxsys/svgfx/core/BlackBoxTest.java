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

package de.saxsys.svgfx.core;

import javafx.scene.Group;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.SVGPath;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.net.URL;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;

/**
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("ConstantConditions")
public final class BlackBoxTest {

    // region Description

    @Test
    public void aGradientThatDoesNotHaveATransformAndRelativeCoordinatesAndAPathThatDoesNotHaveATransformAndAGroupThatDoesNotHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-0.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesNotHaveATransformAndAGroupThatDoesNotHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-1.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesNotHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesNotHaveATransformAndAGroupThatDoesNotHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-1-no-matrix.svg");

        assertLinearGradient(gradient);
    }


    @Test
    public void aGradientThatDoesNotHaveATransformAndRelativeCoordinatesAndAPathThatDoesHaveATransformAndAGroupThatDoesNotHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-0.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesHaveATransformAndAGroupThatDoesNotHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-1.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesNotHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesHaveATransformAndAGroupThatDoesNotHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-1-no-matrix.svg");

        assertLinearGradient(gradient);
    }


    @Test
    public void aGradientThatDoesNotHaveATransformAndRelativeCoordinatesAndAPathThatDoesNotHaveATransformAndAGroupThatDoesHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-0.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesNotHaveATransformAndAGroupThatDoesHaveATransformPWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-1.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesNotHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesNotHaveATransformAndAGroupThatDoesHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-1-no-matrix.svg");

        assertLinearGradient(gradient);
    }


    @Test
    public void aGradientThatDoesNotHaveATransformAndRelativeCoordinatesAndAPathThatDoesHaveATransformAndAGroupThatDoesHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-0.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesHaveATransformAndAGroupThatDoesHaveATransformPWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-1.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aGradientThatDoesNotHaveATransformAndAbsoluteCoordinatesAndAPathThatDoesHaveATransformAndAGroupThatDoesHaveATransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-1-no-matrix.svg");

        assertLinearGradient(gradient);
    }

    // endregion

    // region Private

    private LinearGradient getLinearGradient(final String file) throws SAXParseException, IOException {
        final URL url = getClass().getClassLoader().getResource(file);
        final SVGParser parser = new SVGParser();

        parser.parse(url.getFile());

        final Group main = (Group) parser.getResult().getChildren().get(0);
        final SVGPath path = (SVGPath) main.getChildren().get(0);
        return (LinearGradient) path.getStroke();
    }

    private void assertLinearGradient(final LinearGradient gradient) {
        assertEquals(0.0d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    // endregion
}