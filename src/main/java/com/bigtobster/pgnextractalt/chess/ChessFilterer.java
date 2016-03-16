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
import com.bigtobster.pgnextractalt.core.Filter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * Handles filtering of games in PGN-Extract-Alt
 * All the filtering rules are defined by a class in the filter package
 * Created by Toby Leheup on 04/02/16 for pgn-extract-alt.
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("PublicMethodNotExposedInInterface")
public class ChessFilterer
{
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext chessContext;
	private Filter filter = null;

	/**
	 * Loads a new filter into the filterer
	 * @param newFilter The new filter to be used by the filterer
	 */
	public void loadFilter(final Filter newFilter)
	{
		if(newFilter != null)
		{
			this.filter = newFilter;
		}
	}

	/**
	 * Executes the currently loaded filter
	 * @return The number of games filtered
	 */
	@SuppressWarnings("InstanceMethodNamingConvention")
	public int run()
	{
		if (this.filter == null)
		{
			throw new MissingResourceException("Missing filter", Filter.class.toString(), Filter.class.getSimpleName());
		}
		final ArrayList<Game> games = this.chessContext.getGames();
		final int preGameCount = games.size();
		this.chessContext.setGames(this.filter.filter(games));
		final int postGameCount = games.size();
		return preGameCount - postGameCount;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ChessFilterer{" +
			   "chessContext=" + this.chessContext +
			   ", filter=" + this.filter +
			   '}';
	}
}
