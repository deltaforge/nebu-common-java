package nl.bitbrains.nebu.common.util.xml;

import org.jdom2.Element;

/**
 * Interface for factory classes that can convert an object to its XML
 * representation.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 * @param <T>
 *            Objecttype to make a writer for.
 */
public interface XMLWriter<T> {

    /**
     * @param object
     *            the object to convert to XML.
     * @return an XML representation of object.
     */
    Element toXML(T object);
}
