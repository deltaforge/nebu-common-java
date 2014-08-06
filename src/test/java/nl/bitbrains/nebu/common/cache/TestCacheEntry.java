package nl.bitbrains.nebu.common.cache;

import java.util.Calendar;

import nl.bitbrains.nebu.common.cache.CacheEntry;
import nl.bitbrains.nebu.common.cache.CacheException;
import nl.bitbrains.nebu.common.cache.CacheLoader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class TestCacheEntry {

    @Mock
    private CacheLoader<Object> loader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void setUpLoaderMock(final Object t) throws CacheException {
        Mockito.when(this.loader.refresh()).thenReturn(t);
    }

    @Test
    public void testCacheEntryCachesCorrectly() throws CacheException {
        this.setUpLoaderMock("Hallo");
        final String expected = "hoi";
        final CacheEntry testing = new CacheEntry("hoi",
                Calendar.getInstance(), this.loader, 5);
        Assert.assertEquals(expected, testing.getValue());
    }

    @Test
    public void testCacheEntryCacheExpires() throws CacheException,
            InterruptedException {
        final String expected = "hoi";
        final String expected2 = "hallo";
        this.setUpLoaderMock(expected2);
        final int time = 1;
        final CacheEntry testing = new CacheEntry("hoi",
                Calendar.getInstance(), this.loader, 1);
        Assert.assertEquals(expected, testing.getValue());
        Thread.sleep(time * 1100);
        Assert.assertEquals(expected2, testing.getValue());
    }

    @Test
    public void testExpiredCacheEntryCallsRefresh() throws CacheException {
        this.setUpLoaderMock("Hallo");
        final CacheEntry testing = new CacheEntry("hoi",
                Calendar.getInstance(), this.loader, -5);
        testing.getValue();
        Mockito.verify(this.loader).refresh();
    }

    @Test
    public void testNonExpiredCacheEntryDoesNotCallRefresh()
            throws CacheException {
        final CacheEntry testing = new CacheEntry("hoi",
                Calendar.getInstance(), this.loader, 5000);
        testing.getValue();
        Mockito.verify(this.loader, Mockito.times(0)).refresh();
    }

    @Test
    public void testNonRefreshedDoesNotCallRefresh() throws CacheException {
        final CacheEntry testing = new CacheEntry("hoi",
                Calendar.getInstance(), this.loader, -5);
        testing.getNonRefreshedValue();
        Mockito.verify(this.loader, Mockito.times(0)).refresh();
    }

    @Test
    public void testNullLoaderDoesNotCallRefresh() throws CacheException {
        final CacheEntry testing = new CacheEntry("hoi",
                Calendar.getInstance(), null, -5);
        testing.getValue();
        Mockito.verify(this.loader, Mockito.times(0)).refresh();
    }
}
