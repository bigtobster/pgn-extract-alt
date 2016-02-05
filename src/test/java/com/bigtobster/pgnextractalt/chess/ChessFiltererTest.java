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

import com.bigtobster.pgnextractalt.core.TestContext;
import com.bigtobster.pgnextractalt.filters.ResultFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.MissingResourceException;

/**
 * Tests ChessFilterer performs as expected
 * Created by Toby Leheup on 04/02/16 for pgn-extract-alt.
 * @author Toby Leheup (Bigtobster)
 */
public class ChessFiltererTest
{
	private static final String SHOULD_NOT_FILTER_GAMES = "Filter should not filter any games";

	/**
	 * Tests that load performs without failing
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void loadTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		final ChessFilterer chessFilterer = testChessContext.getChessFilterer();
		chessFilterer.loadFilter(new ResultFilter());
		chessFilterer.loadFilter(null);
		chessFilterer.loadFilter(new ResultFilter());
	}

	/**
	 * Tests a normal use of run with a typical filter
	 */
	@Test
	public void normalRunTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		testChessContext.preloadPGN(TestContext.MULTI_PGN);
		final ChessFilterer chessFilterer = testChessContext.getChessFilterer();
		final ResultFilter resultFilter = new ResultFilter();
		resultFilter.setBlackWinFiltered(true);
		chessFilterer.loadFilter(resultFilter);
		Assert.assertEquals("Exactly 2 games should be filtered", 2L, (long) chessFilterer.run());
	}

	/**
	 * Tests run with a null filter before and after a correct filter insertion
	 */
	@Test
	public void nullFilterRunTest()
	{
		final TestChessContext testChessContext = new TestChessContext();
		final ChessFilterer chessFilterer = testChessContext.getChessFilterer();
		try
		{
			chessFilterer.run();
		}
		catch(final MissingResourceException ignored)
		{
			chessFilterer.loadFilter(new ResultFilter());
			Assert.assertEquals(ChessFiltererTest.SHOULD_NOT_FILTER_GAMES, 0L, (long) chessFilterer.run());
			chessFilterer.loadFilter(null);
			Assert.assertEquals(ChessFiltererTest.SHOULD_NOT_FILTER_GAMES, 0L, (long) chessFilterer.run());
		}
	}

}
