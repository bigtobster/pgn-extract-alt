package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Handles the modification of tags of chess games Created by Toby Leheup on 15/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("PublicMethodNotExposedInInterface")
public class ChessTagModder
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(ChessTagModder.class.getName());

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext chessContext;

	/**
	 * Returns an array of all the tags that can be written to a game
	 *
	 * @return The array of tags that can be written to a game
	 */
	public String[] getWritableTags()
	{
		return this.chessContext.getTagKeys();
	}

	/**
	 * Inserted tags with a given key from Chess games
	 *
	 * @param tagKey   The key of the tag to be inserted
	 * @param tagValue The value of the tag to be inserted
	 * @param tagForce Whether to overwrite existing tags with key of tagKey
	 * @return The total number of games that were inserted
	 */
	@SuppressWarnings({"PublicMethodNotExposedInInterface", "BooleanParameter"})
	public int insertTag(final String tagKey, final String tagValue, final boolean tagForce)
	{
		int counter = 0;
		final ArrayList<Game> games = this.chessContext.getGames();
		for(final Game game : games)
		{
			if(! (! tagForce && (game.getTag(tagKey) != null)))
			{
				game.setTag(tagKey, tagValue);
				counter++;
			}
		}
		return counter;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "ChessTagModder{" +
			   "chessContext=" + this.chessContext +
			   '}';
	}
}
