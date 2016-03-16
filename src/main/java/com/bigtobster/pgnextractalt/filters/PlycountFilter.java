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
 * Filter that filters games by their plycount Created by Toby Leheup on 07/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("PublicMethodNotExposedInInterface")
public class PlycountFilter implements Filter
{
	private int greaterThanParam = 0;
	private int lessThanParam    = 0;

	/**
	 * Filters games by their plycount
	 *
	 * @param games The games to be filtered
	 */
	@Override
	public ArrayList<Game> filter(final ArrayList<Game> games)
	{
		final ArrayList<Game> newGames = new ArrayList<Game>(games.size());
		for(final Game game : games)
		{
			if(! ((game.getNumOfPlies() < this.lessThanParam) && (game.getNumOfPlies() > this.greaterThanParam)))
			{
				newGames.add(game);
			}
		}
		return newGames;
	}

	/**
	 * Sets the value for which plycounts of games must be greater than
	 *
	 * @param greaterThan The decisive value of Plycount
	 */
	public void setGreaterThan(final int greaterThan)
	{
		this.greaterThanParam = greaterThan;
	}

	/**
	 * Sets the value for which plycounts of games must be less than
	 *
	 * @param lessThan The decisive value of Plycount
	 */
	public void setLessThan(final int lessThan)
	{
		this.lessThanParam = lessThan;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "PlycountFilter{" +
			   "greaterThanParam=" + this.greaterThanParam +
			   ", lessThanParam=" + this.lessThanParam +
			   '}';
	}
}
