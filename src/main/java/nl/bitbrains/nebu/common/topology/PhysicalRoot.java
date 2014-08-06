package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the root of a physical server topology. It can contain
 * any number PhysicalDataCenters. The root has a unique identifier and is thus
 * Identifiable.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class PhysicalRoot extends PhysicalResource {

    private final Map<String, PhysicalDataCenter> dataCenters;

    /**
     * Copy constructor.
     * 
     * @param other
     *            root to adopt.
     */
    public PhysicalRoot(final PhysicalRoot other) {
        super(other.getUniqueIdentifier());
        this.dataCenters = new HashMap<String, PhysicalDataCenter>();
        for (final PhysicalDataCenter datacenter : other.getDataCenters()) {
            final PhysicalDataCenter d = new PhysicalDataCenter(datacenter);
            d.setParent(this);
            this.addDataCenter(d);
        }
    }

    /**
     * 
     * @param uniqueIdentifier
     *            the unique identifier for the new PhysicalRoot.
     * @param dcs
     *            the datacenters under this root.
     */
    protected PhysicalRoot(final String uniqueIdentifier, final Map<String, PhysicalDataCenter> dcs) {
        super(uniqueIdentifier);
        this.dataCenters = dcs;
    }

    /**
     * @return the data centers part of this PhyscicalRoot.
     */
    public List<PhysicalDataCenter> getDataCenters() {
        return new ArrayList<PhysicalDataCenter>(this.dataCenters.values());
    }

    /**
     * @param dataCenter
     *            the data center to add to the PhysicalRoot.
     */
    protected final void addDataCenter(final PhysicalDataCenter dataCenter) {
        if (dataCenter != null) {
            this.dataCenters.put(dataCenter.getUniqueIdentifier(), dataCenter);
        }
    }

    /**
     * @param dataCenter
     *            the data center to remove from the PhysicalRoot.
     */
    protected void removeDataCenter(final PhysicalDataCenter dataCenter) {
        if (dataCenter != null) {
            this.dataCenters.remove(dataCenter.getUniqueIdentifier());
        }
    }

    @Override
    public int hashCode() {
        return this.getUniqueIdentifier().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalRoot) {
            final PhysicalRoot that = (PhysicalRoot) obj;
            return this.getUniqueIdentifier().equals(that.getUniqueIdentifier());
        }
        return false;
    }
}
