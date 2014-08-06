package nl.bitbrains.nebu.common.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link PhysicalResourceWithDisks}.
 * 
 * @param <T>
 *            Type to build.
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class PhysicalResourceWithDisksBuilder<T> extends PhysicalResourceBuilder<T> {

    private Map<String, PhysicalStore> disks;

    /**
     * Simple constructor.
     */
    public PhysicalResourceWithDisksBuilder() {
    }

    /**
     * Resets the builder.
     */
    @Override
    public void reset() {
        super.reset();
        this.disks = new HashMap<String, PhysicalStore>();
    }

    /**
     * @param disk
     *            to include.
     * @return this for fluency.
     */
    public PhysicalResourceWithDisksBuilder<T> withDisk(final PhysicalStore disk) {
        ErrorChecker.throwIfNullArgument(disk, "disk");
        this.disks.put(disk.getUniqueIdentifier(), disk);
        return this;
    }

    /**
     * @param disks
     *            to include.
     * @return this for fluency.
     */
    public PhysicalResourceWithDisksBuilder<T> withDisks(final List<PhysicalStore> disks) {
        ErrorChecker.throwIfNullArgument(disks, "disks");
        for (final PhysicalStore disk : disks) {
            this.withDisk(disk);
        }
        return this;
    }

    /**
     * @return the disks
     */
    protected final Map<String, PhysicalStore> getDisks() {
        return this.disks;
    }
}
