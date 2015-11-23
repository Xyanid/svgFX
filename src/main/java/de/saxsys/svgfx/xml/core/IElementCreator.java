package de.saxsys.svgfx.xml.core;

import de.saxsys.svgfx.xml.elements.ElementBase;
import org.xml.sax.Attributes;

/**
 * This interfaces is used to create an instance of an element.
 *
 * @param <TDataProvider> the type of the {@link IDataProvider} Created by Xyanid on 25.10.2015.
 */
public interface IElementCreator<TDataProvider extends IDataProvider> {

    /**
     * creates a new instance of the desired {@link ElementBase} using the given value as an indicator which instance
     * to create.
     *
     * @param name         local value of the element for which the {@link ElementBase} is to be created
     * @param attributes   attributes which are to be applied to the {@link ElementBase}
     * @param parent       the parent to be used for the {@link ElementBase}
     * @param dataProvider the dataProvider to be used for the {@link ElementBase}
     *
     * @return a new instance of an {@link ElementBase}
     */
    ElementBase<TDataProvider, ?, ?> createElement(String name, Attributes attributes, ElementBase<TDataProvider, ?, ?> parent, TDataProvider dataProvider);
}
