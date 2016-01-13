package com.bigtobster.pgnextractalt.commands;

import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.Shell;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Tests the Import/Export Commands Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */

@SuppressWarnings("ClassWithTooManyMethods")

public class IOCommandsTest
{
	private static final String CONSOLE_MESSAGE_DIFFERS = "Console Message differs from expected case";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                  = Logger.getLogger(IOCommandsTest.class.getName());
	private static final char   SPACE                   = ' ';

	/**
	 * Builds a command string up from a basic command plus a Hash Map of Option, Argument value pairs to get "command [--<option> <arg>]*"
	 *
	 * @param command         The base command
	 * @param optionValuesMap The hash map of Option, Argument pairs
	 * @return The fully constructed command
	 */
	private static String buildCommand(final String command, final HashMap<String, String> optionValuesMap)
	{
		String newCommand = command;
		if(optionValuesMap != null)
		{
			for(final String key : optionValuesMap.keySet())
			{
				newCommand = command + " --" + key + IOCommandsTest.SPACE + optionValuesMap.get(key);
			}
		}
		return newCommand;
	}

	private static String createSuccessfulImportMessage(final TestContext testContext)
	{
		return IOCommands.SUCCESSFUL_IMPORT +
			   IOCommandsTest.SPACE +
			   testContext.getApplicationContext().getBean(ChessIO.class).getGamesCount() +
			   IOCommandsTest.SPACE +
			   IOCommands.GAMES_IMPORTED;
	}

	private static String executeTestImportCommand(final String path, final Shell shell)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.IMPORT_FILE_PATH_OPTION, path);
		final String finalCommand = IOCommandsTest.buildCommand(IOCommands.getImportCommand(), optionArgs);
		return shell.executeCommand(finalCommand).getResult().toString();
	}

	/**
	 * Tests Export functionality after Import
	 */

	@Test
	public void exportTest()
	{
		final TestContext testContext = new TestContext();
		final Shell shell = testContext.getShell();
		final String pgnPath = this.getClass().getResource(TestContext.SINGLE_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		CommandResult cr = shell.executeCommand(IOCommands.getExportCommand());
		Assert.assertNotNull("Export unavailable before successful import", cr);
		IOCommandsTest.executeTestImportCommand(pgnPath, shell);
		cr = shell.executeCommand(IOCommands.getExportCommand());
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, IOCommands.SUCCESSFUL_EXPORT, cr.getResult().toString());
	}

	/**
	 * Tests Import functionality on an empty PGN file
	 */
	@Test
	public void importEmptyPGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.EMPTY_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.INVALID_SYNTAX;
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a non-existent path
	 */
	@Test
	public void importFalsePGNTest()
	{
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(TestContext.FALSE_PGN_PATH, testContext.getShell());
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.NO_FILE_AT + IOCommandsTest.SPACE + TestContext
				.FALSE_PGN_PATH;
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a large PGN file with only multiple valid entries
	 */
	@Test
	public void importLargePGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.LARGE_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a PGN file with multiple valid and invalid entries
	 */
	@Test
	public void importMultiInvalidPGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.MULTI_INVALID_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a PGN file with only multiple valid entries
	 */
	@Test
	public void importMultiPGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.MULTI_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a PGN that isn't remotely a PGN file in content
	 */
	@Test
	public void importNotAPGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.NOT_A_PGN_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommands.FAILED_IMPORT + IOCommandsTest.SPACE + IOCommands.NOT_A_PGN_FILE;
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a PGN file with a single invalid entry
	 */
	@Test
	public void importSingleInvalidPGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.SINGLE_INVALID_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Import functionality on a single PGN file
	 */
	@Test
	public void importSinglePGNTest()
	{
		final String pgnPath = this.getClass().getResource(TestContext.SINGLE_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		final TestContext testContext = new TestContext();
		final String resultOutput = IOCommandsTest.executeTestImportCommand(pgnPath, testContext.getShell());
		final String predictedOutput = IOCommandsTest.createSuccessfulImportMessage(testContext);
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, predictedOutput, resultOutput);
	}

	/**
	 * Tests Reset functionality
	 */
	@Test
	public void resetTest()
	{
		final TestContext testContext = new TestContext();
		final Shell shell = testContext.getShell();
		final String pgnPath = this.getClass().getResource(TestContext.SINGLE_PGN_PATH).getPath();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnPath);
		CommandResult cr = shell.executeCommand(IOCommands.getResetCommand());
		Assert.assertNotNull("Reset unavailable before successful import", cr);
		IOCommandsTest.executeTestImportCommand(pgnPath, shell);
		cr = shell.executeCommand(IOCommands.getResetCommand());
		Assert.assertEquals(IOCommandsTest.CONSOLE_MESSAGE_DIFFERS, IOCommands.SUCCESSFUL_RESET, cr.getResult().toString());
	}
}
