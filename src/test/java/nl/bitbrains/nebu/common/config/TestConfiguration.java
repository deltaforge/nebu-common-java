package nl.bitbrains.nebu.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.config.AuthenticationConfiguration;
import nl.bitbrains.nebu.common.config.ClientConfiguration;
import nl.bitbrains.nebu.common.config.Configuration;
import nl.bitbrains.nebu.common.config.InvalidConfigurationException;
import nl.bitbrains.nebu.common.config.ServerConfiguration;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 *         Tests the {@link Configuration} class.
 */
@RunWith(JUnitParamsRunner.class)
public class TestConfiguration {

    @Mock
    private Element rootElement;
    @Mock
    private Element clientElement;
    @Mock
    private Element serverElement;
    @Mock
    private Element authenticationElement;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void setUpDefaultRoot() {
        this.setUpRoot(this.clientElement, this.serverElement);
    }

    private void setUpRoot(final Element client, final Element server) {
        final List<Element> clientlist = new ArrayList<Element>();
        clientlist.add(client);
        Mockito.when(this.rootElement.getChildren(Matchers.eq(ClientConfiguration.ROOTNAME)))
                .thenReturn(clientlist);
        Mockito.when(this.rootElement.getChild(Matchers.eq(ClientConfiguration.ROOTNAME)))
                .thenReturn(client);
        Mockito.when(this.rootElement.getChild(ServerConfiguration.ROOTNAME)).thenReturn(server);
    }

    private void setUpClientElementMock(final String ipAddress, final String vm_port) {
        Mockito.when(this.clientElement.getChildTextTrim(ClientConfiguration.TAG_IP_ADDRESS))
                .thenReturn(ipAddress);
        Mockito.when(this.clientElement.getChildTextTrim(ClientConfiguration.TAG_PORT))
                .thenReturn(vm_port);
    }

    private void setUpClientElementMock(final String ipAddress, final String vm_port,
            final String type) {
        this.setUpClientElementMock(ipAddress, vm_port);
        Mockito.when(this.clientElement.getAttributeValue(ClientConfiguration.TYPENAME))
                .thenReturn(type);
    }

    private void setUpClientElementMock(final String ipAddress, final String vm_port,
            final Element authentication) {
        this.setUpClientElementMock(ipAddress, vm_port);
        Mockito.when(this.clientElement.getChild(AuthenticationConfiguration.ROOTNAME))
                .thenReturn(authentication);
    }

    private void setUpClientElementMock(final String ipAddress, final String vm_port,
            final Element authentication, final String type) {
        this.setUpClientElementMock(ipAddress, vm_port);
        Mockito.when(this.clientElement.getChild(AuthenticationConfiguration.ROOTNAME))
                .thenReturn(authentication);
        Mockito.when(this.clientElement.getAttributeValue(ClientConfiguration.TYPENAME))
                .thenReturn(type);
    }

    private void setUpAuthenticationElementMock(final String username, final String password,
            final String keyfile) {
        Mockito.when(this.authenticationElement.getChildTextTrim(AuthenticationConfiguration.TAG_USERNAME))
                .thenReturn(username);
        Mockito.when(this.authenticationElement.getChildTextTrim(AuthenticationConfiguration.TAG_PASSWORD))
                .thenReturn(password);
        Mockito.when(this.authenticationElement.getChildTextTrim(AuthenticationConfiguration.TAG_KEYFILE))
                .thenReturn(keyfile);
    }

    private void setUpServerElementMock(final String port) {
        Mockito.when(this.serverElement.getChildTextTrim(ServerConfiguration.TAG_PORT))
                .thenReturn(port);
    }

    /**
     * Helper function to test failed case that should throw
     * {@link InvalidConfigurationException}.
     */
    private void testFailureInitializationCase() {
        boolean caught = false;
        try {
            Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    /**
     * Helper function to test failed case that should throw
     * {@link InvalidConfigurationException}.
     * 
     * @param property
     *            expected property to fail
     */
    private void testFailureInitializationCase(final String property) {
        boolean caught = false;
        try {
            Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            caught = true;
            Assert.assertEquals(property, e.getProperty());
        }
        Assert.assertTrue(caught);
    }

    // It is not actually unused, the junitparamsrunner does use it.
    @SuppressWarnings("unused")
    private Object[] invalidServerClientParameters() {
        return JUnitParamsRunner
                .$(JUnitParamsRunner.$(null, "8080", "1234", ClientConfiguration.TAG_IP_ADDRESS),
                   JUnitParamsRunner.$("", "8080", "1234", ClientConfiguration.TAG_IP_ADDRESS),
                   JUnitParamsRunner.$("192.168.2.1",
                                       Integer.toString(Configuration.MAXIMUM_PORT + 1),
                                       "1234",
                                       ClientConfiguration.TAG_PORT),
                   JUnitParamsRunner.$("192.168.2.1", "-1", "1234", ClientConfiguration.TAG_PORT),
                   JUnitParamsRunner.$("192.168.2.2",
                                       "Hello World",
                                       "1234",
                                       ClientConfiguration.TAG_PORT),
                   JUnitParamsRunner.$("192.168.2.1",
                                       "1234",
                                       Integer.toString(Configuration.MAXIMUM_PORT + 1),
                                       ClientConfiguration.TAG_PORT),
                   JUnitParamsRunner.$("192.168.2.1", "1234", "-1", ClientConfiguration.TAG_PORT),
                   JUnitParamsRunner.$("192.168.2.2",
                                       "1234",
                                       "Hello World",
                                       ClientConfiguration.TAG_PORT));

    }

    @Test
    @Parameters(method = "invalidServerClientParameters")
    public void testInvalidParameters(final String ipAddress, final String port,
            final String server_port, final String expectedError) {
        this.setUpDefaultRoot();
        this.setUpClientElementMock(ipAddress, port);
        this.setUpServerElementMock(server_port);
        this.testFailureInitializationCase(expectedError);
    }

    @Test
    public void testNullXML() {
        this.rootElement = null;
        this.testFailureInitializationCase();
    }

    @Test
    public void testFileIOError() {
        boolean caught = false;
        try {
            Configuration.parseConfigurationFile(new File("this file does not exist.txt"));
        } catch (final InvalidConfigurationException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testXMLClientIsCreated() {
        final String ipAddress = "192.168.2.1";
        final int port = 80;

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress, Integer.toString(port));

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail();
        }
        Assert.assertNotNull("Client Config", config.getClientConfigs());
    }

    @Test
    public void testXMLClientIsCorrect() {
        final String ipAddress = "192.168.2.1";
        final int port = 80;
        final String type = "type";

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress, Integer.toString(port), type);

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.toString());
        }
        final ClientConfiguration client = config.getClientConfig(type);
        Assert.assertEquals(ipAddress, client.getIpAddress());
        Assert.assertEquals(port, client.getPort());
    }

    @Test
    public void testXMLServerIsCreated() {
        final int port = 80;

        this.setUpRoot(null, this.serverElement);
        this.setUpServerElementMock(Integer.toString(port));

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.toString());
        }
        Assert.assertNotNull("ServerConfig", config.getServerConfig());
    }

    @Test
    public void testXMLServerIsCorrect() {
        final int port = 80;

        this.setUpRoot(null, this.serverElement);
        this.setUpServerElementMock(Integer.toString(port));

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.toString());
        }
        final ServerConfiguration server = config.getServerConfig();
        Assert.assertEquals(port, server.getPort());
    }

    @Test
    public void testXMLAuthIsCreated() {
        final String ipAddress = "192.168.2.1";
        final int port = 80;
        final String username = "user";
        final String password = "password";
        final String keyfile = "keyfile";
        final String type = "type";

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress,
                                    Integer.toString(port),
                                    this.authenticationElement,
                                    type);
        this.setUpAuthenticationElementMock(username, password, keyfile);

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail();
        }
        Assert.assertNotNull("Authentication Config", config.getClientConfig(type)
                .getAuthenticationConfig());
    }

    @Test
    public void testXMLAuthIsCorrect() throws InvalidConfigurationException {
        final String ipAddress = "192.168.2.1";
        final int port = 80;
        final String username = "user";
        final String password = "password";
        final String keyfile = "keyfile";
        final String type = "type";

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress,
                                    Integer.toString(port),
                                    this.authenticationElement,
                                    type);
        this.setUpAuthenticationElementMock(username, password, keyfile);

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail();
        }
        final AuthenticationConfiguration auth = config.getClientConfig(type)
                .getAuthenticationConfig();
        Assert.assertEquals(username, auth.getUsername());
        Assert.assertEquals(password, auth.getPassword());
        Assert.assertEquals(keyfile, auth.getKeyFileLocation());
    }

    @Test
    public void testXMLAuthErrorIfUsernameNullAndRequested() {
        final String ipAddress = "192.168.2.1";
        final int port = 80;
        final String username = null;
        final String password = "password";
        final String keyfile = "keyfile";
        final String type = "type";

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress,
                                    Integer.toString(port),
                                    this.authenticationElement,
                                    type);
        this.setUpAuthenticationElementMock(username, password, keyfile);

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.toString());
        }
        boolean caught = false;
        try {
            config.getClientConfig(type).getAuthenticationConfig().getUsername();
        } catch (final InvalidConfigurationException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testXMLAuthErrorIfPasswordNullAndRequested() {
        final String ipAddress = "192.168.2.1";
        final int port = 80;
        final String username = "username";
        final String password = null;
        final String keyfile = "keyfile";
        final String type = "type";

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress,
                                    Integer.toString(port),
                                    this.authenticationElement,
                                    type);
        this.setUpAuthenticationElementMock(username, password, keyfile);

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.toString());
        }
        boolean caught = false;
        try {
            config.getClientConfig(type).getAuthenticationConfig().getPassword();
        } catch (final InvalidConfigurationException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testXMLAuthErrorIfKeyfileNullAndRequested() {
        final String ipAddress = "192.168.2.1";
        final int port = 80;
        final String username = "username";
        final String password = "password";
        final String keyfile = null;
        final String type = "type";

        this.setUpRoot(this.clientElement, null);
        this.setUpClientElementMock(ipAddress,
                                    Integer.toString(port),
                                    this.authenticationElement,
                                    type);
        this.setUpAuthenticationElementMock(username, password, keyfile);

        Configuration config = null;
        try {
            config = Configuration.parseXMLRootElement(this.rootElement);
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.toString());
        }
        boolean caught = false;
        try {
            config.getClientConfig(type).getAuthenticationConfig().getKeyFileLocation();
        } catch (final InvalidConfigurationException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testIncorrectFile() {
        boolean caught = false;
        try {
            Configuration.parseConfigurationFile(new File("src/test/res/invalidXML.xml"));
        } catch (final InvalidConfigurationException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testGetReturnsSingleton() {
        Configuration config = null;
        try {
            config = Configuration
                    .parseConfigurationFile(new File("src/test/res/correctConfig.xml"));
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertSame(config, Configuration.get());
    }

    @Test
    public void testCorrectFileClient() {
        final String ipAddress = "192.168.2.1";
        final int port = 8080;
        final String type = "type";
        Configuration config = null;
        try {
            config = Configuration
                    .parseConfigurationFile(new File("src/test/res/correctConfig.xml"));
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.getMessage());
        }
        final ClientConfiguration client = config.getClientConfig(type);
        Assert.assertEquals(ipAddress, client.getIpAddress());
        Assert.assertEquals(port, client.getPort());
    }

    @Test
    public void testCorrectFileServer() {
        final int port = 1234;
        Configuration config = null;
        try {
            config = Configuration
                    .parseConfigurationFile(new File("src/test/res/correctConfig.xml"));
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.getMessage());
        }
        final ServerConfiguration[] configs = new ServerConfiguration[1];
        final ServerConfiguration server = config.getServerConfig();
        Assert.assertEquals(port, server.getPort());
    }

    @Test
    public void testCorrectFileAuth() throws InvalidConfigurationException {
        Configuration config = null;
        try {
            config = Configuration
                    .parseConfigurationFile(new File("src/test/res/correctConfig.xml"));
        } catch (final InvalidConfigurationException e) {
            Assert.fail(e.getMessage());
        }
        final String type = "type";
        final AuthenticationConfiguration auth = config.getClientConfig(type)
                .getAuthenticationConfig();
        Assert.assertEquals("testUser", auth.getUsername());
        Assert.assertEquals("testPass", auth.getPassword());
        Assert.assertEquals("/tmp/testkey", auth.getKeyFileLocation());
    }
}
