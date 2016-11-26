/*
 * Copyright 2015 - 2016 Xyanid
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

package de.saxsys.svgfx.core.attributes.type;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import javafx.util.Pair;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Xyanid on 05.11.2016.
 */
public class SVGAttributeTypeIntegrationTest {

    // region Fields

    private final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

    // endregion

    //region Tests

    /**
     * The text transmitted to the cut will not be parsed as long as the value and/or unit was directly requested.
     */
    @Test
    public void whenTheInheritIndicatorIsProvidedTheValueAndUnitWillBeNullButTheInheritFlagWillBeSet() throws SVGException {

        final SVGAttributeType<Long, Void> cut = new SVGAttributeType<Long, Void>(Long.MAX_VALUE, dataProvider) {
            @Override
            protected Pair<Long, Void> getValueAndUnit(final String text) throws SVGException {
                return new Pair<>(Long.parseLong(text), null);
            }
        };

        cut.setText("inherit");

        final Pair<Long, Void> result = cut.getValueAndUnit();

        assertNull(result.getKey());
        assertNull(result.getValue());
        assertTrue(cut.getIsInherited());
    }

    /**
     * The text transmitted to the cut will not be parsed as long as the value and/or unit was directly requested.
     */
    @Test
    public void whenTheNoneIndicatorIsProvidedTheValueAndUnitWillBeNullButTheNoneFlagWillBeSet() throws SVGException {

        final SVGAttributeType<Long, Void> cut = new SVGAttributeType<Long, Void>(Long.MAX_VALUE, dataProvider) {
            @Override
            protected Pair<Long, Void> getValueAndUnit(final String text) throws SVGException {
                return new Pair<>(Long.parseLong(text), null);
            }
        };

        cut.setText("none");

        final Pair<Long, Void> result = cut.getValueAndUnit();

        assertNull(result.getKey());
        assertNull(result.getValue());
        assertTrue(cut.getIsNone());
    }

    /**
     * The text transmitted to the cut will not be parsed as long as the value and/or unit was directly requested.
     */
    @Test
    public void theSetTextWillOnlyBeParsedWhenTheValueAndUnitAreRequested() throws SVGException {

        final AtomicBoolean counter = new AtomicBoolean(false);

        final SVGAttributeType<Long, Void> cut = new SVGAttributeType<Long, Void>(Long.MAX_VALUE, dataProvider) {
            @Override
            protected Pair<Long, Void> getValueAndUnit(final String text) throws SVGException {
                counter.set(true);
                return new Pair<>(Long.parseLong(text), null);
            }
        };

        cut.setText("1");

        assertFalse(counter.get());
    }

    /**
     * The text will only be parsed once when the value and unit have not yet been initialized or if a new text has been transmitted.
     */
    @Test
    public void asLongAsTheTextIsNotResetTheValueAndUnitWillOnlyBeInitializedOnce() throws SVGException {

        final AtomicInteger counter = new AtomicInteger(0);

        final SVGAttributeType<Long, Void> cut = new SVGAttributeType<Long, Void>(Long.MAX_VALUE, dataProvider) {
            @Override
            protected Pair<Long, Void> getValueAndUnit(final String text) throws SVGException {
                counter.addAndGet(1);
                return new Pair<>(Long.parseLong(text), null);
            }
        };

        cut.setText("1");
        cut.getValue();

        assertEquals(1, counter.get());

        cut.getValue();

        assertEquals(1, counter.get());

        cut.setText("2");

        assertEquals(1, counter.get());

        cut.getValue();

        assertEquals(2, counter.get());
    }

    /**
     * True will be returned if the unit of an attribute is equals to an expected one, this is also true if both the expected and actual unit are null.
     */
    @Test
    public void itIsPossibleToDetermineIfTheAttributeHasAnExpectedUnit() throws SVGException {

        final SVGAttributeType<Long, Long> cut = new SVGAttributeType<Long, Long>(Long.MAX_VALUE, dataProvider) {
            @Override
            protected Pair<Long, Long> getValueAndUnit(final String text) throws SVGException {

                return new Pair<>(Long.parseLong(text.substring(0, 1)), text.length() == 2 ? Long.parseLong(text.substring(1)) : null);
            }
        };

        cut.setText("11");

        assertTrue(cut.hasUnit(1L));
        assertFalse(cut.hasUnit(2L));
        assertFalse(cut.hasUnit(null));

        cut.setText("1");

        assertFalse(cut.hasUnit(1L));
        assertTrue(cut.hasUnit(null));
    }


    //endregion
}