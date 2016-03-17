/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.filters;

import chesspresso.game.Game;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Filter that removes duplicate games Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class DuplicateFilter implements Filter
{
	private static final String              FILTER_NO_MODE_ERROR = "Attempting to filter without a mode!";
	private              DuplicateFilterMode mode                 = null;

	private static ArrayList<Game> filterDuplicates(final ArrayList<Game> games)
	{
		final HashMap<Integer, Game> gamesMap = new HashMap<Integer, Game>(games.size());
		for(final Game game : games)
		{
			gamesMap.put(game.hashCode(), game);
		}
		return new ArrayList<Game>(gamesMap.values());
	}

	private static ArrayList<Game> isolateDuplicates(final ArrayList<Game> games)
	{
		final HashMap<Integer, Game> gamesMap = new HashMap<Integer, Game>(games.size());
		final ArrayList<Game> duplicatedGames = new ArrayList<Game>(games.size());
		final HashMap<Integer, Game> duplicatedGamesMap = new HashMap<Integer, Game>(games.size());
		for(final Game game : games)
		{
			if(gamesMap.containsKey(game.hashCode()))
			{
				duplicatedGames.add(game);
				duplicatedGamesMap.put(game.hashCode(), game);
			}
			else
			{
				gamesMap.put(game.hashCode(), game);
			}
		}
		duplicatedGames.addAll(duplicatedGamesMap.values());
		return duplicatedGames;
	}

	private static ArrayList<Game> purgeDuplicates(final ArrayList<Game> games)
	{
		final HashMap<Integer, Game> gamesMap = new HashMap<Integer, Game>(games.size());
		final HashMap<Integer, Game> duplicatedGamesMap = new HashMap<Integer, Game>(games.size());
		for(final Game game : games)
		{
			if(gamesMap.containsKey(game.hashCode()))
			{
				duplicatedGamesMap.put(game.hashCode(), game);
			}
			else
			{
				gamesMap.put(game.hashCode(), game);
			}
		}
		games.removeAll(duplicatedGamesMap.values());
		return games;
	}

	/**
	 * Filter out duplicate games
	 *
	 * @param games The list of games to be filtered
	 */
	@Override
	public ArrayList<Game> filter(final ArrayList<Game> games)
	{
		if(this.mode.equals(DuplicateFilterMode.FILTER))
		{
			return DuplicateFilter.filterDuplicates(games);
		}
		if(this.mode.equals(DuplicateFilterMode.ISOLATE))
		{
			return DuplicateFilter.isolateDuplicates(games);
		}
		if(this.mode.equals(DuplicateFilterMode.PURGE))
		{
			return DuplicateFilter.purgeDuplicates(games);
		}
		throw new InvalidParameterException(DuplicateFilter.FILTER_NO_MODE_ERROR);
	}

	/**
	 * Sets the operation mode of the filter (Filter, Isolate or Purge)
	 *
	 * @param newMode The mode that the filter should operate in
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	public void setMode(final DuplicateFilterMode newMode)
	{
		this.mode = newMode;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "DuplicateFilter{" +
			   "mode=" + this.mode +
			   '}';
	}
}
