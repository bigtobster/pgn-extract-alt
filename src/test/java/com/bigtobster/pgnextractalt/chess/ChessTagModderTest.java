package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;
import com.bigtobster.pgnextractalt.core.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tests that tag modification works Created by Toby Leheup on 15/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("UnusedDeclaration")
public class ChessTagModderTest
{
	private static final String AUTOWIRED_BEAN_NOT_CREATED = "Autowired bean not created";
	private static final String DIFF_GAME_TAG_AND_TEST_TAG = "Difference between test game tag and original test tag";
	private static final String GAME_FAILED_TO_INSERT      = "Game failed to insert";
	private static final Logger LOGGER                     = Logger.getLogger(ChessTagModder.class.getName());
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String NEW_TEST_VALUE             = "NewTestValue";
	private static final String OLD_TEST_VALUE             = "OldTestValue";
	private static final String TAG_FAILED_TO_INSERT       = "Tag failed to insert";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String TEST_KEY                   = "TestKey";

	private static TestContext initInsertCustomTag()
	{
		final TestContext testContext = new TestContext();
		Assert.assertNotNull("Error in creating TestContext", testContext);
		final ChessTagModder chessTagModder = testContext.getApplicationContext().getBean(ChessTagModder.class);
		Assert.assertNotNull(ChessTagModderTest.AUTOWIRED_BEAN_NOT_CREATED, chessTagModder);
		final ChessContext chessContext = testContext.getApplicationContext().getBean(ChessContext.class);
		Assert.assertNotNull(ChessTagModderTest.AUTOWIRED_BEAN_NOT_CREATED, chessContext);
		return testContext;
	}

	private static void testInsertCustomTag(
			final TestContext testContext, final ArrayList<Game> testGames, final boolean forceInsert,
			final String predictedOutput
										   )
	{
		final ChessIO chessIO = testContext.getChessIO();
		chessIO.addGames(testGames);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, chessIO.isPGNImported());
		final ChessTagModder chessTagModder = testContext.getApplicationContext().getBean(ChessTagModder.class);
		chessTagModder.insertCustomTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.NEW_TEST_VALUE, forceInsert);
		for(final Game game : chessIO.getGames())
		{
			Assert.assertEquals(
					ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG, predictedOutput,
					game.getTag(ChessTagModderTest.TEST_KEY)
							   );
		}
	}

	/**
	 * Tests tag changes appropriately when attempting to change an existing tag
	 */
	@Test
	public void insertCustomTagExistingTest()
	{
		final TestContext testContext = ChessTagModderTest.initInsertCustomTag();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGame.setTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.OLD_TEST_VALUE);
		Assert.assertTrue(ChessTagModderTest.TAG_FAILED_TO_INSERT, testGame.getTags().length > 0);
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());
		Assert.assertEquals(
				ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG,
				ChessTagModderTest.OLD_TEST_VALUE,
				testGame.getTag(ChessTagModderTest.TEST_KEY)
						   );

		ChessTagModderTest.testInsertCustomTag(testContext, testGames, false, ChessTagModderTest.OLD_TEST_VALUE);
	}

	/**
	 * Tests tag changes appropriately when forcing a change to an existing tag
	 */
	@Test
	public void insertCustomTagForceExistingTest()
	{
		final TestContext testContext = ChessTagModderTest.initInsertCustomTag();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGame.setTag(ChessTagModderTest.TEST_KEY, ChessTagModderTest.OLD_TEST_VALUE);
		Assert.assertTrue(ChessTagModderTest.TAG_FAILED_TO_INSERT, testGame.getTags().length > 0);
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());
		Assert.assertEquals(
				ChessTagModderTest.DIFF_GAME_TAG_AND_TEST_TAG,
				ChessTagModderTest.OLD_TEST_VALUE,
				testGame.getTag(ChessTagModderTest.TEST_KEY)
						   );

		ChessTagModderTest.testInsertCustomTag(testContext, testGames, true, ChessTagModderTest.NEW_TEST_VALUE);
	}

	/**
	 * Tests tag inserts appropriately when forcing a change to a non-existent tag
	 */
	@SuppressWarnings("InstanceMethodNamingConvention")
	@Test
	public void insertCustomTagForceNonExistingTest()
	{
		final TestContext testContext = ChessTagModderTest.initInsertCustomTag();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());

		ChessTagModderTest.testInsertCustomTag(testContext, testGames, true, ChessTagModderTest.NEW_TEST_VALUE);
	}

	/**
	 * Tests tag inserts appropriately when inserting a non-existent tag
	 */
	@Test
	public void insertCustomTagNonExistingTest()
	{
		final TestContext testContext = ChessTagModderTest.initInsertCustomTag();

		final ArrayList<Game> testGames = new ArrayList<Game>(2);
		final Game testGame = new Game();
		testGames.add(testGame);
		testGames.add(testGame);
		Assert.assertTrue(ChessTagModderTest.GAME_FAILED_TO_INSERT, ! testGames.isEmpty());

		ChessTagModderTest.testInsertCustomTag(testContext, testGames, false, ChessTagModderTest.NEW_TEST_VALUE);
	}
}
