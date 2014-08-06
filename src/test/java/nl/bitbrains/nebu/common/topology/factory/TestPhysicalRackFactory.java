package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalHostBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRackBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalResourceWithDisks;
import nl.bitbrains.nebu.common.topology.PhysicalStore;
import nl.bitbrains.nebu.common.topology.PhysicalStoreBuilder;
import nl.bitbrains.nebu.common.topology.factory.PhysicalHostFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalRackFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalStoreFactory;
import nl.bitbrains.nebu.common.topology.factory.TopologyFactories;
import nl.bitbrains.nebu.common.topology.factory.TopologyFactory;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PhysicalResourceWithDisks.class)
public class TestPhysicalRackFactory {

    @Mock
    private TopologyFactories factories;
    @Mock
    private PhysicalHostFactory mockedCPUFactory;
    @Mock
    private PhysicalStoreFactory mockedDiskFactory;

    private TopologyFactory<PhysicalRack> fac;

    @Before
    public void setupTests() {
        this.fac = new PhysicalRackFactory();
        this.setUpFactoriesMock();
    }

    private void setUpFactoriesMock() {
        MockitoAnnotations.initMocks(this);
        this.fac.setTopologyFactories(this.factories);
        Mockito.when(this.factories.getPhysicalRackFactory()).thenReturn(this.fac);
        Mockito.when(this.factories.getPhysicalCPUFactory()).thenReturn(this.mockedCPUFactory);
        Mockito.when(this.factories.getPhysicalStoreFactory()).thenReturn(this.mockedDiskFactory);
        Mockito.when(this.factories.getPhysicalDataCenterFactory())
                .thenThrow(new IllegalStateException("Not allowed to use higher layers"));
        Mockito.when(this.factories.getPhysicalRootFactory()).thenThrow(new IllegalStateException(
                "Not allowed to use higher layers"));
    }

    private void setUpPhysicalCPUFactoryfromXMLMock(final List<PhysicalHost> list)
            throws ParseException {
        Mockito.when(this.mockedCPUFactory.fromXML((Element) Matchers.any()))
                .thenAnswer(new Answer<PhysicalHostBuilder>() {
                    private int cnt = 0;

                    public PhysicalHostBuilder answer(final InvocationOnMock invocation)
                            throws Throwable {
                        final PhysicalHostBuilder b = Mockito.mock(PhysicalHostBuilder.class);
                        Mockito.when(b.build()).thenReturn(list.get(this.cnt++));
                        return b;
                    }
                });
    }

    private void setUpPhysicalCPUFactorytoXMLMock(final List<Element> list) throws ParseException {
        Mockito.when(this.mockedCPUFactory.toXML((PhysicalHost) Matchers.any()))
                .thenAnswer(new Answer<Element>() {
                    private int cnt = 0;

                    public Element answer(final InvocationOnMock invocation) throws Throwable {
                        return list.get(this.cnt++);
                    }
                });
    }

    private void setUpPhysicalCPUFactoryfromXMLMock(final Throwable e) throws ParseException {
        Mockito.when(this.mockedCPUFactory.fromXML((Element) Matchers.any())).thenThrow(e);
    }

    private void setUpPhysicalStoreFactoryfromXMLMock(final List<PhysicalStore> list)
            throws ParseException {
        Mockito.when(this.mockedDiskFactory.fromXML((Element) Matchers.any()))
                .thenAnswer(new Answer<PhysicalStoreBuilder>() {
                    private int cnt = 0;

                    public PhysicalStoreBuilder answer(final InvocationOnMock invocation)
                            throws Throwable {
                        final PhysicalStoreBuilder b = Mockito.mock(PhysicalStoreBuilder.class);
                        Mockito.when(b.build()).thenReturn(list.get(this.cnt++));
                        return b;
                    }
                });
    }

    private void setUpPhysicalStoreFactorytoXMLMock(final List<Element> list) throws ParseException {
        Mockito.when(this.mockedDiskFactory.toXML((PhysicalStore) Matchers.any()))
                .thenAnswer(new Answer<Element>() {
                    private int cnt = 0;

                    public Element answer(final InvocationOnMock invocation) throws Throwable {
                        return list.get(this.cnt++);
                    }
                });
    }

    private void setUpPhysicalStoreFactoryfromXMLMock(final Throwable e) throws ParseException {
        Mockito.when(this.mockedDiskFactory.fromXML((Element) Matchers.any())).thenThrow(e);
    }

    @Test
    public void testToXMLEmptyRack() {
        final String id = "rack1";
        final PhysicalRack rack = new PhysicalRackBuilder().withUuid("rack1").build();
        final Element elem = this.fac.toXML(rack);
        Assert.assertEquals(PhysicalRackFactory.TAG_ELEMENT_ROOT, elem.getName());
        Assert.assertEquals(id, elem.getAttributeValue(IdentifiableFactory.TAG_ID));
    }

    @Test
    public void testToXMLRackWith1CPU() throws ParseException {
        final String id = "rack1";
        final PhysicalRack rack = PowerMockito.mock(PhysicalRack.class);
        Mockito.when(rack.getUniqueIdentifier()).thenReturn(id);
        final PhysicalHost cpu = Mockito.mock(PhysicalHost.class);
        final ArrayList<PhysicalHost> cpulist = new ArrayList<PhysicalHost>();
        cpulist.add(cpu);
        Mockito.when(rack.getCPUs()).thenReturn(cpulist);

        final ArrayList<Element> list = new ArrayList<Element>();
        list.add(new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT));
        this.setUpPhysicalCPUFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(rack);
        Assert.assertNotNull(elem.getChild(PhysicalHostFactory.TAG_ELEMENT_ROOT));
    }

    @Test
    public void testToXMLRackWith4CPU() throws ParseException {
        final int numChildren = 4;
        final String id = "rack1";
        final PhysicalRack rack = PowerMockito.mock(PhysicalRack.class);
        Mockito.when(rack.getUniqueIdentifier()).thenReturn(id);
        final ArrayList<PhysicalHost> cpulist = new ArrayList<PhysicalHost>();

        final ArrayList<Element> list = new ArrayList<Element>();
        for (int i = 0; i < numChildren; i++) {
            cpulist.add(Mockito.mock(PhysicalHost.class));
            list.add(new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT));
        }
        Mockito.when(rack.getCPUs()).thenReturn(cpulist);
        this.setUpPhysicalCPUFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(rack);
        Assert.assertNotNull(elem.getChild(PhysicalHostFactory.TAG_ELEMENT_ROOT));
        Assert.assertEquals(numChildren, elem.getChildren().size());
    }

    @Test
    public void testToXMLRackWith1Disk() throws ParseException {
        final String id = "rack1";
        final PhysicalRack rack = PowerMockito.mock(PhysicalRack.class);
        Mockito.when(rack.getUniqueIdentifier()).thenReturn(id);
        final PhysicalStore disk = Mockito.mock(PhysicalStore.class);
        final ArrayList<PhysicalStore> disklist = new ArrayList<PhysicalStore>();
        disklist.add(disk);
        Mockito.when(rack.getDisks()).thenReturn(disklist);

        final ArrayList<Element> list = new ArrayList<Element>();
        list.add(new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT));
        this.setUpPhysicalStoreFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(rack);
        Assert.assertNotNull(elem.getChild(PhysicalStoreFactory.TAG_ELEMENT_ROOT));
    }

    @Test
    public void testToXMLRackWith4Disk() throws ParseException {
        final int numChildren = 4;
        final String id = "rack1";
        final PhysicalRack rack = PowerMockito.mock(PhysicalRack.class);
        Mockito.when(rack.getUniqueIdentifier()).thenReturn(id);
        final ArrayList<PhysicalStore> disklist = new ArrayList<PhysicalStore>();

        final ArrayList<Element> list = new ArrayList<Element>();
        for (int i = 0; i < numChildren; i++) {
            disklist.add(Mockito.mock(PhysicalStore.class));
            list.add(new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT));
        }
        Mockito.when(rack.getDisks()).thenReturn(disklist);
        this.setUpPhysicalStoreFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(rack);
        Assert.assertNotNull(elem.getChild(PhysicalStoreFactory.TAG_ELEMENT_ROOT));
        Assert.assertEquals(numChildren, elem.getChildren().size());
    }

    @Test
    public void testToXMLNull() {
        boolean caught = false;
        try {
            this.fac.toXML(null);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLEmptyRack() throws ParseException {
        final Element rack = new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);

        final PhysicalRack prack = this.fac.fromXML(rack).build();

        Assert.assertEquals(idAttr.getValue(), prack.getUniqueIdentifier());
    }

    @Test
    public void testFromXMLNonEmptyRack() throws ParseException {
        final Element rack = new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 4;
        final List<PhysicalHost> list = new ArrayList<PhysicalHost>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalHostBuilder().withUuid("rack" + i).build());
        }
        this.setUpPhysicalCPUFactoryfromXMLMock(list);

        final List<PhysicalStore> disks = new ArrayList<PhysicalStore>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT));
            disks.add(new PhysicalStoreBuilder().withUuid("store" + i).build());
        }
        this.setUpPhysicalStoreFactoryfromXMLMock(disks);

        final PhysicalRack prack = this.fac.fromXML(rack).build();

        Assert.assertEquals(numChilds, prack.getCPUs().size());
        Assert.assertEquals(numChilds, prack.getDisks().size());
    }

    @Test
    public void testFromXMLNull() {
        boolean caught = false;
        try {
            this.fac.fromXML(null);
        } catch (final IllegalArgumentException e) {
            caught = true;
        } catch (final ParseException e) {
            Assert.fail("Wrong exception");
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLNoID() {
        final Element elem = new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT);
        boolean caught = false;
        try {
            this.fac.fromXML(elem);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLWrongRoot() {
        final Element elem = new Element("thisisawrongtagname");
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "id");
        elem.setAttribute(idAttr);

        boolean caught = false;
        try {
            this.fac.fromXML(elem);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLWrongCPU() throws ParseException {
        final Element rack = new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 2;
        final List<PhysicalHost> list = new ArrayList<PhysicalHost>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalHostBuilder().withUuid("rack" + i).build());
        }
        this.setUpPhysicalCPUFactoryfromXMLMock(new ParseException("error", 1));

        boolean caught = false;
        try {
            this.fac.fromXML(rack);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLWrongDisk() throws ParseException {
        final Element rack = new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 2;
        final List<PhysicalStore> list = new ArrayList<PhysicalStore>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalStoreBuilder().withUuid("rack" + i).build());
        }
        this.setUpPhysicalStoreFactoryfromXMLMock(new ParseException("error", 1));

        boolean caught = false;
        try {
            this.fac.fromXML(rack);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }
}
