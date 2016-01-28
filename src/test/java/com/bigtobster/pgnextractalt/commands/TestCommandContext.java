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

import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.chess.ChessTagModder;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.springframework.shell.core.CommandResult;

import java.io.File;
import java.util.HashMap;

/**
 * Testing context for commands Should only be accessible to members of commands package
 *
 * @author Toby Leheup
 */
final class TestCommandContext extends TestContext
{
	private static final String COMMAND_CONSTRUCTION_ERROR     = "Command construction error";
	private static final String COMMAND_SUCCEEDS_FAIL_EXPECTED = "Command succeeds when failure expected";
	private static final String CONSOLE_MESSAGE_DIFFERS        = "Console Message differs from expected case";
	private static final String MISSING_TAG                    = "Tag list does not contain tag ";

	/**
	 * Weaker test than assertOutputMatchesPredicted as it tests only that output contains a certain substring
	 *
	 * @param actualOutput The output on the shell
	 * @param substr       The substring in the output of the command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	static void assertCommandOutputContains(final String actualOutput, final String substr)
	{
		Assert.assertNotNull(TestCommandContext.COMMAND_CONSTRUCTION_ERROR, substr);
		Assert.assertTrue(TestCommandContext.MISSING_TAG, actualOutput.contains(substr));
	}

	/**
	 * Runs a non-null command and asserts that it's output is not NULL and that it matches the non-null predictedOutput
	 *
	 * @param actualOutput    The output on the shell
	 * @param predictedOutput The predicted output
	 */
	static void assertOutputMatchesPredicted(final String actualOutput, final String predictedOutput)
	{
		Assert.assertNotNull(TestCommandContext.COMMAND_CONSTRUCTION_ERROR, predictedOutput);
		Assert.assertEquals(TestCommandContext.CONSOLE_MESSAGE_DIFFERS, predictedOutput, actualOutput);
	}

	/**
	 * Builds a command string up from a basic command plus a Hash Map of Option, Argument value pairs to get "command [--&lt;option&gt; &lt;
	 * arg&gt;]*"
	 *
	 * @param command         The base command
	 * @param optionValuesMap The hash map of Option, Argument pairs
	 * @return The fully constructed command
	 */
	static String buildCommand(final String command, final HashMap<String, String> optionValuesMap)
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
	 * Builds an import command
	 *
	 * @param file The file being imported
	 * @return The full, complete import command including the reference to the file to be imported
	 */
	static String buildImportCommand(final File file)
	{
		//Execute command
		final HashMap<String, String> optionArgs = new HashMap<String, String>(1);
		optionArgs.put(IOCommands.FILE_PATH_OPTION, file.getPath());
		return TestCommandContext.buildCommand(IOCommands.getImportCommand(), optionArgs);
	}

	/**
	 * Attempts to find a PGN file and returns a File pointing to curWorkDir/target/test-classes/directory/filename Note that this function makes no
	 * guarantee that the File points to anything that actually exists!
	 *
	 * @param directory The parent directory of filename
	 * @param filename  The filename of the PGN file including the extension
	 * @return A file pointer to a PGN file
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "ProtectedMemberInFinalClass", "WeakerAccess"})
	protected static File getPGNFile(final String directory, final String filename)
	{
		return TestContext.getPGNFile(directory, filename);
	}

	/**
	 * Returns the ChessIO for the current context
	 *
	 * @return The current context's ChessIO instance
	 */
	@Override
	protected ChessIO getChessIO()
	{
		return (ChessIO) this.getBean(ChessIO.class);
	}

	/**
	 * Returns the ChessTagModder for the current context
	 *
	 * @return The current context's ChessTagModder instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	@Override
	protected ChessTagModder getChessTagModder()
	{
		return (ChessTagModder) this.getBean(ChessTagModder.class);
	}

	/**
	 * Loads any given context with files located at a given path
	 *
	 * @param pgn Path to a PGN file to import
	 */
	@Override
	protected void preloadPGN(final String pgn)
	{
		final File pgnFile = TestContext.pgnToPGNFile(pgn);
		final String command = TestCommandContext.buildImportCommand(pgnFile);
		final String actualOutput = this.executeValidCommand(command);
		final String predictedOutput = this.createSuccessfulImportMessage();
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Checks that a command fails
	 *
	 * @param finalCommand The command that is destined to fail
	 */
	void assertCommandFails(final String finalCommand)
	{
		Assert.assertNotNull(TestCommandContext.COMMAND_CONSTRUCTION_ERROR, finalCommand);
		this.executeInvalidCommand(finalCommand);
	}

	/**
	 * Creates a message for a successful import
	 *
	 * @return The message
	 */
	String createSuccessfulImportMessage()
	{
		return IOCommands.SUCCESSFUL_IMPORT +
			   TestContext.SPACE +
			   this.getChessIO().getGames().size() +
			   TestContext.SPACE +
			   IOCommands.GAMES_IMPORTED;
	}

	/**
	 * Checks that a command is valid, executes it and then checks the output is valid
	 *
	 * @param finalCommand The command to execute
	 * @return The shell output of that command
	 */
	String executeValidCommand(final String finalCommand)
	{
		final CommandResult commandResult = this.executeCommand(finalCommand);
		Assert.assertNotNull(TestCommandContext.COMMAND_CONSTRUCTION_ERROR, commandResult);
		Assert.assertNotNull(TestCommandContext.COMMAND_CONSTRUCTION_ERROR, commandResult.getResult());
		return commandResult.getResult().toString();
	}

	private CommandResult executeCommand(final String command)
	{
		Assert.assertNotNull(TestCommandContext.COMMAND_CONSTRUCTION_ERROR, command);
		return this.getShell().executeCommand(command);
	}

	private void executeInvalidCommand(final String command)
	{
		final CommandResult commandResult = this.executeCommand(command);
		Assert.assertNull(TestCommandContext.COMMAND_SUCCEEDS_FAIL_EXPECTED, commandResult.getResult());
	}
}
