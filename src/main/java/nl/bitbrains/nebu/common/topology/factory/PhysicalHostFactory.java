package nl.bitbrains.nebu.common.topology.factory;

import java.text.ParseException;
import java.util.List;

import nl.bitbrains.nebu.common.factories.IdentifiableFactory;
import nl.bitbrains.nebu.common.topology.PhysicalHost;
import nl.bitbrains.nebu.common.topology.PhysicalHostBuilder;
import nl.bitbrains.nebu.common.topology.PhysicalStore;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Converts {@link PhysicalHost} objects to and from XML.
 * 
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class PhysicalHostFactory extends PhysicalResourceFactory implements
        TopologyFactory<PhysicalHost> {

    public static final String TAG_ELEMENT_ROOT = "host";
    public static final String TAG_STORES = "stores";
    public static final String TAG_CPU_USAGE = "cpu-usage";
    public static final String TAG_MEM_USAGE = "mem-usage";

    /**
     * Empty default constructor.
     */
    public PhysicalHostFactory() {
    }

    /**
     * Converts the {@link PhysicalHost} to XML.
     * 
     * @param object
     *            to convert to XML.
     * @return the created XML element.
     */
    public Element toXML(final PhysicalHost object) {
        final Element result = super.createRootXMLElement(object,
                                                          PhysicalHostFactory.TAG_ELEMENT_ROOT);
        result.addContent(new Element(PhysicalHostFactory.TAG_CPU_USAGE).setText(Double
                .toString(object.getCpuUsage())));
        result.addContent(new Element(PhysicalHostFactory.TAG_MEM_USAGE).setText(Double
                .toString(object.getMemUsage())));
        final Element stores = new Element(PhysicalHostFactory.TAG_STORES);
        super.fillXMLElement(stores, object.getDisks(), this.getFactories()
                .getPhysicalStoreFactory());
        result.addContent(stores);
        return result;
    }

    /**
     * Creates a {@link PhysicalHost} from XML.
     * 
     * @param xml
     *            element to base the object on.
     * @return the created {@link PhysicalHost}.
     * @throws ParseException
     *             if XML is not valid.
     */
    public PhysicalHostBuilder fromXML(final Element xml) throws ParseException {
        super.throwIfInvalidRoot(xml, PhysicalHostFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttribute = super.getAttribute(xml, IdentifiableFactory.TAG_ID);
        final List<PhysicalStore> disks = super.getInternalIdentifiables(xml
                .getChild(PhysicalHostFactory.TAG_STORES), this.getFactories()
                .getPhysicalStoreFactory());
        final String stringCpuUsage = xml.getChildText(PhysicalHostFactory.TAG_CPU_USAGE);
        final String stringMemUsage = xml.getChildText(PhysicalHostFactory.TAG_MEM_USAGE);
        double cpuUsage = 0;
        if (stringCpuUsage != null) {
            cpuUsage = Double.parseDouble(stringCpuUsage);
        }
        double memUsage = 0;
        if (stringMemUsage != null) {
            memUsage = Double.parseDouble(stringMemUsage);
        }

        final PhysicalHostBuilder builder = new PhysicalHostBuilder();
        builder.withCpuUsage(cpuUsage).withMemUsage(memUsage).withDisks(disks)
                .withUuid(idAttribute.getValue());
        return builder;
    }
}
