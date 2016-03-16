/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;

import javax.naming.OperationNotSupportedException;
import java.io.File;
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
	 * Key for Black tag
	 */
	static final         String          BLACK_KEY                = "Black";
	/**
	 * String for a Black Win
	 */
	static final         String          BLACK_WIN_RESULT         = "0-1";
	/**
	 * String for a Draw
	 */
	static final         String          DRAW_RESULT              = "1/2-1/2";
	/**
	 * Detection string for Linux operating systems
	 */
	@SuppressWarnings("DuplicateStringLiteralInspection")
	static final         String          OS_LINUX                 = "linux";
	/**
	 * Detection string for Apple Mac operating systems
	 */
	static final         String          OS_MAC                   = "mac";
	/**
	 * Detection string for Microsoft Windows operating systems
	 */
	@SuppressWarnings("DuplicateStringLiteralInspection")
	static final         String          OS_WINDOWS               = "windows";
	/**
	 * Key for the Result Tag
	 */
	static final         String          RESULT_KEY               = "Result";
	/**
	 * The string that the path to the engine should contain for Linux OSs
	 */
	@SuppressWarnings("DuplicateStringLiteralInspection")
	static final         String          STOCKFISH_LINUX_SUBSTR   = "linux";
	/**
	 * The string that the path to the engine should contain for Apple Mac OSs
	 */
	static final         String          STOCKFISH_MAC_SUBSTR     = "mac";
	/**
	 * The incomplete os-independent pathname to the stockfish executable
	 */
	@SuppressWarnings("DuplicateStringLiteralInspection")
	static final         String          STOCKFISH_PATH           = "stockfish" + File.separator + "stockfish-7-x64-";
	/**
	 * The string that the path to the engine should contain for Microsoft Windows OSs
	 */
	static final         String          STOCKFISH_WINDOWS_SUBSTR = "win.exe";
	/**
	 * Key for white tag
	 */
	static final         String          WHITE_KEY                = "White";
	/**
	 * String for a White Win
	 */
	static final         String          WHITE_WIN_RESULT         = "1-0";
	private static final String          ARCH_X64                 = "64";
	private static final String          BLACK_ELO_KEY            = "BlackElo";
	private static final String          DATE_KEY                 = "Date";
	private static final String          ECO_KEY                  = "ECO";
	private static final String          EVENT_DATE_KEY           = "EventDate";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String          EVENT_KEY                = "Event";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger          LOGGER                   = Logger.getLogger(ChessContext.class.getName());
	private static final int             NO_OF_EDITABLE_TAGS      = 11;
	private static final String          ROUND_KEY                = "Round";
	private static final String          SITE_KEY                 = "Site";
	private static final String          WHITE_ELO_KEY            = "WhiteElo";
	private final        ArrayList<Game> games                    = new ArrayList<Game>(10);
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

	/**
	 * Fully resolves the URI to the Stockfish chess engine
	 *
	 * @return The full path to the Stockfish chess engine
	 * @throws OperationNotSupportedException Thrown on the operating system not being supported
	 */
	static String resolveStockfishPath() throws OperationNotSupportedException
	{
		final String operatingSystem = System.getProperty("os.name").toLowerCase();
		final String arch = System.getProperty("os.arch").toLowerCase();
		String fullStockfishPath = ChessContext.STOCKFISH_PATH;
		if(! arch.contains(ChessContext.ARCH_X64))
		{
			//noinspection DuplicateStringLiteralInspection
			throw new OperationNotSupportedException(arch + " Architecture not supported");
		}
		if(operatingSystem.contains(ChessContext.OS_LINUX))
		{
			fullStockfishPath += ChessContext.STOCKFISH_LINUX_SUBSTR;
		}
		else if(operatingSystem.contains(ChessContext.OS_WINDOWS))
		{
			fullStockfishPath += ChessContext.STOCKFISH_WINDOWS_SUBSTR;
		}
		else if(operatingSystem.contains(ChessContext.OS_MAC))
		{
			fullStockfishPath += ChessContext.STOCKFISH_MAC_SUBSTR;
		}
		else
		{
			//noinspection DuplicateStringLiteralInspection
			throw new OperationNotSupportedException("Operating system not supported: " + operatingSystem);
		}
		return fullStockfishPath;
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
