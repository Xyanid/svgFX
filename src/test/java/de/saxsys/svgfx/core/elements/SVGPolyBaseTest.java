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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.elements.mocks.SVGPolyBaseMock;
import de.saxsys.svgfx.core.elements.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * This test will ensure the {@link SVGPolyBase} behaves as expected.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGPolyBaseTest {

    /**
     * Ensures Points are parsed correctly
     */
    @Test
    public void ensurePointsAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100,40 100,80");

        SVGPolyBaseMock polyBase = new SVGPolyBaseMock("polygon", attributes, null, new SVGDataProvider());

        Assert.assertEquals(6, polyBase.getPoints().size());
        Assert.assertEquals(60.0d, polyBase.getPoints().get(0), 0.01d);
        Assert.assertEquals(20.0d, polyBase.getPoints().get(1), 0.01d);
        Assert.assertEquals(100.0d, polyBase.getPoints().get(2), 0.01d);
        Assert.assertEquals(40.0d, polyBase.getPoints().get(3), 0.01d);
        Assert.assertEquals(100.0d, polyBase.getPoints().get(4), 0.01d);
        Assert.assertEquals(80.0d, polyBase.getPoints().get(5), 0.01d);
    }

    /**
     * Ensures multiple spaces in a row will cause no problem.
     */
    @Test
    public void ensureMultipleSpacesCaseNotProblems() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20    100,40    100,80");

        SVGPolyBaseMock polyBase = new SVGPolyBaseMock("polygon", attributes, null, new SVGDataProvider());

        Assert.assertEquals(6, polyBase.getPoints().size());
        Assert.assertEquals(60.0d, polyBase.getPoints().get(0), 0.01d);
        Assert.assertEquals(20.0d, polyBase.getPoints().get(1), 0.01d);
        Assert.assertEquals(100.0d, polyBase.getPoints().get(2), 0.01d);
        Assert.assertEquals(40.0d, polyBase.getPoints().get(3), 0.01d);
        Assert.assertEquals(100.0d, polyBase.getPoints().get(4), 0.01d);
        Assert.assertEquals(80.0d, polyBase.getPoints().get(5), 0.01d);
    }

    /**
     * Ensures there are no points if the {@link CoreAttributeMapper#POINTS} is missing.
     */
    @Test
    public void ensurePointsAreEmptyIfAttributeIsMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGPolyBaseMock polyBase = new SVGPolyBaseMock("polygon", attributes, null, new SVGDataProvider());

        Assert.assertEquals(0, polyBase.getPoints().size());
    }

    /**
     * Ensures that points with a missing x or y position will cause an exception.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAPointDoesNotProvideXAndY() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100 100,80");

        TestUtils.assertCreationFails(SVGPolyBaseMock::new,
                                      "polygon",
                                      attributes,
                                      null,
                                      new SVGDataProvider(),
                                      SVGPolyBaseMock.class,
                                      SVGException.class);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100,10 100");

        TestUtils.assertCreationFails(SVGPolyBaseMock::new,
                                      "polygon",
                                      attributes,
                                      null,
                                      new SVGDataProvider(),
                                      SVGPolyBaseMock.class,
                                      SVGException.class);
    }

    /**
     * Ensures that points with a missing x or y position will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void ensureExceptionIsThrownWhenAPointContainsInvalidData() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.POINTS.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("60,20 100,A 100,80");

        SVGPolyBaseMock polyBase = new SVGPolyBaseMock("polygon", attributes, null, new SVGDataProvider());

        polyBase.getPoints();
    }
}
