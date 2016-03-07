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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * This test will ensure that svg stop elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGStopTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getQName(1)).thenReturn(Enumerations.PresentationAttribute.COLOR.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("red");
        Mockito.when(attributes.getQName(2)).thenReturn(Enumerations.PresentationAttribute.STOP_OPACITY.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("0.5");

        SVGStop stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        Assert.assertEquals(0.1d, stop.getResult().getOffset(), 0.01d);
        Assert.assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5d), stop.getResult().getColor());
        Assert.assertEquals(0.5d, stop.getResult().getColor().getOpacity(), 0.01d);
    }

    /**
     * Ensures that the {@link Enumerations.PresentationAttribute#STOP_COLOR} is preferred.
     */
    @Test
    public void ensureThatStopColorIsPreferred() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("0.1");
        Mockito.when(attributes.getQName(1)).thenReturn(Enumerations.PresentationAttribute.COLOR.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("red");
        Mockito.when(attributes.getQName(2)).thenReturn(Enumerations.PresentationAttribute.STOP_COLOR.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("blue");

        SVGStop stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        Assert.assertEquals(0.1d, stop.getResult().getOffset(), 0.01d);
        Assert.assertEquals(Color.BLUE, stop.getResult().getColor());
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(6);

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.CoreAttribute.OFFSET.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(Enumerations.PresentationAttribute.STOP_OPACITY.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(Enumerations.PresentationAttribute.STOP_COLOR.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("A");
        Mockito.when(attributes.getValue(1)).thenReturn("1.0");
        Mockito.when(attributes.getValue(2)).thenReturn("#000000");

        SVGStop stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        try {
            stop.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGStop.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("1.0");
        Mockito.when(attributes.getValue(1)).thenReturn("A");
        Mockito.when(attributes.getValue(2)).thenReturn("#000000");

        stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        try {
            stop.getResult();
            Assert.fail();
        } catch (SVGException ignored) {
        }

        Mockito.when(attributes.getValue(0)).thenReturn("1.0");
        Mockito.when(attributes.getValue(1)).thenReturn("1.0");
        Mockito.when(attributes.getValue(2)).thenReturn("asdsagfa");

        stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        try {
            stop.getResult();
            Assert.fail();
        } catch (SVGException ignored) {
        }

        Mockito.when(attributes.getQName(2)).thenReturn(Enumerations.PresentationAttribute.COLOR.getName());

        stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        try {
            stop.getResult();
            Assert.fail();
        } catch (SVGException e) {
        }
    }

    /**
     * Ensures that a {@link SVGException} is thrown if one of the required attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");

        Mockito.when(attributes.getQName(0)).thenReturn(Enumerations.PresentationAttribute.STOP_COLOR.getName());

        SVGStop stop = new SVGStop("stop", attributes, null, new SVGDataProvider());

        try {
            stop.getResult();
            Assert.fail();
        } catch (SVGException ignored) {
        }
    }
}
