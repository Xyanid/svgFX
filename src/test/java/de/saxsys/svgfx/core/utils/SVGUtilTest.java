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

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGElementFactory;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    //region Fields

    private final SVGElementFactory factory = new SVGElementFactory();

    //endregion

    //region Tests

    /**
     * Ensures that {@link SVGUtil#stripIRIIdentifiers(String)} is able to resolve the url as expected.
     */
    @Test
    public void ensureStripIRIExpectedExceptions() {

        try {
            SVGUtil.stripIRIIdentifiers(null);
            fail("Should not be able to resolve empty string");
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.NULL_ARGUMENT, e.getReason());
        }
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
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.NULL_ARGUMENT, e.getReason());
        }

        try {
            SVGUtil.resolveIRI("", new SVGDocumentDataProvider(), SVGElementBase.class);
            fail();
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.NULL_ARGUMENT, e.getReason());
        }

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER + "test)", new SVGDocumentDataProvider(), null);
            fail();
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.NULL_ARGUMENT, e.getReason());
        }

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER + "test)", new SVGDocumentDataProvider(), SVGElementBase.class);
            fail();
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.MISSING_ELEMENT, e.getReason());
        }

        try {
            SVGUtil.resolveIRI(Constants.IRI_IDENTIFIER, new SVGDocumentDataProvider(), SVGCircle.class);
            fail();
        } catch (final SVGException e) {
            assertEquals(SVGException.Reason.INVALID_IRI_IDENTIFIER, e.getReason());
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

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", factory.createElement("circle", attributes, null, dataProvider));

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