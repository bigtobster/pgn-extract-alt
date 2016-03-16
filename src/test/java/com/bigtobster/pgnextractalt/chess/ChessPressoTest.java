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
import chesspresso.move.IllegalMoveException;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Tests that the ChessPresso functionality works as intended Created by Toby Leheup on 03/03/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ChessPressoTest
{
	private static final String EXPECTED_MC_VALUE            = "Machine Correlation value is expected";
	private static final String INCORRECT_RESULT_EVALUATED   = "Incorrect result evaluated";
	private static final char   SPACE                        = ' ';
	private static final String UNEXPECTED_MC_VALUE          = "Machine Correlation Value should not be present";
	private static final String UNEXPECTED_NEGATIVE_MC_SCORE = "Machine Correlation score should never be negative";

	/**
	 * Tests a loss is correctly evaluated
	 */
	@Test
	public void evaluateResultBlackWinTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.BLACK_WIN_MATE_HEADLESS_PGN);
		ChessPresso.evaluateGameResults(testChessContext.getChessIO().getGames());
		final Game game = testChessContext.getChessIO().getGames().get(0);
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, (long) Chess.RES_BLACK_WINS, (long) game.getResult());
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, ChessContext.BLACK_WIN_RESULT, game.getResultStr());
	}

	/**
	 * Tests a draw is correctly evaluated
	 */
	@Test
	public void evaluateResultDrawTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.DRAW_HEADLESS_PGN);
		ChessPresso.evaluateGameResults(testChessContext.getChessIO().getGames());
		final Game game = testChessContext.getChessIO().getGames().get(0);
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, (long) Chess.RES_DRAW, (long) game.getResult());
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, ChessContext.DRAW_RESULT, game.getResultStr());
	}

	/**
	 * Tests evaluate result does not overwrite an existing result (even if it is erroneous)
	 */
	@Test
	public void evaluateResultExistingResTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.WHITE_WIN_MATE_HEADLESS_PGN);
		final ChessTagModder chessTagModder = testChessContext.getChessTagModder();
		chessTagModder.insertTag(ChessContext.RESULT_KEY, ChessContext.BLACK_WIN_RESULT, false);
		ChessPresso.evaluateGameResults(testChessContext.getChessIO().getGames());
		final Game game = testChessContext.getChessIO().getGames().get(0);
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, (long) Chess.RES_BLACK_WINS, (long) game.getResult());
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, ChessContext.BLACK_WIN_RESULT, game.getResultStr());
	}

	/**
	 * Tests no error on failure to evaluate a result
	 */
	@Test
	public void evaluateResultNoResultTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.INCALCULABLE_HEADLESS_PGN);
		ChessPresso.evaluateGameResults(testChessContext.getChessIO().getGames());
		final Game game = testChessContext.getChessIO().getGames().get(0);
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, (long) Chess.NO_RES, (long) game.getResult());
		org.junit.Assert.assertNull(ChessPressoTest.INCORRECT_RESULT_EVALUATED, game.getResultStr());
	}

	/**
	 * Tests a win is correctly evaluated
	 */
	@Test
	public void evaluateResultWhiteWinTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.WHITE_WIN_MATE_HEADLESS_PGN);
		ChessPresso.evaluateGameResults(testChessContext.getChessIO().getGames());
		final Game game = testChessContext.getChessIO().getGames().get(0);
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, (long) Chess.RES_WHITE_WINS, (long) game.getResult());
		org.junit.Assert.assertEquals(ChessPressoTest.INCORRECT_RESULT_EVALUATED, ChessContext.WHITE_WIN_RESULT, game.getResultStr());
	}

	/**
	 * Tests that machine correlation score retrieval works as expected
	 *
	 * @throws chesspresso.move.IllegalMoveException       Thrown on an illegal move being found in one of the test games
	 * @throws java.io.IOException                         Thrown on difficulties communicating with engine
	 * @throws java.net.URISyntaxException                 Thrown on difficulties finding engine
	 * @throws javax.naming.OperationNotSupportedException Thrown on testing on unsupported OS
	 */
	@SuppressWarnings("MethodWithTooExceptionsDeclared")
	@Test
	public void getMachineCorrelationScoreTest() throws URISyntaxException, IllegalMoveException, OperationNotSupportedException, IOException
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.SINGLE_PGN);
		final Game game = testChessContext.getChessIO().getGames().get(0);
		String whitePlayer = game.getWhite();
		game.setTag(ChessContext.WHITE_KEY, null);
		//noinspection ProhibitedExceptionCaught
		try
		{
			ChessPresso.getMachineCorrelationScore(game.getWhite());
		}
		catch(final NullPointerException ignored)
		{
			//This is what should happen - do nothing
		}
		game.setTag(ChessContext.WHITE_KEY, "");
		try
		{
			ChessPresso.getMachineCorrelationScore(game.getWhite());
		}
		catch(final NumberFormatException ignored)
		{
			//This is what should happen - do nothing
		}
		game.setTag(ChessContext.WHITE_KEY, whitePlayer);
		try
		{
			ChessPresso.getMachineCorrelationScore(game.getWhite());
		}
		catch(final NumberFormatException ignored)
		{
			//This is what should happen - do nothing
		}
		testChessContext.getChessIO().reset();
		testChessContext.loadPGN(TestContext.SINGLE_MC_PGN);
		whitePlayer = testChessContext.getChessIO().getGames().get(0).getWhite();
		final String blackPlayer = testChessContext.getChessIO().getGames().get(0).getBlack();
		float score = ChessPresso.getMachineCorrelationScore(whitePlayer);
		Assert.assertTrue(ChessPressoTest.UNEXPECTED_NEGATIVE_MC_SCORE, score >= 0.0f);
		score = ChessPresso.getMachineCorrelationScore(blackPlayer);
		Assert.assertTrue(ChessPressoTest.UNEXPECTED_NEGATIVE_MC_SCORE, score >= 0.0f);
	}

	/**
	 * Tests that isMachineCorrelationEvaluated works as expected
	 *
	 * @throws chesspresso.move.IllegalMoveException       Thrown on an illegal move being found in one of the test games
	 * @throws java.io.IOException                         Thrown on difficulties communicating with engine
	 * @throws java.net.URISyntaxException                 Thrown on difficulties finding engine
	 * @throws javax.naming.OperationNotSupportedException Thrown on testing on unsupported OS
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "MethodWithTooExceptionsDeclared", "NonBooleanMethodNameMayNotStartWithQuestion"})
	@Test
	public void isMachineCorrelationEvaluatedTest() throws URISyntaxException, IllegalMoveException, OperationNotSupportedException, IOException
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.SINGLE_PGN);
		Game game = testChessContext.getChessIO().getGames().get(0);
		final String whitePlayer = game.getWhite();
		game.setTag(ChessContext.WHITE_KEY, null);
		Assert.assertFalse(ChessPressoTest.UNEXPECTED_MC_VALUE, ChessPresso.isMachineCorrelationEvaluated(game.getWhite()));
		game.setTag(ChessContext.WHITE_KEY, "");
		Assert.assertFalse(ChessPressoTest.UNEXPECTED_MC_VALUE, ChessPresso.isMachineCorrelationEvaluated(game.getWhite()));
		game.setTag(ChessContext.WHITE_KEY, whitePlayer);
		Assert.assertFalse(ChessPressoTest.UNEXPECTED_MC_VALUE, ChessPresso.isMachineCorrelationEvaluated(game.getWhite()));
		game.setTag(ChessContext.WHITE_KEY, whitePlayer + ChessPressoTest.SPACE + 14051991);
		Assert.assertFalse(ChessPressoTest.UNEXPECTED_MC_VALUE, ChessPresso.isMachineCorrelationEvaluated(game.getWhite()));
		testChessContext.getChessIO().reset();
		testChessContext.loadPGN(TestContext.SINGLE_MC_PGN);
		game = testChessContext.getChessIO().getGames().get(0);
		Assert.assertTrue(ChessPressoTest.EXPECTED_MC_VALUE, ChessPresso.isMachineCorrelationEvaluated(game.getWhite()));
	}
}
