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

import java.util.HashMap;

/**
 * Tests result filter commands. Created by Toby Leheup on 05/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ResultFilterCommandsTest
{
	/**
	 * Tests the command for filtering by result is as expected
	 */
	@Test
	public void getFilterByResultCommandTest()
	{
		Assert.assertEquals(TestCommandContext.COMMAND_NOT_EXPECTED_VALUE, "result-filter", ResultFilterCommands.getFilterByResultCommand());
	}

	/**
	 * Tests that that the availability of filter-by-result is correct
	 */
	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	@Test
	public void isFilterByResultAvailableTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String command = TestCommandContext.buildCommand(ResultFilterCommands.getFilterByResultCommand());
		testCommandContext.assertCommandFails(command);
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		Assert.assertNotNull(TestCommandContext.COMMAND_FAILS_UNEXPECTEDLY, testCommandContext.executeValidCommand(command));
	}

	/**
	 * Test that all games are filtered out when expected
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void testFilterByResult()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		final HashMap<String, String> options = new HashMap<String, String>(4);
		options.put(ResultFilterCommands.FILTER_BLACK_WINS_OPTION, Boolean.toString(true));
		options.put(ResultFilterCommands.FILTER_WHITE_WINS_OPTION, Boolean.toString(true));
		options.put(ResultFilterCommands.FILTER_DRAWS_OPTION, Boolean.toString(true));
		options.put(ResultFilterCommands.FILTER_UNRESOLVED_OPTION, Boolean.toString(true));
		final String command = TestCommandContext.buildCommand(ResultFilterCommands.getFilterByResultCommand(), options);
		final String expectedOutput = testCommandContext.getChessIO().getGames().size() + " " + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
		final String actualOutput = testCommandContext.executeValidCommand(command);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, expectedOutput);
	}
}
