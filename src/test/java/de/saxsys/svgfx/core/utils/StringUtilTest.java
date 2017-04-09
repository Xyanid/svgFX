/*
 * Copyright 2015 - 2017 Xyanid
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package de.saxsys.svgfx.core.utils;

import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.interfaces.ThrowablePredicate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * This class will test all methods of the {@link StringUtil}
 *
 * @author Xyanid on 28.10.2015.
 */
public final class StringUtilTest {
    /**
     * Will if the last character in a string is replaces if it was found.
     */
    @Test
    public void theLastOccurrenceInStringWillBeReplacedCorrectly() {
        final String test = "this is a test;";

        //first we test the string version
        assertEquals("this is a test-", StringUtil.replaceLast(test, ";", "-"));

        assertEquals(test, StringUtil.replaceLast(test, "-", ""));

        assertNull(StringUtil.replaceLast(null, "-", ""));

        //here we test the char version which is actually the same anyway but still we need to test it
        assertEquals("this is a test-", StringUtil.replaceLast(test, ';', '-'));

        assertEquals(test, StringUtil.replaceLast(test, '-', ' '));

        assertNull(StringUtil.replaceLast(null, '-', ' '));
    }

    /**
     * Test if string characters are removed at the start and end.
     */
    @Test
    public void stringIndicatorsAreRemovedAtTheStartAndEnd() {
        final String test = "\"this\" is a test;\"";

        //first we test the string version
        assertEquals("this\" is a test;", StringUtil.stripStringIndicators(test));
    }

    /**
     * Ensures that {@link StringUtil#splitByDelimiters(String, Collection, ThrowablePredicate)} will throw the expected exceptions.
     */
    @Test (expected = SVGException.class)
    public void ensureThatAnExceptionIsThrownWhenThe() throws SVGException {
        StringUtil.splitByDelimiters("Test", Collections.singletonList(' '), (currentData, index) -> {
            throw new SVGException(SVGException.Reason.FAILED_TO_PARSE_IRI, "test");
        });
    }

    /**
     * Ensures that {@link StringUtil#splitByDelimiters(String, Collection, ThrowablePredicate)} is able to split a string which contains multiple
     * delimiters.
     */
    @Test
    public void ensureAStringMoreThanOneDelimiterCanBeSplitCorrectly() throws SVGException {

        final List<Character> delimiters = new ArrayList<>();
        delimiters.add(';');
        delimiters.add(',');
        delimiters.add(' ');

        final List<String> result = StringUtil.splitByDelimiters(" This,is a;test", delimiters, (currentData) -> true);

        assertEquals(4, result.size());
        assertEquals("This", result.get(0));
        assertEquals("is", result.get(1));
        assertEquals("a", result.get(2));
        assertEquals("test", result.get(3));
    }

    /**
     * Ensures that {@link StringUtil#splitByDelimiters(String, Collection, ThrowablePredicate)} will only process {@link String}s without
     * delimiters.
     */
    @Test
    public void ensureAStringThatWasSplitWillNeverContainDelimiters() throws SVGException {

        final List<Character> delimiters = new ArrayList<>();
        delimiters.add(' ');

        final List<String> result = StringUtil.splitByDelimiters(" This , is a test ", delimiters, (currentData) -> {

            if (currentData != null) {
                assertFalse(currentData.contains(" "));
                return true;
            }
            return false;
        });

        assertEquals(5, result.size());
        assertEquals("This", result.get(0));
        assertEquals(",", result.get(1));
        assertEquals("is", result.get(2));
        assertEquals("a", result.get(3));
        assertEquals("test", result.get(4));
    }

    @Test
    public void ensureThatIsPossibleToDetermineIfStringAreEmptyOrNull() {
        assertTrue(StringUtil.isNullOrEmpty(null));
        assertTrue(StringUtil.isNullOrEmpty(""));
        assertFalse(StringUtil.isNullOrEmpty(" "));
        assertFalse(StringUtil.isNullOrEmpty("A"));

        assertFalse(StringUtil.isNotNullOrEmpty(null));
        assertFalse(StringUtil.isNotNullOrEmpty(""));
        assertTrue(StringUtil.isNotNullOrEmpty(" "));
        assertTrue(StringUtil.isNotNullOrEmpty("A"));
    }

    @Test
    public void ensureThatIsPossibleToDetermineIfStringAreEmptyOrNullAfterBeingTrimmed() {
        assertTrue(StringUtil.isNullOrEmptyAfterTrim(null));
        assertTrue(StringUtil.isNullOrEmptyAfterTrim(""));
        assertTrue(StringUtil.isNullOrEmptyAfterTrim(" "));
        assertFalse(StringUtil.isNullOrEmpty("A"));

        assertFalse(StringUtil.isNotNullOrEmptyAfterTrim(null));
        assertFalse(StringUtil.isNotNullOrEmptyAfterTrim(""));
        assertFalse(StringUtil.isNotNullOrEmptyAfterTrim(" "));
        assertTrue(StringUtil.isNotNullOrEmpty("A"));
    }
}