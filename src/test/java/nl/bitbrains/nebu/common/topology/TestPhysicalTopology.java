package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalStore;
import nl.bitbrains.nebu.common.topology.PhysicalTopology;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PhysicalRoot.class, PhysicalDataCenter.class, PhysicalRack.class,
        PhysicalStore.class, PhysicalHost.class })
public class TestPhysicalTopology {
    PhysicalRoot root;

    static final String rootID = "Root";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.root = TestPhysicalTopology.newRootMock(TestPhysicalTopology.rootID);
    }

    public static PhysicalRoot newRootMock(final String rootID) {
        final PhysicalRoot res = PowerMockito.mock(PhysicalRoot.class);
        Mockito.when(res.getUniqueIdentifier()).thenReturn(rootID);
        return res;
    }

    public static PhysicalDataCenter mockDataCenter(final String identifier) {
        final PhysicalDataCenter dc = PowerMockito.mock(PhysicalDataCenter.class);
        Mockito.when(dc.getUniqueIdentifier()).thenReturn(identifier);
        return dc;
    }

    public static PhysicalRack mockRack(final String identifier) {
        final PhysicalRack rack = PowerMockito.mock(PhysicalRack.class);
        Mockito.when(rack.getUniqueIdentifier()).thenReturn(identifier);
        return rack;
    }

    public static PhysicalHost mockCPU(final String identifier) {
        final PhysicalHost cpu = PowerMockito.mock(PhysicalHost.class);
        Mockito.when(cpu.getUniqueIdentifier()).thenReturn(identifier);
        return cpu;
    }

    public static PhysicalStore mockStore(final String identifier) {
        final PhysicalStore store = PowerMockito.mock(PhysicalStore.class);
        Mockito.when(store.getUniqueIdentifier()).thenReturn(identifier);
        return store;
    }

    public static List<PhysicalDataCenter> mockDataCentersForTree(final int dataCenters) {
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        for (int i = 0; i < dataCenters; i++) {
            final PhysicalDataCenter newDC = TestPhysicalTopology.mockDataCenter("ID_" + i);
            dcList.add(newDC);
        }
        return dcList;
    }

    public static List<PhysicalRack> mockRacksForTree(final List<PhysicalDataCenter> dataCenters,
            final int racksPerDataCenter) {
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        for (final PhysicalDataCenter dc : dataCenters) {
            final ArrayList<PhysicalRack> dcRackList = new ArrayList<PhysicalRack>();
            for (int i = 0; i < racksPerDataCenter; i++) {
                final PhysicalRack rack = TestPhysicalTopology.mockRack(dc.getUniqueIdentifier()
                        + "_" + i);
                rackList.add(rack);
                dcRackList.add(rack);
            }
            Mockito.when(dc.getRacks()).thenReturn(dcRackList);
        }
        return rackList;
    }

    public static List<PhysicalHost> mockCPUsForTree(final List<PhysicalRack> racks,
            final int cpusPerRack) {
        final ArrayList<PhysicalHost> cpuList = new ArrayList<PhysicalHost>();
        for (final PhysicalRack rack : racks) {
            final ArrayList<PhysicalHost> rackCPUList = new ArrayList<PhysicalHost>();
            for (int i = 0; i < cpusPerRack; i++) {
                final PhysicalHost cpu = TestPhysicalTopology.mockCPU(rack.getUniqueIdentifier()
                        + "_" + i);
                cpuList.add(cpu);
                rackCPUList.add(cpu);
            }
            Mockito.when(rack.getCPUs()).thenReturn(rackCPUList);
        }
        return cpuList;
    }

    public static List<PhysicalStore> mockStoresForTree(final List<PhysicalRack> racks,
            final int storesPerRack) {
        final ArrayList<PhysicalStore> storeList = new ArrayList<PhysicalStore>();
        for (final PhysicalRack rack : racks) {
            final ArrayList<PhysicalStore> rackStoreList = new ArrayList<PhysicalStore>();
            for (int i = 0; i < storesPerRack; i++) {
                final PhysicalStore store = TestPhysicalTopology.mockStore(rack
                        .getUniqueIdentifier() + "_" + i);
                storeList.add(store);
                rackStoreList.add(store);
            }
            Mockito.when(rack.getDisks()).thenReturn(rackStoreList);
        }
        return storeList;
    }

    @Test
    public void testConstructor() {
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // Then
        Assert.assertEquals(topology.getRoot(), this.root);
    }

    @Test
    public void testGetDataCenters() {
        final List<PhysicalDataCenter> dcList = TestPhysicalTopology.mockDataCentersForTree(3);
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        Mockito.when(this.root.getDataCenters()).thenReturn(dcList);
        // Then
        Assert.assertEquals(dcList, topology.getDataCenters());
    }

    @Test
    public void testGetRacks() {
        final List<PhysicalDataCenter> dcList = TestPhysicalTopology.mockDataCentersForTree(3);
        final List<PhysicalRack> rackList = TestPhysicalTopology.mockRacksForTree(dcList, 3);
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        Mockito.when(this.root.getDataCenters()).thenReturn(dcList);
        // Then
        Assert.assertEquals(rackList, topology.getRacks());
    }

    @Test
    public void testGetCPUs() {
        final List<PhysicalDataCenter> dcList = TestPhysicalTopology.mockDataCentersForTree(3);
        final List<PhysicalRack> rackList = TestPhysicalTopology.mockRacksForTree(dcList, 3);
        final List<PhysicalHost> cpuList = TestPhysicalTopology.mockCPUsForTree(rackList, 3);
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        Mockito.when(this.root.getDataCenters()).thenReturn(dcList);
        // Then
        Assert.assertEquals(cpuList, topology.getCPUs());
    }

    @Test
    public void testGetStores() {
        final List<PhysicalDataCenter> dcList = TestPhysicalTopology.mockDataCentersForTree(3);
        final List<PhysicalRack> rackList = TestPhysicalTopology.mockRacksForTree(dcList, 3);
        final List<PhysicalStore> storeList = TestPhysicalTopology.mockStoresForTree(rackList, 3);
        final List<PhysicalHost> hostList = TestPhysicalTopology.mockCPUsForTree(rackList, 1);

        final PhysicalStore extraStore = TestPhysicalTopology.mockStore("extra");
        final List<PhysicalStore> extraStoreList = new ArrayList<PhysicalStore>();
        extraStoreList.add(extraStore);
        Mockito.when(hostList.get(0).getDisks()).thenReturn(extraStoreList);
        storeList.add(extraStore);
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        Mockito.when(this.root.getDataCenters()).thenReturn(dcList);
        // Then
        Assert.assertEquals(storeList, topology.getStores());
    }

    @Test
    public void testAddDataCenterConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalDataCenter dc = TestPhysicalTopology.mockDataCenter("DC_A");
        // When
        topology.addDataCenter(dc);
        // Then
        Mockito.verify(this.root).addDataCenter(dc);
        Mockito.verify(dc).setParent(this.root);
    }

    @Test
    public void testRemoveDataCenterConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalDataCenter dc = TestPhysicalTopology.mockDataCenter("DC_A");
        // When
        topology.removeDataCenter(dc);
        // Then
        Mockito.verify(this.root).removeDataCenter(dc);
        Mockito.verify(dc).setParent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDataCenter() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // When
        topology.addDataCenter(null);
        // Then
        // ... nothing needs to happen, but no Exceptions may be thrown
        Assert.fail();
    }

    @Test
    public void testAddRackConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalDataCenter dc = TestPhysicalTopology.mockDataCenter("DC_A");
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        // When
        topology.addRackToDataCenter(rack, dc);
        // Then
        Mockito.verify(dc).addRack(rack);
        Mockito.verify(rack).setParent(dc);
    }

    @Test
    public void testRemoveRackConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalDataCenter dc = TestPhysicalTopology.mockDataCenter("DC_A");
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        // When
        topology.removeRackFromDataCenter(rack, dc);
        // Then
        Mockito.verify(dc).removeRack(rack);
        Mockito.verify(rack).setParent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullRack() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalDataCenter dc = TestPhysicalTopology.mockDataCenter("DC_A");
        // When
        topology.addRackToDataCenter(null, dc);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRackToNull() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        // When
        topology.addRackToDataCenter(rack, null);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test
    public void testAddCPUConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        final PhysicalHost cpu = TestPhysicalTopology.mockCPU("CPU_A");
        // When
        topology.addCPUToRack(cpu, rack);
        // Then
        Mockito.verify(rack).addCPU(cpu);
        Mockito.verify(cpu).setParent(rack);
    }

    @Test
    public void testRemoveCPUConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        final PhysicalHost cpu = TestPhysicalTopology.mockCPU("CPU_A");
        // When
        topology.removeCPUFromRack(cpu, rack);
        // Then
        Mockito.verify(rack).removeCPU(cpu);
        Mockito.verify(cpu).setParent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullCPU() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        // When
        topology.addCPUToRack(null, rack);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCPUToNull() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalHost cpu = TestPhysicalTopology.mockCPU("CPUA");
        // When
        topology.addCPUToRack(cpu, null);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test
    public void testAddDiskToRackConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        final PhysicalStore store = TestPhysicalTopology.mockStore("STORE_A");
        // When
        topology.addDiskToRack(store, rack);
        // Then
        Mockito.verify(rack).addDisk(store);
        Mockito.verify(store).setParent(rack);
    }

    @Test
    public void testRemoveDiskFromRackConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("Rack_A");
        final PhysicalStore store = TestPhysicalTopology.mockStore("STORE_A");
        // When
        topology.removeDiskFromRack(store, rack);
        // Then
        Mockito.verify(rack).removeDisk(store);
        Mockito.verify(store).setParent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDiskToRack() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRack rack = TestPhysicalTopology.mockRack("STORE_A");
        // When
        topology.addDiskToRack(null, rack);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDiskToRackToNull() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalStore store = TestPhysicalTopology.mockStore("STORE_A");
        // When
        topology.addDiskToRack(store, null);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test
    public void testAddDiskToHostConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalHost host = TestPhysicalTopology.mockCPU("Host_A");
        final PhysicalStore store = TestPhysicalTopology.mockStore("STORE_A");
        // When
        topology.addDiskToHost(store, host);
        // Then
        Mockito.verify(host).addDisk(store);
        Mockito.verify(store).setParent(host);
    }

    @Test
    public void testRemoveDiskFromHostConsistency() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalHost host = TestPhysicalTopology.mockCPU("Host_A");
        final PhysicalStore store = TestPhysicalTopology.mockStore("STORE_A");
        // When
        topology.removeDiskFromHost(store, host);
        // Then
        Mockito.verify(host).removeDisk(store);
        Mockito.verify(store).setParent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDiskToHost() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalHost host = TestPhysicalTopology.mockCPU("Host_A");
        // When
        topology.addDiskToHost(null, host);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDiskToHostToNull() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalStore store = TestPhysicalTopology.mockStore("STORE_A");
        // When
        topology.addDiskToHost(store, null);
        // Then
        // ... an Exception should have been thrown
        Assert.fail();
    }

    @Test
    public void testNotEqualsNull() {
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // Then
        Assert.assertNotEquals(topology, null);
    }

    @Test
    public void testEqualsSelf() {
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // Then
        Assert.assertEquals(topology, topology);
    }

    @Test
    public void testObjectEqualsCopy() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // When
        final PhysicalTopology topologyCopy = new PhysicalTopology(this.root);
        // Then
        Assert.assertEquals(topologyCopy, topology);
    }

    @Test
    public void testCopyEqualsObject() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // When
        final PhysicalTopology topologyCopy = new PhysicalTopology(this.root);
        // Then
        Assert.assertEquals(topology, topologyCopy);
    }

    @Test
    public void testNotEqualsOther() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalRoot otherRoot = Mockito.mock(PhysicalRoot.class);
        Mockito.when(otherRoot.getUniqueIdentifier()).thenReturn(TestPhysicalTopology.rootID + "_");
        // When
        final PhysicalTopology other = new PhysicalTopology(otherRoot);
        // Then
        Assert.assertNotEquals(other, topology);
    }

    @Test
    public void testHashCodeConsistency() {
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        // Then
        Assert.assertEquals(topology.hashCode(), topology.hashCode());
    }

    @Test
    public void testHashCodeCopy() {
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalTopology topologyCopy = new PhysicalTopology(this.root);
        // Then
        Assert.assertEquals(topology.hashCode(), topologyCopy.hashCode());
    }

    @Test
    public void testHashCodeDifferentID() {
        final PhysicalRoot otherRoot = Mockito.mock(PhysicalRoot.class);
        Mockito.when(otherRoot.getUniqueIdentifier()).thenReturn(TestPhysicalTopology.rootID + "_");
        // When
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final PhysicalTopology otherTopology = new PhysicalTopology(otherRoot);
        Assert.assertNotEquals(topology.hashCode(), otherTopology.hashCode());
    }

    @Test
    public void testHasCPUByIDEmptyTreeNotFound() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final List<PhysicalDataCenter> dc = new ArrayList<PhysicalDataCenter>();
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        Assert.assertFalse(topology.hasCPUByID("this does not exist"));
    }

    @Test
    public void testHasCPUByIDFilledTreeNotFound() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(1);
        final List<PhysicalRack> racks = TestPhysicalTopology.mockRacksForTree(dc, 2);
        final List<PhysicalHost> hosts = TestPhysicalTopology.mockCPUsForTree(racks, 2);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        Assert.assertFalse(topology.hasCPUByID("this does not exist"));
    }

    @Test
    public void testHasCPUByIDFilledTreeFound() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(1);
        final List<PhysicalRack> racks = TestPhysicalTopology.mockRacksForTree(dc, 2);
        final List<PhysicalHost> hosts = TestPhysicalTopology.mockCPUsForTree(racks, 2);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        Assert.assertTrue(topology.hasCPUByID("ID_0_1_1"));
    }

    @Test
    public void testGetCPUByIDEmptyTreeNotFound() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final List<PhysicalDataCenter> dc = new ArrayList<PhysicalDataCenter>();
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        boolean caught = false;
        try {
            topology.getCPUByID("this does not exist");
        } catch (final NoSuchElementException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testGetCPUByIDFilledTreeNotFound() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(1);
        final List<PhysicalRack> racks = TestPhysicalTopology.mockRacksForTree(dc, 2);
        final List<PhysicalHost> hosts = TestPhysicalTopology.mockCPUsForTree(racks, 2);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        boolean caught = false;
        try {
            topology.getCPUByID("this does not exist");
        } catch (final NoSuchElementException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testGetCPUByIDFilledTreeFound() {
        final PhysicalTopology topology = new PhysicalTopology(this.root);
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(1);
        final List<PhysicalRack> racks = TestPhysicalTopology.mockRacksForTree(dc, 2);
        final List<PhysicalHost> hosts = TestPhysicalTopology.mockCPUsForTree(racks, 2);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        Assert.assertNotNull(topology.getCPUByID("ID_0_1_1"));
    }
}
