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
import de.saxsys.svgfx.core.elements.SVGElementBase;

/**
 * This class provides functionality related to svg processing
 *
 * @author by Xyanid on 02.11.2015.
 */
public final class SVGUtil {

    // region Constructor

    private SVGUtil() {}

    // endregion

    // region Methods

    /**
     * Strips the given {@link String} from the IRI identifiers if any.
     *
     * @param data the {@link String} to be stripped, must not be null or empty.
     *
     * @return the data striped of its IRI identifiers or null the string does not contain any IRI identifiers.
     */
    public static String stripIRIIdentifiers(final String data) {

        if (StringUtil.isNullOrEmpty(data)) {
            throw new IllegalArgumentException("given data must not be null or empty");
        }

        // initially we assume that the IRI_IDENTIFIER was used
        int dataLengthReduction = 1;
        int identifierLength = de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER.length();
        int index = -1;
        if (data.length() > de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER.length() &&
            data.startsWith(de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER)) {
            index = de.saxsys.svgfx.core.definitions.Constants.IRI_IDENTIFIER.length();
        }

        if (index == -1) {
            dataLengthReduction = 0;
            identifierLength = de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER.length();
            if (data.length() > de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER.length() &&
                data.startsWith(de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER)) {
                index = de.saxsys.svgfx.core.definitions.Constants.IRI_FRAGMENT_IDENTIFIER.length();
            }
        }

        if (index > -1) {
            return data.substring(identifierLength, data.length() - dataLengthReduction);
        }

        return null;
    }

    /**
     * Returns the element which might be referenced by the given data. The data will need to start with the
     * {@link de.saxsys.svgfx.core.definitions.Constants#IRI_IDENTIFIER} in order to be
     * resolved as a reference.
     *
     * @param data              the string which contains the reference to resolve.
     * @param dataProvider      the {@link SVGDocumentDataProvider} which contains the data which is referenced.
     * @param clazz             the class of the element that is expected.
     * @param <TSVGElementBase> the type of element which is expected.
     *
     * @return the {@link SVGElementBase} which is referenced by the data.
     *
     * @throws SVGException if the data references a resource which is not contained in the {@link SVGDocumentDataProvider}.
     */
    public static <TSVGElementBase extends SVGElementBase<?>> TSVGElementBase resolveIRI(final String data,
                                                                                         final SVGDocumentDataProvider dataProvider,
                                                                                         final Class<TSVGElementBase> clazz)
            throws SVGException {

        final String reference = stripIRIIdentifiers(data);
        if (StringUtil.isNullOrEmpty(reference)) {
            throw new IllegalArgumentException(String.format("Given data [%s] appears to not be a IRI reference.", data));
        }

        return dataProvider.getData(reference, clazz)
                           .orElseThrow(() -> new SVGException(String.format("Given reference [%s] could not be resolved", data)));
    }

    // endregion
}