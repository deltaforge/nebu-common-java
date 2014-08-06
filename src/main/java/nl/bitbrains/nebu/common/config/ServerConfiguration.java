package nl.bitbrains.nebu.common.config;

import org.jdom2.Element;

/**
 * Server configuration, should be initialized through {@link Configuration}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public final class ServerConfiguration {

    public static final String ROOTNAME = "server";
    public static final String TAG_PORT = "port";

    private int port;

    /**
     * Hides the constructor.
     */
    private ServerConfiguration() {
    }

    /**
     * @param portString
     *            the port to set
     * @throws InvalidConfigurationException
     *             if portString is not an integer or out of range.
     */
    private void setPort(final String portString) throws InvalidConfigurationException {
        Configuration.throwInvalidConfigIfNonInteger(portString, ServerConfiguration.TAG_PORT);
        this.setPort(Integer.parseInt(portString));
    }

    /**
     * @param port
     *            the port to set
     * @throws InvalidConfigurationException
     *             if port is not in port range.
     */
    private void setPort(final int port) throws InvalidConfigurationException {
        if (port < 0 || port > Configuration.MAXIMUM_PORT) {
            throw new InvalidConfigurationException("Port is out of range",
                    ServerConfiguration.TAG_PORT);
        }
        this.port = port;
    }

    /**
     * @return the serverPort
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Creates a new singleton based on an XML-element file. NB: This creates a
     * new singleton, use with caution!
     * 
     * @param rootElement
     *            element that is the root of the configuration.
     * @return the parsed configuration.
     * @throws InvalidConfigurationException
     *             if the configuration is improperly specified.
     */
    protected static ServerConfiguration parseXMLRootElement(final Element rootElement)
            throws InvalidConfigurationException {

        final ServerConfiguration result = new ServerConfiguration();
        result.setPort(rootElement.getChildTextTrim(ServerConfiguration.TAG_PORT));
        return result;
    }
}
