package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalStore;
import nl.bitbrains.nebu.common.topology.PhysicalStoreBuilder;
import nl.bitbrains.nebu.common.topology.factory.PhysicalStoreFactory;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPhysicalStoreFactory {

    PhysicalStoreFactory fac;

    @Before
    public void setupTests() {
        this.fac = new PhysicalStoreFactory();
    }

    @Test
    public void testToXML() {
        final int cap = 42;
        final long used = 21;
        final PhysicalStore disk = new PhysicalStoreBuilder().withCapacity(cap).withUsed(used)
                .withUuid("STORE_id").build();

        final Element diskElem = this.fac.toXML(disk);

        Assert.assertEquals("STORE_id", diskElem.getAttribute("id").getValue());
        Assert.assertEquals(cap, Integer.parseInt(diskElem
                .getChildTextTrim(PhysicalStoreFactory.TAG_CAPACITY)));
        Assert.assertEquals(used, Integer.parseInt(diskElem
                .getChildTextTrim(PhysicalStoreFactory.TAG_USED)));
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
    public void testFromXMLWithCapacityAndUsed() throws ParseException {
        final long cap = 42;
        final long used = 21;
        final Element diskElem = new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "STORE_id");
        diskElem.setAttribute(idAttr);
        final Element capElem = new Element(PhysicalStoreFactory.TAG_CAPACITY);
        capElem.setText(Long.toString(cap));
        diskElem.addContent(capElem);

        final Element usedElem = new Element(PhysicalStoreFactory.TAG_USED);
        usedElem.setText(Long.toString(used));
        diskElem.addContent(usedElem);

        final PhysicalStore disk = this.fac.fromXML(diskElem).build();

        Assert.assertEquals("STORE_id", disk.getUniqueIdentifier());
        Assert.assertEquals(cap, disk.getCapacity());
    }

    @Test
    public void testFromXMLWithoutCapacity() throws ParseException {
        final Element diskElem = new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "STORE_id");
        diskElem.setAttribute(idAttr);

        final PhysicalStore disk = this.fac.fromXML(diskElem).build();

        Assert.assertEquals("STORE_id", disk.getUniqueIdentifier());
        Assert.assertEquals(0, disk.getCapacity());
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
        final Element diskElem = new Element(PhysicalStoreFactory.TAG_ELEMENT_ROOT);
        boolean caught = false;
        try {
            this.fac.fromXML(diskElem);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testFromXMLWrongRoot() {
        final Element diskElem = new Element("thisisawrongtagname");
        final Attribute idAttr = new Attribute(IdentifiableFactory.TAG_ID, "STORE_id");
        diskElem.setAttribute(idAttr);

        boolean caught = false;
        try {
            this.fac.fromXML(diskElem);
        } catch (final ParseException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }
}
