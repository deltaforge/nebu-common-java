package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenterBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalRack;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Converts a {@link PhysicalDataCenter} to and from an XML element.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalDataCenterFactory extends PhysicalResourceFactory implements
        TopologyFactory<PhysicalDataCenter> {

    public static final String TAG_ELEMENT_ROOT = "dataCenter";

    /**
     * Empty default constructor.
     */
    public PhysicalDataCenterFactory() {
    }

    /**
     * Converts the {@link PhysicalDataCenter} to XML.
     * 
     * @param object
     *            to convert to XML.
     * @return the created XML element.
     */
    public Element toXML(final PhysicalDataCenter object) {
        final Element dcElem = super
                .createRootXMLElement(object, PhysicalDataCenterFactory.TAG_ELEMENT_ROOT);
        super.fillXMLElement(dcElem, object.getRacks(), this.getFactories()
                .getPhysicalRackFactory());
        return dcElem;
    }

    /**
     * Creates a {@link PhysicalDataCenter} from XML.
     * 
     * @param xml
     *            element to base the object on.
     * @return the created {@link PhysicalDataCenter}.
     * @throws ParseException
     *             if XML is not valid.
     */
    public PhysicalDataCenterBuilder fromXML(final Element xml) throws ParseException {
        super.throwIfInvalidRoot(xml, PhysicalDataCenterFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttribute = super.getAttribute(xml, IdentifiableFactory.TAG_ID);
        final List<PhysicalRack> racks = super.getInternalIdentifiables(xml, this.getFactories()
                .getPhysicalRackFactory());
        final PhysicalDataCenterBuilder builder = new PhysicalDataCenterBuilder();
        builder.withRacks(racks).withUuid(idAttribute.getValue());
        return builder;
    }
}
