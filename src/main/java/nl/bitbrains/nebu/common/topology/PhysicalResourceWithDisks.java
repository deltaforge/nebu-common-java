package nl.bitbrains.nebu.common.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class PhysicalResourceWithDisks extends PhysicalResource {

    private final Map<String, PhysicalStore> disks;

    /**
     * @param id
     *            to use.
     * @param disks
     *            to set as disks.
     */
    public PhysicalResourceWithDisks(final String id, final Map<String, PhysicalStore> disks) {
        super(id);
        this.disks = disks;
    }

    /**
     * @param id
     *            to set.
     * @param disks
     *            to add.
     */
    public PhysicalResourceWithDisks(final String id, final List<PhysicalStore> disks) {
        super(id);
        this.disks = new HashMap<String, PhysicalStore>();
        for (final PhysicalStore disk : disks) {
            final PhysicalStore newDisk = new PhysicalStore(disk);
            newDisk.setParent(this);
            this.addDisk(newDisk);
        }
    }

    /**
     * @return Disks contained in the PhysicalRack.
     */
    public final List<PhysicalStore> getDisks() {
        return new ArrayList<PhysicalStore>(this.disks.values());
    }

    /**
     * @param disk
     *            the Disk to add to the PhysicalRack.
     */
    protected final void addDisk(final PhysicalStore disk) {
        if (disk != null) {
            this.disks.put(disk.getUniqueIdentifier(), disk);
        }
    }

    /**
     * @param disk
     *            the disk to remove from the PhysicalRack.
     */
    protected final void removeDisk(final PhysicalStore disk) {
        if (disk != null) {
            this.disks.remove(disk.getUniqueIdentifier());
        }
    }

}
