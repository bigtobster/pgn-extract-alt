package com.bigtobster.pgnextractalt.commands;

import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Tests Tag Modification Commands Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
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
	private static String buildInsertTagCommand(final String key, final String value, final boolean force)
	{
		final String command = TagCommands.getInsertTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		args.put(TagCommands.TAG_KEY, key);
		args.put(TagCommands.TAG_VALUE, value);
		args.put(TagCommands.FORCE, String.valueOf(force));
		return TestContext.buildCommand(command, args);
	}

	@SuppressWarnings("SameParameterValue")
	private static String buildInsertTagCommand(final String key, final String value)
	{
		final String command = TagCommands.getInsertTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		args.put(TagCommands.TAG_KEY, key);
		args.put(TagCommands.TAG_VALUE, value);
		return TestContext.buildCommand(command, args);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagAllGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE, false);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final String predictedOutput = TagCommands.FAILED_TO_INSERT_TAGS + TagCommandsTest.SPACE + TagCommands.KEY_ALREADY_USED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test missing arguments fail to work on insertingTags
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagBadCommandTest()
	{
		final TestContext testContext = new TestContext();

		final String command = TagCommands.getInsertTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);

		testContext.assertCommandFails(TestContext.buildCommand(command, args));

		args.put(TagCommands.TAG_KEY, TagCommandsTest.NEW_TEST_KEY);
		testContext.assertCommandFails(TestContext.buildCommand(command, args));

		args.put(TagCommands.TAG_VALUE, TagCommandsTest.NEW_TEST_VALUE);
		testContext.assertCommandFails(TestContext.buildCommand(command, args));

		args.remove(TagCommands.TAG_VALUE);
		args.put(TagCommands.FORCE, TagCommandsTest.TRUE);
		testContext.assertCommandFails(TestContext.buildCommand(command, args));
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagForceAllGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagForceNoGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagForceSomeGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);
		final ArrayList<Game> games = testContext.getApplicationContext().getBean(ChessIO.class).getGames();
		for(int i = 0; (i < games.size()) && ((i % 2) == 0); i++)
		{
			games.get(i).setTag(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE);
		}

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagMissingForce()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final String predictedOutput = TagCommands.FAILED_TO_INSERT_TAGS + TagCommandsTest.SPACE + TagCommands.KEY_ALREADY_USED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagNoGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, false);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagNoImportedGames()
	{
		final TestContext testContext = new TestContext();
		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);
		testContext.assertCommandFails(finalCommand);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertTagSomeGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);
		final ArrayList<Game> games = testContext.getApplicationContext().getBean(ChessIO.class).getGames();
		int gamesModified = 0;
		for(int i = 0; (i < games.size()) && ((i % 2) == 0); i++)
		{
			games.get(i).setTag(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE);
			gamesModified++;
		}

		final String finalCommand = TagCommandsTest.buildInsertTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, false);
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   (totalGames - gamesModified) + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests that writable tag command output is appropriate
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void listWritableTags()
	{
		final TestContext testContext = new TestContext();

		final String finalCommand = TagCommands.getListWritableTagsCommand();
		final String actualOutput = testContext.executeValidCommand(finalCommand);
		for(final String tag : testContext.getChessTagModder().getWritableTags())
		{
			Assert.assertNotNull("Tag should not be null", tag);
			Assert.assertTrue("Tag should not be empty String", ! tag.isEmpty());
			TestContext.assertCommandOutputContains(actualOutput, tag);
		}
	}
}
