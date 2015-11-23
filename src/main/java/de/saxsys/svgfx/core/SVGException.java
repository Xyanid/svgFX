package de.saxsys.svgfx.core;

/**
 * This exception will be thrown if an error occurs while processing an svg file.
 * Created by Xyanid on 02.11.2015.
 */
public class SVGException extends RuntimeException {

    // region Constructor

    /**
     * Creates a new instance.
     */
    public SVGException() {
    }

    /**
     * Creates a new instance.
     *
     * @param message the message of the exception
     */
    public SVGException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public SVGException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     *
     * @param cause the cause for this exception
     */
    public SVGException(final Throwable cause) {
        super(cause);
    }

    // endregion
}
