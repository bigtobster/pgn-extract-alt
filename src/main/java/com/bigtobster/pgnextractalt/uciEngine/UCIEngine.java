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

import javax.naming.OperationNotSupportedException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A simple and efficient client to run a UCI Chess engine from Java Derived from the implementation in Chess-misc
 * (https://github.com/rahular/chess-misc)
 *
 * @author Rahul A R (https://github.com/rahular)
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassWithTooManyMethods"})
public class UCIEngine
{
	private static final String         BESTMOVE_UCI             = "bestmove";
	private static final String         CP_UCI                   = "cp";
	private static final String         DEBUG_TERMINATION_STRING = "Checkers:";
	private static final String         DEBUG_UCI                = "d";
	private static final long           DEFAULT_THREAD_SLEEP     = 10L;
	private static final String         DEPTH_UCI                = "depth";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final String         FALSE_UCI                = "false";
	private static final String         FEN_DEBUG_MARKER         = "Fen:";
	private static final String         FEN_UCI                  = "fen";
	private static final String         GO_UCI                   = "go";
	private static final String         HASH_UCI                 = "hash";
	private static final String         INFO_UCI                 = "info";
	private static final int            INIT_BUFFER_SIZE         = 100000;
	private static final int            LINES_OF_ANALYSIS        = 1;
	private static final Logger         LOGGER                   = Logger.getLogger(UCIEngine.class.getName());
	private static final String         MATE_UCI                 = "mate";
	private static final String         MULTIPV_UCI              = "multipv";
	private static final String         NAME_UCI                 = "name";
	private static final String         NEW_LINE                 = "\n";
	private static final Pattern        NEW_LINE_PATTERN         = Pattern.compile(UCIEngine.NEW_LINE);
	private static final String         NODES_UCI                = "nodes";
	private static final String         PONDER_UCI               = "ponder";
	private static final String         POSITION_UCI             = "position";
	private static final String         QUIT_UCI                 = "quit";
	private static final String         SCORE_UCI                = "score";
	private static final String         SEARCHMOVES_UCI          = "searchmoves";
	private static final String         SETOPTION_UCI            = "setoption";
	private static final String         SPACE                    = " ";
	private static final Pattern        BEST_MOVE_PATTERN        = Pattern.compile(UCIEngine.BESTMOVE_UCI + UCIEngine.SPACE);
	private static final Pattern        NODES_PATTERN            = Pattern.compile(UCIEngine.SPACE + UCIEngine.NODES_UCI);
	private static final Pattern        SCORE_PATTERN            = Pattern.compile(
			UCIEngine.SCORE_UCI +
			UCIEngine.SPACE
																				  );
	private static final Pattern        SPACE_PATTERN            = Pattern.compile(UCIEngine.SPACE);
	private static final String         THREADS_UCI              = "threads";
	private static final String         UCINEWGAME_UCI           = "ucinewgame";
	private static final String         UPPERBOUND_UCI           = "upperbound";
	private static final Pattern        UPPERBOUND_NODES_PATTERN = Pattern.compile(
			UCIEngine.SPACE +
			UCIEngine.UPPERBOUND_UCI +
			UCIEngine.SPACE +
			UCIEngine.NODES_UCI
																				  );
	private static final int            USE_HASH_MB              = 1024;
	private static final int            USE_THREADS              = 4;
	private static final String         VALUE_UCI                = "value";
	private static final double         WAIT_MODIFIER            = 0.2;
	private              Process        engineProcess            = null;
	private              BufferedReader processReader            = null;
	private              BufferedWriter processWriter            = null;

	/**
	 * This function returns the best move for a given position after it has reached a depth of 'depth'. It will check that it has reached depth of
	 * 'depth' after 'waitTime' seconds.
	 *
	 * @param depth    The depth the engine will go to in the search for the best move
	 * @param waitTime The initial time the engine will wait before checking if analysis to depth 'depth' has been achieved
	 * @return Best Move in Long Algebraic Notation format
	 * @throws java.io.IOException Error on executing UCI command
	 */
	@SuppressWarnings("UnusedDeclaration")
	public String getBestMoveAlt(final int depth, final int waitTime) throws IOException
	{
		this.sendCommand(UCIEngine.GO_UCI + UCIEngine.SPACE + UCIEngine.DEPTH_UCI + UCIEngine.SPACE + depth);
		final String engineOutput = this.getMoveSearchOutput(waitTime);
		return UCIEngine.SPACE_PATTERN.split(UCIEngine.BEST_MOVE_PATTERN.split(engineOutput)[1])[0];
	}

	/**
	 * Get the evaluation score of a given move - does not actually execute the move
	 *
	 * @param lanMove  The move to be evaluated
	 * @param depth    The depth the engine will go to in the search for the best move
	 * @param waitTime in milliseconds
	 * @return evalScore The score of the move
	 * @throws java.io.IOException Error on executing UCI command
	 */
	@SuppressWarnings("UnusedDeclaration")
	public float getMoveScoreAlt(final String lanMove, final int depth, final int waitTime) throws IOException
	{
		this.sendCommand(
				UCIEngine.GO_UCI + UCIEngine.SPACE + UCIEngine.DEPTH_UCI + UCIEngine.SPACE + depth + UCIEngine.SPACE +
				UCIEngine.SEARCHMOVES_UCI + UCIEngine.SPACE + lanMove
						);
		final String engineOutput = this.getMoveSearchOutput(waitTime);

		float evalScore = 0.0f;
		final String[] dump = UCIEngine.NEW_LINE_PATTERN.split(engineOutput);
		@SuppressWarnings("BooleanVariableAlwaysNegated") boolean scoreCalculated = false;
		for(int i = dump.length - 1; (i >= 0) && ! scoreCalculated; i--)
		{
			if(dump[i].startsWith(UCIEngine.INFO_UCI + UCIEngine.SPACE + UCIEngine.DEPTH_UCI + UCIEngine.SPACE) &&
			   dump[i].contains(UCIEngine.SCORE_UCI))
			{
				try
				{
					final String scoreString = UCIEngine.NODES_PATTERN.split(UCIEngine.SCORE_PATTERN.split(dump[i])[1])[0];
					if(scoreString.contains(UCIEngine.MATE_UCI))
					{
						return - 1.0F;
					}
					evalScore = Float.parseFloat(scoreString.replace(UCIEngine.CP_UCI, "").trim());
					scoreCalculated = true;
				}
				catch(final NumberFormatException ignored)
				{
					final String scoreString = UCIEngine.UPPERBOUND_NODES_PATTERN.split(UCIEngine.SCORE_PATTERN.split(dump[i])[1])[0];
					if(scoreString.contains(UCIEngine.MATE_UCI))
					{
						return - 1.0F;
					}
					evalScore = Float.parseFloat(scoreString.replace(UCIEngine.CP_UCI, "").trim());
					scoreCalculated = true;
				}
			}
		}
		return Math.abs(evalScore);
	}

	/**
	 * Discards the current game and sets up a new board
	 *
	 * @throws java.io.IOException Error finding engine binary
	 */
	public void reset() throws IOException
	{
		this.sendCommand(UCIEngine.UCINEWGAME_UCI);
	}

	/**
	 * Extracts an engine out of a jar then runs the engine as a binary and initialises it
	 *
	 * @param fullEnginePath The path to the current UCI engine
	 * @throws javax.naming.OperationNotSupportedException Either architecture or operating system not supported
	 * @throws java.io.IOException                         Error finding engine binary
	 * @throws java.net.URISyntaxException                 Thrown on bad URI
	 */
	@SuppressWarnings({"UnusedDeclaration"})
	public void startEngine(final String fullEnginePath) throws OperationNotSupportedException, IOException, URISyntaxException
	{
		final URI uri = JarExtractor.getJarURI();
		final File engineBinary = JarExtractor.getFile(uri, fullEnginePath);
		//noinspection ResultOfMethodCallIgnored
		engineBinary.setExecutable(true);
		this.startEngine(engineBinary);

	}

	/**
	 * Stops UCIEngine and cleans up before closing it
	 *
	 * @throws java.io.IOException Error on executing UCI command
	 */
	@SuppressWarnings("UnusedDeclaration")
	public void stopEngine() throws IOException
	{
		this.sendCommand(UCIEngine.QUIT_UCI);
		this.processReader.close();
		this.processWriter.close();
		this.engineProcess.destroy();
	}

	@SuppressWarnings({"HardCodedStringLiteral", "MagicCharacter", "ObjectToString"})
	@Override
	public String toString()
	{
		return "UCIEngine{" +
			   "processReader=" + this.processReader +
			   ", processWriter=" + this.processWriter +
			   '}';
	}

	/**
	 * Gets the position of the current game
	 *
	 * @return String Fen of the current position
	 * @throws java.io.IOException Error on executing UCI command
	 */
	String getPosition() throws IOException
	{
		this.sendCommand(UCIEngine.DEBUG_UCI);
		final String debugOutput = this.getOutput(UCIEngine.DEBUG_TERMINATION_STRING);
		final String[] outputLines = UCIEngine.NEW_LINE_PATTERN.split(debugOutput);
		for(final String line : outputLines)
		{
			if(line.startsWith(UCIEngine.FEN_DEBUG_MARKER))
			{
				return UCIEngine.SPACE_PATTERN.split(line.replace(UCIEngine.FEN_DEBUG_MARKER, "").trim())[0];
			}
		}
		//Should never happen
		//noinspection ReturnOfNull
		return null;
	}

	/**
	 * Sets the current game into a new position
	 *
	 * @param fen The new position in FEN format
	 * @throws java.io.IOException Error on executing UCI command
	 */
	public void setPosition(final String fen) throws IOException
	{
		this.sendCommand(UCIEngine.POSITION_UCI + UCIEngine.SPACE + UCIEngine.FEN_UCI + UCIEngine.SPACE + fen);
	}

	/**
	 * Starts Engine as a process and initializes it
	 *
	 * @param engine The engine binary
	 * @throws IOException Error finding engine binary
	 */
	void startEngine(final File engine) throws IOException
	{
		this.engineProcess = new ProcessBuilder(engine.getPath()).start();
		this.processReader = new BufferedReader(new InputStreamReader(this.engineProcess.getInputStream()), UCIEngine.INIT_BUFFER_SIZE);
		this.processWriter = new BufferedWriter(new OutputStreamWriter(this.engineProcess.getOutputStream()), UCIEngine.INIT_BUFFER_SIZE);

		this.sendCommand(
				UCIEngine.SETOPTION_UCI + UCIEngine.SPACE +
				UCIEngine.NAME_UCI + UCIEngine.SPACE + UCIEngine.PONDER_UCI + UCIEngine.SPACE +
				UCIEngine.VALUE_UCI + UCIEngine.SPACE + UCIEngine.FALSE_UCI
						);
		this.sendCommand(
				UCIEngine.SETOPTION_UCI + UCIEngine.SPACE +
				UCIEngine.NAME_UCI + UCIEngine.SPACE + UCIEngine.MULTIPV_UCI + UCIEngine.SPACE +
				UCIEngine.VALUE_UCI + UCIEngine.SPACE + UCIEngine.LINES_OF_ANALYSIS
						);
		this.sendCommand(
				UCIEngine.SETOPTION_UCI + UCIEngine.SPACE +
				UCIEngine.NAME_UCI + UCIEngine.SPACE + UCIEngine.THREADS_UCI + UCIEngine.SPACE +
				UCIEngine.VALUE_UCI + UCIEngine.SPACE + UCIEngine.USE_THREADS
						);
		this.sendCommand(
				UCIEngine.SETOPTION_UCI + UCIEngine.SPACE +
				UCIEngine.NAME_UCI + UCIEngine.SPACE + UCIEngine.HASH_UCI + UCIEngine.SPACE +
				UCIEngine.VALUE_UCI + UCIEngine.SPACE + UCIEngine.USE_HASH_MB
						);
	}

	private String getMoveSearchOutput(final int waitTime) throws IOException
	{
		final StringBuilder builder = new StringBuilder(UCIEngine.INIT_BUFFER_SIZE);
		double currentWaitTime = (double) waitTime;
		String text = "";
		do
		{
			try
			{
				//noinspection BusyWait,NumericCastThatLosesPrecision
				Thread.sleep((long) currentWaitTime);
			}
			catch(final InterruptedException interruptedException)
			{
				final String message = interruptedException.getMessage();
				UCIEngine.LOGGER.log(Level.SEVERE, message);
				UCIEngine.LOGGER.log(Level.SEVERE, interruptedException.getMessage());
				UCIEngine.LOGGER.log(Level.SEVERE, interruptedException.toString(), interruptedException.fillInStackTrace());
			}
			currentWaitTime += (double) waitTime * UCIEngine.WAIT_MODIFIER;
			while(this.processReader.ready())
			{
				text = this.processReader.readLine();
				builder.append(text.trim());
				builder.append(UCIEngine.NEW_LINE);
			}
		}
		while(! text.startsWith(UCIEngine.BESTMOVE_UCI));
		builder.append(UCIEngine.NEW_LINE);
		return builder.toString();
	}

	@SuppressWarnings("SameParameterValue")
	private String getOutput(@SuppressWarnings("SameParameterValue") final String terminationString) throws IOException
	{
		final StringBuilder builder = new StringBuilder(1000);
		String text = "";
		while(! terminationString.equals(text))
		{
			try
			{
				// noinspection BusyWait
				Thread.sleep(UCIEngine.DEFAULT_THREAD_SLEEP);
			}
			catch(final InterruptedException interruptedException)
			{
				final String message = interruptedException.getMessage();
				UCIEngine.LOGGER.log(Level.SEVERE, message);
				UCIEngine.LOGGER.log(Level.SEVERE, interruptedException.getMessage());
				UCIEngine.LOGGER.log(Level.SEVERE, interruptedException.toString(), interruptedException.fillInStackTrace());
			}
			do
			{
				text = this.processReader.readLine().trim();
				builder.append(text);
				builder.append(UCIEngine.NEW_LINE);
			}
			while(this.processReader.ready());
		}
		builder.append(text);
		builder.append(UCIEngine.NEW_LINE);
		return builder.toString();
	}

	private void sendCommand(final String command) throws IOException
	{
		this.processWriter.write(command + UCIEngine.NEW_LINE);
		this.processWriter.flush();
	}
}