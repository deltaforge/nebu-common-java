package nl.bitbrains.nebu.common.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bitbrains.nebu.common.factories.StringFactory;
import nl.bitbrains.nebu.common.interfaces.IBuilder;
import nl.bitbrains.nebu.common.util.xml.XMLConverter;
import nl.bitbrains.nebu.common.util.xml.XMLReader;
import nl.bitbrains.nebu.common.util.xml.XMLWriter;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Document;

/**
 * Tests the {@link XMLConverter} class.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class TestXMLConverter {

    @Mock
    XMLWriter<String> writerMock;
    @Mock
    XMLReader<IBuilder<String>> readerMock;
    @Mock
    XMLReader<IBuilder<String>> keyReaderMock;

    private final String key = "key";
    private final String value = "value";

    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.writerMock.toXML(Matchers.anyString())).thenAnswer(new Answer<Element>() {

            @Override
            public Element answer(final InvocationOnMock invocation) throws Throwable {
                return new Element(XMLConverter.TAG_ITEM);
            }
        });
        Mockito.when(this.readerMock.fromXML(Matchers.any(Element.class)))
                .thenReturn(new StringFactory.Builder().withString(this.value));
        Mockito.when(this.keyReaderMock.fromXML(Matchers.any(Element.class)))
                .thenAnswer(new Answer<StringFactory.Builder>() {

                    private int cnt = 0;

                    @Override
                    public StringFactory.Builder answer(final InvocationOnMock invocation)
                            throws Throwable {
                        return new StringFactory.Builder().withString(TestXMLConverter.this.key
                                + this.cnt++);
                    }
                });
    }

    @Test
    public void testConvertJDOMElementW3CDocument() throws JDOMException {
        final String name = "rootname";
        final Element elem = new Element(name);
        final Document doc = XMLConverter.convertJDOMElementW3CDocument(elem);
        Assert.assertEquals(name, doc.getFirstChild().getNodeName());
    }

    @Test
    public void testConvertW3CDocumentJDOMElement() throws JDOMException {
        final String name = "rootname";
        final Element to = new Element(name);
        final Document doc = XMLConverter.convertJDOMElementW3CDocument(to);
        final Element andBack = XMLConverter.convertW3CDocumentJDOMElement(doc);
        Assert.assertEquals(to.getName(), andBack.getName());
    }

    @Test
    public void testconvertCollectionToJDOMElement() {
        boolean caught = false;
        try {
            XMLConverter.convertCollectionToJDOMElement(null, null);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testConvertList() {
        final int num = 5;
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < num; i++) {
            list.add("string" + i);
        }
        final Element elem = XMLConverter.convertCollectionToJDOMElement(list, this.writerMock);
        Assert.assertEquals(XMLConverter.TAG_LIST, elem.getName());
        Assert.assertEquals(num, elem.getChildren(XMLConverter.TAG_ITEM).size());
    }

    @Test
    public void testConvertListOwnName() {
        final int num = 5;
        final String name = "name";
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < num; i++) {
            list.add("string" + i);
        }
        final Element elem = XMLConverter.convertCollectionToJDOMElement(list,
                                                                         this.writerMock,
                                                                         name,
                                                                         XMLConverter.TAG_ITEM);
        Assert.assertEquals(name, elem.getName());
        Assert.assertEquals(num, elem.getChildren(XMLConverter.TAG_ITEM).size());
    }

    @Test
    public void parseList() throws ParseException {
        final int num = 5;
        final Element elem = new Element(XMLConverter.TAG_LIST);
        for (int i = 0; i < num; i++) {
            final Element item = new Element(XMLConverter.TAG_ITEM);
            item.setText(this.value);
            elem.addContent(item);
        }
        final List<String> list = XMLConverter.convertJDOMElementToList(elem, this.readerMock);
        Assert.assertEquals(num, list.size());
        Assert.assertEquals(this.value, list.get(0));
    }

    @Test
    public void convertMap() {
        final int num = 5;
        final Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < num; i++) {
            map.put(this.key + i, this.value);
        }
        final Element elem = XMLConverter.convertMapToJDOMElement(map,
                                                                  this.writerMock,
                                                                  this.writerMock);
        Assert.assertEquals(XMLConverter.TAG_MAP, elem.getName());
        Assert.assertEquals(num, elem.getChildren(XMLConverter.TAG_MAP_ENTRY).size());
        Assert.assertEquals(1,
                            elem.getChildren(XMLConverter.TAG_MAP_ENTRY).get(0)
                                    .getChildren(XMLConverter.TAG_MAP_ENTRY_KEY).size());
        Assert.assertEquals(1,
                            elem.getChildren(XMLConverter.TAG_MAP_ENTRY).get(0)
                                    .getChildren(XMLConverter.TAG_MAP_ENTRY_VALUE).size());
    }

    @Test
    public void parseMap() throws ParseException {
        final int num = 5;
        final Element elem = new Element(XMLConverter.TAG_MAP);
        for (int i = 0; i < num; i++) {
            final Element entryElem = new Element(XMLConverter.TAG_MAP_ENTRY);
            final Element keyElem = new Element(XMLConverter.TAG_MAP_ENTRY_KEY);
            final Element valueElem = new Element(XMLConverter.TAG_MAP_ENTRY_VALUE);
            entryElem.addContent(keyElem).addContent(valueElem);
            keyElem.addContent(this.key + i);
            valueElem.addContent(this.value);
            elem.addContent(entryElem);
        }
        final Map<String, String> map = XMLConverter.convertJDOMElementToMap(elem,
                                                                             this.keyReaderMock,
                                                                             this.readerMock);
        Assert.assertEquals(num, map.size());
        Assert.assertEquals(this.value, map.get(this.key + 0));
    }

    @Test
    public void lastBits() {
        // Required to get 100% coverage ;)
        new XMLConverter() {
        };
    }
}
