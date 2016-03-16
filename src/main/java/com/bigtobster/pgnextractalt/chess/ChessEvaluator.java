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

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.move.Move;
import chesspresso.position.Position;
import com.bigtobster.pgnextractalt.uciEngine.UCIEngine;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Handles evaluating values from games of chess Created by Toby Leheup on 15/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ChessEvaluator
{
	private static final char   HYPHEN = '-';
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(ChessEvaluator.class.getName());
	private static final String SPACE  = " ";
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext   chessContext;
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessTagModder chessTagModder;

	/**
	 * Evaluates how closely a player correlates to a machine playing at maximum skill Inserts a number into the player's name tag The closer the
	 * number inserted tends towards 0, the more the moves played by that player correlated to a highly skilled machine Note that this is a highly
	 * computationally intense method - treat with caution
	 *
	 * @param games The list of games the be assessed for machine correlation
	 * @param depth The minimum depth that the engine will look for before returning a best move
	 * @param wait  The period of time the application will wait in ms before polling the uci engine for a best move/delay
	 * @param force Whether to overwrite cases which have already got a MC value
	 * @return The number of games correctly evaluated
	 * @throws IOException                    Thrown on an unknown engine failure - typically a failure to connect to the engine for some reason
	 * @throws OperationNotSupportedException Thrown when connected to the engine but engine does not support the current operating system or
	 *                                        architecture
	 * @throws java.net.URISyntaxException    Thrown on a failure to detect a Stockfish binary
	 */
	@SuppressWarnings({
							  "FeatureEnvy",
							  "OverlyLongMethod"
							  , "BooleanParameter"
							  , "MethodWithMoreThanThreeNegations"
					  })
	public static int evaluateMachineCorrelation(final ArrayList<Game> games, final int depth, final int wait, final boolean force) throws
																																	IOException,
																																	OperationNotSupportedException,
																																	URISyntaxException
	{
		final NumberFormat formatter = new DecimalFormat("#0.00");
		final UCIEngine uciEngine = new UCIEngine();
		uciEngine.startEngine(ChessContext.resolveStockfishPath());
		int modifiedGames = 0;
		boolean isGameModified = false;
		for(final Game game : games)
		{
			float whiteMCScore = 0.0F;
			float blackMCScore = 0.0F;
			if(force || ! ChessPresso.isMachineCorrelationEvaluated(game.getWhite()) || ! ChessPresso.isMachineCorrelationEvaluated(game.getBlack()))
			{
				game.gotoStart();
				Move move = game.getNextMove();
				while(move != null)
				{
					final Position position = game.getPosition();
					if(move.isWhiteMove())
					{
						if(force || ! ChessPresso.isMachineCorrelationEvaluated(game.getWhite()))
						{
							final float moveScore = ChessEvaluator.evaluateMoveCorrelationScore(uciEngine, depth, wait, move, position);
							whiteMCScore += moveScore;
						}
					}
					else
					{
						if(force || ! ChessPresso.isMachineCorrelationEvaluated(game.getBlack()))
						{
							final float moveScore = ChessEvaluator.evaluateMoveCorrelationScore(uciEngine, depth, wait, move, position);
							blackMCScore += moveScore;
						}
					}
					game.goForward();
					move = game.getNextMove();
					uciEngine.reset();
				}
			}
			if(force || ! ChessPresso.isMachineCorrelationEvaluated(game.getWhite()))
			{
				final String newWhiteName = game.getWhite() + ChessEvaluator.SPACE + ChessEvaluator.HYPHEN + ChessEvaluator.SPACE +
											ChessPresso.MC_TAG_PREFIX + ChessEvaluator.SPACE + formatter.format((double) whiteMCScore);
				game.setTag(ChessContext.WHITE_KEY, newWhiteName);
				isGameModified = true;
			}
			if(force || ! ChessPresso.isMachineCorrelationEvaluated(game.getBlack()))
			{
				final String newBlackName = game.getBlack() + ChessEvaluator.SPACE + ChessEvaluator.HYPHEN + ChessEvaluator.SPACE +
											ChessPresso.MC_TAG_PREFIX + ChessEvaluator.SPACE + formatter.format((double) blackMCScore);
				game.setTag(ChessContext.BLACK_KEY, newBlackName);
				isGameModified = true;
			}
			if(isGameModified)
			{
				modifiedGames++;
			}
		}
		uciEngine.stopEngine();
		return modifiedGames;
	}

	private static String convertLANtoCoordinateNotation(final Move move)
	{
		return Chess.sqiToStr(move.getFromSqi()) + Chess.sqiToStr(move.getToSqi());
	}

	@SuppressWarnings({"FeatureEnvy", "MethodWithTooManyParameters"})
	private static float evaluateMoveCorrelationScore(
			final UCIEngine uciEngine, final int depth, final int wait, final Move move,
			final Position currentPosition
													 ) throws IOException
	{
		uciEngine.setPosition(currentPosition.getFEN());
		final String actualMoveCoordinate = ChessEvaluator.convertLANtoCoordinateNotation(move);
		final String bestMoveCoordinate = uciEngine.getBestMoveAlt(depth, wait);
		if(actualMoveCoordinate.equals(bestMoveCoordinate))
		{
			return 0.0F;
		}
		uciEngine.reset();
		uciEngine.setPosition(currentPosition.getFEN());
		final float actualMoveEval = uciEngine.getMoveScoreAlt(actualMoveCoordinate, depth, wait);
		if(actualMoveEval < 0.0F)
		{
			return 0.0F;
		}
		uciEngine.reset();
		uciEngine.setPosition(currentPosition.getFEN());
		final float bestMoveEval = uciEngine.getMoveScoreAlt(bestMoveCoordinate, depth, wait);
		if(bestMoveEval <= 0.0F)
		{
			return 0.0F;
		}
		//Sometimes, players do well and find a move better than the engine could find. When that happens, they have high machine
		//machine correlation and so get a move score of 0. The assumption is that they used an engine with different/superior
		//configuration. This explains the use of MAX below. The better the comparison engine is, the less this should happen.
		return Math.max(bestMoveEval - actualMoveEval, 0.0f);
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ChessEvaluator{" +
			   "chessContext=" + this.chessContext +
			   '}';
	}

}
