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

import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.elements.SVGClipPath;
import de.saxsys.svgfx.core.elements.SVGDefs;
import de.saxsys.svgfx.core.elements.SVGElementCreator;
import de.saxsys.svgfx.core.elements.SVGElementMapping;
import de.saxsys.svgfx.core.elements.SVGGroup;
import de.saxsys.svgfx.core.elements.SVGLine;
import de.saxsys.svgfx.core.elements.SVGLinearGradient;
import de.saxsys.svgfx.core.elements.SVGPath;
import de.saxsys.svgfx.core.elements.SVGPolyline;
import de.saxsys.svgfx.core.elements.SVGRadialGradient;
import de.saxsys.svgfx.core.elements.SVGRectangle;
import de.saxsys.svgfx.core.elements.SVGRoot;
import de.saxsys.svgfx.core.elements.SVGStop;
import de.saxsys.svgfx.core.elements.SVGStyle;
import de.saxsys.svgfx.core.elements.SVGUse;
import de.saxsys.svgfx.xml.elements.ElementBase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * @author Xyanid on 05.10.2015.
 */
public final class SVGElementCreatorTest {

    /**
     * Ensures that all elements supported by the {@link SVGElementCreator} are created.
     */
    @Test
    public void createAllSupportedElements() {

        Attributes attributes = Mockito.mock(Attributes.class);

        SVGDataProvider dataProvider = new SVGDataProvider();

        SVGElementCreator creator = null;

        try {
            creator = new SVGElementCreator();
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }

        Assert.assertEquals(Constants.SVG_ELEMENT_CLASSES.size(), creator.getKnownClasses().size());

        ElementBase element = creator.createElement(SVGRoot.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGRoot.class);

        element = creator.createElement(SVGStyle.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGStyle.class);

        element = creator.createElement(SVGStop.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGStop.class);

        element = creator.createElement(SVGRectangle.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGRectangle.class);

        element = creator.createElement(SVGRadialGradient.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGRadialGradient.class);

        element = creator.createElement(SVGPath.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGPath.class);

        element = creator.createElement(SVGPolyline.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGPolyline.class);

        element = creator.createElement(SVGLinearGradient.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGLinearGradient.class);

        element = creator.createElement(SVGLine.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGLine.class);

        element = creator.createElement(SVGDefs.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGDefs.class);

        element = creator.createElement(de.saxsys.svgfx.core.elements.SVGGroup.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGGroup.class);

        element = creator.createElement(SVGUse.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGUse.class);

        element = creator.createElement(SVGClipPath.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), SVGClipPath.class);
    }
}
