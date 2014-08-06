package nl.bitbrains.nebu.common.cache;

/**
 * Thrown if the cache could not refreshed.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 */
public class CacheException extends Exception {

    private static final long serialVersionUID = -4533028130747294473L;

    /**
     * Simple constructor.
     * 
     * @param e
     *            this exception is based on.
     */
    public CacheException(final Throwable e) {
        super(e);
    }

    /**
     * @param message
     *            to pass on to Exception constructor.
     */
    public CacheException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            to pass on as message.
     * @param e
     *            to pass on as cause.
     */
    public CacheException(final String message, final Throwable e) {
        super(message, e);
    }
}
