package nl.bitbrains.nebu.common;

import java.text.ParseException;

import nl.bitbrains.nebu.common.factories.StringFactory;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestStringFactory {

    private StringFactory factory;

    @Before
    public void setUp() {
        this.factory = new StringFactory();
    }

    @Test
    public void testEmptyString() {
        final String empty = "";
        final Element elem = this.factory.toXML(empty);

        Assert.assertEquals(empty, elem.getText());
    }

    @Test
    public void testNonEmptyString() {
        final String test = "test";
        final Element elem = this.factory.toXML(test);

        Assert.assertEquals(test, elem.getText());
    }

    @Test
    public void testEmptyElement() throws ParseException {
        final Element elem = new Element(StringFactory.TAG_STRING);
        final String empty = "";
        elem.setText(empty);

        Assert.assertEquals(empty, this.factory.fromXML(elem).build());
    }

    @Test
    public void testNonEmptyElement() throws ParseException {
        final Element elem = new Element(StringFactory.TAG_STRING);
        final String notEmpty = "test";
        elem.setText(notEmpty);

        Assert.assertEquals(notEmpty, this.factory.fromXML(elem).build());
    }
}
