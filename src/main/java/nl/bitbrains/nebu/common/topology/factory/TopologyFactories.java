package nl.bitbrains.nebu.common.topology.factory;

import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalStore;

/**
 * Collection of the different factories needed to fully convert a topology to
 * and from XML.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class TopologyFactories {

    private TopologyFactory<PhysicalHost> physicalCPUFactory;
    private TopologyFactory<PhysicalStore> physicalStoreFactory;
    private TopologyFactory<PhysicalRack> physicalRackFactory;
    private TopologyFactory<PhysicalDataCenter> physicalDataCenterFactory;
    private TopologyFactory<PhysicalRoot> physicalRootFactory;

    /**
     * Empty default constructor.
     */
    public TopologyFactories() {

    }

    /**
     * @return the physicalCPUFactory.
     */
    public TopologyFactory<PhysicalStore> getPhysicalStoreFactory() {
        return this.physicalStoreFactory;
    }

    /**
     * @param physicalStoreFactory
     *            to use.
     * @return this for chaining of calls.
     */
    public TopologyFactories withPhysicalStoreFactory(
            final TopologyFactory<PhysicalStore> physicalStoreFactory) {
        if (physicalStoreFactory != null) {
            this.physicalStoreFactory = physicalStoreFactory;
            this.physicalStoreFactory.setTopologyFactories(this);
        } else {
            this.physicalStoreFactory = null;
        }
        return this;
    }

    /**
     * @return the physicalCPUFactory.
     */
    public TopologyFactory<PhysicalHost> getPhysicalCPUFactory() {
        return this.physicalCPUFactory;
    }

    /**
     * @param physicalCPUFactory
     *            to use.
     * @return this for chaining of calls.
     */
    public TopologyFactories withPhysicalCPUFactory(
            final TopologyFactory<PhysicalHost> physicalCPUFactory) {
        if (physicalCPUFactory != null) {
            this.physicalCPUFactory = physicalCPUFactory;
            this.physicalCPUFactory.setTopologyFactories(this);
        } else {
            this.physicalCPUFactory = null;
        }
        return this;
    }

    /**
     * @return the physicalCPUFactory.
     */
    public TopologyFactory<PhysicalRack> getPhysicalRackFactory() {
        return this.physicalRackFactory;
    }

    /**
     * @param physicalRackFactory
     *            to use.
     * @return this for chaining.
     */
    public TopologyFactories withPhysicalRackFactory(
            final TopologyFactory<PhysicalRack> physicalRackFactory) {
        if (physicalRackFactory != null) {
            this.physicalRackFactory = physicalRackFactory;
            this.physicalRackFactory.setTopologyFactories(this);
        } else {
            this.physicalRackFactory = null;
        }
        return this;
    }

    /**
     * @return the physicalDataCenterFactory.
     */
    public TopologyFactory<PhysicalDataCenter> getPhysicalDataCenterFactory() {
        return this.physicalDataCenterFactory;
    }

    /**
     * @param physicalDataCenterFactory
     *            to use.
     * @return this for chaining.
     */
    public TopologyFactories withPhysicalDataCenterFactory(
            final TopologyFactory<PhysicalDataCenter> physicalDataCenterFactory) {
        if (physicalDataCenterFactory != null) {
            this.physicalDataCenterFactory = physicalDataCenterFactory;
            this.physicalDataCenterFactory.setTopologyFactories(this);
        } else {
            this.physicalDataCenterFactory = null;
        }
        return this;
    }

    /**
     * @return the physicalRootFactory.
     */
    public TopologyFactory<PhysicalRoot> getPhysicalRootFactory() {
        return this.physicalRootFactory;
    }

    /**
     * @param physicalRootFactory
     *            to use.
     * @return this for chaining.
     */
    public TopologyFactories withPhysicalRootFactory(
            final TopologyFactory<PhysicalRoot> physicalRootFactory) {
        if (physicalRootFactory != null) {
            this.physicalRootFactory = physicalRootFactory;
            this.physicalRootFactory.setTopologyFactories(this);
        } else {
            this.physicalRootFactory = null;
        }
        return this;
    }

    /**
     * @return a {@link TopologyFactories} instance with the default factories
     *         in place.
     */
    public static TopologyFactories createDefault() {
        final TopologyFactories result = new TopologyFactories();
        return result.withPhysicalStoreFactory(new PhysicalStoreFactory())
                .withPhysicalCPUFactory(new PhysicalHostFactory())
                .withPhysicalRackFactory(new PhysicalRackFactory())
                .withPhysicalDataCenterFactory(new PhysicalDataCenterFactory())
                .withPhysicalRootFactory(new PhysicalRootFactory());
    }

}
