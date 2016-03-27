package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.xml.attribute.AttributeType;

/**
 * @author Xyanid on 16.03.2016.
 */
public abstract class AttributeBase<TSVGContentType extends AttributeType> {

    // region Fields

    /**
     * Contains the name of the attribute
     */
    private final String name;
    /**
     * This is the actual content of the attribute.
     */
    private final TSVGContentType content;

    // region

    // region Constructor

    public AttributeBase(final String name, final TSVGContentType content) {
        this.name = name;
        this.content = content;
    }

    // endregion


    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return the {@link #name}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the {@link #content}.
     *
     * @return the {@link #content}.
     */
    public TSVGContentType getContent() {
        return content;
    }

    // endregion
}
