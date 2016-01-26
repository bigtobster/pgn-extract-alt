package com.bigtobster.pgnextractalt.core;

import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.chess.ChessTagModder;
import com.bigtobster.pgnextractalt.commands.IOCommands;
import com.bigtobster.pgnextractalt.commands.IOCommandsTest;
import org.junit.After;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Abstract base test class that all Spring shell Test classes must extend when using a shell
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface"})
public class TestContext
{
	/**
	 * black_win_mate_headless.pgn filename
	 */
	public static final  String              BLACK_WIN_MATE_HEADLESS_PGN    = "black_win_mate_headless.pgn";
	/**
	 * draw_headless.pgn filename
	 */
	public static final  String              DRAW_HEADLESS_PGN              = "draw_headless.pgn";
	/**
	 * Name of the dump directory relative to the binary. Files in here is where PGN-Extract-Alt should write test files to.
	 */
	public static final  String              DUMP_DIR                       = "dump";
	/**
	 * Name of a PGN file with no characters
	 */
	public static final  String              EMPTY_PGN                      = "empty.pgn";
	/**
	 * Name of the exports directory relative to the binary. Files in here should be used for overwrite testing only.
	 */
	public static final  String              EXPORTS_DIR                    = "exports";
	/**
	 * Name of a PGN file that doesn't exist
	 */
	public static final  String              FALSE_PGN_PATH                 = "false.pgn";
	/**
	 * Name of the imports directory relative to the binary
	 */
	public static final  String              IMPORTS_DIR                    = "imports";
	/**
	 * incalculable_headless.pgn filename
	 */
	public static final  String              INCALCULABLE_HEADLESS_PGN      = "incalculable_headless.pgn";
	/**
	 * Name of a large PGN file with many valid games (and no invalid games)
	 */
	public static final  String              LARGE_PGN                      = "large.pgn";
	/**
	 * Name of a PGN file with multiple invalid and multiple valid games
	 */
	public static final  String              MULTI_INVALID_PGN              = "invalid_multi.pgn";
	/**
	 * Name of a PGN file with multiple valid games (and no invalid games)
	 */
	public static final  String              MULTI_PGN                      = "multi.pgn";
	/**
	 * Name of a PGN file that isn't remotely a PGN-looking file
	 */
	public static final  String              NOT_A_PGN                      = "not_a_pgn.test";
	/**
	 * Name of a PGN file with protected file permissions
	 */
	public static final  String              PROTECTED_PGN                  = "protected.pgn";
	/**
	 * Name of a PGN file with a single, syntactically invalid game
	 */
	public static final  String              SINGLE_INVALID_PGN             = "invalid_single.pgn";
	/**
	 * Name of PGN file with a single valid game
	 */
	public static final  String              SINGLE_PGN                     = "single.pgn";
	/**
	 * The name of the target directory from project home where output is placed
	 */
	public static final  String              TARGET_DIR                     = "target";
	/**
	 * The name of the Test Classes directory where test files are placed
	 */
	public static final  String              TEST_CLASSES_DIR               = "test-classes";
	/**
	 * Error message for assertions on null resources
	 */
	public static final  String              TEST_RESOURCE_NOT_FOUND        = "Test resource not found";
	/**
	 * white_win_mate_headless.pgn filename
	 */
	public static final  String              WHITE_WIN_MATE_HEADLESS_PGN    = "white_win_mate_headless.pgn";
	private static final String              AUTOWIRED_BEAN_NOT_CREATED     = "Autowired bean not created";
	private static final String              COMMAND_CONSTRUCTION_ERROR     = "Command construction error";
	/**
	 * Error message when command success when it was expected to fail
	 */
	private static final String              COMMAND_SUCCEEDS_FAIL_EXPECTED = "Command succeeds when failure expected";
	/**
	 * Assertion error message on expected console output being different to the actual result
	 */
	private static final String              CONSOLE_MESSAGE_DIFFERS        = "Console Message differs from expected case";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger              LOGGER                         = Logger.getLogger(TestContext.class.getName());
	/**
	 * Error message when a tag is missing from the provided tag list
	 */
	private static final String              MISSING_TAG                    = "Tag list does not contain tag ";
	private static final char                SPACE                          = ' ';
	private              ApplicationContext  applicationContext             = null;
	private              Bootstrap           bootstrap                      = null;
	private              JLineShellComponent shell                          = null;

	/**
	 * Initialise TestContext
	 */
	public TestContext()
	{
		this.bootstrap = new Bootstrap();
		this.shell = this.bootstrap.getJLineShellComponent();
		this.applicationContext = this.bootstrap.getApplicationContext();
		Assert.assertNotNull(TestContext.AUTOWIRED_BEAN_NOT_CREATED, this.getChessTagModder());
		Assert.assertNotNull(TestContext.AUTOWIRED_BEAN_NOT_CREATED, this.getChessIO());
	}

	/**
	 * Weaker test than assertOutputMatchesPredicted as it tests only that output contains a certain substring
	 * @param actualOutput The output on the shell
	 * @param substr The substring in the output of the command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	public static void assertCommandOutputContains(final String actualOutput, final String substr)
	{
		Assert.assertNotNull(TestContext.COMMAND_CONSTRUCTION_ERROR, substr);
		Assert.assertTrue(TestContext.MISSING_TAG, actualOutput.contains(substr));
	}

	/**
	 * Runs a non-null command and asserts that it's output is not NULL and that it matches the non-null predictedOutput
	 *
	 * @param actualOutput    The output on the shell
	 * @param predictedOutput The predicted output
	 */
	public static void assertOutputMatchesPredicted(final String actualOutput, final String predictedOutput)
	{
		Assert.assertNotNull(TestContext.COMMAND_CONSTRUCTION_ERROR, predictedOutput);
		Assert.assertEquals(TestContext.CONSOLE_MESSAGE_DIFFERS, predictedOutput, actualOutput);
	}

	/**
	 * Builds a command string up from a basic command plus a Hash Map of Option, Argument value pairs to get "command [--&lt;option&gt; &lt;
	 * arg&gt;]*"
	 *
	 * @param command         The base command
	 * @param optionValuesMap The hash map of Option, Argument pairs
	 * @return The fully constructed command
	 */
	public static String buildCommand(final String command, final HashMap<String, String> optionValuesMap)
	{
		final StringBuilder newCommandBuilder = new StringBuilder(50);
		newCommandBuilder.append(command);
		if(optionValuesMap != null)
		{
			for(final String key : optionValuesMap.keySet())
			{
				newCommandBuilder.append(" --");
				newCommandBuilder.append(key);
				newCommandBuilder.append(TestContext.SPACE);
				newCommandBuilder.append(optionValuesMap.get(key));
			}
		}
		return newCommandBuilder.toString();
	}

	/**
	 * Loads any given context with files located at a given path
	 *
	 * @param testContext The context to import files to
	 * @param pgn         Path to a PGN file to import
	 */
	public static void preloadPGN(final TestContext testContext, final String pgn)
	{
		final String path = File.separator + TestContext.IMPORTS_DIR + File.separator + pgn;
		final String pgnPath = IOCommands.class.getResource(path).getPath();
		final File pgnFile = new File(pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		final String command = IOCommandsTest.buildImportCommand(pgnFile);
		final String actualOutput = testContext.executeValidCommand(command);
		final String predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Checks that a command fails
	 *
	 * @param finalCommand The command that is destined to fail
	 */
	public void assertCommandFails(final String finalCommand)
	{
		Assert.assertNotNull(TestContext.COMMAND_CONSTRUCTION_ERROR, finalCommand);
		this.executeInvalidCommand(finalCommand);
	}

	/**
	 * Checks that a command is valid, executes it and then checks the output is valid
	 *
	 * @param finalCommand The command to execute
	 * @return The shell output of that command
	 */
	public String executeValidCommand(final String finalCommand)
	{
		Assert.assertNotNull(TestContext.COMMAND_CONSTRUCTION_ERROR, finalCommand);
		final CommandResult commandResult = this.shell.executeCommand(finalCommand);
		Assert.assertNotNull(TestContext.COMMAND_CONSTRUCTION_ERROR, commandResult);
		Assert.assertNotNull(TestContext.COMMAND_CONSTRUCTION_ERROR, commandResult.getResult());
		return commandResult.getResult().toString();
	}

	/**
	 * Getter for the Bootstrap application context
	 *
	 * @return The bootstrap application context
	 */
	public ApplicationContext getApplicationContext()
	{
		return this.applicationContext;
	}

	/**
	 * Returns the ChessIO for the current context
	 *
	 * @return The current context's ChessIO instance
	 */
	public ChessIO getChessIO()
	{
		return this.applicationContext.getBean(ChessIO.class);
	}

	/**
	 * Returns the ChessTagModder for the current context
	 *
	 * @return The current context's ChessTagModder instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	public ChessTagModder getChessTagModder()
	{
		return this.applicationContext.getBean(ChessTagModder.class);
	}

	/**
	 * Shutdown shell
	 */
	@After
	public void shutdown()
	{
		this.shell.stop();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		//noinspection ObjectToString
		return "TestContext{" +
			   "shell=" + this.shell +
			   ", bootstrap=" + this.bootstrap +
			   '}';
	}

	private void executeInvalidCommand(final String command)
	{
		final CommandResult commandResult = this.shell.executeCommand(command);
		Assert.assertNull(TestContext.COMMAND_SUCCEEDS_FAIL_EXPECTED, commandResult.getResult());
	}
}