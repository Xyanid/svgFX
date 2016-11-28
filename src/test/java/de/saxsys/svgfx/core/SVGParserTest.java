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

package de.saxsys.svgfx.core;

import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("ConstantConditions")
public final class SVGParserTest {

    /**
     * Parses a complex SVG file successfully.
     */
    @Test
    public void parseComplexSVGAndDoNotThrowAnException() {

        SVGParser parser;

        parser = new SVGParser();

        assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/complex.svg");

        assertNotNull(url);

        try {
            parser.parse(url.getFile(), false);
        } catch (final Exception e) {
            Assert.fail();
        }

        assertNotNull(parser.getResult());
    }

    /**
     * When an element that is that is needed for another element is below the element that needs it in the tree. The element will still be able to use this element.
     */
    @Test
    public void parsingASVGFileThatHasADefsElementThatIsNotTheFirstElementInTheTreeWillStillAllowOtherElementsToUseTheReferencedElement() {

        SVGParser parser;

        parser = new SVGParser();

        assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/defsatlastposition.svg");

        assertNotNull(url);

        try {
            parser.parse(url.getFile(), false);
        } catch (final Exception e) {
            Assert.fail();
        }

        assertNotNull(parser.getResult());
        assertThat(parser.getResult().getChildren().get(0), instanceOf(Rectangle.class));

        final Rectangle rectangle = Rectangle.class.cast(parser.getResult().getChildren().get(0));

        assertThat(rectangle.getFill(), instanceOf(LinearGradient.class));
    }

    /**
     * Parsing a file that has the DOCTYPE defined will not try to
     */
    @Test
    public void parsingASVGFileThatHasADocumentTypeForSVG11WillNotUseValidationIfDisabled() {

        SVGParser parser;

        parser = new SVGParser();

        assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/documenttype.svg");

        assertNotNull(url);

        try {
            parser.parse(url.getFile(), false);
        } catch (final Exception e) {
            Assert.fail();
        }

        assertNotNull(parser.getResult());
    }
}