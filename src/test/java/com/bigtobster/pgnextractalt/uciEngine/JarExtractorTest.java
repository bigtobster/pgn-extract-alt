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

import com.bigtobster.pgnextractalt.misc.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.ZipException;

/**
 * Tests JarExtractor and ensures the Jar Extraction is correct.
 * Created by Toby Leheup on 03/03/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public class JarExtractorTest
{
	private static final String NEW_LINE             = "\n";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String TEST_PROPERTIES_FILE = "pgn-extract-alt.properties";

	/**
	 * Tests that getFile is performing as expected
	 */
	@Test
	public void getFileTest()
	{
		File jarFile = null;
		try
		{
			final URI jarURI = JarExtractor.getJarURI();
			jarFile = JarExtractor.getFile(jarURI, JarExtractorTest.TEST_PROPERTIES_FILE);
		}
		catch(final URISyntaxException e)
		{
			Assert.fail(e.getMessage() + JarExtractorTest.NEW_LINE + e);
		}
		catch(final ZipException e)
		{
			Assert.fail(e.getMessage() + JarExtractorTest.NEW_LINE + e);
		}
		catch(final IOException e)
		{
			Assert.fail(e.getMessage() + JarExtractorTest.NEW_LINE + e);
		}
		final String path = File.separator + JarExtractorTest.TEST_PROPERTIES_FILE;
		final String testLicencePath = TestContext.class.getResource(path).getPath();
		final File testLicenceFile = new File(testLicencePath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, testLicencePath);
		Assert.assertNotNull(TestContext.TEST_RESOURCE_NOT_FOUND, testLicenceFile);
		Assert.assertEquals("Extracted Licence should be equal to Test Licence", testLicenceFile, jarFile);
	}

	/**
	 * Tests that getJarURI is performing as expected
	 *
	 * @throws java.net.URISyntaxException Thrown on failure to get item from JAR
	 */
	@Test
	public void getJarURITest() throws URISyntaxException
	{
		final URI jarURI = JarExtractor.getJarURI();
		final File jarFile = new File(jarURI.getPath());
		//This resolves to a classes folder - but as long as it works...
		Assert.assertTrue("Detected JAR should exist!", jarFile.exists());
	}
}
