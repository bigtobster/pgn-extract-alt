package com.bigtobster.pgnextractalt.chess;

import chesspresso.pgn.PGNSyntaxError;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

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

	private static void assertImportFailure(final ChessIO chessIO)
	{
		Assert.assertTrue(ChessIOTest.INTENDED_PGN_IMPORT_FAILURE, ! chessIO.isPGNImported());
		assertEquals("Game list should be empty on import failure", 0L, (long) chessIO.getGamesCount());
	}

	private static void assertImportSuccess(final ChessIO chessIO)
	{
		Assert.assertTrue(ChessIOTest.INTENDED_SUCCESSFUL_IMPORT, chessIO.isPGNImported());
		Assert.assertTrue("Game list should not be empty on import success", chessIO.getGamesCount() > 0);
	}

	private static ChessIO chessIOTestInit()
	{
		final ChessIO chessIO = new TestContext().getApplicationContext().getBean(ChessIO.class);
		Assert.assertTrue("Import", ! chessIO.isPGNImported());
		return chessIO;
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException Thrown on uncaught IOException error
	 */
	@Test
	public void importEmptyTest() throws IOException
	{
		this.testImportFails(TestContext.EMPTY_PGN_PATH);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@Test
	public void importInvalidTest() throws IOException, PGNSyntaxError
	{
		this.testImportSucceeds(TestContext.SINGLE_INVALID_PGN_PATH);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@Test
	public void importLargeTest() throws IOException, PGNSyntaxError
	{
		this.testImportSucceeds(TestContext.LARGE_PGN_PATH);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@Test
	public void importMultiInvalidTest() throws IOException, PGNSyntaxError
	{
		this.testImportSucceeds(TestContext.MULTI_INVALID_PGN_PATH);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 * @throws chesspresso.pgn.PGNSyntaxError Probably bad test data
	 */
	@Test
	public void importMultiTest() throws IOException, PGNSyntaxError
	{
		this.testImportSucceeds(TestContext.MULTI_PGN_PATH);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws java.io.IOException Thrown on uncaught IOException error
	 */
	@Test
	public void importNotAPGNTest() throws IOException
	{
		this.testImportFails(TestContext.NOT_A_PGN_PGN_PATH);
	}

	/**
	 * Tests that PGN importing is flagged correctly
	 *
	 * @throws chesspresso.pgn.PGNSyntaxError Thrown on uncaught syntax error on Parse
	 * @throws java.io.IOException            Thrown on uncaught IOException error
	 */
	@Test
	public void importSingleTest() throws PGNSyntaxError, IOException
	{
		this.testImportSucceeds(TestContext.SINGLE_PGN_PATH);
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
			inputStream.close();
		}
	}

	private void testImportSucceeds(final String path) throws IOException, PGNSyntaxError
	{
		final ChessIO chessIO = ChessIOTest.chessIOTestInit();
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
