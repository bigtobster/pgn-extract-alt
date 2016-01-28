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
import java.io.IOException;
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

	private static String buildExportCommand(final File file)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.FILE_PATH_OPTION, file.getPath());
		return TestCommandContext.buildCommand(IOCommands.getExportCommand(), optionArgs);
	}

	private static String createSuccessfulExportMessage(final TestCommandContext testCommandContext)
	{
		return IOCommands.SUCCESSFUL_EXPORT +
			   IOCommandsTest.SPACE +
			   testCommandContext.getChessIO().getGames().size() +
			   IOCommandsTest.SPACE +
			   IOCommands.GAMES_EXPORTED;
	}

	private static void genericImportTest(final String importFileName, final boolean isSuccessful, final String failureMessage)
	{
		final File pgnFile = TestCommandContext.getPGNFile(TestContext.IMPORTS_DIR, importFileName);

		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String command = TestCommandContext.buildImportCommand(pgnFile);
		final String actualOutput = testCommandContext.executeValidCommand(command);
		final String predictedOutput;
		if(isSuccessful)
		{
			predictedOutput = testCommandContext.createSuccessfulImportMessage();
		}
		else
		{
			predictedOutput = failureMessage;
		}
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
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

	private static void successfulExportToDestination(final String pgnFilename, final boolean isDestPathExist)
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final File pgnFile;
		final String parentDir;

		testCommandContext.preloadPGN(TestContext.MULTI_PGN);

		if(isDestPathExist)
		{
			parentDir = TestContext.EXPORTS_DIR;
		}
		else
		{
			parentDir = TestContext.DUMP_DIR;
		}
		//noinspection MagicCharacter
		pgnFile = TestCommandContext.getPGNFile(parentDir, UUID.randomUUID().toString() + '-' + pgnFilename);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		String command = IOCommandsTest.buildExportCommand(pgnFile);
		String actualOutput = testCommandContext.executeValidCommand(command);
		String predictedOutput = IOCommandsTest.createSuccessfulExportMessage(testCommandContext);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

		//Test Export is importable
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		command = TestCommandContext.buildImportCommand(pgnFile);
		actualOutput = testCommandContext.executeValidCommand(command);
		predictedOutput = testCommandContext.createSuccessfulImportMessage();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Export functionality on an empty PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportNoPathTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		String command = IOCommands.getExportCommand();
		testCommandContext.assertCommandFails(command);
		command = IOCommandsTest.buildExportCommand(new File(""));
		testCommandContext.assertCommandFails(command);
	}

	/**
	 * Tests Export functionality on an existing empty PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportToEmptyPGNTest()
	{
		IOCommandsTest.successfulExportToDestination(TestContext.EMPTY_PGN, true);
	}

	/**
	 * Tests Export functionality to a new file in the current directory
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportToNewPGNNonCurDirTest()
	{
		IOCommandsTest.successfulExportToDestination(TestContext.MULTI_PGN, false);
	}

	/**
	 * Tests Export functionality on an existing empty PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportToNonEmptyPGNTest()
	{

		IOCommandsTest.successfulExportToDestination(TestContext.MULTI_PGN, true);
	}

	/**
	 * Tests Export functionality on a PGN that has protected file permissions (000)
	 */
	@Test
	public void exportToProtectedPGNTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();

		//Pre-load
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);

		@SuppressWarnings("MagicCharacter") final
		File pgnFile = TestCommandContext.getPGNFile(TestContext.EXPORTS_DIR, UUID.randomUUID().toString() + '-' + TestContext.PROTECTED_PGN);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		try
		{
			//noinspection ResultOfMethodCallIgnored
			pgnFile.createNewFile();
		}
		catch(final IOException e)
		{
			Assert.fail(e.getMessage());
		}
		IOCommandsTest.makeFileProtected(pgnFile, false, false, false);
		String command = IOCommandsTest.buildExportCommand(pgnFile);
		String actualOutput = testCommandContext.executeValidCommand(command);
		String predictedOutput = IOCommands.FAILED_EXPORT +
								 IOCommandsTest.SPACE +
								 IOCommands.PGN_NOT_WRITABLE +
								 IOCommandsTest.SPACE +
								 pgnFile.getPath();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

		IOCommandsTest.makeFileProtected(pgnFile, true, false, false);
		command = IOCommandsTest.buildExportCommand(pgnFile);
		actualOutput = testCommandContext.executeValidCommand(command);
		predictedOutput = IOCommands.FAILED_EXPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_WRITABLE + IOCommandsTest.SPACE + pgnFile.getPath();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

		IOCommandsTest.makeFileProtected(pgnFile, false, true, false);
		command = IOCommandsTest.buildExportCommand(pgnFile);
		actualOutput = testCommandContext.executeValidCommand(command);
		predictedOutput = IOCommands.FAILED_EXPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_WRITABLE + IOCommandsTest.SPACE + pgnFile.getPath();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Import functionality on an empty PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importEmptyPGNTest()
	{
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.INVALID_SYNTAX;
		IOCommandsTest.genericImportTest(TestContext.EMPTY_PGN, false, predictedOutput);
	}

	/**
	 * Tests Import functionality on a non-existent path
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importFalsePGNTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String command = TestCommandContext.buildImportCommand(new File(TestContext.FALSE_PGN_PATH));
		final String actualOutput = testCommandContext.executeValidCommand(command);
		final File file = new File(TestContext.FALSE_PGN_PATH);
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.NO_FILE_AT + IOCommandsTest.SPACE + file
				.getAbsolutePath();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Import functionality on a large PGN file with only multiple valid entries
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importLargePGNTest()
	{
		IOCommandsTest.genericImportTest(TestContext.LARGE_PGN, true, null);
	}

	/**
	 * Tests Import functionality on a PGN file with multiple valid and invalid entries
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importMultiInvalidPGNTest()
	{
		IOCommandsTest.genericImportTest(TestContext.MULTI_INVALID_PGN, true, null);
	}

	/**
	 * Tests Import functionality on a PGN file with only multiple valid entries
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importMultiPGNTest()
	{
		IOCommandsTest.genericImportTest(TestContext.MULTI_PGN, true, null);
	}

	/**
	 * Tests Import functionality without adding a file to import
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importNoPathTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		String command = IOCommands.getImportCommand();
		testCommandContext.assertCommandFails(command);
		command = TestCommandContext.buildImportCommand(new File(""));
		testCommandContext.assertCommandFails(command);
	}

	/**
	 * Tests Import functionality on a PGN that isn't remotely a PGN file in content
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importNotAPGNTest()
	{
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.NOT_A_PGN_FILE;
		IOCommandsTest.genericImportTest(TestContext.NOT_A_PGN, false, predictedOutput);
	}

	/**
	 * Tests Import functionality on a PGN that has protected file permissions (000)
	 */
	@Test
	public void importProtectedPGNTest()
	{
		final File pgnFile = TestCommandContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.PROTECTED_PGN);
		IOCommandsTest.makeFileProtected(pgnFile, false, false, false);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String command = TestCommandContext.buildImportCommand(pgnFile);
		final String actualOutput = testCommandContext.executeValidCommand(command);
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.PGN_NOT_READABLE + IOCommandsTest.SPACE +
									   pgnFile.getPath();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests Import functionality on a PGN file with a single invalid entry
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importSingleInvalidPGNTest()
	{
		IOCommandsTest.genericImportTest(TestContext.SINGLE_INVALID_PGN, true, null);
	}

	/**
	 * Tests Import functionality on a single PGN file
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importSinglePGNTest()
	{
		IOCommandsTest.genericImportTest(TestContext.SINGLE_PGN, true, null);
	}

	/**
	 * Tests Reset functionality
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void resetTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.assertCommandFails(IOCommands.getResetCommand());
		testCommandContext.preloadPGN(TestContext.MULTI_PGN);
		final String command = IOCommands.getResetCommand();
		final String actualOutput = testCommandContext.executeValidCommand(command);
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, IOCommands.SUCCESSFUL_RESET);
	}
}
