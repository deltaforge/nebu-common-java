package nl.bitbrains.nebu.common;

import java.util.ArrayList;
import java.util.List;

import nl.bitbrains.nebu.common.interfaces.Identifiable;

/**
 * Represents a VirtualMachine as returned by the VM Manager.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class VirtualMachine implements Identifiable {

    /**
     * Used for the status of the VMs.
     * 
     * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
     * 
     */
    public enum Status {
        ON, OFF, LAUNCHING, UNKNOWN
    }

    private String uuid;
    private String hostname;
    private Status status;
    private String host;
    private List<String> stores;

    /**
     * Constructor.
     * 
     * @param uuid
     *            to set.
     * @param status
     *            to set.
     * @param hostname
     *            to set.
     * @param host
     *            to set.
     * @param stores
     *            to set.
     */
    public VirtualMachine(final String uuid, final String hostname, final Status status,
            final String host, final List<String> stores) {
        this.uuid = uuid;
        this.hostname = hostname;
        this.status = status;
        this.host = host;
        this.stores = stores;
    }

    /**
     * Updates this vm to reflect the values as specified in the parameter.
     * 
     * @param vm
     *            to use the values of.
     */
    public void adoptFromOther(final VirtualMachine vm) {
        this.uuid = vm.getUniqueIdentifier();
        this.hostname = vm.getHostname();
        this.status = vm.getStatus();
        this.host = vm.getHost();
        this.stores = vm.getStores();
    }

    /**
     * @return true iff status == launching.
     */
    public boolean isLaunching() {
        return this.getStatus() == Status.LAUNCHING;
    }

    /**
     * @return iff status is on.
     */
    public boolean isOn() {
        return this.getStatus() == Status.ON;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final Status status) {
        this.status = status;
    }

    /**
     * @return the uuid.
     */
    public String getUniqueIdentifier() {
        return this.uuid;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * @param hostname
     *            the hostname to set
     */
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @return the stores
     */
    public List<String> getStores() {
        return new ArrayList<String>(this.stores);
    }

    /**
     * @param store
     *            to add.
     */
    public void addStore(final String store) {
        this.stores.add(store);
    }

    /**
     * Clears the stores list.
     */
    public void removeStores() {
        this.stores.clear();
    }

    /**
     * @param host
     *            to set.
     */
    public void setHost(final String host) {
        this.host = host;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.uuid.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof VirtualMachine)) {
            return false;
        }
        final VirtualMachine other = (VirtualMachine) obj;
        return this.uuid.equals(other.uuid);
    }

    /**
     * @param uniqueIdentifier
     *            id to set.
     */
    public void setUuid(final String uniqueIdentifier) {
        this.uuid = uniqueIdentifier;
    }

}
