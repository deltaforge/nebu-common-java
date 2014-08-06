package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a physical rack placed inside a data center. Each
 * PhysicalRack has a PhysicalDataCenter parent and has a unique identifier
 * through the Identifiable interface.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class PhysicalRack extends PhysicalResourceWithDisks {

    private final Map<String, PhysicalHost> cpus;
    private PhysicalDataCenter parent;

    /**
     * @param identifier
     *            the unique identifier for the new PhysicalCPU.
     * @param parent
     *            the parent of this new PhysicalCPU.
     * @param cpus
     *            the cpus in the rack.
     * @param disks
     *            the disks in the rack.
     */
    protected PhysicalRack(final String identifier, final PhysicalDataCenter parent,
            final Map<String, PhysicalHost> cpus, final Map<String, PhysicalStore> disks) {
        super(identifier, disks);
        this.parent = parent;
        this.cpus = cpus;
    }

    /**
     * Copy Constructor.
     * 
     * @param other
     *            to copy.
     */
    public PhysicalRack(final PhysicalRack other) {
        super(other.getUniqueIdentifier(), other.getDisks());
        this.cpus = new HashMap<String, PhysicalHost>();
        for (final PhysicalHost cpu : other.getCPUs()) {
            final PhysicalHost newCpu = new PhysicalHost(cpu);
            newCpu.setParent(this);
            this.addCPU(newCpu);
        }
    }

    /**
     * @return the parent of the PhysicalRack.
     */
    public PhysicalDataCenter getParent() {
        return this.parent;
    }

    /**
     * @param parent
     *            the parent of the PhysicalRack.
     */
    protected void setParent(final PhysicalDataCenter parent) {
        this.parent = parent;
    }

    /**
     * @return CPUs contained in the PhysicalRack.
     */
    public List<PhysicalHost> getCPUs() {
        return new ArrayList<PhysicalHost>(this.cpus.values());
    }

    /**
     * @param cpu
     *            the CPU to add to the PhysicalRack.
     */
    protected final void addCPU(final PhysicalHost cpu) {
        if (cpu != null) {
            this.cpus.put(cpu.getUniqueIdentifier(), cpu);
        }
    }

    /**
     * @param cpu
     *            the CPU to remove from the PhysicalRack.
     */
    protected void removeCPU(final PhysicalHost cpu) {
        if (cpu != null) {
            this.cpus.remove(cpu.getUniqueIdentifier());
        }
    }

    @Override
    public int hashCode() {
        return this.getUniqueIdentifier().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalRack) {
            final PhysicalRack that = (PhysicalRack) obj;
            return this.getUniqueIdentifier().equals(that.getUniqueIdentifier());
        }
        return false;
    }
}
