package nl.bitbrains.nebu.common.util;

/**
 * Contains simple tests for argument and field checking.
 * 
 * @author Jesse Donkervliet, Tim Hegeman, and Stefan Hugtenburg
 * 
 */
public abstract class ErrorChecker {

    /**
     * Helper function to check if an argument is null, and throw an exception
     * if it is.
     * 
     * @param argument
     *            the argument to check for null.
     * @param argumentName
     *            the name of the argument to use in the exception message.
     */
    public static void throwIfNullArgument(final Object argument, final String argumentName) {
        if (argument == null) {
            throw new IllegalArgumentException("Argument '" + argumentName
                    + "' should not be null.");
        }
    }

    /**
     * Helper function check if an object is set, and throw and
     * IllegalStateException if it is not.
     * 
     * @param object
     *            the object to check.
     * @param objectName
     *            the name of the object to use in the exception message.
     */
    public static void throwIfNotSet(final Object object, final String objectName) {
        if (object == null) {
            throw new IllegalStateException("Object '" + objectName + "' must not be null.");
        }
    }

}
