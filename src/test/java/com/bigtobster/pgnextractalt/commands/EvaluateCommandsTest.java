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
import org.junit.Test;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Tests Evaluate Commands works as expected Created by Toby Leheup on 15/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class EvaluateCommandsTest
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(EvaluateCommandsTest.class.getName());
	private static final String SPACE  = " ";

	private static String buildEvaluateMachineCorrelationCommand()
	{
		final String command = EvaluateCommands.getEvaluateMachineCorrelationCommand();
		return TestCommandContext.buildCommand(command);
	}

	private static String buildEvaluateMachineCorrelationCommand(final Integer depth, final Integer wait, final Boolean force)
	{
		final String command = EvaluateCommands.getEvaluateMachineCorrelationCommand();
		final HashMap<String, String> args = new HashMap<String, String>(3);
		if(depth != null)
		{
			args.put(EvaluateCommands.DEPTH_OPTION, depth.toString());
		}
		if(wait != null)
		{
			args.put(EvaluateCommands.WAIT_OPTION, wait.toString());
		}
		if(force != null)
		{
			args.put(EvaluateCommands.FORCE_OPTION, force.toString());
		}
		return TestCommandContext.buildCommand(command, args);
	}

	private static String buildEvaluateResultCommand()
	{
		final String command = EvaluateCommands.getEvaluateResultCommand();
		return TestCommandContext.buildCommand(command);
	}

	/**
	 * Tests evaluating the machine correlation when no games have been inserted
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "JUnitTestMethodWithNoAssertions"})
	@Test
	public void evaluateMachineCorrelationNoImportedGamesTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String finalCommand = EvaluateCommandsTest.buildEvaluateMachineCorrelationCommand();
		testCommandContext.assertCommandFails(finalCommand);
	}

	/**
	 * Tests that non-default values in machine correlation perform as expected
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "JUnitTestMethodWithNoAssertions"})
	@Test
	public void evaluateMachineCorrelationNonDefaultsTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.SINGLE_PGN);
		String finalCommand = EvaluateCommandsTest.buildEvaluateMachineCorrelationCommand(- 25, 10, true);
		String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		String predictedOutput = EvaluateCommands.FAILED_EVALUATION + EvaluateCommandsTest.SPACE + EvaluateCommands.DEPTH_OPTION +
								 EvaluateCommandsTest.SPACE + EvaluateCommands
				.PARAMETER_MUST_BE_GREATER_THAN_0;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
		finalCommand = EvaluateCommandsTest.buildEvaluateMachineCorrelationCommand(25, - 10, true);
		actualOutput = testCommandContext.executeValidCommand(finalCommand);
		predictedOutput = EvaluateCommands.FAILED_EVALUATION + EvaluateCommandsTest.SPACE + EvaluateCommands.WAIT_OPTION +
						  EvaluateCommandsTest.SPACE + EvaluateCommands.PARAMETER_MUST_BE_GREATER_THAN_0;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
		finalCommand = EvaluateCommandsTest.buildEvaluateMachineCorrelationCommand(1, 10, true);
		testCommandContext.loadPGN(TestContext.SINGLE_PGN);
		actualOutput = testCommandContext.executeValidCommand(finalCommand);
		predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
						  EvaluateCommandsTest.SPACE +
						  testCommandContext.getChessIO().getGames().size() +
						  EvaluateCommandsTest.SPACE +
						  CommandContext.TAGS_INSERTED;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);

	}

	/**
	 * Tests evaluating machine correlation
	 */
	@SuppressWarnings({"JUnitTestMethodWithNoAssertions"})
	@Test
	public void evaluateMachineCorrelationTest()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.SINGLE_PGN);
		final String finalCommand = EvaluateCommandsTest.buildEvaluateMachineCorrelationCommand();
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS +
									   EvaluateCommandsTest.SPACE +
									   testCommandContext.getChessIO().getGames().size() +
									   EvaluateCommandsTest.SPACE +
									   CommandContext.TAGS_INSERTED;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests inserting a result when none of the currently loaded games have calculable results
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertResultIncalculable()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.INCALCULABLE_HEADLESS_PGN);
		final String finalCommand = EvaluateCommandsTest.buildEvaluateResultCommand();
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final String predictedOutput = CommandContext.FAILED_TO_INSERT_TAGS +
									   EvaluateCommandsTest.SPACE +
									   EvaluateCommands.UNABLE_TO_ASCERTAIN_ANY_RESULTS;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Realistic test of calculating a set of mixed results
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertResultMixedHeadless()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.INCALCULABLE_HEADLESS_PGN);
		testCommandContext.loadPGN(TestContext.DRAW_HEADLESS_PGN);
		testCommandContext.loadPGN(TestContext.BLACK_WIN_MATE_HEADLESS_PGN);
		testCommandContext.loadPGN(TestContext.WHITE_WIN_MATE_HEADLESS_PGN);
		testCommandContext.loadPGN(TestContext.INCALCULABLE_HEADLESS_PGN);
		final String finalCommand = EvaluateCommandsTest.buildEvaluateResultCommand();
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final String predictedOutput = CommandContext.SUCCESSFULLY_INSERTED_TAGS + EvaluateCommandsTest.SPACE + 3 + EvaluateCommandsTest.SPACE +
									   CommandContext.TAGS_INSERTED;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

	/**
	 * Tests inserting results when no games have been imported
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertResultNoImportedGames()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		final String finalCommand = EvaluateCommandsTest.buildEvaluateResultCommand();
		testCommandContext.assertCommandFails(finalCommand);
	}

	/**
	 * Tests inserting a result when there are no results to insert
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void insertResultNoneMissing()
	{
		final TestCommandContext testCommandContext = new TestCommandContext();
		testCommandContext.loadPGN(TestContext.MULTI_PGN);
		final String finalCommand = EvaluateCommandsTest.buildEvaluateResultCommand();
		final String actualOutput = testCommandContext.executeValidCommand(finalCommand);
		final String predictedOutput = CommandContext.FAILED_TO_INSERT_TAGS +
									   EvaluateCommandsTest.SPACE +
									   EvaluateCommands.ALL_GAMES_COMPLETED_RESULT_TAG;
		TestCommandContext.assertOutputMatchesPredicted(actualOutput, predictedOutput);
	}

}
