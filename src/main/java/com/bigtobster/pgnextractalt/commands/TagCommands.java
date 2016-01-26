package com.bigtobster.pgnextractalt.commands;

import chesspresso.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Spring Shell Command class for Modifying PGN Tags Created by Toby Leheup on 15/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
@Component
public class TagCommands implements CommandMarker
{
	/**
	 * Message when all games have a completed results tag
	 */
	static final         String ALL_GAMES_COMPLETED_RESULT_TAG  = "All loaded games have a completed Result tag.";
	/**
	 * Message on failure to insert tags
	 */
	static final         String FAILED_TO_INSERT_TAGS           = "Failed to insert tags.";
	/**
	 * The force option
	 */
	static final         String FORCE                           = "Force";
	/**
	 * Info to user that key has already been fully used
	 */
	static final         String KEY_ALREADY_USED                = "Key already used by all games. Using --Force to overwrite.";
	/**
	 * The message substring on successfully inserting tags
	 */
	static final         String SUCCESSFULLY_INSERTED_TAGS      = "Successfully inserted tags!";
	/**
	 * The message tail substring on successfully inserting tags
	 */
	static final         String TAGS_INSERTED                   = "tags inserted.";
	/**
	 * The TagKey option
	 */
	static final         String TAG_KEY                         = "TagKey";
	/**
	 * The TagValue option
	 */
	static final         String TAG_VALUE                       = "TagValue";
	/**
	 * Message when results cannot be ascertained
	 */
	static final         String UNABLE_TO_ASCERTAIN_ANY_RESULTS = "Unable to ascertain any results.";
	private static final String CALCULATE_RESULT_COMMAND        = "calculate-result";
	private static final String CALCULATE_RESULT_COMMAND_HELP   = "For all games without a result tag, " +
																  "will attempt to calculate the result and add it as a tag. Available when" +
																  " games imported.";
	private static final String INSERT_TAG_COMMAND              = "insert-tag";
	private static final String INSERT_TAG_COMMAND_HELP         =
			"Insert a tag into list of games. Available when games imported. New tag " +
			"must be a recognised PGN tag.";
	private static final String LIST_WRITABLE_TAGS_COMMAND      = "list-writable-tags";
	private static final String LIST_WRITABLE_TAGS_COMMAND_HELP = "Lists all the tags that PGN-Extract-Alt can insert into games";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                          = Logger.getLogger(TagCommands.class.getName());
	private static final char   SPACE                           = ' ';
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	/**
	 * Getter for List Writable Tags Command String
	 *
	 * @return String List Writable Tags Command String
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getCalculateResultCommand()
	{
		return TagCommands.CALCULATE_RESULT_COMMAND;
	}

	/**
	 * Getter for Insert Tag Command String
	 *
	 * @return String Insert Tag Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getInsertTagCommand()
	{
		return TagCommands.INSERT_TAG_COMMAND;
	}

	/**
	 * Getter for List Writable Tags Command String
	 *
	 * @return String List Writable Tags Command String
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getListWritableTagsCommand()
	{
		return TagCommands.LIST_WRITABLE_TAGS_COMMAND;
	}

	/**
	 * Describes when "List Writable Tags" command is available
	 *
	 * @return boolean Availability (Always available))
	 */
	@SuppressWarnings({"MethodReturnAlwaysConstant", "SameReturnValue"})
	@CliAvailabilityIndicator({TagCommands.LIST_WRITABLE_TAGS_COMMAND})
	public static boolean isListWritableTagsAvailable()
	{
		return true;
	}

	/**
	 * Handle the interface for calculating the result of a game and inserting the calculated value into the tag for that game
	 *
	 * @return Success message
	 */
	@CliCommand(value = TagCommands.CALCULATE_RESULT_COMMAND, help = TagCommands.CALCULATE_RESULT_COMMAND_HELP)
	public String calculateResultTag()
	{
		final int tagsInsertedNo = this.commandContext.getChessTagModder().calculateGameResults();
		boolean allResultsCalculated = true;
		for(final Game game : this.commandContext.getChessIO().getGames())
		{
			if((game.getResultStr() == null) || game.getResultStr().isEmpty())
			{
				allResultsCalculated = false;
				//noinspection BreakStatement
				break;
			}
		}
		if(allResultsCalculated)
		{
			return TagCommands.FAILED_TO_INSERT_TAGS + TagCommands.SPACE + TagCommands.ALL_GAMES_COMPLETED_RESULT_TAG;
		}
		if(tagsInsertedNo == 0)
		{
			return TagCommands.FAILED_TO_INSERT_TAGS + TagCommands.SPACE + TagCommands.UNABLE_TO_ASCERTAIN_ANY_RESULTS;
		}
		return TagCommands.SUCCESSFULLY_INSERTED_TAGS + TagCommands.SPACE + tagsInsertedNo + TagCommands.SPACE + TagCommands.TAGS_INSERTED;
	}

	/**
	 * Handle the interface for inserting a tag into a list of games
	 *
	 * @param tagKey      The key of the tag to be inserted
	 * @param tagValue    The value of the tag to be inserted
	 * @param forceInsert Whether to overwrite existing tags of that key
	 * @return Success message
	 */
	@SuppressWarnings("BooleanParameter")
	@CliCommand(value = TagCommands.INSERT_TAG_COMMAND, help = TagCommands.INSERT_TAG_COMMAND_HELP)
	public String insertTag(
			@CliOption(key = {TagCommands.TAG_KEY}, mandatory = true, help = "The key of the tag to be inserted.")
			final String tagKey,
			@CliOption(key = {TagCommands.TAG_VALUE}, mandatory = true, help = "The value of the tag to be inserted.")
			final String tagValue,
			@CliOption(key = {TagCommands.FORCE}, mandatory = false, help = "\"true\" to overwrite any existing tags with this key",
					   unspecifiedDefaultValue = "false")
			final boolean forceInsert
						   )
	{
		final int tagsInsertedNo = this.commandContext.getChessTagModder().insertTag(tagKey, tagValue, forceInsert);
		if(tagsInsertedNo == 0)
		{
			return TagCommands.FAILED_TO_INSERT_TAGS + TagCommands.SPACE + TagCommands.KEY_ALREADY_USED;
		}
		return TagCommands.SUCCESSFULLY_INSERTED_TAGS + TagCommands.SPACE + tagsInsertedNo + TagCommands.SPACE + TagCommands.TAGS_INSERTED;
	}

	/**
	 * Describes when "Calculate Results" command is available
	 *
	 * @return boolean Availability (Available on successful import of at least 1 game)
	 */
	@CliAvailabilityIndicator({TagCommands.CALCULATE_RESULT_COMMAND})
	public boolean isCalculateResultsAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Describes when "Insert Tag" command is available
	 *
	 * @return boolean Availability (Available on successful import of at least 1 game)
	 */
	@CliAvailabilityIndicator({TagCommands.INSERT_TAG_COMMAND})
	public boolean isInsertTagAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Returns a formatted output of all the tags that can be used
	 *
	 * @return The formatted output of all the tags that can be used
	 */
	@CliCommand(value = TagCommands.LIST_WRITABLE_TAGS_COMMAND, help = TagCommands.LIST_WRITABLE_TAGS_COMMAND_HELP)
	public String listWritableTags()
	{
		final StringBuilder outputBuilder = new StringBuilder(500);
		for(final String tag : this.commandContext.getChessTagModder().getWritableTags())
		{
			//noinspection MagicCharacter
			outputBuilder.append('\n').append(tag);
		}
		return outputBuilder.toString();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "TagCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}
}
