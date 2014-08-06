package nl.bitbrains.nebu.common.util;

import nl.bitbrains.nebu.common.util.UUIDGenerator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class TestUUIDGenerator {

    @BeforeClass
    public static void beforeClass() {
        // Just for coverage...
        new UUIDGenerator() {

        };
    }

    @Test
    public void testNotNull() {
        Assert.assertNotNull(UUIDGenerator.generate("hallo"));
    }

    @Test
    public void testPrefix() {
        final String prefix = "prefix";
        Assert.assertTrue(UUIDGenerator.generate(prefix).contains(prefix));
    }
}
