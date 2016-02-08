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

import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Tests Plycount Filter commands Created by Toby Leheup on 07/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class PlycountFilterCommandsTest
{
	/**
	 * Tests the command for filtering by Plycount is as expected
	 */
	@Test
	public void getFilterByPlycountCommandTest()
	{
		Assert.assertEquals(TestCommandContext.COMMAND_NOT_EXPECTED_VALUE, "plycount-filter", PlycountFilterCommands.getFilterByPlycountCommand());
	}

	/**
	 * Tests that that the availability of filter-by-plycount is correct
	 */
	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	@Test
	public void isFilterByPlycountAvailableTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final HashMap<String, String> options = new HashMap<String, String>(2);
		options.put(PlycountFilterCommands.GREATER_THAN_OPTION, "0");
		options.put(PlycountFilterCommands.LESS_THAN_OPTION, String.valueOf(Integer.MAX_VALUE));
		final String command = TestCommandContext.buildCommand(PlycountFilterCommands.getFilterByPlycountCommand(), options);
		testCommandContext.assertCommandFails(command);
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		Assert.assertNotNull(TestCommandContext.COMMAND_FAILS_UNEXPECTEDLY, testCommandContext.executeValidCommand(command));
	}

	/**
	 * Test that all games are filtered out when expected
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void testFilterByPlycount()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		final HashMap<String, String> options = new HashMap<String, String>(2);
		options.put(PlycountFilterCommands.GREATER_THAN_OPTION, String.valueOf(Integer.MIN_VALUE));
		options.put(PlycountFilterCommands.LESS_THAN_OPTION, String.valueOf(Integer.MAX_VALUE));
		final String command = TestCommandContext.buildCommand(PlycountFilterCommands.getFilterByPlycountCommand(), options);
		final String expectedOutput = testCommandContext.getChessIO().getGames().size() + " " + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
		final String actualOutput = testCommandContext.executeValidCommand(command);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, expectedOutput);
	}
}
