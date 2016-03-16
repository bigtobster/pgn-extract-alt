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
 * Tests the DuplicatesFilter filters games correctly Created by Toby Leheup on 08/02/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class DuplicateFilterTest
{
	/**
	 * Tests that all duplicate games are removed when all games are duplicated
	 */
	@Test
	public void allDuplicatesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final int testImportGamesNo = testFilterContext.getChessIO().getGames().size();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final DuplicateFilter duplicatesFilter = new DuplicateFilter();
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(duplicatesFilter);
		final int expectedNoFiltered = testFilterContext.getChessIO().getGames().size() / 2;
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		Assert.assertEquals(
				TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) testImportGamesNo,
				(long) testFilterContext.getChessIO().getGames().size()
						   );
		final ArrayList<Game> remainingGames = testFilterContext.getChessIO().getGames();
		testFilterContext.getChessIO().reset();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, testFilterContext.getChessIO().getGames(), remainingGames);
	}

	/**
	 * Tests that no games are removed when no games are duplicated
	 */
	@Test
	public void noDuplicatesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		final int testImportGamesNo = testFilterContext.getChessIO().getGames().size();
		final DuplicateFilter duplicatesFilter = new DuplicateFilter();
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(duplicatesFilter);
		final int expectedNoFiltered = 0;
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		Assert.assertEquals(
				TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) testImportGamesNo,
				(long) testFilterContext.getChessIO().getGames().size()
						   );
		final ArrayList<Game> remainingGames = testFilterContext.getChessIO().getGames();
		testFilterContext.getChessIO().reset();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, testFilterContext.getChessIO().getGames(), remainingGames);
	}

	/**
	 * Tests that all duplicate games are removed when some games are duplicated
	 */
	@Test
	public void someDuplicatesTest()
	{
		final TestFilterContext testFilterContext = new TestFilterContext();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		testFilterContext.loadPGN(TestContext.SINGLE_PGN);
		final int testImportGamesNo = testFilterContext.getChessIO().getGames().size();
		testFilterContext.loadPGN(TestContext.SINGLE_PGN);
		testFilterContext.loadPGN(TestContext.SINGLE_PGN);

		final DuplicateFilter duplicatesFilter = new DuplicateFilter();
		final ChessFilterer chessFilterer = testFilterContext.getChessFilterer();
		chessFilterer.loadFilter(duplicatesFilter);
		final int expectedNoFiltered = 2;
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) expectedNoFiltered, (long) chessFilterer.run());
		Assert.assertEquals(
				TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, (long) testImportGamesNo,
				(long) testFilterContext.getChessIO().getGames().size()
						   );
		final ArrayList<Game> remainingGames = testFilterContext.getChessIO().getGames();
		testFilterContext.getChessIO().reset();
		testFilterContext.loadPGN(TestContext.MULTI_PGN);
		testFilterContext.loadPGN(TestContext.SINGLE_PGN);
		Assert.assertEquals(TestFilterContext.GAMES_FILTERED_DIFFERENT_EXP, testFilterContext.getChessIO().getGames(), remainingGames);
	}
}
