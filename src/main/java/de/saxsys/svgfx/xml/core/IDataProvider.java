package de.saxsys.svgfx.xml.core;

/**
 * This interface will be used to enable xml element to load data of the parsed xml file.
 * It is also possible for element to add data to the data provider.
 * Created by Xyanid on 03.11.2015.
 */
public interface IDataProvider {

    /**
     * Reset the dataprovider and clears out all allocated resources.
     */
    void clear();
}
