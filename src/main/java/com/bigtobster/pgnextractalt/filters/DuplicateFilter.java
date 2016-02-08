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
import com.bigtobster.pgnextractalt.core.Filter;

import java.util.ArrayList;

/**
 * Filter that removes duplicate games Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class DuplicateFilter implements Filter
{
	private ArrayList<Game> duplicateGames = new ArrayList<Game>(0);

	/**
	 * Filter out duplicate games
	 *
	 * @param games The list of games to be filtered
	 */
	@Override
	public void filter(final ArrayList<Game> games)
	{
		this.duplicateGames = new ArrayList<Game>(games.size() / 2);
		final ArrayList<Game> newGames = new ArrayList<Game>(games.size());
		for(int i = 0; i < games.size(); i++)
		{
			boolean isDuplicated = false;
			for(int j = i + 1; j < games.size(); j++)
			{
				if(games.get(i).equals(games.get(j)))
				{
					games.remove(j);
					//Removed 1 item so pop index back 1 to stay pointing at the item it's supposed to be
					//noinspection AssignmentToForLoopParameter
					j = j - 1;
					isDuplicated = true;
				}
			}
			newGames.add(games.get(i));
			if(isDuplicated)
			{
				this.duplicateGames.add(games.get(i));
			}
		}
		games.clear();
		games.addAll(newGames);
	}

	/**
	 * Gets an (un-duplicated) list of games that were identified as duplicate games in the previous filter run - or an empty list
	 *
	 * @return The list of games that were identified as duplicate games in the previous filter run - or an empty list
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	public ArrayList<Game> getDuplicateGames()
	{
		return this.duplicateGames;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "DuplicateFilter{" +
			   "duplicateGames=" + this.duplicateGames +
			   '}';
	}
}
