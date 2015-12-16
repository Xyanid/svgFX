/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * This class will test all methods of the {@link StringUtils}
 *
 * @author Xyanid on 28.10.2015.
 */
public final class StringUtilTest {
    /**
     * Will if the last character in a string is replaces if it was found.
     */
    @Test
    public void replaceLastOccurrenceInString() {
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

    /**
     * Test if string characters are removed at the start and end.
     */
    @Test
    public void removeStringIndicators() {
        String test = "\"this\" is a test;\"";

        //first we test the string version
        Assert.assertEquals("this\" is a test;", StringUtils.stripStringIndicators(test));
    }
}
