package nl.bitbrains.nebu.common.topology.factory;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class PhysicalResourceFactory extends IdentifiableFactory {
    private TopologyFactories factories;

    /**
     * @param topologyFactories
     *            to set.
     */
    public void setTopologyFactories(final TopologyFactories topologyFactories) {
        this.factories = topologyFactories;
    }

    /**
     * @return the factories
     */
    public TopologyFactories getFactories() {
        return this.factories;
    }
}
