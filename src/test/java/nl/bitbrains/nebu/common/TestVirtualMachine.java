package nl.bitbrains.nebu.common;

import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.bitbrains.nebu.common.VirtualMachine;
import nl.bitbrains.nebu.common.VirtualMachineBuilder;
import nl.bitbrains.nebu.common.VirtualMachine.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TestVirtualMachine {

    private final String vmID = "Some ID";
    private final String notVmID = "Other ID";
    private final String notHostname = "notHostname";
    private final Status notStatus = Status.ON;
    private final String host = "notHost";
    private final String store = "notStore";
    private final List<String> list = new ArrayList<String>();

    private VirtualMachine thisVM;
    private VirtualMachine sameVM;
    private VirtualMachine notSameVM;

    @Before
    public void setUp() {
        this.thisVM = new VirtualMachineBuilder().withUuid(this.vmID).build();
        this.sameVM = new VirtualMachineBuilder().withUuid(this.vmID).build();
        this.notSameVM = new VirtualMachineBuilder().withUuid(this.notVmID).build();
        this.list.add(this.store);
    }

    @Test
    public void testConstructor() {
        // Then
        Assert.assertEquals(this.vmID, this.thisVM.getUniqueIdentifier());
    }

    // It is not actually unused, the junitparamsrunner does use it.
    @SuppressWarnings("unused")
    private Object[] equalsParameters() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.thisVM, null, false),
                                   JUnitParamsRunner.$(this.thisVM, this.thisVM, true),
                                   JUnitParamsRunner.$(this.thisVM, this.sameVM, true),
                                   JUnitParamsRunner.$(this.sameVM, this.thisVM, true),
                                   JUnitParamsRunner.$(this.thisVM, this.notSameVM, false));

    }

    @Test
    @Parameters(method = "equalsParameters")
    public void testEquals(final Object me, final Object other, final boolean equal) {
        if (equal) {
            Assert.assertEquals(me, other);
        } else {
            Assert.assertNotEquals(me, other);
        }
    }

    // It is not actually unused, the junitparamsrunner does use it.
    @SuppressWarnings("unused")
    private Object[] hashCodeParameters() {
        this.setUp();
        return JUnitParamsRunner.$(JUnitParamsRunner.$(this.thisVM, this.thisVM, true),
                                   JUnitParamsRunner.$(this.thisVM, this.sameVM, true),
                                   JUnitParamsRunner.$(this.thisVM, this.notSameVM, false));

    }

    @Test
    @Parameters(method = "hashCodeParameters")
    public void testHashCode(final Object me, final Object other, final boolean equal) {
        if (equal) {
            Assert.assertEquals(me.hashCode(), other.hashCode());
        } else {
            Assert.assertNotEquals(me.hashCode(), other.hashCode());
        }
    }

    @Test
    public void testVirtualMachineStatusValueOf() {
        Assert.assertEquals(Status.ON, Status.valueOf("ON"));
    }

    @Test
    public void setLaunchingCheckIfLaunchingAndNotOn() {
        this.thisVM.setStatus(Status.LAUNCHING);
        Assert.assertTrue(this.thisVM.isLaunching());
        Assert.assertFalse(this.thisVM.isOn());
    }

    @Test
    public void setOnCheckIfOnAndNotLaunching() {
        this.thisVM.setStatus(Status.ON);
        Assert.assertTrue(this.thisVM.isOn());
        Assert.assertFalse(this.thisVM.isLaunching());
    }

    @Test
    public void setOffCheckIfNotOnAndNotLaunching() {
        this.thisVM.setStatus(Status.OFF);
        Assert.assertFalse(this.thisVM.isOn());
        Assert.assertFalse(this.thisVM.isLaunching());
    }

    @Test
    public void setAndGetHostName() {
        this.thisVM.setHostname(this.vmID);
        Assert.assertEquals(this.vmID, this.thisVM.getHostname());
    }

    @Test
    public void testSetAndGetID() {
        this.thisVM.setUuid(this.notVmID);
        Assert.assertEquals(this.notVmID, this.thisVM.getUniqueIdentifier());
    }

    @Test
    public void testAddDisk() {
        final String store = "Store_ID";
        this.thisVM.addStore(store);
        Assert.assertEquals(store, this.thisVM.getStores().get(0));
    }

    @Test
    public void testSetAndGetHost() {
        this.thisVM.setHost(this.notVmID);
        Assert.assertEquals(this.notVmID, this.thisVM.getHost());
    }

    private VirtualMachine vmToAdopt() {
        return new VirtualMachineBuilder().withUuid(this.notVmID).withHostname(this.notHostname)
                .withStatus(this.notStatus).withHost(this.host).withDisks(this.list).build();

    }

    @Test
    public void testAdoptCopiesUUIDAndHostname() {
        this.thisVM.adoptFromOther(this.vmToAdopt());
        Assert.assertEquals(this.notVmID, this.thisVM.getUniqueIdentifier());
        Assert.assertEquals(this.notHostname, this.thisVM.getHostname());
    }

    @Test
    public void testAdoptCopiesPhysicalHostAndDisks() {
        this.thisVM.adoptFromOther(this.vmToAdopt());
        Assert.assertNotNull(this.thisVM.getHost());
        Assert.assertNotNull(this.thisVM.getStores().get(0));
    }

    @Test
    public void testAdoptCopiesStatus() {
        this.thisVM.adoptFromOther(this.vmToAdopt());
        Assert.assertEquals(this.notStatus, this.thisVM.getStatus());
    }

    @Test
    public void testClearStoresClears() {
        this.thisVM.addStore(this.store);
        this.thisVM.removeStores();
        Assert.assertTrue(this.thisVM.getStores().isEmpty());
    }

}
