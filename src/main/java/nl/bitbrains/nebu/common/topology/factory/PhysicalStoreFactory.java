package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalStore;
import nl.bitbrains.nebu.common.topology.PhysicalStoreBuilder;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Converts {@link PhysicalStore} objects to and from XML.
 * 
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalStoreFactory extends PhysicalResourceFactory implements
        TopologyFactory<PhysicalStore> {

    public static final String TAG_ELEMENT_ROOT = "store";
    public static final String TAG_CAPACITY = "capacity";
    public static final String TAG_USED = "used";

    /**
     * Empty default constructor.
     */
    public PhysicalStoreFactory() {
    }

    /**
     * Converts the {@link PhysicalStore} to XML.
     * 
     * @param object
     *            to convert to XML.
     * @return the created XML element.
     */
    public Element toXML(final PhysicalStore object) {
        final Element result = super.createRootXMLElement(object,
                                                          PhysicalStoreFactory.TAG_ELEMENT_ROOT);
        final Element capacityElement = new Element(PhysicalStoreFactory.TAG_CAPACITY);
        capacityElement.setText(Long.toString(object.getCapacity()));
        result.addContent(capacityElement);
        final Element usedElement = new Element(PhysicalStoreFactory.TAG_USED);
        usedElement.setText(Long.toString(object.getUsed()));
        result.addContent(usedElement);
        return result;
    }

    /**
     * Creates a {@link PhysicalStore} from XML.
     * 
     * @param xml
     *            element to base the object on.
     * @return the created {@link PhysicalStore}.
     * @throws ParseException
     *             if XML is not valid.
     */
    public PhysicalStoreBuilder fromXML(final Element xml) throws ParseException {
        super.throwIfInvalidRoot(xml, PhysicalStoreFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttribute = super.getAttribute(xml, IdentifiableFactory.TAG_ID);
        final Element capElem = xml.getChild(PhysicalStoreFactory.TAG_CAPACITY);
        long capacity = 0;
        if (capElem != null) {
            capacity = Long.parseLong(capElem.getTextTrim());
        }

        final Element usedElem = xml.getChild(PhysicalStoreFactory.TAG_USED);
        long used = 0;
        if (usedElem != null) {
            used = Long.parseLong(usedElem.getTextTrim());
        }

        final PhysicalStoreBuilder builder = new PhysicalStoreBuilder();
        builder.withCapacity(capacity).withUsed(used).withUuid(idAttribute.getValue());
        return builder;
    }

}
