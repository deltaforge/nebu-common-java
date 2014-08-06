package nl.bitbrains.nebu.common.util.xml;

import nl.bitbrains.nebu.common.interfaces.IBuilder;

/**
 * Interface for factory classes that can convert objects of a specific type to
 * XML and vice versa.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 * @param <T>
 *            Objecttype to make a factory for.
 */
public interface XMLFactory<T> extends XMLReader<IBuilder<T>>, XMLWriter<T> {

}
