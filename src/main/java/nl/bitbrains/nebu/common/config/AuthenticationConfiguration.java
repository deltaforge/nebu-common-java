package nl.bitbrains.nebu.common.config;

import org.jdom2.Element;

/**
 * Hold authentication configuration details, should be instantiated through
 * {@link Configuration}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public final class AuthenticationConfiguration {

    public static final String ROOTNAME = "auth";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_KEYFILE = "keyfile";

    private String username = null;
    private String password = null;
    private String keyFileLocation = null;

    /**
     * Private constructor to prevent instantiation.
     */
    private AuthenticationConfiguration() {
    }

    /**
     * @return the username
     * @throws InvalidConfigurationException
     *             if null
     */
    public String getUsername() throws InvalidConfigurationException {
        Configuration
                .throwInvalidConfigIfNull(this.username,
                                          AuthenticationConfiguration.TAG_USERNAME);
        return this.username;
    }

    /**
     * @param username
     *            the username to set
     */
    private void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the password
     * @throws InvalidConfigurationException
     *             if null
     */
    public String getPassword() throws InvalidConfigurationException {
        Configuration
                .throwInvalidConfigIfNull(this.password,
                                          AuthenticationConfiguration.TAG_PASSWORD);
        return this.password;
    }

    /**
     * @param password
     *            the password to set
     */
    private void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return the keyFileLocation
     * @throws InvalidConfigurationException
     *             if null
     */
    public String getKeyFileLocation() throws InvalidConfigurationException {
        Configuration
                .throwInvalidConfigIfNull(this.keyFileLocation,
                                          AuthenticationConfiguration.TAG_KEYFILE);
        return this.keyFileLocation;
    }

    /**
     * @param keyFileLocation
     *            the keyFileLocation to set
     */
    private void setKeyFileLocation(final String keyFileLocation) {
        this.keyFileLocation = keyFileLocation;
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
    protected static AuthenticationConfiguration parseXMLRootElement(
            final Element rootElement) throws InvalidConfigurationException {

        final AuthenticationConfiguration result = new AuthenticationConfiguration();
        result.setUsername(rootElement
                .getChildTextTrim(AuthenticationConfiguration.TAG_USERNAME));
        result.setPassword(rootElement
                .getChildTextTrim(AuthenticationConfiguration.TAG_PASSWORD));
        result.setKeyFileLocation(rootElement
                .getChildTextTrim(AuthenticationConfiguration.TAG_KEYFILE));
        return result;
    }
}
