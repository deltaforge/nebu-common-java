package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalHostBuilder;
import nl.bitbrains.nebu.common.topology.factory.PhysicalHostFactory;
import nl.bitbrains.nebu.common.topology.factory.TopologyFactories;
import nl.bitbrains.nebu.common.topology.factory.TopologyFactory;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPhysicalHostFactory {

    TopologyFactory<PhysicalHost> fac;

    @Before
    public void setupTests() {
        final TopologyFactories facs = TopologyFactories.createDefault();
        this.fac = facs.getPhysicalCPUFactory();
    }

    @Test
    public void testToXML() {
        final PhysicalHost cpu = new PhysicalHostBuilder().withUuid("CPU_id").build();

        final Element cpuElem = this.fac.toXML(cpu);

        Assert.assertEquals("CPU_id", cpuElem.getAttribute("id").getValue());
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
    public void testFromXMLNoMemNoCpu() throws ParseException {
        final Element cpuElem = new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        cpuElem.addContent(new Element(PhysicalHostFactory.TAG_STORES));
        cpuElem.setAttribute(idAttr);

        final PhysicalHost cpu = this.fac.fromXML(cpuElem).build();

        Assert.assertEquals("CPU_id", cpu.getUniqueIdentifier());
    }

    @Test
    public void testFromXMLWithMemWithCpu() throws ParseException {
        final double mem = 12.4;
        final double cpuUsed = 42.42;
        final double epsilon = 10E-10;
        final Element cpuElem = new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT);
        cpuElem.addContent(new Element(PhysicalHostFactory.TAG_STORES));
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        cpuElem.setAttribute(idAttr);
        final Element memElem = new Element(PhysicalHostFactory.TAG_MEM_USAGE).setText(Double
                .toString(mem));
        final Element cpuUsedElem = new Element(PhysicalHostFactory.TAG_CPU_USAGE).setText(Double
                .toString(cpuUsed));
        cpuElem.addContent(memElem).addContent(cpuUsedElem);

        final PhysicalHost cpu = this.fac.fromXML(cpuElem).build();

        Assert.assertEquals("CPU_id", cpu.getUniqueIdentifier());
        Assert.assertEquals(mem, cpu.getMemUsage(), epsilon);
        Assert.assertEquals(cpuUsed, cpu.getCpuUsage(), epsilon);
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
        final Element cpuElem = new Element(PhysicalHostFactory.TAG_ELEMENT_ROOT);
        boolean caught = false;
        try {
            this.fac.fromXML(cpuElem);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLWrongRoot() {
        final Element cpuElem = new Element("thisisawrongtagname");
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "CPU_id");
        cpuElem.setAttribute(idAttr);

        boolean caught = false;
        try {
            this.fac.fromXML(cpuElem);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }
}
