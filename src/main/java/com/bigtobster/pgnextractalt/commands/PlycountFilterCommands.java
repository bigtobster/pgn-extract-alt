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
import com.bigtobster.pgnextractalt.filters.PlycountFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Spring shell command class for filtering chess games by their plycount Created by Toby Leheup on 07/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("WeakerAccess")
@Component
public class PlycountFilterCommands implements CommandMarker
{
	/**
	 * Option for filtering games with a plycount greater than X
	 */
	static final         String GREATER_THAN_OPTION             = "GreaterThan";
	/**
	 * Option for filtering games with a plycount less than X
	 */
	static final         String LESS_THAN_OPTION                = "LessThan";
	private static final String PLYCOUNT_FILTER_SUBCOMMAND      = "plycount";
	private static final String PLYCOUNT_FILTER_SUBCOMMAND_HELP = "Filter imported games by their plycount. Available on successful import.";
	private static final String SPACE                           = " ";

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	/**
	 * Getter for Filter-By-Plycount Command String
	 *
	 * @return String Filter-By-Plycount Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	static String getFilterByPlycountCommand()
	{
		return CommandContext.FILTER_SUBCOMMAND + PlycountFilterCommands.PLYCOUNT_FILTER_SUBCOMMAND;
	}

	/**
	 * Filter the currently loaded games by plycount
	 *
	 * @param greaterThan Optional parameter. The plycount which, if the game is greater, will cause that game to be removed.
	 * @param lessThan    The plycount which, if the game is smaller, will cause that game to be removed.
	 * @return Successful/failure message
	 */
	@CliCommand(value = CommandContext.FILTER_SUBCOMMAND + PlycountFilterCommands.PLYCOUNT_FILTER_SUBCOMMAND,
				help = PlycountFilterCommands.PLYCOUNT_FILTER_SUBCOMMAND_HELP)
	public String filterByPlycount(
			@CliOption(key = {PlycountFilterCommands.GREATER_THAN_OPTION},
					   help = "Filter games with a plycount greater than X",
					   mandatory = true) final int greaterThan,
			@CliOption(key = {PlycountFilterCommands.LESS_THAN_OPTION},
					   help = "Filter games with a plycount less than X",
					   mandatory = true) final int lessThan
								  )
	{
		final PlycountFilter plycountFilter = new PlycountFilter();
		plycountFilter.setGreaterThan(greaterThan);
		plycountFilter.setLessThan(lessThan);

		final ChessFilterer chessFilterer = this.commandContext.getChessFilterer();
		chessFilterer.loadFilter(plycountFilter);
		final int removedGames = chessFilterer.run();
		return removedGames + PlycountFilterCommands.SPACE + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
	}

	/**
	 * Describes when "filter-by-plycount" command is available
	 *
	 * @return boolean Available on import
	 */
	@CliAvailabilityIndicator(CommandContext.FILTER_SUBCOMMAND + PlycountFilterCommands.PLYCOUNT_FILTER_SUBCOMMAND)
	public boolean isFilterByPlycountAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "PlycountFilterCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}
}
