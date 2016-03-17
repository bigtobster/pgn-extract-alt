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
import com.bigtobster.pgnextractalt.filters.DuplicateFilter;
import com.bigtobster.pgnextractalt.filters.DuplicateFilterMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

/**
 * Spring shell command class for filtering duplicate chess games. Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@Component
public class DuplicateFilterCommands implements CommandMarker
{
	private static final String DUPLICATE_FILTER_COMMAND_HELP = "Filters out the duplicates of the list of games leaving a set of unique games" +
																"Available on successful import.";
	private static final String DUPLICATE_FILTER_SUBCOMMAND   = "duplicates";
	private static final char   HYPHEN                        = '-';
	private static final String
								ISOLATE_DUPLICATES_COMMAND_HELP
															  = "Removes all games which are not duplicated. Leaves a list of duplicated games. Available on successful import.";
	private static final String ISOLATE_SUBCOMMAND            = "isolate";
	private static final String
								PURGE_DUPLICATES_COMMAND_HELP
															  = "Removes all duplicate games. Leaves a set of games with none duplicated. Available on successful import.";
	private static final String PURGE_SUBCOMMAND              = "purge";
	private static final String SPACE                         = " ";
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private IOCommands ioCommands;

	/**
	 * Getter for filter duplicate Command String
	 *
	 * @return String filter duplicate Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	static String getFilterDuplicatesCommand()
	{
		return CommandContext.FILTER_SUBCOMMAND + DuplicateFilterCommands.HYPHEN + DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND;
	}

	/**
	 * Getter for isolate duplicates Command String
	 *
	 * @return String filter duplicate Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	static String getIsolateDuplicatesCommand()
	{
		return DuplicateFilterCommands.ISOLATE_SUBCOMMAND + DuplicateFilterCommands.HYPHEN + DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND;
	}

	/**
	 * Getter for purge duplicates Command String
	 *
	 * @return String Purge duplicates Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	static String getPurgeDuplicatesCommand()
	{
		return DuplicateFilterCommands.PURGE_SUBCOMMAND + DuplicateFilterCommands.HYPHEN + DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND;
	}

	/**
	 * Filter duplicate games from currently loaded games
	 *
	 * @return Successful/failure message
	 */
	@CliCommand(value = CommandContext.FILTER_SUBCOMMAND + DuplicateFilterCommands.HYPHEN + DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND,
				help = DuplicateFilterCommands.DUPLICATE_FILTER_COMMAND_HELP)
	public String filterDuplicateGames()
	{
		final int removedGames = this.runDuplicateFilter(DuplicateFilterMode.FILTER);
		return removedGames + DuplicateFilterCommands.SPACE + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
	}

	/**
	 * Describes when filter duplicate command is available
	 *
	 * @return boolean Available on import
	 */
	@CliAvailabilityIndicator(CommandContext.FILTER_SUBCOMMAND +
							  DuplicateFilterCommands.HYPHEN +
							  DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND)
	public boolean isFilterDuplicatesAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Describes when isolate duplicates command is available
	 *
	 * @return boolean Available on import
	 */
	@CliAvailabilityIndicator(DuplicateFilterCommands.ISOLATE_SUBCOMMAND +
							  DuplicateFilterCommands.HYPHEN +
							  DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND)
	public boolean isIsolateDuplicatesAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Describes when purge duplicates command is available
	 *
	 * @return boolean Available on import
	 */
	@CliAvailabilityIndicator(DuplicateFilterCommands.PURGE_SUBCOMMAND +
							  DuplicateFilterCommands.HYPHEN +
							  DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND)
	public boolean isPurgeDuplicatesAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Isolate duplicate games from currently loaded games
	 *
	 * @return Successful/failure message
	 */
	@CliCommand(value = DuplicateFilterCommands.ISOLATE_SUBCOMMAND +
						DuplicateFilterCommands.HYPHEN +
						DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND,
				help = DuplicateFilterCommands.ISOLATE_DUPLICATES_COMMAND_HELP)
	public String isolateDuplicateGames()
	{
		final int removedGames = this.runDuplicateFilter(DuplicateFilterMode.ISOLATE);
		return removedGames + DuplicateFilterCommands.SPACE + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
	}

	/**
	 * Purge duplicate games from currently loaded games
	 *
	 * @return Successful/failure message
	 */
	@CliCommand(value = DuplicateFilterCommands.PURGE_SUBCOMMAND + DuplicateFilterCommands.HYPHEN + DuplicateFilterCommands
			.DUPLICATE_FILTER_SUBCOMMAND,
				help = DuplicateFilterCommands.PURGE_DUPLICATES_COMMAND_HELP)
	public String purgeDuplicateGames()
	{
		final int removedGames = this.runDuplicateFilter(DuplicateFilterMode.PURGE);
		return removedGames + DuplicateFilterCommands.SPACE + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter", "DuplicateStringLiteralInspection"})
	@Override
	public String toString()
	{
		return "DuplicateFilterCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}

	private int runDuplicateFilter(final DuplicateFilterMode mode)
	{
		final DuplicateFilter duplicateFilter = new DuplicateFilter();
		duplicateFilter.setMode(mode);
		final ChessFilterer chessFilterer = this.commandContext.getChessFilterer();
		chessFilterer.loadFilter(duplicateFilter);
		return chessFilterer.run();

	}
}
