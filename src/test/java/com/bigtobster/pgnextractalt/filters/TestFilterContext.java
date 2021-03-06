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

import chesspresso.pgn.PGNSyntaxError;
import com.bigtobster.pgnextractalt.chess.ChessEvaluator;
import com.bigtobster.pgnextractalt.chess.ChessFilterer;
import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.chess.ChessTagModder;
import com.bigtobster.pgnextractalt.misc.TestContext;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * Testing context for filter classes.
 * Holds common functionality and resources for Test classes in the filters package.
 * Implements TestContext.
 * @author Toby Leheup (Bigtobster)
 */
class TestFilterContext extends TestContext
{
	/**
	 * Error message when all games expected to be unfiltered and this isn't the case
	 */
	static final String EXPECTED_ALL_GAMES_REMAINING = "All games should remain after removing no games";
	/**
	 * Error message when all games expected to be filtered and this isn't the case
	 */
	static final String EXPECTED_NO_GAMES_REMAINING  = "There should be no games remaining after filtering all games";
	/**
	 * Error message on number of filtered games being different from expected
	 */
	static final String GAMES_FILTERED_DIFFERENT_EXP = "Number of games filtered is different from expected";

	@Override
	protected ChessEvaluator getChessEvaluator()
	{
		return (ChessEvaluator) this.getBean(ChessEvaluator.class);
	}

	@Override
	protected ChessFilterer getChessFilterer()
	{
		return (ChessFilterer) this.getBean(ChessFilterer.class);
	}

	@Override
	protected ChessIO getChessIO()
	{
		return (ChessIO) this.getBean(ChessIO.class);
	}

	@Override
	protected ChessTagModder getChessTagModder()
	{
		return (ChessTagModder) this.getBean(ChessTagModder.class);
	}

	@Override
	protected void loadPGN(final String pgn)
	{
		final File pgnFile = TestContext.pgnToPGNFile(pgn);

		final ChessIO chessIO = this.getChessIO();
		try
		{
			chessIO.importPGN(pgnFile);
		}
		catch(final IOException e)
		{
			//noinspection MagicCharacter
			Assert.fail(TestContext.UNKNOWN_IO_ERROR + e.getMessage() + '\n' + e);
		}
		catch(final PGNSyntaxError pgnSyntaxError)
		{
			//noinspection MagicCharacter
			Assert.fail(TestContext.UNKNOWN_PGNSYNTAX_ERROR + pgnSyntaxError.getMessage() + '\n' + pgnSyntaxError);
		}
	}
}
