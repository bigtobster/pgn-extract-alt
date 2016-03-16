/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.uciEngine;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Extracts files from a JAR and then marks them to be cleaned up Essentially works as a per-instance installer Created by Toby Leheup on 15/02/16 for
 * pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 * @author https://stackoverflow.com/questions/600146/run-exe-which-is-packaged-inside-jar Code originally from stackoverflow post above
 */
final class JarExtractor
{
	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = Logger.getLogger(JarExtractor.class.getName());

	private static void close(final Closeable stream) throws IOException
	{
		if(stream != null)
		{
			stream.close();
		}
	}

	private static URI extract(
			final ZipFile zipFile,
			final String fileName
							  )
			throws IOException
	{
		final File tempFile;
		final ZipEntry entry;
		final InputStream zipStream;
		OutputStream fileStream = null;
		tempFile = File.createTempFile(fileName, Long.toString(System.currentTimeMillis()));
		tempFile.deleteOnExit();
		entry = zipFile.getEntry(fileName);

		if(entry == null)
		{
			throw new FileNotFoundException("cannot find file: " + fileName + " in archive: " + zipFile.getName());
		}

		zipStream = zipFile.getInputStream(entry);

		try
		{
			final byte[] buf;
			int i;

			//noinspection IOResourceOpenedButNotSafelyClosed
			fileStream = new FileOutputStream(tempFile);
			//noinspection MagicNumber
			buf = new byte[1024];
			i = zipStream.read(buf);

			while(i != - 1)
			{
				fileStream.write(buf, 0, i);
				i = zipStream.read(buf);
			}
		}
		finally
		{
			JarExtractor.close(zipStream);
			JarExtractor.close(fileStream);
		}

		return tempFile.toURI();
	}

	/**
	 * Gets URI for a resource located in a URI
	 *
	 * @param where    The containing URI
	 * @param fileName The filename of the resource to be found
	 * @return The URI found
	 * @throws ZipException Issue on unzipping JAR file
	 * @throws IOException  Issue on finding JAR file or writing to disk
	 */
	static File getFile(
			final URI where,
			final String fileName
					   )
			throws ZipException,
				   IOException
	{
		final File location;
		final URI fileURI;

		location = new File(where);

		// not in a JAR, just return the path on disk
		if(location.isDirectory())
		{
			fileURI = URI.create(where + fileName);
		}
		else
		{
			final ZipFile zipFile;

			zipFile = new ZipFile(location);
			try
			{
				fileURI = JarExtractor.extract(zipFile, fileName);
			}
			finally
			{
				zipFile.close();
			}
		}
		return new File(fileURI);
	}

	/**
	 * Gets a URI for the current JAR
	 *
	 * @return The URI for the current JAR
	 * @throws URISyntaxException Thrown on issue converting between URL and URI
	 */
	static URI getJarURI()
			throws URISyntaxException
	{
		final ProtectionDomain domain;
		final CodeSource source;
		final URL url;
		final URI uri;

		domain = JarExtractor.class.getProtectionDomain();
		source = domain.getCodeSource();
		url = source.getLocation();
		uri = url.toURI();

		return uri;
	}
}
