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

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.path.CommandParser;
import de.saxsys.svgfx.xml.core.IElementFactory;
import org.xml.sax.Attributes;

/**
 * Creates all the needed svg elements.
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGElementFactory implements IElementFactory<SVGDocumentDataProvider, SVGElementBase<?>> {

    // region Fields

    private final CommandParser commandParser;

    // endregion

    // region Constructor

    public SVGElementFactory(final CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    // endregion

    // region Implements IElementFactory

    @Override
    public SVGElementBase<?> createElement(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {

        if (SVGCircle.ELEMENT_NAME.equals(name)) {
            return new SVGCircle(name, attributes, dataProvider);
        } else if (SVGClipPath.ELEMENT_NAME.equals(name)) {
            return new SVGClipPath(name, attributes, dataProvider);
        } else if (SVGDefinitions.ELEMENT_NAME.equals(name)) {
            return new SVGDefinitions(name, attributes, dataProvider);
        } else if (SVGEllipse.ELEMENT_NAME.equals(name)) {
            return new SVGEllipse(name, attributes, dataProvider);
        } else if (SVGGroup.ELEMENT_NAME.equals(name)) {
            return new SVGGroup(name, attributes, dataProvider);
        } else if (SVGLine.ELEMENT_NAME.equals(name)) {
            return new SVGLine(name, attributes, dataProvider);
        } else if (SVGLinearGradient.ELEMENT_NAME.equals(name)) {
            return new SVGLinearGradient(name, attributes, dataProvider);
        } else if (SVGPath.ELEMENT_NAME.equals(name)) {
            return new SVGPath(name, attributes, dataProvider, commandParser);
        } else if (SVGPolygon.ELEMENT_NAME.equals(name)) {
            return new SVGPolygon(name, attributes, dataProvider);
        } else if (SVGPolyline.ELEMENT_NAME.equals(name)) {
            return new SVGPolyline(name, attributes, dataProvider);
        } else if (SVGRadialGradient.ELEMENT_NAME.equals(name)) {
            return new SVGRadialGradient(name, attributes, dataProvider);
        } else if (SVGRectangle.ELEMENT_NAME.equals(name)) {
            return new SVGRectangle(name, attributes, dataProvider);
        } else if (SVGRoot.ELEMENT_NAME.equals(name)) {
            return new SVGRoot(name, attributes, dataProvider);
        } else if (SVGStop.ELEMENT_NAME.equals(name)) {
            return new SVGStop(name, attributes, dataProvider);
        } else if (SVGStyle.ELEMENT_NAME.equals(name)) {
            return new SVGStyle(name, attributes, dataProvider);
        } else if (SVGUse.ELEMENT_NAME.equals(name)) {
            return new SVGUse(name, attributes, dataProvider);
        }

        return null;
    }

    // endregion
}
