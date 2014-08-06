package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalDataCenter;
import nl.bitbrains.nebu.common.topology.PhysicalRoot;
import nl.bitbrains.nebu.common.topology.PhysicalRootBuilder;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Converts a {@link PhysicalRoot} to and from XML.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalRootFactory extends PhysicalResourceFactory implements
        TopologyFactory<PhysicalRoot> {

    public static final String TAG_ELEMENT_ROOT = "root";

    /**
     * Empty default constructor.
     */
    public PhysicalRootFactory() {

    }

    /**
     * Converts the {@link PhysicalRoot} to XML.
     * 
     * @param object
     *            to convert to XML.
     * @return the created XML element.
     */
    public Element toXML(final PhysicalRoot object) {
        final Element rootElem = super.createRootXMLElement(object,
                                                            PhysicalRootFactory.TAG_ELEMENT_ROOT);
        super.fillXMLElement(rootElem, object.getDataCenters(), this.getFactories()
                .getPhysicalDataCenterFactory());
        return rootElem;
    }

    /**
     * Creates a {@link PhysicalRoot} from XML.
     * 
     * @param xml
     *            element to base the object on.
     * @return the created {@link PhysicalRoot}.
     * @throws ParseException
     *             if XML is not valid.
     */
    public PhysicalRootBuilder fromXML(final Element xml) throws ParseException {
        super.throwIfInvalidRoot(xml, PhysicalRootFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttribute = super.getAttribute(xml, IdentifiableFactory.TAG_ID);

        final List<PhysicalDataCenter> dcs = super.getInternalIdentifiables(xml, this
                .getFactories().getPhysicalDataCenterFactory());

        final PhysicalRootBuilder builder = new PhysicalRootBuilder();
        builder.withDataCenters(dcs).withUuid(idAttribute.getValue());
        return builder;
    }

}
