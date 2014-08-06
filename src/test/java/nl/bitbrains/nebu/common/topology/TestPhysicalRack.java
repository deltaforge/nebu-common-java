package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRackBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalStore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
@RunWith(JUnitParamsRunner.class)
public class TestPhysicalRack {

    private static final String rackID = "id";
    private static final String otherID = "other";

    private PhysicalRack rack;
    private PhysicalRack cloneRack;
    private PhysicalRack otherRack;

    @Before
    public void setUp() {
        this.rack = new PhysicalRackBuilder().withUuid(TestPhysicalRack.rackID).build();
        this.cloneRack = new PhysicalRackBuilder().withUuid(TestPhysicalRack.rackID).build();
        this.otherRack = new PhysicalRackBuilder().withUuid(TestPhysicalRack.otherID).build();
    }

    public static PhysicalHost mockCPU(final String identifier) {
        final PhysicalHost cpu = Mockito.mock(PhysicalHost.class);
        Mockito.when(cpu.getUniqueIdentifier()).thenReturn(identifier);
        return cpu;
    }

    public static PhysicalStore mockDisk(final String identifier) {
        final PhysicalStore store = Mockito.mock(PhysicalStore.class);
        Mockito.when(store.getUniqueIdentifier()).thenReturn(identifier);
        return store;
    }

    @Test
    public void testEmptyConstructor() {
        Assert.assertEquals(TestPhysicalRack.rackID, this.rack.getUniqueIdentifier());
        Assert.assertNotNull(this.rack.getCPUs());
    }

    @Test
    public void testIDIsRequired() {
        boolean caught = false;
        try {
            new PhysicalRackBuilder().build();
        } catch (final IllegalStateException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testListConstructorWithNullList() {
        Assert.assertTrue(this.rack.getCPUs().isEmpty());
    }

    @Test
    public void testListConstructorWithNullEntry() {
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        cpuList.add(null);

        final PhysicalRackBuilder builder = new PhysicalRackBuilder();
        builder.withUuid("Rack");
        boolean caught = false;
        try {
            builder.withHosts(cpuList);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        // Then
        Assert.assertTrue(caught);
    }

    @Test
    public void testListConstructorWithCPUs() {
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        final PhysicalHost mockedCpu = TestPhysicalRack.mockCPU("CPU_A");
        cpuList.add(mockedCpu);

        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withHosts(cpuList).withUuid("Rack")
                .build();
        // Then
        Assert.assertArrayEquals(cpuList.toArray(), rack.getCPUs().toArray());
    }

    @Test
    public void testAddNullCPU() {
        // When
        this.rack.addCPU(null);
        // Then
        Assert.assertTrue(this.rack.getCPUs().isEmpty());
    }

    @Test
    public void testAddNewCPU() {
        final PhysicalHost mockedCpu = TestPhysicalRack.mockCPU("CPU_A");

        // When
        this.rack.addCPU(mockedCpu);
        // Then
        Assert.assertTrue(this.rack.getCPUs().contains(mockedCpu));
    }

    @Test
    public void testAddExistingCPU() {
        final PhysicalHost mockedCpu = TestPhysicalRack.mockCPU("CPU_A");
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        cpuList.add(mockedCpu);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withHosts(cpuList).withUuid("Rack")
                .build();

        // When
        rack.addCPU(mockedCpu);
        // Then
        Assert.assertArrayEquals(cpuList.toArray(), rack.getCPUs().toArray());
    }

    @Test
    public void testRemoveNullCPU() {
        final PhysicalHost mockedCpu = TestPhysicalRack.mockCPU("CPU_A");
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        cpuList.add(mockedCpu);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withHosts(cpuList).withUuid("Rack")
                .build();

        // When
        rack.removeCPU(null);
        // Then
        Assert.assertArrayEquals(cpuList.toArray(), rack.getCPUs().toArray());
    }

    @Test
    public void testRemoveExistingCPU() {
        final PhysicalHost mockedCpu = TestPhysicalRack.mockCPU("CPU_A");
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        cpuList.add(mockedCpu);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withHosts(cpuList).withUuid("Rack")
                .build();

        // When
        rack.removeCPU(mockedCpu);
        // Then
        Assert.assertFalse(rack.getCPUs().contains(mockedCpu));
    }

    @Test
    public void testRemoveNonExistingCPU() {
        final PhysicalHost mockedCpu = TestPhysicalRack.mockCPU("CPU_A");
        final PhysicalHost newCpu = TestPhysicalRack.mockCPU("CPU_B");
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        cpuList.add(mockedCpu);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withHosts(cpuList).withUuid("Rack")
                .build();

        // When
        rack.removeCPU(newCpu);
        // Then
        Assert.assertArrayEquals(cpuList.toArray(), rack.getCPUs().toArray());
    }

    @Test
    public void testAddNullDisk() {
        // When
        this.rack.addDisk(null);
        // Then
        Assert.assertTrue(this.rack.getDisks().isEmpty());
    }

    @Test
    public void testAddNewDisk() {
        final PhysicalStore mockedDisk = TestPhysicalRack.mockDisk("Disk_A");

        // When
        this.rack.addDisk(mockedDisk);
        // Then
        Assert.assertTrue(this.rack.getDisks().contains(mockedDisk));
    }

    @Test
    public void testAddExistingDisk() {
        final PhysicalStore mockedDisk = TestPhysicalRack.mockDisk("Disk_A");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withDisks(DiskList).withUuid("Rack")
                .build();

        // When
        rack.addDisk(mockedDisk);
        // Then
        Assert.assertArrayEquals(DiskList.toArray(), rack.getDisks().toArray());
    }

    @Test
    public void testRemoveNullDisk() {
        final PhysicalStore mockedDisk = TestPhysicalRack.mockDisk("Disk_A");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withDisks(DiskList).withUuid("Rack")
                .build();

        // When
        rack.removeDisk(null);
        // Then
        Assert.assertArrayEquals(DiskList.toArray(), rack.getDisks().toArray());
    }

    @Test
    public void testRemoveExistingDisk() {
        final PhysicalStore mockedDisk = TestPhysicalRack.mockDisk("Disk_A");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withDisks(DiskList).withUuid("Rack")
                .build();

        // When
        rack.removeDisk(mockedDisk);
        // Then
        Assert.assertFalse(rack.getDisks().contains(mockedDisk));
    }

    @Test
    public void testRemoveNonExistingDisk() {
        final PhysicalStore mockedDisk = TestPhysicalRack.mockDisk("Disk_A");
        final PhysicalStore newDisk = TestPhysicalRack.mockDisk("Disk_B");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalRack rack = new PhysicalRackBuilder().withDisks(DiskList).withUuid("Rack")
                .build();

        // When
        rack.removeDisk(newDisk);
        // Then
        Assert.assertArrayEquals(DiskList.toArray(), rack.getDisks().toArray());
    }

    @Test
    public void testParent() {
        final PhysicalDataCenter datacenter = Mockito.mock(PhysicalDataCenter.class);

        // When
        this.rack.setParent(datacenter);
        // Then
        Assert.assertSame(datacenter, this.rack.getParent());
    }

    @Test
    public void testParentBuilder() {
        final PhysicalDataCenter datacenter = Mockito.mock(PhysicalDataCenter.class);

        this.rack = new PhysicalRackBuilder().withParent(datacenter)
                .withUuid(TestPhysicalRack.rackID).build();

        Assert.assertSame(datacenter, this.rack.getParent());
    }

    @SuppressWarnings("unused")
    private Object[] equalsParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.rack, null, false),
                                   JUnitParamsRunner.$(this.rack, this.cloneRack, true),
                                   JUnitParamsRunner.$(this.cloneRack, this.rack, true),
                                   JUnitParamsRunner.$(this.rack, this.otherRack, false),
                                   JUnitParamsRunner.$(this.rack, 1, false),
                                   JUnitParamsRunner.$(this.rack,
                                                       this.rack.getUniqueIdentifier(),
                                                       false));
    }

    @SuppressWarnings("unused")
    private Object[] hashCodeParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.rack, this.cloneRack, true),
                                   JUnitParamsRunner.$(this.rack, this.rack, true),
                                   JUnitParamsRunner.$(this.rack, this.otherRack, false));
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
