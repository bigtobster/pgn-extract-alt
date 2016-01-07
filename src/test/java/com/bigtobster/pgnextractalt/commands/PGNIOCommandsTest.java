package com.bigtobster.pgnextractalt.commands;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

/**
 * Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster) Tests the Import/Export of PGNs
 */
@SuppressWarnings("UnusedDeclaration")
public class PGNIOCommandsTest
{
	/**
	 * Tests Export functionality after Import
	 */
	@Test
	public void exportAfterImportTest()
	{
		final JLineShellComponent shell = new Bootstrap().getJLineShellComponent();
		//Execute command
		final CommandResult cr = shell.executeCommand(PGNIOCommands.getImportCommand());
		//Get result
		Assert.assertNotNull("Export return is null", cr.getResult().toString());
		shell.stop();
	}

	/**
	 * Tests Export functionality before Import
	 */
	@Test
	public void exportBeforeImportTest()
	{
		final JLineShellComponent shell = new Bootstrap().getJLineShellComponent();
		//Execute command
		final CommandResult cr = shell.executeCommand(PGNIOCommands.getExportCommand());
		//Get result
		Assert.assertNull("Export should not be available until after import", cr.getResult());
		shell.stop();
	}

	/**
	 * Tests Import functionality
	 */
	@Test
	public void importTest()
	{
		final JLineShellComponent shell = new Bootstrap().getJLineShellComponent();
		//Execute command
		final CommandResult cr = shell.executeCommand(PGNIOCommands.getImportCommand());
		//Get result
		Assert.assertNotNull("Import return is null", cr.getResult().toString());
		shell.stop();
	}
}
