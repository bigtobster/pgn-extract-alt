package com.bigtobster.pgnextractalt.commands;

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
	 * Message on failure to insert tags
	 */
	static final         String FAILED_TO_INSERT_TAGS          = "Failed to insert tags.";
	/**
	 * The force option
	 */
	static final         String FORCE                          = "Force";
	/**
	 * Info to user that key has already been fully used
	 */
	static final         String KEY_ALREADY_USED               = "Key already used by all games. Using --Force to overwrite.";
	/**
	 * The message substring on successfully inserting tags
	 */
	static final         String SUCCESSFULLY_INSERTED_TAGS     = "Successfully inserted tags!";
	/**
	 * The message tail substring on successfully inserting tags
	 */
	static final         String TAGS_INSERTED                  = "tags inserted.";
	/**
	 * The TagKey option
	 */
	static final         String TAG_KEY                        = "TagKey";
	/**
	 * The TagValue option
	 */
	static final         String TAG_VALUE                      = "TagValue";
	private static final String INSERT_CUSTOM_TAG_COMMAND      = "insert-custom-tag";
	private static final String INSERT_CUSTOM_TAG_COMMAND_HELP =
			"Insert a custom tag into list of games. Available when games imported. New tag " +
			"must be a recognised PGN tag.";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                         = Logger.getLogger(TagCommands.class.getName());
	private static final char   SPACE                          = ' ';
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	/**
	 * Getter for Insert Tag Command String
	 *
	 * @return String Insert Tag Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getInsertCustomTagCommand()
	{
		return TagCommands.INSERT_CUSTOM_TAG_COMMAND;
	}

	/**
	 * Handle the interface for inserting a custom tag into a list of games
	 *
	 * @param tagKey      The key of the tag to be inserted
	 * @param tagValue    The value of the tag to be inserted
	 * @param forceInsert Whether to overwrite existing tags of that key
	 * @return Success message
	 */
	@SuppressWarnings("BooleanParameter")
	@CliCommand(value = TagCommands.INSERT_CUSTOM_TAG_COMMAND, help = TagCommands.INSERT_CUSTOM_TAG_COMMAND_HELP)
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
		final int tagsInsertedNo = this.commandContext.getChessTagModder().insertCustomTag(tagKey, tagValue, forceInsert);
		if(tagsInsertedNo == 0)
		{
			return TagCommands.FAILED_TO_INSERT_TAGS + TagCommands.SPACE + TagCommands.KEY_ALREADY_USED;
		}
		return TagCommands.SUCCESSFULLY_INSERTED_TAGS + TagCommands.SPACE + tagsInsertedNo + TagCommands.SPACE + TagCommands.TAGS_INSERTED;
	}

	/**
	 * Describes when "Insert Tag" command is available
	 *
	 * @return boolean Availability (Available on successful import of at least 1 game)
	 */
	@CliAvailabilityIndicator({TagCommands.INSERT_CUSTOM_TAG_COMMAND})
	public boolean isInsertCustomTagAvailable()
	{
		return this.commandContext.getChessIO().isPGNImported();
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
