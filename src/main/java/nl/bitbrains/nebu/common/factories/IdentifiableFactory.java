package nl.bitbrains.nebu.common.factories;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.interfaces.Identifiable;
import nl.bitbrains.nebu.common.util.ErrorChecker;
import nl.bitbrains.nebu.common.util.xml.XMLReader;
import nl.bitbrains.nebu.common.util.xml.XMLWriter;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Base class that can convert a {@link Identifiable} to and from xml.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class IdentifiableFactory {
    public static final String TAG_ID = "id";

    /**
     * Creates an XML element for the physical resource.
     * 
     * @param object
     *            {@link Identifiable} to convert.
     * @param elementName
     *            to use as tag name.
     * @return the element.
     */
    protected final Element createRootXMLElement(final Identifiable object, final String elementName) {
        ErrorChecker.throwIfNullArgument(object, "object");

        final Element rootElem = new Element(elementName);
        final Attribute idAttrib = new Attribute(IdentifiableFactory.TAG_ID,
                object.getUniqueIdentifier());
        rootElem.setAttribute(idAttrib);
        return rootElem;
    }

    /**
     * Fills an XML element with children tags for its {@link Identifiable}
     * objects, using the given factory.
     * 
     * @param rootElement
     *            to add the children to.
     * @param objects
     *            list of {@link Identifiable} objects to convert and add.
     * @param factory
     *            to convert the children.
     * @param <T>
     *            {@link Identifiable} type to use.
     * @return rootElement for fluency.
     */
    protected final <T extends Identifiable> Element fillXMLElement(final Element rootElement,
            final List<T> objects, final XMLWriter<T> factory) {
        for (final T res : objects) {
            rootElement.addContent(factory.toXML(res));
        }
        return rootElement;
    }

    /**
     * Checks if the root is of the righ tname, if not throws an exception.
     * 
     * @param xml
     *            to check.
     * @param elementName
     *            should match the xml's name.
     * @throws ParseException
     *             if they do not match.
     */
    protected final void throwIfInvalidRoot(final Element xml, final String elementName)
            throws ParseException {
        ErrorChecker.throwIfNullArgument(xml, "xml");
        if (!xml.getName().equals(elementName)) {
            throw new ParseException("Expected element with name '" + elementName + "', got '"
                    + xml.getName() + "'.", -1);
        }
    }

    /**
     * Gets an attribute from an XML element.
     * 
     * @param xml
     *            element to get the attribute out of.
     * @param attributeName
     *            name of the attribute to get.
     * @return the gotten attribute.
     * @throws ParseException
     *             if attribute does not exist.
     */
    protected final Attribute getAttribute(final Element xml, final String attributeName)
            throws ParseException {
        final Attribute attribute = xml.getAttribute(attributeName);
        if (attribute == null) {
            throw new ParseException("Missing required attribute " + attributeName + ".", -1);
        }
        return attribute;
    }

    /**
     * Gets all the children of the xmltag and converts them to the
     * corresponding {@link Identifiable} class with the given factory.
     * 
     * @param xmlRoot
     *            to get the children out of.
     * @param factory
     *            to convert the xml elements.
     * @param <T>
     *            type of element to convert.
     * @return a list of converted objects.
     * @throws ParseException
     *             if the objects can not be parsed.
     */
    protected final <T extends Identifiable> List<T> getInternalIdentifiables(
            final Element xmlRoot, final XMLReader<IBuilder<T>> factory) throws ParseException {
        final List<T> result = new ArrayList<T>();
        for (final Element element : xmlRoot.getChildren()) {
            result.add(factory.fromXML(element).build());
        }
        return result;
    }

    /**
     * Gets all the children of the xmltag and converts them to the
     * corresponding {@link Identifiable} class with the given factory.
     * 
     * @param xmlRoot
     *            to get the children out of.
     * @param factory
     *            to convert the xml elements.
     * @param childTagName
     *            name of the children to get.
     * @param <T>
     *            type of element to convert.
     * @return a list of converted objects.
     * @throws ParseException
     *             if the objects can not be parsed.
     */
    protected final <T extends Identifiable> List<T> getInternalIdentifiables(
            final Element xmlRoot, final XMLReader<IBuilder<T>> factory, final String childTagName)
            throws ParseException {
        final List<T> result = new ArrayList<T>();
        for (final Element element : xmlRoot.getChildren(childTagName)) {
            result.add(factory.fromXML(element).build());
        }
        return result;
    }

}
