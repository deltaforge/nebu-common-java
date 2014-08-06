package nl.bitbrains.nebu.common.factories;

import java.text.ParseException;

import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.util.xml.XMLFactory;

import org.jdom2.Element;

/**
 * A simple factory that can write Integers to and from XML.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class IntegerFactory implements XMLFactory<Integer> {

    public static final String TAG_INT = "integer";

    /**
     * Builder for Integers.
     * 
     * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
     * 
     */
    public static class Builder implements IBuilder<Integer> {
        private int number;

        /**
         * Default constructor.
         */
        public Builder() {
            this.reset();
        }

        @Override
        public final void reset() {
            this.number = 0;
        }

        /**
         * @param num
         *            to use in building.
         * @return this for fluency
         */
        public Builder withNumber(final int num) {
            this.number = num;
            return this;
        }

        @Override
        public Integer build() {
            final Integer result = Integer.valueOf(this.number);
            this.reset();
            return result;
        }
    }

    /**
     * Default empty constructor.
     */
    public IntegerFactory() {

    }

    @Override
    public Element toXML(final Integer i) {
        final Element elem = new Element(IntegerFactory.TAG_INT);
        elem.addContent(Integer.toString(i));
        return elem;
    }

    @Override
    public IntegerFactory.Builder fromXML(final Element xml) throws ParseException {
        return new IntegerFactory.Builder().withNumber(Integer.parseInt(xml.getValue()));
    }
}
