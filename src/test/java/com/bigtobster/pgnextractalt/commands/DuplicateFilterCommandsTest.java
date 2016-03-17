/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.commands;

import com.bigtobster.pgnextractalt.misc.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Tests Duplicate Filter Commands. Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class DuplicateFilterCommandsTest
{
	private static final String FILTER_DUPLICATES_CMD  = "filter-duplicates";
	private static final String ISOLATE_DUPLICATES_CMD = "isolate-duplicates";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                 = Logger.getLogger(DuplicateFilterCommandsTest.class.getName());
	private static final String PURGE_DUPLICATES_CMD   = "purge-duplicates";

	private static void testCommandAvailability(final String command)
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.assertCommandFails(command);
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		Assert.assertNotNull(TestCommandContext.COMMAND_FAILS_UNEXPECTEDLY, testCommandContext.executeValidCommand(command));
	}

	@SuppressWarnings("SameParameterValue")
	private static void testDuplicatesCommand(final String filterCommand, @SuppressWarnings("SameParameterValue") final int onTripleMulti)
	{
		DuplicateFilterCommandsTest.testDuplicatesCommand(filterCommand, onTripleMulti, - 1);
	}

	private static void testDuplicatesCommand(final String filterCommand, final int onTripleMulti, final int onSelf)
	{
		final TestCommandContext testCommandContext = new TestCommandContext();

		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		String expectedOutput = onTripleMulti + " " + CommandContext.SUCCESSFULLY_FILTERED_GAMES;

		final String command = TestCommandContext.buildCommand(filterCommand);
		String actualOutput = testCommandContext.executeValidCommand(command);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, expectedOutput);

		if(testCommandContext.getChessIO().getGames().isEmpty())
		{
			testCommandContext.assertCommandFails(command);
		}
		else
		{
			expectedOutput = onSelf + " " + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
			actualOutput = testCommandContext.executeValidCommand(command);
			TestCommandContext.assertOutputMatchesPredicted(actualOutput, expectedOutput);
		}
	}

	/**
	 * Tests the command for filtering duplicates is as expected
	 */
	@Test
	public void getFilterDuplicatesCommandTest()
	{
		Assert.assertEquals(
				TestCommandContext.COMMAND_NOT_EXPECTED_VALUE,
				DuplicateFilterCommandsTest.FILTER_DUPLICATES_CMD,
				DuplicateFilterCommands.getFilterDuplicatesCommand()
						   );
	}

	/**
	 * Tests the command for isolating duplicates is as expected
	 */
	@Test
	public void getIsolateDuplicatesCommandTest()
	{
		Assert.assertEquals(
				TestCommandContext.COMMAND_NOT_EXPECTED_VALUE,
				DuplicateFilterCommandsTest.ISOLATE_DUPLICATES_CMD,
				DuplicateFilterCommands.getIsolateDuplicatesCommand()
						   );
	}

	/**
	 * Tests the command for purging duplicates is as expected
	 */
	@Test
	public void getPurgeDuplicatesCommandTest()
	{
		Assert.assertEquals(
				TestCommandContext.COMMAND_NOT_EXPECTED_VALUE,
				DuplicateFilterCommandsTest.PURGE_DUPLICATES_CMD,
				DuplicateFilterCommands.getPurgeDuplicatesCommand()
						   );
	}

	/**
	 * Tests that that the availability of duplicates filter is correct
	 */
	@SuppressWarnings({"NonBooleanMethodNameMayNotStartWithQuestion", "JUnitTestMethodWithNoAssertions"})
	@Test
	public void isDuplicateFilterAvailableTest()
	{
		DuplicateFilterCommandsTest.testCommandAvailability(DuplicateFilterCommands.getFilterDuplicatesCommand());
	}

	/**
	 * Tests that that the availability of isolate duplicates is correct
	 */
	@SuppressWarnings({"NonBooleanMethodNameMayNotStartWithQuestion", "JUnitTestMethodWithNoAssertions"})
	@Test
	public void isIsolateDuplicatesAvailableTest()
	{
		DuplicateFilterCommandsTest.testCommandAvailability(DuplicateFilterCommands.getIsolateDuplicatesCommand());
	}

	/**
	 * Tests that that the availability of purge duplicates is correct
	 */
	@SuppressWarnings({"NonBooleanMethodNameMayNotStartWithQuestion", "JUnitTestMethodWithNoAssertions"})
	@Test
	public void isPurgeDuplicatesAvailableTest()
	{
		DuplicateFilterCommandsTest.testCommandAvailability(DuplicateFilterCommands.getPurgeDuplicatesCommand());
	}

	/**
	 * Test that all games are filtered out when expected
	 *
	 * @throws java.io.IOException Import failure
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void testFilterDuplicates() throws IOException
	{
		DuplicateFilterCommandsTest.testDuplicatesCommand(DuplicateFilterCommands.getFilterDuplicatesCommand(), 10, 0);
	}

	/**
	 * Test that all games are isolated out when expected
	 *
	 * @throws java.io.IOException Import failure
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void testIsolateDuplicates() throws IOException
	{
		DuplicateFilterCommandsTest.testDuplicatesCommand(DuplicateFilterCommands.getIsolateDuplicatesCommand(), 0, 0);
	}

	/**
	 * Test that all games are purged out when expected
	 *
	 * @throws java.io.IOException Import failure
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void testPurgeDuplicates() throws IOException
	{
		DuplicateFilterCommandsTest.testDuplicatesCommand(DuplicateFilterCommands.getPurgeDuplicatesCommand(), 15);
	}
}
