package nl.bitbrains.nebu.common.util.xml;

import java.text.ParseException;

import org.jdom2.Element;

/**
 * Interface for factory classes that can parse XML to an object of a specific
 * type.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 * @param <T>
 *            Objecttype to make a reader for.
 */
public interface XMLReader<T> {

    /**
     * @param xml
     *            the XML representation of an object.
     * @return the object as represented in XML.
     * @throws ParseException
     *             when the XML representation is invalid.
     */
    T fromXML(Element xml) throws ParseException;
}
