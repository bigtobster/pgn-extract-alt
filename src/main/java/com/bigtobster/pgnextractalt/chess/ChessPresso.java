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
import chesspresso.move.Move;
import chesspresso.position.Position;

import java.util.ArrayList;

/**
 * This class is for all the functionality that should be in Chesspresso but isn't In the longer term, everything in the class should be pushed into
 * Chesspresso Until Chesspresso is in such a place where this is do-able, use this class! Once Chesspresso has been properly sorted out, this class
 * should deleted Created by Toby Leheup on 04/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ChessPresso
{
	private static final int MAX_MOVE_CLOCK = 50;

	/**
	 * Inserts the correct result, if calculable, into a chess game Works on confirmed mates and draws In most cases it will not be able to work it
	 * out
	 *
	 * @param games The list of games to calculate
	 * @return Number of inserted tags
	 */
	public static int calculateGameResults(final ArrayList<Game> games)
	{
		int counter = 0;
		for(final Game game : games)
		{
			if((game.getResultStr() == null) || game.getResultStr().isEmpty())
			{
				game.gotoEndOfLine();
				final Position position = game.getPosition();
				if(position.isMate())
				{
					final Move move = position.getLastMove();
					if(move.isWhiteMove())
					{
						game.setTag(ChessContext.RESULT_KEY, "1-0");
						counter++;
					}
					else
					{
						game.setTag(ChessContext.RESULT_KEY, "0-1");
						counter++;
					}
				}
				else if(position.isStaleMate() || (position.getHalfMoveClock() >= ChessPresso.MAX_MOVE_CLOCK))
				{
					game.setTag(ChessContext.RESULT_KEY, "1/2-1/2");
					counter++;
				}
				//Everything else is where someone topples, a result is agreed, the game is unfinished or the game is invalid
				//This state cannot be ascertained from the position of the board
				//Note that Chesspresso ignores the result signifier after the move list
			}
		}
		return counter;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MethodReturnAlwaysConstant"})
	@Override
	public String toString()
	{
		// noinspection MagicCharacter
		return "ChessPresso{" +
			   '}';
	}
}
