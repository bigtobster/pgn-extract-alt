package com.bigtobster.pgnextractalt.chess;

import chesspresso.game.Game;
import chesspresso.move.Move;
import chesspresso.position.Position;
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
	private static final int MAX_MOVE_CLOCK = 50;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessContext chessContext;

	/**
	 * Inserts the correct result, if calculable, into a chess game
	 * Works on confirmed mates and draws
	 * In most cases it will not be able to work it out
	 * @return Number of inserted tags
	 */
	public int calculateGameResults()
	{
		int counter = 0;
		for(final Game game : this.chessContext.getGames())
		{
			if((game.getResultStr() == null) || game.getResultStr().isEmpty())
			{
				game.gotoEndOfLine();
				final Position position = game.getPosition();
				if(position.isMate())
				{
					final Move move = position.getLastMove();
					if(move.isWhiteMove())
					{
						game.setTag(ChessContext.RESULT_KEY, "1-0");
						counter++;
					}
					else
					{
						game.setTag(ChessContext.RESULT_KEY, "0-1");
						counter++;
					}
				}
				else if(position.isStaleMate() || (position.getHalfMoveClock() >= ChessTagModder.MAX_MOVE_CLOCK))
				{
					game.setTag(ChessContext.RESULT_KEY, "1/2-1/2");
					counter++;
				}
				//Everything else is where someone topples, a result is agreed, the game is unfinished or the game is invalid
				//This state cannot be ascertained from the position of the board
				//Note that Chesspresso ignores the result signifier after the move list
			}
		}
		return counter;
	}

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
