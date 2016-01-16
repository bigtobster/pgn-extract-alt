package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Chess Context for PGN-Extract-Alt Essentially an object containing useful runtime chess stuff Should only be accessible to Class in chess package
 * Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
class ChessContext
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger          LOGGER = Logger.getLogger(ChessContext.class.getName());
	private final        ArrayList<Game> games  = new ArrayList<Game>(10);

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ChessContext{" +
			   ", games=" + this.games +
			   '}';
	}

	/**
	 * Setter for the list of parsed games
	 *
	 * @param newGames New list of parsed games
	 */
	void addGames(final ArrayList<Game> newGames)
	{
		this.games.addAll(newGames);
	}

	/**
	 * Getter for the list of parsed games
	 *
	 * @return ArrayList&lt;Game&gt; List of parsed games
	 */
	ArrayList<Game> getGames()
	{
		return this.games;
	}

	/**
	 * Returns if PGN-Extract-Alt has successfully imported a PGN file
	 *
	 * @return boolean True if imported successfully else false
	 */
	boolean isPGNImported()
	{
		return ! this.games.isEmpty();
	}

	/**
	 * Resets PGN-Extract-Alt Context
	 */
	void reset()
	{
		this.games.clear();
	}
}
