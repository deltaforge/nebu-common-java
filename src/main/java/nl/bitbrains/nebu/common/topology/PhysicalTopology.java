package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import nl.bitbrains.nebu.common.util.ErrorChecker;

/**
 * This class provides helper functions for accessing and editing the physical
 * topology representation.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class PhysicalTopology {

    private static final String DISK_NAME = "disk";
    private static final String HOST_NAME = "host";
    private static final String RACK_NAME = "rack";
    private static final String DATACENTER_NAME = "dataCenter";
    private static final String ROOT_NAME = "root";

    private final PhysicalRoot root;

    /**
     * Creates a topology with a default root with id "root".
     */
    public PhysicalTopology() {
        this.root = new PhysicalRootBuilder().withUuid(PhysicalTopology.ROOT_NAME).build();
    }

    /**
     * @param root
     *            the root of the new PhysicalTopology.
     */
    public PhysicalTopology(final PhysicalRoot root) {
        this.root = root;
        this.fixme();
    }

    /**
     * Copies the content of the parameter into a new PhyisicalTopology.
     * 
     * @param topology
     *            to adopt.
     */
    public PhysicalTopology(final PhysicalTopology topology) {
        this.root = new PhysicalRoot(topology.root);
        this.fixme();
    }

    /**
     * Fixes all parent references.
     */
    private void fixme() {
        for (final PhysicalDataCenter dc : this.root.getDataCenters()) {
            dc.setParent(this.root);
            for (final PhysicalRack r : dc.getRacks()) {
                r.setParent(dc);
                for (final PhysicalHost h : r.getCPUs()) {
                    h.setParent(r);
                    for (final PhysicalStore s : h.getDisks()) {
                        s.setParent(h);
                    }
                }
                for (final PhysicalStore s : r.getDisks()) {
                    s.setParent(r);
                }
            }
        }
    }

    /**
     * @return the root of the PhysicalTopology.
     */
    public PhysicalRoot getRoot() {
        return this.root;
    }

    /**
     * @return a list of all data centers in the topology.
     */
    public List<PhysicalDataCenter> getDataCenters() {
        return this.root.getDataCenters();
    }

    /**
     * @return a list of all racks in the topology.
     */
    public List<PhysicalRack> getRacks() {
        final List<PhysicalRack> racks = new ArrayList<PhysicalRack>();
        for (final PhysicalDataCenter dc : this.getDataCenters()) {
            racks.addAll(dc.getRacks());
        }
        return racks;
    }

    /**
     * @return a list of all CPUs in the topology.
     */
    public List<PhysicalHost> getCPUs() {
        final List<PhysicalHost> cpus = new ArrayList<PhysicalHost>();
        for (final PhysicalRack rack : this.getRacks()) {
            cpus.addAll(rack.getCPUs());
        }
        return cpus;
    }

    /**
     * @return a list of all Stores in the topology.
     */
    public List<PhysicalStore> getStores() {
        final List<PhysicalStore> stores = new ArrayList<PhysicalStore>();
        for (final PhysicalRack rack : this.getRacks()) {
            stores.addAll(rack.getDisks());
        }
        final List<PhysicalHost> cpus = this.getCPUs();
        for (final PhysicalHost host : cpus) {
            stores.addAll(host.getDisks());
        }
        return stores;
    }

    /**
     * @param dataCenter
     *            the data center to add to the topology.
     */
    public void addDataCenter(final PhysicalDataCenter dataCenter) {
        ErrorChecker.throwIfNullArgument(dataCenter, PhysicalTopology.DATACENTER_NAME);

        this.root.addDataCenter(dataCenter);
        dataCenter.setParent(this.root);
    }

    /**
     * @param dataCenter
     *            the data center to remove from the topology.
     */
    public void removeDataCenter(final PhysicalDataCenter dataCenter) {
        ErrorChecker.throwIfNullArgument(dataCenter, PhysicalTopology.DATACENTER_NAME);

        this.root.removeDataCenter(dataCenter);
        dataCenter.setParent(null);
    }

    /**
     * @param rack
     *            the rack to add to the topology.
     * @param dataCenter
     *            the data center to use as the parent of rack.
     */
    public void addRackToDataCenter(final PhysicalRack rack, final PhysicalDataCenter dataCenter) {
        ErrorChecker.throwIfNullArgument(rack, PhysicalTopology.RACK_NAME);
        ErrorChecker.throwIfNullArgument(dataCenter, PhysicalTopology.DATACENTER_NAME);

        dataCenter.addRack(rack);
        rack.setParent(dataCenter);
    }

    /**
     * @param rack
     *            the rack to remove from the topology.
     * @param dataCenter
     *            the data center the rack was placed under.
     */
    public void removeRackFromDataCenter(final PhysicalRack rack,
            final PhysicalDataCenter dataCenter) {
        ErrorChecker.throwIfNullArgument(rack, PhysicalTopology.RACK_NAME);
        ErrorChecker.throwIfNullArgument(dataCenter, PhysicalTopology.DATACENTER_NAME);

        dataCenter.removeRack(rack);
        rack.setParent(null);
    }

    /**
     * @param cpu
     *            the cpu to add to the topology.
     * @param rack
     *            the rack to use as the parent of cpu.
     */
    public void addCPUToRack(final PhysicalHost cpu, final PhysicalRack rack) {
        ErrorChecker.throwIfNullArgument(cpu, PhysicalTopology.HOST_NAME);
        ErrorChecker.throwIfNullArgument(rack, PhysicalTopology.HOST_NAME);

        rack.addCPU(cpu);
        cpu.setParent(rack);
    }

    /**
     * @param cpu
     *            the cpu to remove from the topology.
     * @param rack
     *            the rack that is the parent of cpu.
     */
    public void removeCPUFromRack(final PhysicalHost cpu, final PhysicalRack rack) {
        ErrorChecker.throwIfNullArgument(cpu, PhysicalTopology.HOST_NAME);
        ErrorChecker.throwIfNullArgument(rack, PhysicalTopology.RACK_NAME);

        rack.removeCPU(cpu);
        cpu.setParent(null);
    }

    /**
     * @param disk
     *            the disk to add to the topology.
     * @param rack
     *            the rack to use as the parent of disk.
     */
    public void addDiskToRack(final PhysicalStore disk, final PhysicalRack rack) {
        ErrorChecker.throwIfNullArgument(disk, PhysicalTopology.DISK_NAME);
        ErrorChecker.throwIfNullArgument(rack, PhysicalTopology.RACK_NAME);

        rack.addDisk(disk);
        disk.setParent(rack);
    }

    /**
     * @param disk
     *            the disk to remove from the topology.
     * @param rack
     *            the rack that is the parent of disk.
     */
    public void removeDiskFromRack(final PhysicalStore disk, final PhysicalRack rack) {
        ErrorChecker.throwIfNullArgument(disk, PhysicalTopology.DISK_NAME);
        ErrorChecker.throwIfNullArgument(rack, PhysicalTopology.RACK_NAME);

        rack.removeDisk(disk);
        disk.setParent(null);
    }

    /**
     * @param disk
     *            the disk to add to the topology.
     * @param host
     *            the rack to use as the parent of disk.
     */
    public void addDiskToHost(final PhysicalStore disk, final PhysicalHost host) {
        ErrorChecker.throwIfNullArgument(disk, PhysicalTopology.DISK_NAME);
        ErrorChecker.throwIfNullArgument(host, PhysicalTopology.RACK_NAME);

        host.addDisk(disk);
        disk.setParent(host);
    }

    /**
     * @param disk
     *            the disk to remove from the topology.
     * @param host
     *            the rack that is the parent of disk.
     */
    public void removeDiskFromHost(final PhysicalStore disk, final PhysicalHost host) {
        ErrorChecker.throwIfNullArgument(disk, PhysicalTopology.DISK_NAME);
        ErrorChecker.throwIfNullArgument(host, PhysicalTopology.RACK_NAME);

        host.removeDisk(disk);
        disk.setParent(null);
    }

    /**
     * Merges one {@link PhysicalTopology} with another, resulting in a new
     * {@link PhysicalTopology} object.
     * 
     * @param one
     *            first tree.
     * @param two
     *            tree to merge with.
     * @return a new tree object.
     */
    public static PhysicalTopology mergeTree(final PhysicalTopology one, final PhysicalTopology two) {
        ErrorChecker.throwIfNullArgument(one, "First Argument");
        ErrorChecker.throwIfNullArgument(two, "Second Argument");
        if (!one.getRoot().equals(two.getRoot())) {
            return new PhysicalTopology(one);
        }
        final PhysicalTopology res = new PhysicalTopology(one);

        for (final PhysicalDataCenter datacenter : two.getDataCenters()) {
            final int index = one.getDataCenters().indexOf(datacenter);
            PhysicalDataCenter clone;
            if (index >= 0) {
                clone = PhysicalTopology.mergeDatacenter(one.getDataCenters().get(index),
                                                         datacenter);
            } else {
                clone = new PhysicalDataCenter(datacenter);
            }
            clone.setParent(res.getRoot());
            res.getRoot().addDataCenter(clone);
        }

        return res;
    }

    /**
     * Merges two datacenters, whilst leaving them unmodified.
     * 
     * @param one
     *            First datacenter.
     * @param two
     *            Second datacenter.
     * @return the merged new object.
     */
    private static PhysicalDataCenter mergeDatacenter(final PhysicalDataCenter one,
            final PhysicalDataCenter two) {
        final PhysicalDataCenter res = new PhysicalDataCenter(one);
        for (final PhysicalRack rack : two.getRacks()) {
            final int index = one.getRacks().indexOf(rack);
            PhysicalRack clone;
            if (index >= 0) {
                clone = PhysicalTopology.mergeRack(one.getRacks().get(index), rack);
            } else {
                clone = new PhysicalRack(rack);
            }
            clone.setParent(res);
            res.addRack(clone);
        }
        return res;
    }

    /**
     * Merges two PhysicalRacks, whilst leaving them unmodified.
     * 
     * @param one
     *            First PhysicalRack.
     * @param two
     *            Second PhysicalRack.
     * @return the merged new object.
     */
    private static PhysicalRack mergeRack(final PhysicalRack one, final PhysicalRack two) {
        final PhysicalRack res = new PhysicalRack(one);
        for (final PhysicalHost cpu : two.getCPUs()) {
            final int index = one.getCPUs().indexOf(cpu);
            PhysicalHost clone;
            if (index < 0) {
                clone = new PhysicalHost(cpu);
            } else {
                clone = PhysicalTopology.mergeHost(one.getCPUs().get(index), cpu);
            }
            clone.setParent(res);
            res.addCPU(clone);
        }
        for (final PhysicalStore disk : two.getDisks()) {
            final int index = one.getDisks().indexOf(disk);
            if (index < 0) {
                final PhysicalStore clone = new PhysicalStore(disk);
                clone.setParent(res);
                res.addDisk(clone);
            }
        }
        return res;
    }

    /**
     * Merges two PhysicalHosts, whilst leaving them unmodified.
     * 
     * @param one
     *            First PhysicalHost.
     * @param two
     *            Second PhysicalHost.
     * @return the merged new object.
     */
    private static PhysicalHost mergeHost(final PhysicalHost one, final PhysicalHost two) {
        final PhysicalHost res = new PhysicalHost(one);
        for (final PhysicalStore disk : two.getDisks()) {
            final int index = one.getDisks().indexOf(disk);
            if (index < 0) {
                final PhysicalStore clone = new PhysicalStore(disk);
                clone.setParent(res);
                res.addDisk(clone);
            }
        }
        return res;
    }

    @Override
    public int hashCode() {
        return this.root.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalTopology) {
            final PhysicalTopology that = (PhysicalTopology) obj;
            return this.root.equals(that.root);
        }
        return false;
    }

    /**
     * @param cpuID
     *            to look for.
     * @return true iff there is a host without that cpuID
     */
    public boolean hasCPUByID(final String cpuID) {
        final List<PhysicalHost> cpus = this.getCPUs();
        for (final PhysicalHost host : cpus) {
            if (host.getUniqueIdentifier().equals(cpuID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param cpuID
     *            to look for.
     * @return the cpu if it is found.
     */
    public PhysicalHost getCPUByID(final String cpuID) {
        final List<PhysicalHost> cpus = this.getCPUs();
        for (final PhysicalHost host : cpus) {
            if (host.getUniqueIdentifier().equals(cpuID)) {
                return host;
            }
        }
        throw new NoSuchElementException();
    }
}
