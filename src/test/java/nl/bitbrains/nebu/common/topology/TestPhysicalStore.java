package nl.bitbrains.nebu.common.topology;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalStore;
import nl.bitbrains.nebu.common.topology.PhysicalStoreBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(JUnitParamsRunner.class)
public class TestPhysicalStore {

    private final String diskID = "Some CPU ID";
    private final String otherDiskID = "Other CPU ID";

    private PhysicalStore disk;
    private PhysicalStore cloneDisk;
    private PhysicalStore otherDisk;

    @Before
    public void setUp() {
        this.disk = new PhysicalStoreBuilder().withUuid(this.diskID).build();
        this.cloneDisk = new PhysicalStoreBuilder().withUuid(this.diskID).build();
        this.otherDisk = new PhysicalStoreBuilder().withUuid(this.otherDiskID).build();
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(this.diskID, this.disk.getUniqueIdentifier());
    }

    @Test
    public void testIDIsRequired() {
        boolean caught = false;
        try {
            new PhysicalStoreBuilder().build();
        } catch (final IllegalStateException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testParent() {
        final PhysicalRack rack = Mockito.mock(PhysicalRack.class);
        this.disk.setParent(rack);
        Assert.assertEquals(rack, this.disk.getParent());
    }

    @Test
    public void testParentBuilder() {
        final PhysicalRack rack = Mockito.mock(PhysicalRack.class);
        this.disk = new PhysicalStoreBuilder().withParent(rack).withUuid(this.diskID).build();
        Assert.assertEquals(rack, this.disk.getParent());
    }

    @Test
    public void testCapacity() {
        final int cap = 102;
        this.disk.setCapacity(cap);
        Assert.assertEquals(cap, this.disk.getCapacity());
    }

    @Test
    public void testCapacityBuilder() {
        final int cap = 102;
        this.disk = new PhysicalStoreBuilder().withCapacity(cap).withUuid(this.diskID).build();
        Assert.assertEquals(cap, this.disk.getCapacity());
    }

    private Object[] equalsParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.disk, null, false),
                                   JUnitParamsRunner.$(this.disk, this.cloneDisk, true),
                                   JUnitParamsRunner.$(this.cloneDisk, this.disk, true),
                                   JUnitParamsRunner.$(this.disk, this.otherDisk, false),
                                   JUnitParamsRunner.$(this.disk, 1, false),
                                   JUnitParamsRunner.$(this.disk,
                                                       this.disk.getUniqueIdentifier(),
                                                       false));
    }

    private Object[] hashCodeParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.disk, this.cloneDisk, true),
                                   JUnitParamsRunner.$(this.disk, this.disk, true),
                                   JUnitParamsRunner.$(this.disk, this.otherDisk, false));
    }

    @Test
    @Parameters(method = "equalsParams")
    public void equalsTest(final Object a, final Object b, final boolean result) {
        if (result) {
            Assert.assertEquals(a, b);
        } else {
            Assert.assertNotEquals(a, b);
        }
    }

    @Test
    @Parameters(method = "hashCodeParams")
    public void hashCodeTest(final Object a, final Object b, final boolean result) {
        if (result) {
            Assert.assertEquals(a.hashCode(), b.hashCode());
        } else {
            Assert.assertNotEquals(a.hashCode(), b.hashCode());
        }
    }

}
