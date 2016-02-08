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

import com.bigtobster.pgnextractalt.chess.ChessFilterer;
import com.bigtobster.pgnextractalt.filters.ResultFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Spring shell command class for filtering chess games by their result
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("UnusedDeclaration")
@Component
public class ResultFilterCommands implements CommandMarker
{
	/**
	 * The command option string to filter black wins
	 */
	static final         String FILTER_BLACK_WINS_OPTION  = "FilterBlackWins";
	/**
	 * The command option string to filter draws
	 */
	static final         String FILTER_DRAWS_OPTION       = "FilterDraws";
	/**
	 * The command option string to filter unresolved cases
	 */
	static final         String FILTER_UNRESOLVED_OPTION  = "FilterUnresolved";
	/**
	 * The command option string to filter white wins
	 */
	static final         String FILTER_WHITE_WINS_OPTION  = "FilterWhiteWins";
	private static final String FAILED_TO_FILTER          = "Filter failed.";
	private static final String FILTER_BY_RESULT_HELP     = "Filter imported games by their result. Available on successful import.";
	private static final String NO_GAMES_SATISFY_CRITERIA = "None of the games satisfied the removal criteria.";
	private static final String RESULT_FILTER_SUBCOMMAND  = "result";
	private static final String SPACE                     = " ";
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	/**
	 * Getter for Filter-By-Result Command String
	 *
	 * @return String Filter-By-Result Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	static String getFilterByResultCommand()
	{
		return ResultFilterCommands.RESULT_FILTER_SUBCOMMAND + CommandContext.FILTER_SUBCOMMAND;
	}

	/**
	 * Filter the currently loaded games by result value
	 *
	 * @param isWhiteWinFiltered   Whether to filter games of white wins in output
	 * @param isBlackWinFiltered   Whether to filter games of black wins in output
	 * @param isDrawFiltered       Whether to filter games of draws in output
	 * @param isUnresolvedFiltered Whether to filter games of unresolved result in output
	 * @return Successful/failure message
	 */
	@SuppressWarnings({"BooleanParameter", "FeatureEnvy", "DuplicateStringLiteralInspection"})
	@CliCommand(value = ResultFilterCommands.RESULT_FILTER_SUBCOMMAND + CommandContext.FILTER_SUBCOMMAND,
				help = ResultFilterCommands.FILTER_BY_RESULT_HELP)
	public String filterByResult(
			@CliOption(key = {ResultFilterCommands.FILTER_WHITE_WINS_OPTION}, help = "Filter white wins in output", mandatory = false,
					   unspecifiedDefaultValue = "false") final boolean isWhiteWinFiltered,
			@CliOption(key = {ResultFilterCommands.FILTER_BLACK_WINS_OPTION}, help = "Filter black wins in output", mandatory = false,
					   unspecifiedDefaultValue = "false") final boolean isBlackWinFiltered,
			@CliOption(key = {ResultFilterCommands.FILTER_DRAWS_OPTION}, help = "Filter draws in output", mandatory = false,
					   unspecifiedDefaultValue = "false") final boolean isDrawFiltered,
			@CliOption(key = {ResultFilterCommands.FILTER_UNRESOLVED_OPTION}, help = "Filter unresolved results in output", mandatory = false,
					   unspecifiedDefaultValue = "false") final boolean isUnresolvedFiltered
								)
	{
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setWhiteWinFiltered(isWhiteWinFiltered);
		resultFilter.setBlackWinFiltered(isBlackWinFiltered);
		resultFilter.setDrawFiltered(isDrawFiltered);
		resultFilter.setUnresolvedFiltered(isUnresolvedFiltered);
		final ChessFilterer chessFilterer = this.commandContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int removedGames = chessFilterer.run();
		return removedGames + ResultFilterCommands.SPACE + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
	}

	/**
	 * Describes when "filter-by-result" command is available
	 *
	 * @return boolean Available on import
	 */
	@CliAvailabilityIndicator(ResultFilterCommands.RESULT_FILTER_SUBCOMMAND + CommandContext.FILTER_SUBCOMMAND)
	public boolean isFilterByResultAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ResultFilterCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}
}
