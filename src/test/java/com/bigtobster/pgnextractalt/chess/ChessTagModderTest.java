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
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tests that tag modification works Created by Toby Leheup on 15/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"UnusedDeclaration", "ClassWithTooManyMethods"})
public class ChessTagModderTest
{
	private static final String DIFF_GAME_TAG_AND_TEST_TAG  = "Difference between test game tag and original test tag";
	private static final String GAME_FAILED_TO_INSERT       = "Game failed to insert";
	private static final String INCORRECT_RESULT_CALCULATED = "Incorrect result calculated";
	private static final Logger LOGGER                      = Logger.getLogger(ChessTagModder.class.getName());
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_VALUE              = "NewTestValue";
	private static final String OLD_TEST_VALUE              = "OldTestValue";
	private static final String TAG_FAILED_TO_INSERT        = "Tag failed to insert";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String TEST_KEY                    = "TestKey";

	private static void testInsertTag(
			final TestContext testContext, final ArrayList<Game> testGames, final boolean forceInsert,
			final String predictedOutput
									 )
	{
		final ChessIO chessIO = testContext.getChessIO();
		chessIO.addGames(testGames);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, chessIO.isPGNImported());
		final ChessTagModder chessTagModder = testContext.getChessTagModder();
		chessTagModder.insertTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.NEW_TEST_VALUE, forceInsert);
		for(final Game game : chessIO.getGames())
		{
			Assert.assertEquals(
					ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG, predictedOutput,
					game.getTag(ChessTagModderTest.TEST_KEY)
							   );
		}
	}

	/**
	 * Tests a loss is correctly calculated
	 */
	@Test
	public void calculateResultBlackWinTest()
	{
		final TestContext testContext = new TestContext();
		TestContext.preloadPGN(testContext, TestContext.BLACK_WIN_MATE_HEADLESS_PGN);
		testContext.getChessTagModder().calculateGameResults();
		final Game game = testContext.getChessIO().getGames().get(0);
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, (long) Chess.RES_BLACK_WINS, (long) game.getResult());
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, ChessContext.BLACK_WIN_RESULT, game.getResultStr());
	}

	/**
	 * Tests a draw is correctly calculated
	 */
	@Test
	public void calculateResultDrawTest()
	{
		final TestContext testContext = new TestContext();
		TestContext.preloadPGN(testContext, TestContext.DRAW_HEADLESS_PGN);
		testContext.getChessTagModder().calculateGameResults();
		final Game game = testContext.getChessIO().getGames().get(0);
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, (long) Chess.RES_DRAW, (long) game.getResult());
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, ChessContext.DRAW_RESULT, game.getResultStr());
	}

	/**
	 * Tests calculate result does not overwrite an existing result (even if it is erroneous)
	 */
	@Test
	public void calculateResultExistingResTest()
	{
		final TestContext testContext = new TestContext();
		TestContext.preloadPGN(testContext, TestContext.WHITE_WIN_MATE_HEADLESS_PGN);
		testContext.getChessTagModder().insertTag(ChessContext.RESULT_KEY, ChessContext.BLACK_WIN_RESULT, false);
		testContext.getChessTagModder().calculateGameResults();
		final Game game = testContext.getChessIO().getGames().get(0);
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, (long) Chess.RES_BLACK_WINS, (long) game.getResult());
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, ChessContext.BLACK_WIN_RESULT, game.getResultStr());
	}

	/**
	 * Tests no error on failure to calculate a result
	 */
	@Test
	public void calculateResultNoResultTest()
	{
		final TestContext testContext = new TestContext();
		TestContext.preloadPGN(testContext, TestContext.INCALCULABLE_HEADLESS_PGN);
		testContext.getChessTagModder().calculateGameResults();
		final Game game = testContext.getChessIO().getGames().get(0);
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, (long) Chess.NO_RES, (long) game.getResult());
		Assert.assertNull(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, game.getResultStr());
	}

	/**
	 * Tests a win is correctly calculated
	 */
	@Test
	public void calculateResultWhiteWinTest()
	{
		final TestContext testContext = new TestContext();
		TestContext.preloadPGN(testContext, TestContext.WHITE_WIN_MATE_HEADLESS_PGN);
		testContext.getChessTagModder().calculateGameResults();
		final Game game = testContext.getChessIO().getGames().get(0);
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, (long) Chess.RES_WHITE_WINS, (long) game.getResult());
		Assert.assertEquals(ChessTagModderTest.INCORRECT_RESULT_CALCULATED, ChessContext.WHITE_WIN_RESULT, game.getResultStr());
	}

	/**
	 * Tests getWritableTags returns the set of tags it's supposed to return
	 */
	@SuppressWarnings({"MethodMayBeStatic", "PublicMethodNotExposedInInterface"})
	public void getWritableTagsTest()
	{
		final TestContext testContext = new TestContext();
		final ChessContext chessContext = testContext.getApplicationContext().getBean(ChessContext.class);
		Assert.assertArrayEquals("Tag keys do not match!", testContext.getChessTagModder().getWritableTags(), chessContext.getTagKeys());
	}

	/**
	 * Tests tag changes appropriately when attempting to change an existing tag
	 */
	@Test
	public void insertTagExistingTest()
	{
		final TestContext testContext = new TestContext();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGame.setTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.OLD_TEST_VALUE);
		Assert.assertTrue(ChessTagModderTest.TAG_FAILED_TO_INSERT, testGame.getTags().length > 0);
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());
		Assert.assertEquals(
				ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG,
				ChessTagModderTest.OLD_TEST_VALUE,
				testGame.getTag(ChessTagModderTest.TEST_KEY)
						   );

		ChessTagModderTest.testInsertTag(testContext, testGames, false, ChessTagModderTest.OLD_TEST_VALUE);
	}

	/**
	 * Tests tag changes appropriately when forcing a change to an existing tag
	 */
	@Test
	public void insertTagForceExistingTest()
	{
		final TestContext testContext = new TestContext();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGame.setTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.OLD_TEST_VALUE);
		Assert.assertTrue(ChessTagModderTest.TAG_FAILED_TO_INSERT, testGame.getTags().length > 0);
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());
		Assert.assertEquals(
				ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG,
				ChessTagModderTest.OLD_TEST_VALUE,
				testGame.getTag(ChessTagModderTest.TEST_KEY)
						   );

		ChessTagModderTest.testInsertTag(testContext, testGames, true, ChessTagModderTest.NEW_TEST_VALUE);
	}

	/**
	 * Tests tag inserts appropriately when forcing a change to a non-existent tag
	 */
	@Test
	public void insertTagForceNonExistingTest()
	{
		final TestContext testContext = new TestContext();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());

		ChessTagModderTest.testInsertTag(testContext, testGames, true, ChessTagModderTest.NEW_TEST_VALUE);
	}

	/**
	 * Tests tag inserts appropriately when inserting a non-existent tag
	 */
	@Test
	public void insertTagNonExistingTest()
	{
		final TestContext testContext = new TestContext();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());

		ChessTagModderTest.testInsertTag(testContext, testGames, false, ChessTagModderTest.NEW_TEST_VALUE);
	}
}
