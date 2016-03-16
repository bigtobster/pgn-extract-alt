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

import com.bigtobster.pgnextractalt.chess.ChessFilterer;
import com.bigtobster.pgnextractalt.chess.ChessIO;
import com.bigtobster.pgnextractalt.chess.ChessTagModder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Chess Context for PGN-Extract-Alt Essentially an object containing useful runtime chess stuff Should only be accessible to Class in chess package
 * Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
class CommandContext
{
	/**
	 * Message on failure to insert tags
	 */
	static final         String FAILED_TO_INSERT_TAGS       = "Failed to insert tags.";
	/**
	 * The subcommand for all filter commands
	 */
	static final         String FILTER_SUBCOMMAND           = "-filter";
	/**
	 * Message for user to notify developer
	 */
	static final         String NOTIFY_DEV                  = "Please notify PGN-Extract-Alt Developer";
	/**
	 * Partial Message on successfully filtering out games
	 */
	static final         String SUCCESSFULLY_FILTERED_GAMES = "games filtered";
	/**
	 * The message substring on successfully inserting tags
	 */
	static final         String SUCCESSFULLY_INSERTED_TAGS  = "Successfully inserted tags!";
	/**
	 * The message tail substring on successfully inserting tags
	 */
	static final         String TAGS_INSERTED               = "tags inserted.";
	/**
	 * Message for user that something bad has happened
	 */
	static final         String UNKNOWN_IMPORT_ERROR        = "Unknown Import Error ";
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER                      = Logger.getLogger(CommandContext.class.getName());
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessFilterer  chessFilterer;
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessIO        chessIO;
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessTagModder chessTagModder;

	/**
	 * Takes a severe IO error, forms a coherent bundle of failure data and reports to user
	 *
	 * @param exception      The exception that's caused the issue
	 * @param failureDetails Further details of the failure
	 * @throws java.lang.Exception The original exception rethrown
	 */
	@SuppressWarnings("ProhibitedExceptionDeclared")
	static void handleAndThrowSevereError(final Exception exception, final String failureDetails) throws Exception
	{
		CommandContext.logSevereError(CommandContext.LOGGER, failureDetails, exception);
		//noinspection ProhibitedExceptionThrown
		throw exception;
	}

	/**
	 * Logs a severe error in logs
	 *
	 * @param logger    The erring class's Logger
	 * @param message   The message to be logged
	 * @param exception The exception causing the error
	 */
	private static void logSevereError(@SuppressWarnings("SameParameterValue") final Logger logger, final String message, final Exception exception)
	{
		logger.log(Level.SEVERE, message);
		logger.log(Level.SEVERE, exception.getMessage());
		logger.log(Level.SEVERE, exception.toString(), exception.fillInStackTrace());
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "CommandContext{" +
			   "chessIO=" + this.chessIO +
			   '}';
	}

	/**
	 * Getter for an Autowired ChessFilterer instance
	 *
	 * @return the ChessFilterer instance
	 */
	ChessFilterer getChessFilterer()
	{
		return this.chessFilterer;
	}

	/**
	 * Getter for an Autowired ChessIO instance
	 *
	 * @return the ChessIO instance
	 */
	ChessIO getChessIO()
	{
		return this.chessIO;
	}

	/**
	 * Getter for an Autowired ChessTagModder instance
	 *
	 * @return ChessTagModder the ChessTagModder instance
	 */
	ChessTagModder getChessTagModder()
	{
		return this.chessTagModder;
	}
}
