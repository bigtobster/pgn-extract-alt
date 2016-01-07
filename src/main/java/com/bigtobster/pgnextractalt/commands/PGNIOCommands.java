package com.bigtobster.pgnextractalt.commands;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

/**
 * Created by Toby Leheup on 06/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster) Controls Input and Output of PGN files
 */
@Component
public class PGNIOCommands implements CommandMarker
{
	private static final String  EXPORT_COMMAND      = "export";
	private static final String  EXPORT_COMMAND_HELP = "Export loaded data as a PGN file. Available on successful import.";
	private static final String  FAILED_EXPORT       = "Failed to export \"PGN file \"FILENAME\"";
	private static final String  FAILED_IMPORT       = "Failed to import \"PGN file \"FILENAME\"";
	private static final String  IMPORT_COMMAND      = "import";
	private static final String  IMPORT_COMMAND_HELP = "Import a PGN file for processing";
	private static final String  SUCCESSFUL_EXPORT   = "PGN file \"FILENAME\" successfully exported";
	private static final String  SUCCESSFUL_IMPORT   = "PGN file \"FILENAME\" successfully imported";
	private              boolean is_pgn_imported     = false;

	/**
	 * Attempt to export a PGN file
	 *
	 * @return Successful export of PGN file
	 */
	@CliCommand(value = PGNIOCommands.EXPORT_COMMAND, help = PGNIOCommands.EXPORT_COMMAND_HELP)
	public static String exportPGN()
	{
		//ConstantConditions,ConstantIfStatement
		if(true)
		{
			return PGNIOCommands.SUCCESSFUL_EXPORT;
		}
		return PGNIOCommands.FAILED_EXPORT;
	}

	/**
	 * Getter for Export Command String
	 *
	 * @return String Export Command
	 */
	@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
	public static String getExportCommand()
	{
		return PGNIOCommands.EXPORT_COMMAND;
	}

	/**
	 * Getter for Import Command String
	 *
	 * @return String Import Command
	 */
	@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
	public static String getImportCommand()
	{
		return PGNIOCommands.IMPORT_COMMAND;
	}

	/**
	 * Describes when "import" command is available
	 *
	 * @return boolean Availability (always available)
	 */
	@CliAvailabilityIndicator({PGNIOCommands.IMPORT_COMMAND})
	public static boolean isImportAvailable()
	{
		//always available
		return true;
	}

	/**
	 * Attempt to import a PGN file
	 *
	 * @return Successful import of PGN file
	 */
	@CliCommand(value = PGNIOCommands.IMPORT_COMMAND, help = PGNIOCommands.IMPORT_COMMAND_HELP)
	public String importPGN()
	{
		this.is_pgn_imported = true;
		//noinspection ConstantConditions
		if(this.is_pgn_imported)
		{
			return PGNIOCommands.SUCCESSFUL_IMPORT;
		}
		return PGNIOCommands.FAILED_IMPORT;
	}

	/**
	 * Describes when "export" command is available
	 *
	 * @return boolean Available on confirmed PGN import
	 */
	@CliAvailabilityIndicator({PGNIOCommands.EXPORT_COMMAND})
	public boolean isExportAvailable()
	{
		// Available on confirmed PGN import
		return this.is_pgn_imported;
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter"})
	@Override
	public String toString()
	{
		return "PGNIOCommands{" +
			   "is_pgn_imported=" + this.is_pgn_imported +
			   '}';
	}
}
