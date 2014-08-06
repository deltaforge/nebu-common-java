package nl.bitbrains.nebu.common.cache;

import nl.bitbrains.nebu.common.cache.CacheException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class TestCacheException {

    @Test
    public void testStringConstructor() {
        final String string = "message";
        final CacheException e = new CacheException(string);
        Assert.assertEquals(string, e.getMessage());
    }

    @Test
    public void testExceptionConstructor() {
        final Exception e = new IllegalArgumentException();
        final CacheException ce = new CacheException(e);
        Assert.assertEquals(e, ce.getCause());
    }

    @Test
    public void testMessageAndExceptionConstructor() {
        final Exception e = new IllegalArgumentException();
        final String string = "message";
        final CacheException ce = new CacheException(string, e);
        Assert.assertEquals(e, ce.getCause());
        Assert.assertEquals(string, ce.getMessage());
    }
}
