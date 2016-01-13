package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;
import chesspresso.pgn.PGNReader;
import chesspresso.pgn.PGNSyntaxError;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Handles Importing and Exporting PGN files into/out of PGN-Extract-Alt Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface"})
public final class ChessIO
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(ChessIO.class.getName());
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext chessContext;

	/**
	 * Returns number of games in current context
	 *
	 * @return Number of games in current context
	 */
	public int getGamesCount()
	{
		return this.chessContext.getGames().size();
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
		this.chessContext.addGames(games);
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
		return "ChessIO{" +
			   "chessContext=" + this.chessContext +
			   '}';
	}
}
