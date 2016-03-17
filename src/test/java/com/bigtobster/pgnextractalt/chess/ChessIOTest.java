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
import com.bigtobster.pgnextractalt.misc.TestContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.activation.UnsupportedDataTypeException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import static java.nio.file.Files.createDirectory;

/**
 * Tests the IO of PGNExtractAlt. Created by Toby Leheup on 08/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"UnusedDeclaration", "ClassWithTooManyMethods"})
public class ChessIOTest
{
	private static final String INTENDED_PGN_IMPORT_FAILURE = "PGN should not have been successfully imported";
	private static final String INTENDED_SUCCESSFUL_IMPORT  = "PGN should have imported successfully";
	private static final Logger LOGGER                      = Logger.getLogger(ChessIOTest.class.getName());

	/**
	 * Creates the dump directory for us in the following test methods
	 *
	 * @throws IOException Failure to create the directory
	 */
	@BeforeClass
	public static void initDump() throws IOException
	{
		final File exportFile = TestChessContext.getPGNFile(TestContext.DUMP_DIR, TestContext.EMPTY_PGN);
		if(! exportFile.getParentFile().exists())
		{
			createDirectory(exportFile.getParentFile().toPath());
		}

	}

	private static void assertImportFailure(final ChessIO chessIO)
	{
		Assert.assertTrue(ChessIOTest.INTENDED_PGN_IMPORT_FAILURE, ! chessIO.isPGNImported());
		Assert.assertEquals("Game list should be empty on import failure", 0L, (long) chessIO.getGames().size());
	}

	private static void assertImportSuccess(final ChessIO chessIO)
	{
		Assert.assertTrue(ChessIOTest.INTENDED_SUCCESSFUL_IMPORT, chessIO.isPGNImported());
		Assert.assertTrue("Game list should not be empty on import success", chessIO.isPGNImported());
	}

	private static ChessIO chessIOTestInit()
	{
		final ChessIO chessIO = new TestChessContext().getChessIO();
		Assert.assertTrue("Import", ! chessIO.isPGNImported());
		return chessIO;
	}

	private static void testExportSucceeds(final File importFile, final ChessIO chessIO) throws IOException, PGNSyntaxError
	{
		//Need a unique identifier to avoid overwriting other tests
		// noinspection MagicCharacter
		final File exportFile = TestChessContext.getPGNFile(TestContext.DUMP_DIR, UUID.randomUUID().toString() + '-' + importFile.getName());
		//noinspection ResultOfMethodCallIgnored
		exportFile.createNewFile();
		final PrintWriter printWriter = new PrintWriter(exportFile);
		ChessIOTest.testImportSucceeds(importFile, chessIO);
		final ArrayList importedChessGames = chessIO.getGames();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, importFile);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, printWriter);
		chessIO.exportPGN(printWriter);
		chessIO.reset();
		ChessIOTest.testImportSucceeds(exportFile, chessIO);
		final ArrayList exportedChessGames = chessIO.getGames();
		Assert.assertEquals("Imported games should be logically equal to exported games", importedChessGames, exportedChessGames);
	}

	private static void testImportFails(final File pgnFile) throws IOException
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		try
		{
			Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
			chessIO.importPGN(pgnFile);
			Assert.fail("Import has succeeded when failure expected!");
		}
		catch(final PGNSyntaxError ignored)
		{
			ChessIOTest.assertImportFailure(chessIO);
		}
		catch(final UnsupportedDataTypeException ignored)
		{
			ChessIOTest.assertImportFailure(chessIO);
		}
	}

	private static void testImportSucceeds(final File pgnFile, final ChessIO chessIO) throws IOException, PGNSyntaxError
	{
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, pgnFile);
		chessIO.importPGN(pgnFile);
		ChessIOTest.assertImportSuccess(chessIO);
	}

	/**
	 * Tests that an imported large multiple game PGN file is exported correctly
	 *
	 * @throws IOException    Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportLargeTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testExportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.LARGE_PGN), chessIO);
	}

	/**
	 * Tests that an imported invalid multi game PGN file is exported correctly
	 *
	 * @throws IOException    Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportMultiInvalidTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testExportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.MULTI_INVALID_PGN), chessIO);
	}

	/**
	 * Tests that an imported multiple game PGN file is exported correctly
	 *
	 * @throws IOException    Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportMultiTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testExportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.MULTI_PGN), chessIO);
	}

	/**
	 * Tests that an imported invalid single PGN file is exported correctly
	 *
	 * @throws IOException    Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportSingleInvalidTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testExportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.SINGLE_INVALID_PGN), chessIO);
	}

	/**
	 * Tests that an imported single game PGN file is exported correctly
	 *
	 * @throws IOException    Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportSingleTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testExportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.SINGLE_PGN), chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException Thrown on uncaught IOException error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importEmptyTest() throws IOException
	{
		ChessIOTest.testImportFails(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.EMPTY_PGN));
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importInvalidTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testImportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.SINGLE_INVALID_PGN), chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importLargeTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testImportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.LARGE_PGN), chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importMultiInvalidTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testImportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.MULTI_INVALID_PGN), chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importMultiTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testImportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.MULTI_PGN), chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException Thrown on uncaught IOException error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importNotAPGNTest() throws IOException
	{
		ChessIOTest.testImportFails(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.NOT_A_PGN));
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws chesspresso.pgn.PGNSyntaxError Thrown on uncaught syntax error on Parse
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void importSingleTest() throws PGNSyntaxError, IOException
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		ChessIOTest.testImportSucceeds(TestChessContext.getPGNFile(TestContext.IMPORTS_DIR, TestContext.SINGLE_PGN), chessIO);
	}
}
