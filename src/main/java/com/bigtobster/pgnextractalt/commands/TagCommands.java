/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
	 * The force option
	 */
	@SuppressWarnings("DuplicateStringLiteralInspection")
	static final         String FORCE                           = "Force";
	/**
	 * Info to user that key has already been fully used
	 */
	static final         String KEY_ALREADY_USED                = "Key already used by all games. Using --Force to overwrite.";
	/**
	 * The TagKey option
	 */
	static final         String TAG_KEY                         = "TagKey";
	/**
	 * The TagValue option
	 */
	static final         String TAG_VALUE                       = "TagValue";
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
	 * Handle the interface for inserting a tag into a list of games
	 *
	 * @param tagKey      The key of the tag to be inserted
	 * @param tagValue    The value of the tag to be inserted
	 * @param forceInsert Whether to overwrite existing tags of that key
	 * @return Success message
	 */
	@SuppressWarnings({"BooleanParameter", "DuplicateStringLiteralInspection"})
	@CliCommand(value = TagCommands.INSERT_TAG_COMMAND, help = TagCommands.INSERT_TAG_COMMAND_HELP)
	public String insertTag(
			@CliOption(key = {TagCommands.TAG_KEY}, mandatory = true, help = "The key of the tag to be inserted.")
			final String tagKey,
			@CliOption(key = {TagCommands.TAG_VALUE}, mandatory = true, help = "The value of the tag to be inserted.")
			final String tagValue,
			@SuppressWarnings("DuplicateStringLiteralInspection") @CliOption(key = {TagCommands.FORCE},
																			 mandatory = false,
																			 help = "\"true\" to overwrite any existing tags with this key",
																			 unspecifiedDefaultValue = "false")
			final boolean forceInsert
						   )
	{
		final int tagsInsertedNo = this.commandContext.getChessTagModder().insertTag(tagKey, tagValue, forceInsert);
		if(tagsInsertedNo == 0)
		{
			return CommandContext.FAILED_TO_INSERT_TAGS + TagCommands.SPACE + TagCommands.KEY_ALREADY_USED;
		}
		return CommandContext.SUCCESSFULLY_INSERTED_TAGS + TagCommands.SPACE + tagsInsertedNo + TagCommands.SPACE + CommandContext.TAGS_INSERTED;
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

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter", "DuplicateStringLiteralInspection"})
	@Override
	public String toString()
	{
		return "TagCommands{" +
			   "commandContext=" + this.commandContext +
			   '}';
	}
}