package nl.bitbrains.nebu.common.topology;

/**
 * This class represents a {@link PhysicalStore} placed inside a physical rack
 * or physicalHost. Each PhysicalStore has a {@link PhysicalResource} parent and
 * has a unique identifier through the Identifiable interface.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class PhysicalStore extends PhysicalResource {

    private PhysicalResource parent;
    private long capacity;
    private long used;

    /**
     * @param identifier
     *            the unique identifier for the new {@link PhysicalStore}.
     * @param parent
     *            the parent of this new Disk.
     * @param capacity
     *            the capacity of this disk.
     * @param used
     *            the used capacity of the disk.
     */
    PhysicalStore(final String identifier, final PhysicalResource parent, final long capacity,
            final long used) {
        super(identifier);
        this.parent = parent;
        this.capacity = capacity;
        this.setUsed(used);
    }

    /**
     * Copy constructor.
     * 
     * @param disk
     *            to copy.
     */
    public PhysicalStore(final PhysicalStore disk) {
        super(disk.getUniqueIdentifier());
        this.capacity = disk.getCapacity();
        this.used = disk.getUsed();
    }

    /**
     * @return the parent of this {@link PhysicalStore}.
     */
    public PhysicalResource getParent() {
        return this.parent;
    }

    /**
     * @param parent
     *            the new parent of the {@link PhysicalStore}.
     */
    protected void setParent(final PhysicalResource parent) {
        this.parent = parent;
    }

    /**
     * @return the capacity
     */
    public long getCapacity() {
        return this.capacity;
    }

    /**
     * @param capacity
     *            the capacity to set
     */
    public void setCapacity(final long capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the used
     */
    public long getUsed() {
        return this.used;
    }

    /**
     * @param used
     *            the used to set
     */
    public void setUsed(final long used) {
        this.used = used;
    }

    @Override
    public int hashCode() {
        return this.getUniqueIdentifier().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalStore) {
            final PhysicalStore that = (PhysicalStore) obj;
            return this.getUniqueIdentifier().equals(that.getUniqueIdentifier());
        }
        return false;
    }
}
