package nl.bitbrains.nebu.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.bitbrains.nebu.common.VirtualMachine.Status;
import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.interfaces.Identifiable;
import nl.bitbrains.nebu.common.topology.PhysicalResource;
import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * Builder class for the {@link VirtualMachine}.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class VirtualMachineBuilder implements IBuilder<VirtualMachine> {
    private String uuid;
    private String hostname;
    private Status status;
    private String host;
    private List<String> stores;

    /**
     * Simple constructor.
     */
    public VirtualMachineBuilder() {
        this.reset();
    }

    /**
     * Resets all.
     */
    public final void reset() {
        this.uuid = null;
        this.hostname = null;
        this.status = Status.UNKNOWN;
        this.host = null;
        this.stores = new ArrayList<String>();
    }

    /**
     * @param uuid
     *            to build with.
     * @return this for fluency
     */
    public VirtualMachineBuilder withUuid(final String uuid) {
        ErrorChecker.throwIfNullArgument(uuid, PhysicalResource.UUID_NAME);
        this.uuid = uuid;
        return this;
    }

    /**
     * @param hostname
     *            to build with
     * @return this for fluency.
     */
    public VirtualMachineBuilder withHostname(final String hostname) {
        ErrorChecker.throwIfNullArgument(hostname, "hostname");
        this.hostname = hostname;
        return this;
    }

    /**
     * @param status
     *            to include.
     * @return this for fluency.
     */
    public VirtualMachineBuilder withStatus(final Status status) {
        this.status = status;
        return this;
    }

    /**
     * @param host
     *            to include.
     * @return this for fluency.
     */
    public VirtualMachineBuilder withHost(final String host) {
        ErrorChecker.throwIfNullArgument(host, "host");
        this.host = host;
        return this;
    }

    /**
     * @param disk
     *            to add.
     * @return this for fluency.
     */
    public VirtualMachineBuilder withDisk(final String disk) {
        ErrorChecker.throwIfNullArgument(disk, "disk");
        this.stores.add(disk);
        return this;
    }

    /**
     * @param disks
     *            to add.
     * @return this for fluency.
     */
    public VirtualMachineBuilder withDisks(final Collection<String> disks) {
        ErrorChecker.throwIfNullArgument(disks, "disks");
        for (final String store : disks) {
            this.withDisk(store);
        }
        return this;
    }

    /**
     * @return the build {@link VirtualMachine} object.
     */
    public VirtualMachine build() {
        ErrorChecker.throwIfNotSet(this.uuid, Identifiable.UUID_NAME);
        final VirtualMachine vm = new VirtualMachine(this.uuid, this.hostname, this.status,
                this.host, this.stores);
        this.reset();
        return vm;
    }

}