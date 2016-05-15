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

package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.content.SVGAttributeType;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

/**
 * This class determines which svg xlink attributes are mapped to the desired {@link SVGAttributeType}.
 *
 * @author Xyanid on 09.03.2016.
 */
public class XLinkAttributeMapper extends BaseAttributeMapper<SVGDataProvider> {

    // region Constants

    /**
     * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
     */
    public static final XLinkAttributeMapper XLINK_HREF = new XLinkAttributeMapper("xlink:href", SVGAttributeTypeString::new);

    /**
     * Contains all the values that are available for this attribute class.
     */
    public static final ArrayList<XLinkAttributeMapper> VALUES = new ArrayList<>(Collections.singletonList(XLINK_HREF));

    // endregion

    //region Constructor

    /**
     * {@inheritDoc}
     */
    private XLinkAttributeMapper(final String name, final Function<SVGDataProvider, ? extends SVGAttributeType> contentTypeCreator) {
        super(name, contentTypeCreator);
    }

    //endregion
}