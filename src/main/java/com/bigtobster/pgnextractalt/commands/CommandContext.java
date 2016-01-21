package com.bigtobster.pgnextractalt.commands;

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
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(CommandContext.class.getName());

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessIO chessIO;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private ChessTagModder chessTagModder;

	/**
	 * Logs a severe error in logs
	 *
	 * @param logger    The erring class's Logger
	 * @param message   The message to be logged
	 * @param exception The exception causing the error
	 */
	@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
	static void logSevereError(final Logger logger, final String message, final Exception exception)
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
