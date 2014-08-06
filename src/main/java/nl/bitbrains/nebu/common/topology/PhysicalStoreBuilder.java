package nl.bitbrains.nebu.common.topology;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link PhysicalStore}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalStoreBuilder extends PhysicalResourceBuilder<PhysicalStore> {
    private PhysicalResource parent;
    private long capacity;
    private long used;

    /**
     * Simple constructor.
     */
    public PhysicalStoreBuilder() {
        this.reset();
    }

    @Override
    public final void reset() {
        super.reset();
        this.parent = null;
        this.capacity = 0;
        this.used = 0;
    }

    /**
     * @param parent
     *            to build with
     * @return this for fluency.
     */
    public PhysicalStoreBuilder withParent(final PhysicalRack parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @param capacity
     *            to set.
     * @return this for fluency.
     */
    public PhysicalStoreBuilder withCapacity(final long capacity) {
        this.capacity = capacity;
        return this;
    }

    /**
     * @param used
     *            to set.
     * @return this for fluency.
     */
    public PhysicalStoreBuilder withUsed(final long used) {
        this.used = used;
        return this;
    }

    /**
     * @return the build {@link PhysicalStore} object.
     */
    public PhysicalStore build() {
        ErrorChecker.throwIfNotSet(this.getUUID(), PhysicalResource.UUID_NAME);
        final PhysicalStore res = new PhysicalStore(this.getUUID(), this.parent, this.capacity,
                this.used);
        this.reset();
        return res;
    }
}
