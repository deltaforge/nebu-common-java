package nl.bitbrains.nebu.common.interfaces;

/**
 * This interface allows objects to be uniquely identified by a UUID.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public interface Identifiable {
    String UUID_NAME = "uuid";

    /**
     * @return ID that uniquely identifies this object.
     */
    String getUniqueIdentifier();
}
