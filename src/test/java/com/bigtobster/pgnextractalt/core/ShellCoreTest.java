package com.bigtobster.pgnextractalt.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.JLineShellComponent;

/**
 * Created by Toby Leheup on 07/01/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster) Tests the Initialisation of the shell
 */
@SuppressWarnings("UnusedDeclaration")
public class ShellCoreTest
{
	/**
	 * Tests the Shell initialises correctly
	 */
	@Test
	public void shellInitTest()
	{
		JLineShellComponent shell = null;
		try
		{
			shell = new Bootstrap().getJLineShellComponent();
			shell.start();
		}
		catch(final Exception ignored)
		{
			Assert.fail("Failure in Shell initialisation");
		}
		finally
		{
			assert shell != null;
			shell.stop();
		}
	}

	/**
	 * Tests the Shell stops correctly
	 */
	@Test
	public void shellStopTest()
	{
		final JLineShellComponent shell = new Bootstrap().getJLineShellComponent();
		shell.start();
		try
		{
			shell.stop();
		}
		catch(final Exception ignored)
		{
			Assert.fail("Failure in Shell stop");
		}
	}
}
