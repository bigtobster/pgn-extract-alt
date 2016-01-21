package com.bigtobster.pgnextractalt.commands;

import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.Shell;

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
	private static final String COMMAND_CONSTRUCTION_ERROR = "Command construction error";
	private static final String EVENT_KEY                  = "Event";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                     = Logger.getLogger(IOCommandsTest.class.getName());
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_KEY               = "TestKey";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_VALUE             = "NewTestValue";
	private static final char   SPACE                      = ' ';
	@SuppressWarnings("ConstantNamingConvention")
	private static final String TRUE                       = "true";

	@SuppressWarnings("SameParameterValue")
	private static String buildInsertCustomTagCommand(final String key, final String value, final boolean force)
	{
		final String command = TagCommands.getInsertCustomTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		args.put(TagCommands.TAG_KEY, key);
		args.put(TagCommands.TAG_VALUE, value);
		args.put(TagCommands.FORCE, String.valueOf(force));
		return TestContext.buildCommand(command, args);
	}

	private static String buildInsertCustomTagCommand(final String key, final String value)
	{
		final String command = TagCommands.getInsertCustomTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		args.put(TagCommands.TAG_KEY, key);
		args.put(TagCommands.TAG_VALUE, value);
		return TestContext.buildCommand(command, args);
	}

	private static void testInsertCustomTag(final TestContext testContext, final String finalCommand, final String predictedOutput)
	{
		final Shell shell = testContext.getShell();

		final CommandResult commandResult = shell.executeCommand(finalCommand);
		Assert.assertNotNull(TagCommandsTest.COMMAND_CONSTRUCTION_ERROR, commandResult);
		Assert.assertNotNull(TagCommandsTest.COMMAND_CONSTRUCTION_ERROR, commandResult.getResult());
		final String resultOutput = commandResult.getResult().toString();

		Assert.assertEquals(TestContext.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagAllGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE, false);

		final String predictedOutput = TagCommands.FAILED_TO_INSERT_TAGS + TagCommandsTest.SPACE + TagCommands.KEY_ALREADY_USED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}

	/**
	 * Test missing arguments fail to work on insertingCustomTags
	 */
	@Test
	public void insertCustomTagBadCommandTest()
	{
		final TestContext testContext = new TestContext();
		final Shell shell = testContext.getShell();

		final String command = TagCommands.getInsertCustomTagCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		CommandResult commandResult;
		String finalCommand;

		finalCommand = TestContext.buildCommand(command, args);
		commandResult = shell.executeCommand(finalCommand);
		Assert.assertNull(TestContext.CONSOLE_MESSAGE_DIFFERS, commandResult.getResult());

		args.put(TagCommands.TAG_KEY, TagCommandsTest.NEW_TEST_KEY);
		finalCommand = TestContext.buildCommand(command, args);
		commandResult = shell.executeCommand(finalCommand);
		Assert.assertNull(TestContext.CONSOLE_MESSAGE_DIFFERS, commandResult.getResult());

		args.put(TagCommands.TAG_VALUE, TagCommandsTest.NEW_TEST_VALUE);
		finalCommand = TestContext.buildCommand(command, args);
		commandResult = shell.executeCommand(finalCommand);
		Assert.assertNull(TestContext.CONSOLE_MESSAGE_DIFFERS, commandResult.getResult());

		args.remove(TagCommands.TAG_VALUE);
		args.put(TagCommands.FORCE, TagCommandsTest.TRUE);
		finalCommand = TestContext.buildCommand(command, args);
		commandResult = shell.executeCommand(finalCommand);
		Assert.assertNull(TestContext.CONSOLE_MESSAGE_DIFFERS, commandResult.getResult());
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagForceAllGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE, true);

		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagForceNoGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);

		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagForceSomeGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);
		final ArrayList<Game> games = testContext.getApplicationContext().getBean(ChessIO.class).getGames();
		for(int i = 0; (i < games.size()) && ((i % 2) == 0); i++)
		{
			games.get(i).setTag(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE);
		}

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);

		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagMissingForce()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.EVENT_KEY, TagCommandsTest.NEW_TEST_VALUE);

		final String predictedOutput = TagCommands.FAILED_TO_INSERT_TAGS + TagCommandsTest.SPACE + TagCommands.KEY_ALREADY_USED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagNoGames()
	{
		final TestContext testContext = new TestContext();
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, false);

		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   totalGames + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}

	/**
	 * Test forcing all tags to change
	 */
	@Test
	public void insertCustomTagNoImportedGames()
	{
		final TestContext testContext = new TestContext();

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, true);

		final Shell shell = testContext.getShell();
		final CommandResult commandResult = shell.executeCommand(finalCommand);
		Assert.assertNotNull(TagCommandsTest.COMMAND_CONSTRUCTION_ERROR, commandResult);
	}

	/**
	 * Test forcing all tags to change
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertCustomTagSomeGames()
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

		final String finalCommand = TagCommandsTest.buildInsertCustomTagCommand(TagCommandsTest.NEW_TEST_KEY, TagCommandsTest.NEW_TEST_VALUE, false);

		final int totalGames = testContext.getApplicationContext().getBean(ChessIO.class).getGames().size();
		final String predictedOutput = TagCommands.SUCCESSFULLY_INSERTED_TAGS +
									   TagCommandsTest.SPACE +
									   (totalGames - gamesModified) + TagCommandsTest.SPACE + TagCommands.TAGS_INSERTED;

		TagCommandsTest.testInsertCustomTag(testContext, finalCommand, predictedOutput);
	}
}
