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

import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.chess.ChessFilterer;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests that PlyCountFilter filters games correctly Created by Toby Leheup on 07/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup
 */
public class PlycountFilterTest
{
	private static int countGamesGreaterThan(final ArrayList<Game> games, final int threshold)
	{
		int counter = 0;
		for(final Game game : games)
		{
			if(game.getNumOfPlies() > threshold)
			{
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Tests that filtering all games out works correctly
	 */
	@Test
	public void filterAllGamesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final PlycountFilter plycountFilter = new PlycountFilter();
		plycountFilter.setGreaterThan(0);
		plycountFilter.setLessThan(Integer.MAX_VALUE);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(plycountFilter);
		final int expectedNoFiltered = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		for(final Game ignored : testFilterContext.getChessIO().getGames())
		{
			Assert.fail(TestFilterContext.EXPECTED_NO_GAMES_REMAINING);
		}
	}

	/**
	 * Tests that filtering no games out works correctly
	 */
	@Test
	public void filterGreaterThanTest()
	{
		final int threshold = 70;
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final PlycountFilter plycountFilter = new PlycountFilter();
		plycountFilter.setGreaterThan(threshold);
		plycountFilter.setLessThan(Integer.MAX_VALUE);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(plycountFilter);
		final int expectedNoFiltered = PlycountFilterTest.countGamesGreaterThan(testFilterContext.getChessIO().getGames(), threshold);
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
	}

	/**
	 * Tests that filtering no games out works correctly
	 */
	@Test
	public void filterLessThanTest()
	{
		final int threshold = 71;
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final PlycountFilter plycountFilter = new PlycountFilter();
		plycountFilter.setGreaterThan(Integer.MIN_VALUE);
		plycountFilter.setLessThan(threshold);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(plycountFilter);
		final ArrayList<Game> testGames = testFilterContext.getChessIO().getGames();
		final int expectedNoFiltered = testGames.size() - PlycountFilterTest.countGamesGreaterThan(testGames, threshold);
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
	}

	/**
	 * Tests that filtering no games out works correctly
	 */
	@Test
	public void filterNoGamesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final PlycountFilter plycountFilter = new PlycountFilter();
		plycountFilter.setGreaterThan(Integer.MAX_VALUE);
		plycountFilter.setLessThan(Integer.MIN_VALUE);
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(plycountFilter);
		final int expectedNoFiltered = 0;
		final int preFilteredGames = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		final int postFilteredGames = testFilterContext.getChessIO().getGames().size();
		Assert.assertEquals(TestFilterContext.EXPECTED_ALL_GAMES_REMAINING, (long) preFilteredGames, (long) postFilteredGames);
	}
}
