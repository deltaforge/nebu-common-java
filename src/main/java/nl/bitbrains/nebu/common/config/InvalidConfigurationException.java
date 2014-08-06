package nl.bitbrains.nebu.common.config;

/**
 * Exception that can be used to describe what configuration property was not
 * declared correctly.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class InvalidConfigurationException extends Exception {

    private static final long serialVersionUID = 6603656316483359902L;
    private static final String UNDEFINED = "undefined";
    private final String property;

    /**
     * @param message
     *            to display as error.
     */
    public InvalidConfigurationException(final String message) {
        this(message, InvalidConfigurationException.UNDEFINED);
    }

    /**
     * @param e
     *            to base this exception on.
     */
    public InvalidConfigurationException(final Throwable e) {
        super(e);
        this.property = InvalidConfigurationException.UNDEFINED;
    }

    /**
     * @param message
     *            to display as error.
     * @param property
     *            that was misconfigured.
     */
    public InvalidConfigurationException(final String message, final String property) {
        super(message);
        this.property = property;
    }

    /**
     * @param string
     *            to set as message.
     * @param e
     *            to give as cause.
     */
    public InvalidConfigurationException(final String string, final Exception e) {
        super(string, e);
        this.property = InvalidConfigurationException.UNDEFINED;
    }

    /**
     * @param message
     *            to set as message
     * @param property
     *            to set as property.
     * @param e
     *            to give as cause.
     */
    public InvalidConfigurationException(final String property, final String message,
            final Exception e) {
        super(message, e);
        this.property = property;
    }

    /**
     * @return the property that was invalid.
     */
    public final String getProperty() {
        return this.property;
    }

}
