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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Handles the modification of tags of chess games Created by Toby Leheup on 15/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("PublicMethodNotExposedInInterface")
public class ChessTagModder
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER         = Logger.getLogger(ChessTagModder.class.getName());

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext chessContext;

		/**
	 * Returns an array of all the tags that can be written to a game
	 *
	 * @return The array of tags that can be written to a game
	 */
	public String[] getWritableTags()
	{
		return this.chessContext.getTagKeys();
	}

	/**
	 * Inserted tags with a given key from Chess games
	 *
	 * @param tagKey   The key of the tag to be inserted
	 * @param tagValue The value of the tag to be inserted
	 * @param tagForce Whether to overwrite existing tags with key of tagKey
	 * @return The total number of games that were inserted
	 */
	@SuppressWarnings({"PublicMethodNotExposedInInterface", "BooleanParameter"})
	public int insertTag(final String tagKey, final String tagValue, final boolean tagForce)
	{
		int counter = 0;
		final ArrayList<Game> games = this.chessContext.getGames();
		for(final Game game : games)
		{
			if(! (! tagForce && (game.getTag(tagKey) != null)))
			{
				game.setTag(tagKey, tagValue);
				counter++;
			}
		}
		return counter;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ChessTagModder{" +
			   "chessContext=" + this.chessContext +
			   '}';
	}
}
