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

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tests the Context of PGNExtractAlt Created by Toby Leheup on 08/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("UnusedDeclaration")
public class ChessContextTest
{
	private static final String BAD_DIFFERENT_GAME      = "getGames returns different game to the one set";
	private static final String BAD_DIFFERENT_GAME_LIST = "getGames returns different list to the one set";
	private static final String BAD_IMPORT_INIT         = "ChessContext falsely initialising as imported";
	private static final String BAD_NULL_GAME_LIST      = "getGames returns Null when it shouldn't";
	private static final Logger LOGGER                  = Logger.getLogger(ChessContext.class.getName());
	private static final String NON_EMPTY_GAME_LIST     = "chessContext should contain non-empty game list";
	private static final String SUCCESSFUL_IMPORT       = "Import should be successful";

	/**
	 * Tests that addGames and getGames functionality
	 */
	@Test
	public void addGamesTest()
	{
		//Set up
		final ChessContext chessContext = new ChessContext();
		final ArrayList<Game> emptyGameList = new ArrayList<Game>(0);
		final ArrayList<Game> singleGameList = new ArrayList<Game>(1);
		final ArrayList<Game> multiGameList = new ArrayList<Game>(3);
		final Game game = new Game();
		singleGameList.add(game);
		multiGameList.add(game);
		multiGameList.add(game);
		multiGameList.add(game);

		//Test empty game list
		chessContext.addGames(emptyGameList);
		Assert.assertNotNull(ChessContextTest.BAD_NULL_GAME_LIST, chessContext.getGames());
		Assert.assertEquals(ChessContextTest.BAD_DIFFERENT_GAME_LIST, emptyGameList, chessContext.getGames());
		chessContext.reset();

		//Test single game list
		chessContext.addGames(singleGameList);
		Assert.assertNotNull(ChessContextTest.BAD_NULL_GAME_LIST, chessContext.getGames());
		Assert.assertEquals(ChessContextTest.BAD_DIFFERENT_GAME_LIST, singleGameList, chessContext.getGames());
		Assert.assertEquals(ChessContextTest.BAD_DIFFERENT_GAME, game, chessContext.getGames().get(0));
		chessContext.reset();

		//Test multi game list
		chessContext.addGames(multiGameList);
		Assert.assertNotNull(ChessContextTest.BAD_NULL_GAME_LIST, chessContext.getGames());
		Assert.assertEquals(ChessContextTest.BAD_DIFFERENT_GAME_LIST, multiGameList, chessContext.getGames());
		for(final Game testGame : chessContext.getGames())
		{
			Assert.assertEquals(ChessContextTest.BAD_DIFFERENT_GAME, testGame, game);
		}
	}

	/**
	 * Tests reset functionality
	 */
	@Test
	public void resetTest()
	{
		final ChessContext chessContext = new ChessContext();
		Assert.assertFalse(ChessContextTest.BAD_IMPORT_INIT, chessContext.isPGNImported());
		Assert.assertTrue("Should be no Game list registered before import", chessContext.getGames().isEmpty());

		final ArrayList<Game> singleGameList = new ArrayList<Game>(1);
		singleGameList.add(new Game());
		chessContext.addGames(singleGameList);

		Assert.assertFalse("Test game list should not be empty", singleGameList.isEmpty());
		Assert.assertTrue(ChessContextTest.SUCCESSFUL_IMPORT, chessContext.isPGNImported());
		Assert.assertFalse(ChessContextTest.NON_EMPTY_GAME_LIST, chessContext.getGames().isEmpty());

		chessContext.reset();

		Assert.assertFalse("Should be registered as un-imported after reset", chessContext.isPGNImported());
		Assert.assertTrue("Game list should be empty after reset", chessContext.getGames().isEmpty());

		chessContext.addGames(singleGameList);

		Assert.assertTrue(ChessContextTest.SUCCESSFUL_IMPORT, chessContext.isPGNImported());
		Assert.assertFalse(ChessContextTest.NON_EMPTY_GAME_LIST, chessContext.getGames().isEmpty());
	}

	/**
	 * Tests that path to Stockfish engine resolves properly
	 *
	 * @throws OperationNotSupportedException Thrown on attempt to resolve a path on an OS that is not supported
	 */
	@Test
	public void resolveStockfishPathTest() throws OperationNotSupportedException
	{
		final String stockfishPath = ChessContext.resolveStockfishPath();
		final String operatingSystem = System.getProperty("os.name").toLowerCase();
		String pathSubstr = ChessContext.STOCKFISH_PATH;
		if(operatingSystem.contains(ChessContext.OS_LINUX))
		{
			pathSubstr += ChessContext.STOCKFISH_LINUX_SUBSTR;
		}
		else if(operatingSystem.contains(ChessContext.OS_WINDOWS))
		{
			pathSubstr += ChessContext.STOCKFISH_WINDOWS_SUBSTR;
		}
		else if(operatingSystem.contains(ChessContext.OS_MAC))
		{
			pathSubstr += ChessContext.STOCKFISH_MAC_SUBSTR;
		}
		Assert.assertTrue("Path to engine is not a valid path", stockfishPath.contains(pathSubstr));
	}
}
