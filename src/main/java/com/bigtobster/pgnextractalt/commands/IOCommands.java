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

import chesspresso.pgn.PGNSyntaxError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

import javax.activation.UnsupportedDataTypeException;
import java.io.*;
import java.util.logging.Logger;

/**
 * Spring Shell Command class for PGN (Import and Export) Created by Toby Leheup on 06/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster) Controls Input and Output of PGN files
 */
@Component
public class IOCommands implements CommandMarker
{
	/**
	 * The substring in console on any import failure
	 */
	static final         String FAILED_EXPORT        = "Failed to export to PGN!";
	/**
	 * The substring in console on any import failure
	 */
	static final         String FAILED_IMPORT        = "Failed to import PGN!";
	/**
	 * The string for the FilePath option of Import
	 */
	static final         String FILE_PATH_OPTION     = "FilePath";
	/**
	 * The substring for successful export and count of number of imported games
	 */
	static final         String GAMES_EXPORTED       = "games exported";
	/**
	 * The substring for successful import and count of number of imported games
	 */
	static final         String GAMES_IMPORTED       = "games imported";
	/**
	 * Message on PGN parse syntax failure
	 */
	static final         String INVALID_SYNTAX       = "PGN file has invalid syntax";
	/**
	 * The substring in console on input not being PGN
	 */
	static final         String NOT_A_PGN_FILE       = "Input is not a PGN file!";
	/**
	 * The substring in console on file not found error
	 */
	static final         String NO_FILE_AT           = "No file at";
	/**
	 * The substring for importing a PGN with no read permission
	 */
	static final         String PGN_NOT_READABLE     = "PGN file not readable. Check file permissions at";
	/**
	 * The substring for export  a PGN with no read or write permission
	 */
	static final         String PGN_NOT_WRITABLE     = "PGN file is either not readable or not writable. Check file permissions at";
	/**
	 * The substring in console on any successful export
	 */
	static final         String SUCCESSFUL_EXPORT    = "Successfully exported PGN file!";
	/**
	 * The substring in console on any successful import
	 */
	static final         String SUCCESSFUL_IMPORT    = "Successfully imported PGN file!";
	/**
	 * The string in console on successful reset of PGN-Extract-Alt
	 */
	static final         String SUCCESSFUL_RESET     = "PGN-Extract-Alt Successfully Reset!";
	private static final String CANNOT_CREATE_FILE   = "Cannot write to file at";
	private static final String EXPORT_COMMAND       = "export";
	private static final String EXPORT_COMMAND_HELP  = "Export loaded data as a PGN file. Available on successful import.";
	private static final String IMPORT_COMMAND       = "import";
	private static final String IMPORT_COMMAND_HELP  = "Import a PGN file for processing";
	private static final Logger LOGGER               = Logger.getLogger(IOCommands.class.getName());
	private static final String NOTIFY_DEV           = "Please notify PGN-Extract-Alt Developer";
	private static final String NO_CHESS_GAMES       = "Imported file appears to contain 0 chess games";
	private static final String RESET_COMMAND        = "reset";
	private static final String RESET_COMMAND_HELP   = "Reset PGN-Extract-Alt - WARNING: Will lose all changes. Available on successful import.";
	private static final char   SPACE                = ' ';
	private static final String UNKNOWN_IMPORT_ERROR = "Unknown Import Error ";
	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@Autowired
	private CommandContext commandContext;

	/**
	 * Getter for Export Command String
	 *
	 * @return String Export Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getExportCommand()
	{
		return IOCommands.EXPORT_COMMAND;
	}

	/**
	 * Getter for Import Command String
	 *
	 * @return String Import Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getImportCommand()
	{
		return IOCommands.IMPORT_COMMAND;
	}

	/**
	 * Getter for Reset Command String
	 *
	 * @return String Reset Command
	 */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "MethodReturnAlwaysConstant"})
	public static String getResetCommand()
	{
		return IOCommands.RESET_COMMAND;
	}

	/**
	 * Describes when "import" command is available
	 *
	 * @return boolean Availability (always available)
	 */
	@SuppressWarnings({"MethodReturnAlwaysConstant", "SameReturnValue"})
	@CliAvailabilityIndicator({IOCommands.IMPORT_COMMAND})
	public static boolean isImportAvailable()
	{
		//always available
		return true;
	}

	/**
	 * Attempt to export a PGN file
	 *
	 * @param file File to be imported
	 * @return Successful export of PGN file
	 */
	@CliCommand(value = IOCommands.EXPORT_COMMAND, help = IOCommands.EXPORT_COMMAND_HELP)
	public String exportPGN(
			@CliOption(key = {IOCommands.FILE_PATH_OPTION}, help = "Path (including file name) for exported PGN. File will be " +
																   "created if it doesn't exist.", mandatory = true) final File file
						   )
	{
		final PrintWriter printWriter;
		String failureDetails = null;
		final String filePath = file.getAbsolutePath();
		try
		{
			if(! file.exists())
			{
				//noinspection ResultOfMethodCallIgnored
				file.getParentFile().mkdirs();
				//noinspection ResultOfMethodCallIgnored
				file.createNewFile();
			}
			if(! file.canRead() || ! file.canWrite())
			{
				//noinspection ThrowCaughtLocally
				throw new InvalidObjectException(IOCommands.PGN_NOT_WRITABLE + IOCommands.SPACE + filePath);
			}
			printWriter = new PrintWriter(file);
			try
			{
				this.commandContext.getChessIO().exportPGN(printWriter);
			}
			finally
			{
				printWriter.close();
			}
		}
		catch(final FileNotFoundException ignored)
		{
			failureDetails = IOCommands.CANNOT_CREATE_FILE + IOCommands.SPACE + filePath;
		}
		catch(final InvalidObjectException ioe)
		{
			failureDetails = ioe.getMessage();
		}
		catch(final IOException ignored)
		{
			failureDetails = IOCommands.CANNOT_CREATE_FILE + IOCommands.SPACE + filePath;
		}
		if(failureDetails == null)
		{
			return IOCommands.SUCCESSFUL_EXPORT
				   + IOCommands.SPACE
				   + this.commandContext.getChessIO().getGames().size()
				   + IOCommands.SPACE
				   + IOCommands.GAMES_EXPORTED;
		}
		return IOCommands.FAILED_EXPORT + IOCommands.SPACE + failureDetails;
	}

	/**
	 * Attempt to import a PGN file
	 *
	 * @param file The file to be imported
	 * @return Successful import of PGN file
	 * @throws java.io.IOException Crashes on unknown failure to open PGN file
	 */
	@SuppressWarnings("FeatureEnvy")
	@CliCommand(value = IOCommands.IMPORT_COMMAND, help = IOCommands.IMPORT_COMMAND_HELP)
	public String importPGN(
			@CliOption(key = {"", IOCommands.FILE_PATH_OPTION}, help = "Path to the PGN file to be imported", mandatory = true) final File file
						   ) throws IOException
	{
		final FileInputStream fileInputStream;
		String failureDetails = null;
		final String filePath = file.getPath();
		try
		{
			if(! file.canRead() && file.exists())
			{
				//noinspection ThrowCaughtLocally
				throw new InvalidObjectException(IOCommands.PGN_NOT_READABLE + IOCommands.SPACE + filePath);
			}
			fileInputStream = new FileInputStream(file);
			try
			{
				this.commandContext.getChessIO().importPGN(fileInputStream, filePath);
			}
			catch(final UnsupportedDataTypeException ignored)
			{
				failureDetails = IOCommands.NOT_A_PGN_FILE;
			}
			catch(final IOException ioe)
			{
				this.handleAndThrowIOPGNError(ioe);
			}
			catch(final PGNSyntaxError ignored)
			{
				failureDetails = IOCommands.INVALID_SYNTAX;
			}
			finally
			{
				try
				{
					fileInputStream.close();
				}
				catch(final IOException ioe)
				{
					this.handleAndThrowIOPGNError(ioe);
				}
			}
		}
		catch(final FileNotFoundException ignored)
		{
			failureDetails = IOCommands.NO_FILE_AT + IOCommands.SPACE + filePath;
		}
		catch(final InvalidObjectException ioe)
		{
			failureDetails = ioe.getMessage();
		}
		if(this.commandContext.getChessIO().getGames().size() < 0)
		{
			failureDetails = IOCommands.NO_CHESS_GAMES;
		}
		if(failureDetails == null)
		{
			return IOCommands.SUCCESSFUL_IMPORT
				   + IOCommands.SPACE
				   + this.commandContext.getChessIO().getGames().size()
				   + IOCommands.SPACE
				   + IOCommands.GAMES_IMPORTED;
		}
		return IOCommands.FAILED_IMPORT + IOCommands.SPACE + failureDetails;
	}

	/**
	 * Describes when "export" command is available
	 *
	 * @return boolean Available on confirmed PGN import
	 */
	@CliAvailabilityIndicator({IOCommands.EXPORT_COMMAND})
	public boolean isExportAvailable()
	{
		// Available on confirmed PGN import
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Describes when "reset" command is available
	 *
	 * @return boolean Available on confirmed PGN import
	 */
	@CliAvailabilityIndicator({IOCommands.RESET_COMMAND})
	public boolean isResetAvailable()
	{
		// Available on confirmed PGN import
		return this.commandContext.getChessIO().isPGNImported();
	}

	/**
	 * Reset PGN-Extract-Alt
	 *
	 * @return Notice that application reset
	 */
	@CliCommand(value = IOCommands.RESET_COMMAND, help = IOCommands.RESET_COMMAND_HELP)
	public String reset()
	{
		this.commandContext.getChessIO().reset();
		return IOCommands.SUCCESSFUL_RESET;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "IOCommands{" +
			   this.commandContext +
			   '}';
	}

	@SuppressWarnings("MethodMayBeStatic")
	private void handleAndThrowIOPGNError(final IOException ioException) throws IOException
	{
		final String failureDetails = IOCommands.UNKNOWN_IMPORT_ERROR + OsUtils.LINE_SEPARATOR + IOCommands.NOTIFY_DEV;
		CommandContext.logSevereError(IOCommands.LOGGER, failureDetails, ioException);
		throw ioException;
	}
}
