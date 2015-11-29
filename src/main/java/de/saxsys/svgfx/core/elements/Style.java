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
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.css.core.CssStyle;
import de.saxsys.svgfx.css.definitions.Constants;
import org.xml.sax.Attributes;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the style element from svg
 * Created by Xyanid on 27.10.2015.
 */
@SVGElementMapping("style") public class Style extends SVGElementBase<Set<CssStyle>> {

    //region Static
    /**
     * Determines the string which indicates that the type is css.
     */
    public static final String CSS_TYPE = "text/css";

    //endregion

    //region Fields

    /**
     * Contains the characters read by this element.
     */
    private final StringBuilder characters = new StringBuilder();

    //endregion

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Style(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    /**
     * @inheritDoc
     *
     * This implementation does not use the given data
     */
    @Override protected final Set<CssStyle> createResultInternal() {

        Set<CssStyle> result = new HashSet<>();

        if (getAttribute(CoreAttribute.TYPE.getName()) == null || getAttribute(CoreAttribute.TYPE.getName()).equals(CSS_TYPE)) {

            StringBuilder builder = new StringBuilder();

            long lastDeclarationEnd = -1;

            long counter = 0;

            for (int i = 0; i < characters.length(); i++) {

                char character = characters.charAt(i);

                builder.append(character);

                if (character == Constants.DECLARATION_BLOCK_END) {

                    if (lastDeclarationEnd > -1 && lastDeclarationEnd < counter) {

                        CssStyle style = new CssStyle();

                        style.setCssText(builder.toString());

                        result.add(style);

                        lastDeclarationEnd = -1;

                        builder.setLength(0);
                    }

                } else if (character == Constants.PROPERTY_END) {
                    lastDeclarationEnd = counter;
                }

                counter++;
            }
        }

        return result;
    }

    @Override protected void initializeResult(Set<CssStyle> cssStyles) throws SVGException {

    }

    /**
     * @inheritDoc
     * Saves all characters in a StringBuilder to use them later
     */
    @Override public void processCharacterData(final char[] ch, final int start, final int length) {

        for (int i = start; i < length; i++) {
            characters.append(ch[i]);
        }
    }

    //endregion
}
