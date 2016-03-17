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
import com.bigtobster.pgnextractalt.misc.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Tests Tag Modification Spring Shell Commands. Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class TagCommandsTest
{
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String EVENT_KEY      = "Event";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER         = Logger.getLogger(IOCommandsTest.class.getName());
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_KEY   = "TestKey";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_VALUE = "NewTestValue";
	private static final char   SPACE          = ' ';
	@SuppressWarnings("ConstantNamingConvention")
	private static final String TRUE           = "true";


	@SuppressWarnings("SameParameterValue")
	private static String buildInsertTagCommand(final String key, final String value)
	{
		final String command = TagCommands.getInsertTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		args.put(TagCommands.TAG_KEY, key);
		args.put(TagCommands.TAG_VALUE, value);
		return TestCommandContext.buildCommand(command, args);
	}

	@SuppressWarnings("SameParameterValue")
	private static String buildInsertTagCommand(final String key, final String value, final boolean force)
	{
		final String command = TagCommands.getInsertTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		args.put(TagCommands.TAG_KEY, key);
		args.put(TagCommands.TAG_VALUE, value);
		args.put(TagCommands.FORCE, String.valueOf(force));
		return TestCommandContext.buildCommand(command, args);
	}

	/**
	 * Test inserting a tag when already exists in non force mode
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertExistingTag()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final String predictedOutput = CommandContext.FAILED_TO_INSERT_TAGS + TagCommandsTest.SPACE + TagCommands.KEY_ALREADY_USED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change on all games
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagAllGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE, false);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final String predictedOutput = CommandContext.FAILED_TO_INSERT_TAGS + TagCommandsTest.SPACE + TagCommands.KEY_ALREADY_USED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test missing arguments fail to work on insertingTags
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagBadCommandTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();

		final String command = TagCommands.getInsertTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);

		testCommandContext.assertCommandFails(TestCommandContext.buildCommand(command, args));

		args.put(TagCommands.TAG_KEY, TagCommandsTest.NEW_TEST_KEY);
		testCommandContext.assertCommandFails(TestCommandContext.buildCommand(command, args));

		args.put(TagCommands.TAG_VALUE, TagCommandsTest.NEW_TEST_VALUE);
		testCommandContext.assertCommandFails(TestCommandContext.buildCommand(command, args));

		args.remove(TagCommands.TAG_VALUE);
		args.put(TagCommands.FORCE, TagCommandsTest.TRUE);
		testCommandContext.assertCommandFails(TestCommandContext.buildCommand(command, args));
	}

	/**
	 * Test forcing all tags to change on all games in force mode
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagForceAllGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final int totalGames = testCommandContext.getChessIO().getGames().size();
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + CommandContext.TAGS_INSERTED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change when no games have the proposed tag
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagForceNoGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final int totalGames = testCommandContext.getChessIO().getGames().size();
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + CommandContext.TAGS_INSERTED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change when some games have the proposed tag
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagForceSomeGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		final ArrayList<Game> games = testCommandContext.getChessIO().getGames();
		for(int i = 0; (i < games.size()) && ((i % 2) == 0); i++)
		{
			games.get(i).setTag(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE);
		}

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final int totalGames = games.size();
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + CommandContext.TAGS_INSERTED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test adding a tag which doesn't yet exist in non force
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagNoGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, false);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final int totalGames = testCommandContext.getChessIO().getGames().size();
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + CommandContext.TAGS_INSERTED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test inserting a tag when no games have been imported
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagNoImportedGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		testCommandContext.assertCommandFails(finalCommand);
	}

	/**
	 * Test adding a tag when some of the games already have the tag
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagSomeGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		final ArrayList<Game> games = testCommandContext.getChessIO().getGames();
		int gamesModified = 0;
		for(int i = 0; (i < games.size()) && ((i % 2) == 0); i++)
		{
			games.get(i).setTag(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE);
			gamesModified++;
		}

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, false);
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final int totalGames = games.size();
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   (totalGames - gamesModified) + TagCommandsTest.SPACE + CommandContext.TAGS_INSERTED;

		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests that writable tag command output is appropriate
	 */
	@Test
	public void listWritableTags()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();

		final String finalCommand = TagCommands.getListWritableTagsCommand();
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		for(final String tag : testCommandContext.getChessTagModder().getWritableTags())
		{
			Assert.assertNotNull("Tag should not be null", tag);
			Assert.assertTrue("Tag should not be empty String", ! tag.isEmpty());
			TestCommandContext.assertCommandOutputContains(actualOutput, tag);
		}
	}
}
