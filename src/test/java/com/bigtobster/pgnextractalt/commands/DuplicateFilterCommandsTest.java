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

import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Tests Duplicate Filter Commands Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class DuplicateFilterCommandsTest
{
	private static final String SPACE = " ";

	/**
	 * Tests the command for filtering duplicates is as expected
	 */
	@Test
	public void getDuplicateFilterCommandTest()
	{
		Assert.assertEquals(
				TestCommandContext.COMMAND_NOT_EXPECTED_VALUE,
				"duplicate-filter",
				DuplicateFilterCommands.getDuplicateFilterCommand()
						   );
	}

	/**
	 * Tests that that the availability of duplicates filter is correct
	 */
	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	@Test
	public void isDuplicateFilterAvailableTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String command = TestCommandContext.buildCommand(DuplicateFilterCommands.getDuplicateFilterCommand());
		testCommandContext.assertCommandFails(command);
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		Assert.assertNotNull(TestCommandContext.COMMAND_FAILS_UNEXPECTEDLY, testCommandContext.executeValidCommand(command));
	}

	/**
	 * Test that all games are filtered out when expected
	 */
	@Test
	public void testDuplicateFilter()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();

		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		String expectedOutput = testCommandContext.getChessIO().getGames().size() + " " + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);

		final String command = TestCommandContext.buildCommand(DuplicateFilterCommands.getDuplicateFilterCommand());
		final File duplicatesDoc = new File(DuplicateFilterCommands.DUPLICATE_OUT_FILENAME);
		String actualOutput = testCommandContext.executeValidCommand(command);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, expectedOutput);

		final File pgnFile = TestContext.getPGNFile(TestContext.IMPORTS_DIR, DuplicateFilterCommands.DUPLICATE_OUT_FILENAME);
		try
		{
			Files.copy(duplicatesDoc.toPath(), pgnFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(final IOException e)
		{
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);

		testCommandContext.getChessIO().reset();
		testCommandContext.preloadPGN(DuplicateFilterCommands.DUPLICATE_OUT_FILENAME);
		Assert.assertTrue(TestContext.TEST_RESOURCE_NOT_FOUND, testCommandContext.getChessIO().isPGNImported());
		final ArrayList<Game> duplicatedGames = testCommandContext.getChessIO().getGames();
		Assert.assertEquals(TestContext.TEST_RESOURCE_NOT_FOUND, 5L, (long) duplicatedGames.size());
		testCommandContext.getChessIO().reset();
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		Assert.assertEquals(TestContext.TEST_RESOURCE_NOT_FOUND, duplicatedGames, testCommandContext.getChessIO().getGames());

		testCommandContext.getChessIO().reset();
		TestCommandContext.modifyFilePermissions(duplicatesDoc, false, false, false);
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		expectedOutput = DuplicateFilterCommands.ERROR_ON_EXPORTING_DUPLICATE_OUT +
						 IOCommands.FAILED_EXPORT +
						 DuplicateFilterCommandsTest.SPACE +
						 IOCommands.PGN_NOT_WRITABLE +
						 DuplicateFilterCommandsTest.SPACE +
						 duplicatesDoc.getAbsolutePath();
		actualOutput = testCommandContext.executeValidCommand(command);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, expectedOutput);
		TestCommandContext.modifyFilePermissions(duplicatesDoc, true, true, false);
	}
}
