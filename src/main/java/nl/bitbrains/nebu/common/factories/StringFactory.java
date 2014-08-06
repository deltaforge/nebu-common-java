package nl.bitbrains.nebu.common.factories;

import java.text.ParseException;

import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.util.xml.XMLFactory;

import org.jdom2.Element;

/**
 * A simple factory that can write Strings to and from XML.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class StringFactory implements XMLFactory<String> {

    public static final String TAG_STRING = "string";

    /**
     * Builder for Strings.
     * 
     * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
     * 
     */
    public static class Builder implements IBuilder<String> {
        private String string;

        /**
         * Default constructor.
         */
        public Builder() {
            this.reset();
        }

        @Override
        public final void reset() {
            this.string = null;
        }

        /**
         * @param string
         *            to use in building.
         * @return this for fluency
         */
        public Builder withString(final String string) {
            this.string = string;
            return this;
        }

        @Override
        public String build() {
            final String result = this.string;
            this.reset();
            return result;
        }
    }

    /**
     * Empty Default constructor.
     */
    public StringFactory() {

    }

    @Override
    public Element toXML(final String object) {
        final Element elem = new Element(StringFactory.TAG_STRING);
        elem.addContent(object);
        return elem;
    }

    @Override
    public StringFactory.Builder fromXML(final Element xml) throws ParseException {
        return new Builder().withString(xml.getText());
    }

}
