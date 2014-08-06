package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalHostBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalStore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(JUnitParamsRunner.class)
public class TestPhysicalHost {

    private final String cpuID = "Some CPU ID";
    private final String otherCpuID = "Other CPU ID";

    private PhysicalHost cpu;
    private PhysicalHost cloneCPU;
    private PhysicalHost otherCPU;

    @Before
    public void setUp() {
        this.cpu = new PhysicalHostBuilder().withUuid(this.cpuID).build();
        this.cloneCPU = new PhysicalHostBuilder().withUuid(this.cpuID).build();
        this.otherCPU = new PhysicalHostBuilder().withUuid(this.otherCpuID).build();
    }

    public static PhysicalStore mockDisk(final String identifier) {
        final PhysicalStore store = Mockito.mock(PhysicalStore.class);
        Mockito.when(store.getUniqueIdentifier()).thenReturn(identifier);
        return store;
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(this.cpuID, this.cpu.getUniqueIdentifier());
    }

    @Test
    public void testIDIsRequired() {
        boolean caught = false;
        try {
            new PhysicalHostBuilder().build();
        } catch (final IllegalStateException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testParent() {
        final PhysicalRack rack = Mockito.mock(PhysicalRack.class);
        this.cpu.setParent(rack);
        Assert.assertEquals(rack, this.cpu.getParent());
    }

    @Test
    public void testParentBuilder() {
        final PhysicalRack rack = Mockito.mock(PhysicalRack.class);
        this.cpu = new PhysicalHostBuilder().withParent(rack).withUuid(this.cpuID).build();
        Assert.assertEquals(rack, this.cpu.getParent());
    }

    @Test
    public void testAddNullDisk() {
        // When
        this.cpu.addDisk(null);
        // Then
        Assert.assertTrue(this.cpu.getDisks().isEmpty());
    }

    @Test
    public void testAddNewDisk() {
        final PhysicalStore mockedDisk = TestPhysicalHost.mockDisk("Disk_A");

        // When
        this.cpu.addDisk(mockedDisk);
        // Then
        Assert.assertTrue(this.cpu.getDisks().contains(mockedDisk));
    }

    @Test
    public void testAddExistingDisk() {
        final PhysicalStore mockedDisk = TestPhysicalHost.mockDisk("Disk_A");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalHost cpu = new PhysicalHostBuilder().withDisks(DiskList).withUuid("Host")
                .build();

        // When
        cpu.addDisk(mockedDisk);
        // Then
        Assert.assertArrayEquals(DiskList.toArray(), cpu.getDisks().toArray());
    }

    @Test
    public void testRemoveNullDisk() {
        final PhysicalStore mockedDisk = TestPhysicalHost.mockDisk("Disk_A");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalHost cpu = new PhysicalHostBuilder().withDisks(DiskList).withUuid("Host")
                .build();

        // When
        cpu.removeDisk(null);
        // Then
        Assert.assertArrayEquals(DiskList.toArray(), cpu.getDisks().toArray());
    }

    @Test
    public void testRemoveExistingDisk() {
        final PhysicalStore mockedDisk = TestPhysicalHost.mockDisk("Disk_A");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalHost cpu = new PhysicalHostBuilder().withDisks(DiskList).withUuid("Host")
                .build();

        // When
        cpu.removeDisk(mockedDisk);
        // Then
        Assert.assertFalse(cpu.getDisks().contains(mockedDisk));
    }

    @Test
    public void testRemoveNonExistingDisk() {
        final PhysicalStore mockedDisk = TestPhysicalHost.mockDisk("Disk_A");
        final PhysicalStore newDisk = TestPhysicalHost.mockDisk("Disk_B");
        final ArrayList<PhysicalStore> DiskList = new ArrayList<PhysicalStore>();
        DiskList.add(mockedDisk);
        // When
        final PhysicalHost cpu = new PhysicalHostBuilder().withDisks(DiskList).withUuid("Host")
                .build();

        // When
        cpu.removeDisk(newDisk);
        // Then
        Assert.assertArrayEquals(DiskList.toArray(), cpu.getDisks().toArray());
    }

    private Object[] equalsParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.cpu, null, false),
                                   JUnitParamsRunner.$(this.cpu, this.cloneCPU, true),
                                   JUnitParamsRunner.$(this.cloneCPU, this.cpu, true),
                                   JUnitParamsRunner.$(this.cpu, this.otherCPU, false),
                                   JUnitParamsRunner.$(this.cpu, 1, false),
                                   JUnitParamsRunner.$(this.cpu,
                                                       this.cpu.getUniqueIdentifier(),
                                                       false));
    }

    private Object[] hashCodeParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.cpu, this.cloneCPU, true),
                                   JUnitParamsRunner.$(this.cpu, this.cpu, true),
                                   JUnitParamsRunner.$(this.cpu, this.otherCPU, false));
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
