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

package de.saxsys.svgfx.core.elements.mocks;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.core.elements.SVGPolyBase;
import javafx.scene.shape.Polygon;
import org.xml.sax.Attributes;

/**
 * This class is purely used to test the functionality of {@link SVGPolyBase}
 *
 * @author Xyanid on 22.12.2015.
 */
public class SVGPolyBaseMock extends SVGPolyBase<Polygon> {

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGPolyBaseMock(String name, Attributes attributes, SVGElementBase<SVGDataProvider> parent, SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    @Override
    protected Polygon createResult(final SVGCssStyle style) throws SVGException {
        return null;
    }
}
