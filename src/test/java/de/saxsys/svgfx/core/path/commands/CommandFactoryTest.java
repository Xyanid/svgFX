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

package de.saxsys.svgfx.core.path.commands;

import de.saxsys.svgfx.core.path.PathException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author Xyanid on 23.04.2017.
 */
public class CommandFactoryTest {

    @Test (expected = PathException.class)
    public void aMoveCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createMoveCommand('X', "10 10");
    }

    @Test
    public void aMoveCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createMoveCommand(MoveCommand.ABSOLUTE_NAME, "10 10");
        CommandFactory.INSTANCE.createMoveCommand(MoveCommand.RELATIVE_NAME, "10 10");
    }

    @Test
    public void aMoveCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10 Z",
                                                       "10 , Z",
                                                       "Z, 10");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createMoveCommand(MoveCommand.ABSOLUTE_NAME, data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aMoveCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10 10",
                                                     " 10 10",
                                                     " 10 10 ",
                                                     "   10   10   ",
                                                     "10,10",
                                                     " 10,10",
                                                     " 10,10",
                                                     " 10,10 ",
                                                     "   10  ,  10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createMoveCommand(MoveCommand.ABSOLUTE_NAME, data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test (expected = PathException.class)
    public void aLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createLineCommand('X', "10 10");
    }

    @Test
    public void aLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createLineCommand(LineCommand.ABSOLUTE_NAME, "10 10");
        CommandFactory.INSTANCE.createLineCommand(LineCommand.RELATIVE_NAME, "10 10");
    }

    @Test
    public void aLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10",
                                                       "10 10 Z",
                                                       "10 , Z",
                                                       "Z, 10");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createLineCommand(LineCommand.ABSOLUTE_NAME, data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aLineCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10 10",
                                                     " 10 10",
                                                     " 10 10 ",
                                                     "   10   10   ",
                                                     "10,10",
                                                     " 10,10",
                                                     " 10,10",
                                                     " 10,10 ",
                                                     "   10  ,  10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createLineCommand(LineCommand.ABSOLUTE_NAME, data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test (expected = PathException.class)
    public void aVerticalLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createVerticalLineCommand('X', "10");
    }

    @Test
    public void aVerticalLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createVerticalLineCommand(VerticalLineCommand.ABSOLUTE_NAME, "10");
        CommandFactory.INSTANCE.createVerticalLineCommand(VerticalLineCommand.RELATIVE_NAME, "10");
    }

    @Test
    public void aVerticalLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10 10",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createVerticalLineCommand(VerticalLineCommand.ABSOLUTE_NAME, data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aVerticalLineCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10",
                                                     " 10",
                                                     " 10 ",
                                                     "   10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createVerticalLineCommand(VerticalLineCommand.ABSOLUTE_NAME, data);
            } catch (final PathException ignore) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }

    @Test (expected = PathException.class)
    public void aHorizontalLineCommandCanNotBeCreatedWhenTheGivenNameIsInvalid() throws PathException {
        CommandFactory.INSTANCE.createHorizontalLineCommand('X', "10");
    }

    @Test
    public void aHorizontalLineCommandCanBeCreatedWhenTheGivenNameIsValid() throws PathException {
        CommandFactory.INSTANCE.createHorizontalLineCommand(HorizontalLineCommand.ABSOLUTE_NAME, "10");
        CommandFactory.INSTANCE.createHorizontalLineCommand(HorizontalLineCommand.RELATIVE_NAME, "10");
    }

    @Test
    public void aHorizontalLineCommandCanNotBeCreatedWhenTheGivenDataIsInvalid() {

        final List<String> invalidData = Arrays.asList("10 10",
                                                       "10Z",
                                                       "10 Z",
                                                       "Z");

        for (final String data : invalidData) {
            try {
                CommandFactory.INSTANCE.createHorizontalLineCommand(HorizontalLineCommand.ABSOLUTE_NAME, data);
                fail(String.format("Should have not been able to create instance with data %s", data));
            } catch (final PathException ignore) {
            }
        }
    }

    @Test
    public void aHorizontalLineCommandCanBeCreatedFromAllPossibleValidDataStrings() {

        final List<String> validData = Arrays.asList("10",
                                                     " 10",
                                                     " 10 ",
                                                     "   10   ");


        for (final String data : validData) {
            try {
                CommandFactory.INSTANCE.createHorizontalLineCommand(HorizontalLineCommand.ABSOLUTE_NAME, data);
            } catch (PathException e) {
                fail(String.format("Could not use data %s", data));
            }
        }
    }
}