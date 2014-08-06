package nl.bitbrains.nebu.common.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class UUIDGenerator {

    private static final int NUM_BITS = 120;
    private static final int RADIX = 32;

    /**
     * Generates a new uuid.
     * 
     * @param prefix
     *            to use in the uuid.
     * @return the generated uuid.
     */
    public static String generate(final String prefix) {
        final SecureRandom random = new SecureRandom();
        return prefix
                + new BigInteger(UUIDGenerator.NUM_BITS, random).toString(UUIDGenerator.RADIX);
    }
}
