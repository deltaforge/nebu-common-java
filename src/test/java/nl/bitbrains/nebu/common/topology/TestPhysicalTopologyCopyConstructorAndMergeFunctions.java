package nl.bitbrains.nebu.common.topology;

import java.util.List;

import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalRootBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalTopology;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
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
        PhysicalHost.class, PhysicalTopology.class })
public class TestPhysicalTopologyCopyConstructorAndMergeFunctions {

    private PhysicalRoot root;
    private PhysicalRoot root2;

    @Mock
    private PhysicalTopology one;
    @Mock
    private PhysicalTopology two;

    @Mock
    private PhysicalTopology clonedOne;
    @Mock
    private PhysicalTopology clonedTwo;

    private PhysicalRoot createdRoot;
    private PhysicalDataCenter createdDC;
    private PhysicalRack createdRack;
    private PhysicalHost createdCPU;

    private final int numDC = 2;
    private final int numRack = 3;
    private final int numCpu = 4;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.createdRoot = PowerMockito.mock(PhysicalRoot.class);
        this.createdDC = PowerMockito.mock(PhysicalDataCenter.class);
        this.createdRack = PowerMockito.mock(PhysicalRack.class);
        this.createdCPU = PowerMockito.mock(PhysicalHost.class);
    }

    private void setUpForCopyConstructor() throws Exception {
        this.root = PowerMockito.mock(PhysicalRoot.class);
        this.setUpConstructorMocks();
        Mockito.when(this.root.getUniqueIdentifier()).thenReturn(TestPhysicalTopology.rootID);
    }

    private void setUpConstructorMocks() throws Exception {
        PowerMockito.whenNew(PhysicalRoot.class).withParameterTypes(PhysicalRoot.class)
                .withArguments(Matchers.any(PhysicalRoot.class)).thenReturn(this.createdRoot);
        PowerMockito.whenNew(PhysicalDataCenter.class).withParameterTypes(PhysicalDataCenter.class)
                .withArguments(Matchers.any(PhysicalDataCenter.class)).thenReturn(this.createdDC);
        PowerMockito.whenNew(PhysicalRack.class).withParameterTypes(PhysicalRack.class)
                .withArguments(Matchers.any(PhysicalRack.class)).thenReturn(this.createdRack);
        PowerMockito.whenNew(PhysicalHost.class).withParameterTypes(PhysicalHost.class)
                .withArguments(Matchers.any(PhysicalHost.class)).thenReturn(this.createdCPU);
    }

    private void setUpForMergeFunctions() throws Exception {
        this.root = new PhysicalRootBuilder().withUuid("one").build();
        if (this.root2 == null) {
            this.root2 = new PhysicalRootBuilder().withUuid("one").build();
        }
        Mockito.when(this.one.getRoot()).thenReturn(this.root);
        Mockito.when(this.two.getRoot()).thenReturn(this.root2);

        PowerMockito.whenNew(PhysicalTopology.class).withArguments(this.one)
                .thenReturn(this.clonedOne);
        PowerMockito.whenNew(PhysicalTopology.class).withArguments(this.two)
                .thenReturn(this.clonedTwo);
        PowerMockito.when(this.clonedOne.getRoot()).thenReturn(this.createdRoot);

        this.setUpConstructorMocks();
    }

    @Test
    public void testEmptyTreeCopy() throws Exception {
        this.setUpForCopyConstructor();
        final PhysicalTopology origin = new PhysicalTopology(this.root);
        final PhysicalTopology copy = new PhysicalTopology(origin);
        PowerMockito.verifyNew(PhysicalRoot.class);
    }

    @Test
    public void testTreeWithDataCenters() throws Exception {
        this.setUpForCopyConstructor();
        final int numDC = 2;
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(numDC);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        final PhysicalTopology origin = new PhysicalTopology(this.root);
        final PhysicalTopology copy = new PhysicalTopology(origin);

        PowerMockito.verifyNew(PhysicalDataCenter.class, Mockito.times(numDC));
    }

    @Test
    public void testTreeWithRacks() throws Exception {
        this.setUpForCopyConstructor();
        final int numDC = 2;
        final int numRacks = 3;
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(numDC);
        final List<PhysicalRack> rack = TestPhysicalTopology.mockRacksForTree(dc, numRacks);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        final PhysicalTopology origin = new PhysicalTopology(this.root);
        final PhysicalTopology copy = new PhysicalTopology(origin);

        PowerMockito.verifyNew(PhysicalRack.class, Mockito.times(numDC * numRacks));
    }

    @Test
    public void testTreeWithCPUs() throws Exception {
        this.setUpForCopyConstructor();
        final int numDC = 2;
        final int numRacks = 3;
        final int numCpus = 4;
        final List<PhysicalDataCenter> dc = TestPhysicalTopology.mockDataCentersForTree(numDC);
        final List<PhysicalRack> rack = TestPhysicalTopology.mockRacksForTree(dc, numRacks);
        final List<PhysicalHost> cpu = TestPhysicalTopology.mockCPUsForTree(rack, numCpus);
        Mockito.when(this.root.getDataCenters()).thenReturn(dc);
        final PhysicalTopology origin = new PhysicalTopology(this.root);
        final PhysicalTopology copy = new PhysicalTopology(origin);

        PowerMockito.verifyNew(PhysicalHost.class, Mockito.times(numDC * numRacks * numCpus));
    }

    @Test
    public void testMergeTreeDifferentRootsVerifyRootsAreUsed() throws Exception {
        this.root2 = new PhysicalRootBuilder().withUuid("two").build();
        this.setUpForMergeFunctions();

        final PhysicalTopology merged = PhysicalTopology.mergeTree(this.one, this.two);
        Mockito.verify(this.one).getRoot();
        Mockito.verify(this.two).getRoot();
    }

    @Test
    public void testMergeTreeDifferentRootsVerifyOnlyFirstArgumentIsCopied() throws Exception {
        this.root2 = new PhysicalRootBuilder().withUuid("two").build();
        this.setUpForMergeFunctions();

        final PhysicalTopology merged = PhysicalTopology.mergeTree(this.one, this.two);
        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(1)).withArguments(this.one);
        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(0)).withArguments(this.two);
    }

    @Test
    public void testMergeTreeSameRootsNoCenters() throws Exception {
        this.setUpForMergeFunctions();
        final PhysicalTopology merged = PhysicalTopology.mergeTree(this.one, this.two);

        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(1)).withArguments(this.one);
        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(0)).withArguments(this.two);
    }

    @Test
    public void testMergeTreeSameRootsCentersInOne() throws Exception {
        this.setUpForMergeFunctions();
        final List<PhysicalDataCenter> dcs = TestPhysicalTopology
                .mockDataCentersForTree(this.numDC);
        Mockito.when(this.one.getDataCenters()).thenReturn(dcs);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(this.one, this.two);

        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(1)).withArguments(this.one);
        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(0)).withArguments(this.two);
    }

    @Test
    public void testMergeTreeSameRootsCentersInTwo() throws Exception {
        this.setUpForMergeFunctions();
        final List<PhysicalDataCenter> dcs = TestPhysicalTopology
                .mockDataCentersForTree(this.numDC);
        Mockito.when(this.two.getDataCenters()).thenReturn(dcs);
        final PhysicalTopology merged = PhysicalTopology.mergeTree(this.one, this.two);

        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(1)).withArguments(this.one);
        PowerMockito.verifyNew(PhysicalTopology.class, Mockito.times(0)).withArguments(this.two);
        PowerMockito.verifyNew(PhysicalDataCenter.class, Mockito.times(this.numDC))
                .withArguments(Matchers.any(PhysicalDataCenter.class));
    }
}
