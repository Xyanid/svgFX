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
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.net.URL;

import static de.saxsys.svgfx.core.TestUtil.MINIMUM_DEVIATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("ConstantConditions")
public final class BlackBoxTest {

    // region Description

    @Test
    public void anAbsoluteGradientWithNoTransformAPathWithNoTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-0-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithNoTransformAPathWithNoTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-0-relative.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void anAbsoluteGradientWithTransformAPathWithNoTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-1-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithTransformAPathWithNoTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-0-gradient-1-relative.svg");

        assertLinearGradient(gradient);
    }


    @Test
    public void anAbsoluteGradientWithNoTransformAPathWithTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-0-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithNoTransformAPathWithTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-0-relative.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void anAbsoluteGradientWithTransformAPathWithTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-1-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithTransformAPathWithTransformAGroupWithNoTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-0-path-1-gradient-1-relative.svg");

        assertLinearGradient(gradient);
    }


    @Test
    public void anAbsoluteGradientWithNoTransformAPathWithNoTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-0-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithNoTransformAPathWithNoTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-0-relative.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void anAbsoluteGradientWithTransformAPathWithNoTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-1-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithTransformAPathWithNoTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-0-gradient-1-relative.svg");

        assertLinearGradient(gradient);
    }


    @Test
    public void anAbsoluteGradientWithNoTransformAPathWithTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-0-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithNoTransformAPathWithTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-0-relative.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void anAbsoluteGradientWithTransformAPathWithTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-1-absolute.svg");

        assertLinearGradient(gradient);
    }

    @Test
    public void aRelativeGradientWithTransformAPathWithTransformAGroupWithTransformWillCreateTheCorrectGradientCoordinates()
            throws SAXParseException, IOException {
        final LinearGradient gradient = getLinearGradient("de/saxsys/svgfx/core/parent-1-path-1-gradient-1-relative.svg");

        assertLinearGradient(gradient);
    }

    /**
     * Ensures that when parsing the hierarchy is correctly understood, leading to th desired result.
     */
    @Test
    public void theHierarchyOfAnSVGFileIsCorrectlyParsed()
            throws SAXParseException, IOException {

        final Group result = getResult("de/saxsys/svgfx/core/correctHierarchy.svg");

        assertNotNull(result);
        assertEquals(1, result.getChildren().size());

        final Group group = (Group) result.getChildren().get(0);
        assertEquals(3, group.getChildren().size());

        final Rectangle rectangle = (Rectangle) group.getChildren().get(0);
        assertNotNull(rectangle);
        final SVGPath svgPath = (SVGPath) group.getChildren().get(1);
        final Ellipse ellipse = (Ellipse) group.getChildren().get(2);

    }

    // endregion

    // region Private

    private LinearGradient getLinearGradient(final String file) throws SAXParseException, IOException {
        final Group main = (Group) getResult(file).getChildren().get(0);
        final SVGPath path = (SVGPath) main.getChildren().get(0);
        return (LinearGradient) path.getStroke();
    }

    private Group getResult(final String file) throws SAXParseException, IOException {
        final URL url = getClass().getClassLoader().getResource(file);
        final SVGParser parser = new SVGParser();

        parser.parse(url.getFile());

        return parser.getResult();
    }

    private void assertLinearGradient(final LinearGradient gradient) {
        assertEquals(0.0d, gradient.getStartX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getStartY(), MINIMUM_DEVIATION);
        assertEquals(1.0d, gradient.getEndX(), MINIMUM_DEVIATION);
        assertEquals(0.5d, gradient.getEndY(), MINIMUM_DEVIATION);
    }

    // endregion
}