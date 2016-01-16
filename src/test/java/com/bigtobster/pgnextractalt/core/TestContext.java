package com.bigtobster.pgnextractalt.core;

import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.chess.ChessTagModder;
import org.junit.After;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.JLineShellComponent;

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
	 * Assertion error message on expected console output being different to the actual result
	 */
	public static final  String              CONSOLE_MESSAGE_DIFFERS = "Console Message differs from expected case";
	/**
	 * Name of the dump directory relative to the binary. Files in here is where PGN-Extract-Alt should write test files to.
	 */
	public static final  String              DUMP_DIR                = "dump";
	/**
	 * Name of a PGN file with no characters
	 */
	public static final  String              EMPTY_PGN               = "empty.pgn";
	/**
	 * Name of the exports directory relative to the binary. Files in here should be used for overwrite testing only.
	 */
	public static final  String              EXPORTS_DIR             = "exports";
	/**
	 * Name of a PGN file that doesn't exist
	 */
	public static final  String              FALSE_PGN_PATH          = "false.pgn";
	/**
	 * Name of the imports directory relative to the binary
	 */
	public static final  String              IMPORTS_DIR             = "imports";
	/**
	 * Name of a large PGN file with many valid games (and no invalid games)
	 */
	public static final  String              LARGE_PGN               = "large.pgn";
	/**
	 * Name of a PGN file with multiple invalid and multiple valid games
	 */
	public static final  String              MULTI_INVALID_PGN       = "invalid_multi.pgn";
	/**
	 * Name of a PGN file with multiple valid games (and no invalid games)
	 */
	public static final  String              MULTI_PGN               = "multi.pgn";
	/**
	 * Name of a PGN file that isn't remotely a PGN-looking file
	 */
	public static final  String              NOT_A_PGN               = "not_a_pgn.test";
	/**
	 * Name of a PGN file with protected file permissions
	 */
	public static final  String              PROTECTED_PGN           = "protected.pgn";
	/**
	 * Name of a PGN file with a single, syntactically invalid game
	 */
	public static final  String              SINGLE_INVALID_PGN      = "invalid_single.pgn";
	/**
	 * Name of PGN file with a single valid game
	 */
	public static final  String              SINGLE_PGN              = "single.pgn";
	/**
	 * The name of the target directory from project home where output is placed
	 */
	public static final  String              TARGET_DIR              = "target";
	/**
	 * The name of the Test Classes directory where test files are placed
	 */
	public static final  String              TEST_CLASSES_DIR        = "test-classes";
	/**
	 * Error message for assertions on null resources
	 */
	public static final  String              TEST_RESOURCE_NOT_FOUND = "Test resource not found";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger              LOGGER                  = Logger.getLogger(TestContext.class.getName());
	private static final char                SPACE                   = ' ';
	private              ApplicationContext  applicationContext      = null;
	private              Bootstrap           bootstrap               = null;
	private              JLineShellComponent shell                   = null;

	/**
	 * Initialise TestContext
	 */
	public TestContext()
	{
		this.bootstrap = new Bootstrap();
		this.shell = this.bootstrap.getJLineShellComponent();
		this.applicationContext = this.bootstrap.getApplicationContext();
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
	 * Return shell
	 *
	 * @return The current shell
	 */
	public JLineShellComponent getShell()
	{
		return this.shell;
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
}