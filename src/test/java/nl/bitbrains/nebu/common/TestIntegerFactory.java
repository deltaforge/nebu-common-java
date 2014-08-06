package nl.bitbrains.nebu.common;

import java.text.ParseException;

import nl.bitbrains.nebu.common.factories.IntegerFactory;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestIntegerFactory {
    private IntegerFactory factory;

    @Before
    public void setUp() {
        this.factory = new IntegerFactory();
    }

    @Test
    public void testZero() {
        final Integer i = 0;
        final Element elem = this.factory.toXML(i);

        Assert.assertEquals(i.intValue(), Integer.parseInt(elem.getText()));
    }

    @Test
    public void testNonZero() {
        final Integer i = 32151234;
        final Element elem = this.factory.toXML(i);

        Assert.assertEquals(i.intValue(), Integer.parseInt(elem.getText()));
    }

    @Test
    public void testNegative() {
        final Integer i = -231904581;
        final Element elem = this.factory.toXML(i);

        Assert.assertEquals(i.intValue(), Integer.parseInt(elem.getText()));
    }

    @Test(expected = NumberFormatException.class)
    public void testEmptyElem() throws ParseException {
        final String empty = "";
        final Element elem = new Element(IntegerFactory.TAG_INT);
        elem.setText(empty);

        this.factory.fromXML(elem);
    }

    @Test
    public void testZeroElem() throws ParseException {
        final String empty = "0";
        final Element elem = new Element(IntegerFactory.TAG_INT);
        elem.setText(empty);

        Assert.assertEquals(0, this.factory.fromXML(elem).build().intValue());
    }

    @Test
    public void testNonZeroElem() throws ParseException {
        final String number = "1254314";
        final Element elem = new Element(IntegerFactory.TAG_INT);
        elem.setText(number);

        Assert.assertEquals(Integer.parseInt(number), this.factory.fromXML(elem).build().intValue());
    }

    @Test
    public void testNegativeElem() throws ParseException {
        final String number = "-1254314";
        final Element elem = new Element(IntegerFactory.TAG_INT);
        elem.setText(number);

        Assert.assertEquals(Integer.parseInt(number), this.factory.fromXML(elem).build().intValue());
    }
}
