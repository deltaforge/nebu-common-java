package nl.bitbrains.nebu.common.cache;


/**
 * Interface to use for refreshing caches.
 * 
 * @param <T>
 *            Type of object the CacheLoader returns, most often just used as
 *            Object.
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public interface CacheLoader<T> {

    /**
     * Gets a new version of the cache object.
     * 
     * @return the new content for the cache.
     * @throws CacheException
     *             if updating the cache failed.
     */
    T refresh() throws CacheException;
}
