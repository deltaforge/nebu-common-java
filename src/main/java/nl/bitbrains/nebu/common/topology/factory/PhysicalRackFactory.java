package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalRack;
import nl.bitbrains.nebu.common.topology.PhysicalRackBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalStore;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Factory that can be used to create {@link PhysicalRack} objects.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalRackFactory extends PhysicalResourceFactory implements
        TopologyFactory<PhysicalRack> {

    public static final String TAG_ELEMENT_ROOT = "rack";

    /**
     * Empty default constructor.
     */
    public PhysicalRackFactory() {

    }

    /**
     * Converts the {@link PhysicalRack} to XML.
     * 
     * @param object
     *            to convert to XML.
     * @return the created XML element.
     */
    public Element toXML(final PhysicalRack object) {

        final Element rackElem = super.createRootXMLElement(object,
                                                            PhysicalRackFactory.TAG_ELEMENT_ROOT);
        super.fillXMLElement(rackElem, object.getCPUs(), this.getFactories()
                .getPhysicalCPUFactory());
        super.fillXMLElement(rackElem, object.getDisks(), this.getFactories()
                .getPhysicalStoreFactory());
        return rackElem;
    }

    /**
     * Creates a {@link PhysicalRack} from XML.
     * 
     * @param xml
     *            element to base the object on.
     * @return the created {@link PhysicalRack}.
     * @throws ParseException
     *             if XML is not valid.
     */
    public PhysicalRackBuilder fromXML(final Element xml) throws ParseException {
        super.throwIfInvalidRoot(xml, PhysicalRackFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttribute = super.getAttribute(xml, IdentifiableFactory.TAG_ID);
        final List<PhysicalHost> hosts = super.getInternalIdentifiables(xml, this.getFactories()
                .getPhysicalCPUFactory(), PhysicalHostFactory.TAG_ELEMENT_ROOT);
        final List<PhysicalStore> disks = super.getInternalIdentifiables(xml, this.getFactories()
                .getPhysicalStoreFactory(), PhysicalStoreFactory.TAG_ELEMENT_ROOT);
        final PhysicalRackBuilder builder = new PhysicalRackBuilder();
        builder.withHosts(hosts).withDisks(disks).withUuid(idAttribute.getValue());
        return builder;
    }

}
