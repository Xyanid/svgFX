package de.saxsys.svgfx.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * This class will test all methods of the {@link StringUtils}
 * Created by Xyanid on 28.10.2015.
 */
public final class StringUtilTest {
    /**
     * will test the {@link StringUtils#replaceLast(String, String, String)}.
     */
    @Test public void replaceLast() {
        String test = "this is a test;";

        //first we test the string version
        Assert.assertEquals("this is a test-", StringUtils.replaceLast(test, ";", "-"));

        Assert.assertEquals(test, StringUtils.replaceLast(test, "-", ""));

        Assert.assertEquals(null, StringUtils.replaceLast(null, "-", ""));

        //here we test the char version which is actually the same anyway but still we need to test it
        Assert.assertEquals("this is a test-", StringUtils.replaceLast(test, ';', '-'));

        Assert.assertEquals(test, StringUtils.replaceLast(test, '-', ' '));

        Assert.assertEquals(null, StringUtils.replaceLast(null, '-', ' '));
    }
}
