package nl.bitbrains.nebu.common.factories;

import java.text.ParseException;

import nl.bitbrains.nebu.common.VirtualMachine;
import nl.bitbrains.nebu.common.VirtualMachineBuilder;
import nl.bitbrains.nebu.common.VirtualMachine.Status;
import nl.bitbrains.nebu.common.topology.factory.PhysicalHostFactory;
import nl.bitbrains.nebu.common.topology.factory.PhysicalStoreFactory;
import nl.bitbrains.nebu.common.util.xml.XMLFactory;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Converts {@link VirtualMachine} objects to and from XML.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class VirtualMachineFactory extends IdentifiableFactory implements
        XMLFactory<VirtualMachine> {

    public static final String TAG_ELEMENT_ROOT = "virtualmachine";
    public static final String TAG_LIST_ELEMENT_ROOT = "virtualmachines";
    public static final String TAG_HOSTNAME = "hostname";
    public static final String TAG_STATUS = "status";
    public static final String TAG_HOST = PhysicalHostFactory.TAG_ELEMENT_ROOT;
    public static final String TAG_DISK = PhysicalStoreFactory.TAG_ELEMENT_ROOT;

    private final boolean extensive;

    /**
     * Empty default constructor.
     */
    public VirtualMachineFactory() {
        this(true);
    }

    /**
     * @param extensive
     *            indicates whether or not the full xml should be written/read.
     */
    public VirtualMachineFactory(final boolean extensive) {
        this.extensive = extensive;
    }

    /**
     * Converts the {@link VirtualMachine} to XML.
     * 
     * @param object
     *            to convert to XML.
     * @return the created XML element.
     */
    public final Element toXML(final VirtualMachine object) {
        final Element elem = super.createRootXMLElement(object,
                                                        VirtualMachineFactory.TAG_ELEMENT_ROOT);
        if (this.extensive) {
            elem.addContent(new Element(VirtualMachineFactory.TAG_HOSTNAME).addContent(object
                    .getHostname()));
            elem.addContent(new Element(VirtualMachineFactory.TAG_STATUS).addContent(object
                    .getStatus().toString()));
            elem.addContent(new Element(VirtualMachineFactory.TAG_HOST)
                    .setAttribute(IdentifiableFactory.TAG_ID, object.getHost()));
            for (final String store : object.getStores()) {
                elem.addContent(new Element(VirtualMachineFactory.TAG_DISK)
                        .setAttribute(IdentifiableFactory.TAG_ID, store));
            }
        }
        return elem;
    }

    /**
     * Creates a {@link VirtualMachine} from XML.
     * 
     * @param xml
     *            element to base the object on.
     * @return the created {@link VirtualMachine}.
     * @throws ParseException
     *             if XML is not valid.
     */
    public final VirtualMachineBuilder fromXML(final Element xml) throws ParseException {
        super.throwIfInvalidRoot(xml, VirtualMachineFactory.TAG_ELEMENT_ROOT);
        final Attribute idAttribute = super.getAttribute(xml, IdentifiableFactory.TAG_ID);
        final VirtualMachineBuilder builder = new VirtualMachineBuilder();
        builder.withUuid(idAttribute.getValue());
        if (this.extensive) {
            builder.withHostname(xml.getChildText(VirtualMachineFactory.TAG_HOSTNAME))
                    .withStatus(Status.valueOf(xml.getChildText(VirtualMachineFactory.TAG_STATUS)))
                    .withHost(xml.getChild(VirtualMachineFactory.TAG_HOST)
                            .getAttributeValue(IdentifiableFactory.TAG_ID));
            for (final Element elem : xml.getChildren(VirtualMachineFactory.TAG_DISK)) {
                builder.withDisk(elem.getAttributeValue(IdentifiableFactory.TAG_ID));
            }
        }
        return builder;
    }
}
