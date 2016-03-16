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

import chesspresso.pgn.PGNSyntaxError;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * Testing context for chess classes Should only be accessible to members of chess package
 *
 * @author Toby Leheup
 */
class TestChessContext extends TestContext
{

	/**
	 * Returns the ChessContext for the current context
	 *
	 * @return The current context's ChessContext instance
	 */
	@SuppressWarnings("UnusedDeclaration")
	protected ChessContext getChessContext()
	{
		return (ChessContext) this.getBean(ChessContext.class);
	}

	@Override
	protected ChessEvaluator getChessEvaluator()
	{
		return (ChessEvaluator) this.getBean(ChessEvaluator.class);
	}

	@SuppressWarnings("UnusedDeclaration")
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

	@SuppressWarnings("UnusedDeclaration")
	@Override
	protected ChessTagModder getChessTagModder()
	{
		return (ChessTagModder) this.getBean(ChessTagModder.class);
	}

	/**
	 * Loads any given context with files located at a given path
	 *
	 * @param pgn Path to a PGN file to import
	 */
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
