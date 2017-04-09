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
import de.saxsys.svgfx.xml.core.IElementFactory;
import org.xml.sax.Attributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates all the needed svg elements.
 *
 * @author Xyanid on 25.10.2015.
 */
public class SVGElementFactory implements IElementFactory<SVGDocumentDataProvider, SVGElementBase<?>> {

    //region Fields

    /**
     * Contains the constructor of all the svg elements that are known.
     */
    public static final Map<String, Constructor<? extends SVGElementBase<?>>> KNOWN_CLASSES = new HashMap<>();

    static {
        try {
            KNOWN_CLASSES.put(SVGCircle.ELEMENT_NAME, SVGCircle.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGClipPath.ELEMENT_NAME, SVGClipPath.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGDefinitions.ELEMENT_NAME, SVGDefinitions.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGEllipse.ELEMENT_NAME, SVGEllipse.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGGroup.ELEMENT_NAME, SVGGroup.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGLine.ELEMENT_NAME, SVGLine.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGLinearGradient.ELEMENT_NAME, SVGLinearGradient.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGPath.ELEMENT_NAME, SVGPath.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGPolygon.ELEMENT_NAME, SVGPolygon.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGPolyline.ELEMENT_NAME, SVGPolyline.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGRadialGradient.ELEMENT_NAME, SVGRadialGradient.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGRectangle.ELEMENT_NAME, SVGRectangle.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGRoot.ELEMENT_NAME, SVGRoot.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGStop.ELEMENT_NAME, SVGStop.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGStyle.ELEMENT_NAME, SVGStyle.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
            KNOWN_CLASSES.put(SVGUse.ELEMENT_NAME, SVGUse.class.getDeclaredConstructor(String.class, Attributes.class, SVGDocumentDataProvider.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    //endregion

    //region Implements IElementFactory

    @Override
    public SVGElementBase<?> createElement(final String name, final Attributes attributes, final SVGDocumentDataProvider dataProvider) {

        Constructor constructor = KNOWN_CLASSES.get(name);

        if (constructor != null) {
            try {
                return (SVGElementBase<?>) constructor.newInstance(name, attributes, dataProvider);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //endregion
}
