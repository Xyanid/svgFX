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
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Optional;

/**
 * This class represents a stop element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGStop extends SVGElementBase<Stop> {

    // region Constants

    /**
     * Contains the name of this element in an svg file, used to identify the element when parsing.
     */
    public static final String ELEMENT_NAME = "stop";

    // endregion

    //region SVGStop

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param dataProvider dataprovider to be used
     */
    SVGStop(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {
        super(name, attributes, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override
    public boolean keepElement() {
        return true;
    }

    @Override
    public void processCharacterData(char[] ch, int start, int length) throws SAXException {}

    /**
     * {@inheritDoc}
     * This stop used both the {@link PresentationAttributeMapper#STOP_COLOR} and {@link PresentationAttributeMapper#COLOR}, however
     * the {@link PresentationAttributeMapper#STOP_COLOR} is preferred if both are present. Furthermore if an
     * {@link PresentationAttributeMapper#STOP_OPACITY} is present, then it will overwrite the opacity of the original color.
     */
    @Override
    protected final Stop createResult(final SVGCssStyle ownStyle, final Transform ownTransform) throws SVGException {

        Color color = ownStyle.getAttributeHolder().getAttributeValue(PresentationAttributeMapper.STOP_COLOR.getName(), Color.class, null);
        if (color == null) {
            color = ownStyle.getAttributeHolder().getAttributeValue(PresentationAttributeMapper.COLOR.getName(), Color.class, (Color) SVGAttributeTypePaint.DEFAULT_VALUE);
        }

        if (color == null) {
            throw new SVGException(String.format("[%s] can not be created without a color", SVGStop.class.getSimpleName()));
        }

        final Optional<SVGAttributeTypeDouble> stopOpacity = ownStyle.getAttributeHolder().getAttribute(PresentationAttributeMapper.STOP_OPACITY.getName(), SVGAttributeTypeDouble.class);
        if (stopOpacity.isPresent()) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), stopOpacity.get().getValue());
        }

        final SVGAttributeTypeLength offset = getAttributeHolder().getAttributeOrFail(CoreAttributeMapper.OFFSET.getName(), SVGAttributeTypeLength.class);
        if (offset.hasUnit(SVGAttributeTypeLength.Unit.PERCENT)) {
            return new Stop(offset.getValue() / 100.0d, color);
        } else {
            return new Stop(offset.getValue(), color);
        }
    }

    @Override
    protected final void initializeResult(final javafx.scene.paint.Stop stop,
                                          final SVGCssStyle ownStyle,
                                          final Transform ownTransform) throws SVGException {
    }

    //endregion
}
