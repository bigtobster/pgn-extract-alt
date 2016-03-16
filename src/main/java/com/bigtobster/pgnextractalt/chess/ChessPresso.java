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
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class is for all the functionality that should be in Chesspresso but isn't In the longer term, everything in the class should be pushed into
 * Chesspresso Until Chesspresso is in such a place where this is do-able, use this class! Once Chesspresso has been properly sorted out, this class
 * should deleted Created by Toby Leheup on 04/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ChessPresso
{
	/**
	 * The prefix for Machine Correlation score
	 */
	public static final  String  MC_TAG_PREFIX  = "MC:";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger  LOGGER         = Logger.getLogger(ChessPresso.class.getName());
	private static final int     MAX_MOVE_CLOCK = 50;
	private static final Pattern SPACE_PATTERN  = Pattern.compile(" ");

	/**
	 * Inserts the correct result, if evaluate-able, into a chess game Works on confirmed mates and draws In most cases it will not be able to work it
	 * out
	 *
	 * @param games The list of games to evaluate
	 * @return Number of inserted tags
	 */
	public static int evaluateGameResults(final ArrayList<Game> games)
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

	/**
	 * Gets a given player's machine correlation score. Throws an error if this hasn't been calculated for that player
	 *
	 * @param player The player which the MC score is to be retrieved
	 * @return The MC score
	 */
	public static float getMachineCorrelationScore(final String player)
	{
		final String[] words = ChessPresso.SPACE_PATTERN.split(player);
		return Float.parseFloat(words[words.length - 1]);
	}

	/**
	 * Calculates if a player tag instance has a machine correlation score in it
	 *
	 * @param player The player tag string to be analysed
	 * @return Boolean Whether a valid score is present or not
	 */
	public static boolean isMachineCorrelationEvaluated(final String player)
	{
		if((player == null) || player.isEmpty())
		{
			return false;
		}
		final String[] words = ChessPresso.SPACE_PATTERN.split(player);
		if((words.length < 2) || ! words[words.length - 2].equals(ChessPresso.MC_TAG_PREFIX))
		{
			return false;
		}
		try
		{
			//noinspection ResultOfMethodCallIgnored
			Float.parseFloat(words[words.length - 1]);
		}
		catch(final NumberFormatException ignored)
		{
			return false;
		}
		return true;
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
