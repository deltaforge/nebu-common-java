package nl.bitbrains.nebu.common.interfaces;

/**
 * Interface for the Builder pattern we are using.
 * 
 * @param <T>
 *            Class this builder can build.
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public interface IBuilder<T> {

    /**
     * Resets the builder after building the object.
     */
    void reset();

    /**
     * Builds an object of the class this builder is parameterized for.
     * 
     * @return the built object.
     */
    T build();
}
