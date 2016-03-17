/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.misc;

import org.junit.After;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.JLineShellComponent;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Core context holding key common data and abstract methods for implementing test contexts
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("ClassUnconnectedToPackage")
public abstract class TestContext
{
	/**
	 * black_win_mate_headless.pgn filename
	 */
	public static final    String              BLACK_WIN_MATE_HEADLESS_PGN  = "black_win_mate_headless.pgn";
	/**
	 * draw_headless.pgn filename
	 */
	public static final    String              DRAW_HEADLESS_PGN            = "draw_headless.pgn";
	/**
	 * Name of the dump directory relative to the binary. Files in here is where PGN-Extract-Alt should write test files to.
	 */
	public static final    String              DUMP_DIR                     = "dump";
	/**
	 * Name of a PGN file with no characters
	 */
	public static final    String              EMPTY_PGN                    = "empty.pgn";
	/**
	 * Name of the exports directory relative to the binary. Files in here should be used for overwrite testing only.
	 */
	public static final    String              EXPORTS_DIR                  = "exports";
	/**
	 * Name of a PGN file that doesn't exist
	 */
	public static final    String              FALSE_PGN_PATH               = "false.pgn";
	/**
	 * Name of the imports directory relative to the binary
	 */
	public static final    String              IMPORTS_DIR                  = "imports";
	/**
	 * incalculable_headless.pgn filename
	 */
	public static final    String              INCALCULABLE_HEADLESS_PGN    = "incalculable_headless.pgn";
	/**
	 * Name of a large PGN file with many valid games (and no invalid games)
	 */
	public static final    String              LARGE_PGN                    = "large.pgn";
	/**
	 * Name of a PGN file with multiple invalid and multiple valid games
	 */
	public static final    String              MULTI_INVALID_PGN            = "invalid_multi.pgn";
	/**
	 * Name of a PGN file with multiple valid games (and no invalid games)
	 */
	public static final    String              MULTI_PGN                    = "multi.pgn";
	/**
	 * Name of a PGN file that isn't remotely a PGN-looking file
	 */
	public static final    String              NOT_A_PGN                    = "not_a_pgn.test";
	/**
	 * Name of a PGN file with protected file permissions
	 */
	public static final    String              PROTECTED_PGN                = "protected.pgn";
	/**
	 * Name of a PGN file with a single, syntactically invalid game
	 */
	public static final    String              SINGLE_INVALID_PGN           = "invalid_single.pgn";
	/**
	 * Name of a PGN file with a single valid game which has been assessed for Machine Correlation
	 */
	public static final    String              SINGLE_MC_PGN                = "single_mc.pgn";
	/**
	 * Name of PGN file with a single valid game
	 */
	public static final    String              SINGLE_PGN                   = "single.pgn";
	/**
	 * Name of a smaller PGN file with games where a machine was likely used to help player Ivanov
	 */
	public static final    String              SMALL_IVANOV_PGN             = "small_ivanov.pgn";
	/**
	 * Error message for assertions on null resources
	 */
	public static final    String              TEST_RESOURCE_NOT_FOUND      = "Test resource not found";
	/**
	 * white_win_mate_headless.pgn filename
	 */
	public static final    String              WHITE_WIN_MATE_HEADLESS_PGN  = "white_win_mate_headless.pgn";
	/**
	 * A single space character
	 */
	protected static final char                SPACE                        = ' ';
	/**
	 * Error message on unknown IO error
	 */
	protected static final String              UNKNOWN_IO_ERROR             = "Unknown IO error on Import\n";
	/**
	 * Error messaged on unknown PGNSyntax Error
	 */
	protected static final String              UNKNOWN_PGNSYNTAX_ERROR      = "Unknown PGNSyntax error on Import\n";
	private static final   String              BEAN_RETRIEVAL_RETURNED_NULL = "Bean retrieval returned a NULL object";
	@SuppressWarnings("UnusedDeclaration")
	private static final   Logger              LOGGER                       = Logger.getLogger(TestContext.class.getName());
	/**
	 * The name of the target directory from project home where output is placed
	 */
	private static final   String              TARGET_DIR                   = "target";
	/**
	 * The name of the Test Classes directory where test files are placed
	 */
	private static final   String              TEST_CLASSES_DIR             = "test-classes";
	private                ApplicationContext  applicationContext           = null;
	private                Bootstrap           bootstrap                    = null;
	private                JLineShellComponent shell                        = null;

	/**
	 * Initialise TestContext
	 */
	protected TestContext()
	{
		this.bootstrap = new Bootstrap();
		this.shell = this.bootstrap.getJLineShellComponent();
		this.applicationContext = this.bootstrap.getApplicationContext();
	}

	/**
	 * Returns the ChessEvaluator for the current context
	 *
	 * @return The current context's ChessEvaluator instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	abstract protected Object getChessEvaluator();

	/**
	 * Returns the ChessFilterer for the current context
	 *
	 * @return The current context's ChessFilterer instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	abstract protected Object getChessFilterer();

	/**
	 * Returns the ChessIO for the current context
	 *
	 * @return The current context's ChessIO instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	abstract protected Object getChessIO();

	/**
	 * Returns the ChessTagModder for the current context
	 *
	 * @return The current context's ChessTagModder instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	abstract protected Object getChessTagModder();

	/**
	 * Loads any given context with files located at a given path
	 *
	 * @param pgn Path to a PGN file to import
	 */
	@SuppressWarnings("UnusedDeclaration")
	abstract protected void loadPGN(final String pgn);

	/**
	 * Attempts to find a PGN file and returns a File pointing to curWorkDir/target/test-classes/directory/filename Note that this function makes no
	 * guarantee that the File points to anything that actually exists!
	 *
	 * @param directory The parent directory of filename
	 * @param filename  The filename of the PGN file including the extension
	 * @return A file pointer to a PGN file
	 */
	public static File getPGNFile(final String directory, final String filename)
	{
		return new File(
				Paths.get(".").toAbsolutePath().normalize() +
				File.separator +
				TestContext.TARGET_DIR +
				File.separator +
				TestContext.TEST_CLASSES_DIR +
				File.separator +
				directory +
				File.separator +
				filename
		);
	}

	/**
	 * Converts a PGN file name to a validated PGN file
	 *
	 * @param pgn PGN filename
	 * @return File PGN File
	 */
	protected static File pgnToPGNFile(final String pgn)
	{
		final String path = File.separator + TestContext.IMPORTS_DIR + File.separator + pgn;
		final String pgnPath = TestContext.class.getResource(path).getPath();
		final File pgnFile = new File(pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		return pgnFile;
	}

	/**
	 * Shutdown shell
	 */
	@After
	public void shutdown()
	{
		this.bootstrap.getJLineShellComponent().stop();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter", "ObjectToString"})
	@Override
	public String toString()
	{
		return "TestContext{" +
			   "applicationContext=" + this.applicationContext +
			   ", shell=" + this.shell +
			   ", bootstrap=" + this.bootstrap +
			   '}';
	}

	/**
	 * Returns a Spring bean, if created, of type beanClass, else asserts a failure
	 *
	 * @param beanClass Class of bean to be retrieved
	 * @return The retrieved bean
	 */
	protected Object getBean(final Class beanClass)
	{
		final Object bean = this.applicationContext.getBean(beanClass);
		Assert.assertNotNull(TestContext.BEAN_RETRIEVAL_RETURNED_NULL, bean);
		return bean;
	}

	/**
	 * Getter for the current shell
	 *
	 * @return The current shell
	 */
	protected JLineShellComponent getShell()
	{
		return this.shell;
	}
}