package nl.bitbrains.nebu.common.topology;

import java.util.Map;

/**
 * This class represents a physical CPU placed inside a physical rack. Each
 * PhysicalCPU has a PhysicalRack parent and has a unique identifier through the
 * Identifiable interface.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class PhysicalHost extends PhysicalResourceWithDisks {

    private PhysicalRack parent;
    private final double memUsage;
    private final double cpuUsage;

    /**
     * @param identifier
     *            the unique identifier for the new PhysicalCPU.
     * @param parent
     *            the parent of this new PhysicalCPU.
     * @param disks
     *            the disks attached to this CPU.
     * @param cpuUsage
     *            to set.
     * @param memUsage
     *            to set.
     */
    protected PhysicalHost(final String identifier, final PhysicalRack parent,
            final Map<String, PhysicalStore> disks, final double cpuUsage, final double memUsage) {
        super(identifier, disks);
        this.parent = parent;
        this.cpuUsage = cpuUsage;
        this.memUsage = memUsage;
    }

    /**
     * Copyconstructor.
     * 
     * @param other
     *            cpu to adopt.
     */
    public PhysicalHost(final PhysicalHost other) {
        super(other.getUniqueIdentifier(), other.getDisks());
        this.cpuUsage = other.cpuUsage;
        this.memUsage = other.memUsage;
    }

    /**
     * @return the parent of this PhysicalCPU.
     */
    public PhysicalRack getParent() {
        return this.parent;
    }

    /**
     * @param parent
     *            the new parent of the PhysicalCPU.
     */
    protected void setParent(final PhysicalRack parent) {
        this.parent = parent;
    }

    /**
     * @return the cpu usage. Number between 0 and 1.
     */
    public double getCpuUsage() {
        return this.cpuUsage;
    }

    /**
     * @return the memory usage. Number between 0 and 1.
     */
    public double getMemUsage() {
        return this.memUsage;
    }

    @Override
    public int hashCode() {
        return this.getUniqueIdentifier().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalHost) {
            final PhysicalHost that = (PhysicalHost) obj;
            return this.getUniqueIdentifier().equals(that.getUniqueIdentifier());
        }
        return false;
    }

}
