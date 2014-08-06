package nl.bitbrains.nebu.common.topology;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link PhysicalHost}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalHostBuilder extends PhysicalResourceWithDisksBuilder<PhysicalHost> {
    private PhysicalRack parent;
    private double memUsage;
    private double cpuUsage;

    /**
     * Simple constructor.
     */
    public PhysicalHostBuilder() {
        this.reset();
    }

    /**
     * Resets the builder.
     */
    @Override
    public final void reset() {
        super.reset();
        this.parent = null;
        this.memUsage = 0;
        this.cpuUsage = 0;
    }

    /**
     * @param parent
     *            to build with
     * @return this for fluency.
     */
    public PhysicalHostBuilder withParent(final PhysicalRack parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @param usage
     *            to set.
     * @return this for fluency.
     */
    public PhysicalHostBuilder withCpuUsage(final double usage) {
        this.cpuUsage = usage;
        return this;
    }

    /**
     * @param usage
     *            to set.
     * @return this for fluency.
     */
    public PhysicalHostBuilder withMemUsage(final double usage) {
        this.memUsage = usage;
        return this;
    }

    /**
     * @return the build {@link PhysicalHost} object.
     */
    public PhysicalHost build() {
        ErrorChecker.throwIfNotSet(this.getUUID(), PhysicalResource.UUID_NAME);
        final PhysicalHost host = new PhysicalHost(this.getUUID(), this.parent, this.getDisks(),
                this.cpuUsage, this.memUsage);
        this.reset();
        return host;
    }

}
