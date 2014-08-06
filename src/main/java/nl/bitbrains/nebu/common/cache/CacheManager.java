package nl.bitbrains.nebu.common.cache;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class CacheManager {

    private static Map<String, CacheEntry> cache = new HashMap<String, CacheEntry>();

    private static final String NO_SUCH_KEY_TEXT = "No cache entry found for key";

    /**
     * Gets the value for the key if it exists. Will update the cache if it is
     * currently expired.
     * 
     * Throws an {@link IllegalArgumentException} if the key is not present.
     * 
     * @param key
     *            to find the value for.
     * @return the value.
     * @throws CacheException
     *             if updating the cache failed.
     */
    public static Object get(final String key) throws CacheException {
        if (CacheManager.getCache().containsKey(key)) {
            return CacheManager.getCache().get(key).getValue();
        }
        throw new IllegalArgumentException(CacheManager.NO_SUCH_KEY_TEXT);
    }

    /**
     * Puts a new value in that is valid forever.
     * 
     * @param key
     *            to use
     * @param value
     *            to use
     */
    public static void put(final String key, final Object value) {
        CacheManager.getCache().put(key,
                                    new CacheEntry(value, Calendar
                                            .getInstance(), null, 0));
    }

    /**
     * Will give the current value for the key if present. Will not refresh an
     * expired cache entry.
     * 
     * Throws an {@link IllegalArgumentException} if the key is not present.
     * 
     * @param key
     *            to get the value for.
     * @return the value.
     */
    public static Object getNoRefresh(final String key) {
        if (CacheManager.getCache().containsKey(key)) {
            return CacheManager.getCache().get(key).getNonRefreshedValue();
        }
        throw new IllegalArgumentException(CacheManager.NO_SUCH_KEY_TEXT);
    }

    /**
     * Calls {@link CacheManager#get(String, CacheLoader, int)} with the default
     * expiration time found in {@link CacheEntry}.
     * 
     * @param key
     *            to get the value for
     * @param loader
     *            to use if the value needs refreshing.
     * @return the value.
     * @throws CacheException
     *             if refreshing fails.
     */
    public static Object get(final String key, final CacheLoader<Object> loader)
            throws CacheException {
        return CacheManager.get(key, loader,
                                CacheEntry.DEFAULT_EXPIRATION_TIME_SEC);
    }

    /**
     * Will try to get the value for the key, updating it if the key has
     * expired. If updating the value fails, the cache entry will be fully
     * removed from the cache.
     * 
     * @param key
     *            to get the value for
     * @param loader
     *            to use if the value needs refreshing.
     * @param expirationTime
     *            to set as maximum time the cacheEntry can be considered valid.
     * @return the value.
     * @throws CacheException
     *             if refreshing fails.
     */
    public static Object get(final String key,
            final CacheLoader<Object> loader, final int expirationTime)
            throws CacheException {
        if (CacheManager.getCache().containsKey(key)) {
            return CacheManager.getCache().get(key).getValue();
        }
        try {
            CacheManager.getCache().put(key,
                                        new CacheEntry(loader.refresh(),
                                                Calendar.getInstance(), loader,
                                                expirationTime));

        } catch (final CacheException e1) {
            CacheManager.clearCacheEntry(key);
            throw e1;
        }
        return CacheManager.getCache().get(key).getValue();
    }

    /**
     * Completely wipes the cache.
     */
    public static void resetCache() {
        CacheManager.getCache().clear();
    }

    /**
     * Wipes a single entry of the cache.
     * 
     * @param key
     *            to wipe
     */
    public static void clearCacheEntry(final String key) {
        if (CacheManager.getCache().containsKey(key)) {
            CacheManager.getCache().remove(key);
        }
    }

    /**
     * Internal function so that tests can potentially mock the internal cache
     * object.
     * 
     * @return the map.
     */
    private static Map<String, CacheEntry> getCache() {
        return CacheManager.cache;
    }

    /**
     * Function so that tests can potentially mock the internal cache object.
     * 
     * @param cache
     *            the map to set.
     */
    protected static void setCache(final Map<String, CacheEntry> cache) {
        CacheManager.cache = cache;
    }
}
