package nl.bitbrains.nebu.common.topology.factory;

import nl.bitbrains.nebu.common.topology.factory.PhysicalDataCenterFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalHostFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalRackFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalRootFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalStoreFactory;
import nl.bitbrains.nebu.common.topology.factory.TopologyFactories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTopologyFactories {

    private TopologyFactories fac;

    @Before
    public void setupTests() {
        this.fac = new TopologyFactories();
    }

    @Test
    public void testSetPhysicalStoreFactoryNull() {
        this.fac.withPhysicalStoreFactory(null);
        Assert.assertNull(this.fac.getPhysicalStoreFactory());
    }

    @Test
    public void testSetPhysicalStoreFactoryNonNull() {
        final PhysicalStoreFactory x = new PhysicalStoreFactory();
        this.fac.withPhysicalStoreFactory(x);
        Assert.assertSame(x, this.fac.getPhysicalStoreFactory());
        Assert.assertSame(this.fac, x.getFactories());
    }

    @Test
    public void testSetPhysicalCPUFactoryNull() {
        this.fac.withPhysicalCPUFactory(null);
        Assert.assertNull(this.fac.getPhysicalCPUFactory());
    }

    @Test
    public void testSetPhysicalCPUFactoryNonNull() {
        final PhysicalHostFactory x = new PhysicalHostFactory();
        this.fac.withPhysicalCPUFactory(x);
        Assert.assertSame(x, this.fac.getPhysicalCPUFactory());
        Assert.assertSame(this.fac, x.getFactories());
    }

    @Test
    public void testSetPhysicalRackFactoryNull() {
        this.fac.withPhysicalRackFactory(null);
        Assert.assertNull(this.fac.getPhysicalRackFactory());
    }

    @Test
    public void testSetPhysicalRackFactoryNonNull() {
        final PhysicalRackFactory x = new PhysicalRackFactory();
        this.fac.withPhysicalRackFactory(x);
        Assert.assertSame(x, this.fac.getPhysicalRackFactory());
        Assert.assertSame(this.fac, x.getFactories());
    }

    @Test
    public void testSetPhysicalDataCenterFactoryNull() {
        this.fac.withPhysicalDataCenterFactory(null);
        Assert.assertNull(this.fac.getPhysicalDataCenterFactory());
    }

    @Test
    public void testSetPhysicalDataCenterFactoryNonNull() {
        final PhysicalDataCenterFactory x = new PhysicalDataCenterFactory();
        this.fac.withPhysicalDataCenterFactory(x);
        Assert.assertSame(x, this.fac.getPhysicalDataCenterFactory());
        Assert.assertSame(this.fac, x.getFactories());
    }

    @Test
    public void testSetPhysicalRootFactoryNull() {
        this.fac.withPhysicalRootFactory(null);
        Assert.assertNull(this.fac.getPhysicalRootFactory());
    }

    @Test
    public void testSetPhysicalRootFactoryNonNull() {
        final PhysicalRootFactory x = new PhysicalRootFactory();
        this.fac.withPhysicalRootFactory(x);
        Assert.assertSame(x, this.fac.getPhysicalRootFactory());
        Assert.assertSame(this.fac, x.getFactories());
    }

    @Test
    public void testDefaultStaticConstructor() {
        final TopologyFactories defFac = TopologyFactories.createDefault();
        Assert.assertNotNull(defFac.getPhysicalCPUFactory());
        Assert.assertNotNull(defFac.getPhysicalStoreFactory());
        Assert.assertNotNull(defFac.getPhysicalRackFactory());
        Assert.assertNotNull(defFac.getPhysicalDataCenterFactory());
        Assert.assertNotNull(defFac.getPhysicalRootFactory());
    }

}
