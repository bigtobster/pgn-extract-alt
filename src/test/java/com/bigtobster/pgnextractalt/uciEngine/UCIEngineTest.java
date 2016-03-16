/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.uciEngine;

import org.junit.Assert;
import org.junit.Test;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Tests the UCIEngine class works as expected Created by Toby Leheup on 03/03/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class UCIEngineTest
{

	private static final String A8B8_MOVE                = "a8b8";
	private static final String ARCH_X64                 = "64";
	private static final String C2C4_MOVE                = "c2c4";
	private static final String C7C5_MOVE                = "c7c5";
	private static final String G2G3_MOVE                = "g2g3";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                   = Logger.getLogger(UCIEngineTest.class.getName());
	private static final String MATE_BOARD_FEN           = "r2n1R2/p1R3bk/1p3N1p/2p4B/2P5/1P6/PBK3PP/4r3";
	private static final String MID_PLAY_BOARD_FEN       = "rnbqkbnr/pppppppp/8/8/2P5/8/PP1PPPPP/RNBQKBNR";
	private static final String NEARLY_MATE_BOARD_FEN    = "r2n1R2/p1R3bk/1p5p/2p4B/2P3N1/1P6/PBK3PP/4r3";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String OS_LINUX                 = "linux";
	private static final String OS_MAC                   = "mac";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String OS_WINDOWS               = "windows";
	private static final String START_BOARD_FEN          = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String STOCKFISH_7_X64          = "stockfish-7-x64-";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String STOCKFISH_LINUX_SUBSTR   = "linux";
	private static final String STOCKFISH_MAC_SUBSTR     = "mac";
	private static final String STOCKFISH_WINDOWS_SUBSTR = "win.exe";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String TEST_STOCKFISH_DIR       = "stockfish";
	private static final String
								UNEXPECTED_FEN_INEQUALITY
															   = "The current board position should be the same as the most recently set position";
	private static final String UNEXPECTED_NEGATIVE_MOVE_SCORE = "Move score not be negative in a non-mate position";
	private static final String UNEXPECTED_NON_MATE_SCORE      = "Mate-able position should always return -1";

	private static UCIEngine initTestEngine() throws OperationNotSupportedException, IOException
	{
		final UCIEngine uciEngine = new UCIEngine();
		final File stockfishFile = new File(UCIEngineTest.resolveTestStockfishPath());
		Assert.assertTrue("Resolved engine path must exist", stockfishFile.exists());
		Assert.assertTrue("Resolved engine must be a file", stockfishFile.isFile());
		Assert.assertTrue("Engine should be executable", stockfishFile.setExecutable(true));
		uciEngine.startEngine(stockfishFile);
		return uciEngine;
	}

	private static String resolveTestStockfishPath() throws OperationNotSupportedException
	{
		final String operatingSystem = System.getProperty("os.name").toLowerCase();
		final String arch = System.getProperty("os.arch").toLowerCase();
		String fullStockfishPath = File.separator + UCIEngineTest.TEST_STOCKFISH_DIR + File.separator + UCIEngineTest.STOCKFISH_7_X64;
		if(! arch.contains(UCIEngineTest.ARCH_X64))
		{
			//noinspection DuplicateStringLiteralInspection
			throw new OperationNotSupportedException(arch + " Architecture not supported");
		}
		if(operatingSystem.contains(UCIEngineTest.OS_LINUX))
		{
			fullStockfishPath += UCIEngineTest.STOCKFISH_LINUX_SUBSTR;
		}
		else if(operatingSystem.contains(UCIEngineTest.OS_WINDOWS))
		{
			fullStockfishPath += UCIEngineTest.STOCKFISH_WINDOWS_SUBSTR;
		}
		else if(operatingSystem.contains(UCIEngineTest.OS_MAC))
		{
			fullStockfishPath += UCIEngineTest.STOCKFISH_MAC_SUBSTR;
		}
		else
		{
			//noinspection DuplicateStringLiteralInspection
			throw new OperationNotSupportedException("Operating system not supported: " + operatingSystem);
		}
		return UCIEngineTest.class.getResource(fullStockfishPath).getPath();
	}

	/**
	 * Tests that getBestMove works as expected
	 *
	 * @throws IOException                    Error on executing UCI command
	 * @throws OperationNotSupportedException Either architecture or operating system not supported
	 */
	@Test
	public void getBestMoveTest() throws IOException, OperationNotSupportedException
	{
		final UCIEngine uciEngine = UCIEngineTest.initTestEngine();
		String bestMoveLAN = uciEngine.getBestMoveAlt(10, 50);
		Assert.assertNotNull("LAN should not be null", bestMoveLAN);
		Assert.assertFalse("LAN should not be empty", bestMoveLAN.isEmpty());
		Assert.assertTrue("LAN should be longer than a single character", bestMoveLAN.length() > 1);
		Assert.assertTrue("LAN should be no longer than 10 characters", bestMoveLAN.length() < 10);
		uciEngine.reset();
		uciEngine.setPosition(UCIEngineTest.MATE_BOARD_FEN);
		bestMoveLAN = uciEngine.getBestMoveAlt(10, 50).replace("\n", "").trim();
		Assert.assertEquals("A best move should not be found in a checkmate position", "(none)", bestMoveLAN);
		uciEngine.stopEngine();
	}

	/**
	 * Tests that getBestMoveScore works as expected
	 *
	 * @throws IOException                    Error on executing UCI command
	 * @throws OperationNotSupportedException Either architecture or operating system not supported
	 */
	@Test
	public void getMoveScoreTest() throws IOException, OperationNotSupportedException
	{
		final UCIEngine uciEngine = UCIEngineTest.initTestEngine();
		Assert.assertTrue(UCIEngineTest.UNEXPECTED_NEGATIVE_MOVE_SCORE, uciEngine.getMoveScoreAlt(UCIEngineTest.C2C4_MOVE, 10, 20) >= 0.0F);
		uciEngine.setPosition(UCIEngineTest.MID_PLAY_BOARD_FEN);
		Assert.assertTrue(UCIEngineTest.UNEXPECTED_NEGATIVE_MOVE_SCORE, uciEngine.getMoveScoreAlt(UCIEngineTest.C7C5_MOVE, 10, 20) >= 0.0F);
		uciEngine.setPosition(UCIEngineTest.NEARLY_MATE_BOARD_FEN);
		double score = (double) uciEngine.getMoveScoreAlt(UCIEngineTest.G2G3_MOVE, 10, 20);
		Assert.assertEquals(UCIEngineTest.UNEXPECTED_NON_MATE_SCORE, 0.0, score, 0.0);
		score = (double) uciEngine.getMoveScoreAlt(UCIEngineTest.A8B8_MOVE, 10, 20);
		Assert.assertEquals(UCIEngineTest.UNEXPECTED_NON_MATE_SCORE, - 1.0, score, 0.0);
		uciEngine.stopEngine();
	}

	/**
	 * Tests that reset works as expected
	 *
	 * @throws IOException                    Error on executing UCI command
	 * @throws OperationNotSupportedException Either architecture or operating system not supported
	 */
	@Test
	public void resetTest() throws IOException, OperationNotSupportedException
	{
		final UCIEngine uciEngine = UCIEngineTest.initTestEngine();
		uciEngine.setPosition(UCIEngineTest.MID_PLAY_BOARD_FEN);
		Assert.assertEquals(UCIEngineTest.UNEXPECTED_FEN_INEQUALITY, UCIEngineTest.MID_PLAY_BOARD_FEN, uciEngine.getPosition());
		uciEngine.reset();
		uciEngine.setPosition(UCIEngineTest.START_BOARD_FEN);
		Assert.assertEquals(UCIEngineTest.UNEXPECTED_FEN_INEQUALITY, UCIEngineTest.START_BOARD_FEN, uciEngine.getPosition());
		uciEngine.stopEngine();
	}

	/**
	 * Tests that setPosition works as expected
	 *
	 * @throws IOException                    Error on executing UCI command
	 * @throws OperationNotSupportedException Either architecture or operating system not supported
	 */
	@Test
	public void setPositionTest() throws IOException, OperationNotSupportedException
	{
		final UCIEngine uciEngine = UCIEngineTest.initTestEngine();
		uciEngine.setPosition(UCIEngineTest.START_BOARD_FEN);
		final String fenPosition = uciEngine.getPosition();
		Assert.assertEquals(UCIEngineTest.UNEXPECTED_FEN_INEQUALITY, UCIEngineTest.START_BOARD_FEN, fenPosition);
		uciEngine.setPosition(UCIEngineTest.MID_PLAY_BOARD_FEN);
		Assert.assertEquals(UCIEngineTest.UNEXPECTED_FEN_INEQUALITY, UCIEngineTest.MID_PLAY_BOARD_FEN, uciEngine.getPosition());
		uciEngine.stopEngine();
	}
}
