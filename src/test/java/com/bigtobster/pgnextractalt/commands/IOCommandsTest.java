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

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Tests the Import/Export Commands Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"ClassWithTooManyMethods", "UnusedDeclaration"})

public class IOCommandsTest
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(IOCommandsTest.class.getName());
	private static final char   SPACE  = ' ';

	/**
	 * Builds an import command
	 *
	 * @param file The file being imported
	 * @return The full, complete import command including the reference to the file to be imported
	 */
	public static String buildImportCommand(final File file)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.FILE_PATH_OPTION, file.getPath());
		return TestContext.buildCommand(IOCommands.getImportCommand(), optionArgs);
	}

	/**
	 * Creates a message for a successful import
	 *
	 * @param testContext The current context
	 * @return The message
	 */
	public static String createSuccessfulImportMessage(final TestContext testContext)
	{
		return IOCommands.SUCCESSFUL_IMPORT +
			   IOCommandsTest.SPACE +
			   testContext.getChessIO().getGames().size() +
			   IOCommandsTest.SPACE +
			   IOCommands.GAMES_IMPORTED;
	}

	private static String buildExportCommand(final File file)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.FILE_PATH_OPTION, file.getPath());
		return TestContext.buildCommand(IOCommands.getExportCommand(), optionArgs);
	}

	private static String createSuccessfulExportMessage(final TestContext testContext)
	{
		return IOCommands.SUCCESSFUL_EXPORT +
			   IOCommandsTest.SPACE +
			   testContext.getChessIO().getGames().size() +
			   IOCommandsTest.SPACE +
			   IOCommands.GAMES_EXPORTED;
	}

	private static void makeFileProtected(final File pgnFile, final boolean read, final boolean write, final boolean execute)
	{
		//noinspection ResultOfMethodCallIgnored
		pgnFile.setReadable(read);
		//noinspection ResultOfMethodCallIgnored
		pgnFile.setWritable(write);
		//noinspection ResultOfMethodCallIgnored
		pgnFile.setExecutable(execute);
	}

	/**
	 * Tests Export functionality on an empty PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportNoPathTest()
	{
		final TestContext testContext = new TestContext();
		String command = IOCommands.getExportCommand();
		testContext.assertCommandFails(command);
		command = IOCommandsTest.buildExportCommand(new File(""));
		testContext.assertCommandFails(command);
	}

	/**
	 * Tests Export functionality on an existing empty PGN file
	 */
	@Test
	public void exportToEmptyPGNTest()
	{
		this.successfulExportToDestination(TestContext.EMPTY_PGN, true);
	}

	/**
	 * Tests Export functionality to a new file in the current directory
	 */
	@Test
	public void exportToNewPGNNonCurDirTest()
	{
		this.successfulExportToDestination(TestContext.MULTI_PGN, false);
	}

	/**
	 * Tests Export functionality on an existing empty PGN file
	 */
	@Test
	public void exportToNonEmptyPGNTest()
	{

		this.successfulExportToDestination(TestContext.MULTI_PGN, true);
	}

	/**
	 * Tests Export functionality on a PGN that has protected file permissions (000)
	 */
	@Test
	public void exportToProtectedPGNTest()
	{
		final TestContext testContext = new TestContext();

		//Pre-load
		TestContext.preloadPGN(testContext, TestContext.MULTI_PGN);

		final String path = File.separator + TestContext.EXPORTS_DIR + File.separator + TestContext.PROTECTED_PGN;
		final String pgnPath = this.getClass().getResource(path).getPath();
		final File pgnFile = new File(pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);

		IOCommandsTest.makeFileProtected(pgnFile, false, false, false);
		String command = IOCommandsTest.buildExportCommand(pgnFile);
		String actualOutput = testContext.executeValidCommand(command);
		String predictedOutput = IOCommands.FAILED_EXPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_WRITABLE + IOCommandsTest.SPACE + pgnPath;
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

		IOCommandsTest.makeFileProtected(pgnFile, true, false, false);
		command = IOCommandsTest.buildExportCommand(pgnFile);
		actualOutput = testContext.executeValidCommand(command);
		predictedOutput = IOCommands.FAILED_EXPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_WRITABLE + IOCommandsTest.SPACE + pgnPath;
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

		IOCommandsTest.makeFileProtected(pgnFile, false, true, false);
		command = IOCommandsTest.buildExportCommand(pgnFile);
		actualOutput = testContext.executeValidCommand(command);
		predictedOutput = IOCommands.FAILED_EXPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_WRITABLE + IOCommandsTest.SPACE + pgnPath;
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Import functionality on an empty PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importEmptyPGNTest()
	{
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.INVALID_SYNTAX;
		this.genericImportTest(TestContext.EMPTY_PGN, false, predictedOutput);
	}

	/**
	 * Tests Import functionality on a non-existent path
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importFalsePGNTest()
	{
		final TestContext testContext = new TestContext();
		final String command = IOCommandsTest.buildImportCommand(new File(TestContext.FALSE_PGN_PATH));
		final String actualOutput = testContext.executeValidCommand(command);
		final File file = new File(TestContext.FALSE_PGN_PATH);
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.NO_FILE_AT + IOCommandsTest.SPACE + file
				.getAbsolutePath();
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Import functionality on a large PGN file with only multiple valid entries
	 */
	@Test
	public void importLargePGNTest()
	{
		this.genericImportTest(TestContext.LARGE_PGN, true, null);
	}

	/**
	 * Tests Import functionality on a PGN file with multiple valid and invalid entries
	 */
	@Test
	public void importMultiInvalidPGNTest()
	{
		this.genericImportTest(TestContext.MULTI_INVALID_PGN, true, null);
	}

	/**
	 * Tests Import functionality on a PGN file with only multiple valid entries
	 */
	@Test
	public void importMultiPGNTest()
	{
		this.genericImportTest(TestContext.MULTI_PGN, true, null);
	}

	/**
	 * Tests Import functionality without adding a file to import
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importNoPathTest()
	{
		final TestContext testContext = new TestContext();
		String command = IOCommands.getImportCommand();
		testContext.assertCommandFails(command);
		command = IOCommandsTest.buildImportCommand(new File(""));
		testContext.assertCommandFails(command);
	}

	/**
	 * Tests Import functionality on a PGN that isn't remotely a PGN file in content
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importNotAPGNTest()
	{
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.NOT_A_PGN_FILE;
		this.genericImportTest(TestContext.NOT_A_PGN, false, predictedOutput);
	}

	/**
	 * Tests Import functionality on a PGN that has protected file permissions (000)
	 */
	@Test
	public void importProtectedPGNTest()
	{
		final String path = File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.PROTECTED_PGN;
		final String pgnPath = this.getClass().getResource(path).getPath();
		final File pgnFile = new File(pgnPath);
		IOCommandsTest.makeFileProtected(pgnFile, false, false, false);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		final TestContext testContext = new TestContext();
		final String command = IOCommandsTest.buildImportCommand(pgnFile);
		final String actualOutput = testContext.executeValidCommand(command);
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_READABLE + IOCommandsTest.SPACE + pgnPath;
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Import functionality on a PGN file with a single invalid entry
	 */
	@Test
	public void importSingleInvalidPGNTest()
	{
		this.genericImportTest(TestContext.SINGLE_INVALID_PGN, true, null);
	}

	/**
	 * Tests Import functionality on a single PGN file
	 */
	@Test
	public void importSinglePGNTest()
	{
		this.genericImportTest(TestContext.SINGLE_PGN, true, null);
	}

	/**
	 * Tests Reset functionality
	 */
	@Test
	public void resetTest()
	{
		final TestContext testContext = new TestContext();
		final String path = File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.SINGLE_PGN;
		final String pgnPath = this.getClass().getResource(path).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		testContext.assertCommandFails(IOCommands.getResetCommand());
		TestContext.preloadPGN(testContext, TestContext.MULTI_PGN);
		final String command = IOCommands.getResetCommand();
		final String actualOutput = testContext.executeValidCommand(command);
		TestContext.assertOutputMatchesPredicted(actualOutput, IOCommands.SUCCESSFUL_RESET);
	}

	private void genericImportTest(final String importFileName, final boolean isSuccessful, final String failureMessage)
	{
		final String path = File.separator + TestContext.IMPORTS_DIR + File.separator + importFileName;
		final String pgnPath = this.getClass().getResource(path).getPath();
		final File pgnFile = new File(pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		final TestContext testContext = new TestContext();
		final String command = IOCommandsTest.buildImportCommand(pgnFile);
		final String actualOutput = testContext.executeValidCommand(command);
		final String predictedOutput;
		if(isSuccessful)
		{
			predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		}
		else
		{
			predictedOutput = failureMessage;
		}
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	private void successfulExportToDestination(final String destinationPath, final boolean isDestPathExist)
	{
		final TestContext testContext = new TestContext();

		//Pre-load
		TestContext.preloadPGN(testContext, TestContext.MULTI_PGN);

		//Export
		final String pgnPath;
		if(isDestPathExist)
		{
			final String exportFilePath = File.separator + TestContext.EXPORTS_DIR + File.separator + destinationPath;
			pgnPath = this.getClass().getResource(exportFilePath).getPath();
		}
		else
		{
			//noinspection MagicCharacter
			pgnPath = System.getProperty("user.dir") +
					  File.separator +
					  TestContext.TARGET_DIR +
					  File.separator +
					  TestContext.TEST_CLASSES_DIR +
					  File.separator +
					  TestContext.DUMP_DIR +
					  File.separator +
					  UUID.randomUUID() +
					  '-' +
					  destinationPath;
		}
		final File pgnFile = new File(pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		String command = IOCommandsTest.buildExportCommand(pgnFile);
		String actualOutput = testContext.executeValidCommand(command);
		String predictedOutput = IOCommandsTest.createSuccessfulExportMessage(testContext);
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

		//Test Export is importable
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		command = IOCommandsTest.buildImportCommand(pgnFile);
		actualOutput = testContext.executeValidCommand(command);
		predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		TestContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}
}
