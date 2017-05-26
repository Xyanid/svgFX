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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import org.junit.Test;
import org.xml.sax.Attributes;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test to ensure the {@link SVGUtil} work as expected.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ({"unchecked", "OptionalGetWithoutIsPresent"})
public final class SVGUtilTest {

    //region Tests

    /**
     * Ensures that {@link SVGUtil#stripIRIIdentifiers(String)} is able to resolve the url as expected.
     */
    @Test (expected = IllegalArgumentException.class)
    public void ensureStripIRIExpectedExceptions() {
        SVGUtil.stripIRIIdentifiers(null);
    }

    /**
     * Ensures that {@link SVGUtil#stripIRIIdentifiers(String)} is able to strip a string from its iri identifiers.
     */
    @Test
    public void ensureSplitIRIWillStripStringFromIRIIdentifiers() throws SVGException {
        assertEquals("test", SVGUtil.stripIRIIdentifiers(Constants.IRI_IDENTIFIER + "test)"));

        assertEquals("test", SVGUtil.stripIRIIdentifiers(Constants.IRI_FRAGMENT_IDENTIFIER + "test"));
    }

    /**
     * Ensures that {@link SVGUtil#stripIRIIdentifiers(String)} is able to strip a string from its iri identifiers.
     */
    @Test
    public void ensureSplitIRIWillReturnNullIfIRIIdentifiersCanNotBeStriped() throws SVGException {
        assertNull(SVGUtil.stripIRIIdentifiers("test)"));

        assertNull(SVGUtil.stripIRIIdentifiers("(#test"));

        assertNull(SVGUtil.stripIRIIdentifiers("url(#"));

        assertNull(SVGUtil.stripIRIIdentifiers("#"));
    }

    /**
     * Ensures that {@link SVGUtil#resolveIRI(String, SVGDocumentDataProvider, Class)} is able to resolve the url as expected.
     */
    @Test
    public void ensureResolveIRICauseTheExpectedExceptions() {

        try {
            SVGUtil.resolveIRI(null, new SVGDocumentDataProvider(), SVGElementBase.class);
            fail();
        } catch (final IllegalArgumentException | SVGException e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }

        try {
            SVGUtil.resolveIRI("", new SVGDocumentDataProvider(), SVGElementBase.class);
            fail();
        } catch (final IllegalArgumentException | SVGException e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER + "test)", new SVGDocumentDataProvider(), null);
            fail();
        } catch (final IllegalArgumentException | SVGException e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER + "test)", new SVGDocumentDataProvider(), SVGElementBase.class);
            fail();
        } catch (final SVGException ignored) {
        }

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER, new SVGDocumentDataProvider(), SVGCircle.class);
            fail();
        } catch (final IllegalArgumentException | SVGException ignored) {
        }
    }


    /**
     * Ensures that {@link SVGUtil#resolveIRI(String, SVGDocumentDataProvider, Class)} is able to resolve the url as expected.
     */
    @Test
    public void ensureResolveIRICanResolveReference() throws SVGException {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        dataProvider.storeData("test", mock(SVGCircle.class));

        final SVGCircle circle = SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER + "test)", dataProvider, SVGCircle.class);

        assertNotNull(circle);

        final SVGCircle circle1 = SVGUtil.resolveIRI(Constants.IRI_FRAGMENT_IDENTIFIER + "test", dataProvider, SVGCircle.class);

        assertNotNull(circle1);
    }

    /**
     * Ensures that {@link SVGUtil#resolveIRI(String, SVGDocumentDataProvider, Class)} is able to resolve the url as expected.
     */
    @Test
    public void ensureResolveIRICanNotResolveReference() {

        final Attributes attributes = mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER + "test1)", new SVGDocumentDataProvider(), SVGCircle.class);
            fail();
        } catch (final SVGException e) {
            assertTrue(e.getMessage().contains(Constants.IRI_IDENTIFIER + "test1)"));
        }
    }

    //endregion
}