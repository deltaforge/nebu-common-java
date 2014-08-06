package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenterBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalRootBuilder;
import nl.bitbrains.nebu.common.topology.factory.PhysicalDataCenterFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalRootFactory;
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
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class TestPhysicalRootFactory {

    @Mock
    private TopologyFactories factories;
    @Mock
    private PhysicalDataCenterFactory mockedDcFactory;

    private TopologyFactory<PhysicalRoot> fac;

    @Before
    public void setupTests() {
        this.fac = new PhysicalRootFactory();
        this.setUpFactoriesMock();
    }

    private void setUpFactoriesMock() {
        MockitoAnnotations.initMocks(this);
        this.fac.setTopologyFactories(this.factories);
        Mockito.when(this.factories.getPhysicalCPUFactory()).thenThrow(new IllegalStateException(
                "Not allowed to use lower layers"));
        Mockito.when(this.factories.getPhysicalRackFactory()).thenThrow(new IllegalStateException(
                "Not allowed to use lower layers"));
        Mockito.when(this.factories.getPhysicalDataCenterFactory())
                .thenReturn(this.mockedDcFactory);
        Mockito.when(this.factories.getPhysicalRootFactory()).thenReturn(this.fac);
    }

    private void setUpPhysicalDataCenterFactoryfromXMLMock(final List<PhysicalDataCenter> list)
            throws ParseException {
        Mockito.when(this.mockedDcFactory.fromXML((Element) Matchers.any()))
                .thenAnswer(new Answer<PhysicalDataCenterBuilder>() {
                    private int cnt = 0;

                    public PhysicalDataCenterBuilder answer(final InvocationOnMock invocation)
                            throws Throwable {
                        final PhysicalDataCenterBuilder b = Mockito.mock(PhysicalDataCenterBuilder.class);
                        Mockito.when(b.build()).thenReturn(list.get(this.cnt++));
                        return b;
                    }
                });
    }

    private void setUpPhysicalDataCenterFactorytoXMLMock(final List<Element> list)
            throws ParseException {
        Mockito.when(this.mockedDcFactory.toXML((PhysicalDataCenter) Matchers.any()))
                .thenAnswer(new Answer<Element>() {
                    private int cnt = 0;

                    public Element answer(final InvocationOnMock invocation) throws Throwable {
                        return list.get(this.cnt++);
                    }
                });
    }

    private void setUpPhysicalDataCenterFactoryfromXMLMock(final Throwable e) throws ParseException {
        Mockito.when(this.mockedDcFactory.fromXML((Element) Matchers.any())).thenThrow(e);
    }

    @Test
    public void testToXMLEmptyDataCenter() {
        final String id = "root1";
        final PhysicalRoot root = new PhysicalRootBuilder().withUuid(id).build();
        final Element elem = this.fac.toXML(root);
        Assert.assertEquals(PhysicalRootFactory.TAG_ELEMENT_ROOT, elem.getName());
        Assert.assertEquals(id, elem.getAttributeValue(IdentifiableFactory.TAG_ID));
    }

    @Test
    public void testToXMLDataCenterWith1Rack() throws ParseException {
        final String id = "root";
        final PhysicalRoot root = Mockito.mock(PhysicalRoot.class);
        Mockito.when(root.getUniqueIdentifier()).thenReturn(id);
        final PhysicalDataCenter dc = Mockito.mock(PhysicalDataCenter.class);
        final ArrayList<PhysicalDataCenter> dclist = new ArrayList<PhysicalDataCenter>();
        dclist.add(dc);
        Mockito.when(root.getDataCenters()).thenReturn(dclist);

        final ArrayList<Element> list = new ArrayList<Element>();
        list.add(new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT));
        this.setUpPhysicalDataCenterFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(root);
        Assert.assertNotNull(elem.getChild(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT));
    }

    @Test
    public void testToXMLDataCenterWith4Racks() throws ParseException {
        final int numChildren = 4;
        final String id = "root";

        final PhysicalRoot root = Mockito.mock(PhysicalRoot.class);
        Mockito.when(root.getUniqueIdentifier()).thenReturn(id);

        final ArrayList<PhysicalDataCenter> dclist = new ArrayList<PhysicalDataCenter>();

        Mockito.when(root.getDataCenters()).thenReturn(dclist);

        final ArrayList<Element> list = new ArrayList<Element>();
        for (int i = 0; i < numChildren; i++) {
            dclist.add(Mockito.mock(PhysicalDataCenter.class));
            list.add(new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT));
        }
        this.setUpPhysicalDataCenterFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(root);
        Assert.assertNotNull(elem.getChild(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT));
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
    public void testFromXMLEmptyDataCenter() throws ParseException {
        final Element rack = new Element(PhysicalRootFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);

        final PhysicalRoot proot = this.fac.fromXML(rack).build();

        Assert.assertEquals(idAttr.getValue(), proot.getUniqueIdentifier());
    }

    @Test
    public void testFromXMLNonEmptyDataCenter() throws ParseException {
        final Element rack = new Element(PhysicalRootFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 4;
        final List<PhysicalDataCenter> list = new ArrayList<PhysicalDataCenter>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalDataCenterBuilder().withUuid("dc" + i).build());
        }
        this.setUpPhysicalDataCenterFactoryfromXMLMock(list);

        final PhysicalRoot proot = this.fac.fromXML(rack).build();

        Assert.assertEquals(numChilds, proot.getDataCenters().size());
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
        final Element elem = new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT);
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
    public void testFromXMLWrongRack() throws ParseException {
        final Element rack = new Element(PhysicalRootFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 2;
        final List<PhysicalDataCenter> list = new ArrayList<PhysicalDataCenter>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalDataCenterBuilder().withUuid("dc" + 1).build());
        }
        this.setUpPhysicalDataCenterFactoryfromXMLMock(new ParseException("error", 1));

        boolean caught = false;
        try {
            this.fac.fromXML(rack);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }
}
