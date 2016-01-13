package com.bigtobster.pgnextractalt.core;

import org.junit.After;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.JLineShellComponent;

import java.io.File;
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
	 * Error message for assertions on null resources
	 */
	public static final String TEST_RESOURCE_NOT_FOUND = "Test resource not found";

	@SuppressWarnings("UnusedDeclaration")
	private static final Logger              LOGGER                  = Logger.getLogger(TestContext.class.getName());
	private static final String              PGN_DIR_PATH            = File.separator;
	/**
	 * Path a PGN file with a single valid game
	 */
	public static final  String              SINGLE_PGN_PATH         = TestContext.PGN_DIR_PATH + "single.pgn";
	/**
	 * Path to a PGN file that isn't remotely a PGN-looking file
	 */
	public static final  String              NOT_A_PGN_PGN_PATH      = TestContext.PGN_DIR_PATH + "not_a_pgn.test";
	/**
	 * Path to a PGN file with multiple invalid and multiple valid games
	 */
	public static final  String              MULTI_INVALID_PGN_PATH  = TestContext.PGN_DIR_PATH + "invalid_multi.pgn";
	/**
	 * Path to a PGN file with multiple valid games (and no invalid games)
	 */
	public static final  String              MULTI_PGN_PATH          = TestContext.PGN_DIR_PATH + "multi.pgn";
	/**
	 * Path to a large PGN file with many valid games (and no invalid games)
	 */
	public static final  String              LARGE_PGN_PATH          = TestContext.PGN_DIR_PATH + "large.pgn";
	/**
	 * Path to a PGN file with a single, syntactically invalid game
	 */
	public static final  String              SINGLE_INVALID_PGN_PATH = TestContext.PGN_DIR_PATH + "invalid_single.pgn";
	/**
	 * Path to a PGN file with no characters
	 */
	public static final  String              EMPTY_PGN_PATH          = TestContext.PGN_DIR_PATH + "empty.pgn";
	/**
	 * Path to a PGN file that doesn't exist
	 */
	public static final  String              FALSE_PGN_PATH          = TestContext.PGN_DIR_PATH + "false.pgn";
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
	 * Getter for the Bootstrap application context
	 *
	 * @return The bootstrap application context
	 */
	public ApplicationContext getApplicationContext()
	{
		return this.applicationContext;
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