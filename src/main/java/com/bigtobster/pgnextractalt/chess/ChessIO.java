package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;
import chesspresso.pgn.PGNReader;
import chesspresso.pgn.PGNSyntaxError;
import chesspresso.pgn.PGNWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Importing and Exporting PGN files into/out of PGN-Extract-Alt Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface"})
public final class ChessIO
{
	private static final String FAILED_TO_EXPORT_INVALID_GAME = "Failed to export an invalid game";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(ChessIO.class.getName());
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext chessContext;

	/**
	 * Writes the currently imported games to a PGN file
	 * @param printWriter The printWriter with the bundled file to write to
	 */
	public void exportPGN(final PrintWriter printWriter)
	{
		final PGNWriter pgnWriter = new PGNWriter(printWriter);
		for(final Game game : this.chessContext.getGames())
		{
			//noinspection ProhibitedExceptionCaught
			try
			{
				pgnWriter.write(game.getModel());
			}
			catch(final NullPointerException ignored)
			{
				/*
				Bit of a Chesspresso weird-ism here
				It tries super hard to import games - whether they are sane or not
				In theory, the same issue applies when modifying games - you could "corrupt" them
				Consequently, when you try to do something to, in this case export, those games,
				you end up with errors. Those errors aren't fluffy and nice - they're NPEs...
				As a rule, GIGO applies.
				This is fundamentally an issue with Chesspresso that needs fixing at the Chesspresso level
				If this isn't possible, perhaps consider having a validity scanner that validates games before export
				and does something sensible with the invalid ones
				 */
				ChessIO.LOGGER.log(Level.WARNING, ChessIO.FAILED_TO_EXPORT_INVALID_GAME);
				ChessIO.LOGGER.log(Level.WARNING, game.toString());
			}
		}
		/*
		Another Chesspresso weird-ism
		It seems that PGNWriter does not flush the the printWriter
		If you don't do the below, you have missing data on export!
		 */
		if(printWriter != null)
		{
			printWriter.flush();
			printWriter.close();
		}
	}

	/**
	 * Returns number of games in current context
	 *@return ArrayList&lt;Game&gt; List of current games
	 */
	public ArrayList<Game> getGames()
	{
		return this.chessContext.getGames();
	}

	/**
	 * Converts a PGN file into a list of Chesspresso games
	 *
	 * @param inputStream A InputStream pointing to filePath
	 * @param filePath    The path to the PGN file
	 * @throws javax.activation.UnsupportedDataTypeException Throws exception in the event that passed file is not a PGN
	 * @throws IOException                                   Filesystem issue with reading PGN file
	 * @throws PGNSyntaxError                                Syntax error with readying PGN file
	 */
	public void importPGN(final InputStream inputStream, final String filePath) throws IOException, PGNSyntaxError, UnsupportedDataTypeException
	{
		final PGNReader pgnReader;
		final ArrayList<Game> games = new ArrayList<Game>(100);
		if(! PGNReader.isPGNFile(filePath))
		{
			throw new UnsupportedDataTypeException("File at " + filePath + " is not a PGN file");
		}
		pgnReader = new PGNReader(inputStream, filePath);
		Game game = pgnReader.parseGame();
		if(game == null)
		{
			throw new PGNSyntaxError(PGNSyntaxError.ERROR, "Empty PGN file!", filePath, 0, "");
		}
		do
		{
			games.add(game);
			game = pgnReader.parseGame();
		}
		while(game != null);
		this.addGames(games);
	}

	/**
	 * Interface for Commands to call ChessContext.isPGNImported()
	 *
	 * @return boolean True if imported successfully else false
	 */
	public boolean isPGNImported()
	{
		return this.chessContext.isPGNImported();
	}

	/**
	 * Interface for Commands to reset ChessContext.isPGNImported()
	 */
	public void reset()
	{
		this.chessContext.reset();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		//noinspection DuplicateStringLiteralInspection
		return "ChessIO{" +
			   "chessContext=" + this.chessContext +
			   '}';
	}

	/**
	 * Adds a list of established games to the current list of games
	 *
	 * @param games List of games to be added to the existing list
	 */
	void addGames(final ArrayList<Game> games)
	{
		this.chessContext.addGames(games);
	}
}
