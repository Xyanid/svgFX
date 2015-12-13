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

import de.saxsys.svgfx.core.css.SVGCssContentTypeLength;
import de.saxsys.svgfx.core.css.SVGCssContentTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.xml.sax.Attributes;

/**
 * This class represents a stop element from svg
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("stop") public class Stop extends SVGElementBase<javafx.scene.paint.Stop> {

    //region Stop

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Stop(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override protected final void initializeResult(javafx.scene.paint.Stop stop) throws SVGException {

    }

    @Override protected final javafx.scene.paint.Stop createResultInternal() throws SVGException {
        double offset = Double.parseDouble(getAttribute(CoreAttribute.OFFSET.getName()));

        SVGCssStyle style = getCssStyle();

        //TODO use opacity here
        double opacity = 0.0d;

        if (style.hasCssContentType(SVGCssStyle.PresentationAttribute.STOP_OPACITY.getName())) {
            opacity = style.getCssContentType(SVGCssStyle.PresentationAttribute.STOP_OPACITY.getName(), SVGCssContentTypeLength.class).getValue();
        }

        Paint paint = style.getCssContentType(SVGCssStyle.PresentationAttribute.STOP_COLOR.getName(), SVGCssContentTypePaint.class).getValue();

        if (paint == null) {
            throw new IllegalArgumentException("given color must not be null");
        }

        return new javafx.scene.paint.Stop(offset, (Color) paint);
    }

    //endregion
}
