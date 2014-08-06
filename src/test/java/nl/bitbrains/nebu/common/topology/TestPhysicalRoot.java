package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalRootBuilder;

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
public class TestPhysicalRoot {

    private final String rootID = "ID";
    private final String otherID = "otherID";

    private PhysicalRoot root;
    private PhysicalRoot cloneRoot;
    private PhysicalRoot otherRoot;

    @Before
    public void setUp() {
        this.root = new PhysicalRootBuilder().withUuid(this.rootID).build();
        this.cloneRoot = new PhysicalRootBuilder().withUuid(this.rootID).build();
        this.otherRoot = new PhysicalRootBuilder().withUuid(this.otherID).build();
    }

    public static PhysicalDataCenter mockDataCenter(final String identifier) {
        final PhysicalDataCenter dc = Mockito.mock(PhysicalDataCenter.class);
        Mockito.when(dc.getUniqueIdentifier()).thenReturn(identifier);
        return dc;
    }

    @Test
    public void testEmptyConstructor() {
        Assert.assertEquals(this.rootID, this.root.getUniqueIdentifier());
        Assert.assertNotNull(this.root.getDataCenters());
    }

    @Test
    public void testIDIsRequired() {
        boolean caught = false;
        try {
            new PhysicalRootBuilder().build();
        } catch (final IllegalStateException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testListConstructorWithNullList() {
        Assert.assertTrue(this.root.getDataCenters().isEmpty());
    }

    @Test
    public void testListConstructorWithNullEntry() {
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        dcList.add(null);
        final PhysicalRootBuilder builder = new PhysicalRootBuilder();
        builder.withUuid("Rack");
        boolean caught = false;
        try {
            builder.withDataCenters(dcList);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        // Then
        Assert.assertTrue(caught);
    }

    @Test
    public void testListConstructorWithDataCenters() {
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        final PhysicalDataCenter mockedDC = TestPhysicalRoot.mockDataCenter("DC_A");
        dcList.add(mockedDC);

        // When
        final PhysicalRoot root = new PhysicalRootBuilder().withDataCenters(dcList)
                .withUuid("Rack").build();
        // Then
        Assert.assertArrayEquals(dcList.toArray(), root.getDataCenters().toArray());
    }

    @Test
    public void testAddNullDataCenter() {
        // When
        this.root.addDataCenter(null);
        // Then
        Assert.assertTrue(this.root.getDataCenters().isEmpty());
    }

    @Test
    public void testAddNewDataCenter() {
        final PhysicalDataCenter mockedDataCenter = TestPhysicalRoot.mockDataCenter("DC_A");

        // When
        this.root.addDataCenter(mockedDataCenter);
        // Then
        Assert.assertTrue(this.root.getDataCenters().contains(mockedDataCenter));
    }

    @Test
    public void testAddExistingDataCenter() {
        final PhysicalDataCenter mockedDataCenter = TestPhysicalRoot.mockDataCenter("DC_A");
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        dcList.add(mockedDataCenter);
        final PhysicalRoot root = new PhysicalRootBuilder().withDataCenters(dcList)
                .withUuid("Rack").build();

        // When
        root.addDataCenter(mockedDataCenter);
        // Then
        Assert.assertArrayEquals(dcList.toArray(), root.getDataCenters().toArray());
    }

    @Test
    public void testRemoveNullDataCenter() {
        final PhysicalDataCenter mockedDataCenter = TestPhysicalRoot.mockDataCenter("DC_A");
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        dcList.add(mockedDataCenter);
        final PhysicalRoot root = new PhysicalRootBuilder().withDataCenters(dcList)
                .withUuid("Rack").build();

        // When
        root.removeDataCenter(null);
        // Then
        Assert.assertArrayEquals(dcList.toArray(), root.getDataCenters().toArray());
    }

    @Test
    public void testRemoveExistingDataCenter() {
        final PhysicalDataCenter mockedDataCenter = TestPhysicalRoot.mockDataCenter("DC_A");
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        dcList.add(mockedDataCenter);
        final PhysicalRoot root = new PhysicalRootBuilder().withDataCenters(dcList)
                .withUuid("Rack").build();

        // When
        root.removeDataCenter(mockedDataCenter);
        // Then
        Assert.assertFalse(root.getDataCenters().contains(mockedDataCenter));
    }

    @Test
    public void testRemoveNonExistingDataCenter() {
        final PhysicalDataCenter mockedDataCenter = TestPhysicalRoot.mockDataCenter("DC_A");
        final PhysicalDataCenter newDataCenter = TestPhysicalRoot.mockDataCenter("DC_B");
        final ArrayList<PhysicalDataCenter> dcList = new ArrayList<PhysicalDataCenter>();
        dcList.add(mockedDataCenter);
        final PhysicalRoot root = new PhysicalRootBuilder().withDataCenters(dcList)
                .withUuid("Rack").build();

        // When
        root.removeDataCenter(newDataCenter);
        // Then
        Assert.assertArrayEquals(dcList.toArray(), root.getDataCenters().toArray());
    }

    @SuppressWarnings("unused")
    private Object[] equalsParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.root, null, false),
                                   JUnitParamsRunner.$(this.root, this.cloneRoot, true),
                                   JUnitParamsRunner.$(this.cloneRoot, this.root, true),
                                   JUnitParamsRunner.$(this.root, this.otherRoot, false),
                                   JUnitParamsRunner.$(this.root, 1, false),
                                   JUnitParamsRunner.$(this.root,
                                                       this.root.getUniqueIdentifier(),
                                                       false));
    }

    @SuppressWarnings("unused")
    private Object[] hashCodeParams() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.root, this.cloneRoot, true),
                                   JUnitParamsRunner.$(this.root, this.root, true),
                                   JUnitParamsRunner.$(this.root, this.otherRoot, false));
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
