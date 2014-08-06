package nl.bitbrains.nebu.common.topology;

import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder for the PhysicalResources.
 * 
 * @param <T>
 *            Type to build.
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public abstract class PhysicalResourceBuilder<T> implements IBuilder<T> {

    private String uuid;

    /**
     * Simple default (empty) constructor.
     */
    public PhysicalResourceBuilder() {
    }

    @Override
    public void reset() {
        this.uuid = null;
    }

    /**
     * @param uuid
     *            to set.
     * @return this for fluency.
     */
    public PhysicalResourceBuilder<T> withUuid(final String uuid) {
        ErrorChecker.throwIfNullArgument(uuid, PhysicalResource.UUID_NAME);
        this.uuid = uuid;
        return this;
    }

    /**
     * Simple getter.
     * 
     * @return the uuid.
     */
    protected String getUUID() {
        return this.uuid;
    }
}
