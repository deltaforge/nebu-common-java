package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenterBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRackBuilder;
import nl.bitbrains.nebu.common.topology.factory.PhysicalDataCenterFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalRackFactory;
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
public class TestPhysicalDataCenterFactory {

    @Mock
    private TopologyFactories factories;
    @Mock
    private PhysicalRackFactory mockedRackFactory;

    private TopologyFactory<PhysicalDataCenter> fac;

    @Before
    public void setupTests() {
        this.fac = new PhysicalDataCenterFactory();
        this.setUpFactoriesMock();
    }

    private void setUpFactoriesMock() {
        MockitoAnnotations.initMocks(this);
        this.fac.setTopologyFactories(this.factories);
        Mockito.when(this.factories.getPhysicalCPUFactory()).thenThrow(new IllegalStateException(
                "Not allowed to use lower layers"));
        Mockito.when(this.factories.getPhysicalRackFactory()).thenReturn(this.mockedRackFactory);
        Mockito.when(this.factories.getPhysicalDataCenterFactory()).thenReturn(this.fac);
        Mockito.when(this.factories.getPhysicalRootFactory()).thenThrow(new IllegalStateException(
                "Not allowed to use higher layers"));
    }

    private void setUpPhysicalRackFactoryfromXMLMock(final List<PhysicalRack> list)
            throws ParseException {
        Mockito.when(this.mockedRackFactory.fromXML((Element) Matchers.any()))
                .thenAnswer(new Answer<PhysicalRackBuilder>() {
                    private int cnt = 0;

                    public PhysicalRackBuilder answer(final InvocationOnMock invocation)
                            throws Throwable {
                        final PhysicalRackBuilder b = Mockito.mock(PhysicalRackBuilder.class);
                        Mockito.when(b.build()).thenReturn(list.get(this.cnt++));
                        return b;
                    }
                });
    }

    private void setUpPhysicalRackFactorytoXMLMock(final List<Element> list) throws ParseException {
        Mockito.when(this.mockedRackFactory.toXML((PhysicalRack) Matchers.any()))
                .thenAnswer(new Answer<Element>() {
                    private int cnt = 0;

                    public Element answer(final InvocationOnMock invocation) throws Throwable {
                        return list.get(this.cnt++);
                    }
                });
    }

    private void setUpPhysicalRackFactoryfromXMLMock(final Throwable e) throws ParseException {
        Mockito.when(this.mockedRackFactory.fromXML((Element) Matchers.any())).thenThrow(e);
    }

    @Test
    public void testToXMLEmptyDataCenter() {
        final String id = "rack1";
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withUuid(id).build();
        final Element elem = this.fac.toXML(dc);
        Assert.assertEquals(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT, elem.getName());
        Assert.assertEquals(id, elem.getAttributeValue(IdentifiableFactory.TAG_ID));
    }

    @Test
    public void testToXMLDataCenterWith1Rack() throws ParseException {
        final String id = "dc1";
        final PhysicalRack rack = new PhysicalRackBuilder().withUuid("rack1").build();
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withRack(rack).withUuid(id)
                .build();
        final ArrayList<Element> list = new ArrayList<Element>();
        list.add(new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT));
        this.setUpPhysicalRackFactorytoXMLMock(list);

        final Element elem = this.fac.toXML(dc);
        Assert.assertNotNull(elem.getChild(PhysicalRackFactory.TAG_ELEMENT_ROOT));
    }

    @Test
    public void testToXMLDataCenterWith4Racks() throws ParseException {
        final int numChildren = 4;
        final String id = "dc1";
        final PhysicalDataCenterBuilder dcBuilder = new PhysicalDataCenterBuilder();
        dcBuilder.withUuid(id);
        final ArrayList<Element> list = new ArrayList<Element>();
        for (int i = 0; i < numChildren; i++) {
            dcBuilder.withRack(new PhysicalRackBuilder().withUuid("rack" + i).build());
            list.add(new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT));
        }
        this.setUpPhysicalRackFactorytoXMLMock(list);

        final PhysicalDataCenter dc = dcBuilder.build();
        final Element elem = this.fac.toXML(dc);
        Assert.assertNotNull(elem.getChild(PhysicalRackFactory.TAG_ELEMENT_ROOT));
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
        final Element rack = new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);

        final PhysicalDataCenter pdc = this.fac.fromXML(rack).build();

        Assert.assertEquals(idAttr.getValue(), pdc.getUniqueIdentifier());
    }

    @Test
    public void testFromXMLNonEmptyDataCenter() throws ParseException {
        final Element rack = new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 4;
        final List<PhysicalRack> list = new ArrayList<PhysicalRack>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalRackBuilder().withUuid("rack" + i).build());
        }
        this.setUpPhysicalRackFactoryfromXMLMock(list);

        final PhysicalDataCenter pdc = this.fac.fromXML(rack).build();

        Assert.assertEquals(numChilds, pdc.getRacks().size());
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
        final Element rack = new Element(PhysicalDataCenterFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        rack.setAttribute(idAttr);
        final int numChilds = 2;
        final List<PhysicalRack> list = new ArrayList<PhysicalRack>();
        for (int i = 0; i < numChilds; i++) {
            rack.addContent(new Element(PhysicalRackFactory.TAG_ELEMENT_ROOT));
            list.add(new PhysicalRackBuilder().withUuid("rack" + i).build());
        }
        this.setUpPhysicalRackFactoryfromXMLMock(new ParseException("error", 1));

        boolean caught = false;
        try {
            this.fac.fromXML(rack);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }
}
