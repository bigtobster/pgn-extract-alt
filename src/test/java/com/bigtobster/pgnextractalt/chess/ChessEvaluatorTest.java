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
import chesspresso.move.IllegalMoveException;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Tests that the ChessEvaluator functionality works as intended Created by Toby Leheup on 03/03/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class ChessEvaluatorTest
{
	private static final String  ALL_GAMES_SHOULD_BE_UPDATED   = "All games should be updated when not evaluated";
	private static final String  IVANOV_NAME                   = "Ivanov, Borislav";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger  LOGGER                        = Logger.getLogger(ChessEvaluatorTest.class.getName());
	private static final String  NO_GAMES_EXCEEDS_EXPECTATION  = "Number of games exceeds expectation";
	private static final String  NO_GAMES_SHOULD_BE_UPDATED    = "In non-forced mode, games should not be updated when already evaluated";
	private static final Pattern SPACE_SPLITTER                = Pattern.compile(" ");
	private static final String  UNEXPECTED_MOD_TO_PLAYER_NAME = "Unexpected modification to player name";

	/**
	 * Tests that cases where no cheating is expected does not raise a high correlation
	 *
	 * @throws chesspresso.move.IllegalMoveException       Thrown on an illegal move being found in one of the test games
	 * @throws java.io.IOException                         Thrown on difficulties communicating with engine
	 * @throws java.net.URISyntaxException                 Thrown on difficulties finding engine
	 * @throws javax.naming.OperationNotSupportedException Thrown on testing on unsupported OS
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "MethodWithTooExceptionsDeclared"})
	@Test
	public void evaluateMachineCorrelationExpectedCorrelationTest() throws
																	URISyntaxException,
																	IllegalMoveException,
																	OperationNotSupportedException,
																	IOException
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.SMALL_IVANOV_PGN);
		final int ivanovGames = testChessContext.getChessIO().getGames().size();
		Assert.assertEquals(ChessEvaluatorTest.NO_GAMES_EXCEEDS_EXPECTATION, 3L, (long) testChessContext.getChessIO().getGames().size());
		final ArrayList<Game> games = testChessContext.getChessIO().getGames();
		final int evaluatedGames = ChessEvaluator.evaluateMachineCorrelation(games, 12, 10, false);

		Assert.assertEquals(
				ChessEvaluatorTest.ALL_GAMES_SHOULD_BE_UPDATED
				, (long) evaluatedGames
				, (long) games.size()
						   );
		Assert.assertEquals(ChessEvaluatorTest.NO_GAMES_EXCEEDS_EXPECTATION, 3L, (long) testChessContext.getChessIO().getGames().size());
		float notIvanovAggScore = 0.0F;
		float notIvanovCounter = 0.0F;
		float ivanovAggScore = 0.0F;
		float ivanovCounter = 0.0F;
		for(final Game game : games)
		{
			final String whitePlayer = game.getWhite();
			final String blackPlayer = game.getBlack();
			if(whitePlayer.startsWith(ChessEvaluatorTest.IVANOV_NAME))
			{
				ivanovAggScore += ChessPresso.getMachineCorrelationScore(whitePlayer);
				ivanovCounter++;
			}
			else
			{
				notIvanovAggScore += ChessPresso.getMachineCorrelationScore(whitePlayer);
				notIvanovCounter++;
			}
			if(blackPlayer.startsWith(ChessEvaluatorTest.IVANOV_NAME))
			{
				ivanovAggScore += ChessPresso.getMachineCorrelationScore(blackPlayer);
				ivanovCounter++;
			}
			else
			{
				notIvanovAggScore += ChessPresso.getMachineCorrelationScore(blackPlayer);
				notIvanovCounter++;
			}
		}
		Assert.assertEquals("Expected number of Ivanov games not found", (double) ivanovGames, (double) ivanovCounter, 0.0);
		Assert.assertEquals("Expected number of Non-Ivanov games not found", (double) ivanovGames, (double) notIvanovCounter, 0.0);
		notIvanovAggScore /= notIvanovCounter;
		ivanovAggScore /= ivanovCounter;
		Assert.assertTrue(
				"Confirmed cheating games should have a lower Machine Correlation score than regular games",
				ivanovAggScore < notIvanovAggScore
						 );
	}

	/**
	 * Tests that the force option works as expected
	 *
	 * @throws chesspresso.move.IllegalMoveException       Thrown on an illegal move being found in one of the test games
	 * @throws java.io.IOException                         Thrown on difficulties communicating with engine
	 * @throws java.net.URISyntaxException                 Thrown on difficulties finding engine
	 * @throws javax.naming.OperationNotSupportedException Thrown on testing on unsupported OS
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "MethodWithTooExceptionsDeclared"})
	@Test
	public void evaluateMachineCorrelationForceTest() throws URISyntaxException, IllegalMoveException, OperationNotSupportedException, IOException
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.SINGLE_PGN);
		ArrayList<Game> games = testChessContext.getChessIO().getGames();
		Assert.assertEquals(
				ChessEvaluatorTest.ALL_GAMES_SHOULD_BE_UPDATED
				, (long) ChessEvaluator.evaluateMachineCorrelation(games, 13, 10, false)
				, (long) games.size()
						   );
		games = testChessContext.getChessIO().getGames();
		Assert.assertEquals(
				"All games should be updated when forced"
				, (long) ChessEvaluator.evaluateMachineCorrelation(games, 13, 10, true)
				, (long) games.size()
						   );
	}

	/**
	 * Tests that the lack of force option works as expected
	 *
	 * @throws chesspresso.move.IllegalMoveException       Thrown on an illegal move being found in one of the test games
	 * @throws java.io.IOException                         Thrown on difficulties communicating with engine
	 * @throws java.net.URISyntaxException                 Thrown on difficulties finding engine
	 * @throws javax.naming.OperationNotSupportedException Thrown on testing on unsupported OS
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "MethodWithTooExceptionsDeclared"})
	@Test
	public void evaluateMachineCorrelationNoForceTest() throws URISyntaxException, IllegalMoveException, OperationNotSupportedException, IOException
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.SINGLE_PGN);
		final ArrayList<Game> games = testChessContext.getChessIO().getGames();
		Assert.assertEquals(
				ChessEvaluatorTest.ALL_GAMES_SHOULD_BE_UPDATED
				, (long) games.size()
				, (long) ChessEvaluator.evaluateMachineCorrelation(games, 13, 10, false)
						   );
		Assert.assertEquals(
				ChessEvaluatorTest.NO_GAMES_SHOULD_BE_UPDATED
				, 0L
				, (long) ChessEvaluator.evaluateMachineCorrelation(games, 13, 10, false)
						   );
	}

	/**
	 * Tests that the output on the name tag is as expected
	 *
	 * @throws chesspresso.move.IllegalMoveException       Thrown on an illegal move being found in one of the test games
	 * @throws java.io.IOException                         Thrown on difficulties communicating with engine
	 * @throws java.net.URISyntaxException                 Thrown on difficulties finding engine
	 * @throws javax.naming.OperationNotSupportedException Thrown on testing on unsupported OS
	 */
	@SuppressWarnings({"InstanceMethodNamingConvention", "MethodWithTooExceptionsDeclared"})
	@Test
	public void evaluateMachineCorrelationOutputTest() throws URISyntaxException, IllegalMoveException, OperationNotSupportedException, IOException
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.loadPGN(TestContext.SINGLE_PGN);
		final Game unmodifiedSingleGame = testChessContext.getChessIO().getGames().get(0);
		testChessContext.getChessIO().reset();
		testChessContext.loadPGN(TestContext.SINGLE_PGN);
		final ArrayList<Game> games = testChessContext.getChessIO().getGames();
		ChessEvaluator.evaluateMachineCorrelation(games, 13, 10, false);
		final Game modifiedSingleGame = testChessContext.getChessIO().getGames().get(0);
		final String unmodifiedWhite = unmodifiedSingleGame.getWhite();
		final String unmodifiedBlack = unmodifiedSingleGame.getBlack();
		String modifiedWhite = modifiedSingleGame.getWhite();
		String modifiedBlack = modifiedSingleGame.getBlack();
		final String[] modifiedWhiteArr = ChessEvaluatorTest.SPACE_SPLITTER.split(modifiedWhite);
		final String[] modifiedBlackArr = ChessEvaluatorTest.SPACE_SPLITTER.split(modifiedBlack);
		final int wantedWhiteChars = modifiedWhite.length() - modifiedWhiteArr[modifiedWhiteArr.length - 1].length();
		final int wantedBlackChars = modifiedBlack.length() - modifiedBlackArr[modifiedBlackArr.length - 1].length();
		modifiedWhite = StringUtils.arrayToDelimitedString(modifiedWhiteArr, " ").substring(0, wantedWhiteChars).trim();
		modifiedBlack = StringUtils.arrayToDelimitedString(modifiedBlackArr, " ").substring(0, wantedBlackChars).trim();
		modifiedWhite = modifiedWhite.replace(ChessPresso.MC_TAG_PREFIX, "").trim();
		modifiedBlack = modifiedBlack.replace(ChessPresso.MC_TAG_PREFIX, "").trim();
		modifiedWhite = modifiedWhite.substring(0, modifiedWhite.length() - 1).trim();
		modifiedBlack = modifiedBlack.substring(0, modifiedBlack.length() - 1).trim();
		Assert.assertEquals(ChessEvaluatorTest.UNEXPECTED_MOD_TO_PLAYER_NAME, unmodifiedWhite, modifiedWhite);
		Assert.assertEquals(ChessEvaluatorTest.UNEXPECTED_MOD_TO_PLAYER_NAME, unmodifiedBlack, modifiedBlack);
	}
}
