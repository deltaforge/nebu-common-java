package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenterBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;

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
public class TestPhysicalDataCenter {

    private final String dcID = "DC";
    private final String otherdcID = "otherDC";

    private PhysicalDataCenter dc;
    private PhysicalDataCenter copydc;
    private PhysicalDataCenter otherdc;

    @Before
    public void setUp() {
        this.dc = new PhysicalDataCenterBuilder().withUuid(this.dcID).build();
        this.copydc = new PhysicalDataCenterBuilder().withUuid(this.dcID).build();
        this.otherdc = new PhysicalDataCenterBuilder().withUuid(this.otherdcID).build();
    }

    public static PhysicalRack mockRack(final String identifier) {
        final PhysicalRack rack = Mockito.mock(PhysicalRack.class);
        Mockito.when(rack.getUniqueIdentifier()).thenReturn(identifier);
        return rack;
    }

    @Test
    public void testEmptyConstructor() {
        Assert.assertEquals(this.dcID, this.dc.getUniqueIdentifier());
        Assert.assertNotNull(this.dc.getRacks());
    }

    @Test
    public void testIDIsRequired() {
        boolean caught = false;
        try {
            new PhysicalDataCenterBuilder().build();
        } catch (final IllegalStateException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testListConstructorWithNullList() {
        Assert.assertTrue(this.dc.getRacks().isEmpty());
    }

    @Test
    public void testListConstructorWithNullEntry() {
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        rackList.add(null);
        final PhysicalDataCenterBuilder builder = new PhysicalDataCenterBuilder();
        builder.withUuid(this.dcID);
        boolean caught = false;
        try {
            builder.withRacks(rackList);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testListConstructorWithRacks() {
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        final PhysicalRack mockedRack = TestPhysicalDataCenter.mockRack("Rack_A");
        rackList.add(mockedRack);
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withRacks(rackList)
                .withUuid(this.dcID).build();
        Assert.assertArrayEquals(rackList.toArray(), dc.getRacks().toArray());
    }

    @Test
    public void testAddNullRack() {
        // When
        this.dc.addRack(null);
        // Then
        Assert.assertTrue(this.dc.getRacks().isEmpty());
    }

    @Test
    public void testAddNewRack() {
        final PhysicalRack mockedRack = TestPhysicalDataCenter.mockRack("Rack_A");
        // When
        this.dc.addRack(mockedRack);
        // Then
        Assert.assertTrue(this.dc.getRacks().contains(mockedRack));
    }

    @Test
    public void testAddExistingRack() {
        final PhysicalRack mockedRack = TestPhysicalDataCenter.mockRack("Rack_A");
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        rackList.add(mockedRack);
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withRacks(rackList)
                .withUuid(this.dcID).build();

        // When
        dc.addRack(mockedRack);
        // Then
        Assert.assertArrayEquals(rackList.toArray(), dc.getRacks().toArray());
    }

    @Test
    public void testRemoveNullRack() {
        final PhysicalRack mockedRack = TestPhysicalDataCenter.mockRack("Rack_A");
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        rackList.add(mockedRack);
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withRacks(rackList)
                .withUuid(this.dcID).build();

        // When
        dc.removeRack(null);
        // Then
        Assert.assertArrayEquals(rackList.toArray(), dc.getRacks().toArray());
    }

    @Test
    public void testRemoveExistingRack() {
        final PhysicalRack mockedRack = TestPhysicalDataCenter.mockRack("Rack_A");
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        rackList.add(mockedRack);
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withRacks(rackList)
                .withUuid(this.dcID).build();

        // When
        dc.removeRack(mockedRack);
        // Then
        Assert.assertFalse(dc.getRacks().contains(mockedRack));
    }

    @Test
    public void testRemoveNonExistingRack() {
        final PhysicalRack mockedRack = TestPhysicalDataCenter.mockRack("Rack_A");
        final PhysicalRack newRack = TestPhysicalDataCenter.mockRack("Rack_B");
        final ArrayList<PhysicalRack> rackList = new ArrayList<PhysicalRack>();
        rackList.add(mockedRack);
        final PhysicalDataCenter dc = new PhysicalDataCenterBuilder().withRacks(rackList)
                .withUuid(this.dcID).build();

        // When
        dc.removeRack(newRack);
        // Then
        Assert.assertArrayEquals(rackList.toArray(), dc.getRacks().toArray());
    }

    @Test
    public void testParent() {
        final PhysicalRoot root = Mockito.mock(PhysicalRoot.class);

        // When
        this.dc.setParent(root);
        // Then
        Assert.assertSame(root, this.dc.getParent());
    }

    @Test
    public void testParentBuilder() {
        final PhysicalRoot root = Mockito.mock(PhysicalRoot.class);

        // When
        this.dc = new PhysicalDataCenterBuilder().withParent(root).withUuid(this.dcID).build();
        // Then
        Assert.assertSame(root, this.dc.getParent());
    }

    @SuppressWarnings("unused")
    private Object[] equalsParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.dc, null, false),
                                   JUnitParamsRunner.$(this.dc, this.copydc, true),
                                   JUnitParamsRunner.$(this.copydc, this.dc, true),
                                   JUnitParamsRunner.$(this.dc, this.otherdc, false),
                                   JUnitParamsRunner.$(this.dc, 1, false),
                                   JUnitParamsRunner.$(this.dc,
                                                       this.dc.getUniqueIdentifier(),
                                                       false));
    }

    @SuppressWarnings("unused")
    private Object[] hashCodeParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.dc, this.copydc, true),
                                   JUnitParamsRunner.$(this.dc, this.dc, true),
                                   JUnitParamsRunner.$(this.dc, this.otherdc, false));
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
