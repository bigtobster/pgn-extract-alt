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
	/**
	 * String for a Black Win
	 */
	static final String BLACK_WIN_RESULT = "0-1";
	/**
	 * String for a Draw
	 */
	static final String DRAW_RESULT      = "1/2-1/2";
	/**
	 * Key for the Result Tag
	 */
	static final String RESULT_KEY       = "Result";
	/**
	 * String for a White Win
	 */
	static final String WHITE_WIN_RESULT = "1-0";
	private static final String          BLACK_ELO_KEY       = "BlackElo";
	private static final String          BLACK_KEY           = "Black";
	private static final String          DATE_KEY            = "Date";
	private static final String          ECO_KEY             = "ECO";
	private static final String          EVENT_DATE_KEY      = "EventDate";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String          EVENT_KEY           = "Event";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger          LOGGER              = Logger.getLogger(ChessContext.class.getName());
	private static final int             NO_OF_EDITABLE_TAGS = 11;
	private static final String          ROUND_KEY           = "Round";
	private static final String          SITE_KEY            = "Site";
	private static final String          WHITE_ELO_KEY       = "WhiteElo";
	private static final String          WHITE_KEY           = "White";
	private final        ArrayList<Game> games               = new ArrayList<Game>(10);
	private final String[] tagKeys;

	/**
	 * Initialises the chess context
	 */
	ChessContext()
	{
		this.tagKeys = new String[ChessContext.NO_OF_EDITABLE_TAGS];
		this.tagKeys[0] = ChessContext.EVENT_KEY;
		this.tagKeys[1] = ChessContext.SITE_KEY;
		this.tagKeys[2] = ChessContext.DATE_KEY;
		this.tagKeys[3] = ChessContext.ROUND_KEY;
		this.tagKeys[4] = ChessContext.WHITE_KEY;
		this.tagKeys[5] = ChessContext.BLACK_KEY;
		this.tagKeys[6] = ChessContext.RESULT_KEY;
		this.tagKeys[7] = ChessContext.WHITE_ELO_KEY;
		this.tagKeys[8] = ChessContext.BLACK_ELO_KEY;
		this.tagKeys[9] = ChessContext.EVENT_DATE_KEY;
		this.tagKeys[10] = ChessContext.ECO_KEY;
	}

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
	 * Getter for Tag Keys
	 *
	 * @return String[] The array of string of all possible keys in tags
	 */
	String[] getTagKeys()
	{
		return this.tagKeys;
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
