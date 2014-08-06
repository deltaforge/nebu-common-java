package nl.bitbrains.nebu.common.cache;

import java.util.Map;

import nl.bitbrains.nebu.common.cache.CacheEntry;
import nl.bitbrains.nebu.common.cache.CacheException;
import nl.bitbrains.nebu.common.cache.CacheLoader;
import nl.bitbrains.nebu.common.cache.CacheManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CacheEntry.class })
public class TestCacheManager {

    @Mock
    private CacheLoader<Object> loader;

    @Mock
    private Map<String, CacheEntry> cache;

    private CacheEntry mockedEntry;

    private final String key = "key";
    private final String expected = "expected";

    @BeforeClass
    public static void superSetUp() {
        new CacheManager() {
        };
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockedEntry = PowerMockito.mock(CacheEntry.class);
        this.setUpCacheManagerGetCacheMock();
    }

    private void setUpLoaderSuccess() throws CacheException {
        Mockito.when(this.loader.refresh()).thenReturn(this.expected);
    }

    private void setUpLoaderException(final CacheException e)
            throws CacheException {
        Mockito.when(this.loader.refresh()).thenThrow(e);
    }

    private void setUpCacheEntrySuccess(final Object t) throws CacheException {
        Mockito.when(this.mockedEntry.getValue()).thenReturn(t);
        Mockito.when(this.mockedEntry.getNonRefreshedValue()).thenReturn(t);
    }

    private void setUpCacheEntryException(final Throwable t)
            throws CacheException {
        Mockito.when(this.mockedEntry.getValue()).thenThrow(t);
    }

    private void setUpCacheManagerGetCacheMock() throws CacheException {
        CacheManager.setCache(this.cache);
    }

    private void setUpCacheKeyNotFound(final String key) {
        Mockito.when(this.cache.containsKey(key)).thenReturn(false);
        Mockito.when(this.cache.get(key)).thenReturn(null);
    }

    private void setUpCacheKeyFound(final String key) {
        Mockito.when(this.cache.containsKey(key)).thenReturn(true);
        Mockito.when(this.cache.get(key)).thenReturn(this.mockedEntry);
    }

    private void setUpCacheKeyNotFoundLaterFound(final String key) {
        Mockito.when(this.cache.containsKey(key)).thenReturn(false, true);
        Mockito.when(this.cache.get(key)).thenReturn(this.mockedEntry);
    }

    @Test
    public void testGetInvalidKeyNoLoader() throws CacheException {
        this.setUpCacheKeyNotFound(this.key);
        boolean caught = false;
        try {
            CacheManager.get(this.key);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testGetValidKeyCacheEntryError() throws CacheException {
        this.setUpCacheKeyFound(this.key);
        this.setUpCacheEntryException(new CacheException(""));
        boolean caught = false;
        try {
            CacheManager.get(this.key);
        } catch (final CacheException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testGetValidKey() throws CacheException {
        String actual = "WrongResult";
        this.setUpCacheKeyFound(this.key);
        this.setUpCacheEntrySuccess(this.expected);
        try {
            actual = (String) CacheManager.get(this.key);
        } catch (final CacheException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(this.expected, actual);
    }

    @Test
    public void testGetInvalidKeyNoRefreshNoLoader() throws CacheException {
        this.setUpCacheKeyNotFound(this.key);
        boolean caught = false;
        try {
            CacheManager.getNoRefresh(this.key);
        } catch (final IllegalArgumentException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testGetValidKeyNoRefresh() throws CacheException {
        String actual = "notexpected";
        this.setUpCacheKeyFound(this.key);
        this.setUpCacheEntrySuccess(this.expected);
        try {
            actual = (String) CacheManager.getNoRefresh(this.key);
        } catch (final IllegalArgumentException e) {
            Assert.fail();
        }
        Assert.assertEquals(this.expected, actual);
    }

    @Test
    public void testResetCache() throws CacheException {
        CacheManager.resetCache();
        Mockito.verify(this.cache).clear();
    }

    @Test
    public void testGetValidKeyWithLoaderNotFound() throws CacheException {
        String actual = "notexpected";
        this.setUpLoaderSuccess();
        this.setUpCacheKeyNotFoundLaterFound(this.key);
        this.setUpCacheEntrySuccess(this.expected);
        try {
            actual = (String) CacheManager.get(this.key, this.loader);
        } catch (final IllegalArgumentException e) {
            Assert.fail();
        }
        Assert.assertEquals(this.expected, actual);
        Mockito.verify(this.cache).put(Matchers.eq(this.key),
                                       (CacheEntry) Matchers.any());
    }

    @Test
    public void testPutValidForeverKey() throws CacheException {
        CacheManager.put(this.key, this.expected);
        Mockito.verify(this.cache).put(Matchers.eq(this.key),
                                       Matchers.any(CacheEntry.class));
    }

    @Test
    public void testGetValidKeyWithLoaderFound() throws CacheException {
        String actual = "notexpected";
        this.setUpLoaderSuccess();
        this.setUpCacheKeyFound(this.key);
        this.setUpCacheEntrySuccess(this.expected);
        try {
            actual = (String) CacheManager.get(this.key, this.loader);
        } catch (final IllegalArgumentException e) {
            Assert.fail();
        }
        Assert.assertEquals(this.expected, actual);
    }

    @Test
    public void testGetValidKeyWithLoaderException() throws CacheException {
        this.setUpLoaderException(new CacheException(""));
        this.setUpCacheKeyNotFoundLaterFound(this.key);
        this.setUpCacheEntrySuccess(this.expected);
        boolean caught = false;
        try {
            CacheManager.get(this.key, this.loader);
        } catch (final CacheException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
        Mockito.verify(this.cache).remove(this.key);
    }

    @Test
    public void testGetValidKeyWithLoaderExceptionNoRemoval()
            throws CacheException {
        this.setUpLoaderException(new CacheException(""));
        this.setUpCacheKeyNotFound(this.key);
        this.setUpCacheEntrySuccess(this.expected);
        boolean caught = false;
        try {
            CacheManager.get(this.key, this.loader);
        } catch (final CacheException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
        Mockito.verify(this.cache, Mockito.times(0)).remove(this.key);
    }

}
