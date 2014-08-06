package nl.bitbrains.nebu.common.topology;

import nl.bitbrains.nebu.common.interfaces.Identifiable;

/**
 * This abstract class represents any entity in the physical topology of a cloud
 * hosting environment. It implements Identifiable to ensure each physical
 * entity has a unique identifier in the topology representation.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public abstract class PhysicalResource implements Identifiable {

    public static final String UUID_NAME = "uuid";

    private String uuid;

    /**
     * @param id
     *            unique identifier of the new PhysicalResource.
     */
    public PhysicalResource(final String id) {
        this.setUniqueIdentifier(id);
    }

    /**
     * @return the unique identifier of the PhysicalResource.
     */
    public String getUniqueIdentifier() {
        return this.uuid;
    }

    /**
     * @param uuid
     *            the unique identifier of the PhysicalResource.
     */
    private void setUniqueIdentifier(final String uuid) {
        this.uuid = uuid;
    }

}
