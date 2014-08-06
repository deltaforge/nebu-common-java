package nl.bitbrains.nebu.common.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 *         This singleton class can parse a configuration file and present the
 *         configuration in a Java object.
 */
public final class Configuration {

    public static final int MAXIMUM_PORT = 65535;

    private static Configuration instance;
    private Map<String, ClientConfiguration> clientConfig;
    private ServerConfiguration serverConfig;

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private Configuration() {

    }

    /**
     * Creates a new singleton based on an XML-formatted configuration file. NB:
     * This creates a new singleton, use with caution!
     * 
     * @param configurationFile
     *            the XML-formatted file.
     * @return the parsed configuration.
     * @throws InvalidConfigurationException
     *             if the configuration is improperly specified.
     */
    public static Configuration parseConfigurationFile(final File configurationFile)
            throws InvalidConfigurationException {
        final SAXBuilder jdomBuilder = new SAXBuilder();
        try {
            final Document xmlDocument = jdomBuilder.build(configurationFile);
            final Element rootElement = xmlDocument.getRootElement();
            return Configuration.parseXMLRootElement(rootElement);
        } catch (final JDOMException e) {
            throw new InvalidConfigurationException("File was not valid xml", e);
        } catch (final IOException e) {
            throw new InvalidConfigurationException(e);
        }

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
    public static Configuration parseXMLRootElement(final Element rootElement)
            throws InvalidConfigurationException {
        if (rootElement == null) {
            throw new InvalidConfigurationException("No rootNode found");
        }
        final Configuration result = new Configuration();
        Configuration.instance = result;
        result.clientConfig = new HashMap<String, ClientConfiguration>();
        if (rootElement.getChild(ClientConfiguration.ROOTNAME) != null) {
            final List<Element> clientconfigs = rootElement
                    .getChildren(ClientConfiguration.ROOTNAME);
            for (final Element config : clientconfigs) {
                final String typeval = config.getAttributeValue(ClientConfiguration.TYPENAME);
                final ClientConfiguration parsedclient = ClientConfiguration
                        .parseXMLRootElement(config);
                result.clientConfig.put(typeval, parsedclient);
            }
        }
        if (rootElement.getChild(ServerConfiguration.ROOTNAME) != null) {
            result.serverConfig = ServerConfiguration.parseXMLRootElement(rootElement
                    .getChild(ServerConfiguration.ROOTNAME));
        }
        return result;
    }

    /**
     * @return the clientConfig
     */
    public Map<String, ClientConfiguration> getClientConfigs() {
        return this.clientConfig;
    }

    /**
     * Retrieved the {@link ClientConfiguration} of the given type.
     * 
     * @param type
     *            The type.
     * @return A {@link ClientConfiguration} or <code>null</code>.
     */
    public ClientConfiguration getClientConfig(final String type) {
        return this.clientConfig.get(type);
    }

    /**
     * @return the serverConfig
     */
    public ServerConfiguration getServerConfig() {
        return this.serverConfig;
    }

    /**
     * @return the singleton.
     */
    public static Configuration get() {
        return Configuration.instance;
    }

    /**
     * @param object
     *            to check if it is null.
     * @param string
     *            to returnas error.
     * @throws InvalidConfigurationException
     *             if object is null.
     */
    public static void throwInvalidConfigIfNull(final Object object, final String string)
            throws InvalidConfigurationException {
        if (object == null) {
            throw new InvalidConfigurationException(string + " was null");
        }
    }

    /**
     * @param integer
     *            to check if it is an integer.
     * @param name
     *            to return as error.
     * @throws InvalidConfigurationException
     *             if object is null.
     */
    public static void throwInvalidConfigIfNonInteger(final String integer, final String name)
            throws InvalidConfigurationException {
        try {
            Integer.parseInt(integer);
        } catch (final NumberFormatException e) {
            throw new InvalidConfigurationException(name, name + " was not a number", e);
        }
    }

}
