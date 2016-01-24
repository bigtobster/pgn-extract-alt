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
	private static final Logger LOGGER                  = Logger.getLogger(IOCommandsTest.class.getName());
	private static final char   SPACE                   = ' ';

	/**
	 * Loads any given context with files located at a given path
	 * @param testContext The context to import files to
	 * @param pgn Path to a PGN file to import
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

	private static String buildExportCommand(final File file)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.FILE_PATH_OPTION, file.getPath());
		return TestContext.buildCommand(IOCommands.getExportCommand(), optionArgs);
	}

	private static String buildImportCommand(final File file)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.FILE_PATH_OPTION, file.getPath());
		return TestContext.buildCommand(IOCommands.getImportCommand(), optionArgs);
	}

	private static String createSuccessfulExportMessage(final TestContext testContext)
	{
		return IOCommands.SUCCESSFUL_EXPORT +
			   IOCommandsTest.SPACE +
			   testContext.getChessIO().getGames().size() +
			   IOCommandsTest.SPACE +
			   IOCommands.GAMES_EXPORTED;
	}

	private static String createSuccessfulImportMessage(final TestContext testContext)
	{
		return IOCommands.SUCCESSFUL_IMPORT +
			   IOCommandsTest.SPACE +
			   testContext.getChessIO().getGames().size() +
			   IOCommandsTest.SPACE +
			   IOCommands.GAMES_IMPORTED;
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
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

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
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);
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
		IOCommandsTest.preloadPGN(testContext, TestContext.MULTI_PGN);

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
