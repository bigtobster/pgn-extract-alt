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

import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.chess.ChessEvaluator;
import com.bigtobster.pgnextractalt.chess.ChessPresso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Spring Shell Command class for Evaluating properties of chess games Created by Toby Leheup on 15/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@Component
public class EvaluateCommands implements CommandMarker
{
	/**
	 * Message when all games have a completed results tag
	 */
	static final         String ALL_GAMES_COMPLETED_RESULT_TAG            = "All loaded games have a completed Result tag.";
	/**
	 * The depth option argument key in machine correlation evaluation command
	 */
	static final         String DEPTH_OPTION                              = "Depth";
	/**
	 * Substring for output on any failed evaluation
	 */
	static final         String FAILED_EVALUATION                         = "Failed to evaluate games.";
	/**
	 * The force option argument key in machine correlation evaluation command
	 */
	@SuppressWarnings("DuplicateStringLiteralInspection")
	static final         String FORCE_OPTION                              = "Force";
	/**
	 * Failure message substring on entering a parameter value less than or equal to 0 when not permitted
	 */
	static final         String PARAMETER_MUST_BE_GREATER_THAN_0          = "parameter value must be greater than 0";
	/**
	 * Message when results cannot be ascertained
	 */
	static final         String UNABLE_TO_ASCERTAIN_ANY_RESULTS           = "Unable to ascertain any results.";
	/**
	 * The wait option argument key in machine correlation evaluation command
	 */
	static final         String WAIT_OPTION                               = "Wait";
	@SuppressWarnings("ConstantNamingConvention")
	private static final String EVALUATE_MACHINE_CORRELATION_COMMAND      = "evaluate-machine-correlation";
	@SuppressWarnings("ConstantNamingConvention")
	private static final String EVALUATE_MACHINE_CORRELATION_COMMAND_HELP = "Will attempt to calculate a degree of calculation between " +
																			"chess engine and both (presumed) human players. The closer the " +
																			"correlation is to 0, the higher the degree of correlation between the " +
																			"human player and the engine. The correlation value is inserted into " +
																			"the" +
																			" \"Black\" or \"White\" tag. Available when games imported";
	private static final String EVALUATE_RESULT_COMMAND                   = "evaluate-result";
	private static final String EVALUATE_RESULT_COMMAND_HELP              = "For all games without a result tag, " +
																			"will attempt to evaluate the result and add it as a tag. Available when" +
																			" games imported.";
	private static final String SPACE                                     = " ";
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessEvaluator chessEvaluator;
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	/**
	 * Getter for Machine Correlation Command String
	 *
	 * @return String Machine Correlation Command String
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant", "UnusedDeclaration"})
	public static String getEvaluateMachineCorrelationCommand()
	{
		return EvaluateCommands.EVALUATE_MACHINE_CORRELATION_COMMAND;
	}

	/**
	 * Getter for List Writable Tags Command String
	 *
	 * @return String List Writable Tags Command String
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getEvaluateResultCommand()
	{
		return EvaluateCommands.EVALUATE_RESULT_COMMAND;
	}

	/**
	 * Handle the interface for evaluating the machine correlation of a game and inserting the evaluated correlation value into the tag for the
	 * evaluated person for that game
	 *
	 * @param depth The minimum depth that the engine must search to before returning a best move
	 * @param wait  The period that PGN-Extract-Alt will sleep for in ms before polling the UCI engine
	 * @param force Whether to overwrite existing machine correlation scores
	 * @return Success message
	 * @throws java.io.IOException Thrown on unknown engine failure
	 */
	@SuppressWarnings({"ProhibitedExceptionDeclared", "FeatureEnvy", "BooleanParameter"})
	@CliCommand(value = EvaluateCommands.EVALUATE_MACHINE_CORRELATION_COMMAND, help = EvaluateCommands.EVALUATE_MACHINE_CORRELATION_COMMAND_HELP)
	public String evaluateMachineCorrelation(
			@CliOption(key = {EvaluateCommands.DEPTH_OPTION}, help = "The minimum depth the engine will search for the best move (default of 10)",
					   mandatory = false, unspecifiedDefaultValue = "13") final int depth,
			@CliOption(key = {EvaluateCommands.WAIT_OPTION}, help = "The time period in ms the engine will wait before checking if best move found." +
																	" Increase value for slower hardware to improve performance.",
					   mandatory = false, unspecifiedDefaultValue = "20") final int wait,
			@SuppressWarnings("DuplicateStringLiteralInspection") @CliOption(key = {EvaluateCommands.FORCE_OPTION},
																			 help = "Overwrite existing MachineCorrelation scores (default of false)",
																			 mandatory = false,
																			 unspecifiedDefaultValue = "false") final boolean force
											) throws Exception
	{
		int tagsInsertedNo = 0;
		String failureDetails = null;
		if(wait <= 0)
		{
			failureDetails = EvaluateCommands.WAIT_OPTION + EvaluateCommands.SPACE + EvaluateCommands.PARAMETER_MUST_BE_GREATER_THAN_0;
		}
		if(depth <= 0)
		{
			failureDetails = EvaluateCommands.DEPTH_OPTION + EvaluateCommands.SPACE + EvaluateCommands.PARAMETER_MUST_BE_GREATER_THAN_0;
		}
		if(failureDetails == null)
		{
			try
			{
				tagsInsertedNo = ChessEvaluator.evaluateMachineCorrelation(this.commandContext.getChessIO().getGames(), depth, wait, force);
			}
			catch(final IOException ioe)
			{
				failureDetails = CommandContext.UNKNOWN_IMPORT_ERROR + OsUtils.LINE_SEPARATOR + CommandContext.NOTIFY_DEV;
				CommandContext.handleAndThrowSevereError(ioe, failureDetails);
			}
			catch(@SuppressWarnings("LocalVariableNamingConvention") final OperationNotSupportedException operationNotSupportedException)
			{
				failureDetails = operationNotSupportedException.getMessage();
			}
			catch(final URISyntaxException use)
			{
				failureDetails = CommandContext.UNKNOWN_IMPORT_ERROR + OsUtils.LINE_SEPARATOR + CommandContext.NOTIFY_DEV;
				CommandContext.handleAndThrowSevereError(use, failureDetails);
			}
		}
		if(failureDetails == null)
		{
			return CommandContext.SUCCESSFULLY_INSERTED_TAGS +
				   EvaluateCommands.SPACE +
				   tagsInsertedNo +
				   EvaluateCommands.SPACE +
				   CommandContext.TAGS_INSERTED;
		}
		return EvaluateCommands.FAILED_EVALUATION + EvaluateCommands.SPACE + failureDetails;
	}

	/**
	 * Handle the interface for evaluating the result of a game and inserting the evaluated value into the tag for that game
	 *
	 * @return Success message
	 */
	@CliCommand(value = EvaluateCommands.EVALUATE_RESULT_COMMAND, help = EvaluateCommands.EVALUATE_RESULT_COMMAND_HELP)
	public String evaluateResult()
	{
		final int tagsInsertedNo = ChessPresso.evaluateGameResults(this.commandContext.getChessIO().getGames());
		boolean allResultsEvaluated = true;
		for(final Game game : this.commandContext.getChessIO().getGames())
		{
			if((game.getResultStr() == null) || game.getResultStr().isEmpty())
			{
				allResultsEvaluated = false;
				//noinspection BreakStatement
				break;
			}
		}
		if(allResultsEvaluated)
		{
			return CommandContext.FAILED_TO_INSERT_TAGS + EvaluateCommands.SPACE + EvaluateCommands.ALL_GAMES_COMPLETED_RESULT_TAG;
		}
		if(tagsInsertedNo == 0)
		{
			return CommandContext.FAILED_TO_INSERT_TAGS + EvaluateCommands.SPACE + EvaluateCommands.UNABLE_TO_ASCERTAIN_ANY_RESULTS;
		}
		return CommandContext.SUCCESSFULLY_INSERTED_TAGS +
			   EvaluateCommands.SPACE +
			   tagsInsertedNo +
			   EvaluateCommands.SPACE +
			   CommandContext.TAGS_INSERTED;
	}

	/**
	 * Describes when "Evaluate Machine Correlation" command is available
	 *
	 * @return boolean Availability (Available on successful import of at least 1 game)
	 */
	@SuppressWarnings("InstanceMethodNamingConvention")
	@CliAvailabilityIndicator({EvaluateCommands.EVALUATE_MACHINE_CORRELATION_COMMAND})
	public boolean isEvaluateMachineCorrelationAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Describes when "Evaluate Results" command is available
	 *
	 * @return boolean Availability (Available on successful import of at least 1 game)
	 */
	@CliAvailabilityIndicator({EvaluateCommands.EVALUATE_RESULT_COMMAND})
	public boolean isEvaluateResultsAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "EvaluateCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}
}
