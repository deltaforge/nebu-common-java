package nl.bitbrains.nebu.common.topology.factory;

import nl.bitbrains.nebu.common.topology.PhysicalResource;
import nl.bitbrains.nebu.common.util.xml.XMLFactory;

/**
 * Simple interface that allows for consistent creation of the topology
 * factories.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 * @param <T>
 *            type of topology resource we will fabricate.
 */
public interface TopologyFactory<T extends PhysicalResource> extends
        XMLFactory<T> {

    /**
     * @param factories
     *            to set.
     */
    void setTopologyFactories(TopologyFactories factories);
}
