package nl.bitbrains.nebu.common.util.xml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.util.ErrorChecker;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.DOMOutputter;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class XMLConverter {

    public static final String TAG_LIST = "list";
    public static final String TAG_ITEM = "item";
    public static final String TAG_MAP = "map";
    public static final String TAG_MAP_ENTRY = "entry";
    public static final String TAG_MAP_ENTRY_KEY = "key";
    public static final String TAG_MAP_ENTRY_VALUE = "value";

    /**
     * Converts a JDOM element to a W3C Document.
     * 
     * @param elem
     *            to convert.
     * @return the converted document.
     * @throws JDOMException
     *             if it can not be converted.
     */
    public static org.w3c.dom.Document convertJDOMElementW3CDocument(final org.jdom2.Element elem)
            throws JDOMException {
        final DOMOutputter converter = new DOMOutputter();
        final Element elem2 = elem.clone();
        final org.jdom2.Document jdomDoc = new Document(elem2);
        return converter.output(jdomDoc);
    }

    /**
     * @param document
     *            document to convert
     * @return the converted element.
     */
    public static Element convertW3CDocumentJDOMElement(final org.w3c.dom.Document document) {
        final org.jdom2.input.DOMBuilder converter = new DOMBuilder();
        return converter.build(document).getRootElement();
    }

    /**
     * Converts a Collection to a JDOM element.
     * 
     * @param list
     *            to convert.
     * @param factory
     *            to use in converting individual elements.
     * @param <T>
     *            type of the elements in the list.
     * @return the converted element.
     */
    public static <T> Element convertCollectionToJDOMElement(final Collection<T> list,
            final XMLWriter<T> factory) {
        return XMLConverter.convertCollectionToJDOMElement(list,
                                                           factory,
                                                           XMLConverter.TAG_LIST,
                                                           XMLConverter.TAG_ITEM);
    }

    /**
     * Converts a Collection to a JDOM element.
     * 
     * @param list
     *            to convert.
     * @param factory
     *            to use in converting individual elements.
     * @param rootElem
     *            name of root element.
     * @param item
     *            name of item elements
     * @param <T>
     *            type of the elements in the list.
     * @return the converted element.
     */
    public static <T> Element convertCollectionToJDOMElement(final Collection<T> list,
            final XMLWriter<T> factory, final String rootElem, final String item) {
        ErrorChecker.throwIfNullArgument(list, rootElem);
        final Element elem = new Element(rootElem);
        for (final T object : list) {
            elem.addContent(factory.toXML(object));
        }
        return elem;
    }

    /**
     * Convert a JDOM list to a Java list.
     * 
     * @param xml
     *            to convert.
     * @param factory
     *            to use in conversion.
     * @param <T>
     *            the type to place in the list.
     * @return the parsed list.
     * @throws ParseException
     *             if one element can not be parsed.
     */
    public static <T> List<T> convertJDOMElementToList(final Element xml,
            final XMLReader<IBuilder<T>> factory) throws ParseException {
        ErrorChecker.throwIfNullArgument(xml, "xml");
        ErrorChecker.throwIfNullArgument(factory, "factory");
        final List<T> list = new ArrayList<T>();
        for (final Element child : xml.getChildren()) {
            list.add(factory.fromXML(child).build());
        }
        return list;
    }

    /**
     * Convert a {@link Map} to an {@link Element}.
     * 
     * @param map
     *            The map to convert.
     * @param keyFactory
     *            The {@link XMLWriter} that should be used to convert the keys
     *            to XML.
     * @param valFactory
     *            The {@link XMLWriter} that should be used to convert the
     *            values to XML.
     * @param <K>
     *            Key type of map.
     * @param <V>
     *            Value type of map.
     * @return An XML {@link Element} that represents the original {@link Map}.
     */
    public static <K, V> Element convertMapToJDOMElement(final Map<K, V> map,
            final XMLWriter<K> keyFactory, final XMLWriter<V> valFactory) {
        ErrorChecker.throwIfNullArgument(map, "map");
        ErrorChecker.throwIfNullArgument(keyFactory, "key factory");
        ErrorChecker.throwIfNullArgument(valFactory, "value factory");

        final Element root = new Element(XMLConverter.TAG_MAP);

        for (final Entry<K, V> entry : map.entrySet()) {
            final Element entryElem = new Element(XMLConverter.TAG_MAP_ENTRY);
            final Element entryKey = new Element(XMLConverter.TAG_MAP_ENTRY_KEY);
            final Element entryVal = new Element(XMLConverter.TAG_MAP_ENTRY_VALUE);
            final Element key = keyFactory.toXML(entry.getKey());
            final Element val = valFactory.toXML(entry.getValue());

            root.addContent(entryElem);
            entryElem.addContent(entryKey);
            entryElem.addContent(entryVal);
            entryKey.addContent(key);
            entryVal.addContent(val);
        }

        return root;
    }

    /**
     * Convert an JDOM {@link Element} to a {@link Map}.
     * 
     * @param elem
     *            The {@link Element} to convert.
     * @param keyFactory
     *            The {@link XMLReader} that should be used for the keys.
     * @param valFactory
     *            The {@link XMLReader} that should be used for the values.
     * @param <K>
     *            Key type of map.
     * @param <V>
     *            Value type of map.
     * @return A {@link Map} representation of the given {@link Element}.
     * @throws ParseException
     *             when the given {@link Element} has an unexpected format.
     */
    public static <K, V> Map<K, V> convertJDOMElementToMap(final Element elem,
            final XMLReader<IBuilder<K>> keyFactory, final XMLReader<IBuilder<V>> valFactory)
            throws ParseException {
        ErrorChecker.throwIfNullArgument(elem, "element");
        ErrorChecker.throwIfNullArgument(keyFactory, "key factory");
        ErrorChecker.throwIfNullArgument(valFactory, "value factory");

        final Map<K, V> map = new HashMap<K, V>();

        for (final Element entry : elem.getChildren()) {
            final K key = keyFactory.fromXML(entry.getChild(XMLConverter.TAG_MAP_ENTRY_KEY))
                    .build();
            final V val = valFactory.fromXML(entry.getChild(XMLConverter.TAG_MAP_ENTRY_VALUE))
                    .build();
            map.put(key, val);
        }

        return map;
    }
}
