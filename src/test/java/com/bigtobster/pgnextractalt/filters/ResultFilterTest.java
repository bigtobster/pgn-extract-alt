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
import com.bigtobster.pgnextractalt.chess.ChessFilterer;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that ResultFilter filters games correctly
 * Created by Toby Leheup on 05/02/16 for pgn-extract-alt.
 * @author Toby Leheup (Bigtobster)
 */
public class ResultFilterTest
{

	/**
	 * Tests that filtering all games out works correctly
	 */
	@Test
	public void filterAllGamesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.preloadPGN(TestContext.MULTI_PGN);
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setWhiteWinFiltered(true);
		resultFilter.setBlackWinFiltered(true);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int expectedNoFiltered = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		for(final Game ignored : testFilterContext.getChessIO().getGames())
		{
			Assert.fail(TestFilterContext.EXPECTED_NO_GAMES_REMAINING);
		}
	}

	/**
	 * Tests that black wins are filtered out correctly
	 */
	@Test
	public void filterBlackWinTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.preloadPGN(TestContext.MULTI_PGN);
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setBlackWinFiltered(true);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int expectedNoFiltered = 2;
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		for(final Game game : testFilterContext.getChessIO().getGames())
		{
			Assert.assertTrue("No results of Black Win should remain", game.getResult() != Chess.RES_BLACK_WINS);
		}
	}

	/**
	 * Tests that draws are filtered out correctly
	 */
	@Test
	public void filterDrawTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.preloadPGN(TestContext.LARGE_PGN);
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setDrawFiltered(true);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int expectedNoFiltered = 29;
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		for(final Game game : testFilterContext.getChessIO().getGames())
		{
			Assert.assertTrue("No results of Draw should remain", game.getResult() != Chess.RES_DRAW);
		}
	}

	/**
	 * Tests that filtering no games out works correctly
	 */
	@Test
	public void filterNoGamesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.preloadPGN(TestContext.MULTI_PGN);
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setDrawFiltered(true);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int expectedNoFiltered = 0;
		final int preFilteredGames = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		final int postFilteredGames = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.EXPECTED_ALL_GAMES_REMAINING, (long) preFilteredGames, (long) postFilteredGames);
	}

	/**
	 * Tests that unresolved results are filtered out correctly
	 */
	@Test
	public void filterUnresolvedTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.preloadPGN(TestContext.DRAW_HEADLESS_PGN);
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setUnresolvedFiltered(true);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int expectedNoFiltered = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		for(final Game game : testFilterContext.getChessIO().getGames())
		{
			Assert.assertTrue("Unfinished games should not remain", game.getResult() != Chess.RES_NOT_FINISHED);
			Assert.assertTrue("Games without a result should not remain remain", game.getResult() != Chess.NO_RES);
		}
	}

	/**
	 * Tests that white wins are filtered out correctly
	 */
	@Test
	public void filterWhiteWinTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.preloadPGN(TestContext.MULTI_PGN);
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setWhiteWinFiltered(true);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(resultFilter);
		final int expectedNoFiltered = 3;
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		for(final Game game : testFilterContext.getChessIO().getGames())
		{
			Assert.assertTrue("No results of White Win should remain", game.getResult() != Chess.RES_WHITE_WINS);
		}
	}
}
