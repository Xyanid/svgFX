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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGContentTypeDouble;
import de.saxsys.svgfx.core.content.SVGContentTypeLength;
import de.saxsys.svgfx.core.content.SVGContentTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
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
     * This stop used both the {@link PresentationAttributeMapper#STOP_COLOR} and {@link PresentationAttributeMapper#COLOR}, however
     * the {@link PresentationAttributeMapper#STOP_COLOR} is preferred if both are present. Furthermore if an
     * {@link PresentationAttributeMapper#STOP_OPACITY} is present, then it will overwrite the opacity of the original color.
     */
    @Override
    protected final Stop createResult(final SVGCssStyle style) throws SVGException {

        Color color = (Color) SVGContentTypePaint.DEFAULT_VALUE;

        if (!hasContentType(CoreAttributeMapper.OFFSET.getName())) {
            throw new SVGException("Stop does not provide an offset value");
        }

        if (style.hasContentType(PresentationAttributeMapper.STOP_COLOR.getName())) {
            color = (Color) style.getContentType(PresentationAttributeMapper.STOP_COLOR.getName(), SVGContentTypePaint.class).getValue();
        } else if (style.hasContentType(PresentationAttributeMapper.COLOR.getName())) {
            color = (Color) style.getContentType(PresentationAttributeMapper.COLOR.getName(), SVGContentTypePaint.class).getValue();
        }

        if (color == null) {
            throw new SVGException("Given color must not be null");
        }

        if (style.hasContentType(PresentationAttributeMapper.STOP_OPACITY.getName())) {
            double opacity = style.getContentType(PresentationAttributeMapper.STOP_OPACITY.getName(), SVGContentTypeDouble.class).getValue();
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        }

        return new Stop(getContentType(CoreAttributeMapper.OFFSET.getName(), SVGContentTypeLength.class).getValue(), color);
    }

    @Override
    protected final void initializeResult(final javafx.scene.paint.Stop stop, final SVGCssStyle inheritanceResolver) throws SVGException {
    }

    //endregion
}
