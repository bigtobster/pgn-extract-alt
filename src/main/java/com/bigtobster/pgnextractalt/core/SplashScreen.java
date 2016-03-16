/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.core;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Splash Screen which displays on start of PGN-Extract-Alt
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"RefusedBequest", "ClassUnconnectedToPackage", "ClassIndependentOfModule"})
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SplashScreen extends DefaultBannerProvider
{

	private static final String PGNEXTRACTALT_DESCRIPTION = "                                           A modular PGN processing application";
	private static final String PGNEXTRACTALT_URL         = "                                  https://github.com/bigtobster/pgn-extract-alt";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String PROPERTIES_FILE           = "pgn-extract-alt.properties";
	private static final String PROPERTIES_FILE_NOT_FOUND = "pgn-extract-alt.properties file not found";
	private static final String VERSION                   = "Version: ";
	private static final String WELCOME                   = "Welcome to PGN-Extract-Alt. For assistance, type \"help\" then hit ENTER.";
	private final        Logger logger                    = Logger.getLogger(this.getClass().getName());

	@Override
	public String getBanner()
	{
		//noinspection StringBufferReplaceableByString
		final StringBuilder buf = new StringBuilder(250);
		buf.append(OsUtils.LINE_SEPARATOR);
		buf.append(OsUtils.LINE_SEPARATOR);
		buf.append("______ _____  _   _        _____     _                  _           ___  _ _   ");
		buf.append(OsUtils.LINE_SEPARATOR);
		buf.append("| ___ \\  __ \\| \\ | |      |  ___|   | |                | |         / _ \\| | |  ").append(OsUtils.LINE_SEPARATOR);
		buf.append("| |_/ / |  \\/|  \\| |______| |____  _| |_ _ __ __ _  ___| |_ ______/ /_\\ \\ | |_ ").append(OsUtils.LINE_SEPARATOR);
		buf.append("|  __/| | __ | . ` |______|  __\\ \\/ / __| '__/ _` |/ __| __|______|  _  | | __|").append(OsUtils.LINE_SEPARATOR);
		buf.append("| |   | |_\\ \\| |\\  |      | |___>  <| |_| | | (_| | (__| |_       | | | | | |_ ").append(OsUtils.LINE_SEPARATOR);
		buf.append("\\_|    \\____/\\_| \\_/      \\____/_/\\_\\\\__|_|  \\__,_|\\___|\\__|      \\_| |_/_|\\__|").append(OsUtils.LINE_SEPARATOR);
		buf.append(OsUtils.LINE_SEPARATOR);
		buf.append(SplashScreen.PGNEXTRACTALT_DESCRIPTION).append(OsUtils.LINE_SEPARATOR);
		buf.append(SplashScreen.PGNEXTRACTALT_URL).append(OsUtils.LINE_SEPARATOR);
		buf.append(OsUtils.LINE_SEPARATOR);
		buf.append(OsUtils.LINE_SEPARATOR);
		buf.append(SplashScreen.VERSION).append(this.getVersion());
		return buf.toString();
	}

	@Override
	public String getVersion()
	{

		final Properties properties = new Properties();
		InputStream resourceStream = null;
		String version = null;

		try
		{
			final String filename = SplashScreen.PROPERTIES_FILE;
			resourceStream = SplashScreen.class.getClassLoader().getResourceAsStream(filename);
			if(resourceStream == null)
			{
				this.logger.log(Level.WARNING, SplashScreen.PROPERTIES_FILE_NOT_FOUND);
				return SplashScreen.PROPERTIES_FILE_NOT_FOUND;
			}
			properties.load(resourceStream);
			version = properties.getProperty("pgn-extract-alt.version");
		}
		catch(final IOException ex)
		{
			this.logger.log(Level.WARNING, ex.getMessage());
		}
		finally
		{
			if(resourceStream != null)
			{
				try
				{
					resourceStream.close();
				}
				catch(final IOException ex)
				{
					this.logger.log(Level.SEVERE, ex.getMessage());
				}
			}
		}
		return version;
	}

	@SuppressWarnings("MethodReturnAlwaysConstant")
	@Override
	public String getWelcomeMessage()
	{
		return SplashScreen.WELCOME;
	}
}