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

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.css.StyleSupplier;
import de.saxsys.svgfx.css.definitions.Constants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents the style element from svg
 *
 * @author Xyanid on 27.10.2015.
 */
public class SVGStyle extends SVGElementBase<Set<SVGCssStyle>> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "style";

    // endregion

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
    SVGStyle(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override
    public boolean rememberElement() {
        return true;
    }

    @Override
    public void startProcessing() throws SAXException {}

    /**
     * {@inheritDoc}
     * Saves all characters in a StringBuilder to use them later
     */
    @Override
    public void processCharacterData(final char[] ch, final int start, final int length) throws SAXException {
        for (int i = start; i < length; i++) {
            characters.append(ch[i]);
        }
    }

    @Override
    public void endProcessing() throws SAXException {
        getDocumentDataProvider().addStyles(getResult());
    }

    /**
     * {@inheritDoc}
     *
     * @return true if the element not not inside a {@link SVGClipPath} or {@link SVGGroup}, otherwise false.
     */
    @Override
    public boolean canConsumeResult() {
        return !((this.getParent() instanceof SVGClipPath) || (this.getParent() instanceof SVGGroup));
    }

    /**
     * {@inheritDoc}
     * This implementation does not use the given data
     */
    @Override
    protected final Set<SVGCssStyle> createResult(final StyleSupplier styleSupplier) throws SVGException {

        final Set<SVGCssStyle> result = new HashSet<>();

        final Optional<SVGAttributeTypeString> type = getAttributeHolder().getAttribute(CoreAttributeMapper.TYPE.getName(), SVGAttributeTypeString.class);
        if (!type.isPresent() || type.get().getValue().equals(CSS_TYPE)) {

            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < characters.length(); i++) {

                char character = characters.charAt(i);

                builder.append(character);

                if (character == Constants.DECLARATION_BLOCK_END) {

                    SVGCssStyle styleDef = new SVGCssStyle(getDocumentDataProvider());

                    styleDef.parseCssText(builder.toString());

                    result.add(styleDef);

                    builder.setLength(0);
                }
            }
        }

        return result;
    }

    @Override
    protected void initializeResult(final Set<SVGCssStyle> cssStyles, final StyleSupplier styleSupplier) throws SVGException {

    }

    //endregion
}