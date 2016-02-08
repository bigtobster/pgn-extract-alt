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
import com.bigtobster.pgnextractalt.chess.ChessFilterer;
import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.filters.DuplicateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

/**
 * Spring shell command class for filtering duplicate chess games Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@Component
public class DuplicateFilterCommands implements CommandMarker
{
	/**
	 * Filename for duplicate games file
	 */
	static final         String DUPLICATE_OUT_FILENAME           = "duplicated_games.pgn";
	/**
	 * Message on failure to export the duplicate games PGN
	 */
	static final         String ERROR_ON_EXPORTING_DUPLICATE_OUT = "Error on exporting duplicate games: ";
	private static final String DUPLICATE_FILTER_COMMAND_HELP    = "Filter duplicate games from imported games and dumps them to \"duplicates.pgn\"" +
																   ". " +
																   "Available on successful import";
	private static final String DUPLICATE_FILTER_SUBCOMMAND      = "duplicate";
	private static final String SPACE                            = " ";
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
	static String getDuplicateFilterCommand()
	{
		return DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND + CommandContext.FILTER_SUBCOMMAND;
	}

	/**
	 * Filter duplicate games from currently loaded games
	 *
	 * @return Successful/failure message
	 */
	@SuppressWarnings("FeatureEnvy")
	@CliCommand(value = DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND + CommandContext.FILTER_SUBCOMMAND,
				help = DuplicateFilterCommands.DUPLICATE_FILTER_COMMAND_HELP)
	public String filterDuplicateGames()
	{
		final DuplicateFilter duplicateFilter = new DuplicateFilter();
		final ChessIO chessIO = this.commandContext.getChessIO();
		final ArrayList<Game> cachedGames = chessIO.getGames();
		final ChessFilterer chessFilterer = this.commandContext.getChessFilterer();
		final IOCommands ioCommander = this.ioCommands;
		chessFilterer.loadFilter(duplicateFilter);
		final int removedGames = chessFilterer.run();

		chessIO.setGames(duplicateFilter.getDuplicateGames());
		String exportMessage = ioCommander.exportPGN(new File(DuplicateFilterCommands.DUPLICATE_OUT_FILENAME).getAbsoluteFile());
		if(exportMessage.contains(IOCommands.FAILED_EXPORT))
		{
			exportMessage = DuplicateFilterCommands.ERROR_ON_EXPORTING_DUPLICATE_OUT + exportMessage;
			cachedGames.addAll(duplicateFilter.getDuplicateGames());
		}
		else
		{
			exportMessage = removedGames + DuplicateFilterCommands.SPACE + CommandContext.SUCCESSFULLY_FILTERED_GAMES;
		}
		chessIO.setGames(cachedGames);
		return exportMessage;
	}

	/**
	 * Describes when filter duplicate command is available
	 *
	 * @return boolean Available on import
	 */
	@CliAvailabilityIndicator(DuplicateFilterCommands.DUPLICATE_FILTER_SUBCOMMAND + CommandContext.FILTER_SUBCOMMAND)
	public boolean isFilterDuplicateAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter", "DuplicateStringLiteralInspection"})
	@Override
	public String toString()
	{
		return "DuplicateFilterCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}
}
