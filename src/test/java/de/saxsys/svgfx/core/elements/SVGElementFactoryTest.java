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
import de.saxsys.svgfx.core.path.CommandParser;
import de.saxsys.svgfx.xml.core.ElementBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * @author Xyanid on 05.10.2015.
 */
@RunWith (MockitoJUnitRunner.class)
public final class SVGElementFactoryTest {

    /**
     * Ensures that all elements supported by the {@link SVGElementFactory} are created.
     */
    @Test
    public void allSupportedElementsCanBeCreated() {

        final String creationFailMessage = "failed to create element %s";

        final Map<String, Class<? extends SVGElementBase>> classesToCreate = new HashMap<>();
        classesToCreate.put(SVGCircle.ELEMENT_NAME, SVGCircle.class);
        classesToCreate.put(SVGClipPath.ELEMENT_NAME, SVGClipPath.class);
        classesToCreate.put(SVGDefinitions.ELEMENT_NAME, SVGDefinitions.class);
        classesToCreate.put(SVGEllipse.ELEMENT_NAME, SVGEllipse.class);
        classesToCreate.put(SVGGroup.ELEMENT_NAME, SVGGroup.class);
        classesToCreate.put(SVGLine.ELEMENT_NAME, SVGLine.class);
        classesToCreate.put(SVGLinearGradient.ELEMENT_NAME, SVGLinearGradient.class);
        classesToCreate.put(SVGPath.ELEMENT_NAME, SVGPath.class);
        classesToCreate.put(SVGPolygon.ELEMENT_NAME, SVGPolygon.class);
        classesToCreate.put(SVGPolyline.ELEMENT_NAME, SVGPolyline.class);
        classesToCreate.put(SVGRadialGradient.ELEMENT_NAME, SVGRadialGradient.class);
        classesToCreate.put(SVGRectangle.ELEMENT_NAME, SVGRectangle.class);
        classesToCreate.put(SVGRoot.ELEMENT_NAME, SVGRoot.class);
        classesToCreate.put(SVGStop.ELEMENT_NAME, SVGStop.class);
        classesToCreate.put(SVGStyle.ELEMENT_NAME, SVGStyle.class);
        classesToCreate.put(SVGUse.ELEMENT_NAME, SVGUse.class);

        final Attributes attributes = mock(Attributes.class);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementFactory cut = new SVGElementFactory(mock(CommandParser.class));

        for (final Map.Entry<String, Class<? extends SVGElementBase>> entry : classesToCreate.entrySet()) {
            final ElementBase element = cut.createElement(entry.getKey(), attributes, dataProvider);
            assertNotNull(String.format(creationFailMessage, entry.getKey()), element);
            assertEquals(entry.getValue(), element.getClass());
        }
    }
}