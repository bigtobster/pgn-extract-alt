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
	private static final String UNKNOWN_IO_ERROR        = "Unknown IO error on Import\n";
	private static final String UNKNOWN_PGNSYNTAX_ERROR = "Unknown PGNSyntax error on Import\n";

	/**
	 * Attempts to find a PGN file and returns a File pointing to curWorkDir/target/test-classes/directory/filename Note that this function makes no
	 * guarantee that the File points to anything that actually exists!
	 *
	 * @param directory The parent directory of filename
	 * @param filename  The filename of the PGN file including the extension
	 * @return A file pointer to a PGN file
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "WeakerAccess"})
	protected static File getPGNFile(final String directory, final String filename)
	{
		return TestContext.getPGNFile(directory, filename);
	}

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

	/**
	 * Returns the ChessIO for the current context
	 *
	 * @return The current context's ChessIO instance
	 */
	@Override
	protected ChessIO getChessIO()
	{
		return (ChessIO) this.getBean(ChessIO.class);
	}

	/**
	 * Returns the ChessTagModder for the current context
	 *
	 * @return The current context's ChessTagModder instance
	 */
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
	protected void preloadPGN(final String pgn)
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
			Assert.fail(TestChessContext.UNKNOWN_IO_ERROR + e.getMessage() + '\n' + e);
		}
		catch(final PGNSyntaxError pgnSyntaxError)
		{
			//noinspection MagicCharacter
			Assert.fail(TestChessContext.UNKNOWN_PGNSYNTAX_ERROR + pgnSyntaxError.getMessage() + '\n' + pgnSyntaxError);
		}
	}
}
