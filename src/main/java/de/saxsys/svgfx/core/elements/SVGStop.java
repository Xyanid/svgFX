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
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.css.SVGContentTypeDouble;
import de.saxsys.svgfx.core.css.SVGContentTypePaint;
import de.saxsys.svgfx.core.css.SVGContentTypeLength;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.Enumerations;
import de.saxsys.svgfx.core.utils.StringUtils;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

/**
 * This class represents a stop element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("stop")
public class SVGStop extends SVGElementBase<Stop> {

    //region SVGStop

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGStop(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    /**
     * {@inheritDoc}.
     * This stop used both the {@link Enumerations.PresentationAttribute#STOP_COLOR} and {@link Enumerations.PresentationAttribute#COLOR}, however
     * the {@link Enumerations.PresentationAttribute#STOP_COLOR} is preferred if both are present. Furthermore if an
     * {@link Enumerations.PresentationAttribute#STOP_OPACITY} is present, then it will overwrite the opacity of the original color.
     */
    @Override
    protected final Stop createResult(final SVGCssStyle style) throws SVGException {

        SVGContentTypeLength offset = new SVGContentTypeLength(getDataProvider());

        Color color = (Color) SVGContentTypePaint.DEFAULT_VALUE;

        if (StringUtils.isNullOrEmpty(getAttribute(Enumerations.CoreAttribute.OFFSET.getName()))) {
            throw new SVGException("Stop does not provide an offset value");
        }

        offset.parseCssText(getAttribute(Enumerations.CoreAttribute.OFFSET.getName()));

        if (style.hasCssContentType(Enumerations.PresentationAttribute.STOP_COLOR.getName())) {
            color = (Color) style.getCssContentType(Enumerations.PresentationAttribute.STOP_COLOR.getName(), SVGContentTypePaint.class).getValue();
        } else if (style.hasCssContentType(Enumerations.PresentationAttribute.COLOR.getName())) {
            color = (Color) style.getCssContentType(Enumerations.PresentationAttribute.COLOR.getName(), SVGContentTypePaint.class).getValue();
        }

        if (color == null) {
            throw new SVGException("Given color must not be null");
        }

        if (style.hasCssContentType(Enumerations.PresentationAttribute.STOP_OPACITY.getName())) {
            double opacity = style.getCssContentType(Enumerations.PresentationAttribute.STOP_OPACITY.getName(), SVGContentTypeDouble.class).getValue();
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        }

        return new Stop(offset.getValue(), color);
    }

    @Override
    protected final void initializeResult(final javafx.scene.paint.Stop stop, final SVGCssStyle inheritanceResolver) throws SVGException {
    }

    //endregion
}
