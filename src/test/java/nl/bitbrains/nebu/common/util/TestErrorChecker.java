package nl.bitbrains.nebu.common.util;

import nl.bitbrains.nebu.common.util.ErrorChecker;

import org.junit.Assert;
import org.junit.Test;

public class TestErrorChecker {

    @Test
    public void testThrowIfNull_NullValue() {
        try {
            // When
            ErrorChecker.throwIfNullArgument(null, "NullArg");
            // Then
            // Expect exception, fail test otherwise
            Assert.fail("Function should throw IllegalArgumentException.");
        } catch (final IllegalArgumentException ex) {
        }
    }

    @Test
    public void testThrowIfNull_CheckMessage() {
        final String argName = "NullArg";
        try {
            // When
            ErrorChecker.throwIfNullArgument(null, argName);

            Assert.fail("Function should throw IllegalArgumentException.");
        } catch (final IllegalArgumentException ex) {
            // Then
            Assert.assertNotNull("Exception should have a message.", ex.getMessage());
            Assert.assertTrue("Exception should have a message containing the argument name.", ex
                    .getMessage().contains(argName));
        }
    }

    @Test
    public void testThrowIfNull_NotNullValue() {
        // When
        ErrorChecker.throwIfNullArgument(new Object(), "NotNullArg");
        // Then
        // Nothing should happen, fail test otherwise
    }

    @Test
    public void testThrowIfNotSet_NullValue() {
        try {
            // When
            ErrorChecker.throwIfNotSet(null, "NullArg");
            // Then
            // Expect exception, fail test otherwise
            Assert.fail("Function should throw IllegalStateException.");
        } catch (final IllegalStateException ex) {
        }
    }

    @Test
    public void testThrowIfNotSet_CheckMessage() {
        final String argName = "NullArg";
        try {
            // When
            ErrorChecker.throwIfNotSet(null, argName);

            Assert.fail("Function should throw IllegalStateException.");
        } catch (final IllegalStateException ex) {
            // Then
            Assert.assertNotNull("Exception should have a message.", ex.getMessage());
            Assert.assertTrue("Exception should have a message containing the argument name.", ex
                    .getMessage().contains(argName));
        }
    }

    @Test
    public void testThrowIfNotSet_NotNullValue() {
        // When
        ErrorChecker.throwIfNotSet(new Object(), "NotNullArg");
        // Then
        // Nothing should happen, fail test otherwise
    }

    @Test
    public void testForCoverage() {
        // Only needed to get the 100% coverage ;)
        new ErrorChecker() {
        };
    }

}
