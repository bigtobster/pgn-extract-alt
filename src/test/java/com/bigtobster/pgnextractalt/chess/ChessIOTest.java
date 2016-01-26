package com.bigtobster.pgnextractalt.chess;

import chesspresso.pgn.PGNSyntaxError;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import javax.activation.UnsupportedDataTypeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import static java.nio.file.Files.createDirectory;

/**
 * Tests the IO of PGNExtractAlt Created by Toby Leheup on 08/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"UnusedDeclaration", "ClassWithTooManyMethods"})
public class ChessIOTest
{
	private static final String INTENDED_PGN_IMPORT_FAILURE = "PGN should not have been successfully imported";
	private static final String INTENDED_SUCCESSFUL_IMPORT  = "PGN should have imported successfully";
	private static final Logger LOGGER = Logger.getLogger(ChessIOTest.class.getName());

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
		final ChessIO chessIO = new TestContext().getChessIO();
		Assert.assertTrue("Import", ! chessIO.isPGNImported());
		return chessIO;
	}

	/**
	 * Tests that an imported large multiple game PGN file is exported correctly
	 * @throws IOException Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportLargeTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		this.testExportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.LARGE_PGN, chessIO);
	}

	/**
	 * Tests that an imported invalid multi game PGN file is exported correctly
	 * @throws IOException Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportMultiInvalidTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		this.testExportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.MULTI_INVALID_PGN, chessIO);
	}

	/**
	 * Tests that an imported multiple game PGN file is exported correctly
	 * @throws IOException Thrown on import or export file handling failure
	 * @throws PGNSyntaxError Thrown on the imported PGN file having a syntax error
	 */
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void exportMultiTest() throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		this.testExportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.MULTI_PGN, chessIO);
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
		this.testExportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.SINGLE_INVALID_PGN, chessIO);
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
		this.testExportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.SINGLE_PGN, chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException Thrown on uncaught IOException error
	 */
	@Test
	public void importEmptyTest() throws IOException
	{
		this.testImportFails(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.EMPTY_PGN);
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
		this.testImportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.SINGLE_INVALID_PGN, chessIO);
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
		this.testImportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.LARGE_PGN, chessIO);
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
		this.testImportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.MULTI_INVALID_PGN, chessIO);
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
		this.testImportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.MULTI_PGN, chessIO);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException Thrown on uncaught IOException error
	 */
	@Test
	public void importNotAPGNTest() throws IOException
	{
		this.testImportFails(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.NOT_A_PGN);
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
		this.testImportSucceeds(File.separator + TestContext.IMPORTS_DIR + File.separator + TestContext.SINGLE_PGN, chessIO);
	}

	private void testExportSucceeds(final String importPGNPath, final ChessIO chessIO) throws IOException, PGNSyntaxError
	{
		final File importFile = new File(importPGNPath);
		//Need a unique identifier to avoid overwriting other tests
		//noinspection MagicCharacter
		final String exportPGNPath = File.separator +
									 TestContext.DUMP_DIR +
									 File.separator +
									 UUID.randomUUID() +
									 '-' +
									 importFile.getName();
		final String absExportPGNPath = System.getProperty("user.dir") +
										File.separator +
										TestContext.TARGET_DIR +
										File.separator +
										TestContext.TEST_CLASSES_DIR +
										File.separator +
										exportPGNPath;
		final File exportFile = new File(absExportPGNPath);
		if(! exportFile.getParentFile().exists())
		{
			createDirectory(exportFile.getParentFile().toPath());
		}
		//noinspection ResultOfMethodCallIgnored
		exportFile.createNewFile();
		final PrintWriter printWriter = new PrintWriter(exportFile);
		this.testImportSucceeds(importPGNPath, chessIO);
		final ArrayList importedChessGames = chessIO.getGames();
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, importFile);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, printWriter);
		chessIO.exportPGN(printWriter);
		chessIO.reset();
		this.testImportSucceeds(exportPGNPath, chessIO);
		final ArrayList exportedChessGames = chessIO.getGames();
		Assert.assertEquals("Imported games should be logically equal to exported games", importedChessGames, exportedChessGames);
	}

	private void testImportFails(final String path) throws IOException
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
		final InputStream inputStream = this.getClass().getResourceAsStream(path);
		try
		{
			Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, inputStream);
			chessIO.importPGN(inputStream, path);
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
		finally
		{
			if(inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	private void testImportSucceeds(final String path, final ChessIO chessIO) throws IOException, PGNSyntaxError
	{
		final InputStream inputStream = this.getClass().getResourceAsStream(path);
		try
		{
			Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, inputStream);
			chessIO.importPGN(inputStream, path);
			ChessIOTest.assertImportSuccess(chessIO);
		}
		finally
		{
			inputStream.close();
		}
	}
}
