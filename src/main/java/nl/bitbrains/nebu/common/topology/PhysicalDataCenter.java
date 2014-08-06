package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a physical data center. Each PhysicalDataCenter has the
 * PhysicalRoot of a topology as its parent and can contain any number of
 * PhysicalRacks. Each PhysicalDataCenter is uniquely identifiable through the
 * Identifiable interface.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class PhysicalDataCenter extends PhysicalResource {

    private final Map<String, PhysicalRack> racks;
    private PhysicalRoot parent;

    /**
     * Copyconstructor.
     * 
     * @param other
     *            to adopt.
     */
    public PhysicalDataCenter(final PhysicalDataCenter other) {
        super(other.getUniqueIdentifier());
        this.racks = new HashMap<String, PhysicalRack>();
        for (final PhysicalRack rack : other.getRacks()) {
            final PhysicalRack newRack = new PhysicalRack(rack);
            newRack.setParent(this);
            this.addRack(newRack);
        }
    }

    /**
     * @param identifier
     *            the unique identifier for the new PhysicalDataCenter.
     * @param parent
     *            the root this datacenter is placed under.
     * @param racks
     *            racks in this datacenter.
     */
    protected PhysicalDataCenter(final String identifier, final PhysicalRoot parent,
            final Map<String, PhysicalRack> racks) {
        super(identifier);
        this.parent = parent;
        this.racks = racks;
    }

    /**
     * @return the racks contained in the PhysicalDataCenter.
     */
    public List<PhysicalRack> getRacks() {
        return new ArrayList<PhysicalRack>(this.racks.values());
    }

    /**
     * @param rack
     *            the rack to add to the PhysicalDataCenter.
     */
    protected final void addRack(final PhysicalRack rack) {
        if (rack != null) {
            this.racks.put(rack.getUniqueIdentifier(), rack);
        }
    }

    /**
     * @param rack
     *            the rack to remove from the PhysicalDataCenter.
     */
    protected void removeRack(final PhysicalRack rack) {
        if (rack != null) {
            this.racks.remove(rack.getUniqueIdentifier());
        }
    }

    /**
     * @return the parent of the PhysicalDataCenter.
     */
    public PhysicalRoot getParent() {
        return this.parent;
    }

    /**
     * @param parent
     *            the parent of the PhysicalDataCenter.
     */
    protected void setParent(final PhysicalRoot parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        return this.getUniqueIdentifier().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalDataCenter) {
            final PhysicalDataCenter that = (PhysicalDataCenter) obj;
            return this.getUniqueIdentifier().equals(that.getUniqueIdentifier());
        }
        return false;
    }
}
