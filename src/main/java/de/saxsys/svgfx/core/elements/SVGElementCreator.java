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
import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.xml.core.IElementCreator;
import de.saxsys.svgfx.xml.elements.ElementBase;
import org.xml.sax.Attributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates all the needed svg elements, this class will automatically load all the constructors of classes which
 * reside in the de.saxsys.svg.elements package, are derived from {@link SVGElementBase} and have the {@link SVGElementMapping} applied
 * @author Xyanid on 25.10.2015.
 */
public class SVGElementCreator implements IElementCreator<SVGDataProvider> {

    //region Fields

    /**
     * Contains the constructor of all the svg elements that are known.
     */
    private final Map<String, Constructor<? extends SVGElementBase<?>>> knownClasses = new HashMap<>();

    //endregion

    //region Constructor

    /**
     * Creates a new instance.
     */
    public SVGElementCreator() {

        for (Class<? extends SVGElementBase<?>> clazz : Constants.SVG_ELEMENT_CLASSES) {
            SVGElementMapping annotation = clazz.getAnnotation(SVGElementMapping.class);
            if (annotation != null) {
                try {
                    knownClasses.put(annotation.value(), clazz.getConstructor(String.class, Attributes.class, SVGElementBase.class, SVGDataProvider.class));
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(String.format("element %s does not have a matching constructor", annotation.value()), e);
                }
            }
        }
    }

    //endregion

    //region Public

    /**
     * Returns the {@link SVGElementCreator#knownClasses}.
     *
     * @return the {@link SVGElementCreator#knownClasses}
     */
    public Map<String, Constructor<? extends SVGElementBase<?>>> getKnownClasses() {
        return knownClasses;
    }

    //endregion

    //region Implements IElementCreator

    @Override public SVGElementBase<?> createElement(final String name, final Attributes attributes, final ElementBase parent, final SVGDataProvider dataProvider) {

        Constructor constructor = knownClasses.get(name);

        if (constructor != null) {
            try {
                return (SVGElementBase) constructor.newInstance(name, attributes, parent, dataProvider);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //endregion
}
