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
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tests that tag modification works. Created by Toby Leheup on 15/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ChessTagModderTest
{
	private static final String DIFF_GAME_TAG_AND_TEST_TAG = "Difference between test game tag and original test tag";
	private static final String GAME_FAILED_TO_INSERT      = "Game failed to insert";
	private static final Logger LOGGER                     = Logger.getLogger(ChessTagModder.class.getName());
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_VALUE             = "NewTestValue";
	private static final String OLD_TEST_VALUE             = "OldTestValue";
	private static final String TAG_FAILED_TO_INSERT       = "Tag failed to insert";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String TEST_KEY                   = "TestKey";

	private static void testInsertTag(final ArrayList<Game> testGames, final boolean forceInsert, final String predictedOutput)
	{
		final TestChessContext testChessContext = new TestChessContext();
		final ChessIO chessIO = testChessContext.getChessIO();
		chessIO.addGames(testGames);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, chessIO.isPGNImported());
		final ChessTagModder chessTagModder = testChessContext.getChessTagModder();
		chessTagModder.insertTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.NEW_TEST_VALUE, forceInsert);
		for(final Game game : chessIO.getGames())
		{
			Assert.assertEquals(ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG, predictedOutput, game.getTag(ChessTagModderTest.TEST_KEY));
		}
	}

	/**
	 * Tests getWritableTags returns the set of tags it's supposed to return
	 */
	@SuppressWarnings({"MethodMayBeStatic", "PublicMethodNotExposedInInterface"})
	public void getWritableTagsTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		final ChessContext chessContext = testChessContext.getChessContext();
		Assert.assertArrayEquals("Tag keys do not match!", testChessContext.getChessTagModder().getWritableTags(), chessContext.getTagKeys());
	}

	/**
	 * Tests tag changes appropriately when attempting to change an existing tag
	 */
	@Test
	public void insertTagExistingTest()
	{
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

		ChessTagModderTest.testInsertTag(testGames, false, ChessTagModderTest.OLD_TEST_VALUE);
	}

	/**
	 * Tests tag changes appropriately when forcing a change to an existing tag
	 */
	@Test
	public void insertTagForceExistingTest()
	{
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

		ChessTagModderTest.testInsertTag(testGames, true, ChessTagModderTest.NEW_TEST_VALUE);
	}

	/**
	 * Tests tag inserts appropriately when forcing a change to a non-existent tag
	 */
	@Test
	public void insertTagForceNonExistingTest()
	{
		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());

		ChessTagModderTest.testInsertTag(testGames, true, ChessTagModderTest.NEW_TEST_VALUE);
	}

	/**
	 * Tests tag inserts appropriately when inserting a non-existent tag
	 */
	@Test
	public void insertTagNonExistingTest()
	{
		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());

		ChessTagModderTest.testInsertTag(testGames, false, ChessTagModderTest.NEW_TEST_VALUE);
	}
}
