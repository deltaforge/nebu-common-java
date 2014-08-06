package nl.bitbrains.nebu.common;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nl.bitbrains.nebu.common.VirtualMachine;
import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.factories.VirtualMachineFactory;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TestVirtualMachineFactory {

    private final String id = "vmId";
    private final String hostname = "hostname";
    private final String host = "host";
    private final String diskID = "disk";
    private final VirtualMachine.Status status = VirtualMachine.Status.LAUNCHING;

    private VirtualMachineFactory extensiveFactory;
    private VirtualMachineFactory nonExtensiveFactory;
    @Mock
    private VirtualMachine vm;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.extensiveFactory = new VirtualMachineFactory();
        this.nonExtensiveFactory = new VirtualMachineFactory(false);
        Mockito.when(this.vm.getUniqueIdentifier()).thenReturn(this.id);
        Mockito.when(this.vm.getHostname()).thenReturn(this.hostname);
        Mockito.when(this.vm.getStatus()).thenReturn(this.status);
        Mockito.when(this.vm.getHost()).thenReturn(this.host);
    }

    private void addDisksToMock(final List<String> list) {
        Mockito.when(this.vm.getStores()).thenReturn(list);
    }

    @Test
    public void toXMLNull() {
        boolean caught = false;
        try {
            this.extensiveFactory.toXML(null);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void toXMLNonExtensive() {
        final Element xml = this.nonExtensiveFactory.toXML(this.vm);
        Assert.assertEquals(this.id, xml.getAttributeValue(IdentifiableFactory.TAG_ID));
    }

    @Test
    public void toXMLExtensive() {
        final Element xml = this.extensiveFactory.toXML(this.vm);
        Assert.assertEquals(this.id, xml.getAttributeValue(IdentifiableFactory.TAG_ID));
        Assert.assertEquals(this.hostname, xml.getChildTextTrim(VirtualMachineFactory.TAG_HOSTNAME));
        Assert.assertEquals(this.status.name(),
                            xml.getChildTextTrim(VirtualMachineFactory.TAG_STATUS));
    }

    @Test
    public void toXMLExtensiveWithDisks() {
        final List<String> list = new ArrayList<String>();
        final int numDisks = 4;
        for (int i = 0; i < numDisks; i++) {
            list.add(this.diskID + i);
        }
        this.addDisksToMock(list);
        final Element xml = this.extensiveFactory.toXML(this.vm);
        Assert.assertEquals(this.id, xml.getAttributeValue(IdentifiableFactory.TAG_ID));
        Assert.assertEquals(numDisks, xml.getChildren(VirtualMachineFactory.TAG_DISK).size());
    }

    @Test
    public void fromXMLNull() throws ParseException {
        boolean caught = false;
        try {
            this.extensiveFactory.fromXML(null);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void fromXMLNonExtensive() throws ParseException {
        final Element xml = new Element(VirtualMachineFactory.TAG_ELEMENT_ROOT);
        xml.setAttribute(IdentifiableFactory.TAG_ID, this.id);
        final VirtualMachine vm = this.nonExtensiveFactory.fromXML(xml).build();
        Assert.assertEquals(this.id, vm.getUniqueIdentifier());
    }

    private Element createExtensiveXMLWithoutDisks() {
        final Element xml = new Element(VirtualMachineFactory.TAG_ELEMENT_ROOT);
        xml.setAttribute(IdentifiableFactory.TAG_ID, this.id);
        final Element host = new Element(VirtualMachineFactory.TAG_HOSTNAME);
        host.setText(this.hostname);
        final Element statuselem = new Element(VirtualMachineFactory.TAG_STATUS);
        statuselem.setText(this.status.name());
        xml.addContent(host);
        xml.addContent(statuselem);
        final Element hostElem = new Element(VirtualMachineFactory.TAG_HOST);
        hostElem.setAttribute(IdentifiableFactory.TAG_ID, this.host);
        xml.addContent(hostElem);
        return xml;
    }

    @Test
    public void fromXMLExtensive() throws ParseException {
        final Element xml = this.createExtensiveXMLWithoutDisks();
        final VirtualMachine vm = this.extensiveFactory.fromXML(xml).build();
        Assert.assertEquals(this.id, vm.getUniqueIdentifier());
        Assert.assertEquals(this.hostname, vm.getHostname());
        Assert.assertEquals(this.status, vm.getStatus());
        Assert.assertEquals(this.host, vm.getHost());
    }

    @Test
    public void fromXMLExtensiveWithDisks() throws ParseException {
        final Element xml = this.createExtensiveXMLWithoutDisks();
        final int numDisks = 4;
        for (int i = 0; i < numDisks; i++) {
            xml.addContent(new Element(VirtualMachineFactory.TAG_DISK)
                    .setAttribute(IdentifiableFactory.TAG_ID, this.diskID + i));
        }

        final VirtualMachine vm = this.extensiveFactory.fromXML(xml).build();
        Assert.assertEquals(this.id, vm.getUniqueIdentifier());
        Assert.assertEquals(numDisks, vm.getStores().size());
        Assert.assertEquals(this.diskID + 1, vm.getStores().get(1));
    }
}
