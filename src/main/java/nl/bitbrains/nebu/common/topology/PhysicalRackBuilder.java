package nl.bitbrains.nebu.common.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link PhysicalRack}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalRackBuilder extends PhysicalResourceWithDisksBuilder<PhysicalRack> {
    private PhysicalDataCenter parent;
    private Map<String, PhysicalHost> cpus;

    /**
     * Simple constructor.
     */
    public PhysicalRackBuilder() {
        this.reset();
    }

    /**
     * Resets all.
     */
    @Override
    public final void reset() {
        super.reset();
        this.parent = null;
        this.cpus = new HashMap<String, PhysicalHost>();
    }

    /**
     * @param parent
     *            to build with
     * @return this for fluency.
     */
    public PhysicalRackBuilder withParent(final PhysicalDataCenter parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @param cpu
     *            to include.
     * @return this for fluency.
     */
    public PhysicalRackBuilder withHost(final PhysicalHost cpu) {
        ErrorChecker.throwIfNullArgument(cpu, "cpu");
        this.cpus.put(cpu.getUniqueIdentifier(), cpu);
        return this;
    }

    /**
     * @param cpus
     *            to include.
     * @return this for fluency.
     */
    public PhysicalRackBuilder withHosts(final List<PhysicalHost> cpus) {
        ErrorChecker.throwIfNullArgument(cpus, "cpus");
        for (final PhysicalHost cpu : cpus) {
            this.withHost(cpu);
        }
        return this;
    }

    /**
     * @return the build {@link PhysicalRack} object.
     */
    public PhysicalRack build() {
        ErrorChecker.throwIfNotSet(this.getUUID(), PhysicalResource.UUID_NAME);
        final PhysicalRack res = new PhysicalRack(this.getUUID(), this.parent, this.cpus,
                this.getDisks());
        this.reset();
        return res;
    }

}
