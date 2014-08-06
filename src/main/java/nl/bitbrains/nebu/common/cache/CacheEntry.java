package nl.bitbrains.nebu.common.cache;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents an entry for the Cachemanager. Contains a refresh mechanism to
 * ensure the cacheEntry is automatically refreshed.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public class CacheEntry {

    public static final int DEFAULT_EXPIRATION_TIME_SEC = 120;

    private Object value;
    private Calendar dt;
    private final CacheLoader<Object> cacheLoader;
    private final int expirationTime;

    /**
     * Constructor.
     * 
     * @param value
     *            to cache.
     * @param creationDate
     *            when the value parameter was obtained.
     * @param loader
     *            to use when the entry needs refreshing.
     * @param expirationTime
     *            is the number of seconds until the entry is invalidated.
     */
    protected CacheEntry(final Object value, final Calendar creationDate,
            final CacheLoader<Object> loader, final int expirationTime) {
        this.value = value;
        this.dt = creationDate;
        this.cacheLoader = loader;
        this.expirationTime = expirationTime;
    }

    /**
     * Will update the cache value if needed!
     * 
     * @return the value.
     * @throws CacheException
     *             if the updating process failed.
     */
    public final Object getValue() throws CacheException {
        if (!this.isValid()) {
            this.updateCache();
        }
        return this.value;
    }

    /**
     * Get the value without refreshing the cache even if it is invalid.
     * 
     * @return the value.
     */
    public final Object getNonRefreshedValue() {
        return this.value;
    }

    /**
     * Updates the cache.
     * 
     * @throws CacheException
     *             if the updating process fails.
     */
    private void updateCache() throws CacheException {
        this.value = this.cacheLoader.refresh();
        this.dt = Calendar.getInstance();
    }

    /**
     * Confirms the expiration time has not yet passed.
     * 
     * @return true iff the cache entry is still valid.
     */
    private boolean isValid() {
        if (this.cacheLoader == null) {
            return true;
        }
        final Date now = new Date();
        final Calendar exp = (Calendar) this.dt.clone();
        exp.add(Calendar.SECOND, this.expirationTime);
        return now.before(exp.getTime());
    }
}
