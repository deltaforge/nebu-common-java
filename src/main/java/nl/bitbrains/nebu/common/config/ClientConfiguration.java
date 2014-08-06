package nl.bitbrains.nebu.common.config;

import org.jdom2.Element;

/**
 * Client configuration, should be initialized through {@link Configuration}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public final class ClientConfiguration {
    public static final String ROOTNAME = "client";

    public static final String TAG_IP_ADDRESS = "ip";
    public static final String TAG_PORT = "port";
    public static final String TYPENAME = "type";

    private String ip;
    private int port;
    private AuthenticationConfiguration authenticationConfig;

    /**
     * Private to prevent instantiation.
     */
    private ClientConfiguration() {
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     * @throws InvalidConfigurationException
     *             if ipAddress is undefined or of zero length
     */
    private void setIPAddress(final String ipAddress) throws InvalidConfigurationException {
        if (ipAddress == null || ipAddress.length() == 0) {
            throw new InvalidConfigurationException("Ip was empty or missing",
                    ClientConfiguration.TAG_IP_ADDRESS);
        }
        this.ip = ipAddress;
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
                    ClientConfiguration.TAG_PORT);
        }
        this.port = port;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return this.ip;
    }

    /**
     * @return the port
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
    protected static ClientConfiguration parseXMLRootElement(final Element rootElement)
            throws InvalidConfigurationException {
        final ClientConfiguration result = new ClientConfiguration();
        result.setIPAddress(rootElement.getChildTextTrim(ClientConfiguration.TAG_IP_ADDRESS));
        result.setPort(rootElement.getChildTextTrim(ClientConfiguration.TAG_PORT));
        if (rootElement.getChild(AuthenticationConfiguration.ROOTNAME) != null) {
            result.setAuthenticationConfiguration(AuthenticationConfiguration
                    .parseXMLRootElement(rootElement.getChild(AuthenticationConfiguration.ROOTNAME)));
        }
        return result;
    }

    /**
     * @param authenticationconfig
     *            to set.
     */
    private void setAuthenticationConfiguration(
            final AuthenticationConfiguration authenticationconfig) {
        this.authenticationConfig = authenticationconfig;
    }

    /**
     * @return the authenticationConfig
     */
    public AuthenticationConfiguration getAuthenticationConfig() {
        return this.authenticationConfig;
    }
}
