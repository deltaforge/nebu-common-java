package nl.bitbrains.nebu.common.topology;

import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenterBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalHostBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRackBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalRootBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalStore;
import nl.bitbrains.nebu.common.topology.PhysicalStoreBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalTopology;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the copy constructor etc of the physicalTopology without mocks.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
@RunWith(JUnitParamsRunner.class)
public class ITPhysicalTopology {

    private PhysicalTopology topology1;

    public static boolean deepEquals(final PhysicalTopology one, final PhysicalTopology two) {
        return ITPhysicalTopology.deepEquals(one.getRoot(), two.getRoot());
    }

    private static boolean deepEquals(final PhysicalRoot root, final PhysicalRoot root2) {
        if (!root.equals(root2) || root.getDataCenters().size() != root2.getDataCenters().size()) {
            return false;
        }
        final List<PhysicalDataCenter> two = root2.getDataCenters();
        for (final PhysicalDataCenter d : root.getDataCenters()) {
            final int x = two.indexOf(d);
            if (x < 0 || !ITPhysicalTopology.deepEquals(d, two.get(x))) {
                return false;
            }
        }
        return true;
    }

    private static boolean deepEquals(final PhysicalDataCenter dc1, final PhysicalDataCenter dc2) {
        if (!dc1.equals(dc2) || dc1.getRacks().size() != dc2.getRacks().size()) {
            return false;
        }
        final List<PhysicalRack> two = dc2.getRacks();
        for (final PhysicalRack d : dc1.getRacks()) {
            final int x = two.indexOf(d);
            if (x < 0 || !ITPhysicalTopology.deepEquals(d, two.get(x))) {
                return false;
            }
        }
        return true;
    }

    private static boolean deepEquals(final PhysicalRack r1, final PhysicalRack r2) {
        if (!r1.equals(r2) || r1.getDisks().size() != r2.getDisks().size()
                || r1.getCPUs().size() != r2.getCPUs().size()) {
            return false;
        }
        final List<PhysicalHost> two = r2.getCPUs();
        for (final PhysicalHost d : r1.getCPUs()) {
            final int x = two.indexOf(d);
            if (x < 0 || !ITPhysicalTopology.deepEquals(d, two.get(x))) {
                return false;
            }
        }

        final List<PhysicalStore> disks2 = r2.getDisks();
        for (final PhysicalStore d : r1.getDisks()) {
            final int x = disks2.indexOf(d);
            if (x < 0 || !ITPhysicalTopology.deepEquals(d, disks2.get(x))) {
                return false;
            }
        }
        return true;
    }

    private static boolean deepEquals(final PhysicalHost h1, final PhysicalHost h2) {
        if (!h1.equals(h2) || h1.getDisks().size() != h2.getDisks().size()) {
            return false;
        }
        final List<PhysicalStore> disks = h2.getDisks();
        for (final PhysicalStore d : h1.getDisks()) {
            final int x = disks.indexOf(d);
            if (x < 0 || !ITPhysicalTopology.deepEquals(d, disks.get(x))) {
                return false;
            }
        }
        return true;
    }

    private static boolean deepEquals(final PhysicalStore s1, final PhysicalStore s2) {
        if (!s1.equals(s2)) {
            return false;
        }
        return s1.getCapacity() == s2.getCapacity();
    }

    private static PhysicalTopology createTopology(final int numDcs, final int numRacks,
            final int numCpus, final int numNetworkDisks, final int numLocalDisks) {
        final PhysicalTopology result = new PhysicalTopology();
        for (int i = 0; i < numDcs; i++) {
            final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withUuid("dc" + i)
                    .build();
            result.addDataCenter(dc);
            for (int j = 0; j < numRacks; j++) {
                final PhysicalRack rack = new PhysicalRackBuilder().withUuid("rack" + i + "-" + j)
                        .build();
                result.addRackToDataCenter(rack, dc);
                for (int k = 0; k < numCpus; k++) {
                    final PhysicalHost host = new PhysicalHostBuilder().withUuid("host" + i + "-"
                            + j + "-" + k).build();
                    result.addCPUToRack(host, rack);
                    for (int l = 0; l < numLocalDisks; l++) {
                        final PhysicalStore disk = new PhysicalStoreBuilder().withUuid("disk" + i
                                + "-" + j + "-" + k + "-" + l).build();
                        result.addDiskToHost(disk, host);
                    }
                }
                for (int k = 0; k < numLocalDisks; k++) {
                    final PhysicalStore disk = new PhysicalStoreBuilder().withUuid("disk" + i
                            + "-" + j + "-" + k).build();
                    result.addDiskToRack(disk, rack);
                }
            }
        }
        return result;
    }

    @Test
    public void testEmptyConstructor() {
        final PhysicalTopology topology = new PhysicalTopology();
        Assert.assertNotNull(topology.getRoot());
    }

    private Object[] copyConstructorParams() {
        return JUnitParamsRunner.$(JUnitParamsRunner.$(0, 0, 0, 0, 0),
                                   JUnitParamsRunner.$(1, 0, 0, 0, 0),
                                   JUnitParamsRunner.$(2, 0, 0, 0, 0),
                                   JUnitParamsRunner.$(2, 1, 0, 0, 0),
                                   JUnitParamsRunner.$(2, 2, 0, 0, 0),
                                   JUnitParamsRunner.$(3, 2, 1, 0, 0),
                                   JUnitParamsRunner.$(3, 2, 2, 0, 0),
                                   JUnitParamsRunner.$(4, 3, 2, 1, 0),
                                   JUnitParamsRunner.$(4, 3, 2, 2, 0),
                                   JUnitParamsRunner.$(4, 3, 2, 0, 1),
                                   JUnitParamsRunner.$(4, 3, 2, 0, 2),
                                   JUnitParamsRunner.$(5, 4, 3, 2, 1),
                                   JUnitParamsRunner.$(5, 4, 3, 2, 2),
                                   JUnitParamsRunner.$(1, 2, 3, 4, 5),
                                   JUnitParamsRunner.$(3, 1, 4, 2, 1),
                                   JUnitParamsRunner.$(1, 1, 1, 0, 1),
                                   JUnitParamsRunner.$(1, 1, 1, 1, 0),
                                   JUnitParamsRunner.$(1, 1, 1, 1, 1));
    }

    @Test
    @Parameters(method = "copyConstructorParams")
    public void testCopyConstructor(final int numDcs, final int numRacks, final int numCpus,
            final int numNetworkDisks, final int numLocalDisks) {
        final PhysicalTopology original = ITPhysicalTopology.createTopology(numDcs,
                                                                            numRacks,
                                                                            numCpus,
                                                                            numNetworkDisks,
                                                                            numLocalDisks);
        final PhysicalTopology copy = new PhysicalTopology(original);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(original, copy));
    }

    @Test
    public void testMergeIntoEmptyTree() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(0, 0, 0, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeWithEmptyTree() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(0, 0, 0, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(filledTree, origin);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeTwoIdenticals() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(origin, merged));
    }

    @Test
    public void testMergeSubset() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(2, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(filledTree, origin);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeDifferentRoots() {
        final PhysicalTopology origin = new PhysicalTopology(new PhysicalRootBuilder()
                .withUuid("different").build());
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(origin, merged));
    }

    @Test
    public void testMergeOnlyDataCentres() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(0, 0, 0, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(4, 0, 0, 0, 0);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeOnlyRacks() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 0, 0, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 0, 0, 0);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeOnlyHosts() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 0, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 0, 0);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeOnlyLocalDisks() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 3, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 0, 4);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeOnlyNetworkDisks() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 0, 0, 0);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 0, 5, 0);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeLocalDisks() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 3, 4, 2);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

    @Test
    public void testMergeLocalDisks2() {
        final PhysicalTopology origin = ITPhysicalTopology.createTopology(1, 2, 2, 2, 3);
        final PhysicalTopology filledTree = ITPhysicalTopology.createTopology(1, 2, 3, 4, 5);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(origin, filledTree);
        Assert.assertTrue(ITPhysicalTopology.deepEquals(filledTree, merged));
    }

}
