/*
 * Copyright (c) 2016 Toby Leheup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bigtobster.pgnextractalt.filters;

/**
 * Enumerated type of modes for the Duplicate Filter. Created by Toby Leheup on 17/03/16 for pgn-extract-alt.
 *
 * @author Toby Leheup (Bigtobster)
 */
public enum DuplicateFilterMode
{
	/**
	 * Filters out the duplicates of the list of games leaving a set of unique games
	 */
	FILTER,
	/**
	 * Removes all non duplicated games leaving a set of games which are all duplicated (including the duplicates)
	 */
	ISOLATE,
	/**
	 * Filters out all duplicated games. The only games that remain are those that were unique before the filtering.
	 */
	PURGE
}
