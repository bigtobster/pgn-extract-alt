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

import chesspresso.Chess;
import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.core.Filter;

import java.util.ArrayList;

/**
 * Filter that filters games by their results Created by Toby Leheup on 04/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ResultFilter implements Filter
{
	private boolean isBlackWinFiltered   = false;
	private boolean isDrawFiltered       = false;
	private boolean isUnresolvedFiltered = false;
	private boolean isWhiteWinFiltered   = false;

	@Override
	public void filter(final ArrayList<Game> games)
	{
		final ArrayList<Game> newGames = new ArrayList<Game>(games.size());
		for(final Game game : games)
		{
			final int res = game.getResult();
			if(! this.isFiltered(res))
			{
				newGames.add(game);
			}
		}
		games.clear();
		games.addAll(newGames);
	}

	/**
	 * Sets whether Black wins should be retained in the output
	 *
	 * @param preserveBlackWin Whether to retain black wins
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	public void setBlackWinFiltered(final boolean preserveBlackWin)
	{
		this.isBlackWinFiltered = preserveBlackWin;
	}

	/**
	 * Sets whether Draws should be retained in the output
	 *
	 * @param preserveDraw Whether to retain draws
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	public void setDrawFiltered(final boolean preserveDraw)
	{
		this.isDrawFiltered = preserveDraw;
	}

	/**
	 * Sets whether unresolved results should be retained in the output
	 *
	 * @param preserveUnresolved Whether to retain unresolved results
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	public void setUnresolvedFiltered(final boolean preserveUnresolved)
	{
		this.isUnresolvedFiltered = preserveUnresolved;
	}

	/**
	 * Sets whether White wins should be retained in the output
	 *
	 * @param preserveWhiteWin Whether to retain white wins
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	public void setWhiteWinFiltered(final boolean preserveWhiteWin)
	{
		this.isWhiteWinFiltered = preserveWhiteWin;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ResultFilter{" +
			   "isWhiteWinFiltered=" + this.isWhiteWinFiltered +
			   ", isBlackWinFiltered=" + this.isBlackWinFiltered +
			   ", isDrawFiltered=" + this.isDrawFiltered +
			   ", isUnresolvedFiltered=" + this.isUnresolvedFiltered +
			   '}';
	}

	private boolean isBlackWinGameFiltered(final int res)
	{
		return (res == Chess.RES_BLACK_WINS) && this.isBlackWinFiltered;
	}

	private boolean isDrawnGameFiltered(final int res)
	{
		return (res == Chess.RES_DRAW) && this.isDrawFiltered;
	}

	private boolean isFiltered(final int res)
	{
		return this.isWhiteWinGameFiltered(res) ||
			   this.isBlackWinGameFiltered(res) ||
			   this.isDrawnGameFiltered(res) ||
			   this.isUnresolvedGameFiltered(res);
	}

	private boolean isUnresolvedGameFiltered(final int res)
	{
		return ((res == Chess.RES_NOT_FINISHED) || (res == Chess.NO_RES)) && this.isUnresolvedFiltered;
	}

	private boolean isWhiteWinGameFiltered(final int res)
	{
		return (res == Chess.RES_WHITE_WINS) && this.isWhiteWinFiltered;
	}
}
